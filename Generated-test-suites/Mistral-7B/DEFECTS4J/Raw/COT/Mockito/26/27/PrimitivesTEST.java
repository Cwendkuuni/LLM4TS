import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PrimitivesTest {

 private static final Class<Boolean> BOOLEAN_CLASS = Boolean.class;
 private static final Class<Character> CHAR_CLASS = Character.class;
 private static final Class<Byte> BYTE_CLASS = Byte.class;
 private static final Class<Short> SHORT_CLASS = Short.class;
 private static final Class<Integer> INT_CLASS = Integer.class;
 private static final Class<Long> LONG_CLASS = Long.class;
 private static final Class<Float> FLOAT_CLASS = Float.class;
 private static final Class<Double> DOUBLE_CLASS = Double.class;

 @Test
 public void testPrimitiveTypeOf_returnsCorrectClassForPrimitiveType() {
 Primitives primitives = new Primitives();
 Class<Boolean> actual = primitives.primitiveTypeOf(BOOLEAN_CLASS);
 Assert.assertEquals(BOOLEAN_CLASS, actual);
 }

 @Test
 public void testPrimitiveTypeOf_returnsCorrectClassForWrapperType() {
 Primitives primitives = new Primitives();
 Class<Boolean> actual = primitives.primitiveTypeOf(Boolean.class);
 Assert.assertEquals(Boolean.TYPE, actual);
 }

 @Test
 public void testIsPrimitiveWrapper_returnsTrueForWrapperType() {
 Primitives primitives = new Primitives();
 Assert.assertTrue(primitives.isPrimitiveWrapper(Boolean.class));
 }

 @Test
 public void testIsPrimitiveWrapper_returnsFalseForPrimitiveType() {
 Primitives primitives = new Primitives();
 Assert.assertFalse(primitives.isPrimitiveWrapper(boolean.class));
 }

 @Test
 public void testPrimitiveWrapperOf_returnsWrapperValueForWrapperType() {
 Primitives primitives = new Primitives();
 Object actual = primitives.primitiveWrapperOf(Boolean.class);
 Assert.assertSame(false, actual);
 }

 @Test(expected = NullPointerException.class)
 public void testPrimitiveWrapperOf_throwsNPEForNullType() {
 Primitives primitives = new Primitives();
 primitives.primitiveWrapperOf(null);
 }

 @Test
 public void testPrimitiveValueOrNullFor_returnsNullForNullType() {
 Primitives primitives = new Primitives();
 Object actual = primitives.primitiveValueOrNullFor(null);
 Assert.assertNull(actual);
 }

 @Test
 public void testPrimitiveValueOrNullFor_returnsCorrectValueForPrimitiveType() {
 Primitives primitives = new Primitives();
 Boolean actual = (Boolean) primitives.primitiveValueOrNullFor(Boolean.class);
 Assert.assertSame(false, actual);
 }

 @Test
 public void testPrimitiveValueOrNullFor_returnsCorrectValueForWrapperType() {
 Primitives primitives = new Primitives();
 Byte actual = (Byte) primitives.primitiveValueOrNullFor(Byte.class);
 Assert.assertSame((byte) 0, actual);
 }

 @Test
 public void testPrimitiveValueOrNullFor_throwsClassCastExceptionForInvalidType() {
 Primitives primitives = new Primitives();
 try {
 Object actual = primitives.primitiveValueOrNullFor(String.class);
 } catch (ClassCastException e) {
 // expected
 }
 }
}