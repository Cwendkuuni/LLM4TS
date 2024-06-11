package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;

public class CoreOperationEqualTest {
	
	@Test
	public void testComputeValue_equal() {
		Expression arg1 = new Expression();
		Expression arg2 = new Expression();
		CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
		EvalContext context = new EvalContext();
		assertTrue(operation.computeValue(context));
	}
	
	@Test
	public void testComputeValue_notEqual() {
		Expression arg1 = new Expression();
		Expression arg2 = new Expression();
		CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
		EvalContext context = new EvalContext();
		assertFalse(operation.computeValue(context));
	}
	
	@Test
	public void testGetSymbol() {
		Expression arg1 = new Expression();
		Expression arg2 = new Expression();
		CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
		assertEquals("=", operation.getSymbol());
	}

}