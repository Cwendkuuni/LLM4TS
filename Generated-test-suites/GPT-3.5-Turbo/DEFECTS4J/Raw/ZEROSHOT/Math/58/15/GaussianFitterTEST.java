import org.junit.Test;
import static org.junit.Assert.*;

public class GaussianFitterTest {

  @Test
  public void testFit() {
    GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
    double[] initialGuess = {1.0, 0.0, 1.0};
    double[] parameters = fitter.fit(initialGuess);
    double[] expectedParameters = {1.0, 0.0, 1.0};
    assertArrayEquals(expectedParameters, parameters, 0.001);
  }
  
  @Test
  public void testFit_noInitialGuess() {
    GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
    double[] parameters = fitter.fit();
    double[] expectedParameters = {1.0, 0.0, 1.0};
    assertArrayEquals(expectedParameters, parameters, 0.001);
  }

  @Test
  public void testParameterGuesser() {
    WeightedObservedPoint[] observations = {new WeightedObservedPoint(1.0, 0.0, 0.0),
                                            new WeightedObservedPoint(2.0, 1.0, 1.0),
                                            new WeightedObservedPoint(3.0, 1.0, 2.0)};
    GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(observations);
    double[] parameters = guesser.guess();
    double[] expectedParameters = {1.0, 2.0, 1.0};
    assertArrayEquals(expectedParameters, parameters, 0.001);
  }
}