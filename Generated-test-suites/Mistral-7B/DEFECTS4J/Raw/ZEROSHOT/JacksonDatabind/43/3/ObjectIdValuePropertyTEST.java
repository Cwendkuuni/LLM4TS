import com.fasterxml.jackson.core.base.JsonSubType;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectIdReader;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class ObjectIdValuePropertyTest {

 private ObjectIdReader objectIdReader;
 private ObjectIdValueProperty objectIdValueProperty;

 @Before
 public void setup() {
 objectIdReader = mock(ObjectIdReader.class);
 objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 }

 @Test
 public void testWithName() {
 PropertyName newName = PropertyName.construct("newName");
 ObjectIdValueProperty newObjectIdValueProperty = objectIdValueProperty.withName(newName);
 assertEquals(newName, newObjectIdValueProperty.getName());
 }

 @Test
 public void testWithValueDeserializer() {
 JsonDeserializer<String> jsonDeserializer = mock(JsonDeserializer.class);
 ObjectIdValueProperty newObjectIdValueProperty = objectIdValueProperty.withValueDeserializer(jsonDeserializer);
 assertEquals(jsonDeserializer, newObjectIdValueProperty.getValueDeserializer());
 }

 @Test
 public void testGetAnnotation() {
 Class<Annotation> acls = mock(Class.class);
 assertNull(objectIdValueProperty.getAnnotation(acls));
 }

 @Test
 public void testGetMember() {
 assertNull(objectIdValueProperty.getMember());
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 ObjectIdReader generator = mock(ObjectIdReader.class);
 ObjectIdReader resolver = mock(ObjectIdReader.class);
 JsonParser jsonParser = mock(JsonParser.class);
 DeserializationContext context = mock(DeserializationContext.class);
 Object instance = new Object();
 Object id = "idValue";
 ObjectId idObject = new ObjectId(id);
 when(objectIdReader.getIdType()).thenReturn(idObject.getClass());
 when(objectIdReader.generator).thenReturn(generator);
 when(objectIdReader.resolver).thenReturn(resolver);
 when(generator.getObjectIdFactory()).thenReturn(mock(ObjectIdFactory.class));
 when(resolver.findClass(anyString())).thenReturn(Object.class);

 objectIdValueProperty.deserializeAndSet(jsonParser, context, instance);

 verify(instance).setId(idObject);
 }

 @Test
 public void testDeserializeSetAndReturn() throws IOException {
 ObjectIdReader generator = mock(ObjectIdReader.class);
 ObjectIdReader resolver = mock(ObjectIdReader.class);
 JsonParser jsonParser = mock(JsonParser.class);
 DeserializationContext context = mock(DeserializationContext.class);
 Object instance = new Object();
 Object id = "idValue";
 ObjectId idObject = new ObjectId(id);
 when(objectIdReader.getIdType()).thenReturn(idObject.getClass());
 when(objectIdReader.generator).thenReturn(generator);
 when(objectIdReader.resolver).thenReturn(resolver);
 when(generator.getObjectIdFactory()).thenReturn(mock(ObjectIdFactory.class));
 when(resolver.findClass(anyString())).thenReturn(Object.class);
 when(jsonParser.getValueAsToken()).thenReturn(JsonToken.VALUE_STRING);
 when(jsonParser.nextValue()).thenReturn(id);

 Object result = objectIdValueProperty.deserializeSetAndReturn(jsonParser, context, instance);

 assertEquals(instance, result);
 }

 @Test
 public void testSet() throws IOException {
 Object instance = new Object();
 Object id = "idValue";
 ObjectId idObject = new ObjectId(id);

 objectIdValueProperty.set(instance, idObject);

 verify(objectIdValueProperty.getBeanProperty()).set(instance, idObject);
 }

 @Test
 public void testSetAndReturn() throws IOException {
 Object instance = new Object();
 Object id = "idValue";
 ObjectId idObject = new ObjectId(id);
 SettableBeanProperty idProp = mock(SettableBeanProperty.class);
 when(objectIdReader.idProperty).thenReturn(idProp);
 when(idProp.setAndReturn(instance, idObject)).thenReturn(instance);

 Object result = objectIdValueProperty.setAndReturn(instance, idObject);

 assertEquals(instance, result);
 }
}