import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdValueProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import static org.junit.Assert.*;

public class ObjectIdValuePropertyTest {

 private ObjectIdReader objectIdReader;
 private ObjectIdValueProperty objectIdValueProperty;

 @Before
 public void setup() {
 objectIdReader = Mockito.mock(ObjectIdReader.class);
 objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new EmptyStandardSerializer(), null);
 }

 @Test
 public void testConstructor() {
 assertNotNull(objectIdValueProperty);
 assertEquals(objectIdReader.propertyName, objectIdValueProperty.getName());
 }

 @Test
 public void testWithName() {
 PropertyName newName = PropertyName.construct("newName");
 ObjectIdValueProperty newObjectIdValueProperty = objectIdValueProperty.withName(newName);
 assertEquals(newName, newObjectIdValueProperty.getName());
 }

 @Test
 public void testWithValueDeserializer() {
 JsonDeserializer<String> deserializer = Mockito.mock(JsonDeserializer.class);
 ObjectIdValueProperty newObjectIdValueProperty = objectIdValueProperty.withValueDeserializer(deserializer);
 assertEquals(deserializer, newObjectIdValueProperty.getValueDeserializer());
 }

 @Test
 public void testGetAnnotation() {
 assertNull(objectIdValueProperty.getAnnotation(Mockito.mock(Annotation.class).getClass()));
 }

 @Test
 public void testGetMember() {
 assertNull(objectIdValueProperty.getMember());
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 String id = "123";
 JsonParser parser = Mockito.mock(JsonParser.class);
 Object idInstance = Mockito.mock(Object.class);
 DeserializationContext context = Mockito.mock(DeserializationContext.class);
 Mockito.when(parser.getValueAsString()).thenReturn(id);
 Mockito.when(context.findObjectId(Mockito.eq(id), Mockito.any(ReadableObjectId.class), Mockito.any(ObjectIdResolver.class))).thenReturn(Mockito.mock(ReadableObjectId.class));

 objectIdValueProperty.deserializeAndSet(parser, context, idInstance);

 ReadableObjectId readableObjectId = Mockito.argumentCaptor(ReadableObjectId.class).getValue();
 assertEquals(id, readableObjectId.getIdValueAsString());
 }

 @Test
 public void testDeserializeSetAndReturn() throws IOException {
 String id = "123";
 Object idInstance = Mockito.mock(Object.class);
 DeserializationContext context = Mockito.mock(DeserializationContext.class);
 JsonParser parser = Mockito.mock(JsonParser.class);
 Object result = objectIdValueProperty.deserializeSetAndReturn(parser, context, idInstance);
 assertEquals(idInstance, result);
 }

 @Test
 public void testSet() throws IOException {
 Object idInstance = Mockito.mock(Object.class);
 Object id = "123";
 objectIdValueProperty.set(idInstance, id);
 assertNotNull(idInstance);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetWithoutIdProperty() throws IOException {
 Object idInstance = Mockito.mock(Object.class);
 ObjectIdValueProperty objectIdValuePropertyWithoutIdProperty = new ObjectIdValueProperty(objectIdReader, null);
 objectIdValuePropertyWithoutIdProperty.set(idInstance, "123");
 fail("Should throw UnsupportedOperationException");
 }
}