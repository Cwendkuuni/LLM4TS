import org.junit.Before;
import org.junit.Test;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;
import static org.junit.Assert.*;

public class XmlElementTest {
    private XmlElement xmlElem;

    @Before
    public void setUp() {
        xmlElem = new XmlElement();
    }

    @Test
    public void testAddAttribute() {
        assertNull(xmlElem.addAttribute(null, "value"));
        assertNull(xmlElem.addAttribute("name", null));
        assertNull(xmlElem.addAttribute(null, null));
        assertEquals("value1", xmlElem.addAttribute("name1", "value1"));
    }

    @Test
    public void testGetAttribute() {
        assertNull(xmlElem.getAttribute("name"));
        assertNull(xmlElem.getAttribute("name", "default"));
        xmlElem.addAttribute("name", "value");
        assertEquals("value", xmlElem.getAttribute("name"));
        assertEquals("default", xmlElem.getAttribute("name", "default"));
    }

    @Test
    public void testSetAttributes() {
        Hashtable<String, String> attrs = new Hashtable<String, String>();
        attrs.put("name", "value");
        xmlElem.setAttributes(attrs);
        assertEquals(attrs, xmlElem.getAttributes());
    }

    @Test
    public void testGetAttributeNames() {
        Hashtable<String, String> attrs = new Hashtable<String, String>();
        attrs.put("name1", "value1");
        attrs.put("name2", "value2");
        xmlElem.setAttributes(attrs);
        List<String> expectedNames = new Vector<String>();
        expectedNames.add("name1");
        expectedNames.add("name2");
        assertEquals(expectedNames, xmlElem.getAttributeNames());
    }

    @Test
    public void testAddElement() {
        XmlElement e = new XmlElement("element");
        assertTrue(xmlElem.addElement(e));
        assertEquals(e, xmlElem.getElement(0));
    }

    @Test
    public void testRemoveElementByObject() {
        XmlElement e1 = new XmlElement("element1");
        XmlElement e2 = new XmlElement("element2");
        xmlElem.addElement(e1);
        xmlElem.addElement(e2);
        assertEquals(e1, xmlElem.removeElement(e1));
        assertNull(xmlElem.removeElement(e1));
        assertEquals(e2, xmlElem.getElement(0));
    }

    @Test
    public void testRemoveElementByIndex() {
        XmlElement e1 = new XmlElement("element1");
        XmlElement e2 = new XmlElement("element2");
        xmlElem.addElement(e1);
        xmlElem.addElement(e2);
        assertEquals(e1, xmlElem.removeElement(0));
        assertEquals(e2, xmlElem.getElement(0));
    }

    @Test
    public void testRemoveAllElements() {
        XmlElement e1 = new XmlElement("element1");
        XmlElement e2 = new XmlElement("element2");
        xmlElem.addElement(e1);
        xmlElem.addElement(e2);
        xmlElem.removeAllElements();
        assertEquals(0, xmlElem.count());
    }

    @Test
    public void testRemoveFromParent() {
        XmlElement e1 = new XmlElement("element1");
        XmlElement e2 = new XmlElement("element2");
        xmlElem.addElement(e1);
        e1.addElement(e2);
        e2.removeFromParent();
        assertEquals(0, e1.count());
    }

    @Test
    public void testAppend() {
        XmlElement e1 = new XmlElement("element1");
        XmlElement e2 = new XmlElement("element2");
        xmlElem.addElement(e1);
        xmlElem.append(e2);
        assertEquals(e2, xmlElem.getElement(1));
    }

    @Test
    public void testInsertElement() {
        XmlElement e1 = new XmlElement("element1");
        XmlElement e2 = new XmlElement("element2");
        xmlElem.addElement(e1);
        xmlElem.insertElement(e2, 0);
        assertEquals(e2, xmlElem.getElement(0));
        assertEquals(e1, xmlElem.getElement(1));
    }

    @Test
    public void testGetElementByPath_basic() {
        XmlElement e1 = new XmlElement("element1");
        XmlElement e2 = new XmlElement("element2");
        XmlElement e3 = new XmlElement("element3");
        e1.addElement(e2);
        e2.addElement(e3);
        xmlElem.addElement(e1);
        assertEquals(e1, xmlElem.getElement("element1"));
        assertEquals(e2, xmlElem.getElement("element1.element2"));
        assertEquals(e3, xmlElem.getElement("element1.element2.element3"));
    }

    @Test
    public void testCount() {
        XmlElement e1 = new XmlElement("element1");
        xmlElem.addElement(e1);
        assertEquals(1, xmlElem.count());
        xmlElem.addElement(new XmlElement("element2"));
        assertEquals(2, xmlElem.count());
    }

    @Test
    public void testGetElementByPath_invalid() {
        XmlElement e = new XmlElement("element");
        xmlElem.addElement(e);
        assertNull(xmlElem.getElement("invalid"));
        assertNull(xmlElem.getElement("element.invalid"));
    }

    @Test
    public void testGetElementByIndex() {
        XmlElement e1 = new XmlElement("element1");
        xmlElem.addElement(e1);
        assertEquals(e1, xmlElem.getElement(0));
    }

    @Test
    public void testAddSubElementByPath() {
        XmlElement e = new XmlElement("element1");
        xmlElem.addElement(e);
        XmlElement newElem = xmlElem.addSubElement("element1.element2");
        assertNotNull(newElem);
        assertEquals(newElem, xmlElem.getElement("element1.element2"));
    }

    @Test
    public void testAddSubElementByObject() {
        XmlElement newElem = new XmlElement("element1");
        xmlElem.addSubElement(newElem);
        assertEquals(newElem, xmlElem.getElement(0));
    }

    @Test
    public void testAddSubElementByNameAndData() {
        XmlElement newElem = xmlElem.addSubElement("element1", "data");
        assertNotNull(newElem);
        assertEquals(newElem, xmlElem.getElement(0));
        assertEquals("data", newElem.getData());
    }

    @Test
    public void testSetParent() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child = new XmlElement("child");
        child.setParent(parent);
        assertEquals(parent, child.getParent());
    }

    @Test
    public void testSetAndGetData() {
        xmlElem.setData("data");
        assertEquals("data", xmlElem.getData());
    }

    @Test
    public void testSetName() {
        xmlElem.setName("name");
        assertEquals("name", xmlElem.getName());
    }

    @Test
    public void testNotifyObservers() {
        xmlElem.addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                assertEquals(xmlElem, o);
            }
        });
        xmlElem.notifyObservers();
    }

    @Test
    public void testEquals_sameInstance() {
        assertTrue(xmlElem.equals(xmlElem));
    }

    @Test
    public void testEquals_nullObject() {
        assertFalse(xmlElem.equals(null));
    }

    @Test
    public void testEquals_differentClass() {
        assertFalse(xmlElem.equals("String"));
    }

    @Test
    public void testEquals_differentAttributes() {
        XmlElement xmlElem2 = new XmlElement();
        xmlElem2.addAttribute("name", "value");
        assertFalse(xmlElem.equals(xmlElem2));
    }

    @Test
    public void testEquals_differentData() {
        XmlElement xmlElem2 = new XmlElement();
        xmlElem2.setData("data");
        assertFalse(xmlElem.equals(xmlElem2));
    }

    @Test
    public void testEquals_differentName() {
        XmlElement xmlElem2 = new XmlElement("name");
        assertFalse(xmlElem.equals(xmlElem2));
    }

    @Test
    public void testEquals_differentSubElements() {
        XmlElement xmlElem2 = new XmlElement();
        XmlElement child = new XmlElement("child");
        xmlElem2.addElement(child);
        assertFalse(xmlElem.equals(xmlElem2));
    }

    @Test
    public void testEquals_sameAttributesDataNameSubElements() {
        XmlElement xmlElem2 = new XmlElement();
        xmlElem.addAttribute("name", "value");
        xmlElem.setData("data");
        xmlElem.setName("name");
        XmlElement child = new XmlElement("child");
        xmlElem.addElement(child);
        xmlElem2.addAttribute("name", "value");
        xmlElem2.setData("data");
        xmlElem2.setName("name");
        xmlElem2.addElement(child);
        assertTrue(xmlElem.equals(xmlElem2));
    }

    @Test
    public void testHashCode() {
        int expectedHashCode = 23;
        expectedHashCode += xmlElem.getAttributes().hashCode() * 13;
        assertEquals(expectedHashCode, xmlElem.hashCode());
    }
}