import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.base.PropertyName;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.ObjectIdReader;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ObjectIdValuePropertyTest {

 private ObjectMapper objectMapper;
 private ObjectIdValueProperty property;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 AnnotationIntrospector introspector = new AnnotationIntrospector();
 ObjectIdReader objectIdReader = new ObjectIdReader(PropertyName.construct("id"), ObjectIdType.STRING, introspector);
 property = new ObjectIdValueProperty(objectIdReader, new StandardPropertyMetadata(null));
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 ObjectNode jsonNode = ObjectMapperTestUtils.createObjectNode("{\"id\":1}");
 Object id = property.deserializeAndSet(objectMapper.getFactory().createParser(jsonNode), objectMapper.getDeserializationContext(), new Object());
 assertEquals(1, id);
 }

 @Test
 public void testDeserializeSetAndReturn() throws IOException {
 ObjectNode jsonNode = ObjectMapperTestUtils.createObjectNode("{\"id\":1}");
 Object id = property.deserializeSetAndReturn(objectMapper.getFactory().createParser(jsonNode), objectMapper.getDeserializationContext(), new Object());
 assertEquals(1, id);
 assertEquals(id, property.get(new Object()));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSet() throws Exception {
 property.set(new Object(), 1);
 fail("Should not be able to call set() on ObjectIdValueProperty");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetAndReturn() throws Exception {
 Object newObject = new Object();
 property.setAndReturn(newObject, 1);
 fail("Should not be able to call setAndReturn() on ObjectIdValueProperty");
 }

 @Test
 public void testNullId() throws IOException {
 ObjectNode jsonNode = ObjectMapperTestUtils.createObjectNode("{}");
 Object id = property.deserializeAndSet(objectMapper.getFactory().createParser(jsonNode), objectMapper.getDeserializationContext(), new Object());
 assertNull(id);
 }
}