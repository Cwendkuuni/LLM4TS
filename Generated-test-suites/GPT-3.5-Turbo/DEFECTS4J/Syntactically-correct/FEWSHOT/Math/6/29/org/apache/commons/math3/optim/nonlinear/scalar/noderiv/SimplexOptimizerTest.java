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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.optimization.MultivariateOptimizer;
import org.apache.commons.math3.optimization.OptimizationData;
import org.junit.Before;
import org.junit.Test;

public class SimplexOptimizerTest {
    
    private SimplexOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new SimplexOptimizer();
    }

    @Test
    public void testOptimize() {
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] point) {
                return (point[0] - 1) * (point[0] - 1) + (point[1] - 2) * (point[1] - 2);
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        OptimizationData[] optData = new OptimizationData[] {
            new InitialGuess(new double[] {0, 0}),
            new AbstractSimplex(new double[][] {{1, 0}, {0, 1}, {0, 0}}),
        };

        PointValuePair result = optimizer.optimize(100, f, goalType, optData);

        assertTrue(optimizer.getIterations() > 0);
        assertTrue(optimizer.getEvaluations() > 0);
        assertEquals(1.0, result.getPoint()[0], 1e-5);
        assertEquals(2.0, result.getPoint()[1], 1e-5);
    }

    @Test(expected = NullArgumentException.class)
    public void testOptimizeNullSimplex() {
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] point) {
                return (point[0] - 1) * (point[0] - 1) + (point[1] - 2) * (point[1] - 2);
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        OptimizationData[] optData = new OptimizationData[] {
            new InitialGuess(new double[] {0, 0}),
        };

        optimizer.setSimplex(null);
        optimizer.optimize(100, f, goalType, optData);
    }

    @Test
    public void testOptimizeConvergenceChecker() {
        ConvergenceChecker<PointValuePair> convergenceChecker = new SimpleValueChecker(1e-6, 1e-6);
        optimizer = new SimplexOptimizer(convergenceChecker);

        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] point) {
                return (point[0] - 1) * (point[0] - 1) + (point[1] - 2) * (point[1] - 2);
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        OptimizationData[] optData = new OptimizationData[] {
            new InitialGuess(new double[] {0, 0}),
            new AbstractSimplex(new double[][] {{1, 0}, {0, 1}, {0, 0}}),
        };

        PointValuePair result = optimizer.optimize(500, f, goalType, optData);

        assertTrue(optimizer.getIterations() > 0);
        assertTrue(optimizer.getEvaluations() > 0);
        assertEquals(1.0, result.getPoint()[0], 1e-6);
        assertEquals(2.0, result.getPoint()[1], 1e-6);
    }

}