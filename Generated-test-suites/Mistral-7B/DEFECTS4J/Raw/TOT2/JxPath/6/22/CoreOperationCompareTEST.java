import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class CoreOperationCompareTest {

    @Test
    public void testConstructor() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);
        assertTrue(coreOperationCompare != null);
    }
}