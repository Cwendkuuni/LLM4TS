import static org.junit.Assert.*;
import org.junit.Test;

public class WebServiceDescriptorTest {

    WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(TestService.class, "http://test.com/ws", "http://test.com/types");

    @Test
    public void testGetServiceClazz() {
        assertEquals(TestService.class, webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testSetServiceClazz() {
        webServiceDescriptor.setServiceClazz(AnotherTestService.class);
        assertEquals(AnotherTestService.class, webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testGetTargetNamespace() {
        assertEquals("http://test.com/ws", webServiceDescriptor.getTargetNamespace());
    }

    @Test
    public void testSetTargetNamespace() {
        webServiceDescriptor.setTargetNamespace("http://newtest.com/ws");
        assertEquals("http://newtest.com/ws", webServiceDescriptor.getTargetNamespace());
    }

    @Test
    public void testGetTypesNamespace() {
        assertEquals("http://test.com/types", webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void testSetTypesNamespace() {
        webServiceDescriptor.setTypesNamespace("http://newtest.com/types");
        assertEquals("http://newtest.com/types", webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void testHashCode() {
        assertEquals(31, webServiceDescriptor.hashCode());
    }

    @Test
    public void testEquals() {
        WebServiceDescriptor other = new WebServiceDescriptor(TestService.class, "http://test.com/ws", "http://test.com/types");
        assertTrue(webServiceDescriptor.equals(other));
    }

    static class TestService {}
    static class AnotherTestService {}
}