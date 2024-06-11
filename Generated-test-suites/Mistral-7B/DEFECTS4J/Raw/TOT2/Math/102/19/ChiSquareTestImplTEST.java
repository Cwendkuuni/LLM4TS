import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.apache.commons.math3.stat.inference.ChiSquareTestImpl;
import org.junit.Test;
import static org.junit.Assert.*;

public class ChiSquareTestImplTest {

    @Test
    public void testChiSquare() {
        ChiSquareTestImpl test = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(1));
        double[] expected = {1.0, 2.0};
        long[] observed = {2.0, 1.0};
        double chiSquare = test.chiSquare(expected, observed);
        assertEquals(2.0, chiSquare, 0.001);
    }
}