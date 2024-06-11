import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.QRDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.SimpleVectorValueChecker;
import org.apache.commons.math3.optimization.PointVectorValuePair;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class GaussNewtonOptimizerTest {

    private GaussNewtonOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new GaussNewtonOptimizer();
    }

    @Test
    public void testDoOptimize() throws Exception {
        double[] target = {1, 2, 3};
        RealMatrix weight = new BlockRealMatrix(3, 3);
        double[] startPoint = {0, 0, 0};
        ConvergenceChecker<PointVectorValuePair> checker = new SimpleVectorValueChecker();
        optimizer.setTarget(target);
        optimizer.setWeight(weight);
        optimizer.setStartPoint(startPoint);
        optimizer.setConvergenceChecker(checker);

        PointVectorValuePair result = optimizer.doOptimize();

        assertNotNull(result);
        assertEquals(startPoint.length, result.getPoint().length);
        assertTrue(optimizer.getIterations() > 0);
        assertTrue(optimizer.getCost() >= 0.0);
    }

    @Test(expected = NullArgumentException.class)
    public void testDoOptimize_NullChecker() throws Exception {
        double[] target = {1, 2, 3};
        RealMatrix weight = new BlockRealMatrix(3, 3);
        double[] startPoint = {0, 0, 0};
        optimizer.setTarget(target);
        optimizer.setWeight(weight);
        optimizer.setStartPoint(startPoint);

        optimizer.doOptimize();
    }

    @Test(expected = ConvergenceException.class)
    public void testDoOptimize_SingularMatrixException() throws Exception {
        double[] target = {1, 2, 3};
        RealMatrix weight = new BlockRealMatrix(3, 3);
        double[] startPoint = {0, 0, 0};
        ConvergenceChecker<PointVectorValuePair> checker = new SimpleVectorValueChecker();
        optimizer.setTarget(target);
        optimizer.setWeight(weight);
        optimizer.setStartPoint(startPoint);
        optimizer.setConvergenceChecker(checker);

        optimizer.doOptimize();
    }
}