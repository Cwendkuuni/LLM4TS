import org.junit.Test;
import java.util.Arrays;
import static org.junit.Assert.*;

public class HashCodeBuilderTest {

    @Test
    public void testHashCodeBuilderDefaultConstructor() {
        HashCodeBuilder builder = new HashCodeBuilder();
        assertNotNull(builder);
    }
    
    @Test
    public void testHashCodeBuilderConstructor() {
        HashCodeBuilder builder = new HashCodeBuilder(7, 13);
        assertNotNull(builder);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testHashCodeBuilderConstructorWithEvenInitialNumber() {
        HashCodeBuilder builder = new HashCodeBuilder(6, 13);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testHashCodeBuilderConstructorWithEvenMultiplierNumber() {
        HashCodeBuilder builder = new HashCodeBuilder(7, 14);
    }
    
    @Test
    public void testAppendBoolean() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(true);
        assertEquals(53, builder.toHashCode());
        builder.append(false);
        assertEquals(54, builder.toHashCode());
    }
    
    @Test
    public void testAppendBooleanArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        boolean[] array = new boolean[] {true, false};
        builder.append(array);
        assertEquals(50, builder.toHashCode());
    }
    
    @Test
    public void testAppendBooleanArrayWithNull() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((boolean[])null);
        assertEquals(17, builder.toHashCode());
    }
    
    @Test
    public void testAppendByte() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((byte)10);
        assertEquals(447, builder.toHashCode());
    }
    
    @Test
    public void testAppendByteArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        byte[] array = new byte[] {1, 2, 3};
        builder.append(array);
        assertEquals(3096, builder.toHashCode());
    }
    
    @Test
    public void testAppendByteArrayWithNull() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((byte[])null);
        assertEquals(17, builder.toHashCode());
    }
    
    @Test
    public void testAppendChar() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append('a');
        assertEquals(228, builder.toHashCode());
    }
    
    @Test
    public void testAppendCharArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        char[] array = new char[] {'a', 'b', 'c'};
        builder.append(array);
        assertEquals(1982, builder.toHashCode());
    }
    
    @Test
    public void testAppendCharArrayWithNull() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((char[])null);
        assertEquals(17, builder.toHashCode());
    }
    
    @Test
    public void testAppendDouble() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(1.5);
        assertEquals(545, builder.toHashCode());
    }
    
    @Test
    public void testAppendDoubleArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        double[] array = new double[] {1.1, 2.2, 3.3};
        builder.append(array);
        assertEquals(5656, builder.toHashCode());
    }
    
    @Test
    public void testAppendDoubleArrayWithNull() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((double[])null);
        assertEquals(17, builder.toHashCode());
    }
    
    @Test
    public void testAppendFloat() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(1.5f);
        assertEquals(49, builder.toHashCode());
    }
    
    @Test
    public void testAppendFloatArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        float[] array = new float[] {1.1f, 2.2f, 3.3f};
        builder.append(array);
        assertEquals(436, builder.toHashCode());
    }
    
    @Test
    public void testAppendFloatArrayWithNull() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((float[])null);
        assertEquals(17, builder.toHashCode());
    }
    
    @Test
    public void testAppendInt() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(10);
        assertEquals(387, builder.toHashCode());
    }
    
    @Test
    public void testAppendIntArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        int[] array = new int[] {1, 2, 3};
        builder.append(array);
        assertEquals(3340, builder.toHashCode());
    }
    
    @Test
    public void testAppendIntArrayWithNull() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((int[])null);
        assertEquals(17, builder.toHashCode());
    }
    
    @Test
    public void testAppendLong() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(10L);
        assertEquals(387, builder.toHashCode());
    }
    
    @Test
    public void testAppendLongArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        long[] array = new long[] {1L, 2L, 3L};
        builder.append(array);
        assertEquals(3340, builder.toHashCode());
    }
    
    @Test
    public void testAppendLongArrayWithNull() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((long[])null);
        assertEquals(17, builder.toHashCode());
    }
    
    @Test
    public void testAppendNull() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((Object)null);
        assertEquals(17, builder.toHashCode());
    }
    
    @Test
    public void testAppendObjectArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        Object[] array = new Object[] {1, "abc", new Object()};
        builder.append(array);
        assertEquals(17, builder.toHashCode());
    }
    
    @Test
    public void testAppendObjectArrayWithNull() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((Object[])null);
        assertEquals(17, builder.toHashCode());
    }
    
    @Test
    public void testAppendShort() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((short)10);
        assertEquals(367, builder.toHashCode());
    }
    
    @Test
    public void testAppendShortArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        short[] array = new short[] {1, 2, 3};
        builder.append(array);
        assertEquals(3635, builder.toHashCode());
    }
    
    @Test
    public void testAppendShortArrayWithNull() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((short[])null);
        assertEquals(17, builder.toHashCode());
    }
    
    @Test
    public void testAppendSuper() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.appendSuper(12345);
        assertEquals(482217, builder.toHashCode());
    }
    
    @Test
    public void testBuild() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(10);
        Integer hashCode = builder.build();
        assertNotNull(hashCode);
        assertEquals(387, hashCode.intValue());
    }
    
    @Test
    public void testToHashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(10);
        assertEquals(387, builder.toHashCode());
    }
}