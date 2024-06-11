import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.NormalDistributionImpl;
import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImpl_MeanZero_StandardDeviationOne() {
        NormalDistribution dist = new NormalDistributionImpl();
        double expectedResult = 0.5;
        double result = dist.cumulativeProbability(0);
        assertEquals(expectedResult, result, 0.001);
    }

    @Test
    public void testNormalDistributionImpl_MeanPositive_StandardDeviationPositive() {
        NormalDistribution dist = new NormalDistributionImpl(5, 3);
        double expectedResult = 0.999865041; // Approximate value based on Erf function
        double result = dist.cumulativeProbability(5+3);
        assertEquals(expectedResult, result, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNormalDistributionImpl_NegativeStandardDeviation() {
        NormalDistribution dist = new NormalDistributionImpl(0, -1);
    }

    @Test(expected = MathException.class)
    public void testNormalDistributionImpl_ExceedMaxIterations() {
        NormalDistribution dist = new NormalDistributionImpl(1000, 1);
        dist.cumulativeProbability(10000);
    }
}