package org.apache.commons.math.ode;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.function.Constants;
import org.apache.commons.math3.analysis.solvers.BracketingNthOrderBrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateRealSolver;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.ode.AbstractStepInterpolator;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.AbstractEventHandler;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Test;

public class AbstractIntegratorTest {

    @Test
    public void testBasicFunctionality() throws MathIllegalStateException, MathIllegalArgumentException, MaxCountExceededException, NumberIsTooSmallException, DimensionMismatchException {
        String name = "TestIntegrator";
        AbstractIntegrator integrator = new AbstractIntegrator(name);

        // Test adding and removing step handlers
        integrator.addStepHandler((interpolator, isLastStep) -> {
            System.out.println("Step handler called.");
        });
        integrator.clearStepHandlers();

        // Test setting and getting name
        assertEquals("TestIntegrator", integrator.getName());

        // Test setting and getting maximum evaluations
        integrator.setMaxEvaluations(10);
        assertEquals(10, integrator.getMaxEvaluations());

        // Test integrating a simple equation
        FirstOrderDifferentialEquations equations = (t, y) -> new double[] {2 * y[0]};
        double t0 = 0.0;
        double[] y0 = {1.0};
        double t1 = 2.0;
        double[] y1 = integrator.integrate(equations, t0, y0, t1);
        assertEquals(3.0, y1[0], 1e-6);

        // Test integrating with event handlers
        EquationWithEvent equationWithEvent = new EquationWithEvent();
        integrator.addEventHandler(equationWithEvent.getEventHandler(), 0.5, 1e-6, 10);
        integrator.setEquations(equationWithEvent);
        integrator.integrate(1.0, 3.0);
        assertEquals(2.5, equationWithEvent.getEventTime(), 1e-6);

        // Test computing derivatives
        double[] y = {1.0};
        double[] yDot = new double[y.length];
        integrator.computeDerivatives(1.0, y, yDot);
        assertEquals(2.0, yDot[0], 1e-6);

        // Test acceptStep method
        AbstractStepInterpolator interpolator = new StepInterpolator();
        double[] state = {1.0};
        double tEnd = 2.0;
        double t = integrator.acceptStep(interpolator, state, yDot, tEnd);
        assertEquals(1.0, state[0], 1e-6);
        assertEquals(1.0, t, 1e-6);

        // Test sanity checks
        ExpandableStatefulODE equations2 = new ExpandableStatefulODE(() -> new double[] {0.0});
        integrator.sanityChecks(equations2, 1.0);

    }

    @Test
    public void testEventHandling() throws MathIllegalStateException, MathIllegalArgumentException, MaxCountExceededException, NumberIsTooSmallException, DimensionMismatchException {
        String name = "TestIntegrator";
        AbstractIntegrator integrator = new AbstractIntegrator(name);

        // Test adding and removing event handlers
        integrator.addEventHandler(new EventHandler() {
            @Override
            public void handleEvent( double t, double[] y ) {
                System.out.println("Event handler called.");
            }

            @Override
            public boolean isStateDependent() {
                return false;
            }
        }, 0.5, 1e-6, 10, new BracketingNthOrderBrentSolver(1e-6, 5));
        integrator.clearEventHandlers();

        // Test integrating with event handlers
        EquationWithEvent equationWithEvent = new EquationWithEvent();
        integrator.addEventHandler(equationWithEvent.getEventHandler(), 0.5, 1e-6, 10, new BracketingNthOrderBrentSolver(1e-6, 5));
        integrator.setEquations(equationWithEvent);
        integrator.integrate(1.0, 3.0);
        assertEquals(2.5, equationWithEvent.getEventTime(), 1e-6);

    }

    @Test
    public void testMaxEvaluations() throws MathIllegalStateException, MathIllegalArgumentException, MaxCountExceededException, NumberIsTooSmallException, DimensionMismatchException {
        String name = "TestIntegrator";
        AbstractIntegrator integrator = new AbstractIntegrator(name);

        // Test setting and getting maximum evaluations
        integrator.setMaxEvaluations(5);
        integrator.setMaxEvaluations(10);
        assertEquals(10, integrator.getMaxEvaluations());

        // Test integrating until maximum evaluations is reached
        FirstOrderDifferentialEquations equations = (t, y) -> new double[] {2 * y[0]};
        double t0 = 0.0;
        double[] y0 = {1.0};
        integrator.setMaxEvaluations(5);
        integrator.integrate(equations, t0, y0, 5.0);
        assertEquals(5.0, integrator.getTime(), 1e-6);

    }

}

class EquationWithEvent implements ExpandableStatefulODE {

    private double eventTime;

    @Override
    public void setTime( double time ) {
        eventTime = time;
    }

    @Override
    public void setPrimaryState( double[] state ) {
        // empty
    }

    @Override
    public int getDimension() {
        return 1;
    }

    @Override
    public double[] getPrimaryState() {
        return new double[] {getState()};
    }

    @Override
    public void setPrimaryState( double[] primaryState ) {
        // empty
    }

    @Override
    public void setDerivatives( double[] derivatives ) {
        // empty
    }

    @Override
    public void setDerivatives( double t, double[] y, double[] yDot ) {
        // empty
    }

    @Override
    public double[] computeDerivatives( double t, double[] y ) {
        return new double[] {2.0 * y[0]};
    }

    @Override
    public double getTime() {
        return eventTime;
    }

    @Override
    public double[] getState() {
        return new double[] {eventTime};
    }

    @Override
    public double[] getStatePartialDerivatives( int i, double[] y ) {
        return new double[] {1.0};
    }

    @Override
    public void reset() {
        // empty
    }

    @Override
    public EventState getEventState( int i ) {
        return new EventState() {
            @Override
            public void reinitializeBegin( AbstractStepInterpolator interpolator ) {
                // empty
            }

            @Override
            public boolean evaluateStep( AbstractStepInterpolator interpolator ) {
                return eventTime == interpolator.getGlobalCurrentTime();
            }

            @Override
            public double getEventTime() {
                return eventTime;
            }

            @Override
            public void stepAccepted( double t, double[] y ) {
                // empty
            }

            @Override
            public boolean stop() {
                return false;
            }

            @Override
            public void reset( double t, double[] y ) {
                // empty
            }

            @Override
            public EventState copy() {
                return this;
            }

            @Override
            public EventState getEventHandler() {
                return new EventHandler() {
                    @Override
                    public void handleEvent( double t, double[] y ) {
                        eventTime += 0.1;
                    }

                    @Override
                    public boolean isStateDependent() {
                        return false;
                    }
                };
            }
        };
    }

}

class StepInterpolator extends AbstractStepInterpolator {

    @Override
    public double getGlobalPreviousTime() {
        return getPreviousTime();
    }

    @Override
    public double getGlobalCurrentTime() {
        return getCurrentTime();
    }

    @Override
    public void setInterpolatedTime( double t ) {
        setCurrentTime(t);
    }

    @Override
    public double[] getInterpolatedState() {
        return getPrimaryState();
    }

}