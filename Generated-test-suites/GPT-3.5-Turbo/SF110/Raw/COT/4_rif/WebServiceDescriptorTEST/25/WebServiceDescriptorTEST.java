import org.junit.Test;
import static org.junit.Assert.*;

public class WebServiceDescriptorTest {
    // 
    
    @Test
    public void testGetServiceClazz() {
        Class expected = String.class;
        WebServiceDescriptor descriptor = new WebServiceDescriptor(expected, "", "");
        assertEquals(expected, descriptor.getServiceClazz());
    }
    
    @Test
    public void testSetServiceClazz() {
        Class expected = Integer.class;
        WebServiceDescriptor descriptor = new WebServiceDescriptor("", "", "");
        descriptor.setServiceClazz(expected);
        assertEquals(expected, descriptor.getServiceClazz());
    }
    
    @Test
    public void testGetTargetNamespace() {
        String expected = "http://example.com";
        WebServiceDescriptor descriptor = new WebServiceDescriptor("", expected, "");
        assertEquals(expected, descriptor.getTargetNamespace());
    }
    
    @Test
    public void testSetTargetNamespace() {
        String expected = "http://example.com";
        WebServiceDescriptor descriptor = new WebServiceDescriptor("", "", "");
        descriptor.setTargetNamespace(expected);
        assertEquals(expected, descriptor.getTargetNamespace());
    }
    
    @Test
    public void testGetTypesNamespace() {
        String expected = "http://example.com/types";
        WebServiceDescriptor descriptor = new WebServiceDescriptor("", "", expected);
        assertEquals(expected, descriptor.getTypesNamespace());
    }
    
    @Test
    public void testSetTypesNamespace() {
        String expected = "http://example.com/types";
        WebServiceDescriptor descriptor = new WebServiceDescriptor("", "", "");
        descriptor.setTypesNamespace(expected);
        assertEquals(expected, descriptor.getTypesNamespace());
    }
    
    @Test
    public void testHashCode() {
        Class serviceClazz = String.class;
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz, "", "");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz, "", "");
        assertEquals(descriptor1.hashCode(), descriptor2.hashCode());
    }
    
    @Test
    public void testEquals() {
        Class serviceClazz = String.class;
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz, "", "");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz, "", "");
        assertEquals(descriptor1, descriptor2);
    }
    
    // 
}