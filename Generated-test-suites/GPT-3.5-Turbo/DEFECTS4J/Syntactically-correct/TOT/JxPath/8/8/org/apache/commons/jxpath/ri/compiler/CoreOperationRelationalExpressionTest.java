import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {
    
    // Test for evaluateCompare() method with positive result
    @Test
    public void testEvaluateComparePositive() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression() {
            @Override
            protected boolean evaluateCompare(int compare) {
                if(compare > 0) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        
        assertTrue(expression.evaluateCompare(1));
    }
    
    // Test for evaluateCompare() method with negative result
    @Test
    public void testEvaluateCompareNegative() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression() {
            @Override
            protected boolean evaluateCompare(int compare) {
                if(compare > 0) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        
        assertFalse(expression.evaluateCompare(-1));
    }
    
    // Test for compute() method with two equal values
    @Test
    public void testComputeEqualValues() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression() {
            @Override
            protected boolean evaluateCompare(int compare) {
                if(compare == 0) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        
        Object left = new Object();
        Object right = new Object();
        
        assertTrue(expression.compute(left, right));
    }
    
    // Test for compute() method with left value less than right value
    @Test
    public void testComputeLeftLessThanRight() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression() {
            @Override
            protected boolean evaluateCompare(int compare) {
                if(compare < 0) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        
        int left = 5;
        int right = 10;
        
        assertTrue(expression.compute(left, right));
    }
    
    // Test for compute() method with left value greater than right value
    @Test
    public void testComputeLeftGreaterThanRight() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression() {
            @Override
            protected boolean evaluateCompare(int compare) {
                if(compare > 0) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        
        int left = 10;
        int right = 5;
        
        assertTrue(expression.compute(left, right));
    }
    
    // Test for compute() method with left value equal to right value
    @Test
    public void testComputeLeftEqualToRight() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression() {
            @Override
            protected boolean evaluateCompare(int compare) {
                if(compare == 0) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        
        int left = 5;
        int right = 5;
        
        assertFalse(expression.compute(left, right));
    }
    
    // Test for containsMatch() method with iterator containing match
    @Test
    public void testContainsMatchWithMatch() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression() {
            @Override
            protected boolean evaluateCompare(int compare) {
                if(compare == 0) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        
        Iterator<Integer> it = Arrays.asList(1, 2, 3).iterator();
        int value = 2;
        
        assertTrue(expression.containsMatch(it, value));
    }
    
    // Test for containsMatch() method with iterator not containing match
    @Test
    public void testContainsMatchWithoutMatch() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression() {
            @Override
            protected boolean evaluateCompare(int compare) {
                if(compare == 0) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        
        Iterator<Integer> it = Arrays.asList(1, 2, 3).iterator();
        int value = 4;
        
        assertFalse(expression.containsMatch(it, value));
    }
    
    // Test for findMatch() method with matching elements
    @Test
    public void testFindMatchWithMatch() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression() {
            @Override
            protected boolean evaluateCompare(int compare) {
                if(compare == 0) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        
        Iterator<Integer> lit = Arrays.asList(1, 2, 3).iterator();
        Iterator<Integer> rit = Arrays.asList(2, 4, 6).iterator();
        
        assertTrue(expression.findMatch(lit, rit));
    }
    
    // Test for findMatch() method with no matching elements
    @Test
    public void testFindMatchWithoutMatch() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression() {
            @Override
            protected boolean evaluateCompare(int compare) {
                if(compare == 0) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        
        Iterator<Integer> lit = Arrays.asList(1, 2, 3).iterator();
        Iterator<Integer> rit = Arrays.asList(4, 5, 6).iterator();
        
        assertFalse(expression.findMatch(lit, rit));
    }
}