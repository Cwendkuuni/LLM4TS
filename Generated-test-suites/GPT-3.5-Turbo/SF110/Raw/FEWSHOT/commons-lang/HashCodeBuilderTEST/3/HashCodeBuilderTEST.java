package org.apache.commons.lang3.builder;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

public class HashCodeBuilderTest {
    
    @Test
    public void testAppendBoolean(){
        HashCodeBuilder hcb = new HashCodeBuilder();
        assertEquals(hcb.append(true).toHashCode(), 37);
        assertEquals(hcb.append(false).toHashCode(), 38);
        
        boolean[] arr = {true, false};
        assertEquals(hcb.append(arr).toHashCode(), 39);
    }
    
    @Test
    public void testAppendByte(){
        HashCodeBuilder hcb = new HashCodeBuilder();
        assertEquals(hcb.append((byte)1).toHashCode(), 38);
        assertEquals(hcb.append((byte)2).toHashCode(), 76);
        
        byte[] arr = {1, 2};
        assertEquals(hcb.append(arr).toHashCode(), 77);
    }
    
    @Test
    public void testAppendChar(){
        HashCodeBuilder hcb = new HashCodeBuilder();
        assertEquals(hcb.append('a').toHashCode(), 105);
        assertEquals(hcb.append('b').toHashCode(), 212);
        
        char[] arr = {'a', 'b'};
        assertEquals(hcb.append(arr).toHashCode(), 213);
    }
    
    @Test
    public void testAppendDouble(){
        HashCodeBuilder hcb = new HashCodeBuilder();
        assertEquals(hcb.append(1.0).toHashCode(), 208);
        assertEquals(hcb.append(2.0).toHashCode(), 6250);
        
        double[] arr = {1.0, 2.0};
        assertEquals(hcb.append(arr).toHashCode(), 6251);
    }
    
    @Test
    public void testAppendFloat(){
        HashCodeBuilder hcb = new HashCodeBuilder();
        assertEquals(hcb.append(1.0f).toHashCode(), 333);
        assertEquals(hcb.append(2.0f).toHashCode(), 1320);
        
        float[] arr = {1.0f, 2.0f};
        assertEquals(hcb.append(arr).toHashCode(), 1321);
    }
    
    @Test
    public void testAppendInt(){
        HashCodeBuilder hcb = new HashCodeBuilder();
        assertEquals(hcb.append(1).toHashCode(), 38);
        assertEquals(hcb.append(2).toHashCode(), 76);
        
        int[] arr = {1, 2};
        assertEquals(hcb.append(arr).toHashCode(), 77);
    }
    
    @Test
    public void testAppendLong(){
        HashCodeBuilder hcb = new HashCodeBuilder();
        assertEquals(hcb.append(1L).toHashCode(), 205);
        assertEquals(hcb.append(2L).toHashCode(), 412);
        
        long[] arr = {1L, 2L};
        assertEquals(hcb.append(arr).toHashCode(), 413);
    }
    
    @Test
    public void testAppendObject(){
        HashCodeBuilder hcb = new HashCodeBuilder();
        assertEquals(hcb.append(new Object()).toHashCode(), 37);
        
        Object[] arr = {new Object(), new Object()};
        assertEquals(hcb.append(arr).toHashCode(), 74);
    }
    
    @Test
    public void testAppendShort(){
        HashCodeBuilder hcb = new HashCodeBuilder();
        assertEquals(hcb.append((short)1).toHashCode(), 38);
        assertEquals(hcb.append((short)2).toHashCode(), 76);
        
        short[] arr = {1, 2};
        assertEquals(hcb.append(arr).toHashCode(), 77);
    }
    
    @Test
    public void testAppendSuper(){
        HashCodeBuilder hcb = new HashCodeBuilder();
        assertEquals(hcb.appendSuper(37).toHashCode(), 37);
        assertEquals(hcb.appendSuper(38).toHashCode(), 1823);
    }
    
    @Test
    public void testToHashCode(){
        HashCodeBuilder hcb = new HashCodeBuilder();
        assertEquals(hcb.toHashCode(), 0);
        
        hcb.append(1).append('a').append(true);
        assertEquals(hcb.toHashCode(), 691);
    }
    
    @Test
    public void testBuild(){
        HashCodeBuilder hcb = new HashCodeBuilder();
        assertEquals((int)hcb.build(), 0);
        
        hcb.append(1).append('a').append(true);
        assertEquals((int)hcb.build(), 691);
    }
    
    @Test
    public void testReflectionHashCode(){
        Object obj = new Object();
        assertEquals(HashCodeBuilder.reflectionHashCode(1, 1, obj), 961);
        
        Set<String> excludeFields = new HashSet<>();
        excludeFields.add("field");
        assertEquals(HashCodeBuilder.reflectionHashCode(obj, excludeFields), 37);
        
        assertEquals(HashCodeBuilder.reflectionHashCode(obj, "field"), 37);
        
        assertEquals(HashCodeBuilder.reflectionHashCode(obj, true), 37);
    }
    
}