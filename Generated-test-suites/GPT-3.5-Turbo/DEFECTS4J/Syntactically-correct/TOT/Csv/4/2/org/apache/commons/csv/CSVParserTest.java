
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

public class CSVParserTest {

    private static final String CSV_FILE_PATH = "path/to/csv/file.csv";

    @Test
    public void testParseFile() throws IOException {
        CSVParser parser = null;
        try {
            Reader reader = new FileReader(CSV_FILE_PATH);
            parser = new CSVParser(reader, CSVFormat.RFC4180);

            List<CSVRecord> records = parser.getRecords();

            // Check that the records are parsed correctly
            Assertions.assertEquals(3, records.size());

            // Verify the values in the first record
            CSVRecord record = records.get(0);
            Assertions.assertEquals("John", record.get(0));
            Assertions.assertEquals("Doe", record.get(1));
            Assertions.assertEquals("30", record.get(2));

            // Verify the values in the second record
            record = records.get(1);
            Assertions.assertEquals("Jane", record.get(0));
            Assertions.assertEquals("Smith", record.get(1));
            Assertions.assertEquals("25", record.get(2));

            // Verify the values in the third record
            record = records.get(2);
            Assertions.assertEquals("Mike", record.get(0));
            Assertions.assertEquals("Johnson", record.get(1));
            Assertions.assertEquals("35", record.get(2));

        } finally {
            if (parser != null) {
                parser.close();
            }
        }
    }

    @Test
    public void testParseInvalidFile() {
        // Try to parse an invalid file
        CSVParser parser = null;
        try {
            Reader reader = new FileReader("path/to/invalid/csv/file.csv");
            parser = new CSVParser(reader, CSVFormat.RFC4180);

            // Parsing should throw an IOException
            Assertions.assertThrows(IOException.class, () -> {
                parser.getRecords();
            });

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (parser != null) {
                try {
                    parser.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

