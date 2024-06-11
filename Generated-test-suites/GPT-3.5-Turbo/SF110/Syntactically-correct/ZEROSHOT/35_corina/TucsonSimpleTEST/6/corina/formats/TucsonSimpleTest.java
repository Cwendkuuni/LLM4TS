package corina.formats;

import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;
import corina.ui.*;
import corina.util.*;
import corina.*;

public class TucsonSimpleTest {
    
    private TucsonSimple tucsonSimple;
    private Sample sample;
    
    @Before
    public void setUp() {
        tucsonSimple = new TucsonSimple();
        sample = new Sample();
    }
    
    @Test
    public void testToString() {
        String expected = I18n.getText("format.tucsonsimple");
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
        BufferedReader reader = new BufferedReader(new FileReader("test.rwl"));
        tucsonSimple.load(reader);
    }
    
    @Test
    public void testMake6digitCodeWithId() {
        sample.meta.put("id", "123456");
        String expected = "123456";
        String actual = tucsonSimple.make6digitCode(sample);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testMake6digitCodeWithoutId() {
        sample.meta.remove("id");
        String expected = "000000";
        String actual = tucsonSimple.make6digitCode(sample);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testMake6digitCodeWithLongId() {
        sample.meta.put("id", "123456789");
        String expected = "123456";
        String actual = tucsonSimple.make6digitCode(sample);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testSaveRowHeader() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("test.rwl"));
        Year year = new Year(2000);
        tucsonSimple.saveRowHeader(writer, sample, "123456", 6, year);
        writer.close();
        BufferedReader reader = new BufferedReader(new FileReader("test.rwl"));
        String expected = "123456  2000";
        String actual = reader.readLine();
        assertEquals(expected, actual);
        reader.close();
    }
    
    @Test
    public void testSaveData() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("test.rwl"));
        tucsonSimple.saveData(sample, writer);
        writer.close();
        BufferedReader reader = new BufferedReader(new FileReader("test.rwl"));
        String expected = "   999";
        String actual = reader.readLine();
        assertEquals(expected, actual);
        reader.close();
    }
    
    @Test
    public void testComputeRangeWithStartGreaterThanYear1() {
        Year start = new Year(2000);
        sample.range = new Range(start, start);
        Range expected = sample.range;
        Range actual = tucsonSimple.computeRange(sample);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testComputeRangeWithStartMinus8000GreaterThanYear1() {
        Year start = new Year(2000);
        sample.range = new Range(start, start);
        Range expected = sample.range.redateBy(8000);
        Range actual = tucsonSimple.computeRange(sample);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testComputeRangeWithStartMinus8000LessThanYear1() {
        Year start = new Year(1000);
        sample.range = new Range(start, start);
        Range expected = sample.range.redateStartTo(new Year(1001));
        Range actual = tucsonSimple.computeRange(sample);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testSave() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("test.rwl"));
        tucsonSimple.save(sample, writer);
        writer.close();
        BufferedReader reader = new BufferedReader(new FileReader("test.rwl"));
        String expected = "   999";
        String actual = reader.readLine();
        assertEquals(expected, actual);
        reader.close();
    }
}