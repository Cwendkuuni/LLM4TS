import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.impl.ExternalTypeHandler;
import com.fasterxml.jackson.databind.deser.impl.ExternalTypeHandler.Builder;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExternalTypeHandlerTest {

 private ExternalTypeHandler handler;
 private Object testObject;
 private JsonParser parser;
 private DeserializationContext context;
 private TypeDeserializer typeDeserializerMock;
 private Builder builderMock;

 @Before
 public void setup() throws Exception {
 testObject = new Object();
 parser = mock(JsonParser.class);
 context = mock(DeserializationContext.class);
 typeDeserializerMock = mock(TypeDeserializer.class);
 builderMock = mock(Builder.class);

 // Initialize handler
 List<ExtTypedProperty> properties = new ArrayList<>();
 properties.add(new ExtTypedProperty(mock(SettableBeanProperty.class), typeDeserializerMock));
 Builder builder = new Builder();
 builder.addExternal(mock(SettableBeanProperty.class), typeDeserializerMock);
 handler = builder.build();
 }

 @Test
 public void testHandleTypePropertyValue() throws IOException {
 // Given
 String propName = "prop1";
 String typeId = "type1";
 when(parser.getText()).thenReturn(typeId);
 when(parser.getCurrentName()).thenReturn(propName);

 // When
 boolean result = handler.handleTypePropertyValue(parser, context, propName, testObject);

 // Then
 verify(typeDeserializerMock).deserialize(parser, context);
 truth(result);
 }

 @Test
 public void testHandlePropertyValueWithNullTypeId() throws IOException {
 // Given
 String propName = "prop1";
 String typeId = null;
 when(parser.getText()).thenReturn(typeId);
 when(parser.getCurrentName()).thenReturn(propName);

 // When
 boolean result = handler.handlePropertyValue(parser, context, propName, testObject);

 // Then
 verify(parser, never()).skipChildren();
 verify(parser, never()).nextToken();
 falsity(result);
 }

 @Test
 public void testHandlePropertyValueWithPropertyValue() throws IOException {
 // Given
 String propName = "prop1";
 JsonToken jsonToken = JsonToken.VALUE_STRING;
 String propertyValue = "value1";
 when(parser.nextToken()).thenReturn(jsonToken);
 when(parser.getText()).thenReturn(propertyValue);
 when(parser.getCurrentName()).thenReturn(propName);

 // When
 boolean result = handler.handlePropertyValue(parser, context, propName, testObject);

 // Then
 Truth.assertThat(parser.nextToken()).isNull();
 Truth.assertThat(parser.getCurrentLocation().getColumnNumber()).isZero();
 Truth.assertThat(parser.getCurrentLocation().getLineNumber()).isZero();
 truth(result);
 }

 @Test
 public void testCompleteWithEmptyJsonObject() throws IOException {
 // Given
 when(parser.nextToken()).thenReturn(JsonToken.START_OBJECT);

 // When
 Object result = handler.complete(parser, context, testObject);

 // Then
 Truth.assertThat(result).isSameAs(testObject);
 Truth.assertThat(parser.nextToken()).isNull();
 Truth.assertThat(parser.getCurrentLocation().getColumnNumber()).isZero();
 Truth.assertThat(parser.getCurrentLocation().getLineNumber()).isZero();
 }

 @Test
 public void testCompleteWithJsonObjectWithTypeIds() throws IOException {
 // Given
 String typeId1 = "type1";
 String typeId2 = "type2";
 when(parser.nextToken()).thenReturn(JsonToken.START_OBJECT);
 when(parser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
 when(parser.getText()).thenReturn("prop1");
 when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(parser.getText()).thenReturn(typeId1);
 when(parser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
 when(parser.getText()).thenReturn("prop2");
 when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(parser.getText()).thenReturn(typeId2);
 when(parser.nextToken()).thenReturn(JsonToken.END_OBJECT);

 // When
 Object result = handler.complete(parser, context, testObject);

 // Then
 Truth.assertThat(result).isNotNull();
 Truth.assertThat(parser.nextToken()).isNull();
 Truth.assertThat(parser.getCurrentLocation().getColumnNumber()).isZero();
 Truth.assertThat(parser.getCurrentLocation().getLineNumber()).isZero();
 Truth.assertThat(testObject.getClass().getDeclaredField("prop1")).isAssignableFrom(Type1.class);
 Truth.assertThat(testObject.getClass().getDeclaredField("prop2")).isAssignableFrom(Type2.class);
 }

 @Test(expected = IOException.class)
 public void testCompleteWithJsonObjectMissingTypeId() throws IOException {
 // Given
 when(parser.nextToken()).thenReturn(JsonToken.START_OBJECT);
 when(parser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
 when(parser.getText()).thenReturn("prop1");
 when(parser.nextToken()).thenReturn(JsonToken.VALUE_NULL);
 when(parser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
 when(parser.getText()).thenReturn("prop2");
 when(parser.nextToken()).thenReturn(JsonToken.VALUE_NULL);
 when(parser.nextToken()).thenReturn(JsonToken.END_OBJECT);

 // When and Then
 handler.complete(parser, context, testObject);
 }

 @Test(expected = IOException.class)
 public void testCompleteWithJsonObjectMissingProperty() throws IOException {
 // Given
 when(parser.nextToken()).thenReturn(JsonToken.START_OBJECT);
 when(parser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
 when(parser.getText()).thenReturn("prop1");
 when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(parser.getText()).thenReturn("type1");
 when(parser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
 when(parser.getText()).thenReturn("prop2");
 when(parser.nextToken()).thenReturn(JsonToken.END_OBJECT);

 // When and Then
 handler.complete(parser, context, testObject);
 }

 @Test
 public void testCompleteWithJsonArray() throws IOException {
 // Given
 when(parser.nextToken()).thenReturn(JsonToken.START_ARRAY);

 // When
 Object result = handler.complete(parser, context, testObject);

 // Then
 Truth.assertThat(result).isSameAs(testObject);
 Truth.assertThat(parser.nextToken()).isNull();
 Truth.assertThat(parser.getCurrentLocation().getColumnNumber()).isZero();
 Truth.assertThat(parser.getCurrentLocation().getLineNumber()).isZero();
 }

 @Test(expected = IOException.class)
 public void testCompleteWithJsonArrayMissingTypeId() throws IOException {
 // Given
 when(parser.nextToken()).thenReturn(JsonToken.START_ARRAY);

 // When and Then
 handler.complete(parser, context, testObject);
 }
}