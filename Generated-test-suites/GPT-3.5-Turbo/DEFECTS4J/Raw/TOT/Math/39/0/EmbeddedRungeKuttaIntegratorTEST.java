import org.apache.commons.math.exception.MathIllegalArgumentException;
import org.apache.commons.math.exception.MathIllegalStateException;
import org.apache.commons.math.ode.ExpandableStatefulODE;
import org.apache.commons.math.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math.util.FastMath;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {

    class TestODE implements ExpandableStatefulODE {
        private double time;
        private double[] state;

        public TestODE(double time, double[] state) {
            this.time = time;
            this.state = state;
        }

        public double getTime() {
            return time;
        }

        public void setTime(double time) {
            this.time = time;
        }

        public void setCompleteState(double[] state) {
            this.state = state;
        }

        public double[] getCompleteState() {
            return state;
        }

        @Override
        public int getDimension() {
            return state.length;
        }

        @Override
        public double[] getPrimaryState() {
            return state;
        }

        @Override
        public double[][] getSecondaryState() {
            return null;
        }

        @Override
        public double[] getCompleteDerivative() {
            return null;
        }

        @Override
        public double[][] getCompleteSecondaryDerivative() {
            return null;
        }

        @Override
        public double getTimeDerivative() {
            return 0;
        }

        @Override
        public String getName(int index) {
            return null;
        }
    }

    class TestIntegrator extends EmbeddedRungeKuttaIntegrator {

        public TestIntegrator() {
            super("TestIntegrator", true, new double[]{}, new double[][]{}, new double[]{}, null, 0, 0, 0, 0);
        }

        @Override
        protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
            return 0;
        }

        @Override
        protected void computeDerivatives(double v, double[] doubles, double[] doubles1) throws MathIllegalArgumentException, MathIllegalStateException {

        }

        @Override
        public int getOrder() {
            return 0;
        }
    }

    @Test
    public void testIntegrate() throws MathIllegalStateException, MathIllegalArgumentException {
        TestODE ode = new TestODE(0, new double[]{0, 0});
        TestIntegrator integrator = new TestIntegrator();

        double t = 10;

        integrator.integrate(ode, t);

        double[] result = ode.getCompleteState();

        // verify the result
        // ...

    }

}