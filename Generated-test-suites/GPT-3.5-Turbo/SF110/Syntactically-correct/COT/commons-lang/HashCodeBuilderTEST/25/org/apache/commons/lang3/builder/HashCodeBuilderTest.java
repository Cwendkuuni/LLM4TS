import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.junit.Test;

public class HashCodeBuilderTest {

    @Test
    public void testConstructor() {
        HashCodeBuilder builder = new HashCodeBuilder();
        // test code
    }

    @Test
    public void testConstructorWithValues() {
        HashCodeBuilder builder = new HashCodeBuilder(17, 37);
        // test code
    }

    @Test
    public void testAppendBoolean() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(true);
        // test code
    }

    @Test
    public void testAppendBooleanArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new boolean[]{true, false});
        // test code
    }

    @Test
    public void testAppendByte() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((byte) 1);
        // test code
    }

    @Test
    public void testAppendByteArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new byte[]{1, 2});
        // test code
    }

    @Test
    public void testAppendChar() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append('a');
        // test code
    }

    @Test
    public void testAppendCharArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new char[]{'a', 'b'});
        // test code
    }

    @Test
    public void testAppendDouble() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(1.5);
        // test code
    }

    @Test
    public void testAppendDoubleArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new double[]{1.5, 2.5});
        // test code
    }

    @Test
    public void testAppendFloat() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(1.5f);
        // test code
    }

    @Test
    public void testAppendFloatArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new float[]{1.5f, 2.5f});
        // test code
    }

    @Test
    public void testAppendInt() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(123);
        // test code
    }

    @Test
    public void testAppendIntArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new int[]{123, 456});
        // test code
    }

    @Test
    public void testAppendLong() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(123L);
        // test code
    }

    @Test
    public void testAppendLongArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new long[]{123L, 456L});
        // test code
    }

    @Test
    public void testAppendObject() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append("test");
        // test code
    }

    @Test
    public void testAppendObjectArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new String[]{"test1", "test2"});
        // test code
    }

    @Test
    public void testAppendShort() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((short) 123);
        // test code
    }

    @Test
    public void testAppendShortArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new short[]{123, 456});
        // test code
    }

    @Test
    public void testAppendSuper() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.appendSuper(123);
        // test code
    }

    @Test
    public void testToHashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        int hashcode = builder.toHashCode();
        // test code
    }

    @Test
    public void testBuild() {
        HashCodeBuilder builder = new HashCodeBuilder();
        Integer hashcode = builder.build();
        // test code
    }

    @Test
    public void testHashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        int hashcode = builder.hashCode();
        // test code
    }

    @Test
    public void testReflectionHashCode() {
        int hashcode = HashCodeBuilder.reflectionHashCode(17, 37, new Object());
        // test code
    }
}