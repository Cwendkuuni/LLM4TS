import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.model.JDOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.JDOMAttributePointer;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * JDOMAttributeIterator test cases.
 */
public class JDOMAttributeIteratorTest {

    @Test
    public void testConstructor_whenCalledWithValidParentAndName_thenCreateNewInstance() {
        NodePointer parent = new NodePointer(new Element("root"), null);
        QName name = new QName("test", "xml");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        Assert.assertNotNull(iterator);
    }

    @Test
    public void testConstructor_whenCalledWithNullParent_thenThrowIllegalArgumentException() {
        NodePointer parent = null;
        QName name = new QName("test", "xml");

        Assert.assertThrows(IllegalArgumentException.class, () -> new JDOMAttributeIterator(parent, name));
    }

    @Test
    public void testConstructor_whenCalledWithNullName_thenCreateNewInstanceWithAllAttributes() {
        NodePointer parent = new NodePointer(new Element("root"), null);
        QName name = null;
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        Assert.assertNotNull(iterator);
    }

    @Test
    public void testGetNodePointer_whenCalledForValidPosition_thenReturnExpectedAttribute() {
        NodePointer parent = new NodePointer(new Element("root"), null);
        QName name = new QName("test", "xml");
        Element element = new Element("root", "root");
        element.addAttribute("attr1", "val1");
        element.addAttribute("attr2", "val2");

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element, null), name);

        Attribute expected = iterator.getNodePointer().getNode().getAttribute("attr1");
        Attribute actual = iterator.next().getNode().getAttribute("attr1");

        Assert.assertNotNull(expected);
        Assert.assertNotNull(actual);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetNodePointer_whenCalledForInvalidPosition_thenReturnNull() {
        NodePointer parent = new NodePointer(new Element("root"), null);
        QName name = new QName("test", "xml");
        Element element = new Element("root", "root");
        element.addAttribute("attr1", "val1");
        element.addAttribute("attr2", "val2");

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element, null), name);

        Assert.assertNull(iterator.next().getNode());
    }

    @Test
    public void testGetPosition_whenCalledForValidPosition_thenReturnExpectedValue() {
        NodePointer parent = new NodePointer(new Element("root"), null);
        QName name = new QName("test", "xml");
        Element element = new Element("root", "root");
        element.addAttribute("attr1", "val1");
        element.addAttribute("attr2", "val2");

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element, null), name);

        Assert.assertEquals(1, iterator.getPosition());
        Assert.assertEquals(2, iterator.next().getPosition());
        Assert.assertEquals(3, iterator.next().getPosition());
        Assert.assertEquals(-1, iterator.next().getPosition());
    }

    @Test
    public void testSetPosition_whenCalledWithValidPosition_thenSetExpectedPosition() {
        NodePointer parent = new NodePointer(new Element("root"), null);
        QName name = new QName("test", "xml");
        Element element = new Element("root", "root");
        element.addAttribute("attr1", "val1");
        element.addAttribute("attr2", "val2");

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element, null), name);

        iterator.next();
        iterator.setPosition(2);

        Assert.assertEquals(2, iterator.getPosition());
    }

    @Test
    public void testSetPosition_whenCalledWithInvalidPosition_thenThrowIndexOutOfBoundsException() {
        NodePointer parent = new NodePointer(new Element("root"), null);
        QName name = new QName("test", "xml");
        Element element = new Element("root", "root");
        element.addAttribute("attr1", "val1");
        element.addAttribute("attr2", "val2");

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element, null), name);

        Assert.assertThrows(IndexOutOfBoundsException.class, () -> iterator.setPosition(3));
    }

    @Test
    public void testNext_whenCalledForFirstValue_thenReturnNextAttribute() {
        NodePointer parent = new NodePointer(new Element("root"), null);
        QName name = new QName("test", "xml");
        Element element = new Element("root", "root");
        element.addAttribute("attr1", "val1");
        element.addAttribute("attr2", "val2");

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element, null), name);

        Attribute expected = iterator.getNodePointer().getNode().getAttribute("attr1");
        Attribute actual = iterator.next().getNode().getAttribute("attr2");

        Assert.assertNotNull(expected);
        Assert.assertNotNull(actual);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testNext_whenCalledForLastValue_thenReturnNull() {
        NodePointer parent = new NodePointer(new Element("root"), null);
        QName name = new QName("test", "xml");
        Element element = new Element("root", "root");
        element.addAttribute("attr1", "val1");
        element.addAttribute("attr2", "val2");

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element, null), name);

        iterator.next();
        iterator.next();

        Assert.assertNull(iterator.next().getNode());
    }

    @Test
    public void testNext_whenCalledMultipleTimes_thenIterateThroughAllAttributes() {
        NodePointer parent = new NodePointer(new Element("root"), null);
        QName name = new QName("test", "xml");
        Element element = new Element("root", "root");
        element.addAttribute("attr1", "val1");
        element.addAttribute("attr2", "val2");

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element, null), name);

        List<Attribute> attributes = new ArrayList<>();
        Attribute expected;

        for (int i = 0; i < 3; i++) {
            expected = iterator.getNodePointer().getNode().getAttribute("attr" + i);
            Attribute actual = iterator.next().getNode().getAttribute("attr" + (i + 1));

            Assert.assertNotNull(expected);
            Assert.assertNotNull(actual);
            Assert.assertEquals(expected, actual);
            attributes.add(expected);
        }

        Assert.assertEquals(3, attributes.size());
    }

    @Test
    public void testHasNext_whenCalledForFirstValue_thenReturnTrue() {
        NodePointer parent = new NodePointer(new Element("root"), null);
        QName name = new QName("test", "xml");
        Element element = new Element("root", "root");
        element.addAttribute("attr1", "val1");
        element.addAttribute("attr2", "val2");

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element, null), name);

        Assert.assertTrue(iterator.hasNext());
    }

    @Test
    public void testHasNext_whenCalledForLastValue_thenReturnFalse() {
        NodePointer parent = new NodePointer(new Element("root"), null);
        QName name = new QName("test", "xml");
        Element element = new Element("root", "root");
        element.addAttribute("attr1", "val1");
        element.addAttribute("attr2", "val2");

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element, null), name);

        iterator.next();
        iterator.next();

        Assert.assertFalse(iterator.hasNext());
    }
}