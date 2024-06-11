package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import org.junit.Test;

public class CoreOperationCompareTest {
    @Test
    public void testEqual() {
        CoreOperationCompare operation = new CoreOperationCompare(arg1, arg2);

        EvalContext context = new EvalContext();
        Expression left = new Expression() {
            @Override
            public Object compute(EvalContext context) {
                return "left";
            }
        };
        Expression right = new Expression() {
            @Override
            public Object compute(EvalContext context) {
                return "right";
            }
        };

        assertFalse(operation.equal(context, left, right));

        left = new Expression() {
            @Override
            public Object compute(EvalContext context) {
                return "left";
            }
        };
        right = new Expression() {
            @Override
            public Object compute(EvalContext context) {
                return "left";
            }
        };

        assertTrue(operation.equal(context, left, right));
    }

    @Test
    public void testContains() {
        CoreOperationCompare operation = new CoreOperationCompare(arg1, arg2);

        Iterator<Object> it = new Iterator<Object>() {
            int count = 0;

            @Override
            public boolean hasNext() {
                return count < 2;
            }

            @Override
            public Object next() {
                count++;
                if (count == 1) {
                    return "value1";
                } else {
                    return "value2";
                }
            }
        };

        assertFalse(operation.contains(it, "value3"));

        assertTrue(operation.contains(it, "value1"));
    }

    @Test
    public void testFindMatch() {
        CoreOperationCompare operation = new CoreOperationCompare(arg1, arg2);

        Iterator<Object> lit = new Iterator<Object>() {
            int count = 0;

            @Override
            public boolean hasNext() {
                return count < 2;
            }

            @Override
            public Object next() {
                count++;
                if (count == 1) {
                    return "value1";
                } else {
                    return "value2";
                }
            }
        };

        Iterator<Object> rit = new Iterator<Object>() {
            int count = 0;

            @Override
            public boolean hasNext() {
                return count < 2;
            }

            @Override
            public Object next() {
                count++;
                if (count == 1) {
                    return "value3";
                } else {
                    return "value2";
                }
            }
        };

        assertFalse(operation.findMatch(lit, rit));

        lit = new Iterator<Object>() {
            int count = 0;

            @Override
            public boolean hasNext() {
                return count < 2;
            }

            @Override
            public Object next() {
                count++;
                if (count == 1) {
                    return "value1";
                } else {
                    return "value2";
                }
            }
        };

        rit = new Iterator<Object>() {
            int count = 0;

            @Override
            public boolean hasNext() {
                return count < 2;
            }

            @Override
            public Object next() {
                count++;
                if (count == 1) {
                    return "value2";
                } else {
                    return "value3";
                }
            }
        };

        assertTrue(operation.findMatch(lit, rit));
    }

    @Test
    public void testEqualWithPointers() {
        CoreOperationCompare operation = new CoreOperationCompare(arg1, arg2);

        EvalContext context = new EvalContext();

        Pointer pointer1 = new Pointer() {
            @Override
            public Object getNode() {
                return "value1";
            }

            @Override
            public Object getValue() {
                return "value1";
            }
        };

        Pointer pointer2 = new Pointer() {
            @Override
            public Object getNode() {
                return "value2";
            }

            @Override
            public Object getValue() {
                return "value2";
            }
        };

        assertFalse(operation.equal(context, pointer1, pointer2));

        pointer1 = new Pointer() {
            @Override
            public Object getNode() {
                return "value1";
            }

            @Override
            public Object getValue() {
                return "value1";
            }
        };

        pointer2 = new Pointer() {
            @Override
            public Object getNode() {
                return "value1";
            }

            @Override
            public Object getValue() {
                return "value1";
            }
        };

        assertTrue(operation.equal(context, pointer1, pointer2));
    }
}