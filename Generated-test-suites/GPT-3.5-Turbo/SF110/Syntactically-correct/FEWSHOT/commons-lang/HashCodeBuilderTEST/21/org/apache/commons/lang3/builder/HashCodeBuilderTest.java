package org.apache.commons.lang3.builder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class HashCodeBuilderTest {
    @Test
    public void testAppend_boolean() {
        HashCodeBuilder builder = new HashCodeBuilder();
        
        assertEquals(17, builder.toHashCode());
        
        builder.append(true);
        
        assertNotEquals(17, builder.toHashCode());
    }
    
    @Test
    public void testAppend_booleanArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        
        assertEquals(17, builder.toHashCode());
        
        builder.append(new boolean[] {true, false, true});
        
        assertNotEquals(17, builder.toHashCode());
    }
    
    @Test
    public void testAppend_byte() {
        HashCodeBuilder builder = new HashCodeBuilder();
        
        assertEquals(17, builder.toHashCode());
        
        builder.append((byte) 1);
        
        assertNotEquals(17, builder.toHashCode());
    }
    
    @Test
    public void testAppend_byteArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        
        assertEquals(17, builder.toHashCode());
        
        builder.append(new byte[] {1, 2, 3});
        
        assertNotEquals(17, builder.toHashCode());
    }
    
    @Test
    public void testAppend_char() {
        HashCodeBuilder builder = new HashCodeBuilder();
        
        assertEquals(17, builder.toHashCode());
        
        builder.append('a');
        
        assertNotEquals(17, builder.toHashCode());
    }
    
    @Test
    public void testAppend_charArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        
        assertEquals(17, builder.toHashCode());
        
        builder.append(new char[] {'a', 'b', 'c'});
        
        assertNotEquals(17, builder.toHashCode());
    }
    
    @Test
    public void testAppend_double() {
        HashCodeBuilder builder = new HashCodeBuilder();
        
        assertEquals(17, builder.toHashCode());
        
        builder.append(1.23);
        
        assertNotEquals(17, builder.toHashCode());
    }
    
    @Test
    public void testAppend_doubleArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        
        assertEquals(17, builder.toHashCode());
        
        builder.append(new double[] {1.23, 4.56, 7.89});
        
        assertNotEquals(17, builder.toHashCode());
    }
    
    @Test
    public void testAppend_float() {
        HashCodeBuilder builder = new HashCodeBuilder();
        
        assertEquals(17, builder.toHashCode());
        
        builder.append(1.23f);
        
        assertNotEquals(17, builder.toHashCode());
    }
    
    @Test
    public void testAppend_floatArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        
        assertEquals(17, builder.toHashCode());
        
        builder.append(new float[] {1.23f, 4.56f, 7.89f});
        
        assertNotEquals(17, builder.toHashCode());
    }
    
    @Test
    public void testAppend_int() {
        HashCodeBuilder builder = new HashCodeBuilder();
        
        assertEquals(17, builder.toHashCode());
        
        builder.append(123);
        
        assertNotEquals(17, builder.toHashCode());
    }
    
    @Test
    public void testAppend_intArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        
        assertEquals(17, builder.toHashCode());
        
        builder.append(new int[] {123, 456, 789});
        
        assertNotEquals(17, builder.toHashCode());
    }
    
    @Test
    public void testAppend_long() {
        HashCodeBuilder builder = new HashCodeBuilder();
        
        assertEquals(17, builder.toHashCode());
        
        builder.append(1234567890L);
        
        assertNotEquals(17, builder.toHashCode());
    }
    
    @Test
    public void testAppend_longArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        
        assertEquals(17, builder.toHashCode());
        
        builder.append(new long[] {1234567890L, 2345678901L, 3456789012L});
        
        assertNotEquals(17, builder.toHashCode());
    }
    
    @Test
    public void testAppend_Object() {
        HashCodeBuilder builder = new HashCodeBuilder();
        
        assertEquals(17, builder.toHashCode());
        
        Object obj = new Object();
        builder.append(obj);
        
        assertNotEquals(17, builder.toHashCode());
    }
    
    @Test
    public void testAppend_ObjectArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        
        assertEquals(17, builder.toHashCode());
        
        Object[] objArray = new Object[] {1, "two", 3.0};
        builder.append(objArray);
        
        assertNotEquals(17, builder.toHashCode());
    }
    
    @Test
    public void testAppend_short() {
        HashCodeBuilder builder = new HashCodeBuilder();
        
        assertEquals(17, builder.toHashCode());
        
        builder.append((short) 123);
        
        assertNotEquals(17, builder.toHashCode());
    }
    
    @Test
    public void testAppend_shortArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        
        assertEquals(17, builder.toHashCode());
        
        builder.append(new short[] {123, 456, 789});
        
        assertNotEquals(17, builder.toHashCode());
    }
    
    @Test
    public void testAppendSuper() {
        HashCodeBuilder builder = new HashCodeBuilder();
        
        assertEquals(17, builder.toHashCode());
        
        builder.appendSuper(123);
        
        assertNotEquals(17, builder.toHashCode());
    }
    
    @Test
    public void testBuild() {
        HashCodeBuilder builder = new HashCodeBuilder();
        
        assertEquals(17, builder.build().intValue());
        
        builder.append(123);
        
        assertNotEquals(17, builder.build().intValue());
    }
}