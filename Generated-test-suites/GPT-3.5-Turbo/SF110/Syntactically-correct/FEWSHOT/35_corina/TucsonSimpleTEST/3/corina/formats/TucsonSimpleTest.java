package corina.formats;

import static org.junit.Assert.*;
import org.junit.Test;

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
        BufferedReader r = new BufferedReader(new FileReader("test.txt"));
        tucsonSimple.load(r);
    }
    
    @Test
    public void testMake6digitCode() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        
        Sample s1 = new Sample();
        s1.meta.put("id", "123456");
        assertEquals("123456", tucsonSimple.make6digitCode(s1));
        
        Sample s2 = new Sample();
        assertEquals("000000", tucsonSimple.make6digitCode(s2));
    }
    
    @Test
    public void testSaveRowHeader() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedWriter w = new BufferedWriter(new FileWriter("test.txt"));
        
        Sample s = new Sample();
        String code = "123456";
        int yearWidth = 6;
        Year y = new Year(2000);
        
        tucsonSimple.saveRowHeader(w, s, code, yearWidth, y);
        
        w.close();
        
        assertEquals("123456  2000", Files.readAllLines(Paths.get("test.txt")).get(0));
    }
    
    @Test
    public void testSaveData() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedWriter w = new BufferedWriter(new FileWriter("test.txt"));
        
        Sample s = new Sample();
        
        tucsonSimple.saveData(s, w);
        
        w.close();
        
        assertEquals("", Files.readAllLines(Paths.get("test.txt")).get(0));
    }
    
    @Test
    public void testComputeRange() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        
        Sample s1 = new Sample();
        s1.range = new Range(new Year(2000), new Year(2010));
        assertEquals(new Year(2000), tucsonSimple.computeRange(s1).getStart());
        assertEquals(new Year(2010), tucsonSimple.computeRange(s1).getEnd());
        
        Sample s2 = new Sample();
        s2.range = new Range(new Year(1000), new Year(2000));
        assertEquals(new Year(1001), tucsonSimple.computeRange(s2).getStart());
        assertEquals(new Year(2000), tucsonSimple.computeRange(s2).getEnd());
    }
    
    @Test
    public void testSave() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedWriter w = new BufferedWriter(new FileWriter("test.txt"));
        
        Sample s = new Sample();
        
        tucsonSimple.save(s, w);
        
        w.close();
        
        assertEquals("", Files.readAllLines(Paths.get("test.txt")).get(0));
    }
}