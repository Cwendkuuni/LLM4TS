import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression.CoreOperation;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression.CoreOperationArgument;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.ExpressionFactory;
import org.apache.commons.jxpath.ri.compiler.StaticContext;
import org.apache.commons.jxpath.ri.eval.EvalContext;
import org.apache.commons.jxpath.ri.iter.InitialContext;
import org.apache.commons.jxpath.ri.iter.SelfContext;
import org.apache.commons.jxpath.ri.iter.SingletonIter;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testGreaterThan() {
        ExpressionFactory ef = new ExpressionFactory();
        CoreOperation op = ef.createOperation("&gt;");
        CoreOperation arg1 = ef.createString("5");
        CoreOperation arg2 = ef.createNumber("4.5");
        CoreOperation expression = ef.createExpression("5 &gt; 4.5");

        CoreOperationRelationalExpression relationalExpression = (CoreOperationRelationalExpression) expression.getLeftExpression().getOperation();

        StaticContext ctx = new StaticContext();
        EvalContext evalContext = new EvalContext(new SingletonIter(new Object[]{SelfContext.SELF, new Integer(5)}), ctx);

        Object result = relationalExpression.computeValue(evalContext);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        evalContext = new EvalContext(new SingletonIter(new Object[]{SelfContext.SELF, new Integer(4)}), ctx);
        result = relationalExpression.computeValue(evalContext);
        assertFalse(result instanceof Boolean);

        evalContext = new EvalContext(new SingletonIter(new Object[]{SelfContext.SELF, new Double(5.0)}), ctx);
        result = relationalExpression.computeValue(evalContext);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        evalContext = new EvalContext(new SingletonIter(new Object[]{SelfContext.SELF, new Double(4.5)}), ctx);
        result = relationalExpression.computeValue(evalContext);
        assertFalse(result instanceof Boolean);

        // Test with Iterators
        Collection<Object> coll1 = new ArrayList<>();
        coll1.add(new Integer(5));
        Collection<Object> coll2 = new ArrayList<>();
        coll2.add(new Integer(4));
        coll2.add(new Double(4.5));

        evalContext = new EvalContext(new Iterator[] {new SingletonIter(new Object[]{SelfContext.SELF, coll1.iterator()}), new SingletonIter(new Object[]{SelfContext.SELF, coll2.iterator()})}, ctx);
        result = relationalExpression.computeValue(evalContext);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        coll1 = new ArrayList<>();
        coll1.add(new Integer(5));
        coll2 = new ArrayList<>();
        coll2.add(new Double(4.5));

        evalContext = new EvalContext(new Iterator[] {new SingletonIter(new Object[]{SelfContext.SELF, coll1.iterator()}), new SingletonIter(new Object[]{SelfContext.SELF, coll2.iterator()})}, ctx);
        result = relationalExpression.computeValue(evalContext);
        assertFalse(result instanceof Boolean);
    }

    @Test
    public void testGreaterThanOrEqual() {
        ExpressionFactory ef = new ExpressionFactory();
        CoreOperation op = ef.createOperation("&gt;=");
        CoreOperation arg1 = ef.createNumber("5.5");
        CoreOperation arg2 = ef.createNumber("4.5");
        CoreOperation expression = ef.createExpression("5.5 &gt;= 4.5");

        CoreOperationRelationalExpression relationalExpression = (CoreOperationRelationalExpression) expression.getLeftExpression().getOperation();

        StaticContext ctx = new StaticContext();
        EvalContext evalContext = new EvalContext(new SingletonIter(new Object[]{SelfContext.SELF, new Double(5.5)}), ctx);

        Object result = relationalExpression.computeValue(evalContext);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        evalContext = new EvalContext(new SingletonIter(new Object[]{SelfContext.SELF, new Double(4.5)}), ctx);
        result = relationalExpression.computeValue(evalContext);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        evalContext = new EvalContext(new SingletonIter(new Object[]{SelfContext.SELF, new Double(4.5)}), ctx);
        result = relationalExpression.computeValue(evalContext);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        evalContext = new EvalContext(new SingletonIter(new Object[]{SelfContext.SELF, new Double(4.0)}), ctx);
        result = relationalExpression.computeValue(evalContext);
        assertFalse(result instanceof Boolean);

        // Test with Iterators
        Collection<Object> coll1 = new ArrayList<>();
        coll1.add(new Double(5.5));
        Collection<Object> coll2 = new ArrayList<>();
        coll2.add(new Double(4.5));
        coll2.add(new Double(4.0));

        evalContext = new EvalContext(new Iterator[] {new SingletonIter(new Object[]{SelfContext.SELF, coll1.iterator()}), new SingletonIter(new Object[]{SelfContext.SELF, coll2.iterator()})}, ctx);
        result = relationalExpression.computeValue(evalContext);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        coll1 = new ArrayList<>();
        coll1.add(new Double(5.5));
        coll2 = new ArrayList<>();
        coll2.add(new Double(4.0));

        evalContext = new EvalContext(new Iterator[] {new SingletonIter(new Object[]{SelfContext.SELF, coll1.iterator()}), new SingletonIter(new Object[]{SelfContext.SELF, coll2.iterator()})}, ctx);
        result = relationalExpression.computeValue(evalContext);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testLessThan() {
        ExpressionFactory ef = new ExpressionFactory();
        CoreOperation op = ef.createOperation("<");
        CoreOperation arg1 = ef.createNumber("4.5");
        CoreOperation arg2 = ef.createNumber("5.5");
        CoreOperation expression = ef.createExpression("4.5 < 5.5");

        CoreOperationRelationalExpression relationalExpression = (CoreOperationRelationalExpression) expression.getLeftExpression().getOperation();

        StaticContext ctx = new StaticContext();
        EvalContext evalContext = new EvalContext(new SingletonIter(new Object[]{SelfContext.SELF, new Double(4.5)}), ctx);

        Object result = relationalExpression.computeValue(evalContext);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        evalContext = new EvalContext(new SingletonIter(new Object[]{SelfContext.SELF, new Double(5.5)}), ctx);
        result = relationalExpression.computeValue(evalContext);
        assertFalse(result instanceof Boolean);

        evalContext = new EvalContext(new SingletonIter(new Object[]{SelfContext.SELF, new Double(4.5)}), ctx);
        result = relationalExpression.computeValue(evalContext);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        evalContext = new EvalContext(new SingletonIter(new Object[]{SelfContext.SELF, new Double(5.5)}), ctx);
        result = relationalExpression.computeValue(evalContext);
        assertFalse(result instanceof Boolean);

        // Test with Iterators
        Collection<Object> coll1 = new ArrayList<>();
        coll1.add(new Double(4.5));
        Collection<Object> coll2 = new ArrayList<>();
        coll2.add(new Double(5.5));

        evalContext = new EvalContext(new Iterator[] {new SingletonIter(new Object[]{SelfContext.SELF, coll1.iterator()}), new SingletonIter(new Object[]{SelfContext.SELF, coll2.iterator()})}, ctx);
        result = relationalExpression.computeValue(evalContext);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        coll1 = new ArrayList<>();
        coll1.add(new Double(5.5));
        coll2 = new ArrayList<>();
        coll2.add(new Double(4.5));

        evalContext = new EvalContext(new Iterator[] {new SingletonIter(new Object[]{SelfContext.SELF, coll1.iterator()}), new SingletonIter(new Object[]{SelfContext.SELF, coll2.iterator()})}, ctx);
        result = relationalExpression.computeValue(evalContext);
        assertFalse(result instanceof Boolean);
    }

    @Test
    public void testLessThanOrEqual() {
        ExpressionFactory ef = new ExpressionFactory();
        CoreOperation op = ef.createOperation("<=");
        CoreOperation arg1 = ef.createNumber("5.5");
        CoreOperation arg2 = ef.createNumber("4.5");
        CoreOperation expression = ef.createExpression("5.5 <= 4.5");

        CoreOperationRelationalExpression relationalExpression = (CoreOperationRelationalExpression) expression.getLeftExpression().getOperation();

        StaticContext ctx = new StaticContext();
        EvalContext evalContext = new EvalContext(new SingletonIter(new Object[]{SelfContext.SELF, new Double(5.5)}), ctx);

        Object result = relationalExpression.computeValue(evalContext);
        assertFalse(result instanceof Boolean);

        evalContext = new EvalContext(new SingletonIter(new Object[]{SelfContext.SELF, new Double(4.5)}), ctx);
        result = relationalExpression.computeValue(evalContext);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        evalContext = new EvalContext(new SingletonIter(new Object[]{SelfContext.SELF, new Double(4.5)}), ctx);
        result = relationalExpression.computeValue(evalContext);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        evalContext = new EvalContext(new SingletonIter(new Object[]{SelfContext.SELF, new Double(5.5)}), ctx);
        result = relationalExpression.computeValue(evalContext);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        // Test with Iterators
        Collection<Object> coll1 = new ArrayList<>();
        coll1.add(new Double(5.5));
        Collection<Object> coll2 = new ArrayList<>();
        coll2.add(new Double(4.5));

        evalContext = new EvalContext(new Iterator[] {new SingletonIter(new Object[]{SelfContext.SELF, coll1.iterator()}), new SingletonIter(new Object[]{SelfContext.SELF, coll2.iterator()})}, ctx);
        result = relationalExpression.computeValue(evalContext);
        assertFalse(result instanceof Boolean);

        coll1 = new ArrayList<>();
        coll1.add(new Double(4.5));
        coll2 = new ArrayList<>();
        coll2.add(new Double(5.5));

        evalContext = new EvalContext(new Iterator[] {new SingletonIter(new Object[]{SelfContext.SELF, coll1.iterator()}), new SingletonIter(new Object[]{SelfContext.SELF, coll2.iterator()})}, ctx);
        result = relationalExpression.computeValue(evalContext);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }
}