import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Token;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.TokenTypes;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.MockitoAnnotations;

public class VarCheckTest {

  private AbstractCompiler compiler;
  private VarCheck varCheck;
  private NodeFactory nodeFactory;
  private CompilerInput input;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);

    compiler = mock(AbstractCompiler.class);
    nodeFactory = mock(NodeFactory.class);
    when(compiler.newCompilerPass()).thenReturn(new Object() {});
    when(compiler.newExternInput(ArgumentMatchers.anyString())).then(
        invocation -> {
          CompilerInput input = new CompilerInput();
          input.setAstRoot(nodeFactory.createRoot());
          return input;
        });

    varCheck = new VarCheck(compiler);
  }

  @Test
  public void testNoUndefinedVar() {
    input = new CompilerInput();
    Node root = nodeFactory.createRoot();
    Node varNode = nodeFactory.createName("foo");
    Node assignNode = nodeFactory.createAssign(TokenTypes.ASSIGN, varNode,
        nodeFactory.createNumber(42));
    root.addChildToBack(varNode);
    root.addChildToBack(assignNode);

    varCheck.process(input, root);

    Scope scope = varCheck.getScope();
    Scope.Var var = scope.getVar("foo");
    assertNotNull(var);
  }

  @Test
  public void testUndefinedVar() {
    input = new CompilerInput();
    Node root = nodeFactory.createRoot();
    Node varNode = nodeFactory.createName("foo");
    root.addChildToBack(varNode);

    varCheck.process(input, root);

    Scope scope = varCheck.getScope();
    Scope.Var var = scope.getVar("foo");
    assertNull(var);
  }

  @Test
  public void testUndefinedVarInFunction() {
    input = new CompilerInput();
    Node root = nodeFactory.createRoot();
    Node functionNode = nodeFactory.createFunction(TokenTypes.FUNCTION, null, null, null, null, null);
    Node varNode = nodeFactory.createName("foo");
    Node body = nodeFactory.createBlock(null);
    body.addChildToBack(varNode);
    functionNode.setBody(body);
    root.addChildToBack(functionNode);

    varCheck.process(input, root);

    Scope functionScope = varCheck.getScope();
    Scope.Var var = functionScope.getVar("foo");
    assertNull(var);
  }

  @Test
  public void testDuplicateVar() {
    input = new CompilerInput();
    Node root = nodeFactory.createRoot();
    Node varNode1 = nodeFactory.createName("foo");
    Node varNode2 = nodeFactory.createName("foo");
    root.addChildToBack(varNode1);
    root.addChildToBack(varNode2);

    varCheck.process(input, root);

    // The second varNode should have been marked as a duplicate.
    Scope scope = varCheck.getScope();
    Scope.Var var1 = scope.getVar("foo");
    assertNotNull(var1);
    assertFalse(var1.getSuppression().isEmpty());
  }

  @Test
  public void testModuleDependency() {
    CompilerInput input1 = new CompilerInput();
    CompilerInput input2 = new CompilerInput();

    Node root1 = nodeFactory.createRoot();
    Node varNode1 = nodeFactory.createName("foo");
    root1.addChildToBack(varNode1);

    Node root2 = nodeFactory.createRoot();
    Node varNode2 = nodeFactory.createName("foo");
    root2.addChildToBack(varNode2);

    when(compiler.getModuleGraph()).thenReturn(new ModuleGraph() {
      @Override
      public boolean dependsOn(JSModule module1, JSModule module2) {
        if (module1 == input1.getModule() && module2 == input2.getModule()) {
          return true;
        }
        return false;
      }
    });

    varCheck = new VarCheck(compiler);
    varCheck.process(input1, root1);
    varCheck.process(input2, root2);

    // The variable reference should have been reported as an error.
    Scope scope1 = varCheck.getScope();
    Scope.Var var1 = scope1.getVar("foo");
    assertNull(var1);
    CompilerPass pass = varCheck;
    NodeTraversal traversal = NodeTraversal.newEmptyTraversal();
    traversal.traverse(compiler, root1, pass);
    JSError error = traversal.getLastError();
    assertNotNull(error);
    assertEquals("JSC_VIOLATED_MODULE_DEPENDENCY", error.getType().getName());
  }

  @Test
  public void testMissingModuleDependency() {
    CompilerInput input1 = new CompilerInput();
    CompilerInput input2 = new CompilerInput();

    Node root1 = nodeFactory.createRoot();
    Node varNode1 = nodeFactory.createName("foo");
    root1.addChildToBack(varNode1);

    Node root2 = nodeFactory.createRoot();
    Node varNode2 = nodeFactory.createName("foo");
    root2.addChildToBack(varNode2);

    when(compiler.getModuleGraph()).thenReturn(new ModuleGraph() {
      @Override
      public boolean dependsOn(JSModule module1, JSModule module2) {
        return false;
      }
    });

    varCheck = new VarCheck(compiler);
    varCheck.process(input1, root1);
    varCheck.process(input2, root2);

    // The variable reference should have been reported as a warning.
    Scope scope1 = varCheck.getScope();
    Scope.Var var1 = scope1.getVar("foo");
    assertNull(var1);
    CompilerPass pass = varCheck;
    NodeTraversal traversal = NodeTraversal.newEmptyTraversal();
    traversal.traverse(compiler, root1, pass);
    JSError error = traversal.getLastError();
    assertNotNull(error);
    assertEquals("JSC_MISSING_MODULE_DEPENDENCY", error.getType().getName());
  }

  @Test
  public void testStrictModuleDependency() {
    CompilerInput input1 = new CompilerInput();
    CompilerInput input2 = new CompilerInput();

    Node root1 = nodeFactory.createRoot();
    Node varNode1 = nodeFactory.createName("foo");
    root1.addChildToBack(varNode1);

    Node root2 = nodeFactory.createRoot();
    Node varNode2 = nodeFactory.createName("foo");
    root2.addChildToBack(varNode2);

    when(compiler.getModuleGraph()).thenReturn(new ModuleGraph() {
      @Override
      public boolean dependsOn(JSModule module1, JSModule module2) {
        return false;
      }
    });

    varCheck = new VarCheck(compiler, true);
    varCheck.process(input1, root1);
    varCheck.process(input2, root2);

    // The variable reference should not have been reported as an error.
    Scope scope1 = varCheck.getScope();
    Scope.Var var1 = scope1.getVar("foo");
    assertNull(var1);
  }

  @Test
  public void testNameReferenceInExterns() {
    CompilerInput input = new CompilerInput();
    Node root = nodeFactory.createRoot();
    Node varNode = nodeFactory.createName("foo");
    root.addChildToBack(varNode);

    varCheck = new VarCheck(compiler);
    varCheck.process(input, root);

    // The name reference should have been reported as a warning.
    CompilerPass pass = varCheck;
    NodeTraversal traversal = NodeTraversal.newEmptyTraversal();
    traversal.traverse(compiler, root, pass);
    JSError error = traversal.getLastError();
    assertNotNull(error);
    assertEquals("JSC_NAME_REFERENCE_IN_EXTERNS", error.getType().getName());
  }

  @Test
  public void testUndefinedExternVar() {
    CompilerInput input = new CompilerInput();
    Node root = nodeFactory.createRoot();
    Node varNode = nodeFactory.createName("foo");
    root.addChildToBack(varNode);

    varCheck.process(input, root);

    // The variable should not have been reported as an error.
    Scope scope = varCheck.getScope();
    Scope.Var var = scope.getVar("foo");
    assertNull(var);
  }

  @Test
  public void testInvalidFunctionDecl() {
    input = new CompilerInput();
    Node root = nodeFactory.createRoot();
    Node functionNode = nodeFactory.createFunction(TokenTypes.FUNCTION, null, null, null, null, null);
    root.addChildToBack(functionNode);

    varCheck.process(input, root);

    // The function declaration should have been reported as an error.
    CompilerPass pass = varCheck;
    NodeTraversal traversal = NodeTraversal.newEmptyTraversal();
    traversal.traverse(compiler, root, pass);
    JSError error = traversal.getLastError();
    assertNotNull(error);
    assertEquals("JSC_INVALID_FUNCTION_DECL", error.getType().getName());
  }
}