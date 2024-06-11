package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.test.TestSupport;

import java.util.ArrayList;

public class CoreOperationGreaterThanTest {

    @Test
    public void testCoreOperationGreaterThan() {
        CoreOperationGreaterThan cogt = new CoreOperationGreaterThan(
                new CoreConstantExpression(new Double(1.5)),
                new CoreConstantExpression(new Double(1.2)));

        EvalContext context = TestSupport.createSimpleContext();
        Object result = cogt.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        context = TestSupport.createSimpleContext();
        cogt = new CoreOperationGreaterThan(
                new CoreConstantExpression(new Double(1.5)),
                new CoreConstantExpression(new Double(2.5)));
        result = cogt.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        context = TestSupport.createSimpleContext();
        ArrayList<Object> list = new ArrayList<>();
        list.add(new Double(1.5));
        list.add(new Double(1.2));
        cogt = new CoreOperationGreaterThan(
                new CoreListExpression(list),
                new CoreConstantExpression(new Double(1.2)));
        result = cogt.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        context = TestSupport.createSimpleContext();
        list = new ArrayList<>();
        list.add(new Double(1.5));
        list.add(new Double(1.5));
        cogt = new CoreOperationGreaterThan(
                new CoreListExpression(list),
                new CoreConstantExpression(new Double(1.5)));
        result = cogt.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        context = TestSupport.createSimpleContext();
        list = new ArrayList<>();
        list.add(new Double(1.5));
        list.add(new Double(null));
        cogt = new CoreOperationGreaterThan(
                new CoreListExpression(list),
                new CoreConstantExpression(new Double(1.2)));
        result = cogt.computeValue(context);
        assertNull(result);
    }

    @Test
    public void testCoreOperationGreaterThanWithNullArg() {
        CoreOperationGreaterThan cogt = new CoreOperationGreaterThan(
                new CoreConstantExpression(new Double(1.5)),
                new CoreConstantExpression(null));

        EvalContext context = TestSupport.createSimpleContext();
        Object result = cogt.computeValue(context);
        assertNull(result);
    }

    @Test
    public void testCoreOperationGreaterThanWithNullArg2() {
        CoreOperationGreaterThan cogt = new CoreOperationGreaterThan(
                new CoreConstantExpression(null),
                new CoreConstantExpression(new Double(1.5)));

        EvalContext context = TestSupport.createSimpleContext();
        Object result = cogt.computeValue(context);
        assertNull(result);
    }

    @Test
    public void testCoreOperationGreaterThanWithNullBoth() {
        CoreOperationGreaterThan cogt = new CoreOperationGreaterThan(
                new CoreConstantExpression(null),
                new CoreConstantExpression(null));

        EvalContext context = TestSupport.createSimpleContext();
        Object result = cogt.computeValue(context);
        assertNull(result);
    }

    @Test
    public void testCoreOperationGreaterThanWithStringArg1AndDoubleArg2() {
        CoreOperationGreaterThan cogt = new CoreOperationGreaterThan(
                new CoreStringExpression("1.5"),
                new CoreConstantExpression(new Double(1.2)));

        EvalContext context = TestSupport.createSimpleContext();
        Object result = cogt.computeValue(context);
        assertFalse(result instanceof Boolean);
    }

    @Test
    public void testCoreOperationGreaterThanWithDoubleArg1AndStringArg2() {
        CoreOperationGreaterThan cogt = new CoreOperationGreaterThan(
                new CoreConstantExpression(new Double(1.5)),
                new CoreStringExpression("1.2"));

        EvalContext context = TestSupport.createSimpleContext();
        Object result = cogt.computeValue(context);
        assertFalse(result instanceof Boolean);
    }
}