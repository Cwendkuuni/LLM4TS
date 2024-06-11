import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.util.Primitives;

import java.util.HashMap;
import java.util.Map;

public class PrimitivesTest {

 private Map<Class<?>, Object> testWrappers = new HashMap<>();
 private Map<Class<?>, Object> testPrimitives = new HashMap<>();

 static {
 testWrappers.put(Boolean.class, Primitives.wrapperReturnValues.get(Boolean.class));
 testWrappers.put(Character.class, Primitives.wrapperReturnValues.get(Character.class));
 testWrappers.put(Byte.class, Primitives.wrapperReturnValues.get(Byte.class));
 testWrappers.put(Short.class, Primitives.wrapperReturnValues.get(Short.class));
 testWrappers.put(Integer.class, Primitives.wrapperReturnValues.get(Integer.class));
 testWrappers.put(Long.class, Primitives.wrapperReturnValues.get(Long.class));
 testWrappers.put(Float.class, Primitives.wrapperReturnValues.get(Float.class));
 testWrappers.put(Double.class, Primitives.wrapperReturnValues.get(Double.class));

 testPrimitives.put(boolean.class, Primitives.primitiveValues.get(boolean.class));
 testPrimitives.put(char.class, Primitives.primitiveValues.get(char.class));
 testPrimitives.put(byte.class, Primitives.primitiveValues.get(byte.class));
 testPrimitives.put(short.class, Primitives.primitiveValues.get(short.class));
 testPrimitives.put(int.class, Primitives.primitiveValues.get(int.class));
 testPrimitives.put(long.class, Primitives.primitiveValues.get(long.class));
 testPrimitives.put(float.class, Primitives.primitiveValues.get(float.class));
 testPrimitives.put(double.class, Primitives.primitiveValues.get(double.class));
 }

 @Test
 public void testPrimitiveTypeOf() {
 for (Map.Entry<Class<?>, Class<?>> entry : Primitives.primitiveTypes.entrySet()) {
 Class<?> clazz = entry.getKey();
 Class<?> expected = entry.getValue();
 Class<?> actual = Primitives.primitiveTypeOf(clazz);
 Assert.assertEquals(expected, actual);
 }

 for (Map.Entry<Class<?>, Class<?>> entry : Primitives.wrapperTypes.entrySet()) {
 Class<?> clazz = entry.getKey();
 Class<?> expected = null;
 for (Map.Entry<Class<?>, Class<?>> primitiveEntry : Primitives.primitiveTypes.entrySet()) {
 if (clazz.isAssignableFrom(primitiveEntry.getValue())) {
 expected = primitiveEntry.getKey();
 break;
 }
 }
 Class<?> actual = Primitives.primitiveTypeOf(clazz);
 Assert.assertEquals(expected, actual);
 }
}

 @Test
 public void testIsPrimitiveWrapper() {
 for (Map.Entry<Class<?>, Object> entry : Primitives.wrapperReturnValues.entrySet()) {
 Class<?> clazz = entry.getKey();
 boolean expected = true;
 boolean actual = Primitives.isPrimitiveWrapper(clazz);
 Assert.assertEquals(expected, actual);
 }

 for (Map.Entry<Class<?>, Object> entry : Primitives.primitiveTypes.entrySet()) {
 Class<?> clazz = entry.getKey();
 boolean expected = false;
 boolean actual = Primitives.isPrimitiveWrapper(clazz);
 Assert.assertEquals(expected, actual);
 }
}

 @Test
 public void testPrimitiveWrapperOf() {
 for (Map.Entry<Class<?>, Object> entry : Primitives.wrapperReturnValues.entrySet()) {
 Class<?> clazz = entry.getKey();
 Object expected = entry.getValue();
 Object actual = Primitives.primitiveWrapperOf(clazz);
 Assert.assertEquals(expected, actual);
 }
}

 @Test(expected = ClassCastException.class)
 public void testPrimitiveWrapperOf_nonPrimitiveType() {
 Object actual = Primitives.primitiveWrapperOf(String.class);
}

 @Test
 public void testPrimitiveValueOrNullFor() {
 for (Map.Entry<Class<?>, Object> entry : Primitives.primitiveValues.entrySet()) {
 Class<?> clazz = entry.getKey();
 Object expected = entry.getValue();
 Object actual = Primitives.primitiveValueOrNullFor(clazz);
 Assert.assertEquals(expected, actual);
 }
}

 @Test
 public void testPrimitiveValueOrNullFor_nullType() {
 Object actual = Primitives.primitiveValueOrNullFor(null);
 Assert.assertNull(actual);
}
}