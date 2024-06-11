import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ValuedEnumTest {

    private JavaVersionEnum java10;
    private JavaVersionEnum java11;
    private JavaVersionEnum java12;
    private JavaVersionEnum java13;

    @Before
    public void setUp() {
        java10 = new JavaVersionEnum("Java 1.0", JavaVersionEnum.JAVA1_0_VALUE);
        java11 = new JavaVersionEnum("Java 1.1", JavaVersionEnum.JAVA1_1_VALUE);
        java12 = new JavaVersionEnum("Java 1.2", JavaVersionEnum.JAVA1_2_VALUE);
        java13 = new JavaVersionEnum("Java 1.3", JavaVersionEnum.JAVA1_3_VALUE);
    }

    @Test
    public void getValueTest() {
        assertEquals(100, java10.getValue());
        assertEquals(110, java11.getValue());
        assertEquals(120, java12.getValue());
        assertEquals(130, java13.getValue());
    }

    @Test
    public void getEnumByStringTest() {
        assertEquals(java10, JavaVersionEnum.getEnum("Java 1.0"));
        assertEquals(java11, JavaVersionEnum.getEnum("Java 1.1"));
        assertEquals(java12, JavaVersionEnum.getEnum("Java 1.2"));
        assertEquals(java13, JavaVersionEnum.getEnum("Java 1.3"));
    }

    @Test
    public void getEnumByValueTest() {
        assertEquals(java10, JavaVersionEnum.getEnum(100));
        assertEquals(java11, JavaVersionEnum.getEnum(110));
        assertEquals(java12, JavaVersionEnum.getEnum(120));
        assertEquals(java13, JavaVersionEnum.getEnum(130));
    }

    @Test
    public void getEnumMapTest() {
        assertNotNull(JavaVersionEnum.getEnumMap());
        assertEquals(4, JavaVersionEnum.getEnumMap().size());
    }

    @Test
    public void getEnumListTest() {
        assertNotNull(JavaVersionEnum.getEnumList());
        assertEquals(4, JavaVersionEnum.getEnumList().size());
    }

    @Test
    public void iteratorTest() {
        assertNotNull(JavaVersionEnum.iterator());
        assertTrue(JavaVersionEnum.iterator().hasNext());
        assertEquals(java10, JavaVersionEnum.iterator().next());
    }

    @Test
    public void compareToTest() {
        assertTrue(java10.compareTo(java11) < 0);
        assertTrue(java11.compareTo(java12) < 0);
        assertTrue(java12.compareTo(java13) < 0);
        assertTrue(java13.compareTo(java10) > 0);
        assertTrue(java10.compareTo(java10) == 0);
    }

    @Test(expected = NullPointerException.class)
    public void compareToNullTest() {
        java10.compareTo(null);
    }

    @Test(expected = ClassCastException.class)
    public void compareToDifferentTypeTest() {
        java10.compareTo("Java 1.0");
    }

    @Test
    public void toStringTest() {
        assertEquals("JavaVersion[Java 1.0=100]", java10.toString());
        assertEquals("JavaVersion[Java 1.1=110]", java11.toString());
        assertEquals("JavaVersion[Java 1.2=120]", java12.toString());
        assertEquals("JavaVersion[Java 1.3=130]", java13.toString());
    }

    @Test
    public void enumEqualsAndHashCodeTest() {
        assertEquals(java10, java10);
        assertEquals(java10.hashCode(), java10.hashCode());

        assertFalse(java10.equals(java11));
        assertFalse(java11.equals(java12));
        assertFalse(java12.equals(java13));
        assertFalse(java13.equals(java10));

        assertFalse(java10.equals(null));
        assertFalse(java10.equals("Java 1.0"));
    }
}