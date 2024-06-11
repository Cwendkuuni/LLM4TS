import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Callback;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;

import org.junit.Test;

/**
 * Test class for CheckGlobalThis class.
 */
public class CheckGlobalThisTest {

  @Test
  public void testCheckGlobalThis() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, WARNING);

    // Test normal cases.
    Node node1 = Node.newFunctionNode(Token.FUNCTION, new int[]{}, new int[]{Token.RETURN, Token.NAME, Token.STRING, Token.SEMICOLON});
    Node node2 = Node.newExpressionStatementNode(Token.ASSIGN, Node.newThisNode(), Node.newNameNode("useful", false));
    Node node3 = Node.newExpressionStatementNode(Token.ASSIGN, Node.newNameNode("a", false), Node.newFunctionNode(Token.FUNCTION, new int[]{}, new int[]{Token.RETURN, Token.NAME, Token.STRING, Token.SEMICOLON}));
    Node node4 = Node.newExpressionStatementNode(Token.ASSIGN, Node.newNameNode("prototype", false), Node.newObjectLiteralNode());

    Node node5 = Node.newExpressionStatementNode(Token.ASSIGN, Node.newNameNode("a", false), Node.newFunctionNode(Token.FUNCTION, new int[]{Token.ARGV0}, new int[]{Token.RETURN, Token.NAME, Token.STRING, Token.SEMICOLON}));
    Node node6 = Node.newExpressionStatementNode(Token.ASSIGN, Node.newNameNode("a", false), Node.newObjectLiteralNode().setProperty(Node.newNameNode("prototype", false), Node.newFunctionNode(Token.FUNCTION, new int[]{Token.ARGV0}, new int[]{Token.RETURN, Token.NAME, Token.STRING, Token.SEMICOLON})));
    Node node7 = Node.newExpressionStatementNode(Token.ASSIGN, Node.newNameNode("a", false), Node.newFunctionNode(Token.FUNCTION, new int[]{Token.ARGV0}, new int[]{Token.RETURN, Token.NAME, Token.STRING, Token.SEMICOLON}));
    Node node8 = Node.newExpressionStatementNode(Token.ASSIGN, Node.newNameNode("a", false), Node.newNameNode("prototype", false).setProperty(Node.newNameNode("x", false), Node.newUndefinedNode()));

    Node node9 = Node.newExpressionStatementNode(Token.ASSIGN, Node.newNameNode("constructor", false), Node.newFunctionNode(Token.FUNCTION, new int[]{Token.ARGV0, Token.ARGV1}, new int[]{Token.RETURN, Token.NAME, Token.STRING, Token.SEMICOLON, Token.NAME, Token.STRING}));
    Node node10 = Node.newExpressionStatementNode(Token.ASSIGN, Node.newNameNode("constructor", false), Node.newFunctionNode(Token.FUNCTION, new int[]{Token.ARGV0}, new int[]{Token.RETURN, Token.NAME, Token.STRING, Token.SEMICOLON, Token.NAME, Token.STRING, Token.NAME, Token.STRING}));

    Node node11 = Node.newExpressionStatementNode(Token.ASSIGN, Node.newNameNode("a", false), Node.newFunctionNode(Token.FUNCTION, new int[]{}, new int[]{Token.NAME, Token.STRING}));
    Node node12 = Node.newExpressionStatementNode(Token.ASSIGN, Node.newNameNode("a", false), Node.newFunctionNode(Token.FUNCTION, new int[]{Token.ARGV0}, new int[]{Token.NAME, Token.STRING}));

    CheckLevel level = WARNING;
    AbstractCompiler compiler = new AbstractCompiler("test", new NodeTraversal(checkGlobalThis, level));

    NodeTraversal t = compiler.getTraversal();
    Node parent = null;

    t.traverse(node1, parent);
    t.traverse(node2, parent);
    t.traverse(node3, parent);
    t.traverse(node4, parent);
    t.traverse(node5, parent);
    t.traverse(node6, parent);
    t.traverse(node7, parent);
    t.traverse(node8, parent);
    t.traverse(node9, parent);
    t.traverse(node10, parent);
    t.traverse(node11, parent);
    t.traverse(node12, parent);

    assertTrue("No warnings expected for node1", compiler.getDiagnostics().isEmpty());
    assertTrue("No warnings expected for node2", compiler.getDiagnostics().isEmpty());
    assertTrue("No warnings expected for node3", compiler.getDiagnostics().isEmpty());
    assertTrue("No warnings expected for node4", compiler.getDiagnostics().isEmpty());
    assertTrue("No warnings expected for node5", compiler.getDiagnostics().isEmpty());
    assertTrue("No warnings expected for node6", compiler.getDiagnostics().isEmpty());
    assertTrue("No warnings expected for node7", compiler.getDiagnostics().isEmpty());
    assertTrue("No warnings expected for node8", compiler.getDiagnostics().isEmpty());
    assertTrue("1 warning expected for node9", compiler.getDiagnostics().size() == 1);
    assertEquals("JSC_USED_GLOBAL_THIS", compiler.getDiagnostics().get(0).getType().getExternalName());
    assertTrue("1 warning expected for node10", compiler.getDiagnostics().size() == 1);
    assertEquals("JSC_USED_GLOBAL_THIS", compiler.getDiagnostics().get(0).getType().getExternalName());
    assertTrue("0 warnings expected for node11", compiler.getDiagnostics().isEmpty());
    assertTrue("0 warnings expected for node12", compiler.getDiagnostics().isEmpty());

    // Test edge cases.
    Node node13 = Node.newExpressionStatementNode(Token.ASSIGN, Node.newThisNode(), Node.newNameNode("useful", false));
    Node node14 = Node.newExpressionStatementNode(Token.ASSIGN, Node.newNameNode("a", false), Node.newThisNode().setProperty(Node.newNameNode("x", false), Node.newUndefinedNode()));

    CheckLevel levelEdge = WARNING;
    AbstractCompiler compilerEdge = new AbstractCompiler("test", new NodeTraversal(checkGlobalThis, levelEdge));

    NodeTraversal tEdge = compilerEdge.getTraversal();
    Node parentEdge = null;

    tEdge.traverse(node13, parentEdge);
    tEdge.traverse(node14, parentEdge);

    assertTrue("1 warning expected for node13", compilerEdge.getDiagnostics().size() == 1);
    assertEquals("JSC_USED_GLOBAL_THIS", compilerEdge.getDiagnostics().get(0).getType().getExternalName());
    assertTrue("1 warning expected for node14", compilerEdge.getDiagnostics().size() == 1);
    assertEquals("JSC_USED_GLOBAL_THIS", compilerEdge.getDiagnostics().get(0).getType().getExternalName());

    // Test exceptions.
    Node nodeException = Node.newExpressionStatementNode(Token.THROW, Node.newErrorNode("Test Error"));

    CheckLevel levelException = WARNING;
    AbstractCompiler compilerException = new AbstractCompiler("test", new NodeTraversal(checkGlobalThis, levelException));

    NodeTraversal tException = compilerException.getTraversal();
    Node parentException = null;

    try {
      tException.traverse(nodeException, parentException);
      fail("Testing for an exception should have thrown an exception.");
    } catch (RuntimeException e) {
      // Expected
    }
  }
}