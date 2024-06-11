package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.Polynomials;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.DefaultValueEvent;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.EventManager;
import org.apache.commons.math3.ode.events.SimpleEvent;
import org.apache.commons.math3.ode.sampling.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.stepper.StepHandler;
import org.apache.commons.math3.special.BesselFunctions;
import org.junit.Before;
import org.junit.Test;

/**
 * Test case for {@link EmbeddedRungeKuttaIntegrator}.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    private final static int ORDER = 5;
    private final static double RELATIVE_TOL = 1.0e-8;
    private final static double ABSOLUTE_TOL = 1.0e-8;
    private final static double START_TIME = 0.0;
    private final static double END_TIME = 2 * Math.PI;
    private final static double[] IC = new double[] { 1.0, 1.0 };

    private EmbeddedRungeKuttaIntegrator integrator;
    private FirstOrderDifferentialEquations equation;

    @Before
    public void setUp() {
        integrator = new EmbeddedRungeKutta54Method();
        equation = new ExpFunction();
    }

    /**
     * Test the integrator with a simple differential equation.
     */
    @Test
    public void testSimple() {
        double[] y = integrator.integrate(equation, START_TIME, IC, END_TIME, new double[IC.length]);
        double[] expected = new double[] { Math.sin(END_TIME), Math.cos(END_TIME) };
        assertEquals(expected[0], y[0], RELATIVE_TOL, ABSOLUTE_TOL);
        assertEquals(expected[1], y[1], RELATIVE_TOL, ABSOLUTE_TOL);
    }

    /**
     * Test the integrator with an event.
     */
    @Test
    public void testEvent() {
        EventManager manager = new EventManager();
        manager.addRelatively(new SimpleEvent(Math.PI, new ExpFunction(), 2.0));
        StepHandler handler = (step, flag) -> {
            assertEquals(1.0, step.getY()[0], RELATIVE_TOL, ABSOLUTE_TOL);
            assertEquals(1.0, step.getY()[1], RELATIVE_TOL, ABSOLUTE_TOL);
        };
        integrator.setStepHandler(handler);
        double[] y = integrator.integrate(equation, START_TIME, IC, END_TIME, new double[IC.length]);
        assertEquals(2.0, y[1], RELATIVE_TOL, ABSOLUTE_TOL);
    }

    /**
     * Test the integrator with a differential equation with a singularity.
     */
    @Test
    public void testSingularity() {
        FirstOrderDifferentialEquations equations = new SingularFunction();
        assertThrows(IllegalArgumentException.class, () -> integrator.integrate(equations, START_TIME, IC, END_TIME, new double[IC.length]));
    }

    /**
     * Test the integrator with a non-fsal method.
     */
    @Test
    public void testNonFsal() {
        integrator = new EmbeddedRungeKuttaFehlberg78Method();
        double[] y = integrator.integrate(equation, START_TIME, IC, END_TIME, new double[IC.length]);
        double[] expected = new double[] { Math.sin(END_TIME), Math.cos(END_TIME) };
        assertEquals(expected[0], y[0], RELATIVE_TOL, ABSOLUTE_TOL);
        assertEquals(expected[1], y[1], RELATIVE_TOL, ABSOLUTE_TOL);
    }

    /**
     * Test the integrator with a differential equation with a discontinuous derivative.
     */
    @Test
    public void testDiscontinuousDerivative() {
        FirstOrderDifferentialEquations equations = new DiscontinuousFunction();
        assertThrows(IllegalArgumentException.class, () -> integrator.integrate(equations, START_TIME, IC, END_TIME, new double[IC.length]));
    }
}

class ExpFunction implements FirstOrderDifferentialEquations {

    @Override
    public int getDimension() {
        return 2;
    }

    @Override
    public void computeDerivatives(double t, double[] y, double[] yPrime) {
        yPrime[0] = -y[1];
        yPrime[1] = y[0];
    }
}

class SingularFunction implements FirstOrderDifferentialEquations {

    @Override
    public int getDimension() {
        return 1;
    }

    @Override
    public void computeDerivatives(double t, double[] y, double[] yPrime) {
        throw new IllegalArgumentException("Singularity at t = 0");
    }
}

class DiscontinuousFunction implements FirstOrderDifferentialEquations {

    @Override
    public int getDimension() {
        return 1;
    }

    @Override
    public void computeDerivatives(double t, double[] y, double[] yPrime) {
        if (Math.abs(y[0]) < 1.0) {
            yPrime[0] = Math.sin(t);
        } else {
            yPrime[0] = Math.cos(t);
        }
    }
}

class EmbeddedRungeKutta54Method extends EmbeddedRungeKuttaIntegrator {

    public EmbeddedRungeKutta54Method() {
        super("Embedded Runge-Kutta method 5(4)", true, new double[] { 0.3, 0.4, 0.1, 0.0, 0.2, 0.2 },
             new double[][] {{ 0.23692688, 0.0 }, { 0.12679722, 0.4 }, { 0.23692688, 0.0 }, { 0.1 },
             { 0.0, 0.3 }, { 0.171315, 0.0 }, { 0.3670811, 0.4 }, { 0.02886193, 0.4 }, { 0.0 } },
             new double[] { 0.23692688, 0.23692688, 0.02886193, 0.02886193, 0.1, 0.3670811, 0.171315, 0.1, 0.4 },
             new RungeKuttaStepInterpolator(), 1.0e-8, 1.0e-8, null, null);
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}

class EmbeddedRungeKuttaFehlberg78Method extends EmbeddedRungeKuttaIntegrator {

    public EmbeddedRungeKuttaFehlberg78Method() {
        super("Embedded Runge-Kutta method 7(8)", false, new double[] { 0.3, 0.4, 0, 0.3, 0.4, 0, 0.3, 0, 0.3, 0.4, 0, 0.3, 0.4, 0 },
             new double[][] {{ 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 },
             { 0.3, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 },
             { 0.0, 0.3, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 },
             { 0.0, 0.0, 0.3, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 },
             { 0.0, 0.0, 0.0, 0.3, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 },
             { 0.0, 0.0, 0.0, 0.0, 0.3, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 },
             { 0.0, 0.0, 0.0, 0.0, 0.0, 0.3, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 },
             { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.3, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 },
             { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.3, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 },
             { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.3, 0.0, 0.0, 0.0, 0.0, 0.0 } },
             new double[] { 0.3, 0.0 }, new RungeKuttaStepInterpolator(), 1.0e-8, 1.0e-8, null, null);
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}