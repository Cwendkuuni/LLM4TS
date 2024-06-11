import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.util.Primitives;

public class PrimitivesTest {

 private Class<?> primitiveType = boolean.class;
 private Class<?> wrapperType = Boolean.class;

 @Test
 public void testPrimitiveTypeOf() {
 Class<Boolean> actual = Primitives.primitiveTypeOf(wrapperType);
 assertEquals(Boolean.TYPE, actual);

 actual = Primitives.primitiveTypeOf(primitiveType);
 assertEquals(Boolean.TYPE, actual);

 actual = Primitives.primitiveTypeOf(String.class);
 assertNull(actual);
 }

 @Test(expected = NullPointerException.class)
 public void testPrimitiveTypeOfNullClass() {
 Primitives.primitiveTypeOf(null);
 }

 // Add other tests for isPrimitiveWrapper method here, following a similar pattern

 private Class<?> nonPrimitiveWrapperType = Integer.class;

 @Test
 public void testIsPrimitiveWrapper() {
 assertTrue(Primitives.isPrimitiveWrapper(wrapperType));
 assertFalse(Primitives.isPrimitiveWrapper(nonPrimitiveWrapperType));
 }

 @Test
 public void testPrimitiveWrapperOf() {
 Boolean actual = Primitives.primitiveWrapperOf(Boolean.class);
 assertTrue(actual instanceof Boolean);
 assertEquals(false, actual);

 Integer actualInt = Primitives.primitiveWrapperOf(Integer.class);
 assertTrue(actualInt instanceof Integer);
 assertEquals(0, (int) actualInt);
 }

 @Test(expected = NullPointerException.class)
 public void testPrimitiveWrapperOfNullClass() {
 Primitives.primitiveWrapperOf(null);
 }

 // Add other tests for primitiveValueOrNullFor method here, following a similar pattern

 @Test
 public void testPrimitiveTypesMapContainsExpectedValues() {
 Map<Class<?>, Class<?>> actualMap = Primitives.primitiveTypes;
 assertTrue(actualMap.containsKey(Boolean.class));
 assertTrue(actualMap.containsKey(Character.class));
 // Add other assertions for other classes
 }

 @Test
 public void testWrapperReturnValuesMapContainsExpectedValues() {
 Map<Class<?>, Object> actualMap = Primitives.wrapperReturnValues;
 assertTrue(actualMap.containsKey(Boolean.class));
 assertTrue(actualMap.containsKey(Character.class));
 // Add other assertions for other classes
 }

 @Test
 public void testPrimitiveValuesMapContainsExpectedValues() {
 Map<Class<?>, Object> actualMap = Primitives.primitiveValues;
 assertTrue(actualMap.containsKey(boolean.class));
 assertTrue(actualMap.containsKey(char.class));
 // Add other assertions for other classes
 }
}