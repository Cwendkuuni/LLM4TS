import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.FDistributionImpl;
import org.junit.Test;

public class FDistributionImplTest {

    @Test
    public void testConstructor() {
        FDistribution distribution = new FDistributionImpl(5.0, 10.0);
        assert(distribution instanceof FDistribution);
        assert(distribution.getNumeratorDegreesOfFreedom() == 5.0);
        assert(distribution.getDenominatorDegreesOfFreedom() == 10.0);
    }
}