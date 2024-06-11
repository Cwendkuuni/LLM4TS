import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.ObjectIdReader.IdType;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectIdNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.ClassUtil;

import static org.junit.Assert.*;

public class ObjectIdValuePropertyTest {

 private ObjectIdReader mockObjectIdReader;
 private ObjectIdValueProperty objectIdValueProperty;
 private JsonDeserializer<Object> mockValueDeserializer;
 private JsonDeserializer<Object> mockIdDeserializer;
 private DeserializationContext mockContext;
 private JsonParser mockJsonParser;

 @Before
 public void setup() {
 mockObjectIdReader = mock(ObjectIdReader.class);
 mockValueDeserializer = mock(JsonDeserializer.class);
 mockIdDeserializer = mock(JsonDeserializer.class);
 mockContext = mock(DeserializationContext.class);
 mockJsonParser = mock(JsonParser.class);

 objectIdValueProperty = new ObjectIdValueProperty(mockObjectIdReader, null) {
 @Override
 public JsonDeserializer<?> getValueDeserializer() {
 return mockValueDeserializer;
 }

 @Override
 public JsonDeserializer<?> getIdDeserializer() {
 return mockIdDeserializer;
 }
 };
 }

 @Test
 public void testDeserializeAndSetWithoutId() throws Exception {
 // Test case: deserialize and set without id
 when(mockJsonParser.getValueAsToken()).thenReturn(null);
 when(mockContext.findObjectId(any(), any(), any())).thenReturn(null);
 when(mockObjectIdReader.getIdProperty()).thenReturn(null);

 objectIdValueProperty.deserializeAndSet(mockJsonParser, mockContext, new Foo());
 verifyNoMoreInteractions(mockContext);
 verify(mockJsonParser, times(1)).nextToken();
 }

 @Test
 public void testDeserializeAndSetWithNullId() throws Exception {
 // Test case: deserialize and set with null id
 when(mockJsonParser.getValueAsToken()).thenReturn(null);
 when(mockContext.findObjectId(any(), any(), any())).thenReturn(null);
 when(mockObjectIdReader.getIdProperty()).thenReturn(mock(SettableBeanProperty.class));

 Object id = null;
 Object result = objectIdValueProperty.deserializeSetAndReturn(mockJsonParser, mockContext, new Foo());
 assertEquals(id, result);
 }

 @Test
 public void testDeserializeAndSetWithValidId() throws Exception {
 // Test case: deserialize and set with valid id
 when(mockJsonParser.getValueAsToken()).thenReturn(ObjectIdNode.VALUE_STRING);
 when(mockContext.findObjectId(any(), any(), any())).thenReturn(new ObjectId("1"));
 when(mockObjectIdReader.getIdProperty()).thenReturn(mock(SettableBeanProperty.class));
 when(mockValueDeserializer.deserialize(mockJsonParser, mockContext)).thenReturn("123");

 Object id = new ObjectId("1");
 Object result = objectIdValueProperty.deserializeSetAndReturn(mockJsonParser, mockContext, new Foo());
 assertEquals(id, result);
 }

 @Test
 public void testSetWithoutId() throws Exception {
 // Test case: set without id
 Object instance = new Foo();
 Object id = "123";

 objectIdValueProperty.set(instance, id);
 verify(mockObjectIdReader.getIdProperty()).setAndReturn(instance, id);
 }

 @Test
 public void testSetWithNullId() throws Exception {
 // Test case: set with null id
 Object instance = new Foo();
 Object id = null;

 try {
 objectIdValueProperty.set(instance, id);
 fail();
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @Test
 public void testSetWithValidId() throws Exception {
 // Test case: set with valid id
 Object instance = new Foo();
 Object id = new ObjectId("1");
 SettableBeanProperty idProp = mock(SettableBeanProperty.class);

 when(mockObjectIdReader.getIdProperty()).thenReturn(idProp);

 objectIdValueProperty.set(instance, id);
 verify(idProp).setAndReturn(instance, id);
 }

 @Test
 public void testValueDeserializer() {
 // Test case: value deserializer
 ObjectIdValueProperty property = new ObjectIdValueProperty(mockObjectIdReader, null) {
 @Override
 public JsonDeserializer<Object> getValueDeserializer() {
 return new StdSerializer<Object>(Object.class) {};
 }
 };

 assertEquals(StdSerializer.class, property.getValueDeserializer().getClass());
 }
}

class Foo {

 private ObjectId id;

 public ObjectId getId() {
 return id;
 }

 public void setId(ObjectId id) {
 this.id = id;
 }
}