package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*; import static org.mockito.Mockito.mock; import static org.mockito.Mockito.when; import java.util.*; import java.util.ArrayList; import java.util.Arrays; import java.util.List; import org.apache.commons.jxpath.ri.EvalContext; import org.apache.commons.jxpath.ri.axes.InitialContext; import org.apache.commons.jxpath.ri.axes.SelfContext; import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThanExpression; import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression; import org.apache.commons.jxpath.ri.compiler.CoreExpression; import org.apache.commons.jxpath.ri.compiler.ConstantExpression; import org.apache.commons.jxpath.ri.compiler.CollectionExpression; import org.apache.commons.jxpath.ri.compiler.ArrayListExpression; import org.junit.Before; import org.junit.Test;

public class CoreOperationRelationalExpressionTest { CoreOperationRelationalExpression expr; EvalContext context;

@Before public void setup() { context = new EvalContext(); }


@Test public void testComputeValue() { Expression left = new ConstantExpression(1); Expression right = new ConstantExpression(2); CoreOperationRelationalExpression expr = new CoreOperationGreaterThanExpression(new CoreExpression[]{left, right}); Object result = expr.computeValue(context); assertFalse(result);

 left = new ConstantExpression(2); right = new ConstantExpression(1); expr = new CoreOperationGreaterThanExpression(new CoreExpression[]{left, right}); result = expr.computeValue(context); assertTrue(result); }

@Test public void testComputeValueWithNullArguments() { CoreOperationRelationalExpression expr = new CoreOperationGreaterThanExpression(new CoreExpression[]{null, new ConstantExpression(2)}); Object result = expr.computeValue(context); assertTrue(result instanceof RuntimeException); assertEquals("Cannot evaluate null expression", result.getMessage());

 expr = new CoreOperationRelationalExpression(new CoreExpression[]{new ConstantExpression(1), null}); result = expr.computeValue(context); assertTrue(result instanceof RuntimeException); assertEquals("Cannot evaluate null expression", result.getMessage()); }

@Test public void testComputeValueWithDifferentDataTypes() { Expression left = new ConstantExpression("Hello"); Expression right = new ConstantExpression(2); CoreOperationRelationalExpression expr = new CoreOperationGreaterThanExpression(new CoreExpression[]{left, right}); Object result = expr.computeValue(context); assertTrue(result instanceof ComparatorException); assertEquals("Can't compare String and int", result.getMessage()); }

@Test public void testComputeValueWithInitialContext() { Expression left = new InitialContext(); Expression right = new ConstantExpression(1); CoreOperationRelationalExpression expr = new CoreOperationGreaterThanExpression(new CoreExpression[]{left, right}); Object result = expr.computeValue(context); assertTrue(result instanceof RuntimeException); assertEquals("Cannot evaluate InitialContext", result.getMessage());

 left = new ConstantExpression(1); right = new InitialContext(); expr = new CoreOperationGreaterThanExpression(new CoreExpression[]{left, right}); result = expr.computeValue(context); assertTrue(result instanceof RuntimeException); assertEquals("Cannot evaluate InitialContext", result.getMessage()); }

@Test public void testComputeValueWithIterator() { Expression left = new CollectionExpression(new ArrayListExpression(Arrays.asList(1, 2, 3))); Expression right = new CollectionExpression(new ArrayListExpression(Arrays.asList(2, 3, 4))); CoreOperationRelationalExpression expr = new CoreOperationGreaterThanExpression(new CoreExpression[]{left, right}); Object result = expr.computeValue(context); assertTrue(result instanceof Boolean); assertTrue(result); }

// Add more test cases for other edge cases and exceptions

}