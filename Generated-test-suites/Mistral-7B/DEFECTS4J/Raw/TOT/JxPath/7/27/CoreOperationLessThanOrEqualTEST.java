import org.apache.commons.jxpath.ri.EvaluationContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.nodes.NumberNode;
import org.apache.commons.jxpath.ri.nodes.VariableRefNode;
import org.apache.commons.jxpath.ri.nodes.expressions.Expression;
import org.apache.commons.jxpath.ri.nodes.expressions.core.CoreOperationLessThanOrEqual;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanOrEqualTest {

 @Test
 public void testLessThanOrEqualWithBothNumbersLessThan() {
 InfoSet infoSet = InfoSet.newInstance();
 Expression arg1 = new NumberNode(1.5, infoSet);
 Expression arg2 = new NumberNode(2.5, infoSet);

 CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
 EvaluationContext context = new EvaluationContext(infoSet);

 Boolean result = (Boolean) operation.computeValue(context);
 assertTrue(result);
 }

 @Test
 public void testLessThanOrEqualWithBothNumbersEqual() {
 InfoSet infoSet = InfoSet.newInstance();
 Expression arg1 = new NumberNode(2.5, infoSet);
 Expression arg2 = new NumberNode(2.5, infoSet);

 CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
 EvaluationContext context = new EvaluationContext(infoSet);

 Boolean result = (Boolean) operation.computeValue(context);
 assertTrue(result);
 }

 @Test
 public void testLessThanOrEqualWithFirstNumberGreaterThanAndSecondNumberLessThan() {
 InfoSet infoSet = InfoSet.newInstance();
 Expression arg1 = new NumberNode(3.5, infoSet);
 Expression arg2 = new NumberNode(2.5, infoSet);

 CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
 EvaluationContext context = new EvaluationContext(infoSet);

 Boolean result = (Boolean) operation.computeValue(context);
 assertFalse(result);
 }

 @Test
 public void testLessThanOrEqualWithFirstNumberGreaterThanAndSecondNumberEqual() {
 InfoSet infoSet = InfoSet.newInstance();
 Expression arg1 = new NumberNode(3.5, infoSet);
 Expression arg2 = new NumberNode(3.5, infoSet);

 CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
 EvaluationContext context = new EvaluationContext(infoSet);

 Boolean result = (Boolean) operation.computeValue(context);
 assertFalse(result);
 }

 @Test
 public void testLessThanOrEqualWithFirstNumberLessThanAndSecondNumberGreaterThan() {
 InfoSet infoSet = InfoSet.newInstance();
 Expression arg1 = new NumberNode(1.5, infoSet);
 Expression arg2 = new NumberNode(2.5, infoSet);

 CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
 EvaluationContext context = new EvaluationContext(infoSet);

 Boolean result = (Boolean) operation.computeValue(context);
 assertFalse(result);
 }

 @Test
 public void testLessThanOrEqualWithFirstNumberLessThanAndsecondNumberGreaterThanOrEqual() {
 InfoSet infoSet = InfoSet.newInstance();
 Expression arg1 = new NumberNode(1.5, infoSet);
 Expression arg2 = new NumberNode(2.5, infoSet);

 CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
 EvaluationContext context = new EvaluationContext(infoSet);

 Boolean result = (Boolean) operation.computeValue(context);
 assertTrue(result);
 }

 @Test
 public void testLessThanOrEqualWithOneVariable() {
 InfoSet infoSet = InfoSet.newInstance();
 Expression arg1 = new VariableRefNode("x", infoSet);
 Expression arg2 = new NumberNode(2.5, infoSet);

 CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
 EvaluationContext context = new EvaluationContext(infoSet);
 context.setVariableValue("x", 1.5);

 Boolean result = (Boolean) operation.computeValue(context);
 assertTrue(result);
 }

 @Test
 public void testLessThanOrEqualWithSecondVariable() {
 InfoSet infoSet = InfoSet.newInstance();
 Expression arg1 = new NumberNode(2.5, infoSet);
 Expression arg2 = new VariableRefNode("x", infoSet);

 CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
 EvaluationContext context = new EvaluationContext(infoSet);
 context.setVariableValue("x", 3.5);

 Boolean result = (Boolean) operation.computeValue(context);
 assertFalse(result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLessThanOrEqualWithNullArguments() {
 InfoSet infoSet = InfoSet.newInstance();
 Expression arg1 = null;
 Expression arg2 = new NumberNode(2.5, infoSet);

 CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
 EvaluationContext context = new EvaluationContext(infoSet);

 try {
 operation.computeValue(context);
 } catch (IllegalArgumentException ex) {
 // Expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLessThanOrEqualWithNonNumericArguments() {
 InfoSet infoSet = InfoSet.newInstance();
 Expression arg1 = new NumberNode(2.5, infoSet);
 Expression arg2 = new StringNode("test", infoSet);

 CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
 EvaluationContext context = new EvaluationContext(infoSet);

 try {
 operation.computeValue(context);
 } catch (IllegalArgumentException ex) {
 // Expected
 }
 }
}