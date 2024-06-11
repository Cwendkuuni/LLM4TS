import org.apache.commons.csv.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSVRecordTest {

    @Test
    public void testCSVRecordWithValidValues() {
        String[] values = new String[]{"John Doe", "12345", "jdoe@example.com"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("ID", 1);
        mapping.put("Email", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        Assert.assertEquals("John Doe", record.get("Name"));
        Assert.assertEquals("12345", record.get("ID"));
        Assert.assertEquals("jdoe@example.com", record.get("Email"));
        Assert.assertEquals(1, record.size());
        Assert.assertTrue(record.isConsistent());
        Assert.assertTrue(record.isMapped("Name"));
        Assert.assertTrue(record.isSet("Name"));
        Assert.assertTrue(record.isSet("ID"));
        Assert.assertTrue(record.isSet("Email"));
    }

    @Test
    public void testCSVRecordWithEmptyValues() {
        String[] values = new String[0];
        Map<String, Integer> mapping = new HashMap<>();

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        Assert.assertNull(record.get("Name"));
        Assert.assertNull(record.get("ID"));
        Assert.assertNull(record.get("Email"));
        Assert.assertEquals(0, record.size());
        Assert.assertTrue(record.isConsistent());
        Assert.assertFalse(record.isMapped("Name"));
        Assert.assertFalse(record.isSet("Name"));
        Assert.assertFalse(record.isSet("ID"));
        Assert.assertFalse(record.isSet("Email"));
    }

    @Test(expected = IllegalStateException.class)
    public void testCSVRecordWithoutMapping() {
        String[] values = new String[]{"John Doe", "12345", "jdoe@example.com"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        record.get("Name");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCSVRecordWithInvalidIndex() {
        String[] values = new String[]{"John Doe", "12345", "jdoe@example.com"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("ID", 1);
        mapping.put("Email", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        record.get(3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCSVRecordWithInvalidName() {
        String[] values = new String[]{"John Doe", "12345", "jdoe@example.com"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("ID", 1);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        record.get("InvalidName");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCSVRecordWithInconsistentValues() {
        String[] values = new String[]{"John Doe", "12345", "jdoe@example.com", "InvalidValue"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("ID", 1);
        mapping.put("Email", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        record.get("Name");
        record.get("InvalidValue");
    }
}