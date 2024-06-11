import org.junit.Test;
import static org.junit.Assert.*;

public class WebServiceDescriptorTest {

    public WebServiceDescriptor webServiceDescriptor;

    @Before
    public void setUp() {
        webServiceDescriptor = new WebServiceDescriptor(null, null, null);
    }

    @Test
    public void testSetServiceClazz() {
        Class clazz = SomeClass.class;
        webServiceDescriptor.setServiceClazz(clazz);
        assertEquals(clazz, webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testSetTargetNamespace() {
        String targetNamespace = "http://example.com/namespace";
        webServiceDescriptor.setTargetNamespace(targetNamespace);
        assertEquals(targetNamespace, webServiceDescriptor.getTargetNamespace());
    }

    @Test
    public void testSetTypesNamespace() {
        String typesNamespace = "http://example.com/types";
        webServiceDescriptor.setTypesNamespace(typesNamespace);
        assertEquals(typesNamespace, webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void testHashCode() {
        Class clazz = SomeClass.class;
        webServiceDescriptor.setServiceClazz(clazz);
        int expectedHashCode = 31 * 1 + ((clazz == null) ? 0 : clazz.hashCode());
        assertEquals(expectedHashCode, webServiceDescriptor.hashCode());
    }

    @Test
    public void testEquals() {
        Class clazz1 = SomeClass.class;
        Class clazz2 = AnotherClass.class;
        webServiceDescriptor.setServiceClazz(clazz1);

        assertTrue(webServiceDescriptor.equals(webServiceDescriptor));
        assertFalse(webServiceDescriptor.equals(null));
        assertFalse(webServiceDescriptor.equals(new Object()));

        WebServiceDescriptor equalDescriptor = new WebServiceDescriptor(clazz1, null, null);
        assertTrue(webServiceDescriptor.equals(equalDescriptor));

        WebServiceDescriptor differentClazzDescriptor = new WebServiceDescriptor(clazz2, null, null);
        assertFalse(webServiceDescriptor.equals(differentClazzDescriptor));
    }
}



// JUnit test cases go here

