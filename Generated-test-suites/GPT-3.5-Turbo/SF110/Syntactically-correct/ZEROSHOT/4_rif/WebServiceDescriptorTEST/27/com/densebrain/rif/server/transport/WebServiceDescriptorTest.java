import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WebServiceDescriptorTest {

    private WebServiceDescriptor webService;

    @Before
    public void setUp() {
        webService = new WebServiceDescriptor(null, null, null);
    }

    @Test
    public void testGetServiceClazz() {
        assertNull(webService.getServiceClazz());
    }

    @Test
    public void testSetServiceClazz() {
        webService.setServiceClazz(String.class);
        assertEquals(String.class, webService.getServiceClazz());
    }

    @Test
    public void testGetTargetNamespace() {
        assertNull(webService.getTargetNamespace());
    }

    @Test
    public void testSetTargetNamespace() {
        webService.setTargetNamespace("http://www.example.com");
        assertEquals("http://www.example.com", webService.getTargetNamespace());
    }

    @Test
    public void testGetTypesNamespace() {
        assertNull(webService.getTypesNamespace());
    }

    @Test
    public void testSetTypesNamespace() {
        webService.setTypesNamespace("http://www.example.com/types");
        assertEquals("http://www.example.com/types", webService.getTypesNamespace());
    }

    @Test
    public void testHashCode() {
        WebServiceDescriptor webService1 = new WebServiceDescriptor(null, null, null);
        WebServiceDescriptor webService2 = new WebServiceDescriptor(null, null, null);
        assertEquals(webService1.hashCode(), webService2.hashCode());
    }

    @Test
    public void testEquals() {
        WebServiceDescriptor webService1 = new WebServiceDescriptor(null, null, null);
        WebServiceDescriptor webService2 = new WebServiceDescriptor(null, null, null);
        assertTrue(webService1.equals(webService2));
    }
}