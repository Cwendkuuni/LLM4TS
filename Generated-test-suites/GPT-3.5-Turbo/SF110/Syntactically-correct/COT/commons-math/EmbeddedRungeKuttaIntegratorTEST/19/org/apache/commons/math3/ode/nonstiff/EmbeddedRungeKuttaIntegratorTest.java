
import org.junit.Test;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.exception.util.*;
import org.apache.commons.math3.ode.nonstiff.*;
import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.sampling.*;
import org.apache.commons.math3.util.*;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {
    
    @Test
    public void testGetOrder() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        assertEquals(0, integrator.getOrder());
    }
    
    @Test
    public void testGetSafety() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        assertEquals(0.9, integrator.getSafety(), 0.0);
    }
    
    @Test
    public void testSetSafety() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        integrator.setSafety(0.8);
        assertEquals(0.8, integrator.getSafety(), 0.0);
    }
    
    @Test
    public void testIntegrate() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        ExpandableStatefulODE equations = new ExpandableStatefulODE(new FirstOrderDifferentialEquations() {
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = 2 * y[0];
            }
            public int getDimension() { return 1; }
        });
        double t = 0.0;
        integrator.integrate(equations, t);
        assertEquals(2.0, equations.getCompleteState()[0], 0.0);
    }
    
    @Test(expected = NumberIsTooSmallException.class)
    public void testIntegrateException1() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        ExpandableStatefulODE equations = new ExpandableStatefulODE(new FirstOrderDifferentialEquations() {
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = 2 * y[0];
            }
            public int getDimension() { return 1; }
        });
        double t = 0.0;
        integrator.integrate(equations, t - 1.0); // Throws NumberIsTooSmallException
    }
    
    @Test(expected = DimensionMismatchException.class)
    public void testIntegrateException2() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        ExpandableStatefulODE equations = new ExpandableStatefulODE(new FirstOrderDifferentialEquations() {
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = 2 * y[0];
            }
            public int getDimension() { return 2; }
        });
        double t = 0.0;
        integrator.integrate(equations, t); // Throws DimensionMismatchException
    }
    
    private class TestEmbeddedRungeKuttaIntegrator extends EmbeddedRungeKuttaIntegrator {
        public TestEmbeddedRungeKuttaIntegrator() {
            super("Test", true, new double[]{0}, new double[][]{{0}}, new double[]{1}, null, 0.0, 0.0, 0.0, 0.0);
        }
        protected double estimateError(double[][] p0, double[] p1, double[] p2, double p3) {
            return 0.0;
        }
        public int getOrder() { return 0; }
        public void computeDerivatives(double t, double[] y, double[] yDot) {}
    }
    
}
