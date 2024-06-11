import org.junit.Test;
import static org.junit.Assert.*;

public class XmlElementTest {

    @Test
    public void testAddAttributeWithNullValue() {
        XmlElement xmlElement = new XmlElement("element");
        assertNull(xmlElement.addAttribute("attribute1", null));
        assertNull(xmlElement.getAttribute("attribute1"));
    }

    @Test
    public void testGetNonExistingAttribute() {
        XmlElement xmlElement = new XmlElement("element");
        assertNull(xmlElement.getAttribute("attribute2"));
    }

    @Test
    public void testAddAndRemoveElement() {
        XmlElement parentElement = new XmlElement("parent");
        XmlElement childElement = new XmlElement("child");
        assertTrue(parentElement.addElement(childElement));
        assertEquals(childElement, parentElement.removeElement(childElement));
    }

    @Test
    public void testGetNonExistingElement() {
        XmlElement xmlElement = new XmlElement("element");
        assertNull(xmlElement.getElement("nonExistingElement"));
    }

    @Test
    public void testAddAndRetrieveSubElementByPath() {
        XmlElement xmlElement = new XmlElement("element");
        XmlElement subElement1 = xmlElement.addSubElement("subElement1");
        XmlElement subElement2 = xmlElement.addSubElement("subElement2");
        assertNotNull(xmlElement.getElement("subElement1"));
        assertEquals(subElement1, xmlElement.getElement("subElement1"));
        assertNotNull(xmlElement.getElement("subElement2"));
        assertEquals(subElement2, xmlElement.getElement("subElement2"));
    }

    @Test
    public void testAddNullSubElement() {
        XmlElement xmlElement = new XmlElement("element");
        assertNull(xmlElement.addSubElement(null));
    }

    @Test
    public void testSetAndRetrieveParent() {
        XmlElement parentElement = new XmlElement("parent");
        XmlElement childElement = new XmlElement("child");
        assertNull(childElement.getParent());
        parentElement.addElement(childElement);
        assertEquals(parentElement, childElement.getParent());
    }

    @Test
    public void testSetAndGetData() {
        XmlElement xmlElement = new XmlElement("element");
        xmlElement.setData("data");
        assertEquals("data", xmlElement.getData());
    }

    @Test
    public void testCloneXmlElement() {
        XmlElement xmlElement = new XmlElement("element");
        XmlElement clonedElement = (XmlElement) xmlElement.clone();
        assertNotNull(clonedElement);
        assertNotSame(xmlElement, clonedElement);
        assertEquals(xmlElement.getAttributes(), clonedElement.getAttributes());
        assertEquals(xmlElement.getElements(), clonedElement.getElements());
    }

    @Test
    public void testNotifyObservers() {
        XmlElement xmlElement = new XmlElement("element");
        ObserverTestObserver observer = new ObserverTestObserver();
        xmlElement.addObserver(observer);
        xmlElement.notifyObservers();
        assertTrue(observer.isNotified());
    }

    @Test
    public void testXmlElementEqualityAndHashCode() {
        XmlElement xmlElement1 = new XmlElement("element1");
        xmlElement1.addAttribute("attribute1", "value1");
        xmlElement1.addSubElement("subElement1").setData("data1");

        XmlElement xmlElement2 = new XmlElement("element2");
        xmlElement2.addAttribute("attribute2", "value2");
        xmlElement2.addSubElement("subElement2").setData("data2");

        XmlElement xmlElement3 = new XmlElement("element1");
        xmlElement3.addAttribute("attribute1", "value1");
        xmlElement3.addSubElement("subElement1").setData("data1");

        assertNotEquals(xmlElement1, xmlElement2);
        assertNotEquals(xmlElement2, xmlElement3);
        assertEquals(xmlElement1, xmlElement3);
        assertNotEquals(xmlElement1.hashCode(), xmlElement2.hashCode());
        assertEquals(xmlElement1.hashCode(), xmlElement3.hashCode());
    }

    private class ObserverTestObserver implements Observer {
        private boolean notified = false;

        public boolean isNotified() {
            return notified;
        }

        @Override
        public void update(Observable o, Object arg) {
            notified = true;
        }
    }
}