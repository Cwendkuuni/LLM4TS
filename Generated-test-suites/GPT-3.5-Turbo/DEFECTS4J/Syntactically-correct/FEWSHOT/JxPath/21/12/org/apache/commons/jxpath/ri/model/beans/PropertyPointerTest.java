import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.JXPathAbstractFactoryException;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.junit.Before;
import org.junit.Test;

public class PropertyPointerTest {
    
    private PropertyPointer propertyPointer;
    private JXPathContext context;
    
    @Before
    public void setUp() {
        propertyPointer = new PropertyPointer(null) {
            @Override
            public String getPropertyName() {
                return null;
            }

            @Override
            public void setPropertyName(String propertyName) {

            }

            @Override
            public int getPropertyCount() {
                return 0;
            }

            @Override
            public String[] getPropertyNames() {
                return null;
            }

            @Override
            protected boolean isActualProperty() {
                return false;
            }
        };
        
        context = JXPathContext.newContext(null);
    }
    
    @Test
    public void testGetPropertyIndex() {
        assertEquals(PropertyPointer.UNSPECIFIED_PROPERTY, propertyPointer.getPropertyIndex());
    }
    
    @Test
    public void testSetPropertyIndex() {
        propertyPointer.setPropertyIndex(1);
        assertEquals(1, propertyPointer.getPropertyIndex());
    }
    
    @Test
    public void testGetBeanWhenBeanIsNull() {
        assertNull(propertyPointer.getBean());
    }
    
    @Test
    public void testIsActualWhenNotActualProperty() {
        assertTrue(!propertyPointer.isActual());
    }
    
    @Test
    public void testGetImmediateNode() {
        assertNull(propertyPointer.getImmediateNode());
    }
    
    @Test
    public void testIsCollection() {
        assertTrue(!propertyPointer.isCollection());
    }
    
    @Test
    public void testIsLeaf() {
        assertTrue(propertyPointer.isLeaf());
    }
    
    @Test
    public void testGetLength() {
        assertEquals(-1, propertyPointer.getLength());
    }
    
    @Test
    public void testGetImmediateValuePointer() {
        assertNull(propertyPointer.getImmediateValuePointer());
    }
    
    @Test(expected = JXPathAbstractFactoryException.class)
    public void testCreatePath() {
        propertyPointer.createPath(context);
    }
    
    @Test(expected = JXPathAbstractFactoryException.class)
    public void testCreatePathWithValue() {
        propertyPointer.createPath(context, "test");
    }
    
    @Test
    public void testCreateChildWithIndexAndValue() {
        PropertyPointer childPointer = (PropertyPointer) propertyPointer.createChild(context, null, 0, "test");
        assertEquals(propertyPointer.getImmediateParentPointer().hashCode(), childPointer.getImmediateParentPointer().hashCode());
        assertEquals(propertyPointer.getPropertyIndex(), childPointer.getPropertyIndex());
        assertEquals(propertyPointer.getPropertyName(), childPointer.getPropertyName());
        assertEquals(0, childPointer.getIndex());
        assertEquals("test", childPointer.getNode());
    }
    
    @Test
    public void testCreateChildWithIndex() {
        PropertyPointer childPointer = (PropertyPointer) propertyPointer.createChild(context, null, 0);
        assertEquals(propertyPointer.getImmediateParentPointer().hashCode(), childPointer.getImmediateParentPointer().hashCode());
        assertEquals(propertyPointer.getPropertyIndex(), childPointer.getPropertyIndex());
        assertEquals(propertyPointer.getPropertyName(), childPointer.getPropertyName());
        assertEquals(0, childPointer.getIndex());
        assertNull(childPointer.getNode());
    }
    
    @Test
    public void testHashCode() {
        assertEquals(propertyPointer.getImmediateParentPointer().hashCode() + propertyPointer.getPropertyIndex() + propertyPointer.getIndex(), propertyPointer.hashCode());
    }
    
    @Test
    public void testEqualsWhenSameObject() {
        assertTrue(propertyPointer.equals(propertyPointer));
    }
    
    @Test
    public void testEqualsWhenDifferentClass() {
        assertTrue(!propertyPointer.equals(new Object()));
    }
    
    @Test
    public void testEqualsWhenDifferentParent() {
        PropertyPointer other = new PropertyPointer(null) {
            @Override
            public String getPropertyName() {
                return null;
            }

            @Override
            public void setPropertyName(String propertyName) {

            }

            @Override
            public int getPropertyCount() {
                return 0;
            }

            @Override
            public String[] getPropertyNames() {
                return null;
            }

            @Override
            protected boolean isActualProperty() {
                return false;
            }
        };
        
        assertTrue(!propertyPointer.equals(other));
    }
    
    @Test
    public void testEqualsWhenDifferentPropertyIndex() {
        PropertyPointer other = new PropertyPointer(propertyPointer.getImmediateParentPointer()) {
            @Override
            public String getPropertyName() {
                return getPropertyName();
            }

            @Override
            public void setPropertyName(String propertyName) {

            }

            @Override
            public int getPropertyCount() {
                return 0;
            }

            @Override
            public String[] getPropertyNames() {
                return null;
            }

            @Override
            protected boolean isActualProperty() {
                return false;
            }
        };
        
        other.setPropertyIndex(1);
        
        assertTrue(!propertyPointer.equals(other));
    }
    
    @Test
    public void testEqualsWhenDifferentPropertyName() {
        PropertyPointer other = new PropertyPointer(propertyPointer.getImmediateParentPointer()) {
            @Override
            public String getPropertyName() {
                return "test";
            }

            @Override
            public void setPropertyName(String propertyName) {

            }

            @Override
            public int getPropertyCount() {
                return 0;
            }

            @Override
            public String[] getPropertyNames() {
                return null;
            }

            @Override
            protected boolean isActualProperty() {
                return false;
            }
        };
        
        assertTrue(!propertyPointer.equals(other));
    }
    
    @Test
    public void testEqualsWhenDifferentIndex() {
        PropertyPointer other = new PropertyPointer(propertyPointer.getImmediateParentPointer()) {
            @Override
            public String getPropertyName() {
                return getPropertyName();
            }

            @Override
            public void setPropertyName(String propertyName) {

            }

            @Override
            public int getPropertyCount() {
                return 0;
            }

            @Override
            public String[] getPropertyNames() {
                return null;
            }

            @Override
            protected boolean isActualProperty() {
                return false;
            }
        };
        
        other.setIndex(1);
        
        assertTrue(!propertyPointer.equals(other));
    }
    
    @Test
    public void testCompareChildNodePointers() {
        PropertyPointer pointer1 = new PropertyPointer(null) {
            @Override
            public String getPropertyName() {
                return null;
            }

            @Override
            public void setPropertyName(String propertyName) {

            }

            @Override
            public int getPropertyCount() {
                return 0;
            }

            @Override
            public String[] getPropertyNames() {
                return null;
            }

            @Override
            protected boolean isActualProperty() {
                return false;
            }
        };
        PropertyPointer pointer2 = new PropertyPointer(null) {
            @Override
            public String getPropertyName() {
                return null;
            }

            @Override
            public void setPropertyName(String propertyName) {

            }

            @Override
            public int getPropertyCount() {
                return 0;
            }

            @Override
            public String[] getPropertyNames() {
                return null;
            }

            @Override
            protected boolean isActualProperty() {
                return false;
            }
        };
        
        assertTrue(pointer1.compareChildNodePointers(pointer1, pointer2) == 0);
    }
}