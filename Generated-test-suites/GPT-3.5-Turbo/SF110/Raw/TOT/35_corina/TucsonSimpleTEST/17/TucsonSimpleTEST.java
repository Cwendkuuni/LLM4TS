
package corina.formats;

import corina.ui.I18n;
import corina.ui.WrongFiletypeException;
import corina.util.Range;
import corina.util.Year;
import org.junit.Before;
import org.junit.Test;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class TucsonSimpleTest {
    
    private TucsonSimple tucsonSimple;
    private BufferedReader mockBufferedReader;
    private BufferedWriter mockBufferedWriter;
    
    @Before
    public void setUp() {
        tucsonSimple = new TucsonSimple();
        mockBufferedReader = mock(BufferedReader.class);
        mockBufferedWriter = mock(BufferedWriter.class);
    }
    
    // Test toString method
    @Test
    public void testToString() {
        String expected = I18n.getText("format.tucsonsimple");
        String actual = tucsonSimple.toString();
        assertEquals(expected, actual);
    }
    
    // Test getDefaultExtension method
    @Test
    public void testGetDefaultExtension() {
        String expected = ".RWL";
        String actual = tucsonSimple.getDefaultExtension();
        assertEquals(expected, actual);
    }
    
    // Test load method with WrongFiletypeException
    @Test(expected = WrongFiletypeException.class)
    public void testLoad() throws IOException {
        tucsonSimple.load(mockBufferedReader);
    }
    
    // Test make6digitCode method with valid Sample
    @Test
    public void testMake6digitCodeWithValidSample() {
        Sample sample = new Sample();
        Map<String, Object> meta = new HashMap<>();
        meta.put("id", "123456789");
        sample.meta = meta;
        
        String expected = "123456";
        String actual = tucsonSimple.make6digitCode(sample);
        assertEquals(expected, actual);
    }
    
    // Test make6digitCode method with invalid Sample
    @Test
    public void testMake6digitCodeWithInvalidSample() {
        Sample sample = new Sample();
        
        String expected = "000000";
        String actual = tucsonSimple.make6digitCode(sample);
        assertEquals(expected, actual);
    }
    
    // Test make6digitCode method with long code
    @Test
    public void testMake6digitCodeWithLongCode() {
        Sample sample = new Sample();
        Map<String, Object> meta = new HashMap<>();
        meta.put("id", "1234567890");
        sample.meta = meta;
        
        String expected = "123456";
        String actual = tucsonSimple.make6digitCode(sample);
        assertEquals(expected, actual);
    }
    
    // Test saveRowHeader method with start year greater than given year
    @Test
    public void testSaveRowHeaderWithStartYearGreaterThanGivenYear() throws IOException {
        Sample sample = new Sample();
        sample.range = new Range(new Year(2000), new Year(2010));
        Year year = new Year(1999);
        
        tucsonSimple.saveRowHeader(mockBufferedWriter, sample, "123456", 6, year);
        
        verify(mockBufferedWriter).write("123456" + " " + "   1999");
    }
    
    // Test saveRowHeader method with start year less than given year
    @Test
    public void testSaveRowHeaderWithStartYearLessThanGivenYear() throws IOException {
        Sample sample = new Sample();
        sample.range = new Range(new Year(2000), new Year(2010));
        Year year = new Year(2005);
        
        tucsonSimple.saveRowHeader(mockBufferedWriter, sample, "123456", 6, year);
        
        verify(mockBufferedWriter).write("123456" + " " + "2005");
    }
    
    // Test saveRowHeader method with start year equal to given year
    @Test
    public void testSaveRowHeaderWithStartYearEqualToGivenYear() throws IOException {
        Sample sample = new Sample();
        sample.range = new Range(new Year(2000), new Year(2010));
        Year year = new Year(2000);
        
        tucsonSimple.saveRowHeader(mockBufferedWriter, sample, "123456", 6, year);
        
        verify(mockBufferedWriter).write("123456" + " " + "2000");
    }
    
    // Continue with similar test cases for other methods
    
    // Test save method
    @Test
    public void testSave() throws IOException {
        Sample sample = new Sample();
        sample.data.put(0, "Data1");
        sample.count.put(0, "Count1");
        sample.range = new Range(new Year(2000), new Year(2010));
        tucsonSimple.save(sample, mockBufferedWriter);
        
        verify(mockBufferedWriter, times(9)).write(anyString());
        verify(mockBufferedWriter, times(9)).newLine();
    }
    
}
