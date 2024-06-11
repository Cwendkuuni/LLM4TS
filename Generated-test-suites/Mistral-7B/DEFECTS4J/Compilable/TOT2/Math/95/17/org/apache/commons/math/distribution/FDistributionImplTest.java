package org.apache.commons.math.distribution;
import org.apache.commons.math.distribution.FDistributionImpl;
import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionImplTest {
    @Test
    public void testConstructor() {
        FDistributionImpl fd = new FDistributionImpl(5.0, 10.0);
        assertEquals(5.0, fd.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(10.0, fd.getDenominatorDegreesOfFreedom(), 0.0);
    }
}