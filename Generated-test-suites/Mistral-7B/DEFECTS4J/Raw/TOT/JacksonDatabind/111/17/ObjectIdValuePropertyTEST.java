import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectIdReader;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdValuePropertyTest {

 private ObjectMapper mapper;
 private ObjectIdReader objectIdReader;
 private ObjectIdValueProperty objectIdValueProperty;
 private Object idInstance;

 @Before
 public void setup() throws JsonMappingException {
 mapper = new ObjectMapper();
 objectIdReader = new SimpleObjectIdReader("id");
 objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STD_OBJ);
 idInstance = new Object() {{}};
 }

 @Test
 public void testDeserializeAndSetNullId() throws IOException {
 JsonParser parser = mapper.getFactory().createParser("null");
 Object result = objectIdValueProperty.deserializeAndSet(parser, mapper.getDeserializationContext(), idInstance);
 assertNull(result);
 assertEquals(null, idInstance);
 }

 @Test
 public void testDeserializeAndSetValidId() throws IOException {
 ObjectNode node = mapper.createObjectNode();
 node.put("id", 1L);
 JsonParser parser = mapper.getFactory().createParser(node.toString());
 Object result = objectIdValueProperty.deserializeAndSet(parser, mapper.getDeserializationContext(), idInstance);
 assertEquals(1L, idInstance.getClass().getDeclaredField("id").get(idInstance));
 }

 @Test
 public void testSet() throws IOException {
 ObjectNode node = mapper.createObjectNode();
 node.put("id", 2L);
 Object value = node.get("id").numberValue();
 objectIdValueProperty.set(idInstance, value);
 assertEquals(2L, idInstance.getClass().getDeclaredField("id").get(idInstance));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetWithoutIdProperty() throws IOException {
 ObjectIdValueProperty objectIdValuePropertyWithoutIdProperty = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STD_OBJ) {
 @Override
 public SettableBeanProperty idProperty() {
 return null;
 }
 };
 objectIdValuePropertyWithoutIdProperty.set(idInstance, 3L);
 }

 @Test
 public void testAnnotations() throws IOException {
 ObjectNode node = mapper.createObjectNode();
 node.put("id", new ObjectNode(mapper.getNodeFactory()).putPOJO("idValue", new Object()));
 ObjectNode annotationsNode = node.putPOJO("@SomeAnnotation", mapper.createObjectNode());
 AnnotationIntrospector introspector = mapper.getAnnotationIntrospector();
 ObjectIdValueProperty property = mapper.findPropertyValueDeserializer(node, ObjectIdValueProperty.class, introspector);
 assertNotNull(property);
 Object annotation = introspector.findAnnotation(property, SomeAnnotation.class.getName());
 assertNotNull(annotation);
 }

 @Test
 public void testCustomSerializer() throws IOException {
 ObjectIdReader objectIdReaderWithCustomSerializer = new ObjectIdReader("id") {
 @Override
 public JsonDeserializer<?> getDeserializer() {
 return new StdSerializer<Long>(Long.class) {
 @Override
 public Long deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 Long id = p.getValueAsLong();
 if (id == 4L) {
 throw new IOException("Invalid id");
 }
 return id;
 }
 };
 }
 };
 ObjectIdValueProperty propertyWithCustomSerializer = new ObjectIdValueProperty(objectIdReaderWithCustomSerializer, PropertyMetadata.STD_OBJ);
 ObjectNode node = mapper.createObjectNode();
 node.put("id", 5L);
 JsonParser parser = mapper.getFactory().createParser(node.toString());
 try {
 propertyWithCustomSerializer.deserializeAndSet(parser, mapper.getDeserializationContext(), idInstance);
 fail();
 } catch (IOException e) {
 // expected
 }
 assertEquals(5L, idInstance.getClass().getDeclaredField("id").get(idInstance));
 }
}