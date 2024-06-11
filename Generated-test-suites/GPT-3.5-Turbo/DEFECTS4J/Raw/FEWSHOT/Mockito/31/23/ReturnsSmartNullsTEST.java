package org.mockito.internal.stubbing.defaultanswers.tests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.mockito.internal.stubbing.defaultanswers.ReturnsSmartNulls;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswer() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();

        Object result = returnsSmartNulls.answer(null);
        assertNotNull(result);
        assertEquals("SmartNull returned by unstubbed method on mock", result.toString());
    }
}