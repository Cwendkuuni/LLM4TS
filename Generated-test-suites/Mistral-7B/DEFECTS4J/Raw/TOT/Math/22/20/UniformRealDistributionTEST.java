import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class UniformRealDistributionTest {

private RandomGenerator rng;
private UniformRealDistribution dist;

@Before
public void setUp() throws NumberIsTooLargeException {
rng = new Well19937c();
dist = new UniformRealDistribution(rng, 0, 1);
}

@Test
public void testDefaultConstructor() {
UniformRealDistribution defDist = new UniformRealDistribution();
assertEquals(0.0, defDist.getNumericalMean(), 0.001);
assertEquals(0.0, defDist.getNumericalVariance(), 0.001);
}

@Test
public void testConstructorWithBounds() throws NumberIsTooLargeException {
UniformRealDistribution customDist = new UniformRealDistribution(rng, 2, 5);
assertEquals(3.5, customDist.getNumericalMean(), 0.001);
assertEquals(2.1875, customDist.getNumericalVariance(), 0.001);
}

@Test(expected = NumberIsTooLargeException.class)
public void testConstructorWithInvalidBounds() throws NumberIsTooLargeException {
UniformRealDistribution invalidDist = new UniformRealDistribution(rng, 5, 2);
fail("Should not be able to create distribution with invalid bounds");
}

@Test
public void testSample() {
double sample = dist.sample();
assertTrue(sample >= 0.0 && sample < 1.0);
}

@Test
public void testCumulativeProbability() {
for (double x = 0.0; x <= 1.0; x += 0.01) {
double cp = dist.cumulativeProbability(x);
assertTrue(cp >= 0.0 && cp <= 1.0);
assertTrue(cp <= dist.cumulativeProbability(x + 0.001));
}
}

@Test
public void testDensity() {
for (double x = 0.0; x <= 1.0; x += 0.01) {
double density = dist.density(x);
assertTrue(density >= 0.0 && density <= 1.0 / (1.0 - 0.0));
}
}

@Test
public void testNegativeSample() {
try {
dist.sample();
fail("Should not be able to sample a value below lower bound");
} catch (IllegalArgumentException e) {
// Expected
}
}

@Test
public void testSampleOutsideBounds() {
try {
dist.sample();
dist.sample();
dist.sample();
double sample = dist.sample();
assertTrue(sample >= 0.0 && sample < 1.0);
} catch (IllegalStateException e) {
fail("Should be able to sample multiple values within bounds");
}
}
}