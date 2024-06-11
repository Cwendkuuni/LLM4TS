import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.util.Primitives;

import java.util.Map;

public class PrimitivesTest {

 private static final Class<?>[] PRIMITIVE_WRAPPER_TYPES = {Boolean.class, Character.class, Byte.class, Short.class, Integer.class, Long.class, Float.class, Double.class};

 @Test
 public void testPrimitiveTypeOf() {
 for (Class<?> clazz : PRIMITIVE_WRAPPER_TYPES) {
 Class<?> actual = Primitives.primitiveTypeOf(clazz);
 Class<?> expected = clazz;
 Assert.assertEquals(expected, actual);
 }

 Class<?> clazz1 = String.class;
 Class<?> actual1 = Primitives.primitiveTypeOf(clazz1);
 Class<?> expected1 = null;
 Assert.assertNull(actual1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPrimitiveTypeOfNullClazz() {
 Primitives.primitiveTypeOf(null);
 }

 @Test
 public void testIsPrimitiveWrapper() {
 for (Class<?> type : PRIMITIVE_WRAPPER_TYPES) {
 boolean actual = Primitives.isPrimitiveWrapper(type);
 boolean expected = true;
 Assert.assertEquals(expected, actual);
 }

 Class<?> nonPrimitiveWrapperType = Object.class;
 boolean actual1 = Primitives.isPrimitiveWrapper(nonPrimitiveWrapperType);
 boolean expected1 = false;
 Assert.assertEquals(expected1, actual1);
 }

 @Test
 public void testPrimitiveWrapperOf() {
 for (Class<?> type : PRIMITIVE_WRAPPER_TYPES) {
 Object actual = Primitives.primitiveWrapperOf(type);
 Object expected = Primitives.wrapperReturnValues.get(type);
 Assert.assertEquals(expected, actual);
 }
 }

 @Test
 public void testPrimitiveValueOrNullForBoolean() {
 Object actual = Primitives.primitiveValueOrNullFor(boolean.class);
 Object expected = Primitives.primitiveValues.get(boolean.class);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testPrimitiveValueOrNullForCharacter() {
 Character actual = (Character) Primitives.primitiveValueOrNullFor(char.class);
 Character expected = Primitives.primitiveValues.get(char.class);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testPrimitiveValueOrNullForByte() {
 Byte actual = (Byte) Primitives.primitiveValueOrNullFor(byte.class);
 Byte expected = Primitives.primitiveValues.get(byte.class);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testPrimitiveValueOrNullForShort() {
 Short actual = (Short) Primitives.primitiveValueOrNullFor(short.class);
 Short expected = Primitives.primitiveValues.get(short.class);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testPrimitiveValueOrNullForInt() {
 Integer actual = (Integer) Primitives.primitiveValueOrNullFor(int.class);
 Integer expected = Primitives.primitiveValues.get(int.class);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testPrimitiveValueOrNullForLong() {
 Long actual = (Long) Primitives.primitiveValueOrNullFor(long.class);
 Long expected = Primitives.primitiveValues.get(long.class);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testPrimitiveValueOrNullForFloat() {
 Float actual = (Float) Primitives.primitiveValueOrNullFor(float.class);
 Float expected = Primitives.primitiveValues.get(float.class);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testPrimitiveValueOrNullForDouble() {
 Double actual = (Double) Primitives.primitiveValueOrNullFor(double.class);
 Double expected = Primitives.primitiveValues.get(double.class);
 Assert.assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPrimitiveValueOrNullForNullClass() {
 Primitives.primitiveValueOrNullFor(null);
 }
}