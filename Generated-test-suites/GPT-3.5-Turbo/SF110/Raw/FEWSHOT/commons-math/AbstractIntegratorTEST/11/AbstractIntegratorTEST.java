import org.apache.commons.math3.ode.events.*;
import org.apache.commons.math3.analysis.solvers.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.ode.sampling.*;
import java.util.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.exception.util.*;
import static org.junit.Assert.*;
import org.junit.Test;

public class AbstractIntegratorTest {
    @Test
    public void testAbstractIntegrator() {
        AbstractIntegrator integrator = new AbstractIntegratorImpl("Test");
        assertEquals("Test", integrator.getName());

        Collection<StepHandler> handlers = new ArrayList<StepHandler>();
        handlers.add(new StepHandler() {
            public void init(double t0, double[] y0, double t) {}
            public void handleStep(StepInterpolator interpolator, boolean isLast) {}
        });
        integrator.stepHandlers = handlers;

        integrator.stepStart = 1.0;
        assertEquals(1.0, integrator.getCurrentStepStart(), 0.0);

        integrator.stepSize = 0.1;
        assertEquals(0.1, integrator.getCurrentSignedStepsize(), 0.0);

        assertTrue(integrator.eventsStates instanceof ArrayList);
        integrator.addEventHandler(new EventHandler() {
            public void init(double t0, double[] y0, double t) {}
            public Action eventOccurred(double t, double[] y, boolean increasing) { return null; }
            public void resetState(double t, double[] y) {}
        }, 0.01, 0.001, 100);

        assertEquals(1, integrator.getEventHandlers().size());

        integrator.clearEventHandlers();
        assertEquals(0, integrator.getEventHandlers().size());

        integrator.setMaxEvaluations(100);
        assertEquals(100, integrator.getMaxEvaluations());

        assertEquals(0, integrator.getEvaluations());

        double t0 = 0.0;
        double[] y0 = {1.0, 2.0};
        double t = 1.0;
        double[] y = new double[y0.length];
        assertThrows(DimensionMismatchException.class, () -> integrator.integrate(new FirstOrderDifferentialEquations() {
            public int getDimension() { return 2; }
            public void computeDerivatives(double t, double[] y, double[] dot) {}
        }, t0, y0, t, y));

        integrator.evaluations.resetCount();
        assertEquals(0, integrator.getEvaluations());
    }
    
    @Test
    public void testAbstractIntegrator2() {
        AbstractIntegrator integrator = new AbstractIntegratorImpl("Test");

        ExpandableStatefulODE equations = new ExpandableStatefulODE(new FirstOrderDifferentialEquations() {
            public int getDimension() { return 2; }
            public void computeDerivatives(double t, double[] y, double[] dot) {}
        });

        integrator.setEquations(equations);
        assertEquals(equations, integrator.getExpandable());

        Incrementor evaluations = integrator.getEvaluationsCounter();
        assertEquals(0, evaluations.getCount());

        integrator.integrate(equations, 0.0);
        assertEquals(1, evaluations.getCount());

        assertThrows(NumberIsTooSmallException.class, () -> integrator.sanityChecks(equations, 0.0));
    }
}

class AbstractIntegratorImpl extends AbstractIntegrator {
    public AbstractIntegratorImpl(String name) {
        super(name);
    }

    public void integrate(ExpandableStatefulODE equations, double t) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {}
}