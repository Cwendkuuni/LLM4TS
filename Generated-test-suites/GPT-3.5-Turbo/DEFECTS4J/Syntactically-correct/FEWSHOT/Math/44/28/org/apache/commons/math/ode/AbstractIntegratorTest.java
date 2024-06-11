import org.apache.commons.math.ode.*;

import static org.junit.Assert.*;
import org.junit.Test;

public class AbstractIntegratorTest {
  
    @Test
    public void testAddStepHandler() {
        AbstractIntegrator abstractIntegrator = new AbstractIntegratorTest();
        StepHandler stepHandler = new StepHandler();
        abstractIntegrator.addStepHandler(stepHandler);
        assertEquals(stepHandler, abstractIntegrator.getStepHandlers().get(0));
        abstractIntegrator.clearStepHandlers();
        assertTrue(abstractIntegrator.getStepHandlers().isEmpty());
    }
  
    @Test
    public void testAddEventHandler() {
        AbstractIntegrator abstractIntegrator = new AbstractIntegratorTest();
        EventHandler eventHandler = new EventHandler();
        double maxCheckInterval = 0.1;
        double convergence = 1e-6;
        int maxIterationCount = 100;
        abstractIntegrator.addEventHandler(eventHandler, maxCheckInterval, convergence, maxIterationCount);
        assertEquals(eventHandler, abstractIntegrator.getEventHandlers().get(0));
        abstractIntegrator.clearEventHandlers();
        assertTrue(abstractIntegrator.getEventHandlers().isEmpty());
    }
  
    @Test
    public void testGetEvaluations() {
        AbstractIntegrator abstractIntegrator = new AbstractIntegratorTest();
        assertEquals(0, abstractIntegrator.getEvaluations());
        abstractIntegrator.incrementCount();
        assertEquals(1, abstractIntegrator.getEvaluations());
    }
  
    @Test
    public void testIntegrate() {
        AbstractIntegrator abstractIntegrator = new AbstractIntegratorTest();
        double t0 = 0;
        double[] y0 = {1, 2, 3};
        double t = 10;
        double[] y = new double[3];
        abstractIntegrator.integrate(new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 3;
            }
            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = 0;
                yDot[1] = 0;
                yDot[2] = 0;
            }
        }, t0, y0, t, y);
        assertEquals(t, abstractIntegrator.getTime(), 1e-6);
    }
  
    @Test
    public void testSetMaxEvaluations() {
        AbstractIntegrator abstractIntegrator = new AbstractIntegratorTest();
        abstractIntegrator.setMaxEvaluations(100);
        assertEquals(100, abstractIntegrator.getMaxEvaluations());
    }

}