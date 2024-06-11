import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WebServiceDescriptorTest {
    
    private WebServiceDescriptor webServiceDescriptor;
    
    @Before
    public void setUp() {
        webServiceDescriptor = new WebServiceDescriptor(null, null, null);
    }
    
    @Test
    public void testGetServiceClazz() {
        assertNull(webServiceDescriptor.getServiceClazz());
    }
    
    @Test
    public void testSetServiceClazz() {
        Class<Object> expected = Object.class;
        webServiceDescriptor.setServiceClazz(expected);
        assertEquals(expected, webServiceDescriptor.getServiceClazz());
    }
    
    @Test
    public void testGetTargetNamespace() {
        assertNull(webServiceDescriptor.getTargetNamespace());
    }
    
    @Test
    public void testSetTargetNamespace() {
        String expected = "namespace";
        webServiceDescriptor.setTargetNamespace(expected);
        assertEquals(expected, webServiceDescriptor.getTargetNamespace());
    }
    
    @Test
    public void testGetTypesNamespace() {
        assertNull(webServiceDescriptor.getTypesNamespace());
    }
    
    @Test
    public void testSetTypesNamespace() {
        String expected = "typesNamespace";
        webServiceDescriptor.setTypesNamespace(expected);
        assertEquals(expected, webServiceDescriptor.getTypesNamespace());
    }
    
    @Test
    public void testHashCode() {
        webServiceDescriptor.setServiceClazz(Object.class);
        int expected = 31 * 1 + Object.class.hashCode();
        assertEquals(expected, webServiceDescriptor.hashCode());
    }
    
    @Test
    public void testEquals() {
        WebServiceDescriptor other = new WebServiceDescriptor(Object.class, null, null);
        assertTrue(webServiceDescriptor.equals(webServiceDescriptor));
        assertFalse(webServiceDescriptor.equals(null));
        assertFalse(webServiceDescriptor.equals(new Object()));
        assertTrue(webServiceDescriptor.equals(other));
    }
    
}