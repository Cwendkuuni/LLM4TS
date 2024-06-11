import org.junit.Test;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MathInternalError;
import org.apache.commons.math3.optimization.general.GaussNewtonOptimizer;
import org.apache.commons.math3.optimization.PointVectorValuePair;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.SimpleVectorValueChecker;

import static org.junit.Assert.*;

public class GaussNewtonOptimizerTest {

    @Test
    public void testDoOptimize() {
        double[] target = {2.0, 4.0, 6.0};
        double[] startPoint = {1.0, 1.0};

        double[][] weightedJacobianData = {
            {1.0, 1.0},
            {2.0, 2.0},
            {3.0, 3.0}
        };
        RealMatrix weightedJacobian = new Array2DRowRealMatrix(weightedJacobianData);

        double[] computedObjectiveData = {1.0, 2.0, 3.0};
        double[] computedResidualsData = {1.0, 2.0, 3.0};
        PointVectorValuePair computed = new PointVectorValuePair(startPoint, computedObjectiveData);

        double[][] solutionData = {{1.0}, {1.0}};
        RealMatrix solution = new Array2DRowRealMatrix(solutionData);

        TestGaussNewtonOptimizer optimizer = new TestGaussNewtonOptimizer(target, startPoint, weightedJacobian, computedObjectiveData, computedResidualsData, computed, solution);
        PointVectorValuePair result = optimizer.doOptimize();

        assertEquals(computed, result);
        assertEquals(14.0, optimizer.getCost(), 0.0001);
        assertArrayEquals(startPoint, optimizer.getPoint(), 0.0001);
    }

    private static class TestGaussNewtonOptimizer extends GaussNewtonOptimizer {

        private double[] target;
        private double[] startPoint;
        private RealMatrix weightedJacobian;
        private double[] computedObjectiveData;
        private double[] computedResidualsData;
        private PointVectorValuePair computed;
        private RealMatrix solution;

        public TestGaussNewtonOptimizer(double[] target, double[] startPoint, RealMatrix weightedJacobian,
                                        double[] computedObjectiveData, double[] computedResidualsData,
                                        PointVectorValuePair computed, RealMatrix solution) {
            super(new SimpleVectorValueChecker());
            this.target = target;
            this.startPoint = startPoint;
            this.weightedJacobian = weightedJacobian;
            this.computedObjectiveData = computedObjectiveData;
            this.computedResidualsData = computedResidualsData;
            this.computed = computed;
            this.solution = solution;
        }

        @Override
        protected double[] getTarget() {
            return target;
        }

        @Override
        protected double[] getStartPoint() {
            return startPoint;
        }

        @Override
        protected RealMatrix computeWeightedJacobian(double[] point) {
            return weightedJacobian;
        }

        @Override
        protected double[] computeObjectiveValue(double[] point) {
            return computedObjectiveData;
        }

        @Override
        protected double[] computeResiduals(double[] objectiveFunctionValue) {
            return computedResidualsData;
        }

        @Override
        protected double computeCost(double[] residuals) {
            return 14.0;
        }

        @Override
        protected RealMatrix solve(RealMatrix equations, RealVector rhs) throws SingularMatrixException {
            return solution;
        }
    }
}