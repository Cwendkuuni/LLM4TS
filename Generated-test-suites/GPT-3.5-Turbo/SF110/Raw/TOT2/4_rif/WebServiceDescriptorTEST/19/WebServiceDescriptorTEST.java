import org.junit.Test;
import static org.junit.Assert.*;

public class WebServiceDescriptorTest {

    @Test
    public void testConstructor() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(String.class, "http://example.com", "http://types.example.com");
        assertNotNull(webServiceDescriptor);
        assertEquals(String.class, webServiceDescriptor.getServiceClazz());
        assertEquals("http://example.com", webServiceDescriptor.getTargetNamespace());
        assertEquals("http://types.example.com", webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void testSetServiceClazz() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, "", "");
        webServiceDescriptor.setServiceClazz(Integer.class);
        assertEquals(Integer.class, webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testSetTargetNamespace() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, "", "");
        webServiceDescriptor.setTargetNamespace("http://new.example.com");
        assertEquals("http://new.example.com", webServiceDescriptor.getTargetNamespace());
    }

    @Test
    public void testSetTypesNamespace() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, "", "");
        webServiceDescriptor.setTypesNamespace("http://new.types.example.com");
        assertEquals("http://new.types.example.com", webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void testEquals() {
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(Integer.class, "http://example.com", "http://types.example.com");
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(Integer.class, "http://example.com", "http://types.example.com");
        assertEquals(webServiceDescriptor1, webServiceDescriptor2);
    }
    
    @Test
    public void testHashCode() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(Integer.class, "http://example.com", "http://types.example.com");
        assertEquals(31 * 1 + Integer.class.hashCode(), webServiceDescriptor.hashCode());
    }
}