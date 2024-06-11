import static org.junit.Assert.*;
import org.junit.Test;
import java.io.*;
import corina.util.*;
import corina.*;

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
        BufferedReader r = new BufferedReader(new FileReader("testFile.RWL"));
        tucsonSimple.load(r);
    }
    
    @Test
    public void testMake6digitCode() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.meta.put("id", "123456789");
        String expected = "123456";
        String actual = tucsonSimple.make6digitCode(sample);
        assertEquals(expected, actual);
        
        Sample sample2 = new Sample();
        String expected2 = "000000";
        String actual2 = tucsonSimple.make6digitCode(sample2);
        assertEquals(expected2, actual2);
    }
    
    @Test
    public void testSaveRowHeader() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        BufferedWriter w = new BufferedWriter(new FileWriter("testFile.RWL"));
        tucsonSimple.saveRowHeader(w, sample, "000000", 6, new Year(2000));
        w.close();
        BufferedReader r = new BufferedReader(new FileReader("testFile.RWL"));
        String expected = "000000  2000";
        String actual = r.readLine();
        r.close();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testSaveData() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.data.add(10);
        sample.data.add(20);
        sample.data.add(30);
        BufferedWriter w = new BufferedWriter(new FileWriter("testFile.RWL"));
        tucsonSimple.saveData(sample, w);
        w.close();
        BufferedReader r = new BufferedReader(new FileReader("testFile.RWL"));
        String expected = "000000     10     20     30";
        String actual = r.readLine();
        r.close();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testComputeRange() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.range = new Range(new Year(1999), new Year(2001));
        Range expected = new Range(new Year(1999), new Year(2001));
        Range actual = tucsonSimple.computeRange(sample);
        assertEquals(expected, actual);
        
        Sample sample2 = new Sample();
        sample2.range = new Range(new Year(-100), new Year(100));
        Range expected2 = new Range(new Year(7900), new Year(8100));
        Range actual2 = tucsonSimple.computeRange(sample2);
        assertEquals(expected2, actual2);
        
        Sample sample3 = new Sample();
        sample3.range = new Range(new Year(-100), new Year(100));
        Range expected3 = new Range(new Year(1001), new Year(100));
        Range actual3 = tucsonSimple.computeRange(sample3);
        assertEquals(expected3, actual3);
    }
    
    @Test
    public void testSave() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedWriter w = new BufferedWriter(new FileWriter("testFile.RWL"));
        Sample sample = new Sample();
        sample.data.add(10);
        sample.data.add(20);
        sample.data.add(30);
        tucsonSimple.save(sample, w);
        w.close();
        BufferedReader r = new BufferedReader(new FileReader("testFile.RWL"));
        String expected = "000000     10     20     30";
        String actual = r.readLine();
        r.close();
        assertEquals(expected, actual);
    }
}