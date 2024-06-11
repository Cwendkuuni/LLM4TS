import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MappingIteratorTest {

 private MappingIterator<Integer> iterator;
 private JsonFactory jsonFactory;
 private JsonParser jsonParser;

 @Before
 public void setUp() throws IOException {
 jsonFactory = new JsonFactory();
 jsonParser = jsonFactory.createParser("[]");
 iterator = new MappingIterator<>(JsonTypeFactory.defaultInstance().constructCollectionType(List.class, Integer.class), jsonParser, null, null, false, null);
 }

 @After
 public void tearDown() throws IOException {
 if (iterator != null) {
 iterator.close();
 }
 if (jsonParser != null) {
 jsonParser.close();
 }
 }

 @Test
 public void testHasNext() {
 assertTrue(iterator.hasNext());
 }

 @Test
 public void testHasNext_EmptyIterator() {
 MappingIterator<Integer> emptyIterator = MappingIterator.emptyIterator();
 assertFalse(emptyIterator.hasNext());
 }

 @Test
 public void testHasNext_NullParser() {
 MappingIterator<Integer> iterator = new MappingIterator<>(JsonTypeFactory.defaultInstance().constructCollectionType(List.class, Integer.class), null, null, null, false, null);
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testNext() throws IOException {
 assertEquals(0, iterator.next().intValue());
 }

 @Test(expected = NoSuchElementException.class)
 public void testNext_EmptyIterator() {
 MappingIterator<Integer> emptyIterator = MappingIterator.emptyIterator();
 emptyIterator.next();
 }

 @Test(expected = JsonMappingException.class)
 public void testNext_InvalidJson() throws IOException {
 jsonParser.setCurrentToken(JsonToken.START_OBJECT);
 iterator = new MappingIterator<>(JsonTypeFactory.defaultInstance().constructCollectionType(List.class, Integer.class), jsonParser, null, null, false, null);
 iterator.next();
 }

 @Test(expected = IOException.class)
 public void testNext_IOException() throws IOException {
 JsonParser ioExceptionParser = jsonFactory.createParser("{ \"int\": 1 }");
 ioExceptionParser.setException(new IOException("Test Exception"));
 iterator = new MappingIterator<>(JsonTypeFactory.defaultInstance().constructCollectionType(List.class, Integer.class), ioExceptionParser, null, null, false, null);
 iterator.next();
 }

 @Test
 public void testClose() throws IOException {
 iterator.close();
 assertFalse(iterator.hasNext());
 jsonParser.nextToken();
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testReadAll() throws IOException {
 List<Integer> list = iterator.readAll();
 assertEquals(0, list.size());
 JsonParser jsonParser = jsonFactory.createParser("[1, 2, 3]");
 iterator = new MappingIterator<>(JsonTypeFactory.defaultInstance().constructCollectionType(List.class, Integer.class), jsonParser, null, null, false, null);
 list = iterator.readAll();
 assertEquals(3, list.size());
 assertEquals(1, list.get(0).intValue());
 assertEquals(2, list.get(1).intValue());
 assertEquals(3, list.get(2).intValue());
 }

 @Test
 public void testReadAll_List() throws IOException {
 Collection<Integer> collection = iterator.readAll(new ArrayList<>());
 assertEquals(0, collection.size());
 JsonParser jsonParser = jsonFactory.createParser("[1, 2, 3]");
 iterator = new MappingIterator<>(JsonTypeFactory.defaultInstance().constructCollectionType(List.class, Integer.class), jsonParser, null, null, false, null);
 collection = iterator.readAll(new ArrayList<>());
 assertEquals(3, collection.size());
 assertEquals(1, collection.iterator().next().intValue());
 assertEquals(2, collection.iterator().next().intValue());
 assertEquals(3, collection.iterator().next().intValue());
 }

}