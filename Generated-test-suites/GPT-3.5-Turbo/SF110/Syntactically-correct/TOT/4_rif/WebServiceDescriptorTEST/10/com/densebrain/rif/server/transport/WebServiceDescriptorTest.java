import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WebServiceDescriptorTest {
    
    private WebServiceDescriptor webServiceDescriptor;

    @Before
    public void setUp() {
        webServiceDescriptor = new WebServiceDescriptor(WebServiceDescriptorTest.class, "com.target.namespace", "com.types.namespace");
    }

    @Test
    public void testGetServiceClazz() {
        assertEquals(WebServiceDescriptorTest.class, webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testSetServiceClazz() {
        webServiceDescriptor.setServiceClazz(String.class);
        assertEquals(String.class, webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testGetTargetNamespace() {
        assertEquals("com.target.namespace", webServiceDescriptor.getTargetNamespace());
    }

    @Test
    public void testSetTargetNamespace() {
        webServiceDescriptor.setTargetNamespace("new.target.namespace");
        assertEquals("new.target.namespace", webServiceDescriptor.getTargetNamespace());
    }

    @Test
    public void testGetTypesNamespace() {
        assertEquals("com.types.namespace", webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void testSetTypesNamespace() {
        webServiceDescriptor.setTypesNamespace("new.types.namespace");
        assertEquals("new.types.namespace", webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void testHashCode() {
        WebServiceDescriptor otherDescriptor = new WebServiceDescriptor(WebServiceDescriptorTest.class, "com.target.namespace", "com.types.namespace");
        assertEquals(otherDescriptor.hashCode(), webServiceDescriptor.hashCode());
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
    public void testEqualsDifferentClassObject() {
        assertFalse(webServiceDescriptor.equals("SomeOtherClass"));
    }

    @Test
    public void testEqualsDifferentServiceClass() {
        WebServiceDescriptor differentServiceDescriptor = new WebServiceDescriptor(String.class, "com.target.namespace", "com.types.namespace");
        assertFalse(webServiceDescriptor.equals(differentServiceDescriptor));
    }

    @Test
    public void testEqualsSameServiceClass() {
        WebServiceDescriptor sameServiceDescriptor = new WebServiceDescriptor(WebServiceDescriptorTest.class, "different.target.namespace", "some.types.namespace");
        assertTrue(webServiceDescriptor.equals(sameServiceDescriptor));
    }
}