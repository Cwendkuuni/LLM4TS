import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.stat.ranking.NaNStrategy;
import org.apache.commons.math3.stat.ranking.NaturalRanking;
import org.apache.commons.math3.stat.ranking.TiesStrategy;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class MannWhitneyUTestTest {

 private MannWhitneyUTest mannWhitneyUTest;

 @Test
 public void testMannWhitneyUTest() {
 mannWhitneyUTest = new MannWhitneyUTest();
 }

 @Test
 public void testMannWhitneyU() throws NoDataException, NullArgumentException {
 double[] x = {1.0, 2.0, 3.0};
 double[] y = {2.0, 3.0};
 double expectedU = 1.5;
 double mannWhitneyUResult = mannWhitneyUTest.mannWhitneyU(x, y);
 assertEquals("mannWhitneyU test failed", expectedU, mannWhitneyUResult, 0.001);
 }

 @Test
 public void testMannWhitneyUTest() throws NoDataException, NullArgumentException, ConvergenceException, MaxCountExceededException {
 double[] x = {1.0, 2.0, 3.0};
 double[] y = {2.0, 3.0, 2.0}; // ties
 double expectedPValue = 0.3;
 double mannWhitneyUTestResult = mannWhitneyUTest.mannWhitneyUTest(x, y);
 assertEquals("mannWhitneyUTest test failed", expectedPValue, mannWhitneyUTestResult, 0.001);
 }

 @Test
 public void testMannWhitneyUTestWithCustomStrategies() throws NoDataException, NullArgumentException, ConvergenceException, MaxCountExceededException {
 MannWhitneyUTest mannWhitneyUTestWithCustomStrategies = new MannWhitneyUTest(NaNStrategy.REMOVE, TiesStrategy.MIN);
 double[] x = {1.0, 2.0, 3.0};
 double[] y = {2.0, 3.0, 2.0}; // ties
 double expectedPValue = 0.25;
 double mannWhitneyUTestResult = mannWhitneyUTestWithCustomStrategies.mannWhitneyUTest(x, y);
 assertEquals("mannWhitneyUTest test failed", expectedPValue, mannWhitneyUTestResult, 0.001);
 }
}