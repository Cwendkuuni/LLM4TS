import org.apache.commons.jxpath.ri.compiler.Expression;
import org.junit.Assert;
import org.junit.Test;

public class ExpressionTest {

    @Test
    public void testIsContextDependent() {
        Expression expression = new MyExpression(); // Replace MyExpression with the actual subclass under test
        boolean contextDependent = expression.isContextDependent();
        Assert.assertTrue("An expression should be context dependent by default", contextDependent);
    }

    @Test
    public void testIsContextIndependent() {
        Expression expression = new MyExpression(); // Replace MyExpression with the actual subclass under test
        expression.setContextDependent(false);
        boolean contextDependent = expression.isContextDependent();
        Assert.assertFalse("An expression should not be context dependent when set to false", contextDependent);
    }
}