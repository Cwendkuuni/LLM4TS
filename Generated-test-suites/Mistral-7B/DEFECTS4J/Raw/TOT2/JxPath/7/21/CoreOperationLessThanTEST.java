import org.junit.Test;
import org.apache.commons.jxpath.ri.Expression;
import static org.junit.Assert.assertNotNull;

public class CoreOperationLessThanTest {

    @Test
    public void testConstructor() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        assertNotNull(coreOperationLessThan);
    }
}