import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class WebServiceDescriptorTest {
    @Test
    public void testConstructor() {
        Class serviceClazz = new Class();
        String targetNamespace = "target";
        String typesNamespace = "types";

        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        assertEquals(serviceClazz, descriptor.getServiceClazz());
        assertEquals(targetNamespace, descriptor.getTargetNamespace());
        assertEquals(typesNamespace, descriptor.getTypesNamespace());
    }

    @Test
    public void testSetServiceClazz() {
        Class serviceClazz = new Class();
        
        WebServiceDescriptor descriptor = new WebServiceDescriptor();
        descriptor.setServiceClazz(serviceClazz);
        assertEquals(serviceClazz, descriptor.getServiceClazz());
    }

    @Test
    public void testSetTargetNamespace() {
        String targetNamespace = "target";
        
        WebServiceDescriptor descriptor = new WebServiceDescriptor();
        descriptor.setTargetNamespace(targetNamespace);
        assertEquals(targetNamespace, descriptor.getTargetNamespace());
    }

    @Test
    public void testSetTypesNamespace() {
        String typesNamespace = "types";
        
        WebServiceDescriptor descriptor = new WebServiceDescriptor();
        descriptor.setTypesNamespace(typesNamespace);
        assertEquals(typesNamespace, descriptor.getTypesNamespace());
    }

    @Test
    public void testHashCode() {
        Class serviceClazz = new Class();
        
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz, "target1", "types1");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz, "target2", "types2");

        assertEquals(descriptor1.hashCode(), descriptor2.hashCode());
    }

    @Test
    public void testEquals() {
        Class serviceClazz = new Class();
        
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz, "target1", "types1");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz, "target2", "types2");
        WebServiceDescriptor descriptor3 = new WebServiceDescriptor(new Class(), "target1", "types1");

        assertEquals(descriptor1, descriptor2);
        assertEquals(descriptor1, descriptor3);
    }
}