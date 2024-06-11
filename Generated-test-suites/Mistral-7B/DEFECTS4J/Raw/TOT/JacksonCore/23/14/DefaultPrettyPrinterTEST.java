import org.junit.Test;
import static org.junit.Assert.assertEquals;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import java.io.StringWriter;

public class DefaultPrettyPrinterTest {

 @Test
 public void testDefaultPrettyPrinter() throws IOException {
 JsonFactory jsonFactory = new JsonFactory();
 DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
 ObjectMapper mapper = new ObjectMapper(prettyPrinter);

 // Test empty JSON object
 StringWriter sw1 = new StringWriter();
 mapper.writeValue(sw1, new Object());
 assertEquals("{\n}", sw1.toString());

 // Test JSON object with one field
 StringWriter sw2 = new StringWriter();
 Map<String, Object> jsonObject = new HashMap<>();
 jsonObject.put("field1", "value1");
 mapper.writeValue(sw2, jsonObject);
 assertEquals("{\n  \"field1\": \"value1\"\n}", sw2.toString());

 // Test JSON object with multiple fields
 StringWriter sw3 = new StringWriter();
 jsonObject.put("field1", "value1");
 jsonObject.put("field2", "value2");
 mapper.writeValue(sw3, jsonObject);
 assertEquals("{\n  \"field1\": \"value1\"\n,  \"field2\": \"value2\"\n}", sw3.toString());

 // Test JSON array with one element
 StringWriter sw4 = new StringWriter();
 Object jsonArray = new Object[1];
 jsonArray[0] = "element1";
 mapper.writeValue(sw4, jsonArray);
 assertEquals("[\n  \"element1\"\n]", sw4.toString());

 // Test JSON array with multiple elements
 StringWriter sw5 = new StringWriter();
 Object jsonArray2 = new Object[2];
 jsonArray2[0] = "element1";
 jsonArray2[1] = "element2";
 mapper.writeValue(sw5, jsonArray2);
 assertEquals("[\n  \"element1\"\n,  \"element2\"\n]", sw5.toString());

 // Test JSON object with root separator
 JsonFactory jsonFactoryWithRootSeparator = new JsonFactory(new DefaultPrettyPrinter(new SerializedString(": ")));
 StringWriter sw6 = new StringWriter();
 Map<String, Object> jsonObjectWithRootSeparator = new HashMap<>();
 jsonObjectWithRootSeparator.put("field1", "value1");
 ObjectMapper mapperWithRootSeparator = new ObjectMapper(jsonFactoryWithRootSeparator);
 mapperWithRootSeparator.writeValue(sw6, jsonObjectWithRootSeparator);
 assertEquals(":\n{\n  \"field1\": \"value1\"\n}", sw6.toString());
 }
}