
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WebServiceDescriptorTest {

    private WebServiceDescriptor webServiceDescriptor;

    @Before
    public void setUp() {
        webServiceDescriptor = new WebServiceDescriptor(null, null, null);
    }

    @Test
    public void testGetServiceClazz() {
        assertNull(webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testSetServiceClazz() {
        Class expectedClass = String.class;
        webServiceDescriptor.setServiceClazz(expectedClass);
        assertEquals(expectedClass, webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testGetTargetNamespace() {
        assertNull(webServiceDescriptor.getTargetNamespace());
    }

    @Test
    public void testSetTargetNamespace() {
        String expectedNamespace = "http://example.com";
        webServiceDescriptor.setTargetNamespace(expectedNamespace);
        assertEquals(expectedNamespace, webServiceDescriptor.getTargetNamespace());
    }

    @Test
    public void testGetTypesNamespace() {
        assertNull(webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void testSetTypesNamespace() {
        String expectedNamespace = "http://example.com/types";
        webServiceDescriptor.setTypesNamespace(expectedNamespace);
        assertEquals(expectedNamespace, webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void testHashCode() {
        webServiceDescriptor.setServiceClazz(String.class);
        int expectedHashCode = 31 * ((webServiceDescriptor.getServiceClazz() == null) ? 0 : webServiceDescriptor.getServiceClazz().hashCode());
        assertEquals(expectedHashCode, webServiceDescriptor.hashCode());
    }

    @Test
    public void testEqualsSameObject() {
        assertTrue(webServiceDescriptor.equals(webServiceDescriptor));
    }

    @Test
    public void testEqualsNullObject() {
        assertFalse(webServiceDescriptor.equals(null));
    }

    @Test
    public void testEqualsDifferentClass() {
        assertFalse(webServiceDescriptor.equals("not a WebServiceDescriptor"));
    }

    @Test
    public void testEqualsDifferentServiceClazz() {
        webServiceDescriptor.setServiceClazz(String.class);
        WebServiceDescriptor other = new WebServiceDescriptor(null, null, null);
        assertFalse(webServiceDescriptor.equals(other));
    }

    @Test
    public void testEqualsSameServiceClazz() {
        webServiceDescriptor.setServiceClazz(String.class);
        WebServiceDescriptor other = new WebServiceDescriptor(String.class, null, null);
        assertTrue(webServiceDescriptor.equals(other));
    }
}
