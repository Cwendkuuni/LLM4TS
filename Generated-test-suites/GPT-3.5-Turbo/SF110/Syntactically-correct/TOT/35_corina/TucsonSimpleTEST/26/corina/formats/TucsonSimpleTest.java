import org.junit.Test;
import org.junit.Assert;
import static org.mockito.Mockito.*;

import java.io.*;
import corina.*;

public class TucsonSimpleTest {

    @Test
    public void testToString() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        
        String expected = "Tucson Simple";
        String actual = tucsonSimple.toString();
        
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testGetDefaultExtension() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        
        String expected = ".RWL";
        String actual = tucsonSimple.getDefaultExtension();
        
        Assert.assertEquals(expected, actual);
    }
    
    @Test(expected = WrongFiletypeException.class)
    public void testLoad() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        
        BufferedReader mockReader = mock(BufferedReader.class);
        
        tucsonSimple.load(mockReader);
        
        verify(mockReader).close();
    }
    
    @Test
    public void testMake6digitCodeWithExistingId() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        
        Sample mockSample = mock(Sample.class);
        when(mockSample.meta.containsKey("id")).thenReturn(true);
        when(mockSample.meta.get("id")).thenReturn("1234567");
        
        String expected = "123456";
        String actual = tucsonSimple.make6digitCode(mockSample);
        
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testMake6digitCodeWithoutExistingId() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        
        Sample mockSample = mock(Sample.class);
        when(mockSample.meta.containsKey("id")).thenReturn(false);
        
        String expected = "000000";
        String actual = tucsonSimple.make6digitCode(mockSample);
        
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testMake6digitCodeWithLongId() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        
        Sample mockSample = mock(Sample.class);
        when(mockSample.meta.containsKey("id")).thenReturn(true);
        when(mockSample.meta.get("id")).thenReturn("1234567890");
        
        String expected = "123456";
        String actual = tucsonSimple.make6digitCode(mockSample);
        
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testSaveRowHeader() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        
        BufferedWriter mockWriter = mock(BufferedWriter.class);
        Sample mockSample = mock(Sample.class);
        when(mockSample.range.getStart()).thenReturn(new Year(1990));
        Year mockYear = mock(Year.class);
        when(mockYear.compareTo(mockSample.range.getStart())).thenReturn(-1);
        when(mockYear.toString()).thenReturn("1990");
        
        tucsonSimple.saveRowHeader(mockWriter, mockSample, "123456", 10, mockYear);
        
        verify(mockWriter).write("123456 1990");
    }
    
    @Test
    public void testSaveData() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        
        BufferedWriter mockWriter = mock(BufferedWriter.class);
        Sample mockSample = mock(Sample.class);
        Range mockRange = mock(Range.class);
        when(mockSample.computeRange(mockSample)).thenReturn(mockRange);
        Year mockStart = mock(Year.class);
        Year mockEnd = mock(Year.class);
        when(mockRange.getStart()).thenReturn(mockStart);
        when(mockRange.getEnd()).thenReturn(mockEnd);
        when(mockSample.make6digitCode(mockSample)).thenReturn("123456");
        when(mockSample.data.get(anyInt())).thenReturn(1);
        when(mockSample.count.get(anyInt())).thenReturn(2);
        
        tucsonSimple.saveData(mockSample, mockWriter);
        
        verify(mockWriter).write("123456      1   2");
    }
    
    @Test
    public void testComputeRangeWithStartGreaterThan1() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        
        Sample mockSample = mock(Sample.class);
        Range mockRange = mock(Range.class);
        Year mockStart = mock(Year.class);
        when(mockSample.range.getStart()).thenReturn(mockStart);
        when(mockStart.compareTo(new Year(1))).thenReturn(1);
        when(mockSample.range.getStartDate()).thenReturn(mockStart);
        when(mockRange.getStart()).thenReturn(mockStart);
        when(mockSample.range.redateStartTo(new Year(1001))).thenReturn(mockRange);
        
        Range expected = mockRange;
        Range actual = tucsonSimple.computeRange(mockSample);
        
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testComputeRangeWithStartPlus8000GreaterThan1() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        
        Sample mockSample = mock(Sample.class);
        Range mockRange = mock(Range.class);
        Year mockStart = mock(Year.class);
        when(mockSample.range.getStart()).thenReturn(mockStart);
        when(mockStart.compareTo(new Year(1))).thenReturn(-1);
        when(mockStart.add(8000).compareTo(new Year(1))).thenReturn(1);
        when(mockSample.range.redateBy(8000)).thenReturn(mockRange);
        
        Range expected = mockRange;
        Range actual = tucsonSimple.computeRange(mockSample);
        
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testComputeRangeWithStartLessThan1001() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        
        Sample mockSample = mock(Sample.class);
        Range mockRange = mock(Range.class);
        Year mockStart = mock(Year.class);
        when(mockSample.range.getStart()).thenReturn(mockStart);
        when(mockStart.compareTo(new Year(1))).thenReturn(-1);
        when(mockStart.add(8000).compareTo(new Year(1))).thenReturn(-1);
        when(mockSample.range.redateStartTo(new Year(1001))).thenReturn(mockRange);
        
        Range expected = mockRange;
        Range actual = tucsonSimple.computeRange(mockSample);
        
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testSave() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        
        BufferedWriter mockWriter = mock(BufferedWriter.class);
        Sample mockSample = mock(Sample.class);
        
        tucsonSimple.save(mockSample, mockWriter);
        
        verify(mockSample).saveData(mockSample, mockWriter);
    }
}