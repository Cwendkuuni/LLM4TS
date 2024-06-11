import org.junit.*;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testGetOrder() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("test", true, new double[]{1.0}, new double[][]{{1.0}}, new double[]{1.0}, null, 0.1, 1.0, 1.0, 1.0) {
            @Override
            public int getOrder() {
                return 1;
            }
            @Override
            protected double estimateError(double[][] p0, double[] p1, double[] p2, double p3) {
                return 0.0;
            }
            @Override
            protected void computeDerivatives(double p0, double[] p1, double[] p2) {
                // TODO Auto-generated method stub
            }
        };
        assertEquals(1, integrator.getOrder());
    }

    @Test
    public void testGetSafety() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("test", true, new double[]{1.0}, new double[][]{{1.0}}, new double[]{1.0}, null, 0.1, 1.0, 1.0, 1.0) {
            @Override
            public int getOrder() {
                return 1;
            }
            @Override
            protected double estimateError(double[][] p0, double[] p1, double[] p2, double p3) {
                return 0.0;
            }
            @Override
            protected void computeDerivatives(double p0, double[] p1, double[] p2) {
                // TODO Auto-generated method stub
            }
        };
        assertEquals(0.9, integrator.getSafety(), 0.0);
    }

    @Test
    public void testSetSafety() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("test", true, new double[]{1.0}, new double[][]{{1.0}}, new double[]{1.0}, null, 0.1, 1.0, 1.0, 1.0) {
            @Override
            public int getOrder() {
                return 1;
            }
            @Override
            protected double estimateError(double[][] p0, double[] p1, double[] p2, double p3) {
                return 0.0;
            }
            @Override
            protected void computeDerivatives(double p0, double[] p1, double[] p2) {
                // TODO Auto-generated method stub
            }
        };
        integrator.setSafety(0.8);
        assertEquals(0.8, integrator.getSafety(), 0.0);
    }

    @Test
    public void testGetMinReduction() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("test", true, new double[]{1.0}, new double[][]{{1.0}}, new double[]{1.0}, null, 0.1, 1.0, 1.0, 1.0) {
            @Override
            public int getOrder() {
                return 1;
            }
            @Override
            protected double estimateError(double[][] p0, double[] p1, double[] p2, double p3) {
                return 0.0;
            }
            @Override
            protected void computeDerivatives(double p0, double[] p1, double[] p2) {
                // TODO Auto-generated method stub
            }
        };
        assertEquals(0.2, integrator.getMinReduction(), 0.0);
    }

    @Test
    public void testSetMinReduction() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("test", true, new double[]{1.0}, new double[][]{{1.0}}, new double[]{1.0}, null, 0.1, 1.0, 1.0, 1.0) {
            @Override
            public int getOrder() {
                return 1;
            }
            @Override
            protected double estimateError(double[][] p0, double[] p1, double[] p2, double p3) {
                return 0.0;
            }
            @Override
            protected void computeDerivatives(double p0, double[] p1, double[] p2) {
                // TODO Auto-generated method stub
            }
        };
        integrator.setMinReduction(0.1);
        assertEquals(0.1, integrator.getMinReduction(), 0.0);
    }

    @Test
    public void testGetMaxGrowth() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("test", true, new double[]{1.0}, new double[][]{{1.0}}, new double[]{1.0}, null, 0.1, 1.0, 1.0, 1.0) {
            @Override
            public int getOrder() {
                return 1;
            }
            @Override
            protected double estimateError(double[][] p0, double[] p1, double[] p2, double p3) {
                return 0.0;
            }
            @Override
            protected void computeDerivatives(double p0, double[] p1, double[] p2) {
                // TODO Auto-generated method stub
            }
        };
        assertEquals(10.0, integrator.getMaxGrowth(), 0.0);
    }

    @Test
    public void testSetMaxGrowth() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("test", true, new double[]{1.0}, new double[][]{{1.0}}, new double[]{1.0}, null, 0.1, 1.0, 1.0, 1.0) {
            @Override
            public int getOrder() {
                return 1;
            }
            @Override
            protected double estimateError(double[][] p0, double[] p1, double[] p2, double p3) {
                return 0.0;
            }
            @Override
            protected void computeDerivatives(double p0, double[] p1, double[] p2) {
                // TODO Auto-generated method stub
            }
        };
        integrator.setMaxGrowth(5.0);
        assertEquals(5.0, integrator.getMaxGrowth(), 0.0);
    }

    @Test
    public void testEstimateError() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("test", true, new double[]{1.0}, new double[][]{{1.0}}, new double[]{1.0}, null, 0.1, 1.0, 1.0, 1.0) {
            @Override
            public int getOrder() {
                return 1;
            }
            @Override
            protected double estimateError(double[][] p0, double[] p1, double[] p2, double p3) {
                return 0.5;
            }
            @Override
            protected void computeDerivatives(double p0, double[] p1, double[] p2) {
                // TODO Auto-generated method stub
            }
        };
        double[][] p0 = new double[][]{{1.0}};
        double[] p1 = new double[]{2.0};
        double[] p2 = new double[]{3.0};
        double p3 = 0.2;
        assertEquals(0.5, integrator.estimateError(p0, p1, p2, p3), 0.0);
    }

    @Test
    public void testComputeDerivatives() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("test", true, new double[]{1.0}, new double[][]{{1.0}}, new double[]{1.0}, null, 0.1, 1.0, 1.0, 1.0) {
            @Override
            public int getOrder() {
                return 1;
            }
            @Override
            protected double estimateError(double[][] p0, double[] p1, double[] p2, double p3) {
                return 0.0;
            }
            @Override
            protected void computeDerivatives(double p0, double[] p1, double[] p2) {
                p2[0] = 2.0 * p1[0];
            }
        };
        double p0 = 0.0;
        double[] p1 = new double[]{1.0};
        double[] p2 = new double[]{0.0};
        integrator.computeDerivatives(p0, p1, p2);
        assertEquals(2.0, p2[0], 0.0);
    }

}