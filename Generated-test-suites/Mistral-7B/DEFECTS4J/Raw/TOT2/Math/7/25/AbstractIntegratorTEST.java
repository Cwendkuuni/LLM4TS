import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.events.MockEventHandler;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.MockStepHandler;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test class for {@link AbstractIntegrator}.
 */
public class AbstractIntegratorTest {

    /**
     * Test method for {@link AbstractIntegrator#AbstractIntegrator(String)}.
     */
    @Test
    public void testConstructor() {
        AbstractIntegrator integrator = new AbstractIntegrator("testConstructor");
        assertEquals("testConstructor", integrator.getName());
        assertTrue(integrator.getStepHandlers().isEmpty());
        assertFalse(integrator.isLastStep());
        assertFalse(integrator.resetOccurred);
        assertNull(integrator.expandable);
    }

    /**
     * Test method for {@link AbstractIntegrator#addStepHandler(StepHandler)}.
     */
    @Test
    public void testAddStepHandler() {
        AbstractIntegrator integrator = new AbstractIntegrator("testAddStepHandler");
        MockStepHandler mockHandler = new MockStepHandler();
        integrator.addStepHandler(mockHandler);
        assertTrue(integrator.getStepHandlers().contains(mockHandler));
        integrator.clearStepHandlers();
        assertFalse(integrator.getStepHandlers().contains(mockHandler));
    }

    /**
     * Test method for {@link AbstractIntegrator#initIntegration(double, double[], double)}.
     */
    @Test
    public void testInitIntegration() {
        AbstractIntegrator integrator = new AbstractIntegrator("testInitIntegration");
        integrator.initIntegration(0.0, new double[]{1.0}, 1.0);
        assertNaN(integrator.getCurrentStepStart());
        assertNaN(integrator.getCurrentSignedStepsize());
        assertFalse(integrator.isLastStep());
        assertFalse(integrator.resetOccurred);
    }

    /**
     * Test method for {@link AbstractIntegrator#integrate(ExpandableStatefulODE, double)}.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testIntegrateWithDimensionMismatchException() throws Exception {
        AbstractIntegrator integrator = new AbstractIntegrator("testIntegrateWithDimensionMismatchException");
        integrator.setEquations(new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                // empty method
            }

            @Override
            public double getTime() {
                return 0.0;
            }

            @Override
            public double[] getPrimaryState() {
                return new double[]{1.0, 1.0};
            }
        });
        integrator.integrate(new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                // empty method
            }

            @Override
            public double getTime() {
                return 1.0;
            }

            @Override
            public double[] getPrimaryState() {
                return new double[]{1.0};
            }
        }, 1.0);
    }

    /**
     * Test method for {@link AbstractIntegrator#integrate(ExpandableStatefulODE, double)}.
     */
    @Test
    public void testIntegrate() throws Exception {
        AbstractIntegrator integrator = new AbstractIntegrator("testIntegrate");
        MockEventHandler mockEventHandler = new MockEventHandler();
        integrator.addEventHandler(mockEventHandler, 0.0, 1.0E-10, 10);
        integrator.setEquations(new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = 1.0;
            }

            @Override
            public double getTime() {
                return 0.0;
            }

            @Override
            public double[] getPrimaryState() {
                return new double[]{1.0};
            }
        });
        integrator.integrate(new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = 1.0;
            }

            @Override
            public double getTime() {
                return 1.0;
            }

            @Override
            public double[] getPrimaryState() {
                return new double[]{2.0};
            }
        }, 2.0);
        MockStepHandler mockStepHandler = new MockStepHandler();
        integrator.addStepHandler(mockStepHandler);
        integrator.setMaxEvaluations(1);
        integrator.integrate(new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = 1.0;
            }

            @Override
            public double getTime() {
                return 1.0;
            }

            @Override
            public double[] getPrimaryState() {
                return new double[]{1.0};
            }
        }, 2.0);
        assertEquals(1, integrator.getEvaluations());
        assertTrue(mockEventHandler.isCalled());
        assertTrue(mockStepHandler.isCalled());
        assertFalse(integrator.isLastStep());
        assertFalse(integrator.resetOccurred);
    }

    /**
     * Test method for {@link AbstractIntegrator#computeDerivatives(double, double[], double[])}.
     */
    @Test
    public void testComputeDerivatives() throws Exception {
        AbstractIntegrator integrator = new AbstractIntegrator("testComputeDerivatives");
        integrator.expandable = new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] * y[0];
            }

            @Override
            public double getTime() {
                return 0.0;
            }

            @Override
            public double[] getPrimaryState() {
                return new double[]{1.0};
            }
        };
        double[] y = new double[]{1.0};
        double[] yDot = new double[1];
        integrator.computeDerivatives(0.0, y, yDot);
        assertEquals(1.0, yDot[0], 1E-10);
    }

    /**
     * Test method for {@link AbstractIntegrator#setStateInitialized()}.
     */
    @Test
    public void testSetStateInitialized() {
        AbstractIntegrator integrator = new AbstractIntegrator("testSetStateInitialized");
        integrator.setStateInitialized(true);
        assertTrue(integrator.isStateInitialized());
        integrator.setStateInitialized(false);
        assertFalse(integrator.isStateInitialized());
    }

}