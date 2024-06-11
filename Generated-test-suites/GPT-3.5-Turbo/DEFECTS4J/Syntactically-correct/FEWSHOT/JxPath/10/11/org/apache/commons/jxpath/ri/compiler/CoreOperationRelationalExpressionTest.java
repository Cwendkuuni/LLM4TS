import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {
   @Test
   public void testComputeValue_lessThan() {
       CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
       boolean result = expression.computeValue(context);
       assertEquals(expectedResult, result);
   }

   @Test
   public void testComputeValue_lessThanOrEqual() {
       CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
       boolean result = expression.computeValue(context);
       assertEquals(expectedResult, result);
   }

   @Test
   public void testComputeValue_greaterThan() {
       CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
       boolean result = expression.computeValue(context);
       assertEquals(expectedResult, result);
   }

   @Test
   public void testComputeValue_greaterThanOrEqual() {
       CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
       boolean result = expression.computeValue(context);
       assertEquals(expectedResult, result);
   }

   @Test
   public void testGetPrecedence() {
       CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
       int precedence = expression.getPrecedence();
       assertEquals(expectedPrecedence, precedence);
   }

   @Test
   public void testIsSymmetric() {
       CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
       boolean symmetric = expression.isSymmetric();
       assertEquals(expectedSymmetric, symmetric);
   }

   @Test
   public void testEvaluateCompare() {
       CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
       boolean result = expression.evaluateCompare(compare);
       assertEquals(expectedResult, result);
   }

   @Test
   public void testCompute() {
       CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
       boolean result = expression.compute(left, right);
       assertEquals(expectedResult, result);
   }

   @Test
   public void testReduce() {
       CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
       Object result = expression.reduce(o);
       assertEquals(expectedResult, result);
   }

   @Test
   public void testContainsMatch() {
       CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
       boolean result = expression.containsMatch(it, value);
       assertEquals(expectedResult, result);
   }

   @Test
   public void testFindMatch() {
       CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
       boolean result = expression.findMatch(lit, rit);
       assertEquals(expectedResult, result);
   }
}