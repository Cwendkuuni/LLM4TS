import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;

public class VarCheckTest {

  private AbstractCompiler compiler;
  private CompilerPass varCheck;
  private CompilerInput input;
  private Node root;
  private Scope scope;

  @Before
  public void setup() {
    compiler = new TestCompiler();
    varCheck = new VarCheck(compiler);
    input = new CompilerInput("TestFile.js", null);
    root = NodeFactory.createProgram();
    scope = new Scope(root, input, null, null);
  }

  @Test
  public void testUndefinedVariable() {
    Node variableNode = NodeFactory.createName("undefinedVar");
    NodeTraversal traversal = new NodeTraversal(compiler, root, null, null);
    traversal.traverse(variableNode);
    assertEquals(1, compiler.getNumDiagnostics());
    DiagnosticType diagnostic = compiler.getDiagnostics().get(0);
    assertTrue(diagnostic instanceof DiagnosticType.Error);
    assertEquals(DiagnosticType.ERROR_CODE_UNDEFINED_VARIABLE,
        diagnostic.getCode());
  }

  @Test
  public void testNonExistentVariable() {
    Node variableNode = NodeFactory.createName("nonExistentVar");
    Node parentNode = NodeFactory.createVar("testVar");
    Node parent = NodeFactory.createBlock(parentNode);
    root.addChildToBack(parent);
    parent.addChildToBack(variableNode);
    Scope scope = new Scope(root, input, null, null);

    NodeTraversal traversal = new NodeTraversal(compiler, root, null, null);
    traversal.traverse(parentNode);
    traversal.traverse(variableNode);

    assertEquals(1, compiler.getNumDiagnostics());
    DiagnosticType diagnostic = compiler.getDiagnostics().get(0);
    assertTrue(diagnostic instanceof DiagnosticType.Error);
    assertEquals(DiagnosticType.ERROR_CODE_UNDEFINED_VARIABLE,
        diagnostic.getCode());
  }

  @Test
  public void testUndefinedVariableWithFunctionDeclaration() {
    Node functionDeclaration = NodeFactory.createFunctionDeclaration("testFunction", LIST());
    Node variableNode = NodeFactory.createName("undefinedVar");
    Node parentNode = functionDeclaration;
    Node parent = NodeFactory.createBlock(parentNode);
    root.addChildToBack(parent);
    parent.addChildToBack(functionDeclaration);
    parent.addChildToBack(variableNode);

    NodeTraversal traversal = new NodeTraversal(compiler, root, null, null);
    traversal.traverse(functionDeclaration);
    traversal.traverse(variableNode);

    assertEquals(1, compiler.getNumDiagnostics());
    DiagnosticType diagnostic = compiler.getDiagnostics().get(0);
    assertTrue(diagnostic instanceof DiagnosticType.Error);
    assertEquals(DiagnosticType.ERROR_CODE_UNDEFINED_VARIABLE,
        diagnostic.getCode());
  }

  @Test
  public void testFunctionDeclarationWithoutName() {
    Node functionDeclaration = NodeFactory.createFunctionDeclaration(NULL, LIST());
    Node variableNode = NodeFactory.createName("undefinedVar");
    Node parentNode = functionDeclaration;
    Node parent = NodeFactory.createBlock(parentNode);
    root.addChildToBack(parent);
    parent.addChildToBack(functionDeclaration);
    parent.addChildToBack(variableNode);

    NodeTraversal traversal = new NodeTraversal(compiler, root, null, null);
    traversal.traverse(functionDeclaration);
    traversal.traverse(variableNode);

    assertEquals(1, compiler.getNumDiagnostics());
    DiagnosticType diagnostic = compiler.getDiagnostics().get(0);
    assertTrue(diagnostic instanceof DiagnosticType.Error);
    assertEquals(DiagnosticType.ERROR_CODE_INVALID_FUNCTION_DECL,
        diagnostic.getCode());
  }

  @Test
  public void testDuplicateVariableDeclaration() {
    Node variableNode1 = NodeFactory.createName("duplicateVar");
    Node variableNode2 = NodeFactory.createName("duplicateVar");
    Node parentNode = NodeFactory.createBlock(NULL);
    Node parent = NodeFactory.createBlock(parentNode);
    root.addChildToBack(parent);
    parent.addChildToBack(parentNode);
    parent.addChildToBack(variableNode1);
    parent.addChildToBack(variableNode2);

    NodeTraversal traversal = new NodeTraversal(compiler, root, null, null);
    traversal.traverse(parentNode);
    traversal.traverse(root);

    assertEquals(1, compiler.getNumDiagnostics());
    DiagnosticType diagnostic = compiler.getDiagnostics().get(0);
    assertTrue(diagnostic instanceof DiagnosticType.Warning);
  }

  @Test
  public void testVariableAccessInAnotherFile() {
    Node variableNode = NodeFactory.createName("variableInAnotherFile");
    Node parentNode = NodeFactory.createBlock(NULL);
    Node parent = NodeFactory.createBlock(parentNode);
    Node anotherFileRoot = NodeFactory.createProgram();
    anotherFileRoot.addChildToBack(parentNode);
    Node anotherFileInput = new CompilerInput("AnotherFile.js", anotherFileRoot);
    CompilerPass anotherFileVarCheck = new VarCheck(compiler);
    anotherFileVarCheck.process(NULL, anotherFileRoot);
    root.addChildToBack(parent);
    parent.addChildToBack(variableNode);

    CompilerInput input = new CompilerInput("TestFile.js", root);
    CompilerPass varCheck = new VarCheck(compiler);
    varCheck.process(input, root);

    NodeTraversal traversal = new NodeTraversal(compiler, root, null, null);
    traversal.traverse(variableNode);

    assertEquals(1, compiler.getNumDiagnostics());
    DiagnosticType diagnostic = compiler.getDiagnostics().get(0);
    assertTrue(diagnostic instanceof DiagnosticType.Error);
    assertEquals(DiagnosticType.ERROR_CODE_VIOLATED_MODULE_DEPENDENCY,
        diagnostic.getCode());
  }

  @Test
  public void testVariableAccessInExterns() {
    Node variableNode = NodeFactory.createName("variableInExterns");
    Node parentNode = NodeFactory.createBlock(NULL);
    Node parent = NodeFactory.createBlock(parentNode);
    Node externsRoot = NodeFactory.createProgram();
    Node externsInput = new CompilerInput("Externs.js", externsRoot);
    Node externsVariableNode = NodeFactory.createName("variableInExterns");
    externsRoot.addChildToBack(parentNode);
    parent.addChildToBack(variableNode);
    externsRoot.addChildToBack(externsVariableNode);

    root.addChildToBack(parent);
    parent.addChildToBack(variableNode);

    CompilerPass varCheck = new VarCheck(compiler);
    varCheck.process(input, root);

    NodeTraversal traversal = new NodeTraversal(compiler, root, null, null);
    traversal.traverse(variableNode);

    assertEquals(1, compiler.getNumDiagnostics());
    DiagnosticType diagnostic = compiler.getDiagnostics().get(0);
    assertTrue(diagnostic instanceof DiagnosticType.Warning);
  }

  @Test
  public void testDuplicateVariableDeclarationInExterns() {
    Node variableNode1 = NodeFactory.createName("duplicateVarInExterns");
    Node variableNode2 = NodeFactory.createName("duplicateVarInExterns");
    Node parentNode = NodeFactory.createBlock(NULL);
    Node parent = NodeFactory.createBlock(parentNode);
    Node externsRoot = NodeFactory.createProgram();
    Node externsInput = new CompilerInput("Externs.js", externsRoot);
    externsRoot.addChildToBack(parentNode);
    parent.addChildToBack(variableNode1);
    parent.addChildToBack(variableNode2);

    root.addChildToBack(parent);
    parent.addChildToBack(variableNode1);
    parent.addChildToBack(variableNode2);

    CompilerPass varCheck = new VarCheck(compiler);
    varCheck.process(input, root);

    assertEquals(1, compiler.getNumDiagnostics());
    DiagnosticType diagnostic = compiler.getDiagnostics().get(0);
    assertTrue(diagnostic instanceof DiagnosticType.Warning);
  }

  @Test
  public void testDuplicateVariableDeclarationBetweenExternsAndCode() {
    Node variableNode1 = NodeFactory.createName("duplicateVar");
    Node variableNode2 = NodeFactory.createName("duplicateVar");
    Node parentNode = NodeFactory.createBlock(NULL);
    Node parent = NodeFactory.createBlock(parentNode);
    Node externsRoot = NodeFactory.createProgram();
    Node externsInput = new CompilerInput("Externs.js", externsRoot);
    externsRoot.addChildToBack(parentNode);
    parent.addChildToBack(variableNode1);
    Node codeRoot = NodeFactory.createProgram();
    Node codeInput = new CompilerInput("TestFile.js", codeRoot);
    Node codeParent = NodeFactory.createBlock(NULL);
    codeRoot.addChildToBack(codeParent);
    codeParent.addChildToBack(parentNode);
    parent.addChildToBack(variableNode2);

    root.addChildToBack(codeRoot);

    CompilerPass varCheck = new VarCheck(compiler);
    varCheck.process(input, root);

    assertEquals(1, compiler.getNumDiagnostics());
    DiagnosticType diagnostic = compiler.getDiagnostics().get(0);
    assertTrue(diagnostic instanceof DiagnosticType.Warning);
  }
}