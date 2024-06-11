package com.google.javascript.rhino.jstype;

import static org.junit.Assert.*;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.JSTypeRegistry;

public class ArrowTypeTest {

 private JSTypeRegistry registry;
 private ErrorReporter errorReporter;
 private StaticScope<JSType> scope;

 @Before
 public void setUp() {
 registry = new JSTypeRegistry();
 errorReporter = new ErrorReporter() {};
 scope = new StaticScope<>(registry);
 }

 @Test
 public void testArrowTypeConstructorWithNullParametersAndReturnType() {
 ArrowType arrowType = new ArrowType(registry, null, null);
 assertNull(arrowType.parameters);
 assertEquals(JSTypeNative.UNKNOWN_TYPE, arrowType.returnType);
 assertFalse(arrowType.returnTypeInferred);
 }

 @Test
 public void testArrowTypeConstructorWithParametersAndReturnType() {
 Node parametersNode = registry.createParametersWithVarArgs(JSTypeNative.INT_TYPE);
 ArrowType arrowType = new ArrowType(registry, parametersNode, JSTypeNative.STRING_TYPE);
 assertEquals(parametersNode, arrowType.parameters);
 assertEquals(JSTypeNative.STRING_TYPE, arrowType.returnType);
 assertFalse(arrowType.returnTypeInferred);
 }

 @Test
 public void testArrowTypeConstructorWithParametersAndInferredReturnType() {
 Node parametersNode = registry.createParametersWithVarArgs(JSTypeNative.INT_TYPE);
 ArrowType arrowType = new ArrowType(registry, parametersNode, null, true);
 assertEquals(parametersNode, arrowType.parameters);
 assertEquals(JSTypeNative.UNKNOWN_TYPE, arrowType.returnType);
 assertTrue(arrowType.returnTypeInferred);
 }

 @Test
 public void testArrowTypeIsSubtypeWithEqualTypes() {
 ArrowType arrowType1 = new ArrowType(registry, null, JSTypeNative.INT_TYPE);
 ArrowType arrowType2 = new ArrowType(registry, null, JSTypeNative.INT_TYPE);
 assertTrue(arrowType1.isSubtype(arrowType2));
 }

 @Test
 public void testArrowTypeIsSubtypeWithInferredReturnType() {
 ArrowType arrowType1 = new ArrowType(registry, null, JSTypeNative.INT_TYPE);
 ArrowType arrowType2 = new ArrowType(registry, null, null, true);
 assertTrue(arrowType1.isSubtype(arrowType2));
 }

 @Test
 public void testArrowTypeIsSubtypeWithIncompatibleReturnTypes() {
 ArrowType arrowType1 = new ArrowType(registry, null, JSTypeNative.INT_TYPE);
 ArrowType arrowType2 = new ArrowType(registry, null, JSTypeNative.STRING_TYPE);
 assertFalse(arrowType1.isSubtype(arrowType2));
 }

 @Test
 public void testArrowTypeIsSubtypeWithIncompatibleParameters() {
 ArrowType arrowType1 = new ArrowType(registry, null, JSTypeNative.INT_TYPE);
 ArrowType arrowType2 = new ArrowType(registry, Node.createIntLiteral(3), JSTypeNative.INT_TYPE);
 assertFalse(arrowType1.isSubtype(arrowType2));
 }

 @Test
 public void testArrowTypeHasEqualParameters() {
 ArrowType arrowType1 = new ArrowType(registry, null, JSTypeNative.INT_TYPE);
 ArrowType arrowType2 = new ArrowType(registry, null, JSTypeNative.INT_TYPE);
 assertTrue(arrowType1.hasEqualParameters(arrowType2));
 }

 @Test
 public void testArrowTypeEquivalentToEqualTypes() {
 ArrowType arrowType1 = new ArrowType(registry, null, JSTypeNative.INT_TYPE);
 ArrowType arrowType2 = new ArrowType(registry, null, JSTypeNative.INT_TYPE);
 assertTrue(arrowType1.isEquivalentTo(arrowType2));
 }

 @Test
 public void testArrowTypeEquivalentToDifferentReturnTypes() {
 ArrowType arrowType1 = new ArrowType(registry, null, JSTypeNative.INT_TYPE);
 ArrowType arrowType2 = new ArrowType(registry, null, JSTypeNative.STRING_TYPE);
 assertFalse(arrowType1.isEquivalentTo(arrowType2));
 }

 @Test
 public void testArrowTypeEquivalentToDifferentParameters() {
 ArrowType arrowType1 = new ArrowType(registry, Node.createIntLiteral(3), JSTypeNative.INT_TYPE);
 ArrowType arrowType2 = new ArrowType(registry, Node.createIntLiteral(4), JSTypeNative.INT_TYPE);
 assertFalse(arrowType1.isEquivalentTo(arrowType2));
 }

 @Test
 public void testArrowTypeEquivalentToSelf() {
 ArrowType arrowType = new ArrowType(registry, null, JSTypeNative.INT_TYPE);
 assertTrue(arrowType.isEquivalentTo(arrowType));
 }

 @Test
 public void testArrowTypeHashCode() {
 ArrowType arrowType1 = new ArrowType(registry, null, JSTypeNative.INT_TYPE);
 ArrowType arrowType2 = new ArrowType(registry, Node.createIntLiteral(3), JSTypeNative.INT_TYPE);
 ArrowType arrowType3 = new ArrowType(registry, null, JSTypeNative.STRING_TYPE);
 assertEquals(hashCode(arrowType1), hashCode(arrowType1));
 assertEquals(hashCode(arrowType1), hashCode(arrowType2));
 assertNotEquals(hashCode(arrowType1), hashCode(arrowType3));
 }

 private int hashCode(ArrowType arrowType) {
 if (arrowType.returnType != null) {
 return arrowType.returnType.hashCode() + 1;
 }
 if (arrowType.parameters != null) {
 Node param = arrowType.parameters.getFirstChild();
 int hashCode = 0;
 while (param != null) {
 JSType paramType = param.getJSType();
 if (paramType != null) {
 hashCode += paramType.hashCode();
 }
 param = param.getNext();
 }
 return hashCode;
 }
 return 0;
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetLeastSupertype() {
 ArrowType arrowType = new ArrowType(registry, null, JSTypeNative.INT_TYPE);
 arrowType.getLeastSupertype(JSTypeNative.STRING_TYPE);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetGreatestSubtype() {
 ArrowType arrowType = new ArrowType(registry, null, JSTypeNative.INT_TYPE);
 arrowType.getGreatestSubtype(JSTypeNative.STRING_TYPE);
 }

 @Test
 public void testArrowTypeToStringHelper() {
 ArrowType arrowType = new ArrowType(registry, null, JSTypeNative.INT_TYPE);
 assertEquals("function()", arrowType.toStringHelper(false));
 }

 @Test
 public void testArrowTypeResolveInternalWithNullReturnType() {
 ArrowType arrowType = new ArrowType(registry, null, null);
 arrowType.resolveInternal(errorReporter, scope);
 assertNull(arrowType.returnType);
 }

 @Test
 public void testArrowTypeResolveInternalWithSpecifiedReturnType() {
 ArrowType arrowType = new ArrowType(registry, null, Node.createIntLiteral(3));
 arrowType.resolveInternal(errorReporter, scope);
 assertEquals(Node.createIntLiteral(3), arrowType.returnType);
 }

 @Test
 public void testArrowTypeResolveInternalWithInferredReturnType() {
 ArrowType arrowType = new ArrowType(registry, null, null, true);
 arrowType.resolveInternal(errorReporter, scope);
 assertEquals(JSTypeNative.UNKNOWN_TYPE, arrowType.returnType);
 }

 @Test
 public void testArrowTypeResolveInternalWithParameters() {
 ArrowType arrowType = new ArrowType(registry, Node.createIntLiteral(3), null);
 arrowType.resolveInternal(errorReporter, scope);
 assertEquals(Node.createIntLiteral(3), arrowType.parameters.getFirstChild().getJSType());
 }

 @Test
 public void testArrowTypeResolveInternalWithInferredParameters() {
 ArrowType arrowType = new ArrowType(registry, null, null, true);
 arrowType.resolveInternal(errorReporter, scope);
 assertEquals(JSTypeNative.UNKNOWN_TYPE, arrowType.parameters.getFirstChild().getJSType());
 }

 @Test
 public void testArrowTypeHasUnknownParamsOrReturn() {
 ArrowType arrowType1 = new ArrowType(registry, null, null);
 assertTrue(arrowType1.hasUnknownParamsOrReturn());
 ArrowType arrowType2 = new ArrowType(registry, null, Node.createIntLiteral(3));
 assertFalse(arrowType2.hasUnknownParamsOrReturn());
 }
}