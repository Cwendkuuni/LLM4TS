package com.densebrain.rif.server.transport;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class WebServiceDescriptorTest {
    
    private WebServiceDescriptor webServiceDescriptor;
    
    @Before
    public void setup() {
        Class serviceClazz = TestService.class;
        String targetNamespace = "http://www.example.com/targetNamespace";
        String typesNamespace = "http://www.example.com/typesNamespace";
        webServiceDescriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
    }
    
    @Test
    public void testGetServiceClazz() {
        Class expectedServiceClazz = TestService.class;
        Assert.assertEquals(expectedServiceClazz, webServiceDescriptor.getServiceClazz());
    }
    
    @Test
    public void testSetServiceClazz() {
        Class newServiceClazz = AnotherService.class;
        webServiceDescriptor.setServiceClazz(newServiceClazz);
        Assert.assertEquals(newServiceClazz, webServiceDescriptor.getServiceClazz());
    }
    
    @Test
    public void testGetTargetNamespace() {
        String expectedTargetNamespace = "http://www.example.com/targetNamespace";
        Assert.assertEquals(expectedTargetNamespace, webServiceDescriptor.getTargetNamespace());
    }
    
    @Test
    public void testSetTargetNamespace() {
        String newTargetNamespace = "http://www.example.com/newTargetNamespace";
        webServiceDescriptor.setTargetNamespace(newTargetNamespace);
        Assert.assertEquals(newTargetNamespace, webServiceDescriptor.getTargetNamespace());
    }
    
    @Test
    public void testGetTypesNamespace() {
        String expectedTypesNamespace = "http://www.example.com/typesNamespace";
        Assert.assertEquals(expectedTypesNamespace, webServiceDescriptor.getTypesNamespace());
    }
    
    @Test
    public void testSetTypesNamespace() {
        String newTypesNamespace = "http://www.example.com/newTypesNamespace";
        webServiceDescriptor.setTypesNamespace(newTypesNamespace);
        Assert.assertEquals(newTypesNamespace, webServiceDescriptor.getTypesNamespace());
    }
    
    @Test
    public void testHashCode() {
        int expectedHashCode = 31 * 1 + ((webServiceDescriptor.getServiceClazz() == null) ? 0 : webServiceDescriptor.getServiceClazz().hashCode());
        Assert.assertEquals(expectedHashCode, webServiceDescriptor.hashCode());
    }
    
    @Test
    public void testEquals() {
        WebServiceDescriptor sameWebServiceDescriptor = new WebServiceDescriptor(TestService.class, "http://www.example.com/targetNamespace", "http://www.example.com/typesNamespace");
        Assert.assertTrue(webServiceDescriptor.equals(sameWebServiceDescriptor));
        
        WebServiceDescriptor differentWebServiceDescriptor = new WebServiceDescriptor(AnotherService.class, "http://www.example.com/anotherTargetNamespace", "http://www.example.com/anotherTypesNamespace");
        Assert.assertFalse(webServiceDescriptor.equals(differentWebServiceDescriptor));
    }
}