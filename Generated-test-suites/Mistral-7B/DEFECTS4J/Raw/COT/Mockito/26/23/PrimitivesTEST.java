import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.util.Primitives;

import java.util.HashMap;
import java.util.Map;

public class PrimitivesTest {

 private static final Map<Class<?>, Object> expectedWrapperValues = new HashMap<Class<?>, Object>() {{
 put(Boolean.class, false);
 put(Character.class, '\u0000');
 put(Byte.class, (byte) 0);
 put(Short.class, (short) 0);
 put(Integer.class, 0);
 put(Long.class, 0L);
 put(Float.class, 0F);
 put(Double.class, 0D);
 }};

 private static final Map<Class<?>, Object> expectedPrimitiveValues = new HashMap<Class<?>, Object>() {{
 put(boolean.class, false);
 put(char.class, '\u0000');
 put(byte.class, (byte) 0);
 put(short.class, (short) 0);
 put(int.class, 0);
 put(long.class, 0L);
 put(float.class, 0F);
 put(double.class, 0D);
 }};

 @Test
 public void testPrimitiveTypeOf() {
 for (Map.Entry<Class<?>, Class<?>> entry : Primitives.primitiveTypes.entrySet()) {
 Class<?> clazz = entry.getKey();
 Class<?> expected = entry.getValue();
 Assert.assertEquals(expected, Primitives.primitiveTypeOf(clazz));
 }

 for (Map.Entry<Class<?>, Class<?>> entry : Primitives.wrapperTypes.entrySet()) {
 Class<?> wrapperClass = entry.getKey();
 Class<?> primitiveType = entry.getValue();
 Assert.assertEquals(primitiveType, Primitives.primitiveTypeOf(wrapperClass));
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPrimitiveTypeOfNonPrimitive() {
 Class<String> clazz = String.class;
 Assert.assertThrows(IllegalArgumentException.class, () -> Primitives.primitiveTypeOf(clazz));
 }

 @Test
 public void testIsPrimitiveWrapper() {
 for (Map.Entry<Class<?>, Class<?>> entry : Primitives.wrapperTypes.entrySet()) {
 Class<?> type = entry.getKey();
 boolean expected = true;
 Assert.assertEquals(expected, Primitives.isPrimitiveWrapper(type));
 }

 for (Map.Entry<Class<?>, Class<?>> entry : Primitives.primitiveTypes.entrySet()) {
 Class<?> type = entry.getKey();
 boolean expected = false;
 Assert.assertEquals(expected, Primitives.isPrimitiveWrapper(type));
 }

 Class<String> clazz = String.class;
 boolean expected = false;
 Assert.assertEquals(expected, Primitives.isPrimitiveWrapper(clazz));
 }

 @Test
 public void testPrimitiveWrapperOf() {
 for (Map.Entry<Class<?>, Object> entry : Primitives.wrapperReturnValues.entrySet()) {
 Class<?> type = entry.getKey();
 Object expected = entry.getValue();
 Object result = Primitives.primitiveWrapperOf(type);
 Assert.assertEquals(expected, result);
 }
 }

 @Test
 public void testPrimitiveValueOrNullForBoolean() {
 boolean expected = false;
 Object result = Primitives.primitiveValueOrNullFor(boolean.class);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testPrimitiveValueOrNullForCharacter() {
 char expected = '\u0000';
 Object result = Primitives.primitiveValueOrNullFor(char.class);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testPrimitiveValueOrNullForByte() {
 byte expected = 0;
 Object result = Primitives.primitiveValueOrNullFor(byte.class);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testPrimitiveValueOrNullForShort() {
 short expected = 0;
 Object result = Primitives.primitiveValueOrNullFor(short.class);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testPrimitiveValueOrNullForInt() {
 int expected = 0;
 Object result = Primitives.primitiveValueOrNullFor(int.class);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testPrimitiveValueOrNullForLong() {
 long expected = 0L;
 Object result = Primitives.primitiveValueOrNullFor(long.class);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testPrimitiveValueOrNullForFloat() {
 float expected = 0F;
 Object result = Primitives.primitiveValueOrNullFor(float.class);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testPrimitiveValueOrNullForDouble() {
 double expected = 0D;
 Object result = Primitives.primitiveValueOrNullFor(double.class);
 Assert.assertEquals(expected, result);
 }

 @Test(expected = NullPointerException.class)
 public void testPrimitiveValueOrNullForNullType() {
 Class<?> clazz = null;
 Object result = Primitives.primitiveValueOrNullFor(clazz);
 Assert.assertNull(result);
 }

 @Test
 public void testStaticInitialization() {
 Assert.assertEquals(expectedWrapperValues, Primitives.wrapperReturnValues);
 Assert.assertEquals(expectedPrimitiveValues, Primitives.primitiveValues);
 }
}