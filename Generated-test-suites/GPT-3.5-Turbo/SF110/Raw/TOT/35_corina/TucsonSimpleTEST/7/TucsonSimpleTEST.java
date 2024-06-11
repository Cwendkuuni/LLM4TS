import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mockito;
import java.io.*;
import corina.formats.TucsonSimple;
import corina.util.Range;
import corina.ui.I18n;
import static org.mockito.Mockito.*;

public class TucsonSimpleTest {

    @Test
    public void testToString() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        String expected = I18n.getText("format.tucsonsimple");
        assertEquals(expected, tucsonSimple.toString());
    }
    
    @Test
    public void testGetDefaultExtension() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        String expected = ".RWL";
        assertEquals(expected, tucsonSimple.getDefaultExtension());
    }
    
    @Test(expected = WrongFiletypeException.class)
    public void testLoad() throws IOException{
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedReader mockReader = mock(BufferedReader.class);
        tucsonSimple.load(mockReader);
    }
    
    @Test
    public void testMake6digitCode() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = mock(Sample.class);
        when(sample.meta.containsKey("id")).thenReturn(true);
        when(sample.meta.get("id")).thenReturn("1234567");
        
        String expected = "123456";
        String result = tucsonSimple.make6digitCode(sample);
        assertEquals(expected, result);
        
        when(sample.meta.containsKey("id")).thenReturn(false);
        expected = "000000";
        result = tucsonSimple.make6digitCode(sample);
        assertEquals(expected, result);
    }
    
    @Test
    public void testSaveRowHeader() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedWriter mockWriter = mock(BufferedWriter.class);
        Sample sample = mock(Sample.class);
        String code = "123456";
        int yearWidth = 6;
        Year year = mock(Year.class);
        
        when(year.compareTo(sample.range.getStart())).thenReturn(-1);
        when(sample.range.getStart().toString()).thenReturn("2020");
        
        tucsonSimple.saveRowHeader(mockWriter, sample, code, yearWidth, year);
        verify(mockWriter).write(code + "2020");
        
        when(year.compareTo(sample.range.getStart())).thenReturn(1);
        when(year.toString()).thenReturn("2021");
        
        tucsonSimple.saveRowHeader(mockWriter, sample, code, yearWidth, year);
        verify(mockWriter).write(code + " 2021");
    }
    
    @Test
    public void testSaveData() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedWriter mockWriter = mock(BufferedWriter.class);
        Sample sample = mock(Sample.class);
        when(sample.range.getStart()).thenReturn(mock(Year.class));
        when(sample.range.getEnd()).thenReturn(mock(Year.class));
        when(sample.meta.containsKey("id")).thenReturn(true);
        when(sample.meta.get("id")).thenReturn("1234567");
        
        tucsonSimple.saveData(sample, mockWriter);
        
        verify(mockWriter).write(anyString());
        verify(mockWriter).newLine();
    }
    
    @Test
    public void testComputeRange() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = mock(Sample.class);
        when(sample.range.getStart()).thenReturn(mock(Year.class));
        when(sample.range.getEnd()).thenReturn(mock(Year.class));
        
        Range expected = mock(Range.class);
        when(sample.range.redateBy(anyInt())).thenReturn(expected);
        
        Range result = tucsonSimple.computeRange(sample);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testSave() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedWriter mockWriter = mock(BufferedWriter.class);
        Sample sample = mock(Sample.class);
        
        tucsonSimple.save(sample, mockWriter);
        
        verify(mockWriter).newLine();
    }
}