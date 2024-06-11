import org.junit.Before;
import org.junit.Test;

public class CoreOperationGreaterThanTest {
    
    private CoreOperationGreaterThan expression;
    private EvalContext context;
    
    @Before
    public void setUp() {
        expression = new CoreOperationGreaterThan(arg1, arg2);
        context = new EvalContext();
    }
    
    @Test
    public void testComputeValue_withPositiveValues() {
        arg1 = new Expression() {
            public Object computeValue(EvalContext context) {
                return 5;
            }
        };
        arg2 = new Expression() {
            public Object computeValue(EvalContext context) {
                return 3;
            }
        };
        Object result = expression.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }
    
    @Test
    public void testComputeValue_withNegativeValues() {
        arg1 = new Expression() {
            public Object computeValue(EvalContext context) {
                return -5;
            }
        };
        arg2 = new Expression() {
            public Object computeValue(EvalContext context) {
                return -3;
            }
        };
        Object result = expression.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }
    
    @Test
    public void testComputeValue_withEqualValues() {
        arg1 = new Expression() {
            public Object computeValue(EvalContext context) {
                return 5;
            }
        };
        arg2 = new Expression() {
            public Object computeValue(EvalContext context) {
                return 5;
            }
        };
        Object result = expression.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }
    
    @Test(expected=NullPointerException.class)
    public void testComputeValue_withNullValues() {
        arg1 = new Expression() {
            public Object computeValue(EvalContext context) {
                return null;
            }
        };
        arg2 = new Expression() {
            public Object computeValue(EvalContext context) {
                return 3;
            }
        };
        expression.computeValue(context);
    }
    
    @Test(expected=NumberFormatException.class)
    public void testComputeValue_withInvalidValues() {
        arg1 = new Expression() {
            public Object computeValue(EvalContext context) {
                return "five";
            }
        };
        arg2 = new Expression() {
            public Object computeValue(EvalContext context) {
                return 3;
            }
        };
        expression.computeValue(context);
    }
}