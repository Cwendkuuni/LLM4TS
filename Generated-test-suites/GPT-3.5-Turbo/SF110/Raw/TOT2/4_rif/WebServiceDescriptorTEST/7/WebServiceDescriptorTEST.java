import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class WebServiceDescriptorTest {

    @Test
    public void testGetServiceClazz() {
        Class serviceClazz = String.class;
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, "namespace1", "namespace2");

        assertEquals(serviceClazz, descriptor.getServiceClazz());
    }

    @Test
    public void testSetServiceClazz() {
        Class serviceClazz = Integer.class;
        WebServiceDescriptor descriptor = new WebServiceDescriptor(String.class, "namespace1", "namespace2");
        descriptor.setServiceClazz(serviceClazz);

        assertEquals(serviceClazz, descriptor.getServiceClazz());
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
        WebServiceDescriptor descriptor = new WebServiceDescriptor(Integer.class, "namespace1", "namespace2");
        descriptor.setTargetNamespace(targetNamespace);

        assertEquals(targetNamespace, descriptor.getTargetNamespace());
    }

    @Test
    public void testGetTypesNamespace() {
        String typesNamespace = "namespace2";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(Boolean.class, "namespace1", typesNamespace);

        assertEquals(typesNamespace, descriptor.getTypesNamespace());
    }

    @Test
    public void testSetTypesNamespace() {
        String typesNamespace = "namespace1";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(Double.class, "namespace1", "namespace2");
        descriptor.setTypesNamespace(typesNamespace);

        assertEquals(typesNamespace, descriptor.getTypesNamespace());
    }

    @Test
    public void testHashCode() {
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(Float.class, "namespace1", "namespace2");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(Float.class, "namespace1", "namespace2");

        assertEquals(descriptor1.hashCode(), descriptor2.hashCode());
    }

    @Test
    public void testEqualsSameObject() {
        WebServiceDescriptor descriptor = new WebServiceDescriptor(Long.class, "namespace1", "namespace2");

        assertTrue(descriptor.equals(descriptor));
    }

    @Test
    public void testEqualsDifferentObject() {
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(Character.class, "namespace1", "namespace2");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(Short.class, "namespace1", "namespace2");

        assertFalse(descriptor1.equals(descriptor2));
    }

    @Test
    public void testEqualsNullObject() {
        WebServiceDescriptor descriptor = new WebServiceDescriptor(Byte.class, "namespace1", "namespace2");

        assertFalse(descriptor.equals(null));
    }

    @Test
    public void testEqualsDifferentClass() {
        WebServiceDescriptor descriptor = new WebServiceDescriptor(Integer.class, "namespace1", "namespace2");
        Object obj = new Object();

        assertFalse(descriptor.equals(obj));
    }

    @Test
    public void testEqualsDifferentServiceClazz() {
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(String.class, "namespace1", "namespace2");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(Integer.class, "namespace1", "namespace2");

        assertFalse(descriptor1.equals(descriptor2));
    }
}