import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.util.Primitives;

import java.util.HashMap;
import java.util.Map;

public class PrimitivesTest {

 private static final Map<Class<?>, Object> testValues = new HashMap<Class<?>, Object>() {{
 put(Boolean.class, true);
 put(Character.class, 'A');
 put(Byte.class, (byte) 127);
 put(Short.class, (short) 32767);
 put(Integer.class, 2147483647);
 put(Long.class, 9223372036854775807L);
 put(Float.class, 3.14f);
 put(Double.class, 1.729e308);
 }};

 @Test
 public void testPrimitiveTypeOf() {
 for (Map.Entry<Class<?>, Object> entry : testValues.entrySet()) {
 Class<?> clazz = entry.getKey();
 Class<?> expected = PrimitiveTypes.primitiveTypeOf(clazz);
 Class<?> actual = PrimitiveTypes.primitiveTypeOf(clazz);
 Assert.assertEquals("primitiveTypeOf(" + clazz + ") should return " + expected, expected, actual);
 }
 }

 @Test
 public void testIsPrimitiveWrapper() {
 for (Map.Entry<Class<?>, Object> entry : testValues.entrySet()) {
 Class<?> clazz = entry.getKey();
 boolean expected = false;
 if (PrimitiveTypes.isPrimitive(clazz)) {
 continue;
 }
 Class<?> wrapper = PrimitiveTypes.primitiveTypeOf(clazz);
 expected = true;

 boolean actual = PrimitiveTypes.isPrimitiveWrapper(clazz);
 Assert.assertEquals("isPrimitiveWrapper(" + clazz + ") should return " + expected, expected, actual);
 }
 }

 @Test
 public void testPrimitiveWrapperOf() {
 for (Map.Entry<Class<?>, Object> entry : testValues.entrySet()) {
 Class<?> clazz = entry.getKey();
 Object expected = null;
 if (PrimitiveTypes.isPrimitive(clazz)) {
 expected = PrimitiveValues.primitiveValueOrNullFor(clazz);
 } else {
 expected = PrimitiveTypes.primitiveWrapperOf(clazz);
 }
 Object actual = PrimitiveTypes.primitiveWrapperOf(clazz);
 Assert.assertEquals("primitiveWrapperOf(" + clazz + ") should return " + expected, expected, actual);
 }
 }

 @Test(expected = NullPointerException.class)
 public void testPrimitiveWrapperOfNull() {
 PrimitiveTypes.primitiveWrapperOf(null);
 }

 @Test
 public void testPrimitiveValueOrNullForBoolean() {
 Object expected = true;
 Object actual = Primitives.primitiveValueOrNullFor(Boolean.class);
 Assert.assertEquals("primitiveValueOrNullFor(Boolean.class) should return " + expected, expected, actual);
 }

 @Test
 public void testPrimitiveValueOrNullForCharacter() {
 char expected = 'A';
 Object actual = Primitives.primitiveValueOrNullFor(Character.class);
 Assert.assertEquals("primitiveValueOrNullFor(Character.class) should return " + expected, expected, actual);
 }

 @Test
 public void testPrimitiveValueOrNullForByte() {
 byte expected = 127;
 Object actual = Primitives.primitiveValueOrNullFor(Byte.class);
 Assert.assertEquals("primitiveValueOrNullFor(Byte.class) should return " + expected, expected, actual);
 }

 @Test
 public void testPrimitiveValueOrNullForShort() {
 short expected = 32767;
 Object actual = Primitives.primitiveValueOrNullFor(Short.class);
 Assert.assertEquals("primitiveValueOrNullFor(Short.class) should return " + expected, expected, actual);
 }

 @Test
 public void testPrimitiveValueOrNullForInteger() {
 int expected = 2147483647;
 Object actual = Primitives.primitiveValueOrNullFor(Integer.class);
 Assert.assertEquals("primitiveValueOrNullFor(Integer.class) should return " + expected, expected, actual);
 }

 @Test
 public void testPrimitiveValueOrNullForLong() {
 long expected = 9223372036854775807L;
 Object actual = Primitives.primitiveValueOrNullFor(Long.class);
 Assert.assertEquals("primitiveValueOrNullFor(Long.class) should return " + expected, expected, actual);
 }

 @Test
 public void testPrimitiveValueOrNullForFloat() {
 float expected = 3.14f;
 Object actual = Primitives.primitiveValueOrNullFor(Float.class);
 Assert.assertEquals("primitiveValueOrNullFor(Float.class) should return " + expected, expected, actual);
 }

 @Test
 public void testPrimitiveValueOrNullForDouble() {
 double expected = 1.729e308;
 Object actual = Primitives.primitiveValueOrNullFor(Double.class);
 Assert.assertEquals("primitiveValueOrNullFor(Double.class) should return " + expected, expected, actual);
 }

 @Test(expected = NullPointerException.class)
 public void testPrimitiveValueOrNullForNull() {
 Primitives.primitiveValueOrNullFor(null);
 }
}