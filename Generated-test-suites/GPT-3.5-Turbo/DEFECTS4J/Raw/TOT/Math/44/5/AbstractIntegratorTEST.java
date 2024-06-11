import static org.junit.Assert.*;

import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.exception.MaxCountExceededException;
import org.apache.commons.math.exception.MathIllegalStateException;
import org.apache.commons.math.exception.MathIllegalArgumentException;
import org.apache.commons.math.ode.AbstractIntegrator;
import org.apache.commons.math.ode.ExpandableStatefulODE;
import org.apache.commons.math.ode.FirstOrderDifferentialEquations;
import org.junit.Before;
import org.junit.Test;

public class AbstractIntegratorTest {
    
    private static final double TOLERANCE = 1e-8;

    private TestIntegrator integrator;
    private TestODE ode;
    
    @Before
    public void setUp() {
        integrator = new TestIntegrator();
        ode = new TestODE();
    }
    
    @Test
    public void testIntegrate_TypicalUseCase() throws MathIllegalStateException, MathIllegalArgumentException {
        double t0 = 0.0;
        double[] y0 = new double[] {1.0, 2.0, 3.0};
        double t = 10.0;
        double[] y = new double[ode.getDimension()];
        
        integrator.integrate(ode, t0, y0, t, y);
        
        assertEquals(100.0, y[0], TOLERANCE);
        assertEquals(200.0, y[1], TOLERANCE);
        assertEquals(300.0, y[2], TOLERANCE);
    }
    
    @Test(expected=DimensionMismatchException.class)
    public void testIntegrate_IncompatibleDimension() throws MathIllegalStateException, MathIllegalArgumentException {
        double t0 = 0.0;
        double[] y0 = new double[] {1.0};
        double t = 10.0;
        double[] y = new double[ode.getDimension()];
        
        integrator.integrate(ode, t0, y0, t, y);
    }
    
    @Test(expected=MathIllegalStateException.class)
    public void testIntegrate_IllegalStateException() throws MathIllegalStateException, MathIllegalArgumentException {
        double t0 = 0.0;
        double[] y0 = new double[] {1.0, 2.0, 3.0};
        double t = 10.0;
        double[] y = new double[ode.getDimension()];
        
        integrator.setFailIntegration(true);
        integrator.integrate(ode, t0, y0, t, y);
    }
    
    @Test(expected=MathIllegalArgumentException.class)
    public void testIntegrate_IllegalArgumentException() throws MathIllegalStateException, MathIllegalArgumentException {
        double t0 = 0.0;
        double[] y0 = new double[] {1.0, 2.0, 3.0};
        double t = -10.0;
        double[] y = new double[ode.getDimension()];
        
        integrator.integrate(ode, t0, y0, t, y);
    }
    
    private static class TestIntegrator extends AbstractIntegrator {

        private boolean failIntegration;
        
        public TestIntegrator() {
            super("Test Integrator");
            failIntegration = false;
        }
        
        public void setFailIntegration(boolean fail) {
            failIntegration = fail;
        }
        
        @Override
        public void integrate(ExpandableStatefulODE equations, double t)
                throws MathIllegalStateException, MathIllegalArgumentException {
            double[] y = equations.getPrimaryState();
            y[0] = 100.0;
            y[1] = 200.0;
            y[2] = 300.0;
            
            if (failIntegration) {
                throw new MathIllegalStateException();
            }
        }
        
    }
    
    private static class TestODE implements FirstOrderDifferentialEquations {

        @Override
        public int getDimension() {
            return 3;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yDot)
                throws MaxCountExceededException, DimensionMismatchException {
            yDot[0] = 0.0;
            yDot[1] = 0.0;
            yDot[2] = 0.0;
        }
        
    }

}