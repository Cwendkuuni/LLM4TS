import org.junit.Test;
import org.apache.commons.math.distribution.FDistributionImpl;
import org.apache.commons.math.MathException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class FDistributionImplTest {

    @Test
    public void testConstructor() {
        try {
            FDistributionImpl fDistributionImpl = new FDistributionImpl(-1, 5);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected exception
        }
        
        try {
            FDistributionImpl fDistributionImpl = new FDistributionImpl(5, 0);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected exception
        }
    }

    @Test
    public void testCumulativeProbability() {
        try {
            FDistributionImpl fDistributionImpl = new FDistributionImpl(3, 5);
            double result = fDistributionImpl.cumulativeProbability(-1);
            assertEquals(0.0, result, 0);
        } catch (MathException e) {
            fail("MathException thrown");
        }
        
        try {
            FDistributionImpl fDistributionImpl = new FDistributionImpl(3, 5);
            double result = fDistributionImpl.cumulativeProbability(2);
            assertEquals(0.776473040, result, 1e-9);
        } catch (MathException e) {
            fail("MathException thrown");
        }
        
        try {
            FDistributionImpl fDistributionImpl = new FDistributionImpl(0.5, 8);
            double result = fDistributionImpl.cumulativeProbability(2);
            assertEquals(0.904990513, result, 1e-9);
        } catch (MathException e) {
            fail("MathException thrown");
        }

    }

    @Test
    public void testInverseCumulativeProbability() {
        try {
            FDistributionImpl fDistributionImpl = new FDistributionImpl(3, 5);
            double result = fDistributionImpl.inverseCumulativeProbability(0.3);
            assertEquals(0.567762028, result, 1e-9);
        } catch (MathException e) {
            fail("MathException thrown");
        }
        
        try {
            FDistributionImpl fDistributionImpl = new FDistributionImpl(3, 5);
            double result = fDistributionImpl.inverseCumulativeProbability(0.9);
            assertEquals(2.743746453, result, 1e-9);
        } catch (MathException e) {
            fail("MathException thrown");
        }
        
        try {
            FDistributionImpl fDistributionImpl = new FDistributionImpl(0.5, 8);
            double result = fDistributionImpl.inverseCumulativeProbability(0.7);
            assertEquals(0.321606965, result, 1e-9);
        } catch (MathException e) {
            fail("MathException thrown");
        }
    }

    @Test
    public void testGetDomainLowerBound() {
        FDistributionImpl fDistributionImpl = new FDistributionImpl(3, 5);
        double result = fDistributionImpl.getDomainLowerBound(0.7);
        assertEquals(0.0, result, 0);
    }

    @Test
    public void testGetDomainUpperBound() {
        FDistributionImpl fDistributionImpl = new FDistributionImpl(3, 5);
        double result = fDistributionImpl.getDomainUpperBound(0.7);
        assertEquals(Double.MAX_VALUE, result, 0);
    }

    @Test
    public void testGetInitialDomain() {
        FDistributionImpl fDistributionImpl = new FDistributionImpl(3, 5);
        double result = fDistributionImpl.getInitialDomain(0.7);
        assertEquals(2.5, result, 0);
    }

    @Test
    public void testSetNumeratorDegreesOfFreedom() {
        FDistributionImpl fDistributionImpl = new FDistributionImpl(3, 5);
        fDistributionImpl.setNumeratorDegreesOfFreedom(4);
        double result = fDistributionImpl.getNumeratorDegreesOfFreedom();
        assertEquals(4, result, 0);
    }

    @Test
    public void testSetDenominatorDegreesOfFreedom() {
        FDistributionImpl fDistributionImpl = new FDistributionImpl(3, 5);
        fDistributionImpl.setDenominatorDegreesOfFreedom(6);
        double result = fDistributionImpl.getDenominatorDegreesOfFreedom();
        assertEquals(6, result, 0);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistributionImpl fDistributionImpl = new FDistributionImpl(3, 5);
        double result = fDistributionImpl.getNumeratorDegreesOfFreedom();
        assertEquals(3, result, 0);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistributionImpl fDistributionImpl = new FDistributionImpl(3, 5);
        double result = fDistributionImpl.getDenominatorDegreesOfFreedom();
        assertEquals(5, result, 0);
    }
}