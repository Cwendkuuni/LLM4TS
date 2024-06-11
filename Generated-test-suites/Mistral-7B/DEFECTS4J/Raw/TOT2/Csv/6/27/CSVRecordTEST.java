import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

 @Test
 public void testCSVRecordCreation() {
 CSVRecord record = new CSVRecord(new String[]{"John", "Doe", "30"}, null, null, 1);
 assertEquals("John", record.get(0));
 assertEquals("Doe", record.get(1));
 assertEquals("30", record.get(2));
 assertEquals(3, record.size());
 }

 @Test
 public void testCSVRecordGetByEnum() {
 CSVRecord record = new CSVRecord(new String[]{"John", "Doe", "30"}, null, null, 1);
 assertEquals("John", record.get(Enum.ONE));
 assertEquals("Doe", record.get(Enum.TWO));
 assertEquals("30", record.get(Enum.THREE));
 }

 @Test
 public void testCSVRecordGetByIndex() {
 CSVRecord record = new CSVRecord(new String[]{"John", "Doe", "30"}, null, null, 1);
 assertEquals("John", record.get(0));
 assertEquals("Doe", record.get(1));
 assertEquals("30", record.get(2));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCSVRecordGetByNonExistentName() {
 CSVRecord record = new CSVRecord(new String[]{"John", "Doe", "30"}, null, null, 1);
 record.get("NonExistentName");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCSVRecordGetByMissingMapping() {
 CSVRecord record = new CSVRecord(new String[]{"John", "Doe", "30", ""}, new HashMap<>(), null, 1);
 record.get("Doe");
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testCSVRecordGetByInvalidIndex() {
 CSVRecord record = new CSVRecord(new String[]{"John", "Doe", "30"}, null, null, 1);
 record.get(3);
 }

 @Test
 public void testCSVRecordGetComment() {
 CSVRecord record = new CSVRecord(new String[]{"John", "Doe", "30"}, null, "Comment", 1);
 assertEquals("Comment", record.getComment());
 }

 @Test
 public void testCSVRecordGetRecordNumber() {
 CSVRecord record = new CSVRecord(new String[]{"John", "Doe", "30"}, null, null, 1);
 assertEquals(1, record.getRecordNumber());
 }

 @Test
 public void testCSVRecordIsConsistent() {
 CSVRecord record = new CSVRecord(new String[]{"John", "Doe", "30"}, null, null, 1);
 assertTrue(record.isConsistent());
 }

 @Test
 public void testCSVRecordIsMapped() {
 CSVRecord record = new CSVRecord(new String[]{"John", "Doe", "30"}, new HashMap<String, Integer>(){{
 put("Name1", 0);
 put("Name2", 1);
 put("Name3", 2);
 }}, null, 1);
 assertTrue(record.isMapped("Name1"));
 }

 @Test
 public void testCSVRecordIsSet() {
 CSVRecord record = new CSVRecord(new String[]{"John", "Doe", "30"}, new HashMap<String, Integer>(){{
 put("Name1", 0);
 put("Name2", 1);
 put("Name3", 2);
 }}, null, 1);
 assertTrue(record.isSet("Name1"));
 }

 @Test
 public void testCSVRecordIterator() {
 CSVRecord record = new CSVRecord(new String[]{"John", "Doe", "30"}, null, null, 1);
 List<String> values = record.toList();
 Iterator<String> it = record.iterator();
 assertEquals(values.get(0), it.next());
 assertEquals(values.get(1), it.next());
 assertEquals(values.get(2), it.next());
 }

 @Test
 public void testCSVRecordPutIn() {
 CSVRecord record = new CSVRecord(new String[]{"John", "Doe", "30"}, null, null, 1);
 Map<String, String> map = new HashMap<>();
 record.putIn(map);
 assertEquals("John", map.get("0"));
 assertEquals("Doe", map.get("1"));
 assertEquals("30", map.get("2"));
 }

 @Test
 public void testCSVRecordToString() {
 CSVRecord record = new CSVRecord(new String[]{"John", "Doe", "30"}, null, null, 1);
 assertEquals("[John, Doe, 30]", record.toString());
 }

 @Test
 public void testCSVRecordValues() {
 CSVRecord record = new CSVRecord(new String[]{"John", "Doe", "30"}, null, null, 1);
 String[] values = record.values();
 assertEquals("John", values[0]);
 assertEquals("Doe", values[1]);
 assertEquals("30", values[2]);
 }

 @Test
 public void testCSVRecordParseCSV() throws Exception {
 String csvLine = "Name1,Name2,Name3,Comment,RecordNumber";
 CSVFormat format = new CSVFormat(' ', true);
 CSVParser parser = format.parse(new StringReader(csvLine));
 CSVRecord record = parser.next();
 assertEquals("Name1", record.get("Name1"));
 assertEquals("Name2", record.get("Name2"));
 assertEquals(null, record.get("Comment"));
 assertEquals(1, record.getRecordNumber());
 }
}

enum Enum {
 ONE(0), TWO(1), THREE(2);
 private final int value;
 Enum(int value) {
 this.value = value;
 }
 int value() {
 return value;
 }
}