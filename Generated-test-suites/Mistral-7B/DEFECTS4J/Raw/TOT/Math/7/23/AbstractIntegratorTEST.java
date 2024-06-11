package org.apache.commons.math3.ode.integrator;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.ode.AbstractIntegrator;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.sampling.AbstractStepInterpolator;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test suite for the AbstractIntegrator class.
 */
public class AbstractIntegratorTest {

    /**
     * Test the AbstractIntegrator with a simple test equation.
     * @throws Exception if an error occurred
     */
    @Test
    public void testSimpleEquation() throws Exception {

        // create a simple test equation
        final FirstOrderDifferentialEquations equations = new MyDifferentialEquations();

        // initialize integrator
        AbstractIntegrator integrator = new RungeKuttaFehlberg78Integrator();
        integrator.initIntegration(0.0, new double[] { 1.0, 1.0 }, 2.0);

        // perform integration
        integrator.integrate(equations, 2.0);

        // check results
        double expectedResult[] = new double[] { 2.718281828, 2.718281828 };
        assertArrayEquals(expectedResult, integrator.getState(), 0.0000001);

    }

    /**
     * Test the AbstractIntegrator with a non-simple test equation.
     * @throws Exception if an error occurred
     */
    @Test
    public void testNonSimpleEquation() throws Exception {

        // create a non-simple test equation
        final FirstOrderDifferentialEquations equations = new MyNonSimpleDifferentialEquations();

        // initialize integrator
        AbstractIntegrator integrator = new RungeKuttaFehlberg78Integrator();
        integrator.initIntegration(0.0, new double[] { 1.0, 1.0, 1.0 }, 2.0);

        // perform integration
        integrator.integrate(equations, 2.0);

        // check results
        double expectedResult1[] = new double[] { 1.6, 2.16, 1.21 };
        assertArrayEquals(expectedResult1, integrator.getState(), 0.0000001);

        // test for dimension mismatch
        try {
            integrator.integrate(new MyDifferentialEquationsWrongDimension(), 2.0);
            fail("Dimension mismatch exception expected");
        } catch (DimensionMismatchException e) {
            // expected
        }

        // test for max count exceeded exception
        try {
            integrator.setMaxEvaluations(0);
            integrator.integrate(equations, 2.0);
            fail("Max count exceeded exception expected");
        } catch (MaxCountExceededException e) {
            // expected
        }

    }

    /**
     * Test the AbstractIntegrator with an event.
     * @throws Exception if an error occurred
     */
    @Test
    public void testEvent() throws Exception {

        // create a test equation with an event
        final FirstOrderDifferentialEquations equations = new MyEventDifferentialEquations();
        final double eventTime = 1.0;

        // initialize integrator
        AbstractIntegrator integrator = new RungeKuttaFehlberg78Integrator();
        integrator.initIntegration(0.0, new double[] { 1.0, 1.0 }, eventTime);

        // add an event handler
        integrator.addEventHandler(new MyEventEventHandler(), 0.01, 1.0e-8, 10,
                                   new BracketingNthOrderBrentSolver(1.0e-8, 5));

        // perform integration
        integrator.integrate(equations, eventTime);

        // check results
        double expectedResult1[] = new double[] { 1.0, 1.0 };
        assertArrayEquals(expectedResult1, integrator.getState(), 0.0000001);
        assertEquals(1, integrator.getEvaluations());

        // test for no bracketing exception
        try {
            integrator.addEventHandler(new MyEventEventHandlerNoBracketing(), 0.01, 1.0e-8, 10,
                                       new BracketingNthOrderBrentSolver(1.0e-8, 5));
            fail("No bracketing exception expected");
        } catch (NoBracketingException e) {
            // expected
        }

    }

    /**
     * Test the AbstractIntegrator with step handlers.
     * @throws Exception if an error occurred
     */
    @Test
    public void testStepHandlers() throws Exception {

        // create a test equation
        final FirstOrderDifferentialEquations equations = new MyDifferentialEquations();

        // initialize integrator
        AbstractIntegrator integrator = new RungeKuttaFehlberg78Integrator();
        integrator.initIntegration(0.0, new double[] { 1.0, 1.0 }, 2.0);

        // add a step handler
        integrator.addStepHandler(new MyStepHandler());

        // perform integration
        integrator.integrate(equations, 2.0);

        // check results
        double expectedResult[] = new double[] { 2.718281828, 2.718281828 };
        assertArrayEquals(expectedResult, integrator.getState(), 0.0000001);

        // test for reset
        integrator.reset();

        // perform integration again
        integrator.integrate(equations, 2.0);

        // check results
        expectedResult = new double[] { 2.718281828, 2.718281828 };
        assertArrayEquals(expectedResult, integrator.getState(), 0.0000001);

    }

}