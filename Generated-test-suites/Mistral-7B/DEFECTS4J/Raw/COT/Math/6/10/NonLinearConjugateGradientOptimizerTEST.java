package org.apache.commons.math3.optimization.general;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.optimization.*;
import org.apache.commons.math3.optimization.general.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.IdentityHashMap;
import java.util.Map;

/**
 * Test cases for {@link NonLinearConjugateGradientOptimizer}.
 */
public class NonLinearConjugateGradientOptimizerTest {

    /**
     * Tested object.
     */
    private NonLinearConjugateGradientOptimizer optimizer;

    /**
     * Tested objective function.
     */
    private UnivariateFunction objectiveFunction;

    /**
     * Tested convergence checker.
     */
    private ConvergenceChecker<PointValuePair> convergenceChecker;

    /**
     * Tested solver for line search.
     */
    private UnivariateSolver lineSearchSolver;

    /**
     * Tested preconditioner.
     */
    private Preconditioner preconditioner;

    /**
     * Tested initial point.
     */
    private double[] initialPoint;

    /**
     * Tested goal type.
     */
    private GoalType goalType;

    /**
     * Tested maximum number of function evaluations.
     */
    private int maxEvaluations;

    /**
     * Set up test environment.
     * @throws Exception if an error occurs.
     */
    @Before
    public void setUp() throws Exception {
        // Set up objective function
        objectiveFunction = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) + Math.pow(Math.sin(x), 2);
            }

            @Override
            public double derive(double x) {
                return 2 * x + 2 * Math.cos(x);
            }
        };

        // Set up convergence checker
        convergenceChecker = new MaxIterations(1000);

        // Set up solver for line search
        lineSearchSolver = new BrentSolver();

        // Set up preconditioner
        preconditioner = new IdentityPreconditioner();

        // Set up optimizer
        optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                convergenceChecker,
                lineSearchSolver,
                preconditioner);

        // Set up initial point
        initialPoint = new double[]{-1};

        // Set up goal type
        goalType = GoalType.MINIMIZE;

        // Set up maximum number of function evaluations
        maxEvaluations = 100;
    }

    /**
     * Tear down test environment.
     */
    @After
    public void tearDown() {
        optimizer = null;
        objectiveFunction = null;
        convergenceChecker = null;
        lineSearchSolver = null;
        preconditioner = null;
        initialPoint = null;
        goalType = null;
        maxEvaluations = 0;
    }

    /**
     * Test constructor with default update formula and simple value checker.
     */
    @Test
    public void testConstructorWithDefaultUpdateFormulaAndSimpleValueChecker() {
        optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        Assert.assertSame(ConjugateGradientFormula.FLETCHER_REEVES, optimizer.getUpdateFormula());
        Assert.assertSame(new SimpleValueChecker(), optimizer.getChecker());
        Assert.assertNull(optimizer.getSolver());
        Assert.assertNull(optimizer.getPreconditioner());
        Assert.assertEquals(1.0, optimizer.getInitialStep(), 0.0);
    }

    /**
     * Test constructor with custom update formula, simple value checker,
     * Brent solver, and identity preconditioner.
     */
    @Test
    public void testConstructorWithCustomUpdateFormulaSimpleValueCheckerBrentSolverAndIdentityPreconditioner() {
        optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(),
                new BrentSolver(),
                new IdentityPreconditioner());
        Assert.assertSame(ConjugateGradientFormula.FLETCHER_REEVES, optimizer.getUpdateFormula());
        Assert.assertSame(new SimpleValueChecker(), optimizer.getChecker());
        Assert.assertSame(new BrentSolver(), optimizer.getSolver());
        Assert.assertSame(new IdentityPreconditioner(), optimizer.getPreconditioner());
        Assert.assertEquals(1.0, optimizer.getInitialStep(), 0.0);
    }

    /**
     * Test optimize method with default parameters.
     */
    @Test
    public void testOptimizeWithDefaultParameters() {
        optimizer.optimize(objectiveFunction, initialPoint, goalType, maxEvaluations);
        double[] result = optimizer.getPoints();
        double objectiveValue = optimizer.getValue();
        Assert.assertArrayEquals(new double[]{0.0}, result, 0.001);
        Assert.assertEquals(0.0, objectiveValue, 0.001);
    }

    /**
     * Test optimize method with custom parameters.
     */
    @Test
    public void testOptimizeWithCustomParameters() {
        optimizer.setUpdateFormula(ConjugateGradientFormula.POLAK_RIBIERE);
        optimizer.setChecker(new MaxIterations(50));
        optimizer.setSolver(new SecantSolver());
        optimizer.setPreconditioner(new DampedNewtonPreconditioner());
        optimizer.setInitialStep(0.5);
        optimizer.optimize(objectiveFunction, initialPoint, goalType, maxEvaluations);
        double[] result = optimizer.getPoints();
        double objectiveValue = optimizer.getValue();
        Assert.assertNotSame(new double[]{0.0}, result);
        Assert.assertNotEquals(0.0, objectiveValue, 0.001);
    }

    /**
     * Test setInitialStep method.
     */
    @Test
    public void testSetInitialStep() {
        optimizer.setInitialStep(-1.5);
        Assert.assertEquals(1.0, optimizer.getInitialStep(), 0.0);
    }

    /**
     * Test computeObjectiveGradient method with null point.
     */
    @Test(expected = MathIllegalStateException.class)
    public void testComputeObjectiveGradientWithNullPoint() {
        optimizer.computeObjectiveGradient(null);
    }

    /**
     * Test computeObjectiveGradient method with valid point.
     */
    @Test
    public void testComputeObjectiveGradientWithValidPoint() {
        double[] point = new double[]{1.5};
        double[] gradient = optimizer.computeObjectiveGradient(point);
        Assert.assertArrayEquals(new double[]{2 * 1.5 + 2 * Math.cos(1.5), 2 * Math.cos(1.5)}, gradient, 0.001);
    }

    /**
     * Test computeObjectiveValue method with null point.
     */
    @Test(expected = MathIllegalStateException.class)
    public void testComputeObjectiveValueWithNullPoint() {
        optimizer.computeObjectiveValue(null);
    }

    /**
     * Test computeObjectiveValue method with valid point.
     */
    @Test
    public void testComputeObjectiveValueWithValidPoint() {
        double[] point = new double[]{1.5};
        double objectiveValue = optimizer.computeObjectiveValue(point);
        Assert.assertEquals(Math.pow(1.5, 2) + Math.pow(Math.sin(1.5), 2), objectiveValue, 0.001);
    }

    /**
     * Test getUpdateFormula method.
     */
    @Test
    public void testGetUpdateFormula() {
        Assert.assertSame(ConjugateGradientFormula.FLETCHER_REEVES, optimizer.getUpdateFormula());
    }

    /**
     * Test setUpdateFormula method.
     */
    @Test
    public void testSetUpdateFormula() {
        optimizer.setUpdateFormula(ConjugateGradientFormula.POLAK_RIBIERE);
        Assert.assertSame(ConjugateGradientFormula.POLAK_RIBIERE, optimizer.getUpdateFormula());
    }

    /**
     * Test getChecker method.
     */
    @Test
    public void testGetChecker() {
        Assert.assertSame(new SimpleValueChecker(), optimizer.getChecker());
    }

    /**
     * Test setChecker method.
     */
    @Test
    public void testSetChecker() {
        ConvergenceChecker<PointValuePair> newChecker = new MaxIterations(50);
        optimizer.setChecker(newChecker);
        Assert.assertSame(newChecker, optimizer.getChecker());
    }

    /**
     * Test getSolver method.
     */
    @Test
    public void testGetSolver() {
        Assert.assertSame(new BrentSolver(), optimizer.getSolver());
    }

    /**
     * Test setSolver method.
     */
    @Test
    public void testSetSolver() {
        UnivariateSolver newSolver = new SecantSolver();
        optimizer.setSolver(newSolver);
        Assert.assertSame(newSolver, optimizer.getSolver());
    }

    /**
     * Test getPreconditioner method.
     */
    @Test
    public void testGetPreconditioner() {
        Assert.assertSame(new IdentityPreconditioner(), optimizer.getPreconditioner());
    }

    /**
     * Test setPreconditioner method.
     */
    @Test
    public void testSetPreconditioner() {
        Preconditioner newPreconditioner = new DampedNewtonPreconditioner();
        optimizer.setPreconditioner(newPreconditioner);
        Assert.assertSame(newPreconditioner, optimizer.getPreconditioner());
    }

    /**
     * Test getInitialStep method.
     */
    @Test
    public void testGetInitialStep() {
        Assert.assertEquals(1.0, optimizer.getInitialStep(), 0.0);
    }

    /**
     * Test setInitialStep method with invalid argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetInitialStepWithInvalidArgument() {
        optimizer.setInitialStep(-1);
    }

    /**
     * Test getMaxEvaluations method.
     */
    @Test
    public void testGetMaxEvaluations() {
        Assert.assertEquals(100, optimizer.getMaxEvaluations());
    }

    /**
     * Test setMaxEvaluations method.
     */
    @Test
    public void testSetMaxEvaluations() {
        optimizer.setMaxEvaluations(200);
        Assert.assertEquals(200, optimizer.getMaxEvaluations());
    }

    /**
     * Test getPoints method.
     */
    @Test
    public void testGetPoints() {
        optimizer.optimize(objectiveFunction, initialPoint, goalType, maxEvaluations);
        double[] result = optimizer.getPoints();
        Assert.assertArrayNotEquals(new double[]{0.0}, result);
    }

    /**
     * Test getValue method.
     */
    @Test
    public void testGetValue() {
        optimizer.optimize(objectiveFunction, initialPoint, goalType, maxEvaluations);
        double objectiveValue = optimizer.getValue();
        Assert.assertNotEquals(0.0, objectiveValue, 0.001);
    }

    /**
     * Test getStatus method.
     */
    @Test
    public void testGetStatus() {
        optimizer.optimize(objectiveFunction, initialPoint, goalType, maxEvaluations);
        OptimizationStatus status = optimizer.getStatus();
        Assert.assertNotNull(status);
        Assert.assertEquals(OptimizationStatus.OPTIMAL, status.getState());
    }
}