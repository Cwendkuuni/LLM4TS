
package org.apache.commons.math3.dfp;

import static org.junit.Assert.*;
import org.junit.Test;

public class DfpDecTest {

    @Test
    public void testRound() {
        // Test case 1
        DfpDec dfp1 = new DfpDec(new DfpField(), 0.25);
        int actual1 = dfp1.round(0);
        int expected1 = 0;
        assertEquals(expected1, actual1);

        // Test case 2
        DfpDec dfp2 = new DfpDec(new DfpField(), 0.999);
        int actual2 = dfp2.round(0);
        int expected2 = 16;
        assertEquals(expected2, actual2);

        // Test case 3
        DfpDec dfp3 = new DfpDec(new DfpField(), -0.75);
        int actual3 = dfp3.round(0);
        int expected3 = 0;
        assertEquals(expected3, actual3);
    }

    @Test
    public void testNextAfter() {
        // Test case 1
        DfpDec dfp1 = new DfpDec(new DfpField(), 0.5);
        DfpDec x1 = new DfpDec(new DfpField(), 0.75);
        DfpDec actual1 = dfp1.nextAfter(x1);
        DfpDec expected1 = new DfpDec(new DfpField(), 0.5001);
        assertEquals(expected1, actual1);

        // Test case 2
        DfpDec dfp2 = new DfpDec(new DfpField(), -0.5);
        DfpDec x2 = new DfpDec(new DfpField(), -0.75);
        DfpDec actual2 = dfp2.nextAfter(x2);
        DfpDec expected2 = new DfpDec(new DfpField(), -0.5001);
        assertEquals(expected2, actual2);

        // Test case 3
        DfpDec dfp3 = new DfpDec(new DfpField(), 0.0);
        DfpDec x3 = new DfpDec(new DfpField(), 0.0);
        DfpDec actual3 = dfp3.nextAfter(x3);
        DfpDec expected3 = new DfpDec(new DfpField(), 0.0);
        assertEquals(expected3, actual3);
    }
}
