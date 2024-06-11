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

import org.junit.Test;

public class CoreOperationRelationalExpressionTest {
    @Test
    public void testComputeValue() {
        Expression[] args = new Expression[2];
        args[0] = new ConstantExpression("foo");
        args[1] = new ConstantExpression("bar");
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };

        assertFalse(expression.computeValue(null));

        args[0] = new ConstantExpression("foo");
        args[1] = new ConstantExpression("foo");
        expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };

        assertTrue(expression.computeValue(null));

        args[0] = new ConstantExpression("bar");
        args[1] = new ConstantExpression("foo");
        expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare < 0;
            }
        };

        assertFalse(expression.computeValue(null));

        args[0] = new ConstantExpression("bar");
        args[1] = new ConstantExpression("foo");
        expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        assertTrue(expression.computeValue(null));
    }

    @Test
    public void testGetPrecedence() {
        Expression[] args = new Expression[2];
        args[0] = new ConstantExpression("foo");
        args[1] = new ConstantExpression("bar");
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };

        assertEquals(3, expression.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        Expression[] args = new Expression[2];
        args[0] = new ConstantExpression("foo");
        args[1] = new ConstantExpression("bar");
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };

        assertFalse(expression.isSymmetric());
    }

    @Test
    public void testCompute() {
        Expression[] args = new Expression[2];
        args[0] = new ConstantExpression("foo");
        args[1] = new ConstantExpression("bar");
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };

        assertFalse(expression.compute("foo", "bar"));

        assertFalse(expression.compute("foo", "foo"));

        assertFalse(expression.compute("bar", "foo"));

        assertFalse(expression.compute("bar", "foo"));
    }

    @Test
    public void testReduce() {
        Expression[] args = new Expression[2];
        args[0] = new ConstantExpression("foo");
        args[1] = new ConstantExpression("bar");
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };

        Object result = expression.reduce("foo");

        assertEquals("foo", result);

        Object collection = new Object();
        result = expression.reduce(collection);

        assertEquals(collection, result);

        collection = new MyClass();
        result = expression.reduce(collection);

        assertEquals(collection, result);
    }

    @Test
    public void testContainsMatch() {
        Expression[] args = new Expression[2];
        args[0] = new ConstantExpression("element");
        args[1] = new ConstantExpression("value");
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };

        assertFalse(expression.containsMatch(new Iterator() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Object next() {
                return null;
            }
        }, "value"));

        assertTrue(expression.containsMatch(new Iterator() {
            private boolean hasNext = true;

            @Override
            public boolean hasNext() {
                return hasNext;
            }

            @Override
            public Object next() {
                if (hasNext) {
                    hasNext = false;
                    return "value";
                }

                return null;
            }
        }, "value"));
    }

    @Test
    public void testFindMatch() {
        Expression[] args = new Expression[2];
        args[0] = new ConstantExpression("element");
        args[1] = new ConstantExpression("value");
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };

        assertFalse(expression.findMatch(new Iterator() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Object next() {
                return null;
            }
        }, new Iterator() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Object next() {
                return null;
            }
        }));

        assertTrue(expression.findMatch(new Iterator() {
            private boolean hasNext = true;

            @Override
            public boolean hasNext() {
                return hasNext;
            }

            @Override
            public Object next() {
                if (hasNext) {
                    hasNext = false;
                    return "value";
                }

                return null;
            }
        }, new Iterator() {
            private boolean hasNext = true;

            @Override
            public boolean hasNext() {
                return hasNext;
            }

            @Override
            public Object next() {
                if (hasNext) {
                    hasNext = false;
                    return "element";
                }

                return null;
            }
        }));

        assertFalse(expression.findMatch(new Iterator() {
            private boolean hasNext = true;

            @Override
            public boolean hasNext() {
                return hasNext;
            }

            @Override
            public Object next() {
                if (hasNext) {
                    hasNext = false;
                    return "different";
                }

                return null;
            }
        }, new Iterator() {
            private boolean hasNext = true;

            @Override
            public boolean hasNext() {
                return hasNext;
            }

            @Override
            public Object next() {
                if (hasNext) {
                    hasNext = false;
                    return "element";
                }

                return null;
            }
        }));
    }

    private class MyClass {
    }
}