import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverseFunctionWithoutConstructorOrThisAnnotation() {
    NodeTraversal traversal = new NodeTraversal();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);
    traversal.traverse(new Node(FUNCTION, null), checkGlobalThis, null);
    assertTrue(checkGlobalThis.shouldTraverse(traversal, null, null));
  }

  @Test
  public void testShouldNotTraverseFunctionWithConstructorAnnotation() {
    NodeTraversal traversal = new NodeTraversal();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);
    Node functionNode = new Node(FUNCTION, null);
    JSDocInfo constructorAnnotation = new JSDocInfo("@constructor");
    functionNode.setJSDocInfo(constructorAnnotation);
    Node constructorCall = new Node(CALL, null);
    constructorCall.addChildToEnd(functionNode);
    Node parent = new Node(ASSIGN, null);
    parent.addChildToEnd(constructorCall);
    traversal.traverse(parent, checkGlobalThis, null);
    assertFalse(checkGlobalThis.shouldTraverse(traversal, null, null));
  }

  @Test
  public void testShouldNotTraverseFunctionWithThisAnnotation() {
    NodeTraversal traversal = new NodeTraversal();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);
    Node functionNode = new Node(FUNCTION, null);
    JSDocInfo thisAnnotation = new JSDocInfo("@this");
    functionNode.setJSDocInfo(thisAnnotation);
    Node constructorCall = new Node(CALL, null);
    constructorCall.addChildToEnd(functionNode);
    Node parent = new Node(ASSIGN, null);
    parent.addChildToEnd(constructorCall);
    traversal.traverse(parent, checkGlobalThis, null);
    assertFalse(checkGlobalThis.shouldTraverse(traversal, null, null));
  }

  @Test
  public void testShouldTraverseLeftSideOfAssignment() {
    NodeTraversal traversal = new NodeTraversal();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);
    Node assignNode = new Node(ASSIGN, null);
    Node leftSide = new Node(THIS, null);
    assignNode.addChildToEnd(leftSide);
    Node parent = new Node(ASSIGN, null);
    parent.addChildToEnd(assignNode);
    traversal.traverse(parent, checkGlobalThis, null);
    assertTrue(checkGlobalThis.shouldReportThis(assignNode, parent));
  }

  @Test
  public void testShouldNotTraverseRightSideOfAssignmentToPrototypeProperty() {
    NodeTraversal traversal = new NodeTraversal();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);
    Node assignNode = new Node(ASSIGN, null);
    Node leftSide = new Node(ASSIGN, null);
    Node thisNode = new Node(THIS, null);
    Node getPropNode = new Node(GETPROP, null);
    getPropNode.addChildToEnd(new Node(IDENT, "prototype"));
    Node rhs = new Node(BINOP, null, thisNode, getPropNode);
    assignNode.addChildToEnd(leftSide);
    assignNode.addChildToEnd(rhs);
    Node parent = new Node(ASSIGN, null);
    parent.addChildToEnd(assignNode);
    traversal.traverse(parent, checkGlobalThis, null);
    assertFalse(checkGlobalThis.shouldReportThis(rhs, parent));
  }

  @Test
  public void testShouldNotTraverseRightSideOfAssignmentToPrototypeSubproperty() {
    NodeTraversal traversal = new NodeTraversal();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);
    Node assignNode = new Node(ASSIGN, null);
    Node leftSide = new Node(ASSIGN, null);
    Node thisNode = new Node(THIS, null);
    Node getPropNode1 = new Node(GETPROP, null);
    getPropNode1.addChildToEnd(new Node(IDENT, "prototype"));
    Node getPropNode2 = new Node(GETPROP, null);
    getPropNode2.addChildToEnd(new Node(IDENT, "subProperty"));
    Node rhs = new Node(BINOP, null, thisNode, getPropNode2);
    assignNode.addChildToEnd(leftSide);
    assignNode.addChildToEnd(rhs);
    Node parent = new Node(ASSIGN, null);
    parent.addChildToEnd(assignNode);
    traversal.traverse(parent, checkGlobalThis, null);
    assertFalse(checkGlobalThis.shouldReportThis(rhs, parent));
  }

  @Test
  public void testShouldReportThisOnLeftSideOfAssignment() {
    NodeTraversal traversal = new NodeTraversal();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);
    Node assignNode = new Node(ASSIGN, null);
    Node leftSide = new Node(THIS, null);
    assignNode.addChildToEnd(leftSide);
    Node parent = new Node(ASSIGN, null);
    parent.addChildToEnd(assignNode);
    traversal.traverse(parent, checkGlobalThis, null);
    assertTrue(checkGlobalThis.compiler.hasDiagnostics());
  }

  @Test
  public void testShouldNotReportThisOnRightSideOfAssignment() {
    NodeTraversal traversal = new NodeTraversal();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);
    Node assignNode = new Node(ASSIGN, null);
    Node leftSide = new Node(ASSIGN, null);
    Node thisNode = new Node(THIS, null);
    Node rhs = new Node(THIS, null);
    assignNode.addChildToEnd(leftSide);
    assignNode.addChildToEnd(rhs);
    Node parent = new Node(ASSIGN, null);
    parent.addChildToEnd(assignNode);
    traversal.traverse(parent, checkGlobalThis, null);
    assertFalse(checkGlobalThis.compiler.hasDiagnostics());
  }

  @Test
  public void testGetFunctionJsDocInfo() {
    Node functionNode = new Node(FUNCTION, null);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);
    JSDocInfo jsDocInfo = checkGlobalThis.getFunctionJsDocInfo(functionNode);
    assertNull(jsDocInfo);

    functionNode.setJSDocInfo(new JSDocInfo("@constructor"));
    jsDocInfo = checkGlobalThis.getFunctionJsDocInfo(functionNode);
    assertTrue(jsDocInfo.isConstructor());

    functionNode.setJSDocInfo(new JSDocInfo("@this"));
    jsDocInfo = checkGlobalThis.getFunctionJsDocInfo(functionNode);
    assertTrue(jsDocInfo.hasThisType());

    functionNode.setJSDocInfo(new JSDocInfo());
    Node parentNode = new Node(ASSIGN, null);
    parentNode.addChildToEnd(functionNode);
    jsDocInfo = checkGlobalThis.getFunctionJsDocInfo(parentNode);
    assertNull(jsDocInfo);

    parentNode.setJSDocInfo(new JSDocInfo("@this"));
    jsDocInfo = checkGlobalThis.getFunctionJsDocInfo(parentNode);
    assertTrue(jsDocInfo.hasThisType());

    parentNode.setJSDocInfo(new JSDocInfo("@constructor"));
    jsDocInfo = checkGlobalThis.getFunctionJsDocInfo(parentNode);
    assertTrue(jsDocInfo.isConstructor());
  }

  @Test
  public void testShouldTraverseFunctionAnnotatedWithBothConstructorAndThis() {
    NodeTraversal traversal = new NodeTraversal();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);
    Node functionNode = new Node(FUNCTION, null);
    JSDocInfo constructorAnnotation = new JSDocInfo("@constructor");
    functionNode.setJSDocInfo(constructorAnnotation);
    JSDocInfo thisAnnotation = new JSDocInfo("@this");
    functionNode.addJSDocInfo(thisAnnotation);
    Node constructorCall = new Node(CALL, null);
    constructorCall.addChildToEnd(functionNode);
    Node parent = new Node(ASSIGN, null);
    parent.addChildToEnd(constructorCall);
    traversal.traverse(parent, checkGlobalThis, null);
    assertTrue(checkGlobalThis.shouldTraverse(traversal, null, null));
  }

  @Test
  public void testShouldNotTraverseFunctionWithNoLogicalPlaceForThisAnnotation() {
    NodeTraversal traversal = new NodeTraversal();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);
    Node functionNode = new Node(FUNCTION, null);
    Node parent = new Node(ASSIGN, null);
    parent.addChildToEnd(functionNode);
    traversal.traverse(parent, checkGlobalThis, null);
    assertFalse(checkGlobalThis.shouldTraverse(traversal, null, null));
  }
}