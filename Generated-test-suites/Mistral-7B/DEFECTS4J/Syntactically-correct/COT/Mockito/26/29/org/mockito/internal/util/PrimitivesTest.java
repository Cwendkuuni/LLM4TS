import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.util.Primitives;

import java.util.HashMap;
import java.util.Map;

public class PrimitivesTest {

 private Map<Class<?>, Object> testCases = new HashMap<>();

 @Test
 public void testPrimitiveTypeOf() {
 for (Class<?> clazz : new Class[] { boolean.class, char.class, byte.class, short.class,
 Integer.class, Long.class, Float.class, Double.class, String.class }) {
 Class<?> result = Primitives.primitiveTypeOf(clazz);
 Assert.assertEquals("Incorrect primitive type for given Class.", getExpectedType(clazz), result);
 }
 }

 @Test
 public void testIsPrimitiveWrapper() {
 TestCases.put(isPrimitiveWrapper, new Object[] { boolean.class, true,
 Character.class, true, Byte.class, true, Short.class, true,
 Integer.class, false, Long.class, false, Float.class, false,
 Double.class, false, String.class, false });
 for (Object[] testCase : (Object[]) TestCases.get(isPrimitiveWrapper)) {
 Class<?> type = (Class<?>) testCase[0];
 boolean expected = (boolean) testCase[1];
 boolean result = Primitives.isPrimitiveWrapper(type);
 Assert.assertEquals("Incorrect result for isPrimitiveWrapper() method.", expected, result);
 }
 }

 @Test
 public void testPrimitiveWrapperOf() {
 TestCases.put(primitiveWrapperOf, new Object[] { boolean.class, false,
 Character.class, (char) 0, Byte.class, (byte) 0, Short.class, (short) 0,
 Integer.class, 0, Long.class, 0L, Float.class, 0F, Double.class, 0D,
 String.class, null });
 for (Object[] testCase : (Object[]) TestCases.get(primitiveWrapperOf)) {
 Class<?> type = (Class<?>) testCase[0];
 Object expected = testCase[1];
 Object result = Primitives.primitiveWrapperOf(type);
 if (expected != null) {
 Assert.assertEquals("Incorrect result for primitiveWrapperOf() method.", expected, result);
 } else {
 Assert.assertNull("Expected null result for primitiveWrapperOf() method.", result);
 }
 }
 }

 @Test
 public void testPrimitiveValueOrNullFor() {
 TestCases.put(primitiveValueOrNullFor, new Object[] { boolean.class, false,
 Character.class, '\u0000', Byte.class, (byte) 0, Short.class, (short) 0,
 Integer.class, 0, Long.class, 0L, Float.class, 0F, Double.class, 0,
 String.class, null });
 for (Object[] testCase : (Object[]) TestCases.get(primitiveValueOrNullFor)) {
 Class<?> type = (Class<?>) testCase[0];
 Object expected = testCase[1];
 Object result = Primitives.primitiveValueOrNullFor(type);
 Assert.assertEquals("Incorrect result for primitiveValueOrNullFor() method.", expected, result);
 }
 }

 @Test(expected = NullPointerException.class)
 public void testNullArgumentException() {
 Primitives.primitiveTypeOf(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUnknownClassException() {
 Primitives.primitiveTypeOf(String.class);
 }

 private Class<?> getExpectedType(Class<?> clazz) {
 if (clazz.isPrimitive()) {
 return clazz;
 } else {
 return Primitives.primitiveTypes.get(clazz);
 }
 }

 private static Map<String, Map<Class<?>, Object[]>> TestCases = new HashMap<>();
}