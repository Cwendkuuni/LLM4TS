import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WebServiceDescriptorTest {
  
  private WebServiceDescriptor descriptor;
  
  @Before
  public void setUp() {
    descriptor = new WebServiceDescriptor(WebServiceDescriptor.class, "http://target.namespace", "http://types.namespace");
  }
  
  @Test
  public void testGetServiceClazz() {
    assertEquals(WebServiceDescriptor.class, descriptor.getServiceClazz());
  }
  
  @Test
  public void testSetServiceClazz() {
    descriptor.setServiceClazz(String.class);
    assertEquals(String.class, descriptor.getServiceClazz());
  }
  
  @Test
  public void testGetTargetNamespace() {
    assertEquals("http://target.namespace", descriptor.getTargetNamespace());
  }
  
  @Test
  public void testSetTargetNamespace() {
    descriptor.setTargetNamespace("http://new.target.namespace");
    assertEquals("http://new.target.namespace", descriptor.getTargetNamespace());
  }
  
  @Test
  public void testGetTypesNamespace() {
    assertEquals("http://types.namespace", descriptor.getTypesNamespace());
  }
  
  @Test
  public void testSetTypesNamespace() {
    descriptor.setTypesNamespace("http://new.types.namespace");
    assertEquals("http://new.types.namespace", descriptor.getTypesNamespace());
  }
  
  @Test
  public void testHashCode() {
    WebServiceDescriptor descriptor2 = new WebServiceDescriptor(WebServiceDescriptor.class, "http://target.namespace", "http://types.namespace");
    assertEquals(descriptor.hashCode(), descriptor2.hashCode());
    
    descriptor2.setServiceClazz(String.class);
    assertNotEquals(descriptor.hashCode(), descriptor2.hashCode());
  }
  
  @Test
  public void testEquals() {
    WebServiceDescriptor descriptor2 = new WebServiceDescriptor(WebServiceDescriptor.class, "http://target.namespace", "http://types.namespace");
    WebServiceDescriptor descriptor3 = new WebServiceDescriptor(WebServiceDescriptor.class, "http://new.target.namespace", "http://new.types.namespace");
    
    assertTrue(descriptor.equals(descriptor2));
    assertFalse(descriptor.equals(descriptor3));
  }
  
}