import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

    /*
     * Test method for {@link org.apache.commons.csv.CSVRecord#get(java.lang.Enum)}.
     */
    @Test
    public void testGetEnum() {
        CSVRecord record = new CSVRecord(null, null, null, 0);
        assertNull(record.get(TestEnum.TEST1));
        assertNull(record.get(TestEnum.TEST2));

        record = new CSVRecord(new String[] {"test1", "test2"}, null, null, 0);
        assertEquals("test1", record.get(TestEnum.TEST1));
        assertEquals("test2", record.get(TestEnum.TEST2));
    }

    /*
     * Test method for {@link org.apache.commons.csv.CSVRecord#get(int)}.
     */
    @Test
    public void testGet() {
        CSVRecord record = new CSVRecord(new String[] {"test1", "test2"}, null, null, 0);
        assertEquals("test1", record.get(0));
        assertEquals("test2", record.get(1));
    }

    /*
     * Test method for {@link org.apache.commons.csv.CSVRecord#get(java.lang.String)}.
     */
    @Test
    public void testGetString() {
        CSVRecord record = new CSVRecord(new String[] {"test1", "test2"}, null, null, 0);
        assertEquals("test1", record.get("name1"));
        assertEquals("test2", record.get("name2"));

        record = new CSVRecord(new String[] {"test1"}, null, null, 0);
        try {
            record.get("name2");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        try {
            record.get(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        record = new CSVRecord(null, null, null, 0);
        try {
            record.get("name1");
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected exception
        }
    }

    /*
     * Test method for {@link org.apache.commons.csv.CSVRecord#getComment()}.
     */
    @Test
    public void testGetComment() {
        CSVRecord record = new CSVRecord(null, null, null, 0);
        assertNull(record.getComment());

        record = new CSVRecord(null, null, "comment", 0);
        assertEquals("comment", record.getComment());
    }

    /*
     * Test method for {@link org.apache.commons.csv.CSVRecord#getRecordNumber()}.
     */
    @Test
    public void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(null, null, null, 5);
        assertEquals(5, record.getRecordNumber());
    }

    /*
     * Test method for {@link org.apache.commons.csv.CSVRecord#isConsistent()}.
     */
    @Test
    public void testIsConsistent() {
        CSVRecord record = new CSVRecord(null, null, null, 0);
        assertTrue(record.isConsistent());

        record = new CSVRecord(new String[] {"test1", "test2"}, null, null, 0);
        assertTrue(record.isConsistent());

        record = new CSVRecord(new String[] {"test1"}, null, null, 0);
        assertFalse(record.isConsistent());
    }

    /*
     * Test method for {@link org.apache.commons.csv.CSVRecord#isMapped(java.lang.String)}.
     */
    @Test
    public void testIsMapped() {
        CSVRecord record = new CSVRecord(null, null, null, 0);
        assertFalse(record.isMapped("name1"));
        assertFalse(record.isMapped(null));

        record = new CSVRecord(new String[] {"test1", "test2"}, null, null, 0);
        assertFalse(record.isMapped("name1"));
        assertFalse(record.isMapped("name2"));

        record = new CSVRecord(new String[] {"test1", "test2"}, createMapping(), null, 0);
        assertTrue(record.isMapped("name1"));
        assertTrue(record.isMapped("name2"));
        assertFalse(record.isMapped("name3"));
    }

    /*
     * Test method for {@link org.apache.commons.csv.CSVRecord#isSet(java.lang.String)}.
     */
    @Test
    public void testIsSet() {
        CSVRecord record = new CSVRecord(new String[] {"test1", "test2"}, null, null, 0);
        assertFalse(record.isSet("name1"));
        assertFalse(record.isSet("name2"));

        record = new CSVRecord(new String[] {"test1", "test2"}, createMapping(), null, 0);
        assertTrue(record.isSet("name1"));
        assertTrue(record.isSet("name2"));
        assertFalse(record.isSet("name3"));
    }

    /*
     * Test method for {@link org.apache.commons.csv.CSVRecord#iterator()}.
     */
    @Test
    public void testIterator() {
        CSVRecord record = new CSVRecord(new String[] {"test1", "test2"}, null, null, 0);
        assertTrue(record.iterator().hasNext());
        assertEquals("test1", record.iterator().next());
    }

    /*
     * Test method for {@link org.apache.commons.csv.CSVRecord#putIn(java.util.Map)}.
     */
    @Test
    public void testPutIn() {
        CSVRecord record = new CSVRecord(new String[] {"test1", "test2"}, createMapping(), null, 0);
        Map<String, String> map = record.putIn(new HashMap<String, String>());
        assertEquals("test1", map.get("name1"));
        assertEquals("test2", map.get("name2"));

        map = record.putIn(null);
        assertNull(map);
    }

    /*
     * Test method for {@link org.apache.commons.csv.CSVRecord#size()}.
     */
    @Test
    public void testSize() {
        CSVRecord record = new CSVRecord(null, null, null, 0);
        assertEquals(0, record.size());

        record = new CSVRecord(new String[] {"test1", "test2"}, null, null, 0);
        assertEquals(2, record.size());
    }

    /*
     * Test method for {@link org.apache.commons.csv.CSVRecord#toList()}.
     */
    @Test
    public void testToList() {
        CSVRecord record = new CSVRecord(new String[] {"test1", "test2"}, null, null, 0);
        assertTrue(record.toList().contains("test1"));
        assertTrue(record.toList().contains("test2"));
    }

    /*
     * Test method for {@link org.apache.commons.csv.CSVRecord#toMap()}.
     */
    @Test
    public void testToMap() {
        CSVRecord record = new CSVRecord(new String[] {"test1", "test2"}, createMapping(), null, 0);
        Map<String, String> map = record.toMap();
        assertEquals("test1", map.get("name1"));
        assertEquals("test2", map.get("name2"));

        record = new CSVRecord(new String[] {"test1", "test2"}, null, null, 0);
        map = record.toMap();
        assertTrue(map.isEmpty());
    }

    /*
     * Test method for {@link org.apache.commons.csv.CSVRecord#toString()}.
     */
    @Test
    public void testToString() {
        CSVRecord record = new CSVRecord(new String[] {"test1", "test2"}, null, null, 0);
        assertEquals("[test1, test2]", record.toString());

        record = new CSVRecord(null, null, null, 0);
        assertEquals("[]", record.toString());
    }

    private Map<String, Integer> createMapping() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        return mapping;
    }

    private enum TestEnum {
        TEST1, TEST2;
    }
}