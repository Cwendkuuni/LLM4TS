import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import org.junit.Test;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

public class TypeDeserializerBaseTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();

 @Test
 public void testForProperty() throws Exception {
 TypeDeserializerBase deserializer = new TypeDeserializerBaseForTest();
 BeanProperty property = new BeanProperty.Std("testProperty", String.class);
 TypeDeserializer forProperty = deserializer.forProperty(property);
 assertEquals(deserializer.getClass(), forProperty.getClass());
 }

 @Test(expected = IOException.class)
 public void testUnknownTypeId() throws Exception {
 TypeDeserializerBase deserializer = new TypeDeserializerBaseForTest();
 ObjectMapper mapper = new ObjectMapper();
 mapper.registerModule(new SimpleModule().setAbstractTypeAnnotation(TypeDeserializerBase.class)
 .addDeserializer(TypeDeserializerBase.class, deserializer));
 String json = "{\"typeId\": \"nonExistentTypeId\", \"value\": \"test\"}";
 mapper.readValue(new StringReader(json), String.class);
 }

 @Test
 public void testFindDeserializerWithDefaultImplTypeId() throws Exception {
 TypeDeserializerBase deserializer = new TypeDeserializerBaseForTest();
 JsonDeserializer<Object> deserializerForTypeId = deserializer._findDeserializer(MAPPER.getContext(), "defaultImplTypeId");
 assertNull(deserializerForTypeId);
 }

 @Test
 public void testFindDeserializerWithRegisteredDeserializer() throws Exception {
 TypeDeserializerBase deserializer = new TypeDeserializerBaseForTest();
 JsonDeserializer<String> stringDeserializer = new StringDeserializer();
 MAPPER.registerDeserializer(String.class, stringDeserializer);
 MAPPER.registerModule(new SimpleModule().setAbstractTypeAnnotation(TypeDeserializerBase.class)
 .addDeserializer(TypeDeserializerBase.class, deserializer));
 String json = "{\"typeId\": \"StringDeserializerTypeId\", \"value\": \"test\"}";
 Object result = MAPPER.readValue(new StringReader(json), Object.class);
 assertEquals("test", result);
 }

 @Test
 public void testFindDeserializerWithDefaultImpl() throws Exception {
 TypeDeserializerBase deserializer = new TypeDeserializerBaseForTest();
 JsonDeserializer<Object> deserializerForDefaultImplTypeId = deserializer._findDefaultImplDeserializer(MAPPER.getContext());
 assertNull(deserializerForDefaultImplTypeId);
 }

 @Test
 public void testFindDeserializerWithRegisteredDefaultImpl() throws Exception {
 TypeDeserializerBase deserializer = new TypeDeserializerBaseForTest();
 JsonDeserializer<String> stringDeserializer = new StringDeserializer();
 MAPPER.registerDeserializer(String.class, stringDeserializer);
 MAPPER.registerModule(new SimpleModule().setAbstractTypeAnnotation(TypeDeserializerBase.class)
 .addDeserializer(TypeDeserializerBase.class, deserializer)
 .addDeserializer(JsonDeserializer.None.class, stringDeserializer));
 String json = "{\"value\": \"test\"}";
 Object result = MAPPER.readValue(new StringReader(json), Object.class);
 assertEquals("test", result);
 }
}

class StringDeserializer extends StdSerializer<String> {
 protected StringDeserializer() { this(null); }
 StringDeserializer(Class<String> t) { super(t); }
 @Override public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException { return "test"; }
}

class TypeDeserializerBaseForTest extends TypeDeserializerBase {
 public TypeDeserializerBaseForTest() { super(JsonType.construct(), null, "", false, MAPPER.getTypeFactory().constructType(Object.class)); }
}