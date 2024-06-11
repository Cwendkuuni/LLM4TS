import org.apache.commons.math.ode.nonstiff.EmbeddedRungeKuttaIntegratorTest;
import org.apache.commons.math.exception.MathIllegalArgumentException;
import org.apache.commons.math.exception.MathIllegalStateException;
import org.apache.commons.math.ode.ExpandableStatefulODE;
import org.apache.commons.math.util.FastMath;

import static org.junit.Assert.*;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {

    private static final double H_START = 0;
    private static final double H_END = 1;
    private static final double H_ABSOLUTE_TOLERANCE = 1e-9;
    private static final double H_RELATIVE_TOLERANCE = 1e-9;
    private static final double[] Y_START = { 0, 1 };

    private EmbeddedRungeKuttaIntegrator integrator;
    private double[] yEnd;

    @Before
    public void setUp() throws Exception {
        integrator = new EmbeddedRungeKuttaIntegrator(H_START, H_END, H_ABSOLUTE_TOLERANCE, H_RELATIVE_TOLERANCE) {
            protected int getOrder() {
                return 8;
            }
            protected double estimateError(double[][] ydotk, double[] y0, double[] y1, double h) {
                // Dummy implementation
                return 0;
            }
            protected void computeDerivatives(double t, double[] y, double[] yDot) {
                // Dummy implementation
            }
        };
    }

    @Test
    public void testIntegrate() throws MathIllegalArgumentException, MathIllegalStateException {
        integrator.integrate(new ExpandableStatefulODE(new SecondOrderDifferentialEquations() {
            public int getDimension() {
                return 2;
            }
            public void computeSecondDerivatives(double t, double[] y, double[] yDDot, double[] yDot) {
                // Dummy implementation
            }
        }), H_START, Y_START, H_END, yEnd);
        assertEquals(Y_START.length, yEnd.length);
    }

}