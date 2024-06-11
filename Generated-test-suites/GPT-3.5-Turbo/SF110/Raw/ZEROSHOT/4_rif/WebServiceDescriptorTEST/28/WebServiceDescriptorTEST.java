package com.densebrain.rif.server.transport;

import org.junit.Test;
import static org.junit.Assert.*;

public class WebServiceDescriptorTest {

    @Test
    public void testGetServiceClazz() {
        Class expectedClass = MyClass.class;
        WebServiceDescriptor descriptor = new WebServiceDescriptor(expectedClass, "target", "types");
        Class actualClass = descriptor.getServiceClazz();
        
        assertEquals(expectedClass, actualClass);
    }
    
    @Test
    public void testSetServiceClazz() {
        Class expectedClass = MyClass.class;
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, "target", "types");
        descriptor.setServiceClazz(expectedClass);
        
        assertEquals(expectedClass, descriptor.getServiceClazz());
    }
    
    @Test
    public void testGetTargetNamespace() {
        String expectedNamespace = "target";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(MyClass.class, expectedNamespace, "types");
        String actualNamespace = descriptor.getTargetNamespace();
        
        assertEquals(expectedNamespace, actualNamespace);
    }
    
    @Test
    public void testSetTargetNamespace() {
        String expectedNamespace = "target";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(MyClass.class, null, "types");
        descriptor.setTargetNamespace(expectedNamespace);
        
        assertEquals(expectedNamespace, descriptor.getTargetNamespace());
    }
    
    @Test
    public void testGetTypesNamespace() {
        String expectedNamespace = "types";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(MyClass.class, "target", expectedNamespace);
        String actualNamespace = descriptor.getTypesNamespace();
        
        assertEquals(expectedNamespace, actualNamespace);
    }
    
    @Test
    public void testSetTypesNamespace() {
        String expectedNamespace = "types";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(MyClass.class, "target", null);
        descriptor.setTypesNamespace(expectedNamespace);
        
        assertEquals(expectedNamespace, descriptor.getTypesNamespace());
    }
    
    @Test
    public void testHashCode() {
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(MyClass.class, "target", "types");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(MyClass.class, "target", "types");
        
        assertEquals(descriptor1.hashCode(), descriptor2.hashCode());
    }
    
    @Test
    public void testEquals() {
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(MyClass.class, "target", "types");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(MyClass.class, "target", "types");
        WebServiceDescriptor descriptor3 = new WebServiceDescriptor(OtherClass.class, "target", "types");
        
        assertTrue(descriptor1.equals(descriptor2));
        assertFalse(descriptor1.equals(descriptor3));
    }
}

class MyClass {}
class OtherClass {}