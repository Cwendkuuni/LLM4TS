import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class WebServiceDescriptorTest {
    
    WebServiceDescriptor webServiceDescriptor;
    Class serviceClazz;
    String targetNamespace;
    String typesNamespace;
    
    @Before
    public void setup() {
        serviceClazz = MyClass.class;
        targetNamespace = "http://www.example.com/target";
        typesNamespace = "http://www.example.com/types";
        
        webServiceDescriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
    }
    
    @Test
    public void testGetServiceClazz() {
        assertEquals(serviceClazz, webServiceDescriptor.getServiceClazz());
    }
    
    @Test
    public void testSetServiceClazz() {
        Class newServiceClazz = AnotherClass.class;
        webServiceDescriptor.setServiceClazz(newServiceClazz);
        
        assertEquals(newServiceClazz, webServiceDescriptor.getServiceClazz());
    }
    
    @Test
    public void testGetTargetNamespace() {
        assertEquals(targetNamespace, webServiceDescriptor.getTargetNamespace());
    }
    
    @Test
    public void testSetTargetNamespace() {
        String newTargetNamespace = "http://www.example.com/new/target";
        webServiceDescriptor.setTargetNamespace(newTargetNamespace);
        
        assertEquals(newTargetNamespace, webServiceDescriptor.getTargetNamespace());
    }
    
    @Test
    public void testGetTypesNamespace() {
        assertEquals(typesNamespace, webServiceDescriptor.getTypesNamespace());
    }
    
    @Test
    public void testSetTypesNamespace() {
        String newTypesNamespace = "http://www.example.com/new/types";
        webServiceDescriptor.setTypesNamespace(newTypesNamespace);
        
        assertEquals(newTypesNamespace, webServiceDescriptor.getTypesNamespace());
    }
    
    @Test
    public void testHashCode() {
        int expectedResult = 31 * 1 + serviceClazz.hashCode();
        
        assertEquals(expectedResult, webServiceDescriptor.hashCode());
    }
    
    @Test
    public void testEquals_WhenSameObject() {
        assertEquals(true, webServiceDescriptor.equals(webServiceDescriptor));
    }
    
    @Test
    public void testEquals_WhenNullObject() {
        assertEquals(false, webServiceDescriptor.equals(null));
    }
    
    @Test
    public void testEquals_WhenDifferentClass() {
        assertEquals(false, webServiceDescriptor.equals(new Object()));
    }
    
    @Test
    public void testEquals_WhenDifferentServiceClazz() {
        Class differentServiceClazz = AnotherClass.class;
        WebServiceDescriptor differentWebServiceDescriptor = new WebServiceDescriptor(differentServiceClazz, targetNamespace, typesNamespace);
        
        assertEquals(false, webServiceDescriptor.equals(differentWebServiceDescriptor));
    }
    
    @Test
    public void testEquals_WhenServiceClazzIsNull() {
        WebServiceDescriptor nullServiceClazzDescriptor = new WebServiceDescriptor(null, targetNamespace, typesNamespace);
        WebServiceDescriptor anotherNullServiceClazzDescriptor = new WebServiceDescriptor(null, targetNamespace, typesNamespace);
        
        assertEquals(false, nullServiceClazzDescriptor.equals(anotherNullServiceClazzDescriptor));
    }
    
    @Test
    public void testEquals_WhenServiceClazzIsSameInstance() {
        WebServiceDescriptor sameInstanceServiceClazzDescriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        
        assertEquals(true, webServiceDescriptor.equals(sameInstanceServiceClazzDescriptor));
    }

}