import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.util.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class MappingIteratorTest {

 private MappingIterator<Integer> mappingIterator;
 private JsonParser jsonParser;
 private ObjectMapper objectMapper;
 private DeserializationContext deserializationContext;
 private List<Integer> expectedValues = new ArrayList<>();

 @Before
 public void setUp() throws IOException {
 objectMapper = new ObjectMapper();
 jsonParser = objectMapper.getFactory().createParser("[[1,2,3],[4,5,6]]");
 mappingIterator = new MappingIterator<>(
 JavaType.constructCollectionType(List.class, Integer.class), jsonParser, objectMapper.getDeserializationContext(), null, true, null);
 deserializationContext = mappingIterator.getDeserializationContext();
 }

 @Test
 public void testHasNext() throws IOException {
 assertTrue(mappingIterator.hasNext());
 assertTrue(mappingIterator.hasNext());
 assertTrue(mappingIterator.hasNext());
 assertFalse(mappingIterator.hasNext());
 }

 @Test
 public void testHasNextValue() throws IOException {
 assertTrue(mappingIterator.hasNextValue());
 assertTrue(mappingIterator.hasNextValue());
 assertTrue(mappingIterator.hasNextValue());
 assertFalse(mappingiterator.hasNextValue());
 }

 @Test
 public void testNext() throws IOException {
 assertEquals(1, mappingIterator.next().intValue());
 assertEquals(2, mappingIterator.next().intValue());
 assertEquals(3, mappingIterator.next().intValue());
 assertNull(mappingIterator.next());
 }

 @Test
 public void testReadAll() throws IOException {
 mappingIterator.readAll(expectedValues);
 assertEquals(6, expectedValues.size());
 for (int i = 0; i < expectedValues.size(); i++) {
 assertEquals(i + 1, expectedValues.get(i).intValue());
 }
 }

 @Test
 public void testReadAllList() throws IOException {
 List<Integer> resultList = new ArrayList<>();
 mappingIterator.readAll(resultList);
 assertEquals(6, resultList.size());
 for (int i = 0; i < resultList.size(); i++) {
 assertEquals(i + 1, resultList.get(i).intValue());
 }
 }

 @Test
 public void testReadAllCollection() throws IOException {
 Collection<Integer> resultCollection = new ArrayList<>();
 mappingIterator.readAll(resultCollection);
 assertEquals(6, resultCollection.size());
 for (int i = 0; i < resultCollection.size(); i++) {
 assertEquals(i + 1, resultCollection.get(i).intValue());
 }
 }

 @Test(expected = NoSuchElementException.class)
 public void testNoSuchElementException() throws IOException {
 mappingIterator.next();
 mappingIterator.next();
 mappingIterator.next();
 mappingIterator.next();
 mappingIterator.next();
 mappingIterator.next();
 mappingIterator.next();
 }

 @Test(expected = RuntimeJsonMappingException.class)
 public void testMappingException() throws IOException {
 JsonDeserializer<Integer> deserializer = new JsonDeserializer<Integer>() {
 @Override
 public Integer deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
 throw new JsonMappingException("Test Mapping Exception");
 }
 };
 mappingIterator = new MappingIterator<>(
 JavaType.constructCollectionType(List.class, Integer.class), jsonParser, objectMapper.getDeserializationContext(), deserializer, true, null);
 mappingIterator.nextValue();
 }

 @Test(expected = RuntimeException.class)
 public void testIOException() throws IOException {
 JsonDeserializer<Integer> deserializer = new JsonDeserializer<Integer>() {
 @Override
 public Integer deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
 throw new IOException("Test IOException");
 }
 };
 mappingIterator = new MappingIterator<>(
 JavaType.constructCollectionType(List.class, Integer.class), jsonParser, objectMapper.getDeserializationContext(), deserializer, true, null);
 mappingIterator.nextValue();
 }
}