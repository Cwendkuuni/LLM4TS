import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.util.Primitives;

public class PrimitivesTest {

    @Test
    public void testPrimitiveTypeOf() {
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
    public void testIsPrimitiveWrapper() {
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
    public void testPrimitiveWrapperOf() {
        Assert.assertEquals(false, Primitives.primitiveWrapperOf(Boolean.class));
        Assert.assertEquals('\u0000', Primitives.primitiveWrapperOf(Character.class));
        Assert.assertEquals((byte) 0, Primitives.primitiveWrapperOf(Byte.class));
        Assert.assertEquals((short) 0, Primitives.primitiveWrapperOf(Short.class));
        Assert.assertEquals(0, Primitives.primitiveWrapperOf(Integer.class));
        Assert.assertEquals(0L, Primitives.primitiveWrapperOf(Long.class));
        Assert.assertEquals(0F, Primitives.primitiveWrapperOf(Float.class));
        Assert.assertEquals(0D, Primitives.primitiveWrapperOf(Double.class));
    }

    @Test
    public void testPrimitiveValueOrNullFor() {
        Assert.assertEquals(false, Primitives.primitiveValueOrNullFor(boolean.class));
        Assert.assertEquals('\u0000', Primitives.primitiveValueOrNullFor(char.class));
        Assert.assertEquals((byte) 0, Primitives.primitiveValueOrNullFor(byte.class));
        Assert.assertEquals((short) 0, Primitives.primitiveValueOrNullFor(short.class));
        Assert.assertEquals(0, Primitives.primitiveValueOrNullFor(int.class));
        Assert.assertEquals(0L, Primitives.primitiveValueOrNullFor(long.class));
        Assert.assertEquals(0F, Primitives.primitiveValueOrNullFor(float.class));
        Assert.assertEquals(0, Primitives.primitiveValueOrNullFor(double.class));
    }

}