import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.FibonacciSearch;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.NelderMeadSimplex;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.SimplexWalkingStrategy;
import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.commons.math.optimization.direct.MultiDirectional;

public class MultiDirectionalTest {

    /**
     * Test method for {@link MultiDirectional#MultiDirectional()} constructor.
     * @throws Exception if an error occurs
     */
    @Test
    public void testMultiDirectionalDefaultConstructor() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        assertNotNull(optimizer);
        assertEquals(2.0, optimizer.khi, 0.01);
        assertEquals(0.5, optimizer.gamma, 0.01);
    }

    /**
     * Test method for {@link MultiDirectional#MultiDirectional(double, double)} constructor.
     * @throws Exception if an error occurs
     */
    @Test
    public void testMultiDirectionalCustomConstructor() throws Exception {
        MultiDirectional optimizer = new MultiDirectional(3.0, 0.7);
        assertNotNull(optimizer);
        assertEquals(3.0, optimizer.khi, 0.01);
        assertEquals(0.7, optimizer.gamma, 0.01);
    }

    /**
     * Test method for {@link MultiDirectional#iterateSimplex(Comparator)} method.
     * @throws Exception if an error occurs
     */
    @Test
    public void testIterateSimplex() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();

        ObjectiveFunction testFunction = new ObjectiveFunction() {
            @Override
            public double value(double[] point) {
                return Math.pow(Math.sin(point[0]), 2) + Math.pow(Math.sin(point[1]), 2);
            }

            @Override
            public GoalType getGoalType() {
                return GoalType.MINIMIZE;
            }
        };

        SimpleBounds bounds = new SimpleBounds(new double[]{-3.14, -3.14}, new double[]{3.14, 3.14});

        RealPointValuePair initialGuess = new RealPointValuePair(new double[]{0.0, 0.0}, Double.NaN);
        PointValuePair solution = new PointValuePair(new double[]{1.0, 1.0}, 0.0);

        NelderMeadSimplex simplex = new NelderMeadSimplex(2, new FibonacciSearch(), new SimplexWalkingStrategy(100, 1.0E-8));
        Comparator<PointValuePair> comparator = (p1, p2) -> Double.compare(p1.getValue(), p2.getValue());

        optimizer.initialize(testFunction, bounds, initialGuess, simplex, comparator);
        optimizer.optimize();

        assertEquals(solution.getPoint()[0], optimizer.getBestSolution().getPoint()[0], 0.01);
        assertEquals(solution.getPoint()[1], optimizer.getBestSolution().getPoint()[1], 0.01);
        assertEquals(solution.getValue(), optimizer.getBestSolution().getValue(), 0.01);
    }

    /**
     * Test method for {@link MultiDirectional#iterateSimplex(Comparator)} method with an error.
     * @throws Exception if an error occurs
     */
    @Test(expected = IllegalArgumentException.class)
    public void testIterateSimplexIllegalArgumentException() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();

        ObjectiveFunction testFunction = new ObjectiveFunction() {
            @Override
            public double value(double[] point) {
                throw new IllegalArgumentException();
            }

            @Override
            public GoalType getGoalType() {
                return GoalType.MINIMIZE;
            }
        };

        SimpleBounds bounds = new SimpleBounds(new double[]{-3.14, -3.14}, new double[]{3.14, 3.14});

        RealPointValuePair initialGuess = new RealPointValuePair(new double[]{0.0, 0.0}, Double.NaN);
        PointValuePair solution = new PointValuePair(new double[]{1.0, 1.0}, 0.0);

        NelderMeadSimplex simplex = new NelderMeadSimplex(2, new FibonacciSearch(), new SimplexWalkingStrategy(100, 1.0E-8));
        Comparator<PointValuePair> comparator = (p1, p2) -> Double.compare(p1.getValue(), p2.getValue());

        optimizer.initialize(testFunction, bounds, initialGuess, simplex, comparator);
        optimizer.optimize();
    }

}