import org.apache.commons.math3.special.Beta;
import org.apache.commons.math3.stat.StatUtils;
import org.junit.Test;
import org.junit.Assert.*;

import static org.apache.commons.math3.util.Precision.absTolerance;
import static org.apache.commons.math3.util.Precision.relTolerance;

public class FDistributionImplTest {

    @Test
    public void testFDistributionImplConstruction() {
        FDistributionImpl fd = new FDistributionImpl(1.0, 2.0);
        Assert.assertEquals(1.0, fd.getNumeratorDegreesOfFreedom(), 0.0);
        Assert.assertEquals(2.0, fd.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFDistributionImplNegativeNDOF() {
        new FDistributionImpl(-1.0, 2.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFDistributionImplNegativeDDOF() {
        new FDistributionImpl(1.0, -2.0);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        FDistributionImpl fd = new FDistributionImpl(1.0, 2.0);
        double expected[] = {0.0, 0.1, 0.5, 1.0};
        for (double x : expected) {
            double result = fd.cumulativeProbability(x);
            assertEquals(x, result, absTolerance);
        }
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityNegativeX() throws MathException {
        FDistributionImpl fd = new FDistributionImpl(1.0, 2.0);
        fd.cumulativeProbability(-1.0);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        FDistributionImpl fd = new FDistributionImpl(1.0, 2.0);
        double expected[] = {0.0, 1.586552979428175, 2.113144649954618, 3.123425253268248};
        for (double p : expected) {
            double result = fd.inverseCumulativeProbability(p);
            assertEquals(StatUtils.percentile(1.0, new FDistribution(1.0, 2.0), p), result, relTolerance);
        }
    }

    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityOutOfRange() throws MathException {
        FDistributionImpl fd = new FDistributionImpl(1.0, 2.0);
        fd.inverseCumulativeProbability(1.5);
    }

    @Test
    public void testSettersGetters() {
        FDistributionImpl fd = new FDistributionImpl(1.0, 2.0);
        fd.setNumeratorDegreesOfFreedom(3.0);
        fd.setDenominatorDegreesOfFreedom(4.0);
        assertEquals(3.0, fd.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(4.0, fd.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedomNegative() {
        FDistributionImpl fd = new FDistributionImpl(1.0, 2.0);
        fd.setNumeratorDegreesOfFreedom(-1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedomNegative() {
        FDistributionImpl fd = new FDistributionImpl(1.0, 2.0);
        fd.setDenominatorDegreesOfFreedom(-1.0);
    }
}