
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Hashtable;
import java.util.List;

public class XmlElementTest {

    private XmlElement xmlElement;

    @Before
    public void setUp() {
        xmlElement = new XmlElement();
    }

    @After
    public void tearDown() {
        xmlElement = null;
    }

    @Test
    public void testAddAttribute() {
        String attributeName = "attribute";
        String attributeValue = "value";

        Object result = xmlElement.addAttribute(attributeName, attributeValue);

        Assert.assertNull(result);
        Assert.assertEquals(attributeValue, xmlElement.getAttributes().get(attributeName));
    }

    @Test
    public void testGetAttribute() {
        String attributeName = "attribute";
        String attributeValue = "value";
        xmlElement.addAttribute(attributeName, attributeValue);

        String result = xmlElement.getAttribute(attributeName);

        Assert.assertEquals(attributeValue, result);
    }

    @Test
    public void testGetAttributeWithDefaultValue() {
        String attributeName = "attribute";
        String defaultValue = "default";
        String result = xmlElement.getAttribute(attributeName, defaultValue);

        Assert.assertEquals(defaultValue, result);
    }

    @Test
    public void testGetAttributeWithExistingDefaultValue() {
        String attributeName = "attribute";
        String defaultValue = "default";
        xmlElement.addAttribute(attributeName, defaultValue);

        String result = xmlElement.getAttribute(attributeName, "other");

        Assert.assertEquals(defaultValue, result);
    }

    @Test
    public void testSetAttributes() {
        Hashtable<String, String> attributes = new Hashtable<>();
        attributes.put("attribute1", "value1");
        attributes.put("attribute2", "value2");

        xmlElement.setAttributes(attributes);

        Assert.assertEquals(attributes, xmlElement.getAttributes());
    }

    @Test
    public void testGetAttributeNames() {
        Hashtable<String, String> attributes = new Hashtable<>();
        attributes.put("attribute1", "value1");
        attributes.put("attribute2", "value2");
        xmlElement.setAttributes(attributes);

        Enumeration result = xmlElement.getAttributeNames();

        Assert.assertEquals(attributes.keys(), result);
    }

    @Test
    public void testAddElement() {
        XmlElement element = new XmlElement("element");

        boolean result = xmlElement.addElement(element);

        Assert.assertTrue(result);
        Assert.assertEquals(element, xmlElement.getElements().get(0));
        Assert.assertEquals(xmlElement, element.getParent());
    }

    @Test
    public void testRemoveElementByObject() {
        XmlElement element = new XmlElement("element");
        xmlElement.addElement(element);

        XmlElement result = xmlElement.removeElement(element);

        Assert.assertEquals(element, result);
        Assert.assertEquals(0, xmlElement.getElements().size());
        Assert.assertNull(element.getParent());
    }

    @Test
    public void testRemoveElementByIndex() {
        XmlElement element = new XmlElement("element");
        xmlElement.addElement(element);

        XmlElement result = xmlElement.removeElement(0);

        Assert.assertEquals(element, result);
        Assert.assertEquals(0, xmlElement.getElements().size());
        Assert.assertNull(element.getParent());
    }

    @Test
    public void testRemoveAllElements() {
        xmlElement.addElement(new XmlElement("element1"));
        xmlElement.addElement(new XmlElement("element2"));

        xmlElement.removeAllElements();

        Assert.assertEquals(0, xmlElement.getElements().size());
    }

    @Test
    public void testRemoveFromParent() {
        XmlElement parent = new XmlElement("parent");
        XmlElement element = new XmlElement("element");
        parent.addElement(element);

        element.removeFromParent();

        Assert.assertEquals(0, parent.getElements().size());
        Assert.assertNull(element.getParent());
    }

    @Test
    public void testAppend() {
        XmlElement element = new XmlElement("element");

        element.append(xmlElement);

        Assert.assertEquals(element, xmlElement.getElements().get(0));
        Assert.assertEquals(xmlElement, element.getParent());
    }

    @Test
    public void testInsertElement() {
        XmlElement element = new XmlElement("element");
        xmlElement.addElement(new XmlElement("element1"));
        xmlElement.addElement(new XmlElement("element2"));

        xmlElement.insertElement(element, 1);

        Assert.assertEquals(element, xmlElement.getElements().get(1));
        Assert.assertEquals(xmlElement, element.getParent());
    }

    @Test
    public void testGetElements() {
        xmlElement.addElement(new XmlElement("element1"));
        xmlElement.addElement(new XmlElement("element2"));

        List result = xmlElement.getElements();

        Assert.assertEquals(xmlElement.getElements(), result);
    }

    @Test
    public void testCount() {
        xmlElement.addElement(new XmlElement("element1"));
        xmlElement.addElement(new XmlElement("element2"));

        int result = xmlElement.count();

        Assert.assertEquals(2, result);
    }

    @Test
    public void testGetElement() {
        xmlElement.addElement(new XmlElement("element1"));
        xmlElement.addElement(new XmlElement("element2"));

        XmlElement result = xmlElement.getElement("element1");

        Assert.assertEquals(xmlElement.getElements().get(0), result);
    }

    @Test
    public void testGetElementWithSubname() {
        XmlElement parent = new XmlElement("parent");
        XmlElement element1 = new XmlElement("element1");
        XmlElement element2 = new XmlElement("element2");
        xmlElement.addElement(parent);
        parent.addElement(element1);
        element1.addElement(element2);

        XmlElement result = xmlElement.getElement("parent.element1.element2");

        Assert.assertEquals(element2, result);
    }

    @Test
    public void testGetElementWithNonExistingName() {
        xmlElement.addElement(new XmlElement("element"));

        XmlElement result = xmlElement.getElement("nonexisting");

        Assert.assertNull(result);
    }

    @Test
    public void testGetElementWithNonExistingSubname() {
        XmlElement parent = new XmlElement("parent");
        XmlElement element = new XmlElement("element");
        xmlElement.addElement(parent);
        parent.addElement(element);

        XmlElement result = xmlElement.getElement("parent.nonexisting.element");

        Assert.assertNull(result);
    }

    @Test
    public void testGetElementWithEmptyPath() {
        xmlElement.addElement(new XmlElement("element"));
        xmlElement.addElement(new XmlElement("element2"));

        XmlElement result = xmlElement.getElement("");

        Assert.assertNull(result);
    }

    @Test
    public void testGetElementByIndex() {
        XmlElement element = new XmlElement("element");
        xmlElement.addElement(element);

        XmlElement result = xmlElement.getElement(0);

        Assert.assertEquals(element, result);
    }

    @Test
    public void testAddSubElementWithPath() {
        xmlElement.addSubElement("parent.element1.element2");

        List elements = xmlElement.getElements();
        Assert.assertEquals(1, elements.size());

        XmlElement parent = (XmlElement) elements.get(0);
        Assert.assertEquals("parent", parent.getName());

        List subElements1 = parent.getElements();
        Assert.assertEquals(1, subElements1.size());

        XmlElement element1 = (XmlElement) subElements1.get(0);
        Assert.assertEquals("element1", element1.getName());

        List subElements2 = element1.getElements();
        Assert.assertEquals(1, subElements2.size());

        XmlElement element2 = (XmlElement) subElements2.get(0);
        Assert.assertEquals("element2", element2.getName());
    }

    @Test
    public void testAddSubElement() {
        XmlElement element = new XmlElement("element");

        XmlElement result = xmlElement.addSubElement(element);

        Assert.assertEquals(element, result);
        Assert.assertEquals(xmlElement, element.getParent());
        Assert.assertEquals(element, xmlElement.getElements().get(0));
    }

    @Test
    public void testAddSubElementWithNameAndData() {
        String name = "element";
        String data = "data";

        XmlElement result = xmlElement.addSubElement(name, data);

        Assert.assertEquals(name, result.getName());
        Assert.assertEquals(data, result.getData());
        Assert.assertEquals(xmlElement, result.getParent());
        Assert.assertEquals(result, xmlElement.getElements().get(0));
    }

    @Test
    public void testSetParent() {
        XmlElement parent = new XmlElement("parent");
        xmlElement.setParent(parent);

        Assert.assertEquals(parent, xmlElement.getParent());
    }

    @Test
    public void testGetData() {
        String data = "data";
        xmlElement.setData(data);

        String result = xmlElement.getData();

        Assert.assertEquals(data, result);
    }

    @Test
    public void testGetName() {
        String name = "name";
        xmlElement.setName(name);

        String result = xmlElement.getName();

        Assert.assertEquals(name, result);
    }

    @Test
    public void testNotifyObservers() {
        TestObserver observer = new TestObserver();
        xmlElement.addObserver(observer);

        xmlElement.notifyObservers();

        Assert.assertTrue(observer.flag);
    }

    private class TestObserver implements Observer {
        boolean flag = false;

        @Override
        public void update(Observable o, Object arg) {
            flag = true;
        }
    }
}
