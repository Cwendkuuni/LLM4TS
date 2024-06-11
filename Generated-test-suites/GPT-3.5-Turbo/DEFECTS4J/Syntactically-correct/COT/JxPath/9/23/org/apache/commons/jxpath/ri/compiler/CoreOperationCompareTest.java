import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        Expression left = new Expression();
        Expression right = new Expression();
        CoreOperationCompare coreOpCompare = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext();
        boolean result = coreOpCompare.equal(context, left, right);
        assertFalse(result);
    }

}