import static org.junit.Assert.*;
import org.junit.Test;

public class ValuedEnumTest {

    @Test
    public void testGetValue() {
        JavaVersionEnum java1_0 = JavaVersionEnum.JAVA1_0;
        assertEquals(100, java1_0.getValue());

        JavaVersionEnum java1_1 = JavaVersionEnum.JAVA1_1;
        assertEquals(110, java1_1.getValue());

        JavaVersionEnum java1_2 = JavaVersionEnum.JAVA1_2;
        assertEquals(120, java1_2.getValue());

        JavaVersionEnum java1_3 = JavaVersionEnum.JAVA1_3;
        assertEquals(130, java1_3.getValue());
    }

    @Test
    public void testGetEnum() {
        JavaVersionEnum java1_0 = JavaVersionEnum.getEnum("Java 1.0");
        assertEquals(JavaVersionEnum.JAVA1_0, java1_0);

        JavaVersionEnum java1_1 = JavaVersionEnum.getEnum("Java 1.1");
        assertEquals(JavaVersionEnum.JAVA1_1, java1_1);

        JavaVersionEnum java1_2 = JavaVersionEnum.getEnum("Java 1.2");
        assertEquals(JavaVersionEnum.JAVA1_2, java1_2);

        JavaVersionEnum java1_3 = JavaVersionEnum.getEnum("Java 1.3");
        assertEquals(JavaVersionEnum.JAVA1_3, java1_3);
    }
    
    @Test
    public void testCompareTo() {
        JavaVersionEnum java1_0 = JavaVersionEnum.JAVA1_0;
        JavaVersionEnum java1_1 = JavaVersionEnum.JAVA1_1;
        JavaVersionEnum java1_2 = JavaVersionEnum.JAVA1_2;
        JavaVersionEnum java1_3 = JavaVersionEnum.JAVA1_3;

        assertTrue(java1_0.compareTo(java1_1) < 0);
        assertTrue(java1_1.compareTo(java1_2) < 0);
        assertTrue(java1_2.compareTo(java1_3) < 0);
        assertTrue(java1_3.compareTo(java1_0) > 0);
        assertTrue(java1_0.compareTo(java1_0) == 0);
    }

}