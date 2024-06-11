import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.Callback;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private CheckGlobalThis checker;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
  }

  @Test
  public void testShouldTraverseFunctionWithoutJsDoc() {
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node functionNode = createNode(Token.FUNCTION);
    when(compiler.getTraversal(ArgumentMatchers.any())).thenReturn(traversal);
    assertFalse(checker.shouldTraverse(traversal, functionNode, null));
  }

  @Test
  public void testShouldTraverseFunctionWithJsDocButNotConstructor() {
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node functionNode = createNode(Token.FUNCTION);
    JSDocInfo jsDocInfo = createJsDocInfo();
    when(functionNode.getJSDocInfo()).thenReturn(jsDocInfo);
    when(compiler.getTraversal(ArgumentMatchers.any())).thenReturn(traversal);
    assertFalse(checker.shouldTraverse(traversal, functionNode, null));
  }

  @Test
  public void testShouldTraverseFunctionWithJsDocAndConstructor() {
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node functionNode = createNode(Token.FUNCTION);
    JSDocInfo jsDocInfo = createJsDocInfoWithConstructor(true);
    when(functionNode.getJSDocInfo()).thenReturn(jsDocInfo);
    when(compiler.getTraversal(ArgumentMatchers.any())).thenReturn(traversal);
    assertFalse(checker.shouldTraverse(traversal, functionNode, null));
  }

  @Test
  public void testShouldTraverseFunctionWithJsDocButNoThisAnnotation() {
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node functionNode = createNode(Token.FUNCTION);
    JSDocInfo jsDocInfo = createJsDocInfoWithConstructor(false);
    when(functionNode.getJSDocInfo()).thenReturn(jsDocInfo);
    when(compiler.getTraversal(ArgumentMatchers.any())).thenReturn(traversal);
    assertTrue(checker.shouldTraverse(traversal, functionNode, null));
  }

  @Test
  public void testShouldReportThisOnLeftSideOfAssignment() {
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node assignmentNode = createNode(Token.ASSIGN);
    Node leftSideNode = createNode(Token.THIS);
    when(compiler.getTraversal(ArgumentMatchers.any())).thenReturn(traversal);
    checker.visit(traversal, leftSideNode, assignmentNode);
    assertTrue(checker.reportedError());
  }

  @Test
  public void testShouldNotReportThisOnRightSideOfAssignment() {
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node assignmentNode = createNode(Token.ASSIGN);
    Node leftSideNode = createNode(Token.GETPROP, createNode(Token.NAME, "prototype"), createNode(Token.NAME, "a"));
    Node rightSideNode = createNode(Token.THIS);
    when(compiler.getTraversal(ArgumentMatchers.any())).thenReturn(traversal);
    checker.visit(traversal, rightSideNode, assignmentNode);
    assertFalse(checker.reportedError());
  }

  private boolean CheckGlobalThisTest_reportedError() {
    return checker.reportedError();
  }

  private JSDocInfo createJsDocInfo() {
    return new JSDocInfo();
  }

  private JSDocInfo createJsDocInfoWithConstructor(boolean isConstructor) {
    JSDocInfo jsDocInfo = new JSDocInfo();
    jsDocInfo.setIsConstructor(isConstructor);
    return jsDocInfo;
  }

  private Node createNode(int type) {
    return new Node(new Token(type));
  }

  private Node createNode(int type, Node... children) {
    Node node = new Node(new Token(type));
    for (Node child : children) {
      node.addChildren(child);
    }
    return node;
  }
}