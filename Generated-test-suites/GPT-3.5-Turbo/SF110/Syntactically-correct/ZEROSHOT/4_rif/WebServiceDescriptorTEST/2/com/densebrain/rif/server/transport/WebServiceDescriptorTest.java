import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class WebServiceDescriptorTest {

    private WebServiceDescriptor webServiceDescriptor;
    private Class testClass;
    private String testTargetNamespace;
    private String testTypesNamespace;

    @Before
    public void setUp() {
        testClass = WebServiceDescriptorTest.class;
        testTargetNamespace = "http://www.example.com/target";
        testTypesNamespace = "http://www.example.com/types";
        webServiceDescriptor = new WebServiceDescriptor(testClass, testTargetNamespace, testTypesNamespace);
    }

    @Test
    public void testGetServiceClazz() {
        Assert.assertEquals(testClass, webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testSetServiceClazz() {
        Class newTestClass = String.class;
        webServiceDescriptor.setServiceClazz(newTestClass);
        Assert.assertEquals(newTestClass, webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testGetTargetNamespace() {
        Assert.assertEquals(testTargetNamespace, webServiceDescriptor.getTargetNamespace());
    }

    @Test
    public void testSetTargetNamespace() {
        String newNamespace = "http://www.example.com/newTarget";
        webServiceDescriptor.setTargetNamespace(newNamespace);
        Assert.assertEquals(newNamespace, webServiceDescriptor.getTargetNamespace());
    }

    @Test
    public void testGetTypesNamespace() {
        Assert.assertEquals(testTypesNamespace, webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void testSetTypesNamespace() {
        String newNamespace = "http://www.example.com/newTypes";
        webServiceDescriptor.setTypesNamespace(newNamespace);
        Assert.assertEquals(newNamespace, webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void testHashCode() {
        Assert.assertEquals(31 * 1 + testClass.hashCode(), webServiceDescriptor.hashCode());
    }

    @Test
    public void testEquals_sameObject() {
        Assert.assertTrue(webServiceDescriptor.equals(webServiceDescriptor));
    }

    @Test
    public void testEquals_nullObject() {
        Assert.assertFalse(webServiceDescriptor.equals(null));
    }

    @Test
    public void testEquals_differentClass() {
        Object obj = new Object();
        Assert.assertFalse(webServiceDescriptor.equals(obj));
    }

    @Test
    public void testEquals_sameServiceClazz() {
        WebServiceDescriptor other = new WebServiceDescriptor(testClass, "", "");
        Assert.assertTrue(webServiceDescriptor.equals(other));
    }

    @Test
    public void testEquals_differentServiceClazz() {
        Class differentClass = String.class;
        WebServiceDescriptor other = new WebServiceDescriptor(differentClass, "", "");
        Assert.assertFalse(webServiceDescriptor.equals(other));
    }
}