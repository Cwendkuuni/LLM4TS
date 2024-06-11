import org.apache.commons.lang.enums.ValuedEnum;
import org.junit.Test;
import java.util.Arrays;
import static org.junit.Assert.*;

public class ValuedEnumTest {

    @Test
    public void testValuedEnumConstructor() {
        ValuedEnum enum1 = new ValuedEnum("Enum1", 1);
        assertEquals("Enum1", enum1.getName());
        assertEquals(1, enum1.getValue());
    }

    // Test other methods here...

}