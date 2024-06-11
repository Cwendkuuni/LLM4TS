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

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CoreOperationRelationalExpressionTest {
    @Test
    public void testComputeValue() {
        CoreOperationRelationalExpression expression = createExpression();
        EvalContext context = createContext();
        Object value = expression.computeValue(context);
        assertTrue(value instanceof Boolean);
        assertTrue((Boolean) value);
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = createExpression();
        assertEquals(3, expression.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = createExpression();
        assertFalse(expression.isSymmetric());
    }

    @Test
    public void testEvaluateCompare() {
        CoreOperationRelationalExpression expression = createExpression();
        boolean result = expression.evaluateCompare(0);
        assertTrue(result);
    }

    @Test
    public void testCompute() {
        CoreOperationRelationalExpression expression = createExpression();
        EvalContext context = createContext();
        Object value = expression.compute(context, context);
        assertTrue(value instanceof Boolean);
        assertTrue((Boolean) value);
    }

    @Test
    public void testReduce() {
        CoreOperationRelationalExpression expression = createExpression();
        Object result = expression.reduce(new SelfContext());
        assertTrue(result instanceof SelfContext);
    }

    @Test
    public void testContainsMatch() {
        CoreOperationRelationalExpression expression = createExpression();
        Iterator it = createIterator();
        boolean result = expression.containsMatch(it, "test");
        assertTrue(result);
    }

    @Test
    public void testFindMatch() {
        CoreOperationRelationalExpression expression = createExpression();
        Iterator lit = createIterator();
        Iterator rit = createIterator();
        boolean result = expression.findMatch(lit, rit);
        assertTrue(result);
    }

    private CoreOperationRelationalExpression createExpression() {
        return new CoreOperationRelationalExpression(new Expression[]{});
    }

    private EvalContext createContext() {
        return new EvalContext();
    }

    private Iterator createIterator() {
        Collection<String> collection = new HashSet<>();
        collection.add("test");
        return collection.iterator();
    }
}