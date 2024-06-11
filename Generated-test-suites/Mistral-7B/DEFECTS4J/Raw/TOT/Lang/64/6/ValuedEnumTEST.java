import org.apache.commons.lang.enums.ValuedEnum;
import org.junit.Test;
import static org.junit.Assert.*;

public class ValuedEnumTest {

    @Test
    public void testGetEnum() {
        ValuedEnum javaVersion1_0 = ValuedEnum.getEnum("JavaVersionEnum", "JAVA1_0");
        assertEquals("JavaVersion[Java 1.0=100]", javaVersion1_0.toString());

        ValuedEnum javaVersion1_1 = ValuedEnum.getEnum("JavaVersionEnum", 110);
        assertEquals("JavaVersion[Java 1.1=110]", javaVersion1_1.toString());
    }

    @Test
    public void testGetEnum_InvalidEnum() {
        try {
            ValuedEnum invalidEnum = ValuedEnum.getEnum("JavaVersionEnum", 150);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testGetEnum_InvalidClassName() {
        try {
            ValuedEnum invalidEnum = ValuedEnum.getEnum("NonExistentEnum", "JAVA1_0");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testGetEnumMap() {
        List<ValuedEnum> javaVersionList = ValuedEnum.getEnumList(ValuedEnum.class);
        Map<String, ValuedEnum> javaVersionMap = new HashMap<>();
        for (ValuedEnum javaVersion : javaVersionList) {
            javaVersionMap.put(javaVersion.getName(), javaVersion);
        }
        assertEquals(javaVersionMap.get("JavaVersion[Java 1.0=100]"), ValuedEnum.getEnum("JavaVersionEnum", "JAVA1_0"));
    }

    @Test
    public void testGetEnumList() {
        List<ValuedEnum> javaVersionList = ValuedEnum.getEnumList(ValuedEnum.class);
        assertEquals(5, javaVersionList.size());
        for (ValuedEnum javaVersion : javaVersionList) {
            System.out.println(javaVersion.toString());
        }
    }
}