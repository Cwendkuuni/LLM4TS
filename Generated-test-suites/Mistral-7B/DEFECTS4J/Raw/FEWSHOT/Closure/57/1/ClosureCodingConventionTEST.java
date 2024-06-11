package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.javascript.jscomp.ClosureCodingConvention;
import com.google.javascript.jscomp.DefaultCodingConvention;
import com.google.javascript.jscomp.FunctionType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ObjectType;
import com.google.javascript.jscomp.SubclassRelationship;
import com.google.javascript.jscomp.SubclassType;
import com.google.javascript.rhino.NodeTraversal;

import java.util.Collection;
import java.util.List;

public class ClosureCodingConventionTest {

  @Test
  public void testApplySubclassRelationship() {
    FunctionType parentCtor = new FunctionType("Parent", ImmutableList.<FunctionType>of());
    FunctionType childCtor = new FunctionType("Child", ImmutableList.<FunctionType>of());

    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    codingConvention.applySubclassRelationship(parentCtor, childCtor, SubclassType.INHERITS);

    assertEquals("superClass_", childCtor.getDeclaredPropertyNames().get(0));
    assertEquals("constructor", childCtor.getPrototype().getDeclaredPropertyNames().get(0));
    assertEquals(parentCtor, childCtor.getPrototype().getDeclaredProperty("constructor"));
  }

  @Test
  public void testGetclasssDefinedByCall() {
    Node node = Node.newNode(
        Token.CALL,
        Node.newNode(Token.GETPROP, Node.newNode(Token.NAME, "goog")),
        Node.newNode(Token.NAME, "inherits"),
        Node.newNode(Token.NAME, "ChildClass"),
        Node.newNode(Token.NAME, "ParentClass")
    );

    SubclassRelationship relationship = new ClosureCodingConvention().getclasssDefinedByCall(node);
    assertEquals(SubclassType.INHERITS, relationship.getType());
    assertEquals("ChildClass", relationship.getSubclass().getQualifiedName());
    assertEquals("ParentClass", relationship.getSuperclass().getQualifiedName());

    node = Node.newNode(
        Token.CALL,
        Node.newNode(Token.NAME, "goog$inherits"),
        Node.newNode(Token.NAME, "ChildClass"),
        Node.newNode(Token.NAME, "ParentClass")
    );

    relationship = new ClosureCodingConvention().getclasssDefinedByCall(node);
    assertEquals(SubclassType.INHERITS, relationship.getType());
    assertEquals("ChildClass", relationship.getSubclass().getQualifiedName());
    assertEquals("ParentClass", relationship.getSuperclass().getQualifiedName());

    node = Node.newNode(
        Token.CALL,
        Node.newNode(Token.GETPROP, Node.newNode(Token.NAME, "goog$")),
        Node.newNode(Token.NAME, "inherits"),
        Node.newNode(Token.NAME, "ChildClass"),
        Node.newNode(Token.NAME, "ParentClass")
    );

    relationship = new ClosureCodingConvention().getclasssDefinedByCall(node);
    assertEquals(SubclassType.INHERITS, relationship.getType());
    assertEquals("ChildClass", relationship.getSubclass().getQualifiedName());
    assertEquals("ParentClass", relationship.getSuperclass().getQualifiedName());

    node = Node.newNode(
        Token.CALL,
        Node.newNode(Token.GETPROP, Node.newNode(Token.NAME, "ChildClass")),
        Token.NAME, "mixin",
        Node.newNode(Token.GETPROP, Node.newNode(Token.NAME, "ParentClass")),
        Token.DOT
    );

    relationship = new ClosureCodingConvention().getclasssDefinedByCall(node);
    assertEquals(SubclassType.MIXIN, relationship.getType());
    assertEquals("ChildClass", relationship.getSubclass().getQualifiedName());
    assertEquals("ParentClass", relationship.getSuperclass().getQualifiedName());

    node = Node.newNode(
        Token.CALL,
        Node.newNode(Token.NAME, "goog.mixin"),
        Node.newNode(Token.GETPROP, Node.newNode(Token.NAME, "ChildClass")),
        Node.newNode(Token.GETPROP, Node.newNode(Token.NAME, "ParentClass"))
    );

    relationship = new ClosureCodingConvention().getclasssDefinedByCall(node);
    assertEquals(SubclassType.MIXIN, relationship.getType());
    assertEquals("ChildClass", relationship.getSubclass().getQualifiedName());
    assertEquals("ParentClass", relationship.getSuperclass().getQualifiedName());

    node = Node.newNode(
        Token.CALL,
        Node.newNode(Token.NAME, "goog$mixin"),
        Node.newNode(Token.GETPROP, Node.newNode(Token.NAME, "ChildClass")),
        Node.newNode(Token.GETPROP, Node.newNode(Token.NAME, "ParentClass"))
    );

    relationship = new ClosureCodingConvention().getclasssDefinedByCall(node);
    assertEquals(SubclassType.MIXIN, relationship.getType());
    assertEquals("ChildClass", relationship.getSubclass().getQualifiedName());
    assertEquals("ParentClass", relationship.getSuperclass().getQualifiedName());

    node = Node.newNode(Token.CALL, Node.newNode(Token.NAME, "goog.inherits"), null, null);
    assertNull(new ClosureCodingConvention().getclasssDefinedByCall(node));

    node = Node.newNode(Token.CALL, Node.newNode(Token.NAME, "goog.mixin"), null, null);
    assertNull(new ClosureCodingConvention().getclasssDefinedByCall(node));
  }

  @Test
  public void testExtractClassNameIfProvide() {
    Node node = Node.newNode(
        Token.CALL,
        Node.newNode(Token.GETPROP, Node.newNode(Token.NAME, "goog")),
        Token.NAME, "provide",
        Node.newNode(Token.STRING, "X")
    );
    assertEquals("X", new ClosureCodingConvention().extractClassNameIfProvide(node, null));
  }

  @Test
  public void testExtractClassNameIfRequire() {
    Node node = Node.newNode(
        Token.CALL,
        Node.newNode(Token.GETPROP, Node.newNode(Token.NAME, "goog")),
        Token.NAME, "require",
        Node.newNode(Token.STRING, "X")
    );
    assertEquals("X", new ClosureCodingConvention().extractClassNameIfRequire(node, null));
  }

  @Test
  public void testIdentifyTypeDeclarationCall() {
    Node node = Node.newNode(
        Token.CALL,
        Node.newNode(Token.NAME, "goog.addDependency"),
        Node.newNode(Token.ARRAYLIT,
            Node.newNode(Token.STRING, "TypeA"),
            Node.newNode(Token.STRING, "TypeB")
        )
    );
    List<String> result = new ClosureCodingConvention().identifyTypeDeclarationCall(node);
    assertEquals(ImmutableList.of("TypeA", "TypeB"), result);

    node = Node.newNode(
        Token.CALL,
        Node.newNode(Token.NAME, "goog.addDependency"),
        Node.newNode(Token.NAME, "TypeC"),
        Node.newNode(Token.ARRAYLIT, Node.newNode(Token.STRING, "TypeD"))
    );
    result = new ClosureCodingConvention().identifyTypeDeclarationCall(node);
    assertEquals(ImmutableList.of("TypeC", "TypeD"), result);
  }

  @Test
  public void testIsPropertyTestFunction() {
    Node node = Node.newNode(
        Token.CALL,
        Node.newNode(Token.GETPROP, Node.newNode(Token.NAME, "goog.asserts")),
        Token.NAME, "assertString"
    );
    assertTrue(new ClosureCodingConvention().isPropertyTestFunction(node));

    node = Node.newNode(
        Token.CALL,
        Node.newNode(Token.GETPROP, Node.newNode(Token.NAME, "goog.asserts")),
        Token.NAME, "assertNumber"
    );
    assertTrue(new ClosureCodingConvention().isPropertyTestFunction(node));

    node = Node.newNode(
        Token.CALL,
        Node.newNode(Token.GETPROP, Node.newNode(Token.NAME, "goog.asserts")),
        Token.NAME, "assertFunction"
    );
    assertTrue(new ClosureCodingConvention().isPropertyTestFunction(node));

    node = Node.newNode(
        Token.CALL,
        Node.newNode(Token.GETPROP, Node.newNode(Token.NAME, "goog.asserts")),
        Token.NAME, "assertObject"
    );
    assertTrue(new ClosureCodingConvention().isPropertyTestFunction(node));

    node = Node.newNode(
        Token.CALL,
        Node.newNode(Token.GETPROP, Node.newNode(Token.NAME, "goog.asserts")),
        Token.NAME, "assertArray"
    );
    assertTrue(new ClosureCodingConvention().isPropertyTestFunction(node));

    node = Node.newNode(
        Token.CALL,
        Node.newNode(Token.GETPROP, Node.newNode(Token.NAME, "goog.asserts")),
        Token.NAME, "assertInstanceof"
    );
    assertTrue(new ClosureCodingConvention().isPropertyTestFunction(node));
  }

  @Test
  public void testGetObjectLiteralCast() {
    Node node = Node.newNode(
        Token.CALL,
        Node.newNode(Token.GETPROP, Node.newNode(Token.NAME, "goog.reflect.object")),
        Node.newNode(Token.NAME, "cast"),
        Node.newNode(Token.NAME, "X"),
        Node.newNode(Token.OBJECTLIT, Node.newNode(Token.STRING, "Y"))
    );
    ObjectLiteralCast result = new ClosureCodingConvention().getObjectLiteralCast(new NodeTraversal(), node);
    assertEquals("X", result.getType());
    assertEquals("Y", result.getValue());

    node = Node.newNode(
        Token.CALL,
        Node.newNode(Token.NAME, "goog.reflect.object"),
        Token.NAME, "cast",
        Node.newNode(Token.NAME, "X"),
        Node.newNode(Token.OBJECTLIT, Node.newNode(Token.STRING, "Y"))
    );
    result = new ClosureCodingConvention().getObjectLiteralCast(new NodeTraversal(), node);
    assertEquals("X", result.getType());
    assertEquals("Y", result.getValue());
  }
}