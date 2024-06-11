import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSVRecordTest {

    @Test
    public void testCSVRecordCreation() {
        String csvData = "Name,Age,City\nJohn Doe,30,New York\nJane Doe,25,Chicago";

        try (CSVParser parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT.withHeader())) {
            CSVRecord record1 = parser.nextRecord();
            CSVRecord record2 = parser.nextRecord();

            CSVRecord expectedRecord1 = new CSVRecord(new String[]{"John Doe", "30", "New York"}, null, null, 1);
            CSVRecord expectedRecord2 = new CSVRecord(new String[]{"Jane Doe", "25", "Chicago"}, null, null, 2);

            Assert.assertEquals(expectedRecord1.get(0), record1.get(0));
            Assert.assertEquals(expectedRecord1.get(1), record1.get(1));
            Assert.assertEquals(expectedRecord1.get(2), record1.get(2));

            Assert.assertEquals(expectedRecord2.get(0), record2.get(0));
            Assert.assertEquals(expectedRecord2.get(1), record2.get(1));
            Assert.assertEquals(expectedRecord2.get(2), record2.get(2));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testGetWithNoHeaderMapping() {
        String csvData = "Name,Age,City\nJohn Doe,30,New York\nJane Doe,25,Chicago";

        try (CSVParser parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT)) {
            CSVRecord record = parser.nextRecord();
            record.get("UnknownColumn");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testGetWithEnum() {
        String csvData = "Name,Position\nJohn Doe,DIRECTOR\nJane Doe,ASSISTANT";

        try (CSVParser parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT.withHeader())) {
            CSVRecord record = parser.nextRecord();

            Assert.assertEquals("DIRECTOR", record.get(Position.POSITION));
            Assert.assertEquals("ASSISTANT", record.get(Position.POSITION).toUpperCase(), record.get(1).toUpperCase());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testGetWithIndex() {
        String csvData = "Name,Age,City\nJohn Doe,30,New York\nJane Doe,25,Chicago";

        try (CSVParser parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT.withHeader())) {
            CSVRecord record = parser.nextRecord();

            Assert.assertEquals("John Doe", record.get(0));
            Assert.assertEquals("30", record.get(1));
            Assert.assertEquals("New York", record.get(2));
            Assert.assertEquals("New York", record.get(2).toUpperCase(), record.get(2).toUpperCase());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testGetWithName() {
        String csvData = "Name,Age,City\nJohn Doe,30,New York\nJane Doe,25,Chicago";

        try (CSVParser parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT.withHeader())) {
            CSVRecord record = parser.nextRecord();

            Assert.assertEquals("John Doe", record.get("Name"));
            Assert.assertEquals("30", record.get("Age"));
            Assert.assertEquals("New York", record.get("City"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testGetComment() {
        String csvData = "#Comment for record 1\nName,Age,City\nJohn Doe,30,New York\n";

        try (CSVParser parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT.withHeader())) {
            CSVRecord record = parser.nextRecord();

            Assert.assertEquals("Comment for record 1", record.getComment());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testGetRecordNumber() {
        String csvData = "Name,Age,City\nJohn Doe,30,New York\nJane Doe,25,Chicago";

        try (CSVParser parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT.withHeader())) {
            CSVRecord record1 = parser.nextRecord();
            CSVRecord record2 = parser.nextRecord();

            Assert.assertEquals(1, record1.getRecordNumber());
            Assert.assertEquals(2, record2.getRecordNumber());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testIsConsistent() {
        String csvData = "Name,Age,City\nJohn Doe,30,New York\nJane Doe,25,Chicago";

        try (CSVParser parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT.withHeader())) {
            CSVRecord record = parser.nextRecord();

            Assert.assertTrue(record.isConsistent());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String csvDataMissingColumn = "Name,City\nJohn Doe,New York";

        try (CSVParser parser = new CSVParser(new StringReader(csvDataMissingColumn), CSVFormat.DEFAULT.withHeader())) {
            CSVRecord record = parser.nextRecord();

            Assert.assertFalse(record.isConsistent());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testIsMapped() {
        String csvData = "Name,Age,City\nJohn Doe,30,New York\nJane Doe,25,Chicago";

        try (CSVParser parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT.withHeader())) {
            CSVRecord record = parser.nextRecord();

            Assert.assertTrue(record.isMapped("Name"));
            Assert.assertTrue(record.isMapped("Age"));
            Assert.assertTrue(record.isMapped("City"));
            Assert.assertFalse(record.isMapped("UnknownColumn"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testIsSet() {
        String csvData = "Name,Age,City\nJohn Doe,30,New York\nJane Doe,25,Chicago";

        try (CSVParser parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT.withHeader())) {
            CSVRecord record = parser.nextRecord();

            Assert.assertTrue(record.isSet("Name"));
            Assert.assertTrue(record.isSet("Age"));
            Assert.assertTrue(record.isSet("City"));
            Assert.assertFalse(record.isSet("UnknownColumn"));

            String csvDataMissingValue = "Name,City,Age\nJohn Doe,New York\nJane Doe,Chicago";

            try (CSVParser parserMissingValue = new CSVParser(new StringReader(csvDataMissingValue), CSVFormat.DEFAULT.withHeader())) {
                CSVRecord recordMissingValue = parserMissingValue.nextRecord();

                Assert.assertFalse(recordMissingValue.isSet("Age"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testIterator() {
        String csvData = "Name,Age,City\nJohn Doe,30,New York\nJane Doe,25,Chicago";

        try (CSVParser parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT.withHeader())) {
            CSVRecord record = parser.nextRecord();

            List<String> values = Arrays.asList(record.values());
            Iterator<String> iterator = record.iterator();

            Assert.assertEquals(values.toString(), iterator.toString());

            for (String value : values) {
                Assert.assertEquals(value, iterator.next());
            }

            Assert.assertFalse(iterator.hasNext());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testPutIn() {
        String csvData = "Name,Age,City\nJohn Doe,30,New York\nJane Doe,25,Chicago";

        try (CSVParser parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT.withHeader())) {
            CSVRecord record = parser.nextRecord();

            Map<String, String> map = new HashMap<>();
            record.putIn(map);

            Assert.assertEquals("John Doe", map.get("Name"));
            Assert.assertEquals("30", map.get("Age"));
            Assert.assertEquals("New York", map.get("City"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testSize() {
        String csvData = "Name,Age,City\nJohn Doe,30,New York\nJane Doe,25,Chicago";

        try (CSVParser parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT.withHeader())) {
            CSVRecord record = parser.nextRecord();

            Assert.assertEquals(3, record.size());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testToString() {
        String csvData = "Name,Age,City\nJohn Doe,30,New York\nJane Doe,25,Chicago";

        try (CSVParser parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT.withHeader())) {
            CSVRecord record = parser.nextRecord();

            Assert.assertEquals("[Ljava.lang.String;@26c56162]", record.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private enum Position {
        POSITION
    }
}