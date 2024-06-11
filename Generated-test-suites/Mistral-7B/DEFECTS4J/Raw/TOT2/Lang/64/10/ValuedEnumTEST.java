import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class ValuedEnumTest {

    @Test
    public void testGetEnum() {
        ValuedEnum javaVersionEnum = ValuedEnum.JAVA1_0;
        Enum testEnum = ValuedEnum.getEnum(ValuedEnum.class, javaVersionEnum.getValue());
        assertEquals(javaVersionEnum, testEnum);
    }

    @Test
    public void testGetEnum_NullEnumClass() {
        assertThrows(IllegalArgumentException.class, () -> {
            ValuedEnum.getEnum(null, 1);
        });
    }

    @Test
    public void testGetEnum_InvalidValue() {
        assertNull(ValuedEnum.getEnum(ValuedEnum.class, 1000));
    }
}