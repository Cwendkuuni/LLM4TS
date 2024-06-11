import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.apache.commons.math3.stat.ranking.Ranking;
import org.apache.commons.math3.stat.ranking.NaturalRanking;
import org.apache.commons.math3.stat.ranking.TiesStrategy;
import org.apache.commons.math3.stat.ranking.NaNStrategy;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class MannWhitneyUTestTest {

 private double[] createSample1() {
 return new double[] { 1.0, 2.0, 3.0 };
 }

 private double[] createSample2() {
 return new double[] { 3.0, 2.0, 1.0 };
 }

 private double[] createSample1AndSample2() {
 return new double[] { 1.0, 2.0, 3.0, 3.0, 2.0, 1.0 };
 }

 private double[] createSampleWithNaN() {
 return new double[] { 1.0, 2.0, Double.NaN, 3.0 };
 }

 private double[] createEmptySample() {
 return new double[0];
 }

 private double[] createNullSample() {
 return null;
 }

 @Test
 public void testMannWhitneyUTestWithNaturalRanking() {
 MannWhitneyUTest test = new MannWhitneyUTest();
 Ranking ranking = test.naturalRanking;
 double[] sample1 = createSample1();
 double[] sample2 = createSample2();
 double result = test.mannWhitneyU(sample1, sample2);
 double expected = 3.0;
 assertEquals(expected, result, 0.001);
 }

 @Test
 public void testMannWhitneyUTestWithCustomRanking() {
 MannWhitneyUTest test = new MannWhitneyUTest(NaturalRanking.MIN, TiesStrategy.DENSE_RANK);
 Ranking ranking = test.naturalRanking;
 double[] sample1 = createSample1();
 double[] sample2 = createSample2();
 double result = test.mannWhitneyU(sample1, sample2);
 double expected = 3.0;
 assertEquals(expected, result, 0.001);
 }

 @Test
 public void testMannWhitneyUTestWithConcatenatedSamples() {
 MannWhitneyUTest test = new MannWhitneyUTest();
 double[] concatenatedSamples = test.concatenateSamples(createSample1(), createSample2());
 double result = test.mannWhitneyU(concatenatedSamples);
 double expected = 3.0;
 assertEquals(expected, result, 0.001);
 }

 @Test(expected = NullArgumentException.class)
 public void testMannWhitneyUTestWithNullArguments() {
 MannWhitneyUTest test = new MannWhitneyUTest();
 assertThrows(NullArgumentException.class, () -> test.mannWhitneyU(null, createSample2()));
 assertThrows(NullArgumentException.class, () -> test.mannWhitneyU(createSample1(), null));
 }

 @Test(expected = NoDataException.class)
 public void testMannWhitneyUTestWithEmptySamples() {
 MannWhitneyUTest test = new MannWhitneyUTest();
 assertThrows(NoDataException.class, () -> test.mannWhitneyU(createEmptySample(), createEmptySample()));
 }

 @Test(expected = ConvergenceException.class)
 public void testMannWhitneyUTestWithConvergenceException() {
 MannWhitneyUTest test = new MannWhitneyUTest();
 double[] sample1 = createSample1AndSample2();
 double[] ranks = new double[sample1.length];
 for (int i = 0; i < sample1.length; i++) {
 ranks[i] = i + 1.0;
 }
 test.naturalRanking.rank(sample1);
 double result = test.mannWhitneyU(sample1, createSample2());
 }

 @Test(expected = MaxCountExceededException.class)
 public void testMannWhitneyUTestWithMaxCountExceededException() {
 MannWhitneyUTest test = new MannWhitneyUTest();
 double[] sample1 = createSample1AndSample2();
 double[] ranks = new double[sample1.length * 1000];
 for (int i = 0; i < sample1.length; i++) {
 ranks[i] = i + 1.0;
 }
 assertThrows(MaxCountExceededException.class, () -> test.mannWhitneyU(sample1, createSample2()));
 }

 @Test
 public void testMannWhitneyUTestWithNaNInSample() {
 MannWhitneyUTest test = new MannWhitneyUTest(NaNStrategy.REMOVED, TiesStrategy.MIN);
 double[] sample1 = createSample1();
 double[] sample2 = createSampleWithNaN();
 double result = test.mannWhitneyU(sample1, sample2);
 double expected = 5.0; // Assuming 'sample1.length + sample2.length'
 assertEquals(expected, result, 0.001);
 }
}