import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WebServiceDescriptorTest {

    private WebServiceDescriptor webServiceDescriptor;

    @Before
    public void setUp() {
        webServiceDescriptor = new WebServiceDescriptor(TestService.class, "http://www.targetnamespace.com", "http://www.typesnamespace.com");
    }

    @Test
    public void getServiceClazz() {
        assertEquals(TestService.class, webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void setServiceClazz() {
        webServiceDescriptor.setServiceClazz(TestService.class);
        assertEquals(TestService.class, webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void getTargetNamespace() {
        assertEquals("http://www.targetnamespace.com", webServiceDescriptor.getTargetNamespace());
    }

    @Test
    public void setTargetNamespace() {
        webServiceDescriptor.setTargetNamespace("http://www.newtargetnamespace.com");
        assertEquals("http://www.newtargetnamespace.com", webServiceDescriptor.getTargetNamespace());
    }

    @Test
    public void getTypesNamespace() {
        assertEquals("http://www.typesnamespace.com", webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void setTypesNamespace() {
        webServiceDescriptor.setTypesNamespace("http://www.newtypesnamespace.com");
        assertEquals("http://www.newtypesnamespace.com", webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void equals_NullObject_ReturnsFalse() {
        assertFalse(webServiceDescriptor.equals(null));
    }

    @Test
    public void equals_SameObject_ReturnsTrue() {
        assertTrue(webServiceDescriptor.equals(webServiceDescriptor));
    }

    @Test
    public void equals_DifferentClass_ReturnsFalse() {
        assertFalse(webServiceDescriptor.equals(new Object()));
    }

    @Test
    public void equals_ServiceClazzNull_OtherServiceClazzNull_ReturnsTrue() {
        webServiceDescriptor.setServiceClazz(null);
        WebServiceDescriptor other = new WebServiceDescriptor(null, "http://www.targetnamespace.com", "http://www.typesnamespace.com");
        assertTrue(webServiceDescriptor.equals(other));
    }

    @Test
    public void equals_ServiceClazzNull_OtherServiceClazzNotNull_ReturnsFalse() {
        webServiceDescriptor.setServiceClazz(null);
        WebServiceDescriptor other = new WebServiceDescriptor(TestService.class, "http://www.targetnamespace.com", "http://www.typesnamespace.com");
        assertFalse(webServiceDescriptor.equals(other));
    }

    @Test
    public void equals_ServiceClazzNotNull_OtherServiceClazzNull_ReturnsFalse() {
        webServiceDescriptor.setServiceClazz(TestService.class);
        WebServiceDescriptor other = new WebServiceDescriptor(null, "http://www.targetnamespace.com", "http://www.typesnamespace.com");
        assertFalse(webServiceDescriptor.equals(other));
    }

    @Test
    public void equals_ServiceClazzNotNull_OtherServiceClazzNotNull_EqualServiceClazz_ReturnsTrue() {
        webServiceDescriptor.setServiceClazz(TestService.class);
        WebServiceDescriptor other = new WebServiceDescriptor(TestService.class, "http://www.targetnamespace.com", "http://www.typesnamespace.com");
        assertTrue(webServiceDescriptor.equals(other));
    }

    @Test
    public void equals_ServiceClazzNotNull_OtherServiceClazzNotNull_DifferentServiceClazz_ReturnsFalse() {
        webServiceDescriptor.setServiceClazz(TestService.class);
        WebServiceDescriptor other = new WebServiceDescriptor(AnotherService.class, "http://www.targetnamespace.com", "http://www.typesnamespace.com");
        assertFalse(webServiceDescriptor.equals(other));
    }

    @Test
    public void hashCode_ServiceClazzNull_ReturnsCorrectHashCode() {
        webServiceDescriptor.setServiceClazz(null);
        int expectedHashCode = 31 * 1;
        assertEquals(expectedHashCode, webServiceDescriptor.hashCode());
    }

    @Test
    public void hashCode_ServiceClazzNotNull_ReturnsCorrectHashCode() {
        webServiceDescriptor.setServiceClazz(TestService.class);
        int expectedHashCode = 31 * 1 + TestService.class.hashCode();
        assertEquals(expectedHashCode, webServiceDescriptor.hashCode());
    }

    private static class TestService {
    }

    private static class AnotherService {
    }
}