
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WebServiceDescriptorTest {

    private WebServiceDescriptor webServiceDescriptor;
    private Class serviceClazz;
    private String targetNamespace;
    private String typesNamespace;

    @Before
    public void setUp() {
        serviceClazz = WebServiceDescriptor.class;
        targetNamespace = "http://www.example.com/";
        typesNamespace = "http://www.example.com/types/";
        webServiceDescriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
    }
    
    @Test
    public void testGetServiceClazz() {
        assertEquals(serviceClazz, webServiceDescriptor.getServiceClazz());
    }
    
    @Test
    public void testSetServiceClazz() {
        Class newServiceClazz = String.class;
        webServiceDescriptor.setServiceClazz(newServiceClazz);
        assertEquals(newServiceClazz, webServiceDescriptor.getServiceClazz());
    }
    
    @Test
    public void testGetTargetNamespace() {
        assertEquals(targetNamespace, webServiceDescriptor.getTargetNamespace());
    }
    
    @Test
    public void testSetTargetNamespace() {
        String newTargetNamespace = "http://www.newexample.com/";
        webServiceDescriptor.setTargetNamespace(newTargetNamespace);
        assertEquals(newTargetNamespace, webServiceDescriptor.getTargetNamespace());
    }
    
    @Test
    public void testGetTypesNamespace() {
        assertEquals(typesNamespace, webServiceDescriptor.getTypesNamespace());
    }
    
    @Test
    public void testSetTypesNamespace() {
        String newTypesNamespace = "http://www.newexample.com/types/";
        webServiceDescriptor.setTypesNamespace(newTypesNamespace);
        assertEquals(newTypesNamespace, webServiceDescriptor.getTypesNamespace());
    }
    
    @Test
    public void testHashCode() {
        int expectedHashCode = 31 * 1 + ((serviceClazz == null) ? 0 : serviceClazz.hashCode());
        assertEquals(expectedHashCode, webServiceDescriptor.hashCode());
    }
    
    @Test
    public void testEquals() {
        WebServiceDescriptor sameWebServiceDescriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        assertTrue(webServiceDescriptor.equals(sameWebServiceDescriptor));
    }
    
    @Test
    public void testNotEquals() {
        Class differentServiceClazz = String.class;
        WebServiceDescriptor differentWebServiceDescriptor = new WebServiceDescriptor(differentServiceClazz, targetNamespace, typesNamespace);
        assertFalse(webServiceDescriptor.equals(differentWebServiceDescriptor));
    }
}

