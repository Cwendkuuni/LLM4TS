import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CoreOperationRelationalExpressionTest {

 @Test
 public void testGreaterThanExpression() {
 CoreOperation leftOperand = new CoreNumberExpression(5);
 CoreOperation rightOperand = new CoreNumberExpression(6);
 CoreOperationRelationalExpression greaterThanExpression = new CoreOperationGreaterThanExpression(leftOperand, rightOperand);
 assertTrue(greaterThanExpression.evaluate(new Context()));
 }

 @Test
 public void testGreaterThanOrEqualExpression() {
 CoreOperation leftOperand = new CoreNumberExpression(5);
 CoreOperation rightOperand = new CoreNumberExpression(5);
 CoreOperationRelationalExpression greaterThanOrEqualExpression = new CoreOperationGreaterThanOrEqualExpression(leftOperand, rightOperand);
 assertTrue(greaterThanOrEqualExpression.evaluate(new Context()));

 CoreOperation leftOperand2 = new CoreNumberExpression(5);
 CoreOperation rightOperand2 = new CoreNumberExpression(6);
 CoreOperationRelationalExpression greaterThanOrEqualExpression2 = new CoreOperationGreaterThanOrEqualExpression(leftOperand2, rightOperand2);
 assertTrue(greaterThanOrEqualExpression2.evaluate(new Context()));
 }

 @Test
 public void testLessThanExpression() {
 CoreOperation leftOperand = new CoreNumberExpression(5);
 CoreOperation rightOperand = new CoreNumberExpression(6);
 CoreOperationRelationalExpression lessThanExpression = new CoreOperationLessThanExpression(leftOperand, rightOperand);
 assertFalse(lessThanExpression.evaluate(new Context()));
 }

 @Test
 public void testLessThanOrEqualExpression() {
 CoreOperation leftOperand = new CoreNumberExpression(5);
 CoreOperation rightOperand = new CoreNumberExpression(5);
 CoreOperationRelationalExpression lessThanOrEqualExpression = new CoreOperationLessThanOrEqualExpression(leftOperand, rightOperand);
 assertTrue(lessThanOrEqualExpression.evaluate(new Context()));

 CoreOperation leftOperand3 = new CoreNumberExpression(5);
 CoreOperation rightOperand3 = new CoreNumberExpression(6);
 CoreOperationRelationalExpression lessThanOrEqualExpression3 = new CoreOperationLessThanOrEqualExpression(leftOperand3, rightOperand3);
 assertFalse(lessThanOrEqualExpression3.evaluate(new Context()));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidArguments() {
 CoreOperation leftOperand4 = new CoreOperationRelationalExpression(new CoreNumberExpression(5), new CoreOperationRelationalExpression(new CoreNumberExpression(5)));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidArguments2() {
 CoreOperation leftOperand5 = new CoreNumberExpression(5);
 CoreOperationRelationalExpression relationalExpression = new CoreOperationRelationalExpression(new CoreNumberExpression(5), leftOperand5);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidArguments3() {
 CoreOperation leftOperand6 = new CoreOperationRelationalExpression(new CoreNumberExpression(5), new CoreOperationRelationalExpression(new CoreNumberExpression(5), new CoreNumberExpression(5)));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidArguments4() {
 CoreOperation leftOperand7 = new CoreOperationRelationalExpression(new CoreNumberExpression(5), new CoreOperationRelationalExpression(new CoreNumberExpression(5), new CoreExpression[]{}));
 }

}