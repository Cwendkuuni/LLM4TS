import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.SimpleVectorValueChecker;
import org.apache.commons.math3.optimization.PointVectorValuePair;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.MathInternalError;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.QRDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.apache.commons.math3.optimization.general.GaussNewtonOptimizer;

import static org.junit.Assert.assertEquals;

public class GaussNewtonOptimizerTest {

    private GaussNewtonOptimizer optimizer;

    @Before
    public void setUp() {
        ConvergenceChecker<PointVectorValuePair> checker = new SimpleVectorValueChecker();
        optimizer = new GaussNewtonOptimizer(true, checker);
    }

    @Test(expected = NullArgumentException.class)
    public void testDoOptimizeThrowsNullArgumentException() {
        optimizer.doOptimize();
    }

    @Test(expected = MathInternalError.class)
    public void testDoOptimizeThrowsMathInternalError() {
        optimizer.setStartPoint(new double[]{0.0, 0.0});
        optimizer.setTarget(new double[]{1.0, 1.0});
        optimizer.doOptimize();
    }

    @Test
    public void testDoOptimize() {
        optimizer.setStartPoint(new double[]{0.0, 0.0});
        optimizer.setTarget(new double[]{1.0, 1.0});
        PointVectorValuePair result = optimizer.doOptimize();
        double[] expected = new double[]{1.0, 1.0};
        double[] actual = result.getPoint();
        assertEquals(expected, actual, 0.0001);
    }

    @Test
    public void testDoOptimizeWithConvergence() {
        optimizer.setStartPoint(new double[]{0.0, 0.0});
        optimizer.setTarget(new double[]{1.0, 1.0});
        optimizer.setConvergenceChecker(new ConvergenceChecker<PointVectorValuePair>() {
            @Override
            public boolean converged(int iteration, PointVectorValuePair previous, PointVectorValuePair current) {
                return iteration >= 5;
            }
        });
        PointVectorValuePair result = optimizer.doOptimize();
        double[] expected = new double[]{1.0, 1.0};
        double[] actual = result.getPoint();
        assertEquals(expected, actual, 0.0001);
    }

    @Test(expected = SingularMatrixException.class)
    public void testDoOptimizeThrowsSingularMatrixException() {
        optimizer.setStartPoint(new double[]{0.0, 0.0});
        optimizer.setTarget(new double[]{1.0, 1.0});
        optimizer.doOptimize();
    }

    @Test(expected = ConvergenceException.class)
    public void testDoOptimizeThrowsConvergenceException() {
        optimizer.setStartPoint(new double[]{0.0, 0.0});
        optimizer.setTarget(new double[]{1.0, 1.0});
        optimizer.setConvergenceChecker(new ConvergenceChecker<PointVectorValuePair>() {
            @Override
            public boolean converged(int iteration, PointVectorValuePair previous, PointVectorValuePair current) {
                return iteration >= 10;
            }
        });
        optimizer.doOptimize();
    }
}