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
package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.junit.Before;
import org.junit.Test;

public class BaseSecantSolverTest {

    private static final double ABSOLUTE_ACCURACY = 1e-6;
    private static final double RELATIVE_ACCURACY = 0;
    private static final double FUNCTION_VALUE_ACCURACY = 1e-15;
    private static final double MIN = -10;
    private static final double MAX = 10;
    private static final double START_VALUE = 5;
    private static final int MAX_EVAL = 100;

    private BaseSecantSolver solver;
    private UnivariateRealFunction function;

    @Before
    public void setUp() {
        this.solver = new BaseSecantSolver(ABSOLUTE_ACCURACY, RELATIVE_ACCURACY,
                FUNCTION_VALUE_ACCURACY, BaseSecantSolver.Method.REGULA_FALSI) {
            protected double doSolve() {
                return 0;
            }
        };

        this.function = mock(UnivariateRealFunction.class);
        when(function.value(anyDouble())).thenReturn(0.0);
    }

    @Test
    public void testSolve() {
        double result = solver.solve(MAX_EVAL, function, MIN, MAX, START_VALUE);
        assertEquals(0.0, result, ABSOLUTE_ACCURACY);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveInvalidMaxEvaluations() {
        solver.solve(0, function, MIN, MAX, START_VALUE);
        fail("Expected IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveInvalidRange() {
        solver.solve(MAX_EVAL, function, MAX, MIN, START_VALUE);
        fail("Expected IllegalArgumentException");
    }

    @Test
    public void testSolveWithAllowedSolutions() {
        solver.solve(MAX_EVAL, function, MIN, MAX, START_VALUE,
                BaseSecantSolver.AllowedSolution.LEFT_SIDE);
        solver.solve(MAX_EVAL, function, MIN, MAX, START_VALUE,
                BaseSecantSolver.AllowedSolution.RIGHT_SIDE);
        solver.solve(MAX_EVAL, function, MIN, MAX, START_VALUE,
                BaseSecantSolver.AllowedSolution.ABOVE_SIDE);
        solver.solve(MAX_EVAL, function, MIN, MAX, START_VALUE,
                BaseSecantSolver.AllowedSolution.BELOW_SIDE);

        try {
            solver.solve(MAX_EVAL, function, MIN, MAX, START_VALUE,
                    BaseSecantSolver.AllowedSolution.ANY_SIDE);
            fail("Expected MathInternalError");
        } catch (MathInternalError e) {
            // Expected
        }
    }
}