package org.apache.commons.math.ode.nonstiff.EmbeddedRungeKuttaIntegrator;

import static org.junit.Assert.*;

import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.constants.DoubleValue;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaMethod;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.special.BesselFunctions;
import org.apache.commons.math3.util.Precision;

public class EmbeddedRungeKuttaIntegratorTest {

    private static final double EPSILON = 1.0e-10;
    private static final double ACCURACY = 1.0e-6;
    private static final double INITIAL_TIME = 0.0;
    private static final double INITIAL_VALUE = 1.0;
    private static final double FINAL_TIME = 2.0 * Math.PI;
    private static final double[] TOLERANCES = new double[] {EPSILON, ACCURACY};

    @org.junit.Test
    public void testRK4Integrator() {
        final EmbeddedRungeKuttaIntegrator integrator =
                new EmbeddedRungeKuttaIntegrator(
                        "RK4", false,
                        new double[] {0.25, 0.25, 0.25, 0.25},
                        new double[][] {{1.0 / 6.0, 1.0 / 2.0, 1.0 / 2.0, 1.0},
                                         {1.0 / 6.0, 1.25 / 2.0, 1.0 - 1.0 / 2.0, 1.0 / 6.0}},
                        new double[] {1.0 / 6.0, 1.0 / 2.0, 1.0 / 2.0, 1.0 / 6.0},
                        new RungeKuttaStepInterpolator() {
                            @Override
                            public void reinitialize(EmbeddedRungeKuttaIntegrator integrator,
                                                     double[] y, double[] yDot,
                                                     boolean forward,
                                                     org.apache.commons.math3.ode.Mapper[] mappers) {
                                // no-op
                            }

                            @Override
                            public void storeTime(double time) {
                                // no-op
                            }
                        },
                        0.001, 1.0, TOLERANCES);

        final ExpandableStatefulODE equations =
                new ExpandableStatefulODE("Test equation") {
                    @Override
                    public int getDimension() {
                        return 1;
                    }

                    @Override
                    public void computeDerivatives(double t, double[] y) {
                        y[0] = BesselFunctions.sin(t);
                    }

                    @Override
                    public void initialize(double t, double[] y, double[] yDot) {
                        y[0] = INITIAL_VALUE;
                        yDot[0] = 0.0;
                    }

                    @Override
                    public double getTime() {
                        return INITIAL_TIME;
                    }
                };

        integrator.integrate(equations, FINAL_TIME);

        final double finalValue = equations.getCompleteState()[0];
        assertEquals(BesselFunctions.sin(FINAL_TIME), finalValue, Precision.SAFE_EPSILON);
    }

    @org.junit.Test
    public void testRKF45Integrator() {
        final EmbeddedRungeKuttaIntegrator integrator =
                new EmbeddedRungeKuttaIntegrator(
                        "RKF45", true,
                        new double[] {0.25, 0.25, 0.25, 0.25, 0.125, 0.125},
                        new double[][] {{1.0 / 23.0, 4.0 / 23.0, 20.0 / 23.0, 40.0 / 23.0, 168.0 / 23.0, 528.0 / 23.0},
                                         {1.0 / 45.0, 18.0 / 45.0, 27.0 / 45.0, 113.0 / 45.0, 387.0 / 45.0, 822.0 / 45.0},
                                         {1.0 / 45.0, 35.0 / 45.0, 35.0 / 45.0, 110.0 / 45.0, 385.0 / 45.0, 863.0 / 45.0},
                                         {1.0 / 45.0, 216.0 / 45.0, 672.0 / 45.0, 2673.0 / 45.0, 10940.0 / 45.0, 42276.0 / 45.0}},
                        new double[] {1.0 / 45.0, 35.0 / 45.0, 35.0 / 45.0, 110.0 / 45.0, 385.0 / 45.0, 863.0 / 45.0},
                        new RungeKuttaStepInterpolator() {
                            @Override
                            public void reinitialize(EmbeddedRungeKuttaIntegrator integrator,
                                                     double[] y, double[] yDot,
                                                     boolean forward,
                                                     org.apache.commons.math3.ode.Mapper[] mappers) {
                                // no-op
                            }

                            @Override
                            public void storeTime(double time) {
                                // no-op
                            }
                        },
                        0.001, 1.0, TOLERANCES);

        final ExpandableStatefulODE equations =
                new ExpandableStatefulODE("Test equation") {
                    @Override
                    public int getDimension() {
                        return 1;
                    }

                    @Override
                    public void computeDerivatives(double t, double[] y) {
                        y[0] = BesselFunctions.sin(t);
                    }

                    @Override
                    public void initialize(double t, double[] y, double[] yDot) {
                        y[0] = INITIAL_VALUE;
                        yDot[0] = 0.0;
                    }

                    @Override
                    public double getTime() {
                        return INITIAL_TIME;
                    }
                };

        integrator.integrate(equations, FINAL_TIME);

        final double finalValue = equations.getCompleteState()[0];
        assertEquals(BesselFunctions.sin(FINAL_TIME), finalValue, Precision.SAFE_EPSILON);
    }

}