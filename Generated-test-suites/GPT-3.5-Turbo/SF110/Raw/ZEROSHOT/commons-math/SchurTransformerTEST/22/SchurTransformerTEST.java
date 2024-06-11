package org.apache.commons.math3.linear;

import static org.junit.Assert.*;
import org.apache.commons.math3.util.*;
import org.junit.Before;
import org.junit.Test;

public class SchurTransformerTest {

    private SchurTransformer schurTransformer;

    @Before
    public void setUp() {
        double[][] matrix = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        RealMatrix realMatrix = MatrixUtils.createRealMatrix(matrix);
        schurTransformer = new SchurTransformer(realMatrix);
    }

    @Test
    public void testGetP() {
        RealMatrix p = schurTransformer.getP();
        double[][] expectedMatrix = {{-0.6890724810962327, 0.7262313240404491, 1.0}, {-0.44504186791262857, -0.7972211669090129, 0.0}, {-0.5726784457660427, -0.5267454348104491, 0.0}};
        double[][] actualMatrix = p.getData();
        assertArrayEquals(expectedMatrix, actualMatrix);
    }

    @Test
    public void testGetPT() {
        RealMatrix pt = schurTransformer.getPT();
        double[][] expectedMatrix = {{-0.6890724810962327, -0.44504186791262857, -0.5726784457660427}, {0.7262313240404491, -0.7972211669090129, -0.5267454348104491}, {1.0, 0.0, 0.0}};
        double[][] actualMatrix = pt.getData();
        assertArrayEquals(expectedMatrix, actualMatrix);
    }

    @Test
    public void testGetT() {
        RealMatrix t = schurTransformer.getT();
        double[][] expectedMatrix = {{0.8485281374238564, 1.986093794862744, 3.042057784731902}, {0.24852813742385658, -1.292893218813452, -6.846426754104601}, {-0.5536047650710423, -4.303615371519635, 0.0}};
        double[][] actualMatrix = t.getData();
        assertArrayEquals(expectedMatrix, actualMatrix);
    }
}