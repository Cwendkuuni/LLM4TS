import org.apache.commons.csv.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CSVPrinterTest {

 private Appendable out;
 private CSVPrinter printer;

 @Before
 public void setUp() {
 StringWriter stringWriter = new StringWriter();
 out = stringWriter;
 CSVFormat format = new CSVFormat(CSVFormat.DEFAULT.withDelimiter(',').withHeader());
 printer = new CSVPrinter(out, format);
 }

 @After
 public void tearDown() throws IOException {
 printer.close();
 }

 @Test
 public void testPrintString() throws IOException {
 printer.print("hello world");
 printer.println();
 assertEquals("hello world\n", out.toString());
 }

 @Test
 public void testPrintObject() throws IOException {
 String testString = "testString";
 printer.print(testString);
 printer.println();
 assertEquals("testString\n", out.toString());
 }

 @Test
 public void testPrintNull() throws IOException {
 printer.print(null);
 printer.println();
 assertEquals("\"\"\n", out.toString());
 }

 @Test
 public void testPrintMultipleStrings() throws IOException {
 printer.print("hello");
 printer.print(" world");
 printer.println();
 assertEquals("hello, world\n", out.toString());
 }

 @Test
 public void testPrintMultipleObjects() throws IOException {
 Object obj1 = "string1";
 Object obj2 = "string2";
 printer.print(obj1);
 printer.print(obj2);
 printer.println();
 assertEquals(obj1 + "," + obj2 + "\n", out.toString());
 }

 @Test
 public void testPrintMultipleObjectsWithQuotes() throws IOException {
 Object obj1 = "string with \", and ' in it";
 Object obj2 = "another string";
 printer.print(obj1);
 printer.print(obj2);
 printer.println();
 assertEquals(format.getQuoteChar() + obj1 + format.getQuoteChar() + "," + obj2 + "\n", out.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidFormat() throws IOException {
 CSVFormat invalidFormat = new CSVFormat(null);
 printer = new CSVPrinter(out, invalidFormat);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullOut() throws IOException {
 CSVFormat format = new CSVFormat(CSVFormat.DEFAULT);
 printer = new CSVPrinter(null, format);
 }

 @Test(expected = IOException.class)
 public void testCloseStreamBeforeFlush() throws IOException {
 printer.close();
 printer.flush();
 }

 @Test
 public void testPrintComment() throws IOException {
 printer.printComment("this is a comment");
 printer.println();
 String expected = format.getCommentStart() + "\n" + format.getCommentStart() + "this is a comment\n";
 assertEquals(expected, out.toString());
 }

 @Test
 public void testPrintMultipleRecords() throws IOException {
 printer.print("record1,");
 printer.print("record2,");
 printer.println();
 printer.print("record3,");
 printer.print("record4,");
 printer.println();
 assertEquals("record1,record2\nrecord3,record4\n", out.toString());
 }
}