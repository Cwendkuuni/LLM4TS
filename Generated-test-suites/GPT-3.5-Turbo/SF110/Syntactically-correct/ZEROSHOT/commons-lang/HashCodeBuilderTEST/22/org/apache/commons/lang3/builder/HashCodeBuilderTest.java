import org.junit.Test;
import static org.junit.Assert.*;

public class HashCodeBuilderTest {

    @Test
    public void testReflectionHashCode() {
        int hashCode = HashCodeBuilder.reflectionHashCode(17, 37, null);
        assertEquals(17, hashCode);
    }

    @Test
    public void testReflectionHashCode_WithObject() {
        Object object = new Object();
        int hashCode = HashCodeBuilder.reflectionHashCode(object, true);
        assertEquals(object.hashCode(), hashCode);
    }

    @Test
    public void testReflectionHashCode_WithExcludeFields() {
        Object object = new Object();
        int hashCode = HashCodeBuilder.reflectionHashCode(object, "excludeField");
        assertEquals(object.hashCode(), hashCode);
    }

    @Test
    public void testAppend_WithBoolean() {
        boolean value = true;
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(value);
        assertEquals(37, builder.toHashCode());
    }

    @Test
    public void testAppend_WithBooleanArray() {
        boolean[] array = {true, false};
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(array);
        assertEquals(74, builder.toHashCode());
    }

    @Test
    public void testAppend_WithByte() {
        byte value = 1;
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(value);
        assertEquals(38, builder.toHashCode());
    }

    @Test
    public void testAppend_WithByteArray() {
        byte[] array = {1, 2, 3};
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(array);
        assertEquals(115, builder.toHashCode());
    }

    @Test
    public void testAppend_WithChar() {
        char value = 'a';
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(value);
        assertEquals(103, builder.toHashCode());
    }

    @Test
    public void testAppend_WithCharArray() {
        char[] array = {'a', 'b', 'c'};
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(array);
        assertEquals(311, builder.toHashCode());
    }

    @Test
    public void testAppend_WithDouble() {
        double value = 1.5;
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(value);
        assertEquals(922335, builder.toHashCode());
    }

    @Test
    public void testAppend_WithDoubleArray() {
        double[] array = {1.5, 2.5, 3.5};
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(array);
        assertEquals(2767009, builder.toHashCode());
    }

    @Test
    public void testAppend_WithFloat() {
        float value = 1.5f;
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(value);
        assertEquals(69, builder.toHashCode());
    }

    @Test
    public void testAppend_WithFloatArray() {
        float[] array = {1.5f, 2.5f, 3.5f};
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(array);
        assertEquals(207, builder.toHashCode());
    }

    @Test
    public void testAppend_WithInt() {
        int value = 5;
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(value);
        assertEquals(192, builder.toHashCode());
    }

    @Test
    public void testAppend_WithIntArray() {
        int[] array = {1, 2, 3};
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(array);
        assertEquals(636, builder.toHashCode());
    }

    @Test
    public void testAppend_WithLong() {
        long value = 1234567890L;
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(value);
        assertEquals(1886444514, builder.toHashCode());
    }

    @Test
    public void testAppend_WithLongArray() {
        long[] array = {1L, 2L, 3L};
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(array);
        assertEquals(5685772, builder.toHashCode());
    }

    @Test
    public void testAppend_WithObject() {
        Object object = new Object();
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(object);
        assertEquals(object.hashCode(), builder.toHashCode());
    }

    @Test
    public void testAppend_WithObjectArray() {
        Object[] array = {new Object(), new Object(), new Object()};
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(array);
        assertEquals(161277183, builder.toHashCode());
    }

    @Test
    public void testAppend_WithShort() {
        short value = 1;
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(value);
        assertEquals(55, builder.toHashCode());
    }

    @Test
    public void testAppend_WithShortArray() {
        short[] array = {1, 2, 3};
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(array);
        assertEquals(236, builder.toHashCode());
    }

    @Test
    public void testAppendSuper() {
        int superHashCode = 123;
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.appendSuper(superHashCode);
        assertEquals(superHashCode, builder.toHashCode());
    }

    @Test
    public void testBuild() {
        HashCodeBuilder builder = new HashCodeBuilder();
        Integer hashCode = builder.build();
        assertEquals(builder.toHashCode(), hashCode.intValue());
    }

    @Test
    public void testHashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        int hashCode = builder.hashCode();
        assertEquals(builder.toHashCode(), hashCode);
    }

}