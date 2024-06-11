import org.junit.Test;
import static org.junit.Assert.*;

public class WebServiceDescriptorTest {

    @Test
    public void testGetServiceClazz() {
        Class serviceClazz = String.class;
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, "targetNamespace", "typesNamespace");
        assertEquals(serviceClazz, descriptor.getServiceClazz());
    }

    @Test
    public void testSetServiceClazz() {
        Class serviceClazz = Integer.class;
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, "targetNamespace", "typesNamespace");
        descriptor.setServiceClazz(serviceClazz);
        assertEquals(serviceClazz, descriptor.getServiceClazz());
    }

    @Test
    public void testGetTargetNamespace() {
        String targetNamespace = "http://example.com";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, targetNamespace, "typesNamespace");
        assertEquals(targetNamespace, descriptor.getTargetNamespace());
    }

    @Test
    public void testSetTargetNamespace() {
        String targetNamespace = "http://example.com";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, "oldNamespace", "typesNamespace");
        descriptor.setTargetNamespace(targetNamespace);
        assertEquals(targetNamespace, descriptor.getTargetNamespace());
    }

    @Test
    public void testGetTypesNamespace() {
        String typesNamespace = "http://example.com/types";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, "targetNamespace", typesNamespace);
        assertEquals(typesNamespace, descriptor.getTypesNamespace());
    }

    @Test
    public void testSetTypesNamespace() {
        String typesNamespace = "http://example.com/types";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, "targetNamespace", "oldTypesNamespace");
        descriptor.setTypesNamespace(typesNamespace);
        assertEquals(typesNamespace, descriptor.getTypesNamespace());
    }

    @Test
    public void testHashCode() {
        Class serviceClazz = String.class;
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz, "targetNamespace", "typesNamespace");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz, "targetNamespace", "typesNamespace");
        assertEquals(descriptor1.hashCode(), descriptor2.hashCode());
    }

    @Test
    public void testEquals() {
        Class serviceClazz = String.class;
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz, "targetNamespace", "typesNamespace");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz, "targetNamespace", "typesNamespace");
        assertTrue(descriptor1.equals(descriptor2));
    }

    @Test
    public void testEqualsWithDifferentServiceClazz() {
        Class serviceClazz1 = String.class;
        Class serviceClazz2 = Integer.class;
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz1, "targetNamespace", "typesNamespace");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz2, "targetNamespace", "typesNamespace");
        assertFalse(descriptor1.equals(descriptor2));
    }
}