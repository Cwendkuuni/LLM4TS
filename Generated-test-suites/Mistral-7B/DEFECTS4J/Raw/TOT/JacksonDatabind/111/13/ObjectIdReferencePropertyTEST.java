import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReferenceProperty;
import com.fasterxml.jackson.databind.deser.impl.PropertyName;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdReferencePropertyTest {

 private ObjectIdReferenceProperty property;
 private JsonDeserializer<String> stringDeserializer;
 private JsonParser jsonParser;
 private ObjectMapper objectMapper;
 private ObjectNode testObject;

 @Before
 public void setup() throws Exception {
 stringDeserializer = new StandardDeserializer<String>(String.class);
 jsonParser = new ObjectMapper().getFactory().createParser("[{\"name\":\"test\", \"$ref\":\"#/properties/test\"}]");
 objectMapper = new ObjectMapper();
 testObject = objectMapper.readTree(jsonParser);
 property = new ObjectIdReferenceProperty(null, stringDeserializer, null);
 }

 @Test
 public void testWithName() throws Exception {
 PropertyName newName = new PropertyName("newName");
 ObjectIdReferenceProperty newProperty = property.withName(newName);
 assertEquals(newName, newProperty.getName());
 }

 @Test
 public void testWithValueDeserializer() throws Exception {
 JsonDeserializer<Integer> intDeserializer = new StandardDeserializer<Integer>(Integer.class);
 ObjectIdReferenceProperty newProperty = property.withValueDeserializer(intDeserializer);
 assertEquals(intDeserializer, newProperty.getValueDeserializer());
 }

 @Test
 public void testFixAccess() throws Exception {
 DeserializationConfig config = new DeserializationConfig();
 property.fixAccess(config);
 ObjectIdReferenceProperty fixedProperty = new ObjectIdReferenceProperty(property, null);
 assertSame(fixedProperty, fixedProperty.fixAccess(config));
 }

 @Test
 public void testGetAnnotation() throws Exception {
 Class<Annotation> annotationClass = Annotation.class;
 ObjectIdReferenceProperty propertyWithAnnotation = new ObjectIdReferenceProperty(null, null, null) {
 @Override
 public <A extends Annotation> A getAnnotation(Class<A> acls) {
 return (A) mock(aclc);
 }
 };
 Annotation annotation = propertyWithAnnotation.getAnnotation(annotationClass);
 assertNotNull(annotation);
 }

 @Test
 public void testGetMember() throws Exception {
 AnnotatedMember member = new AnnotatedMethod();
 ObjectIdReferenceProperty propertyWithMember = new ObjectIdReferenceProperty(null, null, null) {
 @Override
 public AnnotatedMember getMember() {
 return member;
 }
 };
 assertSame(member, propertyWithMember.getMember());
 }

 @Test
 public void testGetCreatorIndex() throws Exception {
 ObjectIdReferenceProperty propertyWithCreatorIndex = new ObjectIdReferenceProperty(null, null, null) {
 @Override
 public int getCreatorIndex() {
 return 0;
 }
 };
 assertEquals(0, propertyWithCreatorIndex.getCreatorIndex());
 }

 @Test
 public void testDeserializeAndSet() throws Exception {
 String value = "test";
 property.deserializeAndSet(jsonParser, new DeserializationContext(), null);
 ObjectIdReferenceProperty deserializedProperty = (ObjectIdReferenceProperty) jsonParser.readValueAs(ObjectIdReferenceProperty.class);
 assertEquals(value, deserializedProperty.getValueDeserializer().deserialize(jsonParser, new DeserializationContext(), null));
 }

 @Test
 public void testDeserializeSetAndReturn() throws Exception {
 String value = "test";
 Object pojo = new Object();
 property.deserializeSetAndReturn(jsonParser, new DeserializationContext(), pojo);
 assertEquals(value, property.get(pojo));
 }

 @Test(expected = UnresolvedForwardReference.class)
 public void testUnresolvedForwardReference() throws Exception {
 Object pojo = new Object();property.deserializeSetAndReturn(jsonParser, new DeserializationContext(), pojo);
 fail("Expected UnresolvedForwardReference exception");
 }
}