import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.junit.Test;

public class CoreOperationCompareTest {

    @Test
    public void testCoreOperationCompareConstructor() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationCompare comparator = new CoreOperationCompare(arg1, arg2);
        assertNotNull(comparator);
    }
}