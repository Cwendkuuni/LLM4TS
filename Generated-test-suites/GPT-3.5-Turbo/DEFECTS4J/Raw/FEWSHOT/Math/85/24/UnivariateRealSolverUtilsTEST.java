package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.math.ConvergenceException;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class UnivariateRealSolverUtilsTest {

    private static final double EPSILON = 1e-10;

    private UnivariateRealFunction mockFunction;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Before
    public void setup() {
        mockFunction = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };
    }

    @Test
    public void testSolve() throws Exception {
        double result = UnivariateRealSolverUtils.solve(mockFunction, 0, 2);
        assertEquals(2, result, EPSILON);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolveWithConvergenceException() throws Exception {
        UnivariateRealSolverUtils.solve(mockFunction, 0, 2, EPSILON);
    }

    @Test
    public void testBracket() throws Exception {
        double[] result = UnivariateRealSolverUtils.bracket(mockFunction, 0, 1, 2);
        assertNotNull(result);
        assertEquals(1, result[0], EPSILON);
        assertEquals(2, result[1], EPSILON);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBracketWithIllegalArgumentException() throws Exception {
        UnivariateRealSolverUtils.bracket(null, 0, -1, 1);
    }

    @Test(expected = ConvergenceException.class)
    public void testBracketWithConvergenceException() throws Exception {
        UnivariateRealSolverUtils.bracket(mockFunction, 0, 1, 2, 1);
    }

    @Test
    public void testMidpoint() throws Exception {
        double result = UnivariateRealSolverUtils.midpoint(0, 2);
        assertEquals(1, result, EPSILON);
    }

    @Test
    public void testSetup() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x;
            }
        };

        // No exception should be thrown
        UnivariateRealSolverUtils.setup(f);

        exceptionRule.expect(IllegalArgumentException.class);
        UnivariateRealSolverUtils.setup(null);
        fail("Expected an IllegalArgumentException to be thrown");
    }

    @Test
    public void testLazyHolder() throws Exception {
        assertNotNull(UnivariateRealSolverUtils.LazyHolder.FACTORY);
        assertTrue(UnivariateRealSolverUtils.LazyHolder.FACTORY instanceof UnivariateRealSolverFactory);
    }

}