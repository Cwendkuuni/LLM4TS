import org.junit.Before;
import org.junit.Test;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import static org.junit.Assert.*;

public class HashCodeBuilderTest {
    
    private HashCodeBuilder hashCodeBuilder;
    
    @Before
    public void setUp() {
        hashCodeBuilder = new HashCodeBuilder();
    }
    
    @Test
    public void testAppendBoolean() {
        hashCodeBuilder.append(true);
        assertEquals(34, hashCodeBuilder.toHashCode());
    }
    
    @Test
    public void testAppendBooleanArray() {
        boolean[] array = {true, false, true};
        hashCodeBuilder.append(array);
        assertEquals(119, hashCodeBuilder.toHashCode());
    }
    
    @Test
    public void testAppendByte() {
        hashCodeBuilder.append((byte) 5);
        assertEquals(192, hashCodeBuilder.toHashCode());
    }
    
    @Test
    public void testAppendByteArray() {
        byte[] array = {1, 2, 3};
        hashCodeBuilder.append(array);
        assertEquals(3035, hashCodeBuilder.toHashCode());
    }
    
    @Test
    public void testAppendChar() {
        hashCodeBuilder.append('a');
        assertEquals(210, hashCodeBuilder.toHashCode());
    }
    
    @Test
    public void testAppendCharArray() {
        char[] array = {'a', 'b', 'c'};
        hashCodeBuilder.append(array);
        assertEquals(2120, hashCodeBuilder.toHashCode());
    }
    
    @Test
    public void testAppendDouble() {
        hashCodeBuilder.append(3.14);
        assertEquals(121007, hashCodeBuilder.toHashCode());
    }
    
    @Test
    public void testAppendDoubleArray() {
        double[] array = {1.1, 2.2, 3.3};
        hashCodeBuilder.append(array);
        assertEquals(332391, hashCodeBuilder.toHashCode());
    }
    
    @Test
    public void testAppendFloat() {
        hashCodeBuilder.append(2.5f);
        assertEquals(96, hashCodeBuilder.toHashCode());
    }
    
    @Test
    public void testAppendFloatArray() {
        float[] array = {1.5f, 2.5f, 3.5f};
        hashCodeBuilder.append(array);
        assertEquals(314, hashCodeBuilder.toHashCode());
    }
    
    @Test
    public void testAppendInt() {
        hashCodeBuilder.append(100);
        assertEquals(3721, hashCodeBuilder.toHashCode());
    }
    
    @Test
    public void testAppendIntArray() {
        int[] array = {1, 2, 3};
        hashCodeBuilder.append(array);
        assertEquals(37172, hashCodeBuilder.toHashCode());
    }
    
    @Test
    public void testAppendLong() {
        hashCodeBuilder.append(1000L);
        assertEquals(35523, hashCodeBuilder.toHashCode());
    }
    
    @Test
    public void testAppendLongArray() {
        long[] array = {1000L, 2000L, 3000L};
        hashCodeBuilder.append(array);
        assertEquals(6961036, hashCodeBuilder.toHashCode());
    }
    
    @Test
    public void testAppendObject() {
        String str = "Hello";
        hashCodeBuilder.append(str);
        assertEquals(2448197, hashCodeBuilder.toHashCode());
    }
    
    @Test
    public void testAppendObjectArray() {
        Object[] array = {1, 2.5, true};
        hashCodeBuilder.append(array);
        assertEquals(8298119, hashCodeBuilder.toHashCode());
    }
    
    @Test
    public void testAppendShort() {
        hashCodeBuilder.append((short) 50);
        assertEquals(153, hashCodeBuilder.toHashCode());
    }
    
    @Test
    public void testAppendShortArray() {
        short[] array = {10, 20, 30};
        hashCodeBuilder.append(array);
        assertEquals(2832, hashCodeBuilder.toHashCode());
    }
    
    @Test
    public void testAppendSuper() {
        int superHashCode = 12345;
        hashCodeBuilder.appendSuper(superHashCode);
        assertEquals(533571, hashCodeBuilder.toHashCode());
    }
    
    @Test
    public void testBuild() {
        Integer hashCode = hashCodeBuilder.build();
        assertNotNull(hashCode);
        assertEquals(17, hashCode.intValue());
    }
    
    @Test
    public void testHashCode() {
        int hashCode = hashCodeBuilder.hashCode();
        assertEquals(17, hashCode);
    }
}