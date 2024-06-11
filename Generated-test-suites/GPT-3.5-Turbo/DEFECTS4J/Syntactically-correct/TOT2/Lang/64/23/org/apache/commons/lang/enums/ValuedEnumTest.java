import static org.junit.Assert.*;
import org.junit.Test;

public class ValuedEnumTest {

    @Test
    public void testGetValue() {
        JavaVersionEnum javaVersion = JavaVersionEnum.JAVA1_0;
        assertEquals(100, javaVersion.getValue());
    }

    @Test
    public void testGetEnum() {
        JavaVersionEnum javaVersion = JavaVersionEnum.getEnum("Java 1.1");
        assertNotNull(javaVersion);
    }

    @Test
    public void testGetEnumInvalid() {
        JavaVersionEnum javaVersion = JavaVersionEnum.getEnum("Invalid Java Version");
        assertNull(javaVersion);
    }

    @Test
    public void testCompareTo() {
        JavaVersionEnum javaVersion1 = JavaVersionEnum.JAVA1_0;
        JavaVersionEnum javaVersion2 = JavaVersionEnum.JAVA1_1;
        assertTrue(javaVersion1.compareTo(javaVersion2) < 0);
    }

    @Test
    public void testGetEnumByValue() {
        JavaVersionEnum javaVersion = JavaVersionEnum.getEnum(120);
        assertEquals("Java 1.2", javaVersion.getName());
    }

    @Test
    public void testGetEnumMap() {
        assertNotNull(JavaVersionEnum.getEnumMap());
    }

    @Test
    public void testGetEnumList() {
        assertNotNull(JavaVersionEnum.getEnumList());
    }

    @Test
    public void testIterator() {
        assertNotNull(JavaVersionEnum.iterator());
    }
}