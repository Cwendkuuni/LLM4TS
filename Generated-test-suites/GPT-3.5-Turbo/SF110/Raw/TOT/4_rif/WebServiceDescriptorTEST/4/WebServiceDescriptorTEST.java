import org.junit.Assert;
import org.junit.Test;

public class WebServiceDescriptorTest {

    @Test
    public void testConstructor() {
        Class serviceClazz = MyClass.class;
        String targetNamespace = "http://example.com/target";
        String typesNamespace = "http://example.com/types";
        
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        
        Assert.assertEquals(serviceClazz, descriptor.getServiceClazz());
        Assert.assertEquals(targetNamespace, descriptor.getTargetNamespace());
        Assert.assertEquals(typesNamespace, descriptor.getTypesNamespace());
    }

    @Test
    public void testSetServiceClazz() {
        WebServiceDescriptor descriptor = new WebServiceDescriptor();
        Class serviceClazz = MyClass.class;
        
        descriptor.setServiceClazz(serviceClazz);
        
        Assert.assertEquals(serviceClazz, descriptor.getServiceClazz());
    }
    
    @Test
    public void testSetTargetNamespace() {
        WebServiceDescriptor descriptor = new WebServiceDescriptor();
        String targetNamespace = "http://example.com/target";
        
        descriptor.setTargetNamespace(targetNamespace);
        
        Assert.assertEquals(targetNamespace, descriptor.getTargetNamespace());
    }
    
    @Test
    public void testSetTypesNamespace() {
        WebServiceDescriptor descriptor = new WebServiceDescriptor();
        String typesNamespace = "http://example.com/types";
        
        descriptor.setTypesNamespace(typesNamespace);
        
        Assert.assertEquals(typesNamespace, descriptor.getTypesNamespace());
    }
    
    @Test
    public void testHashCode() {
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor();
        descriptor1.setServiceClazz(MyClass.class);
        
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor();
        descriptor2.setServiceClazz(MyClass.class);
        
        Assert.assertEquals(descriptor1.hashCode(), descriptor2.hashCode());
    }
    
    @Test
    public void testEquals() {
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor();
        descriptor1.setServiceClazz(MyClass.class);
        
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor();
        descriptor2.setServiceClazz(MyClass.class);
        
        WebServiceDescriptor descriptor3 = new WebServiceDescriptor();
        descriptor3.setServiceClazz(AnotherClass.class);
        
        Assert.assertEquals(descriptor1, descriptor2);
        Assert.assertNotEquals(descriptor1, descriptor3);
    }
}