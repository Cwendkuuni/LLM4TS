import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.apache.commons.lang.enums.ValuedEnum;
import org.junit.Test;

public class ValuedEnumTest {

    @Test
    public void testGetValue() {
        // Typical use case
        JavaVersionEnum javaVersionEnum = JavaVersionEnum.JAVA1_0;
        int expectedValue = 100;
        int actualValue = javaVersionEnum.getValue();
        assertEquals(expectedValue, actualValue);

        // Edge case
        javaVersionEnum = JavaVersionEnum.JAVA1_3;
        expectedValue = 130;
        actualValue = javaVersionEnum.getValue();
        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testGetEnumByValue() {
        // Typical use case
        JavaVersionEnum expectedEnum = JavaVersionEnum.JAVA1_0;
        JavaVersionEnum actualEnum = (JavaVersionEnum) ValuedEnum.getEnum(JavaVersionEnum.class, 100);
        assertEquals(expectedEnum, actualEnum);

        // Edge case
        expectedEnum = JavaVersionEnum.JAVA1_3;
        actualEnum = (JavaVersionEnum) ValuedEnum.getEnum(JavaVersionEnum.class, 130);
        assertEquals(expectedEnum, actualEnum);

        // Error scenario
        actualEnum = (JavaVersionEnum) ValuedEnum.getEnum(JavaVersionEnum.class, 200);
        assertNull(actualEnum);
    }

    @Test
    public void testCompareTo() {
        // Typical use case
        JavaVersionEnum javaVersionEnum1 = JavaVersionEnum.JAVA1_0;
        JavaVersionEnum javaVersionEnum2 = JavaVersionEnum.JAVA1_1;
        int expectedResult = -10;
        int actualResult = javaVersionEnum1.compareTo(javaVersionEnum2);
        assertEquals(expectedResult, actualResult);

        // Edge case
        javaVersionEnum1 = JavaVersionEnum.JAVA1_3;
        javaVersionEnum2 = JavaVersionEnum.JAVA1_2;
        expectedResult = 10;
        actualResult = javaVersionEnum1.compareTo(javaVersionEnum2);
        assertEquals(expectedResult, actualResult);

        // Error scenario
        try {
            javaVersionEnum1.compareTo("STRING");  // Comparing with a different object type
        } catch (ClassCastException e) {
            // Exception is expected
        }
    }

    @Test
    public void testToString() {
        // Typical use case
        JavaVersionEnum javaVersionEnum = JavaVersionEnum.JAVA1_0;
        String expectedString = "JavaVersionEnum[Java 1.0=100]";
        String actualString = javaVersionEnum.toString();
        assertEquals(expectedString, actualString);

        // Edge case
        javaVersionEnum = JavaVersionEnum.JAVA1_3;
        expectedString = "JavaVersionEnum[Java 1.3=130]";
        actualString = javaVersionEnum.toString();
        assertEquals(expectedString, actualString);
    }
}