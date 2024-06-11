import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyPointerTest {

    @Test
    public void testGetPropertyIndex() {
        NodePointer parent = new NodePointer();
        PropertyPointer propertyPointer = new PropertyPointer(parent) {
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
        
        propertyPointer.setPropertyIndex(1);
        assertEquals(1, propertyPointer.getPropertyIndex());
    }

    @Test
    public void testGetBean() {
        NodePointer parent = new NodePointer();
        PropertyPointer propertyPointer = new PropertyPointer(parent) {
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
        
        propertyPointer.getBean();
        assertNotNull(propertyPointer.getBean());
    }

    @Test
    public void testGetName() {
        NodePointer parent = new NodePointer();
        PropertyPointer propertyPointer = new PropertyPointer(parent) {
            @Override
            public String getPropertyName() {
                return "testName";
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
        
        assertEquals("testName", propertyPointer.getName().toString());
    }

    @Test
    public void testIsActual() {
        NodePointer parent = new NodePointer();
        PropertyPointer propertyPointer = new PropertyPointer(parent) {
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
                return true;
            }
        };
        
        assertTrue(propertyPointer.isActual());
    }

    @Test
    public void testGetImmediateNode() {
        NodePointer parent = new NodePointer();
        PropertyPointer propertyPointer = new PropertyPointer(parent) {
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
        
        assertNull(propertyPointer.getImmediateNode());
    }

    @Test
    public void testIsCollection() {
        NodePointer parent = new NodePointer();
        PropertyPointer propertyPointer = new PropertyPointer(parent) {
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
        
        assertFalse(propertyPointer.isCollection());
    }

    @Test
    public void testIsLeaf() {
        NodePointer parent = new NodePointer();
        PropertyPointer propertyPointer = new PropertyPointer(parent) {
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
        
        assertTrue(propertyPointer.isLeaf());
    }

    @Test
    public void testGetLength() {
        NodePointer parent = new NodePointer();
        PropertyPointer propertyPointer = new PropertyPointer(parent) {
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
        
        assertEquals(-1, propertyPointer.getLength());
    }

}