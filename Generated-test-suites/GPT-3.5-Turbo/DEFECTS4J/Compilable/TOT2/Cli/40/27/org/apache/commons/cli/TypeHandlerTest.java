package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeHandlerTest {

    @Test
    public void testCreateValueWithObject() {
        try {
            Object result = TypeHandler.createValue("test", PatternOptionBuilder.OBJECT_VALUE);
            assertNotNull(result);
            assertEquals(String.class, result.getClass());
            assertEquals("test", result);
        } catch (ParseException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testCreateValueWithNumber() {
        try {
            Number result = TypeHandler.createValue("123", PatternOptionBuilder.NUMBER_VALUE);
            assertNotNull(result);
            assertEquals(Long.class, result.getClass());
            assertEquals(123L, result);
        } catch (ParseException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testCreateValueWithInvalidClass() {
        try {
            Object result = TypeHandler.createValue("InvalidClass", PatternOptionBuilder.OBJECT_VALUE);
            assertNull(result);
        } catch (ParseException e) {
            assertEquals("Unable to find the class: InvalidClass", e.getMessage());
        }
    }

    // Additional test cases for other methods can be added here

}