import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.lang.reflect.Field;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.cfg.MapperFeature;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;

public class FieldPropertyTest {
  
  private FieldProperty fieldProperty;
  
  @Before
  public void setup() {
    BeanPropertyDefinition propDef = new BeanPropertyDefinition();
    JavaType type = new JavaType();
    TypeDeserializer typeDeser = new TypeDeserializer();
    Annotations contextAnnotations = new Annotations();
    AnnotatedField field = new AnnotatedField();
    fieldProperty = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);
  }
  
  @Test
  public void testWithNullProvider() {
    NullValueProvider nva = new NullValueProvider();
    SettableBeanProperty newProperty = fieldProperty.withNullProvider(nva);
    assertNotNull(newProperty);
    assertEquals(nva, newProperty.getNullProvider());
  }
  
  @Test
  public void testWithNullValueDeserializer() {
    JsonDeserializer<?> deser = new JsonDeserializer<>();
    SettableBeanProperty newProperty = fieldProperty.withValueDeserializer(deser);
    assertNotNull(newProperty);
    assertEquals(deser, newProperty.getValueDeserializer());
  }
  
  @Test
  public void testWithName() {
    PropertyName newName = new PropertyName();
    SettableBeanProperty newProperty = fieldProperty.withName(newName);
    assertNotNull(newProperty);
    assertEquals(newName, newProperty.getFullName());
  }
  
  @Test
  public void testDeserializeAndSet() {
    JsonParser p = new JsonParser();
    DeserializationContext ctxt = new DeserializationContext();
    Object instance = new Object();
    try {
      fieldProperty.deserializeAndSet(p, ctxt, instance);
      // assert something
      
    } catch (IOException e) {
      fail("An exception occurred: " + e.getMessage());
    }
  }
  
  @Test
  public void testDeserializeSetAndReturn() {
    JsonParser p = new JsonParser();
    DeserializationContext ctxt = new DeserializationContext();
    Object instance = new Object();
    try {
      Object result = fieldProperty.deserializeSetAndReturn(p, ctxt, instance);
      // assert something
      
    } catch (IOException e) {
      fail("An exception occurred: " + e.getMessage());
    }
  }
  
  @Test
  public void testSet() {
    Object instance = new Object();
    Object value = new Object();
    try {
      fieldProperty.set(instance, value);
      // assert something
      
    } catch (IOException e) {
      fail("An exception occurred: " + e.getMessage());
    }
  }
  
  @Test
  public void testSetAndReturn() {
    Object instance = new Object();
    Object value = new Object();
    try {
      Object result = fieldProperty.setAndReturn(instance, value);
      // assert something
      
    } catch (IOException e) {
      fail("An exception occurred: " + e.getMessage());
    }
  }
  
  @Test
  public void testFixAccess() {
    DeserializationConfig config = new DeserializationConfig();
    fieldProperty.fixAccess(config);
    // assert something
  }
  
  @Test
  public void testGetAnnotation() {
    Class<Annotation> acls = Annotation.class;
    Annotation annotation = fieldProperty.getAnnotation(acls);
    assertNull(annotation);
  }
  
  @Test
  public void testGetMember() {
    AnnotatedMember member = fieldProperty.getMember();
    assertNotNull(member);
  }
  
  @Test
  public void testReadResolve() {
    Object result = fieldProperty.readResolve();
    assertNotNull(result);
    assertTrue(result instanceof FieldProperty);
  }
}