package org.apache.commons.math.ode.nonstiff;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {

    private EmbeddedRungeKuttaIntegrator integrator;
    private double[] initialCondition = {0.0, 1.0};
    private double startTime = 0.0;
    private double endTime = 10.0;
    private double tolerance = 1e-10;
    private double stepSize;

    @Before
    public void setup() {
        integrator = new RK23Integrator("RK23", false, new double[]{0.25, 0.5},
                new double[][]{new double[]{0.25}, new double[]{0.5, 0.25}},
                new double[]{0.25, 0.5}, new RK23StepInterpolator(), 0.01, 0.1, tolerance);
    }

    @Test
    public void testIntegrationWithTwoDegreePolynomial() throws Exception {
        FirstOrderDifferentialEquations equations = new PolynomialEquations(1.0, initialCondition);
        double result = integrator.integrate(equations, startTime, initialCondition, endTime, new double[initialCondition.length]);
        double exactResult = Math.exp(1.0) * (initialCondition[0] + initialCondition[1]);
        assertEquals(exactResult, result, tolerance);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testIntegrationWithTooManySteps() throws Exception {
        FirstOrderDifferentialEquations equations = new PolynomialEquations(1.0, initialCondition);
        integrator.setMaxSteps(100000);
        integrator.integrate(equations, startTime, initialCondition, endTime, new double[initialCondition.length]);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testIntegrationWithTooSmallStepSize() throws Exception {
        FirstOrderDifferentialEquations equations = new PolynomialEquations(1.0, initialCondition);
        integrator.setMinStep(1.0 / 1000.0);
        integrator.integrate(equations, startTime, initialCondition, endTime, new double[initialCondition.length]);
    }

    @Test(expected = IntegratorException.class)
    public void testIntegrationWithSingularMatrix() throws Exception {
        FirstOrderDifferentialEquations equations = new SingularMatrixEquations(initialCondition);
        integrator.integrate(equations, startTime, initialCondition, endTime, new double[initialCondition.length]);
    }

}