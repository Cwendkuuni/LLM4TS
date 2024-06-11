import org.junit.Assert;
import org.junit.Test;

public class WebServiceDescriptorTest {

    @Test
    public void testConstructorAndGetters() {
        Class serviceClazz = String.class;
        String targetNamespace = "http://example.com/target";
        String typesNamespace = "http://example.com/types";

        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

        Assert.assertEquals(serviceClazz, descriptor.getServiceClazz());
        Assert.assertEquals(targetNamespace, descriptor.getTargetNamespace());
        Assert.assertEquals(typesNamespace, descriptor.getTypesNamespace());
    }

    @Test
    public void testSetters() {
        Class serviceClazz = String.class;
        String targetNamespace = "http://example.com/target";
        String typesNamespace = "http://example.com/types";

        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

        Class newServiceClazz = Integer.class;
        String newTargetNamespace = "http://example.com/new-target";
        String newTypesNamespace = "http://example.com/new-types";

        descriptor.setServiceClazz(newServiceClazz);
        descriptor.setTargetNamespace(newTargetNamespace);
        descriptor.setTypesNamespace(newTypesNamespace);

        Assert.assertEquals(newServiceClazz, descriptor.getServiceClazz());
        Assert.assertEquals(newTargetNamespace, descriptor.getTargetNamespace());
        Assert.assertEquals(newTypesNamespace, descriptor.getTypesNamespace());
    }

    @Test
    public void testHashCode() {
        Class serviceClazz = String.class;
        String targetNamespace = "http://example.com/target";
        String typesNamespace = "http://example.com/types";

        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

        Assert.assertEquals(descriptor1.hashCode(), descriptor2.hashCode());
    }

    @Test
    public void testEquals() {
        Class serviceClazz = String.class;
        String targetNamespace = "http://example.com/target";
        String typesNamespace = "http://example.com/types";

        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        WebServiceDescriptor descriptor3 = new WebServiceDescriptor(Integer.class, targetNamespace, typesNamespace);

        Assert.assertTrue(descriptor1.equals(descriptor2));
        Assert.assertFalse(descriptor1.equals(descriptor3));
    }
}

