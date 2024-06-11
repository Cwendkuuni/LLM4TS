package org.apache.commons.cli.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Reference;
import com.google.javascript.jscomp.ReferenceCollection;
import com.google.javascript.jscomp.ReferenceMap;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;
import org.apache.commons.cli.InlineObjectLiterals;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class InlineObjectLiteralsTest {

  private static Node createObjectLiteral(Node parent, String... keysValues) {
    Node object = new Node(Token.OBJECTLIT, null);
    Node keyValue;

    for (String keyValuePair : keysValues) {
      keyValue = Node.newPair(Token.NAME, keyValuePair.split(":"));
      object.addChildToBack(keyValue);
    }

    parent.addChildToBack(object);
    return object;
  }

  private static void testInlineObjectLiterals(
      InlineObjectLiterals pass,
      Node root,
      String variableName,
      String... objectLiteralKeysValues) {
    Scope scope = new Scope(null, null, null, null, null, null, null, null, null);
    Node externs = new Node(Token.SCRIPT, null);
    pass.process(externs, root);

    ReferenceCollection referenceInfo = new ReferenceCollectingCallback(
        pass.compiler, new InliningBehavior()).getReferenceInfo(variableName, scope);

    assertTrue(referenceInfo.isWellDefined());
    assertTrue(referenceInfo.getInitializingReference() != null);

    ReferenceCollection expectedReferenceInfo = new ReferenceCollectingCallback(
        null, new InliningBehavior()).getReferenceInfo(variableName, scope);

    assertTrue(expectedReferenceInfo.isWellDefined());

    List<Reference> expectedReferences = expectedReferenceInfo.references;
    Map<String, Node> expectedInitValues = new ImmutableMap.Builder<String, Node>()
        .putAll(Maps.newLinkedHashMap(expectedReferenceInfo.getInitializingReference().getAssignedValue().getChildren()))
        .build();

    List<Reference> references = referenceInfo.references;
    Map<String, Node> initValues = new ImmutableMap.Builder<String, Node>()
        .putAll(Maps.newLinkedHashMap(referenceInfo.getInitializingReference().getAssignedValue().getChildren()))
        .build();

    assertEquals(expectedReferences.size(), references.size());
    for (int i = 0; i < expectedReferences.size(); i++) {
      Reference reference = expectedReferences.get(i);
      Reference testReference = references.get(i);

      assertEquals(reference.getNode(), testReference.getNode());
      assertEquals(reference.getAssignedValue(), testReference.getAssignedValue());
    }

    assertEquals(expectedInitValues.size(), initValues.size());
    for (Map.Entry<String, Node> entry : expectedInitValues.entrySet()) {
      String key = entry.getKey();
      Node expectedValue = entry.getValue();
      Node testValue = initValues.get(key);

      assertEquals(expectedValue, testValue);
    }

    assertEquals(expectedInitValues, initValues);

    // Check that the object literal has been fully inlined.
    Iterator<Node> it = scope.getRootNode().getFirstChild().getChildren().iterator();
    Node nextNode;

    while (it.hasNext()) {
      nextNode = it.next();

      if (nextNode.getType() == Token.VAR) {
        if (nextNode.getString().equals(variableName)) {
          Node nextChild = nextNode.getFirstChild();
          for (String keyValuePair : objectLiteralKeysValues) {
            assertTrue(nextChild.getType() == Token.ASSIGN);
            String keyValue = keyValuePair.split(":");
            String key = keyValue[0];
            Node expectedKeyNode = new Node(Token.NAME, key);
            Node expectedValueNode = new Node(Token.OBJECTLIT, null);
            Node testKeyNode = nextChild.getFirstChild();
            Node testValueNode = nextChild.getFirstChild().getNext();

            assertEquals(expectedKeyNode, testKeyNode);
            assertEquals(expectedValueNode, testValueNode);
            nextChild = nextChild.getNext();
          }
          break;
        }
      }
    }
  }

  @Test
  public void testInlineSimpleObject() {
    InlineObjectLiterals pass = new InlineObjectLiterals(
        new CompilerPassTestingStub(),
        () -> Node.newString("varTemp"));

    Node root = new Node(Token.SCRIPT, null);
    root.addChildToBack(createObjectLiteral(root, "a:1,b:2"));
    root.addChildToBack(new Node(
        Token.ASSIGN,
        new Node(Token.NAME, "x"),
        new Node(Token.NAME, "JSCompiler_object_inline_a"),
        null));

    testInlineObjectLiterals(pass, root, "x", "a:1,b:2");
  }

  @Test
  public void testInlineComplexObject() {
    InlineObjectLiterals pass = new InlineObjectLiterals(
        new CompilerPassTestingStub(),
        () -> Node.newString("varTemp"));

    Node root = new Node(Token.SCRIPT, null);
    root.addChildToBack(createObjectLiteral(root,
        "a:1," +
        "c:{d:4,e:5}, " + // separate objects
        "f:{g:6,h:7}" // separate objects
    ));
    root.addChildToBack(new Node(
        Token.ASSIGN,
        new Node(Token.NAME, "x"),
        new Node(Token.NAME, "JSCompiler_object_inline_a"),
        null));

    testInlineObjectLiterals(pass, root, "x", "a:1,c:{\"d\":4,\"e\":5},f:{\"g\":6,\"h\":7}");
  }

  @Test
  public void testInlineObjectWithEmptyInitialValue() {
    InlineObjectLiterals pass = new InlineObjectLiterals(
        new CompilerPassTestingStub(),
        () -> Node.newString("varTemp"));

    Node root = new Node(Token.SCRIPT, null);
    root.addChildToBack(createObjectLiteral(root, "a:1,b:2"));
    root.addChildToBack(new Node(
        Token.ASSIGN,
        new Node(Token.NAME, "x"),
        new Node(Token.NAME, "JSCompiler_object_inline_a"),
        NodeUtil.newUndefinedNode(null)
    ));

    testInlineObjectLiterals(pass, root, "x", "a:1,b:2");
  }

  @Test
  public void testInlineObjectWithInitialValue() {
    InlineObjectLiterals pass = new InlineObjectLiterals(
        new CompilerPassTestingStub(),
        () -> Node.newString("varTemp"));

    Node root = new Node(Token.SCRIPT, null);
    root.addChildToBack(createObjectLiteral(root, "a:1,b:2"));
    root.addChildToBack(new Node(
        Token.ASSIGN,
        new Node(Token.NAME, "x"),
        new Node(Token.NAME, "JSCompiler_object_inline_a"),
        new Node(Token.OBJECTLIT, null,
          new Node(Token.NAME, "c"),
          new Node(Token.NUMBER, "3")
        )));

    testInlineObjectLiterals(pass, root, "x", "a:1,b:2,c:3");
  }

  @Test
  public void testInlineObjectWithCircularReference() {
    InlineObjectLiterals pass = new InlineObjectLiterals(
        new CompilerPassTestingStub(),
        () -> Node.newString("varTemp"));

    Node root = new Node(Token.SCRIPT, null);
    Node object1 = createObjectLiteral(root, "a:1,b:{\"c\":\"x\"}");
    Node object2 = createObjectLiteral(root, "c:\"x\",d:2");
    root.addChildToBack(new Node(
        Token.ASSIGN,
        new Node(Token.NAME, "x"),
        new Node(Token.NAME, "JSCompiler_object_inline_a"),
        null
    ));
    Node object1Assignment = new Node(Token.ASSIGN, null, object1, null);
    Node object2Assignment = new Node(Token.ASSIGN, null, object2, null);
    root.addChildToBack(object1Assignment);
    root.addChildToBack(object2Assignment);

    // The pass should fail with an error message when encountering circular references.
    try {
      testInlineObjectLiterals(pass, root, "x", "a:1,b:{\"c\":\"x\"},c:\"x\",d:2");
      fail();
    } catch (AssertionError e) {
      assertNotNull(e.getMessage());
    }
  }

  @Test
  public void testInlineObjectWithSelfAssignment() {
    InlineObjectLiterals pass = new InlineObjectLiterals(
        new CompilerPassTestingStub(),
        () -> Node.newString("varTemp"));

    Node root = new Node(Token.SCRIPT, null);
    Node object = createObjectLiteral(root, "a:{b:x}");
    root.addChildToBack(new Node(
        Token.ASSIGN,
        new Node(Token.NAME, "x"),
        new Node(Token.NAME, "JSCompiler_object_inline_a"),
        null
    ));

    // The pass should fail with an error message when encountering self-assignments.
    try {
      testInlineObjectLiterals(pass, root, "x", "a:{b:x}");
      fail();
    } catch (AssertionError e) {
      assertNotNull(e.getMessage());
    }
  }

  @Test
  public void testInlineObjectWithSelfReference() {
    InlineObjectLiterals pass = new InlineObjectLiterals(
        new CompilerPassTestingStub(),
        () -> Node.newString("varTemp"));

    Node root = new Node(Token.SCRIPT, null);
    Node object1 = createObjectLiteral(root, "a:1,b:{\"c\":\"x\"}");
    Node object2 = createObjectLiteral(root, "c:\"{a:1,b:{c:'x'}}\",d:2");
    root.addChildToBack(new Node(
        Token.ASSIGN,
        new Node(Token.NAME, "x"),
        new Node(Token.NAME, "JSCompiler_object_inline_a"),
        null
    ));
    Node object1Assignment = new Node(Token.ASSIGN, null, object1, null);
    Node object2Assignment = new Node(Token.ASSIGN, null, object2, null);
    root.addChildToBack(object1Assignment);
    root.addChildToBack(object2Assignment);

    testInlineObjectLiterals(pass, root, "x", "a:1,b:{\"c\":\"x\"},c:\"{a:1,b:{c:'x'}}\",d:2");
  }

  @Test
  public void testInlineObjectWithInvalidObjectLiteralAssignment() {
    InlineObjectLiterals pass = new InlineObjectLiterals(
        new CompilerPassTestingStub(),
        () -> Node.newString("varTemp"));

    Node root = new Node(Token.SCRIPT, null);
    Node object = new Node(Token.OBJECTLIT, null);
    root.addChildToBack(new Node(
        Token.ASSIGN,
        new Node(Token.NAME, "x"),
        object,
        null
    ));

    // The pass should fail with an error message when encountering an invalid object literal assignment.
    try {
      testInlineObjectLiterals(pass, root, "x", "");
      fail();
    } catch (AssertionError e) {
      assertNotNull(e.getMessage());
    }
  }

  @Test
  public void testInlineObjectWithMissingInitialValue() {
    InlineObjectLiterals pass = new InlineObjectLiterals(
        new CompilerPassTestingStub(),
        () -> Node.newString("varTemp"));

    Node root = new Node(Token.SCRIPT, null);
    Node object = createObjectLiteral(root, "a:1,b:2");
    root.addChildToBack(new Node(
        Token.ASSIGN,
        new Node(Token.NAME, "x"),
        new Node(Token.NAME, "JSCompiler_object_inline_a"),
        null
    ));

    // The pass should fail with an error message when encountering a missing initial value.
    try {
      testInlineObjectLiterals(pass, root, "x", "a:1,b:2");
      fail();
    } catch (AssertionError e) {
      assertNotNull(e.getMessage());
    }
  }

  @Test
  public void testInlineObjectWithMissingProperty() {
    InlineObjectLiterals pass = new InlineObjectLiterals(
        new CompilerPassTestingStub(),
        () -> Node.newString("varTemp"));

    Node root = new Node(Token.SCRIPT, null);
    Node object = createObjectLiteral(root, "a:1,b:2");
    root.addChildToBack(new Node(
        Token.ASSIGN,
        new Node(Token.NAME, "x"),
        new Node(Token.NAME, "JSCompiler_object_inline_a"),
        null
    ));

    // The pass should fail with an error message when encountering a missing property.
    try {
      testInlineObjectLiterals(pass, root, "x.missing", "a:1,b:2");
      fail();
    } catch (AssertionError e) {
      assertNotNull(e.getMessage());
    }
  }
}

class CompilerPassTestingStub implements AbstractCompiler {
  @Override
  public void process(Node externs, Node root) {
    // Empty implementation.
  }
}

class InliningBehavior extends ReferenceCollectingCallback.Behavior {
  @Override
  public void afterExitScope(NodeTraversal t, ReferenceMap referenceMap) {
    // Empty implementation.
  }
}