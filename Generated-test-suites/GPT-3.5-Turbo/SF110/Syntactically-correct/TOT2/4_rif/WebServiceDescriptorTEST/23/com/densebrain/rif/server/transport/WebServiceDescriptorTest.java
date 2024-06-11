package com.densebrain.rif.server.transport;
import static org.junit.Assert.*;
import org.junit.Test;

public class WebServiceDescriptorTest {

    @Test
    public void testConstructor() {
        Class testClass = TestService.class;
        String targetNamespace = "http://testnamespace.com";
        String typesNamespace = "http://testtypenamespace.com";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(testClass, targetNamespace, typesNamespace);
        
        assertEquals(testClass, descriptor.getServiceClazz());
        assertEquals(targetNamespace, descriptor.getTargetNamespace());
        assertEquals(typesNamespace, descriptor.getTypesNamespace());
    }
    
    @Test
    public void testSetServiceClazz() {
        Class newClass = NewTestService.class;
        WebServiceDescriptor descriptor = new WebServiceDescriptor(TestService.class, "http://testnamespace.com", "http://testtypenamespace.com");
        
        descriptor.setServiceClazz(newClass);
        
        assertEquals(newClass, descriptor.getServiceClazz());
    }
    
    @Test
    public void testSetTargetNamespace() {
        String newNamespace = "http://newnamespace.com";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(TestService.class, "http://testnamespace.com", "http://testtypenamespace.com");
        
        descriptor.setTargetNamespace(newNamespace);
        
        assertEquals(newNamespace, descriptor.getTargetNamespace());
    }
    
    @Test
    public void testSetTypesNamespace() {
        String newTypesNamespace = "http://newtypenamespace.com";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(TestService.class, "http://testnamespace.com", "http://testtypenamespace.com");
        
        descriptor.setTypesNamespace(newTypesNamespace);
        
        assertEquals(newTypesNamespace, descriptor.getTypesNamespace());
    }
    
    @Test
    public void testHashCode() {
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(TestService.class, "http://testnamespace.com", "http://testtypenamespace.com");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(TestService.class, "http://testnamespace.com", "http://testtypenamespace.com");
        
        assertEquals(descriptor1.hashCode(), descriptor2.hashCode());
    }
    
    @Test
    public void testEquals() {
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(TestService.class, "http://testnamespace.com", "http://testtypenamespace.com");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(TestService.class, "http://testnamespace.com", "http://testtypenamespace.com");
        WebServiceDescriptor descriptor3 = new WebServiceDescriptor(NewTestService.class, "http://testnamespace.com", "http://testtypenamespace.com");
        
        assertTrue(descriptor1.equals(descriptor2));
        assertFalse(descriptor1.equals(descriptor3));
    }
    
    class TestService {}
    class NewTestService {}
}