package org.apache.commons.math3.stat.inference;

import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;

public class MannWhitneyUTestTest {

    @Test
    public void testMannWhitneyUTest() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};
        try {
            double result = test.mannWhitneyU(x, y);
            assertEquals(9.0, result, 0.0001);
        } catch(NullArgumentException e) {
            fail("NullArgumentException should not be thrown");
        } catch(NoDataException e) {
            fail("NoDataException should not be thrown");
        }
    }

    @Test
    public void testMannWhitneyUTestWithNullData() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = null;
        double[] y = {4.0, 5.0, 6.0};
        try {
            test.mannWhitneyU(x, y);
            fail("NullArgumentException should be thrown");
        } catch(NullArgumentException e) {
            assertTrue(true);
        } catch(NoDataException e) {
            fail("NoDataException should not be thrown");
        }
    }

    @Test
    public void testCalculateAsymptoticPValue() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        try {
            double result = test.calculateAsymptoticPValue(9.0, 3, 3);
            assertEquals(1.0, result, 0.0001);
        } catch(ConvergenceException e) {
            fail("ConvergenceException should not be thrown");
        } catch(MaxCountExceededException e) {
            fail("MaxCountExceededException should not be thrown");
        }
    }

    @Test
    public void testCalculateAsymptoticPValueWithConvergenceException() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        try {
            double result = test.calculateAsymptoticPValue(9.0, 0, 0);
            fail("ConvergenceException should be thrown");
        } catch(ConvergenceException e) {
            assertTrue(true);
        } catch(MaxCountExceededException e) {
            fail("MaxCountExceededException should not be thrown");
        }
    }

}