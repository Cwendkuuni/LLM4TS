import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.util.Primitives;

public class PrimitivesTest {

  @Test
  public void testPrimitiveTypeOf_returnsPrimitiveType() {
    Assert.assertEquals(boolean.class, Primitives.primitiveTypeOf(Boolean.class));
    Assert.assertEquals(char.class, Primitives.primitiveTypeOf(Character.class));
    Assert.assertEquals(byte.class, Primitives.primitiveTypeOf(Byte.class));
    Assert.assertEquals(short.class, Primitives.primitiveTypeOf(Short.class));
    Assert.assertEquals(int.class, Primitives.primitiveTypeOf(Integer.class));
    Assert.assertEquals(long.class, Primitives.primitiveTypeOf(Long.class));
    Assert.assertEquals(float.class, Primitives.primitiveTypeOf(Float.class));
    Assert.assertEquals(double.class, Primitives.primitiveTypeOf(Double.class));
  }

  @Test
  public void testPrimitiveTypeOf_returnsWrapperType() {
    Assert.assertEquals(Boolean.class, Primitives.primitiveTypeOf(boolean.class));
    Assert.assertEquals(Character.class, Primitives.primitiveTypeOf(char.class));
    Assert.assertEquals(Byte.class, Primitives.primitiveTypeOf(byte.class));
    Assert.assertEquals(Short.class, Primitives.primitiveTypeOf(short.class));
    Assert.assertEquals(Integer.class, Primitives.primitiveTypeOf(int.class));
    Assert.assertEquals(Long.class, Primitives.primitiveTypeOf(long.class));
    Assert.assertEquals(Float.class, Primitives.primitiveTypeOf(float.class));
    Assert.assertEquals(Double.class, Primitives.primitiveTypeOf(double.class));
  }

  @Test
  public void testIsPrimitiveWrapper_returnsTrue() {
    Assert.assertTrue(Primitives.isPrimitiveWrapper(Boolean.class));
    Assert.assertTrue(Primitives.isPrimitiveWrapper(Character.class));
    Assert.assertTrue(Primitives.isPrimitiveWrapper(Byte.class));
    Assert.assertTrue(Primitives.isPrimitiveWrapper(Short.class));
    Assert.assertTrue(Primitives.isPrimitiveWrapper(Integer.class));
    Assert.assertTrue(Primitives.isPrimitiveWrapper(Long.class));
    Assert.assertTrue(Primitives.isPrimitiveWrapper(Float.class));
    Assert.assertTrue(Primitives.isPrimitiveWrapper(Double.class));
  }

  @Test
  public void testIsPrimitiveWrapper_returnsFalse() {
    Assert.assertFalse(Primitives.isPrimitiveWrapper(boolean.class));
    Assert.assertFalse(Primitives.isPrimitiveWrapper(char.class));
    Assert.assertFalse(Primitives.isPrimitiveWrapper(byte.class));
    Assert.assertFalse(Primitives.isPrimitiveWrapper(short.class));
    Assert.assertFalse(Primitives.isPrimitiveWrapper(int.class));
    Assert.assertFalse(Primitives.isPrimitiveWrapper(long.class));
    Assert.assertFalse(Primitives.isPrimitiveWrapper(float.class));
    Assert.assertFalse(Primitives.isPrimitiveWrapper(double.class));
    Assert.assertFalse(Primitives.isPrimitiveWrapper(String.class));
  }

  @Test
  public void testPrimitiveWrapperOf_returnsWrapperInstance() {
    Assert.assertEquals(Boolean.FALSE, Primitives.primitiveWrapperOf(boolean.class));
    Assert.assertEquals('\u0000', Primitives.primitiveWrapperOf(char.class));
    Assert.assertEquals((byte) 0, Primitives.primitiveWrapperOf(byte.class));
    Assert.assertEquals((short) 0, Primitives.primitiveWrapperOf(short.class));
    Assert.assertEquals(0, Primitives.primitiveWrapperOf(int.class));
    Assert.assertEquals(0L, Primitives.primitiveWrapperOf(long.class));
    Assert.assertEquals(0F, Primitives.primitiveWrapperOf(float.class));
    Assert.assertEquals(0D, Primitives.primitiveWrapperOf(double.class));
  }

  @Test
  public void testPrimitiveValueOrNullFor_returnsWrapperValue() {
    Assert.assertEquals(false, Primitives.primitiveValueOrNullFor(boolean.class));
    Assert.assertEquals('\u0000', Primitives.primitiveValueOrNullFor(char.class));
    Assert.assertEquals((byte) 0, Primitives.primitiveValueOrNullFor(byte.class));
    Assert.assertEquals((short) 0, Primitives.primitiveValueOrNullFor(short.class));
    Assert.assertEquals(0, Primitives.primitiveValueOrNullFor(int.class));
    Assert.assertEquals(0L, Primitives.primitiveValueOrNullFor(long.class));
    Assert.assertEquals(0F, Primitives.primitiveValueOrNullFor(float.class), 0.0);
    Assert.assertEquals(0D, Primitives.primitiveValueOrNullFor(double.class), 0.0);
  }
  
}