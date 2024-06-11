import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class HashCodeBuilderTest {
    @Test
    public void testAppendBoolean() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder = builder.append(true);
        assertEquals(33, builder.toHashCode());
        
        builder = new HashCodeBuilder();
        builder = builder.append(false);
        assertEquals(32, builder.toHashCode());
    }

    @Test
    public void testAppendBooleanArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder = builder.append(new boolean[] {true, true});
        assertEquals(35, builder.toHashCode());
        
        builder = new HashCodeBuilder();
        builder = builder.append(new boolean[] {false, false});
        assertEquals(34, builder.toHashCode());
        
        builder = new HashCodeBuilder();
        builder = builder.append((boolean[])null);
        assertEquals(0, builder.toHashCode());
    }

    @Test
    public void testAppendByte() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder = builder.append((byte)1);
        assertEquals(38, builder.toHashCode());
    }

    @Test
    public void testAppendByteArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder = builder.append(new byte[] {1, 2});
        assertEquals(43, builder.toHashCode());
        
        builder = new HashCodeBuilder();
        builder = builder.append((byte[])null);
        assertEquals(0, builder.toHashCode());
    }

    @Test
    public void testAppendChar() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder = builder.append('a');
        assertEquals(108, builder.toHashCode());
    }

    @Test
    public void testAppendCharArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder = builder.append(new char[] {'a', 'b'});
        assertEquals(22835, builder.toHashCode());
        
        builder = new HashCodeBuilder();
        builder = builder.append((char[])null);
        assertEquals(0, builder.toHashCode());
    }

    @Test
    public void testAppendDouble() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder = builder.append(1.5);
        assertEquals(8007, builder.toHashCode());
    }

    @Test
    public void testAppendDoubleArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder = builder.append(new double[] {1.5, 2.5});
        assertEquals(10198227, builder.toHashCode());
        
        builder = new HashCodeBuilder();
        builder = builder.append((double[])null);
        assertEquals(0, builder.toHashCode());
    }

    @Test
    public void testAppendFloat() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder = builder.append(1.5f);
        assertEquals(429494653, builder.toHashCode());
    }

    @Test
    public void testAppendFloatArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder = builder.append(new float[] {1.5f, 2.5f});
        assertEquals(240250, builder.toHashCode());
        
        builder = new HashCodeBuilder();
        builder = builder.append((float[])null);
        assertEquals(0, builder.toHashCode());
    }

    @Test
    public void testAppendInt() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder = builder.append(5);
        assertEquals(186, builder.toHashCode());
    }

    @Test
    public void testAppendIntArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder = builder.append(new int[] {1, 2});
        assertEquals(75059, builder.toHashCode());
        
        builder = new HashCodeBuilder();
        builder = builder.append((int[])null);
        assertEquals(0, builder.toHashCode());
    }

    @Test
    public void testAppendLong() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder = builder.append(5L);
        assertEquals(186, builder.toHashCode());
    }

    @Test
    public void testAppendLongArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder = builder.append(new long[] {1L, 2L});
        assertEquals(75059, builder.toHashCode());
        
        builder = new HashCodeBuilder();
        builder = builder.append((long[])null);
        assertEquals(0, builder.toHashCode());
    }

    @Test
    public void testAppendObject() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder = builder.append("test");
        assertEquals(3556498, builder.toHashCode());
        
        builder = new HashCodeBuilder();
        builder = builder.append((Object)null);
        assertEquals(0, builder.toHashCode());
    }

    @Test
    public void testAppendObjectArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder = builder.append(new Object[] {"test1", "test2"});
        assertEquals(1215154619, builder.toHashCode());
        
        builder = new HashCodeBuilder();
        builder = builder.append((Object[])null);
        assertEquals(0, builder.toHashCode());
    }

    @Test
    public void testAppendShort() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder = builder.append((short)5);
        assertEquals(186, builder.toHashCode());
    }

    @Test
    public void testAppendShortArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder = builder.append(new short[] {1, 2});
        assertEquals(75059, builder.toHashCode());
        
        builder = new HashCodeBuilder();
        builder = builder.append((short[])null);
        assertEquals(0, builder.toHashCode());
    }

    @Test
    public void testAppendSuper() {
        HashCodeBuilder builder1 = new HashCodeBuilder();
        builder1 = builder1.append(5);
        
        HashCodeBuilder builder2 = new HashCodeBuilder();
        builder2 = builder2.appendSuper(builder1.toHashCode());
        
        assertEquals(620, builder2.toHashCode());
    }
    
    
    @Test
    public void testBuild() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder = builder.append(5);
        assertEquals(new Integer(186), builder.build());
    }

    @Test
    public void testHashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder = builder.append(5);
        assertEquals(186, builder.hashCode());
    }
}