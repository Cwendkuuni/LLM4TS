
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

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math.ConvergenceException;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.UnivariateRealFunction;

public class UnivariateRealSolverUtilsTest {

    private final double EPSILON = 1e-10;

    @Test
    public void testSolve() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        double actual = UnivariateRealSolverUtils.solve(function, 1, 3);

        assertEquals(2, actual, EPSILON);
    }

    @Test
    public void testSolveWithAccuracy() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        double actual = UnivariateRealSolverUtils.solve(function, 1, 3, 0.001);

        assertEquals(2, actual, EPSILON);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithNullFunction() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealSolverUtils.solve(null, 1, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithInvalidInterval() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        UnivariateRealSolverUtils.solve(function, 3, 1);
    }

    @Test
    public void testBracket() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        double[] actual = UnivariateRealSolverUtils.bracket(function, 1.5, 1, 3);

        assertEquals(1, actual[0], EPSILON);
        assertEquals(2, actual[1], EPSILON);
    }

    @Test
    public void testBracketWithMaxIterations() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        double[] actual = UnivariateRealSolverUtils.bracket(function, 1.5, 1, 3, 100);

        assertEquals(1, actual[0], EPSILON);
        assertEquals(2, actual[1], EPSILON);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBracketWithNullFunction() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealSolverUtils.bracket(null, 1.5, 1, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBracketWithInvalidInterval() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        UnivariateRealSolverUtils.bracket(function, 1.5, 3, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBracketWithNegativeMaxIterations() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        UnivariateRealSolverUtils.bracket(function, 1.5, 1, 3, -1);
    }

    @Test
    public void testMidpoint() {
        double a = 1;
        double b = 3;

        double actual = UnivariateRealSolverUtils.midpoint(a, b);

        assertEquals(2, actual, EPSILON);
    }
}
