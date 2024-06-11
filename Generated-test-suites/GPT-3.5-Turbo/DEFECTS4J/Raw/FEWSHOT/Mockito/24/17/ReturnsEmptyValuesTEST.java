package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ReturnsEmptyValuesTest {
    @Test
    public void testAnswer_returnValueFor() {
        ReturnsEmptyValues rev = new ReturnsEmptyValues();
        
        assertEquals(false, rev.returnValueFor(boolean.class));
        assertEquals((byte) 0, rev.returnValueFor(byte.class));
        assertEquals((short) 0, rev.returnValueFor(short.class));
        assertEquals(0, rev.returnValueFor(int.class));
        assertEquals(0L, rev.returnValueFor(long.class));
        assertEquals(0.0f, rev.returnValueFor(float.class), 0.0f);
        assertEquals(0.0, rev.returnValueFor(double.class), 0.0);
        assertNull(rev.returnValueFor(void.class));
        assertNull(rev.returnValueFor(Object.class));
    }
    
    @Test
    public void testAnswer_answer() {
        ReturnsEmptyValues rev = new ReturnsEmptyValues();
        
        assertEquals(new LinkedList<Object>(), rev.answer(null));
    }
}