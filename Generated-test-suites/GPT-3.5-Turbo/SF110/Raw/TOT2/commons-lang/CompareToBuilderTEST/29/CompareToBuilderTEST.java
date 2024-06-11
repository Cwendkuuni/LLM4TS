package org.apache.commons.lang3.builder;

import static org.junit.Assert.*;
import org.junit.Test;

public class CompareToBuilderTest {

    @Test
    public void testReflectionCompare() {
        Object lhs = "Hello";
        Object rhs = "Hello";
        int result = CompareToBuilder.reflectionCompare(lhs, rhs);
        assertEquals(0, result);
    }

    @Test
    public void testAppendSuper() {
        CompareToBuilder builder = new CompareToBuilder();
        builder.appendSuper(10);
        int result = builder.toComparison();
        assertEquals(10, result);
    }

    @Test
    public void testAppend() {
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(5, 10);
        int result = builder.toComparison();
        assertEquals(-1, result);
    }

    // Add more test cases for other methods as needed

}