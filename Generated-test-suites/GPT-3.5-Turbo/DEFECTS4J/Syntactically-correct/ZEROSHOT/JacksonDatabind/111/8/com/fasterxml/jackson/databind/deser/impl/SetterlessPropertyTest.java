import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class SetterlessPropertyTest {
  
  private SetterlessProperty property;
  
  @Before
  public void setUp() {
    BeanPropertyDefinition propDef = new BeanPropertyDefinition();
    JavaType type = new JavaType();
    TypeDeserializer typeDeser = new TypeDeserializer();
    Annotations contextAnnotations = new Annotations();
    AnnotatedMethod method = new AnnotatedMethod();
    
    property = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);
  }
  
  @Test
  public void testWithName() {
    PropertyName newName = new PropertyName();
    SettableBeanProperty newProperty = property.withName(newName);
    
    assertEquals(newName, newProperty.getName());
  }
  
  @Test
  public void testWithValueDeserializer() {
    JsonDeserializer<?> deser = new JsonDeserializer<>();
    SettableBeanProperty newProperty = property.withValueDeserializer(deser);
    
    assertEquals(deser, newProperty.getValueDeserializer());
  }
  
  @Test
  public void testWithNullProvider() {
    NullValueProvider nvp = new NullValueProvider();
    SettableBeanProperty newProperty = property.withNullProvider(nvp);
    
    assertEquals(nvp, newProperty.getNullProvider());
  }
  
  @Test
  public void testFixAccess() {
    DeserializationConfig config = new DeserializationConfig();
    property.fixAccess(config);
    
    assertTrue(config.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS));
  }
  
  @Test
  public void testGetAnnotation() {
    Class<A> acls = A.class;
    A annotation = property.getAnnotation(acls);
    
    assertNotNull(annotation);
  }
  
  @Test
  public void testGetMember() {
    AnnotatedMember member = property.getMember();
    
    assertNotNull(member);
  }
  
  @Test
  public void testDeserializeAndSet() {
    JsonParser p = new JsonParser();
    DeserializationContext ctxt = new DeserializationContext();
    Object instance = new Object();
    
    try {
      property.deserializeAndSet(p, ctxt, instance);
    } catch (IOException e) {
      fail("IOException should not be thrown");
    }
  }
  
  @Test
  public void testDeserializeSetAndReturn() {
    JsonParser p = new JsonParser();
    DeserializationContext ctxt = new DeserializationContext();
    Object instance = new Object();
    
    try {
      Object result = property.deserializeSetAndReturn(p, ctxt, instance);
      assertEquals(instance, result);
    } catch (IOException e) {
      fail("IOException should not be thrown");
    }
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testSet() throws IOException {
    Object instance = new Object();
    Object value = new Object();
    
    property.set(instance, value);
  }
  
  @Test
  public void testSetAndReturn() {
    Object instance = new Object();
    Object value = new Object();
    
    try {
      Object result = property.setAndReturn(instance, value);
      assertEquals(instance, result);
    } catch (IOException e) {
      fail("IOException should not be thrown");
    }
  }
}