import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.ri.compiler.CoreOperationNotEqual;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.EvalContext;

public class CoreOperationNotEqualTest {

    private CoreOperationNotEqual notEqual;

    @Before
    public void setup() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        notEqual = new CoreOperationNotEqual(arg1, arg2);
    }

    @Test
    public void testComputeValue_EqualArguments_ReturnsFalse() {
        EvalContext context = new EvalContext();
        context.setExpressionContext(context);
        context.setNodeSet(context);
        context.setNode(context);

        boolean result = (boolean) notEqual.computeValue(context);

        assert result == false;
    }

    @Test
    public void testComputeValue_UnequalArguments_ReturnsTrue() {
        EvalContext context = new EvalContext();
        context.setExpressionContext(context);
        context.setNodeSet(context);
        context.setNode(context);

        context.setValue("value1", 5);
        context.setValue("value2", 10);
        notEqual.getVariables(context);

        boolean result = (boolean) notEqual.computeValue(context);

        assert result == true;
    }

    @Test
    public void testGetSymbol_ReturnsNotEqualSymbol() {
        String symbol = notEqual.getSymbol();

        assert symbol.equals("!=");
    }
}