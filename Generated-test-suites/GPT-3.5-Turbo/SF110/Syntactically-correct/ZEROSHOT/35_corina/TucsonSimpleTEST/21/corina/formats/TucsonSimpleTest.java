package corina.formats;

import org.junit.*;

public class TucsonSimpleTest {
    
    @Test
    public void testToString() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        String result = tucsonSimple.toString();
        Assert.assertEquals("format.tucsonsimple", result);
    }
    
    @Test
    public void testGetDefaultExtension() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        String result = tucsonSimple.getDefaultExtension();
        Assert.assertEquals(".RWL", result);
    }
    
    @Test(expected = WrongFiletypeException.class)
    public void testLoad() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedReader r = new BufferedReader(new FileReader("test.rwl"));
        tucsonSimple.load(r);
    }
    
    @Test
    public void testMake6digitCode() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample s = new Sample();
        s.meta.put("id", "1234567");
        String result = tucsonSimple.make6digitCode(s);
        Assert.assertEquals("123456", result);
        
        s.meta.remove("id");
        result = tucsonSimple.make6digitCode(s);
        Assert.assertEquals("000000", result);
    }
    
    @Test
    public void testSaveRowHeader() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample s = new Sample();
        BufferedWriter w = new BufferedWriter(new FileWriter("test.rwl"));
        String code = "123456";
        int yearWidth = 6;
        Year y = new Year(2020);
        tucsonSimple.saveRowHeader(w, s, code, yearWidth, y);
        w.close(); // close the writer
        
        // read the file and check content
        BufferedReader reader = new BufferedReader(new FileReader("test.rwl"));
        String line = reader.readLine();
        reader.close(); // close the reader
        
        Assert.assertEquals("1234562020", line);
    }
    
    @Test
    public void testSaveData() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample s = new Sample();
        BufferedWriter w = new BufferedWriter(new FileWriter("test.rwl"));
        tucsonSimple.saveData(s, w);
        w.close(); // close the writer
        
        // read the file and check content
        BufferedReader reader = new BufferedReader(new FileReader("test.rwl"));
        String line = reader.readLine();
        reader.close(); // close the reader
        
        Assert.assertEquals("999", line);
    }
    
    @Test
    public void testComputeRange() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample s = new Sample();
        Range result = tucsonSimple.computeRange(s);
        Assert.assertEquals(s.range, result);
        
        s.range = new Range(new Year(1000), new Year(2000));
        result = tucsonSimple.computeRange(s);
        Assert.assertEquals(new Year(1001), result.getStart());
        Assert.assertEquals(new Year(2000), result.getEnd());
        
        s.range = new Range(new Year(-8000), new Year(1));
        result = tucsonSimple.computeRange(s);
        Assert.assertEquals(new Year(1), result.getStart());
        Assert.assertEquals(new Year(8001), result.getEnd());
    }
    
    @Test
    public void testSave() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample s = new Sample();
        BufferedWriter w = new BufferedWriter(new FileWriter("test.rwl"));
        tucsonSimple.save(s, w);
        w.close(); // close the writer
        
        // read the file and check content
        BufferedReader reader = new BufferedReader(new FileReader("test.rwl"));
        String line = reader.readLine();
        reader.close(); // close the reader
        
        Assert.assertEquals("999", line);
    }
}