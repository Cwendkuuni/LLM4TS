package org.apache.commons.math3.linear;

import org.junit.Test;
import static org.junit.Assert.*;

public class SchurTransformerTest {

    @Test
    public void testGetP() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);

        RealMatrix actual = transformer.getP();
        RealMatrix expected = MatrixUtils.createRealMatrix(new double[][]{{-0.8944271909999159, 0.4472135954999579}, {-0.4472135954999579, -0.8944271909999159}});

        assertEquals(expected, actual);
    }

    @Test
    public void testGetPT() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);

        RealMatrix actual = transformer.getPT();
        RealMatrix expected = MatrixUtils.createRealMatrix(new double[][]{{-0.8944271909999159, -0.4472135954999579}, {0.4472135954999579, -0.8944271909999159}});

        assertEquals(expected, actual);
    }

    @Test
    public void testGetT() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);

        RealMatrix actual = transformer.getT();
        RealMatrix expected = MatrixUtils.createRealMatrix(new double[][]{{1.381966011250105, 4.0}, {3.2270327300281488, -0.38196601125010516}});

        assertEquals(expected, actual);
    }
}