package com.example;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Method;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.jsontype.SimpleType;
import com.fasterxml.jackson.databind.util.NameTransformer;

public class SetterlessPropertyTest {

 private static final ObjectMapper objectMapper = new ObjectMapper();

 @SuppressWarnings("unchecked")
 @BeforeClass
 public static void setup() {
 Class<SetterlessProperty> setterlessPropertyClass = SetterlessProperty.class;
 JavaType javaType = SimpleType.constructUnboundedType(setterlessPropertyClass);
 BeanDescription beanDescription = objectMapper.getFactory().createBeanDescription(setterlessPropertyClass);
 AnnotationIntrospector introspector = objectMapper.getAnnotationIntrospector();
 DeserializationConfig config = objectMapper.getDeserializationConfig();
 PropertyName propertyName = PropertyName.construct("propertyName");
 SetterlessProperty setterlessProperty = new SetterlessProperty(new BeanPropertyDefinition(propertyName, null, javaType, null, null), javaType, null, null, mock(AnnotatedMethod.class));
 SettableBeanProperty property = (SettableBeanProperty) setterlessProperty;
 property.setValueDeserializer(mock(JsonDeserializer.class));
 }

 @Test
 public void testConstructor() {
 SetterlessProperty setterlessProperty = new SetterlessProperty(null, null, null, null, null);
 assertNotNull(setterlessProperty);
 }

 @Test
 public void testGetAnnotated() {
 AnnotatedMethod annotatedMethod = mock(AnnotatedMethod.class);
 SetterlessProperty setterlessProperty = new SetterlessProperty(null, null, null, null, annotatedMethod);
 assertEquals(annotatedMethod, setterlessProperty.getAnnotated());
 }

 @Test
 public void testGetGetter() {
 Method getter = mock(Method.class);
 SetterlessProperty setterlessProperty = new SetterlessProperty(null, null, null, null, null, getter);
 assertEquals(getter, setterlessProperty.getGetter());
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 Object instance = new Object();
 SetterlessProperty setterlessProperty = new SetterlessProperty(null, null, null, null, null);
 JsonParser jsonParser = mock(JsonParser.class);
 when(jsonParser.getCurrentToken()).thenReturn(JsonToken.START_ARRAY);
 DeserializationContext deserializationContext = mock(DeserializationContext.class);
 when(deserializationContext.getAnnotationIntrospector()).thenReturn(introspector);
 when(deserializationContext.findBeanDescriptionFor(setterlessProperty.getType())).thenReturn(beanDescription);
 when(deserializationContext.getValueFactory()).thenReturn(mock(ValueFactory.class));
 when(deserializationContext.isEnabled(MapperFeature.AUTO_DETECT_GETTERS)).thenReturn(true);
 when(deserializationContext.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS)).thenReturn(true);
 when(deserializationContext.findClassInfoFor(setterlessProperty.getName())).thenReturn(mock(ClassInfo.class));
 when(deserializationContext.getBeanProperty("propertyName")).thenReturn(property);

 setterlessProperty.deserializeAndSet(jsonParser, deserializationContext, instance);
 Object result = property.getValue();
 assertEquals(result, instance);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSet() throws IOException {
 Object instance = new Object();
 SetterlessProperty setterlessProperty = new SetterlessProperty(null, null, null, null, null);
 setterlessProperty.set(instance, "value");
 }
}