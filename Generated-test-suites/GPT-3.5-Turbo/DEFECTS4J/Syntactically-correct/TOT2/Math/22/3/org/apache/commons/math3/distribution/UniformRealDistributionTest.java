import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UniformRealDistributionTest {

    @Test
    public void testDensity() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        assertEquals(0.0, dist.density(-1), 0.0);
        assertEquals(1.0, dist.density(0.5), 0.0);
        assertEquals(0.0, dist.density(2), 0.0);
    }

    // Additional test cases can be added here

}