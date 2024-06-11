package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ReturnsEmptyValuesTest {
    @Test
    public void testAnswer() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        
        // Test toString() method
        assertEquals("Mock for Object, hashCode: 0", answer.answer(new InvocationOnMock()));
        
        // Test compareTo() method
        assertEquals(1, answer.answer(new InvocationOnMock()));
        
        // Test primitive and wrapper types
        assertNull(answer.answer(new InvocationOnMock()));
        assertEquals(0, answer.answer(new InvocationOnMock()));
        assertEquals(0L, answer.answer(new InvocationOnMock()));
        assertEquals(0.0f, answer.answer(new InvocationOnMock()), 0.0f);
        assertEquals(0.0, answer.answer(new InvocationOnMock()), 0.0);
        assertEquals(false, answer.answer(new InvocationOnMock()));
        assertEquals(' ', answer.answer(new InvocationOnMock()));
        
        // Test collection types
        assertEquals(new LinkedList<Object>(), answer.answer(new InvocationOnMock()));
        assertEquals(new HashSet<Object>(), answer.answer(new InvocationOnMock()));
        assertEquals(new TreeSet<Object>(), answer.answer(new InvocationOnMock()));
        assertEquals(new LinkedHashSet<Object>(), answer.answer(new InvocationOnMock()));
        assertEquals(new ArrayList<Object>(), answer.answer(new InvocationOnMock()));
        assertEquals(new HashMap<Object, Object>(), answer.answer(new InvocationOnMock()));
        assertEquals(new TreeMap<Object, Object>(), answer.answer(new InvocationOnMock()));
        assertEquals(new LinkedHashMap<Object, Object>(), answer.answer(new InvocationOnMock()));
    }
}