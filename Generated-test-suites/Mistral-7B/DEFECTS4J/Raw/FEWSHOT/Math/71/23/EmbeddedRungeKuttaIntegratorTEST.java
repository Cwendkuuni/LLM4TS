package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.ode.util.ODETestUtils;
import org.junit.Before;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {

    private EmbeddedRungeKuttaIntegrator integrator;
    private FirstOrderDifferentialEquations equations;
    private double[] initialState = new double[] { 0.0, 0.0 };
    private double[] finalState = new double[] { 1.0, 1.0 };
    private double time = 1.0;
    private double[] expectedState = new double[] { 1.0, 1.0 };
    private StepInterpolator interpolator;
    private StepHandler stepHandler;
    private Event event;
    private CombinedEventsManager eventsManager;

    @Before
    public void setUp() {
        equations = ODETestUtils.createMockEquations(initialState.length);
        integrator = new EmbeddedRungeKutta23FehlbergIntegrator(
                "EmbeddedRungeKutta23FehlbergIntegrator", true,
                new double[] { 0.3, 0.6, 1.0 / 3.0, 2.0 / 3.0, 1.0 / 4.0 },
                new double[][] { { 0.23972959255767088E-2, 0.903553908512843E-1, 0.3736059911110223 },
                        { 0.8789629625012602E-2, 0.6065108686267872E-1, 0.2764842215245379 },
                        { 0.2473254162450322E-3, 0.1267976243213277E-1, 0.3551549347033882 },
                        { 0.4736695312499407E-3, 0.6279011191649872E-2, 0.2381925689327643 },
                        { 0.12821193111022306E-3, 0.1383361623311911E-1, 0.5027673313863578 },
                        { 0.3168876798055433E-3, 0.2531792217616551E-1, 0.8000003508492445 },
                        { 0.8000000000000000E-3, 0.6881135453943136E-1, 0.9279937350929991 } },
                new double[] { 0.23972959255767088E-2, 0.8789629625012602E-2, 0.2473254162450322E-3,
                        0.12821193111022306E-3, 0.3168876798055433E-3, 0.8000000000000000E-3 },
                new EmbeddedRungeKutta23FehlbergIntegrator.RungeKuttaStepInterpolator() {
                    @Override
                    public void reinitialize(EmbeddedRungeKuttaIntegrator integrator, double[] yTmp,
                                             double[][] yDotK, boolean forward) {
                        this.integrator = integrator;
                        this.yTmp = yTmp;
                        this.yDotK = yDotK;
                        this.forward = forward;
                    }
                }, 1e-10, 1e-10);
        interpolator = integrator.getPrototype();
        stepHandler = new StepHandler() {
            @Override
            public void handleStep(StepInterpolator interpolator, int stepNumber) {
                assertTrue(interpolator instanceof EmbeddedRungeKutta23FehlbergIntegrator.RungeKuttaStepInterpolator);
                EmbeddedRungeKuttaIntegrator.RungeKuttaStepInterpolator rki = (EmbeddedRungeKuttaIntegrator.RungeKuttaStepInterpolator) interpolator;
                assertEquals(integrator, rki.integrator);
                assertArrayEquals(initialState, rki.yTmp);
                assertArrayEquals(expectedState, rki.y);
                assertArrayEquals(initialState, rki.yDotK[0]);
                assertArrayEquals(expectedState, rki.yDotK[1]);
                assertArrayEquals(expectedState, rki.yDotK[2]);
                assertArrayEquals(expectedState, rki.yDotK[3]);
                assertArrayEquals(expectedState, rki.yDotK[4]);
                assertArrayEquals(expectedState, rki.yDotK[5]);
                assertArrayEquals(expectedState, rki.yDotK[6]);
                assertFalse(rki.isLastStep);
            }
        };
        event = new When(time);
        eventsManager = new CombinedEventsManager();
        eventsManager.addEvent(event);
    }

    @Test
    public void testIntegration() throws Exception {
        integrator.setStepHandlers(stepHandler);
        integrator.setEventsManager(eventsManager);
        double[] result = integrator.integrate(equations, 0.0, initialState, time, finalState);
        assertArrayEquals(expectedState, result);
    }

    @Test(expected = DerivativeException.class)
    public void testDerivativeException() throws Exception {
        equations = ODETestUtils.createMockEquationsWithDerivativeException(initialState.length);
        integrator.setStepHandlers(stepHandler);
        integrator.setEventsManager(eventsManager);
        integrator.integrate(equations, 0.0, initialState, time, finalState);
    }

    @Test(expected = IntegratorException.class)
    public void testIntegratorException() throws Exception {
        equations = ODETestUtils.createMockEquationsWithEvent(initialState.length, time);
        integrator.setStepHandlers(stepHandler);
        integrator.setEventsManager(eventsManager);
        integrator.integrate(equations, 0.0, initialState, time, finalState);
    }

}