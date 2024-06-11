import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Callback;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private CheckGlobalThis checkGlobalThis;

  @org.junit.Before
  public void setUp() {
    this.compiler = new AbstractCompiler();
    this.checkGlobalThis = new CheckGlobalThis(compiler, WARNING);
  }

  @org.junit.Test
  public void testShouldTraverseFunction() {
    Node functionNode = new Node(new Token(Token.FUNCTION, 0, 0));
    NodeTraversal traversal = new NodeTraversal(functionNode, checkGlobalThis, null);
    assertTrue("Function should be traversed", checkGlobalThis.shouldTraverse(traversal, null, null));
  }

  @org.junit.Test
  public void testShouldNotTraverseConstructor() {
    Node constructorNode = new Node(new Token(Token.FUNCTION, 0, 0));
    constructorNode.setJSDocInfo(new JSDocInfo(null, new String[]{"@constructor"}));
    NodeTraversal traversal = new NodeTraversal(constructorNode, checkGlobalThis, null);
    assertFalse("Constructor should not be traversed", checkGlobalThis.shouldTraverse(traversal, null, null));
  }

  @org.junit.Test
  public void testShouldNotTraverseFunctionWithThisAnnotation() {
    Node functionNode = new Node(new Token(Token.FUNCTION, 0, 0));
    functionNode.setJSDocInfo(new JSDocInfo(null, new String[]{"@this"}));
    NodeTraversal traversal = new NodeTraversal(functionNode, checkGlobalThis, null);
    assertFalse("Function with @this annotation should not be traversed", checkGlobalThis.shouldTraverse(traversal, null, null));
  }

  @org.junit.Test
  public void testShouldNotTraverseFunctionWithOverrideAnnotation() {
    Node functionNode = new Node(new Token(Token.FUNCTION, 0, 0));
    functionNode.setJSDocInfo(new JSDocInfo(null, new String[]{"@override"}));
    NodeTraversal traversal = new NodeTraversal(functionNode, checkGlobalThis, null);
    assertFalse("Function with @override annotation should not be traversed", checkGlobalThis.shouldTraverse(traversal, null, null));
  }

  @org.junit.Test
  public void testShouldTraverseFunctionWithNoAnnotation() {
    Node functionNode = new Node(new Token(Token.FUNCTION, 0, 0));
    NodeTraversal traversal = new NodeTraversal(functionNode, checkGlobalThis, null);
    assertTrue("Function with no annotation should be traversed", checkGlobalThis.shouldTraverse(traversal, null, null));
  }

  @org.junit.Test
  public void testShouldNotTraverseFunctionOutsideBlockOrScriptOrNameOrAssign() {
    Node functionNode = new Node(new Token(Token.FUNCTION, 0, 0));
    Node parentNode = new Node(new Token(Token.VAR, 0, 0));
    NodeTraversal traversal = new NodeTraversal(functionNode, checkGlobalThis, parentNode);
    assertFalse("Function outside valid context should not be traversed", checkGlobalThis.shouldTraverse(traversal, functionNode, parentNode));
  }

  @org.junit.Test
  public void testShouldTraverseLeftSideOfAssignment() {
    Node assignNode = new Node(new Token(Token.ASSIGN, 0, 0));
    Node lhsNode = new Node(new Token(Token.NAME, 0, 0));
    Node rhsNode = new Node(new Token(Token.NUMBER, 0, 0));
    Node parentNode = new Node(assignNode);
    NodeTraversal traversal = new NodeTraversal(lhsNode, checkGlobalThis, parentNode);
    assertTrue("Left side of assignment should be traversed", checkGlobalThis.shouldTraverse(traversal, lhsNode, parentNode));
  }

  @org.junit.Test
  public void testShouldNotTraverseLeftSideOfAssignmentToPrototype() {
    Node assignNode = new Node(new Token(Token.ASSIGN, 0, 0));
    Node lhsNode = new Node(new Token(Token.GETPROP, 0, 0));
    lhsNode.getLastChild().setString("prototype");
    Node rhsNode = new Node(new Token(Token.NUMBER, 0, 0));
    Node parentNode = new Node(assignNode);
    NodeTraversal traversal = new NodeTraversal(lhsNode, checkGlobalThis, parentNode);
    assertFalse("Left side of assignment to prototype should not be traversed", checkGlobalThis.shouldTraverse(traversal, lhsNode, parentNode));
  }

  @org.junit.Test
  public void testShouldNotTraverseLeftSideOfAssignmentToPrototypeWithGetProperty() {
    Node assignNode = new Node(new Token(Token.ASSIGN, 0, 0));
    Node lhsNode = new Node(new Token(Token.GETPROP, 0, 0));
    lhsNode.setFirstChild(new Node(new Token(Token.NAME, 0, 0)));
    lhsNode.getLastChild().setString("prototype");
    Node rhsNode = new Node(new Token(Token.NUMBER, 0, 0));
    Node parentNode = new Node(assignNode);
    NodeTraversal traversal = new NodeTraversal(lhsNode, checkGlobalThis, parentNode);
    assertFalse("Left side of assignment to prototype with get property should not be traversed", checkGlobalThis.shouldTraverse(traversal, lhsNode, parentNode));
  }

  @org.junit.Test
  public void testShouldTraverseRightSideOfAssignment() {
    Node assignNode = new Node(new Token(Token.ASSIGN, 0, 0));
    Node lhsNode = new Node(new Token(Token.NAME, 0, 0));
    Node rhsNode = new Node(new Token(Token.THIS, 0, 0));
    Node parentNode = new Node(assignNode);
    NodeTraversal traversal = new NodeTraversal(rhsNode, checkGlobalThis, parentNode);
    assertTrue("Right side of assignment should be traversed", checkGlobalThis.shouldTraverse(traversal, rhsNode, parentNode));
  }

  @org.junit.Test
  public void testVisitThisKeyword() {
    Node functionNode = new Node(new Token(Token.FUNCTION, 0, 0));
    Node thisKeywordNode = new Node(new Token(Token.THIS, 0, 0));
    Node parentNode = new Node(functionNode);
    NodeTraversal traversal = new NodeTraversal(thisKeywordNode, checkGlobalThis, parentNode);
    checkGlobalThis.visit(traversal, thisKeywordNode, parentNode);
    assertTrue("This keyword should be reported", compiler.hasDiagnostics());
  }

  @org.junit.Test
  public void testVisitThisKeywordInsideAssignment() {
    Node assignNode = new Node(new Token(Token.ASSIGN, 0, 0));
    Node lhsNode = new Node(new Token(Token.NAME, 0, 0));
    Node thisKeywordNode = new Node(new Token(Token.THIS, 0, 0));
    Node rhsNode = new Node(new Token(Token.NUMBER, 0, 0));
    Node parentNode = new Node(assignNode);
    NodeTraversal traversal = new NodeTraversal(thisKeywordNode, checkGlobalThis, parentNode);
    checkGlobalThis.visit(traversal, thisKeywordNode, parentNode);
    assertTrue("This keyword inside assignment should be reported", compiler.hasDiagnostics());
  }

  @org.junit.Test
  public void testVisitThisKeywordInsidePropertyAccess() {
    Node getNode = new Node(new Token(Token.GETPROP, 0, 0));
    Node thisKeywordNode = new Node(new Token(Token.THIS, 0, 0));
    Node parentNode = new Node(getNode);
    NodeTraversal traversal = new NodeTraversal(thisKeywordNode, checkGlobalThis, parentNode);
    checkGlobalThis.visit(traversal, thisKeywordNode, parentNode);
    assertTrue("This keyword inside property access should be reported", compiler.hasDiagnostics());
  }

  @org.junit.Test
  public void testVisitThisKeywordInsidePrototypePropertyAccess() {
    Node getNode = new Node(new Token(Token.GETPROP, 0, 0));
    Node lhsNode = new Node(new Token(Token.NAME, 0, 0));
    Node thisKeywordNode = new Node(new Token(Token.THIS, 0, 0));
    Node prototypeNode = new Node(new Token(Token.NAME, 0, 0));
    Node prototypePropertyNode = new Node(new Token(Token.GETPROP, 0, 0));
    prototypePropertyNode.getLastChild().setString("prototype");
    Node rhsNode = new Node(new Token(Token.NUMBER, 0, 0));
    Node parentNode = new Node(getNode);
    NodeTraversal traversal = new NodeTraversal(prototypePropertyNode, checkGlobalThis, parentNode);
    checkGlobalThis.visit(traversal, thisKeywordNode, parentNode);
    assertFalse("This keyword inside prototype property access should not be reported", compiler.hasDiagnostics());
  }

  @org.junit.Test
  public void testVisitThisKeywordInsidePrototypePropertyAccessWithGetProperty() {
    Node getNode = new Node(new Token(Token.GETPROP, 0, 0));
    Node lhsNode = new Node(new Token(Token.NAME, 0, 0));
    Node thisKeywordNode = new Node(new Token(Token.THIS, 0, 0));
    Node prototypeNode = new Node(new Token(Token.NAME, 0, 0));
    Node prototypePropertyNode = new Node(new Token(Token.GETPROP, 0, 0));
    prototypePropertyNode.setFirstChild(lhsNode);
    prototypePropertyNode.getLastChild().setString("prototype");
    Node rhsNode = new Node(new Token(Token.NUMBER, 0, 0));
    Node parentNode = new Node(getNode);
    NodeTraversal traversal = new NodeTraversal(prototypePropertyNode, checkGlobalThis, parentNode);
    checkGlobalThis.visit(traversal, thisKeywordNode, parentNode);
    assertFalse("This keyword inside prototype property access with get property should not be reported", compiler.hasDiagnostics());
  }
}