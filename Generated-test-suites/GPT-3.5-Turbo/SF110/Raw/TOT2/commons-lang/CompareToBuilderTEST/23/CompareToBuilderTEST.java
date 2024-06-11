package org.apache.commons.lang3.builder;

import org.junit.Test;
import static org.junit.Assert.*;

public class CompareToBuilderTest {

    // Test for reflectionCompare() method
    @Test
    public void testReflectionCompare() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        int result = CompareToBuilder.reflectionCompare(obj1, obj2);
        assertEquals(0, result);
    }

    // Test for reflectionCompare() method with null object
    @Test
    public void testReflectionCompareNull() {
        Object obj1 = new Object();
        Object obj2 = null;
        assertThrows(NullPointerException.class, () -> {
            CompareToBuilder.reflectionCompare(obj1, obj2);
        });
    }

    // Test for reflectionCompare() method with different types
    @Test
    public void testReflectionCompareClassCastException() {
        Object obj1 = new String("test");
        Object obj2 = new Integer(10);
        assertThrows(ClassCastException.class, () -> {
            CompareToBuilder.reflectionCompare(obj1, obj2);
        });
    }

    // Test for append() method
    @Test
    public void testAppend() {
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(10, 20);
        int result = builder.toComparison();
        assertEquals(-1, result);
    }

    // Add more test cases for other methods if needed

}