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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Resolver;
import org.junit.Before;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    private CoreOperationRelationalExpression operation;
    private Expression[] args;
    private EvalContext context;

    @Before
    public void setup() {
        args = new Expression[2];
        args[0] = mock(Expression.class);
        args[1] = mock(Expression.class);
        operation = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        context = mock(EvalContext.class);
        when(args[0].compute(context)).thenReturn(null);
        when(args[1].compute(context)).thenReturn(null);
    }

    @Test
    public void testComputeValueTrue() {
        when(operation.args[0].compute(context)).thenReturn(2);
        when(operation.args[1].compute(context)).thenReturn(1);
        assertTrue((Boolean) operation.computeValue(context));
    }

    @Test
    public void testComputeValueFalse() {
        when(operation.args[0].compute(context)).thenReturn(1);
        when(operation.args[1].compute(context)).thenReturn(2);
        assertFalse((Boolean) operation.computeValue(context));
    }

    @Test
    public void testGetPrecedence() {
        assertEquals(CoreOperation.RELATIONAL_EXPR_PRECEDENCE, operation.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        assertFalse(operation.isSymmetric());
    }

    @Test
    public void testComputeObjectObject() {
        when(args[0].compute(context)).thenReturn(2);
        when(args[1].compute(context)).thenReturn(1);
        assertTrue(operation.compute(2, 1));
        when(args[0].compute(context)).thenReturn(1);
        when(args[1].compute(context)).thenReturn(2);
        assertFalse(operation.compute(1, 2));
    }

    @Test
    public void testReduceSelfContext() {
        SelfContext selfContext = mock(SelfContext.class);
        when(selfContext.getSingleNodePointer()).thenReturn(2);
        assertEquals(2, operation.reduce(selfContext));
    }

    @Test
    public void testReduceCollection() {
        Collection<Integer> collection = new HashSet<>();
        collection.add(1);
        Iterator<Integer> iterator = operation.reduce(collection);
        assertTrue(iterator.hasNext());
        assertEquals(1, (int) iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testContainsMatch() {
        Iterator<Integer> iterator = mock(Iterator.class);
        when(iterator.hasNext()).thenReturn(true, true, false);
        when(iterator.next()).thenReturn(1, 2);
        assertTrue(operation.containsMatch(iterator, 2));
        assertFalse(operation.containsMatch(iterator, 3));
    }

    @Test
    public void testFindMatch() {
        Iterator<Integer> left = mock(Iterator.class);
        Iterator<Integer> right = mock(Iterator.class);
        Iterator<Integer> emptyIterator = mock(Iterator.class);
        when(left.hasNext()).thenReturn(true, true, false);
        when(right.hasNext()).thenReturn(true, true, false);
        when(left.next()).thenReturn(1, 2);
        when(right.next()).thenReturn(2, 3);
        when(emptyIterator.hasNext()).thenReturn(false);
        assertTrue(operation.findMatch(left, right));
        assertFalse(operation.findMatch(emptyIterator, right));
    }
}