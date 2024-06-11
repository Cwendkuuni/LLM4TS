import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class PropertyPointerTest {
    
    private PropertyPointer propertyPointer;
    
    @Before
    public void setUp() {
        propertyPointer = new PropertyPointerImpl(new NodePointerImpl());
    }
    
    @Test
    public void testGetPropertyIndex() {
        propertyPointer.setPropertyIndex(1);
        assertEquals(1, propertyPointer.getPropertyIndex());
    }
    
    @Test
    public void testSetPropertyIndex() {
        propertyPointer.setPropertyIndex(1);
        assertEquals(1, propertyPointer.getPropertyIndex());
        propertyPointer.setPropertyIndex(2);
        assertEquals(2, propertyPointer.getPropertyIndex());
    }
    
    @Test
    public void testGetBean() {
        propertyPointer.setBean("test bean");
        assertEquals("test bean", propertyPointer.getBean());
    }
    
    @Test
    public void testGetName() {
        assertEquals(new QName(null, propertyPointer.getPropertyName()), propertyPointer.getName());
    }
    
    @Test
    public void testIsActual() {
        assertTrue(propertyPointer.isActual());
    }
    
    @Test
    public void testGetImmediateNode() {
        assertNull(propertyPointer.getImmediateNode());
        propertyPointer.setBaseValue("test value");
        assertEquals("test value", propertyPointer.getImmediateNode());
    }
    
    @Test
    public void testIsCollection() {
        assertFalse(propertyPointer.isCollection());
        
        propertyPointer.setBaseValue(new ArrayList<String>());
        assertTrue(propertyPointer.isCollection());
    }
    
    @Test
    public void testIsLeaf() {
        assertTrue(propertyPointer.isLeaf());
        
        propertyPointer.setBaseValue("test value");
        assertTrue(propertyPointer.isLeaf());
        
        propertyPointer.setBaseValue(new ArrayList<String>());
        assertFalse(propertyPointer.isLeaf());
    }
    
    @Test
    public void testGetLength() {
        assertEquals(-1, propertyPointer.getLength());
        
        propertyPointer.setBaseValue(new ArrayList<String>());
        assertEquals(0, propertyPointer.getLength());
        
        List<String> list = new ArrayList<String>();
        list.add("test value");
        propertyPointer.setBaseValue(list);
        assertEquals(1, propertyPointer.getLength());
    }
    
    @Test
    public void testGetImmediateValuePointer() {
        assertEquals(NodePointer.newChildNodePointer((NodePointer) propertyPointer.clone(), propertyPointer.getName(), propertyPointer.getImmediateNode()), propertyPointer.getImmediateValuePointer());
    }
    
    @Test
    public void testCreatePathWithExistingValue() {
        propertyPointer.setBaseValue("test value");
        assertEquals(propertyPointer, propertyPointer.createPath(new JXPathContext(), propertyPointer.getBaseValue()));
    }
    
    @Test
    public void testCreatePathWithNonExistingValue() {
        assertNull(propertyPointer.getImmediateNode());
        propertyPointer.createPath(new JXPathContext(), "test value");
        assertEquals("test value", propertyPointer.getImmediateNode());
    }
    
    @Test(expected = JXPathAbstractFactoryException.class)
    public void testCreatePathWithUnsuccessfulFactory() {
        propertyPointer.setBean("test bean");
        propertyPointer.createPath(new JXPathContext());
    }
    
    @Test
    public void testCreateChildWithValue() {
        propertyPointer.setBaseValue(new ArrayList<String>());
        NodePointer childPointer = propertyPointer.createChild(new JXPathContext(), new QName("test"), 0, "test value");
        assertEquals(NodePointer.newChildNodePointer((NodePointer) propertyPointer.clone(), new QName("test"), "value"), childPointer);
        assertEquals("test value", childPointer.getImmediateNode());
    }
    
    @Test
    public void testCreateChildWithoutValue() {
        propertyPointer.setBaseValue(new ArrayList<String>());
        NodePointer childPointer = propertyPointer.createChild(new JXPathContext(), new QName("test"), 0);
        assertEquals(NodePointer.newChildNodePointer((NodePointer) propertyPointer.clone(), new QName("test"), null), childPointer);
        assertNull(childPointer.getImmediateNode());
    }
    
    @Test
    public void testHashCode() {
        NodePointer parent = new NodePointerImpl();
        propertyPointer.setParent(parent);
        propertyPointer.setPropertyIndex(1);
        propertyPointer.setIndex(2);
        
        PropertyPointer other = new PropertyPointerImpl(new NodePointerImpl());
        other.setParent(parent);
        other.setPropertyIndex(1);
        other.setIndex(2);
        
        assertEquals(propertyPointer.hashCode(), other.hashCode());
    }
    
    @Test
    public void testEqualsWithSameObject() {
        assertTrue(propertyPointer.equals(propertyPointer));
    }
    
    @Test
    public void testEqualsWithDifferentClass() {
        assertFalse(propertyPointer.equals("test"));
    }
    
    @Test
    public void testEqualsWithDifferentParent() {
        PropertyPointer other = new PropertyPointerImpl(new NodePointerImpl());
        assertNotEquals(propertyPointer.getParent(), other.getParent());
        assertFalse(propertyPointer.equals(other));
    }
    
    @Test
    public void testEqualsWithDifferentPropertyIndex() {
        PropertyPointer other = new PropertyPointerImpl(new NodePointerImpl());
        propertyPointer.setPropertyIndex(1);
        other.setPropertyIndex(2);
        assertFalse(propertyPointer.equals(other));
    }
    
    @Test
    public void testEqualsWithDifferentPropertyName() {
        PropertyPointer other = new PropertyPointerImpl(new NodePointerImpl());
        propertyPointer.setPropertyName("test");
        other.setPropertyName("test2");
        assertFalse(propertyPointer.equals(other));
    }
    
    @Test
    public void testEqualsWithDifferentIndex() {
        PropertyPointer other = new PropertyPointerImpl(new NodePointerImpl());
        propertyPointer.setIndex(1);
        other.setIndex(2);
        assertFalse(propertyPointer.equals(other));
    }
    
    @Test
    public void testCompareChildNodePointers() {
        NodePointer pointer1 = new NodePointerImpl();
        NodePointer pointer2 = new NodePointerImpl();
        propertyPointer.setValuePointer(pointer1);
        assertEquals(pointer1.compareChildNodePointers(pointer1, pointer2), pointer2.compareChildNodePointers(pointer2, pointer1));
    }
    
    private class PropertyPointerImpl extends PropertyPointer {
        
        public PropertyPointerImpl(NodePointer parent) {
            super(parent);
        }

        public String getPropertyName() {
            return "test property";
        }
        
        public void setPropertyName(String propertyName) {
            // Do nothing
        }
        
        public int getPropertyCount() {
            return 0;
        }
        
        public String[] getPropertyNames() {
            return new String[0];
        }
        
        protected boolean isActualProperty() {
            return true;
        }

    }
    
    private class NodePointerImpl extends NodePointer {
        
        public NodePointerImpl() {
            super(null);
        }
        
        public Object getNode() {
            return null;
        }
        
        public CollectionIterator collectionIterator() {
            return null;
        }
        
        public PropertyPointer getPropertyPointer() {
            return null;
        }
        
        protected void ensureNode() {
            // Do nothing
        }
    }

}