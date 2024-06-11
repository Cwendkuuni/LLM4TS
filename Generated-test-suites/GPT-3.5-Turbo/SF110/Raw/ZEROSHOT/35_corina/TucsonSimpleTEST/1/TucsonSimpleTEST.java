package corina.formats;

import corina.ui.*;
import java.io.*;
import corina.util.*;
import corina.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TucsonSimpleTest {
  
  private TucsonSimple tucsonSimple;
  
  @Before
  public void setUp() {
    tucsonSimple = new TucsonSimple();
  }
  
  @Test
  public void testToString() {
    String expected = "Tucson Simple";
    String actual = tucsonSimple.toString();
    
    assertEquals(expected, actual);
  }
  
  @Test
  public void testGetDefaultExtension() {
    String expected = ".RWL";
    String actual = tucsonSimple.getDefaultExtension();
    
    assertEquals(expected, actual);
  }
  
  @Test(expected = WrongFiletypeException.class)
  public void testLoad() throws IOException {
    BufferedReader r = null;
    tucsonSimple.load(r);
  }
  
  @Test
  public void testMake6digitCode_withIdInMeta() {
    Sample s = new Sample();
    s.meta.put("id", "123456789");
    
    String expected = "123456";
    String actual = tucsonSimple.make6digitCode(s);
    
    assertEquals(expected, actual);
  }
  
  @Test
  public void testMake6digitCode_noIdInMeta() {
    Sample s = new Sample();
    
    String expected = "000000";
    String actual = tucsonSimple.make6digitCode(s);
    
    assertEquals(expected, actual);
  }
  
  @Test
  public void testMake6digitCode_longCode() {
    Sample s = new Sample();
    s.meta.put("id", "1234567890");
    
    String expected = "123456";
    String actual = tucsonSimple.make6digitCode(s);
    
    assertEquals(expected, actual);
  }
  
  @Test
  public void testSaveRowHeader_rangeBeforeStart() throws IOException {
    BufferedWriter w = new BufferedWriter(new FileWriter("test.txt"));
    Sample s = new Sample();
    String code = "000000";
    int yearWidth = 6;
    Year y = new Year(1990);
    
    tucsonSimple.saveRowHeader(w, s, code, yearWidth, y);
    w.close();
    
    // Assertion
  }
  
  @Test
  public void testSaveRowHeader_rangeAfterStart() throws IOException {
    BufferedWriter w = new BufferedWriter(new FileWriter("test.txt"));
    Sample s = new Sample();
    String code = "000000";
    int yearWidth = 6;
    Year y = new Year(2020);
    
    tucsonSimple.saveRowHeader(w, s, code, yearWidth, y);
    w.close();
    
    // Assertion
  }
  
  @Test
  public void testSaveRowHeader_rangeEqualToStart() throws IOException {
    BufferedWriter w = new BufferedWriter(new FileWriter("test.txt"));
    Sample s = new Sample();
    String code = "000000";
    int yearWidth = 6;
    Year y = new Year(2020);
    
    tucsonSimple.saveRowHeader(w, s, code, yearWidth, y);
    w.close();
    
    // Assertion
  }
  
  @Test
  public void testSaveRowHeader_rangeBeforeStartWithProcessing() throws IOException {
    BufferedWriter w = new BufferedWriter(new FileWriter("test.txt"));
    Sample s = new Sample();
    String code = "000000";
    int yearWidth = 4;
    Year y = new Year(1990);
    
    tucsonSimple.saveRowHeader(w, s, code, yearWidth, y);
    w.close();
    
    // Assertion
  }
  
  @Test
  public void testSaveRowHeader_withYearPadding() throws IOException {
    BufferedWriter w = new BufferedWriter(new FileWriter("test.txt"));
    Sample s = new Sample();
    String code = "000000";
    int yearWidth = 4;
    Year y = new Year(2020);
    
    tucsonSimple.saveRowHeader(w, s, code, yearWidth, y);
    w.close();
    
    // Assertion
  }
  
  @Test
  public void testSaveData() throws IOException {
    BufferedWriter w = new BufferedWriter(new FileWriter("test.txt"));
    Sample s = new Sample();
    tucsonSimple.saveData(s, w);
    w.close();
    
    // Assertion
  }
  
  @Test
  public void testComputeRange_rangeStartBeforeYearOne() {
    Sample s = new Sample();
    s.range = new Range(new Year(1990), new Year(2020));
    
    Range expected = new Range(new Year(1001), new Year(2020));
    Range actual = tucsonSimple.computeRange(s);
    
    assertEquals(expected, actual);
  }
  
  @Test
  public void testComputeRange_rangeStartAfterYearOne() {
    Sample s = new Sample();
    s.range = new Range(new Year(2000), new Year(2020));
    
    Range expected = new Range(new Year(2000), new Year(2020));
    Range actual = tucsonSimple.computeRange(s);
    
    assertEquals(expected, actual);
  }
  
  @Test
  public void testComputeRange_rangeStartBeforeYearOneWithRedate() {
    Sample s = new Sample();
    s.range = new Range(new Year(-100), new Year(20));
    
    Range expected = new Range(new Year(7916), new Year(8020));
    Range actual = tucsonSimple.computeRange(s);
    
    assertEquals(expected, actual);
  }
  
  @Test
  public void testSave() throws IOException {
    BufferedWriter w = new BufferedWriter(new FileWriter("test.txt"));
    Sample s = new Sample();
    tucsonSimple.save(s, w);
    w.close();
    
    // Assertion
  }
}