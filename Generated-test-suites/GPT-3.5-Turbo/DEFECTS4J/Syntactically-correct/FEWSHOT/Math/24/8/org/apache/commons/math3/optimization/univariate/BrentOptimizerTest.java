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

import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

public class BrentOptimizerTest {
    // Test BrentOptimizer with various inputs
    
    @Test
    public void testBrentOptimizerWithPositiveAbsoluteThresholdAndNullChecker() {
        double absoluteThreshold = 1e-6;
        double relativeThreshold = 1e-10;
        double funcRelThreshold = -1;
        
        double result = Double.NaN;

        BrentOptimizer optimizer = new BrentOptimizer(relativeThreshold, absoluteThreshold, null);

        try {
            result = optimizer.optimize(100, (x) -> Math.exp(-x) - Math.log(x));
        } catch (Exception e) {
            fail("Exception thrown during optimization: " + e.getMessage());
        }

        assertTrue(result > 0);
    }
    
    @Test
    public void testBrentOptimizerWithNegativeRelativeThresholdAndNullChecker() {
        double absoluteThreshold = 1e-6;
        double relativeThreshold = -1;
        double funcRelThreshold = -1;
        
        double result = Double.NaN;

        BrentOptimizer optimizer = new BrentOptimizer(relativeThreshold, absoluteThreshold, null);

        try {
            result = optimizer.optimize(0, Math::abs);
        } catch (Exception e) {
            fail("Exception thrown during optimization: " + e.getMessage());
        }

        assertEquals(0, result, 1e-4);
    }
    
    @Test
    public void testBrentOptimizerWithZeroAbsoluteThresholdAndNullChecker() {
        double absoluteThreshold = 0;
        double relativeThreshold = 1e-10;
        double funcRelThreshold = -1;
        
        double result = Double.NaN;

        BrentOptimizer optimizer = new BrentOptimizer(relativeThreshold, absoluteThreshold, null);

        try {
            result = optimizer.optimize(2, (x) -> x * x - 4);
        } catch (Exception e) {
            fail("Exception thrown during optimization: " + e.getMessage());
        }

        assertEquals(2, result, 1e-4);
    }
    
    @Test
    public void testBrentOptimizerWithZeroRelativeThresholdAndNullChecker() {
        double absoluteThreshold = 1e-6;
        double relativeThreshold = 0;
        double funcRelThreshold = -1;
        
        double result = Double.NaN;

        BrentOptimizer optimizer = new BrentOptimizer(relativeThreshold, absoluteThreshold, null);

        try {
            result = optimizer.optimize(2, (x) -> x * x - 4);
        } catch (Exception e) {
            fail("Exception thrown during optimization: " + e.getMessage());
        }

        assertEquals(2, result, 1e-4);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerWithNegativeAbsoluteThreshold() {
        double absoluteThreshold = -1e-6;
        double relativeThreshold = 1e-10;
        double funcRelThreshold = -1;
        
        BrentOptimizer optimizer = new BrentOptimizer(relativeThreshold, absoluteThreshold, null);
    }
    
    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerWithNegativeRelativeThreshold() {
        double absoluteThreshold = 1e-6;
        double relativeThreshold = -1e-10;
        double funcRelThreshold = -1;
        
        BrentOptimizer optimizer = new BrentOptimizer(relativeThreshold, absoluteThreshold, null);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerWithZeroAbsoluteThreshold() {
        double absoluteThreshold = 0;
        double relativeThreshold = 1e-10;
        double funcRelThreshold = -1;
        
        BrentOptimizer optimizer = new BrentOptimizer(relativeThreshold, absoluteThreshold, null);
    }
    
    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerWithZeroRelativeThreshold() {
        double absoluteThreshold = 1e-6;
        double relativeThreshold = 0;
        double funcRelThreshold = -1;
        
        BrentOptimizer optimizer = new BrentOptimizer(relativeThreshold, absoluteThreshold, null);
    }
}