import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupyter.annotations.DisplayName;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class EmbeddedRungeKuttaIntegratorTest {

    private static final double TOL = 0.001;
    private static final double ACCURACY = 1e-12;
    private static final double INITIAL_TIME = 0.0;
    private static final double FINAL_TIME = 1.0;
    private static final double[] INITIAL_STATE = {0.0, 1.0};

    private ExpandableStatefulODE equations;
    private EmbeddedRungeKuttaIntegrator integrator;
    private RungeKuttaStepInterpolator interpolator;

    @Before
    public void setUp() {
        equations = new MyExpandableStatefulODE();
        integrator = new MyEmbeddedRungeKuttaIntegrator(1, new double[]{-0.236926885, 0.275},
                new double[][]{
                        {0.0, 0.0},
                        {0.659218748, 0.0},
                        {0.360761573, 0.6561687},
                        {0.0, 0.316227766}
                }, new double[]{0.236926885, 0.659218748},
                new MyRungeKuttaStepInterpolator(), 0.001, 0.01, null, null);
        interpolator = (RungeKuttaStepInterpolator) integrator.prototype.copy();
    }

    @Test
    @DisplayName("Test EmbeddedRungeKuttaIntegrator with initial state and time 0.0")
    public void testEmbeddedRungeKuttaIntegratorWithInitialStateAndTimeZero() {
        integrator.integrate(equations, INITIAL_TIME);
        assertNotNull(equations.getCompleteState());
        assertEquals(INITIAL_STATE, equations.getCompleteState(), TOL);
    }

    @Test
    @DisplayName("Test EmbeddedRungeKuttaIntegrator from 0.0 to 1.0")
    public void testEmbeddedRungeKuttaIntegratorFromZeroToOne() {
        integrator.integrate(equations, FINAL_TIME);
        assertNotNull(equations.getCompleteState());
        assertEquals(new double[]{0.918120041, 1.836240133}, equations.getCompleteState(), ACCURACY);
    }

    @Test
    @DisplayName("Test EmbeddedRungeKuttaIntegrator with wrong time")
    public void testEmbeddedRungeKuttaIntegratorWithWrongTime() {
        equations.setTime(INTEGER.MIN_VALUE);
        try {
            integrator.integrate(equations, equations.getTime());
            fail("Expected MathIllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // expected
        }
    }

    @Test
    @DisplayName("Test EmbeddedRungeKuttaIntegrator with wrong number of dimensions")
    public void testEmbeddedRungeKuttaIntegratorWithWrongNumberOfDimensions() {
        equations.setPrimaryMapper(null);
        try {
            integrator.integrate(equations, equations.getTime());
            fail("Expected MathIllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // expected
        }
    }

    @Test
    @DisplayName("Test EmbeddedRungeKuttaIntegrator with not enough secondary mappers")
    public void testEmbeddedRungeKuttaIntegratorWithNotEnoughSecondaryMappers() {
        equations.setPrimaryMapper(null);
        equations.setSecondaryMappers(new Object[0]);
        try {
            integrator.integrate(equations, equations.getTime());
            fail("Expected MathIllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // expected
        }
    }

    @Test
    @DisplayName("Test EmbeddedRungeKuttaIntegrator with wrong step size")
    public void testEmbeddedRungeKuttaIntegratorWithWrongStepSize() {
        try {
            integrator.setStepSize(INTEGER.MIN_VALUE);
            fail("Expected MathIllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // expected
        }
    }

    @Test
    @DisplayName("Test EmbeddedRungeKuttaIntegrator with wrong tolerance")
    public void testEmbeddedRungeKuttaIntegratorWithWrongTolerance() {
        integrator.setSafety(1.1);
        try {
            integrator.integrate(equations, FINAL_TIME);
            fail("Expected MathIllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // expected
        }
    }

    private static class MyExpandableStatefulODE extends ExpandableStatefulODE {
        @Override
        public int getPrimaryDimension() {
            return 2;
        }

        @Override
        public int getSecondaryDimension() {
            return 0;
        }

        @Override
        public int getOrder() {
            return 1;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yDot) {
            yDot[0] = y[1];
            yDot[1] = 2 * y[1];
        }

        @Override
        public void initialize(double t0, double[] y0) {
            setTime(t0);
            setCompleteState(y0);
        }

        @Override
        public void map(double t, double[] y, double[] yMap) {
            System.arraycopy(y, 0, yMap, 0, y.length);
        }

        @Override
        public void mapBackward(double t, double[] yMap, double[] y) {
            System.arraycopy(yMap, 0, y, 0, y.length);
        }

        @Override
        public double getTime() {
            return getTime();
        }

        @Override
        public void setTime(double time) {
            super.setTime(time);
        }
    }

    private static class MyRungeKuttaStepInterpolator extends RungeKuttaStepInterpolator {
        @Override
        public double interpolate(double[][] yDot, int[] k, double h) {
            double sum = 0;
            for (int i = 0; i < k.length; ++i) {
                sum += b[i] * yDot[i];
            }
            return sum * h;
        }
    }

}