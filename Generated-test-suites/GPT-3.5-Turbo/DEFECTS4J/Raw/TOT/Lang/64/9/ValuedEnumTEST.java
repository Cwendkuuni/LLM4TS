
import org.junit.Test;
import static org.junit.Assert.*;

public class ValuedEnumTest {

    @Test
    public void testGetValue() {
        JavaVersionEnum javaVersionEnum = JavaVersionEnum.JAVA1_0;
        assertEquals(100, javaVersionEnum.getValue());

        javaVersionEnum = JavaVersionEnum.JAVA1_1;
        assertEquals(110, javaVersionEnum.getValue());

        javaVersionEnum = JavaVersionEnum.JAVA1_2;
        assertEquals(120, javaVersionEnum.getValue());

        javaVersionEnum = JavaVersionEnum.JAVA1_3;
        assertEquals(130, javaVersionEnum.getValue());
    }

    @Test
    public void testGetEnumByString() {
        JavaVersionEnum javaVersionEnum = JavaVersionEnum.getEnum("Java 1.0");
        assertEquals(JavaVersionEnum.JAVA1_0, javaVersionEnum);

        javaVersionEnum = JavaVersionEnum.getEnum("Java 1.1");
        assertEquals(JavaVersionEnum.JAVA1_1, javaVersionEnum);

        javaVersionEnum = JavaVersionEnum.getEnum("Java 1.2");
        assertEquals(JavaVersionEnum.JAVA1_2, javaVersionEnum);

        javaVersionEnum = JavaVersionEnum.getEnum("Java 1.3");
        assertEquals(JavaVersionEnum.JAVA1_3, javaVersionEnum);
    }

    @Test
    public void testGetEnumByInt() {
        JavaVersionEnum javaVersionEnum = JavaVersionEnum.getEnum(100);
        assertEquals(JavaVersionEnum.JAVA1_0, javaVersionEnum);

        javaVersionEnum = JavaVersionEnum.getEnum(110);
        assertEquals(JavaVersionEnum.JAVA1_1, javaVersionEnum);

        javaVersionEnum = JavaVersionEnum.getEnum(120);
        assertEquals(JavaVersionEnum.JAVA1_2, javaVersionEnum);

        javaVersionEnum = JavaVersionEnum.getEnum(130);
        assertEquals(JavaVersionEnum.JAVA1_3, javaVersionEnum);
    }

    @Test
    public void testGetNonExistingEnumByString() {
        JavaVersionEnum javaVersionEnum = JavaVersionEnum.getEnum("Java 6.0");
        assertNull(javaVersionEnum);
    }

    @Test
    public void testGetNonExistingEnumByInt() {
        JavaVersionEnum javaVersionEnum = JavaVersionEnum.getEnum(200);
        assertNull(javaVersionEnum);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumWithNullClass() {
        JavaVersionEnum.getEnum(null, 100);
    }

}
