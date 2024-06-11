package org.apache.commons.math.ode.nonstiff;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketException;
import org.apache.commons.math3.exception.TooManyIterationsException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.DefaultValueHolder;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.MutableStatefulEvent;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for EmbeddedRungeKuttaIntegrator.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    

    @Test
    public void testGetOrder() {
        // Create a test instance
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK23", true, new double[] {0.25, 0.5, 0.5},
                new double[][] {{0.25, 0.25}, {0.5, 0.5}}, new double[] {0.25, 0.25}, new RungeKuttaStepInterpolator() {});
        int order = integrator.getOrder();
        Assert.assertEquals("Wrong order", 2, order);
    }

    @Test
    public void testGetSafety() {
        // Create a test instance
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK23", true, new double[] {0.25, 0.5, 0.5},
                new double[][] {{0.25, 0.25}, {0.5, 0.5}}, new double[] {0.25, 0.25}, new RungeKuttaStepInterpolator() {}, 0.01, 0.01, null, null);
        double safety = integrator.getSafety();
        Assert.assertEquals("Wrong safety", 0.9, safety, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullName() {
        // Create a test instance with null name
        new EmbeddedRungeKuttaIntegrator(null, true, new double[] {0.25, 0.5, 0.5}, new double[][] {{0.25, 0.25}, {0.5, 0.5}},
                new double[] {0.25, 0.25}, new RungeKuttaStepInterpolator() {}, 0.01, 0.01, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeSafety() {
        // Create a test instance with negative safety
        new EmbeddedRungeKuttaIntegrator("RK23", true, new double[] {0.25, 0.5, 0.5}, new double[][] {{0.25, 0.25}, {0.5, 0.5}},
                new double[] {0.25, 0.25}, new RungeKuttaStepInterpolator() {}, 0.01, 0.01, null, null);
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK23", true, new double[] {0.25, 0.5, 0.5}, new double[][] {{0.25, 0.25}, {0.5, 0.5}},
                new double[] {0.25, 0.25}, new RungeKuttaStepInterpolator() {}, 0.01, 0.01, null, null);
        integrator.setSafety(-1.0);
    }

    @Test
    public void testIntegrateSimple() throws DerivativeException, IntegratorException {
        // Create a test function
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[1];
                yPrime[1] = -y[0];
            }
        };

        // Create a test instance
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK23", true, new double[] {0.25, 0.5, 0.5},
                new double[][] {{0.25, 0.25}, {0.5, 0.5}}, new double[] {0.25, 0.25}, new RungeKuttaStepInterpolator() {}, 0.01, 0.01, null, null);

        // Test integration
        double t0 = 0.0;
        double[] y0 = new double[] {1.0, 0.0};
        double t1 = 1.0;
        double[] y1 = new double[] {0.0, 1.0};
        double result = integrator.integrate(equations, t0, y0, t1, y1);
        Assert.assertEquals("Integration error", 1.0, result, 1e-10);
    }

    @Test
    public void testIntegrateEvent() throws DerivativeException, IntegratorException, NoBracketException, MaxCountExceededException,
            TooManyIterationsException {
        // Create a test function with an event
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[1];
                yPrime[1] = -y[0];
            }

            @Override
            public double getLowerBracket(double t, double[] y) {
                return -1.0;
            }

            @Override
            public double getUpperBracket(double t, double[] y) {
                return 1.0;
            }

            @Override
            public int isStateDefined(int i, double t, double[] y) {
                return i == 0 ? 1 : 0;
            }

            @Override
            public Event[] getEvents(double t, double[] y, Event[] events) {
                if (t > 1.0) {
                    events[0] = new MutableStatefulEvent() {
                        @Override
                        public void evaluate(double t, double[] y, double[] dy, double[] state, Object userData) throws NoBracketException,
                                MaxCountExceededException, TooManyIterationsException {
                            dy[0] = 0.0;
                            dy[1] = 1.0;
                            state[0] = 1.0;
                        }

                        @Override
                        public double bracket(double t, double[] y, double[] dy, double[] state, Object userData) throws NoBracketException {
                            return 1.0 - Math.abs(y[0]);
                        }
                    };
                }
                return new Event[0];
            }
        };

        // Create a test instance
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK23", true, new double[] {0.25, 0.5, 0.5},
                new double[][] {{0.25, 0.25}, {0.5, 0.5}}, new double[] {0.25, 0.25}, new RungeKuttaStepInterpolator() {}, 0.01, 1.1, null, null);

        // Test integration with an event
        double t0 = 0.0;
        double[] y0 = new double[] {1.0, 0.0};
        double[] yEvent = new double[] {0.1, 0.1};
        double t1 = 1.2;
        double[] y1 = new double[] {1.0, 1.0};
        DefaultValueHolder holder = new DefaultValueHolder();
        integrator.addStepHandler(new StepHandler() {
            @Override
            public void handleStep(StepHandlerContext context, int stepNumber) {
                holder.setValue(context.getY());
            }
        });
        integrator.integrate(equations, t0, y0, t1, y1);
        Assert.assertArrayEquals("Y values", yEvent, holder.getValue(), 1e-10);
    }

    @Test
    public void testIntegrateWithDenseOutput() throws DerivativeException, IntegratorException {
        // Create a test function
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[1];
                yPrime[1] = -y[0];
            }
        };

        // Create a test instance
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK23", true, new double[] {0.25, 0.5, 0.5},
                new double[][] {{0.25, 0.25}, {0.5, 0.5}}, new double[] {0.25, 0.25}, new RungeKuttaStepInterpolator() {
                    @Override
                    public RungeKuttaStepInterpolator copy() {
                        return new RungeKuttaStepInterpolator() {
                            @Override
                            public void reinitialize(RungeKuttaIntegrator integrator, double[] y, double[][] yDot, boolean forward) {
                                super.reinitialize(integrator, y, yDot, forward);
                                this.requiresDenseOutput = true;
                            }
                        };
                    }
                }, 0.01, 1.0, null, null);

        // Test integration with dense output
        double t0 = 0.0;
        double[] y0 = new double[] {1.0, 0.0};
        double t1 = 1.0;
        double[] y1 = new double[] {0.0, 1.0};
        double[] yDense = new double[2][];
        yDense[0] = new double[2];
        yDense[1] = new double[2];
        integrator.integrate(equations, t0, y0, t1, y1, yDense);
        Assert.assertEquals("Y dense values size", 2, yDense[0].length);
        Assert.assertEquals("Y dense values size", 2, yDense[1].length);
        Assert.assertArrayEquals("Y dense values", y1, yDense[1], 1e-10);
    }

    @Test
    public void testIntegrateWithSparsity() throws DerivativeException, IntegratorException {
        // Create a test function
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[1];
                yPrime[1] = -y[0];
            }
        };

        // Create a test instance
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK23", true, new double[] {0.25, 0.5, 0.5},
                new double[][] {{0.25, 0.25}, {0.5, 0.5}}, new double[] {0.25, 0.25}, new RungeKuttaStepInterpolator() {
                    @Override
                    public RungeKuttaStepInterpolator copy() {
                        return new RungeKuttaStepInterpolator() {
                            @Override
                            public void reinitialize(RungeKuttaIntegrator integrator, double[] y, double[][] yDot, boolean forward) {
                                super.reinitialize(integrator, y, yDot, forward);
                                this.requiresDenseOutput = false;
                            }
                        };
                    }
                }, 0.01, 1.0, null, null);

        // Test integration with sparsity
        double t0 = 0.0;
        double[] y0 = new double[] {1.0, 0.0};
        double t1 = 1.0;
        double[] y1 = new double[] {0.0, 1.0};
        integrator.integrate(equations, t0, y0, t1, y1);
        Assert.assertArrayEquals("Y values", y1, y);
    }

    @Test
    public void testGetMinReduction() {
        // Create a test instance
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK23", true, new double[] {0.25, 0.5, 0.5},
                new double[][] {{0.25, 0.25}, {0.5, 0.5}}, new double[] {0.25, 0.25}, new RungeKuttaStepInterpolator() {}, 0.01, 0.01, null, null);

        // Test getMinReduction
        double minReduction = integrator.getMinReduction();
        Assert.assertEquals("Wrong minimal reduction", 0.2, minReduction, 0.001);

        // Test setMinReduction
        integrator.setMinReduction(0.1);
        minReduction = integrator.getMinReduction();
        Assert.assertEquals("Wrong minimal reduction", 0.1, minReduction, 0.001);
    }

    @Test
    public void testGetMaxGrowth() {
        // Create a test instance
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK23", true, new double[] {0.25, 0.5, 0.5},
                new double[][] {{0.25, 0.25}, {0.5, 0.5}}, new double[] {0.25, 0.25}, new RungeKuttaStepInterpolator() {}, 0.01, 0.01, null, null);

        // Test getMaxGrowth
        double maxGrowth = integrator.getMaxGrowth();
        Assert.assertEquals("Wrong maximal growth", 10.0, maxGrowth, 0.001);

        // Test setMaxGrowth
        integrator.setMaxGrowth(5.0);
        maxGrowth = integrator.getMaxGrowth();
        Assert.assertEquals("Wrong maximal growth", 5.0, maxGrowth, 0.001);
    }

    
}