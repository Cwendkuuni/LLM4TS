import org.apache.commons.lang.enums.ValuedEnum;
import org.junit.Test;
import static org.junit.Assert.*;

public class ValuedEnumTest {

    @Test
    public void testGetEnum() {
        ValuedEnum java1_0 = ValuedEnum.getEnum("JavaVersionEnum", "JAVA1_0");
        assertNotNull(java1_0);
        assertEquals("Java 1.0", java1_0.getName());
        assertEquals(100, java1_0.getValue());

        ValuedEnum java1_1 = ValuedEnum.getEnum("JavaVersionEnum", "JAVA1_1");
        assertNotNull(java1_1);
        assertEquals("Java 1.1", java1_1.getName());
        assertEquals(110, java1_1.getValue());
    }

    @Test
    public void testGetEnumInvalidName() {
        ValuedEnum javaVersion = ValuedEnum.getEnum("JavaVersionEnum", "InvalidName");
        assertNull(javaVersion);
    }

    @Test
    public void testGetEnumInvalidClass() {
        try {
            ValuedEnum javaVersion = ValuedEnum.getEnum(Object.class.getName(), "JAVA1_0");
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testGetEnumInvalidValue() {
        try {
            ValuedEnum javaVersion = ValuedEnum.getEnum("JavaVersionEnum", -1);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCompareTo() {
        ValuedEnum java1_0 = new ValuedEnum("JavaVersionEnum", "Java 1.0", 100);
        ValuedEnum java1_1 = new ValuedEnum("JavaVersionEnum", "Java 1.1", 110);
        ValuedEnum java1_2 = new ValuedEnum("JavaVersionEnum", "Java 1.2", 120);

        assertEquals(0, java1_0.compareTo(java1_0));
        assertEquals(-1, java1_0.compareTo(java1_1));
        assertEquals(1, java1_1.compareTo(java1_0));
        assertEquals(1, java1_1.compareTo(java1_1));
        assertEquals(-1, java1_1.compareTo(java1_2));
        assertEquals(1, java1_2.compareTo(java1_1));
    }

    @Test
    public void testCompareToDifferentClassLoader() throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        ValuedEnum java1_0 = new ValuedEnum("JavaVersionEnum", "Java 1.0", 100);
        String otherClassName = "Other.JavaVersionEnum";
        Class<?> otherClass = Class.forName(otherClassName);
        Object otherEnum = otherClass.getDeclaredConstructor().newInstance();
        Method compareToMethod = JavaVersionEnum.class.getMethod("compareTo", Object.class);
        int result = (Integer) compareToMethod.invoke(java1_0, otherEnum);
        assertEquals(-1, result);
    }
}