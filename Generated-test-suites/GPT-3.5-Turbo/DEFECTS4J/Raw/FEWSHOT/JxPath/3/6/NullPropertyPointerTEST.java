package org.apache.commons.jxpath.ri.model.beans;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathException;
import org.apache.commons.jxpath.JXPathInvalidAccessException;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.junit.Before;
import org.junit.Test;

public class NullPropertyPointerTest {
    private NullPropertyPointer pointer;
    private NodePointer parent;
    
    @Before
    public void setup() {
        parent = new NodePointer() {
            public boolean isCollection() {
                return false;
            }

            public boolean isLeaf() {
                return true;
            }

            public QName getName() {
                return new QName("parent");
            }

            public Object getValue() {
                return null;
            }

            public void setValue(Object value) {
            }

            public Object getImmediateNode() {
                return null;
            }

            public NodePointer getValuePointer() {
                return null;
            }

            public int getLength() {
                return 0;
            }

            public int getIndex() {
                return 0;
            }

            public NodePointer getImmediateParentPointer() {
                return null;
            }

            public Iterator childIterator(QName name) {
                return null;
            }

            public NodeIterator attributeIterator(QName name) {
                return null;
            }

            public boolean testNode(NodeTest nodeTest) {
                return false;
            }

            public NodePointer getAttributePointer(QName name) {
                return null;
            }

            public QName getAttributeQName(int index) {
                return null;
            }

            public Object getAttribute(int index) {
                return null;
            }

            public String getAttributeStringValue(int index) {
                return null;
            }

            public boolean isAttribute() {
                return false;
            }

            public boolean isNamespace() {
                return false;
            }

            public boolean isCollectionPointer() {
                return false;
            }

            public boolean getValueAsBoolean() {
                return false;
            }

            public double getValueAsNumber() {
                return 0;
            }

            public String getValueAsString() {
                return null;
            }

            public String asPath() {
                return null;
            }

            public int compareChildNodePointers(NodePointer pointer1, NodePointer pointer2) {
                return 0;
            }

            public int compareNode(NodeTest nodeTest, QName name, boolean isElement) {
                return 0;
            }

            public boolean isNumeric() {
                return false;
            }

            public boolean isActual() {
                return false;
            }
        };

        pointer = new NullPropertyPointer(parent);
    }
    
    @Test
    public void testGetName() {
        QName name = pointer.getName();
        assertEquals("*", name.toString());
    }
    
    @Test
    public void testGetPropertyIndex() {
        pointer.setPropertyIndex(0);
        assertFalse(pointer.isCollection());
    }
    
    @Test
    public void testGetLength() {
        assertEquals(0, pointer.getLength());
    }
    
    @Test
    public void testGetBaseValue() {
        assertNull(pointer.getBaseValue());
    }
    
    @Test
    public void testGetImmediateNode() {
        assertNull(pointer.getImmediateNode());
    }
    
    @Test
    public void testIsLeaf() {
        assertTrue(pointer.isLeaf());
    }
    
    @Test
    public void testGetValuePointer() {
        NodePointer valuePointer = pointer.getValuePointer();
        assertTrue(valuePointer instanceof NullPointer);
    }
    
    @Test
    public void testIsActualProperty() {
        assertFalse(pointer.isActualProperty());
    }
    
    @Test
    public void testIsActual() {
        assertFalse(pointer.isActual());
    }
    
    @Test
    public void testIsContainer() {
        assertTrue(pointer.isContainer());
    }
    
    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValue_nullParent() {
        pointer = new NullPropertyPointer(null);
        pointer.setValue("test value");
    }
    
    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValue_nonContainerParent() {
        NodePointer nonContainerParent = new NodePointer() {
            public boolean isCollection() {
                return false;
            }

            public boolean isLeaf() {
                return true;
            }

            public QName getName() {
                return new QName("parent");
            }

            public Object getValue() {
                return "parent value";
            }

            public void setValue(Object value) {
            }

            public Object getImmediateNode() {
                return null;
            }

            public NodePointer getValuePointer() {
                return null;
            }

            public int getLength() {
                return 0;
            }

            public int getIndex() {
                return 0;
            }

            public NodePointer getImmediateParentPointer() {
                return null;
            }

            public Iterator childIterator(QName name) {
                return null;
            }

            public NodeIterator attributeIterator(QName name) {
                return null;
            }

            public boolean testNode(NodeTest nodeTest) {
                return false;
            }

            public NodePointer getAttributePointer(QName name) {
                return null;
            }

            public QName getAttributeQName(int index) {
                return null;
            }

            public Object getAttribute(int index) {
                return null;
            }

            public String getAttributeStringValue(int index) {
                return null;
            }

            public boolean isAttribute() {
                return false;
            }

            public boolean isNamespace() {
                return false;
            }

            public boolean isCollectionPointer() {
                return false;
            }

            public boolean getValueAsBoolean() {
                return false;
            }

            public double getValueAsNumber() {
                return 0;
            }

            public String getValueAsString() {
                return null;
            }

            public String asPath() {
                return null;
            }

            public int compareChildNodePointers(NodePointer pointer1, NodePointer pointer2) {
                return 0;
            }

            public int compareNode(NodeTest nodeTest, QName name, boolean isElement) {
                return 0;
            }

            public boolean isNumeric() {
                return false;
            }

            public boolean isActual() {
                return false;
            }
        };
        
        pointer = new NullPropertyPointer(nonContainerParent);
        pointer.setValue("test value");
    }
    
    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValue_dynamicPropertyDeclarationSupported() {
        NodePointer propertyOwnerParent = new PropertyOwnerPointer(parent) {
            public PropertyPointer getPropertyPointer() {
                return this;
            }
            
            public boolean isDynamicPropertyDeclarationSupported() {
                return true;
            }
        };
        pointer = new NullPropertyPointer(propertyOwnerParent);
        pointer.setValue("test value");
    }
    
    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValue_notMatchingChangeableLocation() {
        NodePointer propertyOwnerParent = new PropertyOwnerPointer(parent) {
            public PropertyPointer getPropertyPointer() {
                return this;
            }
            
            public boolean isDynamicPropertyDeclarationSupported() {
                return false;
            }
        };
        pointer = new NullPropertyPointer(propertyOwnerParent);
        pointer.setValue("test value");
    }
    
    @Test
    public void testCreatePath() {
        JXPathContext context = JXPathContext.newContext(parent);
        
        NodePointer newPath = pointer.createPath(context);
        assertTrue(newPath instanceof NullPropertyPointer);
        assertEquals("*", newPath.getName().toString());
    }
    
    @Test
    public void testCreatePath_withValue() {
        JXPathContext context = JXPathContext.newContext(parent);
        Object value = "test value";
        
        NodePointer newPath = pointer.createPath(context, value);
        assertTrue(newPath instanceof NullPropertyPointer);
        assertEquals("*", newPath.getName().toString());
    }
    
    @Test
    public void testCreateChild() {
        JXPathContext context = JXPathContext.newContext(parent);
        QName name = new QName("child");
        int index = 1;
        
        NodePointer newPath = pointer.createChild(context, name, index);
        assertTrue(newPath instanceof NullPropertyPointer);
        assertEquals("*", newPath.getName().toString());
    }
    
    @Test
    public void testCreateChild_withValue() {
        JXPathContext context = JXPathContext.newContext(parent);
        QName name = new QName("child");
        int index = 1;
        Object value = "test value";
        
        NodePointer newPath = pointer.createChild(context, name, index, value);
        assertTrue(newPath instanceof NullPropertyPointer);
        assertEquals("*", newPath.getName().toString());
    }
    
    @Test
    public void testGetPropertyName() {
        assertEquals("*", pointer.getPropertyName());
    }
    
    @Test
    public void testSetPropertyName() {
        String propertyName = "test property";
        pointer.setPropertyName(propertyName);
        assertEquals(propertyName, pointer.getPropertyName());
    }
    
    @Test
    public void testSetNameAttributeValue() {
        String attributeValue = "test attribute";
        pointer.setNameAttributeValue(attributeValue);
        assertEquals(attributeValue, pointer.getPropertyName());
        assertTrue(pointer.isAttribute());
    }
    
    @Test
    public void testIsCollection() {
        assertFalse(pointer.isCollection());
    }
    
    @Test
    public void testGetPropertyCount() {
        assertEquals(0, pointer.getPropertyCount());
    }
    
    @Test
    public void testGetPropertyNames() {
        String[] propertyNames = pointer.getPropertyNames();
        assertEquals(0, propertyNames.length);
    }
    
    @Test
    public void testAsPath() {
        String path = pointer.asPath();
        assertEquals("*", path);
    }
    
    @Test
    public void testEscape() {
        String string = "test 'quote' \"quote\"";
        String escapedString = pointer.escape(string);
        assertEquals("test &apos;quote&apos; &quot;quote&quot;", escapedString);
    }
}