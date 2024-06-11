import org.junit.Test;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testGetSafety() {
        EmbeddedRungeKuttaIntegrator integrator = new MyIntegrator();
        assertEquals(0.9, integrator.getSafety(), 0.0);
    }

    @Test
    public void testSetSafety() {
        EmbeddedRungeKuttaIntegrator integrator = new MyIntegrator();
        integrator.setSafety(0.8);
        assertEquals(0.8, integrator.getSafety(), 0.0);
    }

    @Test
    public void testIntegrate_WithValidParameters() {
        EmbeddedRungeKuttaIntegrator integrator = new MyIntegrator();
        ExpandableStatefulODE equations = new MyODE();
        try {
            integrator.integrate(equations, 1.0);
            assertEquals(1.0, equations.getTime(), 0.0);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testIntegrate_WithInvalidParameters() throws Exception {
        EmbeddedRungeKuttaIntegrator integrator = new MyIntegrator();
        ExpandableStatefulODE equations = new MyODE();
        integrator.integrate(equations, -1.0);
    }

    @Test
    public void testGetMinReduction() {
        EmbeddedRungeKuttaIntegrator integrator = new MyIntegrator();
        assertEquals(0.2, integrator.getMinReduction(), 0.0);
    }

    @Test
    public void testSetMinReduction() {
        EmbeddedRungeKuttaIntegrator integrator = new MyIntegrator();
        integrator.setMinReduction(0.4);
        assertEquals(0.4, integrator.getMinReduction(), 0.0);
    }

    @Test
    public void testGetMaxGrowth() {
        EmbeddedRungeKuttaIntegrator integrator = new MyIntegrator();
        assertEquals(10.0, integrator.getMaxGrowth(), 0.0);
    }

    @Test
    public void testSetMaxGrowth() {
        EmbeddedRungeKuttaIntegrator integrator = new MyIntegrator();
        integrator.setMaxGrowth(12.0);
        assertEquals(12.0, integrator.getMaxGrowth(), 0.0);
    }

    @Test
    public void testEstimateError_WithValidParameters() {
        EmbeddedRungeKuttaIntegrator integrator = new MyIntegrator();
        double[][] yDotK = new double[3][5];
        double[] y2 = new double[5];
        double[] yTmp = new double[5];
        double stepSize = 0.1;
        double error = integrator.estimateError(yDotK, y2, yTmp, stepSize);
        assertEquals(0.0, error, 0.0);
    }
}

class MyIntegrator extends EmbeddedRungeKuttaIntegrator {
    public MyIntegrator() {
        super("", true, new double[]{0.1, 0.2, 0.3}, new double[][]{{0.1, 0.2},{0.3, 0.4}}, new double[]{0.1, 0.2, 0.3}, null, 0.01, 1.0, 0.01, 0.001);
    }

    @Override
    public int getOrder() {
        return 3;
    }

    @Override
    protected double estimateError(double[][] p0, double[] p1, double[] p2, double p3) {
        return 0.0;
    }

    @Override
    protected void computeDerivatives(double v, double[] doubles, double[] doubles1) {

    }
}

class MyODE extends ExpandableStatefulODE {
    @Override
    public double[] getCompleteState() {
        return new double[5];
    }

    @Override
    public void setCompleteState(double[] doubles) {

    }

    @Override
    public void setEquations(FirstOrderDifferentialEquations firstOrderDifferentialEquations) {

    }

    @Override
    public void setTime(double v) {

    }

    @Override
    public double getTime() {
        return 0;
    }

    @Override
    public int getDimension() {
        return 5;
    }
}