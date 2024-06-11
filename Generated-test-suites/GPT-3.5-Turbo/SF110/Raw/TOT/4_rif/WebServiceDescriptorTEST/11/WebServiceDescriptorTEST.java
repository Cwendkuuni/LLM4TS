import org.junit.Assert;
import org.junit.Test;

public class WebServiceDescriptorTest {

    @Test
    public void testGetServiceClazz() {
        Class expected = String.class;
        WebServiceDescriptor descriptor = new WebServiceDescriptor(expected, "", "");
        
        Class actual = descriptor.getServiceClazz();
        
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSetServiceClazz() {
        Class expected = Integer.class;
        WebServiceDescriptor descriptor = new WebServiceDescriptor(String.class, "", "");
        
        descriptor.setServiceClazz(expected);
        
        Assert.assertEquals(expected, descriptor.getServiceClazz());
    }

    @Test
    public void testGetTargetNamespace() {
        String expected = "http://www.example.com/namespace";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(String.class, expected, "");
        
        String actual = descriptor.getTargetNamespace();
        
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSetTargetNamespace() {
        String expected = "http://www.example.com/namespace";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(String.class, "", "");
        
        descriptor.setTargetNamespace(expected);
        
        Assert.assertEquals(expected, descriptor.getTargetNamespace());
    }

    @Test
    public void testGetTypesNamespace() {
        String expected = "http://www.example.com/types";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(String.class, "", expected);
        
        String actual = descriptor.getTypesNamespace();
        
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSetTypesNamespace() {
        String expected = "http://www.example.com/types";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(String.class, "", "");
        
        descriptor.setTypesNamespace(expected);
        
        Assert.assertEquals(expected, descriptor.getTypesNamespace());
    }

    @Test
    public void testHashCode() {
        Class serviceClazz = String.class;
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz, "", "");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz, "", "");
        
        int hashCode1 = descriptor1.hashCode();
        int hashCode2 = descriptor2.hashCode();
        
        Assert.assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testEquals_sameObject() {
        Class serviceClazz = String.class;
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz, "", "");
        
        boolean result = descriptor1.equals(descriptor1);
        
        Assert.assertTrue(result);
    }

    @Test
    public void testEquals_nullObject() {
        Class serviceClazz = String.class;
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz, "", "");
        
        boolean result = descriptor1.equals(null);
        
        Assert.assertFalse(result);
    }

    @Test
    public void testEquals_differentClass() {
        Class serviceClazz = String.class;
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz, "", "");
        
        boolean result = descriptor1.equals("not a WebServiceDescriptor object");
        
        Assert.assertFalse(result);
    }

    @Test
    public void testEquals_sameServiceClazz() {
        Class serviceClazz = String.class;
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz, "", "");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz, "", "");
        
        boolean result = descriptor1.equals(descriptor2);
        
        Assert.assertTrue(result);
    }

    @Test
    public void testEquals_differentServiceClazz() {
        Class serviceClazz1 = String.class;
        Class serviceClazz2 = Integer.class;
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz1, "", "");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz2, "", "");
        
        boolean result = descriptor1.equals(descriptor2);
        
        Assert.assertFalse(result);
    }

}