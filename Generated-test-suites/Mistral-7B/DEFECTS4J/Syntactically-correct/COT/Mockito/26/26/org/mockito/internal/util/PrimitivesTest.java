import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.util.Primitives;

import java.util.HashMap;
import java.util.Map;

public class PrimitivesTest {

 private Map<Class<?>, Object> testWrapperValues = new HashMap<>();
 private Map<Class<?>, Object> testPrimitiveValues = new HashMap<>();

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 testWrapperValues.putAll(Primitives.wrapperReturnValues);
 testPrimitiveValues.putAll(Primitives.primitiveValues);
 }

 @Test
 public void testPrimitiveTypeOf() {
 Class<Boolean> booleanClass = Boolean.class;
 Class<Boolean> booleanType = Primitives.primitiveTypeOf(booleanClass);
 Assert.assertEquals(Boolean.TYPE, booleanType);

 Class<String> stringClass = String.class;
 Class<String> stringType = Primitives.primitiveTypeOf(stringClass);
 Assert.assertNull(stringType);
 }

 @Test
 public void testIsPrimitiveWrapper() {
 Class<Boolean> booleanWrapper = Boolean.class;
 Assert.assertTrue(Primitives.isPrimitiveWrapper(booleanWrapper));

 Class<String> stringClass = String.class;
 Assert.assertFalse(Primitives.isPrimitiveWrapper(stringClass));
 }

 @Test
 public void testPrimitiveWrapperOf() {
 Class<Boolean> booleanWrapper = Boolean.class;
 Boolean booleanValue = Primitives.primitiveWrapperOf(booleanWrapper);
 Assert.assertEquals(false, booleanValue);

 Class<Character> characterWrapper = Character.class;
 Character characterValue = Primitives.primitiveWrapperOf(characterWrapper);
 Assert.assertEquals('\u0000', characterValue);
 }

 @Test
 public void testPrimitiveValueOrNullFor_boolean() {
 Assert.assertEquals(false, Primitives.primitiveValueOrNullFor(boolean.class));
 }

 @Test
 public void testPrimitiveValueOrNullFor_char() {
 Assert.assertEquals('\u0000', Primitives.primitiveValueOrNullFor(char.class));
 }

 @Test
 public void testPrimitiveValueOrNullFor_byte() {
 Assert.assertEquals((byte) 0, Primitives.primitiveValueOrNullFor(byte.class));
 }

 @Test
 public void testPrimitiveValueOrNullFor_short() {
 Assert.assertEquals((short) 0, Primitives.primitiveValueOrNullFor(short.class));
 }

 @Test
 public void testPrimitiveValueOrNullFor_int() {
 Assert.assertEquals(0, Primitives.primitiveValueOrNullFor(int.class));
 }

 @Test
 public void testPrimitiveValueOrNullFor_long() {
 Assert.assertEquals(0L, Primitives.primitiveValueOrNullFor(long.class));
 }

 @Test
 public void testPrimitiveValueOrNullFor_float() {
 Assert.assertEquals(0F, Primitives.primitiveValueOrNullFor(float.class));
 }

 @Test
 public void testPrimitiveValueOrNullFor_double() {
 Assert.assertEquals(0D, Primitives.primitiveValueOrNullFor(double.class));
 }

 @Test(expected = NullPointerException.class)
 public void testPrimitiveTypeOfNullClass() {
 Primitives.primitiveTypeOf(null);
 }

 @Test(expected = ClassCastException.class)
 public void testIsPrimitiveWrapperClassCastException() {
 Primitives.isPrimitiveWrapper(String.class);
 }

 @Test(expected = ClassCastException.class)
 public void testPrimitiveWrapperOfClassCastException() {
 Primitives.primitiveWrapperOf(String.class);
 }

 @Test(expected = NullPointerException.class)
 public void testPrimitiveValueOrNullForNullClass() {
 Primitives.primitiveValueOrNullFor(null);
 }
}