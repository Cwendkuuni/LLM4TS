package corina.formats;

import static org.junit.Assert.*;
import org.junit.Test;

public class TucsonSimpleTest {
    
    @Test
    public void testToString() {
        TucsonSimple tucson = new TucsonSimple();
        assertEquals("format.tucsonsimple", tucson.toString());
    }
    
    @Test
    public void testGetDefaultExtension() {
        TucsonSimple tucson = new TucsonSimple();
        assertEquals(".RWL", tucson.getDefaultExtension());
    }
    
    @Test(expected = WrongFiletypeException.class)
    public void testLoad() throws IOException {
        TucsonSimple tucson = new TucsonSimple();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        tucson.load(reader);
    }
    
    @Test
    public void testMake6digitCode() {
        TucsonSimple tucson = new TucsonSimple();
        Sample sample = new Sample();
        sample.meta.put("id", "1234567");
        assertEquals("123456", tucson.make6digitCode(sample));
        
        Sample sample2 = new Sample();
        assertEquals("000000", tucson.make6digitCode(sample2));
    }
    
    @Test
    public void testSaveRowHeader() throws IOException {
        TucsonSimple tucson = new TucsonSimple();
        Sample sample = new Sample();
        BufferedWriter writer = new BufferedWriter(new FileWriter("testFile.txt"));
        tucson.saveRowHeader(writer, sample, "000000", 6, new Year(2021));
        writer.close();
        BufferedReader reader = new BufferedReader(new FileReader("testFile.txt"));
        assertEquals("0000002021", reader.readLine());
        reader.close();
    }
    
    @Test
    public void testSaveData() throws IOException {
        TucsonSimple tucson = new TucsonSimple();
        Sample sample = new Sample();
        sample.range.setStart(new Year(2010));
        sample.range.setEnd(new Year(2015));
        sample.data.put(0, 1);
        sample.data.put(1, 2);
        sample.data.put(2, 3);
        sample.data.put(3, 4);
        sample.data.put(4, 5);
        sample.data.put(5, 6);
        sample.data.put(6, 7);
        sample.data.put(7, 8);
        sample.data.put(8, 9);
        sample.data.put(9, 10);
        
        BufferedWriter writer = new BufferedWriter(new FileWriter("testFile.txt"));
        tucson.saveData(sample, writer);
        writer.close();
        BufferedReader reader = new BufferedReader(new FileReader("testFile.txt"));
        assertEquals("       1       2       3       4       5       6       7       8       9      10", reader.readLine());
        reader.close();
    }
    
    @Test
    public void testComputeRange() {
        TucsonSimple tucson = new TucsonSimple();
        Sample sample = new Sample();
        sample.range.setStart(new Year(100));
        Range range = tucson.computeRange(sample);
        assertEquals(new Year(100), range.getStart());
        assertEquals(new Year(9999), range.getEnd());
        
        sample.range.setStart(new Year(1));
        Range range2 = tucson.computeRange(sample);
        assertEquals(new Year(1), range2.getStart());
        assertEquals(new Year(9999), range2.getEnd());
    }
    
    @Test
    public void testSave() throws IOException {
        TucsonSimple tucson = new TucsonSimple();
        Sample sample = new Sample();
        BufferedWriter writer = new BufferedWriter(new FileWriter("testFile.txt"));
        tucson.save(sample, writer);
        writer.close();
        BufferedReader reader = new BufferedReader(new FileReader("testFile.txt"));
        assertNull(reader.readLine());
        reader.close();
    }
}