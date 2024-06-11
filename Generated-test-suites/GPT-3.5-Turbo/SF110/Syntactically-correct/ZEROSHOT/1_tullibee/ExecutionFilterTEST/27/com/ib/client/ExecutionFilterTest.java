import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ExecutionFilterTest {

    @Test
    public void testEquals() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "code1", "time1", "symbol1", "type1", "exchange1", "side1");
        ExecutionFilter filter2 = new ExecutionFilter(1, "code1", "time1", "symbol1", "type1", "exchange1", "side1");

        assertEquals(true, filter1.equals(filter2));
    }
}