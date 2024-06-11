import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class WebServiceDescriptorTest {
    @Test
    public void testConstructor() {
        Class serviceClazz = String.class;
        String targetNamespace = "http://example.com";
        String typesNamespace = "http://example.com/types";
        
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        
        assertEquals(serviceClazz, descriptor.getServiceClazz());
        assertEquals(targetNamespace, descriptor.getTargetNamespace());
        assertEquals(typesNamespace, descriptor.getTypesNamespace());
    }
    
    @Test
    public void testGetServiceClazz() {
        Class serviceClazz = String.class;
        
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, "", "");
        
        assertEquals(serviceClazz, descriptor.getServiceClazz());
    }
    
    @Test
    public void testSetServiceClazz() {
        Class serviceClazz = Integer.class;
        
        WebServiceDescriptor descriptor = new WebServiceDescriptor(String.class, "", "");
        descriptor.setServiceClazz(serviceClazz);
        
        assertEquals(serviceClazz, descriptor.getServiceClazz());
    }
    
    @Test
    public void testGetTargetNamespace() {
        String targetNamespace = "http://example.com";
        
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, targetNamespace, "");
        
        assertEquals(targetNamespace, descriptor.getTargetNamespace());
    }
    
    @Test
    public void testSetTargetNamespace() {
        String targetNamespace = "http://example.com";
        
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, "", "");
        descriptor.setTargetNamespace(targetNamespace);
        
        assertEquals(targetNamespace, descriptor.getTargetNamespace());
    }
    
    @Test
    public void testGetTypesNamespace() {
        String typesNamespace = "http://example.com/types";
        
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, "", typesNamespace);
        
        assertEquals(typesNamespace, descriptor.getTypesNamespace());
    }
    
    @Test
    public void testSetTypesNamespace() {
        String typesNamespace = "http://example.com/types";
        
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, "", "");
        descriptor.setTypesNamespace(typesNamespace);
        
        assertEquals(typesNamespace, descriptor.getTypesNamespace());
    }
    
    @Test
    public void testHashCode() {
        Class serviceClazz = String.class;
        
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, "", "");
        
        assertEquals(serviceClazz.hashCode(), descriptor.hashCode());
    }
    
    @Test
    public void testEquals() {
        Class serviceClazz = String.class;
        
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz, "", "");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(String.class, "", "");
        
        assertEquals(true, descriptor1.equals(descriptor1));
        assertEquals(false, descriptor1.equals(null));
        assertEquals(false, descriptor1.equals(new Object()));
        assertEquals(false, descriptor1.equals(descriptor2));
        assertEquals(true, descriptor1.equals(descriptor1));
    }
}