import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class RungeKuttaIntegratorTest {
    private static final double[] C = {1./5., 3./10., 3./5., 1., 7./8.};
    private static final double[][] A = {
        {1./5.},
        {3./40., 9./40.},
        {3./10., -9./10., 6./5.},
        {-11./54., 5./2., -70./27., 35./27.},
        {1631./55296., 175./512., 575./13824., 44275./110592., 253./4096.}
    };
    private static final double[] B = {37./378., 0., 250./621., 125./594., 0., 512./1771.};
    
    private static final double[] y0 = {1., 2., 3.};
    private static final double t0 = 0.;
    
    private static final double t = 10.;
    
    @Test
    public void testIntegrate() throws DerivativeException, IntegratorException {
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator();
        double step = 0.1;
        
        RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("Test", C, A, B, prototype, step) {
            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) throws DerivativeException {
                for (int i = 0; i < y.length; i++) {
                    yDot[i] = y[i] * t;
                }
            }
        };
        
        double[] y = new double[y0.length];
        double stopTime = integrator.integrate(new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return y0.length;
            }
            
            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) throws DerivativeException {
                for (int i = 0; i < y.length; i++) {
                    yDot[i] = y[i] * t;
                }
            }
        }, t0, y0, t, y);
        
        assertEquals(6801./640., y[0], 1e-10);
        assertEquals(4682./105., y[1], 1e-10);
        assertEquals(8406./128., y[2], 1e-10);
        assertEquals(t, stopTime, 1e-10);
    }
    
    @Test
    public void testIntegrate_nullDerivatives() throws DerivativeException, IntegratorException {
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator();
        double step = 0.1;
        
        RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("Test", C, A, B, prototype, step) {
            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) throws DerivativeException {}
        };
        
        double[] y = new double[y0.length];
        double stopTime = integrator.integrate(new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return y0.length;
            }
            
            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) throws DerivativeException {}
        }, t0, y0, t, y);
        
        assertNull(y);
        assertEquals(t, stopTime, 1e-10);
    }
}