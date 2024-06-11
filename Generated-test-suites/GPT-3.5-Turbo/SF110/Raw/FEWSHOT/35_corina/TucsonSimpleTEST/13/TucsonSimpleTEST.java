package corina.formats;

import org.junit.Before;
import org.junit.Test;
import java.io.*;
import org.mockito.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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
    
    @Test(expected = WrongFiletypeException.class)
    public void testLoad() throws IOException {
        BufferedReader reader = mock(BufferedReader.class);
        tucsonSimple.load(reader);
    }
    
    @Test
    public void testMake6digitCode() {
        Sample sample = mock(Sample.class);
        when(sample.meta.containsKey("id")).thenReturn(true);
        when(sample.meta.get("id")).thenReturn("1234567");
        
        String code = tucsonSimple.make6digitCode(sample);
        assertEquals("123456", code);
        
        when(sample.meta.containsKey("id")).thenReturn(false);
        
        code = tucsonSimple.make6digitCode(sample);
        assertEquals("000000", code);
    }
    
    @Test
    public void testSaveRowHeader() throws IOException {
        Sample sample = mock(Sample.class);
        BufferedWriter writer = mock(BufferedWriter.class);
        String code = "123456";
        int yearWidth = 6;
        Year year = mock(Year.class);
        Year startYear = mock(Year.class);
        
        when(year.compareTo(startYear)).thenReturn(1);
        when(year.toString()).thenReturn("2022");
        when(startYear.toString()).thenReturn("2021");
        
        tucsonSimple.saveRowHeader(writer, sample, code, yearWidth, year);
        
        verify(writer).write("123456 2022");
        
        when(year.compareTo(startYear)).thenReturn(-1);
        when(startYear.toString()).thenReturn("2023");
        
        tucsonSimple.saveRowHeader(writer, sample, code, yearWidth, year);
        
        verify(writer).write("123456 2023");
    }
    
    @Test
    public void testSaveData() throws IOException {
        Sample sample = mock(Sample.class);
        BufferedWriter writer = mock(BufferedWriter.class);
        Range range = mock(Range.class);
        Year startYear = mock(Year.class);
        Year endYear = mock(Year.class);
        Year currentYear = mock(Year.class);
        when(sample.range).thenReturn(range);
        when(range.getStart()).thenReturn(startYear);
        when(range.getEnd()).thenReturn(endYear);
        when(startYear.column()).thenReturn(4);
        when(endYear.column()).thenReturn(8);
        when(currentYear.diff(startYear)).thenReturn(0);
        when(currentYear.add(-startYear.column())).thenReturn(currentYear);
        when(startYear.equals(currentYear)).thenReturn(true);
        when(sample.data.get(0)).thenReturn("1");
        when(sample.count.get(0)).thenReturn("2");
        
        tucsonSimple.saveData(sample, writer);
        
        verify(writer).write("123456    1   2");
    }
    
    @Test
    public void testComputeRange() {
        Sample sample = mock(Sample.class);
        Year startYear = mock(Year.class);
        Year year1 = mock(Year.class);
        Year year2 = mock(Year.class);
        
        when(sample.range).thenReturn(range);
        when(range.getStart()).thenReturn(startYear);
        
        when(startYear.compareTo(new Year(1))).thenReturn(1);
        when(range.redateBy(8000)).thenReturn(range);
        
        assertEquals(range, tucsonSimple.computeRange(sample));
        
        when(startYear.add(8000).compareTo(new Year(1))).thenReturn(-1);
        when(range.redateStartTo(new Year(1001))).thenReturn(range);
        
        assertEquals(range, tucsonSimple.computeRange(sample));
    }
    
    @Test
    public void testSave() throws IOException {
        Sample sample = mock(Sample.class);
        BufferedWriter writer = mock(BufferedWriter.class);
        
        tucsonSimple.save(sample, writer);
        
        verify(tucsonSimple).saveData(sample, writer);
    }
}