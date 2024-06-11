import static org.junit.Assert.assertEquals;

import org.apache.commons.math.MathException;
import org.apache.commons.math.special.Beta;
import org.junit.Before;
import org.junit.Test;

public class FDistributionImplTest {

    private static final double EPSILON = 1e-10;
    
    private FDistributionImpl fDist;
    
    @Before
    public void setUp() {
        fDist = new FDistributionImpl(4, 6);
    }
    
    @Test
    public void testCumulativeProbability() throws MathException {
        double x = 1.5;
        double expected = Beta.regularizedBeta((4 * x) / (6 + 4 * x),
                0.5 * 4,
                0.5 * 6);
        double actual = fDist.cumulativeProbability(x);
        assertEquals(expected, actual, EPSILON);
    }
    
    @Test
    public void testCumulativeProbability_ZeroX() throws MathException {
        double x = 0.0;
        double expected = 0.0;
        double actual = fDist.cumulativeProbability(x);
        assertEquals(expected, actual, EPSILON);
    }
    
    @Test
    public void testCumulativeProbability_NegativeX() throws MathException {
        double x = -1.0;
        double expected = 0.0;
        double actual = fDist.cumulativeProbability(x);
        assertEquals(expected, actual, EPSILON);
    }
    
    @Test
    public void testCumulativeProbability_InfinityX() throws MathException {
        double x = Double.POSITIVE_INFINITY;
        double expected = 1.0;
        double actual = fDist.cumulativeProbability(x);
        assertEquals(expected, actual, EPSILON);
    }
    
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        double p = 0.75;
        double expected = 10.39268862165441;
        double actual = fDist.inverseCumulativeProbability(p);
        assertEquals(expected, actual, EPSILON);
    }
    
    @Test
    public void testInverseCumulativeProbability_ZeroP() throws MathException {
        double p = 0.0;
        double expected = 0.0;
        double actual = fDist.inverseCumulativeProbability(p);
        assertEquals(expected, actual, EPSILON);
    }
    
    @Test
    public void testInverseCumulativeProbability_OneP() throws MathException {
        double p = 1.0;
        double expected = Double.POSITIVE_INFINITY;
        double actual = fDist.inverseCumulativeProbability(p);
        assertEquals(expected, actual, EPSILON);
    }
    
    @Test
    public void testGetDomainLowerBound() {
        double p = 0.25;
        double expected = 0.0;
        double actual = fDist.getDomainLowerBound(p);
        assertEquals(expected, actual, EPSILON);
    }
    
    @Test
    public void testGetDomainUpperBound() {
        double p = 0.25;
        double expected = Double.MAX_VALUE;
        double actual = fDist.getDomainUpperBound(p);
        assertEquals(expected, actual, EPSILON);
    }
    
    @Test
    public void testGetInitialDomain() {
        double p = 0.25;
        double expected = 1.2;
        double actual = fDist.getInitialDomain(p);
        assertEquals(expected, actual, EPSILON);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedom_Negative() {
        fDist.setNumeratorDegreesOfFreedom(-4);
    }
    
    @Test
    public void testSetNumeratorDegreesOfFreedom_Positive() {
        fDist.setNumeratorDegreesOfFreedom(4);
        assertEquals(4, fDist.getNumeratorDegreesOfFreedom(), EPSILON);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedom_Negative() {
        fDist.setDenominatorDegreesOfFreedom(-6);
    }
    
    @Test
    public void testSetDenominatorDegreesOfFreedom_Positive() {
        fDist.setDenominatorDegreesOfFreedom(6);
        assertEquals(6, fDist.getDenominatorDegreesOfFreedom(), EPSILON);
    }
    
}