package org.apache.commons.math.ode.nonstiff.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegratorTest;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator.RungeKuttaStep;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.events.impl.DefaultEventHandler;
import org.apache.commons.math3.ode.events.impl.StepEventHandlerAdapter;
import org.apache.commons.math3.ode.noautodiff.NoAutoDiffIntegrator;
import org.apache.commons.math3.ode.noautodiff.NoAutoDiffMethod;
import org.apache.commons.math3.ode.noautodiff.NoAutoDiffStepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.special.E;

/**
 * Test cases for the {@code EmbeddedRungeKuttaIntegrator} class.
 */
public class EmbeddedRungeKuttaIntegratorTest extends EmbeddedRungeKuttaIntegratorTest {

    @Override
    protected EmbeddedRungeKuttaIntegrator newIntegrator(int order,
            boolean fsal, double[] c, double[][] a, double[] b,
            RungeKuttaStepInterpolator prototype, double minStep, double maxStep,
            double[] vecAbsoluteTolerance, double[] vecRelativeTolerance) {
        return new EmbeddedRungeKuttaIntegrator(NAME, fsal, c, a, b, prototype, minStep, maxStep,
                vecAbsoluteTolerance, vecRelativeTolerance) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                // implementation specific
            }
        };
    }

    @Override
    protected FirstOrderDifferentialEquations newEquations(double[] y0) {
        return new NoAutoDiffIntegrator(new NoAutoDiffMethod(equation) {
            @Override
            protected double evaluateDerivative(double t, double[] y) {
                return equation.evaluateDerivative(t, y);
            }
        }, y0);
    }

    @Override
    protected StepHandler newStepHandler() {
        return new NoAutoDiffStepHandler();
    }

    @Override
    protected StepInterpolator newInterpolator(double[][] yDotK, boolean forward) {
        return new RungeKuttaStepInterpolator(prototype) {
            @Override
            protected RungeKuttaStep interpolate(double t, double[] y, double[] yDot) {
                return new RungeKuttaStep(this, t, y, yDot, forward);
            }
        };
    }

    @Override
    protected double[] initY0() {
        return new double[] {0.0};
    }

    @Override
    protected double[] initY1() {
        return new double[] {Math.exp(1.0)};
    }

    @Override
    protected double initT0() {
        return 0.0;
    }

    @Override
    protected double initT() {
        return 2.0;
    }

    @Override
    protected double getAbsoluteTolerance() {
        return 0.0001;
    }

    @Override
    protected double getRelativeTolerance() {
        return 1.0e-6;
    }

    @Override
    protected StepHandler newStepHandlerWithEvent(double tEvent) {
        return new StepEventHandlerAdapter(new DefaultEventHandler() {
            @Override
            public void handleEvent(Event event) {
                if (event instanceof When && ((When) event).getT() == tEvent) {
                    throw new MaxCountExceededException();
                }
            }
        });
    }

    @Override
    protected double getMaxStep() {
        return 1.0;
    }

    @Override
    protected double getMinStep() {
        return 0.001;
    }

    /**
     * A simple step handler that just throws an exception when it is called.
     */
    protected static class NoAutoDiffStepHandler extends StepHandler {

        @Override
        public void handleStep(StepInterpolator interpolator, int stepNumber)
                throws NumberIsTooSmallException {
            throw new NumberIsTooSmallException();
        }
    }

    /**
     * The equation to integrate.
     */
    private static final String equation = "y' = exp(y)";

    /**
     * The name of the test method.
     */
    private static final String NAME = "EmbeddedRungeKuttaIntegratorTest";
}