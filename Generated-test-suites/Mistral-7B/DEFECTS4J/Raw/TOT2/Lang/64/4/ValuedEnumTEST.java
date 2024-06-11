import org.junit.Test;
import org.junit.Assert.*;
import org.apache.commons.lang.enums.ValuedEnum;

public class ValuedEnumTest {

    @Test
    public void testConstructor() {
        ValuedEnum enum1 = new ValuedEnum("TestEnum1", 1);
        assertEquals("TestEnum1", enum1.getName());
        assertEquals(1, enum1.getValue());
    }

    @Test
    public void testGetEnumCorrectValue() {
        ValuedEnum enum1 = new ValuedEnum("TestEnum1", 1);
        ValuedEnum enum2 = ValuedEnum.getEnum(ValuedEnum.class, 1);
        assertEquals(enum1, enum2);
    }

    @Test(expected = NullPointerException.class)
    public void testGetEnumIncorrectValue() {
        ValuedEnum enum1 = new ValuedEnum("TestEnum1", 1);
        ValuedEnum enum2 = ValuedEnum.getEnum(ValuedEnum.class, 2);
        assertNull(enum2);
    }

    @Test
    public void testGetValue() {
        ValuedEnum enum1 = new ValuedEnum("TestEnum1", 1);
        assertEquals(1, enum1.getValue());
    }

    @Test
    public void testCompareToEqual() {
        ValuedEnum enum1 = new ValuedEnum("TestEnum1", 1);
        ValuedEnum enum2 = new ValuedEnum("TestEnum1", 1);
        assertEquals(0, enum1.compareTo(enum2));
    }

    @Test
    public void testCompareToDifferent() {
        ValuedEnum enum1 = new ValuedEnum("TestEnum1", 1);
        ValuedEnum enum2 = new ValuedEnum("TestEnum2", 2);
        assertEquals(-1, enum1.compareTo(enum2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumNullClass() {
        ValuedEnum.getEnum(null, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumNullValue() {
        ValuedEnum.getEnum(ValuedEnum.class, null);
    }

    @Test(expected = NullPointerException.class)
    public void testGetEnumNonExistentValue() {
        ValuedEnum.getEnum(ValuedEnum.class, 3);
    }

    @Test(expected = ClassCastException.class)
    public void testCompareToNullOther() {
        ValuedEnum enum1 = new ValuedEnum("TestEnum1", 1);
        assertNull(enum1.compareTo(null));
    }
}