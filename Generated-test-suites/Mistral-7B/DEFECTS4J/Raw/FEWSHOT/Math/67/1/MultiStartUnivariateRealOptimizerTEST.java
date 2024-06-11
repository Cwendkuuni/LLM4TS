package org.apache.commons.cli.optimization; // change the package name if necessary

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.optimizers.BrentOptimizer;
import org.apache.commons.math3.optim.optimizers.PowellOptimizer;
import org.junit.Test;

/**
 * Test cases for MultiStartUnivariateRealOptimizer
 */
public class MultiStartUnivariateRealOptimizerTest {

    /**
     * Test optimization with minimum goal and multiple starts
     */
    @Test
    public void testMinimumMultiStart() {
        UnivariateRealFunction f = f -> Math.pow(f, 2);
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new PowellOptimizer(), 3, new RandomGenerator(){
                    @Override
                    public double nextDouble() {
                        return Math.random();
                    }
                });
        double[] optima = new double[3];
        double[] optimaValues = new double[3];
        try {
            PointValuePair result = optimizer.optimize(f, GoalType.MINIMIZE, 0.0, 1.0);
            optima[0] = result.getPoint();
            optimaValues[0] = result.getValue();
        } catch (ConvergenceException e) {
            // should not happen
            assert false;
        }
        try {
            PointValuePair result = optimizer.optimize(f, GoalType.MINIMIZE, 0.0, 1.0);
            optima[1] = result.getPoint();
            optimaValues[1] = result.getValue();
        } catch (ConvergenceException e) {
            // should not happen
            assert false;
        }
        try {
            PointValuePair result = optimizer.optimize(f, GoalType.MINIMIZE, 0.0, 1.0);
            optima[2] = result.getPoint();
            optimaValues[2] = result.getValue();
        } catch (ConvergenceException e) {
            // should not happen
            assert false;
        }
        assertArrayEquals(new double[] { 0.25, 0.5, 0.75 }, optima, 0.001);
        assertArrayEquals(new double[] { 0.0, 0.25, 0.25 }, optimaValues, 0.001);
    }

    /**
     * Test optimization with maximum goal and multiple starts
     */
    @Test
    public void testMaximumMultiStart() {
        UnivariateRealFunction f = f -> Math.pow(f, 2);
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new BrentOptimizer(), 3, new RandomGenerator(){
                    @Override
                    public double nextDouble() {
                        return Math.random();
                    }
                });
        double[] optima = new double[3];
        double[] optimaValues = new double[3];
        try {
            PointValuePair result = optimizer.optimize(f, GoalType.MAXIMIZE, 0.0, 1.0);
            optima[0] = result.getPoint();
            optimaValues[0] = result.getValue();
        } catch (ConvergenceException e) {
            // should not happen
            assert false;
        }
        try {
            PointValuePair result = optimizer.optimize(f, GoalType.MAXIMIZE, 0.0, 1.0);
            optima[1] = result.getPoint();
            optimaValues[1] = result.getValue();
        } catch (ConvergenceException e) {
            // should not happen
            assert false;
        }
        try {
            PointValuePair result = optimizer.optimize(f, GoalType.MAXIMIZE, 0.0, 1.0);
            optima[2] = result.getPoint();
            optimaValues[2] = result.getValue();
        } catch (ConvergenceException e) {
            // should not happen
            assert false;
        }
        assertArrayEquals(new double[] { 0.75, 0.5, 0.25 }, optima, 0.001);
        assertArrayEquals(new double[] { 1.0, 0.25, 0.25 }, optimaValues, 0.001);
    }

    /**
     * Test optimization with minimum goal, multiple starts and custom bounds
     */
    @Test
    public void testMinimumMultiStartCustomBounds() {
        UnivariateRealFunction f = f -> Math.pow(f, 2);
        SimpleBounds bounds = new SimpleBounds(0.0, 1.0);
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new PowellOptimizer(), 3, new RandomGenerator(){
                    @Override
                    public double nextDouble() {
                        return Math.random();
                    }
                }, bounds);
        double[] optima = new double[3];
        double[] optimaValues = new double[3];
        try {
            PointValuePair result = optimizer.optimize(f, GoalType.MINIMIZE, bounds.getMin(), bounds.getMax());
            optima[0] = result.getPoint();
            optimaValues[0] = result.getValue();
        } catch (ConvergenceException e) {
            // should not happen
            assert false;
        }
        try {
            PointValuePair result = optimizer.optimize(f, GoalType.MINIMIZE, bounds.getMin(), bounds.getMax());
            optima[1] = result.getPoint();
            optimaValues[1] = result.getValue();
        } catch (ConvergenceException e) {
            // should not happen
            assert false;
        }
        try {
            PointValuePair result = optimizer.optimize(f, GoalType.MINIMIZE, bounds.getMin(), bounds.getMax());
            optima[2] = result.getPoint();
            optimaValues[2] = result.getValue();
        } catch (ConvergenceException e) {
            // should not happen
            assert false;
        }
        assertArrayEquals(new double[] { 0.25, 0.5, 0.75 }, optima, 0.001);
        assertArrayEquals(new double[] { 0.0, 0.25, 0.25 }, optimaValues, 0.001);
    }

    /**
     * Test optimization with maximum goal, multiple starts, and custom bounds
     */
    @Test
    public void testMaximumMultiStartCustomBounds() {
        UnivariateRealFunction f = f -> Math.pow(f, 2);
        SimpleBounds bounds = new SimpleBounds(0.0, 1.0);
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new BrentOptimizer(), 3, new RandomGenerator(){
                    @Override
                    public double nextDouble() {
                        return Math.random();
                    }
                }, bounds);
        double[] optima = new double[3];
        double[] optimaValues = new double[3];
        try {
            PointValuePair result = optimizer.optimize(f, GoalType.MAXIMIZE, bounds.getMin(), bounds.getMax());
            optima[0] = result.getPoint();
            optimaValues[0] = result.getValue();
        } catch (ConvergenceException e) {
            // should not happen
            assert false;
        }
        try {
            PointValuePair result = optimizer.optimize(f, GoalType.MAXIMIZE, bounds.getMin(), bounds.getMax());
            optima[1] = result.getPoint();
            optimaValues[1] = result.getValue();
        } catch (ConvergenceException e) {
            // should not happen
            assert false;
        }
        try {
            PointValuePair result = optimizer.optimize(f, GoalType.MAXIMIZE, bounds.getMin(), bounds.getMax());
            optima[2] = result.getPoint();
            optimaValues[2] = result.getValue();
        } catch (ConvergenceException e) {
            // should not happen
            assert false;
        }
        assertArrayEquals(new double[] { 0.75, 0.5, 0.25 }, optima, 0.001);
        assertArrayEquals(new double[] { 1.0, 0.25, 0.25 }, optimaValues, 0.001);
    }

    /**
     * Test optimization with minimum goal, multiple starts, and non-converging optimization
     */
    @Test
    public void testMinimumMultiStartNoConvergence() {
        UnivariateRealFunction f = f -> Math.sin(f);
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new PowellOptimizer(), 3, new RandomGenerator(){
                    @Override
                    public double nextDouble() {
                        return Math.random();
                    }
                });
        double[] optima = new double[3];
        double[] optimaValues = new double[3];
        try {
            PointValuePair result = optimizer.optimize(f, GoalType.MINIMIZE, 0.0, 1.0);
            optima[0] = result.getPoint();
            optimaValues[0] = result.getValue();
        } catch (ConvergenceException e) {
            // expected
        }
        try {
            PointValuePair result = optimizer.optimize(f, GoalType.MINIMIZE, 0.0, 1.0);
            optima[1] = result.getPoint();
            optimaValues[1] = result.getValue();
        } catch (ConvergenceException e) {
            // expected
        }
        try {
            PointValuePair result = optimizer.optimize(f, GoalType.MINIMIZE, 0.0, 1.0);
            optima[2] = result.getPoint();
            optimaValues[2] = result.getValue();
        } catch (ConvergenceException e) {
            // expected
        }
        assertArrayEquals(new double[] { Double.NaN, Double.NaN, Double.NaN }, optima, 0.0);
        assertArrayEquals(new double[] { Double.NaN, Double.NaN, Double.NaN }, optimaValues, 0.0);
    }

    /**
     * Test optimization with maximum goal, multiple starts, and non-converging optimization
     */
    @Test
    public void testMaximumMultiStartNoConvergence() {
        UnivariateRealFunction f = f -> Math.sin(f);
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new BrentOptimizer(), 3, new RandomGenerator(){
                    @Override
                    public double nextDouble() {
                        return Math.random();
                    }
                });
        double[] optima = new double[3];
        double[] optimaValues = new double[3];
        try {
            PointValuePair result = optimizer.optimize(f, GoalType.MAXIMIZE, 0.0, 1.0);
            optima[0] = result.getPoint();
            optimaValues[0] = result.getValue();
        } catch (ConvergenceException e) {
            // expected
        }
        try {
            PointValuePair result = optimizer.optimize(f, GoalType.MAXIMIZE, 0.0, 1.0);
            optima[1] = result.getPoint();
            optimaValues[1] = result.getValue();
        } catch (ConvergenceException e) {
            // expected
        }
        try {
            PointValuePair result = optimizer.optimize(f, GoalType.MAXIMIZE, 0.0, 1.0);
            optima[2] = result.getPoint();
            optimaValues[2] = result.getValue();
        } catch (ConvergenceException e) {
            // expected
        }
        assertArrayEquals(new double[] { Double.NaN, Double.NaN, Double.NaN }, optima, 0.0);
        assertArrayEquals(new double[] { Double.NaN, Double.NaN, Double.NaN }, optimaValues, 0.0);
    }

    /**
     * Test optimization with maximum goal, multiple starts, and non-converging optimization with custom bounds
     */
    @Test
    public void testMaximumMultiStartCustomBoundsNoConvergence() {
        UnivariateRealFunction f = f -> Math.sin(f);
        SimpleBounds bounds = new SimpleBounds(0.0, 1.0);
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new BrentOptimizer(), 3, new RandomGenerator(){
                    @Override
                    public double nextDouble() {
                        return Math.random();
                    }
                }, bounds);
        double[] optima = new double[3];
        double[] optimaValues = new double[3];
        try {
            PointValuePair result = optimizer.optimize(f, GoalType.MAXIMIZE, bounds.getMin(), bounds.getMax());
            optima[0] = result.getPoint();
            optimaValues[0] = result.getValue();
        } catch (ConvergenceException e) {
            // expected
        }
        try {
            PointValuePair result = optimizer.optimize(f, GoalType.MAXIMIZE, bounds.getMin(), bounds.getMax());
            optima[1] = result.getPoint();
            optimaValues[1] = result.getValue();
        } catch (ConvergenceException e) {
            // expected
        }
        try {
            PointValuePair result = optimizer.optimize(f, GoalType.MAXIMIZE, bounds.getMin(), bounds.getMax());
            optima[2] = result.getPoint();
            optimaValues[2] = result.getValue();
        } catch (ConvergenceException e) {
            // expected
        }
        assertArrayEquals(new double[] { Double.NaN, Double.NaN, Double.NaN }, optima, 0.0);
        assertArrayEquals(new double[] { Double.NaN, Double.NaN, Double.NaN }, optimaValues, 0.0);
    }

    /**
     * Test optimization with exception when no starts are specified
     */
    @Test
    public void testNoStarts() {
        UnivariateRealFunction f = f -> Math.pow(f, 2);
        assertThrows(IllegalArgumentException.class, () -> {
            new MultiStartUnivariateRealOptimizer(new PowellOptimizer(), 0, new RandomGenerator(){
                @Override
                public double nextDouble() {
                    return Math.random();
                }
            });
        });
    }

    /**
     * Test optimization with exception when null optimizer is specified
     */
    @Test
    public void testNullOptimizer() {
        assertThrows(NullPointerException.class, () -> {
            new MultiStartUnivariateRealOptimizer(null, 3, new RandomGenerator(){
                @Override
                public double nextDouble() {
                    return Math.random();
                }
            });
        });
    }
}