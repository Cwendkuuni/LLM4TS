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
 distribution = new FDistributionImpl(1.0, 2.0);
 }

 /**
 * Test constructor
 */
 @Test
 public void testConstructor() {
 assertEquals(1.0, distribution.getNumeratorDegreesOfFreedom(), 0.001);
 assertEquals(2.0, distribution.getDenominatorDegreesOfFreedom(), 0.001);
 }

 /**
 * Test setNumeratorDegreesOfFreedom method
 */
 @Test
 public void testSetNumeratorDegreesOfFreedom() {
 distribution.setNumeratorDegreesOfFreedom(5.0);
 assertEquals(5.0, distribution.getNumeratorDegreesOfFreedom(), 0.001);
 }

 /**
 * Test setDenominatorDegreesOfFreedom method
 */
 @Test
 public void testSetDenominatorDegreesOfFreedom() {
 distribution.setDenominatorDegreesOfFreedom(10.0);
 assertEquals(10.0, distribution.getDenominatorDegreesOfFreedom(), 0.001);
 }

 /**
 * Test cumulativeProbability method with valid arguments
 */
 @Test
 public void testCumulativeProbabilityValidArguments() throws MathException {
 double x = 1.5;
 double expectedResult = Beta.regularizedBeta((1.0 * x) / (2.0 + 1.0 * x),
 0.5, 0.5);
 double actualResult = distribution.cumulativeProbability(x);
 assertEquals(expectedResult, actualResult, 0.001);
 }

 /**
 * Test cumulativeProbability method with negative argument
 */
 @Test (expected = MathException.class)
 public void testCumulativeProbabilityNegativeArgument() throws MathException {
 double x = -1.0;
 distribution.cumulativeProbability(x);
 }

 /**
 * Test inverseCumulativeProbability method with valid arguments
 */
 @Test
 public void testInverseCumulativeProbabilityValidArguments() throws MathException {
 double p = 0.5;
 double expectedResult = (2.0 / (2.0 - 1.0)) * Beta.incompleteBeta(0.5, 0.5, p);
 double actualResult = distribution.inverseCumulativeProbability(p);
 assertEquals(expectedResult, actualResult, 0.001);
 }

 /**
 * Test inverseCumulativeProbability method with invalid arguments
 */
 @Test (expected = MathException.class)
 public void testInverseCumulativeProbabilityInvalidArguments() throws MathException {
 double p = 1.5;
 distribution.inverseCumulativeProbability(p);
 }

 /**
 * Test inverseCumulativeProbability method with p = 0
 */
 @Test
 public void testInverseCumulativeProbabilityPZero() throws MathException {
 double expectedResult = 0.0;
 double actualResult = distribution.inverseCumulativeProbability(0.0);
 assertEquals(expectedResult, actualResult, 0.001);
 }

 /**
 * Test inverseCumulativeProbability method with p = 1
 */
 @Test
 public void testInverseCumulativeProbabilityPOne() throws MathException {
 double expectedResult = Double.POSITIVE_INFINITY;
 double actualResult = distribution.inverseCumulativeProbability(1.0);
 assertEquals(expectedResult, actualResult);
 }

}