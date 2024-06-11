import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.special.BesselFunctions;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for {@link EmbeddedRungeKuttaIntegrator}.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    /**
     * Test method for {@link EmbeddedRungeKuttaIntegrator#integrate(ExpandableStatefulODE, double)}.
     */
    @Test
    public void testIntegrate() {
        // Test a simple function
        final double[] y0 = new double[] { 1.0, 0.0 };
        final ExpandableStatefulODE ode = new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return y0.length;
            }

            @Override
            public int getOrder() {
                return 2;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[1];
                yDot[1] = -t * y[0];
            }

            @Override
            public double getTime() {
                return 0.0;
            }

            @Override
            public void setTime(double t) {
                // empty
            }
        };
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("Test method", false, new double[] { 0.3, 0.5, 0.2 },
                new double[][] { { 0.3, 0.4, 0.2 }, { 0.2, 0.2, 0.4 }, { 0.6, 0.1, 0.1 } }, new double[] { 0.3, 0.4 },
                new RungeKuttaStepInterpolator() {
                    @Override
                    public double interpolate(double t, double[] y, double[] yDot) {
                        // empty
                    }

                    @Override
                    public void reinitialize(EmbeddedRungeKuttaIntegrator integrator, double[] yDotTmp, double[][] yDotK,
                                             boolean forward, Mapper mapper, Mapper[] secondaryMappers) {
                        // empty
                    }

                    @Override
                    public void storeTime(double time) {
                        // empty
                    }
                }, 0.001, 1.0, 1.0e-10, 1.0e-10) {
            @Override
            public int getOrder() {
                return 2;
            }
        };
        integrator.integrate(ode, 2.0);
        double[] y1 = ode.getCompleteState();
        assertEquals(y1[0], 0.25, 1e-10);
        assertEquals(y1[1], -1.0, 1e-10);

        // Test a function with a singularity
        ode = new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return y0.length;
            }

            @Override
            public int getOrder() {
                return 2;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[1];
                yDot[1] = 1.0 / Math.sqrt(y[0]);
            }

            @Override
            public double getTime() {
                return 1.0;
            }

            @Override
            public void setTime(double t) {
                // empty
            }
        };
        integrator = new EmbeddedRungeKuttaIntegrator("Test method", false, new double[] { 0.3, 0.5, 0.2 },
                new double[][] { { 0.3, 0.4, 0.2 }, { 0.2, 0.2, 0.4 }, { 0.6, 0.1, 0.1 } }, new double[] { 0.3, 0.4 },
                new RungeKuttaStepInterpolator() {
                    @Override
                    public double interpolate(double t, double[] y, double[] yDot) {
                        // empty
                    }

                    @Override
                    public void reinitialize(EmbeddedRungeKuttaIntegrator integrator, double[] yDotTmp, double[][] yDotK,
                                             boolean forward, Mapper mapper, Mapper[] secondaryMappers) {
                        // empty
                    }

                    @Override
                    public void storeTime(double time) {
                        // empty
                    }
                }, 0.001, 1.0, 1.0e-10, 1.0e-10) {
            @Override
            public int getOrder() {
                return 2;
            }
        };
        integrator.integrate(ode, 2.0);
        y1 = ode.getCompleteState();
        double expected = Math.sqrt(2.0);
        assertEquals(y1[0], expected, 1e-10);
        assertEquals(y1[1], 1.0 / expected, 1e-10);

        // Test a function with an oscillatory solution
        ode = new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return y0.length;
            }

            @Override
            public int getOrder() {
                return 2;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[1];
                yDot[1] = -BesselFunctions.BesselJ(0, 2 * Math.PI * t) * y[1];
            }

            @Override
            public double getTime() {
                return 0.0;
            }

            @Override
            public void setTime(double t) {
                // empty
            }
        };
        integrator = new EmbeddedRungeKuttaIntegrator("Test method", false, new double[] { 0.3, 0.5, 0.2 },
                new double[][] { { 0.3, 0.4, 0.2 }, { 0.2, 0.2, 0.4 }, { 0.6, 0.1, 0.1 } }, new double[] { 0.3, 0.4 },
                new RungeKuttaStepInterpolator() {
                    @Override
                    public double interpolate(double t, double[] y, double[] yDot) {
                        // empty
                    }

                    @Override
                    public void reinitialize(EmbeddedRungeKuttaIntegrator integrator, double[] yDotTmp, double[][] yDotK,
                                             boolean forward, Mapper mapper, Mapper[] secondaryMappers) {
                        // empty
                    }

                    @Override
                    public void storeTime(double time) {
                        // empty
                    }
                }, 0.001, 1.0, 1.0e-10, 1.0e-10) {
            @Override
            public int getOrder() {
                return 2;
            }
        };
        integrator.integrate(ode, 10.0);
        y1 = ode.getCompleteState();
        double tolerance = 1e-5;
        double expectedValue = Math.sin(2 * Math.PI);
        assertEquals(y1[0], expectedValue, tolerance);
        assertEquals(y1[1], expectedValue * 2 * Math.PI, tolerance);

        // Test a function with a discontinuous solution
        ode = new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return y0.length;
            }

            @Override
            public int getOrder() {
                return 2;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                if (t > 1.0) {
                    yDot[0] = 1.0;
                    yDot[1] = 0.0;
                } else {
                    yDot[0] = 0.0;
                    yDot[1] = 1.0;
                }
            }

            @Override
            public double getTime() {
                return 0.0;
            }

            @Override
            public void setTime(double t) {
                // empty
            }
        };
        integrator = new EmbeddedRungeKuttaIntegrator("Test method", false, new double[] { 0.3, 0.5, 0.2 },
                new double[][] { { 0.3, 0.4, 0.2 }, { 0.2, 0.2, 0.4 }, { 0.6, 0.1, 0.1 } }, new double[] { 0.3, 0.4 },
                new RungeKuttaStepInterpolator() {
                    @Override
                    public double interpolate(double t, double[] y, double[] yDot) {
                        // empty
                    }

                    @Override
                    public void reinitialize(EmbeddedRungeKuttaIntegrator integrator, double[] yDotTmp, double[][] yDotK,
                                             boolean forward, Mapper mapper, Mapper[] secondaryMappers) {
                        // empty
                    }

                    @Override
                    public void storeTime(double time) {
                        // empty
                    }
                }, 0.001, 1.0, 1.0e-10, 1.0e-10) {
            @Override
            public int getOrder() {
                return 2;
            }
        };
        integrator.integrate(ode, 2.0);
        y1 = ode.getCompleteState();
        assertEquals(y1[0], 1.0, 1e-10);
        assertEquals(y1[1], 0.0, 1e-10);
    }
}