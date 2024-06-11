
import org.junit.Test;
import static org.junit.Assert.*;

public class WebServiceDescriptorTest {

    @Test
    public void testGetServiceClazz() {
        // Arrange
        Class expected = null;
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(expected, "", "");

        // Act
        Class actual = webServiceDescriptor.getServiceClazz();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void testSetServiceClazz() {
        // Arrange
        Class expected = String.class;
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, "", "");

        // Act
        webServiceDescriptor.setServiceClazz(expected);
        Class actual = webServiceDescriptor.getServiceClazz();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void testGetTargetNamespace() {
        // Arrange
        String expected = "targetNamespace";
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, expected, "");

        // Act
        String actual = webServiceDescriptor.getTargetNamespace();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void testSetTargetNamespace() {
        // Arrange
        String expected = "targetNamespace";
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, null, "");

        // Act
        webServiceDescriptor.setTargetNamespace(expected);
        String actual = webServiceDescriptor.getTargetNamespace();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void testGetTypesNamespace() {
        // Arrange
        String expected = "typesNamespace";
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, "", expected);

        // Act
        String actual = webServiceDescriptor.getTypesNamespace();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void testSetTypesNamespace() {
        // Arrange
        String expected = "typesNamespace";
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, "", null);

        // Act
        webServiceDescriptor.setTypesNamespace(expected);
        String actual = webServiceDescriptor.getTypesNamespace();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void testHashCode() {
        // Arrange
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(null, "", "");
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(null, "", "");

        // Act
        int hashCode1 = webServiceDescriptor1.hashCode();
        int hashCode2 = webServiceDescriptor2.hashCode();

        // Assert
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testEquals() {
        // Arrange
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(null, "", "");
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(null, "", "");

        // Act
        boolean equals = webServiceDescriptor1.equals(webServiceDescriptor2);

        // Assert
        assertTrue(equals);
    }
}
