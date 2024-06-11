package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.StaticScope;

public class ArrowTypeTest {

 private static JSTypeRegistry registry;
 private static ErrorReporter errorReporter;

 @BeforeClass
 public static void setUpClass() {
 registry = new JSTypeRegistry();
 errorReporter = new ErrorReporter() {
 @Override
 public void error(String message, Object... args) {
 throw new RuntimeException(message);
 }
 };
 }

 @Test
 public void testConstructor_NullParameters_CreatesArrowTypeWithUnknownTypeParameters() {
 ArrowType arrowType = new ArrowType(registry, null, registry.getNativeType(JSType.VOID));
 assertNull(arrowType.parameters);
 assertNotNull(arrowType.returnType);
 assertTrue(arrowType.returnType.isUnknownType());
 }

 @Test
 public void testConstructor_NullReturnType_CreatesArrowTypeWithUnknownTypeReturn() {
 ArrowType arrowType = new ArrowType(registry, new Node(Node.IDENTIFIER, "param"), null);
 assertNotNull(arrowType.parameters);
 assertNull(arrowType.returnType);
 assertTrue(arrowType.returnTypeInferred);
 }

 @Test
 public void testConstructor_ValidParametersAndReturnType_CreatesArrowTypeWithValidTypes() {
 Node parametersNode = new Node(Node.SEQUENCE, new Node[] {
 new Node(Node.IDENTIFIER, "param1"),
 new Node(Node.IDENTIFIER, "param2")
 });
 ArrowType arrowType = new ArrowType(registry, parametersNode, registry.getIntType(), false);
 assertNotNull(arrowType.parameters);
 assertNotNull(arrowType.returnType);
 assertFalse(arrowType.returnTypeInferred);
 }

 @Test
 public void testIsSubtype_SubtypesMatch_ReturnsTrue() {
 ArrowType arrowType1 = new ArrowType(registry, new Node(Node.SEQUENCE, new Node[] {
 new Node(Node.IDENTIFIER, "param1"),
 new Node(Node.IDENTIFIER, "param2")
 }), registry.getNativeType(JSType.NUMBER));
 ArrowType arrowType2 = new ArrowType(registry, new Node(Node.SEQUENCE, new Node[] {
 new Node(Node.IDENTIFIER, "param1"),
 new Node(Node.IDENTIFIER, "param2")
 }), registry.getNativeType(JSType.STRING));
 assertTrue(arrowType1.isSubtype(arrowType2));
 }

 @Test
 public void testIsSubtype_DifferentReturnTypes_ReturnsFalse() {
 ArrowType arrowType1 = new ArrowType(registry, new Node(Node.SEQUENCE, new Node[] {
 new Node(Node.IDENTIFIER, "param1"),
 new Node(Node.IDENTIFIER, "param2")
 }), registry.getNativeType(JSType.NUMBER));
 ArrowType arrowType2 = new ArrowType(registry, new Node(Node.SEQUENCE, new Node[] {
 new Node(Node.IDENTIFIER, "param1"),
 new Node(Node.IDENTIFIER, "param2")
 }), registry.getNativeType(JSType.STRING));
 assertFalse(arrowType1.isSubtype(arrowType2));
 }

 @Test
 public void testIsSubtype_DifferentParameterTypes_ReturnsFalse() {
 ArrowType arrowType1 = new ArrowType(registry, new Node(Node.SEQUENCE, new Node[] {
 new Node(Node.IDENTIFIER, "number"),
 new Node(Node.IDENTIFIER, "number")
 }), registry.getNativeType(JSType.NUMBER));
 ArrowType arrowType2 = new ArrowType(registry, new Node(Node.SEQUENCE, new Node[] {
 new Node(Node.IDENTIFIER, "string"),
 new Node(Node.IDENTIFIER, "number")
 }), registry.getNativeType(JSType.NUMBER));
 assertFalse(arrowType1.isSubtype(arrowType2));
 }

 @Test
 public void testHasEqualParameters_EqualTypes_ReturnsTrue() {
 ArrowType arrowType1 = new ArrowType(registry, new Node(Node.SEQUENCE, new Node[] {
 new Node(Node.IDENTIFIER, "number"),
 new Node(Node.IDENTIFIER, "number")
 }), registry.getNativeType(JSType.NUMBER));
 ArrowType arrowType2 = new ArrowType(registry, new Node(Node.SEQUENCE, new Node[] {
 new Node(Node.IDENTIFIER, "number"),
 new Node(Node.IDENTIFIER, "number")
 }), registry.getNativeType(JSType.NUMBER));
 assertTrue(arrowType1.hasEqualParameters(arrowType2));
 }

 @Test
 public void testHasEqualParameters_DifferentTypes_ReturnsFalse() {
 ArrowType arrowType1 = new ArrowType(registry, new Node(Node.SEQUENCE, new Node[] {
 new Node(Node.IDENTIFIER, "number"),
 new Node(Node.IDENTIFIER, "number")
 }), registry.getNativeType(JSType.NUMBER));
 ArrowType arrowType2 = new ArrowType(registry, new Node(Node.SEQUENCE, new Node[] {
 new Node(Node.IDENTIFIER, "string"),
 new Node(Node.IDENTIFIER, "number")
 }), registry.getNativeType(JSType.NUMBER));
 assertFalse(arrowType1.hasEqualParameters(arrowType2));
 }

 @Test
 public void testIsEquivalentTo_EqualTypes_ReturnsTrue() {
 ArrowType arrowType1 = new ArrowType(registry, new Node(Node.SEQUENCE, new Node[] {
 new Node(Node.IDENTIFIER, "number"),
 new Node(Node.IDENTIFIER, "number")
 }), registry.getNativeType(JSType.NUMBER));
 ArrowType arrowType2 = new ArrowType(registry, new Node(Node.SEQUENCE, new Node[] {
 new Node(Node.IDENTIFIER, "number"),
 new Node(Node.IDENTIFIER, "number")
 }), registry.getNativeType(JSType.NUMBER));
 assertTrue(arrowType1.isEquivalentTo(arrowType2));
 }

 @Test
 public void testIsEquivalentTo_DifferentTypes_ReturnsFalse() {
 ArrowType arrowType1 = new ArrowType(registry, new Node(Node.SEQUENCE, new Node[] {
 new Node(Node.IDENTIFIER, "number"),
 new Node(Node.IDENTIFIER, "number")
 }), registry.getNativeType(JSType.NUMBER));
 ArrowType arrowType2 = new ArrowType(registry, new Node(Node.SEQUENCE, new Node[] {
 new Node(Node.IDENTIFIER, "string"),
 new Node(Node.IDENTIFIER, "number")
 }), registry.getNativeType(JSType.NUMBER));
 assertFalse(arrowType1.isEquivalentTo(arrowType2));
 }

 @Test
 public void testHashCode_EqualTypes_ReturnsEqualHashCodes() {
 ArrowType arrowType1 = new ArrowType(registry, new Node(Node.SEQUENCE, new Node[] {
 new Node(Node.IDENTIFIER, "number"),
 new Node(Node.IDENTIFIER, "number")
 }), registry.getNativeType(JSType.NUMBER));
 ArrowType arrowType2 = new ArrowType(registry, new Node(Node.SEQUENCE, new Node[] {
 new Node(Node.IDENTIFIER, "number"),
 new Node(Node.IDENTIFIER, "number")
 }), registry.getNativeType(JSType.NUMBER));
 assertEquals(arrowType1.hashCode(), arrowType2.hashCode());
 }

 @Test
 public void testHashCode_DifferentTypes_ReturnsDifferentHashCodes() {
 ArrowType arrowType1 = new ArrowType(registry, new Node(Node.SEQUENCE, new Node[] {
 new Node(Node.IDENTIFIER, "number"),
 new Node(Node.IDENTIFIER, "number")
 }), registry.getNativeType(JSType.NUMBER));
 ArrowType arrowType2 = new ArrowType(registry, new Node(Node.SEQUENCE, new Node[] {
 new Node(Node.IDENTIFIER, "string"),
 new Node(Node.IDENTIFIER, "number")
 }), registry.getNativeType(JSType.NUMBER));
 assertNotEquals(arrowType1.hashCode(), arrowType2.hashCode());
 }

 @Test
 public void testGetLeastSupertype_ExceptionThrown() {
 try {
 ArrowType arrowType = new ArrowType(registry, new Node(Node.IDENTIFIER, "ArrowType"), registry.getNativeType(JSType.NUMBER));
 ArrowType leastSupertype = arrowType.getLeastSupertype(new ArrowType(registry, new Node(Node.SEQUENCE, new Node[] {
 new Node(Node.IDENTIFIER, "param1")
 }), registry.getNativeType(JSType.STRING)));
 fail("Expected UnsupportedOperationException to be thrown");
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @Test
 public void testGetGreatestSubtype_ExceptionThrown() {
 try {
 ArrowType arrowType = new ArrowType(registry, new Node(Node.SEQUENCE, new Node[] {
 new Node(Node.IDENTIFIER, "param1")
 }), registry.getNativeType(JSType.STRING));
 ArrowType greatestSubtype = arrowType.getGreatestSubtype(new ArrowType(registry, new Node(Node.SEQUENCE, new Node[] {
 new Node(Node.IDENTIFIER, "param1"),
 new Node(Node.IDENTIFIER, "param2")
 }), registry.getNativeType(JSType.NUMBER)));
 fail("Expected UnsupportedOperationException to be thrown");
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @Test
 public void testToStringHelper_ValidTypes_ReturnsValidStringRepresentation() {
 ArrowType arrowType = new ArrowType(registry, new Node(Node.SEQUENCE, new Node[] {
 new Node(Node.IDENTIFIER, "param1"),
 new Node(Node.IDENTIFIER, "param2")
 }), registry.getNativeType(JSType.NUMBER));
 assertEquals("ArrowType[parameters=[param1, param2], returnType=Number]", arrowType.toStringHelper(false));
 }

 @Test
 public void testResolveInternal_ValidTypes_ReturnsResolvedTypes() {
 ArrowType arrowType = new ArrowType(registry, new Node(Node.SEQUENCE, new Node[] {
 new Node(Node.IDENTIFIER, "param1"),
 new Node(Node.IDENTIFIER, "param2")
 }), registry.getNativeType(JSType.NUMBER));
 StaticScope<JSType> scope = new StaticScope<>();
 JSType resolvedArrowType = arrowType.resolveInternal(errorReporter, scope);
 assertNotNull(resolvedArrowType.parameters);
 assertNotNull(resolvedArrowType.returnType);
 assertFalse(resolvedArrowType.returnTypeInferred);
 }

 @Test
 public void testHasUnknownParamsOrReturn_UnknownTypes_ReturnsTrue() {
 ArrowType arrowType = new ArrowType(registry, new Node(Node.SEQUENCE, new Node[] {
 new Node(Node.IDENTIFIER, "param1"),
 new Node(Node.IDENTIFIER, "param2")
 }), null);
 assertFalse(arrowType.hasUnknownParamsOrReturn());

 ArrowType arrowTypeWithUnknownReturnType = new ArrowType(registry, new Node(Node.SEQUENCE, new Node[] {
 new Node(Node.IDENTIFIER, "param1"),
 new Node(Node.IDENTIFIER, "param2")
 }), null);
 arrowTypeWithUnknownReturnType.returnType = null;
 assertTrue(arrowTypeWithUnknownReturnType.hasUnknownParamsOrReturn());
 }
}