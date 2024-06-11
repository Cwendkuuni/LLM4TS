package org.apache.commons.csv;
import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testGetInt() {
        CSVRecord record = new CSVRecord(new String[]{"Name", "Age", "City"}, null, null, 1);
        String expected = "Name";
        String actual = record.get(0);
        Assert.assertEquals(expected, actual);
    }

    // Add test cases for other edge cases and exception handling
}