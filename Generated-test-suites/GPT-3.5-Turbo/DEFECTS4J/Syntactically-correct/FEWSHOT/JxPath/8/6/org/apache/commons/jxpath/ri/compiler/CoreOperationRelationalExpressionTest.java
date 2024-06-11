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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Before;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    private CoreOperationRelationalExpression expression;

    @Before
    public void setUp() {
        expression = new CoreOperationRelationalExpression(null) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
    }

    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext(null);
        context.setSingleNodePointer(new SelfContext(context));
        
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testGetPrecedence() {
        int precedence = expression.getPrecedence();
        assertEquals(3, precedence);
    }

    @Test
    public void testIsSymmetric() {
        boolean symmetric = expression.isSymmetric();
        assertFalse(symmetric);
    }

    @Test
    public void testCompute() {
        Object left = 10;
        Object right = 5;
        
        boolean result = expression.compute(left, right);
        assertTrue(result);
    }

    @Test
    public void testReduce() {
        Object o = new SelfContext(null);
        
        Object result = expression.reduce(o);
        assertTrue(result instanceof SelfContext);
    }

    @Test
    public void testContainsMatch() {
        Collection<Object> collection = new ArrayList<Object>();
        collection.add(1);
        collection.add(2);
        Iterator<Object> it = collection.iterator();
        
        boolean result = expression.containsMatch(it, 2);
        assertTrue(result);
    }

    @Test
    public void testFindMatch() {
        Collection<Object> leftCollection = new ArrayList<Object>();
        leftCollection.add(1);
        leftCollection.add(2);
        Iterator<Object> leftIt = leftCollection.iterator();
        
        Collection<Object> rightCollection = new ArrayList<Object>();
        rightCollection.add(3);
        rightCollection.add(2);
        Iterator<Object> rightIt = rightCollection.iterator();
        
        boolean result = expression.findMatch(leftIt, rightIt);
        assertTrue(result);
    }
}