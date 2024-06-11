package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Callback;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticPosition;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;

public class CheckGlobalThisTest {

  private static final DiagnosticType GLOBAL_THIS = DiagnosticType.warning(
      "JSC_USED_GLOBAL_THIS",
      "dangerous use of the global 'this' object");

  @Test
  public void testShouldTraverse() {
    CheckGlobalThis checker = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);

    Node rootNode = Node.newBlock(Token.SCRIPT, null);

    Node functionNode1 = Node.newFunction(Token.FUNCTION, null, null, null);
    Node functionNode2 = Node.newFunction(Token.FUNCTION, null, null, null);
    Node functionNode3 = Node.newFunction(Token.FUNCTION, null, null, null);
    Node functionNode4 = Node.newFunction(Token.FUNCTION, null, null, null);

    Node assignNode1 = Node.newAssign(Token.ASSIGN, null, null, null);
    Node assignNode2 = Node.newAssign(Token.ASSIGN, null, null, null);
    Node assignNode3 = Node.newAssign(Token.ASSIGN, null, null, null);

    Node getNode = Node.newGetProp(Token.GETPROP, null, null, null);

    // Function nodes should not be traversed
    Node functionParent1 = Node.newBlock(Token.BLOCK, null);
    Node functionParent2 = Node.newName(Token.NAME, "func");
    Node functionParent3 = Node.newName(Token.NAME, "obj");
    Node functionParent4 = Node.newName(Token.NAME, "obj");
    Node functionParent5 = Node.newBlock(Token.BLOCK, null);

    when(functionNode1.getFirstChild()).thenReturn(getFunctionJsDocInfo(functionNode1));
    when(functionNode2.getFirstChild()).thenReturn(getFunctionJsDocInfo(functionNode2));

    // Function annotated with @constructor
    when(functionNode3.getFirstChild()).thenReturn(getNodeFor("function constructor() {}"));

    // Function with @this annotation
    when(functionNode4.getFirstChild()).thenReturn(getNodeFor("@this function foo() {}"));

    // Function where there's no logical place to put a this annotation
    Node functionNode5 = Node.newFunction(Token.FUNCTION, null, null, null);

    Node assignParent1 = Node.newName(Token.NAME, "a");
    Node assignParent2 = Node.newName(Token.NAME, "obj");

    Node leftChild = Node.newName(Token.NAME, "this");
    Node rightChild = Node.newName(Token.NAME, "b");

    Node leftChildPropertyAccess = Node.newGetProp(Token.GETPROP, leftChild, null, null);
    Node propertyAccess = Node.newGetProp(Token.GETPROP, Node.newName(Token.NAME, "prototype"), null, null);

    Node traversal = new NodeTraversal(rootNode, null);
    Callback callback = checker;

    // Function nodes should not be traversed
    assertThat(callback.shouldTraverse(traversal, functionNode1, functionParent1)).isFalse();
    assertThat(callback.shouldTraverse(traversal, functionNode2, functionParent2)).isFalse();
    assertThat(callback.shouldTraverse(traversal, functionNode3, functionParent3)).isFalse();
    assertThat(callback.shouldTraverse(traversal, functionNode4, functionParent4)).isFalse();
    assertThat(callback.shouldTraverse(traversal, functionNode5, functionParent5)).isTrue();

    // Assignments
    assertThat(callback.shouldTraverse(traversal, assignNode1, assignParent1)).isTrue();
    assertThat(callback.shouldTraverse(traversal, assignNode2, assignParent2)).isTrue();

    // Assignments with left side as a property access
    assertThat(callback.shouldTravere(traversal, assignNode3, Node.newGetProp(Token.GETPROP, assignParent2, null, null))).isTrue();

    // Assignments with left side as a get property with prototype property
    assertThat(callback.shouldTraverse(traversal, assignNode3, Node.newGetProp(Token.GETPROP, Node.newGetProp(Token.GETPROP, assignParent2, "prototype"), null, null))).isFalse();

    // Left side of assignment is this keyword
    Node leftSide = Node.newName(Token.NAME, "this");
    Node assignmentNode = Node.newAssign(Token.ASSIGN, leftSide, null, null);

    // Should report an error for a left side assignment
    Node leftSideParent = Node.newBlock(Token.BLOCK, null);
    Node rightSide = Node.newName(Token.NAME, "b");
    Node rightSideParent = Node.newBlock(Token.BLOCK, null);

    Node assignmentParent = Node.newName(Token.NAME, "a");
    Node assignment = Node.newAssign(Token.ASSIGN, leftSide, rightSide);

    callback.visit(traversal, assignment, assignmentParent);
    Node error = traversal.errors.get(0);
    assertThat(error.getDiagnosticType()).isEqualTo(GLOBAL_THIS);
    assertThat(error.getPosition().getLineNumber()).isEqualTo(1);
    assertThat(error.getPosition().getColumnNumber()).isEqualTo(1);
  }

  private JSDocInfo getFunctionJsDocInfo(Node node) {
    return null;
  }

  private Node getNodeFor(String nodeType) {
    if (nodeType.startsWith("function")) {
      Node functionNode = Node.newFunction(Token.FUNCTION, null, null, null);
      Node jsDocInfo = Node.newObjectLiteral(Token.OBJECT, null);
      Node constructorNode = Node.newName(Token.NAME, "constructor");
      Node constructorValue = Node.newFunction(Token.FUNCTION, null, null, null);
      Node constructorJsDoc = Node.newObjectLiteral(Token.OBJECT, null);
      Node constructorJsDocType = Node.newName(Token.NAME, "type");
      Node constructorJsDocValue = Node.newString(Token.STRING, "constructor");
      Node constructorJsDocArray = Node.newArrayLiteral(Token.ARRAY, constructorJsDoc);
      Node constructorJsDocAnnotation = Node.newName(Token.NAME, "@constructor");

      Node jsDocInfoProperty = Node.newName(Token.NAME, "jsdocInfo");
      Node jsDocInfoValue = Node.newObjectLiteral(Token.OBJECT, constructorJsDocArray);

      jsDocInfo.setFirstChild(constructorJsDoc);
      constructorJsDoc.setFirstChild(constructorJsDocType);
      constructorJsDocType.setFirstChild(constructorJsDocValue);
      constructorJsDocArray.setFirstChild(constructorJsDoc);
      jsDocInfo.setFirstChild(jsDocInfoProperty);
      jsDocInfoProperty.setFirstChild(jsDocInfoValue);

      functionNode.setFirstChild(jsDocInfo);
      return functionNode;
    } else if (nodeType.startsWith("@this")) {
      Node jsDocAnnotation = Node.newName(Token.NAME, "@this");
      Node functionNode = Node.newFunction(Token.FUNCTION, null, null, null);
      Node jsDocInfo = Node.newObjectLiteral(Token.OBJECT, Node.newArrayLiteral(Token.ARRAY, jsDocAnnotation));

      functionNode.setFirstChild(jsDocInfo);
      return functionNode;
    } else {
      return Node.newName(Token.NAME, nodeType);
    }
  }
}