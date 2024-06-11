
package org.apache.commons.math.optimization.general;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.MaxEvaluationsExceededException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.linear.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link AbstractLeastSquaresOptimizer}.
 */
public class AbstractLeastSquaresOptimizerTest {

    private static final double EPSILON = 1e-9;
    private static final int MAX_ITERATIONS = 1;
    private static final int MAX_EVALUATIONS = 10;

    private AbstractLeastSquaresOptimizer optimizer;
    private MultivariateFunction function;

    @Before
    public void setUp() {
        optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                throw new UnsupportedOperationException();
            }
        };
        optimizer.setMaxIterations(MAX_ITERATIONS);
        optimizer.setMaxEvaluations(MAX_EVALUATIONS);

        function = new MultivariateFunction() {
            @Override
            public double value(RealVector v) {
                return 0;
            }

            @Override
            public RealVector gradient(RealVector v) {
                return VectorUtils.CREATE_VECTOR(new double[]{1, 1});
            }
        };
    }

    // Test methods here...

}
