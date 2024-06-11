import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

 private NormalDistributionImpl distribution;

 @Before
 public void setUp() {
 distribution = new NormalDistributionImpl(0.0, 1.0);
 }

 @Test
 public void testDefaultConstructor() {
 NormalDistribution dist = new NormalDistributionImpl();
 assertEquals(0.0, dist.getMean(), 0.0);
 assertEquals(1.0, dist.getStandardDeviation(), 0.0);
 }

 @Test
 public void testParameterizedConstructor() {
 NormalDistribution dist = new NormalDistributionImpl(1.5, 2.5);
 assertEquals(1.5, dist.getMean(), 0.0);
 assertEquals(2.5, dist.getStandardDeviation(), 0.0);
 }

 @Test
 public void testGetMean() {
 distribution.setMean(3.14);
 assertEquals(3.14, distribution.getMean(), 0.0);
 }

 @Test
 public void testSetMean() {
 distribution.setMean(0.0);
 assertEquals(0.0, distribution.getMean(), 0.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidMean() {
 distribution.setMean(-1.0);
 }

 @Test
 public void testGetStandardDeviation() {
 distribution.setStandardDeviation(2.718);
 assertEquals(2.718, distribution.getStandardDeviation(), 0.0);
 }

 @Test
 public void testSetStandardDeviation() {
 distribution.setStandardDeviation(1.0);
 assertEquals(1.0, distribution.getStandardDeviation(), 0.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidStandardDeviation() {
 distribution.setStandardDeviation(-1.0);
 }

 @Test
 public void testCumulativeProbability() throws MathException {
 double x;
 for (x = -10.0; x <= 10.0; x += 0.1) {
 double p = distribution.cumulativeProbability(x);
 System.out.printf("%.2f: %.4f%n", x, p);
 }
 }

 @Test
 public void testInverseCumulativeProbability() throws MathException {
 for (double p = 0.0; p <= 1.0; p += 0.01) {
 double x = distribution.inverseCumulativeProbability(p);
 System.out.printf("%.2f: %.4f%n", p, x);
 }
 }
}