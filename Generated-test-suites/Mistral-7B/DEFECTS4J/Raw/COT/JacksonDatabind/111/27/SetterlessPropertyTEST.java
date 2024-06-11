import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RunWith(org.junit.runner.JUnit4.class)
public class SetterlessPropertyTest {

 private static final Class<SetterlessProperty> SETTERLESS_PROPERTY_CLASS = SetterlessProperty.class;

 @Test
 public void testConstructorWithValidArguments() {
 SetterlessProperty setterlessProperty = new SetterlessProperty(
Mockito.mock(BeanPropertyDefinition.class), Mockito.mock(JavaType.class),
Mockito.mock(TypeDeserializer.class), Mockito.mock(Annotations.class),
Mockito.mock(AnnotatedMethod.class));

 assert null != setterlessProperty;
 }

 @Test
 public void testGetAnnotation() throws Exception {
 AnnotatedMethod annotatedMethod = mock(AnnotatedMethod.class);
 SetterlessProperty setterlessProperty = new SetterlessProperty(
Mockito.mock(BeanPropertyDefinition.class), Mockito.mock(JavaType.class),
Mockito.mock(TypeDeserializer.class), Mockito.mock(Annotations.class), annotatedMethod);

 Class<Annotation> annotationType = Annotation.class;
 Annotation annotation = setterlessProperty.getAnnotation(annotationType);

 assertNotNull(annotation);
 verify(annotatedMethod).getAnnotation(annotationType);
 }

 @Test
 public void testDeserializeAndSet() throws Exception {
 JsonParser jsonParser = mock(JsonParser.class);
 DeserializationContext deserializationContext = mock(DeserializationContext.class);
 Object instance = mock(Object.class);
 Object collectionOrMapToModify = new HashMap<>();
 SetterlessProperty setterlessProperty = new SetterlessProperty(
Mockito.mock(BeanPropertyDefinition.class), Mockito.mock(JavaType.class),
Mockito.mock(TypeDeserializer.class), Mockito.mock(Annotations.class),
Mockito.mock(AnnotatedMethod.class), Mockito.mock(Method.class)) {
 @Override
 protected void deserializeAndSet(JsonParser p, DeserializationContext ctxt, Object instance)
throws IOException {
 super.deserializeAndSet(p, ctxt, instance);
 collectionOrMapToModify.put("testKey", "testValue");
 }
 };

 setterlessProperty.deserializeAndSet(jsonParser, deserializationContext, instance);

 assertNotNull(instance);
 assertEquals("testValue", collectionOrMapToModify.get("testKey"));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSet() throws Exception {
 SetterlessProperty setterlessProperty = new SetterlessProperty(
Mockito.mock(BeanPropertyDefinition.class), Mockito.mock(JavaType.class),
Mockito.mock(TypeDeserializer.class), Mockito.mock(Annotations.class),
Mockito.mock(AnnotatedMethod.class), Mockito.mock(Method.class)) {
 @Override
 public void set(Object instance, Object value) throws IOException {
 super.set(instance, value);
 }
 };

 Object instance = mock(Object.class);
 setterlessProperty.set(instance, "testValue");
 }

 @Test
 public void testDeserializeSetAndReturn() throws Exception {
 JsonParser jsonParser = mock(JsonParser.class);
 DeserializationContext deserializationContext = mock(DeserializationContext.class);
 Object instance = mock(Object.class);
 SetterlessProperty setterlessProperty = new SetterlessProperty(
Mockito.mock(BeanPropertyDefinition.class), Mockito.mock(JavaType.class),
Mockito.mock(TypeDeserializer.class), Mockito.mock(Annotations.class),
Mockito.mock(AnnotatedMethod.class), Mockito.mock(Method.class)) {
 @Override
 public Object deserializeSetAndReturn(JsonParser p, DeserializationContext ctxt, Object instance)
throws IOException {
 super.deserializeSetAndReturn(p, ctxt, instance);
 return instance;
 }
 };

 setterlessProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance);

 assertNotNull(instance);
 }

 @Test
 public void testFixAccess() throws Exception {
 DeserializationConfig config = mock(DeserializationConfig.class);
 SetterlessProperty setterlessProperty = new SetterlessProperty(
Mockito.mock(BeanPropertyDefinition.class), Mockito.mock(JavaType.class),
Mockito.mock(TypeDeserializer.class), Mockito.mock(Annotations.class),
Mockito.mock(AnnotatedMethod.class), Mockito.mock(Method.class)) {
 @Override
 public void fixAccess(DeserializationConfig config) {
 super.fixAccess(config);
 config.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS);
 }
 };

 setterlessProperty.fixAccess(config);

 assertTrue(config.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS));
 }

 @Test
 public void testNullValue() throws Exception {
 JsonParser jsonParser = mock(JsonParser.class);
 DeserializationContext deserializationContext = mock(DeserializationContext.class);
 Object instance = mock(Object.class);
 SetterlessProperty setterlessProperty = new SetterlessProperty(
Mockito.mock(BeanPropertyDefinition.class), Mockito.mock(JavaType.class),
Mockito.mock(TypeDeserializer.class), Mockito.mock(Annotations.class),
Mockito.mock(AnnotatedMethod.class), Mockito.mock(Method.class)) {
 @Override
 public void deserializeAndSet(JsonParser p, DeserializationContext ctxt, Object instance)
throws IOException {
 super.deserializeAndSet(p, ctxt, instance);
 }
 };

 setterlessProperty.deserializeAndSet(jsonParser, deserializationContext, instance);

 assertNotNull(instance);
 assertNull(instance.getClass().getDeclaredField("_value"));
 }

 @Test(expected = IOException.class)
 public void testExceptionWhenGettingValue() throws Exception {
 JsonParser jsonParser = mock(JsonParser.class);
 DeserializationContext deserializationContext = mock(DeserializationContext.class);
 Object instance = mock(Object.class);
 SetterlessProperty setterlessProperty = new SetterlessProperty(
Mockito.mock(BeanPropertyDefinition.class), Mockito.mock(JavaType.class),
Mockito.mock(TypeDeserializer.class), Mockito.mock(Annotations.class),
Mockito.mock(AnnotatedMethod.class), Mockito.mock(Method.class)) {
 @Override
 protected Object getValue(Object instance) throws IOException {
 throw new IOException("Test IOException");
 }
 };

 setterlessProperty.deserializeAndSet(jsonParser, deserializationContext, instance);
 }

 @Test
 public void testEmptyCollection() throws Exception {
 JsonParser jsonParser = mock(JsonParser.class);
 DeserializationContext deserializationContext = mock(DeserializationContext.class);
 Object instance = mock(Object.class);
 SetterlessProperty setterlessProperty = new SetterlessProperty(
Mockito.mock(BeanPropertyDefinition.class), Mockito.mock(JavaType.class),
Mockito.mock(TypeDeserializer.class), Mockito.mock(Annotations.class),
Mockito.mock(AnnotatedMethod.class), Mockito.mock(Method.class)) {
 @Override
 public void deserializeAndSet(JsonParser p, DeserializationContext ctxt, Object instance)
throws IOException {
 super.deserializeAndSet(p, ctxt, instance);
 }
 };

 jsonParser = Mockito.spy(jsonParser);
 jsonParser.nextToken();
 setterlessProperty.deserializeAndSet(jsonParser, deserializationContext, instance);

 assertNotNull(instance);
 assertTrue(instance.getClass().getDeclaredField("_value").getType().equals(Collections.class));
 assertNull(instance.getClass().getDeclaredField("_value").get(instance));
 }

}