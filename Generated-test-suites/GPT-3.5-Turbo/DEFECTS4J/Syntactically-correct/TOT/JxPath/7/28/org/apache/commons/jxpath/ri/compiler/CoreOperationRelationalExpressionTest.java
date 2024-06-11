import static org.junit.Assert.*;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {
    
    @Before
    public void setUp() {
        // initialization code before each test case
    }
    
    @After
    public void tearDown() {
        // cleanup code after each test case
    }
    
    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(null);
        int precedence = expression.getPrecedence();
        assertEquals(3, precedence);
    }
    
    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(null);
        boolean symmetric = expression.isSymmetric();
        assertEquals(false, symmetric);
    }
    
    @Test
    public void testConstructorWithValidArguments() {
        Expression[] args = new Expression[]{
            new MockExpression(),
            new MockExpression()
        };
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
        Expression[] actualArgs = expression.getArguments();
        assertArrayEquals(args, actualArgs);
    }
    
    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullArguments() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(null);
    }
    
    @Test
    public void testConstructorWithEmptyArguments() {
        Expression[] args = new Expression[0];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
        Expression[] actualArgs = expression.getArguments();
        assertArrayEquals(args, actualArgs);
    }
    
}