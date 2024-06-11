package org.apache.commons.math.ode.nonstiff;

import org.apache.commons.math3.exception.MaxAbsRelativeErrorExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.TooManyIterationsException;
import org.junit.Assert;
import org.junit.Test;

import static org.apache.commons.math3.special.MathConstants.EPSILON;
import static org.apache.commons.math3.special.MathConstants.PI;
import static org.apache.commons.math3.special.MathFunctions.cos;
import static org.apache.commons.math3.special.MathFunctions.sin;

public class RungeKuttaIntegratorTest {

    @Test
    public void testRungeKuttaIntegrator() throws Exception {
        // Test a simple RK4 Integrator
        RungeKuttaIntegrator rk4 = new RK4Integrator(
                "RK4",
                new double[] { 1.0 / 6.0, 1.0 / 2.0, 1.0 / 2.0, 1.0 / 6.0 },
                new double[][] { { 1.0 / 6.0, 1.0 / 2.0, 1.0 / 2.0, 1.0 / 6.0 },
                                  { 0.0, 0.5, 0.5, 0.0 },
                                  { 0.0, 0.0, 0.5, 1.0 / 2.0 },
                                  { 0.0, 0.0, 0.0, 1.0 } },
                new double[] { 1.0 / 6.0, 1.0 / 6.0, 1.0 / 6.0, 1.0 },
                new RK4StepInterpolator(),
                0.01
        );

        FirstOrderDifferentialEquations equations = new AbstractDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public double[] derivative(double time, double[] state) {
                double y = state[0];
                return new double[] { y * (1 - y) };
            }
        };

        double[] y0 = new double[] { 0.1 };
        double t0 = 0.0;
        double t = 1.0;
        double[] y = new double[1];

        double result = rk4.integrate(equations, t0, y0, t, y);
        Assert.assertEquals(1.0, y[0], EPSILON);

        // Test the error of the RK4 Integrator
        double error = Math.abs(y[0] - Math.exp(1.0));
        Assert.assertTrue("Error should be smaller than EPSILON.", error < EPSILON);

        // Test a RK2 Integrator
        RungeKuttaIntegrator rk2 = new RK2Integrator(
                "RK2",
                new double[] { 0.5 },
                new double[][] { { 0.5, 0.5 } },
                new double[] { 1.0 },
                new RK2StepInterpolator(),
                0.01
        );

        double[] yDot = new double[1];

        double errorRK2 = 0.0;
        for (int i = 0; i < 100; i++) {
            double t1 = t0 + 0.01;
            rk2.integrate(equations, t0, y0, t1, y);
            double y1 = y[0];
            double errorSingleStep = Math.abs(y1 - y[0]);
            errorRK2 += errorSingleStep;
            t0 = t1;
            equations.derivative(t, y, yDot);
            double errorDerivative = Math.abs(yDot[0] - equations.derivative(t, y)[0]);
            errorRK2 += errorDerivative;
        }

        double errorTotal = errorRK2 / 100.0;
        Assert.assertTrue("Total error should be smaller than EPSILON.", errorTotal < EPSILON);

        // Test RK2 limiting case
        double h = 1.0e-20;
        rk2.setStep(h);
        try {
            rk2.integrate(equations, t0, y0, t, y);
        } catch (NumberIsTooSmallException e) {
            // Expected
        }

        // Test RK4 limiting case
        h = 1.0e-20;
        rk4.setStep(h);
        try {
            rk4.integrate(equations, t0, y0, t, y);
        } catch (MaxAbsRelativeErrorExceededException e) {
            // Expected
        }

        // Test RK4 with a very large step
        rk4.setStep(10.0);
        try {
            rk4.integrate(equations, t0, y0, t, y);
        } catch (TooManyIterationsException e) {
            // Expected
        }

        // Test RK4 with a singular function
        FirstOrderDifferentialEquations equationsSingular = new AbstractDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public double[] derivative(double time, double[] state) {
                double y = state[0];
                return new double[] { 1.0 / (y * (y - 1.0)) };
            }
        };

        try {
            rk4.integrate(equationsSingular, t0, y0, t, y);
            Assert.fail();
        } catch (Exception e) {
            // Expected
        }

        // Test RK4 with a function with a discontinuity
        FirstOrderDifferentialEquations equationsDiscontinuity = new AbstractDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public double[] derivative(double time, double[] state) {
                double y = state[0];
                if (y >= 0) {
                    return new double[] { y * (1 - y) };
                } else {
                    throw new DerivativeException("Function has a discontinuity at y = 0.");
                }
            }
        };

        try {
            rk4.integrate(equationsDiscontinuity, t0, y0, t, y);
            Assert.fail();
        } catch (DerivativeException e) {
            // Expected
        }

        // Test RK4 with a function with a vertical asymptote
        FirstOrderDifferentialEquations equationsAsymptote = new AbstractDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public double[] derivative(double time, double[] state) {
                double y = state[0];
                return new double[] { 1.0 / (1.0 - y * y) };
            }
        };

        try {
            rk4.integrate(equationsAsymptote, t0, y0, t, y);
            Assert.fail();
        } catch (Exception e) {
            // Expected
        }

        // Test RK4 with a function with a limit cycle
        FirstOrderDifferentialEquations equationsLimitCycle = new AbstractDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public double[] derivative(double time, double[] state) {
                double y = state[0];
                return new double[] { 1.0 - (y * y) };
            }
        };

        double[] initialState = new double[] { 1.0 };
        double[] finalState = new double[] { 1.0 };
        double[] limitCycle = new double[] { 1.0, 1.0 };
        double tStart = 0.0;
        double tEnd = 2 * PI;

        RungeKuttaIntegrator rk4LimitCycle = new RK4Integrator(
                "RK4LimitCycle",
                new double[] { 1.0 / 6.0, 1.0 / 2.0, 1.0 / 2.0, 1.0 / 6.0 },
                new double[][] { { 1.0 / 6.0, 1.0 / 2.0, 1.0 / 2.0, 1.0 / 6.0 },
                                  { 0.0, 0.5, 0.5, 0.0 },
                                  { 0.0, 0.0, 0.5, 1.0 / 2.0 },
                                  { 0.0, 0.0, 0.0, 1.0 } },
                new double[] { 1.0 / 6.0, 1.0 / 6.0, 1.0 / 6.0, 1.0 },
                new RK4StepInterpolator(),
                0.001
        );

        rk4LimitCycle.integrate(equationsLimitCycle, tStart, initialState, tEnd, finalState);
        double[] resultLimitCycle = rk4LimitCycle.getLastStep();
        Assert.assertArrayEquals(limitCycle, resultLimitCycle, 0.001);

    }

}

class AbstractDifferentialEquations {
    // Empty interface
}

class RK2Integrator extends RungeKuttaIntegrator {
    // Empty constructor
}

class RK2StepInterpolator extends RungeKuttaStepInterpolator {
    // Empty constructor
}

class RK4Integrator extends RungeKuttaIntegrator {
    // Empty constructor
}

class RK4StepInterpolator extends RungeKuttaStepInterpolator {
    // Empty constructor
}