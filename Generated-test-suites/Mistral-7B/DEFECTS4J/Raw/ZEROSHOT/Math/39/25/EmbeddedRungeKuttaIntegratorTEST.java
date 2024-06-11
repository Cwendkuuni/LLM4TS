import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testEmbeddedRungeKuttaIntegrator() {
        double[] c = {0.0, 0.23692688505667133, 0.6797959183673469, 0.967252988659237};
        double[][] a = {
            {0.0, 0.0, 0.0, 0.0},
            {0.23692688505667133, 0.873823543521961, 0.0, 0.0},
            {0.6797959183673469, 0.3326743903051242, 0.868183771256356, 0.0},
            {0.967252988659237, 0.162318153252815, 0.275993221010563, 0.6797959183673469}
        };
        double[] b = {0.23692688505667133, 0.6797959183673469, 0.967252988659237};

        EmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator("MyIntegrator", true, c, a, b,
                new RungeKuttaStepInterpolator(), 0.001, 0.01, 1e-10, 1e-10);

        double[] y0 = {0.0, 1.0};
        ExpandableStatefulODE equations = new MyExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public int getStateDimension() {
                return 2;
            }

            @Override
            public int getPrimaryDimension() {
                return 2;
            }

            @Override
            public int getSecondaryDimension() {
                return 0;
            }

            @Override
            public void calculateDerivative(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[1];
                yPrime[1] = -y[0];
            }
        };

        integrator.integrate(equations, 10.0);

        double[] expected = {5.426711750461462, 1.615425507246821};
        assertArrayEquals(expected, equations.getCompleteState(), 0.001);

        double[] yDot = equations.getDerivative(equations.getTime());
        assertEquals(-expected[0], yDot[0], 0.001);
        assertEquals(expected[1], yDot[1], 0.001);
    }

    static class MyEmbeddedRungeKuttaIntegrator extends EmbeddedRungeKuttaIntegrator {
        MyEmbeddedRungeKuttaIntegrator(String name, boolean fsal, double[] c, double[][] a, double[] b,
                RungeKuttaStepInterpolator prototype, double minStep, double maxStep,
                double[] vecAbsoluteTolerance, double[] vecRelativeTolerance) {
            super(name, fsal, c, a, b, prototype, minStep, maxStep, vecAbsoluteTolerance, vecRelativeTolerance);
        }

        @Override
        protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
            double error = 0.0;
            for (int i = 0; i < y1.length; i++) {
                error += Math.pow(Math.abs(y1[i] - y0[i] - h * yDotK[yDotK.length - 1][i]), 2.0);
            }
            return Math.sqrt(error);
        }
    }

    static class MyExpandableStatefulODE implements ExpandableStatefulODE {
        @Override
        public void initialize(double t0, double[] y0) {
            // empty
        }

        @Override
        public double getTime() {
            return 0.0;
        }

        @Override
        public void setTime(double t) {
            // empty
        }

        @Override
        public int getPrimaryMapperIndex(int variable) {
            return 0;
        }

        @Override
        public int getSecondaryMapperIndex(int variable, int secondary) {
            throw new UnsupportedOperationException();
        }

        @Override
        public double[] getPrimaryMapper(int variable) {
            return new double[]{0.0};
        }

        @Override
        public double[] getSecondaryMapper(int variable, int secondary) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void addStateHandler(StateHandler handler) {
            // empty
        }

        @Override
        public void removeStateHandler(StateHandler handler) {
            // empty
        }

        @Override
        public void addEventHandler(EventHandler handler) {
            // empty
        }

        @Override
        public void removeEventHandler(EventHandler handler) {
            // empty
        }

        @Override
        public boolean hasReachedEndEvent() {
            return false;
        }

        @Override
        public void reset() {
            // empty
        }

        @Override
        public int getDimension() {
            return 2;
        }

        @Override
        public int getStateDimension() {
            return 2;
        }

        @Override
        public int getPrimaryDimension() {
            return 2;
        }

        @Override
        public int getSecondaryDimension() {
            return 0;
        }

        @Override
        public void calculateDerivative(double t, double[] y, double[] yPrime) {
            yPrime[0] = y[1];
            yPrime[1] = -y[0];
        }
    }
}