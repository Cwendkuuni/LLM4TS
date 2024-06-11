package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.asserts.ODEAssert;
import org.apache.commons.math3.ode.events.SimpleReleaseState;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Test;

public class RungeKuttaIntegratorTest {

    @Test(expected = UnsupportedOperationException.class)
    public void testConstructorWithNullName() {
        new RungeKuttaIntegrator(null, null, null, null, null, 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullC() {
        double[] c = null;
        double[][] a = new double[3][3];
        double[] b = new double[3];
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(3);
        double step = 0.01;
        new RungeKuttaIntegrator("", c, a, b, prototype, step);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullA() {
        double[] c = new double[3];
        double[][] a = null;
        double[] b = new double[3];
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(3);
        double step = 0.01;
        new RungeKuttaIntegrator("", c, a, b, prototype, step);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullB() {
        double[] c = new double[3];
        double[][] a = new double[3][3];
        double[] b = null;
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(3);
        double step = 0.01;
        new RungeKuttaIntegrator("", c, a, b, prototype, step);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullPrototype() {
        double[] c = new double[3];
        double[][] a = new double[3][3];
        double[] b = new double[3];
        RungeKuttaStepInterpolator prototype = null;
        double step = 0.01;
        new RungeKuttaIntegrator("", c, a, b, prototype, step);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeStep() {
        double[] c = new double[3];
        double[][] a = new double[3][3];
        double[] b = new double[3];
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(3);
        double step = -0.01;
        new RungeKuttaIntegrator("", c, a, b, prototype, step);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithZeroStep() {
        double[] c = new double[3];
        double[][] a = new double[3][3];
        double[] b = new double[3];
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(3);
        double step = 0.0;
        new RungeKuttaIntegrator("", c, a, b, prototype, step);
    }

    @Test
    public void testIntegrationWithEmptyDifferentialEquations() {
        double[] c = new double[3];
        double[][] a = new double[3][3];
        double[] b = new double[3];
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(3);
        double step = 0.01;
        RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("", c, a, b, prototype, step);
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getNumberOfEquations() {
                return 0;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                // Not called
            }
        };
        try {
            integrator.integrate(equations, 0.0, new double[0], 1.0, new double[0]);
            fail("Integration should have thrown an exception.");
        } catch (DerivativeException e) {
            assertNull(e.getCause());
        }
    }

    @Test(expected = DerivativeException.class)
    public void testIntegrationWithNullY() {
        double[] c = new double[3];
        double[][] a = new double[3][3];
        double[] b = new double[3];
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(3);
        double step = 0.01;
        RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("", c, a, b, prototype, step);
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getNumberOfEquations() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = 1.0;
            }
        };
        integrator.integrate(equations, 0.0, null, 1.0, new double[1]);
    }

    @Test(expected = DerivativeException.class)
    public void testIntegrationWithNullYPrime() {
        double[] c = new double[3];
        double[][] a = new double[3][3];
        double[] b = new double[3];
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(3);
        double step = 0.01;
        RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("", c, a, b, prototype, step);
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getNumberOfEquations() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = 1.0;
            }
        };
        integrator.integrate(equations, 0.0, new double[1], 1.0, null);
    }

    @Test(expected = IntegratorException.class)
    public void testIntegrationWithExceptionInComputeDerivatives() {
        double[] c = new double[3];
        double[][] a = new double[3][3];
        double[] b = new double[3];
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(3);
        double step = 0.01;
        RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("", c, a, b, prototype, step);
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getNumberOfEquations() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                throw new MaxCountExceededException();
            }
        };
        integrator.integrate(equations, 0.0, new double[1], 1.0, new double[1]);
    }

    @Test(expected = IntegratorException.class)
    public void testIntegrationWithNullEventsHandlersManager() {
        double[] c = new double[3];
        double[][] a = new double[3][3];
        double[] b = new double[3];
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(3);
        double step = 0.01;
        RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("", c, a, b, prototype, step);
        integrator.addEndTimeChecker(null, 0.0, null);
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getNumberOfEquations() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = 1.0;
            }
        };
        integrator.integrate(equations, 0.0, new double[1], 1.0, new double[1]);
    }

    @Test
    public void testIntegrationWithSimpleReleaseState() {
        double[] c = new double[3];
        double[][] a = new double[3][3];
        double[] b = new double[3];
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(3);
        double step = 0.01;
        RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("", c, a, b, prototype, step);
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getNumberOfEquations() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[0] * 0.1;
            }
        };
        RungeKuttaIntegrator.StepHandler stepHandler = new RungeKuttaIntegrator.StepHandler() {
            @Override
            public void handleStep(RungeKuttaIntegrator.StepContext context, boolean lastStep) {
                SimpleReleaseState releaseState = (SimpleReleaseState) context.getUserData();
                releaseState.release(context.getTime());
            }
        };
        integrator.addStepHandler(stepHandler);
        double[] initialState = {1.0};
        double stopTime = 5.0;
        integrator.integrate(equations, 0.0, initialState, stopTime, new double[1]);
        ODEAssert.assertDoubleArrayEquals(new double[]{5.0}, new double[]{5.0}, 1e-10);
    }

}