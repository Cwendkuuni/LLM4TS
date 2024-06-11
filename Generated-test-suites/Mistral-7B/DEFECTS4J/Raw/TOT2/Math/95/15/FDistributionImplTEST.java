import org.apache.commons.math3.distribution.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class FDistributionImplTest {

    @Test
    public void testCumulativeProbability() {
        FDistributionImpl distribution = new FDistributionImpl(1.0, 2.0);
        double x = 1.5;
        double expected = 0.832365322885584;
        double actual = distribution.cumulativeProbability(x);
        assertEquals(expected, actual, 0.0001);
    }
}