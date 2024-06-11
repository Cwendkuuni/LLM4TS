import org.junit.Test;
import static org.junit.Assert.*;

public class WebServiceDescriptorTest {

    @Test
    public void testConstructor() {
        Class serviceClazz = String.class;
        String targetNamespace = "http://testNamespace";
        String typesNamespace = "http://testTypesNamespace";
        WebServiceDescriptor wsDescriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        
        assertEquals(serviceClazz, wsDescriptor.getServiceClazz());
        assertEquals(targetNamespace, wsDescriptor.getTargetNamespace());
        assertEquals(typesNamespace, wsDescriptor.getTypesNamespace());
    }

    @Test
    public void testSetServiceClazz() {
        WebServiceDescriptor wsDescriptor = new WebServiceDescriptor(null, "", "");
        Class serviceClazz = String.class;

        wsDescriptor.setServiceClazz(serviceClazz);
        assertEquals(serviceClazz, wsDescriptor.getServiceClazz());
    }

    @Test
    public void testSetTargetNamespace() {
        WebServiceDescriptor wsDescriptor = new WebServiceDescriptor(null, "", "");
        String targetNamespace = "http://newTestNamespace";

        wsDescriptor.setTargetNamespace(targetNamespace);
        assertEquals(targetNamespace, wsDescriptor.getTargetNamespace());
    }

    @Test
    public void testSetTypesNamespace() {
        WebServiceDescriptor wsDescriptor = new WebServiceDescriptor(null, "", "");
        String typesNamespace = "http://newTestTypesNamespace";

        wsDescriptor.setTypesNamespace(typesNamespace);
        assertEquals(typesNamespace, wsDescriptor.getTypesNamespace());
    }

    @Test
    public void testHashCode() {
        Class serviceClazz = String.class;
        WebServiceDescriptor wsDescriptor1 = new WebServiceDescriptor(serviceClazz, "http://testNamespace", "http://testTypesNamespace");
        WebServiceDescriptor wsDescriptor2 = new WebServiceDescriptor(serviceClazz, "http://testNamespace", "http://testTypesNamespace");

        assertEquals(wsDescriptor1.hashCode(), wsDescriptor2.hashCode());
    }

    @Test
    public void testEquals_sameObject() {
        WebServiceDescriptor wsDescriptor = new WebServiceDescriptor(String.class, "http://testNamespace", "http://testTypesNamespace");
        
        assertTrue(wsDescriptor.equals(wsDescriptor));
    }

    @Test
    public void testEquals_differentClass() {
        WebServiceDescriptor wsDescriptor = new WebServiceDescriptor(String.class, "http://testNamespace", "http://testTypesNamespace");
        
        assertFalse(wsDescriptor.equals(new Object()));
    }

    @Test
    public void testEquals_nullClass() {
        WebServiceDescriptor wsDescriptor1 = new WebServiceDescriptor(null, "http://testNamespace", "http://testTypesNamespace");
        WebServiceDescriptor wsDescriptor2 = new WebServiceDescriptor(null, "http://testNamespace", "http://testTypesNamespace");

        assertTrue(wsDescriptor1.equals(wsDescriptor2));
    }

    @Test
    public void testEquals_differentClassNotNull() {
        WebServiceDescriptor wsDescriptor1 = new WebServiceDescriptor(String.class, "http://testNamespace", "http://testTypesNamespace");
        WebServiceDescriptor wsDescriptor2 = new WebServiceDescriptor(Integer.class, "http://testNamespace", "http://testTypesNamespace");

        assertFalse(wsDescriptor1.equals(wsDescriptor2));
    }
}