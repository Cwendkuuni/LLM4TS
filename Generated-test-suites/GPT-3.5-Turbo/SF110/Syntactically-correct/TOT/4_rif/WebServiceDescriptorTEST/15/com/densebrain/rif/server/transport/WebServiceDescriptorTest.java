import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WebServiceDescriptorTest {

    private WebServiceDescriptor webServiceDescriptor;

    @Before
    public void setUp() {
        webServiceDescriptor = new WebServiceDescriptor(String.class, "http://www.example.com/target", "http://www.example.com/types");
    }

    @Test
    public void testGetServiceClazz() {
        assertEquals(String.class, webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testSetServiceClazz() {
        webServiceDescriptor.setServiceClazz(Integer.class);
        assertEquals(Integer.class, webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testGetTargetNamespace() {
        assertEquals("http://www.example.com/target", webServiceDescriptor.getTargetNamespace());
    }

    @Test
    public void testSetTargetNamespace() {
        webServiceDescriptor.setTargetNamespace("http://www.example.com/newtarget");
        assertEquals("http://www.example.com/newtarget", webServiceDescriptor.getTargetNamespace());
    }

    @Test
    public void testGetTypesNamespace() {
        assertEquals("http://www.example.com/types", webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void testSetTypesNamespace() {
        webServiceDescriptor.setTypesNamespace("http://www.example.com/newtypes");
        assertEquals("http://www.example.com/newtypes", webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void testHashCode() {
        WebServiceDescriptor otherWebServiceDescriptor = new WebServiceDescriptor(String.class, "http://www.example.com/target", "http://www.example.com/types");
        assertEquals(webServiceDescriptor.hashCode(), otherWebServiceDescriptor.hashCode());
        
        WebServiceDescriptor differentWebServiceDescriptor = new WebServiceDescriptor(String.class, "http://www.example.com/othertarget", "http://www.example.com/types");
        assertNotEquals(webServiceDescriptor.hashCode(), differentWebServiceDescriptor.hashCode());
    }

    @Test
    public void testEquals() {
        WebServiceDescriptor otherWebServiceDescriptor = new WebServiceDescriptor(String.class, "http://www.example.com/target", "http://www.example.com/types");
        assertTrue(webServiceDescriptor.equals(otherWebServiceDescriptor));
        
        WebServiceDescriptor sameClassDifferentTargetWebServiceDescriptor = new WebServiceDescriptor(String.class, "http://www.example.com/othertarget", "http://www.example.com/types");
        assertFalse(webServiceDescriptor.equals(sameClassDifferentTargetWebServiceDescriptor));
        
        WebServiceDescriptor differentClassSameTargetWebServiceDescriptor = new WebServiceDescriptor(Integer.class, "http://www.example.com/target", "http://www.example.com/types");
        assertFalse(webServiceDescriptor.equals(differentClassSameTargetWebServiceDescriptor));
        
        WebServiceDescriptor differentClassDifferentTargetWebServiceDescriptor = new WebServiceDescriptor(Integer.class, "http://www.example.com/othertarget", "http://www.example.com/types");
        assertFalse(webServiceDescriptor.equals(differentClassDifferentTargetWebServiceDescriptor));
        
        assertFalse(webServiceDescriptor.equals(null));
        assertFalse(webServiceDescriptor.equals("String"));
    }
}