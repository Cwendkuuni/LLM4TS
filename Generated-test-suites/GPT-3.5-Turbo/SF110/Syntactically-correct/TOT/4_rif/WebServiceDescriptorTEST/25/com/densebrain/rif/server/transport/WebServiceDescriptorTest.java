import org.junit.Assert;
import org.junit.Test;

public class WebServiceDescriptorTest {

    @Test
    public void testGetServiceClazz() {
        // Test typical use case
        WebServiceDescriptor descriptor = new WebServiceDescriptor(TestService.class, "http://example.com", "http://types.example.com");
        Assert.assertEquals(TestService.class, descriptor.getServiceClazz());
        
        // Test null serviceClazz
        descriptor = new WebServiceDescriptor(null, "http://example.com", "http://types.example.com");
        Assert.assertNull(descriptor.getServiceClazz());
    }
    
    @Test
    public void testSetServiceClazz() {
        // Test typical use case
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, "http://example.com", "http://types.example.com");
        descriptor.setServiceClazz(TestService.class);
        Assert.assertEquals(TestService.class, descriptor.getServiceClazz());
        
        // Test null serviceClazz
        descriptor.setServiceClazz(null);
        Assert.assertNull(descriptor.getServiceClazz());
    }
    
    @Test
    public void testGetTargetNamespace() {
        // Test typical use case
        WebServiceDescriptor descriptor = new WebServiceDescriptor(TestService.class, "http://example.com", "http://types.example.com");
        Assert.assertEquals("http://example.com", descriptor.getTargetNamespace());
        
        // Test empty targetNamespace
        descriptor = new WebServiceDescriptor(TestService.class, "", "http://types.example.com");
        Assert.assertEquals("", descriptor.getTargetNamespace());
        
        // Test null targetNamespace
        descriptor = new WebServiceDescriptor(TestService.class, null, "http://types.example.com");
        Assert.assertNull(descriptor.getTargetNamespace());
    }
    
    @Test
    public void testSetTargetNamespace() {
        // Test typical use case
        WebServiceDescriptor descriptor = new WebServiceDescriptor(TestService.class, null, "http://types.example.com");
        descriptor.setTargetNamespace("http://example.com");
        Assert.assertEquals("http://example.com", descriptor.getTargetNamespace());
        
        // Test empty targetNamespace
        descriptor.setTargetNamespace("");
        Assert.assertEquals("", descriptor.getTargetNamespace());
        
        // Test null targetNamespace
        descriptor.setTargetNamespace(null);
        Assert.assertNull(descriptor.getTargetNamespace());
    }
    
    @Test
    public void testGetTypesNamespace() {
        // Test typical use case
        WebServiceDescriptor descriptor = new WebServiceDescriptor(TestService.class, "http://example.com", "http://types.example.com");
        Assert.assertEquals("http://types.example.com", descriptor.getTypesNamespace());
        
        // Test empty typesNamespace
        descriptor = new WebServiceDescriptor(TestService.class, "http://example.com", "");
        Assert.assertEquals("", descriptor.getTypesNamespace());
        
        // Test null typesNamespace
        descriptor = new WebServiceDescriptor(TestService.class, "http://example.com", null);
        Assert.assertNull(descriptor.getTypesNamespace());
    }
    
    @Test
    public void testSetTypesNamespace() {
        // Test typical use case
        WebServiceDescriptor descriptor = new WebServiceDescriptor(TestService.class, "http://example.com", null);
        descriptor.setTypesNamespace("http://types.example.com");
        Assert.assertEquals("http://types.example.com", descriptor.getTypesNamespace());
        
        // Test empty typesNamespace
        descriptor.setTypesNamespace("");
        Assert.assertEquals("", descriptor.getTypesNamespace());
        
        // Test null typesNamespace
        descriptor.setTypesNamespace(null);
        Assert.assertNull(descriptor.getTypesNamespace());
    }
    
    @Test
    public void testHashCode() {
        // Test typical use case
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(TestService.class, "http://example.com", "http://types.example.com");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(TestService.class, "http://example.com", "http://types.example.com");
        Assert.assertEquals(descriptor1.hashCode(), descriptor2.hashCode());
        
        // Test different serviceClazz
        descriptor2 = new WebServiceDescriptor(OtherService.class, "http://example.com", "http://types.example.com");
        Assert.assertNotEquals(descriptor1.hashCode(), descriptor2.hashCode());
        
        // Test null serviceClazz
        descriptor1 = new WebServiceDescriptor(null, "http://example.com", "http://types.example.com");
        Assert.assertEquals(0, descriptor1.hashCode());
    }
    
    @Test
    public void testEquals() {
        // Test typical use case
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(TestService.class, "http://example.com", "http://types.example.com");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(TestService.class, "http://example.com", "http://types.example.com");
        Assert.assertTrue(descriptor1.equals(descriptor2));
        Assert.assertTrue(descriptor2.equals(descriptor1));
        
        // Test different serviceClazz
        descriptor2 = new WebServiceDescriptor(OtherService.class, "http://example.com", "http://types.example.com");
        Assert.assertFalse(descriptor1.equals(descriptor2));
        Assert.assertFalse(descriptor2.equals(descriptor1));
        
        // Test null serviceClazz
        descriptor1 = new WebServiceDescriptor(null, "http://example.com", "http://types.example.com");
        descriptor2 = new WebServiceDescriptor(null, "http://example.com", "http://types.example.com");
        Assert.assertTrue(descriptor1.equals(descriptor2));
        Assert.assertTrue(descriptor2.equals(descriptor1));
    }
    
    private static class TestService {
        // Test class
    }
    
    private static class OtherService {
        // Test class
    }
}