package org.apache.commons.lang3.builder;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class HashCodeBuilderTest {
    @Test
    public void testHashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        assertEquals(0, builder.toHashCode());
        
        builder = new HashCodeBuilder(17, 37);
        assertEquals(17, builder.toHashCode());
        
        builder.append(true);
        assertEquals(702, builder.toHashCode());
        
        builder.append(false);
        assertEquals(703, builder.toHashCode());
        
        boolean[] boolArray = {true, false, true};
        builder.append(boolArray);
        assertEquals(27961, builder.toHashCode());
        
        builder.append((byte)10);
        assertEquals(28071, builder.toHashCode());
        
        byte[] byteArray = {1, 2, 3};
        builder.append(byteArray);
        assertEquals(32415, builder.toHashCode());
        
        builder.append('a');
        assertEquals(33719, builder.toHashCode());
        
        char[] charArray = {'a', 'b', 'c'};
        builder.append(charArray);
        assertEquals(44543, builder.toHashCode());
        
        builder.append(3.14);
        assertEquals(57329, builder.toHashCode());
        
        double[] doubleArray = {1.1, 2.2, 3.3};
        builder.append(doubleArray);
        assertEquals(61072569, builder.toHashCode());
        
        builder.append(1.5f);
        assertEquals(236488249, builder.toHashCode());
        
        float[] floatArray = {1.1f, 2.2f, 3.3f};
        builder.append(floatArray);
        assertEquals(663378513, builder.toHashCode());
        
        builder.append(10);
        assertEquals(663378523, builder.toHashCode());
        
        int[] intArray = {1, 2, 3};
        builder.append(intArray);
        assertEquals(-1348419132, builder.toHashCode());
        
        builder.append(10000000000L);
        assertEquals(210076724, builder.toHashCode());
        
        long[] longArray = {10000000000L, 20000000000L, 30000000000L};
        builder.append(longArray);
        assertEquals(807464006, builder.toHashCode());
        
        String str = "test";
        builder.append(str);
        assertEquals(837903196, builder.toHashCode());
        
        String[] strArray = {"test1", "test2", "test3"};
        builder.append(strArray);
        assertEquals(1988682032, builder.toHashCode());
        
        builder.append((short)5);
        assertEquals(1988682483, builder.toHashCode());
        
        short[] shortArray = {5, 10, 15};
        builder.append(shortArray);
        assertEquals(2031447932, builder.toHashCode());
        
        builder.appendSuper(12345);
        assertEquals(-1934189640, builder.toHashCode());
    }
    
    @Test
    public void testReflectionHashCode() {
        Object obj = new Object();
        assertEquals(obj.hashCode(), HashCodeBuilder.reflectionHashCode(obj));
        
        obj = null;
        assertEquals(0, HashCodeBuilder.reflectionHashCode(obj));
    }
    
    @Test
    public void testReflectionHashCodeWithExcludeFields() {
        Object obj = new Object();
        assertEquals(obj.hashCode(), HashCodeBuilder.reflectionHashCode(17, 37, obj, false, Object.class, "field1", "field2"));
        
        obj = null;
        assertEquals(0, HashCodeBuilder.reflectionHashCode(17, 37, obj, false, Object.class, "field1", "field2"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testHashCodeWithEvenInitialValue() {
        HashCodeBuilder builder = new HashCodeBuilder(16, 37);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testHashCodeWithEvenMultiplier() {
        HashCodeBuilder builder = new HashCodeBuilder(17, 36);
    }
}