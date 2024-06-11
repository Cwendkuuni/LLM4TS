package org.apache.commons.math.ode.nonstiff.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoRootFoundException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.test.AbstractODETest;
import org.apache.commons.math3.ode.test.MockStepHandler;
import org.apache.commons.math3.ode.test.StepTestValues;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.special.BesselFunctions;
import org.apache.commons.math3.special.BesselFunctionsGen;
import org.junit.Test;

public class RungeKuttaIntegratorTest extends AbstractODETest {

    @Test
    public void testRungeKuttaIntegrator_basicTest() {
        // Basic test for RungeKuttaIntegrator
        double[] coefficients = {0.0, 0.5, 0.5};
        double[][] internalWeights = {{1.0 / 6.0}, {1.0 / 3.0, 1.0 / 3.0}, {1.0 / 2.0, 1.0 / 2.0, 1.0 / 2.0}};
        double[] externalWeights = {0.25, 0.25, 0.5};
        RungeKuttaIntegrator integrator = new EulerIntegrator(
                "EulerIntegrator", coefficients, internalWeights, externalWeights,
                new RungeKuttaStepInterpolator(), 1.0);

        FirstOrderDifferentialEquations equations = new StepFunctionDifferentialEquations();
        double initialTime = 0.0;
        double[] initialState = new double[]{0.0};
        double finalTime = 1.0;
        double[] expectedState = new double[]{1.0};

        double[] result = integrator.integrate(equations, initialTime, initialState, finalTime, new double[initialState.length]);

        assertArrayEquals(expectedState, result);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testRungeKuttaIntegrator_invalidStepSize() {
        // Test for passing invalid step size to RungeKuttaIntegrator
        double[] coefficients = {0.0, 0.5, 0.5};
        double[][] internalWeights = {{1.0 / 6.0}, {1.0 / 3.0, 1.0 / 3.0}, {1.0 / 2.0, 1.0 / 2.0, 1.0 / 2.0}};
        double[] externalWeights = {0.25, 0.25, 0.5};
        RungeKuttaIntegrator integrator = new EulerIntegrator(
                "EulerIntegrator", coefficients, internalWeights, externalWeights,
                new RungeKuttaStepInterpolator(), -1.0);

        FirstOrderDifferentialEquations equations = new StepFunctionDifferentialEquations();
        double initialTime = 0.0;
        double[] initialState = new double[]{0.0};
        double finalTime = 1.0;
        double[] result = integrator.integrate(equations, initialTime, initialState, finalTime, new double[initialState.length]);
    }

    @Test(expected = DerivativeException.class)
    public void testRungeKuttaIntegrator_nonDerivativeFunction() {
        // Test for passing a non-differentiable function to RungeKuttaIntegrator
        class NonDerivativeFunction implements FirstOrderDifferentialEquations {
            public double value(double t, double y[]) {
                throw new DerivativeException("Non-differentiable function!");
            }
        }

        double[] coefficients = {0.0, 0.5, 0.5};
        double[][] internalWeights = {{1.0 / 6.0}, {1.0 / 3.0, 1.0 / 3.0}, {1.0 / 2.0, 1.0 / 2.0, 1.0 / 2.0}};
        double[] externalWeights = {0.25, 0.25, 0.5};
        RungeKuttaIntegrator integrator = new EulerIntegrator(
                "EulerIntegrator", coefficients, internalWeights, externalWeights,
                new RungeKuttaStepInterpolator(), 1.0);

        NonDerivativeFunction nonDerivativeFunction = new NonDerivativeFunction();
        double initialTime = 0.0;
        double[] initialState = new double[]{0.0};
        double finalTime = 1.0;
        integrator.integrate(nonDerivativeFunction, initialTime, initialState, finalTime, new double[initialState.length]);
    }

    @Test(expected = IntegratorException.class)
    public void testRungeKuttaIntegrator_singularMatrix() {
        // Test for passing an integrator with a singular matrix to RungeKuttaIntegrator
        double[] coefficients = {1.0, 1.0, 1.0};
        double[][] internalWeights = {{1.0 / 6.0}, {1.0 / 3.0, 1.0 / 3.0}, {1.0 / 2.0, 1.0 / 2.0, 1.0 / 2.0}};
        double[] externalWeights = {0.25, 0.25, 0.5};
        RungeKuttaIntegrator integrator = new EulerIntegrator(
                "EulerIntegrator", coefficients, internalWeights, externalWeights,
                new RungeKuttaStepInterpolator(), 1.0);

        FirstOrderDifferentialEquations equations = new SingularMatrixDifferentialEquations();
        double initialTime = 0.0;
        double[] initialState = new double[]{0.0};
        double finalTime = 1.0;
        double[] result = integrator.integrate(equations, initialTime, initialState, finalTime, new double[initialState.length]);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testRungeKuttaIntegrator_maxCountExceeded() {
        // Test for reaching the maximum number of steps in RungeKuttaIntegrator
        double[] coefficients = {0.0, 0.5, 0.5};
        double[][] internalWeights = {{1.0 / 6.0}, {1.0 / 3.0, 1.0 / 3.0}, {1.0 / 2.0, 1.0 / 2.0, 1.0 / 2.0}};
        double[] externalWeights = {0.25, 0.25, 0.5};
        RungeKuttaIntegrator integrator = new EulerIntegrator(
                "EulerIntegrator", coefficients, internalWeights, externalWeights,
                new RungeKuttaStepInterpolator(), 1.0);

        FirstOrderDifferentialEquations equations = new MaxCountExceededDifferentialEquations();
        double initialTime = 0.0;
        double[] initialState = new double[]{0.0};
        double finalTime = 1.0;
        double[] result = integrator.integrate(equations, initialTime, initialState, finalTime, new double[initialState.length]);
    }

    @Test
    public void testRungeKuttaIntegrator_eventsHandler() {
        // Test for handling events using a custom event handler
        double[] coefficients = {0.0, 0.5, 0.5};
        double[][] internalWeights = {{1.0 / 6.0}, {1.0 / 3.0, 1.0 / 3.0}, {1.0 / 2.0, 1.0 / 2.0, 1.0 / 2.0}};
        double[] externalWeights = {0.25, 0.25, 0.5};
        RungeKuttaIntegrator integrator = new EulerIntegrator(
                "EulerIntegrator", coefficients, internalWeights, externalWeights,
                new RungeKuttaStepInterpolator(), 1.0);

        FirstOrderDifferentialEquations equations = new StepFunctionDifferentialEquations();
        double initialTime = 0.0;
        double[] initialState = new double[]{0.0};
        double finalTime = 2.0;
        double[] expectedState = new double[]{1.0, Math.PI};

        MockStepHandler stepHandler = new MockStepHandler();
        integrator.addStepHandler(stepHandler);

        RungeKuttaStepInterpolator rki = (RungeKuttaStepInterpolator) integrator.getPrototype().copy();
        rki.reinitialize(integrator, new double[2], new double[2][], true);

        CombinedEventsManager manager = integrator.addEndTimeChecker(finalTime, new StepTestValues(expectedState), new Event[]{
            new When(1.0, t -> BesselFunctionsGen.i0(t) == 1.0 && BesselFunctionsGen.i1(t) == Math.PI)
        });

        double[] result = integrator.integrate(equations, initialTime, initialState, finalTime, new double[initialState.length]);

        assertArrayEquals(expectedState, result);
        assertEquals(1, stepHandler.getCallCount());
    }
}