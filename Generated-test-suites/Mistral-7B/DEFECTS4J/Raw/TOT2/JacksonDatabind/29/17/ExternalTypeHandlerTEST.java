import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.std.StringDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.test.BaseTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ExternalTypeHandlerTest extends BaseTest {

 private ObjectIdReader objectIdReader;
 private StringDeserializer stringDeserializer;
 private TypeDeserializer typeDeserializer;
 private ExternalTypeHandler externalTypeHandler;

 @BeforeMethod
 public void setup() {
 objectIdReader = mock(ObjectIdReader.class);
 stringDeserializer = mock(StringDeserializer.class);
 typeDeserializer = mock(TypeDeserializer.class);
 when(typeDeserializer.getPropertyName()).thenReturn("typePropertyName");
 when(typeDeserializer.getDefaultImpl()).thenReturn(String.class);

 externalTypeHandler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
 .addExternal(mock(SettableBeanProperty.class), objectIdReader)
 .addExternal(mock(SettableBeanProperty.class), stringDeserializer)
 .addExternal(mock(SettableBeanProperty.class), typeDeserializer)
 .build());
 }

 @Test
 public void testHandleTypePropertyValue() throws Exception {
 String propertyName = "typePropertyName";
 String typeId = "StringType";
 Object bean = new Object();
 String expectedTypeId = "typePropertyName";

 doReturn(propertyName).when(externalTypeHandler).getName(0);
 doReturn(typeId).when(objectIdReader).deserialize(any(JsonParser.class), any(DeserializationContext.class), any(JsonDeserializer.Context.class));

 externalTypeHandler.handleTypePropertyValue(mock(JsonParser.class), mock(DeserializationContext.class), propertyName, bean);

 verify(objectIdReader).deserialize(any(JsonParser.class), any(DeserializationContext.class), any(JsonDeserializer.Context.class));
 assertEquals(externalTypeHandler.getTypeIds()[0], expectedTypeId);
 }

 @Test
 public void testHandleTypePropertyValue_whenNoTypePropertyFound() throws Exception {
 String propertyName = "nonexistentProperty";
 Object bean = new Object();

 doReturn(propertyName).when(externalTypeHandler).getName(0);

 externalTypeHandler.handleTypePropertyValue(mock(JsonParser.class), mock(DeserializationContext.class), propertyName, bean);

 verifyNoMoreInteractions(objectIdReader);
 }

 @Test
 public void testHandlePropertyValue_whenHasTypePropertyName() throws Exception {
 String propertyName = "typePropertyName";
 String value = "testValue";
 Object bean = new Object();

 doReturn(propertyName).when(externalTypeHandler).getName(0);
 doReturn(value).when(externalTypeHandler._tokens[0]).getText();
 doThrow(new IOException("Test error")).when(externalTypeHandler._tokens[0]).asParser(any(JsonParser.class));

 externalTypeHandler.handlePropertyValue(mock(JsonParser.class), mock(DeserializationContext.class), propertyName, bean);

 verify(externalTypeHandler._tokens[0]).getText();
 verifyZeroInteractions(objectIdReader);
 }

 @Test(expectedExceptions = IOException.class)
 public void testHandlePropertyValue_whenThrowsIOException() throws Exception {
 String propertyName = "typePropertyName";
 Object bean = new Object();

 doReturn(propertyName).when(externalTypeHandler).getName(0);
 doThrow(new IOException("Test error")).when(externalTypeHandler._tokens[0]).asParser(any(JsonParser.class));

 externalTypeHandler.handlePropertyValue(mock(JsonParser.class), mock(DeserializationContext.class), propertyName, bean);
 }

 @Test
 public void testComplete_whenAllValuesAreFullyDeserialized() throws Exception {
 JsonParser jsonParser = createParser("{\"property1\": \"StringValue1\", \"property2\": \"StringValue2\", \"typePropertyName\": \"ListType\", \"typePropertyValue\": [\"ListElement1\", \"ListElement2\"]}");
 Object bean = new Object();

 externalTypeHandler.handleTypePropertyValue(jsonParser, mock(DeserializationContext.class), "typePropertyName", bean);
 externalTypeHandler.handlePropertyValue(jsonParser, mock(DeserializationContext.class), "property1", bean);
 externalTypeHandler.handlePropertyValue(jsonParser, mock(DeserializationContext.class), "property2", bean);
 externalTypeHandler.handlePropertyValue(jsonParser, mock(DeserializationContext.class), "typePropertyName", bean);
 externalTypeHandler.handlePropertyValue(jsonParser, mock(DeserializationContext.class), "typePropertyValue[0]", bean);
 externalTypeHandler.handlePropertyValue(jsonParser, mock(DeserializationContext.class), "typePropertyValue[1]", bean);

 assertEquals(jsonParser.getValueAsTree(), externalTypeHandler.complete(jsonParser, mock(DeserializationContext.class), bean));
 }

 @Test
 public void testComplete_whenTypeIdIsMissing() throws Exception {
 JsonParser jsonParser = createParser("{\"property1\": \"StringValue1\", \"property2\": \"StringValue2\", \"property3\": \"StringValue3\"}");
 Object bean = new Object();

 externalTypeHandler.handlePropertyValue(jsonParser, mock(DeserializationContext.class), "property1", bean);
 externalTypeHandler.handlePropertyValue(jsonParser, mock(DeserializationContext.class), "property2", bean);
 externalTypeHandler.handlePropertyValue(jsonParser, mock(DeserializationContext.class), "property3", bean);

 assertNull(externalTypeHandler.complete(jsonParser, mock(DeserializationContext.class), bean));
 }

 @Test
 public void testComplete_whenTypeIdIsNotString() throws Exception {
 JsonParser jsonParser = createParser("{\"property1\": \"StringValue1\", \"property2\": \"StringValue2\", \"typePropertyName\": 123, \"typePropertyValue\": [\"ListElement1\", \"ListElement2\"]}");
 Object bean = new Object();

 externalTypeHandler.handleTypePropertyValue(jsonParser, mock(DeserializationContext.class), "typePropertyName", bean);
 externalTypeHandler.handlePropertyValue(jsonParser, mock(DeserializationContext.class), "property1", bean);
 externalTypeHandler.handlePropertyValue(jsonParser, mock(DeserializationContext.class), "property2", bean);
 externalTypeHandler.handlePropertyValue(jsonParser, mock(DeserializationContext.class), "typePropertyName", bean);
 externalTypeHandler.handlePropertyValue(jsonParser, mock(DeserializationContext.class), "typePropertyValue[0]", bean);
 externalTypeHandler.handlePropertyValue(jsonParser, mock(DeserializationContext.class), "typePropertyValue[1]", bean);

 assertNull(externalTypeHandler.complete(jsonParser, mock(DeserializationContext.class), bean));
 }

 @Test
 public void testComplete_whenTypeIdIsNull() throws Exception {
 JsonParser jsonParser = createParser("{\"property1\": \"StringValue1\", \"property2\": \"StringValue2\", \"typePropertyName\": null, \"typePropertyValue\": [\"ListElement1\", \"ListElement2\"]}");
 Object bean = new Object();

 externalTypeHandler.handleTypePropertyValue(jsonParser, mock(DeserializationContext.class), "typePropertyName", bean);
 externalTypeHandler.handlePropertyValue(jsonParser, mock(DeserializationContext.class), "property1", bean);
 externalTypeHandler.handlePropertyValue(jsonParser, mock(DeserializationContext.class), "property2", bean);
 externalTypeHandler.handlePropertyValue(jsonParser, mock(DeserializationContext.class), "typePropertyName", bean);
 externalTypeHandler.handlePropertyValue(jsonParser, mock(DeserializationContext.class), "typePropertyValue[0]", bean);
 externalTypeHandler.handlePropertyValue(jsonParser, mock(DeserializationContext.class), "typePropertyValue[1]", bean);

 assertNull(externalTypeHandler.complete(jsonParser, mock(DeserializationContext.class), bean));
 }

 @Test
 public void testComplete_whenPropertyValueIsMissing() throws Exception {
 JsonParser jsonParser = createParser("{\"property1\": \"StringValue1\", \"property2\": \"StringValue2\", \"typePropertyName\": \"ListType\"}");
 Object bean = new Object();

 externalTypeHandler.handleTypePropertyValue(jsonParser, mock(DeserializationContext.class), "typePropertyName", bean);
 externalTypeHandler.handlePropertyValue(jsonParser, mock(DeserializationContext.class), "property1", bean);
 externalTypeHandler.handlePropertyValue(jsonParser, mock(DeserializationContext.class), "property2", bean);

 assertNull(externalTypeHandler.complete(jsonParser, mock(DeserializationContext.class), bean));
 }

 @Test
 public void testComplete_whenPropertyValueIsNotArray() throws Exception {
 JsonParser jsonParser = createParser("{\"property1\": \"StringValue1\", \"property2\": \"StringValue2\", \"typePropertyName\": \"ListType\", \"typePropertyValue\": \"TestValue\"}");
 Object bean = new Object();

 externalTypeHandler.handleTypePropertyValue(jsonParser, mock(DeserializationContext.class), "typePropertyName", bean);
 externalTypeHandler.handlePropertyValue(jsonParser, mock(DeserializationContext.class), "property1", bean);
 externalTypeHandler.handlePropertyValue(jsonParser, mock(DeserializationContext.class), "property2", bean);

 assertNull(externalTypeHandler.complete(jsonParser, mock(DeserializationContext.class), bean));
 }

 @Test
 public void testComplete_whenTypeIdIsMissingAndDefaultTypeIsSet() throws Exception {
 JsonParser jsonParser = createParser("{\"property1\": \"StringValue1\", \"property2\": \"StringValue2\", \"typePropertyName\": null, \"typePropertyValue\": [null, null]}");
 Object bean = new Object();

 externalTypeHandler.handleTypePropertyValue(jsonParser, mock(DeserializationContext.class), "typePropertyName", bean);
 externalTypeHandler.handlePropertyValue(jsonParser, mock(DeserializationContext.class), "property1", bean);
 externalTypeHandler.handlePropertyValue(jsonParser, mock(DeserializationContext.class), "property2", bean);

 Object expectedValue = "defaultValue";
 doReturn(expectedValue).when(typeDeserializer).deserialize(any(JsonParser.class), any(DeserializationContext.class), any(JsonDeserializer.Context.class));
 doReturn(typeDeserializer).when(externalTypeHandler).getTypeDeserializer(1);
 doReturn(expectedValue).when(externalTypeHandler)._deserialize(jsonParser, mock(DeserializationContext.class), 1, anyString());

 assertEquals(expectedValue, externalTypeHandler.complete(jsonParser, mock(DeserializationContext.class), bean)[1]);
 }

 @Test
 public void testComplete_whenPropertyValueIsEmptyArray() throws Exception {
 JsonParser jsonParser = createParser("{\"property1\": \"StringValue1\", \"property2\": \"StringValue2\", \"typePropertyName\": \"ListType\", \"typePropertyValue\": []}");
 Object bean = new Object();

 externalTypeHandler.handleTypePropertyValue(jsonParser, mock(DeserializationContext.class), "typePropertyName", bean);
 externalTypeHandler.handlePropertyValue(jsonParser, mock(DeserializationContext.class), "property1", bean);
 externalTypeHandler.handlePropertyValue(jsonParser, mock(DeserializationContext.class), "property2", bean);

 Object[] expectedValues = new Object[0];
 assertEquals(expectedValues, externalTypeHandler.complete(jsonParser, mock(DeserializationContext.class), bean));
 }

 @Test
 public void testComplete_whenPropertyValueIsArrayWithNullValues() throws Exception {
 JsonParser jsonParser = createParser("{\"property1\": \"StringValue1\", \"property2\": \"StringValue2\", \"typePropertyName\": \"ListType\", \"typePropertyValue\": [null, null]}");
 Object bean = new Object();

 externalTypeHandler.handleTypePropertyValue(jsonParser, mock(DeserializationContext.class), "typePropertyName", bean);
 externalTypeHandler.handlePropertyValue(jsonParser, mock(DeserializationContext.class), "property1", bean);
 externalTypeHandler.handlePropertyValue(jsonParser, mock(DeserializationContext.class), "property2", bean);

 Object[] expectedValues = new Object[2];
 expectedValues[0] = null;
 expectedValues[1] = null;
 assertEquals(expectedValues, externalTypeHandler.complete(jsonParser, mock(DeserializationContext.class), bean));
 }

 @Test
 public void testComplete_whenPropertyValueIsArrayWithValidValues() throws Exception {
 JsonParser jsonParser = createParser("{\"property1\": \"StringValue1\", \"property2\": \"StringValue2\", \"typePropertyName\": \"ListType\", \"typePropertyValue\": [\"Value1\", \"Value2\"]}");
 Object bean = new Object();

 externalTypeHandler.handleTypePropertyValue(jsonParser, mock(DeserializationContext.class), "typePropertyName", bean);
 externalTypeHandler.handlePropertyValue(jsonParser, mock(DeserializationContext.class), "property1", bean);
 externalTypeHandler.handlePropertyValue(jsonParser, mock(DeserializationContext.class), "property2", bean);

 Object[] expectedValues = new Object[2];
 expectedValues[0] = "Value1";
 expectedValues[1] = "Value2";
 assertEquals(expectedValues, externalTypeHandler.complete(jsonParser, mock(DeserializationContext.class), bean));
 }

 @Test
 public void testComplete_whenCreatorIsUsed() throws Exception {
 JsonParser jsonParser = createParser("{\"creatorProperty1\": \"StringValue1\", \"property1\": \"StringValue1\", \"property2\": \"StringValue2\"}");
 PropertyValueBuffer buffer = mock(PropertyValueBuffer.class);
 PropertyBasedCreator creator = mock(PropertyBasedCreator.class);

 when(creator.findCreatorProperty("creatorProperty1")).thenReturn(mock(SettableBeanProperty.class));
 when(creator.build(any(DeserializationContext.class), any(PropertyValueBuffer.class))).thenReturn(new Object());

 externalTypeHandler.handleTypePropertyValue(jsonParser, mock(DeserializationContext.class), "typePropertyName", jsonParser);
 externalTypeHandler.handlePropertyValue(jsonParser, mock(DeserializationContext.class), "creatorProperty1", buffer);
 externalTypeHandler.handlePropertyValue(jsonParser, mock(DeserializationContext.class), "property1", buffer);
 externalTypeHandler.handlePropertyValue(jsonParser, mock(DeserializationContext.class), "property2", buffer);

 externalTypeHandler.complete(jsonParser, mock(DeserializationContext.class), buffer, creator);

 verify(creator).build(any(DeserializationContext.class), any(PropertyValueBuffer.class));
 }
}