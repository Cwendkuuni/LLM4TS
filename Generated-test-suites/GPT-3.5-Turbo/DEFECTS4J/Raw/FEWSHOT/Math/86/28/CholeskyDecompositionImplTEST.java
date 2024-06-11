/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.math.linear;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CholeskyDecompositionImplTest {
    
    private CholeskyDecompositionImpl decomposition;
    private double[][] matrixData;
    private double[][] expectedL;
    
    @Before
    public void setUp() {
        matrixData = new double[][] { {25, 15, -5}, {15, 18, 0}, {-5, 0, 11} };
        expectedL = new double[][] { {5, 0, 0}, {3, 3, 0}, {-1, 1, 3} };
        decomposition = new CholeskyDecompositionImpl(new Array2DRowRealMatrix(matrixData));
    }
    
    @Test
    public void testGetL() {
        RealMatrix L = decomposition.getL();
        assertEquals(expectedL.length, L.getRowDimension());
        assertEquals(expectedL[0].length, L.getColumnDimension());
        for (int i = 0; i < expectedL.length; i++) {
            for (int j = 0; j < expectedL[0].length; j++) {
                assertEquals(expectedL[i][j], L.getEntry(i, j), 0.000001);
            }
        }
    }
    
    @Test
    public void testGetLT() {
        RealMatrix LT = decomposition.getLT();
        assertEquals(expectedL[0].length, LT.getRowDimension());
        assertEquals(expectedL.length, LT.getColumnDimension());
        for (int i = 0; i < expectedL[0].length; i++) {
            for (int j = 0; j < expectedL.length; j++) {
                assertEquals(expectedL[j][i], LT.getEntry(i, j), 0.000001);
            }
        }
    }
    
    @Test
    public void testGetDeterminant() {
        double determinant = decomposition.getDeterminant();
        double expectedDeterminant = 5 * 3 * 3;
        assertEquals(expectedDeterminant, determinant, 0.000001);
    }
    
    @Test
    public void testGetSolver() {
        DecompositionSolver solver = decomposition.getSolver();
        double[] b = {1, 2, 3};
        double[] x = solver.solve(b);
        double[] expectedX = {-0.6, 0.6, 1.2};
        for (int i = 0; i < expectedX.length; i++) {
            assertEquals(expectedX[i], x[i], 0.000001);
        }
    }
    
}