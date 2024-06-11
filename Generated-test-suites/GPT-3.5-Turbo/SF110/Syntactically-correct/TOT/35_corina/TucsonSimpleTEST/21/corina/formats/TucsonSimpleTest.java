import org.junit.*;
import static org.junit.Assert.*;
import org.mockito.Mockito;

public class TucsonSimpleTest {
  
  private TucsonSimple tucsonSimple;
  private Sample mockedSample;
  private BufferedReader mockedBufferedReader;
  private BufferedWriter mockedBufferedWriter;
  
  @Before
  public void setUp() {
    tucsonSimple = new TucsonSimple();
    mockedSample = Mockito.mock(Sample.class);
    mockedBufferedReader = Mockito.mock(BufferedReader.class);
    mockedBufferedWriter = Mockito.mock(BufferedWriter.class);
  }
  
  // Test case for toString() method
  @Test
  public void testToString() {
    assertEquals("format.tucsonsimple", tucsonSimple.toString());
  }
  
  // Test case for getDefaultExtension() method
  @Test
  public void testGetDefaultExtension() {
    assertEquals(".RWL", tucsonSimple.getDefaultExtension());
  }
  
  // Test case for load() method
  @Test(expected = WrongFiletypeException.class)
  public void testLoad() throws IOException {
    tucsonSimple.load(mockedBufferedReader);
  }
  
  // Test case for make6digitCode() method - typical use case
  @Test
  public void testMake6digitCode_typical() {
    Sample sample = new Sample();
    sample.meta.put("id", "123456");
    assertEquals("123456", tucsonSimple.make6digitCode(sample));
  }
  
  // Test case for make6digitCode() method - edge case
  @Test
  public void testMake6digitCode_edge() {
    Sample sample = new Sample();
    assertEquals("000000", tucsonSimple.make6digitCode(sample));
  }
  
  // Test case for saveRowHeader() method
  @Test
  public void testSaveRowHeader() throws IOException {
    Year year = new Year(2022);
    tucsonSimple.saveRowHeader(mockedBufferedWriter, mockedSample, "123456", 6, year);
    Mockito.verify(mockedBufferedWriter).write("123456 2022");
  }
  
  // Test case for saveData() method
  @Test
  public void testSaveData() throws IOException {
    Year start = new Year(2020);
    Year end = new Year(2022);
    Mockito.when(mockedSample.range).thenReturn(new Range(start, end));
    
    tucsonSimple.saveData(mockedSample, mockedBufferedWriter);
    
    Mockito.verify(mockedBufferedWriter).write("      2020");
    Mockito.verify(mockedBufferedWriter).write("      2021");
    Mockito.verify(mockedBufferedWriter).write("      2022");
    Mockito.verify(mockedBufferedWriter).write("   999");
    Mockito.verify(mockedBufferedWriter).newLine();
  }
  
  // Test case for computeRange() method - typical use case
  @Test
  public void testComputeRange_typical() {
    Year start = new Year(2020);
    Year end = new Year(2022);
    Range sampleRange = new Range(start, end);
    Sample sample = new Sample();
    Mockito.when(mockedSample.range).thenReturn(sampleRange);
    
    Range result = tucsonSimple.computeRange(mockedSample);
    assertEquals(sampleRange, result);
  }
  
  // Test case for computeRange() method - edge case
  @Test
  public void testComputeRange_edge() {
    Year start = new Year(1);
    Year end = new Year(2022);
    Range sampleRange = new Range(start, end);
    Sample sample = new Sample();
    Mockito.when(mockedSample.range).thenReturn(sampleRange);
    
    Range result = tucsonSimple.computeRange(mockedSample);
    assertEquals(sampleRange.redateBy(8000), result);
  }
  
  // Test case for computeRange() method - error scenario
  @Test
  public void testComputeRange_error() {
    Year start = new Year(0);
    Year end = new Year(2022);
    Range sampleRange = new Range(start, end);
    Sample sample = new Sample();
    Mockito.when(mockedSample.range).thenReturn(sampleRange);
    
    Range result = tucsonSimple.computeRange(mockedSample);
    assertEquals(sampleRange.redateStartTo(new Year(1001)), result);
  }
  
  // Test case for save() method
  @Test
  public void testSave() throws IOException {
    Year start = new Year(2020);
    Year end = new Year(2022);
    Range sampleRange = new Range(start, end);
    Sample sample = new Sample();
    Mockito.when(mockedSample.range).thenReturn(sampleRange);
    
    tucsonSimple.save(mockedSample, mockedBufferedWriter);
    
    Mockito.verify(mockedBufferedWriter).write("      2020");
    Mockito.verify(mockedBufferedWriter).write("      2021");
    Mockito.verify(mockedBufferedWriter).write("      2022");
    Mockito.verify(mockedBufferedWriter).write("   999");
    Mockito.verify(mockedBufferedWriter).newLine();
  }
}