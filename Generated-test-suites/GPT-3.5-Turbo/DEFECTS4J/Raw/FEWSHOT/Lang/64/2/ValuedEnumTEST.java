import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ValuedEnumTest {
    @Test
    public void testGetValue() {
        JavaVersionEnum java1_0 = JavaVersionEnum.getEnum(JavaVersionEnum.JAVA1_0_VALUE);
        assertEquals(100, java1_0.getValue());

        JavaVersionEnum java1_1 = JavaVersionEnum.getEnum(JavaVersionEnum.JAVA1_1_VALUE);
        assertEquals(110, java1_1.getValue());

        JavaVersionEnum java1_2 = JavaVersionEnum.getEnum(JavaVersionEnum.JAVA1_2_VALUE);
        assertEquals(120, java1_2.getValue());

        JavaVersionEnum java1_3 = JavaVersionEnum.getEnum(JavaVersionEnum.JAVA1_3_VALUE);
        assertEquals(130, java1_3.getValue());
    }

    @Test
    public void testCompareTo() {
        JavaVersionEnum java1_0 = JavaVersionEnum.getEnum(JavaVersionEnum.JAVA1_0_VALUE);
        JavaVersionEnum java1_1 = JavaVersionEnum.getEnum(JavaVersionEnum.JAVA1_1_VALUE);
        JavaVersionEnum java1_2 = JavaVersionEnum.getEnum(JavaVersionEnum.JAVA1_2_VALUE);
        JavaVersionEnum java1_3 = JavaVersionEnum.getEnum(JavaVersionEnum.JAVA1_3_VALUE);

        assertEquals(-10, java1_0.compareTo(java1_1));
        assertEquals(-10, java1_1.compareTo(java1_2));
        assertEquals(-10, java1_2.compareTo(java1_3));
        assertEquals(0, java1_0.compareTo(java1_0));
        assertEquals(0, java1_1.compareTo(java1_1));
        assertEquals(0, java1_2.compareTo(java1_2));
        assertEquals(0, java1_3.compareTo(java1_3));
    }

    @Test
    public void testToString() {
        JavaVersionEnum java1_0 = JavaVersionEnum.getEnum(JavaVersionEnum.JAVA1_0_VALUE);
        assertEquals("JavaVersion[Java 1.0=100]", java1_0.toString());

        JavaVersionEnum java1_1 = JavaVersionEnum.getEnum(JavaVersionEnum.JAVA1_1_VALUE);
        assertEquals("JavaVersion[Java 1.1=110]", java1_1.toString());

        JavaVersionEnum java1_2 = JavaVersionEnum.getEnum(JavaVersionEnum.JAVA1_2_VALUE);
        assertEquals("JavaVersion[Java 1.2=120]", java1_2.toString());

        JavaVersionEnum java1_3 = JavaVersionEnum.getEnum(JavaVersionEnum.JAVA1_3_VALUE);
        assertEquals("JavaVersion[Java 1.3=130]", java1_3.toString());
    }
}