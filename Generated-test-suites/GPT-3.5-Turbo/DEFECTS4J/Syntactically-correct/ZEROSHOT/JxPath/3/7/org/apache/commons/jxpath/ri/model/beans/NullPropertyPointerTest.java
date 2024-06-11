import org.junit.Test;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathInvalidAccessException;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.apache.commons.jxpath.ri.model.NodePointer;

import static org.junit.Assert.*;

public class NullPropertyPointerTest {
    
    @Test
    public void testGetName() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        
        QName expectedName = new QName("*");
        QName actualName = nullPropertyPointer.getName();
        
        assertEquals(expectedName, actualName);
    }
    
    @Test
    public void testSetPropertyIndex() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        
        int index = 5;
        nullPropertyPointer.setPropertyIndex(index);
        
        // No assertions required as the method has no return value
    }
    
    @Test
    public void testGetLength() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        
        int expectedLength = 0;
        int actualLength = nullPropertyPointer.getLength();
        
        assertEquals(expectedLength, actualLength);
    }
    
    @Test
    public void testGetBaseValue() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        
        Object expectedValue = null;
        Object actualValue = nullPropertyPointer.getBaseValue();
        
        assertEquals(expectedValue, actualValue);
    }
    
    @Test
    public void testGetImmediateNode() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        
        Object expectedNode = null;
        Object actualNode = nullPropertyPointer.getImmediateNode();
        
        assertEquals(expectedNode, actualNode);
    }
    
    @Test
    public void testIsLeaf() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        
        boolean expectedIsLeaf = true;
        boolean actualIsLeaf = nullPropertyPointer.isLeaf();
        
        assertEquals(expectedIsLeaf, actualIsLeaf);
    }
    
    @Test
    public void testGetValuePointer() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        
        NodePointer expectedPointer = new NullPointer(nullPropertyPointer, new QName(nullPropertyPointer.getPropertyName()));
        NodePointer actualPointer = nullPropertyPointer.getValuePointer();
        
        assertEquals(expectedPointer, actualPointer);
    }
    
    @Test
    public void testIsActualProperty() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        
        boolean expectedIsActualProperty = false;
        boolean actualIsActualProperty = nullPropertyPointer.isActualProperty();
        
        assertEquals(expectedIsActualProperty, actualIsActualProperty);
    }
    
    @Test
    public void testIsActual() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        
        boolean expectedIsActual = false;
        boolean actualIsActual = nullPropertyPointer.isActual();
        
        assertEquals(expectedIsActual, actualIsActual);
    }
    
    @Test
    public void testIsContainer() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        
        boolean expectedIsContainer = true;
        boolean actualIsContainer = nullPropertyPointer.isContainer();
        
        assertEquals(expectedIsContainer, actualIsContainer);
    }
    
    @Test
    public void testSetValue() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        
        Object value = new Object();
        
        try {
            nullPropertyPointer.setValue(value);
            fail("Expected JXPathInvalidAccessException to be thrown");
        } catch (JXPathInvalidAccessException e) {
            // JXPathInvalidAccessException expected to be thrown
        }
    }
    
    @Test
    public void testCreatePath() {
        JXPathContext context = new JXPathContext();
        
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        
        NodePointer expectedPointer = null;
        NodePointer actualPointer = nullPropertyPointer.createPath(context);
        
        assertEquals(expectedPointer, actualPointer);
    }
    
    @Test
    public void testCreatePathWithValue() {
        JXPathContext context = new JXPathContext();
        
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        
        Object value = new Object();
        
        NodePointer expectedPointer = null;
        NodePointer actualPointer = nullPropertyPointer.createPath(context, value);
        
        assertEquals(expectedPointer, actualPointer);
    }
    
    @Test
    public void testCreateChildWithNameAndIndex() {
        JXPathContext context = new JXPathContext();
        QName name = new QName("name");
        int index = 5;
        
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        
        NodePointer expectedPointer = null;
        NodePointer actualPointer = nullPropertyPointer.createChild(context, name, index);
        
        assertEquals(expectedPointer, actualPointer);
    }
    
    @Test
    public void testCreateChildWithNameIndexAndValue() {
        JXPathContext context = new JXPathContext();
        QName name = new QName("name");
        int index = 5;
        Object value = new Object();
        
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        
        NodePointer expectedPointer = null;
        NodePointer actualPointer = nullPropertyPointer.createChild(context, name, index, value);
        
        assertEquals(expectedPointer, actualPointer);
    }
    
    @Test
    public void testGetPropertyName() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        
        String expectedPropertyName = "*";
        String actualPropertyName = nullPropertyPointer.getPropertyName();
        
        assertEquals(expectedPropertyName, actualPropertyName);
    }
    
    @Test
    public void testSetPropertyName() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        
        String propertyName = "newPropertyName";
        nullPropertyPointer.setPropertyName(propertyName);
        
        // No assertions required as the method has no return value
    }

    @Test
    public void testSetNameAttributeValue() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        
        String attributeValue = "attributeValue";
        nullPropertyPointer.setNameAttributeValue(attributeValue);
        
        // No assertions required as the method has no return value
    }
    
    @Test
    public void testIsCollection() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        
        boolean expectedIsCollection = false;
        boolean actualIsCollection = nullPropertyPointer.isCollection();
        
        assertEquals(expectedIsCollection, actualIsCollection);
    }
    
    @Test
    public void testGetPropertyCount() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        
        int expectedPropertyCount = 0;
        int actualPropertyCount = nullPropertyPointer.getPropertyCount();
        
        assertEquals(expectedPropertyCount, actualPropertyCount);
    }
    
    @Test
    public void testGetPropertyNames() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        
        String[] expectedPropertyNames = new String[0];
        String[] actualPropertyNames = nullPropertyPointer.getPropertyNames();
        
        assertArrayEquals(expectedPropertyNames, actualPropertyNames);
    }
    
    @Test
    public void testAsPath() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        
        String expectedPath = nullPropertyPointer.getImmediateParentPointer().asPath() + "[@name='" + escape(nullPropertyPointer.getPropertyName()) + "']";
        String actualPath = nullPropertyPointer.asPath();
        
        assertEquals(expectedPath, actualPath);
    }
    
    private String escape(String string) {
        int index = string.indexOf('\'');
        while (index != -1) {
            string =
                string.substring(0, index)
                    + "&apos;"
                    + string.substring(index + 1);
            index = string.indexOf('\'');
        }
        index = string.indexOf('\"');
        while (index != -1) {
            string =
                string.substring(0, index)
                    + "&quot;"
                    + string.substring(index + 1);
            index = string.indexOf('\"');
        }
        return string;
    }
}