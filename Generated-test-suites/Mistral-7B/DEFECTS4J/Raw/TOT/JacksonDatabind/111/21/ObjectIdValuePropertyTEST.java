import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNodeDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.ObjectIdReader;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import static com.fasterxml.jackson.databind.node.TextNode.value;
import static org.junit.Assert.*;

public class ObjectIdValuePropertyTest {

 private ObjectMapper mapper;
 private ObjectIdValueProperty property;
 private ObjectIdReader objectIdReader;
 private JsonParser parser;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 objectIdReader = mock(ObjectIdReader.class);
 parser = mock(JsonParser.class);
 property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STD_OBJ);
 }

 @Test
 public void testDeserializeAndSetNullId() throws Exception {
 when(parser.nextToken()).thenReturn(JsonToken.VALUE_NULL);
 Object instance = new Object();
 property.deserializeAndSet(parser, mapper.getContext(), instance);
 assertNull(instance);
 }

 @Test
 public void testDeserializeAndSetId() throws Exception {
 String id = "123";
 when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(parser.getValueAsString()).thenReturn(id);
 Object instance = new Object();
 property.deserializeAndSet(parser, mapper.getContext(), instance);
 ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
 ObjectIdGenerator generator = mock(ObjectIdGenerator.class);
 ObjectIdResolver resolver = mock(ObjectIdResolver.class);
 when(objectIdReader.generator).thenReturn(generator);
 when(objectIdReader.resolver).thenReturn(resolver);
 Object id = mapper.valueToTree(id).get("id").asInt();
 ReadableObjectId roid = mock(ReadableObjectId.class);
 when(generator.generateId()).thenReturn(id);
 when(resolver.resolve(eq(id), eq(objectIdReader))).thenReturn(roid);
 when(roid.bindItem(instance)).thenReturn(instance);
 property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STD_OBJ);
 property.deserializeAndSet(parser, mapper.getContext(), instance);
 assertSame(instance, roid);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetNullValue() throws Exception {
 Object instance = new Object();
 property.set(instance, null);
 fail("Should throw UnsupportedOperationException");
 }

 @Test
 public void testSetValue() throws Exception {
 Object id = new Object();
 Object instance = new Object();
 property.set(instance, id);
 property = new ObjectIdValueProperty(property, new JsonDeserializer.NullValueProvider());
 property.set(instance, null);
 assertNull(instance.getClass().getDeclaredField("id").get(instance));
 }

 @Test
 public void testGetAnnotation() {
 Annotation annotation = property.getAnnotation(MyAnnotation.class);
 assertNull(annotation);
 }

 @Test
 public void testGetMember() {
 AnnotatedMember member = property.getMember();
 assertNull(member);
 }

 @Test
 public void testDeserializeSetAndReturnNullId() throws Exception {
 when(parser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);
 Object instance = new Object();
 Object result = property.deserializeSetAndReturn(parser, mapper.getContext(), instance);
 assertNull(result);
 }

 @Test
 public void testDeserializeSetAndReturnId() throws Exception {
 Object id = new Object();
 JsonNodeDeserializer deserializer = mock(JsonNodeDeserializer.class);
 when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(parser.getValueAsString()).thenReturn("{\"id\": \"" + id.toString() + "\"}");
 when(deserializer.deserialize(parser, mapper.getContext(), Object.class)).thenReturn(id);
 when(property._valueDeserializer).thenReturn(deserializer);
 Object instance = new Object();
 Object result = property.deserializeSetAndReturn(parser, mapper.getContext(), instance);
 assertSame(instance, result);
 }

 @Test
 public void testSetAndReturnNullValue() throws Exception {
 Object instance = new Object();
 Object result = property.setAndReturn(instance, null);
 assertNull(result);
 }

 @Test
 public void testSetAndReturnValue() throws Exception {
 Object id = new Object();
 Object instance = new Object();
 Object result = property.setAndReturn(instance, id);
 assertEquals(id, result);
 }
}