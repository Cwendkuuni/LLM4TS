import org.junit.Test;
import org.junit.Assert.*;
import org.apache.commons.math.distribution.FDistributionImpl;

public class FDistributionImplTest {

    @Test
    public void testConstructor() {
        // Given
        double numeratorDegreesOfFreedom = 2.0;
        double denominatorDegreesOfFreedom = 4.0;

        // When
        FDistributionImpl fDistributionImpl = new FDistributionImpl(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);

        // Then
        assertEquals(numeratorDegreesOfFreedom, fDistributionImpl.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(denominatorDegreesOfFreedom, fDistributionImpl.getDenominatorDegreesOfFreedom(), 0.0);
    }
}