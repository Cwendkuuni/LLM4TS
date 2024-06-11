import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.util.Primitives;

public class PrimitivesTest {

 private Class<?> booleanWrapperType = Boolean.class;
 private Class<?> charWrapperType = Character.class;
 private Class<?> byteWrapperType = Byte.class;
 private Class<?> shortWrapperType = Short.class;
 private Class<?> intWrapperType = Integer.class;
 private Class<?> longWrapperType = Long.class;
 private Class<?> floatWrapperType = Float.class;
 private Class<?> doubleWrapperType = Double.class;

 @Test
 public void testPrimitiveTypeOf() {
 Class<?> booleanType = Primitives.primitiveTypeOf(Boolean.class);
 Assert.assertEquals(Boolean.TYPE, booleanType);
 Class<?> charType = Primitives.primitiveTypeOf(Character.class);
 Assert.assertEquals(Character.TYPE, charType);
 // Test for non-primitive types
 Class<?> StringType = String.class;
 Class<?> stringType = Primitives.primitiveTypeOf(StringType);
 Assert.assertNull(stringType);
 }

 @Test
 public void testIsPrimitiveWrapper() {
 Assert.assertTrue(Primitives.isPrimitiveWrapper(Boolean.class));
 Assert.assertTrue(Primitives.isPrimitiveWrapper(Character.class));
 Assert.assertTrue(Primitives.isPrimitiveWrapper(Byte.class));
 Assert.assertTrue(Primitives.isPrimitiveWrapper(Short.class));
 Assert.assertTrue(Primitives.isPrimitiveWrapper(Integer.class));
 Assert.assertTrue(Primitives.isPrimitiveWrapper(Long.class));
 Assert.assertTrue(Primitives.isPrimitiveWrapper(Float.class));
 Assert.assertTrue(Primitives.isPrimitiveWrapper(Double.class));
 Assert.assertFalse(Primitives.isPrimitiveWrapper(String.class));
 }

 @Test
 public void testPrimitiveWrapperOf() {
 Object booleanWrapper = Primitives.primitiveWrapperOf(Boolean.class);
 Assert.assertSame(false, booleanWrapper);
 Object charWrapper = Primitives.primitiveWrapperOf(Character.class);
 Assert.assertSame((char) 0, charWrapper);
 // Test for null input
 Assert.assertNull(Primitives.primitiveWrapperOf(null));
 }

 @Test
 public void testPrimitiveValueOrNullFor() {
 Assert.assertSame(false, Primitives.primitiveValueOrNullFor(booleanWrapperType));
 Assert.assertSame((char) 0, Primitives.primitiveValueOrNullFor(charWrapperType));
 // Test for non-primitive types
 Assert.assertNull(Primitives.primitiveValueOrNullFor(String.class));
 }

 @Test(expected = NullPointerException.class)
 public void testPrimitiveTypeOfNullInput() {
 // Test for null input
 Primitives.primitiveTypeOf(null);
 }

 @Test(expected = ClassCastException.class)
 public void testPrimitiveWrapperOfInvalidInput() {
 // Test for invalid input
 Primitives.primitiveWrapperOf(String.class);
 }

 @Test(expected = ClassCastException.class)
 public void testPrimitiveValueOrNullForInvalidInput() {
 // Test for invalid input
 Primitives.primitiveValueOrNullFor(String.class);
 }

}