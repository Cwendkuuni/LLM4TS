import org.junit.Test;
import static org.junit.Assert.*;

public class ValuedEnumTest {

    @Test
    public void testGetValue() {
        JavaVersionEnum javaVersion = JavaVersionEnum.JAVA1_0;
        assertEquals(100, javaVersion.getValue());
    }

    @Test
    public void testGetEnumByClassName() {
        JavaVersionEnum javaVersion = JavaVersionEnum.getEnum("Java 1.0");
        assertNotNull(javaVersion);
        assertEquals(100, javaVersion.getValue());
    }

    @Test
    public void testGetEnumByValue() {
        JavaVersionEnum javaVersion = JavaVersionEnum.getEnum(100);
        assertNotNull(javaVersion);
        assertEquals("Java 1.0", javaVersion.getName());
    }

    @Test
    public void testGetEnumMap() {
        assertNotNull(JavaVersionEnum.getEnumMap());
        assertEquals(4, JavaVersionEnum.getEnumMap().size());
    }

    @Test
    public void testGetEnumList() {
        assertNotNull(JavaVersionEnum.getEnumList());
        assertEquals(4, JavaVersionEnum.getEnumList().size());
    }

    @Test
    public void testIterator() {
        assertNotNull(JavaVersionEnum.iterator());
    }
}