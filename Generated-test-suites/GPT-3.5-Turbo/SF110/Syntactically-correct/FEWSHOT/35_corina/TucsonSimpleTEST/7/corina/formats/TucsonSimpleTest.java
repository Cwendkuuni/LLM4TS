package corina.formats;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.*;
import java.util.*;

public class TucsonSimpleTest {
    
    private TucsonSimple tucsonSimple;
    private StringWriter stringWriter;
    private BufferedWriter bufferedWriter;
    
    @Before
    public void setUp() throws IOException {
        tucsonSimple = new TucsonSimple();
        stringWriter = new StringWriter();
        bufferedWriter = new BufferedWriter(stringWriter);
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
        BufferedReader reader = new BufferedReader(new StringReader(""));
        tucsonSimple.load(reader);
    }
    
    @Test
    public void testMake6digitCode() {
        Sample sample = new Sample("Sample");
        Map<String, Object> meta = new HashMap<>();
        meta.put("id", "123456");
        sample.setMeta(meta);
        
        String code = tucsonSimple.make6digitCode(sample);
        assertEquals("123456", code);
        
        meta.clear();
        code = tucsonSimple.make6digitCode(sample);
        assertEquals("000000", code);
    }
    
    @Test
    public void testMake6digitCodeWhenCodeLengthGreaterThan6() {
        Sample sample = new Sample("Sample");
        Map<String, Object> meta = new HashMap<>();
        meta.put("id", "1234567890");
        sample.setMeta(meta);
        
        String code = tucsonSimple.make6digitCode(sample);
        assertEquals("123456", code);
    }
    
    @Test
    public void testSaveRowHeaderWhenYLessThanStart() throws IOException {
        Sample sample = new Sample("Sample");
        sample.setRange(new Range(new Year(1980), new Year(2000)));
        Year year = new Year(1979);
        tucsonSimple.saveRowHeader(bufferedWriter, sample, "123456", 6, year);
        bufferedWriter.flush();
        String result = stringWriter.toString();
        assertEquals("123456 1979", result);
    }
    
    @Test
    public void testSaveRowHeaderWhenYEqualsStartAndNotProcessed() throws IOException {
        Sample sample = new Sample("Sample");
        sample.setRange(new Range(new Year(1980), new Year(2000)));
        Year year = new Year(1980);
        tucsonSimple.saveRowHeader(bufferedWriter, sample, "123456", 6, year);
        bufferedWriter.flush();
        String result = stringWriter.toString();
        assertEquals("123456  1980", result);
    }
    
    @Test
    public void testSaveRowHeaderWhenYEqualsStartAndProcessed() throws IOException {
        Sample sample = new Sample("Sample");
        sample.setRange(new Range(new Year(1980), new Year(2000)));
        Year year = new Year(1990);
        tucsonSimple.saveRowHeader(bufferedWriter, sample, "123456", 4, year);
        bufferedWriter.flush();
        String result = stringWriter.toString();
        assertEquals("1234 1990", result);
    }
    
    @Test
    public void testSaveRowHeaderWhenYGreaterThanStart() throws IOException {
        Sample sample = new Sample("Sample");
        sample.setRange(new Range(new Year(1980), new Year(2000)));
        Year year = new Year(1985);
        tucsonSimple.saveRowHeader(bufferedWriter, sample, "123456", 4, year);
        bufferedWriter.flush();
        String result = stringWriter.toString();
        assertEquals("1234    1985", result);
    }
    
    @Test
    public void testSaveData() throws IOException {
        Sample sample = new Sample("Sample");
        sample.setRange(new Range(new Year(1980), new Year(2000)));
        Map<Year, Double> data = new HashMap<>();
        data.put(new Year(1980), 1.0);
        data.put(new Year(1981), 2.0);
        data.put(new Year(1982), 3.0);
        data.put(new Year(1983), 4.0);
        sample.setData(data);
        
        tucsonSimple.saveData(sample, bufferedWriter);
        bufferedWriter.flush();
        String result = stringWriter.toString();
        
        StringBuilder expectedResult = new StringBuilder();
        expectedResult.append("      1");
        expectedResult.append("      2");
        expectedResult.append("      3");
        expectedResult.append("      4");
        
        assertEquals(expectedResult.toString(), result);
    }
    
    @Test
    public void testComputeRangeWhenStartGreaterThanEqualTo1() {
        Sample sample = new Sample("Sample");
        sample.setRange(new Range(new Year(1980), new Year(2000)));
        
        Range range = tucsonSimple.computeRange(sample);
        assertEquals(1980, range.getStart().getValue());
        assertEquals(2000, range.getEnd().getValue());
    }
    
    @Test
    public void testComputeRangeWhenStartPlus8000GreaterThanEqualTo1() {
        Sample sample = new Sample("Sample");
        sample.setRange(new Range(new Year(1), new Year(2000)));
        
        Range range = tucsonSimple.computeRange(sample);
        assertEquals(1, range.getStart().getValue());
        assertEquals(2000, range.getEnd().getValue());
    }
    
    @Test
    public void testComputeRangeWhenStartPlus8000LessThan1() {
        Sample sample = new Sample("Sample");
        sample.setRange(new Range(new Year(1), new Year(1000)));
        
        Range range = tucsonSimple.computeRange(sample);
        assertEquals(1001, range.getStart().getValue());
        assertEquals(1000, range.getEnd().getValue());
    }
    
    @Test
    public void testSave() throws IOException {
        Sample sample = new Sample("Sample");
        sample.setRange(new Range(new Year(1980), new Year(2000)));
        Map<Year, Double> data = new HashMap<>();
        data.put(new Year(1980), 1.0);
        data.put(new Year(1981), 2.0);
        data.put(new Year(1982), 3.0);
        data.put(new Year(1983), 4.0);
        sample.setData(data);
        
        tucsonSimple.save(sample, bufferedWriter);
        bufferedWriter.flush();
        String result = stringWriter.toString();
        
        StringBuilder expectedResult = new StringBuilder();
        expectedResult.append("      1");
        expectedResult.append("      2");
        expectedResult.append("      3");
        expectedResult.append("      4");
        expectedResult.append("\n");
        
        assertEquals(expectedResult.toString(), result);
    }

}