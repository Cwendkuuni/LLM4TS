import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdReferencePropertyTest {

 private ObjectIdInfo objectIdInfo;
 private SettableBeanProperty forward;
 private ObjectIdReferenceProperty objectIdReferenceProperty;
 private JsonDeserializer<String> deserializer;

 @Before
 public void setup() throws IOException {
 objectIdInfo = new ObjectIdInfo(null, null, null, null);
 forward = new SimpleBeanProperty.Standard("forward", String.class);
 deserializer = new StandardDeserializer<>(String.class);
 objectIdReferenceProperty = new ObjectIdReferenceProperty(forward, objectIdInfo);
 }

 @Test
 public void testDefaultConstruction() {
 ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(null, null);
 assertNull(property._forward);
 assertNull(property._objectIdInfo);
 }

 @Test
 public void testCopyConstructionWithValueDeserializer() throws IOException {
 ObjectIdReferenceProperty copy = new ObjectIdReferenceProperty(objectIdReferenceProperty, deserializer, null);
 assertEquals(objectIdReferenceProperty, copy);
 }

 @Test
 public void testCopyConstructionWithPropertyName() throws IOException {
 ObjectIdReferenceProperty copy = new ObjectIdReferenceProperty(objectIdReferenceProperty, null, new PropertyName("newName"));
 assertEquals(objectIdReferenceProperty, copy);
 }

 @Test
 public void testWithName() {
 ObjectIdReferenceProperty newProperty = objectIdReferenceProperty.withName(new PropertyName("newName"));
 assertEquals("newName", newProperty.getName().getName());
 }

 @Test
 public void testWithValueDeserializer() {
 JsonDeserializer<Integer> integerDeserializer = new StandardDeserializer<>(Integer.class);
 ObjectIdReferenceProperty newProperty = objectIdReferenceProperty.withValueDeserializer(integerDeserializer);
 assertEquals(integerDeserializer, newProperty.getValueDeserializer());
 }

 @Test
 public void testWithNullProvider() {
 NullValueProvider nullValueProvider = new StandardNullValueProvider();
 ObjectIdReferenceProperty newProperty = objectIdReferenceProperty.withNullProvider(nullValueProvider);
 assertEquals(nullValueProvider, newProperty.getNullProvider());
 }

 @Test
 public void testFixAccess() throws IOException {
 Object id = new Object();
 JsonParser parser = new ObjectMapper().getFactory().createParser("{\"id\": \"id\"}");
 Object context = new Object();
 Object instance = new Object();
 objectIdReferenceProperty.fixAccess(new DeserializationConfig());
 objectIdReferenceProperty.deserializeSetAndReturn(parser, new DeserializationContext(parser, new ObjectMapper()), instance);
 assertEquals(id, instance);
 }

 @Test
 @JsonDeserialize(using = TestClassDeserializer.class)
 public static class TestClass {
 @JsonProperty("idReference")
 private ObjectIdReferenceProperty idReference;
 }

 @Test
 public void testSerializationAndDeserialization() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 Object id = new Object();
 TestClass testClass = new TestClass();
 ObjectIdReferenceProperty idReference = new ObjectIdReferenceProperty(null, null);
 idReference.set(testClass, id);
 mapper.registerModule(new SimpleModule().addDeserializer(TestClass.class, new TestClassDeserializer()));
 String json = mapper.writeValueAsString(testClass);
 TestClass deserializedTestClass = mapper.readValue(json, TestClass.class);
 assertEquals(id, deserializedTestClass.getIdReference().get(deserializedTestClass));
 }

 @Test(expected = UnresolvedForwardReference.ClassNotFoundException.class)
 public void testUnresolvedForwardReference() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 Object id = new Object();
 TestClass testClass = new TestClass();
 ObjectIdReferenceProperty idReference = new ObjectIdReferenceProperty(null, null);
 idReference.set(testClass, id);
 mapper.registerModule(new SimpleModule().addDeserializer(TestClass.class, new TestClassDeserializer()));
 String json = mapper.writeValueAsString(testClass);
 JsonParser parser = mapper.getFactory().createParser(json);
 Object context = new Object();
 Object instance = new Object();
 ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(null, null);
 property.deserializeSetAndReturn(parser, new DeserializationContext(parser, new ObjectMapper()), instance);
 fail("Unresolved forward reference exception not thrown");
 }

 @Test
 public void testPropertyReferring() throws IOException {
 Object id = new Object();Object idReference1 = new ObjectIdReferenceProperty(null, null);Object idReference2 = new ObjectIdReferenceProperty(null, null);Object pojo = new Object();
 idReference1.set(pojo, id);
 ObjectIdReferenceProperty.PropertyReferring referencing1 = new ObjectIdReferenceProperty.PropertyReferring(idReference1, null, Object.class, pojo);
 idReference2.set(pojo, id);
 ObjectIdReferenceProperty.PropertyReferring referencing2 = new ObjectIdReferenceProperty.PropertyReferencing(idReference2, null, Object.class, pojo);
 referencing1.handleResolvedForwardReference(id, id);
 assertEquals(id, pojo);
 }

 @Test
 public void testGetAnnotation() {
 assertNull(objectIdReferenceProperty.getAnnotation(Annotation.class));
 }

 @Test
 public void testGetMember() {
 assertNotNull(objectIdReferenceProperty.getMember());
 }

 @Test
 public void testGetCreatorIndex() {
 assertEquals(-1, objectIdReferenceProperty.getCreatorIndex());
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 Object id = new Object();
 ObjectMapper mapper = new ObjectMapper();
 TestClass testClass = new TestClass();
 ObjectIdReferenceProperty idReference = new ObjectIdReferenceProperty(null, null);
 idReference.set(testClass, id);
 mapper.registerModule(new SimpleModule().addDeserializer(TestClass.class, new TestClassDeserializer()));
 String json = mapper.writeValueAsString(testClass);
 ObjectParser parser = mapper.readerForUpdating(TestClass.class);
 parser.readValue(json);
 assertEquals(id, testClass.getIdReference().get(testClass));
 }

 @Test
 public void testDeserializeSetAndReturn() throws IOException {
 Object id = new Object();
 ObjectMapper mapper = new ObjectMapper();
 TestClass testClass = new TestClass();
 ObjectIdReferenceProperty idReference = new ObjectIdReferenceProperty(null, null);
 idReference.set(testClass, id);
 Object result = idReference.deserializeSetAndReturn(mapper.getFactory().createParser("{}"), new DeserializationContext(mapper.getFactory().createParser("{}"), mapper), new Object());
 assertNull(result);
 }
}

class TestClassDeserializer extends JsonDeserializer<TestClass> {
 @Override
 public TestClass deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 TestClass testClass = new TestClass();
 ObjectIdReferenceProperty idReference = new ObjectIdReferenceProperty(null, null);
 idReference.deserializeAndSet(p, ctxt, testClass);
 testClass.setIdReference(idReference);
 return testClass;
 }
}