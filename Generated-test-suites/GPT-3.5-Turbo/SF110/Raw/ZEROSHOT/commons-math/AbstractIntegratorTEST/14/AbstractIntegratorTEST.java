import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.analysis.solvers.BracketingNthOrderBrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.FirstOrderIntegrator;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.AbstractStepInterpolator;
import org.apache.commons.math3.util.Incrementor;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class AbstractIntegratorTest {

    @Test
    public void testGetName() {
        AbstractIntegrator integrator = new AbstractIntegratorImpl("name");
        assertEquals("name", integrator.getName());
    }

    @Test
    public void testAddStepHandler() {
        AbstractIntegrator integrator = new AbstractIntegratorImpl();
        StepHandler stepHandler = new StepHandler() {
            @Override
            public void init(double t0, double[] y0, double t) {
            }

            @Override
            public void handleStep(AbstractStepInterpolator interpolator, boolean isLast) {
            }
        };

        assertTrue(integrator.getStepHandlers().isEmpty());
        integrator.addStepHandler(stepHandler);
        assertFalse(integrator.getStepHandlers().isEmpty());
        assertTrue(integrator.getStepHandlers().contains(stepHandler));
    }

    @Test
    public void testClearStepHandlers() {
        AbstractIntegrator integrator = new AbstractIntegratorImpl();
        StepHandler stepHandler = new StepHandler() {
            @Override
            public void init(double t0, double[] y0, double t) {
            }

            @Override
            public void handleStep(AbstractStepInterpolator interpolator, boolean isLast) {
            }
        };
        integrator.addStepHandler(stepHandler);

        assertFalse(integrator.getStepHandlers().isEmpty());
        integrator.clearStepHandlers();
        assertTrue(integrator.getStepHandlers().isEmpty());
    }

    @Test
    public void testAddEventHandler() {
        AbstractIntegrator integrator = new AbstractIntegratorImpl();
        EventHandler eventHandler = new EventHandler() {
            @Override
            public void init(double t0, double[] y0, double t) {
            }

            @Override
            public Action eventOccurred(double t, double[] y, boolean increasing) {
                return null;
            }

            @Override
            public double g(double t, double[] y) {
                return 0;
            }
        };
        integrator.addEventHandler(eventHandler, 0.1, 0.01, 10);

        assertTrue(integrator.getEventHandlers().contains(eventHandler));
    }

    @Test
    public void testClearEventHandlers() {
        AbstractIntegrator integrator = new AbstractIntegratorImpl();
        EventHandler eventHandler = new EventHandler() {
            @Override
            public void init(double t0, double[] y0, double t) {
            }

            @Override
            public Action eventOccurred(double t, double[] y, boolean increasing) {
                return null;
            }

            @Override
            public double g(double t, double[] y) {
                return 0;
            }
        };
        integrator.addEventHandler(eventHandler, 0.1, 0.01, 10);

        assertFalse(integrator.getEventHandlers().isEmpty());
        integrator.clearEventHandlers();
        assertTrue(integrator.getEventHandlers().isEmpty());
    }

    @Test
    public void testGetIntegrate() throws DimensionMismatchException, NumberIsTooSmallException, MaxCountExceededException, NoBracketingException {
        AbstractIntegrator integrator = new AbstractIntegratorImpl();
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
            }
        };
        double t0 = 0.0;
        double[] y0 = {0.0};
        double t = 1.0;
        double[] y = new double[1];

        assertThrows(DimensionMismatchException.class, () -> {
            integrator.integrate(equations, t0, new double[2], t, y);
        });

        assertThrows(DimensionMismatchException.class, () -> {
            integrator.integrate(equations, t0, y0, t, new double[2]);
        });

        double result = integrator.integrate(equations, t0, y0, t, y);

        assertEquals(1.0, result, 0.0);
    }

    @Test
    public void testComputeDerivatives() throws MaxCountExceededException, DimensionMismatchException {
        AbstractIntegrator integrator = new AbstractIntegratorImpl();
        double t = 0.0;
        double[] y = {0.0};
        double[] yDot = new double[1];
        integrator.computeDerivatives(t, y, yDot);

        assertTrue(((AbstractIntegratorImpl) integrator).isComputeDerivativesCalled());
        assertArrayEquals(new double[1], yDot, 0.0);
    }

    @Test
    public void testSanityChecks() {
        AbstractIntegrator integrator = new AbstractIntegratorImpl();
        ExpandableStatefulODE equations = new ExpandableStatefulODE(null);
        double t = 0.0;

        assertThrows(NumberIsTooSmallException.class, () -> {
            integrator.sanityChecks(equations, t);
        });

        equations.setTime(1.0);
        assertDoesNotThrow(() -> {
            integrator.sanityChecks(equations, t);
        });
    }
    
    private class AbstractIntegratorImpl extends AbstractIntegrator {

        private boolean computeDerivativesCalled;

        public AbstractIntegratorImpl(String name) {
            super(name);
        }

        public AbstractIntegratorImpl() {
            super();
        }

        public boolean isComputeDerivativesCalled() {
            return computeDerivativesCalled;
        }

        @Override
        public void integrate(ExpandableStatefulODE p0, double p1) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yDot) throws MaxCountExceededException, DimensionMismatchException {
            computeDerivativesCalled = true;
        }
    }
}