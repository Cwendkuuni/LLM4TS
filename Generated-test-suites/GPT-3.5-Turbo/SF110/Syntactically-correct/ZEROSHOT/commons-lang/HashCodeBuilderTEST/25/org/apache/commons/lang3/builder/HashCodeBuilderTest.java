import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.Before;

public class HashCodeBuilderTest {
    
    private HashCodeBuilder builder;
    
    @Before
    public void setup() {
        builder = new HashCodeBuilder();
    }

    @Test
    public void testAppendBoolean() {
        assertTrue(builder.append(true) instanceof HashCodeBuilder);
    }
    
    @Test
    public void testAppendBooleanArray() {
        boolean[] array = {true, false, true};
        assertTrue(builder.append(array) instanceof HashCodeBuilder);
    }
    
    @Test
    public void testAppendByte() {
        assertTrue(builder.append((byte) 1) instanceof HashCodeBuilder);
    }
    
    @Test
    public void testAppendByteArray() {
        byte[] array = {1, 2, 3};
        assertTrue(builder.append(array) instanceof HashCodeBuilder);
    }
    
    @Test
    public void testAppendChar() {
        assertTrue(builder.append('a') instanceof HashCodeBuilder);
    }
    
    @Test
    public void testAppendCharArray() {
        char[] array = {'a', 'b', 'c'};
        assertTrue(builder.append(array) instanceof HashCodeBuilder);
    }
    
    @Test
    public void testAppendDouble() {
        assertTrue(builder.append(1.5) instanceof HashCodeBuilder);
    }
    
    @Test
    public void testAppendDoubleArray() {
        double[] array = {1.5, 2.5, 3.5};
        assertTrue(builder.append(array) instanceof HashCodeBuilder);
    }
    
    @Test
    public void testAppendFloat() {
        assertTrue(builder.append(1.5f) instanceof HashCodeBuilder);
    }
    
    @Test
    public void testAppendFloatArray() {
        float[] array = {1.5f, 2.5f, 3.5f};
        assertTrue(builder.append(array) instanceof HashCodeBuilder);
    }
    
    @Test
    public void testAppendInt() {
        assertTrue(builder.append(1) instanceof HashCodeBuilder);
    }
    
    @Test
    public void testAppendIntArray() {
        int[] array = {1, 2, 3};
        assertTrue(builder.append(array) instanceof HashCodeBuilder);
    }
    
    @Test
    public void testAppendLong() {
        assertTrue(builder.append(1L) instanceof HashCodeBuilder);
    }
    
    @Test
    public void testAppendLongArray() {
        long[] array = {1L, 2L, 3L};
        assertTrue(builder.append(array) instanceof HashCodeBuilder);
    }
    
    @Test
    public void testAppendObject() {
        assertTrue(builder.append("test") instanceof HashCodeBuilder);
    }
    
    @Test
    public void testAppendObjectArray() {
        Object[] array = {"test1", "test2", "test3"};
        assertTrue(builder.append(array) instanceof HashCodeBuilder);
    }
    
    @Test
    public void testAppendShort() {
        assertTrue(builder.append((short) 1) instanceof HashCodeBuilder);
    }
    
    @Test
    public void testAppendShortArray() {
        short[] array = {1, 2, 3};
        assertTrue(builder.append(array) instanceof HashCodeBuilder);
    }
    
    @Test
    public void testAppendSuper() {
        assertTrue(builder.appendSuper(123) instanceof HashCodeBuilder);
    }
    
    @Test
    public void testToHashCode() {
        assertTrue(builder.toHashCode() instanceof Integer);
    }
    
    @Test
    public void testBuild() {
        assertTrue(builder.build() instanceof Integer);
    }
    
    @Test
    public void testHashCode() {
        assertEquals(builder.hashCode(), builder.toHashCode());
    }
}