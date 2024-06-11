import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ValuedEnumTest {
    @Test
    public void testGetEnum() {
        assertEquals(ValuedEnum.JAVA1_0, ValuedEnum.getEnum(ValuedEnum.class, 100));
        assertEquals(ValuedEnum.JAVA1_1, ValuedEnum.getEnum(ValuedEnum.class, 110));
        assertEquals(ValuedEnum.JAVA1_2, ValuedEnum.getEnum(ValuedEnum.class, 120));
        assertEquals(ValuedEnum.JAVA1_3, ValuedEnum.getEnum(ValuedEnum.class, 130));
        assertNull(ValuedEnum.getEnum(ValuedEnum.class, 200));
    }

    @Test
    public void testGetValue() {
        assertEquals(100, ValuedEnum.JAVA1_0.getValue());
        assertEquals(110, ValuedEnum.JAVA1_1.getValue());
        assertEquals(120, ValuedEnum.JAVA1_2.getValue());
        assertEquals(130, ValuedEnum.JAVA1_3.getValue());
    }

    @Test
    public void testCompareTo() {
        assertEquals(-10, ValuedEnum.JAVA1_0.compareTo(ValuedEnum.JAVA1_1));
        assertEquals(0, ValuedEnum.JAVA1_1.compareTo(ValuedEnum.JAVA1_1));
        assertEquals(10, ValuedEnum.JAVA1_1.compareTo(ValuedEnum.JAVA1_0));
    }

    @Test
    public void testToString() {
        assertEquals("ValuedEnum[JAVA1_0=100]", ValuedEnum.JAVA1_0.toString());
        assertEquals("ValuedEnum[JAVA1_1=110]", ValuedEnum.JAVA1_1.toString());
        assertEquals("ValuedEnum[JAVA1_2=120]", ValuedEnum.JAVA1_2.toString());
        assertEquals("ValuedEnum[JAVA1_3=130]", ValuedEnum.JAVA1_3.toString());
    }
}