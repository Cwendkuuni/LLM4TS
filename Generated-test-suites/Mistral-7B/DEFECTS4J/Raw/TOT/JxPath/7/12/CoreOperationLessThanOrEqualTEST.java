import org.apache.commons.jxpath.ri.EvaluationContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.JXPathContext;
import org.apache.commons.jxpath.ri.nodes.NumberNode;
import org.apache.commons.jxpath.ri.nodes.SimpleNode;
import org.apache.commons.jxpath.ri.util.ExpressionUtil;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CoreOperationLessThanOrEqualTest {

 @Test
 public void testLessThanOrEqualWithBothNumbersLess() {
 InfoSet infoSet = new InfoSet();
 NumberNode num1 = new NumberNode("5.0", infoSet);
 NumberNode num2 = new NumberNode("10.0", infoSet);

 SimpleNode arg1 = ExpressionUtil.parseExpression("5.0", infoSet);
 SimpleNode arg2 = ExpressionUtil.parseExpression("10.0", infoSet);

 CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(arg1, arg2);
 JXPathContext ctx = new JXPathContext();
 EvaluationContext evaluationContext = new EvaluationContext();
 ctx.setContextItem(evaluationContext, infoSet);

 Object result = op.computeValue(evaluationContext);

 assertEquals("Expected result to be true.", Boolean.TRUE, result);
 }

 @Test
 public void testLessThanOrEqualWithBothNumbersEqual() {
 InfoSet infoSet = new InfoSet();
 NumberNode num1 = new NumberNode("5.0", infoSet);
 NumberNode num2 = new NumberNode("5.0", infoSet);

 SimpleNode arg1 = ExpressionUtil.parseExpression("5.0", infoSet);
 SimpleNode arg2 = ExpressionUtil.parseExpression("5.0", infoSet);

 CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(arg1, arg2);
 JXPathContext ctx = new JXPathContext();
 EvaluationContext evaluationContext = new EvaluationContext();
 ctx.setContextItem(evaluationContext, infoSet);

 Object result = op.computeValue(evaluationContext);

 assertEquals("Expected result to be true.", Boolean.TRUE, result);
 }

 @Test
 public void testLessThanOrEqualWithFirstNumberGreater() {
 InfoSet infoSet = new InfoSet();
 NumberNode num1 = new NumberNode("10.0", infoSet);
 NumberNode num2 = new NumberNode("5.0", infoSet);

 SimpleNode arg1 = ExpressionUtil.parseExpression("10.0", infoSet);
 SimpleNode arg2 = ExpressionUtil.parseExpression("5.0", infoSet);

 CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(arg1, arg2);
 JXPathContext ctx = new JXPathContext();
 EvaluationContext evaluationContext = new EvaluationContext();
 ctx.setContextItem(evaluationContext, infoSet);

 Object result = op.computeValue(evaluationContext);

 assertEquals("Expected result to be false.", Boolean.FALSE, result);
 }

 @Test
 public void testLessThanOrEqualWithFirstNumberEqual() {
 InfoSet infoSet = new InfoSet();
 NumberNode num1 = new NumberNode("5.0", infoSet);
 NumberNode num2 = new NumberNode("5.0", infoSet);

 SimpleNode arg1 = ExpressionUtil.parseExpression("5.0", infoSet);
 SimpleNode arg2 = ExpressionUtil.parseExpression("5.0", infoSet);

 CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(arg1, arg2);
 JXPathContext ctx = new JXPathContext();
 EvaluationContext evaluationContext = new EvaluationContext();
 ctx.setContextItem(evaluationContext, infoSet);

 Object result = op.computeValue(evaluationContext);

 assertEquals("Expected result to be true.", Boolean.TRUE, result);
 }

 @Test(expected = RuntimeException.class)
 public void testLessThanOrEqualWithInvalidArgumentTypes() {
 InfoSet infoSet = new InfoSet();
 NumberNode num1 = new NumberNode("5.0", infoSet);
 StringNode arg2 = new StringNode("Hello", infoSet);

 SimpleNode arg1 = ExpressionUtil.parseExpression("5.0", infoSet);
 SimpleNode arg2Invalid = ExpressionUtil.parseExpression("Hello", infoSet);

 CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(arg1, arg2Invalid);
 JXPathContext ctx = new JXPathContext();
 EvaluationContext evaluationContext = new EvaluationContext();
 ctx.setContextItem(evaluationContext, infoSet);

 try {
 op.computeValue(evaluationContext);
 } catch (RuntimeException e) {
 assertEquals("Expected error message: " +
 "Cannot compare a number and a string.", e.getMessage());
 }
 }
}