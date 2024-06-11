import org.junit.Test;
import static org.junit.Assert.*;

public class WebServiceDescriptorTest {

    @Test
    public void testGetServiceClazz() {
        Class serviceClazz = String.class;
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, "namespace", "typesNamespace");
        
        assertEquals(serviceClazz, descriptor.getServiceClazz());
    }
    
    @Test
    public void testSetServiceClazz() {
        Class serviceClazz = Integer.class;
        WebServiceDescriptor descriptor = new WebServiceDescriptor();
        
        descriptor.setServiceClazz(serviceClazz);
        
        assertEquals(serviceClazz, descriptor.getServiceClazz());
    }
    
    @Test
    public void testGetTargetNamespace() {
        String targetNamespace = "namespace";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, targetNamespace, null);
        
        assertEquals(targetNamespace, descriptor.getTargetNamespace());
    }
    
    @Test
    public void testSetTargetNamespace() {
        String targetNamespace = "namespace";
        WebServiceDescriptor descriptor = new WebServiceDescriptor();
        
        descriptor.setTargetNamespace(targetNamespace);
        
        assertEquals(targetNamespace, descriptor.getTargetNamespace());
    }
    
    @Test
    public void testGetTypesNamespace() {
        String typesNamespace = "typesNamespace";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, null, typesNamespace);
        
        assertEquals(typesNamespace, descriptor.getTypesNamespace());
    }
    
    @Test
    public void testSetTypesNamespace() {
        String typesNamespace = "typesNamespace";
        WebServiceDescriptor descriptor = new WebServiceDescriptor();
        
        descriptor.setTypesNamespace(typesNamespace);
        
        assertEquals(typesNamespace, descriptor.getTypesNamespace());
    }
    
    @Test
    public void testHashCode() {
        Class serviceClazz = String.class;
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz, "namespace", "typesNamespace");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz, "otherNamespace", "otherTypesNamespace");
        
        assertNotEquals(descriptor1.hashCode(), descriptor2.hashCode());
    }
    
    @Test
    public void testEqualsWithSameObject() {
        Class serviceClazz = String.class;
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, "namespace", "typesNamespace");
        
        assertTrue(descriptor.equals(descriptor));
    }
    
    @Test
    public void testEqualsWithNullObject() {
        Class serviceClazz = String.class;
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, "namespace", "typesNamespace");
        
        assertFalse(descriptor.equals(null));
    }
    
    @Test
    public void testEqualsWithDifferentObjectClass() {
        Class serviceClazz = String.class;
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, "namespace", "typesNamespace");
        
        assertFalse(descriptor.equals("string"));
    }
    
    @Test
    public void testEqualsWithDifferentServiceClass() {
        Class serviceClazz = String.class;
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz, "namespace", "typesNamespace");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(Integer.class, "namespace", "typesNamespace");
        
        assertFalse(descriptor1.equals(descriptor2));
    }
    
    @Test
    public void testEqualsWithEqualServiceClass() {
        Class serviceClazz = String.class;
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz, "namespace", "typesNamespace");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz, "otherNamespace", "otherTypesNamespace");
        
        assertTrue(descriptor1.equals(descriptor2));
    }
}