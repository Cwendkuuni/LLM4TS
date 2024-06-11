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

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Test;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext();

        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(null, null);
        assertEquals(Boolean.FALSE, expression.computeValue(context));

        expression = new CoreOperationLessThanOrEqual(new ExpressionMock(1), new ExpressionMock(2));
        assertEquals(Boolean.TRUE, expression.computeValue(context));

        expression = new CoreOperationLessThanOrEqual(new ExpressionMock(2), new ExpressionMock(1));
        assertEquals(Boolean.FALSE, expression.computeValue(context));

        expression = new CoreOperationLessThanOrEqual(new ExpressionMock(1), new ExpressionMock(1));
        assertEquals(Boolean.TRUE, expression.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(null, null);
        assertEquals("<=", expression.getSymbol());
    }

    private class ExpressionMock extends Expression {
        private Object value;

        public ExpressionMock(Object value) {
            this.value = value;
        }

        public Object computeValue(EvalContext context) {
            return value;
        }
    }
}