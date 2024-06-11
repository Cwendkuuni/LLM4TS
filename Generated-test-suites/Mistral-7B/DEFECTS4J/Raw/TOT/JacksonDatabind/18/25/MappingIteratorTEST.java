import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.*;
import static org.junit.Assert.*;

public class MappingIteratorTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @BeforeClass
 public static void setup() throws Exception {
 ObjectReader reader = mapper.readerFor(List.class);
 }

 @Test
 public void testIterateEmptyMappingIterator() throws Exception {
 MappingIterator<Object> iterator = MappingIterator.emptyIterator();
 List<Object> result = iterator.readAll();
 assertTrue(result.isEmpty());
 }

 @Test
 public void testIterateMappingIteratorNullValues() throws Exception {
 JsonParser parser = mapper.getFactory().createParser("[]");
 MappingIterator<Object> iterator = new MappingIterator<Object>(
 ObjectTypeFactory.defaultInstance.constructValueType(Object.class),
 parser,
 mapper.getDeserializationContext(),
 null,
 true,
 null);
 List<Object> result = iterator.readAll();
 assertTrue(result.isEmpty());
 }

 @Test
 public void testIterateMappingIteratorSingleValue() throws Exception {
 String json = "[]";
 JsonParser parser = mapper.getFactory().createParser(json);
 MappingIterator<String> iterator = new MappingIterator<String>(
 ObjectTypeFactory.defaultInstance.constructType(String.class),
 parser,
 mapper.getDeserializationContext(),
 mapper.findValueDeserializer(String.class),
 true,
 null);
 List<String> result = iterator.readAll();
 assertEquals(1, result.size());
 assertEquals(null, result.get(0));
 }

 @Test
 public void testIterateMappingIteratorMultipleValues() throws Exception {
 String json = "[\"foo\", \"bar\", 123]";
 JsonParser parser = mapper.getFactory().createParser(json);
 MappingIterator<Object> iterator = new MappingIterator<Object>(
 ObjectTypeFactory.defaultInstance.constructCollectionType(List.class, Object.class),
 parser,
 mapper.getDeserializationContext(),
 null,
 true,
 null);
 List<Object> result = iterator.readAll();
 assertEquals(3, result.size());
 assertNotNull(result.get(0));
 assertNotNull(result.get(1));
 assertNotNull(result.get(2));
 }

 @Test(expected = JsonMappingException.class)
 public void testIterateMappingIteratorInvalidJson() throws Exception {
 String json = "[]]";
 JsonParser parser = mapper.getFactory().createParser(json);
 MappingIterator<Object> iterator = new MappingIterator<Object>(
 ObjectTypeFactory.defaultInstance.constructCollectionType(List.class, Object.class),
 parser,
 mapper.getDeserializationContext(),
 null,
 true,
 null);
 iterator.nextValue();
 fail("Should have thrown JsonMappingException");
 }

 @Test(expected = IOException.class)
 public void testIterateMappingIteratorIOException() throws Exception {
 String json = "{\"invalidJson\":\"here\"}";
 JsonParser parser = mapper.getFactory().createParser(json);
 MappingIterator<Object> iterator = new MappingIterator<Object>(
 ObjectTypeFactory.defaultInstance.constructValueType(Object.class),
 parser,
 mapper.getDeserializationContext(),
 null,
 true,
 null);
 iterator.nextValue();
 fail("Should have thrown IOException");
 }

 @Test
 public void testIterateMappingIteratorCloseParser() throws Exception {
 String json = "[\"foo\", \"bar\", 123]";
 JsonParser parser = mapper.getFactory().createParser(json);
 MappingIterator<Object> iterator = new MappingIterator<Object>(
 ObjectTypeFactory.defaultInstance.constructCollectionType(List.class, Object.class),
 parser,
 mapper.getDeserializationContext(),
 null,
 true,
 null);
 List<Object> result = iterator.readAll();
 assertEquals(3, result.size());
 assertNotNull(result.get(0));
 assertNotNull(result.get(1));
 assertNotNull(result.get(2));
 parser.close();
 MappingIterator<Object> iterator2 = new MappingIterator<Object>(
 ObjectTypeFactory.defaultInstance.constructCollectionType(List.class, Object.class),
 parser,
 mapper.getDeserializationContext(),
 null,
 true,
 null);
 List<Object> result2 = iterator2.readAll();
 assertTrue(result2.isEmpty());
 }

 @Test
 public void testIterateMappingIteratorReusableParser() throws Exception {
 String json = "[\"foo\", \"bar\", 123]";
 JsonParser parser = mapper.getFactory().createParser(json);
 MappingIterator<Object> iterator = new MappingIterator<Object>(
 ObjectTypeFactory.defaultInstance.constructCollectionType(List.class, Object.class),
 parser,
 mapper.getDeserializationContext(),
 null,
 false,
 null);
 List<Object> result = iterator.readAll();
 assertEquals(3, result.size());
 assertNotNull(result.get(0));
 assertNotNull(result.get(1));
 assertNotNull(result.get(2));
 JsonParser newParser = mapper.getFactory().createParser(json);
 MappingIterator<Object> iterator2 = new MappingIterator<Object>(
 ObjectTypeFactory.defaultInstance.constructCollectionType(List.class, Object.class),
 newParser,
 mapper.getDeserializationContext(),
 null,
 false,
 null);
 List<Object> result2 = iterator2.readAll();
 assertEquals(3, result2.size());
 assertNotNull(result2.get(0));
 assertNotNull(result2.get(1));
 assertNotNull(result2.get(2));
 }
}