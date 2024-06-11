import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.special.Beta;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class FDistributionTest {

    private double numeratorDegreesOfFreedom;
    private double denominatorDegreesOfFreedom;

    public FDistributionTest(double numeratorDegreesOfFreedom, double denominatorDegreesOfFreedom) {
        this.numeratorDegreesOfFreedom = numeratorDegreesOfFreedom;
        this.denominatorDegreesOfFreedom = denominatorDegreesOfFreedom;
    }

    @Parameterized.Parameters
    public static Iterable<Object[]> data() {
        return new Object[][]{
                {1.0, 2.0},
                {2.0, 3.0},
                {5.0, 6.0},
                {10.0, 15.0},
                {-1.0, 2.0},
                {1.0, -2.0}
        };
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidDegreesOfFreedom() throws NotStrictlyPositiveException {
        new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
    }

    @Test
    public void testGetters() {
        FDistribution distribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        assertEquals(numeratorDegreesOfFreedom, distribution.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(denominatorDegreesOfFreedom, distribution.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testDensity() {
        FDistribution distribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        double result = distribution.density(0.5);
        assertEquals(result, 0.3141592653589793, 0.0000001);
        result = distribution.density(1.0);
        assertEquals(result, 0.2339785954553284, 0.0000001);
    }

    @Test
    public void testCumulativeProbability() {
        FDistribution distribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        double result = distribution.cumulativeProbability(0.5);
        assertEquals(result, 0.5, 0.0000001);
        result = distribution.cumulativeProbability(1.0);
        assertEquals(result, 0.8034771245308522, 0.0000001);
        result = distribution.cumulativeProbability(2.0);
        assertEquals(result, 1.0, 0.0000001);
    }

    @Test
    public void testGetNumericalMean() {
        FDistribution distribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        double result = distribution.getNumericalMean();
        if (denominatorDegreesOfFreedom > 2) {
            assertEquals(result, denominatorDegreesOfFreedom / (denominatorDegreesOfFreedom - 2), 0.0000001);
        } else {
            assertEquals(result, Double.NaN, 0.0);
        }
    }

    @Test
    public void testGetNumericalVariance() {
        FDistribution distribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        double result = distribution.getNumericalVariance();
        if (denominatorDegreesOfFreedom > 4) {
            final double denomDFMinusTwo = denominatorDegreesOfFreedom - 2;
            double expected = (2 * (denominatorDegreesOfFreedom * denominatorDegreesOfFreedom) *
                              (numeratorDegreesOfFreedom + denominatorDegreesOfFreedom - 2)) /
                             ((numeratorDegreesOfFreedom * (denomDFMinusTwo * denomDFMinusTwo) * (denominatorDegreesOfFreedom - 4)));
            assertEquals(result, expected, 0.0000001);
        } else {
            assertEquals(result, Double.NaN, 0.0);
        }
    }

    @Test(expected = ArithmeticException.class)
    public void testCumulativeProbabilityWithNegativeArgument() {
        FDistribution distribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        distribution.cumulativeProbability(-1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDensityWithNegativeArgument() {
        FDistribution distribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        distribution.density(-1.0);
    }
}