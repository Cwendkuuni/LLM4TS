import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.*;
import static org.junit.Assert.*;

public class MappingIteratorTest {

 private ObjectMapper mapper;
 private Iterator<Object> emptyIterator;
 private Iterator<Object> iterator;

 @BeforeClass
 public static void setupClass() {
 ObjectMapper objectMapper = new ObjectMapper();
 }

 @Before
 public void setup() throws IOException {
 mapper = new ObjectMapper();
 emptyIterator = MappingIterator.emptyIterator();
 String jsonString = "[1, 2, 3, 4]";
 JsonNode jsonNode = mapper.readTree(jsonString);
 iterator = mapper.valueIterator(jsonNode.elements());
 }

 @Test
 public void testEmptyIterator() {
 assertFalse(emptyIterator.hasNext());
 }

 @Test
 public void testHasNext() {
 assertTrue(iterator.hasNext());
 }

 @Test
 public void testNext() {
 Object next = iterator.next();
 assertEquals(1, next.getClass().getComponentType().getComponentCount());
 assertEquals(1, ((Number) next).intValue());
 }

 @Test
 public void testNextAfterHasNextCheck() {
 iterator.hasNext();
 Object next = iterator.next();
 assertEquals(2, ((Number) next).intValue());
 }

 @Test
 public void testHasNextAfterNext() {
 assertFalse(iterator.hasNext());
 }

 @Test(expected = NoSuchElementException.class)
 public void testNoSuchElementException() {
 iterator.next();
 iterator.next();
 iterator.next();
 iterator.next();
 iterator.next();
 assertFalse(iterator.hasNext());
 iterator.next();
 }

 @Test
 public void testCloseParser() throws IOException {
 JsonParser jsonParser = mapper.getFactory().createParser(new StringReader("[1, 2, 3, 4]"));;
 iterator = new MappingIterator<Object>(mapper.getTypeFactory().constructCollectionType(List.class, Object.class), jsonParser, mapper.getDeserializationContext(), null, true, null);
 iterator.next();
 jsonParser.close();
 assertFalse(iterator.hasNext());
 }

 @Test(expected = JsonMappingException.class)
 public void testJsonMappingException() throws IOException {
 ObjectMapper objectMapper = new ObjectMapper();
 String jsonString = "[1, 2, 3, 4]";
 JsonNode jsonNode = objectMapper.readTree(jsonString);
 iterator = new MappingIterator<Object>(mapper.getTypeFactory().constructCollectionType(List.class, Object.class), null, mapper.getDeserializationContext(), null, false, null);
 iterator.nextValue();
 jsonNode.elements().next();
 iterator.nextValue(); // should throw JsonMappingException
 }

 @Test(expected = IOException.class)
 public void testIOException() throws IOException {
 ObjectMapper objectMapper = new ObjectMapper();
 String jsonString = "[1, 2, 3, 4]";
 JsonNode jsonNode = objectMapper.readTree(jsonString);
 iterator = new MappingIterator<Object>(mapper.getTypeFactory().constructCollectionType(List.class, Object.class), null, mapper.getDeserializationContext(), null, false, null);
 iterator.nextValue();
 jsonNode.elements().next();
 iterator._parser.close(); // should throw IOException
 }

 @After
 public void teardown() throws IOException {
 mapper.close();
 }

}