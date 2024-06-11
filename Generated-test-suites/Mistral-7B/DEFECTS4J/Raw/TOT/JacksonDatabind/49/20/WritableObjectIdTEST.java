import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import org.junit.Test;
import static org.junit.Assert.*;

public class WritableObjectIdTest {

 private static final ObjectIdGenerator<Integer> INT_GENERATOR = new ObjectIdGenerator<Integer>() {
      @Override
      public Integer generateId(Object obj) {
          return (Integer) obj;
      }
  };

 @Test
 public void testWriteAsId_NullGenerator() throws IOException {
     WritableObjectId id = new WritableObjectId(null);
     JsonGenerator gen = createJsonGenerator();
     assertFalse(id.writeAsId(gen, null, null));
 }

 @Test
 public void testWriteAsId_NullId() throws IOException {
     WritableObjectId id = new WritableObjectId(INT_GENERATOR);
     JsonGenerator gen = createJsonGenerator();
     assertFalse(id.writeAsId(gen, new SerializerProvider(), null));
 }

 @Test
 public void testWriteAsId_NullGeneratorAndWriter() throws IOException {
     WritableObjectId id = new WritableObjectId(INT_GENERATOR);
     JsonGenerator gen = createJsonGenerator();
     assertFalse(id.writeAsId(gen, null, null));
 }

 @Test
 public void testWriteAsId_IdWritten() throws IOException {
     WritableObjectId id = new WritableObjectId(INT_Generator);
     Object object = new Object();
     JsonGenerator gen = createJsonGenerator();
     ObjectIdWriter w = new ObjectIdWriter(new StdSerializer<String>(String.class), null, null, null);
     id.id = 1;
     id.idWritten = true;
     assertTrue(id.writeAsId(gen, new SerializerProvider(), w));
     assertEquals("1", gen.getOutputBuffer().toString().trim());
 }

 @Test
 public void testWriteAsId_IdNotWritten() throws IOException {
     WritableObjectId id = new WritableObjectId(INT_Generator);
     Object object = new Object();
     JsonGenerator gen = createJsonGenerator();
     ObjectIdWriter w = new ObjectIdWriter(new StdSerializer<String>(String.class), null, null, null);
     id.id = 1;
     assertFalse(id.writeAsId(gen, new SerializerProvider(), w));
 }

 @Test
 public void testWriteAsId_AlwaysAsId() throws IOException {
     WritableObjectId id = new WritableObjectId(INT_Generator);
     Object object = new Object();
     JsonGenerator gen = createJsonGenerator();
     ObjectIdWriter w = new ObjectIdWriter(new StdSerializer<String>(String.class), null, null, null);
     w.alwaysAsId = true;
     id.id = 1;
     id.idWritten = false;
     assertTrue(id.writeAsId(gen, new SerializerProvider(), w));
     assertEquals("1", gen.getOutputBuffer().toString().trim());
 }

 @Test
 public void testGenerateId_NullGenerator() {
     WritableObjectId id = new WritableObjectId(null);
     assertNull(id.generateId(new Object()));
 }

 @Test
 public void testGenerateId_NullObject() {
     WritableObjectId id = new WritableObjectId(INT_Generator);
     assertNull(id.generateId(null));
 }

 @Test
 public void testGenerateId() throws IOException {
     WritableObjectId id = new WritableObjectId(INT_Generator);
     Object object = new Object();
     Object expectedId = INT_Generator.generateId(object);
     Object actualId = id.generateId(object);
     assertEquals(expectedId, actualId);
 }

 @Test
 public void testWriteAsField_NullGenerator() throws IOException, JsonMappingException, JsonGenerationException {
     WritableObjectId id = new WritableObjectId(null);
     JsonGenerator gen = createJsonGenerator();
     ObjectIdWriter w = new ObjectIdWriter(new StdSerializer<String>(String.class), null, null, null);
     assertFalse(id.writeAsField(gen, new SerializerProvider(), w));
 }

 @Test
 public void testWriteAsField_NullId() throws IOException, JsonMappingException, JsonGenerationException {
     WritableObjectId id = new WritableObjectId(INT_Generator);
     JsonGenerator gen = createJsonGenerator();
     ObjectIdWriter w = new ObjectIdWriter(new StdSerializer<String>(String.class), null, null, null);
     assertFalse(id.writeAsField(gen, new SerializerProvider(), w));
 }

 @Test
 public void testWriteAsField_NullPropertyName() throws IOException, JsonMappingException, JsonGenerationException {
     WritableObjectId id = new WritableObjectId(INT_Generator);
     JsonGenerator gen = createJsonGenerator();
     ObjectIdWriter w = new ObjectIdWriter(new StdSerializer<String>(String.class), null, null, null);
     id.id = 1;
     assertFalse(id.writeAsField(gen, new SerializerProvider(), w));
 }

 @Test
 public void testWriteAsField() throws IOException, JsonMappingException, JsonGenerationException {
     WritableObjectId id = new WritableObjectId(INT_Generator);
     Object object = new Object();
     JsonGenerator gen = createJsonGenerator();
     ObjectIdWriter w = new ObjectIdWriter(new StdSerializer<String>(String.class), null, null, null);
     id.id = 1;
     String propertyName = "id";
     w.propertyName = new SerializableString(propertyName);
     assertTrue(id.writeAsField(gen, new SerializerProvider(), w));
     assertEquals("\"id\":1", gen.getOutputBuffer().toString().trim());
 }

 private JsonGenerator createJsonGenerator() throws IOException {
     ObjectMapper mapper = new ObjectMapper();
     SerializerProvider provider = mapper.getSerializerProvider();
     JsonGenerator gen = mapper.getFactory().createGenerator(new StringWriter());
     return gen;
 }

 private static class StringWriter extends StringWriter {
      @Override
      public void close() throws IOException {
          super.close();
      }
 }

 static class ObjectIdWriter extends StdSerializer<Object> {
      private final StdSerializer<String> serializer;
      private final String propertyName;
      private boolean alwaysAsId = false;

      public ObjectIdWriter(StdSerializer<String> serializer, Object _idWriter, Class<Object> t, Object _cons) {
          this.serializer = serializer;
          this.propertyName = null;
      }

      public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
          if (propertyName != null) {
              gen.writeFieldName(propertyName.asPrettyPrinted());
          }
          serializer.serialize(value, gen, provider);
      }
  }
}