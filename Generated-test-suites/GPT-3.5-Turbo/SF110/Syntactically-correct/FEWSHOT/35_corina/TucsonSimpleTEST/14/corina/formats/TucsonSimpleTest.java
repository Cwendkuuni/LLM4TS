package corina.formats;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import corina.Sample;
import corina.Year;
import corina.util.Range;
import corina.WrongFiletypeException;

public class TucsonSimpleTest {
    
    private TucsonSimple tucsonSimple;
    
    @Before
    public void setup() {
        tucsonSimple = new TucsonSimple();
    }
    
    @Test
    public void testToString() {
        assertEquals("Tucson Simple", tucsonSimple.toString());
    }
    
    @Test
    public void testGetDefaultExtension() {
        assertEquals(".RWL", tucsonSimple.getDefaultExtension());
    }
    
    @Test(expected = WrongFiletypeException.class)
    public void testLoad() throws IOException {
        BufferedReader reader = mock(BufferedReader.class);
        tucsonSimple.load(reader);
    }
    
    @Test
    public void testMake6digitCodeWithId() {
        Sample sample = new Sample();
        Map<String, Object> meta = new HashMap<>();
        meta.put("id", "1234567");
        sample.setMeta(meta);
        
        String code = tucsonSimple.make6digitCode(sample);
        
        assertEquals("123456", code);
    }
    
    @Test
    public void testMake6digitCodeWithoutId() {
        Sample sample = new Sample();
        
        String code = tucsonSimple.make6digitCode(sample);
        
        assertEquals("000000", code);
    }
    
    @Test
    public void testMake6digitCodeWithLongId() {
        Sample sample = new Sample();
        Map<String, Object> meta = new HashMap<>();
        meta.put("id", "123456789");
        sample.setMeta(meta);
        
        String code = tucsonSimple.make6digitCode(sample);
        
        assertEquals("123456", code);
    }
    
    @Test
    public void testSaveRowHeaderWithLessThanYearWidth() throws IOException {
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        Sample sample = new Sample();
        String code = "123456";
        int yearWidth = 6;
        Year year = new Year(1990);
        
        tucsonSimple.saveRowHeader(writer, sample, code, yearWidth, year);
        
        assertEquals(code + "  1990", writer.toString());
    }
    
    @Test
    public void testSaveRowHeaderWithMoreThanYearWidth() throws IOException {
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        Sample sample = new Sample();
        String code = "123456";
        int yearWidth = 6;
        Year year = new Year(1989);
        
        tucsonSimple.saveRowHeader(writer, sample, code, yearWidth, year);
        
        assertEquals(code + "1989", writer.toString());
    }
    
    @Test
    public void testSaveData() throws IOException {
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        Sample sample = new Sample();
        sample.setData(new HashMap<>());
        sample.getCount().put(new Year(1990).diff(new Year(1990)), 100);
        sample.getCount().put(new Year(1991).diff(new Year(1990)), 200);
        sample.getData().put(new Year(1990).diff(new Year(1990)), 1);
        sample.getData().put(new Year(1991).diff(new Year(1990)), 2);
        sample.setRange(new Range(new Year(1990), new Year(1991)));
        
        tucsonSimple.saveData(sample, writer);
        
        assertEquals("      1    100  1991    200", writer.toString());
    }
    
    @Test
    public void testComputeRangeWithStartGreaterThanYear1() {
        Sample sample = new Sample();
        sample.setRange(new Range(new Year(1990), new Year(1991)));
        
        Range range = tucsonSimple.computeRange(sample);
        
        assertEquals(new Range(new Year(1990), new Year(1991)), range);
    }
    
    @Test
    public void testComputeRangeWithStartPlus8000GreaterThanYear1() {
        Sample sample = new Sample();
        sample.setRange(new Range(new Year(8000), new Year(8001)));
        
        Range range = tucsonSimple.computeRange(sample);
        
        assertEquals(new Range(new Year(8000), new Year(8001)), range);
    }
    
    @Test
    public void testComputeRangeWithStartPlus8000LessThanYear1() {
        Sample sample = new Sample();
        sample.setRange(new Range(new Year(-7000), new Year(-6999)));
        
        Range range = tucsonSimple.computeRange(sample);
        
        assertEquals(new Range(new Year(1001), new Year(1002)), range);
    }
    
    @Test
    public void testSave() throws IOException {
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        Sample sample = new Sample();
        
        tucsonSimple.save(sample, writer);
        
        assertEquals("      0", writer.toString());
    }
}