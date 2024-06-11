
package org.apache.commons.math.distribution;

import org.apache.commons.math.MathException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class FDistributionImplTest {

    @Test
    public void testCumulativeProbability() throws MathException {
        FDistributionImpl distribution = new FDistributionImpl(5.0, 10.0);
        double x = 2.0;
        double expected = 0.3910;
        double delta = 0.0001;
        double result = distribution.cumulativeProbability(x);
        assertEquals(expected, result, delta);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        FDistributionImpl distribution = new FDistributionImpl(5.0, 10.0);
        double p = 0.6;
        double expected = 0.9099;
        double delta = 0.0001;
        double result = distribution.inverseCumulativeProbability(p);
        assertEquals(expected, result, delta);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedom() {
        FDistributionImpl distribution = new FDistributionImpl(5.0, 10.0);
        double degreesOfFreedom = -1.0;
        distribution.setNumeratorDegreesOfFreedom(degreesOfFreedom);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedom() {
        FDistributionImpl distribution = new FDistributionImpl(5.0, 10.0);
        double degreesOfFreedom = -1.0;
        distribution.setDenominatorDegreesOfFreedom(degreesOfFreedom);
    }
}
