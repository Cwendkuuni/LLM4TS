import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultPrettyPrinterTest {

 private JsonFactory jsonFactory = new JsonFactory();

 @Test
 public void testDefaultPrettyPrinter() throws JsonProcessingException {
 JsonGenerator generator = jsonFactory.createGenerator(System.out);
 DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
 prettyPrinter.writeRootValueSeparator(generator);

 // Test root level value
 prettyPrinter.writeObjectValue(generator, 123);
 prettyPrinter.writeEndObject(generator);

 // Test empty object
 prettyPrinter.writeStartObject(generator);
 prettyPrinter.writeEndObject(generator);

 // Test empty array
 prettyPrinter.writeStartArray(generator);
 prettyPrinter.writeEndArray(generator);

 // Test nested objects and arrays
 prettyPrinter.writeStartObject(generator);
 prettyPrinter.writeFieldName("name");
 prettyPrinter.writeObjectValue(generator, new PrettyObject("John Doe"));
 prettyPrinter.writeFieldName("age");
 prettyPrinter.writeNumberValue(generator, 30);
 prettyPrinter.writeEndObject(generator);
 prettyPrinter.writeStartArray(generator);
 prettyPrinter.writeValue(generator, "apple");
 prettyPrinter.writeValue(generator, new PrettyArray(new Integer[]{1, 2, 3}));
 prettyPrinter.writeEndArray(generator);
 prettyPrinter.writeEndObject(generator);

 generator.close();
 }

 @Test(expected = JsonProcessingException.class)
 public void testNullRootValueSeparator() throws JsonProcessingException {
 JsonGenerator generator = jsonFactory.createGenerator(System.out);
 DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
 prettyPrinter.writeRootValueSeparator(generator);
 prettyPrinter.writeObjectValue(generator, null);
 prettyPrinter.writeEndObject(generator);
 generator.close();
 }

 @Test(expected = JsonProcessingException.class)
 public void testEmptyRootValueSeparator() throws JsonProcessingException {
 JsonGenerator generator = jsonFactory.createGenerator(System.out);
 DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter("");
 prettyPrinter.writeRootValueSeparator(generator);
 prettyPrinter.writeObjectValue(generator, 123);
 prettyPrinter.writeEndObject(generator);
 generator.close();
 }

 @Test(expected = JsonProcessingException.class)
 public void testInvalidRootValueSeparator() throws JsonProcessingException {
 JsonGenerator generator = jsonFactory.createGenerator(System.out);
 DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter(new SerializedString("abc"));
 prettyPrinter.writeRootValueSeparator(generator);
 prettyPrinter.writeObjectValue(generator, 123);
 prettyPrinter.writeEndObject(generator);
 generator.close();
 }

 @Test(expected = JsonProcessingException.class)
 public void testNegativeNestingLevel() throws JsonProcessingException {
 JsonGenerator generator = jsonFactory.createGenerator(System.out);
 DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
 prettyPrinter.writeStartObject(generator);
 try {
 prettyPrinter.writeStartObject(generator);
 fail("Should throw an exception");
 } catch (JsonProcessingException e) {
 // Expected
 }
 generator.close();
 }
}