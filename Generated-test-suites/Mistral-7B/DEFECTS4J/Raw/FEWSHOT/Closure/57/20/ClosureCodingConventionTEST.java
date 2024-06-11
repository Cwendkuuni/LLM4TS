package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;

import com.google.javascript.jscomp.ClosureCodingConvention;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.SubclassRelationship;
import com.google.javascript.rhino.NodeUtil;

import org.junit.Test;

public class ClosureCodingConventionTest {

  @Test
  public void testApplySubclassRelationship() {
    FunctionType parentCtor = new FunctionType("ParentCtor", null, null);
    FunctionType childCtor = new FunctionType("ChildCtor", null, null);

    ClosureCodingConvention convention = new ClosureCodingConvention();
    convention.applySubclassRelationship(parentCtor, childCtor, SubclassType.INHERITS);

    ObjectType parentProto = parentCtor.getPrototype();
    ObjectType childProto = childCtor.getPrototype();

    assertThat(parentProto.getDeclaredProperties().size()).isEqualTo(2);
    assertThat(parentProto.getDeclaredProperties().get(0).getName()).isEqualTo("superClass_");
    assertThat(parentProto.getDeclaredProperties().get(1).getName()).isEqualTo("constructor");

    assertThat(childProto.getDeclaredProperties().size()).isEqualTo(2);
    assertThat(childProto.getDeclaredProperties().get(0).getName()).isEqualTo("superClass_");
    assertThat(childProto.getDeclaredProperties().get(1).getName()).isEqualTo("constructor");
    assertThat(childProto.getDeclaredProperties().get(1).getValueType()).isInstanceOf(childCtor);
  }

  @Test
  public void testExtractClassNameIfProvide() {
    Node node = Node.newNode(Token.CALL, null,
        Node.newNode(Token.GETPROP, null, Node.newNode(Token.STRING, "goog.provide")),
        Node.newNode(Token.STRING, "X")
    );

    String className = new ClosureCodingConvention().extractClassNameIfProvide(node, null);
    assertThat(className).isEqualTo("X");
  }

  @Test
  public void testExtractClassNameIfRequire() {
    Node node = Node.newNode(Token.CALL, null,
        Node.newNode(Token.GETPROP, null, Node.newNode(Token.STRING, "goog.require")),
        Node.newNode(Token.STRING, "X")
    );

    String className = new ClosureCodingConvention().extractClassNameIfRequire(node, null);
    assertThat(className).isEqualTo("X");
  }

  @Test
  public void testIsSuperClassReference() {
    ClosureCodingConvention convention = new ClosureCodingConvention();
    assertTrue(convention.isSuperClassReference("superClass_"));
  }

  @Test
  public void testEndsWithPrototype() {
    Node qualifiedName = Node.newNode(Token.GETPROP, null,
        Node.newNode(Token.STRING, "a")
    );
    Node prototypeNode = Node.newNode(Token.GETPROP, null,
        Node.newNode(Token.STRING, "prototype")
    );

    Node prototypeQualifiedName = Node.newNode(Token.GETPROP, null, qualifiedName, prototypeNode);

    assertTrue(new ClosureCodingConvention().endsWithPrototype(prototypeQualifiedName));
  }

  @Test
  public void testApplySingletonGetter() {
    FunctionType getterType = new FunctionType("getter", null, null);
    FunctionType functionType = new FunctionType("functionType", null, null);

    ObjectType objectType = new ObjectType("objectType", null);

    new ClosureCodingConvention().applySingletonGetter(functionType, getterType, objectType);

    assertThat(functionType.getDeclaredProperties().size()).isEqualTo(2);
    assertThat(functionType.getDeclaredProperties().get(0).getName()).isEqualTo("getInstance");
    assertThat(functionType.getDeclaredProperties().get(1).getName()).isEqualTo("instance_");
  }

  @Test
  public void testGetExportPropertyFunction() {
    String functionName = new ClosureCodingConvention().getExportPropertyFunction();
    assertEquals("goog.exportProperty", functionName);
  }

  @Test
  public void testGetExportSymbolFunction() {
    String functionName = new ClosureCodingConvention().getExportSymbolFunction();
    assertEquals("goog.exportSymbol", functionName);
  }

  @Test
  public void testIdentifyTypeDeclarationCall() {
    Node node = Node.newNode(Token.CALL, null,
        Node.newNode(Token.STRING, "goog.addDependency"),
        Node.newNode(Token.ARRAYLIT, null),
        Node.newNode(Token.OBJECTLIT, null)
    );

    List<String> typeNames = new ClosureCodingConvention().identifyTypeDeclarationCall(node);
    Node typeListNode = node.getFirstChild().getNext();

    List<String> expectedTypeNames = Lists.newArrayList();
    for (Node typeNameNode = typeListNode.getFirstChild(); typeNameNode != null;
         typeNameNode = typeNameNode.getNext()) {
      expectedTypeNames.add(typeNameNode.getString());
    }

    assertThat(typeNames).isEqualTo(expectedTypeNames);
  }

  @Test
  public void testGetAbstractMethodName() {
    String abstractMethodName = new ClosureCodingConvention().getAbstractMethodName();
    assertEquals("goog.abstractMethod", abstractMethodName);
  }

  @Test
  public void testGetSingletonGetterClassName() {
    Node node = Node.newNode(Token.CALL, null,
        Node.newNode(Token.STRING, "goog.addSingletonGetter"),
        Node.newNode(Token.STRING, "getterName")
    );

    String className = new ClosureCodingConvention().getSingletonGetterClassName(node);
    assertEquals("getterName", className);
  }

  @Test
  public void testIsPropertyTestFunction() {
    Node node = Node.newNode(Token.CALL, null,
        Node.newNode(Token.GETPROP, null, Node.newNode(Token.STRING, "goog.isDef")),
        Node.newNode(Token.NUMBER, 42)
    );

    assertTrue(new ClosureCodingConvention().isPropertyTestFunction(node));
  }

  @Test
  public void testGetObjectLiteralCast() {
    Node node = Node.newNode(Token.CALL, null,
        Node.newNode(Token.GETPROP, null, Node.newNode(Token.STRING, "goog.reflect.object")),
        Node.newNode(Token.STRING, "X"),
        Node.newNode(Token.OBJECTLIT, null)
    );

    ObjectLiteralCast cast = new ClosureCodingConvention().getObjectLiteralCast(new NodeTraversal(null), node);
    assertEquals("X", cast.getTypeName());
  }

  @Test
  public void testDescribeFunctionBind() {
    Node node = Node.newNode(Token.CALL, null,
        Node.newNode(Token.GETPROP, null, Node.newNode(Token.STRING, "goog.bind")),
        Node.newNode(Token.FUNCTION, null, null, null),
        Node.newNode(Token.STRING, "self"),
        Node.newNode(Token.ARRAYLIT, null)
    );

    Bind bind = new ClosureCodingConvention().describeFunctionBind(node);
    assertEquals(Node.newNode(Token.FUNCTION, null, null, null), bind.getFunction());
    assertEquals(Node.newNode(Token.NAME, "self"), bind.getThisValue());
  }

  private enum SubclassType {
    INHERITS,
    MIXIN
  }
}