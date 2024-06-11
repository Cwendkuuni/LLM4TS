import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class InnerClassPropertyTest {
  
  private InnerClassProperty innerClassProperty;
  private SettableBeanProperty delegate;
  private Constructor<?> ctor;
  
  @Before
  public void setUp() {
    delegate = new SettableBeanProperty();
    ctor = new Constructor<>();
    innerClassProperty = new InnerClassProperty(delegate, ctor);
  }
  
  @Test
  public void testConstructor() {
    assertEquals(delegate, innerClassProperty._delegate);
    assertEquals(ctor, innerClassProperty._creator);
  }
  
  @Test
  public void testWithAnnotation() {
    Class<Annotation> acls = Annotation.class;
    assertEquals(delegate.getAnnotation(acls), innerClassProperty.getAnnotation(acls));
  }
  
  @Test
  public void testGetMember() {
    assertEquals(delegate.getMember(), innerClassProperty.getMember());
  }
  
  @Test
  public void testDeserializeAndSet() {
    JsonParser jp = new JsonParser();
    DeserializationContext ctxt = new DeserializationContext();
    Object bean = new Object();
    
    // Test when current token is null
    jp.setCurrentToken(null);
    innerClassProperty.deserializeAndSet(jp, ctxt, bean);
    assertNull(innerClassProperty.get(bean));
    
    // Test when value type deserializer is null
    innerClassProperty._valueTypeDeserializer = null;
    innerClassProperty.deserializeAndSet(jp, ctxt, bean);
    assertNotNull(innerClassProperty.get(bean));
  }
  
  @Test
  public void testDeserializeSetAndReturn() {
    JsonParser jp = new JsonParser();
    DeserializationContext ctxt = new DeserializationContext();
    Object instance = new Object();
    
    assertNotNull(innerClassProperty.deserializeSetAndReturn(jp, ctxt, instance));
  }
  
  @Test
  public void testSetAndReturn() {
    Object instance = new Object();
    Object value = new Object();
    
    assertEquals(delegate.setAndReturn(instance, value), innerClassProperty.setAndReturn(instance, value));
  }
  
  @Test
  public void testReadResolve() {
    assertNotNull(innerClassProperty.readResolve());
  }
  
  @Test
  public void testWriteReplace() {
    assertNotNull(innerClassProperty.writeReplace());
  }
}