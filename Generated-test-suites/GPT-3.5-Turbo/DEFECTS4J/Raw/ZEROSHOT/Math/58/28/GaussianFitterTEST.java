import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class GaussianFitterTest {

  private GaussianFitter fitter;

  @Before
  public void setup() {
    fitter = new GaussianFitter(new DifferentiableMultivariateVectorialOptimizer());
  }

  @Test
  public void testFitWithInitialGuess() {
    double[] initialGuess = {1.0, 2.0, 3.0};
    double[] result = fitter.fit(initialGuess);
    assertNotNull(result);
    assertEquals(3, result.length);
    // Add more assertions as needed
  }

  @Test
  public void testFitWithoutInitialGuess() {
    double[] result = fitter.fit();
    assertNotNull(result);
    assertEquals(3, result.length);
    // Add more assertions as needed
  }

  @Test
  public void testParameterGuesser() {
    WeightedObservedPoint[] observations = {
      new WeightedObservedPoint(1.0, 4.0, 531026.0),
      new WeightedObservedPoint(2.0, 5.0, 984167.0),
      new WeightedObservedPoint(3.0, 6.0, 1887233.0)
    };
    GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(observations);
    double[] result = guesser.guess();
    assertNotNull(result);
    assertEquals(3, result.length);
    // Add more assertions as needed
  }

}