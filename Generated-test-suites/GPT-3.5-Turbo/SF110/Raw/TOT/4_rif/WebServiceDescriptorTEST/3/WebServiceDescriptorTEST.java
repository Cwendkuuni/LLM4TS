import static org.junit.Assert.*;
import org.junit.Test;

public class WebServiceDescriptorTest {
  
  @Test
  public void testConstructorAndGetters() {
    Class serviceClazz = String.class;
    String targetNamespace = "http://www.example.com";
    String typesNamespace = "http://www.example.com/types";
    
    WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
    
    assertEquals(serviceClazz, webServiceDescriptor.getServiceClazz());
    assertEquals(targetNamespace, webServiceDescriptor.getTargetNamespace());
    assertEquals(typesNamespace, webServiceDescriptor.getTypesNamespace());
  }
  
  @Test
  public void testSetterAndGetters() {
    Class serviceClazz = Integer.class;
    String targetNamespace = "http://www.example.com/target";
    String typesNamespace = "http://www.example.com/types";
    
    WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor();
    
    webServiceDescriptor.setServiceClazz(serviceClazz);
    webServiceDescriptor.setTargetNamespace(targetNamespace);
    webServiceDescriptor.setTypesNamespace(typesNamespace);
    
    assertEquals(serviceClazz, webServiceDescriptor.getServiceClazz());
    assertEquals(targetNamespace, webServiceDescriptor.getTargetNamespace());
    assertEquals(typesNamespace, webServiceDescriptor.getTypesNamespace());
  }
  
  @Test
  public void testHashCode() {
    Class serviceClazz = Double.class;
    String targetNamespace = "http://www.example.com";
    String typesNamespace = "http://www.example.com/types";
    
    WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
    WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
    
    assertEquals(webServiceDescriptor1.hashCode(), webServiceDescriptor2.hashCode());
  }
  
  @Test
  public void testEquals_sameObject() {
    WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor();
    
    assertEquals(webServiceDescriptor, webServiceDescriptor);
  }
  
  @Test
  public void testEquals_differentClass() {
    WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor();
    Object object = new Object();
    
    assertNotEquals(webServiceDescriptor, object);
  }
  
  @Test
  public void testEquals_null() {
    WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor();
    
    assertNotEquals(webServiceDescriptor, null);
  }
  
  @Test
  public void testEquals_differentServiceClazz() {
    Class serviceClazz1 = String.class;
    Class serviceClazz2 = Integer.class;
    String targetNamespace = "http://www.example.com";
    String typesNamespace = "http://www.example.com/types";
    
    WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz1, targetNamespace, typesNamespace);
    WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz2, targetNamespace, typesNamespace);
    
    assertNotEquals(webServiceDescriptor1, webServiceDescriptor2);
  }
  
  @Test
  public void testEquals_sameServiceClazz() {
    Class serviceClazz = String.class;
    String targetNamespace1 = "http://www.example.com";
    String targetNamespace2 = "http://www.example.net";
    String typesNamespace = "http://www.example.com/types";
    
    WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz, targetNamespace1, typesNamespace);
    WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz, targetNamespace2, typesNamespace);
    
    assertEquals(webServiceDescriptor1, webServiceDescriptor2);
  }
}