import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaIntegrator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.ode.sampling.TrapezoidStepInterpolator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the {@link RungeKuttaIntegrator} class.
 */
public class RungeKuttaIntegratorTest {

    private static final double EPSILON = 1e-12;

    private RungeKuttaIntegrator rk4;
    private FirstOrderDifferentialEquations equations;

    @Before
    public void setUp() {
        // create a simple differential equation
        equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public double[] derivatives(double t, double[] y) {
                return new double[] { y[0] + y[0] * 0.1 };
            }
        };

        // create an integrator
        rk4 = new RungeKuttaIntegrator("RK4",
                new double[] { 0.25, 0.25, 0.25, 0.25 },
                new double[][] { { 0.25, 0 }, { 0.25, 0.25 }, { 0.25, 0.25 }, { 0.25, 0.25 } },
                new double[] { 0.25, 0.25, 0.25, 0.25 },
                new RungeKuttaStepInterpolator(), 0.01) {

            @Override
            protected void computeDerivatives(double t, double[] y, double[] dy) {
                equations.computeDerivatives(t, y, dy);
            }
        };
    }

    @Test
    public void testSimpleIntegration() {
        // integrate a simple equation from t=0 to t=1
        double[] result = rk4.integrate(equations, 0.0, new double[] { 1.0 }, 1.0, new double[1]);
        Assert.assertArrayEquals(new double[] { 2.718281828}, result, EPSILON);

        // integrate the same equation backward from t=1 to t=0
        result = rk4.integrate(equations, 1.0, new double[] { 2.718281828 }, 0.0, new double[1]);
        Assert.assertArrayEquals(new double[] { 1.0 }, result, EPSILON);
    }

    @Test(expected = DerivativeException.class)
    public void testDerivativeException() {
        // create a differential equation with a singular Jacobian
        FirstOrderDifferentialEquations equationsSingular = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public double[] derivatives(double t, double[] y) {
                double x = y[0];
                double yp = y[1];
                double detJacobian = x * yp - yp * x;
                if (detJacobian == 0.0) {
                    throw new SingularMatrixException("Jacobian is singular");
                }
                return new double[] { yp, x };
            }
        };

        // try to integrate the singular equation
        rk4.integrate(equationsSingular, 0.0, new double[] { 1.0, 1.0 }, 1.0, new double[2]);
    }

    @Test(expected = IntegratorException.class)
    public void testIntegratorException() {
        // create a differential equation with a maximum number of iterations
        FirstOrderDifferentialEquations equationsMaxIterations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public double[] derivatives(double t, double[] y) {
                // this equation has no solution,
                // so we need to set the maximum number of iterations to 0
                throw new MaxIterationsExceededException(0);
            }
        };

        // try to integrate the equation
        rk4.integrate(equationsMaxIterations, 0.0, new double[] { 1.0 }, 1.0, new double[1]);
    }

    @Test
    public void testStepHandlers() {
        // create a step handler that throws an exception
        StepHandler stepHandler = new StepHandler() {
            @Override
            public void handleStep(StepInterpolator interpolator, int last) {
                throw new RuntimeException("Exception in step handler!");
            }
        };

        // add the step handler to the integrator
        rk4.addStepHandler(stepHandler);

        // integrate a differential equation
        double[] result = rk4.integrate(equations, 0.0, new double[] { 1.0 }, 1.0, new double[1]);

        // the exception should not be thrown
        Assert.assertArrayEquals(new double[] { 2.718281828 }, result, EPSILON);
    }

    @Test
    public void testEvents() {
        // create a differential equation with an event
        FirstOrderDifferentialEquations equationsWithEvent = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public double[] derivatives(double t, double[] y) {
                return new double[] { y[0] + y[0] * 0.1 };
            }

            @Override
            public Event[] getEvents(double t, double[] y) {
                return new Event[] { new When(1.5, null) };
            }
        };

        // create a step interpolator with an event handler
        StepInterpolator interpolator = new TrapezoidStepInterpolator() {
            @Override
            public void handleEvent(Event event) {
                // this event handler just prints a message
                System.out.println("Event detected!");
            }
        };

        // set up the integrator with the event handler
        RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("RK4",
                new double[] { 0.25, 0.25, 0.25, 0.25 },
                new double[][] { { 0.25, 0 }, { 0.25, 0.25 }, { 0.25, 0.25 }, { 0.25, 0.25 } },
                new double[] { 0.25, 0.25, 0.25, 0.25 },
                new RungeKuttaStepInterpolator(), 0.01) {

            @Override
            protected void computeDerivatives(double t, double[] y, double[] dy) {
                equationsWithEvent.computeDerivatives(t, y, dy);
            }
        };
        integrator.setStepHandler(interpolator);

        // integrate the differential equation from t=0 to t=2
        integrator.integrate(equationsWithEvent, 0.0, new double[] { 1.0 }, 2.0, new double[1]);

        // the event should be detected
        Assert.assertTrue("Event not detected!", interpolator.isEventDetected());
    }

}