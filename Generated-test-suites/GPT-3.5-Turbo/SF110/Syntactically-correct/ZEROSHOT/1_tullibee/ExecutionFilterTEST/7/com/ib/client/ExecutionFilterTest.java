import org.junit.Assert;
import org.junit.Test;

public class ExecutionFilterTest {

    @Test
    public void testEquals_NullObject_ReturnsFalse() {
        ExecutionFilter filter = new ExecutionFilter();
        boolean result = filter.equals(null);
        Assert.assertFalse(result);
    }

    @Test
    public void testEquals_SameObject_ReturnsTrue() {
        ExecutionFilter filter = new ExecutionFilter();
        boolean result = filter.equals(filter);
        Assert.assertTrue(result);
    }

    @Test
    public void testEquals_EqualObjects_ReturnsTrue() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "acctCode1", "time1", "symbol1", "secType1", "exchange1", "side1");
        ExecutionFilter filter2 = new ExecutionFilter(1, "acctCode1", "time1", "symbol1", "secType1", "exchange1", "side1");
        boolean result = filter1.equals(filter2);
        Assert.assertTrue(result);
    }

    @Test
    public void testEquals_DifferentClientId_ReturnsFalse() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "acctCode1", "time1", "symbol1", "secType1", "exchange1", "side1");
        ExecutionFilter filter2 = new ExecutionFilter(2, "acctCode1", "time1", "symbol1", "secType1", "exchange1", "side1");
        boolean result = filter1.equals(filter2);
        Assert.assertFalse(result);
    }

    @Test
    public void testEquals_DifferentAcctCode_ReturnsFalse() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "acctCode1", "time1", "symbol1", "secType1", "exchange1", "side1");
        ExecutionFilter filter2 = new ExecutionFilter(1, "acctCode2", "time1", "symbol1", "secType1", "exchange1", "side1");
        boolean result = filter1.equals(filter2);
        Assert.assertFalse(result);
    }

    @Test
    public void testEquals_DifferentTime_ReturnsFalse() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "acctCode1", "time1", "symbol1", "secType1", "exchange1", "side1");
        ExecutionFilter filter2 = new ExecutionFilter(1, "acctCode1", "time2", "symbol1", "secType1", "exchange1", "side1");
        boolean result = filter1.equals(filter2);
        Assert.assertFalse(result);
    }

    @Test
    public void testEquals_DifferentSymbol_ReturnsFalse() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "acctCode1", "time1", "symbol1", "secType1", "exchange1", "side1");
        ExecutionFilter filter2 = new ExecutionFilter(1, "acctCode1", "time1", "symbol2", "secType1", "exchange1", "side1");
        boolean result = filter1.equals(filter2);
        Assert.assertFalse(result);
    }

    @Test
    public void testEquals_DifferentSecType_ReturnsFalse() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "acctCode1", "time1", "symbol1", "secType1", "exchange1", "side1");
        ExecutionFilter filter2 = new ExecutionFilter(1, "acctCode1", "time1", "symbol1", "secType2", "exchange1", "side1");
        boolean result = filter1.equals(filter2);
        Assert.assertFalse(result);
    }

    @Test
    public void testEquals_DifferentExchange_ReturnsFalse() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "acctCode1", "time1", "symbol1", "secType1", "exchange1", "side1");
        ExecutionFilter filter2 = new ExecutionFilter(1, "acctCode1", "time1", "symbol1", "secType1", "exchange2", "side1");
        boolean result = filter1.equals(filter2);
        Assert.assertFalse(result);
    }

    @Test
    public void testEquals_DifferentSide_ReturnsFalse() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "acctCode1", "time1", "symbol1", "secType1", "exchange1", "side1");
        ExecutionFilter filter2 = new ExecutionFilter(1, "acctCode1", "time1", "symbol1", "secType1", "exchange1", "side2");
        boolean result = filter1.equals(filter2);
        Assert.assertFalse(result);
    }
}