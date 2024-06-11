import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class WebServiceDescriptorTest {

    private WebServiceDescriptor webServiceDescriptor;
    private Class serviceClazz;
    private String targetNamespace;
    private String typesNamespace;
    
    @Before
    public void setUp() {
        serviceClazz = Object.class;
        targetNamespace = "http://www.example.com";
        typesNamespace = "http://www.example.com/types";
        
        webServiceDescriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
    }
    
    @Test
    public void testGetServiceClazz() {
        Assert.assertEquals(serviceClazz, webServiceDescriptor.getServiceClazz());
    }
    
    @Test
    public void testSetServiceClazz() {
        Class newServiceClazz = String.class;
        webServiceDescriptor.setServiceClazz(newServiceClazz);
        
        Assert.assertEquals(newServiceClazz, webServiceDescriptor.getServiceClazz());
    }
    
    @Test
    public void testGetTargetNamespace() {
        Assert.assertEquals(targetNamespace, webServiceDescriptor.getTargetNamespace());
    }
    
    @Test
    public void testSetTargetNamespace() {
        String newTargetNamespace = "http://www.newexample.com";
        webServiceDescriptor.setTargetNamespace(newTargetNamespace);
        
        Assert.assertEquals(newTargetNamespace, webServiceDescriptor.getTargetNamespace());
    }
    
    @Test
    public void testGetTypesNamespace() {
        Assert.assertEquals(typesNamespace, webServiceDescriptor.getTypesNamespace());
    }
    
    @Test
    public void testSetTypesNamespace() {
        String newTypesNamespace = "http://www.newexample.com/types";
        webServiceDescriptor.setTypesNamespace(newTypesNamespace);
        
        Assert.assertEquals(newTypesNamespace, webServiceDescriptor.getTypesNamespace());
    }
    
    @Test
    public void testHashCode() {
        // Test when serviceClazz is null
        webServiceDescriptor.setServiceClazz(null);
        Assert.assertEquals(0, webServiceDescriptor.hashCode());
        
        // Test when serviceClazz is not null
        webServiceDescriptor.setServiceClazz(serviceClazz);
        int expectedHashCode = 31 * 1 + serviceClazz.hashCode();
        Assert.assertEquals(expectedHashCode, webServiceDescriptor.hashCode());
    }
    
    @Test
    public void testEquals() {
        // Test when comparing with same instance
        Assert.assertTrue(webServiceDescriptor.equals(webServiceDescriptor));
        
        // Test when comparing with null
        Assert.assertFalse(webServiceDescriptor.equals(null));
        
        // Test when comparing with different class
        Assert.assertFalse(webServiceDescriptor.equals(new Object()));
        
        // Test when comparing with different serviceClazz
        WebServiceDescriptor otherWithDifferentServiceClazz = new WebServiceDescriptor(String.class, targetNamespace, typesNamespace);
        Assert.assertFalse(webServiceDescriptor.equals(otherWithDifferentServiceClazz));
        
        // Test when comparing with same serviceClazz
        WebServiceDescriptor otherWithSameServiceClazz = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        Assert.assertTrue(webServiceDescriptor.equals(otherWithSameServiceClazz));
    }
}