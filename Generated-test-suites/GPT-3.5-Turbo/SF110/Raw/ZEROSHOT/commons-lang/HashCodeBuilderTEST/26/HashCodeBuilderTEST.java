import org.junit.Assert;
import org.junit.Test;

public class HashCodeBuilderTest {

    @Test
    public void testAppendBoolean() {
        boolean value = true;
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(value);
        int result = builder.toHashCode();
        Assert.assertNotEquals(0, result);
    }

    @Test
    public void testAppendBooleanArray() {
        boolean[] array = {true, false, true};
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(array);
        int result = builder.toHashCode();
        Assert.assertNotEquals(0, result);
    }

    @Test
    public void testAppendByte() {
        byte value = 10;
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(value);
        int result = builder.toHashCode();
        Assert.assertNotEquals(0, result);
    }

    @Test
    public void testAppendByteArray() {
        byte[] array = {1, 2, 3};
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(array);
        int result = builder.toHashCode();
        Assert.assertNotEquals(0, result);
    }

    @Test
    public void testAppendChar() {
        char value = 'a';
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(value);
        int result = builder.toHashCode();
        Assert.assertNotEquals(0, result);
    }

    @Test
    public void testAppendCharArray() {
        char[] array = {'a', 'b', 'c'};
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(array);
        int result = builder.toHashCode();
        Assert.assertNotEquals(0, result);
    }

    @Test
    public void testAppendDouble() {
        double value = 3.14;
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(value);
        int result = builder.toHashCode();
        Assert.assertNotEquals(0, result);
    }

    @Test
    public void testAppendDoubleArray() {
        double[] array = {1.2, 3.4, 5.6};
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(array);
        int result = builder.toHashCode();
        Assert.assertNotEquals(0, result);
    }

    @Test
    public void testAppendFloat() {
        float value = 2.5f;
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(value);
        int result = builder.toHashCode();
        Assert.assertNotEquals(0, result);
    }

    @Test
    public void testAppendFloatArray() {
        float[] array = {1.0f, 2.0f, 3.0f};
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(array);
        int result = builder.toHashCode();
        Assert.assertNotEquals(0, result);
    }

    @Test
    public void testAppendInt() {
        int value = 100;
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(value);
        int result = builder.toHashCode();
        Assert.assertNotEquals(0, result);
    }

    @Test
    public void testAppendIntArray() {
        int[] array = {1, 2, 3};
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(array);
        int result = builder.toHashCode();
        Assert.assertNotEquals(0, result);
    }

    @Test
    public void testAppendLong() {
        long value = 1000L;
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(value);
        int result = builder.toHashCode();
        Assert.assertNotEquals(0, result);
    }

    @Test
    public void testAppendLongArray() {
        long[] array = {100L, 200L, 300L};
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(array);
        int result = builder.toHashCode();
        Assert.assertNotEquals(0, result);
    }

    @Test
    public void testAppendObject() {
        Object object = new Object();
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(object);
        int result = builder.toHashCode();
        Assert.assertNotEquals(0, result);
    }

    @Test
    public void testAppendObjectArray() {
        Object[] array = {new Object(), new Object(), new Object()};
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(array);
        int result = builder.toHashCode();
        Assert.assertNotEquals(0, result);
    }

    @Test
    public void testAppendShort() {
        short value = 10;
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(value);
        int result = builder.toHashCode();
        Assert.assertNotEquals(0, result);
    }

    @Test
    public void testAppendShortArray() {
        short[] array = {1, 2, 3};
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(array);
        int result = builder.toHashCode();
        Assert.assertNotEquals(0, result);
    }

    @Test
    public void testAppendSuper() {
        int superHashCode = 99;
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.appendSuper(superHashCode);
        int result = builder.toHashCode();
        Assert.assertNotEquals(0, result);
    }
}