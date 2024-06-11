package org.apache.commons.math3.optimization.general;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MathInternalError;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.QRDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.PointVectorValuePair;
import org.junit.Before;
import org.junit.Test;

public class GaussNewtonOptimizerTest {
    
    private GaussNewtonOptimizer optimizer;

    @Before
    public void setup() {
        optimizer = new GaussNewtonOptimizer();
    }
    
    @Test
    public void testDoOptimize() {
        double[] target = {1.0, 2.0, 3.0};
        double[] startPoint = {1.0, 1.0};
        double[][] weightMatrixData = {{1.0, 0.0}, {0.0, 1.0}};
        RealMatrix weightMatrix = new BlockRealMatrix(weightMatrixData);
        
        optimizer.setTarget(target);
        optimizer.setStartPoint(startPoint);
        optimizer.setWeightMatrix(weightMatrix);
        
        PointVectorValuePair result = optimizer.doOptimize();
        
        assertArrayEquals(startPoint, result.getPoint(), 1e-15);
        assertArrayEquals(target, result.getValue(), 1e-15);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testDoOptimizeNullChecker() {
        optimizer.setConvergenceChecker(null);
        optimizer.doOptimize();
    }
    
    @Test
    public void testStripLeadingAndTrailingQuotes() {
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("\"foo\""));
        assertEquals("foo \"bar\"", Util.stripLeadingAndTrailingQuotes("foo \"bar\""));
        assertEquals("\"foo\" bar", Util.stripLeadingAndTrailingQuotes("\"foo\" bar"));
        assertEquals("\"foo\" and \"bar\"", Util.stripLeadingAndTrailingQuotes("\"foo\" and \"bar\""));
        assertEquals("", Util.stripLeadingAndTrailingQuotes(""));
    }
    
    @Test
    public void testStripLeadingHyphens() {
        assertEquals("f", Util.stripLeadingHyphens("-f"));
        assertEquals("foo", Util.stripLeadingHyphens("--foo"));
        assertEquals("-foo", Util.stripLeadingHyphens("---foo"));
        assertNull(Util.stripLeadingHyphens(null));
    }
}