
import org.junit.Test;
import org.junit.Assert;
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

public class GaussNewtonOptimizerTest {

    @Test
    public void testDoOptimize() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        try {
            optimizer.doOptimize();
            Assert.fail("ConvergenceException should have been thrown");
        } catch (ConvergenceException e) {
            Assert.assertEquals(LocalizedFormats.UNABLE_TO_SOLVE_SINGULAR_PROBLEM, e.getMessageEnum());
        } catch (NullArgumentException e) {
            Assert.fail("NullArgumentException should not have been thrown");
        } catch (MathInternalError e) {
            Assert.fail("MathInternalError should not have been thrown");
        }
    }
}
