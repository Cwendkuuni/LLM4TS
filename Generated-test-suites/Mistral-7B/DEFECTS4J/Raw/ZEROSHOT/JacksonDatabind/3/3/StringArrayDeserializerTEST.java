import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectBuffer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.TypeDeserializer;
import com.fasterxml.jackson.databind.json.JsonToken;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StringArrayDeserializerTest {

 private ObjectMapper objectMapper;
 private JsonDeserializer<String> stringDeserializer;
 private StringArrayDeserializer stringArrayDeserializer;
 private DeserializationContext context;
 private ObjectBuffer objectBuffer;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 stringDeserializer = mock(JsonDeserializer.class);
 stringArrayDeserializer = new StringArrayDeserializer();
 context = mock(DeserializationContext.class);
 objectBuffer = mock(ObjectBuffer.class);
 }

 @Test
 public void testDeserializeNullElementDeserializer() throws Exception {
 String[] result = stringArrayDeserializer.deserialize(mockJsonParser(), context);
 assertArrayEquals(new String[0], result);
 }

 @Test
 public void testDeserializeWithNullElementDeserializer() throws Exception {
 JsonDeserializer<String> nullDeserializer = null;
 StringArrayDeserializer deserializerWithNullElementDeserializer = new StringArrayDeserializer(nullDeserializer);
 String[] result = deserializerWithNullElementDeserializer.deserialize(mockJsonParser(), context);
 assertArrayEquals(new String[0], result);
 }

 @Test(expected = IOException.class)
 public void testDeserializeWithoutStartArrayToken() throws Exception {
 when(mockJsonParser().nextToken()).thenReturn(JsonToken.VALUE_STRING);
 stringArrayDeserializer.deserialize(mockJsonParser(), context);
 }

 @Test(expected = IOException.class)
 public void testDeserializeWithInvalidToken() throws Exception {
 when(mockJsonParser().nextToken()).thenReturn(JsonToken.VALUE_NUMBER);
 stringArrayDeserializer.deserialize(mockJsonParser(), context);
 }

 @Test(expected = IOException.class)
 public void testDeserializeWithEndArrayToken() throws Exception {
 when(mockJsonParser().nextToken()).thenReturn(JsonToken.END_ARRAY);
 stringArrayDeserializer.deserialize(mockJsonParser(), context);
 }

 @Test
 public void testDeserializeCustomElementDeserializer() throws Exception {
 when(mockJsonParser().nextToken()).thenReturn(JsonToken.START_ARRAY);
 when(stringDeserializer.deserialize(any(JsonParser.class), any(DeserializationContext.class))).thenReturn("test");
 String[] result = stringArrayDeserializer.deserialize(mockJsonParser(), context);
 assertArrayEquals(new String[]{"test"}, result);
 }

 @Test
 public void testDeserializeWithNullValue() throws Exception {
 when(mockJsonParser().nextToken()).thenReturn(JsonToken.VALUE_NULL);
 String[] result = stringArrayDeserializer.deserialize(mockJsonParser(), context);
 assertNull(result);
 }

 @Test
 public void testDeserializeWithEmptyArray() throws Exception {
 when(mockJsonParser().nextToken()).thenReturn(JsonToken.START_ARRAY);
 when(mockJsonParser().nextToken()).thenReturn(JsonToken.END_ARRAY);
 String[] result = stringArrayDeserializer.deserialize(mockJsonParser(), context);
 assertArrayEquals(new String[0], result);
 }

 @Test
 public void testDeserializeWithOneElement() throws Exception {
 when(mockJsonParser().nextToken()).thenReturn(JsonToken.START_ARRAY);
 when(mockJsonParser().nextToken()).thenReturn(JsonToken.VALUE_STRING, JsonToken.END_ARRAY);
 when(mockJsonParser().getText()).thenReturn("test");
 String[] result = stringArrayDeserializer.deserialize(mockJsonParser(), context);
 assertArrayEquals(new String[]{"test"}, result);
 }

 @Test
 public void testDeserializeWithSeveralElements() throws Exception {
 when(mockJsonParser().nextToken()).thenReturn(JsonToken.START_ARRAY);
 when(mockJsonParser().nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(mockJsonParser().getText(anyInt())).thenReturnArg(0, "test1");
 when(mockJsonParser().nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(mockJsonParser().getText(anyInt())).thenReturnArg(1, "test2");
 when(mockJsonParser().nextToken()).thenReturn(JsonToken.END_ARRAY);
 String[] result = stringArrayDeserializer.deserialize(mockJsonParser(), context);
 assertArrayEquals(new String[]{"test1", "test2"}, result);
 }

 @Test
 public void testContextualizeWithNullElementDeserializer() throws Exception {
 StringArrayDeserializer deserializerWithNullElementDeserializer = new StringArrayDeserializer(null);
 assertEquals(StringArrayDeserializer.class, deserializerWithNullElementDeserializer.createContextual(context, null).getClass());
 }

 @Test
 public void testContextualizeWithNullProperty() throws Exception {
 assertEquals(StringArrayDeserializer.class, stringArrayDeserializer.createContextual(context, null).getClass());
 }

 @Test
 public void testContextualizeWithSingleElementDeserializer() throws Exception {
 stringDeserializer = Mockito.spy(new StringDeserializer());
 when(mockJsonParser().nextToken()).thenReturn(JsonToken.START_ARRAY);
 when(mockJsonParser().nextToken()).thenReturn(JsonToken.VALUE_NULL);
 StringArrayDeserializer deserializerWithSingleElementDeserializer = new StringArrayDeserializer(stringDeserializer);
 JsonDeserializer<?> result = deserializerWithSingleElementDeserializer.createContextual(context, null);
 assertEquals(StringArrayDeserializer.class, result.getClass());
 assertEquals(StringDeserializer.class, result.getNullValueDeserializer().getClass());
 }

 @Test
 public void testContextualizeWithArrayElementDeserializer() throws Exception {
 stringDeserializer = Mockito.spy(new StringDeserializer());
 when(mockJsonParser().nextToken()).thenReturn(JsonToken.START_ARRAY);
 when(mockJsonParser().nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(mockJsonParser().getText()).thenReturn("test");
 StringArrayDeserializer deserializerWithArrayElementDeserializer = new StringArrayDeserializer(stringDeserializer);
 JsonDeserializer<?> result = deserializerWithArrayElementDeserializer.createContextual(context, null);
 assertEquals(StringArrayDeserializer.class, result.getClass());
 assertEquals(StringDeserializer.class, result.getElementDeserializer().getClass());
 }

 @Test
 public void testContextualizeWithContentConverter() throws Exception {
 StringContentConverter contentConverter = mock(StringContentConverter.class);
 when(context.findValueDeserializer(String[].class, null, StringContentConverter.class)).thenReturn(contentConverter);
 StringArrayDeserializer deserializerWithContentConverter = new StringArrayDeserializer();
 JsonDeserializer<?> result = deserializerWithContentConverter.createContextual(context, null);
 assertEquals(StringArrayDeserializer.class, result.getClass());
 assertEquals(contentConverter.getClass(), result.getContentConverter().getClass());
 }

 @Test
 public void testContextualizeWithTypeDeserializer() throws Exception {
 TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
 when(mockJsonParser().nextToken()).thenReturn(JsonToken.START_ARRAY);
 when(mockJsonParser().nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(mockJsonParser().getText()).thenReturn("test");
 when(context.findValueDeserializer(String.class, null)).thenReturn(stringDeserializer);
 when(context.findTypeDeserializer(String[].class, null)).thenReturn(typeDeserializer);
 StringArrayDeserializer deserializerWithTypeDeserializer = new StringArrayDeserializer();
 Object[] result = deserializerWithTypeDeserializer.deserializeWithType(mockJsonParser(), context, typeDeserializer);
 assertArrayEquals(new String[]{"test"}, (String[]) result);
 }

 private JsonParser mockJsonParser() {
 return Mockito.mock(JsonParser.class);
 }
}