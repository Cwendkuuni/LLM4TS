import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class WebServiceDescriptorTest {

    private WebServiceDescriptor webServiceDescriptor;

    @Before
    public void setUp() {
        webServiceDescriptor = new WebServiceDescriptor(null, null, null);
    }

    @Test
    public void testGetServiceClazz() {
        Assert.assertNull(webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testSetServiceClazz() {
        Class<?> serviceClazz = String.class;
        webServiceDescriptor.setServiceClazz(serviceClazz);
        Assert.assertEquals(serviceClazz, webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testGetTargetNamespace() {
        Assert.assertNull(webServiceDescriptor.getTargetNamespace());
    }

    @Test
    public void testSetTargetNamespace() {
        String targetNamespace = "http://example.com";
        webServiceDescriptor.setTargetNamespace(targetNamespace);
        Assert.assertEquals(targetNamespace, webServiceDescriptor.getTargetNamespace());
    }

    @Test
    public void testGetTypesNamespace() {
        Assert.assertNull(webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void testSetTypesNamespace() {
        String typesNamespace = "http://example.com/types";
        webServiceDescriptor.setTypesNamespace(typesNamespace);
        Assert.assertEquals(typesNamespace, webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void testHashCode() {
        webServiceDescriptor.setServiceClazz(String.class);
        int hashCode = webServiceDescriptor.getServiceClazz().hashCode();
        Assert.assertEquals(hashCode, webServiceDescriptor.hashCode());
    }

    @Test
    public void testEqualsSameObject() {
        Assert.assertTrue(webServiceDescriptor.equals(webServiceDescriptor));
    }

    @Test
    public void testEqualsNull() {
        Assert.assertFalse(webServiceDescriptor.equals(null));
    }

    @Test
    public void testEqualsDifferentClass() {
        Assert.assertFalse(webServiceDescriptor.equals("string"));
    }

    @Test
    public void testEqualsSameServiceClazz() {
        webServiceDescriptor.setServiceClazz(String.class);
        WebServiceDescriptor other = new WebServiceDescriptor(null, null, null);
        other.setServiceClazz(String.class);
        Assert.assertTrue(webServiceDescriptor.equals(other));
    }

    @Test
    public void testEqualsDifferentServiceClazz() {
        webServiceDescriptor.setServiceClazz(String.class);
        WebServiceDescriptor other = new WebServiceDescriptor(null, null, null);
        Assert.assertFalse(webServiceDescriptor.equals(other));
    }

}