import org.junit.Test;
import static org.junit.Assert.*;

public class WebServiceDescriptorTest {

    @Test
    public void testConstructor() {
        Class serviceClazz = MyClass.class;
        String targetNamespace = "http://www.example.com/target";
        String typesNamespace = "http://www.example.com/types";
        
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        
        assertNotNull(descriptor);
        assertEquals(serviceClazz, descriptor.getServiceClazz());
        assertEquals(targetNamespace, descriptor.getTargetNamespace());
        assertEquals(typesNamespace, descriptor.getTypesNamespace());
    }
    
    @Test
    public void testSetServiceClazz() {
        Class serviceClazz = MyClass.class;
        
        WebServiceDescriptor descriptor = new WebServiceDescriptor();
        descriptor.setServiceClazz(serviceClazz);
        
        assertEquals(serviceClazz, descriptor.getServiceClazz());
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
    public void testEquals() {
        Class serviceClazz1 = MyClass.class;
        String targetNamespace1 = "http://www.example.com/target1";
        String typesNamespace1 = "http://www.example.com/types1";
        
        Class serviceClazz2 = MyClass.class;
        String targetNamespace2 = "http://www.example.com/target2";
        String typesNamespace2 = "http://www.example.com/types2";
        
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz1, targetNamespace1, typesNamespace1);
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz2, targetNamespace2, typesNamespace2);
        
        // Test with same serviceClazz
        assertTrue(descriptor1.equals(descriptor2));
        
        // Test with different serviceClazz
        descriptor2.setServiceClazz(OtherClass.class);
        assertFalse(descriptor1.equals(descriptor2));
    }
    
    @Test
    public void testHashCode() {
        Class serviceClazz = MyClass.class;
        String targetNamespace = "http://www.example.com/target";
        String typesNamespace = "http://www.example.com/types";
        
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        
        int expectedHashCode = 31 * serviceClazz.hashCode();
        assertEquals(expectedHashCode, descriptor.hashCode());
    }
}