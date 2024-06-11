import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import java.io.*;
import corina.util.*;
import corina.*;
import corina.ui.*;
import corina.formats.*;

public class TucsonSimpleTest {

    private TucsonSimple tucsonSimple;
    private Sample sample;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    @Before
    public void setup() {
        tucsonSimple = new TucsonSimple();
        sample = Mockito.mock(Sample.class);
        bufferedReader = Mockito.mock(BufferedReader.class);
        bufferedWriter = Mockito.mock(BufferedWriter.class);
    }

    // Testing toString method

    @Test
    public void testToString() {
        assertEquals("format.tucsonsimple", tucsonSimple.toString());
    }
    
    // Testing getDefaultExtension method
    
    @Test
    public void testGetDefaultExtension() {
        assertEquals(".RWL", tucsonSimple.getDefaultExtension());
    }
    
    // Testing load method with WrongFiletypeException
    
    @Test(expected = WrongFiletypeException.class)
    public void testLoadWithWrongFiletypeException() throws IOException {
        tucsonSimple.load(bufferedReader);
    }
    
    // Testing make6digitCode method with id in meta
    
    @Test
    public void testMake6digitCodeWithIdInMeta() {
        Sample sample = new Sample();
        sample.meta.put("id", "123456789");
        String code = tucsonSimple.make6digitCode(sample);
        
        assertEquals("123456", code);
    }
    
    // Testing make6digitCode method without id in meta
    
    @Test
    public void testMake6digitCodeWithoutIdInMeta() {
        Sample sample = new Sample();
        String code = tucsonSimple.make6digitCode(sample);
        
        assertEquals("000000", code);
    }

    // Testing saveRowHeader method with y less than s.range.getStart()
    
    @Test
    public void testSaveRowHeaderWithYLessThanStart() throws IOException {
        Sample sample = new Sample();
        sample.range = new Range(new Year(10), new Year(20));
        Mockito.when(sample.range.getStart()).thenReturn(new Year(5));
        
        Mockito.when(bufferedWriter.toString()).thenReturn("");
        
        tucsonSimple.saveRowHeader(bufferedWriter, sample, "", 6, new Year(3));
        String result = bufferedWriter.toString();
        
        assertEquals(" " + " 3", result);
    }
    
    // Testing saveRowHeader method with y greater than or equal to s.range.getStart()
    
    @Test
    public void testSaveRowHeaderWithYGreaterThanOrEqualToStart() throws IOException {
        Sample sample = new Sample();
        sample.range = new Range(new Year(10), new Year(20));
        Mockito.when(sample.range.getStart()).thenReturn(new Year(5));
        
        Mockito.when(bufferedWriter.toString()).thenReturn("");
        
        tucsonSimple.saveRowHeader(bufferedWriter, sample, "", 6, new Year(15));
        String result = bufferedWriter.toString();
        
        assertEquals(" " + "15", result);
    }
    
    // Testing saveData method
    
    @Test
    public void testSaveData() throws IOException {
        Sample sample = new Sample();
        sample.range = new Range(new Year(10), new Year(20));
        sample.range.setStart(new Year(15));
        sample.range.setEnd(new Year(17));
        
        sample.data.put(0, 1);
        sample.data.put(1, 2);
        sample.data.put(2, 3);
        
        tucsonSimple.saveData(sample, bufferedWriter);
        Mockito.verify(bufferedWriter, Mockito.times(3)).write(Mockito.anyString());
        Mockito.verify(bufferedWriter, Mockito.times(1)).newLine();
    }
    
    // Testing computeRange method with start greater than or equal to Year(1)
    
    @Test
    public void testComputeRangeWithStartGreaterThanOrEqualToYearOne() {
        Sample sample = new Sample();
        sample.range = new Range(new Year(10), new Year(20));
        
        Range result = tucsonSimple.computeRange(sample);
        
        assertEquals(sample.range, result);
    }
    
    // Testing computeRange method with start.add(8000) greater than or equal to Year(1)
    
    @Test
    public void testComputeRangeWithStartAdd8000GreaterThanOrEqualToYearOne() {
        Sample sample = new Sample();
        sample.range = new Range(new Year(1), new Year(2));
        
        Range result = tucsonSimple.computeRange(sample);
        
        assertEquals(sample.range.redateBy(8000), result);
    }
    
    // Testing computeRange method with start and start.add(8000) both less than Year(1)
    
    @Test
    public void testComputeRangeWithStartAndStartAdd8000BothLessThanYearOne() {
        Sample sample = new Sample();
        sample.range = new Range(new Year(1), new Year(2));
        
        Range result = tucsonSimple.computeRange(sample);
        
        assertEquals(sample.range.redateStartTo(new Year(1001)), result);
    }
    
    // Testing save method
    
    @Test
    public void testSave() throws IOException {
        Sample sample = new Sample();
        
        tucsonSimple.save(sample, bufferedWriter);
        Mockito.verify(bufferedWriter, Mockito.times(1)).newLine();
    }
}