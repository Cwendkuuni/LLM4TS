import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class WebServiceDescriptorTest {
    
    @Test
    public void testConstructorAndGetters() {
        Class serviceClazz = String.class;
        String targetNamespace = "http://example.com";
        String typesNamespace = "http://example.com/types";
        
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        
        assertEquals(serviceClazz, descriptor.getServiceClazz());
        assertEquals(targetNamespace, descriptor.getTargetNamespace());
        assertEquals(typesNamespace, descriptor.getTypesNamespace());
    }
    
    @Test
    public void testSetters() {
        Class serviceClazz = String.class;
        String targetNamespace = "http://example.com";
        String typesNamespace = "http://example.com/types";
        
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        
        Class newServiceClazz = Integer.class;
        String newTargetNamespace = "http://newexample.com";
        String newTypesNamespace = "http://newexample.com/types";
        
        descriptor.setServiceClazz(newServiceClazz);
        descriptor.setTargetNamespace(newTargetNamespace);
        descriptor.setTypesNamespace(newTypesNamespace);
        
        assertEquals(newServiceClazz, descriptor.getServiceClazz());
        assertEquals(newTargetNamespace, descriptor.getTargetNamespace());
        assertEquals(newTypesNamespace, descriptor.getTypesNamespace());
    }
    
    @Test
    public void testHashCode() {
        Class serviceClazz = String.class;
        String targetNamespace = "http://example.com";
        String typesNamespace = "http://example.com/types";
        
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        
        assertEquals(descriptor1.hashCode(), descriptor2.hashCode());
    }
    
    @Test
    public void testEquals() {
        Class serviceClazz = String.class;
        String targetNamespace = "http://example.com";
        String typesNamespace = "http://example.com/types";
        
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        
        assertEquals(descriptor1, descriptor2);
    }
}