package org.apache.commons.math.ode.nonstiff;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.DefaultValueEvent;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.EventInterpreter;
import org.apache.commons.math3.ode.events.OneTimeEvent;
import org.apache.commons.math3.ode.sampling.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.testchampions.TestChampion5;
import org.apache.commons.math3.ode.testchampions.TestChampion6;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for {@link EmbeddedRungeKuttaIntegrator}.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testBasicFunctionality() {
        double[] y = {0.0, 0.0};
        double  t = 1.0;
        FirstOrderDifferentialEquations equations = new TestChampion5();
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaMethod1(false);
        double result = integrator.integrate(equations, 0.0, y, t, new double[2]);
        Assert.assertEquals(t, result, 1e-12);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testNegativeMinStep() {
        double[] y = {0.0, 0.0};
        double  t = 1.0;
        FirstOrderDifferentialEquations equations = new TestChampion6();
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaMethod1(false);
        integrator.setMinStep(-1.0);
        integrator.integrate(equations, 0.0, y, t, new double[2]);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testZeroMinStep() {
        double[] y = {0.0, 0.0};
        double  t = 1.0;
        FirstOrderDifferentialEquations equations = new TestChampion6();
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaMethod1(false);
        integrator.setMinStep(0.0);
        integrator.integrate(equations, 0.0, y, t, new double[2]);
    }

    @Test
    public void testEdgeCases() {
        double[] y = {0.0, 0.0};
        double  t = 0.0;
        FirstOrderDifferentialEquations equations = new TestChampion5();
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaMethod1(false);
        integrator.setMinStep(0.001);
        integrator.setMaxStep(1.0);
        integrator.integrate(equations, t, y, t + 1.0, new double[2]);
    }

    @Test
    public void testEventHandling() {
        double[] y = {0.0, 0.0};
        double  t = 0.0;
        double[] yEvent = {1.0, 1.0};
        FirstOrderDifferentialEquations equations = new TestChampion5();
        EventInterpreter interpreter = new EventInterpreter();
        interpreter.addEvent(new OneTimeEvent(1.0, new DefaultValueEvent(yEvent)));
        equations.setEventInterpreter(interpreter);
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaMethod1(false);
        integrator.setMinStep(0.001);
        integrator.setMaxStep(1.0);
        integrator.integrate(equations, t, y, 1.5, new double[2]);
        Assert.assertArrayEquals(yEvent, y, 1e-12);
    }

    @Test(expected = IntegratorException.class)
    public void testEventRejection() {
        double[] y = {0.0, 0.0};
        double  t = 0.0;
        double[] yEvent = {1.0, 1.0};
        FirstOrderDifferentialEquations equations = new TestChampion5();
        EventInterpreter interpreter = new EventInterpreter();
        interpreter.addEvent(new OneTimeEvent(0.5, new DefaultValueEvent(yEvent)));
        equations.setEventInterpreter(interpreter);
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaMethod1(false);
        integrator.setMinStep(0.001);
        integrator.setMaxStep(1.0);
        integrator.integrate(equations, t, y, 0.6, new double[2]);
    }

    @Test
    public void testFSALMethodFunctionality() {
        double[] y = {0.0, 0.0};
        double  t = 1.0;
        FirstOrderDifferentialEquations equations = new TestChampion5();
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaMethod2(true);
        double result = integrator.integrate(equations, 0.0, y, t, new double[2]);
        Assert.assertEquals(t, result, 1e-12);
    }

}