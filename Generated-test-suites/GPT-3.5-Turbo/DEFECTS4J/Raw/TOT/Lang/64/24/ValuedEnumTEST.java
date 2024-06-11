import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Map;

import org.junit.Test;

public class ValuedEnumTest {

    @Test
    public void testGetValue() {
        // Typical use case
        JavaVersionEnum java1_0 = JavaVersionEnum.JAVA1_0;
        assertEquals(100, java1_0.getValue());

        // Edge case
        JavaVersionEnum java1_3 = JavaVersionEnum.JAVA1_3;
        assertEquals(130, java1_3.getValue());
    }

    @Test
    public void testCompareTo() {
        // Typical use case
        JavaVersionEnum java1_2 = JavaVersionEnum.JAVA1_2;
        JavaVersionEnum java1_3 = JavaVersionEnum.JAVA1_3;
        assertEquals(-1, java1_2.compareTo(java1_3));
        assertEquals(1, java1_3.compareTo(java1_2));

        // Edge case
        JavaVersionEnum java1_0 = JavaVersionEnum.JAVA1_0;
        assertEquals(0, java1_0.compareTo(java1_0));
    }

    @Test
    public void testGetEnum() {
        // Typical use case
        JavaVersionEnum java1_1 = JavaVersionEnum.getEnum(110);
        assertEquals(JavaVersionEnum.JAVA1_1, java1_1);

        // Edge case
        JavaVersionEnum java1_3 = JavaVersionEnum.getEnum(130);
        assertEquals(JavaVersionEnum.JAVA1_3, java1_3);

        // Error scenario
        JavaVersionEnum java1_4 = JavaVersionEnum.getEnum(140);
        assertNull(java1_4);
    }

    @Test
    public void testToString() {
        // Typical use case
        JavaVersionEnum java1_2 = JavaVersionEnum.JAVA1_2;
        assertEquals("JavaVersionEnum[Java 1.2=120]", java1_2.toString());

        // Edge case
        JavaVersionEnum java1_0 = JavaVersionEnum.JAVA1_0;
        assertEquals("JavaVersionEnum[Java 1.0=100]", java1_0.toString());
    }

    @Test
    public void testGetEnumList() {
        // Typical use case
        List<JavaVersionEnum> enumList = JavaVersionEnum.getEnumList();
        assertEquals(4, enumList.size());
        assertTrue(enumList.contains(JavaVersionEnum.JAVA1_0));
        assertTrue(enumList.contains(JavaVersionEnum.JAVA1_1));
        assertTrue(enumList.contains(JavaVersionEnum.JAVA1_2));
        assertTrue(enumList.contains(JavaVersionEnum.JAVA1_3));
    }

    @Test
    public void testGetEnumMap() {
        // Typical use case
        Map<String, JavaVersionEnum> enumMap = JavaVersionEnum.getEnumMap();
        assertEquals(4, enumMap.size());
        assertEquals(JavaVersionEnum.JAVA1_0, enumMap.get("Java 1.0"));
        assertEquals(JavaVersionEnum.JAVA1_1, enumMap.get("Java 1.1"));
        assertEquals(JavaVersionEnum.JAVA1_2, enumMap.get("Java 1.2"));
        assertEquals(JavaVersionEnum.JAVA1_3, enumMap.get("Java 1.3"));
    }
}