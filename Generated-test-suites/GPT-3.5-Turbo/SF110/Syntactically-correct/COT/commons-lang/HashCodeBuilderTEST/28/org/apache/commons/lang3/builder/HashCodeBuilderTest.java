
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class HashCodeBuilderTest {
    
    private HashCodeBuilder hashCodeBuilder;
    
    @Before
    public void setup() {
        hashCodeBuilder = new HashCodeBuilder();
    }
    
    @Test
    public void testAppendBoolean() {
        hashCodeBuilder.append(true);
        int hashCode = hashCodeBuilder.toHashCode();
        assertEquals(37, hashCode);
    }
    
    @Test
    public void testAppendBooleanArray_Null() {
        hashCodeBuilder.append((boolean[])null);
        int hashCode = hashCodeBuilder.toHashCode();
        assertEquals(0, hashCode);
    }
    
    @Test
    public void testAppendBooleanArray() {
        boolean[] array = {true, false, true};
        hashCodeBuilder.append(array);
        int hashCode = hashCodeBuilder.toHashCode();
        assertEquals(4357, hashCode);
    }
    
    @Test
    public void testAppendByte() {
        hashCodeBuilder.append((byte)10);
        int hashCode = hashCodeBuilder.toHashCode();
        assertEquals(37 * 10, hashCode);
    }
    
    @Test
    public void testAppendByteArray_Null() {
        hashCodeBuilder.append((byte[])null);
        int hashCode = hashCodeBuilder.toHashCode();
        assertEquals(0, hashCode);
    }
    
    @Test
    public void testAppendByteArray() {
        byte[] array = {10, 20, 30};
        hashCodeBuilder.append(array);
        int hashCode = hashCodeBuilder.toHashCode();
        assertEquals(647786530, hashCode);
    }
    
    @Test
    public void testAppendChar() {
        hashCodeBuilder.append('a');
        int hashCode = hashCodeBuilder.toHashCode();
        assertEquals(37 * 97, hashCode);
    }
    
    @Test
    public void testAppendCharArray_Null() {
        hashCodeBuilder.append((char[])null);
        int hashCode = hashCodeBuilder.toHashCode();
        assertEquals(0, hashCode);
    }
    
    @Test
    public void testAppendCharArray() {
        char[] array = {'a', 'b', 'c'};
        hashCodeBuilder.append(array);
        int hashCode = hashCodeBuilder.toHashCode();
        assertEquals(961325, hashCode);
    }
    
    @Test
    public void testAppendDouble() {
        hashCodeBuilder.append(10.5);
        int hashCode = hashCodeBuilder.toHashCode();
        assertEquals(37 * Double.doubleToLongBits(10.5), hashCode);
    }
    
    @Test
    public void testAppendDoubleArray_Null() {
        hashCodeBuilder.append((double[])null);
        int hashCode = hashCodeBuilder.toHashCode();
        assertEquals(0, hashCode);
    }
    
    @Test
    public void testAppendDoubleArray() {
        double[] array = {10.5, 20.5, 30.5};
        hashCodeBuilder.append(array);
        int hashCode = hashCodeBuilder.toHashCode();
        assertEquals(2137527558, hashCode);
    }
    
    @Test
    public void testAppendFloat() {
        hashCodeBuilder.append(10.5f);
        int hashCode = hashCodeBuilder.toHashCode();
        assertEquals(37 * Float.floatToIntBits(10.5f), hashCode);
    }
    
    @Test
    public void testAppendFloatArray_Null() {
        hashCodeBuilder.append((float[])null);
        int hashCode = hashCodeBuilder.toHashCode();
        assertEquals(0, hashCode);
    }
    
    @Test
    public void testAppendFloatArray() {
        float[] array = {10.5f, 20.5f, 30.5f};
        hashCodeBuilder.append(array);
        int hashCode = hashCodeBuilder.toHashCode();
        assertEquals(786939747, hashCode);
    }
    
    @Test
    public void testAppendInt() {
        hashCodeBuilder.append(10);
        int hashCode = hashCodeBuilder.toHashCode();
        assertEquals(37 * 10, hashCode);
    }
    
    @Test
    public void testAppendIntArray_Null() {
        hashCodeBuilder.append((int[])null);
        int hashCode = hashCodeBuilder.toHashCode();
        assertEquals(0, hashCode);
    }
    
    @Test
    public void testAppendIntArray() {
        int[] array = {10, 20, 30};
        hashCodeBuilder.append(array);
        int hashCode = hashCodeBuilder.toHashCode();
        assertEquals(834056041, hashCode);
    }
    
    @Test
    public void testAppendLong() {
        hashCodeBuilder.append(10L);
        int hashCode = hashCodeBuilder.toHashCode();
        assertEquals(37 * 10 + 10 >>> 32, hashCode);
    }
    
    @Test
    public void testAppendLongArray_Null() {
        hashCodeBuilder.append((long[])null);
        int hashCode = hashCodeBuilder.toHashCode();
        assertEquals(0, hashCode);
    }
    
    @Test
    public void testAppendLongArray() {
        long[] array = {10L, 20L, 30L};
        hashCodeBuilder.append(array);
        int hashCode = hashCodeBuilder.toHashCode();
        assertEquals(875789987, hashCode);
    }
    
    @Test
    public void testAppendObject_Null() {
        hashCodeBuilder.append((Object)null);
        int hashCode = hashCodeBuilder.toHashCode();
        assertEquals(0, hashCode);
    }
    
    @Test
    public void testAppendObject_Array() {
        Object[] array = {10, "hello", true};
        hashCodeBuilder.append(array);
        int hashCode = hashCodeBuilder.toHashCode();
        assertEquals(116458645, hashCode);
    }
    
    @Test
    public void testAppendShort() {
        hashCodeBuilder.append((short)10);
        int hashCode = hashCodeBuilder.toHashCode();
        assertEquals(37 * 10, hashCode);
    }
    
    @Test
    public void testAppendShortArray_Null() {
        hashCodeBuilder.append((short[])null);
        int hashCode = hashCodeBuilder.toHashCode();
        assertEquals(0, hashCode);
    }
    
    @Test
    public void testAppendShortArray() {
        short[] array = {10, 20, 30};
        hashCodeBuilder.append(array);
        int hashCode = hashCodeBuilder.toHashCode();
        assertEquals(849587845, hashCode);
    }
    
    @Test
    public void testAppendSuper() {
        hashCodeBuilder.appendSuper(100);
        int hashCode = hashCodeBuilder.toHashCode();
        assertEquals(37 * 17 + 100, hashCode);
    }
    
    @Test
    public void testHashCode() {
        int hashCode = hashCodeBuilder.hashCode();
        assertEquals(17, hashCode);
    }
    
    @Test
    public void testBuild() {
        int hashCode = hashCodeBuilder.build();
        assertEquals(17, hashCode);
    }
}
