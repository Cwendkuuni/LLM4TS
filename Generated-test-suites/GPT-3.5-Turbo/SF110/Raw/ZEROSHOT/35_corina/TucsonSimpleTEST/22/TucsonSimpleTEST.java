import org.junit.*;
import static org.junit.Assert.*;

public class TucsonSimpleTest {
  
  @Test
  public void testToString() {
    TucsonSimple tucson = new TucsonSimple();
    assertEquals("format.tucsonsimple", tucson.toString());
  }
  
  @Test
  public void testGetDefaultExtension() {
    TucsonSimple tucson = new TucsonSimple();
    assertEquals(".RWL", tucson.getDefaultExtension());
  }
  
  @Test(expected = WrongFiletypeException.class)
  public void testLoad() throws IOException {
    TucsonSimple tucson = new TucsonSimple();
    BufferedReader reader = new BufferedReader(new StringReader(""));
    tucson.load(reader);
  }
  
  @Test
  public void testMake6digitCode() {
    TucsonSimple tucson = new TucsonSimple();
    Sample sample = new Sample();
    sample.meta.put("id", "12345678");
    String code = tucson.make6digitCode(sample);
    assertEquals("123456", code);
    
    sample.meta.remove("id");
    code = tucson.make6digitCode(sample);
    assertEquals("000000", code);
  }
  
  @Test
  public void testSaveRowHeader() throws IOException {
    TucsonSimple tucson = new TucsonSimple();
    BufferedWriter writer = new BufferedWriter(new StringWriter());
    Sample sample = new Sample();
    String code = "123456";
    int yearWidth = 6;
    Year year = new Year(2022);
    tucson.saveRowHeader(writer, sample, code, yearWidth, year);
    writer.flush();
    String output = writer.toString();
    assertEquals("123456 2022", output);
  }
  
  @Test
  public void testSaveData() throws IOException {
    TucsonSimple tucson = new TucsonSimple();
    BufferedWriter writer = new BufferedWriter(new StringWriter());
    Sample sample = new Sample();
    sample.data.put(0, 1.23);
    sample.data.put(1, 4.56);
    sample.range = new Range(new Year(2020), new Year(2021));
    tucson.saveData(sample, writer);
    writer.flush();
    String output = writer.toString();
    assertEquals("1.23   4.56   999", output);
  }
  
  @Test
  public void testComputeRange() {
    TucsonSimple tucson = new TucsonSimple();
    Sample sample = new Sample();
    sample.range = new Range(new Year(1000), new Year(2000));
    Range range = tucson.computeRange(sample);
    assertEquals(new Range(new Year(1001), new Year(2000)), range);
    
    sample.range = new Range(new Year(1), new Year(2000));
    range = tucson.computeRange(sample);
    assertEquals(new Range(new Year(1), new Year(2000)), range);
    
    sample.range = new Range(new Year(-1), new Year(2000));
    range = tucson.computeRange(sample);
    assertEquals(new Range(new Year(8000), new Year(2000)), range);
  }
  
  @Test
  public void testSave() throws IOException {
    TucsonSimple tucson = new TucsonSimple();
    BufferedWriter writer = new BufferedWriter(new StringWriter());
    Sample sample = new Sample();
    sample.data.put(0, 1.23);
    sample.data.put(1, 4.56);
    sample.range = new Range(new Year(2020), new Year(2021));
    tucson.save(sample, writer);
    writer.flush();
    String output = writer.toString();
    assertEquals("1.23   4.56   999\n", output);
  }
  
}