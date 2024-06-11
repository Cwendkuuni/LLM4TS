import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.FDistributionImpl;
import org.apache.commons.math3.special.Beta;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FDistributionImplTest {

 private FDistributionImpl distribution;

 @Before
 public void setUp() {
 distribution = new FDistributionImpl(2.0, 3.0);
 }

 /**
 * Test constructor
 */
 @Test
 public void testConstructor() {
 assertEquals(2.0, distribution.getNumeratorDegreesOfFreedom(), 0.0);
 assertEquals(3.0, distribution.getDenominatorDegreesOfFreedom(), 0.0);
 }

 /**
 * Test getNumeratorDegreesOfFreedom()
 */
 @Test
 public void testGetNumeratorDegreesOfFreedom() {
 assertEquals(2.0, distribution.getNumeratorDegreesOfFreedom(), 0.0);
 }

 /**
 * Test setNumeratorDegreesOfFreedom()
 */
 @Test
 public void testSetNumeratorDegreesOfFreedom() {
 distribution.setNumeratorDegreesOfFreedom(4.0);
 assertEquals(4.0, distribution.getNumeratorDegreesOfFreedom(), 0.0);
 }

 /**
 * Test getDenominatorDegreesOfFreedom()
 */
 @Test
 public void testGetDenominatorDegreesOfFreedom() {
 assertEquals(3.0, distribution.getDenominatorDegreesOfFreedom(), 0.0);
 }

 /**
 * Test setDenominatorDegreesOfFreedom()
 */
 @Test
 public void testSetDenominatorDegreesOfFreedom() {
 distribution.setDenominatorDegreesOfFreedom(5.0);
 assertEquals(5.0, distribution.getDenominatorDegreesOfFreedom(), 0.0);
 }

 /**
 * Test cumulativeProbability() for valid input
 */
 @Test
 public void testCumulativeProbabilityValidInput() throws MathException {
 double x = 1.5;
 double p = distribution.cumulativeProbability(x);
 assertTrue(p >= 0.0 && p <= 1.0);
 }

 /**
 * Test cumulativeProbability() for negative input
 */
 @Test(expected = MathException.class)
 public void testCumulativeProbabilityNegativeInput() throws MathException {
 distribution.cumulativeProbability(-1.0);
 }

 /**
 * Test inverseCumulativeProbability() for valid input
 */
 @Test
 public void testInverseCumulativeProbabilityValidInput() throws MathException {
 double p = 0.75;
 double x = distribution.inverseCumulativeProbability(p);
 assertTrue(x > 0.0);
 }

 /**
 * Test inverseCumulativeProbability() for p=0
 */
 @Test
 public void testInverseCumulativeProbabilityPZero() throws MathException {
 double x = distribution.inverseCumulativeProbability(0.0);
 assertEquals(0.0, x, 0.0);
 }

 /**
 * Test inverseCumulativeProbability() for p=1
 */
 @Test
 public void testInverseCumulativeProbabilityPOne() throws MathException {
 double x = distribution.inverseCumulativeProbability(1.0);
 assertTrue(Double.isInfinite(x));
 }

 /**
 * Test inverseCumulativeProbability() for invalid input (p<0)
 */
 @Test(expected = MathException.class)
 public void testInverseCumulativeProbabilityInvalidInput() throws MathException {
 distribution.inverseCumulativeProbability(-1.0);
 }
}