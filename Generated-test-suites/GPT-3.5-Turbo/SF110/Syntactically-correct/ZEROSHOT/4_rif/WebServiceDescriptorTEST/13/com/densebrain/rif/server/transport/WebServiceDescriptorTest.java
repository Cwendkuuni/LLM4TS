import org.junit.Test;
import static org.junit.Assert.*;

public class WebServiceDescriptorTest {
    
    @Test
    public void testGetServiceClazz() {
        Class expected = String.class;
        WebServiceDescriptor descriptor = new WebServiceDescriptor(expected, "namespace", "types");
        assertEquals(expected, descriptor.getServiceClazz());
    }
    
    @Test
    public void testSetServiceClazz() {
        Class expected = Integer.class;
        WebServiceDescriptor descriptor = new WebServiceDescriptor(String.class, "namespace", "types");
        descriptor.setServiceClazz(expected);
        assertEquals(expected, descriptor.getServiceClazz());
    }
    
    @Test
    public void testGetTargetNamespace() {
        String expected = "targetNamespace";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(String.class, expected, "types");
        assertEquals(expected, descriptor.getTargetNamespace());
    }
    
    @Test
    public void testSetTargetNamespace() {
        String expected = "newTargetNamespace";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(String.class, "targetNamespace", "types");
        descriptor.setTargetNamespace(expected);
        assertEquals(expected, descriptor.getTargetNamespace());
    }
    
    @Test
    public void testGetTypesNamespace() {
        String expected = "typesNamespace";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(String.class, "targetNamespace", expected);
        assertEquals(expected, descriptor.getTypesNamespace());
    }
    
    @Test
    public void testSetTypesNamespace() {
        String expected = "newTypesNamespace";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(String.class, "targetNamespace", "typesNamespace");
        descriptor.setTypesNamespace(expected);
        assertEquals(expected, descriptor.getTypesNamespace());
    }
    
    @Test
    public void testEquals() {
        Class class1 = String.class;
        Class class2 = Integer.class;
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(class1, "namespace", "types");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(class1, "namespace", "types");
        WebServiceDescriptor descriptor3 = new WebServiceDescriptor(class2, "namespace", "types");
        
        assertTrue(descriptor1.equals(descriptor2));
        assertFalse(descriptor1.equals(descriptor3));
    }
    
    @Test
    public void testHashCode() {
        Class class1 = String.class;
        Class class2 = Integer.class;
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(class1, "namespace", "types");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(class1, "namespace", "types");
        WebServiceDescriptor descriptor3 = new WebServiceDescriptor(class2, "namespace", "types");
        
        assertEquals(descriptor1.hashCode(), descriptor2.hashCode());
        assertNotEquals(descriptor1.hashCode(), descriptor3.hashCode());
    }
}