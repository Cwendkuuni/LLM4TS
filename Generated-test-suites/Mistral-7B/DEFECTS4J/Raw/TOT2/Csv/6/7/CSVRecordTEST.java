import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

public class CSVRecordTest {

 private String[] testValues = new String[]{"John Doe", "30", "New York", "Sales"};
 private Map<String, Integer> testMapping = new HashMap<String, Integer>(){{
 put("Name", 0);
 put("Age", 1);
 put("City", 2);
 put("Department", 3);
 }};

 @Test
 public void testGetByEnum() {
 CSVRecord record = new CSVRecord(testValues, testMapping, null, 1);
 Assert.assertEquals("John Doe", record.get(Name.class));
 }

 @Test
 public void testGetByIndex() {
 CSVRecord record = new CSVRecord(testValues, testMapping, null, 1);
 Assert.assertEquals("John Doe", record.get(0));
 }

 @Test
 public void testGetByName() {
 CSVRecord record = new CSVRecord(testValues, testMapping, null, 1);
 Assert.assertEquals("John Doe", record.get("Name"));
 }

 @Test
 public void testGetComment() {
 CSVRecord record = new CSVRecord(testValues, testMapping, "Test Comment", 1);
 Assert.assertEquals("Test Comment", record.getComment());
 }

 @Test
 public void testGetRecordNumber() {
 CSVRecord record = new CSVRecord(testValues, testMapping, null, 1);
 Assert.assertEquals(1, record.getRecordNumber());
 }

 @Test
 public void testIsConsistent() {
 CSVRecord record = new CSVRecord(testValues, testMapping, null, 1);
 Assert.assertTrue(record.isConsistent());
 }

 @Test(expected = IllegalStateException.class)
 public void testNoHeaderMapping() {
 CSVRecord record = new CSVRecord(testValues, null, null, 1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetByNameMissingColumn() {
 CSVRecord record = new CSVRecord(testValues, testMapping, null, 1);
 Assert.assertFalse(record.isMapped("NonExistentColumn"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetByNameInvalidColumnIndex() {
 CSVRecord record = new CSVRecord(testValues, testMapping, null, 1);
 Assert.assertFalse(record.isSet("Name"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetByNameInvalidRecordSize() {
 CSVRecord record = new CSVRecord(testValues, testMapping, null, 1);
 Assert.assertFalse(record.isConsistent());
 }
}