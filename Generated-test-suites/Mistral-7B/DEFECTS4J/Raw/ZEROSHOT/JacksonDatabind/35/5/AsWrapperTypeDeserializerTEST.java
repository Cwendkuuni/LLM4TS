import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AsWrapperTypeDeserializerTest {

 private ObjectMapper objectMapper;
 private TypeIdResolver typeIdResolver;
 private AsWrapperTypeDeserializer asWrapperTypeDeserializer;

 @BeforeEach
 public void setup() {
 objectMapper = new ObjectMapper();
 typeIdResolver = mock(TypeIdResolver.class);
 asWrapperTypeDeserializer = new AsWrapperTypeDeserializer(
 Mockito.mock(JavaType.class), typeIdResolver, null, false, null);
 }

 @Test
 public void testDeserializeTypedFromObject() throws Exception {
 JsonParser jsonParser = Mockito.mock(JsonParser.class);
 DeserializationContext context = mock(DeserializationContext.class);

 when(jsonParser.getText()).thenReturn("TestTypeId");
 when(jsonParser.hasToken(JsonToken.START_OBJECT)).thenReturn(true);
 when(jsonParser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
 when(jsonParser.getText()).thenReturn("typeId");
 when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(jsonParser.getText()).thenReturn("TestTypeValue");
 when(jsonParser.nextToken()).thenReturn(JsonToken.END_OBJECT);
 when(typeIdResolver.idFromValueAndType(Mockito.anyString(), Mockito.any())).thenReturn("TestTypeId");

 TypeDeserializer deserializer = Mockito.mock(TypeDeserializer.class);
 when(deserializer.deserialize(jsonParser, context)).thenReturn(new Object());

 when(asWrapperTypeDeserializer._findDeserializer(context, "TestTypeId")).thenReturn(deserializer);

 Object result = asWrapperTypeDeserializer.deserializeTypedFromObject(jsonParser, context);

 assertEquals("TestTypeValue", result.getClass().getSimpleName());
 assertNotNull(result);
 }

 @Test
 public void testDeserializeTypedFromArray() throws Exception {
 JsonParser jsonParser = Mockito.mock(JsonParser.class);
 DeserializationContext context = mock(DeserializationContext.class);

 when(jsonParser.getText()).thenReturn("TestTypeId");
 when(jsonParser.hasToken(JsonToken.START_ARRAY)).thenReturn(true);
 when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_NULL);
 when(jsonParser.nextToken()).thenReturn(JsonToken.END_ARRAY);

 Object result = asWrapperTypeDeserializer.deserializeTypedFromArray(jsonParser, context);

 assertNotNull(result);
 assertEquals(Object.class, result.getClass());
 }

 @Test
 public void testDeserializeTypedFromScalar() throws Exception {
 JsonParser jsonParser = Mockito.mock(JsonParser.class);
 DeserializationContext context = mock(DeserializationContext.class);

 when(jsonParser.getText()).thenReturn("TestTypeId");
 when(jsonParser.hasToken(JsonToken.VALUE_STRING)).thenReturn(true);
 when(jsonParser.getText()).thenReturn("TestTypeValue");

 Object result = asWrapperTypeDeserializer.deserializeTypedFromScalar(jsonParser, context);

 assertEquals("TestTypeValue", result.getClass().getSimpleName());
 assertNotNull(result);
 }

 @Test
 public void testDeserializeTypedFromAny() throws Exception {
 JsonParser jsonParser = Mockito.mock(JsonParser.class);
 DeserializationContext context = mock(DeserializationContext.class);

 when(jsonParser.getText()).thenReturn("TestTypeId");
 when(jsonParser.hasToken(JsonToken.START_OBJECT)).thenReturn(true);
 when(jsonParser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
 when(jsonParser.getText()).thenReturn("typeId");
 when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(jsonParser.getText()).thenReturn("TestTypeValue");
 when(jsonParser.nextToken()).thenReturn(JsonToken.END_OBJECT);
 when(typeIdResolver.idFromValueAndType(Mockito.anyString(), Mockito.any())).thenReturn("TestTypeId");

 TypeDeserializer deserializer = Mockito.mock(TypeDeserializer.class);
 when(deserializer.deserialize(jsonParser, context)).thenReturn(new Object());

 when(asWrapperTypeDeserializer._findDeserializer(context, "TestTypeId")).thenReturn(deserializer);

 Object result = asWrapperTypeDeserializer.deserializeTypedFromAny(jsonParser, context);

 assertEquals("TestTypeValue", result.getClass().getSimpleName());
 assertNotNull(result);
 }
}