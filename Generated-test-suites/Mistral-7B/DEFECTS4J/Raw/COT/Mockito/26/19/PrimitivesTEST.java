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

 private static final Map<Class<?>, Object> expectedPrimitives = new HashMap<Class<?>, Object>() {{
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
 Class<?> clazz = String.class;
 Class<?> expected = null;
 Class<?> actual = Primitives.primitiveTypeOf(clazz);
 Assert.assertNull(actual);
 expected = Boolean.TYPE;
 clazz = Boolean.class;
 actual = Primitives.primitiveTypeOf(clazz);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testIsPrimitiveWrapper() {
 Class<?> type = Boolean.class;
 boolean expected = true;
 boolean actual = Primitives.isPrimitiveWrapper(type);
 Assert.assertEquals(expected, actual);
 type = Integer.class;
 expected = false;
 actual = Primitives.isPrimitiveWrapper(type);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testPrimitiveWrapperOf() {
 Class<?> type = Boolean.class;
 Object expected = false;
 Object actual = Primitives.primitiveWrapperOf(type);
 Assert.assertEquals(expected, actual);
 type = Character.class;
 expected = '\u0000';
 actual = Primitives.primitiveWrapperOf(type);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testPrimitiveValueOrNullFor() {
 Class<?> primitiveType = boolean.class;
 Object expected = false;
 Object actual = Primitives.primitiveValueOrNullFor(primitiveType);
 Assert.assertEquals(expected, actual);
 primitiveType = char.class;
 expected = '\u0000';
 actual = Primitives.primitiveValueOrNullFor(primitiveType);
 Assert.assertEquals(expected, actual);
 }

 @Test(expected = NullPointerException.class)
 public void testPrimitiveTypeOfNull() {
 Class<?> clazz = null;
 Primitives.primitiveTypeOf(clazz);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPrimitiveTypeOfArray() {
 Class<?> clazz = int[].class;
 Primitives.primitiveTypeOf(clazz);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPrimitiveTypeOfCollection() {
 Class<?> clazz = java.util.ArrayList.class;
 Primitives.primitiveTypeOf(clazz);
 }

 @Test
 public void testWrapperTypesInitialization() {
 Map<Class<?>, Class<?>> actual = Primitives.wrapperTypes;
 Assert.assertEquals(expectedWrapperTypes(), actual);
 }

 @Test
 public void testPrimitiveTypesInitialization() {
 Map<Class<?>, Class<?>> actual = Primitives.primitiveTypes;
 Assert.assertEquals(expectedPrimitivesTypes(), actual);
 }

 private Map<Class<?>, Class<?>> expectedWrapperTypes() {
 Map<Class<?>, Class<?>> map = new HashMap<Class<?>, Class<?>>();
 //... (same as static initialization in Primitives class)
 return map;
 }

 private Map<Class<?>, Class<?>> expectedPrimitivesTypes() {
 Map<Class<?>, Class<?>> map = new HashMap<Class<?>, Class<?>>();
 //... (same as static initialization in Primitives class)
 return map;
 }

}