import com.fasterxml.jackson.databind.deser.impl.FieldProperty;
import com.fasterxml.jackson.databind.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.ObjectMapper.MapperFeature;
import com.fasterxml.jackson.databind.deser.NullValueProvider;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(MockitoJUnitRunner.class)
public class FieldPropertyTest {
  
  @Test
  public void testConstructor() {
    
    BeanPropertyDefinition propDef = Mockito.mock(BeanPropertyDefinition.class);
    JavaType type = Mockito.mock(JavaType.class);
    TypeDeserializer typeDeser = Mockito.mock(TypeDeserializer.class);
    Annotations contextAnnotations = Mockito.mock(Annotations.class);
    AnnotatedField field = Mockito.mock(AnnotatedField.class);
    
    FieldProperty fieldProperty = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);
    
    assertEquals(propDef, fieldProperty.getMember());
    assertEquals(type, fieldProperty.getType());
  }
  
  @Test
  public void testWithNullProvider() {
    NullValueProvider nva = Mockito.mock(NullValueProvider.class);
    
    BeanPropertyDefinition propDef = Mockito.mock(BeanPropertyDefinition.class);
    JavaType type = Mockito.mock(JavaType.class);
    TypeDeserializer typeDeser = Mockito.mock(TypeDeserializer.class);
    Annotations contextAnnotations = Mockito.mock(Annotations.class);
    AnnotatedField field = Mockito.mock(AnnotatedField.class);
    
    FieldProperty fieldProperty = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);
    FieldProperty newFieldProperty = fieldProperty.withNullProvider(nva);
    
    assertEquals(propDef, newFieldProperty.getMember());
    assertEquals(type, newFieldProperty.getType());
    assertEquals(nva, newFieldProperty.getNullValueProvider());
  }
  
  @Test
  public void testDeserializingAndSetting() throws IOException {
    JsonParser p = Mockito.mock(JsonParser.class);
    DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
    Object instance = new Object();
    
    BeanPropertyDefinition propDef = Mockito.mock(BeanPropertyDefinition.class);
    JavaType type = Mockito.mock(JavaType.class);
    TypeDeserializer typeDeser = Mockito.mock(TypeDeserializer.class);
    Annotations contextAnnotations = Mockito.mock(Annotations.class);
    AnnotatedField field = Mockito.mock(AnnotatedField.class);
    
    FieldProperty fieldProperty = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);
    
    fieldProperty.deserializeAndSet(p, ctxt, instance);
    
    Mockito.verify(p).hasToken(JsonToken.VALUE_NULL);
    Mockito.verify(p, Mockito.never()).getValueAsString();
    Mockito.verify(ctxt).getNullValueProvider();
    Mockito.verify(ctxt).handleUnexpectedToken(JsonToken.VALUE_NULL, p,
    "Cannot deserialize value of type `java.lang.Object` from NullNode");
  }
  
  @Test
  public void testDeserializingSetAndReturn() throws IOException {
    JsonParser p = Mockito.mock(JsonParser.class);
    DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
    Object instance = new Object();
    
    BeanPropertyDefinition propDef = Mockito.mock(BeanPropertyDefinition.class);
    JavaType type = Mockito.mock(JavaType.class);
    TypeDeserializer typeDeser = Mockito.mock(TypeDeserializer.class);
    Annotations contextAnnotations = Mockito.mock(Annotations.class);
    AnnotatedField field = Mockito.mock(AnnotatedField.class);
    
    FieldProperty fieldProperty = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);
    
    fieldProperty.deserializeSetAndReturn(p, ctxt, instance);
    
    Mockito.verify(p).hasToken(JsonToken.VALUE_NULL);
    Mockito.verify(p, Mockito.never()).getValueAsString();
    Mockito.verify(ctxt).getNullValueProvider();
    Mockito.verify(ctxt).handleUnexpectedToken(JsonToken.VALUE_NULL, p,
    "Cannot deserialize value of type `java.lang.Object` from NullNode");
  }
  
  @Test
  public void testSetAndReturn() throws IOException {
    Object instance = new Object();
    Object value = new Object();
    
    BeanPropertyDefinition propDef = Mockito.mock(BeanPropertyDefinition.class);
    JavaType type = Mockito.mock(JavaType.class);
    TypeDeserializer typeDeser = Mockito.mock(TypeDeserializer.class);
    Annotations contextAnnotations = Mockito.mock(Annotations.class);
    AnnotatedField field = Mockito.mock(AnnotatedField.class);
    
    FieldProperty fieldProperty = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);
    
    fieldProperty.setAndReturn(instance, value);
    
    Mockito.verify(field).set(instance, value);
  }
  
  @Test
  public void testGetAnnotation() {
    Class<A> acls = Mockito.mock(Class.class);
    
    BeanPropertyDefinition propDef = Mockito.mock(BeanPropertyDefinition.class);
    JavaType type = Mockito.mock(JavaType.class);
    TypeDeserializer typeDeser = Mockito.mock(TypeDeserializer.class);
    Annotations contextAnnotations = Mockito.mock(Annotations.class);
    AnnotatedField field = Mockito.mock(AnnotatedField.class);
    
    FieldProperty fieldProperty = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);
    
    fieldProperty.getAnnotation(acls);
    
    Mockito.verify(field).getAnnotation(acls);
  }
  
}