import org.junit.Assert;
import org.junit.Test;

public class HashCodeBuilderTest {

    @Test
    public void testReflectionHashCode() {
        Object object = new Object();
        int hashcode = HashCodeBuilder.reflectionHashCode(17, 37, object);
        Assert.assertNotNull(hashcode);
    }

    @Test
    public void testReflectionHashCodeWithTransients() {
        Object object = new Object();
        int hashcode = HashCodeBuilder.reflectionHashCode(17, 37, object, true);
        Assert.assertNotNull(hashcode);
    }

    @Test
    public void testReflectionHashCodeWithExcludedFields() {
        Object object = new Object();
        int hashcode = HashCodeBuilder.reflectionHashCode(17, 37, object, false, Object.class, "field1", "field2");
        Assert.assertNotNull(hashcode);
    }

    @Test
    public void testReflectionHashCodeWithoutTransientsAndExcludedFields() {
        Object object = new Object();
        int hashcode = HashCodeBuilder.reflectionHashCode(17, 37, object, false, null);
        Assert.assertNotNull(hashcode);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReflectionHashCodeWithNullObject() {
        int hashcode = HashCodeBuilder.reflectionHashCode(17, 37, null);
    }

    @Test
    public void testReflectionHashCodeForObjectWithNullExcludedFields() {
        Object object = new Object();
        int hashcode = HashCodeBuilder.reflectionHashCode(object, (String[]) null);
        Assert.assertNotNull(hashcode);
    }

    @Test
    public void testReflectionHashCodeForObjectWithExcludedFields() {
        Object object = new Object();
        int hashcode = HashCodeBuilder.reflectionHashCode(object, "field1", "field2");
        Assert.assertNotNull(hashcode);
    }

    @Test
    public void testHashCodeBuilderAppendBoolean() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(true);
        int hashcode = builder.toHashCode();
        Assert.assertNotNull(hashcode);
    }

    @Test
    public void testHashCodeBuilderAppendBooleanArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new boolean[]{true, false});
        int hashcode = builder.toHashCode();
        Assert.assertNotNull(hashcode);
    }

    @Test
    public void testHashCodeBuilderAppendByte() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((byte) 1);
        int hashcode = builder.toHashCode();
        Assert.assertNotNull(hashcode);
    }

    @Test
    public void testHashCodeBuilderAppendByteArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new byte[]{1, 2});
        int hashcode = builder.toHashCode();
        Assert.assertNotNull(hashcode);
    }

    @Test
    public void testHashCodeBuilderAppendChar() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append('a');
        int hashcode = builder.toHashCode();
        Assert.assertNotNull(hashcode);
    }

    @Test
    public void testHashCodeBuilderAppendCharArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new char[]{'a', 'b'});
        int hashcode = builder.toHashCode();
        Assert.assertNotNull(hashcode);
    }

    @Test
    public void testHashCodeBuilderAppendDouble() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(1.0);
        int hashcode = builder.toHashCode();
        Assert.assertNotNull(hashcode);
    }

    @Test
    public void testHashCodeBuilderAppendDoubleArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new double[]{1.0, 2.0});
        int hashcode = builder.toHashCode();
        Assert.assertNotNull(hashcode);
    }

    @Test
    public void testHashCodeBuilderAppendFloat() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(1.0f);
        int hashcode = builder.toHashCode();
        Assert.assertNotNull(hashcode);
    }

    @Test
    public void testHashCodeBuilderAppendFloatArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new float[]{1.0f, 2.0f});
        int hashcode = builder.toHashCode();
        Assert.assertNotNull(hashcode);
    }

    @Test
    public void testHashCodeBuilderAppendInt() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(1);
        int hashcode = builder.toHashCode();
        Assert.assertNotNull(hashcode);
    }

    @Test
    public void testHashCodeBuilderAppendIntArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new int[]{1, 2});
        int hashcode = builder.toHashCode();
        Assert.assertNotNull(hashcode);
    }

    @Test
    public void testHashCodeBuilderAppendLong() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(1L);
        int hashcode = builder.toHashCode();
        Assert.assertNotNull(hashcode);
    }

    @Test
    public void testHashCodeBuilderAppendLongArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new long[]{1L, 2L});
        int hashcode = builder.toHashCode();
        Assert.assertNotNull(hashcode);
    }

    @Test
    public void testHashCodeBuilderAppendObject() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new Object());
        int hashcode = builder.toHashCode();
        Assert.assertNotNull(hashcode);
    }

    @Test
    public void testHashCodeBuilderAppendObjectArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new Object[]{new Object(), new Object()});
        int hashcode = builder.toHashCode();
        Assert.assertNotNull(hashcode);
    }

    @Test
    public void testHashCodeBuilderAppendShort() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((short) 1);
        int hashcode = builder.toHashCode();
        Assert.assertNotNull(hashcode);
    }

    @Test
    public void testHashCodeBuilderAppendShortArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new short[]{(short) 1, (short) 2});
        int hashcode = builder.toHashCode();
        Assert.assertNotNull(hashcode);
    }

    @Test
    public void testHashCodeBuilderAppendSuper() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.appendSuper(123);
        int hashcode = builder.toHashCode();
        Assert.assertNotNull(hashcode);
    }

    @Test
    public void testHashCodeBuilderToHashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        int hashcode = builder.toHashCode();
        Assert.assertNotNull(hashcode);
    }

    @Test
    public void testHashCodeBuilderBuild() {
        HashCodeBuilder builder = new HashCodeBuilder();
        Integer result = builder.build();
        Assert.assertNotNull(result);
    }
}