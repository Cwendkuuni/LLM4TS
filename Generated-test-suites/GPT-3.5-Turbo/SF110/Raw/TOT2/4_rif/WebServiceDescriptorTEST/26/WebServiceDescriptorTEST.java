import org.junit.Test;
import static org.junit.Assert.*;

public class WebServiceDescriptorTest {

    @Test
    public void testConstructor() {
        Class serviceClazz = MyClass.class;
        String targetNamespace = "http://www.example.com/target";
        String typesNamespace = "http://www.example.com/types";

        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

        assertEquals(serviceClazz, descriptor.getServiceClazz());
        assertEquals(targetNamespace, descriptor.getTargetNamespace());
        assertEquals(typesNamespace, descriptor.getTypesNamespace());
    }

    @Test
    public void testSetServiceClazz() {
        Class serviceClazz = MyClass.class;
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, "http://www.example.com/target", "http://www.example.com/types");

        Class newServiceClazz = NewClass.class;
        descriptor.setServiceClazz(newServiceClazz);

        assertEquals(newServiceClazz, descriptor.getServiceClazz());
    }

    @Test
    public void testSetTargetNamespace() {
        String targetNamespace = "http://www.example.com/target";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(MyClass.class, targetNamespace, "http://www.example.com/types");

        String newTargetNamespace = "http://www.newexample.com/target";
        descriptor.setTargetNamespace(newTargetNamespace);

        assertEquals(newTargetNamespace, descriptor.getTargetNamespace());
    }

    @Test
    public void testSetTypesNamespace() {
        String typesNamespace = "http://www.example.com/types";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(MyClass.class, "http://www.example.com/target", typesNamespace);

        String newTypesNamespace = "http://www.newexample.com/types";
        descriptor.setTypesNamespace(newTypesNamespace);

        assertEquals(newTypesNamespace, descriptor.getTypesNamespace());
    }

    @Test
    public void testHashCode() {
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(MyClass.class, "http://www.example.com/target", "http://www.example.com/types");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(MyClass.class, "http://www.example.com/target", "http://www.example.com/types");

        assertEquals(descriptor1.hashCode(), descriptor2.hashCode());
    }

    @Test
    public void testEquals() {
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(MyClass.class, "http://www.example.com/target", "http://www.example.com/types");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(MyClass.class, "http://www.example.com/target", "http://www.example.com/types");

        assertTrue(descriptor1.equals(descriptor2));
    }

}