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
 Class<?> actual = Primitives.primitiveTypeOf(clazz);
 Assert.assertEquals("primitiveTypeOf failed for " + clazz.getName(), expected, actual);
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPrimitiveTypeOfNull() {
 Class<?> clazz = null;
 try {
 Primitives.primitiveTypeOf(clazz);
 } catch (IllegalArgumentException e) {
 // expected
 Assert.assertEquals("IllegalArgumentException not thrown for null", e.getMessage(), "Argument for class cannot be null");
 }
 }

 @Test
 public void testIsPrimitiveWrapper() {
 for (Map.Entry<Class<?>, Class<?>> entry : Primitives.wrapperTypes.entrySet()) {
 Class<?> type = entry.getKey();
 boolean expected = true;
 boolean actual = Primitives.isPrimitiveWrapper(type);
 Assert.assertEquals("isPrimitiveWrapper failed for " + type.getName(), expected, actual);
 }

 for (Class<?> type : new Class[] { int.class, double.class, String.class }) {
 boolean expected = false;
 boolean actual = Primitives.isPrimitiveWrapper(type);
 Assert.assertEquals("isPrimitiveWrapper failed for " + type.getName(), expected, actual);
 }
 }

 @Test
 public void testPrimitiveWrapperOf() {
 for (Map.Entry<Class<?>, Class<?>> entry : Primitives.wrapperTypes.entrySet()) {
 Class<?> wrapperType = entry.getKey();
 Class<?> primitiveType = entry.getValue();
 Object expected = Primitives.wrapperReturnValues.get(wrapperType);
 Object actual = Primitives.primitiveWrapperOf(primitiveType);
 Assert.assertEquals("primitiveWrapperOf failed for " + primitiveType.getName() + " wrapper type " + wrapperType.getName(), expected, actual);
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPrimitiveWrapperOfNull() {
 Class<?> primitiveType = null;
 try {
 Primitives.primitiveWrapperOf(primitiveType);
 } catch (IllegalArgumentException e) {
 // expected
 Assert.assertEquals("IllegalArgumentException not thrown for null", e.getMessage(), "Argument for class cannot be null");
 }
 }

 @Test
 public void testPrimitiveValueOrNullFor() {
 for (Map.Entry<Class<?>, Object> entry : Primitives.primitiveValues.entrySet()) {
 Class<?> primitiveType = entry.getKey();
 Object expected = entry.getValue();
 Object actual = Primitives.primitiveValueOrNullFor(primitiveType);
 Assert.assertEquals("primitiveValueOrNullFor failed for " + primitiveType.getName(), expected, actual);
 }
 }

 @Test
 public void testPrimitiveValueOrNullForNull() {
 Class<?> primitiveType = null;
 Object actual = Primitives.primitiveValueOrNullFor(primitiveType);
 Assert.assertNull("primitiveValueOrNullFor should return null for null primitiveType");
 }

 @Test
 public void testStaticInitialization() {
 Map<Class<?>, Class<?>> primitiveTypesCopy = new HashMap<Class<?>, Class<?>>(Primitives.primitiveTypes);
 Map<Class<?>, Object> wrapperReturnValuesCopy = new HashMap<Class<?>, Object>(Primitives.wrapperReturnValues);
 Map<Class<?>, Object> primitiveValuesCopy = new HashMap<Class<?>, Object>(Primitives.primitiveValues);
 Assert.assertEquals("primitiveTypes not initialized correctly", primitiveTypesCopy, Primitives.primitiveTypes);
 Assert.assertEquals("wrapperReturnValues not initialized correctly", wrapperReturnValuesCopy, Primitives.wrapperReturnValues);
 Assert.assertEquals("primitiveValues not initialized correctly", primitiveValuesCopy, Primitives.primitiveValues);
 }

}