
import org.junit.Test;
import static org.junit.Assert.*;

public class WebServiceDescriptorTest {

    @Test
    public void testGetServiceClazz() {
        Class expected = null;
        WebServiceDescriptor descriptor = new WebServiceDescriptor(expected, "", "");
        Class actual = descriptor.getServiceClazz();
        assertEquals(expected, actual);
    }

    @Test
    public void testSetServiceClazz() {
        Class expected = String.class;
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, "", "");
        descriptor.setServiceClazz(expected);
        Class actual = descriptor.getServiceClazz();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetTargetNamespace() {
        String expected = "namespace";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, expected, "");
        String actual = descriptor.getTargetNamespace();
        assertEquals(expected, actual);
    }

    @Test
    public void testSetTargetNamespace() {
        String expected = "namespace";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, null, "");
        descriptor.setTargetNamespace(expected);
        String actual = descriptor.getTargetNamespace();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetTypesNamespace() {
        String expected = "namespace";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, "", expected);
        String actual = descriptor.getTypesNamespace();
        assertEquals(expected, actual);
    }

    @Test
    public void testSetTypesNamespace() {
        String expected = "namespace";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, "", null);
        descriptor.setTypesNamespace(expected);
        String actual = descriptor.getTypesNamespace();
        assertEquals(expected, actual);
    }

    @Test
    public void testHashCode() {
        Class clazz = String.class;
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(clazz, "", "");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(clazz, "", "");
        int hashCode1 = descriptor1.hashCode();
        int hashCode2 = descriptor2.hashCode();
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testEqualsWithSameObject() {
        Class clazz = String.class;
        WebServiceDescriptor descriptor = new WebServiceDescriptor(clazz, "", "");
        assertTrue(descriptor.equals(descriptor));
    }

    @Test
    public void testEqualsWithDifferentClass() {
        Class clazz = String.class;
        WebServiceDescriptor descriptor = new WebServiceDescriptor(clazz, "", "");
        assertFalse(descriptor.equals("not a descriptor"));
    }

    @Test
    public void testEqualsWithDifferentServiceClazz() {
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(null, "", "");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(String.class, "", "");
        assertFalse(descriptor1.equals(descriptor2));
    }

    @Test
    public void testEqualsWithEqualServiceClazz() {
        Class clazz = String.class;
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(clazz, "", "");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(clazz, "", "");
        assertTrue(descriptor1.equals(descriptor2));
    }

}

