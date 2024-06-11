
import org.junit.Test;
import static org.junit.Assert.*;

public class WebServiceDescriptorTest {
    
    @Test
    public void testGetServiceClazz() {
        Class expected = Object.class;
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(expected, "", "");
        Class actual = webServiceDescriptor.getServiceClazz();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testSetServiceClazz() {
        Class expected = Object.class;
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, "", "");
        webServiceDescriptor.setServiceClazz(expected);
        Class actual = webServiceDescriptor.getServiceClazz();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetTargetNamespace() {
        String expected = "testNamespace";
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(Object.class, expected, "");
        String actual = webServiceDescriptor.getTargetNamespace();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testSetTargetNamespace() {
        String expected = "testNamespace";
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(Object.class, null, "");
        webServiceDescriptor.setTargetNamespace(expected);
        String actual = webServiceDescriptor.getTargetNamespace();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetTypesNamespace() {
        String expected = "testTypesNamespace";
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(Object.class, "", expected);
        String actual = webServiceDescriptor.getTypesNamespace();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testSetTypesNamespace() {
        String expected = "testTypesNamespace";
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(Object.class, "", null);
        webServiceDescriptor.setTypesNamespace(expected);
        String actual = webServiceDescriptor.getTypesNamespace();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testEquals() {
        Class serviceClazz = Object.class;
        String targetNamespace = "testNamespace";
        String typesNamespace = "testTypesNamespace";
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        assertTrue(webServiceDescriptor1.equals(webServiceDescriptor2));
    }
    
    @Test
    public void testHashCode() {
        Class serviceClazz = Object.class;
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, "", "");
        int expected = serviceClazz.hashCode();
        int actual = webServiceDescriptor.hashCode();
        assertEquals(expected, actual);
    }
}
