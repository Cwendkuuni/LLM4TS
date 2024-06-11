import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.ode.events.*;
import org.apache.commons.math3.analysis.solvers.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.ode.sampling.*;
import java.util.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.exception.util.*;

public class AbstractIntegratorTest {

    @Test
    public void testGetName() {
        AbstractIntegrator integrator = new AbstractIntegrator("Integrator");
        assertEquals("Integrator", integrator.getName());
    }

    @Test
    public void testAddStepHandler() {
        AbstractIntegrator integrator = new AbstractIntegrator();
        StepHandler stepHandler = new StepHandler() {
            public void init(double t0, double[] y0, double t) {}
            public void handleStep(StepInterpolator interpolator, boolean isLast) {}
        };
        integrator.addStepHandler(stepHandler);
        assertEquals(1, integrator.getStepHandlers().size());
    }

    @Test
    public void testClearStepHandlers() {
        AbstractIntegrator integrator = new AbstractIntegrator();
        StepHandler stepHandler = new StepHandler() {
            public void init(double t0, double[] y0, double t) {}
            public void handleStep(StepInterpolator interpolator, boolean isLast) {}
        };
        integrator.addStepHandler(stepHandler);
        assertEquals(1, integrator.getStepHandlers().size());
        integrator.clearStepHandlers();
        assertEquals(0, integrator.getStepHandlers().size());
    }

    @Test
    public void testAddEventHandler() {
        AbstractIntegrator integrator = new AbstractIntegrator();
        EventHandler eventHandler = new EventHandler() {
            public void init(double t0, double[] y0, double t) {}
            public double g(double t, double[] y) {return 0;}
            public Action eventOccurred(double t, double[] y, boolean increasing) {return null;}
            public void resetState(double t, double[] y) {}
        };
        integrator.addEventHandler(eventHandler, 0.1, 1e-6, 100);
        assertEquals(1, integrator.getEventHandlers().size());
    }

    @Test
    public void testClearEventHandlers() {
        AbstractIntegrator integrator = new AbstractIntegrator();
        EventHandler eventHandler = new EventHandler() {
            public void init(double t0, double[] y0, double t) {}
            public double g(double t, double[] y) {return 0;}
            public Action eventOccurred(double t, double[] y, boolean increasing) {return null;}
            public void resetState(double t, double[] y) {}
        };
        integrator.addEventHandler(eventHandler, 0.1, 1e-6, 100);
        assertEquals(1, integrator.getEventHandlers().size());
        integrator.clearEventHandlers();
        assertEquals(0, integrator.getEventHandlers().size());
    }

    @Test
    public void testGetMaxEvaluations() {
        AbstractIntegrator integrator = new AbstractIntegrator();
        assertEquals(Integer.MAX_VALUE, integrator.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        AbstractIntegrator integrator = new AbstractIntegrator();
        assertEquals(0, integrator.getEvaluations());
    }

    @Test
    public void testInitIntegration() {
        AbstractIntegrator integrator = new AbstractIntegrator();
        double[] y0 = {0.0};
        double[] y = {0.0};
        double t0 = 0.0;
        double t = 1.0;
        integrator.initIntegration(t0, y0, t);
        assertEquals(0, integrator.getEvaluations());
    }

    @Test
    public void testSetMaxEvaluations() {
        AbstractIntegrator integrator = new AbstractIntegrator();
        integrator.setMaxEvaluations(100);
        assertEquals(100, integrator.getMaxEvaluations());
    }

    @Test
    public void testIntegrate() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            public void integrate(ExpandableStatefulODE ode, double t) {}
        };
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            public int getDimension() {return 1;}
            public void computeDerivatives(double t, double[] y, double[] yDot) {}
        };
        double[] y0 = {0.0};
        double[] y = {0.0};
        double t0 = 0.0;
        double t = 1.0;
        try {
            integrator.integrate(equations, t0, y0, t, y);
        } catch (Exception e) {
            fail("Exception: " + e.getMessage());
        }
    }

    @Test
    public void testComputeDerivatives() {
        AbstractIntegrator integrator = new AbstractIntegrator();
        double[] y = {0.0};
        double[] yDot = {0.0};
        try {
            integrator.computeDerivatives(0.0, y, yDot);
            assertEquals(1, integrator.getEvaluations());
        } catch (Exception e) {
            fail("Exception: " + e.getMessage());
        }
    }

    @Test
    public void testSetStateInitialized() {
        AbstractIntegrator integrator = new AbstractIntegrator();
        integrator.setStateInitialized(true);
        assertTrue(integrator.isStateInitialized());
    }

    @Test
    public void testAcceptStep() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            public void integrate(ExpandableStatefulODE ode, double t) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {}
        };
        StepInterpolator interpolator = new StepInterpolator() {
            public void setSoftPreviousTime(double time) {}
            public void setSoftCurrentTime(double time) {}
            public double getGlobalPreviousTime() {return 0.0;}
            public double getGlobalCurrentTime() {return 1.0;}
            public double getInterpolatedTime() {return 1.0;}
            public double[] getInterpolatedState() {return new double[] {0.0};}
            public double[] getInterpolatedDerivatives() {return new double[] {0.0};}
            public double[][] getInterpolatedSecondaryState() {return new double[0][0];}
            public double[] getInterpolatedSecondaryDerivatives(int index) {return new double[0];}
        };
        double[] y = {0.0};
        double[] yDot = {0.0};
        try {
            integrator.acceptStep(interpolator, y, yDot, 1.0);
        } catch (Exception e) {
            fail("Exception: " + e.getMessage());
        }
    }

    @Test
    public void testSanityChecks() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            public void integrate(ExpandableStatefulODE ode, double t) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {}
        };
        ExpandableStatefulODE equations = new ExpandableStatefulODE(new FirstOrderDifferentialEquations() {
            public int getDimension() {return 1;}
            public void computeDerivatives(double t, double[] y, double[] yDot) {}
        });
        double t = 0.0;
        try {
            integrator.sanityChecks(equations, t);
        } catch (Exception e) {
            fail("Exception: " + e.getMessage());
        }
    }
}