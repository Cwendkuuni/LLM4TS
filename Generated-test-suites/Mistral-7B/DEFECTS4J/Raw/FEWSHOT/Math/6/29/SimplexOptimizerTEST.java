package org.apache.commons.cli.optimization.direct;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.goaltype.GoalType;
import org.apache.commons.math3.optim.nonlinear.functions.PolynomialFunction;
import org.apache.commons.math3.optim.nonlinear.functions.SimpleFunction;
import org.apache.commons.math3.optim.simpledirect.GoalTypeSensitivity;
import org.apache.commons.math3.optim.simpledirect.SimpleValueChecker;
import org.apache.commons.math3.optim.simpledirect.SimpleXOptimizer;
import org.apache.commons.math3.optim.simpledirect.AbstractSimplex;
import org.junit.Test;

public class SimplexOptimizerTest {

    @Test
    public void testSetSimplex() {
        AbstractSimplex simplex = new DummySimplex();
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.setSimplex(simplex);
        assertSame(simplex, optimizer.getSimplex());
    }

    @Test(expected = NullArgumentException.class)
    public void testOptimizeNullSimplex() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.optimize(1, new SimpleFunction(0), GoalType.MINIMIZE, (Object[]) null);
    }

    @Test
    public void testOptimizeMinimize() {
        SimpleFunction f = new SimpleFunction("f(x, y)", x -> x * x + y * y);
        PointValuePair opt = new PointValuePair(new double[]{1.0, 1.0}, 2.0);
        AbstractSimplex simplex = new DummySimplex();
        SimpleXOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker());
        optimizer.setSimplex(simplex);
        optimizer.optimize(10, f, GoalType.MINIMIZE, opt.getPoint());
        assertEquals(opt.getPoint(), optimizer.getPointValuePair().getPoint());
        assertEquals(opt.getValue(), optimizer.getPointValuePair().getValue(), 1e-6);
    }

    @Test
    public void testOptimizeMaximize() {
        SimpleFunction f = new SimpleFunction("f(x, y)", x -> -x * x - y * y);
        PointValuePair opt = new PointValuePair(new double[]{1.0, 1.0}, -2.0);
        AbstractSimplex simplex = new DummySimplex();
        SimpleXOptimizer optimizer = new SimplexOptimizer(GoalTypeSensitivity.MAXIMIZE, new SimpleValueChecker());
        optimizer.setSimplex(simplex);
        optimizer.optimize(10, f, GoalType.MAXIMIZE, opt.getPoint());
        assertEquals(opt.getPoint(), optimizer.getPointValuePair().getPoint());
        assertEquals(opt.getValue(), optimizer.getPointValuePair().getValue(), 1e-6);
    }

    @Test
    public void testOptimizeGoalTypeSensitivity() {
        PolynomialFunction f = new PolynomialFunction(3, 2, new double[]{-1.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0});
        PointValuePair opt = new PointValuePair(new double[]{0.5, 1.5}, 2.25);
        AbstractSimplex simplex = new DummySimplex();
        SimpleXOptimizer optimizer = new SimplexOptimizer(GoalTypeSensitivity.MINIMIZE, new SimpleValueChecker());
        optimizer.setSimplex(simplex);
        optimizer.optimize(10, f, GoalTypeSensitivity.MAXIMIZE, new InitialGuess[0]);
        assertEquals(opt.getPoint(), optimizer.getPointValuePair().getPoint(), 1e-6);
        assertEquals(opt.getValue(), optimizer.getPointValuePair().getValue(), 1e-6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeInvalidGoalType() {
        SimpleFunction f = new SimpleFunction("f(x, y)", x -> x * x + y * y);
        AbstractSimplex simplex = new DummySimplex();
        SimpleXOptimizer optimizer = new SimplexOptimizer(GoalType.MAXIMIZE, new SimpleValueChecker());
        optimizer.setSimplex(simplex);
        optimizer.optimize(10, f, null, new double[0]);
    }

    @Test
    public void testOptimizeNullOptData() {
        SimpleFunction f = new SimpleFunction("f(x, y)", x -> x * x + y * y);
        AbstractSimplex simplex = new DummySimplex();
        SimpleXOptimizer optimizer = new SimplexOptimizer(GoalType.MINIMIZE, new SimpleValueChecker());
        optimizer.optimize(10, f, GoalType.MINIMIZE, (Object[]) null);
        assertNull(optimizer.getPointValuePair());
    }

    @Test
    public void testOptimizeWithInitialGuess() {
        SimpleFunction f = new SimpleFunction("f(x, y)", x -> x * x + y * y);
        PointValuePair opt = new PointValuePair(new double[]{1.0, 1.0}, 2.0);
        AbstractSimplex simplex = new DummySimplex();
        SimpleXOptimizer optimizer = new SimplexOptimizer(GoalType.MINIMIZE, new SimpleValueChecker());
        optimizer.setSimplex(simplex);
        optimizer.optimize(10, f, GoalType.MINIMIZE, new Object[]{new InitialGuess[]{new InitialGuess(new double[]{1.5, 0.5})}});
        assertEquals(opt.getPoint(), optimizer.getPointValuePair().getPoint(), 1e-6);
        assertEquals(opt.getValue(), optimizer.getPointValuePair().getValue(), 1e-6);
    }

    @Test
    public void testOptimizeWithSimplexInitialization() {
        SimpleFunction f = new SimpleFunction("f(x, y)", x -> x * x + y * y);
        PointValuePair opt = new PointValuePair(new double[]{1.0, 1.0}, 2.0);
        AbstractSimplex simplex = new DummySimplex();
        simplex.build(new double[]{1.5, 0.5});
        SimpleXOptimizer optimizer = new SimplexOptimizer(GoalType.MINIMIZE, new SimpleValueChecker());
        optimizer.optimize(10, f, GoalType.MINIMIZE, opt.getPoint());
        assertEquals(opt.getPoint(), optimizer.getPointValuePair().getPoint(), 1e-6);
        assertEquals(opt.getValue(), optimizer.getPointValuePair().getValue(), 1e-6);
    }

    @Test
    public void testOptimizeWithMaxEval() {
        SimpleFunction f = new SimpleFunction("f(x, y)", x -> x * x + y * y);
        PointValuePair opt = new PointValuePair(new double[]{1.0, 1.0}, 2.0);
        AbstractSimplex simplex = new DummySimplex();
        SimpleXOptimizer optimizer = new SimplexOptimizer(GoalType.MINIMIZE, new SimpleValueChecker());
        optimizer.setSimplex(simplex);
        optimizer.optimize(5, f, GoalType.MINIMIZE, opt.getPoint());
        assertEquals(opt.getPoint(), optimizer.getPointValuePair().getPoint(), 1e-6);
        assertEquals(opt.getValue(), optimizer.getPointValuePair().getValue(), 1e-6);
    }

    @Test(expected = MaxEval.ReachedException.class)
    public void testOptimizeMaxEval() {
        SimpleFunction f = new SimpleFunction("f(x, y)", x -> x * x + y * y);
        AbstractSimplex simplex = new DummySimplex();
        SimpleXOptimizer optimizer = new SimplexOptimizer(GoalType.MINIMIZE, new SimpleValueChecker());
        optimizer.setSimplex(simplex);
        optimizer.optimize(1, f, GoalType.MINIMIZE, new double[0]);
    }

    @Test
    public void testOptimizeConvergenceChecker() {
        SimpleFunction f = new SimpleFunction("f(x, y)", x -> Math.pow(x * x + y * y, 0.5));
        PointValuePair opt = new PointValuePair(new double[]{1.0, 1.0}, 2.0);
        AbstractSimplex simplex = new DummySimplex();
        SimpleXOptimizer optimizer = new SimplexOptimizer(GoalType.MINIMIZE, new SimpleValueChecker());
        optimizer.setSimplex(simplex);
        optimizer.setConvergenceChecker(new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
                double[] p1 = previous.getPoint();
                double[] p2 = current.getPoint();
                return FunctionUtils.max(Math.abs(p1[0] - p2[0]), Math.abs(p1[1] - p2[1])) < 1e-5;
            }
        });
        optimizer.optimize(10, f, GoalType.MINIMIZE, opt.getPoint());
        assertEquals(opt.getPoint(), optimizer.getPointValuePair().getPoint(), 1e-6);
        assertEquals(opt.getValue(), optimizer.getPointValuePair().getValue(), 1e-6);
    }

    @Test
    public void testOptimizeWithBadData() {
        SimpleFunction f = new SimpleFunction("f(x, y)", x -> x * x + y * y);
        AbstractSimplex simplex = new DummySimplex();
        SimpleXOptimizer optimizer = new SimplexOptimizer(GoalType.MINIMIZE, new SimpleValueChecker());
        optimizer.setSimplex(simplex);
        try {
            optimizer.optimize(10, f, null, new double[0]);
        } catch (NullPointerException e) {
            // Expected
            return;
        }
        fail("Expected NullPointerException");
    }

    private static class DummySimplex implements AbstractSimplex {
        @Override
        public int getSize() {
            return 2;
        }

        @Override
        public double[] getPoint(int index) {
            return new double[]{0.0, 0.0};
        }

        @Override
        public void build(double[] startPoint) {
            // Empty
        }

        @Override
        public void iterate(MultivariateFunction f, Comparator<? super PointValuePair> comparator) {
            // Empty
        }

        @Override
        public void evaluate(MultivariateFunction f, Comparator<? super PointValuePair> comparator) {
            // Empty
        }
    }
}