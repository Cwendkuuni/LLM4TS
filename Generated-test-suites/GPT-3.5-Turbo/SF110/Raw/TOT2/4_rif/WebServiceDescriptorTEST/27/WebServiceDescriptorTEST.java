import static org.junit.Assert.*;
import org.junit.Test;

public class WebServiceDescriptorTest {

    @Test
    public void testGetServiceClazz() {
        Class serviceClass = String.class;
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClass, "namespace1", "namespace2");
        assertEquals(serviceClass, descriptor.getServiceClazz());
    }

    @Test
    public void testSetServiceClazz() {
        Class serviceClass = Integer.class;
        WebServiceDescriptor descriptor = new WebServiceDescriptor(String.class, "namespace1", "namespace2");
        descriptor.setServiceClazz(serviceClass);
        assertEquals(serviceClass, descriptor.getServiceClazz());
    }

    @Test
    public void testGetTargetNamespace() {
        String targetNamespace = "namespace1";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(String.class, targetNamespace, "namespace2");
        assertEquals(targetNamespace, descriptor.getTargetNamespace());
    }

    @Test
    public void testSetTargetNamespace() {
        String targetNamespace = "namespace2";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(String.class, "namespace1", "namespace2");
        descriptor.setTargetNamespace(targetNamespace);
        assertEquals(targetNamespace, descriptor.getTargetNamespace());
    }

    @Test
    public void testGetTypesNamespace() {
        String typesNamespace = "namespace2";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(String.class, "namespace1", typesNamespace);
        assertEquals(typesNamespace, descriptor.getTypesNamespace());
    }

    @Test
    public void testSetTypesNamespace() {
        String typesNamespace = "namespace1";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(String.class, "namespace1", "namespace2");
        descriptor.setTypesNamespace(typesNamespace);
        assertEquals(typesNamespace, descriptor.getTypesNamespace());
    }

    @Test
    public void testHashCode() {
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(String.class, "namespace1", "namespace2");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(Integer.class, "namespace1", "namespace2");
        assertNotEquals(descriptor1.hashCode(), descriptor2.hashCode());
    }

    @Test
    public void testEquals() {
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(String.class, "namespace1", "namespace2");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(String.class, "namespace1", "namespace2");
        assertTrue(descriptor1.equals(descriptor2));
    }
}