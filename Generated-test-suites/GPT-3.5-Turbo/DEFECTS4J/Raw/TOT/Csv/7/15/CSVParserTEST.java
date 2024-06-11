import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.csv.*;

public class CSVParserTest {

   @Test
   public void parse_ValidFile_ReturnsCSVRecords() throws Exception {
      // Arrange
      String csvFile = "test.csv";
      CSVFormat format = CSVFormat.EXCEL;

      // Act
      CSVParser parser = CSVParser.parse(csvFile, format);
      List<CSVRecord> records = parser.getRecords();

      // Assert
      assertNotNull(records);
   }

   @Test
   public void parse_NullFile_ThrowsIllegalArgumentException() throws Exception {
      try {
         // Arrange
         CSVFormat format = CSVFormat.EXCEL;

         // Act
         CSVParser.parse(null, format);
         fail("Expected IllegalArgumentException");

      } catch (IllegalArgumentException e) {
         // Assert
         assertEquals("file", e.getMessage());
      }
   }

   @Test
   public void parse_NullFormat_ThrowsIllegalArgumentException() throws Exception {
      try {
         // Arrange
         File file = new File("test.csv");

         // Act
         CSVParser.parse(file, null);
         fail("Expected IllegalArgumentException");

      } catch (IllegalArgumentException e) {
         // Assert
         assertEquals("format", e.getMessage());
      }
   }

   // ... Other test cases for each method in the CSVParser class

}