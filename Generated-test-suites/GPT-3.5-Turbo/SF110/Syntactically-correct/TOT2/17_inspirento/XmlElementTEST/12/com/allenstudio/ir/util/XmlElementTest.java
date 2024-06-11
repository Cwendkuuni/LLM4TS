package com.allenstudio.ir.util;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Hashtable;
import java.util.Vector;

public class XmlElementTest {

    @Test
    public void testDefaultConstructor() {
        XmlElement element = new XmlElement();
        assertNotNull(element);
        assertNotNull(element.getAttributes());
        assertEquals(0, element.getAttributes().size());
        assertEquals(0, element.getElements().size());
    }

    @Test
    public void testParameterizedConstructorWithName() {
        String name = "TestName";
        XmlElement element = new XmlElement(name);
        assertNotNull(element);
        assertEquals(name, element.getName());
        assertNotNull(element.getAttributes());
    }

    @Test
    public void testAddAttribute() {
        XmlElement element = new XmlElement();
        String name = "TestAttribute";
        String value = "TestValue";
        assertNull(element.addAttribute(name, value));
        assertEquals(value, element.getAttributes().get(name));
    }

    // More test cases for other methods can be added here

}