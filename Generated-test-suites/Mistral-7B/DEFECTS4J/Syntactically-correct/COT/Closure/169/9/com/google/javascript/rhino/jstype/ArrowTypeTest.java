import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.StaticScope;
import com.google.javascript.rhino.testing.AssertErrorReporter;

import org.junit.Before;
import org.junit.Test;

public class ArrowTypeTest {
  private ErrorReporter t;
  private StaticScope<JSType> scope;

  @Before
  public void setUp() {
    t = new AssertErrorReporter();
    scope = new StaticScope<>();
  }

  @Test
  public void testArrowTypeConstructorWithParametersAndReturnType() {
    Node parametersNode = Node.newList();
    Node firstParamNode = Node.newIdentifier("param1");
    Node secondParamNode = Node.newIdentifier("param2");
    parametersNode.addChild(firstParamNode);
    parametersNode.addChild(secondParamNode);

    JSType returnType = JSTypeNative.INT;

    ArrowType arrowType = new ArrowType(
        new JSTypeRegistry(), parametersNode, returnType);

    assertEquals(ArrowType.class, arrowType.getClass());
    assertEquals(parametersNode, arrowType.parameters);
    assertEquals(returnType, arrowType.returnType);
    assertFalse(arrowType.returnTypeInferred);
  }

  @Test
  public void testArrowTypeConstructorWithOnlyReturnType() {
    ArrowType arrowType = new ArrowType(
        new JSTypeRegistry(), null, JSTypeNative.INT);

    assertEquals(ArrowType.class, arrowType.getClass());
    assertNull(arrowType.parameters);
    assertEquals(JSTypeNative.INT, arrowType.returnType);
    assertTrue(arrowType.returnTypeInferred);
  }

  @Test
  public void testIsSubtypeArrowTypeWithSameTypes() {
    JSType returnType1 = JSTypeNative.INT;
    JSType returnType2 = JSTypeNative.INT;

    ArrowType arrowType1 = new ArrowType(
        new JSTypeRegistry(), null, returnType1);
    ArrowType arrowType2 = new ArrowType(
        new JSTypeRegistry(), null, returnType2);

    assertTrue(arrowType1.isSubtype(arrowType2));
    assertTrue(arrowType2.isSubtype(arrowType1));
  }

  @Test
  public void testIsSubtypeArrowTypeDifferentReturnTypes() {
    JSType returnType1 = JSTypeNative.INT;
    JSType returnType2 = JSTypeNative.STRING;

    ArrowType arrowType1 = new ArrowType(
        new JSTypeRegistry(), null, returnType1);
    ArrowType arrowType2 = new ArrowType(
        new JSTypeRegistry(), null, returnType2);

    assertFalse(arrowType1.isSubtype(arrowType2));
    assertFalse(arrowType2.isSubtype(arrowType1));
  }

  @Test
  public void testIsSubtypeArrowTypeWithDifferentParameters() {
    Node parametersNode1 = Node.newList();
    Node firstParamNode1 = Node.newIdentifier("param1");
    Node secondParamNode1 = Node.newIdentifier("param2");
    parametersNode1.addChild(firstParamNode1);
    parametersNode1.addChild(secondParamNode1);

    Node parametersNode2 = Node.newList();
    Node firstParamNode2 = Node.newIdentifier("param2");
    Node secondParamNode2 = Node.newIdentifier("param3");
    parametersNode2.addChild(firstParamNode2);
    parametersNode2.addChild(secondParamNode2);

    JSType returnType = JSTypeNative.INT;

    ArrowType arrowType1 = new ArrowType(
        new JSTypeRegistry(), parametersNode1, returnType);
    ArrowType arrowType2 = new ArrowType(
        new JSTypeRegistry(), parametersNode2, returnType);

    assertFalse(arrowType1.isSubtype(arrowType2));
    assertFalse(arrowType2.isSubtype(arrowType1));
  }

  @Test
  public void testIsSubtypeArrowTypeWithSameParametersButDifferentReturnTypes() {
    Node parametersNode = Node.newList();
    Node firstParamNode = Node.newIdentifier("param1");
    Node secondParamNode = Node.newIdentifier("param2");
    parametersNode.addChild(firstParamNode);
    parametersNode.addChild(secondParamNode);

    JSType returnType1 = JSTypeNative.INT;
    JSType returnType2 = JSTypeNative.STRING;

    ArrowType arrowType1 = new ArrowType(
        new JSTypeRegistry(), parametersNode, returnType1);
    ArrowType arrowType2 = new ArrowType(
        new JSTypeRegistry(), parametersNode, returnType2);

    assertFalse(arrowType1.isSubtype(arrowType2));
    assertFalse(arrowType2.isSubtype(arrowType1));
  }

  @Test
  public void testIsSubtypeArrowTypeWithInferredReturnType() {
    Node parametersNode = Node.newList();
    Node firstParamNode = Node.newIdentifier("param1");
    Node secondParamNode = Node.newIdentifier("param2");
    parametersNode.addChild(firstParamNode);
    parametersNode.addChild(secondParamNode);

    JSType returnType = null;

    ArrowType arrowType1 = new ArrowType(
        new JSTypeRegistry(), parametersNode, returnType);
    ArrowType arrowType2 = new ArrowType(
        new JSTypeRegistry(), Node.newList(), JSTypeNative.STRING);

    assertFalse(arrowType1.isSubtype(arrowType2));
    assertFalse(arrowType2.isSubtype(arrowType1));
  }

  @Test
  public void testIsSubtypeArrowTypeWithOptionalArguments() {
    Node parametersNode1 = Node.newList();
    Node firstParamNode1 = Node.newIdentifier("param1");
    Node secondParamNode1 = Node.newIdentifier("param2");
    parametersNode1.addChild(firstParamNode1);
    parametersNode1.addChild(secondParamNode1);

    Node parametersNode2 = Node.newList();
    Node firstParamNode2 = Node.newIdentifier("param1");
    Node secondParamNode2 = Node.newIdentifier("param2");
    secondParamNode2.setOptionalArg(true);
    parametersNode2.addChild(firstParamNode2);
    parametersNode2.addChild(secondParamNode2);

    JSType returnType = JSTypeNative.INT;

    ArrowType arrowType1 = new ArrowType(
        new JSTypeRegistry(), parametersNode1, returnType);
    ArrowType arrowType2 = new ArrowType(
        new JSTypeRegistry(), parametersNode2, returnType);

    // arrowType1 is a subtype of arrowType2, because arrowType2 accepts
    // any value for the second argument.
    assertTrue(arrowType1.isSubtype(arrowType2));
    assertFalse(arrowType2.isSubtype(arrowType1));
  }

  @Test
  public void testHasEqualParameters() {
    Node parametersNode1 = Node.newList();
    Node firstParamNode1 = Node.newIdentifier("param1");
    Node secondParamNode1 = Node.newIdentifier("param2");
    parametersNode1.addChild(firstParamNode1);
    parametersNode1.addChild(secondParamNode1);

    Node parametersNode2 = Node.newList();
    Node firstParamNode2 = Node.newIdentifier("param1");
    Node secondParamNode2 = Node.newIdentifier("param2");
    parametersNode2.addChild(firstParamNode2);
    parametersNode2.addChild(secondParamNode2);

    ArrowType arrowType1 = new ArrowType(
        new JSTypeRegistry(), parametersNode1, null);
    ArrowType arrowType2 = new ArrowType(
        new JSTypeRegistry(), parametersNode2, null);

    assertTrue(arrowType1.hasEqualParameters(arrowType2, true));
    assertTrue(arrowType2.hasEqualParameters(arrowType1, true));
  }

  @Test
  public void testHasEqualParametersDifferentReturnType() {
    Node parametersNode1 = Node.newList();
    Node firstParamNode1 = Node.newIdentifier("param1");
    Node secondParamNode1 = Node.newIdentifier("param2");
    parametersNode1.addChild(firstParamNode1);
    parametersNode1.addChild(secondParamNode1);

    Node parametersNode2 = Node.newList();
    Node firstParamNode2 = Node.newIdentifier("param1");
    Node secondParamNode2 = Node.newIdentifier("param2");
    parametersNode2.addChild(firstParamNode2);
    parametersNode2.addChild(secondParamNode2);

    JSType returnType1 = JSTypeNative.INT;
    JSType returnType2 = JSTypeNative.STRING;

    ArrowType arrowType1 = new ArrowType(
        new JSTypeRegistry(), parametersNode1, returnType1);
    ArrowType arrowType2 = new ArrowType(
        new JSTypeRegistry(), parametersNode2, returnType2);

    assertFalse(arrowType1.hasEqualParameters(arrowType2, true));
    assertFalse(arrowType2.hasEqualParameters(arrowType1, true));
  }

  @Test
  public void testHasEqualParametersNullParameters() {
    ArrowType arrowType1 = new ArrowType(
        new JSTypeRegistry(), null, null);
    ArrowType arrowType2 = new ArrowType(
        new JSTypeRegistry(), null, null);

    assertTrue(arrowType1.hasEqualParameters(arrowType2, true));
    assertTrue(arrowType2.hasEqualParameters(arrowType1, true));
  }

  @Test
  public void testHashCode() {
    JSType returnType = JSTypeNative.INT;

    Node parametersNode1 = Node.newList();
    Node firstParamNode1 = Node.newIdentifier("param1");
    Node secondParamNode1 = Node.newIdentifier("param2");
    parametersNode1.addChild(firstParamNode1);
    parametersNode1.addChild(secondParamNode1);

    Node parametersNode2 = Node.newList();
    Node firstParamNode2 = Node.newIdentifier("param1");
    Node secondParamNode2 = Node.newIdentifier("param2");
    parametersNode2.addChild(firstParamNode2);
    parametersNode2.addChild(secondParamNode2);

    ArrowType arrowType1 = new ArrowType(
        new JSTypeRegistry(), parametersNode1, returnType);
    ArrowType arrowType2 = new ArrowType(
        new JSTypeRegistry(), parametersNode2, returnType);

    int hashCode1 = arrowType1.hashCode();
    int hashCode2 = arrowType2.hashCode();

    // The hash codes should be different, because the objects have
    // different parameter lists.
    assertNotSame(hashCode1, hashCode2);

    // The hash codes should be equal, if the objects have the same
    // parameter lists.
    ArrowType arrowType3 = new ArrowType(
        new JSTypeRegistry(), parametersNode1, returnType);
    assertEquals(hashCode1, arrowType3.hashCode());

    // The hash code should be equal, if the objects have no parameter list.
    ArrowType arrowType4 = new ArrowType(
        new JSTypeRegistry(), null, returnType);
    assertEquals(hashCode1, arrowType4.hashCode());
  }

  @Test
  public void testGetLeastSupertype() {
    throw new UnsupportedOperationException(
        "ArrowType does not implement getLeastSupertype()");
  }

  @Test
  public void testGetGreatestSubtype() {
    throw new UnsupportedOperationException(
        "ArrowType does not implement getGreatestSubtype()");
  }

  @Test
  public void testTestForEquality() {
    throw new UnsupportedOperationException(
        "ArrowType does not implement testForEquality()");
  }

  @Test
  public void testVisit() {
    throw new UnsupportedOperationException(
        "ArrowType does not implement visit()");
  }

  @Test
  public void testGetPossibleToBooleanOutcomes() {
    throw new UnsupportedOperationException(
        "ArrowType does not implement getPossibleToBooleanOutcomes()");
  }

  @Test
  public void testResolveInternal() {
    Node parametersNode = Node.newList();
    Node firstParamNode = Node.newIdentifier("param1");
    Node secondParamNode = Node.newIdentifier("param2");
    parametersNode.addChild(firstParamNode);
    parametersNode.addChild(secondParamNode);

    JSType returnType = JSTypeNative.INT;

    ArrowType arrowType = new ArrowType(
        new JSTypeRegistry(), parametersNode, returnType);

    JSType resolvedArrowType = arrowType.resolve(t, scope);

    assertEquals(ArrowType.class, resolvedArrowType.getClass());
    assertSame(returnType, resolvedArrowType.returnType);

    JSType paramType1 = firstParamNode.getJSType();
    JSType paramType2 = secondParamNode.getJSType();

    assertEquals(JSTypeRegistry.createUnknownType(), paramType1);
    assertEquals(JSTypeRegistry.createUnknownType(), paramType2);

    assertEquals(arrowType.parameters, resolvedArrowType.parameters);
  }

  @Test
  public void testHasAnyTemplateInternalWithReturnTypeTemplate() {
    JSType returnType = new JSTypeNative.TemplateType(
        Node.newIdentifier("T"), JSTypeRegistry.ANY);

    ArrowType arrowType = new ArrowType(
        new JSTypeRegistry(), null, returnType);

    assertTrue(arrowType.hasAnyTemplateInternal());
  }

  @Test
  public void testHasAnyTemplateInternalWithParameterTypeTemplate() {
    Node parametersNode = Node.newList();
    Node paramNode = Node.newIdentifier("param");
    paramNode.setOptionalArg(true);
    paramNode.setTemplateArg(Node.newIdentifier("T"));
    parametersNode.addChild(paramNode);

    JSType returnType = JSTypeNative.INT;

    ArrowType arrowType = new ArrowType(
        new JSTypeRegistry(), parametersNode, returnType);

    assertTrue(arrowType.hasAnyTemplateInternal());
  }

  @Test
  public void testHasAnyTemplateInternalWithNoTemplate() {
    JSType returnType = JSTypeNative.INT;

    ArrowType arrowType = new ArrowType(
        new JSTypeRegistry(), null, returnType);

    assertFalse(arrowType.hasAnyTemplateInternal());
  }
}