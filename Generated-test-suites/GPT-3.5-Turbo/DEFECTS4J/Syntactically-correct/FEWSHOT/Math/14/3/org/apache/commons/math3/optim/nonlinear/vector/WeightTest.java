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

import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.NonSquareMatrixException;
import org.junit.Test;

public class WeightTest {
    
    @Test
    public void testWeight() {
        double[] weights = {1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);
        RealMatrix expectedMatrix = new Array2DRowRealMatrix(3, 3);
        expectedMatrix.setEntry(0, 0, 1.0);
        expectedMatrix.setEntry(1, 1, 2.0);
        expectedMatrix.setEntry(2, 2, 3.0);
        
        assertEquals(expectedMatrix, weight.getWeight());
    }
    
    @Test(expected = NonSquareMatrixException.class)
    public void testWeightWithNonSquareMatrix() {
        double[][] weights = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weights);
        new Weight(weightMatrix);
    }
    
    @Test
    public void testGetWeight() {
        double[] weights = {1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);
        
        assertEquals(new Array2DRowRealMatrix(3, 3), weight.getWeight());
    }
}