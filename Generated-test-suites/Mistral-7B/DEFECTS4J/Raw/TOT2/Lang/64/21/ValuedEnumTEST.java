import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TestValuedEnum {

    @Test
    public void testConstructor() {
        ValuedEnum enumObj = new ValuedEnum("TestEnum", 10);
        assertEquals("TestEnum", enumObj.getName());
        assertEquals(10, enumObj.getValue());
    }
}