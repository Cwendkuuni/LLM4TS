import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.SetterlessProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class SetterlessPropertyTest {

 private static final String TEST_PROPERTY_NAME = "testProperty";
 private static final Class<SetterlessProperty> TEST_CLASS = SetterlessProperty.class;

 @Test
 public void testConstructor() {
 SetterlessProperty setterlessProperty = new SetterlessProperty(
 Mockito.mock(BeanPropertyDefinition.class), Mockito.mock(JavaType.class),
 Mockito.mock(TypeDeserializer.class), Mockito.mock(Annotations.class),
 Mockito.mock(AnnotatedMethod.class));

 assertNotNull(setterlessProperty);
 }

 @Test
 public void testWithName() {
 SetterlessProperty setterlessProperty = new SetterlessProperty(
 Mockito.mock(BeanPropertyDefinition.class), Mockito.mock(JavaType.class),
 Mockito.mock(TypeDeserializer.class), Mockito.mock(Annotations.class),
 Mockito.mock(AnnotatedMethod.class));

 SettableBeanProperty newSetterlessProperty = setterlessProperty.withName(
 Mockito.mock(PropertyName.class));

 assertEquals(newSetterlessProperty, setterlessProperty.withName(
 Mockito.mock(PropertyName.class)));
 }

 @Test
 public void testWithValueDeserializer() {
 SetterlessProperty setterlessProperty = new SetterlessProperty(
 Mockito.mock(BeanPropertyDefinition.class), Mockito.mock(JavaType.class),
 Mockito.mock(TypeDeserializer.class), Mockito.mock(Annotations.class),
 Mockito.mock(AnnotatedMethod.class));

 JsonDeserializer<?> valueDeserializerMock = mock(JsonDeserializer.class);
 SettableBeanProperty newSetterlessProperty = setterlessProperty.withValueDeserializer(
 valueDeserializerMock);

 assertEquals(valueDeserializerMock, setterlessProperty.withValueDeserializer(
 valueDeserializerMock));
 }

 @Test
 public void testWithNullProvider() {
 SetterlessProperty setterlessProperty = new SetterlessProperty(
 Mockito.mock(BeanPropertyDefinition.class), Mockito.mock(JavaType.class),
 Mockito.mock(TypeDeserializer.class), Mockito.mock(Annotations.class),
 Mockito.mock(AnnotatedMethod.class));

 NullValueProvider nvaMock = mock(NullValueProvider.class);
 SettableBeanProperty newSetterlessProperty = setterlessProperty.withNullProvider(
 nvaMock);

 assertEquals(nvaMock, setterlessProperty.withNullProvider(nvaMock));
 }

 @Test
 public void testFixAccess() {
 SetterlessProperty setterlessProperty = new SetterlessProperty(
 Mockito.mock(BeanPropertyDefinition.class), Mockito.mock(JavaType.class),
 Mockito.mock(TypeDeserializer.class), Mockito.mock(Annotations.class),
 Mockito.mock(AnnotatedMethod.class));

 DeserializationConfig configMock = mock(DeserializationConfig.class);
 setterlessProperty.fixAccess(configMock);

 verify(configMock).isEnabled(eq(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSet() throws IOException {
 SetterlessProperty setterlessProperty = new SetterlessProperty(
 Mockito.mock(BeanPropertyDefinition.class), Mockito.mock(JavaType.class),
 Mockito.mock(TypeDeserializer.class), Mockito.mock(Annotations.class),
 Mockito.mock(AnnotatedMethod.class));

 Object instance = new HashMap<>();
 setterlessProperty.set(instance, Mockito.mock(Object.class));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetAndReturn() throws IOException {
 SetterlessProperty setterlessProperty = new SetterlessProperty(
 Mockito.mock(BeanPropertyDefinition.class), Mockito.mock(JavaType.class),
 Mockito.mock(TypeDeserializer.class), Mockito.mock(Annotations.class),
 Mockito.mock(AnnotatedMethod.class));

 Object instance = new HashMap<>();
 setterlessProperty.setAndReturn(instance, Mockito.mock(Object.class));
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 SetterlessProperty setterlessProperty = new SetterlessProperty(
 Mockito.mock(BeanPropertyDefinition.class), Mockito.mock(JavaType.class),
 Mockito.mock(TypeDeserializer.class), Mockito.mock(Annotations.class),
 Mockito.mock(AnnotatedMethod.class));

 Object instance = new HashMap<>();
 JsonParser parserMock = mock(JsonParser.class);
 when(parserMock.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
 when(parserMock.getValueAsString()).thenReturn("testValue");

 setterlessProperty.deserializeAndSet(parserMock, Mockito.mock(DeserializationContext.class), instance);

 verify(instance).getClass();
 verify(parserMock).getValueAsString();
 }

 @Test
 public void testDeserializeSetAndReturn() throws IOException {
 SetterlessProperty setterlessProperty = new SetterlessProperty(
 Mockito.mock(BeanPropertyDefinition.class), Mockito.mock(JavaType.class),
 Mockito.mock(TypeDeserializer.class), Mockito.mock(Annotations.class),
 Mockito.mock(AnnotatedMethod.class));

 Object instance = new HashMap<>();
 JsonParser parserMock = mock(JsonParser.class);
 when(parserMock.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
 when(parserMock.getValueAsString()).thenReturn("testValue");

 setterlessProperty.deserializeSetAndReturn(parserMock, Mockito.mock(DeserializationContext.class), instance);

 verify(instance).getClass();
 verify(parserMock).getValueAsString();
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetAnnotation() {
 SetterlessProperty setterlessProperty = new SetterlessProperty(
 Mockito.mock(BeanPropertyDefinition.class), Mockito.mock(JavaType.class),
 Mockito.mock(TypeDeserializer.class), Mockito.mock(Annotations.class),
 Mockito.mock(AnnotatedMethod.class));

 setterlessProperty.getAnnotation(JsonProperty.class);
 }

 @Test(expected = IOException.class)
 public void testDeserializeAndSetNullValue() throws IOException {
 SetterlessProperty setterlessProperty = new SetterlessProperty(
 Mockito.mock(BeanPropertyDefinition.class), Mockito.mock(JavaType.class),
 Mockito.mock(TypeDeserializer.class), Mockito.mock(Annotations.class),
 Mockito.mock(AnnotatedMethod.class));

 Object instance = new HashMap<>();
 JsonParser parserMock = mock(JsonParser.class);
 when(parserMock.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);

 setterlessProperty.deserializeAndSet(parserMock, Mockito.mock(DeserializationContext.class), instance);
 }

 @Test(expected = IOException.class)
 public void testDeserializeAndSetInvalidType() throws IOException {
 SetterlessProperty setterlessProperty = new SetterlessProperty(
 Mockito.mock(BeanPropertyDefinition.class), Mockito.mock(JavaType.class),
 Mockito.mock(TypeDeserializer.class), Mockito.mock(Annotations.class),
 Mockito.mock(AnnotatedMethod.class));

 Object instance = new HashMap<>();
 JsonParser parserMock = mock(JsonParser.class);
 when(parserMock.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
 when(parserMock.getValueAsString()).thenReturn("testValue");
 when(parserMock.getTypeDeserializer()).thenReturn(
 Mockito.mock(TypeDeserializer.class));

 setterlessProperty.deserializeAndSet(parserMock, Mockito.mock(DeserializationContext.class), instance);

 verify(parserMock).getTypeDeserializer();
 }

 @Test(expected = IOException.class)
 public void testDeserializeAndSetThrowException() throws IOException {
 SetterlessProperty setterlessProperty = new SetterlessProperty(
 Mockito.mock(BeanPropertyDefinition.class), Mockito.mock(JavaType.class),
 Mockito.mock(TypeDeserializer.class), Mockito.mock(Annotations.class),
 Mockito.mock(AnnotatedMethod.class));

 Object instance = new HashMap<>();
 JsonParser parserMock = mock(JsonParser.class);
 when(parserMock.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
 when(parserMock.getValueAsString()).thenReturn("testValue");
 when(parserMock.isExpectedStartArrayToken()).thenReturn(true);
 when(parserMock.nextToken()).thenThrow(new IOException("testException"));

 setterlessProperty.deserializeAndSet(parserMock, Mockito.mock(DeserializationContext.class), instance);
 }

 @Test(expected = IOException.class)
 public void testDeserializeSetAndReturnThrowException() throws IOException {
 SetterlessProperty setterlessProperty = new SetterlessProperty(
 Mockito.mock(BeanPropertyDefinition.class), Mockito.mock(JavaType.class),
 Mockito.mock(TypeDeserializer.class), Mockito.mock(Annotations.class),
 Mockito.mock(AnnotatedMethod.class));

 Object instance = new HashMap<>();
 JsonParser parserMock = mock(JsonParser.class);
 when(parserMock.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
 when(parserMock.getValueAsString()).thenReturn("testValue");
 when(parserMock.isExpectedStartArrayToken()).thenReturn(true);
 when(parserMock.nextToken()).thenThrow(new IOException("testException"));

 setterlessProperty.deserializeSetAndReturn(parserMock, Mockito.mock(DeserializationContext.class), instance);
 }
}