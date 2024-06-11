package com.google.javascript.rhino.jstype.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.ArrowType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.JSTypeVisitor;
import com.google.javascript.rhino.jstype.StaticScope;

import org.junit.Test;

public class ArrowTypeTest {

  private static ErrorReporter t = new ErrorReporter() {};
  private static JSTypeRegistry registry = new JSTypeRegistry();
  private static StaticScope<JSType> scope = new StaticScope<>(registry);

  @Test
  public void testArrowType() {
    JSType intType = registry.createPrimitiveType(JSTypeNative.INT);
    JSType stringType = registry.createPrimitiveType(JSTypeNative.STRING);
    JSType voidType = registry.createPrimitiveType(JSTypeNative.VOID);

    /**
     * ArrowType with no parameters and no return type
     */
    ArrowType emptyArrowType = new ArrowType(registry, null, null);
    assertEquals("() -> ", emptyArrowType.toString());
    assertEquals(voidType, emptyArrowType.returnType);
    assertNull(emptyArrowType.parameters);
    assertEquals(JSType.UNDEFINED, emptyArrowType.returnTypeInferred);

    /**
     * ArrowType with no parameters and a return type
     */
    ArrowType arrowTypeNoParams = new ArrowType(registry, null, intType);
    assertEquals("() -> int", arrowTypeNoParams.toString());
    assertEquals(intType, arrowTypeNoParams.returnType);
    assertNull(arrowTypeNoParams.parameters);
    assertEquals(false, arrowTypeNoParams.returnTypeInferred);

    /**
     * ArrowType with one parameter and a return type
     */
    Node paramNode = registry.createIdentifier("p");
    paramNode.setJSType(intType);
    ArrowType arrowTypeOneParam = new ArrowType(registry, Node.newList(paramNode), intType);
    assertEquals("(int) -> int", arrowTypeOneParam.toString());
    assertEquals(intType, arrowTypeOneParam.returnType);
    assertEquals(intType, arrowTypeOneParam.parameters.getFirstChild().getJSType());
    assertEquals(false, arrowTypeOneParam.returnTypeInferred);

    /**
     * ArrowType with multiple parameters and a return type
     */
    Node paramNode1 = registry.createIdentifier("p1");
    paramNode1.setJSType(intType);
    Node paramNode2 = registry.createIdentifier("p2");
    paramNode2.setJSType(stringType);
    ArrowType arrowTypeMultipleParams = new ArrowType(registry,
        Node.newList(paramNode1, paramNode2), intType);
    assertEquals("(int, string) -> int", arrowTypeMultipleParams.toString());
    assertEquals(intType, arrowTypeMultipleParams.returnType);
    assertEquals(intType, arrowTypeMultipleParams.parameters.getFirstChild().getJSType());
    assertEquals(stringType, arrowTypeMultipleParams.parameters.getChild(1).getJSType());
    assertEquals(false, arrowTypeMultipleParams.returnTypeInferred);

    /**
     * ArrowType with a variable number of arguments and a return type
     */
    ArrowType arrowTypeVarArgs = new ArrowType(registry, null, intType, true);
    assertEquals("(...) -> int", arrowTypeVarArgs.toString());
    assertEquals(intType, arrowTypeVarArgs.returnType);
    assertNull(arrowTypeVarArgs.parameters);
    assertEquals(true, arrowTypeVarArgs.returnTypeInferred);

    /**
     * ArrowType with a return type that is also an ArrowType
     */
    ArrowType arrowTypeReturnArrowType = new ArrowType(registry, null,
        new ArrowType(registry, null, intType));
    assertEquals("() -> ((int) -> int)", arrowTypeReturnArrowType.toString());
    assertEquals(new ArrowType(registry, null, intType), arrowTypeReturnArrowType.returnType);
    assertNull(arrowTypeReturnArrowType.parameters);
    assertEquals(false, arrowTypeReturnArrowType.returnTypeInferred);
  }

  @Test
  public void testEquivalence() {
    JSType intType = registry.createPrimitiveType(JSTypeNative.INT);
    JSType stringType = registry.createPrimitiveType(JSTypeNative.STRING);

    ArrowType arrowType1 = new ArrowType(registry, null, intType);
    ArrowType arrowType2 = new ArrowType(registry, null, intType);
    ArrowType arrowType3 = new ArrowType(registry, null, stringType);

    assertEquals(arrowType1, arrowType1);
    assertEquals(arrowType1, arrowType1);
    assertEquals(arrowType1, arrowType2);
    assertEquals(arrowType1, arrowType2);
    assertEquals(arrowType1, arrowType1);

    assertNotEquals(arrowType1, arrowType3);
    assertNotEquals(arrowType3, arrowType1);
  }

  @Test
  public void testHashCode() {
    JSType intType = registry.createPrimitiveType(JSTypeNative.INT);
    JSType stringType = registry.createPrimitiveType(JSTypeNative.STRING);

    ArrowType arrowType1 = new ArrowType(registry, null, intType);
    ArrowType arrowType2 = new ArrowType(registry, null, intType);
    ArrowType arrowType3 = new ArrowType(registry, null, stringType);

    int hashCode1 = arrowType1.hashCode();
    int hashCode2 = arrowType2.hashCode();
    int hashCode3 = arrowType3.hashCode();

    assertEquals(hashCode1, hashCode2);
    assertNotEquals(hashCode1, hashCode3);

    /**
     * Testing the edge case where a new ArrowType is created with the same
     * parameters and return type as an existing one.
     */
    ArrowType arrowType4 = new ArrowType(registry, null, intType);
    ArrowType arrowType5 = new ArrowType(registry, null, intType);

    assertEquals(hashCode1, hashCode4);
    assertEquals(hashCode1, hashCode5);
  }

  @Test
  public void testToString() {
    JSType intType = registry.createPrimitiveType(JSTypeNative.INT);
    JSType stringType = registry.createPrimitiveType(JSTypeNative.STRING);

    ArrowType arrowType1 = new ArrowType(registry, null, intType);
    ArrowType arrowType2 = new ArrowType(registry, null, intType);
    ArrowType arrowType3 = new ArrowType(registry, null, stringType);

    assertEquals("() -> int", arrowType1.toString());
    assertEquals("() -> int", arrowType2.toString());
    assertEquals("() -> string", arrowType3.toString());

    Node paramNode = registry.createIdentifier("p");
    paramNode.setJSType(intType);
    ArrowType arrowType4 = new ArrowType(registry, Node.newList(paramNode), intType);
    assertEquals("(int) -> int", arrowType4.toString());
  }

  @Test
  public void testIsSubtype() {
    JSType intType = registry.createPrimitiveType(JSTypeNative.INT);
    JSType stringType = registry.createPrimitiveType(JSTypeNative.STRING);

    ArrowType arrowType1 = new ArrowType(registry, null, intType);
    ArrowType arrowType2 = new ArrowType(registry, null, intType);
    ArrowType arrowType3 = new ArrowType(registry, null, stringType);
    ArrowType arrowType4 = new ArrowType(registry, Node.newList(registry.createIdentifier("s")), intType);

    // Testing the ArrowType with no parameters
    assertTrue(arrowType1.isSubtype(arrowType2));
    assertTrue(arrowType1.isSubtype(arrowType1));
    assertFalse(arrowType1.isSubtype(arrowType3));
    assertFalse(arrowType3.isSubtype(arrowType1));

    // Testing the ArrowType with one parameter
    Node paramNode = registry.createIdentifier("p");
    JSType paramType = registry.createPrimitiveType(JSTypeNative.INT);
    paramNode.setJSType(paramType);
    ArrowType arrowType5 = new ArrowType(registry, Node.newList(paramNode), intType);

    assertTrue(arrowType1.isSubtype(arrowType5));
    assertTrue(arrowType5.isSubtype(arrowType1));

    // Testing the ArrowType with multiple parameters
    ArrowType arrowType6 = new ArrowType(registry, Node.newList(paramNode, registry.createIdentifier("s")), intType);
    ArrowType arrowType7 = new ArrowType(registry, Node.newList(registry.createIdentifier("s"), paramNode), intType);

    assertFalse(arrowType1.isSubtype(arrowType6));
    assertFalse(arrowType6.isSubtype(arrowType1));
    assertTrue(arrowType5.isSubtype(arrowType6));
    assertTrue(arrowType6.isSubtype(arrowType5));
    assertTrue(arrowType5.isSubtype(arrowType7));
    assertTrue(arrowType7.isSubtype(arrowType5));

    // Testing the ArrowType with a variable number of arguments
    ArrowType arrowType8 = new ArrowType(registry, null, intType, true);
    ArrowType arrowType9 = new ArrowType(registry, Node.newList(registry.createIdentifier("p1"), registry.createIdentifier("p2")), intType);

    assertFalse(arrowType1.isSubtype(arrowType8));
    assertFalse(arrowType8.isSubtype(arrowType1));
    assertFalse(arrowType8.isSubtype(arrowType9));
    assertFalse(arrowType9.isSubtype(arrowType8));
  }

  @Test
  public void testHasEqualParameters() {
    JSType intType = registry.createPrimitiveType(JSTypeNative.INT);
    JSType stringType = registry.createPrimitiveType(JSTypeNative.STRING);

    ArrowType arrowType1 = new ArrowType(registry, null, intType);
    ArrowType arrowType2 = new ArrowType(registry, null, intType);
    ArrowType arrowType3 = new ArrowType(registry, null, stringType);
    ArrowType arrowType4 = new ArrowType(registry, Node.newList(registry.createIdentifier("s"), registry.createIdentifier("p")), intType);

    assertTrue(arrowType1.hasEqualParameters(arrowType2));
    assertTrue(arrowType1.hasEqualParameters(arrowType1));
    assertFalse(arrowType1.hasEqualParameters(arrowType3));
    assertFalse(arrowType3.hasEqualParameters(arrowType1));
    assertFalse(arrowType1.hasEqualParameters(arrowType4));
    assertFalse(arrowType4.hasEqualParameters(arrowType1));

    Node paramNode1 = registry.createIdentifier("p1");
    paramNode1.setJSType(intType);
    Node paramNode2 = registry.createIdentifier("p2");
    paramNode2.setJSType(intType);
    Node paramNode3 = registry.createIdentifier("p3");
    ArrowType arrowType5 = new ArrowType(registry, Node.newList(paramNode1, paramNode2), intType);
    ArrowType arrowType6 = new ArrowType(registry, Node.newList(paramNode1, paramNode3), intType);

    assertFalse(arrowType5.hasEqualParameters(arrowType6));
    assertFalse(arrowType6.hasEqualParameters(arrowType5));
  }

  @Test
  public void testIsEquivalentTo() {
    JSType intType = registry.createPrimitiveType(JSTypeNative.INT);
    JSType stringType = registry.createPrimitiveType(JSTypeNative.STRING);

    ArrowType arrowType1 = new ArrowType(registry, null, intType);
    ArrowType arrowType2 = new ArrowType(registry, null, intType);
    ArrowType arrowType3 = new ArrowType(registry, null, stringType);
    ArrowType arrowType4 = new ArrowType(registry, Node.newList(registry.createIdentifier("s"), registry.createIdentifier("p")), intType);

    assertTrue(arrowType1.isEquivalentTo(arrowType1));
    assertTrue(arrowType1.isEquivalentTo(arrowType2));
    assertFalse(arrowType1.isEquivalentTo(arrowType3));
    assertFalse(arrowType3.isEquivalentTo(arrowType1));
    assertFalse(arrowType1.isEquivalentTo(arrowType4));
    assertFalse(arrowType4.isEquivalentTo(arrowType1));

    Node paramNode1 = registry.createIdentifier("p1");
    paramNode1.setJSType(intType);
    Node paramNode2 = registry.createIdentifier("p2");
    paramNode2.setJSType(intType);
    Node paramNode3 = registry.createIdentifier("p3");
    ArrowType arrowType5 = new ArrowType(registry, Node.newList(paramNode1, paramNode2), intType);
    ArrowType arrowType6 = new ArrowType(registry, Node.newList(paramNode1, paramNode3), intType);

    assertFalse(arrowType5.isEquivalentTo(arrowType6));
    assertFalse(arrowType6.isEquivalentTo(arrowType5));
  }

  @Test
  public void testVisit() {
    JSTypeRegistry registry = new JSTypeRegistry();
    JSTypeVisitor visitor = new JSTypeVisitor() {
      private int count = 0;

      @Override
      public <T> T visitArrowType(ArrowType type, T arg) {
        count++;
        return arg;
      }
    };

    JSType intType = registry.createPrimitiveType(JSTypeNative.INT);
    JSType arrowType = new ArrowType(registry, null, intType);

    arrowType.visit(visitor, null);

    assertEquals(1, count);
  }

  @Test
  public void testResolve() {
    JSType intType = registry.createPrimitiveType(JSTypeNative.INT);
    JSType stringType = registry.createPrimitiveType(JSTypeNative.STRING);

    ArrowType arrowType1 = new ArrowType(registry, null, intType);
    Node paramNode1 = registry.createIdentifier("p1");
    paramNode1.setJSType(registry.createPrimitiveType(JSTypeNative.INT));
    ArrowType arrowType2 = new ArrowType(registry, Node.newList(paramNode1), intType);

    ArrowType resolvedArrowType1 = arrowType1.resolve(t, scope);
    assertEquals(intType, resolvedArrowType1.returnType);
    assertNull(resolvedArrowType1.parameters);
    assertEquals(false, resolvedArrowType1.returnTypeInferred);

    ArrowType resolvedArrowType2 = arrowType2.resolve(t, scope);
    assertEquals(intType, resolvedArrowType2.returnType);
    assertEquals(intType, resolvedArrowType2.parameters.getFirstChild().getJSType());
    assertEquals(false, resolvedArrowType2.returnTypeInferred);
  }

  @Test
  public void testHasUnknownParamsOrReturn() {
    JSType intType = registry.createPrimitiveType(JSTypeNative.INT);
    ArrowType arrowType1 = new ArrowType(registry, null, intType);
    ArrowType arrowType2 = new ArrowType(registry, null, null);
    ArrowType arrowType3 = new ArrowType(registry, Node.newList(registry.createIdentifier("p")), intType, true);

    assertTrue(arrowType1.hasUnknownParamsOrReturn());
    assertFalse(arrowType2.hasUnknownParamsOrReturn());
    assertTrue(arrowType3.hasUnknownParamsOrReturn());
  }
}