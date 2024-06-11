import org.junit.*;
import static org.junit.Assert.*;

public class WebServiceDescriptorTest {

    @Test
    public void testGetServiceClazz() {
        Class expected = String.class;
        WebServiceDescriptor descriptor = new WebServiceDescriptor(expected, "targetNamespace", "typesNamespace");
        Class actual = descriptor.getServiceClazz();
        assertEquals(expected, actual);
    }

    @Test
    public void testSetServiceClazz() {
        Class expected = Integer.class;
        WebServiceDescriptor descriptor = new WebServiceDescriptor(String.class, "targetNamespace", "typesNamespace");
        descriptor.setServiceClazz(expected);
        Class actual = descriptor.getServiceClazz();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetTargetNamespace() {
        String expected = "targetNamespace";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(String.class, expected, "typesNamespace");
        String actual = descriptor.getTargetNamespace();
        assertEquals(expected, actual);
    }

    @Test
    public void testSetTargetNamespace() {
        String expected = "newTargetNamespace";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(String.class, "targetNamespace", "typesNamespace");
        descriptor.setTargetNamespace(expected);
        String actual = descriptor.getTargetNamespace();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetTypesNamespace() {
        String expected = "typesNamespace";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(String.class, "targetNamespace", expected);
        String actual = descriptor.getTypesNamespace();
        assertEquals(expected, actual);
    }

    @Test
    public void testSetTypesNamespace() {
        String expected = "newTypesNamespace";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(String.class, "targetNamespace", "typesNamespace");
        descriptor.setTypesNamespace(expected);
        String actual = descriptor.getTypesNamespace();
        assertEquals(expected, actual);
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
}