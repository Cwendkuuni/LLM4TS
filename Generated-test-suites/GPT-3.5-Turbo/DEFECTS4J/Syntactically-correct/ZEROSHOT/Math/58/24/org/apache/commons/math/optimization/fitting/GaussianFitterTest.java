import static org.junit.Assert.*;
import org.junit.Test;

public class GaussianFitterTest {

  @Test
  public void testFit_doubleArray() {
    double[] initialGuess = {1.0, 2.0, 3.0};
    GaussianFitter fitter = new GaussianFitter(new DifferentiableMultivariateVectorialOptimizer());
    double[] result = fitter.fit(initialGuess);
    assertNotNull(result);
  }

  @Test
  public void testFit() {
    GaussianFitter fitter = new GaussianFitter(new DifferentiableMultivariateVectorialOptimizer());
    double[] result = fitter.fit();
    assertNotNull(result);
  }

  @Test
  public void testParameterGuesser() {
    WeightedObservedPoint[] observations = {
      new WeightedObservedPoint(1.0, 2.0, 3.0),
      new WeightedObservedPoint(4.0, 5.0, 6.0),
      new WeightedObservedPoint(7.0, 8.0, 9.0)
    };
    GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(observations);
    double[] result = guesser.guess();
    assertNotNull(result);
  }

}