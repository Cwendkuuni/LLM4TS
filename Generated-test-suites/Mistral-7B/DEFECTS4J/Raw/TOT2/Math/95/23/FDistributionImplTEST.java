import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class FDistributionImplTest {

    @Test
    public void testFDistributionImplConstruction() throws MathException {
        FDistributionImpl fDist = new FDistributionImpl(5.0, 10.0);
        assertEquals(5.0, fDist.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(10.0, fDist.getDenominatorDegreesOfFreedom(), 0.0);
    }
}