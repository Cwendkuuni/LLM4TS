import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

public class CSVRecordTest {

 private CSVRecord record;

 @Test
 public void testCSVRecordConstructor() {
 String[] values = new String[]{"Name", "Age", "City"};
 Map<String, Integer> mapping = Map.of("Name", 0, "Age", 1, "City", 2);
 String comment = "Record Comment";
 long recordNumber = 1;
 record = new CSVRecord(values, mapping, comment, recordNumber);

 Assert.assertEquals("Name", record.get(0));
 Assert.assertEquals("Age", record.get(1));
 Assert.assertEquals("City", record.get(2));
 Assert.assertEquals(comment, record.getComment());
 Assert.assertEquals(recordNumber, record.getRecordNumber());
 Assert.assertEquals(3, record.size());
 }

 @Test
 public void testGetMethodWithValidIndex() {
 record = new CSVRecord(new String[]{"Name", "Age", "City"}, null, null, 1);
 Assert.assertEquals("Name", record.get(0));
 Assert.assertEquals("Age", record.get(1));
 Assert.assertEquals("City", record.get(2));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetMethodWithInvalidIndex() {
 record = new CSVRecord(new String[]{"Name", "Age", "City"}, null, null, 1);
 record.get(3);
 }

 @Test
 public void testGetMethodWithValidName() {
 record = new CSVRecord(new String[]{"Name", "Age", "City"}, Map.of("Name", 0, "Age", 1, "City", 2), null, 1);
 Assert.assertEquals("Name", record.get("Name"));
 Assert.assertEquals("Age", record.get("Age"));
 Assert.assertEquals("City", record.get("City"));
 Assert.assertNull(record.get("UnknownColumnName"));
 }

 @Test(expected = IllegalStateException.class)
 public void testGetMethodWithNoMapping() {
 record = new CSVRecord(new String[]{"Name", "Age", "City"}, null, null, 1);
 record.get("Name");
 }

 @Test
 public void testIsConsistentMethodWithValidRecord() {
 record = new CSVRecord(new String[]{"Name", "Age", "City"}, Map.of("Name", 0, "Age", 1, "City", 2), null, 1);
 Assert.assertTrue(record.isConsistent());
 }

 @Test
 public void testIsConsistentMethodWithInvalidRecord() {
 record = new CSVRecord(new String[]{"Name", "Age", "City"}, Map.of("Name", 0, "Age", 1), null, 1);
 Assert.assertFalse(record.isConsistent());
 }

 @Test
 public void testIsMappedMethodWithValidName() {
 record = new CSVRecord(new String[]{"Name", "Age", "City"}, Map.of("Name", 0, "Age", 1, "City", 2), null, 1);
 Assert.assertTrue(record.isMapped("Name"));
 Assert.assertTrue(record.isMapped("Age"));
 Assert.assertTrue(record.isMapped("City"));
 Assert.assertFalse(record.isMapped("UnknownColumnName"));
 }

 @Test
 public void testIsSetMethodWithValidName() {
 record = new CSVRecord(new String[]{"Name", "Age", "City"}, Map.of("Name", 0, "Age", 1, "City", 2), null, 1);
 record.get("Name");
 record.get("Age");
 record.get("City");
 Assert.assertTrue(record.isSet("Name"));
 Assert.assertTrue(record.isSet("Age"));
 Assert.assertTrue(record.isSet("City"));
 Assert.assertFalse(record.isSet("UnknownColumnName"));
 }

 @Test(expected = IllegalStateException.class)
 public void testGetMethodWithInvalidMapping() {
 record = new CSVRecord(new String[]{"Name", "Age", "City"}, null, null, 1);
 record.get("Name");
 }

}