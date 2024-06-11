import static org.junit.Assert.*;
import org.junit.Test;

public class CSVPrinterTest {

   @Test
   public void testCSVPrinterConstructor() {
      CSVPrinter printer = new CSVPrinter(null,null);
      assertNotNull(printer);
   }

   @Test
   public void testCSVPrinterClose() {
      CSVPrinter printer = new CSVPrinter(null,null);
      Exception exception = null;
      try {
         printer.close();
      } catch (Exception e) {
         exception = e;
      }
      assertNull(exception);
   }

   @Test
   public void testCSVPrinterFlush() {
      CSVPrinter printer = new CSVPrinter(null,null);
      Exception exception = null;
      try {
         printer.flush();
      } catch (Exception e) {
         exception = e;
      }
      assertNull(exception);
   }

   @Test
   public void testCSVPrinterPrint() {
      CSVPrinter printer = new CSVPrinter(null,null);
      String value = "test";
      Exception exception = null;
      try {
         printer.print(value);
      } catch (Exception e) {
         exception = e;
      }
      assertNull(exception);
   }

   @Test
   public void testCSVPrinterPrintComment() {
      CSVPrinter printer = new CSVPrinter(null,null);
      String comment = "test";
      Exception exception = null;
      try {
         printer.printComment(comment);
      } catch (Exception e) {
         exception = e;
      }
      assertNull(exception);
   }

   @Test
   public void testCSVPrinterPrintln() {
      CSVPrinter printer = new CSVPrinter(null,null);
      Exception exception = null;
      try {
         printer.println();
      } catch (Exception e) {
         exception = e;
      }
      assertNull(exception);
   }

   @Test
   public void testCSVPrinterPrintRecordIterable() {
      CSVPrinter printer = new CSVPrinter(null,null);
      Iterable<String> values = new ArrayList<String>();
      Exception exception = null;
      try {
         printer.printRecord(values);
      } catch (Exception e) {
         exception = e;
      }
      assertNull(exception);
   }

   @Test
   public void testCSVPrinterPrintRecordObjectArray() {
      CSVPrinter printer = new CSVPrinter(null,null);
      Object[] values = new Object[]{"test"};
      Exception exception = null;
      try {
         printer.printRecord(values);
      } catch (Exception e) {
         exception = e;
      }
      assertNull(exception);
   }

   @Test
   public void testCSVPrinterPrintRecordsIterable() {
      CSVPrinter printer = new CSVPrinter(null,null);
      Iterable<Object> values = new ArrayList<Object>();
      Exception exception = null;
      try {
         printer.printRecords(values);
      } catch (Exception e) {
         exception = e;
      }
      assertNull(exception);
   }

   @Test
   public void testCSVPrinterPrintRecordsObjectArray() {
      CSVPrinter printer = new CSVPrinter(null,null);
      Object[] values = new Object[]{"test"};
      Exception exception = null;
      try {
         printer.printRecords(values);
      } catch (Exception e) {
         exception = e;
      }
      assertNull(exception);
   }

   @Test
   public void testCSVPrinterPrintRecordsResultSet() {
      CSVPrinter printer = new CSVPrinter(null,null);
      ResultSet resultSet = null;
      Exception exception = null;
      try {
         printer.printRecords(resultSet);
      } catch (Exception e) {
         exception = e;
      }
      assertNull(exception);
   }

   @Test
   public void testCSVPrinterGetOut() {
      CSVPrinter printer = new CSVPrinter(null,null);
      assertNotNull(printer.getOut());
   }

}