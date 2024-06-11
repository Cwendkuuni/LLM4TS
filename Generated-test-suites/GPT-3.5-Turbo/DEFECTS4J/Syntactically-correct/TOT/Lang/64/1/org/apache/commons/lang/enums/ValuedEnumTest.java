import org.junit.Test;

public class ValuedEnumTest {

    @Test
    public void testGetValue_NormalCase() {
        // This is a typical use case where we get the value of an enum item
        JavaVersionEnum javaVersion = JavaVersionEnum.JAVA1_0;
        int value = javaVersion.getValue();
        // Assert that the value returned is the expected value
        assertEquals(100, value);
    }

    @Test
    public void testGetValue_EdgeCase() {
        // This is an edge case where we get the value of the first enum item
        JavaVersionEnum javaVersion = JavaVersionEnum.values()[0];
        int value = javaVersion.getValue();
        // Assert that the value returned is the expected value
        assertEquals(100, value);
    }

    @Test
    public void testGetValue_NullEnum() {
        // This is an error scenario where we try to get the value of a null enum item
        JavaVersionEnum javaVersion = null;
        // Assert that calling getValue() on a null enum item throws NullPointerException
        assertThrows(NullPointerException.class, () -> {
            javaVersion.getValue();
        });
    }

    @Test
    public void testGetEnum_NormalCase_StringParam() {
        // This is a typical use case where we get an enum item by its name
        JavaVersionEnum javaVersion = JavaVersionEnum.getEnum("Java 1.0");
        // Assert that the enum item returned is not null
        assertNotNull(javaVersion);
        // Assert that the name of the enum item is the expected name
        assertEquals("Java 1.0", javaVersion.getName());
        // Assert that the value of the enum item is the expected value
        assertEquals(100, javaVersion.getValue());
    }

    @Test
    public void testGetEnum_NormalCase_IntParam() {
        // This is a typical use case where we get an enum item by its value
        JavaVersionEnum javaVersion = JavaVersionEnum.getEnum(130);
        // Assert that the enum item returned is not null
        assertNotNull(javaVersion);
        // Assert that the name of the enum item is the expected name
        assertEquals("Java 1.3", javaVersion.getName());
        // Assert that the value of the enum item is the expected value
        assertEquals(130, javaVersion.getValue());
    }

    @Test
    public void testGetEnum_NonExistentEnum() {
        // This is an error scenario where we try to get a non-existent enum item
        JavaVersionEnum javaVersion = JavaVersionEnum.getEnum("Java 2.0");
        // Assert that calling getEnum() with a non-existent name returns null
        assertNull(javaVersion);
    }

    @Test
    public void testGetEnum_NullEnumClass() {
        // This is an error scenario where we try to get an enum item with a null enum class
        Class enumClass = null;
        // Assert that calling getEnum() with a null enum class throws IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            JavaVersionEnum.getEnum(enumClass, "Java 1.0");
        });
    }
}