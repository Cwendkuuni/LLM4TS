import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.Converter;

public class WritableObjectIdTest {

 private ObjectMapper mapper;
 private ObjectIdWriter idWriter;
 private StdSerializer<Object> idSerializer;
 private Converter<Object, String> stringConverter;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 idWriter = new ObjectIdWriter();
 idSerializer = new StdSerializer<Object>(Object.class);
 stringConverter = mapper.getFactory()
 .getValueInstance(Converter.ValueHandler.class);
 }

 @Test
 public void testWritableObjectIdConstructor() {
 WritableObjectId writableObjectId = new WritableObjectId(null);
 assertNull(writableObjectId.generator);
 assertNull(writableObjectId.id);
 assertFalse(writableObjectId.idWritten);
 }

 @Test
 public void testWriteAsIdSuccess() throws IOException {
 ObjectIdGenerator<?> generatorMock = mock(ObjectIdGenerator.class);
 when(generatorMock.generateId(any())).thenReturn(1L);
 WritableObjectId writableObjectId = new WritableObjectId(generatorMock);
 id = 1L;
 JsonGenerator gen = mock(JsonGenerator.class);
 SerializerProvider provider = mock(SerializerProvider.class);
 assertTrue(writableObjectId.writeAsId(gen, provider, idWriter));
 verify(gen).writeObjectRef(eq(String.valueOf(id)));
 }

 @Test
 public void testWriteAsIdFailure() throws IOException {
 ObjectIdGenerator<?> generatorMock = mock(ObjectIdGenerator.class);
 when(generatorMock.generateId(any())).thenReturn(null);
 WritableObjectId writableObjectId = new WritableObjectId(generatorMock);
 id = new Object();
 JsonGenerator gen = mock(JsonGenerator.class);
 SerializerProvider provider = mock(SerializerProvider.class);
 assertFalse(writableObjectId.writeAsId(gen, provider, idWriter));
 }

 @Test
 public void testGenerateIdSuccess() {
 Object forPojo = new Object();
 WritableObjectId writableObjectId = new WritableObjectId(null);
 Object id = writableObjectId.generateId(forPojo);
 assertNotNull(id);
 }

 @Test(expected = JsonMappingException.class)
 public void testGenerateIdFailure() {
 Object forPojo = new Object();
 WritableObjectId writableObjectId = new WritableObjectId(null);
 writableObjectId.generator = mock(ObjectIdGenerator.class);
 when(writableObjectId.generator.generateId(any())).thenThrow(new JsonMappingException());
 Object id = writableObjectId.generateId(forPojo);
 fail("GenerateId method should throw JsonMappingException when ObjectIdGenerator fails");
 }

 @Test
 public void testWriteAsFieldSuccess() throws IOException {
 ObjectIdGenerator<?> generatorMock = mock(ObjectIdGenerator.class);
 when(generatorMock.generateId(any())).thenReturn(1L);
 WritableObjectId writableObjectId = new WritableObjectId(generatorMock);
 id = 1L;
 JsonGenerator gen = mock(JsonGenerator.class);
 SerializerProvider provider = mock(SerializerProvider.class);
 idWriter.serializer = idSerializer;
 idWriter.propertyName = new SerializableString("testPropertyName");
 writableObjectId.writeAsField(gen, provider, idWriter);
 verify(gen).writeObjectId(eq(String.valueOf(id)));
 }

 @Test
 public void testWriteAsFieldFailure() throws IOException {
 ObjectIdGenerator<?> generatorMock = mock(ObjectIdGenerator.class);
 when(generatorMock.generateId(any())).thenReturn(1L);
 WritableObjectId writableObjectId = new WritableObjectId(generatorMock);
 id = new Object();
 JsonGenerator gen = mock(JsonGenerator.class);
 SerializerProvider provider = mock(SerializerProvider.class);
 idWriter.serializer = idSerializer;
 idWriter.propertyName = null;
 writableObjectId.writeAsField(gen, provider, idWriter);
 verifyNoMoreInteractions(gen);
 }

 @Test(expected = JsonGenerationException.class)
 public void testWriteAsFieldFailureWithJsonGenerationException() throws IOException {
 ObjectIdGenerator<?> generatorMock = mock(ObjectIdGenerator.class); when(generatorMock.generateId(eq(forPojo))).thenReturn(1L); WritableObjectId writableObjectId = new WritableObjectId(generatorMock); id = new Object(); JsonGenerator gen = mock(JsonGenerator.class); SerializerProvider provider = mock(SerializerProvider.class); idWriter.serializer = idSerializer; idWriter.propertyName = new SerializableString("testPropertyName"); doThrow(new JsonGenerationException()).when(idSerializer).serialize(id, gen, provider); writableObjectId.writeAsField(gen, provider, idWriter); fail("writeAsField method should throw JsonGenerationException when serializer fails"); }

 @Test
 public void testIdWriterAlwaysAsId() { ObjectIdGenerator<?> generatorMock = mock(ObjectIdGenerator.class); when(generatorMock.generateId(any())).thenReturn(1L); WritableObjectId writableObjectId = new WritableObjectId(generatorMock); id = 1L; idWriter.alwaysAsId = true; JsonGenerator gen = mock(JsonGenerator.class); SerializerProvider provider = mock(SerializerProvider.class); assertTrue(writableObjectId.writeAsId(gen, provider, idWriter)); verify(gen).writeObjectRef(eq(String.valueOf(id))); }
}