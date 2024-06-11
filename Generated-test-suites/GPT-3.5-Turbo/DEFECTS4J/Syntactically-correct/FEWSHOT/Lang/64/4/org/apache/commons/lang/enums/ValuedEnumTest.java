import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class ValuedEnumTest {
    @Test
    public void testGetValue() {
        JavaVersionEnum javaVersion = JavaVersionEnum.JAVA1_0;
        assertEquals(100, javaVersion.getValue());

        javaVersion = JavaVersionEnum.JAVA1_1;
        assertEquals(110, javaVersion.getValue());

        javaVersion = JavaVersionEnum.JAVA1_2;
        assertEquals(120, javaVersion.getValue());

        javaVersion = JavaVersionEnum.JAVA1_3;
        assertEquals(130, javaVersion.getValue());
    }

    @Test
    public void testGetEnum() {
        ValuedEnum javaVersion = JavaVersionEnum.getEnum(100);
        assertEquals(JavaVersionEnum.JAVA1_0, javaVersion);

        javaVersion = JavaVersionEnum.getEnum(110);
        assertEquals(JavaVersionEnum.JAVA1_1, javaVersion);

        javaVersion = JavaVersionEnum.getEnum(120);
        assertEquals(JavaVersionEnum.JAVA1_2, javaVersion);

        javaVersion = JavaVersionEnum.getEnum(130);
        assertEquals(JavaVersionEnum.JAVA1_3, javaVersion);

        javaVersion = JavaVersionEnum.getEnum(200);
        assertNull(javaVersion);
    }

    @Test
    public void testCompareTo() {
        JavaVersionEnum javaVersion1 = JavaVersionEnum.JAVA1_0;
        JavaVersionEnum javaVersion2 = JavaVersionEnum.JAVA1_1;

        assertTrue(javaVersion1.compareTo(javaVersion2) < 0);
        assertTrue(javaVersion2.compareTo(javaVersion1) > 0);
        assertEquals(0, javaVersion1.compareTo(javaVersion1));
    }

    @Test
    public void testToString() {
        JavaVersionEnum javaVersion = JavaVersionEnum.JAVA1_0;
        assertEquals("JavaVersion[Java 1.0=100]", javaVersion.toString());

        javaVersion = JavaVersionEnum.JAVA1_1;
        assertEquals("JavaVersion[Java 1.1=110]", javaVersion.toString());

        javaVersion = JavaVersionEnum.JAVA1_2;
        assertEquals("JavaVersion[Java 1.2=120]", javaVersion.toString());

        javaVersion = JavaVersionEnum.JAVA1_3;
        assertEquals("JavaVersion[Java 1.3=130]", javaVersion.toString());
    }
}