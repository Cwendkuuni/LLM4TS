import org.apache.commons.math3.ode.AbstractIntegrator;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.sampling.AbstractStepInterpolator;
import org.apache.commons.math3.ode.stephandlers.MockStepHandler;
import org.apache.commons.math3.util.Incrementor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AbstractIntegratorTest {

    private AbstractIntegrator integrator;
    private FirstOrderDifferentialEquations equations;
    private ExpandableStatefulODE expandableEquations;
    private AbstractStepInterpolator stepInterpolator;
    private MockStepHandler stepHandler;
    private List<EventHandler> eventHandlers;

    @BeforeEach
    void setUp() {
        integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t)
                    throws MathIllegalStateException, MathIllegalArgumentException {
                // no-op
            }
        };
        equations = (FirstOrderDifferentialEquations) new MyDifferentialEquations(1);
        expandableEquations = new ExpandableStatefulODE(equations);
        stepInterpolator = new StepInterpolator();
        stepHandler = new MockStepHandler();
        integrator.addStepHandler(stepHandler);
        eventHandlers = new ArrayList<>();
    }

    @Test
    void testConstructor() {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator");
        assertEquals("TestIntegrator", integrator.getName());
        assertEquals(Collections.emptyList(), integrator.getStepHandlers());
        assertEquals(Double.NaN, integrator.getCurrentStepStart());
        assertEquals(Double.NaN, integrator.getCurrentSignedStepsize());
        assertEquals(false, integrator.isLastStep());
        assertEquals(false, integrator.resetOccurred);
        assertEquals(Collections.emptyList(), integrator.getEventHandlers());
        assertEquals(false, integrator.statesInitialized);
        assertEquals(new Incrementor(), integrator.evaluations);
        assertEquals(null, integrator.expandable);
    }

    @Test
    void testAddStepHandler() {
        integrator.addStepHandler(stepHandler);
        assertEquals(Collections.singletonList(stepHandler), integrator.getStepHandlers());
    }

    @Test
    void testClearStepHandlers() {
        integrator.addStepHandler(stepHandler);
        integrator.clearStepHandlers();
        assertEquals(Collections.emptyList(), integrator.getStepHandlers());
    }

    @Test
    void testIntegrate() {
        integrator.setEquations(expandableEquations);
        integrator.integrate(expandableEquations, 1.0);
        // verify that the integration was not performed
        assertEquals(0, integrator.getEvaluations());
    }

    @Test
    void testIntegrateWithEventHandler() {
        integrator.addEventHandler(new MyEventHandler(), 1.0, 1.0e-8, 10);
        integrator.setEquations(expandableEquations);
        integrator.integrate(expandableEquations, 1.1);
        // verify that the event handler was called
        assertEquals(1, eventHandlers.size());
        assertEquals(1.1, eventHandlers.get(0).getEventTime(), 1.0e-8);
    }

    @Test
    void testIntegrateWithMaxEvaluations() {
        integrator.setMaxEvaluations(10);
        integrator.setEquations(expandableEquations);
        integrator.integrate(expandableEquations, 2.0);
        // verify that the integration was stopped due to max evaluations
        assertEquals(10, integrator.getEvaluations());
        assertThrows(MaxCountExceededException.class, () -> integrator.integrate(expandableEquations, 3.0));
    }

    @Test
    void testIntegrateWithReset() {
        integrator.addEventHandler(new MyEventHandlerWithReset(), 1.0, 1.0e-8, 10);
        integrator.setEquations(expandableEquations);
        integrator.integrate(expandableEquations, 1.1);
        // verify that the event handler was called and the derivatives were reset
        assertEquals(1, eventHandlers.size());
        assertEquals(1.1, eventHandlers.get(0).getEventTime(), 1.0e-8);
        assertEquals(1.0, integrator.getEvaluations(), 1.0);
    }

    @Test
    void testAcceptStep() throws MathIllegalStateException {
        integrator.addEventHandler(new MyEventHandler(), 1.0, 1.0e-8, 10);
        integrator.addStepHandler(stepHandler);
        integrator.setEquations(expandableEquations);
        stepInterpolator.setGlobalPreviousTime(0.0);
        stepInterpolator.setGlobalCurrentTime(1.0);
        double tEnd = 2.0;
        integrator.acceptStep(stepInterpolator, new double[1], new double[1], tEnd);
        // verify that the event handler was called
        assertEquals(1, eventHandlers.size());
        assertEquals(1.0, eventHandlers.get(0).getEventTime(), 1.0e-8);
        // verify that the step handler was called
        assertEquals(1, stepHandler.getCallCount());
        // verify that the integration was stopped due to reaching the final time
        assertEquals(1, integrator.getEvaluations(), 1.0);
    }

    @Test
    void testSanityChecks() {
        integrator.setEquations(new ExpandableStatefulODE(new MyDifferentialEquations(1)) {
            @Override
            public double getTime() {
                return 0.0;
            }

            @Override
            public double[] getPrimaryState() {
                return new double[0];
            }

            @Override
            public int getDimension() {
                return 1;
            }
        });
        integrator.sanityChecks(expandableEquations, 0.0);
    }

    @Test
    void testIntegrateWithTooSmallIntegrationInterval() {
        integrator.setEquations(expandableEquations);
        integrator.setMaxEvaluations(1);
        stepInterpolator.setGlobalPreviousTime(0.0);
        stepInterpolator.setGlobalCurrentTime(1.0e-6);
        integrator.acceptStep(stepInterpolator, new double[1], new double[1], 1.0);
        // verify that the integration was stopped due to too small integration interval
        assertEquals(1, integrator.getEvaluations());
    }

}

class MyDifferentialEquations implements FirstOrderDifferentialEquations {

    private final int dimension;

    public MyDifferentialEquations(int dimension) {
        this.dimension = dimension;
    }

    @Override
    public int getDimension() {
        return dimension;
    }

    @Override
    public void computeDerivatives(double t, double[] y, double[] yDot) {
        yDot[0] = y[0] + 1.0;
    }

}

class MyEventHandler implements EventHandler {

    @Override
    public void evaluateStep(AbstractStepInterpolator interpolator) {
        return true;
    }

    @Override
    public void stepAccepted(double t, double[] y) {
        eventHandlers.add(new EventState(this, t, y));
    }

    @Override
    public void stepRejected(double t, double[] y, int cause) {
        // no-op
    }

}

class MyEventHandlerWithReset implements EventHandler {

    @Override
    public void evaluateStep(AbstractStepInterpolator interpolator) {
        return true;
    }

    @Override
    public void stepAccepted(double t, double[] y) {
        eventHandlers.add(new EventState(this, t, y));
        resetOccurred = true;
    }

    @Override
    public void stepRejected(double t, double[] y, int cause) {
        // no-op
    }

}

class MockStepHandler implements StepHandler {

    private int callCount;

    @Override
    public void handleStep(AbstractStepInterpolator interpolator, boolean lastStep) {
        callCount++;
    }

    public int getCallCount() {
        return callCount;
    }

}

class StepInterpolator extends AbstractStepInterpolator {

    @Override
    public double getInterpolatedTime() {
        return 0.0;
    }

    @Override
    public double[] getInterpolatedState() {
        return new double[0];
    }

    @Override
    public double[] getDerivatives() {
        return new double[0];
    }

    @Override
    public double getStepSize() {
        return 0.0;
    }

    @Override
    public void setGlobalPreviousTime(double previousTime) {
        // no-op
    }

    @Override
    public void setGlobalCurrentTime(double currentTime) {
        // no-op
    }

}