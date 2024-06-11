package org.apache.commons.math3.linear;

import org.junit.Test;
import static org.junit.Assert.*;

public class SchurTransformerTest {

    @Test
    public void testGetP() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expectedP = MatrixUtils.createRealMatrix(new double[][]{{-0.464080180993835, 0.8829058010338913, -0.408248290463863}, {-0.5700885896423918, 0.2395204205863557, 0.8164965809277261}, {-0.6789777206823764, -0.4082482904638631, -0.408248290463863}});
        
        RealMatrix actualP = transformer.getP();
        
        assertEquals(expectedP, actualP);
    }
    
    @Test
    public void testGetPT() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expectedPT = MatrixUtils.createRealMatrix(new double[][]{{-0.464080180993835, -0.5700885896423917, -0.6789777206823765}, {0.8829058010338913, 0.23952042058635567, -0.408248290463863}, {-0.408248290463863, 0.816496580927726, -0.4082482904638631}});
        
        RealMatrix actualPT = transformer.getPT();
        
        assertEquals(expectedPT, actualPT);
    }
    
    @Test
    public void testGetT() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expectedT = MatrixUtils.createRealMatrix(new double[][]{{0.3333333333333333, 3.6333333333333326, 7.252235853184547E-16}, {3.4211388289951703E-16, -1.116843969807042E-16, -3.700743415417006}, {0.0, -4.567328978493178E-17, -3.0213120747411026E-17}});
        
        RealMatrix actualT = transformer.getT();
        
        assertEquals(expectedT, actualT);
    }

}