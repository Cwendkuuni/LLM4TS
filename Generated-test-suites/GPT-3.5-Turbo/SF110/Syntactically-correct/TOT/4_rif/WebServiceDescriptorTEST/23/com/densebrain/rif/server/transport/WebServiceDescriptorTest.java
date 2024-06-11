import org.junit.Assert;
import org.junit.Test;

public class WebServiceDescriptorTest {

    @Test
    public void testGetServiceClazz() {
        // Typical use case
        Class expected = SomeService.class;
        WebServiceDescriptor descriptor = new WebServiceDescriptor(expected, "target", "types");
        Class actual = descriptor.getServiceClazz();
        Assert.assertEquals(expected, actual);

        // Edge case
        descriptor = new WebServiceDescriptor(null, "target", "types");
        actual = descriptor.getServiceClazz();
        Assert.assertNull(actual);
    }

    @Test
    public void testSetServiceClazz() {
        // Typical use case
        Class expected = SomeService.class;
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, "target", "types");
        descriptor.setServiceClazz(expected);
        Class actual = descriptor.getServiceClazz();
        Assert.assertEquals(expected, actual);

        // Edge case
        descriptor.setServiceClazz(null);
        actual = descriptor.getServiceClazz();
        Assert.assertNull(actual);
    }

    @Test
    public void testGetTargetNamespace() {
        // Typical use case
        String expected = "targetNamespace";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(SomeService.class, expected, "types");
        String actual = descriptor.getTargetNamespace();
        Assert.assertEquals(expected, actual);

        // Edge case
        descriptor = new WebServiceDescriptor(SomeService.class, null, "types");
        actual = descriptor.getTargetNamespace();
        Assert.assertNull(actual);
    }

    @Test
    public void testSetTargetNamespace() {
        // Typical use case
        String expected = "targetNamespace";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(SomeService.class, null, "types");
        descriptor.setTargetNamespace(expected);
        String actual = descriptor.getTargetNamespace();
        Assert.assertEquals(expected, actual);

        // Edge case
        descriptor.setTargetNamespace(null);
        actual = descriptor.getTargetNamespace();
        Assert.assertNull(actual);
    }

    @Test
    public void testGetTypesNamespace() {
        // Typical use case
        String expected = "typesNamespace";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(SomeService.class, "target", expected);
        String actual = descriptor.getTypesNamespace();
        Assert.assertEquals(expected, actual);

        // Edge case
        descriptor = new WebServiceDescriptor(SomeService.class, "target", null);
        actual = descriptor.getTypesNamespace();
        Assert.assertNull(actual);
    }

    @Test
    public void testSetTypesNamespace() {
        // Typical use case
        String expected = "typesNamespace";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(SomeService.class, "target", null);
        descriptor.setTypesNamespace(expected);
        String actual = descriptor.getTypesNamespace();
        Assert.assertEquals(expected, actual);

        // Edge case
        descriptor.setTypesNamespace(null);
        actual = descriptor.getTypesNamespace();
        Assert.assertNull(actual);
    }

    @Test
    public void testHashCode() {
        // Typical use case
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(SomeService.class, "target", "types");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(SomeService.class, "target", "types");
        Assert.assertEquals(descriptor1.hashCode(), descriptor2.hashCode());

        // Inequality case
        WebServiceDescriptor descriptor3 = new WebServiceDescriptor(OtherService.class, "target", "types");
        Assert.assertNotEquals(descriptor1.hashCode(), descriptor3.hashCode());
    }

    @Test
    public void testEquals() {
        // Typical use case
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(SomeService.class, "target", "types");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(SomeService.class, "target", "types");
        Assert.assertEquals(descriptor1, descriptor2);

        // Inequality case
        WebServiceDescriptor descriptor3 = new WebServiceDescriptor(OtherService.class, "target", "types");
        Assert.assertNotEquals(descriptor1, descriptor3);

        // Null case
        Assert.assertNotEquals(descriptor1, null);

        // Same instance case
        Assert.assertEquals(descriptor1, descriptor1);
    }
}