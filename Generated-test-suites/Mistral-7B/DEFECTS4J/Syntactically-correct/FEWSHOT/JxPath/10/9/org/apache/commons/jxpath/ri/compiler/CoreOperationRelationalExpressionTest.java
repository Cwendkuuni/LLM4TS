package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.iter.FilterIterator;
import org.apache.commons.jxpath.ri.iter.SingletonIter;
import org.apache.commons.jxpath.ri.iter.SingleNodeIterator;
import org.apache.commons.jxpath.ri.iter.StandardIterators;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        CoreOperationRelationalExpression expression = new GreaterThanExpression(new VariableExpression("x"), new NumberLiteralExpression(5.0));

        EvalContext context = new EvalContext(new InformationSet() {
            @Override
            public Iterator getIterator() {
                return StandardIterators.EMPTY_ITERATOR;
            }

            @Override
            public Object getSingleNodePointer() {
                return new Object() {};
            }

            @Override
            public int getSize() {
                return 0;
            }
        });

        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        context = new EvalContext(new InformationSet() {
            @Override
            public Iterator getIterator() {
                List<Object> list = new ArrayList<Object>();
                list.add(new Double(5.0));
                list.add(new Double(4.0));
                return list.iterator();
            }

            @Override
            public Object getSingleNodePointer() {
                return new Object() {};
            }

            @Override
            public int getSize() {
                return 2;
            }
        });

        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        context = new EvalContext(new InformationSet() {
            @Override
            public Iterator getIterator() {
                List<Object> list = new ArrayList<Object>();
                list.add(new Double(5.0));
                return list.iterator();
            }

            @Override
            public Object getSingleNodePointer() {
                return new Object() {};
            }

            @Override
            public int getSize() {
                return 1;
            }
        });

        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new GreaterThanExpression(new VariableExpression("x"), new NumberLiteralExpression(5.0));

        int precedence = expression.getPrecedence();
        assertEquals(3, precedence);
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new GreaterThanExpression(new VariableExpression("x"), new NumberLiteralExpression(5.0));

        boolean symmetric = expression.isSymmetric();
        assertFalse(symmetric);
    }

    @Test
    public void testEvaluateCompare() {
        CoreOperationRelationalExpression expression = new GreaterThanExpression(new VariableExpression("x"), new NumberLiteralExpression(5.0));

        boolean result = expression.evaluateCompare(0);
        assertFalse(result);

        result = expression.evaluateCompare(-1);
        assertTrue(result);

        result = expression.evaluateCompare(1);
        assertFalse(result);
    }

    @Test
    public void testComputeWithIterators() {
        CoreOperationRelationalExpression expression = new GreaterThanExpression(new VariableExpression("x"), new VariableExpression("y"));

        EvalContext context = new EvalContext(new InformationSet() {
            @Override
            public Iterator getIterator() {
                List<Object> list1 = new ArrayList<Object>();
                list1.add(new Double(1.0));
                list1.add(new Double(2.0));
                list1.add(new Double(3.0));
                list1.add(new Double(4.0));
                list1.add(new Double(5.0));

                List<Object> list2 = new ArrayList<Object>();
                list2.add(new Double(1.0));
                list2.add(new Double(2.0));
                list2.add(new Double(3.0));
                list2.add(new Double(5.0));
                list2.add(new Double(6.0));

                return new FilterIterator<Object>(list1.iterator(), new Predicate<Object>() {
                    @Override
                    public boolean evaluate(Object object) {
                        double d1 = InfoSetUtil.doubleValue(object);
                        double d2 = InfoSetUtil.doubleValue(expression.args[1].computeValue(context));

                        return d1 > d2;
                    }
                });
            }

            @Override
            public Object getSingleNodePointer() {
                return new Object() {};
            }

            @Override
            public int getSize() {
                return 5;
            }
        });

        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        context = new EvalContext(new InformationSet() {
            @Override
            public Iterator getIterator() {
                List<Object> list1 = new ArrayList<Object>();
                list1.add(new Double(1.0));
                list1.add(new Double(2.0));
                list1.add(new Double(3.0));
                list1.add(new Double(4.0));
                list1.add(new Double(5.0));

                List<Object> list2 = new ArrayList<Object>();
                list2.add(new Double(1.0));
                list2.add(new Double(2.0));
                list2.add(new Double(3.0));
                list2.add(new Double(4.0));
                list2.add(new Double(5.0));
                list2.add(new Double(6.0));

                return new FilterIterator<Object>(list1.iterator(), new Predicate<Object>() {
                    @Override
                    public boolean evaluate(Object object) {
                        double d1 = InfoSetUtil.doubleValue(object);
                        double d2 = InfoSetUtil.doubleValue(expression.args[1].computeValue(context));

                        return d1 > d2;
                    }
                });
            }

            @Override
            public Object getSingleNodePointer() {
                return new Object() {};
            }

            @Override
            public int getSize() {
                return 5;
            }
        });

        context.setVariableValue("x", new SingletonIter<Object>(new Double(5.0)));
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        context = new EvalContext(new InformationSet() {
            @Override
            public Iterator getIterator() {
                List<Object> list1 = new ArrayList<Object>();
                list1.add(new Double(1.0));
                list1.add(new Double(2.0));
                list1.add(new Double(3.0));
                list1.add(new Double(4.0));
                list1.add(new Double(5.0));

                List<Object> list2 = new ArrayList<Object>();
                list2.add(new Double(1.0));
                list2.add(new Double(2.0));
                list2.add(new Double(3.0));
                list2.add(new Double(4.0));
                list2.add(new Double(5.0));
                list2.add(new Double(6.0));

                return new FilterIterator<Object>(list1.iterator(), new Predicate<Object>() {
                    @Override
                    public boolean evaluate(Object object) {
                        double d1 = InfoSetUtil.doubleValue(object);
                        double d2 = InfoSetUtil.doubleValue(expression.args[1].computeValue(context));

                        return d1 > d2;
                    }
                });
            }

            @Override
            public Object getSingleNodePointer() {
                return new Object() {};
            }

            @Override
            public int getSize() {
                return 5;
            }
        });

        context.setVariableValue("x", new SingleNodeIterator<Object>(new Double(5.0)));
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeWithCollections() {
        CoreOperationRelationalExpression expression = new GreaterThanExpression(new VariableExpression("x"), new VariableExpression("y"));

        EvalContext context = new EvalContext(new InformationSet() {
            @Override
            public Iterator getIterator() {
                Collection<Object> collection1 = new ArrayList<Object>();
                collection1.add(new Double(1.0));
                collection1.add(new Double(2.0));
                collection1.add(new Double(3.0));
                collection1.add(new Double(4.0));
                collection1.add(new Double(5.0));

                Collection<Object> collection2 = new ArrayList<Object>();
                collection2.add(new Double(1.0));
                collection2.add(new Double(2.0));
                collection2.add(new Double(3.0));
                collection2.add(new Double(4.0));
                collection2.add(new Double(5.0));
                collection2.add(new Double(6.0));

                return new FilterIterator<Object>(collection1.iterator(), new Predicate<Object>() {
                    @Override
                    public boolean evaluate(Object object) {
                        double d1 = InfoSetUtil.doubleValue(object);
                        double d2 = InfoSetUtil.doubleValue(expression.args[1].computeValue(context));

                        return d1 > d2;
                    }
                });
            }

            @Override
            public Object getSingleNodePointer() {
                return new Object() {};
            }

            @Override
            public int getSize() {
                return 5;
            }
        });

        context.setVariableValue("x", collection1);
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        context.setVariableValue("x", collection2);
        context.setVariableValue("y", new SingletonIter<Object>(new Double(5.0)));
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }
}