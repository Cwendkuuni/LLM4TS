import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.apache.commons.math3.stat.ranking.Ranking;
import org.apache.commons.math3.stat.ranking.NaturalRanking;
import org.apache.commons.math3.stat.ranking.TiesStrategy;
import org.apache.commons.math3.stat.ranking.NaNStrategy;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.junit.Test;
import static org.junit.Assert.*;

public class MannWhitneyUTestTest {

 private MannWhitneyUTest mannWhitneyUTest;
 private Ranking naturalRanking;

 @Before
 public void setup() {
 mannWhitneyUTest = new MannWhitneyUTest();
 naturalRanking = new NaturalRanking(NaNStrategy.FIXED, TiesStrategy.AVERAGE);
 }

 /**
 * Test method for {@link MannWhitneyUTest#mannWhitneyU(double[], double[])}.
 */
 @Test
 public void testMannWhitneyU() throws NoDataException, NullArgumentException {
 double[] x = {1.0, 2.0, 3.0, 3.0, 3.0};
 double[] y = {4.0, 4.0, 4.0, 4.0, 4.0};
 double mannWhitneyU = mannWhitneyUTest.mannWhitneyU(x, y);
 assertEquals("Mann-Whitney U statistic is not as expected.", 6.0, mannWhitneyU);
 }

 /**
 * Test method for {@link MannWhitneyUTest#mannWhitneyUTest(double[], double[])}.
 */
 @Test
 public void testMannWhitneyUTestWithEqualMean() throws NoDataException,
 NullArgumentException, ConvergenceException, MaxCountExceededException {
 double[] x = {1.5, 2.0, 2.5};
 double[] y = {1.5, 2.5, 2.5};
 double pValue = mannWhitneyUTest.mannWhitneyUTest(x, y);
 assertEquals("p-value is not as expected.", 1.0, pValue, 0.001);
 }

 /**
 * Test method for {@link MannWhitneyUTest#mannWhitneyUTest(double[], double[])}.
 */
 @Test
 public void testMannWhitneyUTestWithDifferentMean() throws NoDataException,
 NullArgumentException, ConvergenceException, MaxCountExceededException {
 double[] x = {1.0, 2.0, 3.0, 3.0, 3.0};
 double[] y = {4.5, 5.5, 6.5, 6.5, 6.5};
 double pValue = mannWhitneyUTest.mannWhitneyUTest(x, y);
 assertNotEquals("p-value should not be equal to zero.", 0.0, pValue, 0.001);
 }

 /**
 * Test method for {@link MannWhitneyUTest#mannWhitneyUTest(double[], double[])}.
 * Test for edge case with NaN values.
 */
 @Test(expected = NoDataException.class)
 public void testMannWhitneyUTestWithNaN() {
 double[] x = {1.0, Double.NaN, 3.0};
 double[] y = {4.0, 4.0, Double.NaN};
 mannWhitneyUTest.mannWhitneyUTest(x, y);
 }

 /**
 * Test method for {@link MannWhitneyUTest#mannWhitneyUTest(double[], double[])}.
 * Test for edge case with empty arrays.
 */
 @Test(expected = NoDataException.class)
 public void testMannWhitneyUTestWithEmptyArrays() {
 double[] x = {};
 double[] y = {};
 mannWhitneyUTest.mannWhitneyUTest(x, y);
 }

 /**
 * Test method for {@link MannWhitneyUTest#mannWhitneyUTest(double[], double[])}.
 * Test for edge case with two arrays of different sizes.
 */
 @Test(expected = IllegalArgumentException.class)
 public void testMannWhitneyUTestWithDifferentSizes() {
 double[] x = {1.0, 2.0, 3.0};
 double[] y = {1.0, 2.0, 3.0, 4.0};
 mannWhitneyUTest.mannWhitneyUTest(x, y);
 }

 /**
 * Test method for {@link MannWhitneyUTest#mannWhitneyUTest(NaNStrategy,
 * TiesStrategy)}.
 */
 @Test
 public void testMannWhitneyUTestWithCustomRanking() {
 mannWhitneyUTest = new MannWhitneyUTest(NaturalRanking.INSTANCE,
 TiesStrategy.MIN);
 double[] x = {1.0, 2.0, 3.0, 3.0, 3.0};
 double[] y = {4.0, 4.0, 4.0, 4.0, 4.0};
 double pValue = mannWhitneyUTest.mannWhitneyUTest(x, y);
 assertEquals("p-value is not as expected.", 6.0 / 15.0, pValue, 0.001);
 }
}