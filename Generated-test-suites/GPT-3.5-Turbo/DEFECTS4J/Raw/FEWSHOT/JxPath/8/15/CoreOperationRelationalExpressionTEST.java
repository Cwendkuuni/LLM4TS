import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotEquals;

import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.InitialContext;
import org.apache.commons.jxpath.ri.compiler.SelfContext;
import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class CoreOperationRelationalExpressionTest {

    private CoreOperationRelationalExpression coreOpRelExp;

    @Before
    public void setUp() {
        Expression[] args = new Expression[2];
        args[0] = new ExpressionStub();
        args[1] = new ExpressionStub();
        coreOpRelExp = new CoreOperationRelationalExpressionStub(args);
    }

    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext();
        Object result = coreOpRelExp.computeValue(context);
        assertNotNull(result);
        assertTrue(result instanceof Boolean);
    }

    @Test
    public void testGetPrecedence() {
        int precedence = coreOpRelExp.getPrecedence();
        assertNotEquals(0, precedence);
    }

    @Test
    public void testIsSymmetric() {
        boolean symmetric = coreOpRelExp.isSymmetric();
        assertFalse(symmetric);
    }

    @Test
    public void testEvaluateCompare() {
        boolean compare = coreOpRelExp.evaluateCompare(0);
        assertFalse(compare);
        compare = coreOpRelExp.evaluateCompare(-1);
        assertFalse(compare);
        compare = coreOpRelExp.evaluateCompare(1);
        assertFalse(compare);
    }

    @Test
    public void testCompute() {
        boolean compute = coreOpRelExp.compute(new InitialContext(), new InitialContext());
        assertFalse(compute);
        compute = coreOpRelExp.compute(new InitialContext(), new SelfContext());
        assertFalse(compute);
        compute = coreOpRelExp.compute(new SelfContext(), new InitialContext());
        assertFalse(compute);
        compute = coreOpRelExp.compute(new SelfContext(), new SelfContext());
        assertFalse(compute);
        compute = coreOpRelExp.compute(new IteratorStub(), new IteratorStub());
        assertFalse(compute);
        compute = coreOpRelExp.compute(new IteratorStub(), new Object());
        assertFalse(compute);
        compute = coreOpRelExp.compute(new Object(), new IteratorStub());
        assertFalse(compute);
        compute = coreOpRelExp.compute(new Object(), new Object());
        assertFalse(compute);
    }

    @Test
    public void testReduce() {
        Object o = coreOpRelExp.reduce(new SelfContext());
        assertNotNull(o);
        assertFalse(o instanceof SelfContext);
        o = coreOpRelExp.reduce(new HashSet<Object>());
        assertNotNull(o);
        assertTrue(o instanceof Iterator);
    }

    @Test
    public void testContainsMatch() {
        IteratorStub it = new IteratorStub();
        Object value = new Object();

        boolean containsMatch = coreOpRelExp.containsMatch(it, value);
        assertFalse(containsMatch);
    }

    @Test
    public void testFindMatch() {
        IteratorStub lit = new IteratorStub();
        IteratorStub rit = new IteratorStub();

        boolean findMatch = coreOpRelExp.findMatch(lit, rit);
        assertFalse(findMatch);
    }

    private class CoreOperationRelationalExpressionStub extends CoreOperationRelationalExpression {

        public CoreOperationRelationalExpressionStub(Expression[] args) {
            super(args);
        }

        protected boolean evaluateCompare(int compare) {
            return false;
        }
    }

    private class ExpressionStub extends Expression {

        public Object computeValue(EvalContext context) {
            return null;
        }
    }

    private class IteratorStub implements Iterator<Object> {

        public boolean hasNext() {
            return false;
        }

        public Object next() {
            return null;
        }

        public void remove() {

        }
    }
}