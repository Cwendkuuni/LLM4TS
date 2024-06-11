import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Test;

import static org.junit.Assert.*;

public class JsonTreeWriterTest {

   @Test
   public void testGet() {
       JsonTreeWriter jsonWriter = new JsonTreeWriter();
       assertNotNull(jsonWriter.get());
   }

   @Test
   public void testBeginArray() {
       JsonTreeWriter jsonWriter = new JsonTreeWriter();
       assertNotNull(jsonWriter.beginArray());
   }

   @Test
   public void testEndArray() {
       JsonTreeWriter jsonWriter = new JsonTreeWriter();
       assertThrows(IllegalStateException.class, jsonWriter::endArray);
   }

   @Test
   public void testBeginObject() {
       JsonTreeWriter jsonWriter = new JsonTreeWriter();
       assertNotNull(jsonWriter.beginObject());
   }

   @Test
   public void testEndObject() {
       JsonTreeWriter jsonWriter = new JsonTreeWriter();
       assertThrows(IllegalStateException.class, jsonWriter::endObject);
   }

   @Test
   public void testName() {
       JsonTreeWriter jsonWriter = new JsonTreeWriter();
       assertThrows(IllegalStateException.class, () -> jsonWriter.name("test"));
   }

   @Test
   public void testValueString() {
       JsonTreeWriter jsonWriter = new JsonTreeWriter();
       assertNotNull(jsonWriter.value("test"));
   }

   @Test
   public void testNullValue() {
       JsonTreeWriter jsonWriter = new JsonTreeWriter();
       assertNotNull(jsonWriter.nullValue());
   }

   @Test
   public void testValueBoolean() {
       JsonTreeWriter jsonWriter = new JsonTreeWriter();
       assertNotNull(jsonWriter.value(true));
   }

   @Test
   public void testValueDouble() {
       JsonTreeWriter jsonWriter = new JsonTreeWriter();
       assertThrows(IllegalArgumentException.class, () -> jsonWriter.value(Double.NaN));
   }

   @Test
   public void testValueLong() {
       JsonTreeWriter jsonWriter = new JsonTreeWriter();
       assertNotNull(jsonWriter.value(10L));
   }

   @Test
   public void testValueNumber() {
       JsonTreeWriter jsonWriter = new JsonTreeWriter();
       assertThrows(IllegalArgumentException.class, () -> jsonWriter.value(Double.POSITIVE_INFINITY));
   }

   @Test
   public void testFlush() {
       JsonTreeWriter jsonWriter = new JsonTreeWriter();
       assertDoesNotThrow(jsonWriter::flush);
   }

   @Test
   public void testClose() {
       JsonTreeWriter jsonWriter = new JsonTreeWriter();
       assertThrows(IOException.class, jsonWriter::close);
   }
}