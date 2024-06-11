import org.junit.Test;
import static org.junit.Assert.*;

public class WebServiceDescriptorTest {
    
    @Test
    public void testConstructor() {
        Class clazz = new Class();
        String targetNamespace = "http://www.example.com/target";
        String typesNamespace = "http://www.example.com/types";
        
        WebServiceDescriptor descriptor = new WebServiceDescriptor(clazz, targetNamespace, typesNamespace);
        
        assertSame(clazz, descriptor.getServiceClazz());
        assertEquals(targetNamespace, descriptor.getTargetNamespace());
        assertEquals(typesNamespace, descriptor.getTypesNamespace());
    }
    
    @Test
    public void testSetServiceClazz() {
        Class clazz = new Class();
        WebServiceDescriptor descriptor = new WebServiceDescriptor();
        
        descriptor.setServiceClazz(clazz);
        
        assertSame(clazz, descriptor.getServiceClazz());
    }
    
    @Test
    public void testSetTargetNamespace() {
        String targetNamespace = "http://www.example.com/target";
        WebServiceDescriptor descriptor = new WebServiceDescriptor();
        
        descriptor.setTargetNamespace(targetNamespace);
        
        assertEquals(targetNamespace, descriptor.getTargetNamespace());
    }
    
    @Test
    public void testSetTypesNamespace() {
        String typesNamespace = "http://www.example.com/types";
        WebServiceDescriptor descriptor = new WebServiceDescriptor();
        
        descriptor.setTypesNamespace(typesNamespace);
        
        assertEquals(typesNamespace, descriptor.getTypesNamespace());
    }
    
    @Test
    public void testHashCode() {
        Class clazz = new Class();
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(clazz, "http://www.example.com/target1", "http://www.example.com/types1");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(clazz, "http://www.example.com/target2", "http://www.example.com/types2");
        
        int hashCode1 = descriptor1.hashCode();
        int hashCode2 = descriptor2.hashCode();
        
        assertNotEquals(hashCode1, hashCode2);
    }
    
    @Test
    public void testEquals() {
        Class clazz1 = new Class();
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(clazz1, "http://www.example.com/target", "http://www.example.com/types");
        
        Class clazz2 = new Class();
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(clazz2, "http://www.example.com/target", "http://www.example.com/types");
        
        Class clazz3 = new Class();
        WebServiceDescriptor descriptor3 = new WebServiceDescriptor(clazz3, "http://www.example.com/target1", "http://www.example.com/types1");
        
        assertTrue(descriptor1.equals(descriptor2));
        assertFalse(descriptor1.equals(descriptor3));
    }
    
}