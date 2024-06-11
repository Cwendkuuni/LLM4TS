package org.apache.commons.lang.enums;
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.List;
import java.util.Map;
import java.util.Iterator;

public class ValuedEnumTest {

    @Test
    public void testConstructor() {
        ValuedEnum enumItem = new ValuedEnumImpl("ENUM_ITEM", 1);
        assertEquals("ENUM_ITEM", enumItem.getName());
        assertEquals(1, enumItem.getValue());
    }

    @Test
    public void testGetEnum() {
        ValuedEnum enumItem = new ValuedEnumImpl("ENUM_ITEM", 1);
        ValuedEnum otherEnumItem = new ValuedEnumImpl("OTHER_ENUM_ITEM", 2);
        assertTrue(enumItem.equals(ValuedEnum.getEnum(ValuedEnumImpl.class, 1)));
        assertTrue(otherEnumItem.equals(ValuedEnum.getEnum(ValuedEnumImpl.class, 2)));
        assertNull(ValuedEnum.getEnum(ValuedEnumImpl.class, 3));
    }

    @Test
    public void testGetValue() {
        ValuedEnum enumItem = new ValuedEnumImpl("ENUM_ITEM", 1);
        assertEquals(1, enumItem.getValue());
    }

    @Test
    public void testCompareTo() {
        ValuedEnum enumItem1 = new ValuedEnumImpl("ENUM_ITEM1", 1);
        ValuedEnum enumItem2 = new ValuedEnumImpl("ENUM_ITEM2", 2);
        ValuedEnum enumItem3 = new ValuedEnumImpl("ENUM_ITEM3", 3);
        ValuedEnum enumItem4 = new ValuedEnumImpl("ENUM_ITEM4", 4);
        assertTrue(enumItem1.compareTo(enumItem2) < 0);
        assertTrue(enumItem2.compareTo(enumItem1) > 0);
        assertTrue(enumItem3.compareTo(enumItem4) < 0);
        assertTrue(enumItem4.compareTo(enumItem3) > 0);
        assertTrue(enumItem1.compareTo(enumItem1) == 0);
    }

    @Test
    public void testToString() {
        ValuedEnum enumItem = new ValuedEnumImpl("ENUM_ITEM", 1);
        assertEquals("ValuedEnumImpl[ENUM_ITEM=1]", enumItem.toString());
    }

    private class ValuedEnumImpl extends ValuedEnum {
        private static final long serialVersionUID = 1L;
        ValuedEnumImpl(String name, int value) {
            super(name, value);
        }
    }
}