
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

package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class BrentOptimizerTest {

    private static final double EPSILON = 1e-10;

    private final double[] inputs = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

    private final double[] expectedMinima = {0.0, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0};

    private final double[] expectedMaxima = {0.0, -1.0, -2.0, -3.0, -4.0, -5.0, -6.0, -7.0, -8.0, -9.0};

    private double function(double x) {
        return x;
    }

    private double functionNeg(double x) {
        return -x;
    }

    @Test
    public void testFindMinimum() {
        for (int i = 0; i < inputs.length; i++) {
            BrentOptimizer optimizer = new BrentOptimizer(EPSILON, EPSILON);
            optimizer.setMin(inputs[i]);
            optimizer.setMax(inputs[i]);
            optimizer.setStartValue(inputs[i]);
            UnivariatePointValuePair result = optimizer.optimize(
                    1000, new UnivariateObjectiveFunction(this::function),
                    GoalType.MINIMIZE, new UnivariateInterval(0, 1, inputs[i]));
            assertEquals(expectedMinima[i], result.getPoint(), EPSILON);
        }
    }

    @Test
    public void testFindMaximum() {
        for (int i = 0; i < inputs.length; i++) {
            BrentOptimizer optimizer = new BrentOptimizer(EPSILON, EPSILON);
            optimizer.setMin(inputs[i]);
            optimizer.setMax(inputs[i]);
            optimizer.setStartValue(inputs[i]);
            UnivariatePointValuePair result = optimizer.optimize(
                    1000, new UnivariateObjectiveFunction(this::functionNeg),
                    GoalType.MAXIMIZE, new UnivariateInterval(0, 1, inputs[i]));
            assertEquals(expectedMaxima[i], result.getPoint(), EPSILON);
        }
    }

    @Test
    public void testInvalidRelativeThreshold() {
        assertThrows(NumberIsTooSmallException.class, () -> new BrentOptimizer(1e-16, EPSILON));
    }

    @Test
    public void testInvalidAbsoluteThreshold() {
        assertThrows(NotStrictlyPositiveException.class, () -> new BrentOptimizer(EPSILON, 0));
    }

    @Test
    public void testInvalidAbsoluteThreshold2() {
        assertThrows(NotStrictlyPositiveException.class, () -> new BrentOptimizer(EPSILON, -1));
    }
}

