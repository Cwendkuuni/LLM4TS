import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.Scope.ROOT;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.UnreachableCodeElimination;
import com.google.javascript.rhino.NodeFactory;

import org.junit.Test;

public class UnreachableCodeEliminationTest {

  @Test
  public void testBasicFunctionality() {
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler(options);
    Node rootNode = NodeFactory.createRootNode();
    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);
    NodeTraversal traversal = new NodeTraversal(rootNode, ROOT);
    NodeTraversal.traverse(compiler, rootNode, pass);

    assertEquals("Expected no errors.", 0, compiler.getNumErrors());
  }

  @Test(expected = JSError.class)
  public void testUnreachableCodeWithReturnStatement() {
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler(options);
    Node rootNode = NodeFactory.createRootNode();
    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);

    Node ifStatement = NodeFactory.createIfStatement(Token.FALSE, null, null);
    Node returnStatement = NodeFactory.createReturnStatement();
    Node statementBlock = NodeFactory.createBlock(null);
    Node functionNode = NodeFactory.createFunction(Token.FUNCTION, "test", null, null, null, null, null, null, null, statementBlock);
    statementBlock.addChild(ifStatement);
    statementBlock.addChild(returnStatement);

    Node functionDeclaration = NodeFactory.createFunctionDeclaration(Token.FUNCTION, "test", null, null, null, null, null, null, null, functionNode);
    rootNode.addChild(functionDeclaration);

    NodeTraversal traversal = new NodeTraversal(rootNode, ROOT);
    NodeTraversal.traverse(compiler, rootNode, pass);

    fail("Expected an error.");
  }

  @Test
  public void testNoOpStatements() {
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler(options);
    Node rootNode = NodeFactory.createRootNode();
    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, false);

    Node statementBlock = NodeFactory.createBlock(null);
    Node declaration1 = NodeFactory.createVariableDeclarationStatement("x", NodeFactory.createIdentifier("x"), NodeFactory.createNullLiteral());
    statementBlock.addChild(declaration1);
    Node declaration2 = NodeFactory.createVariableDeclarationStatement("y", NodeFactory.createIdentifier("y"), NodeFactory.createNullLiteral());
    statementBlock.addChild(declaration2);
    Node functionNode = NodeFactory.createFunction(Token.FUNCTION, "test", null, null, null, null, null, null, null, statementBlock);
    rootNode.addChild(NodeFactory.createFunctionDeclaration(Token.FUNCTION, "test", null, null, null, null, null, null, null, functionNode));

    NodeTraversal traversal = new NodeTraversal(rootNode, ROOT);
    NodeTraversal.traverse(compiler, rootNode, pass);

    assertEquals("Expected no errors.", 0, compiler.getNumErrors());
  }

  @Test
  public void testEdgeCaseWithBranch() {
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler(options);
    Node rootNode = NodeFactory.createRootNode();
    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);

    Node ifStatement = NodeFactory.createIfStatement(Token.TRUE, null, null);
    Node statement1 = NodeFactory.createExpressionStatement(NodeFactory.createIdentifier("x").negate());
    Node statement2 = NodeFactory.createExpressionStatement(NodeFactory.createIdentifier("y").negate());
    Node statementBlock = NodeFactory.createBlock(null);
    statementBlock.addChild(statement1);
    statementBlock.addChild(statement2);
    Node functionNode = NodeFactory.createFunction(Token.FUNCTION, "test", null, null, null, null, null, null, null, statementBlock);

    Node functionDeclaration = NodeFactory.createFunctionDeclaration(Token.FUNCTION, "test", null, null, null, null, null, null, null, functionNode);
    rootNode.addChild(functionDeclaration);

    NodeTraversal traversal = new NodeTraversal(rootNode, ROOT);
    NodeTraversal.traverse(compiler, rootNode, pass);

    assertEquals("Expected no errors.", 0, compiler.getNumErrors());
  }

  @Test(expected = JSError.class)
  public void testExceptionHandling() {
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler(options);
    Node rootNode = NodeFactory.createRootNode();
    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);

    Node tryStatement = NodeFactory.createTryStatement(null, null, null, null);
    Node catchStatement = NodeFactory.createCatchStatement(NodeFactory.createIdentifier("e"), null, null, null);
    Node functionNode = NodeFactory.createFunction(Token.FUNCTION, "test", null, null, null, null, null, null, null, null);
    catchStatement.setParent(tryStatement);
    tryStatement.setParent(NodeFactory.createBlock(null));
    Node functionDeclaration = NodeFactory.createFunctionDeclaration(Token.FUNCTION, "test", null, null, null, null, null, null, null, tryStatement, functionNode);
    rootNode.addChild(functionDeclaration);

    NodeTraversal traversal = new NodeTraversal(rootNode, ROOT);
    NodeTraversal.traverse(compiler, rootNode, pass);

    fail("Expected an error.");
  }
}