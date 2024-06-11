import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class WebServiceDescriptorTest {

    @Test
    public void testConstructor() {
        String targetNamespace = "http://example.com/target";
        String typesNamespace = "http://example.com/types";
        Class serviceClazz = Object.class;
        
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        
        assertEquals(serviceClazz, descriptor.getServiceClazz());
        assertEquals(targetNamespace, descriptor.getTargetNamespace());
        assertEquals(typesNamespace, descriptor.getTypesNamespace());
    }
    
    @Test
    public void testSetServiceClazz() {
        WebServiceDescriptor descriptor = new WebServiceDescriptor(Object.class, "http://example.com/target", "http://example.com/types");
        
        Class newServiceClazz = String.class;
        descriptor.setServiceClazz(newServiceClazz);
        
        assertEquals(newServiceClazz, descriptor.getServiceClazz());
    }
    
    @Test
    public void testSetTargetNamespace() {
        WebServiceDescriptor descriptor = new WebServiceDescriptor(Object.class, "http://example.com/target", "http://example.com/types");
        
        String newTargetNamespace = "http://example.com/new-target";
        descriptor.setTargetNamespace(newTargetNamespace);
        
        assertEquals(newTargetNamespace, descriptor.getTargetNamespace());
    }
    
    @Test
    public void testSetTypesNamespace() {
        WebServiceDescriptor descriptor = new WebServiceDescriptor(Object.class, "http://example.com/target", "http://example.com/types");
        
        String newTypesNamespace = "http://example.com/new-types";
        descriptor.setTypesNamespace(newTypesNamespace);
        
        assertEquals(newTypesNamespace, descriptor.getTypesNamespace());
    }
    
    @Test
    public void testHashCode() {
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(Object.class, "http://example.com/target", "http://example.com/types");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(Object.class, "http://example.com/target", "http://example.com/types");
        
        assertEquals(descriptor1.hashCode(), descriptor2.hashCode());
    }
    
    @Test
    public void testEquals() {
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(Object.class, "http://example.com/target", "http://example.com/types");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(Object.class, "http://example.com/target", "http://example.com/types");
        WebServiceDescriptor descriptor3 = new WebServiceDescriptor(String.class, "http://example.com/target", "http://example.com/types");
        
        assertEquals(descriptor1, descriptor2);
        assertNotEquals(descriptor1, descriptor3);
    }
}