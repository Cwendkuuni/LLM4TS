import org.junit.Test;
import static org.junit.Assert.*;

public class ValuedEnumTest {

    @Test
    public void testValuedEnumConstructor() {
        String name = "TestEnum";
        int value = 10;
        ValuedEnum testEnum = new ValuedEnum(name, value);
        assertEquals(name, testEnum.getName());
        assertEquals(value, testEnum.getValue());
    }

    @Test
    public void testGetEnumMethod() throws Exception {
        Class<ValuedEnum> enumClass = ValuedEnum.class;
        ValuedEnum testEnum = new ValuedEnum("TestEnum", 10);
        ValuedEnum retrievedEnum = (ValuedEnum) ValuedEnum.getEnum(enumClass, 10);
        assertEquals(testEnum, retrievedEnum);
    }

    @Test
    public void testGetValueMethod() {
        ValuedEnum testEnum = new ValuedEnum("TestEnum", 10);
        assertEquals(10, testEnum.getValue());
    }

    @Test
    public void testCompareToMethod() {
        ValuedEnum enum1 = new ValuedEnum("Enum1", 1);
        ValuedEnum enum2 = new ValuedEnum("Enum2", 2);
        assertEquals(-1, enum1.compareTo(enum2));
        assertEquals(0, enum1.compareTo(enum1));
        assertEquals(1, enum2.compareTo(enum1));
    }

    @Test
    public void testToStringMethod() {
        ValuedEnum testEnum = new ValuedEnum("TestEnum", 10);
        assertEquals("TestEnum[name=TestEnum, value=10]", testEnum.toString());
    }

    // Add edge case and error scenario tests here

}