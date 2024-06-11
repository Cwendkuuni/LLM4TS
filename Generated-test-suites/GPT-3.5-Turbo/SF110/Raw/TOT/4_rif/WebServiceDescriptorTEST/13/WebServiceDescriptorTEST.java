
import org.junit.Test;
import static org.junit.Assert.*;

public class WebServiceDescriptorTest {

    @Test
    public void testGetServiceClazz() {
        Class serviceClazz = String.class;
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, "target", "types");
        assertEquals(serviceClazz, descriptor.getServiceClazz());
    }
    
    @Test
    public void testSetServiceClazz() {
        Class serviceClazz = Integer.class;
        WebServiceDescriptor descriptor = new WebServiceDescriptor(String.class, "target", "types");
        descriptor.setServiceClazz(serviceClazz);
        assertEquals(serviceClazz, descriptor.getServiceClazz());
    }
    
    @Test
    public void testGetTargetNamespace() {
        String targetNamespace = "target";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(String.class, targetNamespace, "types");
        assertEquals(targetNamespace, descriptor.getTargetNamespace());
    }
    
    @Test
    public void testSetTargetNamespace() {
        String targetNamespace = "newTarget";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(String.class, "target", "types");
        descriptor.setTargetNamespace(targetNamespace);
        assertEquals(targetNamespace, descriptor.getTargetNamespace());
    }
    
    @Test
    public void testGetTypesNamespace() {
        String typesNamespace = "types";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(String.class, "target", typesNamespace);
        assertEquals(typesNamespace, descriptor.getTypesNamespace());
    }
    
    @Test
    public void testSetTypesNamespace() {
        String typesNamespace = "newTypes";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(String.class, "target", "types");
        descriptor.setTypesNamespace(typesNamespace);
        assertEquals(typesNamespace, descriptor.getTypesNamespace());
    }
    
    @Test
    public void testHashCode() {
        Class serviceClazz = String.class;
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz, "target", "types");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz, "target", "types");
        assertEquals(descriptor1.hashCode(), descriptor2.hashCode());
    }
    
    @Test
    public void testEqualsWhenEqual() {
        Class serviceClazz = String.class;
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz, "target", "types");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz, "target", "types");
        assertTrue(descriptor1.equals(descriptor2));
    }
    
    @Test
    public void testEqualsWhenNotEqual() {
        Class serviceClazz1 = String.class;
        Class serviceClazz2 = Integer.class;
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz1, "target", "types");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz2, "target", "types");
        assertFalse(descriptor1.equals(descriptor2));
    }
}
