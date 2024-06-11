import org.junit.Assert;
import org.junit.Test;

public class WebServiceDescriptorTest {

    @Test
    public void testGetServiceClazz() {
        // Test basic functionality
        Class expected = String.class;
        WebServiceDescriptor descriptor = new WebServiceDescriptor(expected, "target", "types");
        Class actual = descriptor.getServiceClazz();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSetServiceClazz() {
        // Test basic functionality
        Class expected = Integer.class;
        WebServiceDescriptor descriptor = new WebServiceDescriptor(String.class, "target", "types");
        descriptor.setServiceClazz(expected);
        Class actual = descriptor.getServiceClazz();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetTargetNamespace() {
        // Test basic functionality
        String expected = "target";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(String.class, expected, "types");
        String actual = descriptor.getTargetNamespace();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSetTargetNamespace() {
        // Test basic functionality
        String expected = "newTarget";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(String.class, "target", "types");
        descriptor.setTargetNamespace(expected);
        String actual = descriptor.getTargetNamespace();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetTypesNamespace() {
        // Test basic functionality
        String expected = "types";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(String.class, "target", expected);
        String actual = descriptor.getTypesNamespace();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSetTypesNamespace() {
        // Test basic functionality
        String expected = "newTypes";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(String.class, "target", "types");
        descriptor.setTypesNamespace(expected);
        String actual = descriptor.getTypesNamespace();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testHashCode() {
        // Test null serviceClazz
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(null, "target", "types");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(null, "target", "types");
        Assert.assertEquals(descriptor1.hashCode(), descriptor2.hashCode());

        // Test non-null serviceClazz
        Class clazz = String.class;
        WebServiceDescriptor descriptor3 = new WebServiceDescriptor(clazz, "target", "types");
        WebServiceDescriptor descriptor4 = new WebServiceDescriptor(clazz, "target", "types");
        Assert.assertEquals(descriptor3.hashCode(), descriptor4.hashCode());
    }

    @Test
    public void testEquals() {
        // Test equal objects
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(String.class, "target", "types");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(String.class, "target", "types");
        Assert.assertEquals(descriptor1, descriptor2);

        // Test unequal objects
        WebServiceDescriptor descriptor3 = new WebServiceDescriptor(String.class, "target1", "types");
        WebServiceDescriptor descriptor4 = new WebServiceDescriptor(String.class, "target2", "types");
        Assert.assertNotEquals(descriptor3, descriptor4);
    }

}