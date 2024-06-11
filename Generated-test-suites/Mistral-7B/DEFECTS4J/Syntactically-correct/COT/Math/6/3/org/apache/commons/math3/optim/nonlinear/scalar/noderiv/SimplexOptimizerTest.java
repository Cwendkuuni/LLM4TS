package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.LinearConstraint;
import org.apache.commons.math3.optim.nonlinear.scalar.PointValuePairCollection;
import org.apache.commons.math3.optim.nonlinear.scalar.SimpleBounds;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Test class for SimplexOptimizer.
 */
public class SimplexOptimizerTest {

    private static final double EPSILON = 1e-9;
    private static final double RELATIVE_TOLERANCE = 1e-5;
    private static final double ABSOLUTE_TOLERANCE = 1e-5;

    private AbstractSimplex simplex;
    private SimplexOptimizer optimizer;

    @Before
    public void setUp() {
        simplex = new NelderMeadSimplex();
        optimizer = new SimplexOptimizer(new SimpleValueChecker(RELATIVE_TOLERANCE, ABSOLUTE_TOLERANCE));
    }

    /**
     * Test method for {@link SimplexOptimizer#SimplexOptimizer()}.
     */
    @Test
    public void testConstructor() {
        assertEquals("Default constructor should create an optimizer with a SimpleValueChecker",
                SimpleValueChecker.class, optimizer.getConvergenceChecker().getClass());
    }

    /**
     * Test method for {@link SimplexOptimizer#SimplexOptimizer(ConvergenceChecker)}.
     */
    @Test
    public void testConstructorWithCustomChecker() {
        ConvergenceChecker<PointValuePair> customChecker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
                // Custom convergence checker implementation
                return previous.getValue() > current.getValue() - EPSILON;
            }
        };
        optimizer = new SimplexOptimizer(customChecker);
        assertEquals("Custom constructor should create an optimizer with the provided custom ConvergenceChecker",
                customChecker.getClass(), optimizer.getConvergenceChecker().getClass());
    }

    /**
     * Test method for {@link SimplexOptimizer#SimplexOptimizer(double, double)}.
     */
    @Test
    public void testConstructorWithRelativeAndAbsoluteTolerance() {
        optimizer = new SimplexOptimizer(RELATIVE_TOLERANCE, ABSOLUTE_TOLERANCE);
        assertEquals("Constructor with relative and absolute tolerance should create an optimizer with a SimpleValueChecker",
                SimpleValueChecker.class, optimizer.getConvergenceChecker().getClass());
        assertEquals("Relative tolerance should be set correctly", RELATIVE_TOLERANCE, optimizer.getRelativeTolerance(), 0.0);
        assertEquals("Absolute tolerance should be set correctly", ABSOLUTE_TOLERANCE, optimizer.getAbsoluteTolerance(), 0.0);
    }

    /**
     * Test method for {@link SimplexOptimizer#setSimplex(AbstractSimplex)}.
     */
    @Test
    public void testSetSimplex() {
        MockAbstractSimplex mockSimplex = new MockAbstractSimplex();
        optimizer.setSimplex(mockSimplex);
        assertEquals("setSimplex method should set the AbstractSimplex property", mockSimplex, optimizer.getSimplex());
    }

    /**
     * Test method for {@link SimplexOptimizer#optimize(int, MultivariateFunction, GoalType, double[])}.
     */
    @Test
    public void testOptimize() {
        UnivariateFunction f = new PolynomialFunction(2, 1, -1, 4, 1, 0);
        PointValuePair result = optimizer.optimize(50, f, GoalType.MINIMIZE, new double[]{1.0, 1.0});
        PointValuePair expected = new PointValuePair(new double[]{1.0, 1.0}, -4.0);
        assertEquals("optimize method should return the expected result", expected, result);
    }

    /**
     * Test method for {@link SimplexOptimizer#optimize(int, MultivariateFunction, GoalType, double[], OptimizationData...)}.
     */
    @Test
    public void testOptimizeWithOptimizationData() {
        UnivariateFunction f = new PolynomialFunction(2, 1, -1, 4, 1, 0);
        double[] startPoint = new double[]{1.0, 1.0};
        AbstractSimplex simplex = new NelderMeadSimplex();
        PointValuePair result = optimizer.optimize(50, f, GoalType.MINIMIZE, startPoint, simplex);
        PointValuePair expected = new PointValuePair(new double[]{1.0, 1.0}, -4.0);
        assertEquals("optimize method with optimization data should return the expected result", expected, result);
        assertEquals("AbstractSimplex provided in optimization data should be used", simplex, optimizer.getSimplex());
    }

    /**
     * Test method for {@link SimplexOptimizer#optimize(int, MultivariateFunction, GoalType, OptimizationData[])}.
     */
    @Test
    public void testOptimizeWithOptimizationDataArray() {
        UnivariateFunction f = new PolynomialFunction(2, 1, -1, 4, 1, 0);
        double[] startPoint = new double[]{1.0, 1.0};
        OptimizationData optimizationData = new OptimizationData();
        optimizationData.setFunction(f);
        optimizationData.setInitialGuess(startPoint);
        optimizationData.setBounds(new SimpleBounds(new double[]{-10.0, 10.0}, new double[]{-10.0, 10.0}));
        optimizationData.setMaxEvaluations(50);
        optimizationData.setGoalType(GoalType.MINIMIZE);
        optimizationData.setSimplex(new NelderMeadSimplex());

        PointValuePair result = optimizer.optimize(optimizationData);
        PointValuePair expected = new PointValuePair(new double[]{1.0, 1.0}, -4.0);
        assertEquals("optimize method with optimization data array should return the expected result", expected, result);
        assertEquals("Function provided in optimization data should be used", f, optimizer.getFunction());
        assertEquals("Initial guess provided in optimization data should be used", startPoint, optimizer.getStartPoint());
        assertEquals("Maximum number of evaluations provided in optimization data should be used", 50, optimizer.getMaxEvaluations());
        assertEquals("Goal type provided in optimization data should be used", GoalType.MINIMIZE, optimizer.getGoalType());
        assertEquals("AbstractSimplex provided in optimization data should be used", new NelderMeadSimplex(), optimizer.getSimplex());
    }

    /**
     * Test method for edge cases and exceptions.
     */
    @Test(expected = NullArgumentException.class)
    public void testNullArgumentException() {
        optimizer.optimize(null, null, null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testNullPointerException() {
        optimizer.optimize(50, null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentException() {
        optimizer.optimize(-1, new PolynomialFunction(2, 1, -1, 4, 1, 0), null, new double[]{1.0, 1.0});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionWithNonPositiveMaxEvaluations() {
        optimizer.optimize(0, new PolynomialFunction(2, 1, -1, 4, 1, 0), null, new double[]{1.0, 1.0});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionWithNullGoalType() {
        optimizer.optimize(50, new PolynomialFunction(2, 1, -1, 4, 1, 0), null, new double[]{1.0, 1.0});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionWithStartPointNull() {
        optimizer.optimize(50, new PolynomialFunction(2, 1, -1, 4, 1, 0), GoalType.MINIMIZE, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionWithNullFunction() {
        optimizer.optimize(50, null, GoalType.MINIMIZE, new double[]{1.0, 1.0});
    }

    /**
     * Test method for {@link SimplexOptimizer#parseOptimizationData(OptimizationData...)}.
     */
    @Test
    public void testParseOptimizationData() {
        UnivariateFunction f = new PolynomialFunction(2, 1, -1, 4, 1, 0);
        double[] startPoint = new double[]{1.0, 1.0};
        OptimizationData optimizationData1 = new OptimizationData();
        optimizationData1.setFunction(f);
        optimizationData1.setInitialGuess(startPoint);
        OptimizationData optimizationData2 = new OptimizationData();
        optimizationData2.setFunction(f);
        optimizationData2.setInitialGuess(startPoint);
        optimizationData2.setBounds(new SimpleBounds(new double[]{-10.0, 10.0}, new double[]{-10.0, 10.0}));
        optimizationData2.setMaxEvaluations(50);
        optimizationData2.setGoalType(GoalType.MINIMIZE);
        optimizationData2.setSimplex(new NelderMeadSimplex());

        List<OptimizationData> optimizationDataList = Arrays.asList(optimizationData1, optimizationData2);
        optimizer.parseOptimizationData(optimizationDataList.toArray(new OptimizationData[0]));

        assertEquals("Function provided in optimization data should be used", f, optimizer.getFunction());
        assertEquals("Initial guess provided in optimization data should be used", startPoint, optimizer.getStartPoint());
        assertEquals("Maximum number of evaluations provided in optimization data should be used", 50, optimizer.getMaxEvaluations());
        assertEquals("Goal type provided in optimization data should be used", GoalType.MINIMIZE, optimizer.getGoalType());
        assertEquals("AbstractSimplex provided in optimization data should be used", new NelderMeadSimplex(), optimizer.getSimplex());
    }

    @Test
    public void testDoOptimize() {
        UnivariateFunction f = new PolynomialFunction(2, 1, -1, 4, 1, 0);
        PointValuePairCollection pvs = new PointValuePairCollection();
        double[] startPoint = new double[]{1.0, 1.0};
        simplex.build(startPoint);
        simplex.evaluate(f, comparator);

        optimizer.doOptimize();

        PointValuePair expected = simplex.getPoint(0);
        assertEquals("doOptimize method should return the expected result", expected, simplex.getBestPoint());
        assertEquals("Value of the best point should be the minimum value of the simplex", -4.0, simplex.getBestValue(), 0.0);
    }
}

class MockAbstractSimplex implements AbstractSimplex {
    @Override
    public void build(double[] startPoint) {
        // Empty implementation
    }

    @Override
    public void evaluate(MultivariateFunction multivariateFunction, Comparator<PointValuePair> comparator) {
        // Empty implementation
    }

    @Override
    public void iterate(MultivariateFunction multivariateFunction, Comparator<PointValuePair> comparator) {
        // Empty implementation
    }

    @Override
    public List<PointValuePair> getPoints() {
        return Arrays.asList(new PointValuePair[]{new PointValuePair(new double[]{0.0, 0.0}, 0.0), new PointValuePair(new double[]{1.0, 1.0}, 1.0)});
    }

    @Override
    public int getSize() {
        return 2;
    }

    @Override
    public PointValuePair getBestPoint() {
        return new PointValuePair(new double[]{0.0, 0.0}, 0.0);
    }

    @Override
    public double getBestValue() {
        return 0.0;
    }
}