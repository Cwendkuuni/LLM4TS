import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.traverse;
import static com.google.javascript.rhino.NodeFactory.createName;
import static com.google.javascript.rhino.Token.*;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.JsMessage;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.NodeOperations;

import org.junit.Before;
import org.junit.Test;

public class VarCheckTest {

  private AbstractCompiler compiler;
  private VarCheck varCheck;
  private CompilerInput input;
  private Node root;

  @Before
  public void setUp() {
    compiler = new TestCompiler();
    varCheck = new VarCheck(compiler);
    input = NodeFactory.createInput("test.js");
    root = NodeFactory.createProgram();
  }

  @Test
  public void testBasic() {
    varCheck.process(input, root);
    assertFalse(varCheck.varsToDeclareInExterns.isEmpty());
  }

  @Test
  public void testUndefinedVarError() {
    Node varNode = createName("undefinedVar");
    Node program = NodeOperations.appendToRoot(root, varNode);

    NodeTraversal traversal = new NodeTraversal(compiler);
    traversal.traverse(varCheck, program, null);

    assertEquals(1, varCheck.varsToDeclareInExterns.size());
    assertEquals("undefinedVar", varCheck.varsToDeclareInExterns.iterator().next());

    DiagnosticType diagnostic = varCheck.getLastError();
    assertEquals(DiagnosticType.error, diagnostic.getSeverity());
    assertEquals("JSC_UNDEFINED_VARIABLE", diagnostic.getType().getName());
    assertEquals("variable undefinedVar is undefined", diagnostic.getMessage());
  }

  @Test
  public void testViolatedModuleDepError() {
    JSModule moduleA = new JSModule("moduleA");
    JSModule moduleB = new JSModule("moduleB");
    compiler.getModuleGraph().addModule(moduleA);
    compiler.getModuleGraph().addModule(moduleB);
    moduleA.addDependency(moduleB);

    Node moduleANode = NodeFactory.createModule(moduleA.getName());
    Node moduleBNode = NodeFactory.createModule(moduleB.getName());
    Node program = NodeOperations.appendToRoot(root, moduleANode, moduleBNode);

    Node varNodeA = createName("varA");
    Node varNodeB = createName("varB");
    moduleANode.addChildToBack(varNodeA);
    moduleBNode.addChildToBack(varNodeB);

    NodeTraversal traversal = new NodeTraversal(compiler);
    traversal.traverse(varCheck, program, null);

    DiagnosticType diagnostic = varCheck.getLastError();
    assertEquals(DiagnosticType.error, diagnostic.getSeverity());
    assertEquals("JSC_VIOLATED_MODULE_DEPENDENCY", diagnostic.getType().getName());
    assertEquals("module moduleA cannot reference moduleB, defined in moduleB, since moduleB loads after moduleA", diagnostic.getMessage());
  }

  @Test
  public void testMissingModuleDepWarning() {
    JSModule moduleA = new JSModule("moduleA");
    JSModule moduleB = new JSModule("moduleB");
    compiler.getModuleGraph().addModule(moduleA);
    compiler.getModuleGraph().addModule(moduleB);

    Node moduleANode = NodeFactory.createModule(moduleA.getName());
    Node moduleBNode = NodeFactory.createModule(moduleB.getName());
    Node program = NodeOperations.appendToRoot(root, moduleANode, moduleBNode);

    Node varNodeA = createName("varA");
    moduleANode.addChildToBack(varNodeA);
    Node varNodeB = createName("varB");
    moduleBNode.addChildToBack(varNodeB);

    Node functionNode = NodeFactory.createFunction(
        createName("function"), NodeFactory.createList(varNodeB), null);
    moduleBNode.addChildToBack(functionNode);

    NodeTraversal traversal = new NodeTraversal(compiler);
    traversal.traverse(varCheck, program, null);

    DiagnosticType diagnostic = varCheck.getLastError();
    assertNull(diagnostic);

    diagnostic = varCheck.getLastWarning();
    assertEquals(DiagnosticType.warning, diagnostic.getSeverity());
    assertEquals("JSC_MISSING_MODULE_DEPENDENCY", diagnostic.getType().getName());
    assertEquals("missing module dependency; module moduleA should depend on moduleB because it references varB", diagnostic.getMessage());
  }

  @Test
  public void testStrictModuleDepError() {
    JSModule moduleA = new JSModule("moduleA");
    JSModule moduleB = new JSModule("moduleB");
    compiler.getModuleGraph().addModule(moduleA);
    compiler.getModuleGraph().addModule(moduleB);
    moduleB.addDependency(moduleA);

    Node moduleANode = NodeFactory.createModule(moduleA.getName());
    Node moduleBNode = NodeFactory.createModule(moduleB.getName());
    Node program = NodeOperations.appendToRoot(root, moduleANode, moduleBNode);

    Node varNodeA = createName("varA");
    moduleANode.addChildToBack(varNodeA);
    Node varNodeB = createName("varB");
    moduleBNode.addChildToBack(varNodeB);

    Node functionNode = NodeFactory.createFunction(
        createName("function"), NodeFactory.createList(varNodeB), null);
    moduleBNode.addChildToBack(functionNode);

    varCheck = new VarCheck(compiler, true);

    NodeTraversal traversal = new NodeTraversal(compiler);
    traversal.traverse(varCheck, program, null);

    DiagnosticType diagnostic = varCheck.getLastError();
    assertNull(diagnostic);

    diagnostic = varCheck.getLastWarning();
    assertEquals(DiagnosticType.disabled, diagnostic.getSeverity());
    assertEquals("JSC_STRICT_MODULE_DEPENDENCY", diagnostic.getType().getName());
    assertEquals("module moduleA cannot reference moduleB, defined in moduleB", diagnostic.getMessage());
  }

  @Test
  public void testNameReferenceInExternsWarning() {
    Node program = NodeOperations.appendToRoot(root, NodeFactory.createModule("module"));
    Node varNode = createName("var");
    program.addChildToBack(varNode);

    NodeTraversal traversal = new NodeTraversal(compiler);
    traversal.traverse(new NameRefInExternsCheck(), program, null);

    DiagnosticType diagnostic = traversal.getLastWarning();
    assertEquals(DiagnosticType.warning, diagnostic.getSeverity());
    assertEquals("JSC_NAME_REFERENCE_IN_EXTERNS", diagnostic.getType().getName());
    assertEquals("accessing name var in externs has no effect", diagnostic.getMessage());
  }

  @Test
  public void testUndefinedExternVarWarning() {
    Node program = NodeOperations.appendToRoot(root, NodeFactory.createModule("module"));
    Node varNode = createName("undefinedVar");
    program.addChildToBack(varNode);

    VarCheck check = new VarCheck(compiler);
    check.process(input, root);

    DiagnosticType diagnostic = check.getLastWarning();
    assertEquals(DiagnosticType.warning, diagnostic.getSeverity());
    assertEquals("JSC_UNDEFINED_EXTERN_VAR_ERROR", diagnostic.getType().getName());
    assertEquals("name undefinedVar is not undefined in the externs.", diagnostic.getMessage());
  }

  @Test
  public void testInvalidFunctionDeclError() {
    Node functionNode = NodeFactory.createFunctionDeclaration(
        createName("func"), NodeFactory.createList(), null);
    Node program = NodeOperations.appendToRoot(root, functionNode);

    VarCheck check = new VarCheck(compiler);
    check.process(input, root);

    DiagnosticType diagnostic = check.getLastError();
    assertEquals(DiagnosticType.error, diagnostic.getSeverity());
    assertEquals("JSC_INVALID_FUNCTION_DECL", diagnostic.getType().getName());
    assertEquals("Syntax error: function declaration must have a name", diagnostic.getMessage());
  }
}

class TestCompiler extends AbstractCompiler {
  @Override
  public CompilerPass getPass(String passName) {
    if (VarCheck.class.getName().equals(passName)) {
      return new VarCheck(this, false);
    }
    return null;
  }
}

class NameRefInExternsCheck extends AbstractPostOrderCallback {
  @Override
  public void visit(NodeTraversal t, Node n, Node parent) {
    if (n.getType() == NAME) {
      switch (parent.getType()) {
        case VAR:
        case FUNCTION:
          break;
        case LP:
          if (n == parent.getFirstChild()) {
            return;
          }
          break;
        case GETPROP:
          if (n == parent.getFirstChild()) {
            Scope scope = t.getScope();
            Scope.Var var = scope.getVar(n.getString());
            if (var == null) {
              // TODO: Add test for the case where var is declared but not defined.
              fail("Variable " + n.getString() + " is not defined.");
            }
          }
          break;
        default:
          fail("Unsupported parent type: " + parent);
      }
      assertTrue("Expected a variable or function declaration, but found: " + n,
          NodeUtil.isFunctionDeclaration(parent) || NodeUtil.isFunctionExpression(parent));
    }
  }
}