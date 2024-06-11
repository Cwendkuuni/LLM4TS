import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonMappingExceptionTest {

 @Test
 public void testJsonMappingExceptionWithMessage() {
 JsonMappingException exception = new JsonMappingException("Exception message");
 assertEquals("Exception message", exception.getMessage());
 }

 @Test(expected = JsonMappingException.class)
 public void testJsonMappingExceptionWithMessageAndLocation() {
 ObjectMapper mapper = new ObjectMapper();
 JsonMappingException exception = null;
 try {
 mapper.readValue("{}", String.class);
 } catch (JsonProcessingException e) {
 exception = JsonMappingException.from(e);
 }
 assertEquals("Unexpected token (END_OBJECT) at [Source: (StringReader); line: 1, column: 1]", exception.getMessage());
 }

 @Test(expected = JsonMappingException.class)
 public void testJsonMappingExceptionWithMessageAndLocationAndRootCause() {
 ObjectMapper mapper = new ObjectMapper();
 JsonMappingException exception = null;
 try {
 mapper.readValue("{\"name\":1}", Person.class);
 } catch (JsonProcessingException e) {
 exception = JsonMappingException.from(e);
 }
 assertEquals("Cannot deserialize value of type [simple type, class com.example.Person] from Object value (number ‘1’) at [Source: (StringReader); line: 1, column: 11]", exception.getMessage());
 assertEquals("Expected a String but was an INTEGER of type long", exception.getCause().getMessage());
 }

 @Test(expected = JsonMappingException.class)
 public void testJsonMappingExceptionWithProcessor() {
 ObjectMapper mapper = new ObjectMapper();
 JsonParser parser = mapper.getFactory().createParser("{}");
 JsonMappingException exception = JsonMappingException.from(parser, "Exception message");
 assertEquals("Exception message", exception.getMessage());
 }

 @Test(expected = JsonMappingException.class)
 public void testJsonMappingExceptionWithProcessorAndMessage() {
 ObjectMapper mapper = new ObjectMapper();
 JsonParser parser = mapper.getFactory().createParser("{}");
 JsonMappingException exception = JsonMappingException.from(parser, "Unexpected token (END_OBJECT) at [Source: (StringReader); line: 1, column: 1]");
 assertEquals("Unexpected token (END_OBJECT) at [Source: (StringReader); line: 1, column: 1]", exception.getMessage());
 }

 @Test(expected = JsonMappingException.class)
 public void testJsonMappingExceptionWithProcessorAndMessageAndLocation() {
 ObjectMapper mapper = new ObjectMapper();
 JsonParser parser = mapper.getFactory().createParser("{}");
 JsonMappingException exception = JsonMappingException.from(parser, "Unexpected token (END_OBJECT) at [Source: (StringReader); line: 1, column: 1]", new LocationTest("test.json", 1, 1));
 assertEquals("Unexpected token (END_OBJECT) at [Source: (StringReader); line: 1, column: 1]", exception.getMessage());
 }

 @Test(expected = JsonMappingException.class)
 public void testJsonMappingExceptionWithProcessorAndMessageAndLocationAndRootCause() {
 ObjectMapper mapper = new ObjectMapper();
 JsonParser parser = mapper.getFactory().createParser("{}");
 JsonMappingException exception = JsonMappingException.from(parser, "Unexpected token (END_OBJECT) at [Source: (StringReader); line: 1, column: 1]", new LocationTest("test.json", 1, 1), new IOException("IOException message"));
 assertEquals("Unexpected token (END_OBJECT) at [Source: (StringReader); line: 1, column: 1]", exception.getMessage());
 assertEquals("IOException message", exception.getCause().getMessage());
 }

 @Test
 public void testJsonMappingExceptionWithPath() {
 ObjectMapper mapper = new ObjectMapper();
 JsonParser parser = mapper.getFactory().createParser("{\"name\":{\"age\":1}}");
 JsonMappingException exception = JsonMappingException.from(parser, "Unexpected token (END_OBJECT) at [Source: (StringReader); line: 1, column: 1]", new Reference(parser, "name"));
 assertEquals("[name]", exception.getPathReference());
 }

 @Test
 public void testJsonMappingExceptionWithPathAndAdditionalReference() {
 ObjectMapper mapper = new ObjectMapper();
 JsonParser parser = mapper.getFactory().createParser("{\"name\":{\"age\":1,\"city\":\"New York\"}}");
 JsonMappingException exception = JsonMappingException.from(parser, "Unexpected token (END_OBJECT) at [Source: (StringReader); line: 1, column: 1]", new Reference(parser, "name"), new Reference(parser, "city"));
 assertEquals("[name->city]", exception.getPathReference());
 }

 @Test
 public void testJsonMappingExceptionWithPathAndIndex() {
 ObjectMapper mapper = new ObjectMapper();
 JsonParser parser = mapper.getFactory().createParser("[1,2,3]");
 JsonMappingException exception = JsonMappingException.from(parser, "Unexpected character ('[') at line 1 column 1 path [/Array][0]", new Reference(parser, 0));
 assertEquals("[0]", exception.getPathReference());
 }

 @Test
 public void testJsonMappingExceptionWithPathAndIndexAndAdditionalReference() {
 ObjectMapper mapper = new ObjectMapper();
 JsonParser parser = mapper.getFactory().createParser("[1,2{name:\"John\"},3]");
 JsonMappingException exception = JsonMappingException.from(parser, "Unexpected character ('{' at line 1 column 5 path [Array][1]->[Object#0]->name]", new Reference(parser, 1), new Reference(parser, "name"));
 assertEquals("[1->name]", exception.getPathReference());
 }
}