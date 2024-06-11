package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.IteratingNodes;
import org.apache.commons.jxpath.ri.Iterators;
import org.apache.commons.jxpath.ri.Nodes;
import org.apache.commons.jxpath.ri.axes.Axis;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.apache.commons.jxpath.ri.iter.FilterIterator;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        Expression left = new ConstantExpression(new SelfContext(new InfoSet() {
            @Override
            public Axis getAxis() {
                return null;
            }

            @Override
            public Iterator iterator() {
                return Iterators.emptyIterator();
            }

            @Override
            public int getSize() {
                return 0;
            }

            @Override
            public Node getSingleNodePointer() {
                return Nodes.newNode();
            }
        }));

        Expression right = new ConstantExpression(new SelfContext(new InfoSet() {
            @Override
            public Axis getAxis() {
                return null;
            }

            @Override
            public Iterator iterator() {
                return Iterators.emptyIterator();
            }

            @Override
            public int getSize() {
                return 0;
            }

            @Override
            public Node getSingleNodePointer() {
                return Nodes.newNode();
            }
        }));

        CoreOperationRelationalExpression expr = new CoreOperationGreaterThan(new CoreOperation[] { left, right });
        EvalContext context = new EvalContext(new InfoSet() {
            @Override
            public int getSize() {
                return 1;
            }

            @Override
            public Iterator iterator() {
                return Iterators.singletonIterator(Nodes.newNode());
            }
        });

        Object result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        context = new EvalContext(new InfoSet() {
            @Override
            public int getSize() {
                return 1;
            }

            @Override
            public Iterator iterator() {
                return Iterators.singletonIterator(Nodes.newNode());
            }
        });

        left = new ConstantExpression(new SelfContext(new InfoSet() {
            @Override
            public Axis getAxis() {
                return null;
            }

            @Override
            public Iterator iterator() {
                return Iterators.singletonIterator(Nodes.newNode());
            }

            @Override
            public int getSize() {
                return 1;
            }

            @Override
            public Node getSingleNodePointer() {
                return Nodes.newNode();
            }
        }));

        right = new ConstantExpression(new SelfContext(new InfoSet() {
            @Override
            public Axis getAxis() {
                return null;
            }

            @Override
            public Iterator iterator() {
                return Iterators.singletonIterator(Nodes.newNode());
            }

            @Override
            public int getSize() {
                return 1;
            }

            @Override
            public Node getSingleNodePointer() {
                return Nodes.newNode();
            }
        }));

        expr = new CoreOperationGreaterThanOrEqual(new CoreOperation[] { left, right });
        result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        context = new EvalContext(new InfoSet() {
            @Override
            public int getSize() {
                return 1;
            }

            @Override
            public Iterator iterator() {
                return Iterators.singletonIterator(Nodes.newNode());
            }
        });

        left = new ConstantExpression(new SelfContext(new InfoSet() {
            @Override
            public Axis getAxis() {
                return null;
            }

            @Override
            public Iterator iterator() {
                return Iterators.singletonIterator(Nodes.newNode());
            }

            @Override
            public int getSize() {
                return 1;
            }

            @Override
            public Node getSingleNodePointer() {
                return Nodes.newNode();
            }
        }));

        right = new ConstantExpression(new SelfContext(new InfoSet() {
            @Override
            public Axis getAxis() {
                return null;
            }

            @Override
            public Iterator iterator() {
                return Iterators.singletonIterator(Nodes.newNode());
            }

            @Override
            public int getSize() {
                return 1;
            }

            @Override
            public Node getSingleNodePointer() {
                return Nodes.newNode();
            }
        }));

        expr = new CoreOperationLessThan(new CoreOperation[] { left, right });
        result = expr.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertNull(result);

        context = new EvalContext(new InfoSet() {
            @Override
            public int getSize() {
                return 1;
            }

            @Override
            public Iterator iterator() {
                return Iterators.singletonIterator(Nodes.newNode());
            }
        });

        left = new ConstantExpression(new SelfContext(new InfoSet() {
            @Override
            public Axis getAxis() {
                return null;
            }

            @Override
            public Iterator iterator() {
                return Iterators.singletonIterator(Nodes.newNode());
            }

            @Override
            public int getSize() {
                return 1;
            }

            @Override
            public Node getSingleNodePointer() {
                return Nodes.newNode();
            }
        }));

        right = new ConstantExpression(new SelfContext(new InfoSet() {
            @Override
            public Axis getAxis() {
                return null;
            }

            @Override
            public Iterator iterator() {
                return Iterators.singletonIterator(Nodes.newNode());
            }

            @Override
            public int getSize() {
                return 1;
            }

            @Override
            public Node getSingleNodePointer() {
                return Nodes.newNode();
            }
        }));

        expr = new CoreOperationLessThanOrEqual(new CoreOperation[] { left, right });
        result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        context = new EvalContext(new InfoSet() {
            @Override
            public int getSize() {
                return 1;
            }

            @Override
            public Iterator iterator() {
                return Iterators.singletonIterator(Nodes.newNode());
            }
        });

        left = new ConstantExpression(new SelfContext(new InfoSet() {
            @Override
            public Axis getAxis() {
                return null;
            }

            @Override
            public Iterator iterator() {
                return Iterators.singletonIterator(Nodes.newNode());
            }

            @Override
            public int getSize() {
                return 1;
            }

            @Override
            public Node getSingleNodePointer() {
                return Nodes.newNode();
            }
        }));

        right = new ConstantExpression(new SelfContext(new InfoSet() {
            @Override
            public Axis getAxis() {
                return null;
            }

            @Override
            public Iterator iterator() {
                return Iterators.singletonIterator(Nodes.newNode());
            }

            @Override
            public int getSize() {
                return 1;
            }

            @Override
            public Node getSingleNodePointer() {
                return Nodes.newNode();
            }
        }));

        expr = new CoreOperationEqual(new CoreOperation[] { left, right });
        result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        context = new EvalContext(new InfoSet() {
            @Override
            public int getSize() {
                return 1;
            }

            @Override
            public Iterator iterator() {
                return Iterators.singletonIterator(Nodes.newNode());
            }
        });

        left = new ConstantExpression(new SelfContext(new InfoSet() {
            @Override
            public Axis getAxis() {
                return null;
            }

            @Override
            public Iterator iterator() {
                return Iterators.singletonIterator(Nodes.newNode());
            }

            @Override
            public int getSize() {
                return 1;
            }

            @Override
            public Node getSingleNodePointer() {
                return Nodes.newNode();
            }
        }));

        right = new ConstantExpression(new SelfContext(new InfoSet() {
            @Override
            public Axis getAxis() {
                return null;
            }

            @Override
            public Iterator iterator() {
                return Iterators.singletonIterator(Nodes.newNode());
            }

            @Override
            public int getSize() {
                return 1;
            }

            @Override
            public Node getSingleNodePointer() {
                return Nodes.newNode();
            }
        }));

        expr = new CoreOperationNotEqual(new CoreOperation[] { left, right });
        result = expr.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertNull(result);

        context = new EvalContext(new InfoSet() {
            @Override
            public int getSize() {
                return 1;
            }

            @Override
            public Iterator iterator() {
                return Iterators.singletonIterator(Nodes.newNode());
            }
        });

        List<Node> nodes = Collections.singletonList(Nodes.newNode());
        Expression leftCollection = new AxesExpression("./node()", new AxesExpressionContext(context, Axis.CHILD));
        Expression rightCollection = new AxesExpression("./node()", new AxesExpressionContext(context, Axis.CHILD));

        CoreOperationRelationalExpression exprCollection = new CoreOperationGreaterThan(new CoreOperation[] { leftCollection, rightCollection });

        result = exprCollection.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertNull(result);

        context = new EvalContext(new InfoSet() {
            @Override
            public int getSize() {
                return nodes.size();
            }

            @Override
            public Iterator iterator() {
                return nodes.iterator();
            }
        });

        IteratingNodes iteratingNodes = new IteratingNodes(nodes, context);
        FilterIterator filterIterator = new FilterIterator(iteratingNodes, expr);

        result = filterIterator.next();
        assertFalse(result instanceof Boolean);
        assertNull(result);
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expr = new CoreOperationGreaterThan(new CoreOperation[] { new ConstantExpression(new SelfContext(new InfoSet())), new ConstantExpression(new SelfContext(new InfoSet())) });
        assertFalse(expr.isSymmetric());

        expr = new CoreOperationGreaterThanOrEqual(new CoreOperation[] { new ConstantExpression(new SelfContext(new InfoSet())), new ConstantExpression(new SelfContext(new InfoSet())) });
        assertFalse(expr.isSymmetric());

        expr = new CoreOperationLessThan(new CoreOperation[] { new ConstantExpression(new SelfContext(new InfoSet())), new ConstantExpression(new SelfContext(new InfoSet())) });
        assertFalse(expr.isSymmetric());

        expr = new CoreOperationLessThanOrEqual(new CoreOperation[] { new ConstantExpression(new SelfContext(new InfoSet())), new ConstantExpression(new SelfContext(new InfoSet())) });
        assertFalse(expr.isSymmetric());

        expr = new CoreOperationEqual(new CoreOperation[] { new ConstantExpression(new SelfContext(new InfoSet())), new ConstantExpression(new SelfContext(new InfoSet())) });
        assertTrue(expr.isSymmetric());

        expr = new CoreOperationNotEqual(new CoreOperation[] { new ConstantExpression(new SelfContext(new InfoSet())), new ConstantExpression(new SelfContext(new InfoSet())) });
        assertTrue(expr.isSymmetric());
    }

}