import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.util.Primitives;

import java.util.HashMap;
import java.util.Map;

public class PrimitivesTest {

 private static final Map<Class<?>, Object> testValues = new HashMap<Class<?>, Object>() {{
 put(Boolean.class, true);
 put(Character.class, (char) 0x30); // digit 0
 put(Byte.class, (byte) 0x7F); // max value for byte
 put(Short.class, (short) 0x7FFF); // max value for short
 put(Integer.class, Integer.MAX_VALUE);
 put(Long.class, Long.MAX_VALUE);
 put(Float.class, Float.MAX_VALUE);
 put(Double.class, Double.MAX_VALUE);
 }};

 @Test
 public void testPrimitveTypeOf() {
 for (Map.Entry<Class<?>, Object> entry : testValues.entrySet()) {
 Class<?> clazz = entry.getKey();
 Class<?> expected = Primitives.primitiveTypeOf(clazz);
 Class<?> actual = Primitives.primitiveTypeOf(clazz);
 Assert.assertEquals("primitiveTypeOf() returned unexpected result for " + clazz.getName(), expected, actual);
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPrimitveTypeOfNull() {
 Class<?> clazz = null;
 Primitives.primitiveTypeOf(clazz);
 }

 @Test
 public void testIsPrimitiveWrapper() {
 for (Map.Entry<Class<?>, Object> entry : testValues.entrySet()) {
 Class<?> clazz = entry.getKey();
 boolean expected = Primitives.isPrimitiveWrapper(clazz);
 boolean actual = Primitives.isPrimitiveWrapper(clazz);
 Assert.assertEquals("isPrimitiveWrapper() returned unexpected result for " + clazz.getName(), expected, actual);
 }
 }

 @Test
 public void testPrimitiveWrapperOf() {
 for (Map.Entry<Class<?>, Object> entry : testValues.entrySet()) {
 Class<?> clazz = entry.getKey();
 Object actual = Primitives.primitiveWrapperOf(clazz);
 Object expected = testValues.get(clazz.getClass());
 if (clazz.isPrimitive()) {
 Assert.assertEquals("primitiveWrapperOf() returned unexpected result for primitive type " + clazz.getName(), expected, actual);
 } else {
 Assert.assertEquals("primitiveWrapperOf() returned unexpected result for wrapper type " + clazz.getName(), expected.getClass(), actual.getClass());
 Assert.assertEquals("primitiveWrapperOf() returned unexpected value for wrapper type " + clazz.getName(), expected, actual);
 }
 }
 }

 @Test
 public void testPrimitiveValueOrNullFor() {
 for (Map.Entry<Class<?>, Object> entry : testValues.entrySet()) {
 Class<?> clazz = entry.getKey();
 Object actual = Primitives.primitiveValueOrNullFor(clazz);
 Object expected = entry.getValue();
 if (clazz.isPrimitive()) {
 Assert.assertNotNull("primitiveValueOrNullFor() returned null for primitive type " + clazz.getName(), actual);
 Assert.assertEquals("primitiveValueOrNullFor() returned unexpected value for primitive type " + clazz.getName(), expected, actual);
 } else {
 Assert.assertNull("primitiveValueOrNullFor() returned non-null value for wrapper type " + clazz.getName(), actual);
 }
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPrimitiveValueOrNullForNull() {
 Class<?> clazz = null;
 Primitives.primitiveValueOrNullFor(clazz);
 }
}