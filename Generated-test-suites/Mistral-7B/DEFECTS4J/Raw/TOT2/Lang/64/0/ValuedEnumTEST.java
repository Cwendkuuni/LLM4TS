import org.junit.Test;
import static org.junit.Assert.*;

public class ValuedEnumTest {

    @Test
    public void testGetValue() {
        ValuedEnum valueEnum = ValuedEnum.JAVA1_0;
        int expectedValue = 100;
        int actualValue = valueEnum.getValue();
        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testCompareTo() {
        ValuedEnum enum1 = ValuedEnum.JAVA1_0;
        ValuedEnum enum2 = ValuedEnum.JAVA1_1;
        int expectedComparisonResult = -1;
        int actualComparisonResult = enum1.compareTo(enum2);
        assertEquals(expectedComparisonResult, actualComparisonResult);
    }

    @Test
    public void testToString() {
        ValuedEnum javaVersionEnum = ValuedEnum.JAVA1_0;
        String expectedToStringValue = "JavaVersion[Java 1.0=100]";
        String actualToStringValue = javaVersionEnum.toString();
        assertEquals(expectedToStringValue, actualToStringValue);
    }
}