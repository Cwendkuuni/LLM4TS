package org.apache.commons.math.ode.nonstiff.test;

import org.apache.commons.math.ode.AbstractIntegrator;
import org.apache.commons.math.ode.DerivativeException;
import org.apache.commons.math.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math.ode.IntegratorException;
import org.apache.commons.math.ode.events.CombinedEventsManager;
import org.apache.commons.math.ode.sampling.AbstractStepInterpolator;
import org.apache.commons.math.ode.sampling.DummyStepInterpolator;
import org.apache.commons.math.ode.sampling.StepHandler;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RungeKuttaIntegratorTest {

    private RungeKuttaIntegrator integrator;

    @Before
    public void setUp() {
        integrator = new RungeKuttaIntegrator() {
            public double integrate(FirstOrderDifferentialEquations equations, double t0, double[] y0, double t, double[] y) throws DerivativeException, IntegratorException {
                return 0;
            }
        };
    }

    @Test
    public void testIntegrate() {
        // TODO: write test for the integrate method
    }

}