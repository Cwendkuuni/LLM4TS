package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        CoreOperation leftExpression = new CoreOperation() {
            @Override
            public Object compute(EvalContext context) {
                return new Integer(5);
            }
        };

        CoreOperation rightExpression = new CoreOperation() {
            @Override
            public Object compute(EvalContext context) {
                return new Integer(5);
            }
        };

        CoreOperationCompare compare = new CoreOperationCompare(leftExpression, rightExpression);
        EvalContext context = new EvalContext(new InitialContext());

        assertTrue(compare.equal(context, leftExpression, rightExpression));
    }

    @Test
    public void testEqual_NullContext() {
        CoreOperation leftExpression = new CoreOperation() {
            @Override
            public Object compute(EvalContext context) {
                return new Integer(5);
            }
        };

        CoreOperation rightExpression = new CoreOperation() {
            @Override
            public Object compute(EvalContext context) {
                return new Integer(5);
            }
        };

        assertFalse(new CoreOperationCompare(leftExpression, rightExpression).equal(null, leftExpression, rightExpression));
    }

    @Test
    public void testEqual_NullLeftExpression() {
        CoreOperation rightExpression = new CoreOperation() {
            @Override
            public Object compute(EvalContext context) {
                return new Integer(5);
            }
        };

        assertFalse(new CoreOperationCompare(null, rightExpression).equal(new EvalContext(new InitialContext()), null, rightExpression));
    }

    @Test
    public void testEqual_NullRightExpression() {
        CoreOperation leftExpression = new CoreOperation() {
            @Override
            public Object compute(EvalContext context) {
                return new Integer(5);
            }
        };

        assertFalse(new CoreOperationCompare(leftExpression, null).equal(new EvalContext(new InitialContext()), leftExpression, null));
    }

    @Test
    public void testEqual_DifferentDataTypes() {
        CoreOperation leftExpression = new CoreOperation() {
            @Override
            public Object compute(EvalContext context) {
                return new Integer(5);
            }
        };

        CoreOperation rightExpression = new CoreOperation() {
            @Override
            public Object compute(EvalContext context) {
                return new Character('A');
            }
        };

        assertFalse(new CoreOperationCompare(leftExpression, rightExpression).equal(new EvalContext(new InitialContext()), leftExpression, rightExpression));
    }

    @Test
    public void testEqual_Iterators() {
        Iterator<Integer> leftIterator = new Iterator<Integer>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Integer next() {
                return new Integer(5);
            }
        };

        Iterator<Integer> rightIterator = new Iterator<Integer>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Integer next() {
                return new Integer(5);
            }
        };

        CoreOperationCompare compare = new CoreOperationCompare(new CoreOperation() {
            @Override
            public Object compute(EvalContext context) {
                return leftIterator;
            }
        }, new CoreOperation() {
            @Override
            public Object compute(EvalContext context) {
                return rightIterator;
            }
        });

        EvalContext context = new EvalContext(new InitialContext());

        assertTrue(compare.equal(context, new CoreOperation() {
            @Override
            public Object compute(EvalContext context) {
                return leftIterator;
            }
        }, new CoreOperation() {
            @Override
            public Object compute(EvalContext context) {
                return rightIterator;
            }
        }));
    }

    @Test
    public void testEqual_EmptyCollections() {
        CoreOperation leftExpression = new CoreOperation() {
            @Override
            public Object compute(EvalContext context) {
                return new HashSet<>();
            }
        };

        CoreOperation rightExpression = new CoreOperation() {
            @Override
            public Object compute(EvalContext context) {
                return new HashSet<>();
            }
        };

        assertTrue(new CoreOperationCompare(leftExpression, rightExpression).equal(new EvalContext(new InitialContext()), leftExpression, rightExpression));
    }

    @Test
    public void testEqual_DifferentCollectionsOrder() {
        CoreOperation leftExpression = new CoreOperation() {
            @Override
            public Object compute(EvalContext context) {
                return new HashSet<Object>() {{
                    add(new Integer(5));
                    add(new Integer(3));
                }};
            }
        };

        CoreOperation rightExpression = new CoreOperation() {
            @Override
            public Object compute(EvalContext context) {
                return new HashSet<Object>() {{
                    add(new Integer(3));
                    add(new Integer(5));
                }};
            }
        };

        assertFalse(new CoreOperationCompare(leftExpression, rightExpression).equal(new EvalContext(new InitialContext()), leftExpression, rightExpression));
    }

    @Test
    public void testEqual_DifferentCollections() {
        CoreOperation leftExpression = new CoreOperation() {
            @Override
            public Object compute(EvalContext context) {
                return new HashSet<Object>() {{
                    add(new Integer(5));
                    add(new Integer(3));
                }};
            }
        };

        CoreOperation rightExpression = new CoreOperation() {
            @Override
            public Object compute(EvalContext context) {
                return new HashSet<Object>() {{
                    add(new Integer(3));
                    add(new Integer(4));
                }};
            }
        };

        assertFalse(new CoreOperationCompare(leftExpression, rightExpression).equal(new EvalContext(new InitialContext()), leftExpression, rightExpression));
    }

    @Test
    public void testEqual_Arrays() {
        Object[] leftArray = {new Integer(5), new Integer(3)};
        Object[] rightArray = {new Integer(3), new Integer(5)};

        CoreOperation leftExpression = new CoreOperation() {
            @Override
            public Object compute(EvalContext context) {
                return leftArray;
            }
        };

        CoreOperation rightExpression = new CoreOperation() {
            @Override
            public Object compute(EvalContext context) {
                return rightArray;
            }
        };

        assertFalse(new CoreOperationCompare(leftExpression, rightExpression).equal(new EvalContext(new InitialContext()), leftExpression, rightExpression));
    }

    @Test
    public void testEqual_NullValues() {
        CoreOperation leftExpression = new CoreOperation() {
            @Override
            public Object compute(EvalContext context) {
                return null;
            }
        };

        CoreOperation rightExpression = new CoreOperation() {
            @Override
            public Object compute(EvalContext context) {
                return new Integer(5);
            }
        };

        assertFalse(new CoreOperationCompare(leftExpression, rightExpression).equal(new EvalContext(new InitialContext()), leftExpression, rightExpression));
    }

    @Test
    public void testEqual_NonEqualsValues() {
        CoreOperation leftExpression = new CoreOperation() {
            @Override
            public Object compute(EvalContext context) {
                return new Integer(5);
            }
        };

        CoreOperation rightExpression = new CoreOperation() {
            @Override
            public Object compute(EvalContext context) {
                return new Integer(6);
            }
        };

        assertFalse(new CoreOperationCompare(leftExpression, rightExpression).equal(new EvalContext(new InitialContext()), leftExpression, rightExpression));
    }

    @Test
    public void testEqual_Pointers() {
        Pointer leftPointer = new Pointer(new Integer(5));
        Pointer rightPointer = new Pointer(new Integer(5));

        CoreOperation leftExpression = new CoreOperation() {
            @Override
            public Object compute(EvalContext context) {
                return leftPointer;
            }
        };

        CoreOperation rightExpression = new CoreOperation() {
            @Override
            public Object compute(EvalContext context) {
                return rightPointer;
            }
        };

        assertTrue(new CoreOperationCompare(leftExpression, rightExpression).equal(new EvalContext(new InitialContext()), leftExpression, rightExpression));
    }
}