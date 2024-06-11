
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class HashCodeBuilderTest {

    @Test
    public void testAppend_boolean() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(false);
        builder.append(true);
        
        int result = builder.toHashCode();
        
        assertEquals(37, result);
    }

    @Test
    public void testAppend_booleanArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new boolean[] {true, false});
        
        int result = builder.toHashCode();
        
        assertEquals(569, result);
    }

    @Test
    public void testAppend_byte() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((byte) 5);
        
        int result = builder.toHashCode();
        
        assertEquals(5, result);
    }

    @Test
    public void testAppend_byteArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new byte[] {1, 2, 3});
        
        int result = builder.toHashCode();
        
        assertEquals(133, result);
    }
    
    @Test
    public void testAppend_char() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append('a');
        
        int result = builder.toHashCode();
        
        assertEquals(104, result);
    }

    @Test
    public void testAppend_charArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new char[] {'a', 'b', 'c'});
        
        int result = builder.toHashCode();
        
        assertEquals(246, result);
    }

    @Test
    public void testAppend_double() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(3.14);
        
        int result = builder.toHashCode();
        
        assertEquals(-1368589439, result);
    }

    @Test
    public void testAppend_doubleArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new double[] {1.1, 2.2, 3.3});
        
        int result = builder.toHashCode();
        
        assertEquals(766537845, result);
    }

    @Test
    public void testAppend_float() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(2.5f);
        
        int result = builder.toHashCode();
        
        assertEquals(1066642508, result);
    }

    @Test
    public void testAppend_floatArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new float[] {1.1f, 2.2f, 3.3f});
        
        int result = builder.toHashCode();
        
        assertEquals(-838830494, result);
    }

    @Test
    public void testAppend_int() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(42);
        
        int result = builder.toHashCode();
        
        assertEquals(42, result);
    }

    @Test
    public void testAppend_intArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new int[] {1, 2, 3});
        
        int result = builder.toHashCode();
        
        assertEquals(573168531, result);
    }

    @Test
    public void testAppend_long() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(123456789L);
        
        int result = builder.toHashCode();
        
        assertEquals(1234568520, result);
    }

    @Test
    public void testAppend_longArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new long[] {1000000L, 2000000L, 3000000L});
        
        int result = builder.toHashCode();
        
        assertEquals(1712356051, result);
    }

    @Test
    public void testAppend_Object() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append("Hello");
        builder.append(new StringBuilder("World"));
        
        int result = builder.toHashCode();
        
        assertEquals(1476175283, result);
    }

    @Test
    public void testAppend_ObjectArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new Object[] {"Hello", new StringBuilder("World")});
        
        int result = builder.toHashCode();
        
        assertEquals(-1130642092, result);
    }

    @Test
    public void testAppend_short() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((short) 10);
        
        int result = builder.toHashCode();
        
        assertEquals(1014, result);
    }

    @Test
    public void testAppend_shortArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new short[] {1, 2, 3});
        
        int result = builder.toHashCode();
        
        assertEquals(587, result);
    }
    
    @Test
    public void testAppendSuper() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.appendSuper(10);
        
        int result = builder.toHashCode();
        
        assertEquals(813, result);
    }

    @Test
    public void testBuilder() {
        HashCodeBuilder builder = new HashCodeBuilder();
        assertNotNull(builder);
        assertEquals(17, builder.toHashCode());
    }

    @Test
    public void testBuilder_initialOddNumber() {
        HashCodeBuilder builder = new HashCodeBuilder(13, 37);
        assertNotNull(builder);
        assertEquals(13, builder.toHashCode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuilder_initialOddNumberEven() {
        new HashCodeBuilder(12, 37);
    }

    @Test
    public void testBuilder_multiplierOddNumber() {
        HashCodeBuilder builder = new HashCodeBuilder(13, 39);
        assertNotNull(builder);
        assertEquals(13, builder.toHashCode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuilder_multiplierOddNumberEven() {
        new HashCodeBuilder(13, 38);
    }
}
