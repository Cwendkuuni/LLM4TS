package org.apache.commons.math.ode.nonstiff.test;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.RootFindersException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.RootFunction;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.special.BesselFunctions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class EmbeddedRungeKuttaIntegratorTest {

    private EmbeddedRungeKuttaIntegrator integrator;
    private FirstOrderDifferentialEquations equations;
    private double[] initialValues;
    private double startTime, endTime;

    @Before
    public void setUp() {
        equations = mock(FirstOrderDifferentialEquations.class);
        initialValues = new double[]{-1.0, 0.0};
        startTime = 0.0;
        endTime = 2.0 * Math.PI;

        integrator = new EmbeddedRungeKuttaIntegrator("RK4", false,
                new double[]{0.25, 0.25, 0.25, 0.25},
                new double[][]{{0.25, 0.0, 0.0, 0.0},
                        {0, 0.25, 0.25, 0.25},
                        {0, 0.6, 0.14285714285714285, 0.05714285714285714},
                        {0, 0.8, 0.36646463664646366, 0.36646463664646366}},
                new double[]{0.25, 0.25, 0.25, 0.25},
                new RungeKuttaStepInterpolator() {
                    @Override
                    public void initialize(EmbeddedRungeKuttaIntegrator integrator, double[] y, double[] yPrime, boolean forward) {
                        integrator.reinitialize(equations, forward);
                    }
                },
                0.001, 0.01, null, null);

        when(equations.getDerivatives(anyDouble(), anyArray())).thenCallRealMethod();
    }

    @Test
    public void testIntegration() throws RootFindersException, MaxCountExceededException, NoBracketingException, DerivativeException, IntegratorException {
        double[] solution = new double[]{-Math.cos(1.0), Math.sin(1.0)};
        double[] computedSolution = new double[2];

        integrator.integrate(equations, startTime, initialValues, endTime, computedSolution);

        Assert.assertArrayEquals(solution, computedSolution, 1e-12);
        verify(equations, times(7)).getDerivatives(anyDouble(), anyArray());
    }

    @Test
    public void testIntegrationWithEvents() throws RootFindersException, MaxCountExceededException, NoBracketingException, DerivativeException, IntegratorException {
        double time = 1.5708;
        double[] initialValues = {-1.0, 0.0};
        double[] solution = new double[]{-Math.cos(time), Math.sin(time)};
        double[] computedSolution = new double[2];

        RootFunction rootFunction = mock(RootFunction.class);
        when(rootFunction.value(anyDouble(), anyArray())).then((invocation) -> {
            double t = ((Double) invocation.getArgs()[0]).doubleValue();
            double[] y = (double[]) invocation.getArgs()[1];
            return Math.sin(t) - 0.5;
        });
        Event event = new Event(time, rootFunction, null);

        StepHandler stepHandler = mock(StepHandler.class);
        when(stepHandler.handleStep(any(StepInterpolator.class), anyBoolean())).thenReturn(true);

        integrator.addStepHandler(stepHandler);

        integrator.integrate(equations, startTime, initialValues, endTime, computedSolution);

        Assert.assertArrayEquals(solution, computedSolution, 1e-12);
        verify(equations, times(7)).getDerivatives(anyDouble(), anyArray());
        verify(stepHandler, times(1)).handleStep(any(StepInterpolator.class), anyBoolean());
    }

    @Test(expected = IntegratorException.class)
    public void testIntegrationWithException() throws RootFindersException, MaxCountExceededException, NoBracketingException, DerivativeException {
        integrator.integrate(equations, startTime, initialValues, endTime, null);
    }

    @Test
    public void testIntegrationWithEmptyDerivativeFunction() throws DerivativeException {
        when(equations.getDerivatives(anyDouble(), anyArray())).thenReturn(new double[0]);

        try {
            integrator.integrate(equations, startTime, initialValues, endTime, new double[2]);
            fail("An IntegratorException should have been thrown.");
        } catch (IntegratorException e) {
            // expected
        }

        verify(equations, times(1)).getDerivatives(anyDouble(), anyArray());
    }

}