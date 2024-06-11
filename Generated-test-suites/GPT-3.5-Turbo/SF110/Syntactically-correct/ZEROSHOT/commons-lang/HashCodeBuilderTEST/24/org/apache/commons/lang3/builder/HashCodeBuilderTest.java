import org.junit.Test;
import static org.junit.Assert.*;

public class HashCodeBuilderTest {
    
    @Test
    public void testAppend_boolean_value() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(true);
        int actual = builder.toHashCode();
        int expected = 59;
        assertEquals(expected, actual);
    }
    
    @Test
    public void testAppend_boolean_array() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new boolean[] {true, false, true});
        int actual = builder.toHashCode();
        int expected = 1003;
        assertEquals(expected, actual);
    }
    
    @Test
    public void testAppend_byte_value() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((byte) 5);
        int actual = builder.toHashCode();
        int expected = 188;
        assertEquals(expected, actual);
    }
    
    @Test
    public void testAppend_byte_array() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new byte[] {2, 4, 6});
        int actual = builder.toHashCode();
        int expected = 187;
        assertEquals(expected, actual);
    }
    
    @Test
    public void testAppend_char_value() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append('c');
        int actual = builder.toHashCode();
        int expected = 182;
        assertEquals(expected, actual);
    }
    
    @Test
    public void testAppend_char_array() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new char[] {'a', 'b', 'c'});
        int actual = builder.toHashCode();
        int expected = 136666182;
        assertEquals(expected, actual);
    }
    
    @Test
    public void testAppend_double_value() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(3.14);
        int actual = builder.toHashCode();
        int expected = 231001091;
        assertEquals(expected, actual);
    }
    
    @Test
    public void testAppend_double_array() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new double[] {1.2, 3.4, 5.6});
        int actual = builder.toHashCode();
        int expected = 71480324;
        assertEquals(expected, actual);
    }
    
    @Test
    public void testAppend_float_value() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(2.5f);
        int actual = builder.toHashCode();
        int expected = -1308891058;
        assertEquals(expected, actual);
    }
    
    @Test
    public void testAppend_float_array() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new float[] {1.2f, 3.4f, 5.6f});
        int actual = builder.toHashCode();
        int expected = -801247382;
        assertEquals(expected, actual);
    }
    
    @Test
    public void testAppend_int_value() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(42);
        int actual = builder.toHashCode();
        int expected = 16042;
        assertEquals(expected, actual);
    }
    
    @Test
    public void testAppend_int_array() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new int[] {1, 2, 3});
        int actual = builder.toHashCode();
        int expected = -777963617;
        assertEquals(expected, actual);
    }
    
    @Test
    public void testAppend_long_value() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(123456789L);
        int actual = builder.toHashCode();
        int expected = 1872576052;
        assertEquals(expected, actual);
    }
    
    @Test
    public void testAppend_long_array() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new long[] {1L, 2L, 3L});
        int actual = builder.toHashCode();
        int expected = -1177940981;
        assertEquals(expected, actual);
    }
    
    @Test
    public void testAppend_object_value() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append("test");
        int actual = builder.toHashCode();
        int expected = -249350748;
        assertEquals(expected, actual);
    }
    
    @Test
    public void testAppend_object_array() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new Object[] {"a", "b", "c"});
        int actual = builder.toHashCode();
        int expected = -1909979155;
        assertEquals(expected, actual);
    }
    
    @Test
    public void testAppend_short_value() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((short) 10);
        int actual = builder.toHashCode();
        int expected = 356;
        assertEquals(expected, actual);
    }
    
    @Test
    public void testAppend_short_array() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new short[] {2, 4, 6});
        int actual = builder.toHashCode();
        int expected = 183;
        assertEquals(expected, actual);
    }
    
    @Test
    public void testAppendSuper() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.appendSuper(123);
        int actual = builder.toHashCode();
        int expected = 4681;
        assertEquals(expected, actual);
    }
    
    @Test
    public void testToHashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(true);
        builder.append(42);
        builder.append("test");
        int actual = builder.toHashCode();
        int expected = 337610004;
        assertEquals(expected, actual);
    }
    
    @Test
    public void testBuild() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(true);
        builder.append(42);
        builder.append("test");
        int actual = builder.build();
        int expected = 337610004;
        assertEquals(expected, actual.intValue());
    }
    
    @Test
    public void testHashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(true);
        builder.append(42);
        builder.append("test");
        int actual = builder.hashCode();
        int expected = 337610004;
        assertEquals(expected, actual);
    }
}