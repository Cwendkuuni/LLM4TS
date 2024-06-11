import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WebServiceDescriptorTest {

    private WebServiceDescriptor descriptor;
    private Class serviceClazz;
    private String targetNamespace;
    private String typesNamespace;

    @Before
    public void setUp() {
        serviceClazz = TestServiceClass.class;
        targetNamespace = "http://www.example.com/target";
        typesNamespace = "http://www.example.com/types";
        descriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
    }

    @Test
    public void getServiceClazz_shouldReturnServiceClazz() {
        Class expected = TestServiceClass.class;
        Class actual = descriptor.getServiceClazz();
        assertEquals(expected, actual);
    }

    @Test
    public void setServiceClazz_shouldSetServiceClazz() {
        Class newServiceClazz = TestServiceClass2.class;
        descriptor.setServiceClazz(newServiceClazz);
        Class expected = TestServiceClass2.class;
        Class actual = descriptor.getServiceClazz();
        assertEquals(expected, actual);
    }

    @Test
    public void getTargetNamespace_shouldReturnTargetNamespace() {
        String expected = "http://www.example.com/target";
        String actual = descriptor.getTargetNamespace();
        assertEquals(expected, actual);
    }

    @Test
    public void setTargetNamespace_shouldSetTargetNamespace() {
        String newTargetNamespace = "http://www.example.com/new_target";
        descriptor.setTargetNamespace(newTargetNamespace);
        String expected = "http://www.example.com/new_target";
        String actual = descriptor.getTargetNamespace();
        assertEquals(expected, actual);
    }

    @Test
    public void getTypesNamespace_shouldReturnTypesNamespace() {
        String expected = "http://www.example.com/types";
        String actual = descriptor.getTypesNamespace();
        assertEquals(expected, actual);
    }

    @Test
    public void setTypesNamespace_shouldSetTypesNamespace() {
        String newTypesNamespace = "http://www.example.com/new_types";
        descriptor.setTypesNamespace(newTypesNamespace);
        String expected = "http://www.example.com/new_types";
        String actual = descriptor.getTypesNamespace();
        assertEquals(expected, actual);
    }

    @Test
    public void hashCode_shouldReturnHashCode() {
        int expected = serviceClazz.hashCode();
        int actual = descriptor.hashCode();
        assertEquals(expected, actual);
    }

    @Test
    public void equals_withSameInstance_shouldReturnTrue() {
        boolean result = descriptor.equals(descriptor);
        assertTrue(result);
    }

    @Test
    public void equals_withNull_shouldReturnFalse() {
        boolean result = descriptor.equals(null);
        assertFalse(result);
    }

    @Test
    public void equals_withDifferentClass_shouldReturnFalse() {
        boolean result = descriptor.equals(new Object());
        assertFalse(result);
    }

    @Test
    public void equals_withEqualServiceClazz_shouldReturnTrue() {
        WebServiceDescriptor other = new WebServiceDescriptor(serviceClazz, "http://www.example.com/other_target", "http://www.example.com/other_types");
        boolean result = descriptor.equals(other);
        assertTrue(result);
    }

    @Test
    public void equals_withNonEqualServiceClazz_shouldReturnFalse() {
        WebServiceDescriptor other = new WebServiceDescriptor(TestServiceClass2.class, targetNamespace, typesNamespace);
        boolean result = descriptor.equals(other);
        assertFalse(result);
    }
}