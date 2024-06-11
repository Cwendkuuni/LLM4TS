import org.junit.Test;
import static org.junit.Assert.*;

public class WebServiceDescriptorTest {

    @Test
    public void testConstructor() {
        Class serviceClazz = MyClass.class;
        String targetNamespace = "http://www.example.com/target";
        String typesNamespace = "http://www.example.com/types";
        
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        
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
    public void testHashCode() {
        Class serviceClazz = MyClass.class;
        
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz, "http://www.example.com/target", "http://www.example.com/types");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz, "http://www.example.com/target2", "http://www.example.com/types2");
        
        assertEquals(descriptor1.hashCode(), descriptor1.hashCode());
        assertNotEquals(descriptor1.hashCode(), descriptor2.hashCode());
    }
    
    @Test
    public void testEquals() {
        Class serviceClazz = MyClass.class;
        
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz, "http://www.example.com/target", "http://www.example.com/types");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz, "http://www.example.com/target2", "http://www.example.com/types2");
        WebServiceDescriptor descriptor3 = new WebServiceDescriptor(serviceClazz, "http://www.example.com/target", "http://www.example.com/types");
        
        assertTrue(descriptor1.equals(descriptor1));
        assertFalse(descriptor1.equals(descriptor2));
        assertTrue(descriptor1.equals(descriptor3));
    }
}