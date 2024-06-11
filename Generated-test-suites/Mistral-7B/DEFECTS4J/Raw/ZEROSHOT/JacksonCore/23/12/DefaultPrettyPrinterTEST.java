import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;

public class DefaultPrettyPrinterTest {

 private JsonFactory jsonFactory;
 private JsonGenerator jsonGenerator;
 private DefaultPrettyPrinter prettyPrinter;

 @Before
 public void setUp() throws IOException {
 jsonFactory = new JsonFactory();
 jsonGenerator = jsonFactory.createGenerator(new ByteArrayOutputStream(), prettyPrinter);
 prettyPrinter = new DefaultPrettyPrinter();
 }

 @Test
 public void testDefaultRootValueSeparator() throws IOException {
 prettyPrinter.writeRootValueSeparator(jsonGenerator);
 assertEquals("", jsonGenerator.getOutputBuffer().toString());
 }

 @Test
 public void testWriteStartObject() throws IOException {
 prettyPrinter.writeStartObject(jsonGenerator);
 assertEquals("{", jsonGenerator.getOutputBuffer().toString());
 }

 @Test
 public void testWriteEndObject() throws IOException {
 prettyPrinter.writeEndObject(jsonGenerator, 1);
 assertEquals("}", jsonGenerator.getOutputBuffer().toString());
 }

 @Test
 public void testWriteObjectEntrySeparator() throws IOException {
 prettyPrinter.writeObjectEntrySeparator(jsonGenerator);
 assertEquals(" :", jsonGenerator.getOutputBuffer().toString());
 }

 @Test
 public void testWriteObjectFieldValueSeparator() throws IOException {
 prettyPrinter.writeObjectFieldValueSeparator(jsonGenerator);
 assertEquals(" :", jsonGenerator.getOutputBuffer().toString());
 }

 @Test
 public void testWriteStartArray() throws IOException {
 prettyPrinter.writeStartArray(jsonGenerator);
 assertEquals("[", jsonGenerator.getOutputBuffer().toString());
 }

 @Test
 public void testWriteEndArray() throws IOException {
 prettyPrinter.writeEndArray(jsonGenerator, 1);
 assertEquals("]", jsonGenerator.getOutputBuffer().toString());
 }

 @Test
 public void testWriteArrayValueSeparator() throws IOException {
 prettyPrinter.writeArrayValueSeparator(jsonGenerator);
 assertEquals(",", jsonGenerator.getOutputBuffer().toString());
 }

 @Test
 public void testWriteRootValueSeparatorWithSeparator() throws IOException {
 prettyPrinter = new DefaultPrettyPrinter(":");
 prettyPrinter.writeRootValueSeparator(jsonGenerator);
 assertEquals(":", jsonGenerator.getOutputBuffer().toString());
 }

 @Test
 public void testIndentArraysWith() throws IOException {
 DefaultPrettyPrinter newPrettyPrinter = prettyPrinter.withArrayIndenter(new FixedSpaceIndenter());
 newPrettyPrinter.writeStartArray(jsonGenerator);
 newPrettyPrinter.writeArrayValueSeparator(jsonGenerator);
 newPrettyPrinter.writeStartArray(jsonGenerator);
 newPrettyPrinter.writeEndArray(jsonGenerator, 1);
 newPrettyPrinter.writeEndArray(jsonGenerator, 1);
 assertEquals("[ [],]", jsonGenerator.getOutputBuffer().toString());
 }

 @Test
 public void testIndentObjectsWith() throws IOException {
 DefaultPrettyPrinter newPrettyPrinter = prettyPrinter.withObjectIndenter(new DefaultIndenter.SYSTEM_LINEFEED_INSTANCE());
 newPrettyPrinter.writeStartObject(jsonGenerator);
 newPrettyPrinter.writeObjectFieldValueSeparator(jsonGenerator);
 newPrettyPrinter.writeObjectFieldName("name", jsonGenerator);
 newPrettyPrinter.writeObjectFieldValueSeparator(jsonGenerator);
 newPrettyPrinter.writeObjectFieldValue("John Doe", jsonGenerator);
 newPrettyPrinter.writeObjectEntrySeparator(jsonGenerator);
 newPrettyPrinter.writeObjectFieldName("age", jsonGenerator);
 newPrettyPrinter.writeObjectFieldValueSeparator(jsonGenerator);
 newPrettyPrinter.writeObjectFieldValue("30", jsonGenerator);
 newPrettyPrinter.writeEndObject(jsonGenerator, 2);
 assertEquals("{\n  \"name\": \"John Doe\",\n  \"age\": 30\n}", jsonGenerator.getOutputBuffer().toString());
 }

 @Test
 public void testWithSpacesInObjectEntries() throws IOException {
 DefaultPrettyPrinter newPrettyPrinter = prettyPrinter.withSpacesInObjectEntries();
 newPrettyPrinter.writeStartObject(jsonGenerator);
 newPrettyPrinter.writeObjectFieldValueSeparator(jsonGenerator);
 newPrettyPrinter.writeObjectFieldName("name", jsonGenerator);
 newPrettyPrinter.writeObjectFieldValueSeparator(jsonGenerator);
 newPrettyPrinter.writeObjectFieldValue("John Doe", jsonGenerator);
 newPrettyPrinter.writeObjectEntrySeparator(jsonGenerator);
 newPrettyPrinter.writeObjectFieldName("age", jsonGenerator);
 newPrettyPrinter.writeObjectFieldValueSeparator(jsonGenerator);
 newPrettyPrinter.writeObjectFieldValue("30", jsonGenerator);
 newPrettyPrinter.writeEndObject(jsonGenerator, 2);
 assertEquals("{\n  \"name\": \"John Doe\",\n  \"age\": 30\n}", jsonGenerator.getOutputBuffer().toString());
 }

 @Test
 public void testWithSeparators() throws IOException {
 DefaultPrettyPrinter newPrettyPrinter = new DefaultPrettyPrinter();
 Separators separators = new Separators(",", " :", ",\n");
 newPrettyPrinter.withSeparators(separators);
 newPrettyPrinter.writeStartArray(jsonGenerator);
 newPrettyPrinter.writeArrayValueSeparator(jsonGenerator);
 newPrettyPrinter.writeStartArray(jsonGenerator);
 newPrettyPrinter.writeEndArray(jsonGenerator, 1);
 newPrettyPrinter.writeEndArray(jsonGenerator, 1);
 assertEquals("[ [],]", jsonGenerator.getOutputBuffer().toString());
 }
}