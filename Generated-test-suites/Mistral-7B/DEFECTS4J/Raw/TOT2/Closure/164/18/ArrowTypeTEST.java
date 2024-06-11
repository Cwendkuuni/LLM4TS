import static org.junit.Assert.*;
import static com.google.javascript.rhino.JSType.*;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.types.JSType;
import com.google.javascript.rhino.types.arrow.ArrowType;
import org.junit.Test;

public class ArrowTypeTest {

 // Test constructor with valid parameters and return type
 @Test
 public void testArrowTypeConstructorValid() {
 ArrowType arrowType1 = new ArrowType(new JSTypeRegistry(), null, INT_TYPE);
 ArrowType arrowType2 = new ArrowType(new JSTypeRegistry(), Node.newList(), STRING_TYPE);
 ArrowType arrowType3 = new ArrowType(new JSTypeRegistry(), Node.newList(INT_TYPE),
 BOOL_TYPE);
 ArrowType arrowType4 = new ArrowType(new JSTypeRegistry(), Node.newList(INT_TYPE, INT_TYPE),
 VOID_TYPE);

 assertEquals("ArrowType with null parameters and INT return type", arrowType1,
 new ArrowType(new JSTypeRegistry(), null, INT_TYPE));
 assertEquals("ArrowType with empty list of parameters and STRING return type",
 arrowType2, new ArrowType(new JSTypeRegistry(), Node.EMPTY_LIST, STRING_TYPE));
 assertEquals("ArrowType with INT parameter and BOOL return type", arrowType3,
 new ArrowType(new JSTypeRegistry(), Node.newList(INT_TYPE), BOOL_TYPE));
 assertEquals("ArrowType with INT and INT parameters and VOID return type",
 arrowType4, new ArrowType(new JSTypeRegistry(), Node.newList(INT_TYPE, INT_TYPE),
 VOID_TYPE));
 }

 // Test constructor with invalid return type
 @Test(expected = IllegalArgumentException.class)
 public void testArrowTypeConstructorInvalidReturnType() {
 new ArrowType(new JSTypeRegistry(), Node.newList(), NULL_TYPE);
 }

 // Test constructor with null parameters and non-null return type
 @Test(expected = IllegalArgumentException.class)
 public void testArrowTypeConstructorNullParameters() {
 new ArrowType(new JSTypeRegistry(), null, INT_TYPE);
 }

 // Test isSubtype method with equal ArrowTypes
 @Test
 public void testIsSubtypeArrowTypesEqual() {
 ArrowType arrowType1 = new ArrowType(new JSTypeRegistry(), Node.newList(INT_TYPE),
 INT_TYPE);
 ArrowType arrowType2 = new ArrowType(new JSTypeRegistry(), Node.newList(INT_TYPE),
 INT_TYPE);
 assertTrue("ArrowType1 should be a subtype of ArrowType2", arrowType1.isSubtype(arrowType2));
 }

 // Test isSubtype method with different ArrowTypes
 @Test
 public void testIsSubtypeArrowTypesDifferent() {
 ArrowType arrowType1 = new ArrowType(new JSTypeRegistry(), Node.newList(INT_TYPE),
 INT_TYPE);
 ArrowType arrowType2 = new ArrowType(new JSTypeRegistry(), Node.newList(INT_TYPE, INT_TYPE),
 INT_TYPE);
 assertFalse("ArrowType1 should not be a subtype of ArrowType2", arrowType1.isSubtype(arrowType2));
 }

 // Test isSubtype method with ArrowType and FunctionType
 @Test
 public void testIsSubtypeArrowAndFunctionType() {
 ArrowType arrowType = new ArrowType(new JSTypeRegistry(), Node.newList(INT_TYPE),
 INT_TYPE);
 FunctionType functionType = new FunctionType(new JSTypeRegistry(), INT_TYPE,
 ArrayListType.of(INT_TYPE), VOID_TYPE);
 assertFalse("ArrowType should not be a subtype of FunctionType", arrowType.isSubtype(functionType));
 }

 // Test isSubtype method with ArrowType and ArrowType with varargs
 @Test
 public void testIsSubtypeArrowsWithVarargs() {
 ArrowType arrowType1 = new ArrowType(new JSTypeRegistry(), Node.newList(INT_TYPE),
 INT_TYPE);
 ArrowType arrowType2 = new ArrowType(new JSTypeRegistry(),
 Node.newList(INT_TYPE, INT_TYPE, INT_TYPE), INT_TYPE);
 assertTrue("ArrowType1 should be a subtype of ArrowType2", arrowType1.isSubtype(arrowType2));
 }

 // Test hasEqualParameters method
 @Test
 public void testHasEqualParameters() {
 ArrowType arrowType1 = new ArrowType(new JSTypeRegistry(),
 Node.newList(INT_TYPE), INT_TYPE);
 ArrowType arrowType2 = new ArrowType(new JSTypeRegistry(),
 Node.newList(INT_TYPE), INT_TYPE);
 ArrowType arrowType3 = new ArrowType(new JSTypeRegistry(),
 Node.newList(INT_TYPE, INT_TYPE), INT_TYPE);
 ArrowType arrowType4 = new ArrowType(new JSTypeRegistry(),
 Node.newList(INT_TYPE, INT_TYPE, INT_TYPE), INT_TYPE);
 ArrowType arrowType5 = new ArrowType(new JSTypeRegistry(),
 Node.newList(INT_TYPE, STRING_TYPE), INT_TYPE);
 assertTrue("ArrowType1 and ArrowType2 have equal parameters",
 arrowType1.hasEqualParameters(arrowType2));
 assertFalse("ArrowType1 and ArrowType3 do not have equal parameters",
 arrowType1.hasEqualParameters(arrowType3));
 assertFalse("ArrowType1 and ArrowType4 do not have equal parameters",
 arrowType1.hasEqualParameters(arrowType4));
 assertFalse("ArrowType1 and ArrowType5 do not have equal parameters",
 arrowType1.hasEqualParameters(arrowType5));
 }

 // Test equals and hashCode methods
 @Test
 public void testEqualsAndHashCode() {
 ArrowType arrowType1 = new ArrowType(new JSTypeRegistry(), Node.newList(INT_TYPE),
 INT_TYPE);
 ArrowType arrowType2 = new ArrowType(new JSTypeRegistry(), Node.newList(INT_TYPE),
 INT_TYPE);
 ArrowType arrowType3 = new ArrowType(new JSTypeRegistry(), Node.newList(INT_TYPE, INT_TYPE),
 INT_TYPE);
 assertEquals("ArrowType1 and ArrowType2 are equal", arrowType1, arrowType2);
 assertNotEquals("ArrowType1 and ArrowType3 are not equal", arrowType1, arrowType3);
 }

 // Test toString method
 @Test
 public void testToString() {
 ArrowType arrowType1 = new ArrowType(new JSTypeRegistry(), Node.newList(INT_TYPE),
 INT_TYPE);
 assertEquals("ArrowType(INT_TYPE, INT_TYPE)", arrowType1.toString());
 }

 // Test resolveInternal method with valid input
 @Test
 public void testResolveInternalValid() {
 ArrowType arrowType1 = new ArrowType(new JSTypeRegistry(), Node.newList(INT_TYPE),
 INT_TYPE);
 ErrorReporter errorReporter = mock(ErrorReporter.class);
 StaticScope<JSType> scope = mock(StaticScope.class);
 arrowType1.resolveInternal(errorReporter, scope);
 verifyNoMoreInteractions(errorReporter);
 verifyNoMoreInteractions(scope);
 }

 // Test resolveInternal method with null input
 @Test(expected = NullPointerException.class)
 public void testResolveInternalNullInput() {
 ArrowType arrowType1 = new ArrowType(new JSTypeRegistry(), Node.newList(INT_TYPE),
 INT_TYPE);
 arrowType1.resolveInternal(null, null);
 }

 // Test resolveInternal method with invalid return type
 @Test(expected = IllegalArgumentException.class)
 public void testResolveInternalInvalidReturnType() {
 ArrowType arrowType1 = new ArrowType(new JSTypeRegistry(), Node.newList(INT_TYPE),
 INT_TYPE);
 ErrorReporter errorReporter = mock(ErrorReporter.class);
 StaticScope<JSType> scope = mock(StaticScope.class);
 arrowType1.returnType = null;
 arrowType1.resolveInternal(errorReporter, scope);
 verify(errorReporter).reportError(anyString());
 }

 // Test resolveInternal method with unresolved parameters
 @Test
 public void testResolveInternalUnresolvedParameters() {
 ArrowType arrowType1 = new ArrowType(new JSTypeRegistry(), Node.newList(INT_TYPE),
 INT_TYPE);
 ErrorReporter errorReporter = mock(ErrorReporter.class);
 StaticScope<JSType> scope = mock(StaticScope.class);
 arrowType1.parameters = Node.newList(Node.newIdentifier("x"));
 arrowType1.resolveInternal(errorReporter, scope);
 verify(scope).put("x", any(JSType.class));
 }

 // Test hasUnknownParamsOrReturn method
 @Test
 public void testHasUnknownParamsOrReturn() {
 ArrowType arrowType1 = new ArrowType(new JSTypeRegistry(), Node.newList(INT_TYPE),
 INT_TYPE);
 ArrowType arrowType2 = new ArrowType(new JSTypeRegistry(), Node.newList(INT_TYPE),
 NULL_TYPE);
 ArrowType arrowType3 = new ArrowType(new JSTypeRegistry(), Node.newList(INT_TYPE),
 INT_TYPE);
 ArrowType arrowType4 = new ArrowType(new JSTypeRegistry(), null, INT_TYPE);
 assertTrue("ArrowType2 has an unknown return type", arrowType2.hasUnknownParamsOrReturn());
 assertFalse("ArrowType1 and ArrowType3 do not have unknown params or return",
 arrowType1.hasUnknownParamsOrReturn());
 assertFalse("ArrowType3 has no unknown params or return", arrowType3.hasUnknownParamsOrReturn());
 assertFalse("ArrowType4 has an unknown parameters list", arrowType4.hasUnknownParamsOrReturn());
 }
}