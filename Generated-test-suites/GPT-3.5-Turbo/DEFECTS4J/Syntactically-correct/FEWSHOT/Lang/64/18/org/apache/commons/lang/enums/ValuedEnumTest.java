import org.junit.Test;

public class ValuedEnumTest {

    @Test
    public void testGetEnum() {
        assertEquals(JavaVersionEnum.JAVA1_0, ValuedEnum.getEnum(JavaVersionEnum.class, 100));
        assertEquals(JavaVersionEnum.JAVA1_1, ValuedEnum.getEnum(JavaVersionEnum.class, 110));
        assertEquals(JavaVersionEnum.JAVA1_2, ValuedEnum.getEnum(JavaVersionEnum.class, 120));
        assertEquals(JavaVersionEnum.JAVA1_3, ValuedEnum.getEnum(JavaVersionEnum.class, 130));
        assertNull(ValuedEnum.getEnum(JavaVersionEnum.class, 999));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnum_NullClass() {
        ValuedEnum.getEnum(null, 100);
    }

    @Test
    public void testGetValue() {
        assertEquals(100, JavaVersionEnum.JAVA1_0.getValue());
        assertEquals(110, JavaVersionEnum.JAVA1_1.getValue());
        assertEquals(120, JavaVersionEnum.JAVA1_2.getValue());
        assertEquals(130, JavaVersionEnum.JAVA1_3.getValue());
    }

    @Test
    public void testCompareTo() {
        assertTrue(JavaVersionEnum.JAVA1_0.compareTo(JavaVersionEnum.JAVA1_0) == 0);
        assertTrue(JavaVersionEnum.JAVA1_0.compareTo(JavaVersionEnum.JAVA1_1) < 0);
        assertTrue(JavaVersionEnum.JAVA1_1.compareTo(JavaVersionEnum.JAVA1_0) > 0);
    }

    @Test(expected = ClassCastException.class)
    public void testCompareTo_DifferentType() {
        JavaVersionEnum.JAVA1_0.compareTo(Util.EMPTY_STRING_ARRAY);
    }

    @Test(expected = NullPointerException.class)
    public void testCompareTo_NullInput() {
        JavaVersionEnum.JAVA1_0.compareTo(null);
    }

    @Test
    public void testToString() {
        assertEquals("JavaVersionEnum[Java 1.0=100]", JavaVersionEnum.JAVA1_0.toString());
        assertEquals("JavaVersionEnum[Java 1.1=110]", JavaVersionEnum.JAVA1_1.toString());
        assertEquals("JavaVersionEnum[Java 1.2=120]", JavaVersionEnum.JAVA1_2.toString());
        assertEquals("JavaVersionEnum[Java 1.3=130]", JavaVersionEnum.JAVA1_3.toString());
    }
}