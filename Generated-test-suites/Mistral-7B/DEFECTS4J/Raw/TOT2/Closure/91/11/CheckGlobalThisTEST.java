import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.rhino.NodeFactory.createFunction;
import static com.google.javascript.rhino.NodeFactory.createIdentifier;
import static com.google.javascript.rhino.NodeFactory.createName;
import static com.google.javascript.rhino.NodeFactory.createScript;
import static com.google.javascript.rhino.NodeFactory.createToken;
import static com.google.javascript.rhino.TokenType.*;
import static com.google.javascript.jscomp.NodeTraversal.Callback.*;

import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverse() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal traversal = new NodeTraversal(null, null);

    Node functionNode = createFunction(null, null, new Node[0]);
    Node scriptNode = createScript("function test() {}");

    assertTrue(checkGlobalThis.shouldTraverse(traversal, functionNode, scriptNode));
  }

  @Test
  public void testShouldTraverseConstructor() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal traversal = new NodeTraversal(null, null);
    Node constructorNode = createFunction(null, new Node[0], new Node[0], new Node[0], createToken(FUNCTION, "constructor")).setJSDocInfo(createFunctionJsDocInfo("constructor"));
    Node scriptNode = createScript("function test() {}");

    assertFalse(checkGlobalThis.shouldTraverse(traversal, constructorNode, scriptNode));
  }

  @Test
  public void testShouldTraverseThisAnnotation() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal traversal = new NodeTraversal(null, null);
    Node thisAnnotatedNode = createFunction(null, new Node[0], new Node[0], new Node[0], null).setJSDocInfo(createFunctionJsDocInfo("@this"));
    Node scriptNode = createScript("function test() {}");

    assertTrue(checkGlobalThis.shouldTraverse(traversal, thisAnnotatedNode, scriptNode));
  }

  @Test
  public void testShouldTraversePrototype() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal traversal = new NodeTraversal(null, null);
    Node prototypeNode = createFunction(null, new Node[0], new Node[0], new Node[0], null).setJSDocInfo(createFunctionJsDocInfo("prototypeMethod"));
    Node prototypeNodeParent = createIdentifier("prototype");
    Node protoObjectNode = createScript("var obj = {prototypeMethod: function() {}}");

    assertTrue(checkGlobalThis.shouldTraverse(traversal, prototypeNode, protoObjectNode));
    assertTrue(checkGlobalThis.shouldTraverse(traversal, prototypeNode, prototypeNodeParent));
  }

  @Test
  public void testShouldTraversePrototypeProperty() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal traversal = new NodeTraversal(null, null);
    Node getterNode = createIdentifier("getter");
    Node prototypeNode = createFunction(null, new Node[0], new Node[0], new Node[0], null).setJSDocInfo(createFunctionJsDocInfo("prototypeMethod"));
    Node prototypeNodeParent = createIdentifier("prototype");
    Node protoObjectNode = createScript("var obj = {getter: function() {return prototypeMethod;}}");

    assertTrue(checkGlobalThis.shouldTraverse(traversal, getterNode, protoObjectNode));
    assertTrue(checkGlobalThis.shouldTraverse(traversal, getterNode, prototypeNodeParent));
  }

  @Test
  public void testShouldTraverseAssignmentLeftSide() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal traversal = new NodeTraversal(null, null);
    Node lhsNode = createIdentifier("a");
    Node rhsNode = createIdentifier("this");
    Node assignNode = createToken(ASSIGN, "=");
    Node scriptNode = createScript("var a = this;");

    assertTrue(checkGlobalThis.shouldTraverse(traversal, lhsNode, scriptNode));
  }

  @Test
  public void testShouldTraverseAssignmentRightSide() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal traversal = new NodeTraversal(null, null);
    Node lhsNode = createIdentifier("a");
    Node rhsNode = createIdentifier("this");
    Node assignNode = createToken(ASSIGN, "=");
    Node scriptNode = createScript("var a = this;");

    assertTrue(checkGlobalThis.shouldTraverse(traversal, rhsNode, scriptNode));
  }

  private JSDocInfo createFunctionJsDocInfo(String annotation) {
    return new JSDocInfo(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, new Node[0], null, null, null, null, null, null, null, null, null, createToken(ANNOTATION, "@" + annotation), null);
  }
}