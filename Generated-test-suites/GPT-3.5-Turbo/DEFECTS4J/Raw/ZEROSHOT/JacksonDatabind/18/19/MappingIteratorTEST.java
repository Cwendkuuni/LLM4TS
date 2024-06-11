import static org.junit.Assert.*;
import org.junit.*;
import org.mockito.*;

public class MappingIteratorTest {
  
  private MappingIterator<Object> mappingIterator;
  private ObjectMapper objectMapper;
  
  @Before
  public void setUp() {
    objectMapper = new ObjectMapper();
  }
  
  @Test
  public void testHasNext() {
    JsonParser parser = Mockito.mock(JsonParser.class);
    mappingIterator = new MappingIterator<>(null, parser, null, null, false, null);
    Mockito.when(parser.getCurrentToken()).thenReturn(JsonToken.FIELD_NAME);
    assertTrue(mappingIterator.hasNext());
  }
  
  @Test
  public void testHasNextValue() throws IOException {
    JsonParser parser = Mockito.mock(JsonParser.class);
    mappingIterator = new MappingIterator<>(null, parser, null, null, false, null);
    Mockito.when(parser.getCurrentToken()).thenReturn(JsonToken.FIELD_NAME);
    assertTrue(mappingIterator.hasNextValue());
  }
  
  @Test
  public void testNext() {
    JsonParser parser = Mockito.mock(JsonParser.class);
    mappingIterator = new MappingIterator<>(null, parser, null, null, false, null);
    Mockito.when(parser.getCurrentToken()).thenReturn(JsonToken.FIELD_NAME);
    assertNotNull(mappingIterator.next());
  }
  
  @Test
  public void testNextValue() throws IOException {
    JsonParser parser = Mockito.mock(JsonParser.class);
    mappingIterator = new MappingIterator<>(null, parser, null, null, false, null);
    Mockito.when(parser.getCurrentToken()).thenReturn(JsonToken.FIELD_NAME);
    assertNotNull(mappingIterator.nextValue());
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testRemove() {
    JsonParser parser = Mockito.mock(JsonParser.class);
    mappingIterator = new MappingIterator<>(null, parser, null, null, false, null);
    mappingIterator.remove();
  }
  
  @Test
  public void testClose() throws IOException {
    JsonParser parser = Mockito.mock(JsonParser.class);
    mappingIterator = new MappingIterator<>(null, parser, null, null, false, null);
    mappingIterator.close();
    Mockito.verify(parser, Mockito.times(1)).close();
  }
  
  @Test
  public void testReadAll() throws IOException {
    List<Object> resultList = mappingIterator.readAll(new ArrayList<>());
    assertNotNull(resultList);
  }
  
  @Test
  public void testReadAllList() throws IOException {
    List<Object> resultList = mappingIterator.readAll();
    assertNotNull(resultList);
  }
  
  @Test
  public void testReadAllCollection() throws IOException {
    List<Object> resultList = mappingIterator.readAll(new LinkedList<>());
    assertNotNull(resultList);
  }
  
  @Test
  public void testGetParser() {
    JsonParser parser = Mockito.mock(JsonParser.class);
    mappingIterator = new MappingIterator<>(null, parser, null, null, false, null);
    assertEquals(parser, mappingIterator.getParser());
  }
  
  @Test
  public void testGetParserSchema() {
    JsonParser parser = Mockito.mock(JsonParser.class);
    FormatSchema schema = Mockito.mock(FormatSchema.class);
    mappingIterator = new MappingIterator<>(null, parser, null, null, false, null);
    Mockito.when(parser.getSchema()).thenReturn(schema);
    assertEquals(schema, mappingIterator.getParserSchema());
  }
  
  @Test
  public void testGetCurrentLocation() {
    JsonParser parser = Mockito.mock(JsonParser.class);
    JsonLocation location = Mockito.mock(JsonLocation.class);
    mappingIterator = new MappingIterator<>(null, parser, null, null, false, null);
    Mockito.when(parser.getCurrentLocation()).thenReturn(location);
    assertEquals(location, mappingIterator.getCurrentLocation());
  }
}