package corina.formats;

import org.junit.Test;
import static org.junit.Assert.*;

import corina.ui.*;
import java.io.*;
import corina.util.*;
import corina.*;

public class TucsonSimpleTest {

  @Test
  public void testToString() {
    TucsonSimple tucsonSimple = new TucsonSimple();
    assertEquals("TucsonSimple", tucsonSimple.toString());
  }

  @Test
  public void testGetDefaultExtension() {
    TucsonSimple tucsonSimple = new TucsonSimple();
    assertEquals(".RWL", tucsonSimple.getDefaultExtension());
  }

  @Test(expected = WrongFiletypeException.class)
  public void testLoad() throws IOException {
    TucsonSimple tucsonSimple = new TucsonSimple();
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    tucsonSimple.load(reader);
  }

  @Test
  public void testMake6digitCode() {
    TucsonSimple tucsonSimple = new TucsonSimple();

    // Test when meta contains key "id"
    Sample sampleWithMetaId = new Sample();
    sampleWithMetaId.meta.put("id", "123456");
    String codeWithMetaId = tucsonSimple.make6digitCode(sampleWithMetaId);
    assertEquals("123456", codeWithMetaId);

    // Test when meta does not contain key "id"
    Sample sampleWithoutMetaId = new Sample();
    String codeWithoutMetaId = tucsonSimple.make6digitCode(sampleWithoutMetaId);
    assertEquals("000000", codeWithoutMetaId);
  }

  @Test
  public void testSaveRowHeaderWhenYIsBeforeStart() throws IOException {
    TucsonSimple tucsonSimple = new TucsonSimple();
    BufferedWriter writer = new BufferedWriter(new StringWriter());
    Sample sample = new Sample();
    String code = "123456";
    int yearWidth = 6;
    Year year = new Year(2000);

    tucsonSimple.saveRowHeader(writer, sample, code, yearWidth, year);
    String expected = "123456  2000";
    assertEquals(expected, writer.toString());
  }

  @Test
  public void testSaveRowHeaderWhenYIsStartAndNotProcessed() throws IOException {
    TucsonSimple tucsonSimple = new TucsonSimple();
    BufferedWriter writer = new BufferedWriter(new StringWriter());
    Sample sample = new Sample();
    String code = "123456";
    int yearWidth = 6;
    Year year = new Year(2000);

    tucsonSimple.saveRowHeader(writer, sample, code, yearWidth, year);
    String expected = "123456  2000";
    assertEquals(expected, writer.toString());
  }

  @Test
  public void testSaveRowHeaderWhenYIsAfterStartAndNotProcessed() throws IOException {
    TucsonSimple tucsonSimple = new TucsonSimple();
    BufferedWriter writer = new BufferedWriter(new StringWriter());
    Sample sample = new Sample();
    String code = "123456";
    int yearWidth = 6;
    Year year = new Year(2001);

    tucsonSimple.saveRowHeader(writer, sample, code, yearWidth, year);
    String expected = "123456  2001";
    assertEquals(expected, writer.toString());
  }

  @Test
  public void testSaveRowHeaderWhenYIsStartAndProcessed() throws IOException {
    TucsonSimple tucsonSimple = new TucsonSimple();
    BufferedWriter writer = new BufferedWriter(new StringWriter());
    Sample sample = new Sample();
    String code = "000000";
    int yearWidth = 4;
    Year year = new Year(2000);

    tucsonSimple.saveRowHeader(writer, sample, code, yearWidth, year);
    String expected = "0000 2000";
    assertEquals(expected, writer.toString());
  }

  @Test
  public void testSaveRowHeaderWhenYIsAfterStartAndProcessed() throws IOException {
    TucsonSimple tucsonSimple = new TucsonSimple();
    BufferedWriter writer = new BufferedWriter(new StringWriter());
    Sample sample = new Sample();
    String code = "000000";
    int yearWidth = 4;
    Year year = new Year(2001);

    tucsonSimple.saveRowHeader(writer, sample, code, yearWidth, year);
    String expected = "0000 2001";
    assertEquals(expected, writer.toString());
  }

  @Test
  public void testSaveDataWhenNotSummedAndNotProcessed() throws IOException {
    TucsonSimple tucsonSimple = new TucsonSimple();
    BufferedWriter writer = new BufferedWriter(new StringWriter());
    Sample sample = new Sample();
    sample.range = new Range(new Year(2000), new Year(2002));
    sample.data.put(0, 1.23);
    sample.data.put(1, 4.56);
    sample.data.put(2, 7.89);

    tucsonSimple.saveData(sample, writer);
    String expected = "      1      4      7" + System.lineSeparator();
    assertEquals(expected, writer.toString());
  }

  @Test
  public void testSaveDataWhenSummedAndNotProcessed() throws IOException {
    TucsonSimple tucsonSimple = new TucsonSimple();
    BufferedWriter writer = new BufferedWriter(new StringWriter());
    Sample sample = new Sample();
    sample.range = new Range(new Year(2000), new Year(2002));
    sample.data.put(0, 1.23);
    sample.data.put(1, 4.56);
    sample.data.put(2, 7.89);
    sample.count.put(0, 10);
    sample.count.put(1, 20);
    sample.count.put(2, 30);

    tucsonSimple.saveData(sample, writer);
    String expected = "      1      4      7  10  20  30" + System.lineSeparator();
    assertEquals(expected, writer.toString());
  }

  @Test
  public void testSaveDataWhenProcessed() throws IOException {
    TucsonSimple tucsonSimple = new TucsonSimple();
    BufferedWriter writer = new BufferedWriter(new StringWriter());
    Sample sample = new Sample();
    sample.range = new Range(new Year(2000), new Year(2002));
    sample.data.put(0, 1.23);
    sample.data.put(1, 4.56);
    sample.data.put(2, 7.89);

    for (int i = 0; i < 9; i++) {
      tucsonSimple.saveData(sample, writer);
    }

    String expected = "      1      4      7   999" + System.lineSeparator()
        + "      1      4      7   999" + System.lineSeparator()
        + "      1      4      7   999" + System.lineSeparator()
        + "      1      4      7   999" + System.lineSeparator()
        + "      1      4      7   999" + System.lineSeparator()
        + "      1      4      7   999" + System.lineSeparator()
        + "      1      4      7   999" + System.lineSeparator()
        + "      1      4      7   999" + System.lineSeparator()
        + "      1      4      7   999" + System.lineSeparator();
    assertEquals(expected, writer.toString());
  }

  @Test
  public void testComputeRangeWhenStartIsGreaterThanOne() {
    TucsonSimple tucsonSimple = new TucsonSimple();
    Sample sample = new Sample();
    sample.range = new Range(new Year(2000), new Year(2002));

    Range computedRange = tucsonSimple.computeRange(sample);
    assertEquals(sample.range, computedRange);
  }

  @Test
  public void testComputeRangeWhenStartPlus8000IsGreaterThanOne() {
    TucsonSimple tucsonSimple = new TucsonSimple();
    Sample sample = new Sample();
    sample.range = new Range(new Year(1), new Year(2002));

    Range computedRange = tucsonSimple.computeRange(sample);
    assertEquals(sample.range.redateBy(8000), computedRange);
  }

  @Test
  public void testComputeRangeWhenStartIsLessThanOne() {
    TucsonSimple tucsonSimple = new TucsonSimple();
    Sample sample = new Sample();
    sample.range = new Range(new Year(1), new Year(1000));

    Range computedRange = tucsonSimple.computeRange(sample);
    assertEquals(sample.range.redateStartTo(new Year(1001)), computedRange);
  }

  @Test
  public void testSave() throws IOException {
    TucsonSimple tucsonSimple = new TucsonSimple();
    BufferedWriter writer = new BufferedWriter(new StringWriter());
    Sample sample = new Sample();
    sample.range = new Range(new Year(2000), new Year(2002));
    sample.data.put(0, 1.23);
    sample.data.put(1, 4.56);
    sample.data.put(2, 7.89);

    tucsonSimple.save(sample, writer);
    String expected = "      1      4      7" + System.lineSeparator();
    assertEquals(expected, writer.toString());
  }
}