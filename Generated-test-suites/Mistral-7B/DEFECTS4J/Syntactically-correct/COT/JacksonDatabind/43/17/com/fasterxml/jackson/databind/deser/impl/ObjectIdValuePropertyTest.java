import static org.junit.Assert.*;

import com.fasterxml.jackson.core.base.JsonLocation;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.json.JsonFactory;
import com.fasterxml.jackson.databind.util.LoginName;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ObjectIdValuePropertyTest {

 private ObjectMapper mapper;
 private ObjectIdReader objectIdReader;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 objectIdReader = new SimpleObjectIdReader(new JsonFactory(), null, null, null);
 }

 @After
 public void cleanup() throws Exception {
 mapper = null;
 objectIdReader = null;
 }

 // Test for constructor
 @Test
 public void testConstructor() {
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 assertNotNull(property);
 }

 // Test for deserializeAndSet method
 @Test
 public void testDeserializeAndSet() throws Exception {
 Object id = new LoginName("123").getText();
 Object idValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata()) {
 @JsonDeserialize
 private ObjectId id;
 };
 Object idInstance = new Object();

 Object result = idValueProperty.deserializeAndSet(mapper.getFactory().createParser("{\"id\":\"" + id + "\"}"), mapper.getDeserializationContext(), idInstance);
 assertNotNull(result);

 Object idProperty = mapper.findValueToAccess(idInstance, idValueProperty);
 assertEquals(id, idProperty);
 }

 // Test for deserializeSetAndReturn method
 @Test
 public void testDeserializeSetAndReturn() throws Exception {
 Object id = new LoginName("123").getText();
 Object idValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata()) {
 @JsonDeserialize
 private ObjectId id;
 };
 Object idInstance = new Object();

 Object result = idValueProperty.deserializeSetAndReturn(mapper.getFactory().createParser("{\"id\":\"" + id + "\"}"), mapper.getDeserializationContext(), idInstance);
 assertNotNull(result);

 Object idProperty = mapper.findValueToAccess(idInstance, idValueProperty);
 assertEquals(id, idProperty);

 assertEquals(idInstance, result);
 }

 // Test for set method
 @Test
 public void testSet() throws Exception {
 Object id = new LoginName("123").getText();
 Object idValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata()) {
 @JsonDeserialize
 private ObjectId id;
 };
 Object idInstance = new Object();

 idValueProperty.set(idInstance, id);
 Object idProperty = mapper.findValueToAccess(idInstance, idValueProperty);
 assertEquals(id, idProperty);
 }

 // Test for setAndReturn method
 @Test
 public void testSetAndReturn() throws Exception {
 Object id = new LoginName("123").getText();
 Object idValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata()) {
 @JsonDeserialize
 private ObjectId id;
 };
 Object idInstance = new Object();

 Object result = idValueProperty.setAndReturn(idInstance, id);
 assertEquals(idInstance, result);

 Object idProperty = mapper.findValueToAccess(idInstance, idValueProperty);
 assertEquals(id, idProperty);
 }

 // Test for deserializeAndSet method with null id
 @Test
 public void testDeserializeAndSetNullId() throws Exception {
 Object idValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata()) {
 @JsonDeserialize
 private ObjectId id;
 };
 Object idInstance = new Object();

 idValueProperty.deserializeAndSet(mapper.getFactory().createParser("{}"), mapper.getDeserializationContext(), idInstance);
 assertNull(mapper.findValueToAccess(idInstance, idValueProperty));
 }

 // Test for deserializeSetAndReturn method with null id
 @Test (expected = UnsupportedOperationException.class)
 public void testDeserializeSetAndReturnNullId() throws Exception {
 Object idValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata()) {
 @JsonDeserialize
 private ObjectId id;
 };
 Object idInstance = new Object();

 Object result = idValueProperty.deserializeSetAndReturn(mapper.getFactory().createParser("{}"), mapper.getDeserializationContext(), idInstance);
 assertNull(mapper.findValueToAccess(idInstance, idValueProperty));

 idValueProperty.setAndReturn(idInstance, null);
 }
}