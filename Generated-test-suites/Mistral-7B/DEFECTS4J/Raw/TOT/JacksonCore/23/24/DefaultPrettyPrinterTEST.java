import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultPrettyPrinterTest {

 @Test
 public void testDefaultPrettyPrinter() throws IOException {
 ObjectWriter objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();

 // Test JSON object with root separator
 String jsonObjectWithRootSeparator = "{\"name\": \"John Doe\", \"age\": 30}";
 String prettyJsonObjectWithRootSeparator = "name John Doe\nage 30";
 String result = objectWriter.writeValueAsString(new ObjectNode(JSON.parseObject(jsonObjectWithRootSeparator)));
 assertEquals(prettyJsonObjectWithRootSeparator, result);

 // Test JSON array with indentation
 String jsonArray = "[1, 2, 3, 4]";
 String prettyJsonArray = "[1\n  2\n  3\n  4]";
 ObjectNode arrayNode = new ObjectNode(JSON.parseObject(jsonArray));
 arrayNode.set("_", new ObjectNode().putPOJOInField("array", new int[]{1, 2, 3, 4}));
 result = objectWriter.writeValueAsString(arrayNode);
 assertEquals(prettyJsonArray, result);
 }

 @Test(expected = IOException.class)
 public void testNullInput() throws IOException {
 ObjectWriter objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();
 ObjectNode nullInput = null;
 objectWriter.writeValueAsString(nullInput);
 fail("Expected IOException when null input is provided");
 }
}