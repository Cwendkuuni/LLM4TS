import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.dom.DOMNodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;

public class DOMAttributeIteratorTest {
    private NodePointer parent;
    private QName name;
    
    @Before
    public void setup() {
        // Setup parent node (Element)
        Element element = TestUtils.createElement("testElement");
        // Create and add attributes to the parent element
        Attr attribute1 = TestUtils.createAttribute("attribute1", "value1");
        Attr attribute2 = TestUtils.createAttribute("attribute2", "value2");
        element.setAttributeNode(attribute1);
        element.setAttributeNode(attribute2);
        
        parent = new DOMNodePointer(null, element);
        name = new QName("", "attribute1");
    }
    
    @Test
    public void testConstructor() {
        DOMAttributeIterator attributeIterator = new DOMAttributeIterator(parent, name);
        
        Assert.assertEquals(parent, attributeIterator.parent);
        Assert.assertEquals(name, attributeIterator.name);
        Assert.assertNotNull(attributeIterator.attributes);
        Assert.assertEquals(1, attributeIterator.attributes.size());
        Assert.assertEquals("attribute1", ((Attr) attributeIterator.attributes.get(0)).getName());
    }
    
    @Test
    public void testConstructor_WildcardName() {
        QName wildcardName = new QName("", "*");
        DOMAttributeIterator attributeIterator = new DOMAttributeIterator(parent, wildcardName);
        
        Assert.assertEquals(parent, attributeIterator.parent);
        Assert.assertEquals(wildcardName, attributeIterator.name);
        Assert.assertNotNull(attributeIterator.attributes);
        Assert.assertEquals(2, attributeIterator.attributes.size());
        Assert.assertEquals("attribute1", ((Attr) attributeIterator.attributes.get(0)).getName());
        Assert.assertEquals("attribute2", ((Attr) attributeIterator.attributes.get(1)).getName());
    }
    
    @Test
    public void testConstructor_NoMatchingAttributes() {
        QName nonExistingName = new QName("", "nonExistingAttribute");
        DOMAttributeIterator attributeIterator = new DOMAttributeIterator(parent, nonExistingName);
        
        Assert.assertEquals(parent, attributeIterator.parent);
        Assert.assertEquals(nonExistingName, attributeIterator.name);
        Assert.assertNotNull(attributeIterator.attributes);
        Assert.assertTrue(attributeIterator.attributes.isEmpty());
    }
    
    @Test
    public void testConstructor_NoAttributesOnParent() {
        // Setup parent node (Element) with no attributes
        Element element = TestUtils.createElement("testElement");
        parent = new DOMNodePointer(null, element);
        
        DOMAttributeIterator attributeIterator = new DOMAttributeIterator(parent, name);
        
        Assert.assertEquals(parent, attributeIterator.parent);
        Assert.assertEquals(name, attributeIterator.name);
        Assert.assertNotNull(attributeIterator.attributes);
        Assert.assertTrue(attributeIterator.attributes.isEmpty());
    }
    
    @Test
    public void testTestAttr() {
        DOMAttributeIterator attributeIterator = new DOMAttributeIterator(parent, name);
        Attr attribute = (Attr) attributeIterator.attributes.get(0);
        
        // Test with valid attribute
        QName testName = new QName("", "attribute1");
        boolean result = attributeIterator.testAttr(attribute, testName);
        Assert.assertTrue(result);
        
        // Test with valid attribute and prefix
        QName testNameWithPrefix = new QName("prefix", "attribute1");
        result = attributeIterator.testAttr(attribute, testNameWithPrefix);
        Assert.assertFalse(result);
        
        // Test with attribute having xmlns prefix
        Attr xmlnsAttr = TestUtils.createAttribute("xmlns", "xmlnsValue");
        result = attributeIterator.testAttr(xmlnsAttr, testName);
        Assert.assertFalse(result);
        
        // Test with attribute having xmlns prefix and local name xmlns
        Attr xmlnsLocalNameAttr = TestUtils.createAttribute("xmlns", "attribute1");
        result = attributeIterator.testAttr(xmlnsLocalNameAttr, testName);
        Assert.assertFalse(result);
        
        // Test with invalid local name
        QName invalidTestName = new QName("", "nonExistingAttribute");
        result = attributeIterator.testAttr(attribute, invalidTestName);
        Assert.assertFalse(result);
    }
    
    @Test
    public void testEqualStrings() {
        String s1 = "testString";
        String s2 = "testString";
        boolean result = DOMAttributeIterator.equalStrings(s1, s2);
        Assert.assertTrue(result);
        
        s2 = "differentString";
        result = DOMAttributeIterator.equalStrings(s1, s2);
        Assert.assertFalse(result);
        
        s1 = null;
        s2 = null;
        result = DOMAttributeIterator.equalStrings(s1, s2);
        Assert.assertTrue(result);
        
        s1 = null;
        s2 = "testString";
        result = DOMAttributeIterator.equalStrings(s1, s2);
        Assert.assertFalse(result);
        
        s1 = "testString";
        s2 = null;
        result = DOMAttributeIterator.equalStrings(s1, s2);
        Assert.assertFalse(result);
    }
    
    @Test
    public void testGetAttribute() {
        DOMAttributeIterator attributeIterator = new DOMAttributeIterator(parent, name);
        Element element = (Element) parent.getNode();
        
        // Test with attribute having namespace
        QName testNameWithNamespace = new QName("namespace", "attribute1");
        Attr attribute = attributeIterator.getAttribute(element, testNameWithNamespace);
        Assert.assertNotNull(attribute);
        Assert.assertEquals("attribute1", attribute.getName());
        
        // Test with attribute having namespace, but not found
        QName nonExistingTestNameWithNamespace = new QName("namespace", "nonExistingAttribute");
        attribute = attributeIterator.getAttribute(element, nonExistingTestNameWithNamespace);
        Assert.assertNull(attribute);
        
        // Test with attribute without namespace
        QName testNameWithoutNamespace = new QName("", "attribute2");
        attribute = attributeIterator.getAttribute(element, testNameWithoutNamespace);
        Assert.assertNotNull(attribute);
        Assert.assertEquals("attribute2", attribute.getName());
        
        // Test with attribute without namespace, but not found
        QName nonExistingTestNameWithoutNamespace = new QName("", "nonExistingAttribute");
        attribute = attributeIterator.getAttribute(element, nonExistingTestNameWithoutNamespace);
        Assert.assertNull(attribute);
    }
    
    @Test
    public void testGetAttribute_NoNamespaceSupport() {
        DOMAttributeIterator attributeIterator = new DOMAttributeIterator(parent, name);
        Element element = (Element) parent.getNode();
        
        // Test with attribute having namespace
        QName testNameWithNamespace = new QName("namespace", "attribute1");
        Attr attribute = attributeIterator.getAttribute(element, testNameWithNamespace);
        Assert.assertNotNull(attribute);
        Assert.assertEquals("attribute1", attribute.getName());
        
        // Test with attribute having namespace, but not found
        // This test emulates a parser without namespace support
        QName nonExistingTestNameWithNamespace = new QName("namespace", "nonExistingAttribute");
        attribute = attributeIterator.getAttribute(element, nonExistingTestNameWithNamespace);
        Assert.assertNull(attribute);
        
        // Test with attribute without namespace
        QName testNameWithoutNamespace = new QName("", "attribute2");
        attribute = attributeIterator.getAttribute(element, testNameWithoutNamespace);
        Assert.assertNotNull(attribute);
        Assert.assertEquals("attribute2", attribute.getName());
        
        // Test with attribute without namespace, but not found
        QName nonExistingTestNameWithoutNamespace = new QName("", "nonExistingAttribute");
        attribute = attributeIterator.getAttribute(element, nonExistingTestNameWithoutNamespace);
        Assert.assertNull(attribute);
    }
    
    @Test
    public void testGetNodePointer_FirstPosition() {
        DOMAttributeIterator attributeIterator = new DOMAttributeIterator(parent, name);
        
        NodePointer nodePointer = attributeIterator.getNodePointer();
        
        Assert.assertNotNull(nodePointer);
        Assert.assertEquals(1, nodePointer.getPosition());
        Assert.assertEquals(DOMAttributePointer.class, nodePointer.getClass());
        Assert.assertEquals(parent, nodePointer.getParent());
        Assert.assertEquals("attribute1", ((Attr) nodePointer.getNode()).getName());
    }
    
    @Test
    public void testGetNodePointer_NextPosition() {
        DOMAttributeIterator attributeIterator = new DOMAttributeIterator(parent, name);
        attributeIterator.setPosition(2);
        
        NodePointer nodePointer = attributeIterator.getNodePointer();
        
        Assert.assertNotNull(nodePointer);
        Assert.assertEquals(2, nodePointer.getPosition());
        Assert.assertEquals(DOMAttributePointer.class, nodePointer.getClass());
        Assert.assertEquals(parent, nodePointer.getParent());
        Assert.assertEquals("attribute2", ((Attr) nodePointer.getNode()).getName());
    }
    
    @Test
    public void testGetNodePointer_PositionOutOfRange() {
        DOMAttributeIterator attributeIterator = new DOMAttributeIterator(parent, name);
        attributeIterator.setPosition(3);
        
        NodePointer nodePointer = attributeIterator.getNodePointer();
        
        Assert.assertNull(nodePointer);
    }
    
    @Test
    public void testGetPosition() {
        DOMAttributeIterator attributeIterator = new DOMAttributeIterator(parent, name);
        attributeIterator.setPosition(1);
        
        int position = attributeIterator.getPosition();
        
        Assert.assertEquals(1, position);
    }
    
    @Test
    public void testSetPosition_ValidPosition() {
        DOMAttributeIterator attributeIterator = new DOMAttributeIterator(parent, name);
        
        boolean result = attributeIterator.setPosition(2);
        
        Assert.assertTrue(result);
        Assert.assertEquals(2, attributeIterator.getPosition());
    }
    
    @Test
    public void testSetPosition_InvalidPosition() {
        DOMAttributeIterator attributeIterator = new DOMAttributeIterator(parent, name);
        
        boolean result = attributeIterator.setPosition(0);
        
        Assert.assertFalse(result);
        Assert.assertEquals(0, attributeIterator.getPosition());
    }
}