import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {

    private EmbeddedRungeKuttaIntegrator integrator;

    @Before
    public void setUp() {
        integrator = new EmbeddedRungeKuttaIntegrator("test", true, new double[]{}, new double[][]{}, new double[]{}, null, 0.0, 0.0, 0.0, 0.0) {
            @Override
            public int getOrder() {
                return 0;
            }

            @Override
            protected double estimateError(double[][] p0, double[] p1, double[] p2, double p3) {
                return 0;
            }

            @Override
            protected void computeDerivatives(double v, double[] doubles, double[] doubles1) {

            }
        };
    }

    @Test
    public void testGetSafety() {
        assertEquals(0.9, integrator.getSafety(), 0.0);
    }

    @Test
    public void testSetSafety() {
        integrator.setSafety(0.8);
        assertEquals(0.8, integrator.getSafety(), 0.0);
    }

    @Test
    public void testIntegrate() {
        ExpandableStatefulODE equations = new ExpandableStatefulODE();
        double t = 0.0;
        try {
            integrator.integrate(equations, t);
        } catch (NumberIsTooSmallException | DimensionMismatchException | MaxCountExceededException | NoBracketingException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testGetMinReduction() {
        assertEquals(0.2, integrator.getMinReduction(), 0.0);
    }

    @Test
    public void testSetMinReduction() {
        integrator.setMinReduction(0.1);
        assertEquals(0.1, integrator.getMinReduction(), 0.0);
    }

    @Test
    public void testGetMaxGrowth() {
        assertEquals(10.0, integrator.getMaxGrowth(), 0.0);
    }

    @Test
    public void testSetMaxGrowth() {
        integrator.setMaxGrowth(15.0);
        assertEquals(15.0, integrator.getMaxGrowth(), 0.0);
    }

    @Test
    public void testEstimateError() {
        double[][] p0 = new double[][]{{}};
        double[] p1 = new double[]{};
        double[] p2 = new double[]{};
        double p3 = 0.0;
        assertEquals(0.0, integrator.estimateError(p0, p1, p2, p3), 0.0);
    }

}