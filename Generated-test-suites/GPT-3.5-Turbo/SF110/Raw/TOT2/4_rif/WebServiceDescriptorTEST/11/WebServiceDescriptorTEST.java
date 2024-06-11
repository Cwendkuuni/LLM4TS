import org.junit.Test;
import static org.junit.Assert.*;

public class WebServiceDescriptorTest {

    @Test
    public void testConstructor() {
        Class serviceClazz = String.class;
        String targetNamespace = "http://example.com";
        String typesNamespace = "http://types.example.com";
        
        WebServiceDescriptor wsDescriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        
        assertEquals(serviceClazz, wsDescriptor.getServiceClazz());
        assertEquals(targetNamespace, wsDescriptor.getTargetNamespace());
        assertEquals(typesNamespace, wsDescriptor.getTypesNamespace());
    }
    
    @Test
    public void testSetServiceClazz() {
        Class serviceClazz = String.class;
        WebServiceDescriptor wsDescriptor = new WebServiceDescriptor(serviceClazz, "", "");
        
        wsDescriptor.setServiceClazz(Integer.class);
        
        assertEquals(Integer.class, wsDescriptor.getServiceClazz());
    }
    
    @Test
    public void testSetTargetNamespace() {
        String targetNamespace = "http://example.com";
        WebServiceDescriptor wsDescriptor = new WebServiceDescriptor(null, "", "");
        
        wsDescriptor.setTargetNamespace(targetNamespace);
        
        assertEquals(targetNamespace, wsDescriptor.getTargetNamespace());
    }
    
    @Test
    public void testSetTypesNamespace() {
        String typesNamespace = "http://types.example.com";
        WebServiceDescriptor wsDescriptor = new WebServiceDescriptor(null, "", "");
        
        wsDescriptor.setTypesNamespace(typesNamespace);
        
        assertEquals(typesNamespace, wsDescriptor.getTypesNamespace());
    }
    
    @Test
    public void testHashCode() {
        Class serviceClazz = String.class;
        WebServiceDescriptor wsDescriptor1 = new WebServiceDescriptor(serviceClazz, "", "");
        WebServiceDescriptor wsDescriptor2 = new WebServiceDescriptor(serviceClazz, "", "");
        
        assertEquals(wsDescriptor1.hashCode(), wsDescriptor2.hashCode());
    }
    
    @Test
    public void testEquals() {
        Class serviceClazz = String.class;
        WebServiceDescriptor wsDescriptor1 = new WebServiceDescriptor(serviceClazz, "", "");
        WebServiceDescriptor wsDescriptor2 = new WebServiceDescriptor(serviceClazz, "", "");
        
        assertTrue(wsDescriptor1.equals(wsDescriptor2));
    }
}