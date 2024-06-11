import static org.junit.Assert.*;
import org.junit.Test;

public class HashCodeBuilderTest {

    @Test
    public void testReflectionHashCode() {
        // Test reflectionHashCode method with all default values
        int hashCode = HashCodeBuilder.reflectionHashCode(17, 37, new Object());
        assertEquals(17, hashCode);
    }

    @Test
    public void testReflectionHashCodeWithTransients() {
        // Test reflectionHashCode method with testTransients set to true
        int hashCode = HashCodeBuilder.reflectionHashCode(17, 37, new Object(), true);
        assertEquals(17, hashCode);
    }

    @Test
    public void testReflectionHashCodeWithExcludeFields() {
        // Test reflectionHashCode method with excludeFields
        int hashCode = HashCodeBuilder.reflectionHashCode(17, 37, new Object(), false, Object.class, "field1", "field2");
        assertEquals(17, hashCode);
    }

    @Test
    public void testReflectionHashCodeWithNullObject() {
        // Test reflectionHashCode method with null object
        try {
            HashCodeBuilder.reflectionHashCode(17, 37, null, false, null);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("The object to build a hash code for must not be null", e.getMessage());
        }
    }

    @Test
    public void testReflectionHashCodeWithExcludeFieldsArray() {
        // Test reflectionHashCode method with excludeFields array
        int hashCode = HashCodeBuilder.reflectionHashCode(new Object(), new String[] {"field1", "field2"});
        assertEquals(17, hashCode);
    }

    @Test
    public void testHashCodeBuilder() {
        HashCodeBuilder builder = new HashCodeBuilder();
        int hashCode = builder.toHashCode();
        assertEquals(17, hashCode);
    }

    @Test
    public void testHashCodeBuilderWithInitialAndMultiplier() {
        HashCodeBuilder builder = new HashCodeBuilder(5, 7);
        int hashCode = builder.toHashCode();
        assertEquals(5, hashCode);
    }

    @Test
    public void testAppendBoolean() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(true);
        int hashCode = builder.toHashCode();
        assertEquals(37, hashCode);
    }

    @Test
    public void testAppendBooleanArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new boolean[] {true, false});
        int hashCode = builder.toHashCode();
        assertEquals(74, hashCode);
    }

    @Test
    public void testAppendByte() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((byte) 8);
        int hashCode = builder.toHashCode();
        assertEquals(72, hashCode);
    }

    @Test
    public void testAppendByteArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new byte[] {8, 9});
        int hashCode = builder.toHashCode();
        assertEquals(74, hashCode);
    }

    @Test
    public void testAppendChar() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append('a');
        int hashCode = builder.toHashCode();
        assertEquals(136, hashCode);
    }

    @Test
    public void testAppendCharArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new char[] {'a', 'b'});
        int hashCode = builder.toHashCode();
        assertEquals(34304, hashCode);
    }

    @Test
    public void testAppendDouble() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(1.2);
        int hashCode = builder.toHashCode();
        assertEquals(-1646405766, hashCode);
    }

    @Test
    public void testAppendDoubleArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new double[] {1.2, 3.4});
        int hashCode = builder.toHashCode();
        assertEquals(1871501608, hashCode);
    }

    @Test
    public void testAppendFloat() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(1.2f);
        int hashCode = builder.toHashCode();
        assertEquals(-2141708354, hashCode);
    }

    @Test
    public void testAppendFloatArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new float[] {1.2f, 3.4f});
        int hashCode = builder.toHashCode();
        assertEquals(-1894915954, hashCode);
    }

    @Test
    public void testAppendInt() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(5);
        int hashCode = builder.toHashCode();
        assertEquals(186, hashCode);
    }

    @Test
    public void testAppendIntArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new int[] {5, 6});
        int hashCode = builder.toHashCode();
        assertEquals(418, hashCode);
    }

    @Test
    public void testAppendLong() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(123456L);
        int hashCode = builder.toHashCode();
        assertEquals(1192923, hashCode);
    }

    @Test
    public void testAppendLongArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new long[] {123456L, 789012L});
        int hashCode = builder.toHashCode();
        assertEquals(-888941679, hashCode);
    }

    @Test
    public void testAppendObject() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new Object());
        int hashCode = builder.toHashCode();
        assertEquals(17, hashCode);
    }

    @Test
    public void testAppendObjectArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new Object[] {new Object(), new Object()});
        int hashCode = builder.toHashCode();
        assertEquals(1015, hashCode);
    }

    @Test
    public void testAppendShort() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((short) 3);
        int hashCode = builder.toHashCode();
        assertEquals(114, hashCode);
    }

    @Test
    public void testAppendShortArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new short[] {3, 4});
        int hashCode = builder.toHashCode();
        assertEquals(241, hashCode);
    }

    @Test
    public void testAppendSuper() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.appendSuper(123);
        int hashCode = builder.toHashCode();
        assertEquals(2097, hashCode);
    }

    @Test
    public void testBuild() {
        HashCodeBuilder builder = new HashCodeBuilder();
        assertEquals(17, builder.build().intValue());
    }

    @Test
    public void testHashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        assertEquals(17, builder.hashCode());
    }
}