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

package org.apache.commons.math3.optimization.direct;

import java.util.Comparator;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.optimization.MultivariateOptimizer;
import org.apache.commons.math3.optimization.OptimizationData;
import org.junit.Test;

import static org.junit.Assert.*;

public class SimplexOptimizerTest {

    @Test
    public void testSetSimplex() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        AbstractSimplex simplex = new Simplex(new double[]{1, 2, 3}, new double[]{2, 3, 4}, new double[]{1, 1, 1});
        optimizer.setSimplex(simplex);
        assertEquals(simplex, optimizer.simplex);
    }

    @Test(expected = NullArgumentException.class)
    public void testSetSimplexNull() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.setSimplex(null);
    }

    @Test
    public void testParseOptimizationData() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        OptimizationData[] optData = new OptimizationData[] {new Simplex(new double[]{1, 2, 3}, new double[]{2, 3, 4}, new double[]{1, 1, 1})};
        optimizer.parseOptimizationData(optData);
        assertEquals(optData[0], optimizer.simplex);
    }

    @Test
    public void testParseOptimizationDataNoSimplex() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        OptimizationData[] optData = new OptimizationData[0];
        optimizer.parseOptimizationData(optData);
        assertNull(optimizer.simplex);
    }

    @Test
    public void testOptimizeInternal() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.setSimplex(new Simplex(new double[]{1, 2, 3}, new double[]{2, 3, 4}, new double[]{1, 1, 1}));
        int maxEval = 100;
        MultivariateFunction f = new MultivariateFunction() {
            @Override
            public double value(double[] point) {
                return point[0] + point[1] + point[2];
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        OptimizationData[] optData = new OptimizationData[0];
        PointValuePair result = optimizer.optimizeInternal(maxEval, f, goalType, optData);
        assertEquals(6.0, result.getPoint()[0], 0.0);
        assertEquals(0.0, result.getPoint()[1], 0.0);
        assertEquals(0.0, result.getPoint()[2], 0.0);
    }

    @Test
    public void testOptimizeInternalNoSimplex() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        int maxEval = 100;
        MultivariateFunction f = new MultivariateFunction() {
            @Override
            public double value(double[] point) {
                return point[0] + point[1] + point[2];
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        OptimizationData[] optData = new OptimizationData[0];
        PointValuePair result = optimizer.optimizeInternal(maxEval, f, goalType, optData);
        assertNull(result);
    }
}