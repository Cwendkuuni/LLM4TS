
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributeIterator;
import org.junit.Assert;
import org.junit.Test;

public class JDOMAttributeIteratorTest {

    @Test
    public void testGetNodePointer() {
        // Test when parent is an Element and attributes exist
        // Create a parent Element
        Element parentElement = new Element("test");
        
        // Add attributes to the parent Element
        parentElement.setAttribute("attr1", "value1");
        parentElement.setAttribute("attr2", "value2");
        
        // Create a NodePointer with the parent Element and a QName
        NodePointer parent = new NodePointer(parentElement);
        QName qname = new QName(null, "attr1");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, qname);
        
        // Get the first NodePointer and check if it's correct
        NodePointer nodePointer = iterator.getNodePointer();
        Assert.assertNotNull(nodePointer);
        Assert.assertEquals("attr1", nodePointer.getNode().getName());
        Assert.assertEquals("value1", ((Attribute) nodePointer.getNode()).getValue());
        
        // Try to get more NodePointers and check if they are correct
        iterator.setPosition(2);
        nodePointer = iterator.getNodePointer();
        Assert.assertNotNull(nodePointer);
        Assert.assertEquals("attr2", nodePointer.getNode().getName());
        Assert.assertEquals("value2", ((Attribute) nodePointer.getNode()).getValue());
        
        // Try to get a NodePointer out of range
        iterator.setPosition(3);
        nodePointer = iterator.getNodePointer();
        Assert.assertNull(nodePointer);
        
        // Test when parent is not an Element
        // Create a parent NodePointer
        NodePointer nonElementParent = new NodePointer("test");
        iterator = new JDOMAttributeIterator(nonElementParent, qname);
        
        // Try to get a NodePointer when parent is not an Element
        nodePointer = iterator.getNodePointer();
        Assert.assertNull(nodePointer);
    }

    @Test
    public void testGetPosition() {
        // Create a parent Element
        Element parentElement = new Element("test");
        
        // Add attributes to the parent Element
        parentElement.setAttribute("attr1", "value1");
        parentElement.setAttribute("attr2", "value2");
        
        // Create a NodePointer with the parent Element and a QName
        NodePointer parent = new NodePointer(parentElement);
        QName qname = new QName(null, "attr1");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, qname);
        
        // Initial position should be 0
        Assert.assertEquals(0, iterator.getPosition());
        
        // Set position and check if it's correct
        iterator.setPosition(1);
        Assert.assertEquals(1, iterator.getPosition());
        
        // Set position outside of range
        iterator.setPosition(3);
        Assert.assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        // Create a parent Element
        Element parentElement = new Element("test");
        
        // Add attributes to the parent Element
        parentElement.setAttribute("attr1", "value1");
        parentElement.setAttribute("attr2", "value2");
        
        // Create a NodePointer with the parent Element and a QName
        NodePointer parent = new NodePointer(parentElement);
        QName qname = new QName(null, "attr1");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, qname);
        
        // Set position and check if it's correct
        Assert.assertTrue(iterator.setPosition(1));
        Assert.assertEquals(1, iterator.getPosition());
        
        // Set position outside of range
        Assert.assertFalse(iterator.setPosition(3));
        Assert.assertEquals(0, iterator.getPosition());
    }
}
