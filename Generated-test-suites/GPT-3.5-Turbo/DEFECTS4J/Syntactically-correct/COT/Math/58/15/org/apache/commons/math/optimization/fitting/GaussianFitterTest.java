
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math.optimization.fitting.GaussianFitter;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.exception.NullArgumentException;
import org.apache.commons.math.exception.NumberIsTooSmallException;
import org.apache.commons.math.exception.OutOfRangeException;
import org.apache.commons.math.exception.ZeroException;
import org.apache.commons.math.exception.util.LocalizedFormats;
import static org.junit.Assert.*;

public class GaussianFitterTest {
  
  private GaussianFitter fitter;

  @Before
  public void setup() {
    fitter = new GaussianFitter();
  }
  
  @Test
  public void testFitWithInitialGuess() {
    double[] initialGuess = {1.0, 2.0, 3.0};
    double[] parameters = fitter.fit(initialGuess);
    assertNotNull(parameters);
  }
  
  @Test
  public void testFitWithoutInitialGuess() {
    double[] parameters = fitter.fit();
    assertNotNull(parameters);
  }
  
  @Test(expected = NullArgumentException.class)
  public void testFitWithNullObservations() {
    fitter.fit(null);
  }
  
  @Test(expected = NumberIsTooSmallException.class)
  public void testFitWithLessThanThreeObservations() {
    WeightedObservedPoint[] observations = {
      new WeightedObservedPoint(1.0, 2.0),
      new WeightedObservedPoint(3.0, 4.0)
    };
    fitter.addObservedPoints(observations);
    fitter.fit();
  }
  
  @Test
  public void testParameterGuesser() {
    WeightedObservedPoint[] observations = {
      new WeightedObservedPoint(1.0, 2.0),
      new WeightedObservedPoint(3.0, 4.0),
      new WeightedObservedPoint(5.0, 6.0)
    };
    GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(observations);
    double[] parameters = guesser.guess();
    assertNotNull(parameters);
  }
  
  @Test(expected = OutOfRangeException.class)
  public void testInterpolateXAtYOutOfRange() {
    WeightedObservedPoint[] points = {
      new WeightedObservedPoint(1.0, 2.0),
      new WeightedObservedPoint(3.0, 4.0),
      new WeightedObservedPoint(5.0, 6.0)
    };
    GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(points);
    guesser.interpolateXAtY(points, 0, -1, -1);
  }
  
  @Test(expected = ZeroException.class)
  public void testInterpolateXAtYZeroStep() {
    WeightedObservedPoint[] points = {
      new WeightedObservedPoint(1.0, 2.0),
      new WeightedObservedPoint(3.0, 4.0),
      new WeightedObservedPoint(5.0, 6.0)
    };
    GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(points);
    guesser.interpolateXAtY(points, 0, 0, 2);
  }
  
  @Test
  public void testParameterGuesserBasicGuess() {
    WeightedObservedPoint[] points = {
      new WeightedObservedPoint(1.0, 2.0),
      new WeightedObservedPoint(3.0, 4.0),
      new WeightedObservedPoint(5.0, 6.0)
    };
    GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(points);
    double[] parameters = guesser.basicGuess(points);
    assertNotNull(parameters);
  }
}
