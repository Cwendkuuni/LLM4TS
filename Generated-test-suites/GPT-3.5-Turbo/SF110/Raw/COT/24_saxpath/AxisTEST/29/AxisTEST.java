
package org.saxpath;

import org.junit.Test;
import static org.junit.Assert.*;

public class AxisTest {

    @Test
    public void testLookup() {
        int expected = 1;
        int result = Axis.lookup("child");
        assertEquals(expected, result);
    }

    @Test
    public void testLookupWithInvalidAxisName() {
        int expected = 0;
        int result = Axis.lookup("invalid_axis");
        assertEquals(expected, result);
    }

    @Test
    public void testLookupWithInvalidAxisNum() {
        String expected = null;
        String result = Axis.lookup(0);
        assertEquals(expected, result);
    }
}
