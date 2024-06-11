import org.junit.Test;
import org.junit.Assert;

public class HashCodeBuilderTest {

    @Test
    public void testAppendBoolean() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(true);
        int result = builder.toHashCode();
        Assert.assertEquals(37, result);
    }
    
    @Test
    public void testAppendBooleanArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new boolean[] { true, false });
        int result = builder.toHashCode();
        Assert.assertEquals(38, result);
    }
    
    @Test
    public void testAppendByte() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((byte) 5);
        int result = builder.toHashCode();
        Assert.assertEquals(182, result);
    }
    
    @Test
    public void testAppendByteArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new byte[] { 1, 2, 3 });
        int result = builder.toHashCode();
        Assert.assertEquals(292, result);
    }
    
    @Test
    public void testAppendChar() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append('a');
        int result = builder.toHashCode();
        Assert.assertEquals(109, result);
    }
    
    @Test
    public void testAppendCharArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new char[] { 'a', 'b', 'c' });
        int result = builder.toHashCode();
        Assert.assertEquals(303, result);
    }
    
    @Test
    public void testAppendDouble() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(5.5);
        int result = builder.toHashCode();
        Assert.assertEquals(1273851293, result);
    }
    
    @Test
    public void testAppendDoubleArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new double[] { 1.1, 2.2, 3.3 });
        int result = builder.toHashCode();
        Assert.assertEquals(925607748, result);
    }
    
    @Test
    public void testAppendFloat() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(5.5f);
        int result = builder.toHashCode();
        Assert.assertEquals(1083136659, result);
    }
    
    @Test
    public void testAppendFloatArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new float[] { 1.1f, 2.2f, 3.3f });
        int result = builder.toHashCode();
        Assert.assertEquals(-24375988, result);
    }
    
    @Test
    public void testAppendInt() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(5);
        int result = builder.toHashCode();
        Assert.assertEquals(214, result);
    }
    
    @Test
    public void testAppendIntArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new int[] { 1, 2, 3 });
        int result = builder.toHashCode();
        Assert.assertEquals(1748493134, result);
    }
    
    @Test
    public void testAppendLong() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(5L);
        int result = builder.toHashCode();
        Assert.assertEquals(93, result);
    }
    
    @Test
    public void testAppendLongArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new long[] { 1L, 2L, 3L });
        int result = builder.toHashCode();
        Assert.assertEquals(-753178440, result);
    }
    
    @Test
    public void testAppendObject() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new Object());
        int result = builder.toHashCode();
        Assert.assertNotEquals(0, result);
    }
    
    @Test
    public void testAppendObjectArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new Object[] { new Object(), new Object() });
        int result = builder.toHashCode();
        Assert.assertNotEquals(0, result);
    }
    
    @Test
    public void testAppendShort() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((short) 5);
        int result = builder.toHashCode();
        Assert.assertEquals(517, result);
    }
    
    @Test
    public void testAppendShortArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new short[] { 1, 2, 3 });
        int result = builder.toHashCode();
        Assert.assertEquals(1376272749, result);
    }
    
    @Test
    public void testAppendSuper() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.appendSuper(42);
        int result = builder.toHashCode();
        Assert.assertEquals(1626, result);
    }
    
    @Test
    public void testBuild() {
        HashCodeBuilder builder = new HashCodeBuilder();
        Integer result = builder.build();
        Assert.assertEquals(17, result.intValue());
    }
    
    @Test
    public void testHashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        int result = builder.hashCode();
        Assert.assertEquals(17, result);
    }
}