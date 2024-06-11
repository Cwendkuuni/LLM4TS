package corina.formats;

import org.junit.Test;
import static org.junit.Assert.*;

public class TucsonSimpleTest {

    @Test
    public void testToString() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        String expected = "Tucson Simple";
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
        BufferedReader r = new BufferedReader(new FileReader("testFile.rwl"));
        tucsonSimple.load(r);
    }
    
    @Test
    public void testMake6digitCode_withValidId() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.meta.put("id", "123456");
        
        String expected = "123456";
        String actual = tucsonSimple.make6digitCode(sample);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testMake6digitCode_withInvalidId() {
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
        sample.meta.put("id", "1234567890");
        
        String expected = "123456";
        String actual = tucsonSimple.make6digitCode(sample);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testSaveRowHeader_withStartBeforeRange() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedWriter w = new BufferedWriter(new FileWriter("testFile.rwl"));
        Sample sample = new Sample();
        sample.range = new Range(new Year(2000), new Year(2010));
        String code = "123456";
        int yearWidth = 6;
        Year y = new Year(1999);
        
        String expected = "1234561999";
        tucsonSimple.saveRowHeader(w, sample, code, yearWidth, y);
        w.close();
        
        BufferedReader r = new BufferedReader(new FileReader("testFile.rwl"));
        String actual = r.readLine();
        r.close();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testSaveRowHeader_withStartAfterRange() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedWriter w = new BufferedWriter(new FileWriter("testFile.rwl"));
        Sample sample = new Sample();
        sample.range = new Range(new Year(2000), new Year(2010));
        String code = "123456";
        int yearWidth = 6;
        Year y = new Year(2011);
        
        String expected = "123456 2011";
        tucsonSimple.saveRowHeader(w, sample, code, yearWidth, y);
        w.close();
        
        BufferedReader r = new BufferedReader(new FileReader("testFile.rwl"));
        String actual = r.readLine();
        r.close();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testSaveRowHeader_withYearWidthGreater() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedWriter w = new BufferedWriter(new FileWriter("testFile.rwl"));
        Sample sample = new Sample();
        sample.range = new Range(new Year(2000), new Year(2010));
        String code = "123456";
        int yearWidth = 4;
        Year y = new Year(2005);
        
        String expected = "12342005";
        tucsonSimple.saveRowHeader(w, sample, code, yearWidth, y);
        w.close();
        
        BufferedReader r = new BufferedReader(new FileReader("testFile.rwl"));
        String actual = r.readLine();
        r.close();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testSaveData() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedWriter w = new BufferedWriter(new FileWriter("testFile.rwl"));
        Sample sample = new Sample();
        sample.data.put(0, 10);
        sample.data.put(1, 20);
        sample.data.put(2, 30);
        sample.range = new Range(new Year(2000), new Year(2002));
        
        String expected = "      10      20      30";
        tucsonSimple.saveData(sample, w);
        w.close();
        
        BufferedReader r = new BufferedReader(new FileReader("testFile.rwl"));
        String actual = r.readLine();
        r.close();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testSave() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedWriter w = new BufferedWriter(new FileWriter("testFile.rwl"));
        Sample sample = new Sample();
        sample.range = new Range(new Year(2000), new Year(2002));
        sample.data.put(0, 10);
        sample.data.put(1, 20);
        sample.data.put(2, 30);
        
        tucsonSimple.save(sample, w);
        w.close();
        
        BufferedReader r = new BufferedReader(new FileReader("testFile.rwl"));
        assertNotNull(r.readLine());
        r.close();
    }

}