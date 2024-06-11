package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Iterateur;
import org.apache.commons.jxpath.ri.IterateurFactory;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.apache.commons.jxpath.ri.iter.ArrayIter;
import org.apache.commons.jxpath.ri.iter.SingletonIter;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        Object left = new Integer(5);
        Object right = new Integer(10);
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreExpressionConstant(left), new CoreExpressionConstant(right));
        EvalContext context = new EvalContext(new NodeInfo[]{new NodeInfo(1)}, new IterateurFactory(){
            @Override
            public Iterateur getIterator(EvalContext context, NodeInfo node) {
                return new SingletonIter(new Object[]{left});
            }
        });
        boolean result = (Boolean) expression.computeValue(context);
        assertTrue(result);

        left = new Double(3.5);
        right = new Double(5.0);
        expression = new CoreOperationGreaterThan(new CoreExpressionConstant(left), new CoreExpressionConstant(right));
        context = new EvalContext(new NodeInfo[]{new NodeInfo(1)}, new IterateurFactory(){
            @Override
            public Iterateur getIterator(EvalContext context, NodeInfo node) {
                return new SingletonIter(new Object[]{left});
            }
        });
        result = (Boolean) expression.computeValue(context);
        assertFalse(result);

        left = new ArrayIter(new Object[]{new Integer(1), new Integer(2), new Integer(3), new Integer(4), new Integer(5)});
        right = new Integer(3);
        expression = new CoreOperationGreaterThan(new CoreExpressionVariable(0), new CoreExpressionConstant(right));
        context = new EvalContext(new NodeInfo[]{new NodeInfo(1)}, new IterateurFactory(){
            @Override
            public Iterateur getIterator(EvalContext context, NodeInfo node) {
                return (Iterateur) left;
            }
        });
        result = (Boolean) expression.computeValue(context);
        assertTrue(result);

        left = new ArrayIter(new Object[]{new Integer(1), new Integer(2), new Integer(3), new Integer(4), new Integer(5)});
        right = new Integer(6);
        expression = new CoreOperationGreaterThan(new CoreExpressionVariable(0), new CoreExpressionConstant(right));
        context = new EvalContext(new NodeInfo[]{new NodeInfo(1)}, new IterateurFactory(){
            @Override
            public Iterateur getIterator(EvalContext context, NodeInfo node) {
                return (Iterateur) left;
            }
        });
        result = (Boolean) expression.computeValue(context);
        assertFalse(result);

        left = new SingletonIter(new Object[]{new Integer(5)});
        right = new SingletonIter(new Object[]{new Integer(5)});
        expression = new CoreOperationGreaterThan(new CoreExpressionVariable(0), new CoreExpressionVariable(1));
        context = new EvalContext(new NodeInfo[]{new NodeInfo(1)}, new IterateurFactory(){
            @Override
            public Iterateur getIterator(EvalContext context, NodeInfo node) {
                return (Iterateur) left;
            }
        });
        result = (Boolean) expression.computeValue(context);
        assertTrue(result);

        left = new SingletonIter(new Object[]{new Integer(5)});
        right = new SingletonIter(new Object[]{new Integer(6)});
        expression = new CoreOperationGreaterThan(new CoreExpressionVariable(0), new CoreExpressionVariable(1));
        context = new EvalContext(new NodeInfo[]{new NodeInfo(1)}, new IterateurFactory(){
            @Override
            public Iterateur getIterator(EvalContext context, NodeInfo node) {
                return (Iterateur) left;
            }
        });
        result = (Boolean) expression.computeValue(context);
        assertFalse(result);

        left = new ArrayIter(new Object[]{new Integer(1), new Integer(2), new Integer(3), new Integer(4), new Integer(5)});
        right = new ArrayIter(new Object[]{new Integer(2), new Integer(3), new Integer(4), new Integer(5)});
        expression = new CoreOperationGreaterThan(new CoreExpressionVariable(0), new CoreExpressionVariable(1));
        context = new EvalContext(new NodeInfo[]{new NodeInfo(1)}, new IterateurFactory(){
            @Override
            public Iterateur getIterator(EvalContext context, NodeInfo node) {
                return (Iterateur) left;
            }
        });
        result = (Boolean) expression.computeValue(context);
        assertFalse(result);

        left = new ArrayIter(new Object[]{new Integer(1), new Integer(2), new Integer(3), new Integer(4), new Integer(5)});
        right = new ArrayIter(new Object[]{new Integer(1), new Integer(2), new Integer(3), new Integer(4), new Integer(6)});
        expression = new CoreOperationGreaterThan(new CoreExpressionVariable(0), new CoreExpressionVariable(1));
        context = new EvalContext(new NodeInfo[]{new NodeInfo(1)}, new IterateurFactory(){
            @Override
            public Iterateur getIterator(EvalContext context, NodeInfo node) {
                return (Iterateur) left;
            }
        });
        result = (Boolean) expression.computeValue(context);
        assertFalse(result);
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(null, null);
        assertEquals(3, expression.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(null, null);
        assertFalse(expression.isSymmetric());
    }

}