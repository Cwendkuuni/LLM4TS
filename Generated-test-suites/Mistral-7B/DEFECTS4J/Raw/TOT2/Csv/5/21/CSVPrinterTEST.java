import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.Quote;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class CSVPrinterTest {

 private CSVPrinter printer;
 private ByteArrayOutputStream outputStream;

 @Before
 public void setup() throws IOException {
 outputStream = new ByteArrayOutputStream();
 printer = new CSVPrinter(new OutputStreamWriter(outputStream), getDefaultFormat());
 }

 @Test
 public void testPrintString() throws IOException {
 printer.print("Hello World");
 assertEquals("Hello World", outputStream.toString());
 }

 @Test
 public void testPrintNull() throws IOException {
 printer.print(null);
 assertEquals("", outputStream.toString());
 }

 @Test
 public void testPrintInteger() throws IOException {
 printer.print(123);
 assertEquals("123", outputStream.toString());
 }

 @Test
 public void testPrintDouble() throws IOException {
 printer.print(123.45);
 assertEquals("123.45", outputStream.toString());
 }

 @Test
 public void testPrintBoolean() throws IOException {
 printer.print(true);
 assertEquals("true", outputStream.toString());
 printer.print(false);
 assertEquals("false", outputStream.toString());
 }

 @Test
 public void testPrintListString() throws IOException {
 List<String> list = new LinkedList<>(Arrays.asList("One", "Two", "Three"));
 printer.printRecords(list);
 String expected = "One,Two,Three\n";
 assertEquals(expected, outputStream.toString());
 }

 @Test
 public void testPrintListInteger() throws IOException {
 List<Integer> list = new LinkedList<>(Arrays.asList(1, 2, 3));
 printer.printRecords(list);
 String expected = "1,2,3\n";
 assertEquals(expected, outputStream.toString());
 }

 @Test
 public void testPrintListMixed() throws IOException {
 List<Object> list = new LinkedList<>(Arrays.asList("One", 2, "Three", null, 3.5));
 printer.printRecords(list);
 String expected = "One,2,Three,,\"\",3.5\n";
 assertEquals(expected, outputStream.toString());
 }

 @Test
 public void testPrintEmptyList() throws IOException {
 printer.printRecords(Collections.emptyList());
 assertEquals("", outputStream.toString());
 }

 @Test
 public void testPrintRecordsFromArray() throws IOException {
 Object[] array = new Object[]{"One", 2, "Three", null, 3.5};
 printer.printRecords(Arrays.asList(array));
 String expected = "One,2,Three,,\"\",3.5\n";
 assertEquals(expected, outputStream.toString());
 }

 @Test
 public void testPrintRecordsFromResultSet() throws IOException, SQLException {
 String[] columnNames = {"Name", "Age", "City"};
 String[][] data = {{"John Doe", "30", "New York"}, {"Jane Doe", "25", "Boston"}};
 String expected = "Name,Age,City\n" +
                 "John Doe,30,New York\n" +
                 "Jane Doe,25,Boston\n";

 ByteArrayOutputStream resultSetOutput = new ByteArrayOutputStream();
 OutputStreamWriter resultSetWriter = new OutputStreamWriter(resultSetOutput);

 resultSetWriter.write("Name VARCHAR(20), Age INT, City VARCHAR(10)\n");
 resultSetWriter.flush();

 // Create a mock ResultSet
 ByteArrayInputStream resultSetInput = new ByteArrayInputStream(resultSetOutput.toByteArray());
 // Create a mock statement
 Object statement = new Object(){};
 // Create a mock connection
 Object connection = new Object(){};

 // Set up the test
 printer.printRecords(Collections.singletonList(statement), connection, resultSetInput);

 // Test
 printer.format = new CSVFormat(',', '\n', null, null, null, null, Quote.MINIMAL);
 printer.print("Name,Age,City");
 printer.flush();

 // Check the output
 assertEquals(expected, outputStream.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testExceptionNullOut() {
 try {
 new CSVPrinter(null, getDefaultFormat());
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testExceptionNullFormat() {
 try {
 new CSVPrinter(new OutputStreamWriter(outputStream), null);
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test(expected = IOException.class)
 public void testExceptionIOException() throws IOException {
 ByteArrayOutputStream outputStreamSpy = new ByteArrayOutputStream() {
 @Override
 public void write(final int b) throws IOException {
 throw new IOException("Test Exception");
 }
 };
 try {
 new CSVPrinter(new OutputStreamWriter(outputStreamSpy), getDefaultFormat());
 } catch (IOException e) {
 // Expected
 }
 }

 @Test
 public void testComment() throws IOException {
 printer.printComment("This is a comment");
 String expected = "#This is a comment\n";
 assertEquals(expected, outputStream.toString());
 }

 private CSVFormat getDefaultFormat() {
 return new CSVFormat(',', '\n', null, null, null, null, Quote.MINIMAL);
 }
}