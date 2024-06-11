import static org.junit.Assert.*;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {

  @Test
  public void testIntegrate() {

    // create instance of EmbeddedRungeKuttaIntegrator with parameters
    EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("Test", true, new double[]{0.0, 1.0}, new double[][]{{0.0, 0.0}, {0.5, 0.5}}, new double[]{0.5, 0.5}, new RungeKuttaStepInterpolator(), 1.0, 2.0, new double[]{0.01}, new double[]{0.001});

    // create instance of ExpandableStatefulODE class
    ExpandableStatefulODE equations = new ExpandableStatefulODE();
    
    // set time of ExpandableStatefulODE object
    equations.setTime(0.0);

    // create instance of double array to be used for state
    double[] state = new double[]{1.0, 2.0, 3.0};

    // set state of ExpandableStatefulODE object
    equations.setPrimaryState(state);

    // create instance of double array to be used for derivative
    double[] derivative = new double[]{4.0, 5.0, 6.0};

    // create instance of double array to be used for absolute tolerance
    double[] absoluteTolerance = new double[]{0.001};

    // create instance of double array to be used for relative tolerance
    double[] relativeTolerance = new double[]{0.0001};

    // set derivatives of ExpandableStatefulODE object
    equations.setPrimaryDerivative(derivative);

    // set absolute tolerance of ExpandableStatefulODE object
    equations.setPrimaryAbsoluteTolerance(absoluteTolerance);

    // set relative tolerance of ExpandableStatefulODE object
    equations.setPrimaryRelativeTolerance(relativeTolerance);

    // create instance of double to be used for time
    double t = 1.0;

    // create instance of double array to be used for complete state
    double[] completeState = equations.getCompleteState();

    // set complete state of ExpandableStatefulODE object
    equations.setCompleteState(completeState);

    // call the integrate method of EmbeddedRungeKuttaIntegrator class
    try {
        integrator.integrate(equations, t);
    } catch (Exception e) {
        fail("An exception occurred");
    }

    // create instance of double array to be used for expected state
    double[] expectedState = new double[]{1.5, 3.0, 4.5};

    // assert that the computed state is equal to the expected state
    assertArrayEquals(expectedState, equations.getCompleteState(), 0.0);

  }

}