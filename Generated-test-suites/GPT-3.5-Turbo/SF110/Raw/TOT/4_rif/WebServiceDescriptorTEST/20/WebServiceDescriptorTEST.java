import org.junit.Assert;
import org.junit.Test;

public class WebServiceDescriptorTest {

    @Test
    public void testGetServiceClazz() {
        Class serviceClazz = String.class;
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, "namespace", "typesNamespace");
        Assert.assertEquals(serviceClazz, descriptor.getServiceClazz());
    }

    @Test
    public void testSetServiceClazz() {
        Class serviceClazz = Integer.class;
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, "namespace", "typesNamespace");
        descriptor.setServiceClazz(serviceClazz);
        Assert.assertEquals(serviceClazz, descriptor.getServiceClazz());
    }

    @Test
    public void testGetTargetNamespace() {
        String targetNamespace = "targetNamespace";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, targetNamespace, "typesNamespace");
        Assert.assertEquals(targetNamespace, descriptor.getTargetNamespace());
    }

    @Test
    public void testSetTargetNamespace() {
        String targetNamespace = "targetNamespace";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, null, "typesNamespace");
        descriptor.setTargetNamespace(targetNamespace);
        Assert.assertEquals(targetNamespace, descriptor.getTargetNamespace());
    }

    @Test
    public void testGetTypesNamespace() {
        String typesNamespace = "typesNamespace";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, "namespace", typesNamespace);
        Assert.assertEquals(typesNamespace, descriptor.getTypesNamespace());
    }

    @Test
    public void testSetTypesNamespace() {
        String typesNamespace = "typesNamespace";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, "namespace", null);
        descriptor.setTypesNamespace(typesNamespace);
        Assert.assertEquals(typesNamespace, descriptor.getTypesNamespace());
    }

    @Test
    public void testHashCode() {
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(null, "namespace1", "typesNamespace1");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(null, "namespace2", "typesNamespace2");
        WebServiceDescriptor descriptor3 = new WebServiceDescriptor(null, "namespace1", "typesNamespace3");
        Assert.assertEquals(descriptor1.hashCode(), descriptor1.hashCode());
        Assert.assertNotEquals(descriptor1.hashCode(), descriptor2.hashCode());
        Assert.assertNotEquals(descriptor1.hashCode(), descriptor3.hashCode());
        Assert.assertNotEquals(descriptor2.hashCode(), descriptor3.hashCode());
    }

    @Test
    public void testEquals() {
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(null, "namespace1", "typesNamespace1");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(null, "namespace2", "typesNamespace2");
        WebServiceDescriptor descriptor3 = new WebServiceDescriptor(null, "namespace1", "typesNamespace3");
        WebServiceDescriptor descriptor4 = new WebServiceDescriptor(null, "namespace1", "typesNamespace1");
        Assert.assertTrue(descriptor1.equals(descriptor1));
        Assert.assertFalse(descriptor1.equals(null));
        Assert.assertFalse(descriptor1.equals(new Object()));
        Assert.assertFalse(descriptor1.equals(descriptor2));
        Assert.assertFalse(descriptor1.equals(descriptor3));
        Assert.assertTrue(descriptor1.equals(descriptor4));
    }
}