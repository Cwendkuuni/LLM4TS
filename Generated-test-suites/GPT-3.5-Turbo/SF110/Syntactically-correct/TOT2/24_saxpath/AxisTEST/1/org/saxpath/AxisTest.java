package saxpath;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class AxisTest {

    @Test
    public void testLookupWithValidAxisNum() {
        assertEquals("child", Axis.lookup(1));
    }
}