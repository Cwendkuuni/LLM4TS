package org.apache.commons.lang.enums;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ValuedEnumTest {
    @Test
    public void testConstructor() {
        ValuedEnum valueEnum = new ValuedEnum("Name", 1) {};
        assertEquals("Name", valueEnum.getName());
        assertEquals(1, valueEnum.getValue());
    }

    @Test
    public void testGetEnum() {
        ValuedEnum valueEnum = new ValuedEnum("Name", 1) {};
        assertNull(ValuedEnum.getEnum(null, 1));
        assertNull(ValuedEnum.getEnum(ValuedEnum.class, 2));
        assertEquals(valueEnum, ValuedEnum.getEnum(ValuedEnum.class, 1));
    }
    
    @Test
    public void testGetValue() {
        ValuedEnum valueEnum = new ValuedEnum("Name", 1) {};
        assertEquals(1, valueEnum.getValue());
    }
    
    @Test
    public void testCompareTo() {
        ValuedEnum valueEnum1 = new ValuedEnum("Name1", 1) {};
        ValuedEnum valueEnum2 = new ValuedEnum("Name2", 2) {};
        ValuedEnum valueEnum3 = new ValuedEnum("Name3", 3) {};
        assertEquals(-1, valueEnum1.compareTo(valueEnum2));
        assertEquals(0, valueEnum1.compareTo(valueEnum1));
        assertEquals(1, valueEnum2.compareTo(valueEnum1));
        assertEquals(-2, valueEnum1.compareTo(valueEnum3));
    }
    
    @Test
    public void testToString() {
        ValuedEnum valueEnum = new ValuedEnum("Name", 1) {};
        assertEquals("ValuedEnum[Name=1]", valueEnum.toString());
    }
}