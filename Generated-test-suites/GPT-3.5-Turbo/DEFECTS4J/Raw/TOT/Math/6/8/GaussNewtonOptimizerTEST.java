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
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.SimpleVectorValueChecker;
import org.apache.commons.math3.optimization.PointVectorValuePair;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class GaussNewtonOptimizerTest {

    private GaussNewtonOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new GaussNewtonOptimizer();
    }

    @Test(expected = NullPointerException.class)
    public void testNullTarget() {
        double[] targetValues = null;
        optimizer.withTarget(targetValues);
        optimizer.optimize();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidTargetLength() {
        double[] targetValues = new double[0];
        optimizer.withTarget(targetValues);
        optimizer.optimize();
    }

    @Test(expected = NullPointerException.class)
    public void testNullWeight() {
        RealMatrix weightMatrix = null;
        optimizer.withWeight(weightMatrix);
        optimizer.optimize();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidWeightSize() {
        double[][] weightValues = {{1.0, 0.0}, {0.0, 1.0}, {0.0, 0.0}};
        RealMatrix weightMatrix = new BlockRealMatrix(weightValues);
        optimizer.withWeight(weightMatrix);
        optimizer.optimize();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidStartPoint() {
        double[] startPoint = new double[0];
        optimizer.withStartPoint(startPoint);
        optimizer.optimize();
    }

    @Test(expected = NullPointerException.class)
    public void testNullConvergenceChecker() {
        ConvergenceChecker<PointVectorValuePair> checker = null;
        optimizer.withConvergenceChecker(checker);
        optimizer.optimize();
    }

    @Test(expected = NullArgumentException.class)
    public void testNullConvergenceChecker2() {
        optimizer.withConvergenceChecker(null);
        optimizer.optimize();
    }

    @Test(expected = ConvergenceException.class)
    public void testUnableToSolveSingularProblem() {
        double[] targetValues = {1.0, 2.0};
        optimizer.withTarget(targetValues);
        double[][] weightValues = {{1.0, 0.0}, {0.0, 0.0}};
        RealMatrix weightMatrix = new BlockRealMatrix(weightValues);
        optimizer.withWeight(weightMatrix);
        double[] startPoint = {0.0, 0.0};
        optimizer.withStartPoint(startPoint);
        optimizer.optimize();
    }

    @Test
    public void testOptimize() {
        double[] targetValues = {1.0, 2.0};
        optimizer.withTarget(targetValues);
        double[][] weightValues = {{1.0, 0.0}, {0.0, 1.0}};
        RealMatrix weightMatrix = new BlockRealMatrix(weightValues);
        optimizer.withWeight(weightMatrix);
        double[] startPoint = {0.0, 0.0};
        optimizer.withStartPoint(startPoint);
        optimizer.withMaxIterations(100);
        optimizer.optimize();

        double[] expectedPoint = {1.0, 2.0};
        double[] actualPoint = optimizer.getPoint();
        assertArrayEquals(expectedPoint, actualPoint, 1e-15);
    }

}