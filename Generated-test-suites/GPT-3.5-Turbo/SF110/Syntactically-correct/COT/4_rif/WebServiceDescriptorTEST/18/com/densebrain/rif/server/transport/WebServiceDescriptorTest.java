import org.junit.Test;
import static org.junit.Assert.*;

public class WebServiceDescriptorTest {
    
    @Test
    public void getServiceClazzTest() {
        Class expected = Object.class;
        WebServiceDescriptor descriptor = new WebServiceDescriptor(expected, "", "");
        
        Class actual = descriptor.getServiceClazz();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void setServiceClazzTest() {
        Class expected = Object.class;
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, "", "");
        
        descriptor.setServiceClazz(expected);
        
        Class actual = descriptor.getServiceClazz();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void getTargetNamespaceTest() {
        String expected = "namespace";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, expected, "");
        
        String actual = descriptor.getTargetNamespace();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void setTargetNamespaceTest() {
        String expected = "namespace";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, "", "");
        
        descriptor.setTargetNamespace(expected);
        
        String actual = descriptor.getTargetNamespace();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void getTypesNamespaceTest() {
        String expected = "namespace";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, "", expected);
        
        String actual = descriptor.getTypesNamespace();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void setTypesNamespaceTest() {
        String expected = "namespace";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, "", "");
        
        descriptor.setTypesNamespace(expected);
        
        String actual = descriptor.getTypesNamespace();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void hashCodeTest() {
        Class serviceClazz = Object.class;
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz, "", "");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz, "", "");
        
        int hashCode1 = descriptor1.hashCode();
        int hashCode2 = descriptor2.hashCode();
        
        assertEquals(hashCode1, hashCode2);
    }
    
    @Test
    public void equalsTest_sameInstance() {
        WebServiceDescriptor descriptor = new WebServiceDescriptor(Object.class, "", "");
        
        boolean result = descriptor.equals(descriptor);
        
        assertTrue(result);
    }
    
    @Test
    public void equalsTest_null() {
        WebServiceDescriptor descriptor = new WebServiceDescriptor(Object.class, "", "");
        
        boolean result = descriptor.equals(null);
        
        assertFalse(result);
    }
    
    @Test
    public void equalsTest_differentClass() {
        WebServiceDescriptor descriptor = new WebServiceDescriptor(Object.class, "", "");
        String other = "test";
        
        boolean result = descriptor.equals(other);
        
        assertFalse(result);
    }
    
    @Test
    public void equalsTest_sameServiceClazz() {
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(Object.class, "", "");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(Object.class, "", "");
        
        boolean result = descriptor1.equals(descriptor2);
        
        assertTrue(result);
    }
    
    @Test
    public void equalsTest_differentServiceClazz() {
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(Object.class, "", "");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(String.class, "", "");
        
        boolean result = descriptor1.equals(descriptor2);
        
        assertFalse(result);
    }
}