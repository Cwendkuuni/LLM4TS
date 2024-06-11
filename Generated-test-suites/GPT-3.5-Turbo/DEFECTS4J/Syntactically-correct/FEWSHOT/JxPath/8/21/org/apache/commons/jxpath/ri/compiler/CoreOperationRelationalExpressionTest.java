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
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {
    
    @Test
    public void testComputeValue() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionImpl(args);
        
        EvalContext context = new EvalContext();
        context.setSingleNodePointer(new SelfContext());
        
        Object result = expression.computeValue(context);
        
        // assert result here
        
    }
    
    @Test
    public void testGetPrecedence() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionImpl(args);
        
        int result = expression.getPrecedence();
        
        // assert result here
        
    }
    
    @Test
    public void testIsSymmetric() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionImpl(args);
        
        boolean result = expression.isSymmetric();
        
        // assert result here
        
    }
    
    @Test
    public void testEvaluateCompare() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionImpl(args);
        
        int compare = 0; // assign a value
        
        boolean result = expression.evaluateCompare(compare);
        
        // assert result here
        
    }
    
    @Test
    public void testCompute() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionImpl(args);
        
        Object left = new Object(); // assign a value
        Object right = new Object(); // assign a value
        
        boolean result = expression.compute(left, right);
        
        // assert result here
        
    }
    
    @Test
    public void testReduce() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionImpl(args);
        
        Object o = new Object(); // assign a value
        
        Object result = expression.reduce(o);
        
        // assert result here
        
    }
    
    @Test
    public void testContainsMatch() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionImpl(args);
        
        Iterator it = new Iterator(); // assign a value
        Object value = new Object(); // assign a value
        
        boolean result = expression.containsMatch(it, value);
        
        // assert result here
        
    }
    
    @Test
    public void testFindMatch() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionImpl(args);
        
        Iterator lit = new Iterator(); // assign a value
        Iterator rit = new Iterator(); // assign a value
        
        boolean result = expression.findMatch(lit, rit);
        
        // assert result here
        
    }
    
    private class CoreOperationRelationalExpressionImpl extends CoreOperationRelationalExpression {
        
        public CoreOperationRelationalExpressionImpl(Expression[] args) {
            super(args);
        }
        
        protected boolean evaluateCompare(int compare) {
            // implement this method
        }
        
    }

}