import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math.MathException;

public class FDistributionImplTest {
    
    @Test
    public void testCumulativeProbability() {
        FDistributionImpl fDist = new FDistributionImpl(2, 3);
        try {
            double result = fDist.cumulativeProbability(0.5);
            assertEquals(0.36338022763241836, result, 0.0001);
        } catch (MathException e) {
            fail("MathException not expected");
        }
    }
    
    @Test
    public void testInverseCumulativeProbability() {
        FDistributionImpl fDist = new FDistributionImpl(2, 3);
        try {
            double result = fDist.inverseCumulativeProbability(0.2);
            assertEquals(0.1683358712182347, result, 0.0001);
            
            result = fDist.inverseCumulativeProbability(0.8);
            assertEquals(5.143252848261349, result, 0.0001);
            
        } catch (MathException e) {
            fail("MathException not expected");
        }
    }
    
    @Test
    public void testSetNumeratorDegreesOfFreedom() {
        FDistributionImpl fDist = new FDistributionImpl(2, 3);
        fDist.setNumeratorDegreesOfFreedom(5);
        assertEquals(5.0, fDist.getNumeratorDegreesOfFreedom(), 0.0001);
    }
    
    @Test
    public void testSetDenominatorDegreesOfFreedom() {
        FDistributionImpl fDist = new FDistributionImpl(2, 3);
        fDist.setDenominatorDegreesOfFreedom(5);
        assertEquals(5.0, fDist.getDenominatorDegreesOfFreedom(), 0.0001);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNumeratorDegreesOfFreedomException() {
        FDistributionImpl fDist = new FDistributionImpl(2, 3);
        fDist.setNumeratorDegreesOfFreedom(-5);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testDenominatorDegreesOfFreedomException() {
        FDistributionImpl fDist = new FDistributionImpl(2, 3);
        fDist.setDenominatorDegreesOfFreedom(-5);
    }
}