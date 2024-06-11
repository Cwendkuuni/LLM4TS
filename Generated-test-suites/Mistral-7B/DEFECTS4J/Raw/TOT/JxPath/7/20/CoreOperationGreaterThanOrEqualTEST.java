package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.apache.commons.jxpath.ri.nodes.NumberNode;
import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest {

 // Test case 1: Valid use case with double numbers
 @Test
 public void testGreaterThanOrEqualDoubleNumbers() {
 EvalContext context = new EvalContext(new InfoSet());
 NumberNode arg1 = new NumberNode(new Double(5.0));
 NumberNode arg2 = new NumberNode(new Double(3.0));
 CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
 boolean expectedResult = true;
 boolean actualResult = operation.computeValue(context).booleanValue();
 assertEquals(expectedResult, actualResult);
 }

 // Test case 2: Edge case with equal double numbers
 @Test
 public void testGreaterThanOrEqualEqualDoubleNumbers() {
 EvalContext context = new EvalContext(new InfoSet());
 NumberNode arg1 = new NumberNode(new Double(5.0));
 NumberNode arg2 = new NumberNode(new Double(5.0));
 CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
 boolean expectedResult = true;
 boolean actualResult = operation.computeValue(context).booleanValue();
 assertEquals(expectedResult, actualResult);
 }

 // Test case 3: Error scenario with non-numeric arguments
 @Test(expected = ClassCastException.class)
 public void testGreaterThanOrEqualNonNumericArguments() {
 EvalContext context = new EvalContext(new InfoSet());
 String arg1 = new String("5");
 NumberNode arg2 = new NumberNode(new Double(3.0));
 CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
 operation.computeValue(context);
 }

 // Test case 4: Error scenario with null context
 @Test(expected = NullPointerException.class)
 public void testGreaterThanOrEqualNullContext() {
 CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(new NumberNode(new Double(5.0)), new NumberNode(new Double(3.0)));
 operation.computeValue(null);
 }

 // Test case 5: Error scenario with null argument
 @Test(expected = NullPointerException.class)
 public void testGreaterThanOrEqualNullArgument() {
 EvalContext context = new EvalContext(new InfoSet());
 CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(null, new NumberNode(new Double(3.0)));
 operation.computeValue(context);
 }
}