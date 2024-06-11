import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.util.Primitives;

public class PrimitivesTest {

 private Class<Boolean> booleanType = Boolean.TYPE;
 private Class<Character> characterType = Character.TYPE;
 private Class<Byte> byteType = Byte.TYPE;
 private Class<Short> shortType = Short.TYPE;
 private Class<Integer> integerType = Integer.TYPE;
 private Class<Long> longType = Long.TYPE;
 private Class<Float> floatType = Float.TYPE;
 private Class<Double> doubleType = Double.TYPE;

 @Test
 public void testPrimitiveTypeOf_ClassIsPrimitive() {
 Class<Boolean> result = Primitives.primitiveTypeOf(booleanType);
 assertEquals(booleanType, result);
 }

 @Test
 public void testPrimitiveTypeOf_ClassIsWrapper() {
 Class<Integer> integerWrapperType = Integer.class;
 Class<Integer> integerPrimitiveType = Integer.TYPE;
 Class<Integer> result = Primitives.primitiveTypeOf(integerWrapperType);
 assertEquals(integerPrimitiveType, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPrimitiveTypeOf_NullClass() {
 try {
 Primitives.primitiveTypeOf(null);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testIsPrimitiveWrapper_BooleanWrapper() {
 assertTrue(Primitives.isPrimitiveWrapper(Boolean.class));
 }

 @Test
 public void testIsPrimitiveWrapper_CharacterWrapper() {
 assertTrue(Primitives.isPrimitiveWrapper(Character.class));
 }

 @Test
 public void testIsPrimitiveWrapper_ByteWrapper() {
 assertTrue(Primitives.isPrimitiveWrapper(Byte.class));
 }

 @Test
 public void testIsPrimitiveWrapper_ShortWrapper() {
 assertTrue(Primitives.isPrimitiveWrapper(Short.class));
 }

 @Test
 public void testIsPrimitiveWrapper_IntegerWrapper() {
 assertTrue(Primitives.isPrimitiveWrapper(Integer.class));
 }

 @Test
 public void testIsPrimitiveWrapper_LongWrapper() {
 assertTrue(Primitives.isPrimitiveWrapper(Long.class));
 }

 @Test
 public void testIsPrimitiveWrapper_FloatWrapper() {
 assertTrue(Primitives.isPrimitiveWrapper(Float.class));
 }

 @Test
 public void testIsPrimitiveWrapper_DoubleWrapper() {
 assertTrue(Primitives.isPrimitiveWrapper(Double.class));
 }

 @Test
 public void testPrimitiveWrapperOf_BooleanWrapper() {
 Boolean result = Primitives.primitiveWrapperOf(booleanType);
 assertEquals(false, result);
 }

 @Test
 public void testPrimitiveWrapperOf_CharacterWrapper() {
 Character result = Primitives.primitiveWrapperOf(characterType);
 assertEquals('\u0000', result);
 }

 @Test
 public void testPrimitiveWrapperOf_ByteWrapper() {
 Byte result = Primitives.primitiveWrapperOf(byteType);
 assertEquals((byte) 0, result);
 }

 @Test
 public void testPrimitiveWrapperOf_ShortWrapper() {
 Short result = Primitives.primitiveWrapperOf(shortType);
 assertEquals((short) 0, result);
 }

 @Test
 public void testPrimitiveWrapperOf_IntegerWrapper() {
 Integer result = Primitives.primitiveWrapperOf(integerType);
 assertEquals(0, result.intValue());
 }

 @Test
 public void testPrimitiveWrapperOf_LongWrapper() {
 Long result = Primitives.primitiveWrapperOf(longType);
 assertEquals(0L, result.longValue());
 }

 @Test
 public void testPrimitiveWrapperOf_FloatWrapper() {
 Float result = Primitives.primitiveWrapperOf(floatType);
 assertEquals(0F, result);
 }

 @Test
 public void testPrimitiveWrapperOf_DoubleWrapper() {
 Double result = Primitives.primitiveWrapperOf(doubleType);
 assertEquals(0D, result);
 }

 @Test
 public void testPrimitiveValueOrNullFor_boolean() {
 Boolean result = Primitives.primitiveValueOrNullFor(boolean.class);
 assertEquals(false, result);
 }

 @Test
 public void testPrimitiveValueOrNullFor_char() {
 Character result = Primitives.primitiveValueOrNullFor(char.class);
 assertEquals('\u0000', result);
 }

 @Test
 public void testPrimitiveValueOrNullFor_byte() {
 Byte result = Primitives.primitiveValueOrNullFor(byte.class);
 assertEquals((byte) 0, result);
 }

 @Test
 public void testPrimitiveValueOrNullFor_short() {
 Short result = Primitives.primitiveValueOrNullFor(short.class);
 assertEquals((short) 0, result);
 }

 @Test
 public void testPrimitiveValueOrNullFor_int() {
 Integer result = Primitives.primitiveValueOrNullFor(int.class);
 assertEquals(0, result.intValue());
 }

 @Test
 public void testPrimitiveValueOrNullFor_long() {
 Long result = Primitives.primitiveValueOrNullFor(long.class);
 assertEquals(0L, result.longValue());
 }

 @Test
 public void testPrimitiveValueOrNullFor_float() {
 Float result = Primitives.primitiveValueOrNullFor(float.class);
 assertEquals(0F, result);
 }

 @Test
 public void testPrimitiveValueOrNullFor_double() {
 Double result = Primitives.primitiveValueOrNullFor(double.class);
 assertEquals(0D, result);
 }

 @Test
 public void testPrimitiveTypesMapContainsAllPrimitives() {
 Set<Map.Entry<Class<?>, Class<?>>> entries = primitiveTypes.entrySet();
 for (Map.Entry<Class<?>, Class<?>> entry : entries) {
 Class<?> key = entry.getKey();
 Class<?> value = entry.getValue();
 assertTrue(primitiveTypes.containsKey(key));
 assertTrue(primitiveTypes.containsValue(value));
 }
 }

 @Test
 public void testWrapperTypesMapContainsAllWrappers() {
 Set<Map.Entry<Class<?>, Class<?>>> entries = wrapperTypes.entrySet();
 for (Map.Entry<Class<?>, Class<?>> entry : entries) {
 Class<?> key = entry.getKey();
 Class<?> value = entry.getValue();
 assertTrue(wrapperTypes.containsKey(key));
 assertTrue(wrapperTypes.containsValue(value));
 }
 }

 @Test
 public void testWrapperReturnValuesMapContainsAllWrappersWithDefaultValues() {
 Set<Map.Entry<Class<?>, Object>> entries = wrapperReturnValues.entrySet();
 for (Map.Entry<Class<?>, Object> entry : entries) {
 Class<?> key = entry.getKey();
 Object value = entry.getValue();
 assertTrue(wrapperReturnValues.containsKey(key));
 assertEquals(value, Primitives.primitiveWrapperOf(key).getValue());
 }
 }

 @Test
 public void testPrimitiveValuesMapContainsAllPrimitivesWithDefaultValues() {
 Set<Map.Entry<Class<?>, Object>> entries = primitiveValues.entrySet();
 for (Map.Entry<Class<?>, Object> entry : entries) {
 Class<?> key = entry.getKey();
 Object value = entry.getValue();
 assertTrue(primitiveValues.containsKey(key));
 assertEquals(value, Primitives.primitiveValueOrNullFor(key));
 }
 }
}