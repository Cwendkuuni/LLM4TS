import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.apache.commons.math3.stat.ranking.NaturalRanking;
import org.junit.Test;
import static org.junit.Assert.*;

public class MannWhitneyUTestTest {

 private final MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
 private final double[] sample1 = {1.0, 2.0, 3.0};
 private final double[] sample2 = {3.0, 2.0, 1.0};
 private final double[] sample1Null = null;
 private final double[] sample2Null = null;
 private final double[] sample1Empty = {};
 private final double[] sample2Empty = {};
 private final double[] sample1NaN = {Double.NaN, 2.0, 3.0};
 private final double[] sample2NaN = {2.0, Double.NaN, 3.0};

 @Test
 public void testMannWhitneyU_twoSamples() {
 double uStatistic = mannWhitneyUTest.mannWhitneyU(sample1, sample2);
 assertEquals(6.0, uStatistic, 0.001);
 }

 @Test(expected = NullArgumentException.class)
 public void testMannWhitneyU_nullArguments() {
 mannWhitneyUTest.mannWhitneyU(sample1Null, sample2);
 }

 @Test(expected = NoDataException.class)
 public void testMannWhitneyU_emptySamples() {
 mannWhitneyUTest.mannWhitneyU(sample1Empty, sample2Empty);
 }

 @Test(expected = ConvergenceException.class)
 public void testMannWhitneyU_convergenceException() {
 mannWhitneyUTest.mannWhitneyU(sample1, sample2); // Tests the convergence exception is thrown when necessary
 }

 @Test(expected = MaxCountExceededException.class)
 public void testMannWhitneyU_maxCountExceededException() {
 mannWhitneyUTest.mannWhitneyU(sample1, sample2); // Tests the max count exceeded exception is thrown when necessary
 }

 @Test
 public void testMannWhitneyUTest_twoSamples() {
 double pValue = mannWhitneyUTest.mannWhitneyUTest(sample1, sample2);
 assertEquals(0.05, pValue, 0.001);
 }

 @Test(expected = NullArgumentException.class)
 public void testMannWhitneyUTest_nullArguments() {
 mannWhitneyUTest.mannWhitneyUTest(sample1Null, sample2);
 }

 @Test(expected = NoDataException.class)
 public void testMannWhitneyUTest_emptySamples() {
 mannWhitneyUTest.mannWhitneyUTest(sample1Empty, sample2Empty);
 }

 @Test(expected = ConvergenceException.class)
 public void testMannWhitneyUTest_convergenceException() {
 mannWhitneyUTest.mannWhitneyUTest(sample1, sample2); // Tests the convergence exception is thrown when necessary
 }

 @Test(expected = MaxCountExceededException.class)
 public void testMannWhitneyUTest_maxCountExceededException() {
 mannWhitneyUTest.mannWhitneyUTest(sample1, sample2); // Tests the max count exceeded exception is thrown when necessary
 }

 @Test
 public void testMannWhitneyUTest_twoSamplesNaN() {
 NaturalRanking ranking = new NaturalRanking(new NaNStrategy.NaNmedian(), TiesStrategy.DENSE);
 mannWhitneyUTest = new MannWhitneyUTest(ranking, ranking);
 double pValue = mannWhitneyUTest.mannWhitneyUTest(sample1NaN, sample2NaN);
 assertEquals(0.05, pValue, 0.001);
 }
}