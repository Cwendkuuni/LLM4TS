import org.junit.Test;
import static org.junit.Assert.*;

public class HashCodeBuilderTest {
    @Test
    public void testAppendBoolean() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(true);
        assertEquals(37, builder.toHashCode());
    }
    
    @Test
    public void testAppendBooleanArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        boolean[] array = {true, false, true};
        builder.append(array);
        assertEquals(75, builder.toHashCode());
    }
    
    @Test
    public void testAppendByte() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((byte)1);
        assertEquals(38, builder.toHashCode());
    }
    
    @Test
    public void testAppendByteArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        byte[] array = {1, 2, 3};
        builder.append(array);
        assertEquals(119, builder.toHashCode());
    }
    
    @Test
    public void testAppendChar() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append('a');
        assertEquals(96, builder.toHashCode());
    }
    
    @Test
    public void testAppendCharArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        char[] array = {'a', 'b', 'c'};
        builder.append(array);
        assertEquals(289, builder.toHashCode());
    }
    
    @Test
    public void testAppendDouble() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(3.14);
        assertEquals(1070513950, builder.toHashCode());
    }
    
    @Test
    public void testAppendDoubleArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        double[] array = {1.1, 2.2, 3.3};
        builder.append(array);
        assertEquals(224590157, builder.toHashCode());
    }
    
    @Test
    public void testAppendFloat() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(4.5f);
        assertEquals(1565314157, builder.toHashCode());
    }
    
    @Test
    public void testAppendFloatArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        float[] array = {1.1f, 2.2f, 3.3f};
        builder.append(array);
        assertEquals(-1986000815, builder.toHashCode());
    }
    
    @Test
    public void testAppendInt() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(123);
        assertEquals(4761, builder.toHashCode());
    }
    
    @Test
    public void testAppendIntArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        int[] array = {1, 2, 3};
        builder.append(array);
        assertEquals(108809394, builder.toHashCode());
    }
    
    @Test
    public void testAppendLong() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(123456789012345L);
        assertEquals(1965884370, builder.toHashCode());
    }
    
    @Test
    public void testAppendLongArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        long[] array = {1L, 2L, 3L};
        builder.append(array);
        assertEquals(1076198299, builder.toHashCode());
    }
    
    @Test
    public void testAppendObject() {
        HashCodeBuilder builder = new HashCodeBuilder();
        Object obj = new Object();
        builder.append(obj);
        assertEquals(960, builder.toHashCode());
    }
    
    @Test
    public void testAppendArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        Object[] array = {1, "abc", true};
        builder.append(array);
        assertEquals(-1281481979, builder.toHashCode());
    }
    
    @Test
    public void testAppendShort() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((short)12);
        assertEquals(461, builder.toHashCode());
    }
    
    @Test
    public void testAppendShortArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        short[] array = {1, 2, 3};
        builder.append(array);
        assertEquals(-725307400, builder.toHashCode());
    }
    
    @Test
    public void testAppendSuper() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.appendSuper(12345);
        assertEquals(341840, builder.toHashCode());
    }
    
    @Test
    public void testBuild() {
        HashCodeBuilder builder = new HashCodeBuilder();
        Integer hashCode = builder.build();
        assertNotNull(hashCode);
        assertEquals(17, hashCode.intValue());
    }
    
    @Test
    public void testHashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        int hashCode = builder.hashCode();
        assertEquals(17, hashCode);
    }
}