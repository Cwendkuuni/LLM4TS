import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.CheckLevel.WARNING;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Callback;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;
import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private CheckGlobalThis checker;
  private NodeTraversal traversal;

  @Before
  public void setup() {
    compiler = new AbstractCompiler();
    checker = new CheckGlobalThis(compiler, WARNING);
    traversal = new NodeTraversal(compiler.getSourceRoots(), null, null, checker, null);
  }

  @Test
  public void testTraverseFunctionAnnotatedWithConstructor() {
    Node functionNode = new Node(Token.FUNCTION);
    functionNode.setJSDocInfo(new JSDocInfo("@constructor"));
    assertFalse(checker.shouldTraverse(traversal, null, functionNode));
  }

  @Test
  public void testTraverseFunctionAnnotatedWithThis() {
    Node functionNode = new Node(Token.FUNCTION);
    functionNode.setJSDocInfo(new JSDocInfo("@this"));
    assertFalse(checker.shouldTraverse(traversal, null, functionNode));
  }

  @Test
  public void testTraverseFunctionAnnotatedWithOverride() {
    Node functionNode = new Node(Token.FUNCTION);
    functionNode.setJSDocInfo(new JSDocInfo("@override"));
    assertFalse(checker.shouldTraverse(traversal, null, functionNode));
  }

  @Test
  public void testTraverseFunctionWithNoAnnotation() {
    Node functionNode = new Node(Token.FUNCTION);
    assertTrue(checker.shouldTraverse(traversal, null, functionNode));
  }

  @Test
  public void testTraverseFunctionWithPrototypePropertyAccess() {
    Node functionNode = new Node(Token.FUNCTION);
    Node prototypeNode = new Node(Token.GETPROP);
    prototypeNode.setString("prototype");
    Node parentNode = new Node(Token.BLOCK);
    parentNode.addChildToEnd(prototypeNode);
    parentNode.addChildToEnd(functionNode);
    assertTrue(checker.shouldTraverse(traversal, parentNode, null));
  }

  @Test
  public void testTraverseFunctionWithPrototypePropertyAccessDeep() {
    Node functionNode = new Node(Token.FUNCTION);
    Node prototypeNode1 = new Node(Token.GETPROP);
    prototypeNode1.setString("prototype");
    Node prototypeNode2 = new Node(Token.GETPROP);
    prototypeNode2.setString("prototype");
    Node parentNode = new Node(Token.BLOCK);
    parentNode.addChildToEnd(prototypeNode1);
    prototypeNode1.addChildToEnd(prototypeNode2);
    prototypeNode2.addChildToEnd(functionNode);
    assertTrue(checker.shouldTraverse(traversal, parentNode, null));
  }

  @Test
  public void testTraverseNonGlobalContext() {
    Node functionNode = new Node(Token.FUNCTION);
    Node parentNode = new Node(Token.BLOCK);
    parentNode.addChildToEnd(functionNode);
    assertFalse(checker.shouldTraverse(traversal, parentNode, null));
  }

  @Test
  public void testTraverseLeftSideOfAssignment() {
    Node assignNode = new Node(Token.ASSIGN);
    Node leftSide = new Node(Token.THIS);
    assignNode.addChildToEnd(leftSide);

    Node parentNode = new Node(Token.BLOCK);
    parentNode.addChildToEnd(assignNode);
    assertTrue(checker.shouldTraverse(traversal, parentNode, null));
  }

  @Test
  public void testTraverseRightSideOfAssignment() {
    Node assignNode = new Node(Token.ASSIGN);
    Node leftSide = new Node(Token.THIS);
    Node rightSide = new Node(Token.IDENT);
    assignNode.addChildToEnd(leftSide);
    assignNode.addChildToEnd(rightSide);

    Node parentNode = new Node(Token.BLOCK);
    parentNode.addChildToEnd(assignNode);
    assertFalse(checker.shouldTraverse(traversal, parentNode, null));
  }

  @Test
  public void testVisitThisKeyword() {
    Node functionNode = new Node(Token.FUNCTION);
    Node thisKeywordNode = new Node(Token.THIS);
    functionNode.addChildToEnd(thisKeywordNode);

    Node parentNode = new Node(Token.BLOCK);
    parentNode.addChildToEnd(functionNode);

    checker.visit(traversal, null, parentNode);
    assertTrue(compiler.hasDiagnostics());
  }

  @Test
  public void testVisitThisKeywordWithPrototypePropertyAccess() {
    Node functionNode = new Node(Token.FUNCTION);
    Node thisKeywordNode = new Node(Token.THIS);
    Node prototypeNode = new Node(Token.GETPROP);
    prototypeNode.setString("prototype");
    functionNode.addChildToEnd(prototypeNode);
    prototypeNode.addChildToEnd(thisKeywordNode);

    Node parentNode = new Node(Token.BLOCK);
    parentNode.addChildToEnd(functionNode);

    checker.visit(traversal, null, parentNode);
    assertTrue(compiler.hasDiagnostics());
  }
}