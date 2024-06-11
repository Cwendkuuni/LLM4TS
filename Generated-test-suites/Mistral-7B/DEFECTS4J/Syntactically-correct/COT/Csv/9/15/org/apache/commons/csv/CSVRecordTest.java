import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

public class CSVRecordTest {

 private static final String COMMENT = "This is a test comment.";
 private static final long RECORD_NUMBER = 1;
 private static final String[] VALUES = {"Value1", "Value2", "Value3"};
 private static final Map<String, Integer> MAPPING = new HashMap<String, Integer>() {{
 put("Column1", 0);
 put("Column2", 1);
 put("Column3", 2);
 }};

 @Test
 public void testCSVRecord_ValidConstruction() {
 CSVRecord record = new CSVRecord(VALUES, MAPPING, COMMENT, RECORD_NUMBER);
 Assert.assertEquals(COMMENT, record.getComment());
 Assert.assertEquals(RECORD_NUMBER, record.getRecordNumber());
 Assert.assertArrayEquals(VALUES, record.values());
 Assert.assertEquals(MAPPING, record.mapping);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCSVRecord_NullMapping() {
 CSVRecord record = new CSVRecord(VALUES, null, COMMENT, RECORD_NUMBER);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCSVRecord_InvalidMappingSize() {
 Map<String, Integer> mapping = new HashMap<String, Integer>() {{
 put("Column1", 0);
 put("Column2", 1);
 put("Column4", 3); // Adding an invalid index
 }};
 CSVRecord record = new CSVRecord(VALUES, mapping, COMMENT, RECORD_NUMBER);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCSVRecord_InvalidMappingName() {
 Map<String, Integer> mapping = new HashMap<String, Integer>() {{
 put("Column1", 0);
 put("ColumnX", -1); // Adding an invalid name
 }};
 CSVRecord record = new CSVRecord(VALUES, mapping, COMMENT, RECORD_NUMBER);
 }

 @Test(expected = IllegalStateException.class)
 public void testCSVRecord_NoHeaderMapping() {
 CSVRecord record = new CSVRecord(VALUES, null, COMMENT, RECORD_NUMBER);
 }

 @Test
 public void testCSVRecord_GetValueByEnum() {
 CSVRecord record = new CSVRecord(VALUES, MAPPING, COMMENT, RECORD_NUMBER);
 Enum<String> columnName = Enum.valueOf(String.class, "Column1");
 String expectedValue = "Value1";
 String actualValue = record.get(columnName);
 Assert.assertEquals(expectedValue, actualValue);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCSVRecord_GetValueByInvalidEnum() {
 CSVRecord record = new CSVRecord(VALUES, MAPPING, COMMENT, RECORD_NUMBER);
 Enum<String> invalidEnum = Enum.valueOf(String.class, "InvalidColumn");
 record.get(invalidEnum);
 }

 @Test
 public void testCSVRecord_GetValueByName() {
 CSVRecord record = new CSVRecord(VALUES, MAPPING, COMMENT, RECORD_NUMBER);
 String columnName = "Column1";
 String expectedValue = "Value1";
 String actualValue = record.get(columnName);
 Assert.assertEquals(expectedValue, actualValue);
 }

 @Test(expected = IllegalStateException.class)
 public void testCSVRecord_GetValueByName_NoMapping() {
 CSVRecord record = new CSVRecord(VALUES, null, COMMENT, RECORD_NUMBER);
 String columnName = "Column1";
 record.get(columnName);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCSVRecord_GetValueByName_InvalidMapping() {
 CSVRecord record = new CSVRecord(VALUES, MAPPING, COMMENT, RECORD_NUMBER);
 String invalidColumnName = "InvalidColumn";
 record.get(invalidColumnName);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCSVRecord_GetValueByName_InconsistentRecord() {
 CSVRecord record = new CSVRecord(VALUES, null, COMMENT, RECORD_NUMBER);
 String columnName = "Column1";
 record.get(columnName);
 }

 @Test
 public void testCSVRecord_GetValueByName_ConsistentRecord() {
 CSVRecord record = new CSVRecord(VALUES, MAPPING, COMMENT, RECORD_NUMBER);
 String columnName = "Column1";
 String expectedValue = "Value1";
 String actualValue = record.get(columnName);
 Assert.assertEquals(expectedValue, actualValue);
 }

 @Test
 public void testCSVRecord_GetRecordNumber() {
 CSVRecord record = new CSVRecord(VALUES, MAPPING, COMMENT, RECORD_NUMBER);
 long expectedRecordNumber = RECORD_NUMBER;
 long actualRecordNumber = record.getRecordNumber();
 Assert.assertEquals(expectedRecordNumber, actualRecordNumber);
 }

 @Test
 public void testCSVRecord_IsConsistent() {
 CSVRecord record1 = new CSVRecord(VALUES, MAPPING, COMMENT, RECORD_NUMBER);
 CSVRecord record2 = new CSVRecord(Arrays.copyOfRange(VALUES, 0, 2), MAPPING, COMMENT, RECORD_NUMBER);
 Assert.assertTrue(record1.isConsistent());
 Assert.assertFalse(record2.isConsistent());
 }

 @Test
 public void testCSVRecord_IsMapped() {
 CSVRecord record = new CSVRecord(VALUES, MAPPING, COMMENT, RECORD_NUMBER);
 String columnName = "Column1";
 boolean expectedResult = true;
 boolean actualResult = record.isMapped(columnName);
 Assert.assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testCSVRecord_IsSet() {
 CSVRecord record = new CSVRecord(VALUES, MAPPING, COMMENT, RECORD_NUMBER);
 String columnName = "Column1";
 boolean expectedResult = true;
 boolean actualResult = record.isSet(columnName);
 Assert.assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testCSVRecord_Size() {
 CSVRecord record = new CSVRecord(VALUES, MAPPING, COMMENT, RECORD_NUMBER);
 int expectedSize = VALUES.length;
 int actualSize = record.size();
 Assert.assertEquals(expectedSize, actualSize);
 }

 @Test
 public void testCSVRecord_ToList() {
 CSVRecord record = new CSVRecord(VALUES, MAPPING, COMMENT, RECORD_NUMBER);
 List<String> expectedValues = Arrays.asList(VALUES);
 List<String> actualValues = record.toList();
 Assert.assertEquals(expectedValues, actualValues);
 }

 @Test
 public void testCSVRecord_ToMap() {
 CSVRecord record = new CSVRecord(VALUES, MAPPING, COMMENT, RECORD_NUMBER);
 Map<String, String> expectedMap = new HashMap<String, String>() {{
 put("Column1", "Value1");
 put("Column2", "Value2");
 put("Column3", "Value3");
 }};
 Map<String, String> actualMap = record.toMap();
 Assert.assertEquals(expectedMap, actualMap);
 }

 @Test
 public void testCSVRecord_ToString() {
 CSVRecord record = new CSVRecord(VALUES, MAPPING, COMMENT, RECORD_NUMBER);
 String expectedString = "[Value1, Value2, Value3]";
 String actualString = record.toString();
 Assert.assertEquals(expectedString, actualString);
 }

 @Test
 public void testCSVRecord_Values() {
 CSVRecord record = new CSVRecord(VALUES, MAPPING, COMMENT, RECORD_NUMBER);
 String[] expectedValues = VALUES;
 String[] actualValues = record.values();
 Assert.assertArrayEquals(expectedValues, actualValues);
 }

}