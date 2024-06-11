
import org.junit.Assert;
import org.junit.Test;

public class WebServiceDescriptorTest {

    @Test
    public void testGetServiceClazz() {
        Class expectedClass = SomeTestClass.class;
        WebServiceDescriptor descriptor = new WebServiceDescriptor(expectedClass, "targetNamespace", "typesNamespace");
        Class result = descriptor.getServiceClazz();
        Assert.assertEquals(expectedClass, result);
    }

    @Test
    public void testSetServiceClazz() {
        Class expectedClass = SomeTestClass.class;
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, "targetNamespace", "typesNamespace");
        descriptor.setServiceClazz(expectedClass);
        Class result = descriptor.getServiceClazz();
        Assert.assertEquals(expectedClass, result);
    }

    @Test
    public void testGetTargetNamespace() {
        String expectedNamespace = "targetNamespace";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(SomeTestClass.class, expectedNamespace, "typesNamespace");
        String result = descriptor.getTargetNamespace();
        Assert.assertEquals(expectedNamespace, result);
    }

    @Test
    public void testSetTargetNamespace() {
        String expectedNamespace = "targetNamespace";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(SomeTestClass.class, null, "typesNamespace");
        descriptor.setTargetNamespace(expectedNamespace);
        String result = descriptor.getTargetNamespace();
        Assert.assertEquals(expectedNamespace, result);
    }

    @Test
    public void testGetTypesNamespace() {
        String expectedNamespace = "typesNamespace";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(SomeTestClass.class, "targetNamespace", expectedNamespace);
        String result = descriptor.getTypesNamespace();
        Assert.assertEquals(expectedNamespace, result);
    }

    @Test
    public void testSetTypesNamespace() {
        String expectedNamespace = "typesNamespace";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(SomeTestClass.class, "targetNamespace", null);
        descriptor.setTypesNamespace(expectedNamespace);
        String result = descriptor.getTypesNamespace();
        Assert.assertEquals(expectedNamespace, result);
    }

    @Test
    public void testHashCode() {
        Class serviceClazz = SomeTestClass.class;
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz, "targetNamespace", "typesNamespace");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz, "targetNamespace", "typesNamespace");
        int hashCode1 = descriptor1.hashCode();
        int hashCode2 = descriptor2.hashCode();
        Assert.assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testEquals() {
        Class serviceClazz = SomeTestClass.class;
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz, "targetNamespace", "typesNamespace");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz, "targetNamespace", "typesNamespace");
        boolean equals = descriptor1.equals(descriptor2);
        Assert.assertTrue(equals);
    }
}

class SomeTestClass {
}
