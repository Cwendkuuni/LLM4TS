import static org.junit.Assert.*;
import org.junit.Test;
import java.io.*;
import corina.formats.*;
import corina.ui.*;
import corina.util.*;
import corina.*;

public class TucsonSimpleTest {
    
    @Test
    public void testToString() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        String expected = "Format TucsonSimple";
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
        BufferedReader r = new BufferedReader(new FileReader("sample.txt"));
        tucsonSimple.load(r);
    }
    
    @Test
    public void testMake6digitCode_withId() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.meta.put("id", 123456);
        String expected = "123456";
        String actual = tucsonSimple.make6digitCode(sample);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testMake6digitCode_withNoId() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        String expected = "000000";
        String actual = tucsonSimple.make6digitCode(sample);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testMake6digitCode_withLongId() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.meta.put("id", 1234567890);
        String expected = "123456";
        String actual = tucsonSimple.make6digitCode(sample);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testSaveRowHeader_lessThanStart() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedWriter w = new BufferedWriter(new FileWriter("output.txt"));
        Sample sample = new Sample();
        sample.range = new Range(new Year(2000), new Year(2019));
        String code = "123456";
        int yearWidth = 4;
        Year y = new Year(1999);
        
        tucsonSimple.saveRowHeader(w, sample, code, yearWidth, y);
        w.close();
        
        BufferedReader r = new BufferedReader(new FileReader("output.txt"));
        String expected = "1234 1999";
        String actual = r.readLine();
        r.close();

        assertEquals(expected, actual);
    }
    
    @Test
    public void testSaveRowHeader_greaterThanOrEqualStart() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedWriter w = new BufferedWriter(new FileWriter("output.txt"));
        Sample sample = new Sample();
        sample.range = new Range(new Year(2000), new Year(2019));
        String code = "123456";
        int yearWidth = 4;
        Year y = new Year(2000);
        
        tucsonSimple.saveRowHeader(w, sample, code, yearWidth, y);
        w.close();
        
        BufferedReader r = new BufferedReader(new FileReader("output.txt"));
        String expected = "123456 2000";
        String actual = r.readLine();
        r.close();

        assertEquals(expected, actual);
    }
    
    @Test
    public void testSaveData() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedWriter w = new BufferedWriter(new FileWriter("output.txt"));
        Sample sample = new Sample();
        sample.data.put(0, 1.0);
        sample.data.put(1, 2.0);
        sample.range = new Range(new Year(2000), new Year(2001));
        
        tucsonSimple.saveData(sample, w);
        w.close();
        
        BufferedReader r = new BufferedReader(new FileReader("output.txt"));
        String expected = "000000 2000                                " + 
            "1.00                                \n" +
            "000000 2001                                " + 
            "2.00                                \n";
        String actual = r.readLine() + "\n" + r.readLine();
        r.close();

        assertEquals(expected, actual);
    }
    
    @Test
    public void testComputeRange_greaterThanOrEqualOne() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.range = new Range(new Year(2000), new Year(2019));
        
        Range expected = sample.range;
        Range actual = tucsonSimple.computeRange(sample);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testComputeRange_add8000_greaterThanOrEqualOne() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.range = new Range(new Year(-7999), new Year(-1));
        
        Range expected = sample.range.redateBy(8000);
        Range actual = tucsonSimple.computeRange(sample);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testComputeRange_redateStartTo1001() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.range = new Range(new Year(-1000), new Year(-1));
        
        Range expected = sample.range.redateStartTo(new Year(1001));
        Range actual = tucsonSimple.computeRange(sample);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testSave() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedWriter w = new BufferedWriter(new FileWriter("output.txt"));
        Sample sample = new Sample();
        sample.data.put(0, 1.0);
        sample.data.put(1, 2.0);
        sample.range = new Range(new Year(2000), new Year(2001));
        
        tucsonSimple.save(sample, w);
        w.close();
        
        BufferedReader r = new BufferedReader(new FileReader("output.txt"));
        String expected = "000000 2000                                " + 
            "1.00                                \n" +
            "000000 2001                                " + 
            "2.00                                \n";
        String actual = r.readLine() + "\n" + r.readLine();
        r.close();

        assertEquals(expected, actual);
    }
    
}