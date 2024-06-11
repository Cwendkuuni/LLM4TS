import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Before;
import org.junit.Test;

public class JDOMAttributeIteratorTest {
    
    private JDOMAttributeIterator iterator;
    private Element parentElement;
    private NodePointer parentPointer;
    
    @Before
    public void setup() {
        parentElement = new Element("parent");
        Attribute attribute1 = new Attribute("attribute1", "Value1");
        Attribute attribute2 = new Attribute("attribute2", "Value2");
        Attribute attribute3 = new Attribute("attribute3", "Value3");
        parentElement.setAttribute(attribute1);
        parentElement.setAttribute(attribute2);
        parentElement.setAttribute(attribute3);
        parentPointer = new JDOMAttributePointer(new JDOMNodePointer(), attribute1);
        iterator = new JDOMAttributeIterator(parentPointer, new QName("attribute1"));
    }
    
    @Test
    public void testConstructor_WithElementAndMatchingName_ShouldInitializeAttributesList() {
        assertEquals(1, iterator.attributes.size());
        assertEquals(new QName("attribute1"), ((Attribute)iterator.attributes.get(0)).getName());
    }
    
    @Test
    public void testConstructor_WithElementAndNonMatchingName_ShouldLeaveAttributesListEmpty() {
        iterator = new JDOMAttributeIterator(parentPointer, new QName("nonMatchingAttribute"));
        assertTrue(iterator.attributes.isEmpty());
    }
    
    @Test
    public void testConstructor_WithElementAndWildcardName_ShouldInitializeAttributesList() {
        Attribute attribute4 = new Attribute("attribute4", "Value4", Namespace.NO_NAMESPACE);
        parentElement.setAttribute(attribute4);
        
        iterator = new JDOMAttributeIterator(parentPointer, new QName("*"));
        
        assertEquals(4, iterator.attributes.size());
        List<QName> attributeNames = Arrays.asList(
            ((Attribute)iterator.attributes.get(0)).getName(),
            ((Attribute)iterator.attributes.get(1)).getName(),
            ((Attribute)iterator.attributes.get(2)).getName(),
            ((Attribute)iterator.attributes.get(3)).getName()
        );
        assertTrue(attributeNames.contains(new QName("attribute1")));
        assertTrue(attributeNames.contains(new QName("attribute2")));
        assertTrue(attributeNames.contains(new QName("attribute3")));
        assertTrue(attributeNames.contains(new QName("attribute4")));
    }
    
    @Test
    public void testGetNodePointer_WhenPositionZero_ShouldReturnNull() {
        assertNull(iterator.getNodePointer());
    }
    
    @Test
    public void testGetNodePointer_WhenPositionOne_ShouldReturnFirstAttributeNodePointer() {
        iterator.setPosition(1);
        NodePointer nodePointer = iterator.getNodePointer();
        assertTrue(nodePointer instanceof JDOMAttributePointer);
        assertEquals("attribute1", ((JDOMAttributePointer) nodePointer).getAttribute().getName());
    }
    
    @Test
    public void testGetNodePointer_WhenPositionGreaterThanOne_ShouldReturnAttributeNodePointerAtThatPosition() {
        iterator.setPosition(2);
        NodePointer nodePointer = iterator.getNodePointer();
        assertTrue(nodePointer instanceof JDOMAttributePointer);
        assertEquals("attribute2", ((JDOMAttributePointer) nodePointer).getAttribute().getName());
    }
    
    @Test
    public void testGetNodePointer_WhenPositionGreaterThanAttributesSize_ShouldReturnNull() {
        iterator.setPosition(10);
        assertNull(iterator.getNodePointer());
    }
    
    @Test
    public void testGetPosition_WhenPositionNotSet_ShouldReturnZero() {
        assertEquals(0, iterator.getPosition());
    }
    
    @Test
    public void testGetPosition_WhenPositionSet_ShouldReturnPositionValue() {
        iterator.setPosition(3);
        assertEquals(3, iterator.getPosition());
    }
    
    @Test
    public void testSetPosition_WhenAttributesIsNull_ShouldReturnFalse() {
        iterator.attributes = null;
        assertFalse(iterator.setPosition(1));
    }
    
    @Test
    public void testSetPosition_WhenPositionIsZero_ShouldReturnFalse() {
        assertFalse(iterator.setPosition(0));
    }
    
    @Test
    public void testSetPosition_WhenPositionIsGreaterThanAttributesSize_ShouldReturnFalse() {
        assertFalse(iterator.setPosition(10));
    }
    
    @Test
    public void testSetPosition_WhenPositionIsValid_ShouldReturnTrue() {
        assertTrue(iterator.setPosition(1));
        assertEquals(1, iterator.getPosition());
    }
}