package corina.formats;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import corina.Sample;
import corina.Year;
import corina.util.Range;

public class TucsonSimpleTest {
    
    private TucsonSimple tucsonSimple;
    private Sample sample;
    private BufferedReader reader;
    private BufferedWriter writer;
    
    @Before
    public void setup() {
        tucsonSimple = new TucsonSimple();
        sample = new Sample();
        reader = null;
        writer = null;
    }
    
    @Test
    public void testToString() {
        assertEquals("format.tucsonsimple", tucsonSimple.toString());
    }
    
    @Test
    public void testGetDefaultExtension() {
        assertEquals(".RWL", tucsonSimple.getDefaultExtension());
    }
    
    @Test(expected = WrongFiletypeException.class)
    public void testLoad() throws IOException {
        tucsonSimple.load(reader);
    }
    
    @Test
    public void testMake6digitCodeWithId() {
        sample.meta = new HashMap<>();
        sample.meta.put("id", "123");
        
        String code = tucsonSimple.make6digitCode(sample);
        assertEquals("123   ", code);
    }
    
    @Test
    public void testMake6digitCodeWithoutId() {
        String code = tucsonSimple.make6digitCode(sample);
        assertEquals("000000", code);
    }
    
    @Test
    public void testMake6digitCodeLongId() {
        sample.meta = new HashMap<>();
        sample.meta.put("id", "1234567");
        
        String code = tucsonSimple.make6digitCode(sample);
        assertEquals("123456", code);
    }
    
    @Test
    public void testMake6digitCodeShortId() {
        sample.meta = new HashMap<>();
        sample.meta.put("id", "12");
        
        String code = tucsonSimple.make6digitCode(sample);
        assertEquals("12    ", code);
    }
    
    @Test
    public void testSaveRowHeaderStartBeforeRange() throws IOException {
        writer = new BufferedWriter(new FileWriter("testfile"));
        tucsonSimple.saveRowHeader(writer, sample, "123", 4, new Year(1990));
        writer.close();
        
        BufferedReader reader = new BufferedReader(new FileReader("testfile"));
        String line = reader.readLine();
        reader.close();
        
        assertEquals("123 1990", line);
    }
    
    @Test
    public void testSaveRowHeaderStartAfterRange() throws IOException {
        writer = new BufferedWriter(new FileWriter("testfile"));
        tucsonSimple.saveRowHeader(writer, sample, "123", 4, new Year(2000));
        writer.close();
        
        BufferedReader reader = new BufferedReader(new FileReader("testfile"));
        String line = reader.readLine();
        reader.close();
        
        assertEquals("123 2000", line);
    }
    
    @Test
    public void testSaveRowHeaderStartEqualToRangeNotProcessed() throws IOException {
        writer = new BufferedWriter(new FileWriter("testfile"));
        tucsonSimple.saveRowHeader(writer, sample, "123", 6, new Year(2000));
        writer.close();
        
        BufferedReader reader = new BufferedReader(new FileReader("testfile"));
        String line = reader.readLine();
        reader.close();
        
        assertEquals("123 2000", line);
    }
    
    @Test
    public void testSaveRowHeaderStartEqualToRangeProcessed() throws IOException {
        writer = new BufferedWriter(new FileWriter("testfile"));
        tucsonSimple.saveRowHeader(writer, sample, "123", 4, new Year(2000));
        writer.close();
        
        BufferedReader reader = new BufferedReader(new FileReader("testfile"));
        String line = reader.readLine();
        reader.close();
        
        assertEquals("123 2000", line);
    }
    
    @Test
    public void testSaveData() throws IOException {
        writer = new BufferedWriter(new FileWriter("testfile"));
        tucsonSimple.saveData(sample, writer);
        writer.close();
        
        BufferedReader reader = new BufferedReader(new FileReader("testfile"));
        String line = reader.readLine();
        reader.close();
        
        assertTrue(line.length() > 0);
    }
    
    @Test
    public void testComputeRangeStartGreaterThanOne() {
        sample.range = new Range(new Year(2000), new Year(2010));
        Range range = tucsonSimple.computeRange(sample);
        assertEquals(new Year(2000), range.getStart());
        assertEquals(new Year(2010), range.getEnd());
    }
    
    @Test
    public void testComputeRangeStartLessThanOneAndStartPlus8000GreaterThanOne() {
        sample.range = new Range(new Year(-100), new Year(2010));
        Range range = tucsonSimple.computeRange(sample);
        assertEquals(new Year(-100), range.getStart());
        assertEquals(new Year(2010), range.getEnd());
    }
    
    @Test
    public void testComputeRangeStartLessThanOneAndStartPlus8000LessThanOne() {
        sample.range = new Range(new Year(-10000), new Year(-90000));
        Range range = tucsonSimple.computeRange(sample);
        assertEquals(new Year(1001), range.getStart());
        assertEquals(new Year(-90000), range.getEnd());
    }
    
    @Test
    public void testSave() throws IOException {
        writer = new BufferedWriter(new FileWriter("testfile"));
        tucsonSimple.save(sample, writer);
        writer.close();
        
        BufferedReader reader = new BufferedReader(new FileReader("testfile"));
        String line = reader.readLine();
        reader.close();
        
        assertTrue(line.length() > 0);
    }
}