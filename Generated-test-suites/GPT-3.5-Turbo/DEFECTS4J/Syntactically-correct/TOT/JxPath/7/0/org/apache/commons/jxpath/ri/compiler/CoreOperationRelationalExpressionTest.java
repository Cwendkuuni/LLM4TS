import org.apache.commons.jxpath.ri.compiler.Expression;
import org.junit.Assert;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression coreOp = new CoreOperationRelationalExpression(null);
        int expectedPrecedence = 3;
        int actualPrecedence = coreOp.getPrecedence();
        Assert.assertEquals(expectedPrecedence, actualPrecedence);
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression coreOp = new CoreOperationRelationalExpression(null);
        boolean expectedSymmetry = false;
        boolean actualSymmetry = coreOp.isSymmetric();
        Assert.assertEquals(expectedSymmetry, actualSymmetry);
    }

    @Test
    public void testConstructorArgs_Null() {
        CoreOperationRelationalExpression coreOp = new CoreOperationRelationalExpression(null);
        Assert.assertNull(coreOp.getArgs());
    }

    @Test
    public void testConstructorArgs_NonNull() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression coreOp = new CoreOperationRelationalExpression(args);
        Assert.assertEquals(args, coreOp.getArgs());
    }
}