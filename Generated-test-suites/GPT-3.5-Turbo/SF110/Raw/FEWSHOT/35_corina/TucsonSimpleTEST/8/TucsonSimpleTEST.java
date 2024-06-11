package corina.formats;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import corina.ui.*;
import java.io.*;
import corina.util.*;
import corina.*;

import org.junit.Before;
import org.junit.Test;

public class TucsonSimpleTest {
    
    private TucsonSimple tucsonSimple;
    private Sample sample;
    
    @Before
    public void setUp() {
        tucsonSimple = new TucsonSimple();
        sample = mock(Sample.class);
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
        BufferedReader reader = mock(BufferedReader.class);
        tucsonSimple.load(reader);
        verify(reader).close();
    }
    
    @Test
    public void testMake6digitCode() {
        when(sample.getMeta()).thenReturn(new HashMap<String, Object>());
        when(sample.getRange()).thenReturn(new Range(new Year(2000), new Year(2010)));
        when(sample.getRange().getStart().toString()).thenReturn("2000");
        when(sample.getRange().getEnd()).thenReturn(new Year(2010));
        
        assertEquals("000000", tucsonSimple.make6digitCode(sample));
        
        HashMap<String, Object> meta = new HashMap<String, Object>();
        meta.put("id", "1234567890");
        when(sample.getMeta()).thenReturn(meta);
        assertEquals("123456", tucsonSimple.make6digitCode(sample));
        
        meta.put("id", "123");
        assertEquals("123   ", tucsonSimple.make6digitCode(sample));
    }
    
    @Test
    public void testSaveRowHeader() throws IOException {
        BufferedWriter writer = mock(BufferedWriter.class);
        Sample sample = mock(Sample.class);
        String code = "123456";
        int yearWidth = 6;
        Year year = new Year(2000);
        
        tucsonSimple.saveRowHeader(writer, sample, code, yearWidth, year);
        
        verify(writer).write(code + "  2000");
    }
    
    @Test
    public void testSaveData() throws IOException {
        BufferedWriter writer = mock(BufferedWriter.class);
        Sample sample = mock(Sample.class);
        Range range = mock(Range.class);
        Year startYear = new Year(2000);
        Year endYear = new Year(2010);
        List<Integer> data = new ArrayList<>();
        List<Integer> count = new ArrayList<>();
        
        when(sample.getRange()).thenReturn(range);
        when(range.getStart()).thenReturn(startYear);
        when(range.getEnd()).thenReturn(endYear);
        when(sample.getData()).thenReturn(data);
        when(sample.getCount()).thenReturn(count);
        
        tucsonSimple.saveData(sample, writer);
        
        verify(writer, times(endYear.diff(startYear) + 1)).write(anyString());
        verify(writer, times(endYear.diff(startYear) + 1)).newLine();
    }
    
    @Test
    public void testComputeRange() {
        Sample sample = mock(Sample.class);
        Range range = new Range(new Year(1), new Year(2010));
        when(sample.getRange()).thenReturn(range);
        
        assertEquals(range, tucsonSimple.computeRange(sample));
        
        range = new Range(new Year(-5000), new Year(2010));
        when(sample.getRange()).thenReturn(range);
        
        assertEquals(range.redateBy(8000), tucsonSimple.computeRange(sample));
        
        range = new Range(new Year(-5000), new Year(1));
        when(sample.getRange()).thenReturn(range);
        
        assertEquals(range.redateStartTo(new Year(1001)), tucsonSimple.computeRange(sample));
    }
    
    @Test
    public void testSave() throws IOException {
        Sample sample = mock(Sample.class);
        BufferedWriter writer = mock(BufferedWriter.class);
        
        tucsonSimple.save(sample, writer);
        
        verify(writer).newLine();
    }
}