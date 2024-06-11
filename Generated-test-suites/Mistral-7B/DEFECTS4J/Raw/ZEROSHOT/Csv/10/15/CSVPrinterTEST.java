import org.apache.commons.csv.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CSVPrinterTest {

 private CSVFormat format;
 private CSVPrinter printer;
 private Appendable out;

 @Before
 public void setUp() throws Exception {
 format = new CSVFormatBuilder() // build a simple CSVFormat
 .setHeader("Header1", "Header2")
 .setDelimiter(';')
 .build();
 out = new StringBuffer();
 printer = new CSVPrinter(out, format);
 }

 @After
 public void tearDown() throws Exception {
 printer.close();
 }

 @Test
 public void testPrint() throws Exception {
 printer.print("value1");
 printer.print("value2");
 printer.println();
 String csv = out.toString();
 assertEquals("value1;value2\n", csv);
 }

 @Test
 public void testPrintNull() throws Exception {
 printer.print(null);
 printer.print("value2");
 printer.println();
 String csv = out.toString();
 assertEquals("value2\n", csv);
 }

 @Test
 public void testPrintRecord() throws Exception {
 Object[] record = {"record1", "record2"};
 printer.printRecord(record);
 printer.println();
 String csv = out.toString();
 assertEquals("record1;record2\n", csv);
 }

 @Test
 public void testPrintRecords() throws Exception {
 Object[][] records = {{"record1", "record2"}, {"record3", "record4"}};
 printer.printRecords(Arrays.asList(records));
 printer.println();
 String csv = out.toString();
 assertEquals("record1;record2\nrecord3;record4\n", csv);
 }

 @Test
 public void testPrintComment() throws Exception {
 printer.printComment("comment");
 printer.println();
 String csv = out.toString();
 assertEquals("comment;\n", csv);
 }

 @Test
 public void testPrintQuotedValue() throws Exception {
 printer.print('"');
 printer.println();
 String csv = out.toString();
 assertEquals("\"\n", csv);
 }

 @Test
 public void testPrintEscapedValue() throws Exception {
 printer.print("\"hello\"world");
 printer.println();
 String csv = out.toString();
 assertEquals("\"hello\\\"world\n", csv);
 }

 @Test
 public void testPrintEscapedDelimiter() throws Exception {
 printer.setDelimiter('\\');
 printer.print("value1");
 printer.print("value2");
 printer.println();
 String csv = out.toString();
 assertEquals("value1\\value2\n", csv);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullFormat() throws Exception {
 try {
 printer = new CSVPrinter(out, null);
 } catch (IllegalArgumentException e) {
 throw new AssertionError("Unexpected exception", e);
 }
 }

 @Test(expected = IOException.class)
 public void testNullOut() throws Exception {
 try {
 printer = new CSVPrinter(null, format);
 } catch (IOException e) {
 // expected
 }
 }
}