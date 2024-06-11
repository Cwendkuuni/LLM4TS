import static org.junit.Assert.*;

import org.apache.commons.csv.CSVRecord;
import org.junit.Test;

public class CSVRecordTest {

	@Test
	public void testGetEnum() {
		CSVRecord record = new CSVRecord(new String[] { "value1", "value2", "value3" }, null, null, 1);
		assertEquals("value1", record.get(TestEnum.TEST1));
		assertEquals("value2", record.get(TestEnum.TEST2));
		assertEquals("value3", record.get(TestEnum.TEST3));
	}

	@Test
	public void testGetByIndex() {
		CSVRecord record = new CSVRecord(new String[] { "value1", "value2", "value3" }, null, null, 1);
		assertEquals("value1", record.get(0));
		assertEquals("value2", record.get(1));
		assertEquals("value3", record.get(2));
	}

	@Test
	public void testGetByName() {
		CSVRecord record = new CSVRecord(new String[] { "value1", "value2", "value3" }, null, null, 1);
		assertEquals("value1", record.get("name1"));
		assertEquals("value2", record.get("name2"));
		assertEquals("value3", record.get("name3"));
	}

	@Test(expected = IllegalStateException.class)
	public void testGetByNameNoHeaderMapping() {
		CSVRecord record = new CSVRecord(new String[] { "value1", "value2", "value3" }, null, null, 1);
		record.get("name");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetByNameInvalidMapping() {
		CSVRecord record = new CSVRecord(new String[] { "value1", "value2", "value3" }, createMapping(), null, 1);
		record.get("invalid");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetByNameIndexOutOfBounds() {
		CSVRecord record = new CSVRecord(new String[] { "value1", "value2", "value3" }, createMapping(), null, 1);
		record.get("name4");
	}

	@Test
	public void testGetComment() {
		CSVRecord record = new CSVRecord(new String[] { "value1", "value2", "value3" }, null, "comment", 1);
		assertEquals("comment", record.getComment());
	}

	@Test
	public void testGetRecordNumber() {
		CSVRecord record = new CSVRecord(new String[] { "value1", "value2", "value3" }, null, null, 1);
		assertEquals(1, record.getRecordNumber());
	}

	@Test
	public void testIsConsistent() {
		CSVRecord record1 = new CSVRecord(new String[] { "value1", "value2", "value3" }, createMapping(), null, 1);
		CSVRecord record2 = new CSVRecord(new String[] { "value1", "value2" }, createMapping(), null, 1);
		assertTrue(record1.isConsistent());
		assertFalse(record2.isConsistent());
	}

	@Test
	public void testIsMapped() {
		CSVRecord record = new CSVRecord(new String[] { "value1", "value2", "value3" }, createMapping(), null, 1);
		assertTrue(record.isMapped("name1"));
		assertTrue(record.isMapped("name2"));
		assertTrue(record.isMapped("name3"));
		assertFalse(record.isMapped("invalid"));
	}

	@Test
	public void testIsSet() {
		CSVRecord record = new CSVRecord(new String[] { "value1", "value2", "value3" }, createMapping(), null, 1);
		assertTrue(record.isSet("name1"));
		assertTrue(record.isSet("name2"));
		assertTrue(record.isSet("name3"));
		assertFalse(record.isSet("invalid"));
	}

	@Test
	public void testIterator() {
		CSVRecord record = new CSVRecord(new String[] { "value1", "value2", "value3" }, null, null, 1);
		assertArrayEquals(new String[] { "value1", "value2", "value3" }, toArray(record.iterator()));
	}

	@Test
	public void testPutIn() {
		CSVRecord record = new CSVRecord(new String[] { "value1", "value2", "value3" }, createMapping(), null, 1);
		Map<String, String> map = record.putIn(new HashMap<String, String>());
		assertEquals(3, map.size());
		assertEquals("value1", map.get("name1"));
		assertEquals("value2", map.get("name2"));
		assertEquals("value3", map.get("name3"));
	}

	@Test
	public void testSize() {
		CSVRecord record = new CSVRecord(new String[] { "value1", "value2", "value3" }, null, null, 1);
		assertEquals(3, record.size());
	}

	@Test
	public void testToMap() {
		CSVRecord record = new CSVRecord(new String[] { "value1", "value2", "value3" }, createMapping(), null, 1);
		Map<String, String> map = record.toMap();
		assertEquals(3, map.size());
		assertEquals("value1", map.get("name1"));
		assertEquals("value2", map.get("name2"));
		assertEquals("value3", map.get("name3"));
	}

	@Test
	public void testToString() {
		CSVRecord record = new CSVRecord(new String[] { "value1", "value2", "value3" }, null, null, 1);
		assertEquals("[value1, value2, value3]", record.toString());
	}

	private Map<String, Integer> createMapping() {
		Map<String, Integer> mapping = new HashMap<>();
		mapping.put("name1", 0);
		mapping.put("name2", 1);
		mapping.put("name3", 2);
		return mapping;
	}

	private String[] toArray(Iterator<String> iterator) {
		if (!iterator.hasNext()) {
			return new String[] {};
		}
		String[] array = new String[3];
		int index = 0;

		while (iterator.hasNext()) {
			array[index] = iterator.next();
			index++;
		}
		return array;
	}
}