package org.apache.commons.lang3.builder;

import org.junit.Test;
import static org.junit.Assert.*;

public class HashCodeBuilderTest {

    @Test
    public void testReflectionHashCode() {
        int result = HashCodeBuilder.reflectionHashCode(10, 20, "test");
        assertEquals(664, result);
    }

    @Test
    public void testReflectionHashCodeWithBoolean() {
        int result = HashCodeBuilder.reflectionHashCode(true, false);
        assertEquals(655, result);
    }

    @Test
    public void testReflectionHashCodeWithExcludeFields() {
        int result = HashCodeBuilder.reflectionHashCode("test", new String[]{"field1", "field2"});
        assertEquals(663, result);
    }

    // Add more test cases for other methods including edge cases and exception handling scenarios

}