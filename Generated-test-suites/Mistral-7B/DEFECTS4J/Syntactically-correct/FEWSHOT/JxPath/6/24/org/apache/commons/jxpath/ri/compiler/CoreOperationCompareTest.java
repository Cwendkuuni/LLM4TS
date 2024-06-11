package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        CoreOperationCompare op = new CoreOperationCompare(
            new ConstantExpression(new Integer(1)),
            new ConstantExpression(new Integer(1))
        );

        EvalContext ctx = new EvalContext(new InitialContext());
        assertTrue(op.evaluate(ctx));

        op = new CoreOperationCompare(
            new ConstantExpression(new Integer(1)),
            new ConstantExpression(new Integer(2))
        );

        ctx = new EvalContext(new InitialContext());
        assertFalse(op.evaluate(ctx));
    }

    @Test
    public void testEqualWithNulls() {
        CoreOperationCompare op = new CoreOperationCompare(
            new ConstantExpression(null),
            new ConstantExpression(null)
        );

        EvalContext ctx = new EvalContext(new InitialContext());
        assertTrue(op.evaluate(ctx));

        op = new CoreOperationCompare(
            new ConstantExpression(null),
            new ConstantExpression(new String("foo"))
        );

        ctx = new EvalContext(new InitialContext());
        assertFalse(op.evaluate(ctx));
    }

    @Test
    public void testEqualWithStrings() {
        CoreOperationCompare op = new CoreOperationCompare(
            new ConstantExpression("foo"),
            new ConstantExpression("foo")
        );

        EvalContext ctx = new EvalContext(new InitialContext());
        assertTrue(op.evaluate(ctx));
    }

    @Test
    public void testEqualWithDifferentTypes() {
        CoreOperationCompare op = new CoreOperationCompare(
            new ConstantExpression(new Integer(1)),
            new ConstantExpression(new String("foo"))
        );

        EvalContext ctx = new EvalContext(new InitialContext());
        assertFalse(op.evaluate(ctx));
    }

    @Test
    public void testEqualWithCollection() {
        Collection collection = new HashSet();
        collection.add(new Integer(1));
        collection.add(new Integer(2));

        CoreOperationCompare op = new CoreOperationCompare(
            new ConstantExpression(collection),
            new ConstantExpression(new Integer(1))
        );

        EvalContext ctx = new EvalContext(new InitialContext());
        assertTrue(op.evaluate(ctx));

        op = new CoreOperationCompare(
            new ConstantExpression(collection),
            new ConstantExpression(new Integer(2))
        );

        ctx = new EvalContext(new InitialContext());
        assertFalse(op.evaluate(ctx));
    }

    @Test
    public void testEqualWithIterator() {
        Integer[] array = new Integer[] { new Integer(1), new Integer(2) };
        Iterator iterator = InfoSetUtil.getIterator(array);

        CoreOperationCompare op = new CoreOperationCompare(
            new ConstantExpression(iterator),
            new ConstantExpression(new Integer(1))
        );

        EvalContext ctx = new EvalContext(new InitialContext());
        assertTrue(op.evaluate(ctx));

        op = new CoreOperationCompare(
            new ConstantExpression(iterator),
            new ConstantExpression(new Integer(2))
        );

        ctx = new EvalContext(new InitialContext());
        assertFalse(op.evaluate(ctx));
    }

    @Test
    public void testContains() {
        Integer[] array = new Integer[] { new Integer(1), new Integer(2) };

        CoreOperationCompare op = new CoreOperationCompare(
            new ConstantExpression(new Integer(1)),
            new ConstantExpression(new Iterator() {
                @Override
                public boolean hasNext() {
                    return array.length > 0;
                }

                @Override
                public Object next() {
                    return array[0];
                }
            })
        );

        EvalContext ctx = new EvalContext(new InitialContext());
        assertTrue(op.evaluate(ctx));

        op = new CoreOperationCompare(
            new ConstantExpression(new Integer(3)),
            new ConstantExpression(new Iterator() {
                @Override
                public boolean hasNext() {
                    return array.length > 0;
                }

                @Override
                public Object next() {
                    return array[0];
                }
            })
        );

        ctx = new EvalContext(new InitialContext());
        assertFalse(op.evaluate(ctx));
    }

    @Test
    public void testFindMatch() {
        Integer[] leftArray = new Integer[] { new Integer(1), new Integer(2) };
        Integer[] rightArray = new Integer[] { new Integer(1), new Integer(3) };

        CoreOperationCompare op = new CoreOperationCompare(
            new ConstantExpression(new Iterator() {
                @Override
                public boolean hasNext() {
                    return leftArray.length > 0;
                }

                @Override
                public Object next() {
                    return leftArray[0];
                }
            }),
            new ConstantExpression(new Iterator() {
                @Override
                public boolean hasNext() {
                    return rightArray.length > 0;
                }

                @Override
                public Object next() {
                    return rightArray[0];
                }
            })
        );

        EvalContext ctx = new EvalContext(new InitialContext());
        assertTrue(op.evaluate(ctx));

        op = new CoreOperationCompare(
            new ConstantExpression(new Iterator() {
                @Override
                public boolean hasNext() {
                    return leftArray.length > 0;
                }

                @Override
                public Object next() {
                    return leftArray[0];
                }
            }),
            new ConstantExpression(new Iterator() {
                @Override
                public boolean hasNext() {
                    return rightArray.length > 0;
                }

                @Override
                public Object next() {
                    return new Integer(3);
                }
            })
        );

        ctx = new EvalContext(new InitialContext());
        assertFalse(op.evaluate(ctx));
    }

    @Test
    public void testFindMatchWithNulls() {
        Integer[] leftArray = new Integer[] { null, new Integer(1), new Integer(2) };
        Integer[] rightArray = new Integer[] { new Integer(1), new Integer(2), null };

        CoreOperationCompare op = new CoreOperationCompare(
            new ConstantExpression(new Iterator() {
                @Override
                public boolean hasNext() {
                    return leftArray.length > 0;
                }

                @Override
                public Object next() {
                    return leftArray[0];
                }
            }),
            new ConstantExpression(new Iterator() {
                @Override
                public boolean hasNext() {
                    return rightArray.length > 0;
                }

                @Override
                public Object next() {
                    return rightArray[0];
                }
            })
        );

        EvalContext ctx = new EvalContext(new InitialContext());
        assertTrue(op.evaluate(ctx));

        op = new CoreOperationCompare(
            new ConstantExpression(new Iterator() {
                @Override
                public boolean hasNext() {
                    return leftArray.length > 0;
                }

                @Override
                public Object next() {
                    return leftArray[0];
                }
            }),
            new ConstantExpression(new Iterator() {
                @Override
                public boolean hasNext() {
                    return rightArray.length > 0;
                }

                @Override
                public Object next() {
                    return null;
                }
            })
        );

        ctx = new EvalContext(new InitialContext());
        assertFalse(op.evaluate(ctx));
    }

    @Test
    public void testPointerValues() {
        CoreOperationCompare op = new CoreOperationCompare(
            new ConstantExpression(new Pointer() {
                @Override
                public Object getValue() {
                    return new Integer(1);
                }
            }),
            new ConstantExpression(new Pointer() {
                @Override
                public Object getValue() {
                    return new Integer(1);
                }
            })
        );

        EvalContext ctx = new EvalContext(new InitialContext());
        assertTrue(op.evaluate(ctx));
    }

    @Test
    public void testPointerValuesDifferent() {
        CoreOperationCompare op = new CoreOperationCompare(
            new ConstantExpression(new Pointer() {
                @Override
                public Object getValue() {
                    return new Integer(1);
                }
            }),
            new ConstantExpression(new Pointer() {
                @Override
                public Object getValue() {
                    return new Integer(2);
                }
            })
        );

        EvalContext ctx = new EvalContext(new InitialContext());
        assertFalse(op.evaluate(ctx));
    }
}