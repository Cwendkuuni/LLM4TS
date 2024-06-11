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
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {
    @Test
    public void testComputeValue() {
        Object[] objectArray = {new Object(), new Object()};
        EvalContext context = new EvalContext();
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(objectArray) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };

        Object result = expression.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGetPrecedence() {
        Object[] objectArray = {new Object(), new Object()};
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(objectArray) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };

        int result = expression.getPrecedence();
        assertEquals(3, result);
    }

    @Test
    public void testIsSymmetric() {
        Object[] objectArray = {new Object(), new Object()};
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(objectArray) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };

        boolean result = expression.isSymmetric();
        assertEquals(false, result);
    }

    @Test
    public void testCompute() {
        Object[] objectArray = {new Object(), new Object()};
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(objectArray) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };

        Object result = expression.compute(new Object(), new Object());
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testReduce1() {
        Object[] objectArray = {new Object(), new Object()};
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(objectArray) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };

        Object result = expression.reduce(new SelfContext());
        assertEquals(new SelfContext(), result);
    }

    @Test
    public void testReduce2() {
        Object[] objectArray = {new Object(), new Object()};
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(objectArray) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };

        Object result = expression.reduce(new HashSet<>());
        assertEquals(new HashSet<>().iterator(), result);
    }

    @Test
    public void testContainsMatch() {
        Object[] objectArray = {new Object(), new Object()};
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(objectArray) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };

        Iterator iterator = new HashSet<>().iterator();
        boolean result = expression.containsMatch(iterator, new Object());
        assertEquals(false, result);
    }

    @Test
    public void testFindMatch() {
        Object[] objectArray = {new Object(), new Object()};
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(objectArray) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };

        Iterator literalIterator = new HashSet<>().iterator();
        Iterator rightIterator = new HashSet<>().iterator();
        boolean result = expression.findMatch(literalIterator, rightIterator);
        assertEquals(false, result);
    }
}