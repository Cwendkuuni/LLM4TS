
import org.junit.Test;
import static org.junit.Assert.*;

public class ValuedEnumTest {

    @Test
    public void testConstructor() {
        String name = "TestEnum";
        int value = 10;
        ValuedEnum testedEnum = new ValuedEnum(name, value);
        assertEquals(name, testedEnum.getName());
        assertEquals(value, testedEnum.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullName() {
        String name = null;
        int value = 10;
        try {
            ValuedEnum testedEnum = new ValuedEnum(name, value);
        } catch (IllegalArgumentException e) {
            //Test passes if the exception is thrown
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeValue() {
        String name = "TestEnum";
        int value = -1;
        try {
            ValuedEnum testedEnum = new ValuedEnum(name, value);
        } catch (IllegalArgumentException e) {
            //Test passes if the exception is thrown
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithValueAlreadyUsed() {
        String name1 = "TestEnum1";
        int value1 = 10;
        String name2 = "TestEnum2";
        int value2 = value1;
        try {
            ValuedEnum enum1 = new ValuedEnum(name1, value1);
            ValuedEnum enum2 = new ValuedEnum(name2, value2);
        } catch (IllegalArgumentException e) {
            //Test passes if the exception is thrown
        }
    }

    @Test
    public void testGetEnumWithCorrectClassAndValue() {
        Class enumClass = ValuedEnum.class;
        int value = 1;
        ValuedEnum expectedEnum = ValuedEnum.getValue1(); // Replace with the correct enum and value
        ValuedEnum actualEnum = (ValuedEnum) ValuedEnum.getEnum(enumClass, value);
        assertEquals(expectedEnum, actualEnum);
    }

    // Add other test cases for getEnum() here

    // Add test cases for other methods here

    // ...
}
