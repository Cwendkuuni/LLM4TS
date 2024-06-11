import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.initializer.AverageStartingPointGenerator;
import org.apache.commons.math3.optim.initializer.GaussianRandomStartingPointGenerator;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.SimpleBounds;
import org.apache.commons.math3.optim.search.InitialGuess;
import org.apache.commons.math3.optim.search.NelderMeadSimulatedAnnealing;
import org.apache.commons.math3.optim.search.convergence.ConvergenceChecker;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BaseOptimizerTest<PAIR extends PointValuePair<Double, Double>> {

    private static final int MAX_EVALUATIONS = 100;

    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<PAIR> optimizer = new NelderMeadSimulatedAnnealing<PAIR>(new ObjectiveFunction() {
            @Override
            public double value(double[] point) {
                return Math.pow(point[0] * point[1], 2);
            }

            @Override
            public GoalType getGoalType() {
                return GoalType.MINIMIZE;
            }
        }, new SimpleBounds(new double[]{-5.0, -5.0}, new double[]{5.0, 5.0}), new MaxEval(MAX_EVALUATIONS), null, new AverageStartingPointGenerator(), new ConvergenceChecker<PAIR>() {
            @Override
            public boolean isConverged(PAIR currentBest, PAIR currentIterate) {
                return false;
            }
        });

        assertEquals(MAX_EVALUATIONS, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        BaseOptimizer<PAIR> optimizer = new NelderMeadSimulatedAnnealing<PAIR>(new ObjectiveFunction() {
            @Override
            public double value(double[] point) {
                return Math.pow(point[0] * point[1], 2);
            }

            @Override
            public GoalType getGoalType() {
                return GoalType.MINIMIZE;
            }
        }, new SimpleBounds(new double[]{-5.0, -5.0}, new double[]{5.0, 5.0}), new MaxEval(MAX_EVALUATIONS), null, new AverageStartingPointGenerator(), new ConvergenceChecker<PAIR>() {
            @Override
            public boolean isConverged(PAIR currentBest, PAIR currentIterate) {
                return false;
            }
        });

        assertEquals(0, optimizer.getEvaluations());

        optimizer.optimize();

        int evaluations = optimizer.getEvaluations();
        assertTrue(evaluations > 0);
        assertEquals(evaluations, MAX_EVALUATIONS);
    }

    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<PAIR> optimizer = new NelderMeadSimulatedAnnealing<PAIR>(new ObjectiveFunction() {
            @Override
            public double value(double[] point) {
                return Math.pow(point[0] * point[1], 2);
            }

            @Override
            public GoalType getGoalType() {
                return GoalType.MINIMIZE;
            }
        }, new SimpleBounds(new double[]{-5.0, -5.0}, new double[]{5.0, 5.0}), new MaxEval(MAX_EVALUATIONS), null, new AverageStartingPointGenerator(), null);

        ConvergenceChecker<PAIR> convergenceChecker = optimizer.getConvergenceChecker();
        assertFalse(convergenceChecker instanceof ConvergenceChecker<PAIR>);

        optimizer = new NelderMeadSimulatedAnnealing<PAIR>(new ObjectiveFunction() {
            @Override
            public double value(double[] point) {
                return Math.pow(point[0] * point[1], 2);
            }

            @Override
            public GoalType getGoalType() {
                return GoalType.MINIMIZE;
            }
        }, new SimpleBounds(new double[]{-5.0, -5.0}, new double[]{5.0, 5.0}), new MaxEval(MAX_EVALUATIONS), null, new AverageStartingPointGenerator(), new ConvergenceChecker<PAIR>() {
            @Override
            public boolean isConverged(PAIR currentBest, PAIR currentIterate) {
                return false;
            }
        });

        convergenceChecker = optimizer.getConvergenceChecker();
        assertTrue(convergenceChecker instanceof ConvergenceChecker<PAIR>);
    }
}