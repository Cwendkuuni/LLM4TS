import org.junit.Assert;
import org.junit.Test;

public class WebServiceDescriptorTest {

    @Test
    public void testGetServiceClazz() {
        // Test initialization
        Class expected = WebServiceDescriptor.class;
        WebServiceDescriptor descriptor = new WebServiceDescriptor(expected, "", "");

        // Test method
        Class actual = descriptor.getServiceClazz();

        // Assert
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSetServiceClazz() {
        // Test initialization
        Class expected = String.class;
        WebServiceDescriptor descriptor = new WebServiceDescriptor(WebServiceDescriptor.class, "", "");

        // Test method
        descriptor.setServiceClazz(expected);
        Class actual = descriptor.getServiceClazz();

        // Assert
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetTargetNamespace() {
        // Test initialization
        String expected = "http://example.com/namespace";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(WebServiceDescriptor.class, expected, "");

        // Test method
        String actual = descriptor.getTargetNamespace();

        // Assert
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSetTargetNamespace() {
        // Test initialization
        String expected = "http://example.com/namespace";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(WebServiceDescriptor.class, "", "");

        // Test method
        descriptor.setTargetNamespace(expected);
        String actual = descriptor.getTargetNamespace();

        // Assert
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetTypesNamespace() {
        // Test initialization
        String expected = "http://example.com/namespace";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(WebServiceDescriptor.class, "", expected);

        // Test method
        String actual = descriptor.getTypesNamespace();

        // Assert
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSetTypesNamespace() {
        // Test initialization
        String expected = "http://example.com/namespace";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(WebServiceDescriptor.class, "", "");

        // Test method
        descriptor.setTypesNamespace(expected);
        String actual = descriptor.getTypesNamespace();

        // Assert
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testHashCode() {
        // Test initialization
        int expected = 31;
        WebServiceDescriptor descriptor = new WebServiceDescriptor(WebServiceDescriptor.class, "", "");

        // Test method
        int actual = descriptor.hashCode();

        // Assert
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        // Test initialization
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(WebServiceDescriptor.class, "", "");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(WebServiceDescriptor.class, "", "");

        // Test method
        boolean actual = descriptor1.equals(descriptor2);

        // Assert
        Assert.assertTrue(actual);
    }
}