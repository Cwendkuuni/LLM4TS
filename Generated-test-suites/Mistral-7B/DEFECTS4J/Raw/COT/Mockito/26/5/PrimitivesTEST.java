import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.util.Primitives;

import java.util.HashMap;
import java.util.Map;

public class PrimitivesTest {

 private static final Map<Class<?>, Object> expectedWrapperReturnValues = new HashMap<Class<?>, Object>() {{
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
 Class<?> inputType = entry.getKey();
 Class<?> expectedType = entry.getValue();
 Class<?> result = Primitives.primitiveTypeOf(inputType);
 Assert.assertEquals(expectedType, result);
 }
 }

 @Test
 public void testIsPrimitiveWrapper() {
 for (Map.Entry<Class<?>, Class<?>> entry : Primitives.wrapperTypes.entrySet()) {
 Class<?> inputType = entry.getKey();
 boolean expectedResult = true;
 boolean result = Primitives.isPrimitiveWrapper(inputType);
 Assert.assertEquals(expectedResult, result);
 }
 }

 @Test
 public void testIsPrimitiveWrapperForNonWrapperClass() {
 Class<?> intType = Integer.TYPE;
 boolean expectedResult = false;
 boolean result = Primitives.isPrimitiveWrapper(intType);
 Assert.assertEquals(expectedResult, result);
 }

 @Test
 public void testPrimitiveWrapperOf() {
 for (Map.Entry<Class<?>, Class<?>> entry : Primitives.wrapperTypes.entrySet()) {
 Class<?> inputType = entry.getKey();
 Object expectedResult = Primitives.wrapperReturnValues.get(inputType);
 Object result = Primitives.primitiveWrapperOf(inputType);
 Assert.assertEquals(expectedResult, result);
 }
 }

 @Test
 public void testPrimitiveValueOrNullForNullInput() {
 assertNull(Primitives.primitiveValueOrNullFor(null));
 }

 @Test
 public void testPrimitiveValueOrNullForAllPrimitives() {
 for (Map.Entry<Class<?>, Object> entry : Primitives.primitiveValues.entrySet()) {
 Class<?> inputType = entry.getKey();
 Object expectedResult = entry.getValue();
 Object result = Primitives.primitiveValueOrNullFor(inputType);
 Assert.assertEquals(expectedResult, result);
 }
 }

 @Test(expected = NullPointerException.class)
 public void testPrimitiveTypeOfNullInput() {
 Primitives.primitiveTypeOf(null);
 }

 @Test(expected = NullPointerException.class)
 public void testIsPrimitiveWrapperForNullInput() {
 Primitives.isPrimitiveWrapper(null);
 }

 @Test(expected = NullPointerException.class)
 public void testPrimitiveWrapperOfNullInput() {
 Primitives.primitiveWrapperOf(null);
 }

 @Test(expected = NullPointerException.class)
 public void testPrimitiveValueOrNullForNullInputType() {
 Primitives.primitiveValueOrNullFor(null);
 }
}