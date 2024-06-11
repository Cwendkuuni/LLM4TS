import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyPointerTest {
    
    @Test
    public void testGetPropertyIndex() {
        NodePointer parent = new PropertyOwnerNodePointer();
        PropertyPointer pointer = new ConcretePropertyPointer(parent);
        
        int expected = PropertyPointer.UNSPECIFIED_PROPERTY;
        int actual = pointer.getPropertyIndex();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testSetPropertyIndex() {
        NodePointer parent = new PropertyOwnerNodePointer();
        PropertyPointer pointer = new ConcretePropertyPointer(parent);
        
        int index = 1;
        pointer.setPropertyIndex(index);
        
        int expected = index;
        int actual = pointer.getPropertyIndex();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetBean() {
        NodePointer parent = new PropertyOwnerNodePointer();
        PropertyPointer pointer = new ConcretePropertyPointer(parent);
        
        Object bean = new Object();
        parent.setValue(bean);
        
        Object expected = bean;
        Object actual = pointer.getBean();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetName() {
        NodePointer parent = new PropertyOwnerNodePointer();
        PropertyPointer pointer = new ConcretePropertyPointer(parent);
        
        QName expected = new QName(null, "property");
        QName actual = pointer.getName();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsActual() {
        NodePointer parent = new PropertyOwnerNodePointer();
        PropertyPointer pointer = new ConcretePropertyPointer(parent);
        
        boolean expected = true;
        boolean actual = pointer.isActual();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetImmediateNode() {
        NodePointer parent = new PropertyOwnerNodePointer();
        PropertyPointer pointer = new ConcretePropertyPointer(parent);
        
        Object value = new Object();
        parent.setValue(value);
        
        Object expected = value;
        Object actual = pointer.getImmediateNode();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsCollection() {
        NodePointer parent = new PropertyOwnerNodePointer();
        PropertyPointer pointer = new ConcretePropertyPointer(parent);
        
        boolean expected = false;
        boolean actual = pointer.isCollection();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsLeaf() {
        NodePointer parent = new PropertyOwnerNodePointer();
        PropertyPointer pointer = new ConcretePropertyPointer(parent);
        
        boolean expected = true;
        boolean actual = pointer.isLeaf();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetLength() {
        NodePointer parent = new PropertyOwnerNodePointer();
        PropertyPointer pointer = new ConcretePropertyPointer(parent);
        
        int expected = -1;
        int actual = pointer.getLength();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetImmediateValuePointer() {
        NodePointer parent = new PropertyOwnerNodePointer();
        PropertyPointer pointer = new ConcretePropertyPointer(parent);
        
        NodePointer expected = new ChildNodePointer(pointer, new QName(null, "property"), pointer.getImmediateNode());
        NodePointer actual = pointer.getImmediateValuePointer();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCreatePath() {
        NodePointer parent = new PropertyOwnerNodePointer();
        PropertyPointer pointer = new ConcretePropertyPointer(parent);
        
        JXPathContext context = new JXPathContext(null);
        Object bean = new Object();
        parent.setValue(bean);
        
        NodePointer expected = pointer;
        NodePointer actual = pointer.createPath(context);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCreatePathWithValue() {
        NodePointer parent = new PropertyOwnerNodePointer();
        PropertyPointer pointer = new ConcretePropertyPointer(parent);
        
        JXPathContext context = new JXPathContext(null);
        Object value = new Object();
        
        NodePointer expected = pointer;
        NodePointer actual = pointer.createPath(context, value);
        
        assertEquals(expected, actual);
        
        Object expectedValue = value;
        Object actualValue = pointer.getNode();
        
        assertEquals(expectedValue, actualValue);
    }
    
    @Test
    public void testCreateChildWithPathAndValue() {
        NodePointer parent = new PropertyOwnerNodePointer();
        PropertyPointer pointer = new ConcretePropertyPointer(parent);
        
        JXPathContext context = new JXPathContext(null);
        QName name = new QName(null, "child");
        int index = 0;
        Object value = new Object();
        
        NodePointer expected = new ConcretePropertyPointer(pointer, name, index, value);
        NodePointer actual = pointer.createChild(context, name, index, value);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCreateChildWithPathAndNoValue() {
        NodePointer parent = new PropertyOwnerNodePointer();
        PropertyPointer pointer = new ConcretePropertyPointer(parent);
        
        JXPathContext context = new JXPathContext(null);
        QName name = new QName(null, "child");
        int index = 0;
        
        NodePointer expected = new ConcretePropertyPointer(pointer, name, index);
        NodePointer actual = pointer.createChild(context, name, index);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testHashCode() {
        NodePointer parent = new PropertyOwnerNodePointer();
        PropertyPointer pointer = new ConcretePropertyPointer(parent);
        
        int expected = parent.hashCode() + pointer.getPropertyIndex() + pointer.getIndex();
        int actual = pointer.hashCode();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testEqualsWithSameObject() {
        NodePointer parent = new PropertyOwnerNodePointer();
        PropertyPointer pointer = new ConcretePropertyPointer(parent);
        
        boolean expected = true;
        boolean actual = pointer.equals(pointer);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testEqualsWithNullObject() {
        NodePointer parent = new PropertyOwnerNodePointer();
        PropertyPointer pointer = new ConcretePropertyPointer(parent);
        
        boolean expected = false;
        boolean actual = pointer.equals(null);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testEqualsWithDifferentClassObject() {
        NodePointer parent = new PropertyOwnerNodePointer();
        PropertyPointer pointer = new ConcretePropertyPointer(parent);
        
        boolean expected = false;
        boolean actual = pointer.equals(new Object());
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testEqualsWithDifferentParent() {
        NodePointer parent1 = new PropertyOwnerNodePointer();
        NodePointer parent2 = new PropertyOwnerNodePointer();
        PropertyPointer pointer1 = new ConcretePropertyPointer(parent1);
        PropertyPointer pointer2 = new ConcretePropertyPointer(parent2);
        
        boolean expected = false;
        boolean actual = pointer1.equals(pointer2);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testEqualsWithDifferentPropertyIndex() {
        NodePointer parent = new PropertyOwnerNodePointer();
        PropertyPointer pointer1 = new ConcretePropertyPointer(parent);
        PropertyPointer pointer2 = new ConcretePropertyPointer(parent);
        
        pointer1.setPropertyIndex(1);
        
        boolean expected = false;
        boolean actual = pointer1.equals(pointer2);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testEqualsWithDifferentPropertyName() {
        NodePointer parent = new PropertyOwnerNodePointer();
        PropertyPointer pointer1 = new ConcretePropertyPointer(parent);
        PropertyPointer pointer2 = new ConcretePropertyPointer(parent);
        
        pointer1.setPropertyName("property");
        
        boolean expected = false;
        boolean actual = pointer1.equals(pointer2);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testEqualsWithDifferentIndex() {
        NodePointer parent = new PropertyOwnerNodePointer();
        PropertyPointer pointer1 = new ConcretePropertyPointer(parent);
        PropertyPointer pointer2 = new ConcretePropertyPointer(parent);
        
        pointer1.setIndex(1);
        
        boolean expected = false;
        boolean actual = pointer1.equals(pointer2);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testEqualsWithSameParentPropertyIndexPropertyNameAndIndex() {
        NodePointer parent = new PropertyOwnerNodePointer();
        PropertyPointer pointer1 = new ConcretePropertyPointer(parent);
        PropertyPointer pointer2 = new ConcretePropertyPointer(parent);
        
        boolean expected = true;
        boolean actual = pointer1.equals(pointer2);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCompareChildNodePointers() {
        NodePointer pointer = new PropertyOwnerNodePointer();
        NodePointer pointer1 = new PropertyOwnerNodePointer();
        NodePointer pointer2 = new PropertyOwnerNodePointer();
        
        int expected = pointer1.compareChildNodePointers(pointer, pointer2);
        int actual = pointer.compareChildNodePointers(pointer1, pointer2);
        
        assertEquals(expected, actual);
    }
    
    private class ConcretePropertyPointer extends PropertyPointer {
        
        public ConcretePropertyPointer(NodePointer parent) {
            super(parent);
        }
        
        public String getPropertyName() {
        	return "property";
        }
        
        public void setPropertyName(String propertyName) {
        	
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
}