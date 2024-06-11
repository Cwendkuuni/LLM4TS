import org.junit.Test;
import static org.junit.Assert.*;

public class HashCodeBuilderTest {
    
    @Test
    public void testAppendBoolean() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(true);
        int result = builder.toHashCode();
        assertEquals(38, result);   
    }
    
    @Test
    public void testAppendBooleanArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        boolean[] array = {true, false, true};
        builder.append(array);
        int result = builder.toHashCode();
        assertEquals(40, result);
    }
    
    @Test
    public void testAppendByte() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((byte) 5);
        int result = builder.toHashCode();
        assertEquals(47, result);
    }
    
    @Test
    public void testAppendByteArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        byte[] array = {1, 2, 3};
        builder.append(array);
        int result = builder.toHashCode();
        assertEquals(71, result);
    }
    
    @Test
    public void testAppendChar() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append('a');
        int result = builder.toHashCode();
        assertEquals(134, result);
    }
    
    @Test
    public void testAppendCharArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        char[] array = {'a', 'b', 'c'};
        builder.append(array);
        int result = builder.toHashCode();
        assertEquals(192, result);
    }
    
    @Test
    public void testAppendDouble() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(5.5);
        int result = builder.toHashCode();
        assertEquals(1925105431, result);
    }
    
    @Test
    public void testAppendDoubleArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        double[] array = {1.1, 2.2, 3.3};
        builder.append(array);
        int result = builder.toHashCode();
        assertEquals(1205279927, result);
    }
    
    @Test
    public void testAppendFloat() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(5.5f);
        int result = builder.toHashCode();
        assertEquals(1127220959, result);
    }
    
    @Test
    public void testAppendFloatArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        float[] array = {1.1f, 2.2f, 3.3f};
        builder.append(array);
        int result = builder.toHashCode();
        assertEquals(1084001667, result);
    }
    
    @Test
    public void testAppendInt() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(5);
        int result = builder.toHashCode();
        assertEquals(186, result);
    }
    
    @Test
    public void testAppendIntArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        int[] array = {1, 2, 3};
        builder.append(array);
        int result = builder.toHashCode();
        assertEquals(2421719, result);
    }
    
    @Test
    public void testAppendLong() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(5L);
        int result = builder.toHashCode();
        assertEquals(134, result);
    }
    
    @Test
    public void testAppendLongArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        long[] array = {1L, 2L, 3L};
        builder.append(array);
        int result = builder.toHashCode();
        assertEquals(228627967, result);
    }
    
    @Test
    public void testAppendObject() {
        HashCodeBuilder builder = new HashCodeBuilder();
        Object obj = new Object();
        builder.append(obj);
        int result = builder.toHashCode();
        assertEquals(-1932431053, result);
    }
    
    @Test
    public void testAppendObjectArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        Object[] array = {"a", "b", "c"};
        builder.append(array);
        int result = builder.toHashCode();
        assertEquals(1924941138, result);
    }
    
    @Test
    public void testAppendShort() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((short) 5);
        int result = builder.toHashCode();
        assertEquals(47, result);
    }
    
    @Test
    public void testAppendShortArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        short[] array = {1, 2, 3};
        builder.append(array);
        int result = builder.toHashCode();
        assertEquals(71, result);
    }
    
    @Test
    public void testAppendSuper() {
        HashCodeBuilder builder = new HashCodeBuilder();
        int superHashCode = 123;
        builder.appendSuper(superHashCode);
        int result = builder.toHashCode();
        assertEquals(4464, result);
    }
}