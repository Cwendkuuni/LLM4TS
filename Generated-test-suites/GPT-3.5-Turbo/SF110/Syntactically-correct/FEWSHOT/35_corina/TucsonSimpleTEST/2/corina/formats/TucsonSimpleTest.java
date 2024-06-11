import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

import corina.*;
import corina.ui.*;
import corina.util.*;
import java.io.*;
import org.junit.Before;
import org.junit.Test;

public class TucsonSimpleTest {
    private TucsonSimple tucsonSimple;
    private Sample sample;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private Range range;
    private Year year;
    
    @Before
    public void setUp() {
        tucsonSimple = new TucsonSimple();
        bufferedReader = mock(BufferedReader.class);
        bufferedWriter = mock(BufferedWriter.class);
        range = mock(Range.class);
        year = mock(Year.class);
        sample = new Sample();
        sample.range = range;
    }

    @Test
    public void testToString() {
        String expected = "Tucson Simple";
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
        tucsonSimple.load(bufferedReader);
    }
    
    @Test
    public void testMake6digitCodeWithExistingId() {
        String expected = "123456";
        sample.meta.put("id", expected);
        String actual = tucsonSimple.make6digitCode(sample);
        assertEquals(expected, actual);        
    }
    
    @Test
    public void testMake6digitCodeWithoutId() {
        String expected = "000000";
        String actual = tucsonSimple.make6digitCode(sample);
        assertEquals(expected, actual);        
    }
    
    @Test
    public void testMake6digitCodeWithLongId() {
        String expected = "123456";
        sample.meta.put("id", "1234567890");
        String actual = tucsonSimple.make6digitCode(sample);
        assertEquals(expected, actual);        
    }
    
    @Test
    public void testSaveRowHeader() throws IOException {
        final String code = "123456";
        final int yearWidth = 4;
        tucsonSimple.saveRowHeader(bufferedWriter, sample, code, yearWidth, year);
        String expected = "1234";
        verify(bufferedWriter, times(1)).write(eq(code + expected));
    }
    
    @Test
    public void testSaveData() throws IOException {
        tucsonSimple.saveData(sample, bufferedWriter);
        verify(bufferedWriter, times(1)).write(any(String.class));
        verify(bufferedWriter, times(1)).newLine();
    }
    
    @Test
    public void testComputeRange() {
        final Year start = new Year(1);
        sample.range = new Range(start, year);
        Range expected = sample.range;
        Range actual = tucsonSimple.computeRange(sample);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testComputeRangeWithYearStartOver8000() {
        final Year start = new Year(8001);
        sample.range = new Range(start, year);
        Range expected = sample.range.redateBy(8000);
        Range actual = tucsonSimple.computeRange(sample);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testComputeRangeWithYearStartBefore1000() {
        final Year start = new Year(999);
        sample.range = new Range(start, year);
        Range expected = sample.range.redateStartTo(new Year(1001));
        Range actual = tucsonSimple.computeRange(sample);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testSave() throws IOException {
        tucsonSimple.save(sample, bufferedWriter);
        verify(bufferedWriter, times(1)).write(any(String.class));
        verify(bufferedWriter, times(1)).newLine();
    }
}