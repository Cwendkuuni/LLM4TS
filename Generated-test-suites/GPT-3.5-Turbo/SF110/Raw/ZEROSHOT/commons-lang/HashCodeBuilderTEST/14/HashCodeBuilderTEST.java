import org.junit.Assert;
import org.junit.Test;

public class HashCodeBuilderTest {

    @Test
    public void testAppendBoolean() {
        HashCodeBuilder builder = new HashCodeBuilder();
        boolean value = true;
        builder.append(value);
        int result = builder.toHashCode();
        Assert.assertEquals(38, result);
    }

    @Test
    public void testAppendBooleanArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        boolean[] array = {true, false, true};
        builder.append(array);
        int result = builder.toHashCode();
        Assert.assertEquals(43, result);
    }

    @Test
    public void testAppendByte() {
        HashCodeBuilder builder = new HashCodeBuilder();
        byte value = 25;
        builder.append(value);
        int result = builder.toHashCode();
        Assert.assertEquals(1042, result);
    }

    @Test
    public void testAppendByteArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        byte[] array = {1, 2, 3};
        builder.append(array);
        int result = builder.toHashCode();
        Assert.assertEquals(1581, result);
    }

    @Test
    public void testAppendChar() {
        HashCodeBuilder builder = new HashCodeBuilder();
        char value = 'a';
        builder.append(value);
        int result = builder.toHashCode();
        Assert.assertEquals(98, result);
    }

    @Test
    public void testAppendCharArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        char[] array = {'a', 'b', 'c'};
        builder.append(array);
        int result = builder.toHashCode();
        Assert.assertEquals(5235, result);
    }

    @Test
    public void testAppendDouble() {
        HashCodeBuilder builder = new HashCodeBuilder();
        double value = 3.14;
        builder.append(value);
        int result = builder.toHashCode();
        Assert.assertEquals(-2065307695, result);
    }

    @Test
    public void testAppendDoubleArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        double[] array = {1.1, 2.2, 3.3};
        builder.append(array);
        int result = builder.toHashCode();
        Assert.assertEquals(-110727526, result);
    }

    @Test
    public void testAppendFloat() {
        HashCodeBuilder builder = new HashCodeBuilder();
        float value = 1.23f;
        builder.append(value);
        int result = builder.toHashCode();
        Assert.assertEquals(-1738571909, result);
    }

    @Test
    public void testAppendFloatArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        float[] array = {1.1f, 2.2f, 3.3f};
        builder.append(array);
        int result = builder.toHashCode();
        Assert.assertEquals(-948800949, result);
    }

    @Test
    public void testAppendInt() {
        HashCodeBuilder builder = new HashCodeBuilder();
        int value = 42;
        builder.append(value);
        int result = builder.toHashCode();
        Assert.assertEquals(1589, result);
    }

    @Test
    public void testAppendIntArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        int[] array = {1, 2, 3};
        builder.append(array);
        int result = builder.toHashCode();
        Assert.assertEquals(6995, result);
    }

    @Test
    public void testAppendLong() {
        HashCodeBuilder builder = new HashCodeBuilder();
        long value = 1234567890L;
        builder.append(value);
        int result = builder.toHashCode();
        Assert.assertEquals(-1743222524, result);
    }

    @Test
    public void testAppendLongArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        long[] array = {1L, 2L, 3L};
        builder.append(array);
        int result = builder.toHashCode();
        Assert.assertEquals(1994392008, result);
    }

    @Test
    public void testAppendObject() {
        HashCodeBuilder builder = new HashCodeBuilder();
        Object value = new Object();
        builder.append(value);
        int result = builder.toHashCode();
        Assert.assertEquals(17, result);
    }

    @Test
    public void testAppendObjectArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        Object[] array = {new Object(), new Object(), new Object()};
        builder.append(array);
        int result = builder.toHashCode();
        Assert.assertEquals(1552058572, result);
    }

    @Test
    public void testAppendShort() {
        HashCodeBuilder builder = new HashCodeBuilder();
        short value = 123;
        builder.append(value);
        int result = builder.toHashCode();
        Assert.assertEquals(4746, result);
    }

    @Test
    public void testAppendShortArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        short[] array = {1, 2, 3};
        builder.append(array);
        int result = builder.toHashCode();
        Assert.assertEquals(7190, result);
    }

    @Test
    public void testAppendSuper() {
        HashCodeBuilder builder = new HashCodeBuilder();
        int superHashCode = 1234567890;
        builder.appendSuper(superHashCode);
        int result = builder.toHashCode();
        Assert.assertEquals(1234567927, result);
    }
}