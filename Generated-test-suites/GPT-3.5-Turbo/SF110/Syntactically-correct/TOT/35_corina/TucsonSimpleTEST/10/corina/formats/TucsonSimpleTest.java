import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class TucsonSimpleTest {
    
    private TucsonSimple tucsonSimple;
    private BufferedReader mockBufferedReader;
    private BufferedWriter mockBufferedWriter;
    private Sample mockSample;
    private Range mockRange;
    private Year mockYear;
    
    @Before
    public void setUp() {
        tucsonSimple = new TucsonSimple();
        mockBufferedReader = mock(BufferedReader.class);
        mockBufferedWriter = mock(BufferedWriter.class);
        mockSample = mock(Sample.class);
        mockRange = mock(Range.class);
        mockYear = mock(Year.class);
    }
    
    @Test
    public void testToString() {
        String result = tucsonSimple.toString();
        assertEquals("format.tucsonsimple", result);
    }
    
    @Test
    public void testGetDefaultExtension() {
        String result = tucsonSimple.getDefaultExtension();
        assertEquals(".RWL", result);
    }
    
    @Test(expected = WrongFiletypeException.class)
    public void testLoad() throws IOException {
        tucsonSimple.load(mockBufferedReader);
    }
    
    @Test
    public void testMake6digitCodeWithIdPresent() {
        HashMap<String, Object> meta = new HashMap<>();
        meta.put("id", 123456);
        when(mockSample.getMeta()).thenReturn(meta);
        
        String result = tucsonSimple.make6digitCode(mockSample);
        assertEquals("123456", result);
    }
    
    @Test
    public void testMake6digitCodeWithIdNotPresent() {
        HashMap<String, Object> meta = new HashMap<>();
        when(mockSample.getMeta()).thenReturn(meta);
        
        String result = tucsonSimple.make6digitCode(mockSample);
        assertEquals("000000", result);
    }
    
    @Test
    public void testMake6digitCodeWithIdLengthGreaterThan6() {
        HashMap<String, Object> meta = new HashMap<>();
        meta.put("id", 1234567890);
        when(mockSample.getMeta()).thenReturn(meta);
        
        String result = tucsonSimple.make6digitCode(mockSample);
        assertEquals("123456", result);
    }
    
    @Test
    public void testMake6digitCodeWithIdLengthLessThan6() {
        HashMap<String, Object> meta = new HashMap<>();
        meta.put("id", 123);
        when(mockSample.getMeta()).thenReturn(meta);
        
        String result = tucsonSimple.make6digitCode(mockSample);
        assertEquals("123   ", result);
    }
    
    @Test
    public void testSaveRowHeaderWhenYearLessThanSampleRangeStart() throws IOException {
        when(mockYear.compareTo(any(Year.class))).thenReturn(-1);
        when(mockSample.getRange()).thenReturn(mockRange);
        when(mockRange.getStart()).thenReturn(mockYear);
        when(mockYear.toString()).thenReturn("2000");
        
        tucsonSimple.saveRowHeader(mockBufferedWriter, mockSample, "123456", 4, mockYear);
        
        verify(mockBufferedWriter).write("12342000");
    }
    
    @Test
    public void testSaveRowHeaderWhenYearGreaterThanOrEqualToSampleRangeStart() throws IOException {
        when(mockYear.compareTo(any(Year.class))).thenReturn(1);
        when(mockYear.toString()).thenReturn("2000");
        
        tucsonSimple.saveRowHeader(mockBufferedWriter, mockSample, "123456", 6, mockYear);
        
        verify(mockBufferedWriter).write("1234562000");
    }
    
    @Test
    public void testSaveData() throws IOException {
        when(mockSample.getRange()).thenReturn(mockRange);
        when(mockRange.getStart()).thenReturn(mockYear);
        when(mockRange.getEnd()).thenReturn(mockYear);
        when(mockYear.column()).thenReturn(0, 9);
        when(mockYear.add(anyInt())).thenReturn(mockYear);
        when(mockSample.getData()).thenReturn(new HashMap<>());
        when(mockSample.getCount()).thenReturn(new HashMap<>());
        
        tucsonSimple.saveData(mockSample, mockBufferedWriter);
        
        verify(mockBufferedWriter, times(11)).write(anyString());
        verify(mockBufferedWriter, times(1)).newLine();
    }
    
    @Test
    public void testComputeRangeWhenStartEqualToOrGreaterThanOne() {
        when(mockRange.getStart()).thenReturn(mockYear);
        when(mockYear.compareTo(any(Year.class))).thenReturn(0);
        
        Range result = tucsonSimple.computeRange(mockSample);
        assertEquals(mockRange, result);
    }
    
    @Test
    public void testComputeRangeWhenStartPlus8000EqualToOrGreaterThanOne() {
        when(mockRange.getStart()).thenReturn(mockYear);
        when(mockYear.add(anyInt())).thenReturn(mockYear);
        when(mockYear.compareTo(any(Year.class))).thenReturn(1);
        
        Range result = tucsonSimple.computeRange(mockSample);
        assertEquals(mockRange, result);
        verify(mockSample).redateBy(8000);
    }
    
    @Test
    public void testComputeRangeWhenStartLessThanOne() {
        when(mockRange.getStart()).thenReturn(mockYear);
        when(mockYear.add(anyInt())).thenReturn(mockYear);
        when(mockYear.compareTo(any(Year.class))).thenReturn(-1);
        
        Year mockNewStart = mock(Year.class);
        when(mockNewStart.compareTo(any(Year.class))).thenReturn(0);
        when(mockSample.redateStartTo(any(Year.class))).thenReturn(mockRange);
        when(mockNewStart.toString()).thenReturn("1001");
        
        Range result = tucsonSimple.computeRange(mockSample);
        assertEquals(mockRange, result);
        verify(mockSample).redateStartTo(mockNewStart);
    }
    
    @Test
    public void testSave() throws IOException {
        tucsonSimple.save(mockSample, mockBufferedWriter);
        verify(mockBufferedWriter, times(11)).write(anyString());
        verify(mockBufferedWriter, times(1)).newLine();
    }
}