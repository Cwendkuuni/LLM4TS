import org.junit.Test;
import static org.junit.Assert.*;

public class WebServiceDescriptorTest {

    @Test
    public void testConstructor() {
        Class serviceClazz = new Class();
        String targetNamespace = "http://www.example.com";
        String typesNamespace = "http://www.example.com/types";
        
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        
        assertEquals(serviceClazz, descriptor.getServiceClazz());
        assertEquals(targetNamespace, descriptor.getTargetNamespace());
        assertEquals(typesNamespace, descriptor.getTypesNamespace());
    }
    
    @Test
    public void testSetServiceClazz() {
        Class serviceClazz = new Class();
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, "", "");
        
        Class newServiceClazz = new Class();
        descriptor.setServiceClazz(newServiceClazz);
        
        assertEquals(newServiceClazz, descriptor.getServiceClazz());
    }
    
    @Test
    public void testSetTargetNamespace() {
        Class serviceClazz = new Class();
        String targetNamespace = "http://www.example.com";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, "", "");
        
        descriptor.setTargetNamespace(targetNamespace);
        
        assertEquals(targetNamespace, descriptor.getTargetNamespace());
    }
    
    @Test
    public void testSetTypesNamespace() {
        Class serviceClazz = new Class();
        String typesNamespace = "http://www.example.com/types";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, "", "");
        
        descriptor.setTypesNamespace(typesNamespace);
        
        assertEquals(typesNamespace, descriptor.getTypesNamespace());
    }
    
    @Test
    public void testHashCode() {
        Class serviceClazz = new Class();
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz, "", "");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz, "", "");
        
        assertEquals(descriptor1.hashCode(), descriptor2.hashCode());
    }
    
    @Test
    public void testEquals() {
        Class serviceClazz = new Class();
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz, "", "");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz, "", "");
        
        assertTrue(descriptor1.equals(descriptor2));
    }
}