import org.junit.Test;
import static org.junit.Assert.*;

public class WebServiceDescriptorTest {

    @Test
    public void testGetServiceClazz() {
        // Arrange
        Class expected = Service.class;
        WebServiceDescriptor descriptor = new WebServiceDescriptor(expected, "target", "types");

        // Act
        Class actual = descriptor.getServiceClazz();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void testSetServiceClazz() {
        // Arrange
        Class expected = Service.class;
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, "target", "types");

        // Act
        descriptor.setServiceClazz(expected);
        Class actual = descriptor.getServiceClazz();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void testGetTargetNamespace() {
        // Arrange
        String expected = "target";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(Service.class, expected, "types");

        // Act
        String actual = descriptor.getTargetNamespace();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void testSetTargetNamespace() {
        // Arrange
        String expected = "target";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(Service.class, null, "types");

        // Act
        descriptor.setTargetNamespace(expected);
        String actual = descriptor.getTargetNamespace();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void testGetTypesNamespace() {
        // Arrange
        String expected = "types";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(Service.class, "target", expected);

        // Act
        String actual = descriptor.getTypesNamespace();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void testSetTypesNamespace() {
        // Arrange
        String expected = "types";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(Service.class, "target", null);

        // Act
        descriptor.setTypesNamespace(expected);
        String actual = descriptor.getTypesNamespace();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void testHashCode() {
        // Arrange
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(Service.class, "target", "types");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(Service.class, "target", "types");

        // Act
        int hashCode1 = descriptor1.hashCode();
        int hashCode2 = descriptor2.hashCode();

        // Assert
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testEquals() {
        // Arrange
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(Service.class, "target", "types");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(Service.class, "target", "types");
        WebServiceDescriptor descriptor3 = new WebServiceDescriptor(Service2.class, "target", "types");

        // Assert
        assertTrue(descriptor1.equals(descriptor2));
        assertFalse(descriptor1.equals(descriptor3));
    }
}