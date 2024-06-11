
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RungeKuttaIntegratorTest {

    private RungeKuttaIntegrator integrator;
    private FirstOrderDifferentialEquations equations;
    private double t;
    private double[] y;
    
    @Before
    public void setUp() {
        // initialize integrator, equations, t, and y
        integrator = new RungeKuttaIntegratorStub();
        equations = new FirstOrderDifferentialEquationsStub();
        t = 0.0;
        y = new double[] {1.0, 2.0, 3.0};
    }

    @Test
    public void testIntegrate() {
        double tEnd = 10.0;
        double[] yEnd = new double[] {11.0, 12.0, 13.0};
        
        double result = integrator.integrate(equations, t, y, tEnd, yEnd);
        
        // assert expected result
        Assert.assertEquals(tEnd, result, 0.001);
        
        // assert final state yEnd
        Assert.assertArrayEquals(yEnd, y, 0.001);
    }
    
    @Test(expected = DerivativeException.class)
    public void testIntegrateWithDerivativeException() {
        double tEnd = 10.0;
        double[] yEnd = new double[] {11.0, 12.0, 13.0};
        
        // throw derivative exception
        integrator.setThrowDerivativeException(true);
        
        integrator.integrate(equations, t, y, tEnd, yEnd);
    }
    
    @Test(expected = IntegratorException.class)
    public void testIntegrateWithIntegratorException() {
        double tEnd = 10.0;
        double[] yEnd = new double[] {11.0, 12.0, 13.0};
        
        // throw integrator exception
        integrator.setThrowIntegratorException(true);
        
        integrator.integrate(equations, t, y, tEnd, yEnd);
    }
    
    // Additional test cases for edge cases and exception handling scenarios
    
    @Test(expected = IllegalArgumentException.class)
    public void testIntegrateWithNegativeStep() {
        double tEnd = 10.0;
        double[] yEnd = new double[] {11.0, 12.0, 13.0};
        
        integrator.integrate(equations, t, y, tEnd, yEnd, -1.0);
    }
    
    @Test
    public void testIntegrateWithZeroStep() {
        double tEnd = 10.0;
        double[] yEnd = new double[] {11.0, 12.0, 13.0};
        
        double result = integrator.integrate(equations, t, y, tEnd, yEnd, 0.0);
        
        // assert no integration is performed
        Assert.assertEquals(t, result, 0.001);
        
        // assert initial state y
        Assert.assertArrayEquals(new double[] {1.0, 2.0, 3.0}, y, 0.001);
    }
    
    @Test
    public void testIntegrateWithEmptyStepHandlers() {
        double tEnd = 10.0;
        double[] yEnd = new double[] {11.0, 12.0, 13.0};
        
        integrator.setIntegratorHandlers(new StepHandler[0]);
        
        double result = integrator.integrate(equations, t, y, tEnd, yEnd);
        
        // assert expected result
        Assert.assertEquals(tEnd, result, 0.001);
        
        // assert final state yEnd
        Assert.assertArrayEquals(yEnd, y, 0.001);
    }
    
    @Test
    public void testIntegrateWithResettingStepHandler() {
        double tEnd = 10.0;
        double[] yEnd = new double[] {11.0, 12.0, 13.0};
        
        StepHandlerStub handler = new StepHandlerStub();
        integrator.setIntegratorHandlers(new StepHandler[] {handler});
        
        double result = integrator.integrate(equations, t, y, tEnd, yEnd);
        
        // assert expected result
        Assert.assertEquals(tEnd, result, 0.001);
        
        // assert final state yEnd
        Assert.assertArrayEquals(yEnd, y, 0.001);
        
        // assert handler is reset
        Assert.assertTrue(handler.isResetCalled());
    }

    // Stub class for RungeKuttaIntegrator
    
    private class RungeKuttaIntegratorStub extends RungeKuttaIntegrator {
        
        private boolean throwDerivativeException;
        private boolean throwIntegratorException;

        public RungeKuttaIntegratorStub() {
            super("Name", null, null, null, null, 0);
        }

        @Override
        public double integrate(FirstOrderDifferentialEquations equations, double t0, double[] y0, double t, double[] y)
                throws DerivativeException, IntegratorException {
            
            if (throwDerivativeException) {
                throw new DerivativeException("Derivative Exception");
            }
            
            if (throwIntegratorException) {
                throw new IntegratorException("Integrator Exception");
            }
            
            // perform integration
            y[0] = t;
            y[1] = t + 1;
            y[2] = t + 2;
            
            return tEnd;
        }
        
        public void setThrowDerivativeException(boolean throwDerivativeException) {
            this.throwDerivativeException = throwDerivativeException;
        }
        
        public void setThrowIntegratorException(boolean throwIntegratorException) {
            this.throwIntegratorException = throwIntegratorException;
        }
        
        public void setIntegratorHandlers(StepHandler[] handlers) {
            this.stepHandlers = handlers;
        }

    }
    
    // Stub class for FirstOrderDifferentialEquations
    
    private class FirstOrderDifferentialEquationsStub implements FirstOrderDifferentialEquations {

        public int getDimension() {
            return 3;
        }

        public void computeDerivatives(double t, double[] y, double[] yDot)
                throws DerivativeException {

            yDot[0] = y[0];
            yDot[1] = y[1];
            yDot[2] = y[2];
        }
        
    }
    
    // Stub class for StepHandler
    
    private class StepHandlerStub implements StepHandler {
        
        private boolean resetCalled = false;

        public void handleStep(StepInterpolator interpolator, boolean isLast) {
            // do nothing
        }

        public void init(double t0, double[] y0, double t) {
            // do nothing
        }

        public void reset() {
            resetCalled = true;
        }
        
        public boolean isResetCalled() {
            return resetCalled;
        }
        
    }

}
