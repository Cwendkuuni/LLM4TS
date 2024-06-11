import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.distribution.PoissonDistributionImpl;
import org.apache.commons.math3.special.Gamma;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class PoissonDistributionImplTest {

private PoissonDistributionImpl poissonDistribution;

@Test
public void testConstructorWithPositiveMean() {
poissonDistribution = new PoissonDistributionImpl(2.5);
}

@Test(expected = MathRuntimeException.class)
public void testConstructorWithNegativeMean() {
poissonDistribution = new PoissonDistributionImpl(-1.0);
}

@Test
public void testProbabilityMethodForZero() {
double probability = poissonDistribution.probability(0);
assertEquals(Math.exp(-mean), probability, 1e-6);
}

@Test
public void testProbabilityMethodForValidInt() {
int x = 5;
double probability = poissonDistribution.probability(x);
assertEquals(probability, poissonDistribution.normalApproximateProbability(x), 1e-6);
}

@Test
public void testProbabilityMethodForNegativeInt() {
assertThrows(IllegalArgumentException.class, () -> poissonDistribution.probability(-1));
}

@Test
public void testProbabilityMethodForMaxIntValue() {
int x = Integer.MAX_VALUE;
double probability = poissonDistribution.probability(x);
assertEquals(0.0, probability, 1e-6);
}

@Test(expected = MathException.class)
public void testCumulativeProbabilityMethodForNegativeInt() {
poissonDistribution.cumulativeProbability(-1);
}

@Test
public void testCumulativeProbabilityMethodForZero() {
double cdf = poissonDistribution.cumulativeProbability(0);
assertEquals(0.0, cdf, 1e-6);
}

@Test
public void testCumulativeProbabilityMethodForValidInt() throws MathException {
double cdf = poissonDistribution.cumulativeProbability(5);
assertEquals(cdf, Gamma.regularizedGammaP(5.5, mean), 1e-6);
}

@Test
public void testCumulativeProbabilityMethodForMaxIntValue() throws MathException {
double cdf = poissonDistribution.cumulativeProbability(Integer.MAX_VALUE);
assertEquals(1.0, cdf, 1e-6);
}

@Test
public void testNormalApproximateProbabilityMethodForNegativeInt() {
assertThrows(IllegalArgumentException.class, () -> poissonDistribution.normalApproximateProbability(-1));
}

@Test
public void testNormalApproximateProbabilityMethodForZero() {
double probability = poissonDistribution.normalApproximateProbability(0);
assertEquals(FastMath.exp(-mean), probability, 1e-6);
}

@Test
public void testNormalApproximateProbabilityMethodForValidInt() throws MathException {
double probability = poissonDistribution.normalApproximateProbability(5);
assertEquals(probability, Gamma.regularizedGammaQ(5.5, mean), 1e-6);
}

@Test
public void testSampleMethodForNegativeMean() throws MathException {
assertThrows(MathRuntimeException.class, () -> poissonDistribution.sample());
}

@Test
public void testSampleMethodForMeanZero() throws MathException {
int sample = poissonDistribution.sample();
assertEquals(0, sample);
}

@Test
public void testSampleMethodForMeanGreaterThanZero() throws MathException {
int sample = poissonDistribution.sample();
assertEquals(sample, poissonDistribution.sample(), 1);
}

@Test
public void testInverseCumulativeProbabilityMethodForNegativeProbability() {
assertThrows(IllegalArgumentException.class, () -> poissonDistribution.inverseCumulativeProbability(-1.0));
}

@Test
public void testInverseCumulativeProbabilityMethodForProbabilityGreaterThanOne() {
assertThrows(IllegalArgumentException.class, () -> poissonDistribution.inverseCumulativeProbability(1.1));
}

@Test
public void testInverseCumulativeProbabilityMethodForProbabilityZero() {
int result = poissonDistribution.inverseCumulativeProbability(0.0);
assertEquals(0, result);
}

@Test
public void testInverseCumulativeProbabilityMethodForProbabilityGreaterThanZero() throws MathException {
int result = poissonDistribution.inverseCumulativeProbability(0.5);
assertEquals(result, poissonDistribution.sample(), 1);
}

}