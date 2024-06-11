package com.allenstudio.ir.util;
import org.junit.*;
import static org.junit.Assert.*;

public class XmlElementTest {

    private XmlElement testXmlElement;

    @Before
    public void setUp() {
        testXmlElement = new XmlElement("test");
    }

    @Test
    public void testAddAttributeNotNull() {
        Object returnValue = testXmlElement.addAttribute("key", "value");
        assertNotNull(returnValue);
    }

    @Test
    public void testAddAttributeNull() {
        Object returnValue = testXmlElement.addAttribute(null, "value");
        assertNull(returnValue);
    }

    @Test
    public void testGetAttribute() {
        testXmlElement.addAttribute("key", "value");
        assertEquals("value", testXmlElement.getAttribute("key"));
    }

    @Test
    public void testGetAttributeDefault() {
        assertEquals("default", testXmlElement.getAttribute("nonexistent_key", "default"));
    }

    @Test
    public void testAddElement() {
        XmlElement element = new XmlElement("sub");
        assertTrue(testXmlElement.addElement(element));
    }

    @Test
    public void testRemoveElement() {
        XmlElement element = new XmlElement("sub");
        testXmlElement.addElement(element);
        assertEquals(element, testXmlElement.removeElement(element));
    }

    @Test
    public void testRemoveElementByIndex() {
        XmlElement element = new XmlElement("sub");
        testXmlElement.addElement(element);
        assertEquals(element, testXmlElement.removeElement(0));
    }

    @Test
    public void testRemoveAllElements() {
        XmlElement element = new XmlElement("sub");
        testXmlElement.addElement(element);
        testXmlElement.removeAllElements();
        assertEquals(0, testXmlElement.count());
    }

    @After
    public void tearDown() {
        testXmlElement = null;
    }
}