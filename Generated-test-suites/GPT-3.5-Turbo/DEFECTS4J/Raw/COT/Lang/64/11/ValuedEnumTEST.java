
import org.junit.Test;
import static org.junit.Assert.*;

public class ValuedEnumTest {

    @Test
    public void testValuedEnumConstructor() {
    ValuedEnum enum1 = new ValuedEnumTest("enum1", 1);
    assertEquals("enum1", enum1.getName());
    assertEquals(1, enum1.getValue());
}

    @Test(expected=NullPointerException.class)
    public void testValuedEnumConstructor_NullName() {
        ValuedEnum enum1 = new ValuedEnumTest(null, 1);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testValuedEnumConstructor_NegativeValue() {
        ValuedEnum enum1 = new ValuedEnumTest("enum1", -1);
    }

    @Test
    public void testGetEnum_ValidValue() {
        ValuedEnum enum1 = ValuedEnum.getEnum(ValuedEnumTest.class, 1);
        assertNotNull(enum1);
        assertEquals("enum1", enum1.getName());
        assertEquals(1, enum1.getValue());
    }

    @Test
    public void testGetEnum_InvalidValue() {
        ValuedEnum enum1 = ValuedEnum.getEnum(ValuedEnumTest.class, 10);
        assertNull(enum1);
    }

    @Test
    public void testGetValue() {
        ValuedEnum enum1 = new ValuedEnumTest("enum1", 1);
        assertEquals(1, enum1.getValue());

        ValuedEnum enum2 = new ValuedEnumTest("enum2", 2);
        assertEquals(2, enum2.getValue());
    }

    @Test
    public void testCompareTo_LessThan() {
        ValuedEnum enum1 = new ValuedEnumTest("enum1", 1);
        ValuedEnum enum2 = new ValuedEnumTest("enum2", 2);
        assertTrue(enum1.compareTo(enum2) < 0);
    }

    @Test
    public void testCompareTo_GreaterThan() {
        ValuedEnum enum1 = new ValuedEnumTest("enum1", 1);
        ValuedEnum enum2 = new ValuedEnumTest("enum2", 2);
        assertTrue(enum2.compareTo(enum1) > 0);
    }

    @Test
    public void testCompareTo_Equal() {
        ValuedEnum enum1 = new ValuedEnumTest("enum1", 1);
        ValuedEnum enum2 = new ValuedEnumTest("enum2", 1);
        assertTrue(enum1.compareTo(enum2) == 0);
    }

    @Test
    public void testToString() {
        ValuedEnum enum1 = new ValuedEnumTest("enum1", 1);
        assertEquals("ValuedEnumTest[enum1=1]", enum1.toString());

        ValuedEnum enum2 = new ValuedEnumTest("enum2", 2);
        assertEquals("ValuedEnumTest[enum2=2]", enum2.toString());
    }
}