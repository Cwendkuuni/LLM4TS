import org.junit.Test;
import org.apache.commons.math.distribution.FDistribution;
import static org.junit.Assert.assertEquals;

public class FDistributionImplTest {

    @Test
    public void testFDistributionImplConstruction() {
        FDistribution fd = new FDistributionImpl(3.0, 4.0);
        FDistribution expected = new FDistribution(3.0, 4.0);
        assertEquals(expected, fd);
    }
}