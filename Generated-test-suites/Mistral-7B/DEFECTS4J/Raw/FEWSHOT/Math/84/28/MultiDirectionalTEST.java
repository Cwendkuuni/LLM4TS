package org.apache.commons.math.optimization.direct;

import static org.junit.Assert.*;

import org.junit.Test;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.real.RealPointValuePair;
import org.apache.commons.math3.optim.SimpleGoalType;
import org.apache.commons.math3.optim.nonlinear.function.GoalType;
import org.apache.commons.math3.optim.nonlinear.function.LinearFunction;
import org.apache.commons.math3.optim.nonlinear.function.LinearFunctionFoo;
import org.apache.commons.math3.optim.initializer.AveragedStartingPointInitializer;
import org.apache.commons.math3.optim.initializer.GaussianBallPointInitializer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeChecker;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeSorter;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunctionFoo;
import org.apache.commons.math3.optim.nonlinear.scalar.RealVector;
import org.apache.commons.math3.optim.nonlinear.scalar.RealVectorN;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearConstraint;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearConstraintCollection;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearObjectiveFunctionFoo;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LqRhs;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LqRhsCollection;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearOptimizationProblem;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearSolver;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.QPCG;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.SimplexSolver;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.SimplexSolver.TerminationType;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.SimplexTableau;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.SimplexTableauFoo;

public class MultiDirectionalTest {

    @Test
    public void testMultiDirectional() throws Exception {
        final LinearObjectiveFunctionFoo func = new LinearObjectiveFunctionFoo(-1.0, new double[]{-1.0, -1.0});
        final SimplexSolver solver = new SimplexSolver(new GoalTypeChecker(), new GoalTypeSorter());

        // check default constructor
        final MultiDirectional md1 = new MultiDirectional();
        final RealPointValuePair[] simplex1 = md1.optimize(func, new SimpleGoalType(), null, null, null);
        assertEquals(15, md1.getIterationsCounter());
        assertArrayEquals(new double[]{0.0, 0.0}, simplex1[0].getPointRef());
        assertNull(simplex1[0].getFunctionValue());

        // check constructor with specified coefficients
        final MultiDirectional md2 = new MultiDirectional(2.5, 0.25);
        final RealPointValuePair[] simplex2 = md2.optimize(func, new SimpleGoalType(), null, null, null);
        assertEquals(21, md2.getIterationsCounter());
        assertArrayEquals(new double[]{0.5, 0.5}, simplex2[0].getPointRef());
        assertNull(simplex2[0].getFunctionValue());

        // check optimization with a linear function and initial simplex
        final LinearFunctionFoo linFunc = new LinearFunctionFoo(new double[]{-1.0, -1.0}, new double[]{-2.0, -2.0});
        final double[] initial = new double[]{1.0, 1.0};
        final RealPointValuePair[] initialSimplex = new RealPointValuePair[]{
            new RealPointValuePair(new RealVectorN(2), initial),
            new RealPointValuePair(new RealVectorN(2), new double[]{-1.0, -1.0}),
            new RealPointValuePair(new RealVectorN(2), new double[]{1.0, 1.0})
        };
        final RealPointValuePair[] simplex3 = md2.optimize(linFunc, new SimpleGoalType(), new AveragedStartingPointInitializer(initial), null, null);
        assertEquals(13, md2.getIterationsCounter());
        assertArrayEquals(new double[]{0.0, 0.0}, simplex3[0].getPointRef());
        assertEquals(0.0, simplex3[0].getFunctionValue().getValue(), 1e-6);

        // check optimization with a linear function and Gaussian ball initial simplex
        final RealVectorN initialGaussian = new GaussianBallPointInitializer(0.1, new RealVectorN(2)).getInitialPoint();
        final RealPointValuePair[] simplex4 = md2.optimize(linFunc, new SimpleGoalType(), null, initialGaussian, null);
        assertEquals(15, md2.getIterationsCounter());
        assertArrayEquals(new double[]{0.0, 0.0}, simplex4[0].getPointRef());
        assertEquals(0.0, simplex4[0].getFunctionValue().getValue(), 1e-6);

        // check optimization with a linear function, linear constraints and linear objective
        final LinearConstraintCollection constraints = new LinearConstraintCollection();
        final LinearConstraint c1 = new LinearConstraint(new double[]{-1.0, -1.0}, 0.0);
        final LinearConstraint c2 = new LinearConstraint(new double[]{1.0, 1.0}, 0.0);
        constraints.add(c1);
        constraints.add(c2);
        final LinearObjectiveFunctionFoo linObj = new LinearObjectiveFunctionFoo(new double[]{2.0, 2.0}, new double[]{-3.0, -3.0});
        final LqRhsCollection rhs = new LqRhsCollection();
        final LqRhs r1 = new LqRhs(new double[]{1.0, 1.0}, new double[]{-1.0, -1.0});
        final LqRhs r2 = new LqRhs(new double[]{1.0, 1.0}, new double[]{1.0, 1.0});
        rhs.add(r1);
        rhs.add(r2);
        final LinearOptimizationProblem prob = new LinearOptimizationProblem(linObj, constraints, rhs);
        final LinearSolver solverLP = new QPCG();
        final SimplexTableauFoo simplexTableau = new SimplexTableauFoo();
        final LinearSolver solverMD = new SimplexSolver(new GoalTypeChecker(), new GoalTypeSorter(), solverLP, simplexTableau);
        final MultiDirectional mdLP = new MultiDirectional(2.0, 0.5);
        final RealPointValuePair[] simplex5 = mdLP.optimize(prob, new SimpleGoalType(), null, null, null);
        assertEquals(32, mdLP.getIterationsCounter());
        assertTrue(simplex5[0].getFunctionValue().getValue() < 0);

        // check optimization with a nonlinear function
        final ObjectiveFunction obj = new LinearFunction(new double[]{0.0, 0.0}, -1.0);
        final RealPointValuePair[] simplex6 = md2.optimize(obj, new SimpleGoalType(), null, null, null);
        fail("Should not be reached");

    }

}