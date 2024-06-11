import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.io.SerializedString;
import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultPrettyPrinterTest {

 private DefaultPrettyPrinter prettyPrinter;

 @Before
 public void setup() {
 prettyPrinter = new DefaultPrettyPrinter();
 }

 // Test constructor with no arguments
 @Test
 public void testDefaultConstructor() {
 JsonGenerator jsonGenerator = createMockJsonGenerator();
 prettyPrinter.writeRootValueSeparator(jsonGenerator);
 assertEquals("", jsonGenerator.getOutputBuffer().toString());
 }

 // Test constructor with root separator argument
 @Test
 public void testConstructorWithRootSeparator() {
 JsonGenerator jsonGenerator = createMockJsonGenerator();
 String rootSeparator = "test";
 prettyPrinter = new DefaultPrettyPrinter(new SerializedString(rootSeparator));
 prettyPrinter.writeRootValueSeparator(jsonGenerator);
 assertEquals(rootSeparator, jsonGenerator.getOutputBuffer().toString());
 }

 // Test copy constructor
 @Test
 public void testCopyConstructor() {
 JsonGenerator jsonGenerator = createMockJsonGenerator();
 DefaultPrettyPrinter original = new DefaultPrettyPrinter(jsonGenerator, null);
 DefaultPrettyPrinter copy = new DefaultPrettyPrinter(original);
 assertEquals(original._objectIndenter, copy._objectIndenter);
 assertEquals(original._arrayIndenter, copy._arrayIndenter);
 assertEquals(original._spacesInObjectEntries, copy._spacesInObjectEntries);
 assertEquals(original._nesting, copy._nesting);
 assertEquals(original._rootSeparator, copy._rootSeparator);
 }

 // Test withRootSeparator method
 @Test
 public void testWithRootSeparator() {
 JsonGenerator jsonGenerator = createMockJsonGenerator();
 String rootSeparator = "test";
 prettyPrinter = new DefaultPrettyPrinter(jsonGenerator, null);
 prettyPrinter = prettyPrinter.withRootSeparator(new SerializedString(rootSeparator));
 prettyPrinter.writeRootValueSeparator(jsonGenerator);
 assertEquals(rootSeparator, jsonGenerator.getOutputBuffer().toString());
 }

 // Test withArrayIndenter method
 @Test
 public void testWithArrayIndenter() {
 JsonGenerator jsonGenerator = createMockJsonGenerator();
 Indenter indenter = new NopIndenter();
 prettyPrinter = new DefaultPrettyPrinter(jsonGenerator, null);
 prettyPrinter.indentArraysWith(indenter);
 prettyPrinter.writeStartArray(jsonGenerator);
 assertEquals(0, jsonGenerator.getOutputBuffer().length());
 }

 // Test withObjectIndenter method
 @Test
 public void testWithObjectIndenter() {
 JsonGenerator jsonGenerator = createMockJsonGenerator();
 Indenter indenter = new NopIndenter();
 prettyPrinter = new DefaultPrettyPrinter(jsonGenerator, null);
 prettyPrinter.indentObjectsWith(indenter);
 prettyPrinter.writeStartObject(jsonGenerator);
 assertEquals(0, jsonGenerator.getOutputBuffer().length());
 }

 // Test withSpacesInObjectEntries method
 @Test
 public void testWithSpacesInObjectEntries() {
 JsonGenerator jsonGenerator = createMockJsonGenerator();
 prettyPrinter = new DefaultPrettyPrinter(jsonGenerator, null);
 boolean spacesInObjectEntries = true;
 DefaultPrettyPrinter newPrettyPrinter = prettyPrinter.withSpacesInObjectEntries();
 assertEquals(spacesInObjectEntries, newPrettyPrinter._spacesInObjectEntries);
 }

 // Test writeStartObject method
 @Test
 public void testWriteStartObject() {
 JsonGenerator jsonGenerator = createMockJsonGenerator();
 prettyPrinter.writeStartObject(jsonGenerator);
 assertEquals("{\n", jsonGenerator.getOutputBuffer().toString());
 }

 // Test writeEndObject method with no entries
 @Test
 public void testWriteEndObjectWithNoEntries() {
 JsonGenerator jsonGenerator = createMockJsonGenerator();
 prettyPrinter.writeStartObject(jsonGenerator);
 prettyPrinter.writeEndObject(jsonGenerator, 0);
 assertEquals("}", jsonGenerator.getOutputBuffer().toString());
 }

 // Test writeEndObject method with entries
 @Test
 public void testWriteEndObjectWithEntries() {
 JsonGenerator jsonGenerator = createMockJsonGenerator();
 prettyPrinter.writeStartObject(jsonGenerator);
 String fieldName = "testField";
 Object fieldValue = "testValue";
 prettyPrinter.writeObjectFieldName(jsonGenerator, fieldName);
 prettyPrinter.writeObjectValue(jsonGenerator, fieldValue);
 prettyPrinter.writeEndObject(jsonGenerator, 1);
 assertEquals("{\n  \"" + fieldName + "\": " + fieldValue + "\n}", jsonGenerator.getOutputBuffer().toString());
 }

 // Test writeStartArray method
 @Test
 public void testWriteStartArray() {
 JsonGenerator jsonGenerator = createMockJsonGenerator();
 prettyPrinter.writeStartArray(jsonGenerator);
 assertEquals("[", jsonGenerator.getOutputBuffer().toString());
 }

 // Test writeEndArray method with no values
 @Test
 public void testWriteEndArrayWithNoValues() {
 JsonGenerator jsonGenerator = createMockJsonGenerator();
 prettyPrinter.writeStartArray(jsonGenerator);
 prettyPrinter.writeEndArray(jsonGenerator, 0);
 assertEquals("]", jsonGenerator.getOutputBuffer().toString());
 }

 // Test writeEndArray method with values
 @Test
 public void testWriteEndArrayWithValues() {
 JsonGenerator jsonGenerator = createMockJsonGenerator();
 Object value1 = "value1";
 Object value2 = "value2";
 prettyPrinter.writeStartArray(jsonGenerator);
 prettyPrinter.writeValue(jsonGenerator, value1);
 prettyPrinter.writeValue(jsonGenerator, value2);
 prettyPrinter.writeEndArray(jsonGenerator, 2);
 assertEquals("[\"value1\", \"value2\"]", jsonGenerator.getOutputBuffer().toString());
 }

 // Test writeObjectEntrySeparator method
 @Test
 public void testWriteObjectEntrySeparator() {
 JsonGenerator jsonGenerator = createMockJsonGenerator();
 prettyPrinter.writeStartObject(jsonGenerator);
 String fieldName = "testField";
 prettyPrinter.writeObjectFieldName(jsonGenerator, fieldName);
 assertEquals("{\n  \"" + fieldName + "\":", jsonGenerator.getOutputBuffer().toString());
 prettyPrinter.writeObjectValueSeparator(jsonGenerator);
 assertEquals("{\n  \"" + fieldName + "\": ", jsonGenerator.getOutputBuffer().toString());
 prettyPrinter.writeObjectValue(jsonGenerator, "testValue");
 assertEquals("{\n  \"" + fieldName + "\": \"testValue\"", jsonGenerator.getOutputBuffer().toString());
 prettyPrinter.writeObjectEntrySeparator(jsonGenerator);
 assertEquals("{\n  \"" + fieldName + "\": \"testValue\",", jsonGenerator.getOutputBuffer().toString());
 prettyPrinter.writeEndObject(jsonGenerator, 1);
 assertEquals("{\n  \"" + fieldName + "\": \"testValue\"\n}", jsonGenerator.getOutputBuffer().toString());
 }

 // Test writeArrayValueSeparator method
 @Test
 public void testWriteArrayValueSeparator() {
 JsonGenerator jsonGenerator = createMockJsonGenerator();
 Object value1 = "value1";
 Object value2 = "value2";
 prettyPrinter.writeStartArray(jsonGenerator);
 prettyPrinter.writeValue(jsonGenerator, value1);
 assertEquals("[\"value1\"", jsonGenerator.getOutputBuffer().toString());
 prettyPrinter.writeArrayValueSeparator(jsonGenerator);
 assertEquals("[\"value1\"", jsonGenerator.getOutputBuffer().toString());
 prettyPrinter.writeValue(jsonGenerator, value2);
 assertEquals("[\"value1\", \"value2\"]", jsonGenerator.getOutputBuffer().toString());
 }

 // Edge case: writeObjectFieldValueSeparator with empty string
 @Test
 public void testWriteObjectFieldValueSeparatorWithEmptyString() {
 JsonGenerator jsonGenerator = createMockJsonGenerator();
 String fieldName = "testField";
 String emptyString = "";
 prettyPrinter = new DefaultPrettyPrinter(jsonGenerator, null);
 prettyPrinter._spacesInObjectEntries = false;
 prettyPrinter.writeStartObject(jsonGenerator);
 prettyPrinter.writeObjectFieldName(jsonGenerator, fieldName);
 prettyPrinter.writeObjectFieldValueSeparator(jsonGenerator);
 assertEquals("{\n  \"" + fieldName + "\":", jsonGenerator.getOutputBuffer().toString());
 prettyPrinter.writeObjectValue(jsonGenerator, emptyString);
 assertEquals("{\n  \"" + fieldName + "\": \"" + emptyString + "\"", jsonGenerator.getOutputBuffer().toString());
 prettyPrinter.writeObjectEntrySeparator(jsonGenerator);
 assertEquals("{\n  \"" + fieldName + "\": \"" + emptyString + "\",", jsonGenerator.getOutputBuffer().toString());
 prettyPrinter.writeEndObject(jsonGenerator, 1);
 assertEquals("{\n  \"" + fieldName + "\": \"" + emptyString + "\"\n}", jsonGenerator.getOutputBuffer().toString());
 }

 // Edge case: writeObjectFieldValueSeparator with null value
 @Test
 public void testWriteObjectFieldValueSeparatorWithNullValue() {
 JsonGenerator jsonGenerator = createMockJsonGenerator();
 String fieldName = "testField";
 prettyPrinter = new DefaultPrettyPrinter(jsonGenerator, null);
 prettyPrinter._spacesInObjectEntries = false;
 prettyPrinter.writeStartObject(jsonGenerator);
 prettyPrinter.writeObjectFieldName(jsonGenerator, fieldName);
 prettyPrinter.writeObjectFieldValueSeparator(jsonGenerator);
 assertEquals("{\n  \"" + fieldName + "\":", jsonGenerator.getOutputBuffer().toString());
 prettyPrinter.writeObjectValue(jsonGenerator, null);
 assertEquals("{\n  \"" + fieldName + "\": null", jsonGenerator.getOutputBuffer().toString());
 prettyPrinter.writeObjectEntrySeparator(jsonGenerator);
 assertEquals("{\n  \"" + fieldName + "\": null,\n}", jsonGenerator.getOutputBuffer().toString());
 prettyPrinter.writeEndObject(jsonGenerator, 1);
 assertEquals("{\n  \"" + fieldName + "\": null\n}", jsonGenerator.getOutputBuffer().toString());
 }

 // Exception handling: writeObjectFieldValueSeparator with SerializableString argument that is null
 @Test(expected = IllegalArgumentException.class)
 public void testWriteObjectFieldValueSeparatorWithNullSerializedString() {
 JsonGenerator jsonGenerator = createMockJsonGenerator();
 String fieldName = "testField";
 DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter(jsonGenerator, null);
 prettyPrinter.writeObjectFieldValueSeparator(jsonGenerator, null);
 }

 // Exception handling: writeObjectEntrySeparator with SerializableString argument that is null
 @Test(expected = IllegalArgumentException.class)
 public void testWriteObjectEntrySeparatorWithNullSerializedString() {
 JsonGenerator jsonGenerator = createMockJsonGenerator();
 String fieldName = "testField";
 DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter(jsonGenerator, null);
 prettyPrinter.writeObjectEntrySeparator(jsonGenerator, null);
 }
}