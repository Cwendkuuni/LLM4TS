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
		mapping.put("column1", 0);
		mapping.put("column2", 1);
		mapping.put("column3", 2);
		comment = "This is a comment";
		recordNumber = 1;
		csvRecord = new CSVRecord(values, mapping, comment, recordNumber);
	}

	@Test
	public void testGetEnum() {
		assertEquals("value1", csvRecord.get(TestEnum.TEST1));
		assertEquals("value2", csvRecord.get(TestEnum.TEST2));
		assertEquals("value3", csvRecord.get(TestEnum.TEST3));
	}

	@Test(expected = IllegalStateException.class)
	public void testGetMappingNull() {
		csvRecord = new CSVRecord(values, null, comment, recordNumber);
		csvRecord.get("column1");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetMappingNonexistent() {
		csvRecord.get("nonexistent");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetMappingOutOfBounds() {
		csvRecord.get("column5");
	}

	@Test
	public void testGetMappingValid() {
		assertEquals("value1", csvRecord.get("column1"));
		assertEquals("value2", csvRecord.get("column2"));
		assertEquals("value3", csvRecord.get("column3"));
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
		
		values = new String[] {"value1"};
		csvRecord = new CSVRecord(values, mapping, comment, recordNumber);
		assertFalse(csvRecord.isConsistent());
	}

	@Test
	public void testIsMapped() {
		assertTrue(csvRecord.isMapped("column1"));
		assertTrue(csvRecord.isMapped("column2"));
		assertTrue(csvRecord.isMapped("column3"));
		assertFalse(csvRecord.isMapped("nonexistent"));
	}

	@Test
	public void testIsSet() {
		assertTrue(csvRecord.isSet("column1"));
		assertTrue(csvRecord.isSet("column2"));
		assertTrue(csvRecord.isSet("column3"));
		assertFalse(csvRecord.isSet("nonexistent"));
	}

	@Test
	public void testIterator() {
		String[] expectedValues = {"value1", "value2", "value3"};
		int index = 0;

		for (String value : csvRecord) {
			assertEquals(expectedValues[index], value);
			index++;
		}
	}

	@Test
	public void testPutIn() {
		Map<String, String> map = new HashMap<>();
		map.put("column1", "value1");
		map.put("column2", "value2");
		map.put("column3", "value3");

		assertEquals(map, csvRecord.putIn(new HashMap<String, String>()));
	}

	@Test
	public void testSize() {
		assertEquals(3, csvRecord.size());
	}

	@Test
	public void testValues() {
		String[] expectedValues = {"value1", "value2", "value3"};
		assertEquals(expectedValues, csvRecord.values());
	}
	
	private enum TestEnum {
		TEST1, TEST2, TEST3;
	}

}