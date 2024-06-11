import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.DefaultValueEvent;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.MassSpringForceEvent;
import org.apache.commons.math3.ode.events.MultivaluedMapEvent;
import org.apache.commons.math3.ode.events.StepHandler;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandlerAdapter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RungeKuttaIntegratorTest {

    private RungeKuttaIntegrator integrator;
    private RungeKuttaStepInterpolator prototype;

    @Before
    public void setUp() {
        double[] c = new double[]{0.5, 0.5, 0.5, 0.5};
        double[][] a = new double[][]{{0, 0, 1/2, 1/2},
                                       {0, 0, 1/2, 1},
                                       {0, 1/3, 1/4, 1/3},
                                       {1/4, 1/3, 1/2, 1/4}};
        double[] b = new double[]{1/6, 1/3, 1/3, 1/6};
        integrator = new RungeKuttaIntegrator("RungeKuttaIntegrator", c, a, b, new RungeKuttaStepInterpolator(), 0.1) {
            @Override
            protected void computeDerivatives(double t, double[] y, double[] dy) {
                // Custom implementation of computeDerivatives method goes here.
            }
        };
        prototype = new RungeKuttaStepInterpolator();
    }

    @Test
    public void testIntegratorFunctionality() throws Exception {
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] dy) {
                dy[0] = Math.exp(y[0]);
            }
        };

        double[] initialValues = new double[]{0.0};
        double initialTime = 0.0;
        double finalTime = 1.0;
        double[] finalValues = new double[]{Math.E};

        double result = integrator.integrate(equations, initialTime, initialValues, finalTime, finalValues);

        Assert.assertEquals(finalTime, result, 1e-12);
        Assert.assertArrayEquals(finalValues, initialValues, 1e-12);
    }

    @Test
    public void testIntegratorWithEvents() throws Exception {
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] dy) {
                dy[0] = Math.exp(y[0]);
            }
        };

        double[] initialValues = new double[]{0.0};
        double initialTime = 0.0;
        double finalTime = 2.0;

        StepHandler stepHandler = new StepHandlerAdapter() {
            @Override
            public void handleStep(StepHandler event, int type) {
                Assert.assertEquals(MassSpringForceEvent.class, event.getClass());
                Assert.assertEquals(2.0, event.getValue(), 1e-12);
            }
        };

        integrator.setStepHandlers(stepHandler);
        integrator.setEvents(new Event[] {
                new MassSpringForceEvent(1.0, 2.0),
                new DefaultValueEvent(2.0, new double[]{0.0, 1.0})
        });

        double result = integrator.integrate(equations, initialTime, initialValues, finalTime, null);
        Assert.assertFalse(Double.isNaN(result));
    }

    @Test(expected = DerivativeException.class)
    public void testDerivativeException() throws Exception {
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] dy) {
                throw new DerivativeException("Test Derivative Exception");
            }
        };

        double[] initialValues = new double[]{0.0};
        double initialTime = 0.0;
        double finalTime = 1.0;

        integrator.integrate(equations, initialTime, initialValues, finalTime, null);
    }

    @Test(expected = IntegratorException.class)
    public void testIntegratorException() throws Exception {
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] dy) {
                throw new IntegratorException("Test Integrator Exception");
            }
        };

        double[] initialValues = new double[]{0.0};
        double initialTime = 0.0;
        double finalTime = 1.0;

        integrator.integrate(equations, initialTime, initialValues, finalTime, null);
    }

    @Test
    public void testIntegratorWithMultivaluedMapEvent() throws Exception {
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] dy) {
                dy[0] = Math.exp(y[0]);
            }
        };

        double[] initialValues = new double[]{0.0};
        double initialTime = 0.0;
        double finalTime = 2.0;

        StepHandler stepHandler = new StepHandlerAdapter() {
            @Override
            public void handleStep(StepHandler event, int type) {
                Assert.assertEquals(MultivaluedMapEvent.class, event.getClass());
                Assert.assertEquals(new double[]{1.0, 2.0}, event.getValues(), 1e-12);
            }
        };

        integrator.setStepHandlers(stepHandler);
        integrator.setEvents(new Event[] {
                new MultivaluedMapEvent(1.0, new double[]{1.0, 2.0})
        });

        double result = integrator.integrate(equations, initialTime, initialValues, finalTime, null);
        Assert.assertFalse(Double.isNaN(result));
    }
}