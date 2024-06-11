import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Token;

public class VarCheckTest {

  private AbstractCompiler compiler;
  private CompilerInput input;
  private Node root;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler() {};
    input = new CompilerInput("test.js", "test.js", null);
    root = new Node(Token.PROGRAM, null);
  }

  @Test
  public void testValidVariableDeclaration() {
    VarCheck varCheck = new VarCheck(compiler);
    NodeTraversal traversal = new NodeTraversal(compiler, root);
    NodeTraversal.traverse(compiler, root, varCheck);
    assertEquals(0, varCheck.varsToDeclareInExterns.size());
  }

  @Test
  public void testUndefinedVariable() {
    root.addChildToBack(new Node(Token.VAR, Node.newString(Token.NAME, "x")));
    VarCheck varCheck = new VarCheck(compiler);
    NodeTraversal traversal = new NodeTraversal(compiler, root);
    varCheck.process(null, root);
    assertEquals(1, varCheck.varsToDeclareInExterns.size());
    assertEquals("x", varCheck.varsToDeclareInExterns.iterator().next());
  }

  @Test
  public void testDuplicateVariableDeclaration() {
    root.addChildToBack(new Node(Token.VAR, Node.newString(Token.NAME, "x")));
    root.addChildToBack(new Node(Token.VAR, Node.newString(Token.NAME, "x")));
    VarCheck varCheck = new VarCheck(compiler);
    NodeTraversal traversal = new NodeTraversal(compiler, root);
    varCheck.process(null, root);
    assertEquals(1, varCheck.varsToDeclareInExterns.size());
    assertEquals("x", varCheck.varsToDeclareInExterns.iterator().next());
  }

  @Test
  public void testUndefinedVariableInFunction() {
    Node functionNode = new Node(Token.FUNCTION, null);
    root.addChildToBack(functionNode);
    functionNode.addChildToBack(new Node(Token.VAR, Node.newString(Token.NAME, "x")));
    functionNode.addChildToBack(new Node(Token.RETURN, Node.newName(Token.NAME, "x")));
    VarCheck varCheck = new VarCheck(compiler);
    NodeTraversal traversal = new NodeTraversal(compiler, root);
    varCheck.process(null, root);
    assertEquals(1, varCheck.varsToDeclareInExterns.size());
    assertEquals("x", varCheck.varsToDeclareInExterns.iterator().next());
  }

  @Test
  public void testFunctionDeclarationWithoutName() {
    Node functionNode = new Node(Token.FUNCTION, null);
    root.addChildToBack(functionNode);
    VarCheck varCheck = new VarCheck(compiler);
    NodeTraversal traversal = new NodeTraversal(compiler, root);
    traversal.traverse(functionNode);
    assertTrue(varCheck.varsToDeclareInExterns.isEmpty());
    assertEquals(1, traversal.diagnostics.size());
    assertEquals("JSC_INVALID_FUNCTION_DECL", traversal.diagnostics.get(0).getType().getName());
  }

  @Test
  public void testModuleDependency() {
    CompilerInput moduleAInput = new CompilerInput("moduleA.js", "moduleA.js", null);
    CompilerInput moduleBInput = new CompilerInput("moduleB.js", "moduleB.js", null);
    input.getModuleGraph().addDependency(moduleAInput.getModule(), moduleBInput.getModule());
    root.addChildToBack(new Node(Token.IMPORT, Node.newString(Token.STRING_LITERAL, "moduleB.js")));
    root.addChildToBack(new Node(Token.VAR, Node.newString(Token.NAME, "x")));
    VarCheck varCheck = new VarCheck(compiler);
    NodeTraversal traversal = new NodeTraversal(compiler, root);
    varCheck.process(null, root);
    assertTrue(varCheck.varsToDeclareInExterns.isEmpty());
    assertEquals(1, traversal.diagnostics.size());
    assertEquals("JSC_VIOLATED_MODULE_DEPENDENCY", traversal.diagnostics.get(0).getType().getName());
  }

  @Test
  public void testMissingModuleDependency() {
    CompilerInput moduleAInput = new CompilerInput("moduleA.js", "moduleA.js", null);
    CompilerInput moduleBInput = new CompilerInput("moduleB.js", "moduleB.js", null);
    input.getModuleGraph().addDependency(moduleBInput.getModule(), moduleAInput.getModule());
    root.addChildToBack(new Node(Token.IMPORT, Node.newString(Token.STRING_LITERAL, "moduleA.js")));
    root.addChildToBack(new Node(Token.VAR, Node.newString(Token.NAME, "x")));
    VarCheck varCheck = new VarCheck(compiler);
    NodeTraversal traversal = new NodeTraversal(compiler, root);
    varCheck.process(null, root);
    assertEquals(1, varCheck.varsToDeclareInExterns.size());
    assertEquals("x", varCheck.varsToDeclareInExterns.iterator().next());
  }

  @Test
  public void testStrictModuleDependency() {
    CompilerInput moduleAInput = new CompilerInput("moduleA.js", "moduleA.js", null);
    CompilerInput moduleBInput = new CompilerInput("moduleB.js", "moduleB.js", null);
    input.getModuleGraph().addDependency(moduleAInput.getModule(), moduleBInput.getModule());
    root.addChildToBack(new Node(Token.IMPORT, Node.newString(Token.STRING_LITERAL, "moduleB.js")));
    root.addChildToBack(new Node(Token.VAR, Node.newString(Token.NAME, "x")));
    VarCheck varCheck = new VarCheck(compiler, true);
    NodeTraversal traversal = new NodeTraversal(compiler, root);
    varCheck.process(null, root);
    assertTrue(varCheck.varsToDeclareInExterns.isEmpty());
    assertEquals(1, traversal.diagnostics.size());
    assertEquals("JSC_STRICT_MODULE_DEPENDENCY", traversal.diagnostics.get(0).getType().getName());
  }

  @Test
  public void testNameReferenceInExterns() {
    CompilerInput input = new CompilerInput("test.js", "test.js", null);
    Node root = new Node(Token.PROGRAM, null);
    root.addChildToBack(new Node(Token.IMPORT, Node.newString(Token.STRING_LITERAL, "test.js")));
    root.addChildToBack(new Node(Token.VAR, Node.newString(Token.NAME, "x")));
    VarCheck varCheck = new VarCheck(compiler);
    NodeTraversal traversal = new NodeTraversal(compiler, root);
    varCheck.process(input, root);
    assertTrue(varCheck.varsToDeclareInExterns.isEmpty());
    assertEquals(1, traversal.diagnostics.size());
    assertEquals("JSC_NAME_REFERENCE_IN_EXTERNS", traversal.diagnostics.get(0).getType().getName());
  }

  @Test
  public void testUndefinedExternVariable() {
    CompilerInput input = new CompilerInput("test.js", "test.js", null);
    Node root = new Node(Token.PROGRAM, null);
    root.addChildToBack(new Node(Token.IMPORT, Node.newString(Token.STRING_LITERAL, "test.js")));
    root.addChildToBack(new Node(Token.VAR, Node.newString(Token.NAME, "x")));
    VarCheck varCheck = new VarCheck(compiler);
    NodeTraversal traversal = new NodeTraversal(compiler, root);
    traversal.setCurrentInput(input);
    varCheck.process(input, root);
    assertTrue(varCheck.varsToDeclareInExterns.isEmpty());
    assertEquals(1, traversal.diagnostics.size());
    assertEquals("JSC_UNDEFINED_EXTERN_VAR_ERROR", traversal.diagnostics.get(0).getType().getName());
  }
}