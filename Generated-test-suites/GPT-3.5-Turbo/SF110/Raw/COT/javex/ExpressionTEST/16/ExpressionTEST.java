
package org.javex;

import static org.junit.Assert.*;
import org.junit.Test;

public class ExpressionTest {
  
  @Test
  public void testGetExternalFunctions() {
    Expression expression = new Expression("// Test expression");
    assertNotNull(expression.getExternalFunctions());
  }
  
  @Test
  public void testConstructor() {
    Expression expression = new Expression("// Test expression");
    assertNotNull(expression);
  }
  
  @Test
  public void testIsReservedFunction() {
    Expression expression = new Expression("// Test expression");
    assertFalse(expression.isReservedFunction("test"));
  }
  
  @Test
  public void testIsLogicalFunction() {
    Expression expression = new Expression("// Test expression");
    assertFalse(expression.isLogicalFunction("test"));
  }
  
  @Test
  public void testIsMathFunction() {
    Expression expression = new Expression("// Test expression");
    assertFalse(expression.isMathFunction("test"));
  }
  
  @Test
  public void testIsDateFunction() {
    Expression expression = new Expression("// Test expression");
    assertFalse(expression.isDateFunction("test"));
  }
  
  @Test
  public void testIsStringFunction() {
    Expression expression = new Expression("// Test expression");
    assertFalse(expression.isStringFunction("test"));
  }
  
  @Test
  public void testGetExternalFunction() {
    Expression expression = new Expression("// Test expression");
    assertNull(expression.getExternalFunction("test"));
  }
  
  @Test
  public void testIsExternalFunction() {
    Expression expression = new Expression("// Test expression");
    assertFalse(expression.isExternalFunction("test"));
  }
  
  @Test
  public void testGetFunction() {
    Expression expression = new Expression("// Test expression");
    assertNull(expression.getFunction("test"));
  }
  
  @Test
  public void testIsFunction() {
    Expression expression = new Expression("// Test expression");
    assertFalse(expression.isFunction("test"));
  }
  
  @Test
  public void testExpression() {
    Expression expression = new Expression();
    assertNotNull(expression);
  }
  
  @Test
  public void testPrepare() {
    Expression expression = new Expression("// Test expression");
    expression.prepare();
    assertTrue(expression.isPrepared());
  }
  
  @Test
  public void testGetStackCopy() {
    Expression expression = new Expression("// Test expression");
    expression.prepare();
    assertNotNull(expression.getStackCopy());
  }
  
  @Test
  public void testEvaluate() {
    Expression expression = new Expression("// Test expression");
    try {
      assertNotNull(expression.evaluate());
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }
  
  @Test
  public void testTokenize() {
    Expression expression = new Expression("// Test expression");
    expression.prepare();
    assertNotNull(expression.getStackCopy());
  }
}

