
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

import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.NodePointer;
import org.apache.commons.jxpath.Pointer;
import org.junit.Test;

import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;

import static org.junit.Assert.*;

public class ExpressionTest {
    private class TestExpression extends Expression {
        private boolean isContextDependent;

        public TestExpression(boolean isContextDependent) {
            this.isContextDependent = isContextDependent;
        }

        @Override
        public boolean computeContextDependent() {
            return isContextDependent;
        }

        @Override
        public Object computeValue(EvalContext context) {
            return null;
        }

        @Override
        public Object compute(EvalContext context) {
            return null;
        }
    }

    @Test
    public void testIsContextDependent_True() {
        TestExpression expression = new TestExpression(true);
        assertTrue(expression.isContextDependent());
    }

    @Test
    public void testIsContextDependent_False() {
        TestExpression expression = new TestExpression(false);
        assertFalse(expression.isContextDependent());
    }

    @Test
    public void testComputeValue_Null() {
        TestExpression expression = new TestExpression(true);
        assertNull(expression.computeValue(null));
    }

    @Test
    public void testCompute_Null() {
        TestExpression expression = new TestExpression(true);
        assertNull(expression.compute(null));
    }

    @Test
    public void testIterate_Null() {
        TestExpression expression = new TestExpression(true);
        Iterator iterator = expression.iterate(null);
        assertNotNull(iterator);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratePointers_Null() {
        TestExpression expression = new TestExpression(true);
        Iterator iterator = expression.iteratePointers(null);
        assertNotNull(iterator);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testPointerIterator_HasNext() {
        TestExpression.PointerIterator iterator = new TestExpression.PointerIterator(Collections.singleton(new Pointer()).iterator(), null, Locale.getDefault());
        assertTrue(iterator.hasNext());
    }

    @Test
    public void testPointerIterator_Next() {
        Pointer pointer = new Pointer();
        TestExpression.PointerIterator iterator = new TestExpression.PointerIterator(Collections.singleton(pointer).iterator(), null, Locale.getDefault());
        assertSame(pointer, iterator.next());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testPointerIterator_Remove() {
        TestExpression.PointerIterator iterator = new TestExpression.PointerIterator(Collections.emptyList().iterator(), null, Locale.getDefault());
        iterator.remove();
    }

    @Test
    public void testValueIterator_HasNext() {
        TestExpression.ValueIterator iterator = new TestExpression.ValueIterator(Collections.singletonList(new Pointer()).iterator());
        assertTrue(iterator.hasNext());
    }

    @Test
    public void testValueIterator_Next() {
        Pointer pointer = new Pointer();
        TestExpression.ValueIterator iterator = new TestExpression.ValueIterator(Collections.singletonList(pointer).iterator());
        assertSame(pointer.getValue(), iterator.next());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testValueIterator_Remove() {
        TestExpression.ValueIterator iterator = new TestExpression.ValueIterator(Collections.emptyList().iterator());
        iterator.remove();
    }
}
