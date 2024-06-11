package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ReturnsEmptyValuesTest {
    @Test
    public void testAnswer() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        
        // Test toString method
        assertEquals("Mock for Object, hashCode: 0", answer.answer(null));
        
        // Test compareTo method
        assertEquals(1, answer.answer(null));
        
        // Test primitive return type
        assertNotNull(answer.answer(null));
        
        // Test collection return type
        assertNotNull(answer.answer(null));
        
        // Test other return types
        assertNull(answer.answer(null));
    }
}