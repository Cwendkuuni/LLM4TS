import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.rhino.NodeType.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;

@RunWith(JUnit4.class)
public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverseFunctions() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);

    Node functionNode = Node.newFunctionNode(
        "functionEvil() {}",
        ImmutableList.of(),
        Node.newTextNode("functionEvil"));

    NodeTraversal traversal = NodeTraversal.createEmpty();
    Node parentNode = Node.newTextNode("test");

    assertFalse(checkGlobalThis.shouldTraverse(traversal, functionNode, parentNode));
  }

  @Test
  public void testShouldTraverseFunctionsWithConstructorsAnnotation() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);

    Node functionNode = Node.newFunctionNode(
        "function EvilConstructor() { this.x = 0; }",
        ImmutableList.of(),
        Node.newTextNode("function EvilConstructor"));

    NodeTraversal traversal = NodeTraversal.createEmpty();
    Node parentNode = Node.newBlockNode(ImmutableList.of());

    assertTrue(checkGlobalThis.shouldTraverse(traversal, functionNode, parentNode));
  }

  @Test
  public void testShouldTraverseFunctionsWithThisAnnotation() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);

    Node functionNode = Node.newFunctionNode(
        "function @this MyFunction() {}",
        ImmutableList.of(),
        Node.newTextNode("function MyFunction"));

    NodeTraversal traversal = NodeTraversal.createEmpty();
    Node parentNode = Node.newBlockNode(ImmutableList.of());

    assertTrue(checkGlobalThis.shouldTraverse(traversal, functionNode, parentNode));
  }

  @Test
  public void testShouldTraverseFunctionsInValidContext() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);

    Node functionNode = Node.newFunctionNode(
        "function evil() {}",
        ImmutableList.of(),
        Node.newTextNode("function evil"));

    Node blockNode = Node.newBlockNode(ImmutableList.of());
    Node scriptNode = Node.newScriptNode("");

    assertFalse(checkGlobalThis.shouldTraverse(
        null, functionNode, scriptNode)); // not in a script or block

    assertFalse(checkGlobalThis.shouldTraverse(
        null, functionNode, blockNode)); // not in a block

    Node nameNode = Node.newNameNode("a");
    Node assignNode = Node.newAssignNode(nameNode, functionNode);
    Node textNode = Node.newTextNode("test");

    assertFalse(checkGlobalThis.shouldTraverse(
        null, functionNode, textNode)); // not a script, block or assign

    Node scriptNodeWithBlock = Node.newScriptNode("");
    scriptNodeWithBlock.addChildToEnd(blockNode);
    scriptNodeWithBlock.addChildToEnd(functionNode);

    assertTrue(checkGlobalThis.shouldTraverse(
        null, functionNode, scriptNodeWithBlock)); // in a script with a block
  }

  @Test
  public void testShouldTraverseAssignmentsWithThisOnLeftSide() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);

    Node assignNode = Node.newAssignNode(
        Node.newNameNode("a"),
        Node.newThisNode());

    Node blockNode = Node.newBlockNode(ImmutableList.of());

    assertTrue(checkGlobalThis.shouldTraverse(
        null, assignNode, blockNode));
  }

  @Test
  public void testShouldTraverseAssignmentsWithThisOnRightSide() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);

    Node assignNode = Node.newAssignNode(
        Node.newNameNode("a"),
        Node.newThisNode());

    Node assignParentNode = Node.newAssignNode(
        Node.newNameNode("b"),
        assignNode);

    Node blockNode = Node.newBlockNode(ImmutableList.of());

    assertFalse(checkGlobalThis.shouldTraverse(
        null, assignNode, blockNode)); // not on the left side

    assertTrue(checkGlobalThis.shouldTraverse(
        null, assignParentNode, blockNode)); // on the right side of an assign
  }

  @Test
  public void testShouldTraverseGetProperties() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);

    Node getPropertyNode = Node.newGetPropNode(
        Node.newNameNode("a.prototype"),
        Node.newTextNode("x"));

    Node blockNode = Node.newBlockNode(ImmutableList.of());

    assertTrue(checkGlobalThis.shouldTraverse(
        null, getPropertyNode, blockNode));
  }

  @Test
  public void testShouldTraverseGetPropertiesWithPrototypeInName() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);

    Node getPropertyNode = Node.newGetPropNode(
        Node.newNameNode("a.prototype.x"),
        Node.newTextNode(""));

    Node blockNode = Node.newBlockNode(ImmutableList.of());

    assertTrue(checkGlobalThis.shouldTraverse(
        null, getPropertyNode, blockNode));
  }

  @Test
  public void testShouldNotTraverseGetPropertiesWithPrototypeInValue() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);

    Node getPropertyNode = Node.newGetPropNode(
        Node.newNameNode("a"),
        Node.newThisNode());

    Node blockNode = Node.newBlockNode(ImmutableList.of());

    assertFalse(checkGlobalThis.shouldTraverse(
        null, getPropertyNode, blockNode));
  }

  @Test
  public void testShouldNotTraverseAssignmentsToPrototypeProperties() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);

    Node assignNode = Node.newAssignNode(
        Node.newGetPropNode(
            Node.newNameNode("a.prototype.x"),
            Node.newTextNode("")),
        Node.newTextNode(""));

    Node blockNode = Node.newBlockNode(ImmutableList.of());

    assertFalse(checkGlobalThis.shouldTraverse(
        null, assignNode, blockNode));
  }

  @Test
  public void testShouldTraverseFunctionsAnnotatedWithJSDocInfo() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);

    Node functionNode = Node.newFunctionNode(
        "function MyFunction() {}",
        ImmutableList.of(),
        Node.newTextNode("function MyFunction"));

    Node jsDocInfoNode = Node.newNode(
        "/**\n * @constructor\n */");

    functionNode.setJSDocInfo(new JSDocInfo(null, null, jsDocInfoNode, null));

    NodeTraversal traversal = NodeTraversal.createEmpty();
    Node parentNode = Node.newBlockNode(ImmutableList.of());

    assertTrue(checkGlobalThis.shouldTraverse(traversal, functionNode, parentNode));
  }

  @Test
  public void testShouldTraverseAssignmentsWithThisOnLeftSideAndParentFunctionWithThisAnnotation() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);

    Node functionNodeParent = Node.newFunctionNode(
        "function @this ParentFunction() {}",
        ImmutableList.of(),
        Node.newTextNode("function ParentFunction"));

    Node functionNodeChild = Node.newFunctionNode(
        "function ChildFunction() {}",
        ImmutableList.of(),
        Node.newTextNode("function ChildFunction"));

    Node assignNode = Node.newAssignNode(
        Node.newNameNode("a"),
        Node.newThisNode());

    Node assignParentNode = Node.newAssignNode(
        Node.newNameNode("b"),
        functionNodeChild);

    Node blockNode = Node.newBlockNode(ImmutableList.of());

    Node traversalParent = NodeTraversal.createEmpty();
    Node traversalChild = NodeTraversal.createEmpty();

    functionNodeParent.accept(traversalParent, null, null);
    functionNodeChild.accept(traversalChild, null, null);

    checkGlobalThis = new CheckGlobalThis(
        new AbstractCompiler() {}, CheckLevel.WARNING);

    assertTrue(checkGlobalThis.shouldTraverse(
        traversalParent, assignNode, blockNode));
  }

  @Test
  public void testShouldNotTraverseAssignmentsWithThisOnLeftSideAndParentFunctionWithThisAnnotationButDifferentScope() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);

    Node functionNodeParent = Node.newFunctionNode(
        "function @this ParentFunction() {}",
        ImmutableList.of(),
        Node.newTextNode("function ParentFunction"));

    Node blockNodeParent = Node.newBlockNode(ImmutableList.of());

    Node functionNodeChild = Node.newFunctionNode(
        "function ChildFunction() {}",
        ImmutableList.of(),
        Node.newTextNode("function ChildFunction"));

    Node assignNode = Node.newAssignNode(
        Node.newNameNode("a"),
        Node.newThisNode());

    Node assignParentNode = Node.newAssignNode(
        Node.newNameNode("b"),
        functionNodeChild);

    Node blockNodeChild = Node.newBlockNode(ImmutableList.of());

    Node traversalParent = NodeTraversal.createEmpty();
    Node traversalChild = NodeTraversal.createEmpty();

    Node parentScope = Node.newBlockNode(ImmutableList.of());
    parentScope.addChildToEnd(functionNodeParent);
    parentScope.addChildToEnd(blockNodeParent);

    Node childScope = Node.newBlockNode(ImmutableList.of());
    childScope.addChildToEnd(functionNodeChild);
    childScope.addChildToEnd(assignParentNode);
    childScope.addChildToEnd(blockNodeChild);
    childScope.addChildToEnd(assignNode);

    functionNodeParent.accept(traversalParent, null, null);
    functionNodeChild.accept(traversalChild, null, null);

    checkGlobalThis = new CheckGlobalThis(
        new AbstractCompiler() {}, CheckLevel.WARNING);

    assertFalse(checkGlobalThis.shouldTraverse(
        traversalParent, assignNode, blockNodeChild));
  }

  @Test
  public void testShouldNotTraverseAssignmentsWithThisOnLeftSideAndParentFunctionWithThisAnnotationButSameScope() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);

    Node functionNodeParent = Node.newFunctionNode(
        "function @this ParentFunction() {}",
        ImmutableList.of(),
        Node.newTextNode("function ParentFunction"));

    Node blockNodeParent = Node.newBlockNode(ImmutableList.of());

    Node functionNodeChild = Node.newFunctionNode(
        "function ChildFunction() {}",
        ImmutableList.of(),
        Node.newTextNode("function ChildFunction"));

    Node assignNode = Node.newAssignNode(
        Node.newNameNode("a"),
        Node.newThisNode());

    Node assignParentNode = Node.newAssignNode(
        Node.newNameNode("b"),
        functionNodeChild);

    Node blockNodeChild = Node.newBlockNode(ImmutableList.of());

    Node parentScope = Node.newBlockNode(ImmutableList.of());
    parentScope.addChildToEnd(functionNodeParent);
    parentScope.addChildToEnd(blockNodeParent);

    Node childScope = Node.newBlockNode(ImmutableList.of());
    childScope.addChildToEnd(functionNodeChild);
    childScope.addChildToEnd(assignParentNode);
    childScope.addChildToEnd(blockNodeChild);
    childScope.addChildToEnd(assignNode);

    functionNodeParent.accept(NodeTraversal.createEmpty(), null, null);
    functionNodeChild.accept(NodeTraversal.createEmpty(), null, null);

    checkGlobalThis = new CheckGlobalThis(
        new AbstractCompiler() {}, CheckLevel.WARNING);

    assertFalse(checkGlobalThis.shouldTraverse(
        NodeTraversal.createEmpty(), assignNode, blockNodeChild));
  }

  @Test
  public void testShouldReportThisOnLeftSideOfAssignments() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);

    Node assignNode = Node.newAssignNode(
        Node.newNameNode("a"),
        Node.newThisNode());

    Node blockNode = Node.newBlockNode(ImmutableList.of());

    NodeTraversal traversal = NodeTraversal.createEmpty();

    checkGlobalThis.visit(traversal, assignNode, blockNode);

    assertTrue(checkGlobalThis.compiler.hasDiagnostics());
  }

  @Test
  public void testShouldNotReportThisOnRightSideOfAssignments() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);

    Node assignNode = Node.newAssignNode(
        Node.newNameNode("a"),
        Node.newThisNode());

    Node assignParentNode = Node.newAssignNode(
        Node.newNameNode("b"),
        assignNode);

    Node blockNode = Node.newBlockNode(ImmutableList.of());

    NodeTraversal traversal = NodeTraversal.createEmpty();

    checkGlobalThis.visit(traversal, assignNode, blockNode);

    assertFalse(checkGlobalThis.compiler.hasDiagnostics());
  }

  @Test
  public void testShouldReportThisOnPropertyAccess() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);

    Node getPropertyNode = Node.newGetPropNode(
        Node.newNameNode("this.x"),
        Node.newTextNode(""));

    Node blockNode = Node.newBlockNode(ImmutableList.of());

    NodeTraversal traversal = NodeTraversal.createEmpty();

    checkGlobalThis.visit(traversal, getPropertyNode, blockNode);

    assertTrue(checkGlobalThis.compiler.hasDiagnostics());
  }

  @Test
  public void testShouldNotReportThisOnPropertyAccessWithPrototype() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);

    Node getPropertyNode = Node.newGetPropNode(
        Node.newNameNode("this.prototype.x"),
        Node.newTextNode(""));

    Node blockNode = Node.newBlockNode(ImmutableList.of());

    NodeTraversal traversal = NodeTraversal.createEmpty();

    checkGlobalThis.visit(traversal, getPropertyNode, blockNode);

    assertFalse(checkGlobalThis.compiler.hasDiagnostics());
  }
}