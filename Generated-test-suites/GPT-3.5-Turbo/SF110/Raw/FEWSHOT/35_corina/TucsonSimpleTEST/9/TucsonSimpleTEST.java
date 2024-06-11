import static org.junit.Assert.*;
import org.junit.Test;
import java.io.*;
import corina.*;
import corina.util.*;
import corina.ui.*;

public class TucsonSimpleTest {
    @Test
    public void testToString() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        assertEquals("format.tucsonsimple", tucsonSimple.toString());
    }

    @Test
    public void testGetDefaultExtension() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        assertEquals(".RWL", tucsonSimple.getDefaultExtension());
    }

    @Test(expected = WrongFiletypeException.class)
    public void testLoad() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedReader r = new BufferedReader(new FileReader("testFile.rwl"));
        tucsonSimple.load(r);
    }

    @Test
    public void testMake6digitCode() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        
        Sample sample1 = new Sample();
        sample1.meta.put("id", "123456789");
        assertEquals("123456", tucsonSimple.make6digitCode(sample1));
        
        Sample sample2 = new Sample();
        assertEquals("000000", tucsonSimple.make6digitCode(sample2));
    }

    @Test
    public void testSaveRowHeader() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedWriter w = new BufferedWriter(new FileWriter("testOutput.txt"));
        
        Sample sample = new Sample();
        String code = "123456";
        int yearWidth = 6;
        Year y = new Year(2020);

        tucsonSimple.saveRowHeader(w, sample, code, yearWidth, y);
        
        w.close();
    }

    @Test
    public void testSaveData() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedWriter w = new BufferedWriter(new FileWriter("testOutput.txt"));
        
        Sample sample = new Sample();
        
        tucsonSimple.saveData(sample, w);
        
        w.close();
    }

    @Test
    public void testComputeRange() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        
        Sample sample1 = new Sample();
        sample1.range = new Range(new Year(2000), new Year(2010));
        assertEquals(sample1.range, tucsonSimple.computeRange(sample1));
        
        Sample sample2 = new Sample();
        sample2.range = new Range(new Year(1000), new Year(2010));
        assertEquals(sample2.range.redateBy(8000), tucsonSimple.computeRange(sample2));
        
        Sample sample3 = new Sample();
        sample3.range = new Range(new Year(500), new Year(1000));
        assertEquals(sample3.range.redateStartTo(new Year(1001)), tucsonSimple.computeRange(sample3));
    }

    @Test
    public void testSave() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedWriter w = new BufferedWriter(new FileWriter("testOutput.txt"));
        
        Sample sample = new Sample();
        
        tucsonSimple.save(sample, w);
        
        w.close();
    }
}