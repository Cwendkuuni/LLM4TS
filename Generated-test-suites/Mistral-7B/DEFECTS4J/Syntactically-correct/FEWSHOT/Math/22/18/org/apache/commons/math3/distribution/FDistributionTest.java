package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.special.Beta;

public class FDistributionTest {

    @Test
    public void testFDistribution() throws NotStrictlyPositiveException {
        double numeratorDegreesOfFreedom = 2.5;
        double denominatorDegreesOfFreedom = 4.5;
        FDistribution fDistribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        
        assertEquals(numeratorDegreesOfFreedom, fDistribution.getNumeratorDegreesOfFreedom(), 0.001);
        assertEquals(denominatorDegreesOfFreedom, fDistribution.getDenominatorDegreesOfFreedom(), 0.001);
        
        double density = fDistribution.density(1.5);
        double expectedDensity = FastMath.exp(2.5 * FastMath.log(1.5) - FastMath.log(1.5) + 1.5 * FastMath.log(2.5) + 1.5 * FastMath.log(4.5) - 1.5 * FastMath.log(1.5 * 2.5 + 4.5 * 1.5) - Beta.logBeta(2.5, 1.5) - Beta.logBeta(1.5, 1.5));
        assertEquals(expectedDensity, density, 0.001);

        double cumulativeProbability = fDistribution.cumulativeProbability(0.5);
        double expectedCumulativeProbability = Beta.regularizedBeta(2.5 * 0.5 / (4.5 + 2.5 * 0.5), 1.5, 1.5);
        assertEquals(expectedCumulativeProbability, cumulativeProbability, 0.001);

        double mean = fDistribution.getNumericalMean();
        if (denominatorDegreesOfFreedom > 2) {
            assertEquals(denominatorDegreesOfFreedom / (denominatorDegreesOfFreedom - 2), mean, 0.001);
        } else {
            assertNotNull(mean);
            assertTrue(Double.isNaN(mean));
        }

        double variance = fDistribution.getNumericalVariance();
        if (denominatorDegreesOfFreedom > 4) {
            double numeratorDF = numeratorDegreesOfFreedom;
            double denomDFMinusTwo = denominatorDegreesOfFreedom - 2;
            double expectedVariance = (2 * (denominatorDegreesOfFreedom * denominatorDegreesOfFreedom) * (numeratorDF + denominatorDegreesOfFreedom - 2)) / ((numeratorDF * (denomDFMinusTwo * denomDFMinusTwo) * (denominatorDegreesOfFreedom - 4)));
            assertEquals(expectedVariance, variance, 0.001);
        } else {
            assertNotNull(variance);
            assertTrue(Double.isNaN(variance));
        }
    }
}