import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaMethod;
import org.apache.commons.math3.util.Precision;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {

    private static final double ACCURACY = 1e-9;
    private static final double RELATIVE_TOLERANCE = 1e-5;
    private static final double ABSTOL = 1e-10;

    private EmbeddedRungeKuttaIntegrator integrator;
    private ExpandableStatefulODE testODE;

    @Before
    public void setUp() {
        double[] c = {0.0, 0.23, 0.6};
        double[][] a = {{0.0, 0.0, 0.0}, {0.23, 0.648, 0.027}, {0.6, -0.316, 1.0}};
        double[] b = {0.23, 0.6, 0.6};
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(3);
        integrator = new EmbeddedRungeKuttaMethod("RK4", true, c, a, b, prototype, 1e-6, 1e-6, ABSTOL, RELATIVE_TOLERANCE) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                return (y1[0] - y0[0]) / h;
            }
        };
        testODE = new TestODE();
    }

    @Test
    public void testIntegration() throws Exception {
        double start = 0.0;
        double end = 5.0;
        double[] y0 = new double[]{0.0, 1.0};
        double[] yExpect = new double[]{3.732050807568877, 2.9682535619729475};

        integrator.integrate(testODE, end);
        double[] y = testODE.getCompleteState();
        assertArrayEquals(yExpect, y, ACCURACY);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testNegativeStepSize() {
        integrator.setMinStep(-1.0);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxCountExceeded() throws Exception {
        double[] y0 = new double[]{0.0, 0.0};
        double start = 0.0;
        double end = 1.0;
        integrator.integrate(testODE, end);
        integrator.integrate(testODE, end);
        integrator.integrate(testODE, end);
        integrator.integrate(testODE, end);
    }

    @Test
    public void testStepControl() throws Exception {
        double start = 0.0;
        double end = 5.0;
        double[] y0 = new double[]{0.0, 1.0};
        double[] yExpect = new double[]{0.3670866462599655, 0.4679245387463165};
        integrator.setSafety(0.5);
        integrator.setMinReduction(0.1);

        integrator.integrate(testODE, end);
        double[] y = testODE.getCompleteState();
        assertArrayEquals(yExpect, y, ACCURACY);

        // check that the step size was adjusted
        assertEquals(0.5 * 0.3670866462599655, integrator.getStepSize(), ACCURACY);

        // integrate again, this time without adjusting the step size
        integrator.setSafety(1.0);
        integrator.integrate(testODE, end);
        yExpect = new double[]{1.680173138145173, 1.984186535313464};
        y = testODE.getCompleteState();
        assertArrayEquals(yExpect, y, ACCURACY);
        assertEquals(0.3670866462599655, integrator.getStepSize(), ACCURACY);
    }

}

class TestODE implements ExpandableStatefulODE {

    private double time;

    @Override
    public int getDimension() {
        return 2;
    }

    @Override
    public void setTime(double time) {
        this.time = time;
    }

    @Override
    public double getTime() {
        return time;
    }

    @Override
    public void setCompleteState(double[] state) {
        // empty method
    }

    @Override
    public double[] getCompleteState() {
        double[] y = new double[2];
        y[0] = time;
        y[1] = Math.exp(time);
        return y;
    }

    @Override
    public void setPrimaryMapper(String name, int dimension, int index) {
        // empty method
    }

    @Override
    public void setSecondaryMappers(String[] names, int size) {
        // empty method
    }

    @Override
    public void addEvent(double time, int type, Object userObject) {
        // empty method
    }

    @Override
    public void removeEvents(int start, int end) {
        // empty method
    }

    @Override
    public void addStepHandler(int index, ExpandableODEHandler handler) {
        // empty method
    }

    @Override
    public void removeStepHandlers(int start, int end) {
        // empty method
    }

    @Override
    public double[] derivatives(double[] state) {
        double[] y = state;
        double[] yDot = new double[2];
        yDot[0] = y[1];
        yDot[1] = y[1] * y[1];
        return yDot;
    }

}