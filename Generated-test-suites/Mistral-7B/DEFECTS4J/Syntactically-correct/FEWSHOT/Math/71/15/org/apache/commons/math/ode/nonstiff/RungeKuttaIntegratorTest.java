package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.UnivariateFunctionEvaluationException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.AbstractEvent;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.EventException;
import org.apache.commons.math3.ode.events.EventManager;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.ode.sampling.TrapezoidStepInterpolator;
import org.apache.commons.math3.ode.util.UnivariateFunctionUtils;
import org.apache.commons.math3.special.BesselFunctions;
import org.junit.Test;

public class RungeKuttaIntegratorTest {

    @Test
    public void testIntegration() throws Exception {
        final String name = "Runge-Kutta Integrator";
        final double step = 0.1;

        // Test case 1: Simple ODE with no events
        final double[] initialValues = {0.0, 1.0};
        final double[] finalValues  = {1.0, 0.9};
        final FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = -y[1];
                yPrime[1] = y[0];
            }
        };

        final RungeKuttaIntegrator integrator =
                new RungeKuttaIntegrator(name, new double[] {0.5, 0.5},
                                         new double[][] {new double[] {0.5, 0.5}, {0.5, 0.5}},
                                         new double[] {0.5, 0.5},
                                         new RungeKuttaStepInterpolator(), step) {
                    @Override
                    protected StepInterpolator createStepInterpolator() {
                        return new TrapezoidStepInterpolator();
                    }
                };

        assertArrayEquals(finalValues, integrator.integrate(equations, 0.0, initialValues, 1.0, new double[2]));

        // Test case 2: Simple ODE with event
        final double eventTime = Math.log(2.0) / Math.log(2.5);
        final Event event = new When(eventTime) {
            @Override
            public void evaluateEvent(double t, double[] y, double[] yDot) throws EventException {
                y[0] = 1.0;
                yDot[0] = 0.0;
            }
        };
        final EventManager eventsManager = new EventManager();
        eventsManager.addEvent(event);

        final double[] initialValuesWithEvent = {0.0, 1.0};
        final double[] finalValuesWithEvent = {1.0, 0.0};
        assertArrayEquals(finalValuesWithEvent, integrator.integrate(equations, 0.0, initialValuesWithEvent, 1.0, new double[2], eventsManager));

        // Test case 3: ODE with singular Jacobian
        final FirstOrderDifferentialEquations singularEquations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double y, double[] yPrime) {
                double x = Math.sqrt(y);
                yPrime[0] = 1.0 / (2.0 * x * Math.sqrt(x));
            }
        };
        try {
            integrator.integrate(singularEquations, 0.0, 0.0, 1.0, new double[1]);
            fail("Expected IntegratorException when encountering singular Jacobian");
        } catch (IntegratorException e) {
            // Expected
        }

        // Test case 4: ODE with infinite Jacobian
        final FirstOrderDifferentialEquations infiniteEquations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double y, double[] yPrime) {
                double x = Math.pow(y, 0.5);
                yPrime[0] = Math.log(x) / x;
            }
        };
        try {
            integrator.integratorStep(infiniteEquations, new double[1], 0.0, 1.0);
            fail("Expected IntegratorException when encountering infinite Jacobian");
        } catch (IntegratorException e) {
            // Expected
        }

        // Test case 5: ODE with root
        final FirstOrderDifferentialEquations rootEquations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double y, double[] yPrime) {
                yPrime[0] = y * y - 1.0;
            }
        };
        final double root = 1.0;
        try {
            integrator.integrate(rootEquations, 0.0, 0.0, root, new double[1]);
            fail("Expected RootFoundException when encountering root");
        } catch (IntegratorException e) {
            // Expected
            assertInstanceOf(RootFoundException.class, e.getCause());
        }

        // Test case 6: ODE with limit cycle
        final FirstOrderDifferentialEquations limitCycleEquations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double y, double[] yPrime) {
                double x = Math.sin(y);
                yPrime[0] = Math.cos(y) * (1.0 + Math.sin(y) * Math.sin(y));
            }
        };
        final double limitCycleInitial = Math.PI;
        try {
            integrator.integrate(limitCycleEquations, 0.0, new double[] {limitCycleInitial}, 4.0 * Math.PI, new double[1]);
            fail("Expected Exception when encountering limit cycle");
        } catch (Exception e) {
            // Expected
            assertInstanceOf(IllegalArgumentException.class, e);
        }

        // Test case 7: ODE with maximum number of steps reached
        final FirstOrderDifferentialEquations maxStepsEquations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 3;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[1];
                yPrime[1] = y[2];
                yPrime[2] = Math.sin(y[2]) / (1.0 + y[2] * y[2]);
            }
        };
        final double maxStepsInitial[] = {0.0, 1.0, 1.0};
        try {
            integrator.integrate(maxStepsEquations, 0.0, maxStepsInitial, 100.0, new double[3]);
            fail("Expected MaxCountExceededException when maximum number of steps is reached");
        } catch (IntegratorException e) {
            // Expected
            assertInstanceOf(MaxCountExceededException.class, e.getCause());
        }

        // Test case 8: ODE with steps smaller than allowed minimum
        final FirstOrderDifferentialEquations minStepEquations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double y, double[] yPrime) {
                yPrime[0] = 1.0;
            }
        };
        final double minStepInitial = 0.0;
        try {
            integrator.integrate(minStepEquations, minStepInitial, new double[1], 1.0e-10, new double[1]);
            fail("Expected Exception when step size is smaller than allowed minimum");
        } catch (IntegratorException e) {
            // Expected
            assertInstanceOf(IllegalArgumentException.class, e.getCause());
        }

        // Test case 9: ODE with bessel functions
        final FirstOrderDifferentialEquations besselEquations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double y, double[] yPrime) {
                yPrime[0] = UnivariateFunctionUtils.value(BesselFunctions.I0, y);
            }
        };
        final double besselInitial = 1.0;
        try {
            integrator.integrate(besselEquations, besselInitial, new double[1], 10.0, new double[1]);
            fail("Expected Exception when bessel functions are encountered");
        } catch (IntegratorException e) {
            // Expected
            assertInstanceOf(UnivariateFunctionEvaluationException.class, e.getCause());
        }

        // Test case 10: ODE with bessel functions and events
        final double besselEventTime = Math.PI;
        final Event besselEvent = new When(besselEventTime) {
            @Override
            public void evaluateEvent(double t, double[] y, double[] yDot) throws EventException {
                y[0] = UnivariateFunctionUtils.value(BesselFunctions.I0, y[0]) - 1.0;
            }
        };
        final EventManager besselEventsManager = new EventManager();
        besselEventsManager.addEvent(besselEvent);
        try {
            integrator.integrate(besselEquations, besselInitial, new double[1], 20.0, new double[1], besselEventsManager);
            fail("Expected Exception when bessel functions and events are encountered");
        } catch (IntegratorException e) {
            // Expected
            assertInstanceOf(UnivariateFunctionEvaluationException.class, e.getCause());
        }

        // Test case 11: ODE with bessel functions, events, and singular Jacobian
        final FirstOrderDifferentialEquations singularBesselEquations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double y, double[] yPrime) {
                double x = UnivariateFunctionUtils.value(BesselFunctions.I0, y);
                yPrime[0] = x * x;
            }
        };
        try {
            integrator.integrate(singularBesselEquations, besselInitial, new double[1], 10.0, new double[1]);
            fail("Expected IntegratorException when encountering singular Jacobian and bessel functions");
        } catch (IntegratorException e) {
            // Expected
            assertInstanceOf(SingularMatrixException.class, e.getCause());
        }

        // Test case 12: ODE with bessel functions, events, and infinite Jacobian
        final FirstOrderDifferentialEquations infiniteBesselEquations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double y, double[] yPrime) {
                double x = UnivariateFunctionUtils.value(BesselFunctions.I0, y);
                yPrime[0] = 1.0 / x;
            }
        };
        try {
            integrator.integrate(infiniteBesselEquations, besselInitial, new double[1], 10.0, new double[1]);
            fail("Expected IntegratorException when encountering infinite Jacobian and bessel functions");
        } catch (IntegratorException e) {
            // Expected
            assertInstanceOf(SingularMatrixException.class, e.getCause());
        }

    }

}