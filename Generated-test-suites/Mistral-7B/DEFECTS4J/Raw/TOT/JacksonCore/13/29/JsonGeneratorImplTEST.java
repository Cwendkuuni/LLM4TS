import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.io.CharTypes;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class JsonGeneratorImplTest {

 private JsonGenerator generator;

 @Before
 public void setup() throws IOException {
 JsonFactory factory = new JsonFactory();
 generator = factory.createGenerator(System.out);
 }

 @Test
 public void testWriteStringField() throws IOException {
 // Test writing a string field with a valid name and value
 generator.writeStartObject();
 generator.writeFieldName("testField");
 generator.writeString("testValue");
 generator.writeEndObject();
 assertEquals("{\"testField\":\"testValue\"}", generator.getOutputContentAsString());

 // Test writing a string field with an empty name
 generator.writeStartObject();
 generator.writeFieldName("");
 generator.writeString("testValue");
 generator.writeEndObject();
 assertEquals("{\"\":\"testValue\"}", generator.getOutputContentAsString());

 // Test writing a string field with a null name
 generator.writeStartObject();
 generator.writeFieldName(null);
 generator.writeString("testValue");
 generator.writeEndObject();
 assertThrows(IllegalArgumentException.class, () -> generator.getOutputContentAsString());

 // Test writing a string field with a name containing invalid characters
 generator.writeStartObject();
 generator.writeFieldName("testField\""); // invalid character in name
 generator.writeString("testValue");
 generator.writeEndObject();
 assertThrows(IllegalArgumentException.class, () -> generator.getOutputContentAsString());

 // Test writing a string field with an empty value
 generator.writeStartObject();
 generator.writeFieldName("testField");
 generator.writeString("");
 generator.writeEndObject();
 assertEquals("{\"testField\":\"\"}", generator.getOutputContentAsString());

 // Test writing a string field with a null value
 generator.writeStartObject();
 generator.writeFieldName("testField");
 generator.writeNull();
 generator.writeEndObject();
 assertEquals("{\"testField\":null}", generator.getOutputContentAsString());
 }

 @Test
 public void testSetHighestNonEscapedChar() throws IOException {
 // Test setting the highest non-escaped character code to 127 (default)
 generator.writeStartObject();
 generator.writeFieldName("testField1");
 generator.writeString("testValue1");
 generator.writeFieldName("testField2");
 generator.writeString("\u007FtestValue2"); // valid character, no escaping
 generator.writeEndObject();
 assertEquals("{\"testField1\":\"testValue1\",\"testField2\":\"\\u007FtestValue2\"}", generator.getOutputContentAsString());

 // Test setting the highest non-escaped character code to a higher value
 generator.setHighestNonEscapedChar(255);
 generator.writeStartObject();
 generator.writeFieldName("testField1");
 generator.writeString("testValue1");
 generator.writeFieldName("testField2");
 generator.writeString("\u00FFtestValue2"); // valid character, no escaping
 generator.writeEndObject();
 assertEquals("{\"testField1\":\"testValue1\",\"testField2\":\"\\u00FFtestValue2\"}", generator.getOutputContentAsString());

 // Test setting the highest non-escaped character code to 0 (all characters need escaping)
 generator.setHighestNonEscapedChar(0);
 generator.writeStartObject();
 generator.writeFieldName("testField1");
 generator.writeString("testValue1");
 generator.writeFieldName("testField2");
 generator.writeString("testValue2"); // all characters need escaping
 generator.writeEndObject();
 assertEquals("{\"testField1\":\"testValue1\",\"testField2\":\"testValue2\"}", generator.getOutputContentAsString());
 }
}