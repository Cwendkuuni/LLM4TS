import org.apache.commons.math3.ode.AbstractIntegrator;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.StepHandler;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.sampling.AbstractStepInterpolator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class AbstractIntegratorTest {

    private AbstractIntegrator integrator;
    private FirstOrderDifferentialEquations equations;
    private double[] initialState;
    private double targetTime;
    private double[] expectedResult;

    @Before
    public void setUp() {
        integrator = new AbstractIntegrator() {};
        equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] * 2;
            }
        };
        initialState = new double[]{1.0};
        targetTime = 10.0;
        expectedResult = new double[]{10.0};
        integrator.initIntegration(0.0, initialState, targetTime);
        integrator.setEquations(equations);
    }

    @Test
    public void testIntegration() throws Exception {
        double[] result = new double[equations.getDimension()];
        integrator.integrate(equations, targetTime);
        assertEquals(expectedResult[0], integrator.getCurrentStepStart(), 1e-10);
        System.arraycopy(integrator.getState(), 0, result, 0, equations.getDimension());
        assertEquals(expectedResult[0], result[0], 1e-10);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testIntegrationWithDimensionMismatch() throws Exception {
        FirstOrderDifferentialEquations equationsWithDimensionMismatch = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] * 2;
            }
        };
        integrator.initIntegration(0.0, initialState, targetTime);
        integrator.setEquations(equationsWithDimensionMismatch);
        integrator.integrate(equationsWithDimensionMismatch, targetTime);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testIntegrationWithSmallStepSize() throws Exception {
        double targetTimeWithSmallStepSize = 0.001;
        integrator.initIntegration(0.0, initialState, targetTimeWithSmallStepSize);
        integrator.setEquations(equations);
        integrator.integrate(equations, targetTimeWithSmallStepSize);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testIntegrationWithMaxEvaluationsReached() throws Exception {
        integrator.setMaxEvaluations(1);
        integrator.integrate(equations, targetTime);
    }

    @Test(expected = NoBracketingException.class)
    public void testIntegrationWithNoBracketing() throws Exception {
        StepHandler stepHandler = new StepHandler() {
            @Override
            public void handleStep(AbstractStepInterpolator interpolator, boolean isLastStep) {}

            @Override
            public void init(double t, double[] y, double tTarget) {}
        };
        integrator.addStepHandler(stepHandler);
        integrator.initIntegration(0.0, initialState, targetTime);
        integrator.setEquations(equations);
        integrator.integrate(equations, targetTime);
        integrator.addStepHandler(new StepHandler() {
            @Override
            public void handleStep(AbstractStepInterpolator interpolator, boolean isLastStep) {
                throw new NoBracketingException("No bracketing.");
            }

            @Override
            public void init(double t, double[] y, double tTarget) {}
        });
        integrator.integrate(equations, targetTime);
    }

    @Test
    public void testEventHandling() throws Exception {
        EventHandler eventHandler = new EventHandler() {
            @Override
            public void init(double t, double[] y, double tTarget) {}

            @Override
            public void stepAccepted(double t, double[] y) {}

            @Override
            public boolean evaluateStep(AbstractStepInterpolator interpolator) {
                return true;
            }

            @Override
            public void reinitializeBegin(AbstractStepInterpolator interpolator) {}

            @Override
            public void reset(double t, double[] y) {
                y[0] = 0.0;
            }

            @Override
            public void stepAccepted(double t, double[] y) {
                throw new RuntimeException("Should not be called.");
            }
        };
        integrator.addEventHandler(eventHandler, 1.0, 1.0e-10, 10, new BracketingNthOrderBrentSolver(1.0e-10, 10));
        integrator.initIntegration(0.0, initialState, targetTime);
        integrator.setEquations(equations);
        integrator.integrate(equations, targetTime);
        assertTrue(integrator.resetOccurred);
    }
}