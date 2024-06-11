import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CSVPrinterTest {

  private static CSVPrinter csvPrinter;

  @BeforeClass
  public static void setUp() {
    csvPrinter = new CSVPrinter(null, null);
  }

  @Test
  public void testClose() {
    try {
      csvPrinter.close();
    } catch (Exception e) {
      assertEquals("java.io.IOException", e.getClass().getName());
    }
  }

  @Test
  public void testFlush() {
    try {
      csvPrinter.flush();
    } catch (Exception e) {
      assertEquals("java.io.IOException", e.getClass().getName());
    }
  }

  @Test
  public void testPrintNullValue() {
    try {
      csvPrinter.print(null);
    } catch (Exception e) {
      assertEquals("java.io.IOException", e.getClass().getName());
    }
  }

  @Test
  public void testPrintValue() {
    try {
      csvPrinter.print(123);
    } catch (Exception e) {
      assertEquals("java.io.IOException", e.getClass().getName());
    }
  }

  @Test
  public void testPrintComment() {
    try {
      csvPrinter.printComment("This is a comment");
    } catch (Exception e) {
      assertEquals("java.io.IOException", e.getClass().getName());
    }
  }

  @Test
  public void testPrintln() {
    try {
      csvPrinter.println();
    } catch (Exception e) {
      assertEquals("java.io.IOException", e.getClass().getName());
    }
  }

  @Test
  public void testPrintRecordIterable() {
    try {
      csvPrinter.printRecord(1, 2, 3);
    } catch (Exception e) {
      assertEquals("java.io.IOException", e.getClass().getName());
    }
  }

  @Test
  public void testPrintRecordArray() {
    try {
      csvPrinter.printRecord(new Object[]{1, 2, 3});
    } catch (Exception e) {
      assertEquals("java.io.IOException", e.getClass().getName());
    }
  }

  @Test
  public void testPrintRecordsIterable() {
    try {
      csvPrinter.printRecords(new Object[]{1, 2, 3});
    } catch (Exception e) {
      assertEquals("java.io.IOException", e.getClass().getName());
    }
  }

  @Test
  public void testPrintRecordsArray() {
    try {
      csvPrinter.printRecords(new Object[]{1, 2, 3});
    } catch (Exception e) {
      assertEquals("java.io.IOException", e.getClass().getName());
    }
  }

  @Test
  public void testPrintRecordsResultSet() {
    try {
      csvPrinter.printRecords(null);
    } catch (Exception e) {
      assertEquals("java.io.IOException", e.getClass().getName());
    }
  }

  @Test
  public void testGetOut() {
    Appendable result = csvPrinter.getOut();
    assertEquals(null, result);
  }
}