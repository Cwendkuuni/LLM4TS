import static org.junit.Assert.*;
import org.junit.Test;

public class WebServiceDescriptorTest {

    @Test
    public void testConstructor(){
        Class<?> serviceClazz = String.class;
        String targetNamespace = "http://www.example.com";
        String typesNamespace = "http://www.example.com/types";
        
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        
        assertEquals(serviceClazz, descriptor.getServiceClazz());
        assertEquals(targetNamespace, descriptor.getTargetNamespace());
        assertEquals(typesNamespace, descriptor.getTypesNamespace());
    }
    
    @Test
    public void testSetServiceClazz(){
        Class<?> serviceClazz = String.class;
        
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, null, null);
        descriptor.setServiceClazz(serviceClazz);
        
        assertEquals(serviceClazz, descriptor.getServiceClazz());
    }
    
    @Test
    public void testSetTargetNamespace(){
        String targetNamespace = "http://www.example.com";
        
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, null, null);
        descriptor.setTargetNamespace(targetNamespace);
        
        assertEquals(targetNamespace, descriptor.getTargetNamespace());
    }
    
    @Test
    public void testSetTypesNamespace(){
        String typesNamespace = "http://www.example.com/types";
        
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, null, null);
        descriptor.setTypesNamespace(typesNamespace);
        
        assertEquals(typesNamespace, descriptor.getTypesNamespace());
    }
    
    @Test
    public void testEquals(){
        Class<?> serviceClazz1 = String.class;
        String targetNamespace1 = "http://www.example.com";
        String typesNamespace1 = "http://www.example.com/types";
        
        Class<?> serviceClazz2 = Integer.class;
        String targetNamespace2 = "http://www.example.org";
        String typesNamespace2 = "http://www.example.org/types";
        
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz1, targetNamespace1, typesNamespace1);
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz1, targetNamespace1, typesNamespace1);
        WebServiceDescriptor descriptor3 = new WebServiceDescriptor(serviceClazz2, targetNamespace2, typesNamespace2);
        
        assertEquals(descriptor1, descriptor2);
        assertNotEquals(descriptor1, descriptor3);
    }
    
    @Test
    public void testHashCode(){
        Class<?> serviceClazz1 = String.class;
        String targetNamespace1 = "http://www.example.com";
        String typesNamespace1 = "http://www.example.com/types";
        
        Class<?> serviceClazz2 = Integer.class;
        String targetNamespace2 = "http://www.example.org";
        String typesNamespace2 = "http://www.example.org/types";
        
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz1, targetNamespace1, typesNamespace1);
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz1, targetNamespace1, typesNamespace1);
        WebServiceDescriptor descriptor3 = new WebServiceDescriptor(serviceClazz2, targetNamespace2, typesNamespace2);
        
        assertEquals(descriptor1.hashCode(), descriptor2.hashCode());
        assertNotEquals(descriptor1.hashCode(), descriptor3.hashCode());
    }
}