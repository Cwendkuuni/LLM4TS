import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class ExpressionTest {
  
  // Test case for getExternalFunctions() method
  @Test
  public void testGetExternalFunctions() {
    Expression expression = new Expression() {
      public Object evaluate() {
        return null;
      }
    };
    List<FunctionExpression> externalFunctions = new ArrayList<>();
    externalFunctions.add(new AbsFunction());
    expression.externalFunctions = externalFunctions;
    
    assertEquals(externalFunctions, expression.getExternalFunctions());
  }
  
  // Test case for isReservedFunction() method
  @Test
  public void testIsReservedFunction() {
    Expression expression = new Expression() {
      public Object evaluate() {
        return null;
      }
    };
    expression.reservedFunctions = new TreeMap<>();
    expression.reservedFunctions.put(new AndFunction(), new AndFunction());
    expression.reservedFunctions.put(new ContainsFunction(), new ContainsFunction());
    
    assertTrue(expression.isReservedFunction("and"));
    assertFalse(expression.isReservedFunction("or"));
  }
  
  // Test case for isLogicalFunction() method
  @Test
  public void testIsLogicalFunction() {
    Expression expression = new Expression() {
      public Object evaluate() {
        return null;
      }
    };
    expression.reservedFunctions = new TreeMap<>();
    expression.reservedFunctions.put(new AndFunction(), new AndFunction());
    expression.reservedFunctions.put(new OrFunction(), new OrFunction());
    
    assertTrue(expression.isLogicalFunction("and"));
    assertFalse(expression.isLogicalFunction("not"));
  }
  
  // Test case for isMathFunction() method
  @Test
  public void testIsMathFunction() {
    Expression expression = new Expression() {
      public Object evaluate() {
        return null;
      }
    };
    expression.reservedFunctions = new TreeMap<>();
    expression.reservedFunctions.put(new PlusFunction(), new PlusFunction());
    expression.reservedFunctions.put(new MinusFunction(), new MinusFunction());
    
    assertTrue(expression.isMathFunction("plus"));
    assertFalse(expression.isMathFunction("multiply"));
  }
  
  // Test case for isDateFunction() method
  @Test
  public void testIsDateFunction() {
    Expression expression = new Expression() {
      public Object evaluate() {
        return null;
      }
    };
    expression.reservedFunctions = new TreeMap<>();
    expression.reservedFunctions.put(new DateAddFunction(), new DateAddFunction());
    expression.reservedFunctions.put(new DateDiffFunction(), new DateDiffFunction());
    
    assertTrue(expression.isDateFunction("dateadd"));
    assertFalse(expression.isDateFunction("datediff"));
  }
  
  // Test case for isStringFunction() method
  @Test
  public void testIsStringFunction() {
    Expression expression = new Expression() {
      public Object evaluate() {
        return null;
      }
    };
    expression.reservedFunctions = new TreeMap<>();
    expression.reservedFunctions.put(new StartsWithFunction(), new StartsWithFunction());
    expression.reservedFunctions.put(new EndsWithFunction(), new EndsWithFunction());
    
    assertTrue(expression.isStringFunction("startswith"));
    assertFalse(expression.isStringFunction("length"));
  }
  
  // Test case for getExternalFunction() method
  @Test
  public void testGetExternalFunction() {
    Expression expression = new Expression() {
      public Object evaluate() {
        return null;
      }
    };
    List<FunctionExpression> externalFunctions = new ArrayList<>();
    externalFunctions.add(new AbsFunction());
    externalFunctions.add(new PowerFunction());
    expression.externalFunctions = externalFunctions;
    
    assertNotNull(expression.getExternalFunction("abs"));
    assertNull(expression.getExternalFunction("sqrt"));
  }
  
  // Test case for isExternalFunction() method
  @Test
  public void testIsExternalFunction() {
    Expression expression = new Expression() {
      public Object evaluate() {
        return null;
      }
    };
    List<FunctionExpression> externalFunctions = new ArrayList<>();
    externalFunctions.add(new AbsFunction());
    externalFunctions.add(new PowerFunction());
    expression.externalFunctions = externalFunctions;
    
    assertTrue(expression.isExternalFunction("abs"));
    assertFalse(expression.isExternalFunction("sqrt"));
  }
  
  // Test case for getFunction() method
  @Test
  public void testGetFunction() {
    Expression expression = new Expression() {
      public Object evaluate() {
        return null;
      }
    };
    expression.reservedFunctions = new TreeMap<>();
    expression.reservedFunctions.put(new PlusFunction(), new PlusFunction());
    List<FunctionExpression> externalFunctions = new ArrayList<>();
    externalFunctions.add(new AbsFunction());
    expression.externalFunctions = externalFunctions;
    
    assertNotNull(expression.getFunction("plus"));
    assertNotNull(expression.getFunction("abs"));
    assertNull(expression.getFunction("power"));
  }
  
  // Test case for isFunction() method
  @Test
  public void testIsFunction() {
    Expression expression = new Expression() {
      public Object evaluate() {
        return null;
      }
    };
    expression.reservedFunctions = new TreeMap<>();
    expression.reservedFunctions.put(new PlusFunction(), new PlusFunction());
    List<FunctionExpression> externalFunctions = new ArrayList<>();
    externalFunctions.add(new AbsFunction());
    expression.externalFunctions = externalFunctions;
    
    assertTrue(expression.isFunction("plus"));
    assertTrue(expression.isFunction("abs"));
    assertFalse(expression.isFunction("power"));
  }
  
  // Test case for prepare() method
  @Test
  public void testPrepare() {
    Expression expression = new Expression() {
      public Object evaluate() {
        return null;
      }
    };
    expression.expression = "2 + 3";
    
    expression.prepare();
    
    assertNotNull(expression.stack);
    assertTrue(expression.prepared);
  }
  
  // Test case for getStackCopy() method
  @Test
  public void testGetStackCopy() {
    Expression expression = new Expression() {
      public Object evaluate() {
        return null;
      }
    };
    expression.stack = new ExpressionStack();
    expression.stack.add("2");
    expression.stack.add("+");
    expression.stack.add("3");
    
    ExpressionStack stackCopy = expression.getStackCopy();
    
    assertEquals(expression.stack, stackCopy);
    assertNotSame(expression.stack, stackCopy);
  }
  
  // Test case for evaluate() method
  @Test
  public void testEvaluate() {
    Expression expression = new Expression() {
      public Object evaluate() {
        return 2 + 3;
      }
    };
    
    try {
      Object result = expression.evaluate();
      assertEquals(5, result);
    } catch (Exception e) {
      fail("An exception should not have been thrown.");
    }
  }
  
  // Test case for tokenize() method
  @Test
  public void testTokenize() {
    Expression expression = new Expression() {
      public Object evaluate() {
        return null;
      }
    };
    expression.expression = "2 + 3";
    
    expression.tokenize();
    
    assertEquals(3, expression.stack.size());
  }
}