import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class EmbeddedRungeKuttaIntegratorTest {

    private EmbeddedRungeKuttaIntegrator integrator;
    private FirstOrderDifferentialEquations equations;
    private double[] initialValues = {1.0, 1.0};
    private double[] solution = {2.718281828459045, 2.718281828459045};
    private double stepSize = 0.1;
    private double endTime = 2.0;
    private double tolerance = 1e-8;
    private double absoluteTolerance = 1e-8;
    private double relativeTolerance = 1e-8;

    @Before
    public void setUp() {
        equations = (FirstOrderDifferentialEquations) () -> new double[]{0.0, 1.0};
        integrator = new EmbeddedRungeKuttaIntegrator("TestMethod", false,
                new double[]{0.3, 0.6, 1.0},
                new double[][]{{0.0, 0.3466919257824246, 0.6233193817213615},
                        {0.0, 0.23563170849857916, 0.4453544112746367, 0.6233193817213615}},
                new double[]{0.0, 0.23563170849857916, 0.4453544112746367, 0.6233193817213615},
                new RungeKuttaStepInterpolator(), 0.01, 1.0,
                absoluteTolerance, relativeTolerance) {
            @Override
            public int getOrder() {
                return 3;
            }

            @Override
            public void setSafety(double safety) {
                // do nothing
            }

            @Override
            public void setMinReduction(double minReduction) {
                // do nothing
            }

            @Override
            public void setMaxGrowth(double maxGrowth) {
                // do nothing
            }

            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                double error = 0.0;
                for (int i = 0; i < y0.length; i++) {
                    error += Math.pow(Math.abs(y1[i] - y0[i]), 2);
                }
                return Math.sqrt(error);
            }
        };
    }

    @Test
    public void testIntegration() {
        integrator.integrate(equations, 0.0, initialValues, endTime, new double[initialValues.length]);
        assertEquals(solution[0], integrator.getY()[0], tolerance);
        assertEquals(solution[1], integrator.getY()[1], tolerance);
    }

    @Test
    public void testStepHandlers() {
        StepHandler stepHandler = new StepHandler() {
            private int count = 0;

            @Override
            public void handleStep(StepHandler event, int type) {
                count++;
            }
        };
        integrator.addStepHandler(stepHandler);
        integrator.integrate(equations, 0.0, initialValues, endTime, new double[initialValues.length]);
        assertEquals(2, count);
    }

    @Test
    public void testEvents() {
        double[] eventTimes = {1.5, 1.9};
        integrator.addEndTimeChecker(endTime, eventTimes, new Event[0]);
        integrator.integrate(equations, 0.0, initialValues, endTime, new double[initialValues.length]);
        assertEquals(2, integrator.getEventCount());
    }

    @Test(expected = DerivativeException.class)
    public void testDerivativeException() {
        FirstOrderDifferentialEquations equationsWithException = (FirstOrderDifferentialEquations) () -> {
            throw new DerivativeException("Test Derivative Exception");
        };
        integrator.integrate(equationsWithException, 0.0, initialValues, endTime, new double[initialValues.length]);
    }

    @Test(expected = IntegratorException.class)
    public void testIntegratorException() {
        FirstOrderDifferentialEquations equationsWithException = (FirstOrderDifferentialEquations) () -> {
            throw new IntegratorException("Test Integrator Exception");
        };
        integrator.integrate(equationsWithException, 0.0, initialValues, endTime, new double[initialValues.length]);
    }

}