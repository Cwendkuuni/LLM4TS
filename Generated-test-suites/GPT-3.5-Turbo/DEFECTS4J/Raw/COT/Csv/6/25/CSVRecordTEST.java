import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

    @Test
    public void testGetEnum() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, null, null, 1);
        assertEquals("A", record.get(TestEnum.A));
        assertEquals("B", record.get(TestEnum.B));
        assertEquals("C", record.get(TestEnum.C));
    }

    @Test
    public void testGetInt() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, null, null, 1);
        assertEquals("A", record.get(0));
        assertEquals("B", record.get(1));
        assertEquals("C", record.get(2));
    }

    @Test
    public void testGetString() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, null, null, 1);
        assertNull(record.get("D"));
        assertEquals("A", record.get("A"));
        assertEquals("B", record.get("B"));
        assertEquals("C", record.get("C"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetWithoutMapping() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, null, null, 1);
        record.get("A");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetInvalidName() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, null, null, 1);
        record.get("D");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetInvalidIndex() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, null, null, 1);
        record.get(3);
    }
    
    @Test
    public void testGetComment() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, null, "This is a comment", 1);
        assertEquals("This is a comment", record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, null, null, 100);
        assertEquals(100, record.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        // Mapping is null, so it should be true
        CSVRecord record1 = new CSVRecord(new String[]{"A", "B", "C"}, null, null, 1);
        assertTrue(record1.isConsistent());

        // Mapping size is 3, but values length is 2, so it should be false
        CSVRecord record2 = new CSVRecord(new String[]{"A", "B", "C"}, new HashMap<>(), null, 1);
        assertFalse(record2.isConsistent());

        // Mapping size is 3, and values length is 3, so it should be true
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("A", 0);
        mapping.put("B", 1);
        mapping.put("C", 2);
        CSVRecord record3 = new CSVRecord(new String[]{"A", "B", "C"}, mapping, null, 1);
        assertTrue(record3.isConsistent());
    }

    @Test
    public void testIsMapped() {
        // Mapping is null, so it should be false
        CSVRecord record1 = new CSVRecord(new String[]{"A", "B", "C"}, null, null, 1);
        assertFalse(record1.isMapped("A"));

        // Mapping contains "A", so it should be true
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("A", 0);
        CSVRecord record2 = new CSVRecord(new String[]{"A", "B", "C"}, mapping, null, 1);
        assertTrue(record2.isMapped("A"));

        // Mapping does not contain "D", so it should be false
        assertFalse(record2.isMapped("D"));
    }

    @Test
    public void testIsSet() {
        // Mapping is null, so it should be false
        CSVRecord record1 = new CSVRecord(new String[]{"A", "B", "C"}, null, null, 1);
        assertFalse(record1.isSet("A"));

        // Mapping contains "A", but values length is 2, so it should be false
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("A", 0);
        CSVRecord record2 = new CSVRecord(new String[]{"A", "B"}, mapping, null, 1);
        assertFalse(record2.isSet("A"));

        // Mapping contains "A", and values length is 3, so it should be true
        CSVRecord record3 = new CSVRecord(new String[]{"A", "B", "C"}, mapping, null, 1);
        assertTrue(record3.isSet("A"));

        // Mapping does not contain "D", so it should be false
        assertFalse(record3.isSet("D"));
    }

    @Test
    public void testIterator() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, null, null, 1);
        Iterator<String> iterator = record.iterator();
        assertTrue(iterator.hasNext());
        assertEquals("A", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("B", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("C", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testPutIn() {
        // Mapping is null, so the map should be empty
        CSVRecord record1 = new CSVRecord(new String[]{"A", "B", "C"}, null, null, 1);
        assertEquals(0, record1.putIn(new HashMap<>()).size());

        // Mapping contains "A", but values length is 2, so only "A" should be in the map
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("A", 0);
        CSVRecord record2 = new CSVRecord(new String[]{"A", "B"}, mapping, null, 1);
        Map<String, String> map2 = record2.putIn(new HashMap<>());
        assertEquals(1, map2.size());
        assertEquals("A", map2.get("A"));

        // Mapping contains "A", "B", "C", and values length is 3, so all values should be in the map
        CSVRecord record3 = new CSVRecord(new String[]{"A", "B", "C"}, mapping, null, 1);
        Map<String, String> map3 = record3.putIn(new HashMap<>());
        assertEquals(3, map3.size());
        assertEquals("A", map3.get("A"));
        assertEquals("B", map3.get("B"));
        assertEquals("C", map3.get("C"));
    }

    @Test
    public void testSize() {
        // Mapping is null, so it should return the length of values
        CSVRecord record1 = new CSVRecord(new String[]{"A", "B", "C"}, null, null, 1);
        assertEquals(3, record1.size());

        // Mapping size is 3, so it should return the size of mapping
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("A", 0);
        mapping.put("B", 1);
        mapping.put("C", 2);
        CSVRecord record2 = new CSVRecord(new String[]{"A", "B", "C"}, mapping, null, 1);
        assertEquals(3, record2.size());
    }

    @Test
    public void testToList() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, null, null, 1);
        List<String> list = record.toList();
        assertEquals(3, list.size());
        assertEquals("A", list.get(0));
        assertEquals("B", list.get(1));
        assertEquals("C", list.get(2));
    }

    @Test
    public void testToMap() {
        // Mapping is null, so the map should be empty
        CSVRecord record1 = new CSVRecord(new String[]{"A", "B", "C"}, null, null, 1);
        assertEquals(0, record1.toMap().size());

        // Mapping contains "A", but values length is 2, so only "A" should be in the map
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("A", 0);
        CSVRecord record2 = new CSVRecord(new String[]{"A", "B"}, mapping, null, 1);
        Map<String, String> map2 = record2.toMap();
        assertEquals(1, map2.size());
        assertEquals("A", map2.get("A"));

        // Mapping contains "A", "B", "C", and values length is 3, so all values should be in the map
        CSVRecord record3 = new CSVRecord(new String[]{"A", "B", "C"}, mapping, null, 1);
        Map<String, String> map3 = record3.toMap();
        assertEquals(3, map3.size());
        assertEquals("A", map3.get("A"));
        assertEquals("B", map3.get("B"));
        assertEquals("C", map3.get("C"));
    }

    @Test
    public void testToString() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, null, null, 1);
        assertEquals("[A, B, C]", record.toString());
    }

    @Test
    public void testValues() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, null, null, 1);
        assertArrayEquals(new String[]{"A", "B", "C"}, record.values());
    }

    private enum TestEnum {
        A, B, C
    }
}