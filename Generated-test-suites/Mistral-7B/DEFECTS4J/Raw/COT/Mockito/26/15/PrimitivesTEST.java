import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class PrimitivesTest {

 private static final Map<Class<?>, Object> testCases = new HashMap<>();

 static {
 testCases.put(Boolean.class, false);
 testCases.put(Character.class, (char) 0);
 testCases.put(Byte.class, (byte) -128);
 testCases.put(Short.class, (short) 32767);
 testCases.put(Integer.class, -123456789);
 testCases.put(Long.class, 123456789123L);
 testCases.put(Float.class, 3.14F);
 testCases.put(Double.class, 1.5707963267948966);
 }

 @Test
 public void testPrimitiveTypeOf() {
 for (Map.Entry<Class<?>, Object> entry : testCases.entrySet()) {
 Class<?> clazz = entry.getKey();
 Class<?> result = Primitives.primitiveTypeOf(clazz);
 Assert.assertEquals(clazz, result);
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPrimitiveTypeOfNull() {
 Class<?> nullClass = null;
 try {
 Primitives.primitiveTypeOf(nullClass);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testIsPrimitiveWrapper() {
 for (Map.Entry<Class<?>, Object> entry : testCases.entrySet()) {
 Class<?> clazz = entry.getKey();
 boolean result = Primitives.isPrimitiveWrapper(clazz);
 if (Primitives.isPrimitive(clazz)) {
 Assert.assertFalse(result);
 } else {
 Assert.assertTrue(result);
 }
 }
 }

 @Test
 public void testPrimitiveWrapperOf() {
 for (Map.Entry<Class<?>, Object> entry : testCases.entrySet()) {
 Class<?> primitiveType = (Class<?>) entry.getValue();
 Class<?> wrapperType = Primitives.primitiveTypeOf(primitiveType);
 Object wrapper = Primitives.primitiveWrapperOf(wrapperType);
 Assert.assertSame(wrapper, Primitives.primitiveValueOrNullFor(primitiveType));
 }
 }

 @Test(expected = NullPointerException.class)
 public void testPrimitiveWrapperOfNull() {
 Class<?> nullClass = null;
 try {
 Primitives.primitiveWrapperOf(nullClass);
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testPrimitiveValueOrNullFor() {
 for (Map.Entry<Class<?>, Object> entry : testCases.entrySet()) {
 Class<?> primitiveType = (Class<?>) entry.getKey();
 Object result = Primitives.primitiveValueOrNullFor(primitiveType);
 if (Primitives.isPrimitive(primitiveType)) {
 Assert.assertEquals(entry.getValue(), result);
 } else {
 Assert.assertNull(result);
 }
 }
 }
}