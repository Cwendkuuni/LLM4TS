import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class HashCodeBuilderTest {

    /* append(boolean value) test cases */
    @Test
    public void testAppend_boolean() {
        HashCodeBuilder hcBuilder = new HashCodeBuilder();
        hcBuilder.append(true);
        assertEquals(38, hcBuilder.toHashCode());
    }
    
    /* append(boolean[] array) test cases */
    @Test
    public void testAppend_booleanArray() {
        HashCodeBuilder hcBuilder = new HashCodeBuilder();
        boolean[] array = {true, false, true};
        hcBuilder.append(array);
        assertEquals(38, hcBuilder.toHashCode());
    }

    /* append(byte value) test cases */
    @Test
    public void testAppend_byte() {
        HashCodeBuilder hcBuilder = new HashCodeBuilder();
        hcBuilder.append((byte)2);
        assertEquals(75, hcBuilder.toHashCode());
    }
    
    /* append(byte[] array) test cases */
    @Test
    public void testAppend_byteArray() {
        HashCodeBuilder hcBuilder = new HashCodeBuilder();
        byte[] array = {1, 2, 3};
        hcBuilder.append(array);
        assertEquals(186, hcBuilder.toHashCode());
    }
      
    /* append(char value) test cases */
    @Test
    public void testAppend_char() {
        HashCodeBuilder hcBuilder = new HashCodeBuilder();
        hcBuilder.append('c');
        assertEquals(107, hcBuilder.toHashCode());
    }
    
    /* append(char[] array) test cases */
    @Test
    public void testAppend_charArray() {
        HashCodeBuilder hcBuilder = new HashCodeBuilder();
        char[] array = {'a', 'b', 'c'};
        hcBuilder.append(array);
        assertEquals(299, hcBuilder.toHashCode());
    }
    
    /* append(double value) test cases */
    @Test
    public void testAppend_double() {
        HashCodeBuilder hcBuilder = new HashCodeBuilder();
        hcBuilder.append(2.5);
        assertEquals(107101, hcBuilder.toHashCode());
    }
    
    /* append(double[] array) test cases */
    @Test
    public void testAppend_doubleArray() {
        HashCodeBuilder hcBuilder = new HashCodeBuilder();
        double[] array = {1.0, 2.0, 3.0};
        hcBuilder.append(array);
        assertEquals(1052992, hcBuilder.toHashCode());
    }
    
    /* append(float value) test cases */
    @Test
    public void testAppend_float() {
        HashCodeBuilder hcBuilder = new HashCodeBuilder();
        hcBuilder.append(2.5f);
        assertEquals(107195, hcBuilder.toHashCode());
    }
    
    /* append(float[] array) test cases */
    @Test
    public void testAppend_floatArray() {
        HashCodeBuilder hcBuilder = new HashCodeBuilder();
        float[] array = {1.0f, 2.0f, 3.0f};
        hcBuilder.append(array);
        assertEquals(405194, hcBuilder.toHashCode());
    }
    
    /* append(int value) test cases */
    @Test
    public void testAppend_int() {
        HashCodeBuilder hcBuilder = new HashCodeBuilder();
        hcBuilder.append(10);
        assertEquals(375, hcBuilder.toHashCode());
    }
    
    /* append(int[] array) test cases */
    @Test
    public void testAppend_intArray() {
        HashCodeBuilder hcBuilder = new HashCodeBuilder();
        int[] array = {1, 2, 3};
        hcBuilder.append(array);
        assertEquals(186, hcBuilder.toHashCode());
    }
    
    /* append(long value) test cases */
    @Test
    public void testAppend_long() {
        HashCodeBuilder hcBuilder = new HashCodeBuilder();
        hcBuilder.append(1234567890L);
        assertEquals(-2040864062, hcBuilder.toHashCode());
    }
    
    /* append(long[] array) test cases */
    @Test
    public void testAppend_longArray() {
        HashCodeBuilder hcBuilder = new HashCodeBuilder();
        long[] array = {123L, 456L, 789L};
        hcBuilder.append(array);
        assertEquals(113391, hcBuilder.toHashCode());
    }
    
    /* append(Object object) test cases */
    @Test
    public void testAppend_object() {
        HashCodeBuilder hcBuilder = new HashCodeBuilder();
        Object obj = new Object();
        hcBuilder.append(obj);
        assertEquals(2593996, hcBuilder.toHashCode());
    }
    
    /* append(Object[] array) test cases */
    @Test
    public void testAppend_objectArray() {
        HashCodeBuilder hcBuilder = new HashCodeBuilder();
        Object[] array = {new Object(), new Object(), new Object()};
        hcBuilder.append(array);
        assertEquals(6086936, hcBuilder.toHashCode());
    }
    
    /* append(short value) test cases */
    @Test
    public void testAppend_short() {
        HashCodeBuilder hcBuilder = new HashCodeBuilder();
        hcBuilder.append((short)5);
        assertEquals(192, hcBuilder.toHashCode());
    }
    
    /* append(short[] array) test cases */
    @Test
    public void testAppend_shortArray() {
        HashCodeBuilder hcBuilder = new HashCodeBuilder();
        short[] array = {1, 2, 3};
        hcBuilder.append(array);
        assertEquals(186, hcBuilder.toHashCode());
    }
    
    /* appendSuper(int superHashCode) test cases */
    @Test
    public void testAppendSuper() {
        HashCodeBuilder hcBuilder = new HashCodeBuilder();
        hcBuilder.appendSuper(123456);
        assertEquals(4593829, hcBuilder.toHashCode());
    }
}