package org.apache.commons.math.optimization.direct;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.exception.MaxEvaluationsExceededException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.OutOfBoundsException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealFunction;
import org.apache.commons.math3.optim.SimpleBoundingBox;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.junit.Test;

public class MultiDirectionalTest {

    @Test
    public void testDefaultConstructor() {
        MultiDirectional optimizer = new MultiDirectional();
        assertEquals(2.0, optimizer.getKhi(), 0.001);
        assertEquals(0.5, optimizer.getGamma(), 0.001);
    }

    @Test
    public void testCustomConstructor() {
        MultiDirectional optimizer = new MultiDirectional(3.0, 0.7);
        assertEquals(3.0, optimizer.getKhi(), 0.001);
        assertEquals(0.7, optimizer.getGamma(), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullComparator() {
        new MultiDirectional(new Comparator<PointValuePair>() {
            @Override
            public int compare(PointValuePair o1, PointValuePair o2) {
                return 0;
            }
        });
    }

    @Test
    public void testIterateSimplexNoImprovement() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        RealFunction function = x -> Math.pow(Math.sin(x), 2) + Math.pow(Math.cos(x), 2);
        ObjectiveFunction objFunction = new ObjectiveFunction(function);

        SimpleBoundingBox bounds = new SimpleBoundingBox(0.0, Math.PI, 1.0);

        optimizer.setMaxIterations(10);
        optimizer.setMaxEvaluations(20);
        optimizer.setGoalType(GoalType.MINIMIZE);

        optimizer.optimize(objFunction, bounds);

        assertArrayEquals(new double[]{}, optimizer.getSimpleX(), 0.001);
        assertNull(optimizer.getMinimum());
        assertEquals(10, optimizer.getIterations());
        assertEquals(20, optimizer.getFunctionEvaluations());
    }

    @Test
    public void testIterateSimplexReflection() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        RealFunction function = x -> Math.pow(Math.sin(x), 2) + Math.pow(Math.cos(x), 2);
        ObjectiveFunction objFunction = new ObjectiveFunction(function);

        SimpleBoundingBox bounds = new SimpleBoundingBox(0.0, Math.PI, 1.0);

        optimizer.setMaxIterations(20);
        optimizer.setMaxEvaluations(50);
        optimizer.setGoalType(GoalType.MINIMIZE);

        optimizer.optimize(objFunction, bounds);

        double[] expected = new double[]{-3.139897861843448, -1.1763990498377564};
        assertArrayEquals(expected, optimizer.getSimpleX(), 0.001);
        assertEquals(-3.236068222736195, optimizer.getMinimum(), 0.001);
        assertEquals(20, optimizer.getIterations());
        assertEquals(50, optimizer.getFunctionEvaluations());
    }

    @Test
    public void testIterateSimplexContraction() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        RealFunction function = x -> Math.pow(Math.sin(x), 2) + Math.pow(Math.cos(x), 2);
        ObjectiveFunction objFunction = new ObjectiveFunction(function);

        SimpleBoundingBox bounds = new SimpleBoundingBox(0.0, Math.PI, 1.0);

        optimizer.setMaxIterations(20);
        optimizer.setMaxEvaluations(50);
        optimizer.setGoalType(GoalType.MINIMIZE);

        optimizer.optimize(objFunction, bounds);

        double[] expected = new double[]{-1.2915226763585546, 0.7853981633974483};
        assertArrayEquals(expected, optimizer.getSimpleX(), 0.001);
        assertEquals(-1.283650825028346, optimizer.getMinimum(), 0.001);
        assertEquals(20, optimizer.getIterations());
        assertEquals(50, optimizer.getFunctionEvaluations());
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionEvaluationException() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        RealFunction function = x -> { throw new FunctionEvaluationException("Message"); };
        ObjectiveFunction objFunction = new ObjectiveFunction(function);

        SimpleBoundingBox bounds = new SimpleBoundingBox(0.0, 1.0, 1.0);

        optimizer.setMaxIterations(10);
        optimizer.setMaxEvaluations(10);
        optimizer.setGoalType(GoalType.MINIMIZE);

        optimizer.optimize(objFunction, bounds);
    }

    @Test(expected = OptimizationException.class)
    public void testOptimizationException() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        RealFunction function = x -> 0.0;
        ObjectiveFunction objFunction = new ObjectiveFunction(function);

        SimpleBoundingBox bounds = new SimpleBoundingBox(0.0, 1.0, 1.0);

        optimizer.setMaxIterations(10);
        optimizer.setMaxEvaluations(10);
        optimizer.setGoalType(GoalType.MINIMIZE);

        optimizer.optimize(objFunction, bounds);
    }

    @Test
    public void testMaxIterationsExceeded() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        RealFunction function = x -> Math.pow(Math.sin(x), 2) + Math.pow(Math.cos(x), 2);
        ObjectiveFunction objFunction = new ObjectiveFunction(function);

        SimpleBoundingBox bounds = new SimpleBoundingBox(0.0, Math.PI, 1.0);

        optimizer.setMaxIterations(1);
        optimizer.setMaxEvaluations(10);
        optimizer.setGoalType(GoalType.MINIMIZE);

        assertThrows(MaxEvaluationsExceededException.class, () -> optimizer.optimize(objFunction, bounds));
    }

    @Test
    public void testNullObjectiveFunction() {
        MultiDirectional optimizer = new MultiDirectional();
        assertThrows(NullArgumentException.class, () -> optimizer.optimize(null, new SimpleBoundingBox(0.0, 0.0, 1.0)));
    }

    @Test
    public void testNullBounds() {
        MultiDirectional optimizer = new MultiDirectional();
        assertThrows(NullArgumentException.class, () -> optimizer.optimize(new ObjectiveFunction(x -> 0.0), null));
    }

    @Test
    public void testBoundsOutOfBounds() {
        MultiDirectional optimizer = new MultiDirectional();
        RealFunction function = x -> Math.pow(Math.sin(x), 2) + Math.pow(Math.cos(x), 2);
        ObjectiveFunction objFunction = new ObjectiveFunction(function);

        assertThrows(OutOfBoundsException.class, () -> optimizer.optimize(objFunction, new SimpleBoundingBox(-1.0, 0.0, 1.0)));
    }

    @Test
    public void testBoundsWithSameBounds() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        RealFunction function = x -> Math.pow(Math.sin(x), 2) + Math.pow(Math.cos(x), 2);
        ObjectiveFunction objFunction = new ObjectiveFunction(function);

        SimpleBoundingBox bounds = new SimpleBoundingBox(0.0, 0.0, 1.0);

        optimizer.setMaxIterations(10);
        optimizer.setMaxEvaluations(10);
        optimizer.setGoalType(GoalType.MINIMIZE);

        optimizer.optimize(objFunction, bounds);

        double[] expected = new double[]{0.0};
        assertArrayEquals(expected, optimizer.getSimpleX(), 0.001);
        assertEquals(Double.NaN, optimizer.getMinimum(), 0.001);
        assertEquals(10, optimizer.getIterations());
        assertEquals(10, optimizer.getFunctionEvaluations());
    }

    @Test(expected = NoBracketingException.class)
    public void testNoBracketingException() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        RealFunction function = x -> Math.pow(Math.sin(x), 2) + Math.pow(Math.cos(x), 2);
        ObjectiveFunction objFunction = new ObjectiveFunction(function);

        SimpleBoundingBox bounds = new SimpleBoundingBox(-1.0, 1.0, 1.0);

        optimizer.setMaxIterations(10);
        optimizer.setMaxEvaluations(10);
        optimizer.setGoalType(GoalType.MINIMIZE);

        optimizer.optimize(objFunction, bounds);
    }

    @Test
    public void testNumberIsTooSmallException() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        RealFunction function = x -> Math.pow(Math.sin(x), 2) + Math.pow(Math.cos(x), 2);
        ObjectiveFunction objFunction = new ObjectiveFunction(function);

        SimpleBoundingBox bounds = new SimpleBoundingBox(-1.0, 0.0, 1.0);

        optimizer.setMaxIterations(10);
        optimizer.setMaxEvaluations(10);
        optimizer.setGoalType(GoalType.MINIMIZE);

        optimizer.optimize(objFunction, bounds);

        double[] expected = new double[]{0.0};
        assertArrayEquals(expected, optimizer.getSimpleX(), 0.001);
        assertEquals(Double.NaN, optimizer.getMinimum(), 0.001);
        assertEquals(10, optimizer.getIterations());
        assertEquals(10, optimizer.getFunctionEvaluations());
    }
}