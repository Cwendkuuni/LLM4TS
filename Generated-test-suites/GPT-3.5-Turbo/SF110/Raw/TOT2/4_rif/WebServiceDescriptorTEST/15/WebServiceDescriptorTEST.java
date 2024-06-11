import static org.junit.Assert.*;
import org.junit.Test;

public class WebServiceDescriptorTest {

    @Test
    public void testConstructor() {
        Class serviceClazz = ExampleService.class;
        String targetNamespace = "http://example.com";
        String typesNamespace = "http://example.com/types";
        
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        
        assertEquals(serviceClazz, descriptor.getServiceClazz());
        assertEquals(targetNamespace, descriptor.getTargetNamespace());
        assertEquals(typesNamespace, descriptor.getTypesNamespace());
    }
    
    @Test
    public void testSetServiceClazz() {
        Class serviceClazz = AnotherService.class;
        
        WebServiceDescriptor descriptor = new WebServiceDescriptor(ExampleService.class, "http://example.com", "http://example.com/types");
        descriptor.setServiceClazz(serviceClazz);
        
        assertEquals(serviceClazz, descriptor.getServiceClazz());
    }
    
    @Test
    public void testSetTargetNamespace() {
        String targetNamespace = "http://newexample.com";
        
        WebServiceDescriptor descriptor = new WebServiceDescriptor(ExampleService.class, "http://example.com", "http://example.com/types");
        descriptor.setTargetNamespace(targetNamespace);
        
        assertEquals(targetNamespace, descriptor.getTargetNamespace());
    }
    
    @Test
    public void testSetTypesNamespace() {
        String typesNamespace = "http://newexample.com/types";
        
        WebServiceDescriptor descriptor = new WebServiceDescriptor(ExampleService.class, "http://example.com", "http://example.com/types");
        descriptor.setTypesNamespace(typesNamespace);
        
        assertEquals(typesNamespace, descriptor.getTypesNamespace());
    }
    
    @Test
    public void testHashCode() {
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(ExampleService.class, "http://example.com", "http://example.com/types");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(AnotherService.class, "http://example.com", "http://example.com/types");
        
        assertNotEquals(descriptor1.hashCode(), descriptor2.hashCode());
    }
    
    @Test
    public void testEquals() {
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(ExampleService.class, "http://example.com", "http://example.com/types");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(ExampleService.class, "http://example.com", "http://example.com/types");
        
        assertEquals(descriptor1, descriptor2);
    }
}