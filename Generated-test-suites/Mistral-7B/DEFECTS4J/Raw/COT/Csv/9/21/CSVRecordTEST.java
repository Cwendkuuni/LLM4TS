import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

public class CSVRecordTest {

 private static final String COMMENT = "Test Comment";
 private static final String[] VALUES = new String[]{"Value1", "Value2", "Value3"};
 private static final Map<String, Integer> MAPPING = new HashMap<String, Integer>() {{
 put("Header1", 0);
 put("Header2", 1);
 put("Header3", 2);
 }};

 @Test
 public void testCSVRecordCreation() {
 CSVRecord record = new CSVRecord(VALUES, MAPPING, COMMENT, 1L);
 Assert.assertEquals(1L, record.getRecordNumber());
 Assert.assertArrayEquals(VALUES, record.values());
 Assert.assertEquals(COMMENT, record.getComment());
 Assert.assertTrue(record.isConsistent());
 }

 @Test(expected = IllegalStateException.class)
 public void testCSVRecordCreationWithoutMapping() {
 CSVRecord record = new CSVRecord(VALUES, null, COMMENT, 1L);
 }

 @Test
 public void testGetMethod() {
 CSVRecord record = new CSVRecord(VALUES, MAPPING, COMMENT, 1L);

 // Test get by index
 Assert.assertEquals("Value1", record.get(0));
 // Test get by enum
 Assert.assertEquals("Value1", record.get(CSVRecord.class.getEnumConstants()[0]));
 // Test get by name
 Assert.assertEquals("Value2", record.get("Header2"));
 // Test getting null value
 Assert.assertEquals(CSVFormat.EMPTY_STRING, record.get("NonExistingHeader"));
 // Test getting comment
 Assert.assertEquals(COMMENT, record.getComment());
 // Test getting record number
 Assert.assertEquals(1L, record.getRecordNumber());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetByNonExistingHeader() {
 CSVRecord record = new CSVRecord(VALUES, MAPPING, COMMENT, 1L);
 record.get("NonExistingHeader");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetByMappedButEmptyColumn() {
 CSVRecord record = new CSVRecord(VALUES, MAPPING, COMMENT, 1L);
 record.get("Header4");
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testGetByOutOfBoundsIndex() {
 CSVRecord record = new CSVRecord(VALUES, MAPPING, COMMENT, 1L);
 record.get(VALUES.length);
 }

 @Test
 public void testIsConsistent() {
 CSVRecord record1 = new CSVRecord(new String[0], null, COMMENT, 1L);
 CSVRecord record2 = new CSVRecord(new String[]{"Value1", "Value2"}, null, COMMENT, 1L);
 CSVRecord record3 = new CSVRecord(VALUES, MAPPING, COMMENT, 1L);
 CSVRecord record4 = new CSVRecord(VALUES, null, COMMENT, 1L);
 CSVRecord record5 = new CSVRecord(VALUES, MAPPING, COMMENT, 2L);

 Assert.assertFalse(record1.isConsistent());
 Assert.assertFalse(record2.isConsistent());
 Assert.assertTrue(record3.isConsistent());
 Assert.assertFalse(record4.isConsistent());
 Assert.assertFalse(record5.isConsistent());
 }

 @Test
 public void testIsMapped() {
 CSVRecord record = new CSVRecord(VALUES, MAPPING, COMMENT, 1L);

 Assert.assertTrue(record.isMapped("Header1"));
 Assert.assertTrue(record.isMapped("Header2"));
 Assert.assertTrue(record.isMapped("Header3"));
 Assert.assertFalse(record.isMapped("NonExistingHeader"));
 }

 @Test
 public void testIsSet() {
 CSVRecord record = new CSVRecord(VALUES, MAPPING, COMMENT, 1L);

 Assert.assertTrue(record.isSet("Header1"));
 Assert.assertTrue(record.isSet("Header2"));
 Assert.assertTrue(record.isSet("Header3"));
 Assert.assertFalse(record.isSet("NonExistingHeader"));
 Assert.assertFalse(record.isSet("Header4"));
 }

 @Test
 public void testIterator() {
 CSVRecord record = new CSVRecord(VALUES, MAPPING, COMMENT, 1L);
 List<String> valuesList = record.toList();

 Iterator<String> iterator = record.iterator();

 Assert.assertEquals(VALUES[0], iterator.next());
 Assert.assertEquals(VALUES[1], iterator.next());
 Assert.assertEquals(VALUES[2], iterator.next());
 Assert.assertFalse(iterator.hasNext());
 }

 @Test
 public void testPutIn() {
 CSVRecord record = new CSVRecord(VALUES, MAPPING, COMMENT, 1L);
 Map<String, String> map = record.putIn(new HashMap<String, String>());

 Assert.assertEquals("Value1", map.get("Header1"));
 Assert.assertEquals("Value2", map.get("Header2"));
 Assert.assertEquals("Value3", map.get("Header3"));
 Assert.assertNull(map.get("Header4"));
 }

 @Test
 public void testSize() {
 CSVRecord record = new CSVRecord(VALUES, MAPPING, COMMENT, 1L);

 Assert.assertEquals(VALUES.length, record.size());
 }

 @Test
 public void testToString() {
 CSVRecord record = new CSVRecord(VALUES, MAPPING, COMMENT, 1L);

 Assert.assertEquals("[Value1, Value2, Value3]", record.toString());
 }
}