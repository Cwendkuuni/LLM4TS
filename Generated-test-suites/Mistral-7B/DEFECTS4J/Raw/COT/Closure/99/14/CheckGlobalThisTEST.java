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

import org.junit.Test;

public class CheckGlobalThisTest {
  @Test
  public void testConstructorFunction() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, WARNING);

    Node constructorNode = Node.newFunctionNode("constructorFunction", null, false, null, null);
    JSDocInfo constructorJsDoc = Node.newJSDocInfo(true, null, null, null, null, null, null, null, null, null, null, null, null, null);
    constructorNode.setJSDocInfo(constructorJsDoc);

    NodeTraversal traversal = new NodeTraversal(new AbstractCompiler(null, null), null) {
      @Override
      public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
        return checkGlobalThis.shouldTraverse(this, n, parent);
      }
    };

    checkGlobalThis.visit(traversal, null, null);
    checkGlobalThis.visit(traversal, constructorNode, null);
    assertFalse(checkGlobalThis.level.hasErrors());
  }

  @Test
  public void testPrototypeMethod() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, WARNING);

    Node constructorNode = Node.newFunctionNode("constructorFunction", null, false, null, null);
    Node prototypeNode = Node.newNameNode("prototype");
    Node methodNode = Node.newFunctionNode("prototypeMethod", null, false, null, null);
    methodNode.setParent(prototypeNode);

    JSDocInfo constructorJsDoc = Node.newJSDocInfo(true, null, null, null, null, null, null, null, null, null, null, null, null, null);
    Node constructorJsDocNode = Node.newJSDocInfoNode(constructorJsDoc);
    constructorNode.setFirstChild(constructorJsDocNode);

    JSDocInfo methodJsDoc = Node.newJSDocInfo(true, null, null, null, null, null, null, null, null, null, null, null, null, null);
    methodNode.setJSDocInfo(methodJsDoc);

    NodeTraversal traversal = new NodeTraversal(new AbstractCompiler(null, null), null) {
      @Override
      public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
        return checkGlobalThis.shouldTraverse(this, n, parent);
      }
    };

    checkGlobalThis.visit(traversal, null, null);
    checkGlobalThis.visit(traversal, constructorNode, null);
    checkGlobalThis.visit(traversal, prototypeNode, constructorNode);
    checkGlobalThis.visit(traversal, methodNode, prototypeNode);
    assertFalse(checkGlobalThis.level.hasErrors());
  }

  @Test
  public void testFunctionWithThisAnnotation() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, WARNING);

    Node constructorNode = Node.newFunctionNode("constructorFunction", null, false, null, null);
    Node thisAnnotationNode = Node.newTokenNode(Token.THIS, "@this");
    Node methodNode = Node.newFunctionNode("thisAnnotatedMethod", null, false, null, null);
    methodNode.setParent(constructorNode);
    methodNode.setFirstChild(thisAnnotationNode);

    JSDocInfo constructorJsDoc = Node.newJSDocInfo(true, null, null, null, null, null, null, null, null, null, null, null, null, null);
    Node constructorJsDocNode = Node.newJSDocInfoNode(constructorJsDoc);
    constructorNode.setFirstChild(constructorJsDocNode);

    JSDocInfo methodJsDoc = Node.newJSDocInfo(true, null, null, null, null, null, null, null, null, null, null, null, null, null);
    methodNode.setJSDocInfo(methodJsDoc);

    NodeTraversal traversal = new NodeTraversal(new AbstractCompiler(null, null), null) {
      @Override
      public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
        return checkGlobalThis.shouldTraverse(this, n, parent);
      }
    };

    checkGlobalThis.visit(traversal, null, null);
    checkGlobalThis.visit(traversal, constructorNode, null);
    checkGlobalThis.visit(traversal, methodNode, constructorNode);
    assertFalse(checkGlobalThis.level.hasErrors());
  }

  @Test
  public void testFunctionWithoutThisAnnotation() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, WARNING);

    Node functionNode = Node.newFunctionNode("functionWithoutThisAnnotation", null, false, null, null);
    Node assignNode = Node.newAssignment(Node.newNameNode("x"), Node.newThis());
    functionNode.addChildToEnd(assignNode);

    NodeTraversal traversal = new NodeTraversal(new AbstractCompiler(null, null), null) {
      @Override
      public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
        return checkGlobalThis.shouldTraverse(this, n, parent);
      }
    };

    checkGlobalThis.visit(traversal, null, null);
    checkGlobalThis.visit(traversal, functionNode, null);
    assertTrue(checkGlobalThis.level.hasErrors());
  }

  @Test
  public void testAssignmentToPrototypeProperty() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, WARNING);

    Node objectNode = Node.newObjectLiteral();
    Node prototypeNode = Node.newNameNode("prototype");
    Node assignNode = Node.newAssignment(Node.newGetProp(prototypeNode, "x"), Node.newThis());

    NodeTraversal traversal = new NodeTraversal(new AbstractCompiler(null, null), null) {
      @Override
      public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
        return checkGlobalThis.shouldTraverse(this, n, parent);
      }
    };

    checkGlobalThis.visit(traversal, null, null);
    checkGlobalThis.visit(traversal, objectNode, null);
    Node prototypeChild = Node.newNameNode("prototype");
    checkGlobalThis.visit(traversal, prototypeChild, objectNode);
    checkGlobalThis.visit(traversal, assignNode, prototypeChild);
    assertFalse(checkGlobalThis.level.hasErrors());
  }

  @Test
  public void testAssignmentToPrototypeSubProperty() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, WARNING);

    Node objectNode = Node.newObjectLiteral();
    Node prototypeNode = Node.newNameNode("prototype");
    Node getPropNode = Node.newGetProp(prototypeNode, "subProperty");
    Node assignNode = Node.newAssignment(getPropNode, Node.newThis());

    NodeTraversal traversal = new NodeTraversal(new AbstractCompiler(null, null), null) {
      @Override
      public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
        return checkGlobalThis.shouldTraverse(this, n, parent);
      }
    };

    checkGlobalThis.visit(traversal, null, null);
    checkGlobalThis.visit(traversal, objectNode, null);
    Node prototypeChild = Node.newNameNode("prototype");
    checkGlobalThis.visit(traversal, prototypeChild, objectNode);
    Node subPropertyChild = Node.newNameNode("subProperty");
    checkGlobalThis.visit(traversal, subPropertyChild, prototypeChild);
    checkGlobalThis.visit(traversal, getPropNode, subPropertyChild);
    checkGlobalThis.visit(traversal, assignNode, getPropNode);
    assertFalse(checkGlobalThis.level.hasErrors());
  }

  @Test(expected = RuntimeException.class)
  public void testNullParent() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, WARNING);

    Node functionNode = Node.newFunctionNode("functionWithNullParent", null, false, null, null);
    Node assignNode = Node.newAssignment(Node.newNameNode("x"), Node.newThis());
    functionNode.addChildToEnd(assignNode);

    NodeTraversal traversal = new NodeTraversal(new AbstractCompiler(null, null), null) {
      @Override
      public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
        return checkGlobalThis.shouldTraverse(this, n, parent);
      }
    };

    checkGlobalThis.visit(traversal, null, null);
    checkGlobalThis.visit(traversal, functionNode, null);
  }
}