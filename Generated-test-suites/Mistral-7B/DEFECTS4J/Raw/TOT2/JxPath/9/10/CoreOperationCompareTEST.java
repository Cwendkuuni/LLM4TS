import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CoreOperationCompareTest {

    @Test
    public void testEqualMethod() {
        Expression left = new ConstantExpression(1);
        Expression right = new ConstantExpression(1);
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext();
        boolean result = coreOperationCompare.equal(context, left, right);
        assertEquals(true, result);
    }
}