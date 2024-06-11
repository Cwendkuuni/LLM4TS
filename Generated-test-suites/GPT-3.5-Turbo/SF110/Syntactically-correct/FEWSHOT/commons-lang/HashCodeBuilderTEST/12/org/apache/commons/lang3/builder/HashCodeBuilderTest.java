package org.apache.commons.lang3.builder;

import org.junit.Test;
import static org.junit.Assert.*;

public class HashCodeBuilderTest {

    @Test
    public void testConstructor() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        assertEquals(17, hcb.toHashCode());
        
        HashCodeBuilder hcb2 = new HashCodeBuilder(5, 20);
        assertEquals(5, hcb2.toHashCode());
    }
    
    @Test
    public void testAppendBoolean() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(true);
        assertEquals(36, hcb.toHashCode());
        
        hcb.append(false);
        assertEquals(37, hcb.toHashCode());
    }
    
    @Test
    public void testAppendBooleanArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        boolean[] array = {true, false};
        hcb.append(array);
        assertEquals(10563, hcb.toHashCode());
        
        hcb.append((boolean[]) null);
        assertEquals(10563, hcb.toHashCode());
    }
    
    @Test
    public void testAppendByte() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append((byte) 5);
        assertEquals(160, hcb.toHashCode());
    }
    
    @Test
    public void testAppendByteArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        byte[] array = {1, 2, 3};
        hcb.append(array);
        assertEquals(1432768, hcb.toHashCode());
        
        hcb.append((byte[]) null);
        assertEquals(1432768, hcb.toHashCode());
    }
    
    @Test
    public void testAppendChar() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append('a');
        assertEquals(151, hcb.toHashCode());
    }
    
    @Test
    public void testAppendCharArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        char[] array = {'a', 'b', 'c'};
        hcb.append(array);
        assertEquals(1473936, hcb.toHashCode());
        
        hcb.append((char[]) null);
        assertEquals(1473936, hcb.toHashCode());
    }
    
    @Test
    public void testAppendDouble() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(5.5);
        assertEquals(160, hcb.toHashCode());
    }
    
    @Test
    public void testAppendDoubleArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        double[] array = {1.1, 2.2, 3.3};
        hcb.append(array);
        assertEquals(2048505001, hcb.toHashCode());
        
        hcb.append((double[]) null);
        assertEquals(2048505001, hcb.toHashCode());
    }
    
    @Test
    public void testAppendFloat() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(5.5f);
        assertEquals(1048577, hcb.toHashCode());
    }
    
    @Test
    public void testAppendFloatArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        float[] array = {1.1f, 2.2f, 3.3f};
        hcb.append(array);
        assertEquals(4148128, hcb.toHashCode());
        
        hcb.append((float[]) null);
        assertEquals(4148128, hcb.toHashCode());
    }
    
    @Test
    public void testAppendInt() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(5);
        assertEquals(160, hcb.toHashCode());
    }
    
    @Test
    public void testAppendIntArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        int[] array = {1, 2, 3};
        hcb.append(array);
        assertEquals(30146, hcb.toHashCode());
        
        hcb.append((int[]) null);
        assertEquals(30146, hcb.toHashCode());
    }
    
    @Test
    public void testAppendLong() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(5L);
        assertEquals(160, hcb.toHashCode());
    }
    
    @Test
    public void testAppendLongArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        long[] array = {1L, 2L, 3L};
        hcb.append(array);
        assertEquals(2084410634, hcb.toHashCode());
        
        hcb.append((long[]) null);
        assertEquals(2084410634, hcb.toHashCode());
    }
    
    @Test
    public void testAppendObject() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append("hello");
        assertEquals(3543705, hcb.toHashCode());
        
        hcb.append((String) null);
        assertEquals(3543705, hcb.toHashCode());
    }
    
    @Test
    public void testAppendObjectArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        Object[] array = {"hello", "world"};
        hcb.append(array);
        assertEquals(46246208, hcb.toHashCode());
        
        hcb.append((Object[]) null);
        assertEquals(46246208, hcb.toHashCode());
    }
    
    @Test
    public void testAppendShort() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append((short) 5);
        assertEquals(160, hcb.toHashCode());
    }
    
    @Test
    public void testAppendShortArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        short[] array = {1, 2, 3};
        hcb.append(array);
        assertEquals(70030, hcb.toHashCode());
        
        hcb.append((short[]) null);
        assertEquals(70030, hcb.toHashCode());
    }
    
    @Test
    public void testAppendSuper() {
        HashCodeBuilder hcb = new HashCodeBuilder(5, 20);
        hcb.appendSuper(50);
        assertEquals(100, hcb.toHashCode());
    }
    
    @Test
    public void testBuild() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        assertEquals(17, hcb.build().intValue());
    }
    
    @Test
    public void testHashCode() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        assertEquals(hcb.toHashCode(), hcb.hashCode());
    }
}