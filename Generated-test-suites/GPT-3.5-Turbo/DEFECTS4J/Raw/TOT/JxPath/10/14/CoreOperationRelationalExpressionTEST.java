import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class CoreOperationRelationalExpressionTest {
    
    @Test
    public void testComputeValue_BothArgsNull_ReturnsFalse() {
        CoreOperationRelationalExpression operation = new CoreOperationRelationalExpression(null);
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        assertFalse((Boolean) result);
    }
    
    @Test
    public void testComputeValue_LeftArgNull_ReturnsFalse() {
        Expression[] args = { null, new Expression() };
        CoreOperationRelationalExpression operation = new CoreOperationRelationalExpression(args);
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        assertFalse((Boolean) result);
    }
    
    @Test
    public void testComputeValue_RightArgNull_ReturnsFalse() {
        Expression[] args = { new Expression(), null };
        CoreOperationRelationalExpression operation = new CoreOperationRelationalExpression(args);
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        assertFalse((Boolean) result);
    }
    
    @Test
    public void testComputeValue_LeftArgNonNumeric_ReturnsFalse() {
        Expression[] args = { new Expression(), new Expression() };
        CoreOperationRelationalExpression operation = new CoreOperationRelationalExpression(args);
        EvalContext context = new EvalContext();
        context.setSingleNodePointer("abc");
        Object result = operation.computeValue(context);
        assertFalse((Boolean) result);
    }
    
    @Test
    public void testComputeValue_RightArgNonNumeric_ReturnsFalse() {
        Expression[] args = { new Expression(), new Expression() };
        CoreOperationRelationalExpression operation = new CoreOperationRelationalExpression(args);
        EvalContext context = new EvalContext();
        context.setSingleNodePointer(123);
        Object result = operation.computeValue(context);
        assertFalse((Boolean) result);
    }
    
    @Test
    public void testComputeValue_LeftArgIterator_RightArgIterator_ReturnsFalse() {
        Expression[] args = { new Expression(), new Expression() };
        CoreOperationRelationalExpression operation = new CoreOperationRelationalExpression(args);
        EvalContext context = new EvalContext();
        context.setSingleNodePointer(new Iterator() {
            @Override
            public boolean hasNext() {
                return false;
            }
            @Override
            public Object next() {
                return null;
            }
        });
        Object result = operation.computeValue(context);
        assertFalse((Boolean) result);
    }
    
    @Test
    public void testComputeValue_LeftArgIterator_RightArgNonNumeric_ReturnsFalse() {
        Expression[] args = { new Expression(), new Expression() };
        CoreOperationRelationalExpression operation = new CoreOperationRelationalExpression(args);
        EvalContext context = new EvalContext();
        context.setSingleNodePointer(new Iterator() {
            @Override
            public boolean hasNext() {
                return true;
            }
            @Override
            public Object next() {
                return null;
            }
        });
        context.setSingleNodePointer("abc");
        Object result = operation.computeValue(context);
        assertFalse((Boolean) result);
    }
    
    @Test
    public void testComputeValue_LeftArgIterator_RightArgNumeric_ReturnsFalse() {
        Expression[] args = { new Expression(), new Expression() };
        CoreOperationRelationalExpression operation = new CoreOperationRelationalExpression(args);
        EvalContext context = new EvalContext();
        context.setSingleNodePointer(new Iterator() {
            @Override
            public boolean hasNext() {
                return true;
            }
            @Override
            public Object next() {
                return null;
            }
        });
        context.setSingleNodePointer(123);
        Object result = operation.computeValue(context);
        assertFalse((Boolean) result);
    }
    
    @Test
    public void testComputeValue_LeftArgNonIterator_RightArgIterator_ReturnsFalse() {
        Expression[] args = { new Expression(), new Expression() };
        CoreOperationRelationalExpression operation = new CoreOperationRelationalExpression(args);
        EvalContext context = new EvalContext();
        context.setSingleNodePointer("abc");
        context.setSingleNodePointer(new Iterator() {
            @Override
            public boolean hasNext() {
                return false;
            }
            @Override
            public Object next() {
                return null;
            }
        });
        Object result = operation.computeValue(context);
        assertFalse((Boolean) result);
    }
    
    @Test
    public void testComputeValue_LeftArgNumeric_RightArgIterator_ReturnsFalse() {
        Expression[] args = { new Expression(), new Expression() };
        CoreOperationRelationalExpression operation = new CoreOperationRelationalExpression(args);
        EvalContext context = new EvalContext();
        context.setSingleNodePointer(123);
        context.setSingleNodePointer(new Iterator() {
            @Override
            public boolean hasNext() {
                return false;
            }
            @Override
            public Object next() {
                return null;
            }
        });
        Object result = operation.computeValue(context);
        assertFalse((Boolean) result);
    }
    
    @Test
    public void testComputeValue_LeftArgIterator_RightArgIterator_ReturnsCorrectResult() {
        Expression[] args = { new Expression(), new Expression() };
        CoreOperationRelationalExpression operation = new CoreOperationRelationalExpression(args);
        EvalContext context = new EvalContext();
        context.setSingleNodePointer(new Iterator() {
            private int index = 0;
            @Override
            public boolean hasNext() {
                return index < 5;
            }
            @Override
            public Object next() {
                return index++;
            }
        });
        Object result = operation.computeValue(context);
        assertFalse((Boolean) result);
    }
    
    @Test
    public void testComputeValue_LeftArgIterator_ContainsMatchWithRightArg_ReturnsTrue() {
        Expression[] args = { new Expression(), new Expression() };
        CoreOperationRelationalExpression operation = new CoreOperationRelationalExpression(args);
        EvalContext context = new EvalContext();
        context.setSingleNodePointer(new Iterator() {
            private int index = 3;
            @Override
            public boolean hasNext() {
                return index < 10;
            }
            @Override
            public Object next() {
                return index++;
            }
        });
        context.setSingleNodePointer(7);
        Object result = operation.computeValue(context);
        assertTrue((Boolean) result);
    }
    
    @Test
    public void testComputeValue_RightArgIterator_ContainsMatchWithLeftArg_ReturnsTrue() {
        Expression[] args = { new Expression(), new Expression() };
        CoreOperationRelationalExpression operation = new CoreOperationRelationalExpression(args);
        EvalContext context = new EvalContext();
        context.setSingleNodePointer(7);
        context.setSingleNodePointer(new Iterator() {
            private int index = 3;
            @Override
            public boolean hasNext() {
                return index < 10;
            }
            @Override
            public Object next() {
                return index++;
            }
        });
        Object result = operation.computeValue(context);
        assertTrue((Boolean) result);
    }
    
    @Test
    public void testComputeValue_LeftArgLowerThanRightArg_ReturnsTrue() {
        Expression[] args = { new Expression(), new Expression() };
        CoreOperationRelationalExpression operation = new CoreOperationRelationalExpression(args);
        EvalContext context = new EvalContext();
        context.setSingleNodePointer(5);
        context.setSingleNodePointer(10);
        Object result = operation.computeValue(context);
        assertTrue((Boolean) result);
    }
    
    @Test
    public void testComputeValue_LeftArgEqualToRightArg_ReturnsTrue() {
        Expression[] args = { new Expression(), new Expression() };
        CoreOperationRelationalExpression operation = new CoreOperationRelationalExpression(args);
        EvalContext context = new EvalContext();
        context.setSingleNodePointer(5);
        context.setSingleNodePointer(5);
        Object result = operation.computeValue(context);
        assertTrue((Boolean) result);
    }
    
    @Test
    public void testComputeValue_LeftArgGreaterThanRightArg_ReturnsFalse() {
        Expression[] args = { new Expression(), new Expression() };
        CoreOperationRelationalExpression operation = new CoreOperationRelationalExpression(args);
        EvalContext context = new EvalContext();
        context.setSingleNodePointer(10);
        context.setSingleNodePointer(5);
        Object result = operation.computeValue(context);
        assertFalse((Boolean) result);
    }
}