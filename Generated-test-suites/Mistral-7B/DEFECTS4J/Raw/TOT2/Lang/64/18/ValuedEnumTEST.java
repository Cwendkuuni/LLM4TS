import org.junit.Test;
import org.junit.Assert.*;
import org.apache.commons.lang.enums.ValuedEnum;
import org.apache.commons.lang.enums.JavaVersionEnum;

public class ValuedEnumTest {

    @Test
    public void testValuedEnumConstructor() {
        ValuedEnum testEnum = new ValuedEnum("TestEnum", 1);
        assertNotNull(testEnum);
    }

    @Test
    public void testgetValue() {
        JavaVersionEnum javaVersion1 = JavaVersionEnum.JAVA1_0;
        int value = javaVersion1.getValue();
        assertEquals(JavaVersionEnum.JAVA1_0_VALUE, value);
    }

    @Test
    public void testcompareTo() {
        JavaVersionEnum javaVersion1 = JavaVersionEnum.JAVA1_0;
        JavaVersionEnum javaVersion2 = JavaVersionEnum.JAVA1_1;
        int comparisonResult = javaVersion1.compareTo(javaVersion2);
        assertLessThan(0, comparisonResult);
    }

    @Test
    public void testtoString() {
        JavaVersionEnum javaVersionEnum = JavaVersionEnum.JAVA1_0;
        String expectedString = "JavaVersion[Java 1.0=100]";
        String actualString = javaVersionEnum.toString();
        assertEquals(expectedString, actualString);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testgetEnumWithNullEnumClass() {
        Class enumClass = null;
        int value = 1;
        Enum result = ValuedEnum.getEnum(enumClass, value);
        fail("Expected IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testgetEnumWithNullValue() {
        Class enumClass = JavaVersionEnum.class;
        int value = null;
        Enum result = ValuedEnum.getEnum(enumClass, value);
        fail("Expected IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testgetEnumWithIncorrectEnumClass() {
        Class enumClass = String.class;
        int value = JavaVersionEnum.JAVA1_0_VALUE;
        Enum result = ValuedEnum.getEnum(enumClass, value);
        fail("Expected IllegalArgumentException");
    }

    @Test
    public void testgetEnumWithEmptyEnumList() {
        Class enumClass = JavaVersionEnum.class;
        int value = JavaVersionEnum.JAVA1_0_VALUE;
        Enum result = ValuedEnum.getEnum(enumClass, value);
        assertNull(result);
    }

    @Test
    public void testgetEnumWithSubclass() {
        Class subclassEnum = JavaVersionSubEnum.class;
        int value = JavaVersionSubEnum.JAVA1_0_VALUE;
        Enum result = ValuedEnum.getEnum(subclassEnum, value);
        assertNotNull(result);
        assertEquals(JavaVersionSubEnum.JAVA1_0, result);
    }

    @Test
    public void testgetEnumWithSameInstance() {
        JavaVersionEnum javaVersion1 = JavaVersionEnum.JAVA1_0;
        int comparisonResult = javaVersion1.compareTo(javaVersion1);
        assertEquals(0, comparisonResult);
    }

    @Test(expected = NullPointerException.class)
    public void testcompareToWithNullObject() {
        JavaVersionEnum javaVersion1 = JavaVersionEnum.JAVA1_0;
        Object nullObj = null;
        int comparisonResult = javaVersion1.compareTo(nullObj);
        fail("Expected NullPointerException");
    }
}