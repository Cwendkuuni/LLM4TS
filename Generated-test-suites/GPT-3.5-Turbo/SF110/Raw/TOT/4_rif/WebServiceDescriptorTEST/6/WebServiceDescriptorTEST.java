import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class WebServiceDescriptorTest {

    private WebServiceDescriptor webServiceDescriptor;

    @Before
    public void setUp() {
        webServiceDescriptor = new WebServiceDescriptor(null, null, null);
    }

    @Test
    public void getServiceClazz_ValidInput_ReturnsServiceClazz() {
        Class expected = String.class;
        webServiceDescriptor.setServiceClazz(expected);

        Class actual = webServiceDescriptor.getServiceClazz();

        assertEquals(expected, actual);
    }

    @Test
    public void setServiceClazz_ValidInput_SetsServiceClazz() {
        Class expected = Integer.class;

        webServiceDescriptor.setServiceClazz(expected);

        assertEquals(expected, webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void getTargetNamespace_ValidInput_ReturnsTargetNamespace() {
        String expected = "http://example.com";

        webServiceDescriptor.setTargetNamespace(expected);

        assertEquals(expected, webServiceDescriptor.getTargetNamespace());
    }

    @Test
    public void setTargetNamespace_ValidInput_SetsTargetNamespace() {
        String expected = "http://example.org";

        webServiceDescriptor.setTargetNamespace(expected);

        assertEquals(expected, webServiceDescriptor.getTargetNamespace());
    }

    @Test
    public void getTypesNamespace_ValidInput_ReturnsTypesNamespace() {
        String expected = "http://example.com/types";

        webServiceDescriptor.setTypesNamespace(expected);

        assertEquals(expected, webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void setTypesNamespace_ValidInput_SetsTypesNamespace() {
        String expected = "http://example.org/types";

        webServiceDescriptor.setTypesNamespace(expected);

        assertEquals(expected, webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void hashCode_SameServiceClazz_ReturnsSameHashCode() {
        Class serviceClazz = Integer.class;
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz, null, null);
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz, null, null);

        assertEquals(webServiceDescriptor1.hashCode(), webServiceDescriptor2.hashCode());
    }

    @Test
    public void equals_SameWebServiceDescriptorObject_ReturnsTrue() {
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(null, null, null);

        assertEquals(webServiceDescriptor1, webServiceDescriptor1);
    }

    @Test
    public void equals_SameServiceClazz_ReturnsTrue() {
        Class serviceClazz = Integer.class;
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz, null, null);
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz, null, null);

        assertEquals(webServiceDescriptor1, webServiceDescriptor2);
    }

    @Test
    public void equals_DifferentServiceClazz_ReturnsFalse() {
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(Integer.class, null, null);
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(String.class, null, null);

        assertEquals(false, webServiceDescriptor1.equals(webServiceDescriptor2));
    }
}