import org.junit.Test;
import static org.junit.Assert.*;

public class WebServiceDescriptorTest {

    @Test
    public void testConstructor() {
        Class serviceClazz = Class.class;
        String targetNamespace = "http://example.com";
        String typesNamespace = "http://types.example.com";

        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

        assertEquals(descriptor.getServiceClazz(), serviceClazz);
        assertEquals(descriptor.getTargetNamespace(), targetNamespace);
        assertEquals(descriptor.getTypesNamespace(), typesNamespace);
    }

    @Test
    public void testSetServiceClazz() {
        Class serviceClazz = Class.class;

        WebServiceDescriptor descriptor = new WebServiceDescriptor(Class.class, "http://example.com", "http://types.example.com");
        descriptor.setServiceClazz(serviceClazz);

        assertEquals(descriptor.getServiceClazz(), serviceClazz);
    }

    @Test
    public void testSetTargetNamespace() {
        String targetNamespace = "http://updated.com";

        WebServiceDescriptor descriptor = new WebServiceDescriptor(Class.class, "http://example.com", "http://types.example.com");
        descriptor.setTargetNamespace(targetNamespace);

        assertEquals(descriptor.getTargetNamespace(), targetNamespace);
    }

    @Test
    public void testSetTypesNamespace() {
        String typesNamespace = "http://updatedtypes.com";

        WebServiceDescriptor descriptor = new WebServiceDescriptor(Class.class, "http://example.com", "http://types.example.com");
        descriptor.setTypesNamespace(typesNamespace);

        assertEquals(descriptor.getTypesNamespace(), typesNamespace);
    }

    @Test
    public void testHashCode() {
        Class serviceClazz = Class.class;

        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz, "http://example.com", "http://types.example.com");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz, "http://example.com", "http://types.example.com");

        assertEquals(descriptor1.hashCode(), descriptor2.hashCode());
    }

    @Test
    public void testEquals() {
        Class serviceClazz = Class.class;

        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz, "http://example.com", "http://types.example.com");

        // Same object should be equal
        assertTrue(descriptor1.equals(descriptor1));

        // Different class should not be equal
        assertFalse(descriptor1.equals(new Object()));

        // Objects with different serviceClazz should not be equal
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(Class.class, "http://example.com", "http://types.example.com");
        assertFalse(descriptor1.equals(descriptor2));

        // Objects with same serviceClazz should be equal
        WebServiceDescriptor descriptor3 = new WebServiceDescriptor(serviceClazz, "http://example.com", "http://types.example.com");
        assertTrue(descriptor1.equals(descriptor3));
    }
}