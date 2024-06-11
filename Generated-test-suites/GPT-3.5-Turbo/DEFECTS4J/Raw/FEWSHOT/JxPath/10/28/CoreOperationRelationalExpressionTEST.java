
package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        Expression[] args1 = { new FieldValue(), new FieldValue() };
        CoreOperationRelationalExpression expression1 = new CoreOperationRelationalExpression(args1) {

            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        CoreOperationRelationalExpression expression2 = new CoreOperationRelationalExpression(args1) {

            @Override
            protected boolean evaluateCompare(int compare) {
                return compare >= 0;
            }
        };
        CoreOperationRelationalExpression expression3 = new CoreOperationRelationalExpression(args1) {

            @Override
            protected boolean evaluateCompare(int compare) {
                return compare < 0;
            }
        };
        CoreOperationRelationalExpression expression4 = new CoreOperationRelationalExpression(args1) {

            @Override
            protected boolean evaluateCompare(int compare) {
                return compare <= 0;
            }
        };

        EvalContext context = new EvalContext(new Object());

        List<Object> list = new ArrayList<>();
        list.add("test");

        assertEquals(Boolean.TRUE, expression1.computeValue(context));
        assertEquals(Boolean.TRUE, expression2.computeValue(context));
        assertEquals(Boolean.FALSE, expression3.computeValue(context));
        assertEquals(Boolean.FALSE, expression4.computeValue(context));

        InitialContext initialContext = new InitialContext(list);
        context.setSingleNodePointer(initialContext);
        assertEquals(Boolean.TRUE, expression1.computeValue(context));
        assertEquals(Boolean.TRUE, expression2.computeValue(context));
        assertEquals(Boolean.TRUE, expression3.computeValue(context));
        assertEquals(Boolean.TRUE, expression4.computeValue(context));

        SelfContext selfContext = new SelfContext(new Object());
        context.setSingleNodePointer(selfContext);
        assertEquals(Boolean.TRUE, expression1.computeValue(context));
        assertEquals(Boolean.TRUE, expression2.computeValue(context));
        assertEquals(Boolean.FALSE, expression3.computeValue(context));
        assertEquals(Boolean.FALSE, expression4.computeValue(context));
    }

    @Test
    public void testGetPrecedence() {
        Expression[] args = { new FieldValue(), new FieldValue() };
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {

            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        assertEquals(3, expression.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        Expression[] args = { new FieldValue(), new FieldValue() };
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {

            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        assertEquals(false, expression.isSymmetric());
    }

    @Test
    public void testCompute() {
        Expression[] args = { new FieldValue(), new FieldValue() };
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {

            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        Object object1 = new Object();
        Object object2 = new Object();

        assertEquals(Boolean.TRUE, expression.compute(object1, object2));
    }

    @Test
    public void testReduce() {
        Expression[] args = { new FieldValue(), new FieldValue() };
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {

            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        SelfContext selfContext = new SelfContext(new Object());
        Iterator<Object> iterator = new ArrayList<>().iterator();
        
        assertEquals(selfContext, expression.reduce(selfContext));
        assertEquals(iterator, expression.reduce(new ArrayList<>()));
    }

    @Test
    public void testContainsMatch() {
        Expression[] args = { new FieldValue(), new FieldValue() };
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {

            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        Iterator<Object> iterator = new ArrayList<>().iterator();
        Object object = new Object();

        assertEquals(false, expression.containsMatch(iterator, object));
    }

    @Test
    public void testFindMatch() {
        Expression[] args = { new FieldValue(), new FieldValue() };
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {

            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        Iterator<Object> lit = new ArrayList<>().iterator();
        Iterator<Object> rit = new ArrayList<>().iterator();

        assertEquals(false, expression.findMatch(lit, rit));
    }
    
    private class FieldValue extends Expression {

        @Override
        public Expression getFilter() {
            return null;
        }

        @Override
        public Iterator iterate(Object contextNode, NodeTest nodeTest) {
            return null;
        }

        @Override
        public ValueIterator valueIterator(Object object, ExecutionContext executionContext) {
            return null;
        }
    }
}
