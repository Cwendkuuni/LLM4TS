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
        assertEquals(null, webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testSetServiceClazz() {
        webServiceDescriptor.setServiceClazz(String.class);
        assertEquals(String.class, webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testGetTargetNamespace() {
        assertEquals(null, webServiceDescriptor.getTargetNamespace());
    }

    @Test
    public void testSetTargetNamespace() {
        String targetNamespace = "http://www.example.com";
        webServiceDescriptor.setTargetNamespace(targetNamespace);
        assertEquals(targetNamespace, webServiceDescriptor.getTargetNamespace());
    }

    @Test
    public void testGetTypesNamespace() {
        assertEquals(null, webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void testSetTypesNamespace() {
        String typesNamespace = "http://www.example.com/types";
        webServiceDescriptor.setTypesNamespace(typesNamespace);
        assertEquals(typesNamespace, webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void testHashCode() {
        webServiceDescriptor.setServiceClazz(String.class);
        int expectedHashCode = 31 * 1 + ((String.class == null) ? 0 : String.class.hashCode());
        assertEquals(expectedHashCode, webServiceDescriptor.hashCode());
    }

    @Test
    public void testEquals_sameObject() {
        assertTrue(webServiceDescriptor.equals(webServiceDescriptor));
    }

    @Test
    public void testEquals_nullObject() {
        assertFalse(webServiceDescriptor.equals(null));
    }

    @Test
    public void testEquals_differentClass() {
        assertFalse(webServiceDescriptor.equals(new Object()));
    }

    @Test
    public void testEquals_sameServiceClazz_null() {
        webServiceDescriptor.setServiceClazz(null);
        WebServiceDescriptor other = new WebServiceDescriptor(null, "", "");
        assertTrue(webServiceDescriptor.equals(other));
    }

    @Test
    public void testEquals_sameServiceClazz_notNull() {
        webServiceDescriptor.setServiceClazz(String.class);
        WebServiceDescriptor other = new WebServiceDescriptor(String.class, "", "");
        assertTrue(webServiceDescriptor.equals(other));
    }

    @Test
    public void testEquals_differentServiceClazz() {
        webServiceDescriptor.setServiceClazz(String.class);
        WebServiceDescriptor other = new WebServiceDescriptor(Integer.class, "", "");
        assertFalse(webServiceDescriptor.equals(other));
    }

    @Test
    public void testEquals_sameServiceClazz_notEqual() {
        webServiceDescriptor.setServiceClazz(String.class);
        WebServiceDescriptor other = new WebServiceDescriptor(StringBuilder.class, "", "");
        assertFalse(webServiceDescriptor.equals(other));
    }

    @Test
    public void testEquals_sameServiceClazz_sameTargetNamespace_null() {
        webServiceDescriptor.setServiceClazz(String.class);
        webServiceDescriptor.setTargetNamespace(null);
        WebServiceDescriptor other = new WebServiceDescriptor(String.class, null, "");
        assertTrue(webServiceDescriptor.equals(other));
    }

    @Test
    public void testEquals_sameServiceClazz_sameTargetNamespace_notNull() {
        webServiceDescriptor.setServiceClazz(String.class);
        webServiceDescriptor.setTargetNamespace("http://www.example.com");
        WebServiceDescriptor other = new WebServiceDescriptor(String.class, "http://www.example.com", "");
        assertTrue(webServiceDescriptor.equals(other));
    }

    @Test
    public void testEquals_sameServiceClazz_sameTargetNamespace_notEqual() {
        webServiceDescriptor.setServiceClazz(String.class);
        webServiceDescriptor.setTargetNamespace("http://www.example.com");
        WebServiceDescriptor other = new WebServiceDescriptor(String.class, "http://www.google.com", "");
        assertFalse(webServiceDescriptor.equals(other));
    }

    @Test
    public void testEquals_sameServiceClazz_sameTargetNamespace_sameTypesNamespace_null() {
        webServiceDescriptor.setServiceClazz(String.class);
        webServiceDescriptor.setTargetNamespace("http://www.example.com");
        webServiceDescriptor.setTypesNamespace(null);
        WebServiceDescriptor other = new WebServiceDescriptor(String.class, "http://www.example.com", null);
        assertTrue(webServiceDescriptor.equals(other));
    }

    @Test
    public void testEquals_sameServiceClazz_sameTargetNamespace_sameTypesNamespace_notNull() {
        webServiceDescriptor.setServiceClazz(String.class);
        webServiceDescriptor.setTargetNamespace("http://www.example.com");
        webServiceDescriptor.setTypesNamespace("http://www.example.com/types");
        WebServiceDescriptor other = new WebServiceDescriptor(String.class, "http://www.example.com", "http://www.example.com/types");
        assertTrue(webServiceDescriptor.equals(other));
    }

    @Test
    public void testEquals_sameServiceClazz_sameTargetNamespace_sameTypesNamespace_notEqual() {
        webServiceDescriptor.setServiceClazz(String.class);
        webServiceDescriptor.setTargetNamespace("http://www.example.com");
        webServiceDescriptor.setTypesNamespace("http://www.example.com/types");
        WebServiceDescriptor other = new WebServiceDescriptor(String.class, "http://www.example.com", "http://www.google.com/types");
        assertFalse(webServiceDescriptor.equals(other));
    }
}