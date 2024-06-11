import static org.junit.Assert.*;
import org.junit.Test;

public class TucsonSimpleTest {

  @Test
  public void testToString() {
    TucsonSimple tucsonSimple = new TucsonSimple();
    String expected = "format.tucsonsimple";
    String actual = tucsonSimple.toString();
    assertEquals(expected, actual);
  }

  @Test
  public void testGetDefaultExtension() {
    TucsonSimple tucsonSimple = new TucsonSimple();
    String expected = ".RWL";
    String actual = tucsonSimple.getDefaultExtension();
    assertEquals(expected, actual);
  }

  @Test(expected = WrongFiletypeException.class)
  public void testLoad() throws IOException {
    TucsonSimple tucsonSimple = new TucsonSimple();
    BufferedReader reader = new BufferedReader(new StringReader(""));
    tucsonSimple.load(reader);
  }

  @Test
  public void testMake6digitCode() {
    TucsonSimple tucsonSimple = new TucsonSimple();
    Sample sample = new Sample();
    sample.meta.put("id", "12345678");
    String expected = "123456";
    String actual = tucsonSimple.make6digitCode(sample);
    assertEquals(expected, actual);
  }

  @Test
  public void testSaveRowHeader() throws IOException {
    TucsonSimple tucsonSimple = new TucsonSimple();
    Sample sample = new Sample();
    BufferedWriter writer = new BufferedWriter(new StringWriter());
    Year year = new Year(2021);
    tucsonSimple.saveRowHeader(writer, sample, "123456", 6, year);
    writer.flush();
    String expected = "123456 2021";
    String actual = writer.toString();
    assertEquals(expected, actual);
  }

  @Test
  public void testSaveData() throws IOException {
    TucsonSimple tucsonSimple = new TucsonSimple();
    Sample sample = new Sample();
    sample.data.put(0, 1);
    sample.range = new Range(new Year(2000), new Year(2005));
    BufferedWriter writer = new BufferedWriter(new StringWriter());
    tucsonSimple.saveData(sample, writer);
    writer.flush();
    String expected = "000000 2000" + System.lineSeparator() + "  1    " + System.lineSeparator() + "  0    "
        + System.lineSeparator() + "  0    " + System.lineSeparator() + "  0    " + System.lineSeparator()
        + "  0    " + System.lineSeparator() + "  0    " + System.lineSeparator() + "  0    " + System.lineSeparator()
        + "  0    " + System.lineSeparator() + "  0    " + System.lineSeparator() + "  0    " + System.lineSeparator()
        + "  0    " + System.lineSeparator() + "  0    " + System.lineSeparator() + "  0    " + System.lineSeparator()
        + "  0    " + System.lineSeparator() + "  0    " + System.lineSeparator() + "  0    " + System.lineSeparator()
        + "  0    " + System.lineSeparator() + "  0    " + System.lineSeparator() + "  0    " + System.lineSeparator()
        + "  0    ";
    String actual = writer.toString();
    assertEquals(expected, actual);
  }

  @Test
  public void testComputeRange() {
    TucsonSimple tucsonSimple = new TucsonSimple();
    Sample sample = new Sample();
    sample.range = new Range(new Year(-10), new Year(2022));
    Range expected = new Range(new Year(-10), new Year(2022));
    Range actual = tucsonSimple.computeRange(sample);
    assertEquals(expected, actual);
  }

  @Test
  public void testSave() throws IOException {
    TucsonSimple tucsonSimple = new TucsonSimple();
    Sample sample = new Sample();
    BufferedWriter writer = new BufferedWriter(new StringWriter());
    tucsonSimple.save(sample, writer);
    writer.flush();
    String expected = "";
    String actual = writer.toString();
    assertEquals(expected, actual);
  }
}