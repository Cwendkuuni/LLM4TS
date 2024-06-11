import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class WebServiceDescriptorTest {

    private WebServiceDescriptor webServiceDescriptor;

    @Before
    public void setUp() {
        Class serviceClazz = TestClass.class;
        String targetNamespace = "http://www.example.com";
        String typesNamespace = "http://www.example.com/types";
        webServiceDescriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
    }

    @Test
    public void testGetServiceClazz() {
        Class expected = TestClass.class;
        Class actual = webServiceDescriptor.getServiceClazz();
        assertEquals(expected, actual);
    }

    @Test
    public void testSetServiceClazz() {
        Class expected = AnotherTestClass.class;
        webServiceDescriptor.setServiceClazz(expected);
        Class actual = webServiceDescriptor.getServiceClazz();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetTargetNamespace() {
        String expected = "http://www.example.com";
        String actual = webServiceDescriptor.getTargetNamespace();
        assertEquals(expected, actual);
    }

    @Test
    public void testSetTargetNamespace() {
        String expected = "http://www.anotherexample.com";
        webServiceDescriptor.setTargetNamespace(expected);
        String actual = webServiceDescriptor.getTargetNamespace();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetTypesNamespace() {
        String expected = "http://www.example.com/types";
        String actual = webServiceDescriptor.getTypesNamespace();
        assertEquals(expected, actual);
    }

    @Test
    public void testSetTypesNamespace() {
        String expected = "http://www.anotherexample.com/types";
        webServiceDescriptor.setTypesNamespace(expected);
        String actual = webServiceDescriptor.getTypesNamespace();
        assertEquals(expected, actual);
    }

    @Test
    public void testHashCode() {
        int expected = 31 * 1 + webServiceDescriptor.getServiceClazz().hashCode();
        int actual = webServiceDescriptor.hashCode();
        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        WebServiceDescriptor other = new WebServiceDescriptor(TestClass.class, "http://www.example.com", "http://www.example.com/types");
        assertTrue(webServiceDescriptor.equals(other));
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
        assertFalse(webServiceDescriptor.equals("Not a WebServiceDescriptor object"));
    }

    @Test
    public void testEquals_differentServiceClazz() {
        WebServiceDescriptor other = new WebServiceDescriptor(AnotherTestClass.class, "http://www.example.com", "http://www.example.com/types");
        assertFalse(webServiceDescriptor.equals(other));
    }

    class TestClass {}

    class AnotherTestClass {}
}