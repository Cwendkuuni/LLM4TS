package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.PointValuePairList;
import org.apache.commons.math3.optim.nonlinear.scalar.SimplexSimplex;
import org.apache.commons.math3.optim.nonlinear.vector.LinearConstraint;
import org.apache.commons.math3.optim.nonlinear.vector.LinearConstraintSet;
import org.apache.commons.math3.optim.nonlinear.vector.LinearObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.vector.linear.AffineFunction;
import org.apache.commons.math3.optim.nonlinear.vector.linear.LinearConstraintFactory;
import org.apache.commons.math3.optim.nonlinear.vector.linear.LinearObjectiveFunctionFactory;
import org.apache.commons.math3.optim.nonlinear.vector.linear.LinOps;
import org.junit.Before;
import org.junit.Test;

public class SimplexOptimizerTest {

    private static final double EPSILON = 1e-14;
    private static final double DELTA = 1e-8;

    private ObjectiveFunction objFunc;
    private PointValuePair optPoint;
    private PointValuePairList pvl;
    private SimplexOptimizer optimizer;
    private UnivariateOptimizer simplex;
    private AbstractSimplex simplexAlgorithm;

    @Before
    public void setup() {
        objFunc = new PolynomialFunction(new double[]{-1, 4, -1, 0}, 4);
        optimizer = new SimplexOptimizer(new SimpleValueChecker(EPSILON, DELTA));
        simplex = new NelderMeadSimplex(new SimpleValueChecker(EPSILON, DELTA), 4);
        simplexAlgorithm = new SimplexSimplex();

        pvl = new PointValuePairList();
    }

    @Test
    public void testEmptyConstructor() {
        try {
            new SimplexOptimizer();
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testConstructorWithChecker() {
        optimizer = new SimplexOptimizer(new SimpleValueChecker());
        assertNotNull(optimizer.getConvergenceChecker());
    }

    @Test
    public void testConstructorWithRelativeAbsThreshold() {
        optimizer = new SimplexOptimizer(1e-2, 1e-4);
        assertNotNull(optimizer.getConvergenceChecker());
        assertEquals(1e-2, optimizer.getConvergenceChecker().getRelativeThreshold(), 1e-6);
        assertEquals(1e-4, optimizer.getConvergenceChecker().getAbsoluteThreshold(), 1e-6);
    }

    @Test
    public void testSetSimplexNull() {
        optimizer.setSimplex(null);
        try {
            optimizer.optimize(1, objFunc, GoalType.MINIMIZE, new double[0]);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testSetSimplex() {
        optimizer.setSimplex(simplexAlgorithm);
        assertEquals(simplexAlgorithm, optimizer.getSimplex());
    }

    @Test
    public void testSetSimplexWrongType() {
        optimizer.setSimplex(new NelderMeadSimplex(new SimpleValueChecker(), 2));
        try {
            optimizer.optimize(1, objFunc, GoalType.MINIMIZE, new double[4]);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testOptimizeWithNullFunction() {
        try {
            optimizer.optimize(1, null, GoalType.MINIMIZE, new double[4]);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testOptimizeWithWrongGoalType() {
        try {
            optimizer.optimize(1, objFunc, GoalType.MAXIMIZE, new double[4]);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testOptimizeWithEmptyData() {
        try {
            optimizer.optimize(1, objFunc, GoalType.MINIMIZE, new double[0]);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testOptimizeWithInitialGuess() {
        double[] initialGuess = new double[]{1.0, 0.0, 1.0, 0.0};
        double[] startPoint = new double[]{0.0, 0.0, 0.0, 0.0};
        double[] optPointExpected = new double[]{0.0, 0.0, 0.0, 0.0};
        double[] constraints = new double[]{0.0, 0.0, 0.0, 0.0};
        int maxEval = 100;
        PointValuePair optPointActual = optimizer.optimize(maxEval, objFunc, GoalType.MINIMIZE,
                new Object[][]{new Object[]{initialGuess},
                        new Object[]{simplexAlgorithm},
                        new Object[]{new LinearConstraintSet(new LinearConstraint[0])},
                        new Object[]{startPoint}});

        assertEquals(optPointExpected, optPointActual.getPoint());
        assertEquals(objFunc.value(optPointExpected), optPointActual.getValue(), EPSILON);
    }

    @Test
    public void testOptimizeWithSimpleSimplex() {
        double[] initialGuess = new double[]{1.0, 1.0, 0.0, 0.0};
        double[] startPoint = new double[]{0.5, 0.5, 0.0, 0.0};
        double[] optPointExpected = new double[]{0.0, 0.0, 0.0, 0.0};
        double[] constraints = new double[]{0.0, 0.0, 0.0, 0.0};
        int maxEval = 100;

        optimizer.setSimplex(new SimplexSimplex());
        PointValuePair optPointActual = optimizer.optimize(maxEval, objFunc, GoalType.MINIMIZE,
                new Object[][]{new Object[]{initialGuess},
                        new Object[]{optimizer},
                        new Object[]{new LinearConstraintSet(new LinearConstraint[0])},
                        new Object[]{startPoint}});

        assertEquals(optPointExpected, optPointActual.getPoint(), EPSILON);
        assertEquals(objFunc.value(optPointExpected), optPointActual.getValue(), EPSILON);
    }

    @Test
    public void testOptimizeWithNelderMeadSimplex() {
        double[] initialGuess = new double[]{1.0, 1.0, 0.0, 0.0};
        double[] startPoint = new double[]{0.5, 0.5, 0.0, 0.0};
        double[] optPointExpected = new double[]{0.1469229014361037, 0.2447436323361311,
                0.6387254726981643, 1.1220701352691876};
        double[] constraints = new double[]{0.0, 0.0, 0.0, 0.0};
        int maxEval = 100;

        optimizer.setSimplex(simplex);
        optimizer.setInitialGuess(initialGuess);
        optimizer.setStartPoint(startPoint);
        optimizer.optimize(maxEval, objFunc, GoalType.MINIMIZE, new Object[0]);

        assertEquals(optPointExpected, optimizer.getLastBestPoint(), EPSILON);
        assertEquals(objFunc.value(optPointExpected), optimizer.getFunctionValue(), EPSILON);
    }

    @Test
    public void testOptimizeWithLinearConstraints() {
        double[] initialGuess = new double[]{1.0, 1.0, 0.0, 0.0};
        double[] startPoint = new double[]{0.5, 0.5, 0.0, 0.0};
        double[] optPointExpected = new double[]{0.1469229014361037, 0.2447436323361311,
                0.6387254726981643, 1.1220701352691876};
        double[] constraints = new double[]{0.0, 1.0, 0.0, 0.0};
        int maxEval = 100;

        optimizer.setSimplex(simplex);
        optimizer.setInitialGuess(initialGuess);
        optimizer.setStartPoint(startPoint);
        optimizer.setConstraints(new LinearConstraintSet(new LinearConstraint[]{
                LinearConstraintFactory.createLessOrEqual(new AffineFunction(new double[]{-1, 0, 0, 0}, 0), 0),
                LinearConstraintFactory.createLessOrEqual(new AffineFunction(new double[]{0, -1, 0, 0}, 0), 1)}));
        optimizer.optimize(maxEval, objFunc, GoalType.MINIMIZE, new Object[0]);

        assertEquals(optPointExpected, optimizer.getLastBestPoint(), EPSILON);
        assertEquals(objFunc.value(optPointExpected), optimizer.getFunctionValue(), EPSILON);
    }

    @Test
    public void testOptimizeWithConstraintViolated() {
        double[] initialGuess = new double[]{1.0, 1.0, 0.0, 0.0};
        double[] startPoint = new double[]{0.5, 0.5, 0.0, 0.0};
        double[] optPointExpected = new double[]{0.1469229014361037, 0.2447436323361311,
                0.6387254726981643, 1.1220701352691876};
        double[] constraints = new double[]{0.0, 1.0, 0.0, -1.0};
        int maxEval = 100;

        optimizer.setSimplex(simplex);
        optimizer.setInitialGuess(initialGuess);
        optimizer.setStartPoint(startPoint);
        optimizer.setConstraints(new LinearConstraintSet(new LinearConstraint[]{
                LinearConstraintFactory.createLessOrEqual(new AffineFunction(new double[]{-1, 0, 0, 0}, 0), 0),
                LinearConstraintFactory.createLessOrEqual(new AffineFunction(new double[]{0, -1, 0, 0}, 0), 1),
                LinearConstraintFactory.createLessOrEqual(new AffineFunction(new double[]{0, 0, -1, 0}, 0), -1)}));

        try {
            optimizer.optimize(maxEval, objFunc, GoalType.MINIMIZE, new Object[0]);
            fail("Expected ConstraintViolationException");
        } catch (ConstraintViolationException e) {
            // Expected
        }
    }

    @Test
    public void testOptimizeWithPenaltyFunction() {
        double[] initialGuess = new double[]{1.0, 1.0, 0.0, 0.0};
        double[] startPoint = new double[]{0.5, 0.5, 0.0, 0.0};
        double[] optPointExpected = new double[]{0.1469229014361037, 0.2447436323361311,
                0.6387254726981643, 1.1220701352691876};
        double[] constraints = new double[]{0.0, 1.0, 0.0, 0.0};
        int maxEval = 100;

        double penalty = 1000.0;
        MultivariateFunction penaltyFunc = new LinearObjectiveFunction(new double[]{-penalty, -penalty, -penalty, -penalty});

        optimizer.setSimplex(simplex);
        optimizer.setInitialGuess(initialGuess);
        optimizer.setStartPoint(startPoint);
        optimizer.setConvergenceChecker(new SimpleValueChecker(EPSILON, DELTA));
        optimizer.setPenaltyFunction(new MultivariateFunctionMappingAdapter(penaltyFunc));
        optimizer.optimize(maxEval, objFunc, GoalType.MINIMIZE, new Object[0]);

        assertEquals(optPointExpected, optimizer.getLastBestPoint(), EPSILON);
        assertEquals(objFunc.value(optPointExpected), optimizer.getFunctionValue(), EPSILON);
    }

}