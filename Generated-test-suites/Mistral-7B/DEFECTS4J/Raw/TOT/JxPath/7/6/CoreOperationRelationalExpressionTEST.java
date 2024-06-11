import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

 @Test
 public void testGreaterThan() {
 CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new CoreNumber(5), new CoreNumber(3)) {
 @Override
 public int getOperator() {
 return GREATER_THAN;
 }
 };
 JXPathContext context = new JXPathContext();
 context.setValues("a", 5, "b", 3);
 int result = expr.evaluateToInt(context);
 assertTrue(result == 1);
 }

 @Test
 public void testLessThan() {
 CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new CoreNumber(3), new CoreNumber(5)) {
 @Override
 public int getOperator() {
 return LESS_THAN;
 }
 };
 JXPathContext context = new JXPathContext();
 context.setValues("a", 5, "b", 3);
 int result = expr.evaluateToInt(context);
 assertFalse(result == 1);
 }

 @Test
 public void testGreaterThanOrEqual() {
 CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new CoreNumber(5), new CoreNumber(5)) {
 @Override
 public int getOperator() {
 return GREATER_THAN_OR_EQUAL;
 }
 };
 JXPathContext context = new JXPathContext();
 context.setValues("a", 5, "b", 5);
 int result = expr.evaluateToInt(context);
 assertTrue(result == 1);
 }

 @Test
 public void testLessThanOrEqual() {
 CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new CoreNumber(3), new CoreNumber(5)) {
 @Override
 public int getOperator() {
 return LESS_THAN_OR_EQUAL;
 }
 };
 JXPathContext context = new JXPathContext();
 context.setValues("a", 5, "b", 3);
 int result = expr.evaluateToInt(context);
 assertTrue(result == 0);
 }

 @Test
 public void testInvalidOperator() {
 CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new CoreNumber(5), new CoreNumber(3)) {
 @Override
 public int getOperator() {
 return -1; // Invalid operator
 }
 };
 JXPathContext context = new JXPathContext();
 context.setValues("a", 5, "b", 3);
 int result = expr.evaluateToInt(context);
 assertThrows(IllegalArgumentException.class, () -> expr.evaluateToInt(context));
 }

 @Test
 public void testNullArg() {
 CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new CoreNull(), new CoreNumber(5)) {
 @Override
 public int getOperator() {
 return LESS_THAN;
 }
 };
 JXPathContext context = new JXPathContext();
 context.setValues("a", null, "b", 5);
 int result = expr.evaluateToInt(context);
 assertThrows(NullPointerException.class, () -> expr.evaluateToInt(context));
 }
}