
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

package org.apache.commons.math3.optimization.general;

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

import static org.junit.Assert.*;
import org.junit.Test;

public class GaussNewtonOptimizerTest {

    @Test(expected = NullArgumentException.class)
    public void testDoOptimize_ConvergenceCheckerNull() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        optimizer.setConvergenceChecker(null);
        optimizer.doOptimize();
    }

    @Test
    public void testDoOptimize_Simple() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        optimizer.setTarget(new double[]{1.0, 2.0, 3.0});
        optimizer.setStartPoint(new double[]{0.0, 0.0, 0.0});
        PointVectorValuePair result = optimizer.doOptimize();
        assertArrayEquals(new double[]{1.0, 2.0, 3.0}, result.getPoint(), 1e-10);
    }

    @Test
    public void testDoOptimize_SingularMatrixException() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        optimizer.setTarget(new double[]{1.0, 1.0, 1.0});
        optimizer.setStartPoint(new double[]{0.0, 0.0, 0.0});
        try {
            PointVectorValuePair result = optimizer.doOptimize();
            fail("Expected SingularMatrixException");
        } catch (ConvergenceException e) {
            assertEquals(LocalizedFormats.UNABLE_TO_SOLVE_SINGULAR_PROBLEM, e.getPattern());
        }
    }
}
