import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.Before;
import org.junit.Test;

public class CSVPrinterTest {

  private Appendable out;
  private CSVFormat format;
  private CSVPrinter csvPrinter;

  @Before
  public void setUp() {
    out = mock(Appendable.class);
    format = CSVFormat.DEFAULT;
    csvPrinter = new CSVPrinter(out, format);
  }

  @Test
  public void whenPrintCalled_ThenValuePrinted() throws IOException {
    // Arrange
    String value = "testValue";

    // Act
    csvPrinter.print(value);

    // Assert
    verify(out).append(value);
  }

  @Test
  public void whenPrintRecordCalled_ThenValuesPrintedSeparatedByDelimiter() throws IOException {
    // Arrange
    String[] values = {"value1", "value2", "value3"};

    // Act
    csvPrinter.printRecord(values);

    // Assert
    verify(out).append("value1");
    verify(out).append(",");
    verify(out).append("value2");
    verify(out).append(",");
    verify(out).append("value3");
  }

  @Test
  public void whenPrintRecordCalledWithIterable_ThenValuesPrintedSeparatedByDelimiter() throws IOException {
    // Arrange
    Iterable<String> values = mock(Iterable.class);

    // Act
    csvPrinter.printRecord(values);

    // Assert
    verify(values).forEach(value -> {
      try {
        verify(out).append(value);
        verify(out).append(",");
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
  }

  @Test
  public void whenPrintCommentCalled_ThenCommentPrintedWithCommentStart() throws IOException {
    // Arrange
    String comment = "test comment";

    // Act
    csvPrinter.printComment(comment);

    // Assert
    verify(out).append(format.getCommentStart().charValue());
    verify(out).append(" ");
    verify(out).append(comment);
    verify(out).append(System.lineSeparator());
  }

  @Test
  public void whenPrintlnCalled_ThenRecordSeparatorPrinted() throws IOException {
    // Act
    csvPrinter.println();

    // Assert
    verify(out).append(format.getRecordSeparator());
  }

  @Test
  public void whenOutGetterCalled_ThenReturnOut() {
    // Act
    Appendable returnedOut = csvPrinter.getOut();

    // Assert
    assertEquals(out, returnedOut);
  }

}