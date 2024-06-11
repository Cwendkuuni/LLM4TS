import org.junit.Test;
import static org.junit.Assert.*;

public class WebServiceDescriptorTest {

    @Test
    public void testGetServiceClazz() {
        Class expectedClazz = String.class;
        WebServiceDescriptor descriptor = new WebServiceDescriptor(expectedClazz, "namespace1", "namespace2");
        assertEquals(expectedClazz, descriptor.getServiceClazz());
    }

    @Test
    public void testSetServiceClazz() {
        Class expectedClazz = Integer.class;
        WebServiceDescriptor descriptor = new WebServiceDescriptor(String.class, "namespace1", "namespace2");
        descriptor.setServiceClazz(expectedClazz);
        assertEquals(expectedClazz, descriptor.getServiceClazz());
    }

    @Test
    public void testGetTargetNamespace() {
        String expectedNamespace = "namespace1";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(String.class, expectedNamespace, "namespace2");
        assertEquals(expectedNamespace, descriptor.getTargetNamespace());
    }

    @Test
    public void testSetTargetNamespace() {
        String expectedNamespace = "namespace2";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(String.class, "namespace1", "namespace3");
        descriptor.setTargetNamespace(expectedNamespace);
        assertEquals(expectedNamespace, descriptor.getTargetNamespace());
    }

    @Test
    public void testGetTypesNamespace() {
        String expectedNamespace = "namespace3";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(String.class, "namespace1", expectedNamespace);
        assertEquals(expectedNamespace, descriptor.getTypesNamespace());
    }

    @Test
    public void testSetTypesNamespace() {
        String expectedNamespace = "namespace4";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(String.class, "namespace1", "namespace3");
        descriptor.setTypesNamespace(expectedNamespace);
        assertEquals(expectedNamespace, descriptor.getTypesNamespace());
    }

    @Test
    public void testHashCode() {
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(String.class, "namespace1", "namespace2");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(String.class, "namespace1", "namespace2");
        assertEquals(descriptor1.hashCode(), descriptor2.hashCode());
    }

    @Test
    public void testEquals() {
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(String.class, "namespace1", "namespace2");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(String.class, "namespace1", "namespace2");
        assertTrue(descriptor1.equals(descriptor2));
    }
}