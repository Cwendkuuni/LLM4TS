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
 put(Double.class, 1.729);
 }};

 @Test
 public void testPrimitiveTypeOf() {
 for (Map.Entry<Class<?>, Object> entry : testValues.entrySet()) {
 Class<?> clazz = entry.getKey();
 Class<?> expected = Primitives.primitiveTypeOf(clazz);
 Class<?> actual = Primitives.primitiveTypeOf(clazz);
 Assert.assertEquals("Unexpected primitive type for class: " + clazz.getSimpleName(), expected, actual);
 }
 }

 @Test
 public void testIsPrimitiveWrapper() {
 for (Map.Entry<Class<?>, Object> entry : testValues.entrySet()) {
 Class<?> clazz = entry.getKey();
 boolean expected = Primitives.isPrimitiveWrapper(clazz);
 boolean actual = Primitives.isPrimitiveWrapper(clazz);
 Assert.assertEquals("Unexpected result for isPrimitiveWrapper for class: " + clazz.getSimpleName(), expected, actual);
 }
 }

 @Test
 public void testPrimitiveWrapperOf() {
 for (Map.Entry<Class<?>, Object> entry : testValues.entrySet()) {
 Class<?> wrapperType = Primitives.primitiveTypeOf(entry.getKey());
 Class<?> expected = wrapperType;
 Object actual = Primitives.primitiveWrapperOf(wrapperType);
 if (Collection.class.isAssignableFrom(expected)) {
 List<?> expectedList = (List<?>) java.util.Arrays.asList(new Object[]{entry.getValue()});
 Class<?> listElementType = expectedList.get(0).getClass();
 actual = ((List<?>) actual).get(0);
 expected = listElementType;
 }
 Assert.assertEquals("Unexpected primitive wrapper object for class: " + wrapperType.getSimpleName(), expected, actual.getClass());
 }
 }

 @Test(expected = ClassCastException.class)
 public void testPrimitiveWrapperOfNull() {
 Primitives.primitiveWrapperOf(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPrimitiveWrapperOfInvalidClass() {
 Primitives.primitiveWrapperOf(String.class);
 }

 @Test
 public void testPrimitiveValueOrNullFor() {
 for (Map.Entry<Class<?>, Object> entry : testValues.entrySet()) {
 Class<?> primitiveType = Primitives.primitiveTypeOf(entry.getKey());
 Object expected = entry.getValue();
 Object actual = Primitives.primitiveValueOrNullFor(primitiveType);
 if (expected == null) {
 Assert.assertNull("Unexpected non-null value for nullable primitive type: " + primitiveType.getSimpleName(), actual);
 } else {
 Assert.assertEquals("Unexpected value for primitive type: " + primitiveType.getSimpleName(), expected, actual);
 }
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPrimitiveValueOrNullForInvalidClass() {
 Primitives.primitiveValueOrNullFor(String.class);
 }
}