import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;
import corina.formats.*;
import corina.ui.*;
import corina.util.*;
import corina.*;

public class TucsonSimpleTest {
  
  private TucsonSimple tucsonSimple;

  @Before
  public void setUp() {
      tucsonSimple = new TucsonSimple();
  }

  @Test
  public void testToString() {
      String expected = "TucsonSimple";
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
      BufferedReader r = new BufferedReader(new FileReader("wrongFile.txt"));
      tucsonSimple.load(r);
  }

  @Test
  public void testMake6digitCode() {
      Sample s = new Sample();
      s.meta.put("id", "1234567");
      String expected = "123456";
      String actual = tucsonSimple.make6digitCode(s);
      assertEquals(expected, actual);
  }

  @Test
  public void testMake6digitCodeWithNoID() {
      Sample s = new Sample();
      String expected = "000000";
      String actual = tucsonSimple.make6digitCode(s);
      assertEquals(expected, actual);
  }

  @Test
  public void testSaveRowHeader() {
      BufferedWriter w = new BufferedWriter(new FileWriter("testFile.txt"));
      Sample s = new Sample();
      String code = "123456";
      int yearWidth = 6;
      Year y = new Year(2000);
      String expected = "123456  2000";
      tucsonSimple.saveRowHeader(w, s, code, yearWidth, y);
      w.close();
      BufferedReader r = new BufferedReader(new FileReader("testFile.txt"));
      String actual = r.readLine();
      r.close();
      assertEquals(expected, actual);
  }

  @Test
  public void testSaveData() {
      BufferedWriter w = new BufferedWriter(new FileWriter("testFile.txt"));
      Sample s = new Sample();
      s.range = new Range(new Year(2000), new Year(2002));
      String expected = "   001 2000";
      tucsonSimple.saveData(s, w);
      w.close();
      BufferedReader r = new BufferedReader(new FileReader("testFile.txt"));
      String actual = r.readLine();
      r.close();
      assertEquals(expected, actual);
  }

  @Test
  public void testComputeRangeWithStartGreaterThanEqual1() {
      Sample s = new Sample();
      s.range = new Range(new Year(2000), new Year(2002));
      Range expected = new Range(new Year(2000), new Year(2002));
      Range actual = tucsonSimple.computeRange(s);
      assertEquals(expected, actual);
  }

  @Test
  public void testComputeRangeWithStartAdd8000GreaterThanEqual1() {
      Sample s = new Sample();
      s.range = new Range(new Year(9992), new Year(9994));
      Range expected = new Range(new Year(8000), new Year(8002));
      Range actual = tucsonSimple.computeRange(s);
      assertEquals(expected, actual);
  }

  @Test
  public void testComputeRangeWithStartLessThan1() {
      Sample s = new Sample();
      s.range = new Range(new Year(10), new Year(12));
      Range expected = new Range(new Year(1001), new Year(1003));
      Range actual = tucsonSimple.computeRange(s);
      assertEquals(expected, actual);
  }

  @Test
  public void testSave() {
      BufferedWriter w = new BufferedWriter(new FileWriter("testFile.txt"));
      Sample s = new Sample();
      String expected = "   001";
      tucsonSimple.save(s, w);
      w.close();
      BufferedReader r = new BufferedReader(new FileReader("testFile.txt"));
      String actual = r.readLine();
      r.close();
      assertEquals(expected, actual);
  }

  @Test
  public void testFullFunctionality() {
      Sample s = new Sample();
      s.range = new Range(new Year(2000), new Year(2002));
      s.data.put(0, 1);
      s.data.put(1, 2);
      s.data.put(2, 3);
      BufferedWriter w = new BufferedWriter(new FileWriter("testFile.txt"));
      
      // Testing saveData()
      String expected = "   001 2000   1   2   3";
      tucsonSimple.saveData(s, w);
      w.close();
      BufferedReader r = new BufferedReader(new FileReader("testFile.txt"));
      String actual = r.readLine();
      r.close();
      assertEquals(expected, actual);

      // Testing save()
      w = new BufferedWriter(new FileWriter("testFile.txt"));
      expected = "   001";
      tucsonSimple.save(s, w);
      w.close();
      r = new BufferedReader(new FileReader("testFile.txt"));
      actual = r.readLine();
      r.close();
      assertEquals(expected, actual);

      // Testing toString()
      expected = "TucsonSimple";
      actual = tucsonSimple.toString();
      assertEquals(expected, actual);

      // Testing getDefaultExtension()
      expected = ".RWL";
      actual = tucsonSimple.getDefaultExtension();
      assertEquals(expected, actual);
  }

}