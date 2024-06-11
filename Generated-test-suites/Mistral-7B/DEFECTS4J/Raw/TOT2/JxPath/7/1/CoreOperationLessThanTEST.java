import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.apache.commons.jxpath.ri.Path;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThan;
import org.junit.Assert;
import org.junit.Test;

public class CoreOperationLessThanTest {

    @Test
    public void testLessThan() throws Exception {
        // Given
        Expression arg1 = new LiteralExpression(new Double(2.5));
        Expression arg2 = new LiteralExpression(new Double(3.5));
        CoreOperationLessThan operation = new CoreOperationLessThan(arg1, arg2);
        EvalContext context = new EvalContext(new Path(""), new InfoSet(), new NodeInfo[0]);

        // When
        Boolean result = operation.computeValue(context).booleanValue();

        // Then
        Assert.assertFalse(result);

        arg1 = new LiteralExpression(new Double(2.0));
        arg2 = new LiteralExpression(new Double(3.5));

        // Given
        context = new EvalContext(new Path(""), new InfoSet(), new NodeInfo[0]);

        // When
        result = operation.computeValue(context).booleanValue();

        // Then
        Assert.assertTrue(result);
    }
}