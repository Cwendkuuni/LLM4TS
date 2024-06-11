import static org.junit.Assert.assertEquals;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.junit.Test;

public class XmlElementTest {

	@Test
	public void testAddAttribute() {
		XmlElement element = new XmlElement("test");
		Object returnValue = element.addAttribute("name", "value");
		assertEquals(null, returnValue);
	}

	@Test
	public void testGetAttribute() {
		XmlElement element = new XmlElement("test");
		element.addAttribute("name", "value");
		String attribute = element.getAttribute("name");
		assertEquals("value", attribute);
	}

	@Test
	public void testGetAttributeWithDefaultValue() {
		XmlElement element = new XmlElement("test");
		String attribute = element.getAttribute("name", "defaultValue");
		assertEquals("defaultValue", attribute);
	}

	@Test
	public void testGetAttributes() {
		XmlElement element = new XmlElement("test");
		Hashtable<String, String> attributes = new Hashtable<String, String>();
		attributes.put("name1", "value1");
		attributes.put("name2", "value2");
		element.setAttributes(attributes);
		Hashtable<String, String> result = element.getAttributes();
		assertEquals(attributes, result);
	}

	@Test
	public void testGetAttributeNames() {
		XmlElement element = new XmlElement("test");
		Hashtable<String, String> attributes = new Hashtable<String, String>();
		attributes.put("name1", "value1");
		attributes.put("name2", "value2");
		element.setAttributes(attributes);
		Enumeration<Object> enumeration = element.getAttributeNames();
		assertEquals(attributes.keys(), enumeration);
	}

	@Test
	public void testAddElement() {
		XmlElement parent = new XmlElement("parent");
		XmlElement child = new XmlElement("child");
		boolean result = parent.addElement(child);
		assertEquals(true, result);
	}

	@Test
	public void testRemoveElementByElement() {
		XmlElement parent = new XmlElement("parent");
		XmlElement child1 = new XmlElement("child1");
		XmlElement child2 = new XmlElement("child2");
		parent.addElement(child1);
		parent.addElement(child2);
		XmlElement removedChild = parent.removeElement(child1);
		assertEquals(child1, removedChild);
	}

	@Test
	public void testRemoveElementByIndex() {
		XmlElement parent = new XmlElement("parent");
		XmlElement child1 = new XmlElement("child1");
		XmlElement child2 = new XmlElement("child2");
		parent.addElement(child1);
		parent.addElement(child2);
		XmlElement removedChild = parent.removeElement(0);
		assertEquals(child1, removedChild);
	}

	@Test
	public void testRemoveAllElements() {
		XmlElement parent = new XmlElement("parent");
		XmlElement child1 = new XmlElement("child1");
		XmlElement child2 = new XmlElement("child2");
		parent.addElement(child1);
		parent.addElement(child2);
		parent.removeAllElements();
		List<XmlElement> subElements = parent.getElements();
		assertEquals(0, subElements.size());
	}

	@Test
	public void testRemoveFromParent() {
		XmlElement parent = new XmlElement("parent");
		XmlElement child = new XmlElement("child");
		parent.addElement(child);
		child.removeFromParent();
		XmlElement result = child.getParent();
		assertEquals(null, result);
	}

	@Test
	public void testAppend() {
		XmlElement parent = new XmlElement("parent");
		XmlElement child = new XmlElement("child");
		child.removeFromParent();
		parent.append(child);
		assertEquals(parent, child.getParent());
	}

	@Test
	public void testInsertElement() {
		XmlElement parent = new XmlElement("parent");
		XmlElement child1 = new XmlElement("child1");
		XmlElement child3 = new XmlElement("child3");
		parent.addElement(child1);
		parent.insertElement(child3, 1);
		List<XmlElement> subElements = parent.getElements();
		assertEquals(child1, subElements.get(0));
		assertEquals(child3, subElements.get(1));
	}

	@Test
	public void testGetElements() {
		XmlElement element = new XmlElement("test");
		XmlElement child1 = new XmlElement("child1");
		XmlElement child2 = new XmlElement("child2");
		element.addElement(child1);
		element.addElement(child2);
		List<XmlElement> subElements = element.getElements();
		assertEquals(2, subElements.size());
	}

	@Test
	public void testCount() {
		XmlElement element = new XmlElement("test");
		XmlElement child1 = new XmlElement("child1");
		XmlElement child2 = new XmlElement("child2");
		element.addElement(child1);
		element.addElement(child2);
		int count = element.count();
		assertEquals(2, count);
	}

	@Test
	public void testGetElementByPath() {
		XmlElement element1 = new XmlElement("element1");
		XmlElement child1 = new XmlElement("child1");
		XmlElement child2 = new XmlElement("child2");
		element1.addElement(child1);
		child1.addElement(child2);
		XmlElement result = element1.getElement("child1.child2");
		assertEquals(child2, result);
	}

	@Test
	public void testGetElementByIndex() {
		XmlElement element = new XmlElement("element");
		XmlElement child1 = new XmlElement("child1");
		XmlElement child2 = new XmlElement("child2");
		element.addElement(child1);
		element.addElement(child2);
		XmlElement result = element.getElement(0);
		assertEquals(child1, result);
	}

	@Test
	public void testAddSubElementByPath() {
		XmlElement element = new XmlElement("element");
		XmlElement child1 = element.addSubElement("child1");
		assertEquals(child1, element.getElement("child1"));
	}

	@Test
	public void testAddSubElementByElement() {
		XmlElement element = new XmlElement("element");
		XmlElement child1 = new XmlElement("child1");
		XmlElement result = element.addSubElement(child1);
		assertEquals(child1, result);
	}

	@Test
	public void testAddSubElementByNameAndData() {
		XmlElement element = new XmlElement("element");
		XmlElement child1 = element.addSubElement("child1", "data1");
		assertEquals(child1, element.getElement("child1"));
		assertEquals("data1", child1.getData());
	}

	@Test
	public void testSetParent() {
		XmlElement parent = new XmlElement("parent");
		XmlElement child = new XmlElement("child");
		child.setParent(parent);
		assertEquals(parent, child.getParent());
	}

	@Test
	public void testGetData() {
		XmlElement element = new XmlElement("test");
		element.setData("data");
		String result = element.getData();
		assertEquals("data", result);
	}

	@Test
	public void testGetName() {
		XmlElement element = new XmlElement("test");
		String name = element.getName();
		assertEquals("test", name);
	}

	@Test
	public void testGetHashCode() {
		XmlElement element = new XmlElement("test");
		int hashCode = element.hashCode();
		assertEquals(23, hashCode);
	}
}