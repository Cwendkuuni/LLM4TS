import org.apache.commons.csv.CSVRecord;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CSVRecordTest {

 @Test
 public void testGetByIndex() {
 CSVRecord record = new CSVRecord(new String[]{"Name", "Age", "City"}, null, null, 1);
 record.values = new String[]{"John Doe", "30", "New York"};

 String value = record.get(0);
 assertTrue(value.equals("Name"));
 value = record.get(1);
 assertTrue(value.equals("John Doe"));
 value = record.get(2);
 assertTrue(value.equals("30"));
 }

 @Test
 public void testGetByNullIndex() {
 CSVRecord record = new CSVRecord(new String[]{"Name", "Age", "City"}, null, null, 1);
 record.values = new String[]{"Name", "Age", "City"};

 try {
 record.get(-1);
 } catch (ArrayIndexOutOfBoundsException e) {
 // expected
 }
 }

 @Test
 public void testGetByNameNullMapping() {
 CSVRecord record = new CSVRecord(new String[]{"Name", "Age", "City"}, null, null, 1);
 record.values = new String[]{"Name", "Age", "City"};

 String value = record.get("Name");
 assertTrue(value.equals("Name"));
 value = record.get("Age");
 assertTrue(value.equals("Age"));
 value = record.get("City");
 assertTrue(value.equals("City"));

 value = record.get("Unknown");
 assertNull(value);
 }

 @Test
 public void testGetByNameNullValue() {
 CSVRecord record = new CSVRecord(new String[]{"Name", "Age", "City"}, null, null, 1);
 record.values = new String[]{"Name", "Age", "City"};
 record.mapping = new java.util.HashMap<String, Integer>();
 record.mapping.put("Name", 0);
 record.mapping.put("Age", 1);
 record.mapping.put("City", 2);

 String value = record.get("Name");
 assertTrue(value.equals("Name"));
 value = record.get("Age");
 assertTrue(value.equals("Age"));
 value = record.get("City");
 assertTrue(value.equals("City"));

 value = record.get("Unknown");
 assertNull(value);
 }

 @Test
 public void testIsConsistentTrue() {
 CSVRecord record = new CSVRecord(new String[]{"Name", "Age", "City"}, null, null, 1);
 record.values = new String[]{"Name", "Age", "City"};

 assertTrue(record.isConsistent());
 }

 @Test
 public void testIsConsistentFalse() {
 CSVRecord record = new CSVRecord(new String[]{"Name", "Age", "City"}, null, null, 1);
 record.values = new String[]{"Name", "Age", "City", "Comment"};

 assertFalse(record.isConsistent());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetByNameNullMappingAndSet() {
 CSVRecord record = new CSVRecord(new String[]{"Name", "Age", "City"}, null, null, 1);
 record.values = new String[]{"Name", "Age", "City"};

 record.get("Name");
 record.get("Unknown");
 }

 @Test(expected = IllegalStateException.class)
 public void testGetByNameInvalidIndex() {
 CSVRecord record = new CSVRecord(new String[]{"Name", "Age", "City"}, null, null, 1);
 record.values = new String[]{"Name", "Age", "City"};
 record.mapping = new java.util.HashMap<String, Integer>();
 record.mapping.put("Name", 0);
 record.mapping.put("Age", 1);
 record.mapping.put("City", 2);

 record.get("Unknown");
 }

 @Test
 public void testIsMappedTrue() {
 CSVRecord record = new CSVRecord(new String[]{"Name", "Age", "City"}, null, null, 1);
 record.values = new String[]{"Name", "Age", "City"};
 record.mapping = new java.util.HashMap<String, Integer>();
 record.mapping.put("Name", 0);
 record.mapping.put("Age", 1);
 record.mapping.put("City", 2);

 assertTrue(record.isMapped("Name"));
 assertTrue(record.isMapped("Age"));
 assertTrue(record.isMapped("City"));
 }

 @Test
 public void testIsMappedFalse() {
 CSVRecord record = new CSVRecord(new String[]{"Name", "Age", "City"}, null, null, 1);
 record.values = new String[]{"Name", "Age", "City"};

 assertFalse(record.isMapped("Unknown"));
 }

 @Test
 public void testIsSetTrue() {
 CSVRecord record = new CSVRecord(new String[]{"Name", "Age", "City"}, null, null, 1);
 record.values = new String[]{"Name", "Age", "City"};
 record.mapping = new java.util.HashMap<String, Integer>();
 record.mapping.put("Name", 0);
 record.mapping.put("Age", 1);
 record.mapping.put("City", 2);

 assertTrue(record.isSet("Name"));
 assertTrue(record.isSet("Age"));
 assertTrue(record.isSet("City"));
 }

 @Test
 public void testIsSetFalse() {
 CSVRecord record = new CSVRecord(new String[]{"Name", "Age", "City"}, null, null, 1);
 record.values = new String[]{"Name", "Age", "City", "Comment"};
 record.mapping = new java.util.HashMap<String, Integer>();
 record.mapping.put("Name", 0);
 record.mapping.put("Age", 1);
 record.mapping.put("City", 2);
 record.mapping.put("Comment", 3);

 assertFalse(record.isSet("Comment"));
 }

}