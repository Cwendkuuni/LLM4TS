import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testConstructor_withMinimalParameters() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK4", false,
                new double[]{0.25, 0.25, 0.5, 0.25},
                new double[][]{{0.25, 0}, {0, 0.25}, {0.682609435985521, 0.05}, {0.8333333333333333, -0.1666666666666667}},
                new double[]{0.25, 0.25, 0.5, 0.25},
                new RungeKuttaStepInterpolator() {
                    @Override
                    public void reinitialize(EmbeddedRungeKuttaIntegrator integrator, double[] y, double[] yDot, boolean forward,
                                             ExpandableStatefulODE equations, double[][] secondaryMappers) {
                        // empty implementation
                    }

                    @Override
                    public void storeTime(double time) {
                        // empty implementation
                    }
                });
        assertEquals("RK4", integrator.getName());
        assertEquals(4, integrator.getOrder());
        assertArrayEquals(new double[]{0.25, 0.25, 0.5, 0.25}, integrator.getC());
        assertArrayEquals(new double[][]{{0.25, 0}, {0, 0.25}, {0.682609435985521, 0.05}, {0.8333333333333333, -0.1666666666666667}}, integrator.getA());
        assertArrayEquals(new double[]{0.25, 0.25, 0.5, 0.25}, integrator.getB());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_withIllegalParameter() {
        new EmbeddedRungeKuttaIntegrator("RK4", false,
                new double[]{0.25, 0.25, 0.5, 0.25},
                new double[][]{{0.25, 0}, {0, 0.25}, {0.682609435985521, 0.05}, null},
                new double[]{0.25, 0.25, 0.5, 0.25},
                new RungeKuttaStepInterpolator() {});
    }

    @Test
    public void testIntegrate_withValidParameters() throws MathIllegalStateException, MathIllegalArgumentException {
        double[] initialState = new double[]{0.0, 1.0};
        double  timeStart      = 0.0;
        double  timeEnd        = 10.0;

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK4", false,
                new double[]{0.25, 0.25, 0.5, 0.25},
                new double[][]{{0.25, 0}, {0, 0.25}, {0.682609435985521, 0.05}, {0.8333333333333333, -0.1666666666666667}},
                new double[]{0.25, 0.25, 0.5, 0.25},
                new RungeKuttaStepInterpolator() {
                    @Override
                    public void reinitialize(EmbeddedRungeKuttaIntegrator integrator, double[] y, double[] yDot, boolean forward,
                                             ExpandableStatefulODE equations, double[][] secondaryMappers) {
                        // empty implementation
                    }

                    @Override
                    public void storeTime(double time) {
                        // empty implementation
                    }
                });

        ExpandableStatefulODE equations = new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return initialState.length;
            }

            @Override
            public int getPrimaryDimension() {
                return initialState.length;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                for (int i = 0; i < y.length; ++i) {
                    yDot[i] = 2 * y[i];
                }
            }

            @Override
            public double getTime() {
                return timeStart;
            }

            @Override
            public void setTime(double time) {
                timeStart = time;
            }

            @Override
            public void setCompleteState(double[] state) {
                System.arraycopy(state, 0, initialState, 0, initialState.length);
            }
        };

        integrator.integrate(equations, timeEnd);

        double[] expectedFinalState = new double[]{FastMath.exp(2.0 * timeEnd), FastMath.exp(2.0 * timeEnd)};
        assertArrayEquals(expectedFinalState, equations.getCompleteState());
    }

    @Test(expected = IllegalStateException.class)
    public void testIntegrate_withIllegalState() throws MathIllegalStateException, MathIllegalArgumentException {
        double[] initialState = new double[]{0.0, 1.0};
        double  timeStart      = 0.0;
        double  timeEnd        = 10.0;

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK4", false,
                new double[]{0.25, 0.25, 0.5, 0.25},
                new double[][]{{0.25, 0}, {0, 0.25}, {0.682609435985521, 0.05}, {0.8333333333333333, -0.1666666666666667}},
                new double[]{0.25, 0.25, 0.5, 0.25},
                new RungeKuttaStepInterpolator() {
                    @Override
                    public void reinitialize(EmbeddedRungeKuttaIntegrator integrator, double[] y, double[] yDot, boolean forward,
                                             ExpandableStatefulODE equations, double[][] secondaryMappers) {
                        // empty implementation
                    }

                    @Override
                    public void storeTime(double time) {
                        // empty implementation
                    }
                });

        ExpandableStatefulODE equations = new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return initialState.length;
            }

            @Override
            public int getPrimaryDimension() {
                return initialState.length;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                throw new UnsupportedOperationException();
            }

            @Override
            public double getTime() {
                return timeStart;
            }

            @Override
            public void setTime(double time) {
                timeStart = time;
            }

            @Override
            public void setCompleteState(double[] state) {
                System.arraycopy(state, 0, initialState, 0, initialState.length);
            }
        };

        integrator.integrate(equations, timeEnd);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntegrate_withIllegalArgument() throws MathIllegalStateException, MathIllegalArgumentException {
        double[] initialState = new double[]{0.0, 1.0};
        double  timeStart      = 0.0;
        double  timeEnd        = 10.0;

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK4", false,
                new double[]{0.25, 0.25, 0.5, 0.25},
                new double[][]{{0.25, 0}, {0, 0.25}, {0.682609435985521, 0.05}, {0.8333333333333333, -0.1666666666666667}},
                new double[]{0.25, 0.25, 0.5, 0.25},
                new RungeKuttaStepInterpolator() {
                    @Override
                    public void reinitialize(EmbeddedRungeKuttaIntegrator integrator, double[] y, double[] yDot, boolean forward,
                                             ExpandableStatefulODE equations, double[][] secondaryMappers) {
                        // empty implementation
                    }

                    @Override
                    public void storeTime(double time) {
                        // empty implementation
                    }
                });

        ExpandableStatefulODE equations = new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return initialState.length;
            }

            @Override
            public int getPrimaryDimension() {
                return initialState.length;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                // empty implementation
            }

            @Override
            public double getTime() {
                return timeStart;
            }

            @Override
            public void setTime(double time) {
                timeStart = time;
            }

            @Override
            public void setCompleteState(double[] state) {
                System.arraycopy(state, 0, initialState, 0, initialState.length);
            }
        };

        integrator.integrate(equations, null);
    }

}