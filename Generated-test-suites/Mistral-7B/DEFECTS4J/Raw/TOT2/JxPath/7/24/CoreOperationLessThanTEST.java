package org.junit.jupiter.engine.execution.tests.junit4.core.operation.lessthan;

import org.apache.commons.jxpath.ri.*;
import org.junit.*;
import static org.junit.Assert.*;

public class CoreOperationLessThanTest {

@Test
public void testComputeValue() {
Expression arg1 = new Const(1.0);
Expression arg2 = new Const(2.0);
CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
EvalContext context = new EvalContext();

Object result = coreOperationLessThan.computeValue(context);

assertTrue(result instanceof Boolean);
assertFalse((Boolean) result);
}

@Test
public void testComputeValueTrue() {
Expression arg1 = new Const(1.0);
Expression arg2 = new Const(2.1);
CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
EvalContext context = new EvalContext();

Object result = coreOperationLessThan.computeValue(context);

assertTrue(result instanceof Boolean);
assertTrue((Boolean) result);
}

@Test
public void testComputeValueEqual() {
Expression arg1 = new Const(1.0);
Expression arg2 = new Const(1.0);
CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
EvalContext context = new EvalContext();

Object result = coreOperationLessThan.computeValue(context);

assertTrue(result instanceof Boolean);
assertFalse((Boolean) result);
}

@Test(expected = NullPointerException.class)
public void testNullArg() {
Expression arg1 = null;
CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, new Const(2.0));
}

@Test(expected = ClassCastException.class)
public void testNonNumberArg() {
Expression arg1 = new StringConst("test");
CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, new Const(2.0));
}
}