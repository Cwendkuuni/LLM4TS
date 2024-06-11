import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.sampling.DummyStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.ode.sampling.StepNormalizer;
import org.apache.commons.math3.util.FastMath;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class EmbeddedRungeKuttaIntegratorTest {

    private static final double EPSILON = 1e-10;

    private EmbeddedRungeKuttaIntegrator integrator;

    @Before
    public void setUp() throws Exception {
        this.integrator = new TestIntegrator();
    }

    @After
    public void tearDown() throws Exception {
        this.integrator = null;
    }

    @Test
    public void testIntegrate() {
        final double[] y0 = new double[2];
        y0[0] = 0.5;
        y0[1] = 1.5;
        final double t = 10.0;
        final TestExpandableStatefulODE equations = new TestExpandableStatefulODE(y0);
        equations.setTime(0.0);
        equations.addStepHandler(new TestStepHandler());
        try {
            integrator.integrate(equations, t);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Assert.assertEquals(t, equations.getTime(), EPSILON);
        final double[] y = equations.getCompleteState();
        Assert.assertEquals(0.0, y[0], EPSILON);
        Assert.assertEquals(0.0, y[1], EPSILON);
    }

    private class TestIntegrator extends EmbeddedRungeKuttaIntegrator {

        public TestIntegrator() {
            super("TestIntegrator", true, null, null, null, null, 0.0,
                    1.0, 1.0, 1.0);
        }

        @Override
        public int getOrder() {
            return 4;
        }

        @Override
        protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double dt) {
            return 0; // to be implemented
        }

        @Override
        protected void computeDerivatives(double t, double[] y, double[] yDot) {
            // do nothing, this is a test integrator
        }
    }

    private class TestExpandableStatefulODE extends ExpandableStatefulODE {

        private final double[] initialState;

        public TestExpandableStatefulODE(final double[] y0) {
            super(new TestEquations(), new double[y0.length]);
            this.initialState = y0.clone();
            reset();

        }

        @Override
        public void reset() {
            super.reset();
            System.arraycopy(initialState, 0, currentState, 0, initialState.length);
        }
    }

    private class TestEquations implements FirstOrderDifferentialEquations {

        @Override
        public int getDimension() {
            return 2;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yDot) {
            yDot[0] = -y[0];
            yDot[1] = -y[1];
        }
    }

    private class TestStepHandler implements StepHandler {

        private final List<Double> timePoints;
        private final List<double[]> states;

        public TestStepHandler() {
            this.timePoints = new ArrayList<Double>();
            this.states = new ArrayList<double[]>();
        }

        @Override
        public void init(double t0, double[] y0, double t) {
            this.timePoints.clear();
            this.states.clear();
            storeStep(t0, y0);
        }

        @Override
        public void handleStep(StepInterpolator interpolator, boolean isLast) {

            final double t = interpolator.getCurrentTime();
            final double[] y = interpolator.getInterpolatedState();

            storeStep(t, y);
        }

        private void storeStep(final double t, final double[] y) {
            timePoints.add(t);
            states.add(y.clone());
        }
    }
}