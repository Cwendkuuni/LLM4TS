import org.junit.Test;
import static org.junit.Assert.*;

public class ValuedEnumTest {

    @Test
    public void testGetName() {
        JavaVersionEnum java10 = JavaVersionEnum.JAVA1_0;
        assertEquals("Java 1.0", java10.getName());
    }

    @Test
    public void testGetValue() {
        JavaVersionEnum java10 = JavaVersionEnum.JAVA1_0;
        assertEquals(100, java10.getValue());
    }

    @Test
    public void testGetEnumByName() {
        JavaVersionEnum java10 = JavaVersionEnum.getEnum("Java 1.0");
        assertEquals(JavaVersionEnum.JAVA1_0, java10);
    }

    @Test
    public void testGetEnumByValue() {
        JavaVersionEnum java10 = JavaVersionEnum.getEnum(100);
        assertEquals(JavaVersionEnum.JAVA1_0, java10);
    }

    @Test
    public void testGetInvalidEnumByName() {
        JavaVersionEnum invalidEnum = JavaVersionEnum.getEnum("Invalid Enum");
        assertNull(invalidEnum);
    }

    @Test
    public void testGetInvalidEnumByValue() {
        JavaVersionEnum invalidEnum = JavaVersionEnum.getEnum(999);
        assertNull(invalidEnum);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumWithNullClass() {
        Enum.getEnum(null, 100);
    }

    @Test
    public void testCompareTo() {
        JavaVersionEnum java10 = JavaVersionEnum.JAVA1_0;
        JavaVersionEnum java20 = JavaVersionEnum.JAVA1_1;
        JavaVersionEnum java30 = JavaVersionEnum.JAVA1_2;

        assertTrue(java10.compareTo(java20) < 0);
        assertTrue(java30.compareTo(java20) > 0);
        assertTrue(java20.compareTo(java20) == 0);
    }
}