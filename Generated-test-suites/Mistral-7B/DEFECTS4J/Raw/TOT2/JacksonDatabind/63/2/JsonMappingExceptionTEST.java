import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonMappingExceptionTest {

 private JsonParser parser;
 private JsonGenerator generator;
 private JsonMappingException exception;

 @Before
 public void setup() throws IOException {
 parser = new ObjectMapper().getFactory().createParser(getClass().getResourceAsStream("/input.json"));
 generator = new ObjectMapper().getFactory().createGenerator(System.out);
 }

 @Test
 public void testNormalSerialization() throws JsonProcessingException {
 generator.writeStartObject();
 generator.writeStringField("name", "John Doe");
 generator.writeEndObject();
 generator.flush();
 }

 @Test
 public void testSerializationWithPath() throws JsonProcessingException {
 Object root = new ObjectMapper().readValue(getClass().getResourceAsStream("/input.json"), Object.class);
 exception = JsonMappingException.wrapWithPath(new RuntimeException("Test failure"), root, "name");
 assertEquals("Root->name (through reference chain: Root->\"name\")", exception.getPathReference());
 }

 @Test
 public void testSerializationWithIndex() throws JsonProcessingException {
 Object list = new ObjectMapper().readValue(getClass().getResourceAsStream("/input.json"), Object[].class);
 exception = JsonMappingException.wrapWithPath(new RuntimeException("Test failure"), list, 0);
 assertEquals("List[0] (through reference chain: List[0])", exception.getPathReference());
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidDeserialization() throws IOException {
 parser.nextTokenId(); // consume first token to make an exception throwable
 parser.nextValue(); // consume value to progress parsing
 exception = parser.readValueAs(String.class);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializationWithPath() throws IOException {
 Object root = parser.readValueAsTree();
 parser.nextToken(); // consume next token to make an exception throwable
 exception = JsonMappingException.wrapWithPath(parser, "Unexpected condition: stream is closed");
 assertEquals("Root (through reference chain: JsonParser->getTokenLocation())", exception.getPathReference());
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializationWithIndex() throws IOException {
 Object list = parser.readValueAsTree();
 parser.nextToken(); // consume next token to make an exception throwable
 exception = JsonMappingException.wrapWithPath(parser, "Unexpected condition: stream is closed");
 assertEquals("List[0] (through reference chain: JsonParser->getCurrentLocation())", exception.getPathReference());
 }

 @Test
 public void testUnexpectedIOException() {
 IOException ioException = new IOException("Test IOException");
 exception = JsonMappingException.fromUnexpectedIOE(ioException);
 assertEquals("Unexpected IOException (of type java.io.IOException): Test IOException", exception.getMessage());
 }
}