package org.apache.commons.csv;
import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.csv.CSVRecord;
import java.util.HashMap;
import java.util.Map;

public class CSVRecordTest {

	@Test
	public void testGet() {
		CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
		assertEquals("value1", record.get(0));
		assertEquals("value2", record.get(1));
		assertEquals("value3", record.get(2));
	}

	@Test
	public void testGetString() {
		Map<String, Integer> mapping = new HashMap<>();
		mapping.put("col1", 0);
		mapping.put("col2", 1);
		mapping.put("col3", 2);
		CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, mapping, null, 1);
		assertEquals("value1", record.get("col1"));
		assertEquals("value2", record.get("col2"));
		assertEquals("value3", record.get("col3"));
	}

	@Test(expected = IllegalStateException.class)
	public void testGetStringNoMapping() {
		CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
		record.get("col1");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetStringInvalidName() {
		Map<String, Integer> mapping = new HashMap<>();
		mapping.put("col1", 0);
		mapping.put("col2", 1);
		mapping.put("col3", 2);
		CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, mapping, null, 1);
		record.get("col4");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetStringInvalidIndex() {
		Map<String, Integer> mapping = new HashMap<>();
		mapping.put("col1", 0);
		mapping.put("col2", 1);
		mapping.put("col3", 2);
		CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, mapping, null, 1);
		record.get("col2");
	}

	@Test
	public void testGetComment() {
		CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "This is a comment", 1);
		assertEquals("This is a comment", record.getComment());
	}

	@Test
	public void testGetRecordNumber() {
		CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 12);
		assertEquals(12, record.getRecordNumber());
	}

	@Test
	public void testIsConsistent() {
		Map<String, Integer> mapping = new HashMap<>();
		mapping.put("col1", 0);
		mapping.put("col2", 1);
		mapping.put("col3", 2);
		CSVRecord record1 = new CSVRecord(new String[]{"value1", "value2", "value3"}, mapping, null, 1);
		assertTrue(record1.isConsistent());
		
		CSVRecord record2 = new CSVRecord(new String[]{"value1", "value2"}, mapping, null, 1);
		assertFalse(record2.isConsistent());
		
		CSVRecord record3 = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
		assertTrue(record3.isConsistent());
	}

	@Test
	public void testIsMapped() {
		Map<String, Integer> mapping = new HashMap<>();
		mapping.put("col1", 0);
		mapping.put("col2", 1);
		mapping.put("col3", 2);
		CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, mapping, null, 1);
		assertTrue(record.isMapped("col1"));
		assertTrue(record.isMapped("col2"));
		assertTrue(record.isMapped("col3"));
		assertFalse(record.isMapped("col4"));
	}

	@Test
	public void testIsSet() {
		Map<String, Integer> mapping = new HashMap<>();
		mapping.put("col1", 0);
		mapping.put("col2", 1);
		mapping.put("col3", 2);
		CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, mapping, null, 1);
		assertTrue(record.isSet("col1"));
		assertTrue(record.isSet("col2"));
		assertTrue(record.isSet("col3"));
		assertFalse(record.isSet("col4"));
	}

	@Test
	public void testIterator() {
		CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
		int count = 0;
		for (String value : record) {
			switch (count) {
				case 0:
					assertEquals("value1", value);
					break;
				case 1:
					assertEquals("value2", value);
					break;
				case 2:
					assertEquals("value3", value);
					break;
			}
			count++;
		}
	}

	@Test
	public void testPutIn() {
		Map<String, String> map = new HashMap<>();
		Map<String, Integer> mapping = new HashMap<>();
		mapping.put("col1", 0);
		mapping.put("col2", 1);
		mapping.put("col3", 2);
		CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, mapping, null, 1);
		record.putIn(map);
		assertEquals(3, map.size());
		assertEquals("value1", map.get("col1"));
		assertEquals("value2", map.get("col2"));
		assertEquals("value3", map.get("col3"));
	}

	@Test
	public void testSize() {
		CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
		assertEquals(3, record.size());
	}

	@Test
	public void testToString() {
		CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
		assertEquals("[value1, value2, value3]", record.toString());
	}

	@Test
	public void testValues() {
		CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
		String[] values = record.values();
		assertEquals(3, values.length);
		assertEquals("value1", values[0]);
		assertEquals("value2", values[1]);
		assertEquals("value3", values[2]);
	}
}