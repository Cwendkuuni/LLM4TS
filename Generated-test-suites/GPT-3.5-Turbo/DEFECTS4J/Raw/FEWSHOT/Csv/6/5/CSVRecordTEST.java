package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class CSVRecordTest {
	
	private CSVRecord csvRecord;
	private String[] values;
	private Map<String, Integer> mapping;
	private String comment;
	private long recordNumber;
	
	@Before
	public void setUp() {
		values = new String[] {"value1", "value2", "value3"};
		mapping = new HashMap<>();
		mapping.put("name1", 0);
		mapping.put("name2", 1);
		mapping.put("name3", 2);
		comment = "This is a comment";
		recordNumber = 1;
		csvRecord = new CSVRecord(values, mapping, comment, recordNumber);
	}
	
	@Test
	public void testGet() {
		assertEquals("value1", csvRecord.get(0));
		assertEquals("value2", csvRecord.get(1));
		assertEquals("value3", csvRecord.get(2));
	}
	
	@Test(expected = IndexOutOfBoundsException.class)
	public void testGet_IndexOutOfBounds() {
		csvRecord.get(3);
	}
	
	@Test
	public void testGet_Enum() {
		assertEquals("value1", csvRecord.get(CSVRecordEnum.NAME1));
		assertEquals("value2", csvRecord.get(CSVRecordEnum.NAME2));
		assertEquals("value3", csvRecord.get(CSVRecordEnum.NAME3));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testGet_Enum_NotMapped() {
		csvRecord.get(CSVRecordEnum.NAME4);
	}
	
	@Test(expected = IllegalStateException.class)
	public void testGet_NoMapping() {
		CSVRecord csvRecordNoMapping = new CSVRecord(values, null, comment, recordNumber);
		csvRecordNoMapping.get("name1");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testGet_NotMapped() {
		csvRecord.get("name4");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testGet_IndexOutOfBoundsException() {
		csvRecord.get("name1");
	}
	
	@Test
	public void testGetComment() {
		assertEquals("This is a comment", csvRecord.getComment());
	}
	
	@Test
	public void testGetRecordNumber() {
		assertEquals(1, csvRecord.getRecordNumber());
	}
	
	@Test
	public void testIsConsistent() {
		assertTrue(csvRecord.isConsistent());
	}
	
	@Test
	public void testIsConsistent_Mismatch() {
		mapping.remove("name3");
		assertFalse(csvRecord.isConsistent());
	}
	
	@Test
	public void testIsMapped() {
		assertTrue(csvRecord.isMapped("name1"));
		assertTrue(csvRecord.isMapped("name2"));
		assertTrue(csvRecord.isMapped("name3"));
		assertFalse(csvRecord.isMapped("name4"));
	}
	
	@Test
	public void testIsSet() {
		assertTrue(csvRecord.isSet("name1"));
		assertTrue(csvRecord.isSet("name2"));
		assertTrue(csvRecord.isSet("name3"));
		assertFalse(csvRecord.isSet("name4"));
	}
	
	@Test
	public void testIterator() {
		assertEquals("value1", csvRecord.iterator().next());
	}
	
	@Test
	public void testPutIn() {
		Map<String, String> map = csvRecord.putIn(new HashMap<String, String>());
		assertEquals("value1", map.get("name1"));
		assertEquals("value2", map.get("name2"));
		assertEquals("value3", map.get("name3"));
	}
	
	@Test
	public void testSize() {
		assertEquals(3, csvRecord.size());
	}
	
	@Test
	public void testToList() {
		assertEquals(3, csvRecord.toList().size());
	}
	
	@Test
	public void testToMap() {
		Map<String, String> map = csvRecord.toMap();
		assertEquals("value1", map.get("name1"));
		assertEquals("value2", map.get("name2"));
		assertEquals("value3", map.get("name3"));
	}
	
	@Test
	public void testToString() {
		assertEquals("[value1, value2, value3]", csvRecord.toString());
	}
	
	@Test
	public void testValues() {
		String[] values = csvRecord.values();
		assertEquals("value1", values[0]);
		assertEquals("value2", values[1]);
		assertEquals("value3", values[2]);
	}
}

enum CSVRecordEnum {
	NAME1, NAME2, NAME3, NAME4
}