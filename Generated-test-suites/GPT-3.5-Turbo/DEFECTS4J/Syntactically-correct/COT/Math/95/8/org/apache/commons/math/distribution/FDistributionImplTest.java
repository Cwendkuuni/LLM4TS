import org.junit.Test;
import org.apache.commons.math.distribution.FDistributionImpl;
import org.apache.commons.math.MathException;

public class FDistributionImplTest {

    @Test
    public void testCumulativeProbability() throws MathException {
        FDistributionImpl f = new FDistributionImpl(4.0, 5.0);
        double result = f.cumulativeProbability(2.0);
        assertEquals(0.7557010211271552, result, 0.0001);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        FDistributionImpl f = new FDistributionImpl(4.0, 5.0);
        double result = f.inverseCumulativeProbability(0.75);
        assertEquals(1.6221067857494744, result, 0.0001);
    }

}