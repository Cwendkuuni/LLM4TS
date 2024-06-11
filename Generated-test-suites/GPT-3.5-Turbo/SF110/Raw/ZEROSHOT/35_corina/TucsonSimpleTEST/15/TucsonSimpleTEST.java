import org.junit.Test;

public class TucsonSimpleTest {
  
  @Test
  public void testToString() {
    TucsonSimple tucsonSimple = new TucsonSimple();
    String expected = "format.tucsonsimple";
    String result = tucsonSimple.toString();
    assertEquals(expected, result);
  }
  
  @Test
  public void testGetDefaultExtension() {
    TucsonSimple tucsonSimple = new TucsonSimple();
    String expected = ".RWL";
    String result = tucsonSimple.getDefaultExtension();
    assertEquals(expected, result);
  }
  
  @Test(expected=WrongFiletypeException.class)
  public void testLoad() throws IOException {
    TucsonSimple tucsonSimple = new TucsonSimple();
    BufferedReader r = new BufferedReader(new StringReader(""));
    tucsonSimple.load(r);
  }
  
  @Test
  public void testMake6digitCode() {
    TucsonSimple tucsonSimple = new TucsonSimple();
    Sample s = new Sample();
    s.meta.put("id", "123456789");
    String expected = "123456";
    String result = tucsonSimple.make6digitCode(s);
    assertEquals(expected, result);
    
    Sample s2 = new Sample();
    String expected2 = "000000";
    String result2 = tucsonSimple.make6digitCode(s2);
    assertEquals(expected2, result2);
  }
  
  @Test
  public void testSaveRowHeader() throws IOException {
    TucsonSimple tucsonSimple = new TucsonSimple();
    Sample s = new Sample();
    BufferedWriter w = new BufferedWriter(new StringWriter());
    String code = "123456";
    int yearWidth = 6;
    Year y = new Year(2000);
    tucsonSimple.saveRowHeader(w, s, code, yearWidth, y);
    String expected = "123456  2000";
    String result = w.toString();
    assertEquals(expected, result);
  }
  
  @Test
  public void testSaveData() throws IOException {
    TucsonSimple tucsonSimple = new TucsonSimple();
    Sample s = new Sample();
    s.data.add(1);
    s.data.add(2);
    s.range.setStart(new Year(2000));
    s.range.setEnd(new Year(2001));
    BufferedWriter w = new BufferedWriter(new StringWriter());
    tucsonSimple.saveData(s, w);
    String expected = "      1      2";
    String result = w.toString();
    assertEquals(expected, result);
  }
  
  @Test
  public void testComputeRange() {
    TucsonSimple tucsonSimple = new TucsonSimple();
    Sample s = new Sample();
    s.range.setStart(new Year(1));
    s.range.setEnd(new Year(10));
    Range expected = new Range(new Year(1), new Year(10));
    Range result = tucsonSimple.computeRange(s);
    assertEquals(expected, result);
    
    Sample s2 = new Sample();
    s2.range.setStart(new Year(-7999));
    s2.range.setEnd(new Year(1));
    Range expected2 = new Range(new Year(1), new Year(8000));
    Range result2 = tucsonSimple.computeRange(s2);
    assertEquals(expected2, result2);
    
    Sample s3 = new Sample();
    s3.range.setStart(new Year(-1000));
    s3.range.setEnd(new Year(2000));
    Range expected3 = new Range(new Year(1001), new Year(2000));
    Range result3 = tucsonSimple.computeRange(s3);
    assertEquals(expected3, result3);
  }
  
  @Test
  public void testSave() throws IOException {
    TucsonSimple tucsonSimple = new TucsonSimple();
    Sample s = new Sample();
    s.data.add(1);
    s.range.setStart(new Year(2000));
    s.range.setEnd(new Year(2001));
    BufferedWriter w = new BufferedWriter(new StringWriter());
    tucsonSimple.save(s, w);
    String expected = "      1      ";
    String result = w.toString();
    assertEquals(expected, result);
  }
}