import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.*;
import org.junit.Assert;

public class DOMAttributeIteratorTest {

    private Element element;
    private DOMAttributeIterator attributeIterator;

    @Before
    public void setUp() {
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        element = document.createElement("element");
        
        Attr attr1 = document.createAttribute("attr1");
        attr1.setValue("value1");
        element.setAttributeNode(attr1);
        
        Attr attr2 = document.createAttributeNS("http://www.example.com", "ns:attr2");
        attr2.setValue("value2");
        element.setAttributeNode(attr2);
        
        attributeIterator = new DOMAttributeIterator(new DOMNodePointer(element), new QName("", "attr1"));
    }

    @Test
    public void testGetNodePointer() {
        NodePointer nodePointer = attributeIterator.getNodePointer();
        String expectedLocalName = "attr1";
        String actualLocalName = ((Attr) nodePointer.getNode()).getLocalName();
        
        Assert.assertEquals(expectedLocalName, actualLocalName);
    }

    @Test
    public void testGetPosition() {
        int expectedPosition = 0;
        int actualPosition = attributeIterator.getPosition();
        
        Assert.assertEquals(expectedPosition, actualPosition);
    }
    
    @Test
    public void testSetPosition() {
        int position = 2;
        boolean success = attributeIterator.setPosition(position);
        
        Assert.assertTrue(success);
        
        NodePointer nodePointer = attributeIterator.getNodePointer();
        String expectedLocalName = "ns:attr2";
        String actualLocalName = ((Attr) nodePointer.getNode()).getLocalName();
        
        Assert.assertEquals(expectedLocalName, actualLocalName);
    }

}