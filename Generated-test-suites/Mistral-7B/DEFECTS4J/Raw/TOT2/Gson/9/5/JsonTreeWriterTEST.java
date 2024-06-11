import static org.junit.Assert.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Test;

public class JsonTreeWriterTest {

 JsonTreeWriterBasicTest jsonTreeWriterBasicTest = new JsonTreeWriterBasicTest();
 JsonTreeWriterEdgeCases jsonTreeWriterEdgeCases = new JsonTreeWriterEdgeCases();

 @Test
 public void testJsonTreeWriterBasic() {
 jsonTreeWriterBasicTest.runTests();
 }

 @Test(expected = IllegalStateException.class)
 public void testJsonTreeWriterWithNullInput_String() {
 JsonTreeWriter writer = new JsonTreeWriter();
 writer.value((String) null);
 }

 @Test(expected = IllegalStateException.class)
 public void testJsonTreeWriterWithNullInput_Boolean() {
 JsonTreeWriter writer = new JsonTreeWriter();
 writer.value((Boolean) null);
 }

 @Test(expected = IllegalStateException.class)
 public void testJsonTreeWriterWithNullInput_Double() {
 JsonTreeWriter writer = new JsonTreeWriter();
 writer.value((Double) null);
 }

 @Test(expected = IllegalStateException.class)
 public void testJsonTreeWriterWithNullInput_Long() {
 JsonTreeWriter writer = new JsonTreeWriter();
 writer.value((Long) null);
 }

 @Test(expected = IllegalStateException.class)
 public void testJsonTreeWriterWithNullInput_Number() {
 JsonTreeWriter writer = new JsonTreeWriter();
 writer.value((Number) null);
 }

 @Test
 public void testJsonTreeWriterWithoutClose() {
 JsonTreeWriter writer = new JsonTreeWriter();
 writer.beginObject();
 writer.name("name");
 writer.value("John");
 writer.endArray(); // This should cause an error
 }

 @Test
 public void testJsonTreeWriterNameDuplication() {
 JsonTreeWriter writer = new JsonTreeWriter();
 writer.beginObject();
 writer.name("name");
 writer.value("John");
 writer.name("name");
 writer.value("Mary");
 writer.endObject();
 JsonObject jsonObject = writer.get();
 JsonArray jsonArray = jsonObject.getAsJsonArray();
 assertEquals(1, jsonArray.size());
 JsonObject jsonName = jsonArray.get(0).getAsJsonObject();
 assertEquals(2, jsonName.size());
 }

 @Test(expected = IllegalStateException.class)
 public void testJsonTreeWriterCloseTwice() {
 JsonTreeWriter writer = new JsonTreeWriter();
 writer.beginObject();
 writer.name("name");
 writer.value("John");
 writer.endObject();
 writer.close();
 writer.close();
 }

 public void runTests() {
 // Run all the basic tests
 testBeginArray();
 testEndArray();
 testBeginObject();
 testEndObject();
 testName();
 testValueString();
 testValueBoolean();
 testValueDouble();
 testValueLong();
 testValueNumber();
 }
}