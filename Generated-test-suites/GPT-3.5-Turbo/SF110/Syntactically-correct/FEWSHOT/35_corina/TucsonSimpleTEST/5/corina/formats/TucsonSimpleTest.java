
package corina.formats;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;
import corina.*;
import corina.ui.*;
import corina.util.*;

public class TucsonSimpleTest {
    
    private TucsonSimple tucsonSimple;
    
    @Before
    public void setUp() {
        tucsonSimple = new TucsonSimple();
    }
    
    @Test
    public void testToString() {
        assertEquals("format.tucsonsimple", tucsonSimple.toString());
    }
    
    @Test
    public void testGetDefaultExtension() {
        assertEquals(".RWL", tucsonSimple.getDefaultExtension());
    }
    
    @Test(expected=WrongFiletypeException.class)
    public void testLoad() throws IOException {
        BufferedReader bufferedReader = Mockito.mock(BufferedReader.class);
        tucsonSimple.load(bufferedReader);
    }
    
    @Test
    public void testMake6digitCode() {
        Sample sample = Mockito.mock(Sample.class);
        HashMap<String, Object> meta = new HashMap<>();
        meta.put("id", 1234567);
        Mockito.when(sample.getMeta()).thenReturn(meta);
        
        String code = tucsonSimple.make6digitCode(sample);
        assertEquals("123456", code);
        
        meta.clear();
        code = tucsonSimple.make6digitCode(sample);
        assertEquals("000000", code);
    }
    
    @Test
    public void testMake6digitCodeWithCodeLengthGreaterThan6() {
        Sample sample = Mockito.mock(Sample.class);
        HashMap<String, Object> meta = new HashMap<>();
        meta.put("id", 123456789);
        Mockito.when(sample.getMeta()).thenReturn(meta);
        
        String code = tucsonSimple.make6digitCode(sample);
        assertEquals("123456", code);
    }
    
    @Test
    public void testMake6digitCodeWithCodeLengthLessThan6() {
        Sample sample = Mockito.mock(Sample.class);
        HashMap<String, Object> meta = new HashMap<>();
        meta.put("id", 123);
        Mockito.when(sample.getMeta()).thenReturn(meta);
        
        String code = tucsonSimple.make6digitCode(sample);
        assertEquals("123   ", code);
    }
    
    @Test
    public void testSaveRowHeaderWithYLowerThanSampleRangeStart() throws IOException {
        BufferedWriter bufferedWriter = Mockito.mock(BufferedWriter.class);
        Sample sample = Mockito.mock(Sample.class);
        Mockito.when(sample.getRange()).thenReturn(new Range(new Year(2000), new Year(2020)));
        Mockito.when(sample.getMeta()).thenReturn(new HashMap<>());
        Year y = new Year(1999);
        
        tucsonSimple.saveRowHeader(bufferedWriter, sample, "123456", 6, y);
        Mockito.verify(bufferedWriter).write(Mockito.eq("123456 1999"));
    }
    
    @Test
    public void testSaveRowHeaderWithYEqualToSampleRangeStart() throws IOException {
        BufferedWriter bufferedWriter = Mockito.mock(BufferedWriter.class);
        Sample sample = Mockito.mock(Sample.class);
        Mockito.when(sample.getRange()).thenReturn(new Range(new Year(2000), new Year(2020)));
        Mockito.when(sample.getMeta()).thenReturn(new HashMap<>());
        Year y = new Year(2000);
        
        tucsonSimple.saveRowHeader(bufferedWriter, sample, "123456", 6, y);
        Mockito.verify(bufferedWriter).write(Mockito.eq("123456 2000"));
    }
    
    @Test
    public void testSaveRowHeaderWithYPrefixLengthGreaterThanYearWidth() throws IOException {
        BufferedWriter bufferedWriter = Mockito.mock(BufferedWriter.class);
        Sample sample = Mockito.mock(Sample.class);
        Mockito.when(sample.getRange()).thenReturn(new Range(new Year(2000), new Year(2020)));
        Mockito.when(sample.getMeta()).thenReturn(new HashMap<>());
        Year y = new Year(2020);
        
        tucsonSimple.saveRowHeader(bufferedWriter, sample, "123456", 6, y);
        Mockito.verify(bufferedWriter).write(Mockito.eq("1234562020"));
    }
    
    @Test
    public void testSaveRowHeaderWithYPrefixLengthLessThanYearWidth() throws IOException {
        BufferedWriter bufferedWriter = Mockito.mock(BufferedWriter.class);
        Sample sample = Mockito.mock(Sample.class);
        Mockito.when(sample.getRange()).thenReturn(new Range(new Year(2000), new Year(2020)));
        Mockito.when(sample.getMeta()).thenReturn(new HashMap<>());
        Year y = new Year(2019);
        
        tucsonSimple.saveRowHeader(bufferedWriter, sample, "123456", 4, y);
        Mockito.verify(bufferedWriter).write(Mockito.eq("1234 2019"));
    }
    
    @Test
    public void testSaveDataWithoutSummedAndProcessed() throws IOException {
        Sample s = Mockito.mock(Sample.class);
        BufferedWriter bufferedWriter = Mockito.mock(BufferedWriter.class);
        Mockito.when(s.getRange()).thenReturn(new Range(new Year(2000), new Year(2020)));
        Mockito.when(s.getData()).thenReturn(new HashMap<>());
        Mockito.when(s.getCount()).thenReturn(new HashMap<>());
        
        tucsonSimple.saveData(s, bufferedWriter);
        
        Mockito.verify(bufferedWriter, Mockito.times(5)).write(Mockito.anyString());
        Mockito.verify(bufferedWriter, Mockito.times(15)).write(Mockito.anyInt());
        Mockito.verify(bufferedWriter, Mockito.times(2)).newLine();
    }
    
    @Test
    public void testSaveDataWithSummedAndProcessed() throws IOException {
        Sample s = Mockito.mock(Sample.class);
        BufferedWriter bufferedWriter = Mockito.mock(BufferedWriter.class);
        Mockito.when(s.getRange()).thenReturn(new Range(new Year(2000), new Year(2020)));
        Mockito.when(s.getData()).thenReturn(new HashMap<>());
        Mockito.when(s.getCount()).thenReturn(new HashMap<>());
        
        tucsonSimple.saveData(s, bufferedWriter);
        
        Mockito.verify(bufferedWriter, Mockito.times(5)).write(Mockito.anyString());
        Mockito.verify(bufferedWriter, Mockito.times(15)).write(Mockito.anyInt());
        Mockito.verify(bufferedWriter, Mockito.times(2)).newLine();
    }
    
    @Test
    public void testComputeRangeWithStartGreaterThanOrEqualToYear1() {
        Sample s = Mockito.mock(Sample.class);
        Range range = new Range(new Year(2000), new Year(2020));
        Mockito.when(s.getRange()).thenReturn(range);
        
        Range computedRange = tucsonSimple.computeRange(s);
        
        assertEquals(range, computedRange);
    }
    
    @Test
    public void testComputeRangeWithStartPlus8000GreaterThanOrEqualToYear1() {
        Sample s = Mockito.mock(Sample.class);
        Range range = new Range(new Year(8000), new Year(10000));
        Mockito.when(s.getRange()).thenReturn(range);
        
        Range computedRange = tucsonSimple.computeRange(s);
        
        assertEquals(new Range(new Year(8000), new Year(10000)), computedRange);
    }
    
    @Test
    public void testComputeRangeWithStartLessThanYear1() {
        Sample s = Mockito.mock(Sample.class);
        Range range = new Range(new Year(-1000), new Year(1000));
        Mockito.when(s.getRange()).thenReturn(range);
        
        Range computedRange = tucsonSimple.computeRange(s);
        
        assertEquals(new Range(new Year(1001), new Year(1000)), computedRange);
    }
    
    @Test
    public void testSave() throws IOException {
        Sample s = Mockito.mock(Sample.class);
        BufferedWriter bufferedWriter = Mockito.mock(BufferedWriter.class);
        Mockito.when(s.getRange()).thenReturn(new Range(new Year(2000), new Year(2020)));
        Mockito.when(s.getData()).thenReturn(new HashMap<>());
        Mockito.when(s.getCount()).thenReturn(new HashMap<>());
        
        tucsonSimple.save(s, bufferedWriter);
        
        Mockito.verify(tucsonSimple).saveData(Mockito.eq(s), Mockito.eq(bufferedWriter));
    }
}
