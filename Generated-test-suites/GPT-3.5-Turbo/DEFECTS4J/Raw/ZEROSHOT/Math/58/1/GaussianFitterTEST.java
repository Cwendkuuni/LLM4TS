import static org.junit.Assert.*;
import org.junit.Test;

public class GaussianFitterTest {

  @Test
  public void testFitWithInitialGuess() {
    GaussianFitter fitter = new GaussianFitter();
    double[] initialGuess = {1.0, 0.0, 1.0};
    double[] expected = {1.0, 0.0, 1.0};

    double[] result = fitter.fit(initialGuess);

    assertArrayEquals(expected, result, 0.0001);
  }

  @Test
  public void testFitWithoutInitialGuess() {
    GaussianFitter fitter = new GaussianFitter();
    double[] expected = {0.39614, 4.05052, 0.14958};

    double[] result = fitter.fit();

    assertArrayEquals(expected, result, 0.0001);
  }

  @Test
  public void testParameterGuesser() {
    WeightedObservedPoint[] observations = {
        new WeightedObservedPoint(1.0, 4.0254623, 531026.0),
        new WeightedObservedPoint(1.0, 4.03128248, 984167.0),
        new WeightedObservedPoint(1.0, 4.03839603, 1887233.0),
        new WeightedObservedPoint(1.0, 4.04421621, 2687152.0),
        new WeightedObservedPoint(1.0, 4.05132976, 3461228.0),
        new WeightedObservedPoint(1.0, 4.05326982, 3580526.0),
        new WeightedObservedPoint(1.0, 4.05779662, 3439750.0),
        new WeightedObservedPoint(1.0, 4.0636168, 2877648.0),
        new WeightedObservedPoint(1.0, 4.06943698, 2175960.0),
        new WeightedObservedPoint(1.0, 4.07525716, 1447024.0),
        new WeightedObservedPoint(1.0, 4.08237071, 717104.0),
        new WeightedObservedPoint(1.0, 4.08366408, 620014.0)
    };
    GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(observations);
    double[] expected = {3580526.0, 4.05326982, 0.14958};

    double[] result = guesser.guess();

    assertArrayEquals(expected, result, 0.0001);
  }
}