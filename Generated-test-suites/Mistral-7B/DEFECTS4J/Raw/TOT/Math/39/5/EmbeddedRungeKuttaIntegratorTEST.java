package org.apache.commons.math.ode.nonstiff;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.Verifier;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKutta45Integrator;
import org.junit.Test;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {

    private static final double ABSOLUTE_TOLERANCE = 1.0e-10;
    private static final double RELATIVE_TOLERANCE = 1.0e-6;

    @Test
    public void testRungeKutta45Integrator() {
        EmbeddedRungeKuttaIntegrator integrator = new RungeKutta45Integrator("RK45", true);
        ExpandableStatefulODE ode = new Rk45TestFunction();
        double[] initialValues = {1.0};
        double startTime = 0.0;
        double endTime = 2.0 * Math.PI;

        integrator.integrate(ode, startTime, endTime, initialValues);

        double[] finalValues = ode.getCompleteState();
        Verifier.verify(ode, startTime, finalValues, ABSOLUTE_TOLERANCE, RELATIVE_TOLERANCE);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testStepSizeRejectionSmallStep() throws NumberIsTooSmallException {
        EmbeddedRungeKuttaIntegrator integrator = new RungeKutta45Integrator("RK45", true);
        ExpandableStatefulODE ode = new Rk45TestFunction();
        double[] initialValues = {1.0};
        double startTime = 0.0;
        double endTime = 0.1;
        integrator.setMinStep(0.001);
        integrator.integrate(ode, startTime, endTime, initialValues);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testStepSizeRejectionMaximumSteps() throws MaxCountExceededException {
        EmbeddedRungeKuttaIntegrator integrator = new RungeKutta45Integrator("RK45", true);
        ExpandableStatefulODE ode = new Rk45TestFunction();
        double[] initialValues = {1.0};
        double startTime = 0.0;
        double endTime = 2.0 * Math.PI;
        integrator.setMaxSteps(100);
        integrator.integrate(ode, startTime, endTime, initialValues);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testNegativeMinStep() {
        EmbeddedRungeKuttaIntegrator integrator = new RungeKutta45Integrator("RK45", true);
        integrator.setMinStep(-0.1);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testNegativeMaxStep() {
        EmbeddedRungeKuttaIntegrator integrator = new RungeKutta45Integrator("RK45", true);
        integrator.setMaxStep(-0.1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNonFsalMethod() {
        EmbeddedRungeKuttaIntegrator integrator = new RungeKuttaFehlberg78Integrator("RKF78", false);
        integrator.integrate(new Rk45TestFunction(), 0.0, 1.0, new double[1]);
    }

}