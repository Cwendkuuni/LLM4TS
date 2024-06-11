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

package org.apache.commons.math3.optimization;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.NonSquareMatrixException;

import static org.junit.Assert.*;

import org.junit.Test;

public class WeightTest {
    
    @Test
    public void testWeightConstructor() {
        double[] weights = {1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);
        RealMatrix weightMatrix = weight.getWeight();
        
        int n = weightMatrix.getRowDimension();
        assertEquals(weights.length, n);
        assertEquals(weights.length, weightMatrix.getColumnDimension());
        
        for (int i = 0; i < n; i++) {
            assertEquals(weights[i], weightMatrix.getEntry(i, i), 0.0);
        }
    }
    
    @Test
    public void testWeightMatrixConstructor() {
        RealMatrix weightMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 2.0}});
        Weight weight = new Weight(weightMatrix);
        
        RealMatrix actualWeightMatrix = weight.getWeight();
        assertEquals(weightMatrix.getRowDimension(), actualWeightMatrix.getRowDimension());
        assertEquals(weightMatrix.getColumnDimension(), actualWeightMatrix.getColumnDimension());
        
        for (int i = 0; i < weightMatrix.getRowDimension(); i++) {
            for (int j = 0; j < weightMatrix.getColumnDimension(); j++) {
                assertEquals(weightMatrix.getEntry(i, j), actualWeightMatrix.getEntry(i, j), 0.0);
            }
        }
    }
    
    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareWeightMatrixConstructor() {
        RealMatrix weightMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}});
        new Weight(weightMatrix);
    }
    
    @Test
    public void testGetWeight() {
        double[] weights = {1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);
        
        RealMatrix weightMatrix = weight.getWeight();
        assertNotNull(weightMatrix);
        
        int n = weightMatrix.getRowDimension();
        assertEquals(weights.length, n);
        assertEquals(weights.length, weightMatrix.getColumnDimension());
        
        for (int i = 0; i < n; i++) {
            assertEquals(weights[i], weightMatrix.getEntry(i, i), 0.0);
        }
    }
}