import static org.junit.Assert.*;
import org.junit.Test;

public class DOMAttributeIteratorTest {

    @Test
    public void testConstructor_NormalElementAndAttribute() {
        // Given
        NodePointer parent = new NodePointer();
        QName name = new QName("testNamespace", "testName");
        
        // When
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        
        // Then
        assertNotNull(iterator);
    }
    
    @Test
    public void testConstructor_ElementWithoutAttribute() {
        // Given
        NodePointer parent = new NodePointer();
        QName name = new QName("testNamespace", "testName");
        
        // When
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        
        // Then
        assertNotNull(iterator);
    }
    
    @Test
    public void testConstructor_ElementAndWildcardAttribute() {
        // Given
        NodePointer parent = new NodePointer();
        QName name = new QName("*", "*");
        
        // When
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        
        // Then
        assertNotNull(iterator);
    }
    
    @Test
    public void testTestAttr_NormalAttribute() {
        // Given
        Attr attr = new Attr();
        attr.setPrefix("testPrefix");
        attr.setLocalName("testName");
        QName testName = new QName("testNamespace", "testName");
        
        // When
        boolean result = DOMAttributeIterator.testAttr(attr, testName);
        
        // Then
        assertTrue(result);
    }
    
    @Test
    public void testTestAttr_NamespaceAttribute() {
        // Given
        Attr attr = new Attr();
        attr.setPrefix("xmlns");
        attr.setLocalName("testName");
        QName testName = new QName("testNamespace", "testName");
        
        // When
        boolean result = DOMAttributeIterator.testAttr(attr, testName);
        
        // Then
        assertFalse(result);
    }
    
    @Test
    public void testTestAttr_DefaultNamespaceAttribute() {
        // Given
        Attr attr = new Attr();
        attr.setLocalName("xmlns");
        QName testName = new QName("testNamespace", "testName");
        
        // When
        boolean result = DOMAttributeIterator.testAttr(attr, testName);
        
        // Then
        assertFalse(result);
    }
    
    @Test
    public void testEqualStrings_SameString() {
        // Given
        String s1 = "test";
        String s2 = "test";
        
        // When
        boolean result = DOMAttributeIterator.equalStrings(s1, s2);
        
        // Then
        assertTrue(result);
    }
    
    @Test
    public void testEqualStrings_DifferentStrings() {
        // Given
        String s1 = "test1";
        String s2 = "test2";
        
        // When
        boolean result = DOMAttributeIterator.equalStrings(s1, s2);
        
        // Then
        assertFalse(result);
    }
    
    @Test
    public void testEqualStrings_OneNullString() {
        // Given
        String s1 = null;
        String s2 = "test";
        
        // When
        boolean result = DOMAttributeIterator.equalStrings(s1, s2);
        
        // Then
        assertFalse(result);
    }
    
    @Test
    public void testGetAttribute_AttributeWithNamespace() {
        // Given
        Element element = new Element();
        element.setNamespace("testNamespace");
        QName name = new QName("testNamespace", "testName");
        
        // When
        Attr result = DOMAttributeIterator.getAttribute(element, name);
        
        // Then
        assertNotNull(result);
    }
    
    @Test
    public void testGetAttribute_AttributeWithoutNamespace() {
        // Given
        Element element = new Element();
        QName name = new QName("testNamespace", "testName");
        
        // When
        Attr result = DOMAttributeIterator.getAttribute(element, name);
        
        // Then
        assertNotNull(result);
    }
    
    @Test
    public void testGetAttribute_ElementWithoutAttribute() {
        // Given
        Element element = new Element();
        QName name = new QName("testNamespace", "testName");
        
        // When
        Attr result = DOMAttributeIterator.getAttribute(element, name);
        
        // Then
        assertNull(result);
    }
    
    @Test
    public void testGetNodePointer_InvalidPosition() {
        // Given
        DOMAttributeIterator iterator = new DOMAttributeIterator();
        iterator.attributes.add(new Attr());
        iterator.attributes.add(new Attr());
        
        // When
        NodePointer result = iterator.getNodePointer();
        
        // Then
        assertNull(result);
    }
    
    @Test
    public void testGetNodePointer_ValidPosition() {
        // Given
        DOMAttributeIterator iterator = new DOMAttributeIterator();
        Attr attr1 = new Attr();
        Attr attr2 = new Attr();
        iterator.attributes.add(attr1);
        iterator.attributes.add(attr2);
        
        // When
        NodePointer result = iterator.getNodePointer();
        
        // Then
        assertNotNull(result);
    }
    
    @Test
    public void testGetPosition_StartingPosition() {
        // Given
        DOMAttributeIterator iterator = new DOMAttributeIterator();
        
        // When
        int position = iterator.getPosition();
        
        // Then
        assertEquals(0, position);
    }
    
    @Test
    public void testGetPosition_AfterSettingPosition() {
        // Given
        DOMAttributeIterator iterator = new DOMAttributeIterator();
        iterator.setPosition(2);
        
        // When
        int position = iterator.getPosition();
        
        // Then
        assertEquals(2, position);
    }
    
    @Test
    public void testSetPosition_InvalidPosition() {
        // Given
        DOMAttributeIterator iterator = new DOMAttributeIterator();
        iterator.attributes.add(new Attr());
        
        // When
        boolean result = iterator.setPosition(2);
        
        // Then
        assertFalse(result);
    }
    
    @Test
    public void testSetPosition_ValidPosition() {
        // Given
        DOMAttributeIterator iterator = new DOMAttributeIterator();
        iterator.attributes.add(new Attr());
        
        // When
        boolean result = iterator.setPosition(1);
        
        // Then
        assertTrue(result);
    }
}