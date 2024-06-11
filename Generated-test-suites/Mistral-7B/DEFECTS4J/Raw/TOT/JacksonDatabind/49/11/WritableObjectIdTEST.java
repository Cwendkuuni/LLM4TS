import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectIdGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WritableObjectIdTest {

 private ObjectIdGenerator<Long> generator;
 private ObjectMapper mapper;
 private SerializerProvider provider;
 private ObjectIdWriter idWriter;
 private WritableObjectId idContainer;

 @Before
 public void setup() {
 generator = new ObjectIdGenerator<Long>() {
 @Override
 public Long generateId(Object bean) {
 return System.currentTimeMillis();
 }
 };
 mapper = new ObjectMapper();
 provider = mapper.getSerializerProvider();
 idWriter = new ObjectIdWriter();
 idWriter.propertyName = new SerializableString("testProperty");
 idContainer = new WritableObjectId(generator);
 }

 @Test
 public void testWriteAsId() throws IOException {
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out, null);
 idContainer.writeAsId(gen, provider, idWriter);
 assertTrue(gen.isFlushed());
 }

 @Test
 public void testWriteAsId_NullId() throws IOException {
 idContainer.id = null;
 idContainer.writeAsId(mapper.getFactory().createGenerator(System.out, null), provider, idWriter);
 assertFalse(idContainer.idWritten);
 }

 @Test
 public void testWriteAsId_IdWritten() throws IOException {
 idContainer.id = 1L;
 idContainer.idWritten = true;
 idContainer.writeAsId(mapper.getFactory().createGenerator(System.out, null), provider, idWriter);
 assertFalse(idContainer.idWritten);
 }

 @Test
 public void testGenerateId_NullForPojo() {
 Object result = idContainer.generateId(null);
 assertNull(result);
 }

 @Test
 public void testGenerateId_NotNullForPojo() {
 Object result = idContainer.generateId(new Object());
 assertNotNull(result);
 }

 @Test
 public void testWriteAsField() throws JsonGenerationException, JsonMappingException, IOException {
 ObjectNode jsonNode = mapper.createObjectNode();
 idContainer.writeAsField(mapper.getFactory().createGenerator(jsonNode, provider), provider, idWriter);
 assertEquals("testProperty", jsonNode.fieldNames().nextValue().asText());
 assertEquals(idContainer.id.toString(), jsonNode.get(jsonNode.fieldNames().nextValue().asText()).asText());
 }

 @Test
 public void testWriteAsField_NullId() throws JsonGenerationException, JsonMappingException, IOException {
 idContainer.id = null;
 idContainer.writeAsField(mapper.getFactory().createGenerator(System.out, null), provider, idWriter);
 assertFalse(idContainer.idWritten);
 }

 @Test
 public void testWriteAsField_IdWritten() throws JsonGenerationException, JsonMappingException, IOException {
 idContainer.id = 1L;
 idContainer.idWritten = true;
 idContainer.writeAsField(mapper.getFactory().createGenerator(System.out, null), provider, idWriter);
 assertFalse(idContainer.idWritten);
 }

 @Test
 public void testWriteAsField_NoPropertyName() {
 idWriter.propertyName = null;
 idContainer.writeAsField(mapper.getFactory().createGenerator(System.out, null), provider, idWriter);
 fail("Expected JsonWritingException");
 }

 @Test
 public void testWriteAsField_AlwaysAsId() throws JsonGenerationException, JsonMappingException, IOException {
 idWriter.alwaysAsId = true;
 idContainer.writeAsId(mapper.getFactory().createGenerator(System.out, null), provider, idWriter);
 assertFalse(idContainer.idWritten);
 }
}

class ObjectIdWriter extends StdSerializer<Object> {
 SerializableString propertyName;
 boolean alwaysAsId;

 public ObjectIdWriter() {
 this(null);
 }

 public ObjectIdWriter(SerializableString propertyName) {
 this(propertyName, false);
 }

 public ObjectIdWriter(SerializableString propertyName, boolean alwaysAsId) {
 this.propertyName = propertyName;
 this.alwaysAsId = alwaysAsId;
 }
}