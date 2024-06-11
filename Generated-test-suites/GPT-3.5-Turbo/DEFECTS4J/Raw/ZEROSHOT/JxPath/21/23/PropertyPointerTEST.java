import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyPointerTest {
    
    // Define a mock parent pointer for testing
    private static class MockParentPointer extends NodePointer {
        public MockParentPointer() {
            super(null);
        }
        
        public Object getNode() {
            return new Object();
        }
    }
    
    private PropertyPointer propertyPointer;
    
    @Before
    public void setup() {
        propertyPointer = new PropertyPointer(new MockParentPointer()) {
            public String getPropertyName() {
                return "property";
            }
            
            public void setPropertyName(String propertyName) {
                // Do nothing
            }
            
            public int getPropertyCount() {
                return 1;
            }
            
            public String[] getPropertyNames() {
                return new String[] {"property"};
            }
            
            protected boolean isActualProperty() {
                return true;
            }
        };
    }
    
    @Test
    public void testGetPropertyIndex() {
        int expected = PropertyPointer.UNSPECIFIED_PROPERTY;
        int actual = propertyPointer.getPropertyIndex();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testSetPropertyIndex() {
        int expected = 0;
        propertyPointer.setPropertyIndex(expected);
        int actual = propertyPointer.getPropertyIndex();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetBean() {
        Object expected = propertyPointer.getImmediateParentPointer().getNode();
        Object actual = propertyPointer.getBean();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetName() {
        String expected = "property";
        String actual = propertyPointer.getName().getName();
        assertEquals(expected, actual);
    }
    
    // Implement the remaining test cases for all methods in the PropertyPointer class
    
}