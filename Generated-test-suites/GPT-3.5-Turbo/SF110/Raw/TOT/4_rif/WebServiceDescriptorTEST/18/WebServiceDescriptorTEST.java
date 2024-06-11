import org.junit.Assert;
import org.junit.Test;

public class WebServiceDescriptorTest {

    @Test
    public void testConstructor() {
        // Typical test case
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(MyService.class, "http://target", "http://types");
        
        Assert.assertEquals(MyService.class, webServiceDescriptor.getServiceClazz());
        Assert.assertEquals("http://target", webServiceDescriptor.getTargetNamespace());
        Assert.assertEquals("http://types", webServiceDescriptor.getTypesNamespace());
    }
    
    @Test
    public void testSetServiceClazz() {
        // Typical test case
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(MyService.class, "http://target", "http://types");
        Class newServiceClazz = MyOtherService.class;
        
        webServiceDescriptor.setServiceClazz(newServiceClazz);
        
        Assert.assertEquals(newServiceClazz, webServiceDescriptor.getServiceClazz());
    }
    
    @Test
    public void testSetTargetNamespace() {
        // Typical test case
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(MyService.class, "http://target", "http://types");
        String newTargetNamespace = "http://newtarget";
        
        webServiceDescriptor.setTargetNamespace(newTargetNamespace);
        
        Assert.assertEquals(newTargetNamespace, webServiceDescriptor.getTargetNamespace());
    }
    
    @Test
    public void testSetTypesNamespace() {
        // Typical test case
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(MyService.class, "http://target", "http://types");
        String newTypesNamespace = "http://newtypes";
        
        webServiceDescriptor.setTypesNamespace(newTypesNamespace);
        
        Assert.assertEquals(newTypesNamespace, webServiceDescriptor.getTypesNamespace());
    }
    
    @Test
    public void testHashCode() {
        // Typical test case
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(MyService.class, "http://target", "http://types");
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(MyService.class, "http://target", "http://types");
        
        Assert.assertEquals(webServiceDescriptor1.hashCode(), webServiceDescriptor2.hashCode());
    }
    
    @Test
    public void testEquals() {
        // Typical test case
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(MyService.class, "http://target", "http://types");
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(MyService.class, "http://target", "http://types");
        
        Assert.assertTrue(webServiceDescriptor1.equals(webServiceDescriptor2));
    }
    
    @Test
    public void testEquals_NullObject() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(MyService.class, "http://target", "http://types");
        
        Assert.assertFalse(webServiceDescriptor.equals(null));
    }
    
    @Test
    public void testEquals_DifferentClass() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(MyService.class, "http://target", "http://types");
        
        Assert.assertFalse(webServiceDescriptor.equals("randomString"));
    }
    
    @Test
    public void testEquals_DifferentServiceClazz() {
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(MyService.class, "http://target", "http://types");
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(MyOtherService.class, "http://target", "http://types");
        
        Assert.assertFalse(webServiceDescriptor1.equals(webServiceDescriptor2));
    }
    
    @Test
    public void testEquals_NullServiceClazz() {
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(null, "http://target", "http://types");
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(null, "http://target", "http://types");
        
        Assert.assertTrue(webServiceDescriptor1.equals(webServiceDescriptor2));
    }
}