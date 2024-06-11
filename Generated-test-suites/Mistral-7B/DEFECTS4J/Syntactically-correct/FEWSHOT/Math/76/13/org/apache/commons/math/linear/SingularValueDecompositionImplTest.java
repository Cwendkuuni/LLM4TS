package org.apache.commons.cli;

import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SingularValueDecompositionImplTest {

    private RealMatrix matrix;
    private SingularValueDecompositionImpl svd;

    @Before
    public void setup() {
        double[][] data = {
                {1.0, 1.0, 1.0},
                {1.0, 2.0, 3.0},
                {1.0, 3.0, 6.0}
        };
        matrix = new Array2DRowRealMatrix(data, false);
    }

    @Test
    public void testSingularValueDecomposition() throws Exception {
        svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix u = svd.getU();
        RealMatrix s = svd.getS();
        RealMatrix v = svd.getV();
        RealMatrix expectedU = new Array2DRowRealMatrix(new double[][]{{0.54421131, -0.82245855, 0.23113103},
                {-0.82245855, 0.54421131, 0.23113103},
                {0.23113103, 0.23113103, -0.49325132}}, false);
        RealMatrix expectedV = new Array2DRowRealMatrix(new double[][]{{-0.01603883, -0.99475829},
                {0.99475829, 0.01603883},
                {0.10594981, 0.31622777}}, false);
        assertEquals(expectedU, u);
        assertEquals(singularValues(data), s.getData(), 1e-13);
        assertEquals(expectedV, v);
    }

    @Test
    public void testSolveWithSingularValues() throws Exception {
        RealMatrix expectedX = new Array2DRowRealMatrix(new double[][]{{-1.04850551, 1.46137735, -1.18492322},
                {1.46137735, -0.96348393, 0.62510099},
                {-1.18492322, 0.62510099, 0.54526196}}, false);
        RealVector b = new ArrayRealVector(new double[]{3.0, 2.0, 1.0});
        RealVector x = svd.getSolver().solve(b);
        RealMatrix xMatrix = new Array2DRowRealMatrix(new double[][]{{x.getEntry(0)}, {x.getEntry(1)}, {x.getEntry(2)}});
        assertEquals(expectedX, xMatrix);
    }

    @Test
    public void testSolveLUDecomposition() throws Exception {
        RealMatrix expectedX = new Array2DRowRealMatrix(new double[][]{{-1.04850551, 1.46137735, -1.18492322},
                {1.46137735, -0.96348393, 0.62510099},
                {-1.18492322, 0.62510099, 0.54526196}}, false);
        RealVector b = new ArrayRealVector(new double[]{3.0, 2.0, 1.0});
        RealMatrix luDecomposition = LUDecomposition.getDecomposition(matrix);
        RealMatrix inverseL = luDecomposition.getInverse();
        RealMatrix expectedInverseL = new Array2DRowRealMatrix(new double[][]{{0.54421131, -0.82245855, 0.23113103},
                {-0.82245855, 0.54421131, 0.23113103},
                {0.23113103, 0.23113103, -0.49325132}}, false);
        RealVector x = inverseL.operate(b);
        RealMatrix xMatrix = new Array2DRowRealMatrix(new double[][]{{x.getEntry(0)}, {x.getEntry(1)}, {x.getEntry(2)}});
        assertEquals(expectedX, xMatrix);
    }

    @Test
    public void testRank() throws Exception {
        svd = new SingularValueDecompositionImpl(matrix);
        assertEquals(3, svd.getRank());
    }

    @Test
    public void testGetSingularValues() throws Exception {
        svd = new SingularValueDecompositionImpl(matrix);
        double[] expectedSingularValues = {2.0, 1.38585329, 0.0};
        assertArrayEquals(expectedSingularValues, svd.getSingularValues(), 1e-13);
    }

    @Test(expected = MathRuntimeException.class)
    public void testNegativeSingularValues() throws Exception {
        svd = new SingularValueDecompositionImpl(new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, -1.0}}));
    }

    private static double[] singularValues(double[][] data) {
        SingularValueDecomposition svd = new SingularValueDecomposition(data);
        return svd.getSingularValues().toArray();
    }

}