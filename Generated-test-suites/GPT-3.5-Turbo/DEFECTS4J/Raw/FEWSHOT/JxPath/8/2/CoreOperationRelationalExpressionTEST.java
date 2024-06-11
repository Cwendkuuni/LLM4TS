/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.jxpath.ri.compiler;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        // Initialize
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                // Test implementation
                return false;
            }
        };
        EvalContext context = new EvalContext(null, null, null);
        context.setSingleNodePointer(null);

        // Test cases
        assertEquals(Boolean.FALSE, expression.computeValue(context));
    }

    @Test
    public void testGetPrecedence() {
        // Initialize
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };

        // Test cases
        assertEquals(3, expression.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
         // Initialize
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };

        // Test cases
        assertEquals(false, expression.isSymmetric());
    }

}