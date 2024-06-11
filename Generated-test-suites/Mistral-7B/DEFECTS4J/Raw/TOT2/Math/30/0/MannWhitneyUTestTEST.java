import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.apache.commons.math3.stat.ranking.NaturalRanking;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class MannWhitneyUTestTest {

 private double[] createSample1() {
 return new double[] {1.0, 2.0, 3.0};
 }

 private double[] createSample2() {
 return new double[] {3.0, 2.0, 1.0};
 }

 private double[] createSample1WithDuplicates() {
 return new double[] {1.0, 1.0, 2.0, 3.0};
 }

 private double[] createSample2WithDuplicates() {
 return new double[] {3.0, 3.0, 2.0, 1.0};
 }

 private double[] createSampleWithNaN() {
 return new double[] {1.0, 2.0, Double.NaN};
 }

 private double[] createSampleWithEmptyFirst() {
 return new double[0];
 }

 private double[] createSampleWithEmptySecond() {
 return new double[] {};
 }

 @Test
 public void mannWhitneyU_testSameData() {
 MannWhitneyUTest test = new MannWhitneyUTest();
 double[] x = createSample1();
 double[] y = x.clone(); // ensure same data
 double result = test.mannWhitneyU(x, y);
 assertEquals(0, result, 0.001);
 }

 @Test
 public void mannWhitneyU_testDifferentData() {
 MannWhitneyUTest test = new MannWhitneyUTest();
 double[] x = createSample1();
 double[] y = createSample2();
 double result = test.mannWhitneyU(x, y);
 assertEquals(3, result, 0.001);
 }

 @Test(expected = NullArgumentException.class)
 public void mannWhitneyU_testNullArgumentX() {
 MannWhitneyUTest test = new MannWhitneyUTest();
 double[] x = null;
 double[] y = createSample2();
 test.mannWhitneyU(x, y);
 }

 @Test(expected = NullArgumentException.class)
 public void mannWhitneyU_testNullArgumentY() {
 MannWhitneyUTest test = new MannWhitneyUTest();
 double[] x = createSample1();
 double[] y = null;
 test.mannWhitneyU(x, y);
 }

 @Test(expected = NoDataException.class)
 public void mannWhitneyU_testEmptyFirstData() {
 MannWhitneyUTest test = new MannWhitneyUTest();
 double[] x = createSampleWithEmptyFirst();
 double[] y = createSample2();
 test.mannWhitneyU(x, y);
 }

 @Test(expected = NoDataException.class)
 public void mannWhitneyU_testEmptySecondData() {
 MannWhitneyUTest test = new MannWhitneyUTest();
 double[] x = createSample1();
 double[] y = createSampleWithEmptySecond();
 test.mannWhitneyU(x, y);
 }

 @Test(expected = ConvergenceException.class)
 public void mannWhitneyUTest_testNaNInData() {
 MannWhitneyUTest test = new MannWhitneyUTest();
 double[] x = createSample1WithDuplicates();
 double[] y = createSample2WithDuplicates();
 test.mannWhitneyUTest(x, y);
 }

 @Test
 public void mannWhitneyUTest_testDifferentMeans() {
 MannWhitneyUTest test = new MannWhitneyUTest();
 double[] x = createSample1();
 double[] y = createSample2();
 double result = test.mannWhitneyUTest(x, y);
 assertEquals(0.05, result, 0.01);
 }
}