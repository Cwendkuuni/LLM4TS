import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class DOMAttributeIteratorTest {
    
    private static final String XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<root xmlns:prefix=\"http://example.com\">" +
            "<element prefix:attr1=\"value1\" prefix:attr2=\"value2\" />" +
            "<element attr3=\"value3\" attr4=\"value4\" />" +
            "</root>";
    
    private Document document;
    private NodePointer nodePointer;

    @Before
    public void setUp() throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        document = builder.parse(IOUtils.toInputStream(XML, "UTF-8"));
        nodePointer = new DOMNodePointer(document);
    }

    @Test
    public void testGetNodePointer_FirstAttribute_Success() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, new QName("prefix:attr1"));
        NodePointer attributeNodePointer = iterator.getNodePointer();
        
        assertTrue(attributeNodePointer instanceof DOMAttributePointer);
        assertEquals("value1", attributeNodePointer.getValue());
    }
    
    @Test
    public void testGetNodePointer_SecondAttribute_Success() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, new QName("prefix:attr2"));
        iterator.setPosition(2);
        NodePointer attributeNodePointer = iterator.getNodePointer();
        
        assertTrue(attributeNodePointer instanceof DOMAttributePointer);
        assertEquals("value2", attributeNodePointer.getValue());
    }
    
    @Test
    public void testGetNodePointer_InvalidAttribute_ReturnsNull() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, new QName("prefix:attr3"));
        iterator.setPosition(3);
        NodePointer attributeNodePointer = iterator.getNodePointer();
        
        assertNull(attributeNodePointer);
    }
    
    @Test
    public void testGetNodePointer_NonexistentAttribute_ReturnsNull() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, new QName("prefix:attr5"));
        NodePointer attributeNodePointer = iterator.getNodePointer();
        
        assertNull(attributeNodePointer);
    }
    
    @Test
    public void testGetPosition_InitialPosition_ReturnsOne() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, new QName("prefix:attr1"));
        int position = iterator.getPosition();
        
        assertEquals(1, position);
    }
    
    @Test
    public void testGetPosition_PositionAfterSetPosition_ReturnsCorrectPosition() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, new QName("prefix:attr1"));
        iterator.setPosition(2);
        int position = iterator.getPosition();
        
        assertEquals(2, position);
    }
    
    @Test
    public void testSetPosition_ValidPosition_ReturnsTrue() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, new QName("prefix:attr1"));
        boolean result = iterator.setPosition(2);
        
        assertTrue(result);
    }
    
    @Test
    public void testSetPosition_InvalidPosition_ReturnsFalse() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, new QName("prefix:attr1"));
        boolean result = iterator.setPosition(3);
        
        assertFalse(result);
    }
    
    @Test
    public void testSetPosition_PositionZero_ReturnsFalse() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, new QName("prefix:attr1"));
        boolean result = iterator.setPosition(0);
        
        assertFalse(result);
    }
    
    @Test
    public void testSetPosition_PositionGreaterThanSize_ReturnsFalse() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, new QName("prefix:attr1"));
        boolean result = iterator.setPosition(5);
        
        assertFalse(result);
    }
    
    @Test
    public void testSetPosition_PositivePosition_EqualsPosition() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, new QName("prefix:attr1"));
        iterator.setPosition(2);
        int position = iterator.getPosition();
        
        assertEquals(2, position);
    }
    
    @Test
    public void testSetPosition_NegativePosition_EqualsPosition() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, new QName("prefix:attr1"));
        iterator.setPosition(-1);
        int position = iterator.getPosition();
        
        assertEquals(-1, position);
    }
    
    @Test
    public void testSetPosition_ZeroPosition_EqualsPosition() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, new QName("prefix:attr1"));
        iterator.setPosition(0);
        int position = iterator.getPosition();
        
        assertEquals(0, position);
    }
    
    @Test
    public void testSetPosition_PositionOutOfRange_EqualsPosition() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, new QName("prefix:attr1"));
        iterator.setPosition(5);
        int position = iterator.getPosition();
        
        assertEquals(5, position);
    }
    
    @Test
    public void testSetPosition_PositionLessThanSize_EqualsPosition() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, new QName("prefix:attr1"));
        iterator.setPosition(1);
        int position = iterator.getPosition();
        
        assertEquals(1, position);
    }
    
    @Test
    public void testSetPosition_PositionExceedsSize_EqualsPosition() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, new QName("prefix:attr1"));
        iterator.setPosition(3);
        int position = iterator.getPosition();
        
        assertEquals(3, position);
    }
}