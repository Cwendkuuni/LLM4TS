package org.apache.commons.math3.dfp;

import org.junit.Test;
import static org.junit.Assert.*;

public class DfpDecTest {

    @Test
    public void testDfpDecConstructor() {
        DfpDec dfp = new DfpDec(10);
        assertEquals("Incorrect value for DfpDec constructor", 10.0, dfp.toDouble(), 0.0);
    }

    @Test
    public void testDfpDecAddition() {
        DfpDec dfp1 = new DfpDec(5);
        DfpDec dfp2 = new DfpDec(7);
        DfpDec result = dfp1.add(dfp2);
        assertEquals("Incorrect result for addition", 12.0, result.toDouble(), 0.0);
    }

    @Test
    public void testDfpDecSubtraction() {
        DfpDec dfp1 = new DfpDec(10);
        DfpDec dfp2 = new DfpDec(3);
        DfpDec result = dfp1.subtract(dfp2);
        assertEquals("Incorrect result for subtraction", 7.0, result.toDouble(), 0.0);
    }

    // Additional test cases for other methods can be added here

}