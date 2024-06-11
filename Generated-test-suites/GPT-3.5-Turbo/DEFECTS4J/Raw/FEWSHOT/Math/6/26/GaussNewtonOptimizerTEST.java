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
    public void setup() {
        optimizer = new GaussNewtonOptimizer();
    }

    @Test(expected = NullArgumentException.class)
    public void testDoOptimize_NullConvergenceChecker() {
        optimizer.setConvergenceChecker(null);
        optimizer.doOptimize();
    }

    @Test
    public void testDoOptimize_ExactConvergence() {
        ConvergenceChecker<PointVectorValuePair> checker = new SimpleVectorValueChecker();
        checker.setMaxIterations(1);
        optimizer.setConvergenceChecker(checker);

        double[] start = {0.0, 0.0};
        optimizer.setStartPoint(start);

        double[] target = {0.0, 0.0};
        optimizer.setTarget(target);

        RealMatrix weight = new BlockRealMatrix(2, 2);
        weight.setEntry(0, 0, 1.0);
        weight.setEntry(1, 1, 1.0);
        optimizer.setWeight(weight);

        PointVectorValuePair result = optimizer.doOptimize();

        assertEquals(result.getPointRef()[0], 0.0, 0.0);
        assertEquals(result.getPointRef()[1], 0.0, 0.0);
        assertEquals(optimizer.getCost(), 0.0, 0.0);
    }

    @Test(expected = SingularMatrixException.class)
    public void testDoOptimize_SingularMatrixException() {
        double[] start = {1.0, 2.0};
        optimizer.setStartPoint(start);

        double[] target = {3.0, 4.0};
        optimizer.setTarget(target);

        RealMatrix weight = new BlockRealMatrix(2, 2);
        weight.setEntry(0, 0, 1.0);
        weight.setEntry(1, 1, 1.0);
        optimizer.setWeight(weight);

        optimizer.doOptimize();
    }

    @Test
    public void testDoOptimize_NonSingularMatrix() {
        ConvergenceChecker<PointVectorValuePair> checker = new SimpleVectorValueChecker();
        checker.setMaxIterations(100);
        optimizer.setConvergenceChecker(checker);

        double[] start = {0.0, 0.0};
        optimizer.setStartPoint(start);

        double[] target = {3.0, 4.0};
        optimizer.setTarget(target);

        RealMatrix weight = new BlockRealMatrix(2, 2);
        weight.setEntry(0, 0, 1.0);
        weight.setEntry(1, 1, 1.0);
        optimizer.setWeight(weight);

        PointVectorValuePair result = optimizer.doOptimize();

        assertEquals(result.getPointRef()[0], 3.0, 0.0);
        assertEquals(result.getPointRef()[1], 4.0, 0.0);
        assertEquals(optimizer.getCost(), 0.0, 0.0);
    }
}