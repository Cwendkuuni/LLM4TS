package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.expression.Expression;
import org.apache.commons.jxpath.ri.expression.Variable;

public class CoreOperationNotEqualTest {

    @Test
    public void testCoreOperationNotEqual_Boolean_Boolean() {
        Expression arg1 = new Variable("arg1");
        Expression arg2 = new Variable("arg2");
        CoreOperationNotEqual notEqual = new CoreOperationNotEqual(arg1, arg2);

        Object result = notEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);

        // true != true
        Variable arg1BooleanTrue = new Variable("arg1BooleanTrue", Boolean.TRUE);
        Variable arg2BooleanTrue = new Variable("arg2BooleanTrue", Boolean.TRUE);
        notEqual = new CoreOperationNotEqual(arg1BooleanTrue, arg2BooleanTrue);
        result = notEqual.computeValue(new EvalContext());
        assertTrue((Boolean) result);

        // true != false
        Variable arg1BooleanTrue2 = new Variable("arg1BooleanTrue2", Boolean.TRUE);
        Variable arg2BooleanFalse = new Variable("arg2BooleanFalse", Boolean.FALSE);
        notEqual = new CoreOperationNotEqual(arg1BooleanTrue2, arg2BooleanFalse);
        result = notEqual.computeValue(new EvalContext());
        assertTrue((Boolean) result);

        // false != true
        Variable arg1BooleanFalse = new Variable("arg1BooleanFalse", Boolean.FALSE);
        notEqual = new CoreOperationNotEqual(arg1BooleanFalse, arg2BooleanTrue);
        result = notEqual.computeValue(new EvalContext());
        assertTrue((Boolean) result);

        // false != false
        Variable arg1BooleanFalse2 = new Variable("arg1BooleanFalse2", Boolean.FALSE);
        notEqual = new CoreOperationNotEqual(arg1BooleanFalse2, arg2BooleanFalse);
        result = notEqual.computeValue(new EvalContext());
        assertFalse((Boolean) result);
    }

    @Test
    public void testCoreOperationNotEqual_Integer_Integer() {
        Expression arg1 = new Variable("arg1");
        Expression arg2 = new Variable("arg2");
        CoreOperationNotEqual notEqual = new CoreOperationNotEqual(arg1, arg2);

        Object result = notEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);

        // 1 != 1
        Variable arg1Integer1 = new Variable("arg1Integer1", Integer.valueOf(1));
        Variable arg2Integer1 = new Variable("arg2Integer1", Integer.valueOf(1));
        notEqual = new CoreOperationNotEqual(arg1Integer1, arg2Integer1);
        result = notEqual.computeValue(new EvalContext());
        assertFalse((Boolean) result);

        // 1 != 2
        Variable arg1Integer1_2 = new Variable("arg1Integer1_2", Integer.valueOf(1));
        Variable arg2Integer2 = new Variable("arg2Integer2", Integer.valueOf(2));
        notEqual = new CoreOperationNotEqual(arg1Integer1_2, arg2Integer2);
        result = notEqual.computeValue(new EvalContext());
        assertTrue((Boolean) result);
    }

    @Test
    public void testCoreOperationNotEqual_String_String() {
        Expression arg1 = new Variable("arg1");
        Expression arg2 = new Variable("arg2");
        CoreOperationNotEqual notEqual = new CoreOperationNotEqual(arg1, arg2);

        Object result = notEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);

        // "foo" != "foo"
        Variable arg1StringFoo = new Variable("arg1StringFoo", "foo");
        Variable arg2StringFoo = new Variable("arg2StringFoo", "foo");
        notEqual = new CoreOperationNotEqual(arg1StringFoo, arg2StringFoo);
        result = notEqual.computeValue(new EvalContext());
        assertFalse((Boolean) result);

        // "foo" != "bar"
        Variable arg1StringFoo_2 = new Variable("arg1StringFoo_2", "foo");
        Variable arg2StringBar = new Variable("arg2StringBar", "bar");
        notEqual = new CoreOperationNotEqual(arg1StringFoo_2, arg2StringBar);
        result = notEqual.computeValue(new EvalContext());
        assertTrue((Boolean) result);
    }

    @Test
    public void testCoreOperationNotEqual_Number_Number() {
        Expression arg1 = new Variable("arg1");
        Expression arg2 = new Variable("arg2");
        CoreOperationNotEqual notEqual = new CoreOperationNotEqual(arg1, arg2);

        Object result = notEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);

        // 1.0 != 1.0
        Variable arg1Number1_0 = new Variable("arg1Number1_0", Double.valueOf(1.0));
        Variable arg2Number1_0 = new Variable("arg2Number1_0", Double.valueOf(1.0));
        notEqual = new CoreOperationNotEqual(arg1Number1_0, arg2Number1_0);
        result = notEqual.computeValue(new EvalContext());
        assertFalse((Boolean) result);

        // 1.0 != 2.0
        Variable arg1Number1_0_2 = new Variable("arg1Number1_0_2", Double.valueOf(1.0));
        Variable arg2Number2_0 = new Variable("arg2Number2_0", Double.valueOf(2.0));
        notEqual = new CoreOperationNotEqual(arg1Number1_0_2, arg2Number2_0);
        result = notEqual.computeValue(new EvalContext());
        assertTrue((Boolean) result);
    }

    @Test
    public void testCoreOperationNotEqual_Null_Object() {
        Expression arg1 = new Variable("arg1");
        Expression arg2 = new Variable("arg2");
        CoreOperationNotEqual notEqual = new CoreOperationNotEqual(arg1, arg2);

        Object result = notEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);

        // null != Object
        Variable arg1Null = new Variable("arg1Null", null);
        Variable arg2Object = new Variable("arg2Object", new Object());
        notEqual = new CoreOperationNotEqual(arg1Null, arg2Object);
        result = notEqual.computeValue(new EvalContext());
        assertTrue((Boolean) result);
    }

    @Test
    public void testCoreOperationNotEqual_Object_Null() {
        Expression arg1 = new Variable("arg1");
        Expression arg2 = new Variable("arg2");
        CoreOperationNotEqual notEqual = new CoreOperationNotEqual(arg1, arg2);

        Object result = notEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);

        // Object != null
        Variable arg1Object = new Variable("arg1Object", new Object());
        Variable arg2Null = new Variable("arg2Null", null);
        notEqual = new CoreOperationNotEqual(arg1Object, arg2Null);
        result = notEqual.computeValue(new EvalContext());
        assertTrue((Boolean) result);
    }

    @Test
    public void testCoreOperationNotEqual_Object_Object() {
        Expression arg1 = new Variable("arg1");
        Expression arg2 = new Variable("arg2");
        CoreOperationNotEqual notEqual = new CoreOperationNotEqual(arg1, arg2);

        Object result = notEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);

        // Object1 != Object2
        Variable arg1Object1 = new Variable("arg1Object1", new Object());
        Variable arg2Object2 = new Variable("arg2Object2", new Object());
        notEqual = new CoreOperationNotEqual(arg1Object1, arg2Object2);
        result = notEqual.computeValue(new EvalContext());
        assertTrue((Boolean) result);
    }
}