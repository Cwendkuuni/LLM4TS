package org.apache.commons.math3.optimization.general;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.NonLinearConstraint;
import org.apache.commons.math3.optim.nonlinear.scalar.NonLinearObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.NonLinearPointValuePair;
import org.apache.commons.math3.optim.nonlinear.scalar.NonLinearSolver;
import org.apache.commons.math3.optim.nonlinear.scalar.SimpleNonLinearConstraint;
import org.apache.commons.math3.optim.nonlinear.scalar.SimpleNonLinearObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.conjugate.ConjugateGradientFormula;
import org.apache.commons.math3.optim.nonlinear.scalar.preconditioner.IdentityPreconditioner;
import org.apache.commons.math3.optim.nonlinear.scalar.preconditioner.Preconditioner;
import org.junit.Test;

public class NonLinearConjugateGradientOptimizerTest {
    /**
     * Test constructor with default {@link SimpleValueChecker checker},
     * {@link BrentSolver line search solver} and
     * {@link IdentityPreconditioner preconditioner}.
     */
    @Test
    public void testDefaultConstructor() {
        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.FLETCHER_REEVES;
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula);
        assertNotNull(optimizer);
    }

    /**
     * Test constructor with default {@link SimpleValueChecker checker},
     * {@link BrentSolver line search solver} and
     * {@link IdentityPreconditioner preconditioner}.
     */
    @Test
    public void testDefaultConstructorWithCustomUpdateFormula() {
        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.POLAK_RIBIERE;
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula);
        assertNotNull(optimizer);
    }

    /**
     * Test constructor with custom {@link SimpleValueChecker checker},
     * {@link BrentSolver line search solver} and
     * {@link IdentityPreconditioner preconditioner}.
     */
    @Test
    public void testConstructorWithCustomChecker() {
        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.FLETCHER_REEVES;
        SimpleValueChecker checker = new SimpleValueChecker(1e-6);
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, checker);
        assertNotNull(optimizer);
    }

    /**
     * Test constructor with custom {@link SimpleValueChecker checker},
     * {@link BrentSolver line search solver},
     * {@link IdentityPreconditioner preconditioner} and custom {@link UnivariateSolver line search solver}.
     */
    @Test
    public void testConstructorWithCustomSolver() {
        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.POLAK_RIBIERE;
        SimpleValueChecker checker = new SimpleValueChecker(1e-6);
        BrentSolver lineSearchSolver = new BrentSolver();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, checker,
                lineSearchSolver);
        assertNotNull(optimizer);
    }

    /**
     * Test constructor with custom {@link SimpleValueChecker checker},
     * {@link BrentSolver line search solver},
     * {@link IdentityPreconditioner preconditioner} and custom {@link Preconditioner preconditioner}.
     */
    @Test
    public void testConstructorWithCustomPreconditioner() {
        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.FLETCHER_REEVES;
        SimpleValueChecker checker = new SimpleValueChecker(1e-6);
        BrentSolver lineSearchSolver = new BrentSolver();
        IdentityPreconditioner preconditioner = new IdentityPreconditioner();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, checker,
                lineSearchSolver, preconditioner);
        assertNotNull(optimizer);
    }

    /**
     * Test setInitialStep method.
     */
    @Test
    public void testSetInitialStep() {
        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.FLETCHER_REEVES;
        SimpleValueChecker checker = new SimpleValueChecker(1e-6);
        BrentSolver lineSearchSolver = new BrentSolver();
        IdentityPreconditioner preconditioner = new IdentityPreconditioner();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, checker,
                lineSearchSolver, preconditioner);

        optimizer.setInitialStep(2.0);
        assertEquals(2.0, optimizer.getInitialStep(), 1e-6);

        optimizer.setInitialStep(-1.0);
        assertEquals(1.0, optimizer.getInitialStep(), 1e-6);
    }

    /**
     * Test doOptimize method with a simple objective function.
     */
    @Test
    public void testDoOptimizeSimpleObjectiveFunction() {
        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.FLETCHER_REEVES;
        SimpleValueChecker checker = new SimpleValueChecker(1e-6);
        BrentSolver lineSearchSolver = new BrentSolver();
        IdentityPreconditioner preconditioner = new IdentityPreconditioner();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, checker,
                lineSearchSolver, preconditioner);

        NonLinearObjectiveFunction objectiveFunction = new SimpleNonLinearObjectiveFunction() {
            @Override
            public PointValuePair value(NonLinearPointValuePair initialPoint) {
                double x = initialPoint.getPoint()[0];
                return new NonPointValuePair(Math.pow(x, 2), Math.pow(x, 3));
            }
        };

        NonLinearPointValuePair initialPoint = new SimplePointValuePair(1.0);
        NonLinearPointValuePair result = optimizer.optimize(objectiveFunction, initialPoint);

        assertEquals(0.0, result.getValue(), 1e-6);
        assertEquals(0.0, result.getPoint()[0], 1e-6);
    }

    /**
     * Test doOptimize method with a simple objective function and custom constraints.
     */
    @Test
    public void testDoOptimizeSimpleObjectiveFunctionWithConstraints() {
        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.FLETCHER_REEVES;
        SimpleValueChecker checker = new SimpleValueChecker(1e-6);
        SimpleNonLinearConstraint constraint = new SimpleNonLinearConstraint("x >= 0") {
            @Override
            public int getNumberOfConstraints() {
                return 1;
            }

            @Override
            public boolean isSatisfied(NonLinearPointValuePair point) {
                double x = point.getPoint()[0];
                return x >= 0.0;
            }
        };
        BrentSolver lineSearchSolver = new BrentSolver();
        IdentityPreconditioner preconditioner = new IdentityPreconditioner();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, checker,
                lineSearchSolver, preconditioner);

        NonLinearObjectiveFunction objectiveFunction = new SimpleNonLinearObjectiveFunction() {
            @Override
            public PointValuePair value(NonLinearPointValuePair initialPoint) {
                double x = initialPoint.getPoint()[0];
                return new NonPointValuePair(Math.pow(x, 2), Math.pow(x, 3));
            }
        };

        NonLinearPointValuePair initialPoint = new SimplePointValuePair(1.0);
        NonLinearSolver solver = optimizer.optimize(objectiveFunction, initialPoint, new GoalType[] {GoalType.MINIMIZE},
                new NonLinearConstraint[] {constraint});

        assertEquals(0.0, solver.getValue(), 1e-6);
        assertEquals(0.0, solver.getPoint()[0], 1e-6);
        assertTrue(solver.isFeasible());
    }

    /**
     * Test doOptimize method with a complex objective function.
     */
    @Test
    public void testDoOptimizeComplexObjectiveFunction() {
        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.FLETCHER_REEVES;
        SimpleValueChecker checker = new SimpleValueChecker(1e-6);
        BrentSolver lineSearchSolver = new BrentSolver();
        IdentityPreconditioner preconditioner = new IdentityPreconditioner();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, checker,
                lineSearchSolver, preconditioner);

        NonLinearObjectiveFunction objectiveFunction = new SimpleNonLinearObjectiveFunction() {
            @Override
            public PointValuePair value(NonLinearPointValuePair initialPoint) {
                double[] point = initialPoint.getPoint();
                double x = point[0];
                double y = point[1];
                return new NonPointValuePair(Math.sin(x) + Math.cos(y), Math.sin(x * 2 + y * 2));
            }
        };

        NonLinearPointValuePair initialPoint = new SimplePointValuePair(0.0, 0.0);
        NonLinearPointValuePair result = optimizer.optimize(objectiveFunction, initialPoint);

        assertEquals(0.0, result.getValue(), 1e-6);
        assertEquals(Math.PI / 2, result.getPoint()[0], 1e-6);
        assertEquals(-Math.PI / 2, result.getPoint()[1], 1e-6);
    }

    /**
     * Test doOptimize method with a complex objective function and custom constraints.
     */
    @Test
    public void testDoOptimizeComplexObjectiveFunctionWithConstraints() {
        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.FLETCHER_REEVES;
        SimpleValueChecker checker = new SimpleValueChecker(1e-6);
        SimpleNonLinearConstraint constraint1 = new SimpleNonLinearConstraint("x >= 0") {
            @Override
            public int getNumberOfConstraints() {
                return 1;
            }

            @Override
            public boolean isSatisfied(NonLinearPointValuePair point) {
                double x = point.getPoint()[0];
                return x >= 0.0;
            }
        };
        SimpleNonLinearConstraint constraint2 = new SimpleNonLinearConstraint("y >= 0") {
            @Override
            public int getNumberOfConstraints() {
                return 1;
            }

            @Override
            public boolean isSatisfied(NonLinearPointValuePair point) {
                double y = point.getPoint()[1];
                return y >= 0.0;
            }
        };
        BrentSolver lineSearchSolver = new BrentSolver();
        IdentityPreconditioner preconditioner = new IdentityPreconditioner();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, checker,
                lineSearchSolver, preconditioner);

        NonLinearObjectiveFunction objectiveFunction = new SimpleNonLinearObjectiveFunction() {
            @Override
            public PointValuePair value(NonLinearPointValuePair initialPoint) {
                double[] point = initialPoint.getPoint();
                double x = point[0];
                double y = point[1];
                return new NonPointValuePair(Math.sin(x) + Math.cos(y), Math.sin(x * 2 + y * 2));
            }
        };

        NonLinearPointValuePair initialPoint = new SimplePointValuePair(0.0, 0.0);
        NonLinearSolver solver = optimizer.optimize(objectiveFunction, initialPoint,
                new GoalType[] {GoalType.MINIMIZE}, new NonLinearConstraint[] {constraint1, constraint2});

        assertEquals(0.0, solver.getValue(), 1e-6);
        assertEquals(Math.PI / 2, solver.getPoint()[0], 1e-6);
        assertEquals(Math.PI / 2, solver.getPoint()[1], 1e-6);
        assertTrue(solver.isFeasible());
    }

    /**
     * Test convergence checker.
     */
    @Test(expected = MathIllegalStateException.class)
    public void testConvergenceChecker() {
        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.FLETCHER_REEVES;
        SimpleValueChecker checker = new SimpleValueChecker(1e-6) {
            @Override
            public boolean isConverged(NonLinearPointValuePair current, NonLinearPointValuePair previous) {
                return false;
            }
        };
        BrentSolver lineSearchSolver = new BrentSolver();
        IdentityPreconditioner preconditioner = new IdentityPreconditioner();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, checker,
                lineSearchSolver, preconditioner);

        NonLinearObjectiveFunction objectiveFunction = new SimpleNonLinearObjectiveFunction() {
            @Override
            public PointValuePair value(NonLinearPointValuePair initialPoint) {
                return new NonPointValuePair(0.0, 0.0);
            }
        };

        NonLinearPointValuePair initialPoint = new SimplePointValuePair(0.0, 0.0);
        optimizer.optimize(objectiveFunction, initialPoint);
    }
}