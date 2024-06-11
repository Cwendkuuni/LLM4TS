
package corina.formats;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import corina.Sample;
import corina.Year;
import corina.util.Range;
import corina.util.WrongFiletypeException;

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
        BufferedReader mockReader = new BufferedReader(new StringReader("test"));
        tucsonSimple.load(mockReader);
    }
    
    @Test
    public void testMake6digitCodeWithValueInMeta() {
        Sample sample = new Sample();
        Map<String, Object> meta = new HashMap<>();
        meta.put("id", "12345678");
        sample.meta = meta;
        
        String code = tucsonSimple.make6digitCode(sample);
        
        assertEquals("123456", code);
    }
    
    @Test
    public void testMake6digitCodeWithoutValueInMeta() {
        Sample sample = new Sample();
        
        String code = tucsonSimple.make6digitCode(sample);
        
        assertEquals("000000", code);
    }
    
    @Test
    public void testMake6digitCodeWithLongValueInMeta() {
        Sample sample = new Sample();
        Map<String, Object> meta = new HashMap<>();
        meta.put("id", "1234567890");
        sample.meta = meta;
        
        String code = tucsonSimple.make6digitCode(sample);
        
        assertEquals("123456", code);
    }
    
    @Test
    public void testSaveRowHeaderWithPrefixStart() throws IOException {
        StringWriter writer = new StringWriter();
        BufferedWriter bufferedWriter = new BufferedWriter(writer);
        Sample sample = new Sample();
        sample.range = new Range(new Year(1990), new Year(1999));
        Year year = new Year(1980);
        
        tucsonSimple.saveRowHeader(bufferedWriter, sample, "123456", 6, year);
        
        assertEquals("123456 1980", writer.toString());
    }
    
    @Test
    public void testSaveRowHeaderWithPrefixNotStart() throws IOException {
        StringWriter writer = new StringWriter();
        BufferedWriter bufferedWriter = new BufferedWriter(writer);
        Sample sample = new Sample();
        sample.range = new Range(new Year(1990), new Year(1999));
        Year year = new Year(1995);
        
        tucsonSimple.saveRowHeader(bufferedWriter, sample, "123456", 6, year);
        
        assertEquals("123456 1995", writer.toString());
    }
    
    @Test
    public void testSaveData() throws IOException {
        StringWriter writer = new StringWriter();
        BufferedWriter bufferedWriter = new BufferedWriter(writer);
        Sample sample = new Sample();
        sample.range = new Range(new Year(1990), new Year(1999));
        sample.data.put(0, 1);
        sample.data.put(1, 2);
        sample.data.put(2, 3);
        Year start = new Year(1990);
        Year end = new Year(1999);
        
        tucsonSimple.saveData(sample, bufferedWriter);
        
        String expectedOutput = "      1      2      3\n";
        assertEquals(expectedOutput, writer.toString());
    }
    
    @Test
    public void testComputeRangeWithStartGreaterThanEqualOne() {
        Sample sample = new Sample();
        sample.range = new Range(new Year(1990), new Year(1999));
        
        Range range = tucsonSimple.computeRange(sample);
        
        assertNotNull(range);
        assertEquals(sample.range, range);
    }
    
    @Test
    public void testComputeRangeWithStartAdd8000GreaterThanEqualOne() {
        Sample sample = new Sample();
        sample.range = new Range(new Year(-7999), new Year(1999));
        
        Range range = tucsonSimple.computeRange(sample);
        
        assertNotNull(range);
        assertEquals(new Range(new Year(0), new Year(9999)), range);
    }
    
    @Test
    public void testComputeRangeWithStartAdd8000LessThanOne() {
        Sample sample = new Sample();
        sample.range = new Range(new Year(-7999), new Year(-7000));
        
        Range range = tucsonSimple.computeRange(sample);
        
        assertNotNull(range);
        assertEquals(new Range(new Year(1001), new Year(1999)), range);
    }
    
    @Test
    public void testSave() throws IOException {
        StringWriter writer = new StringWriter();
        BufferedWriter bufferedWriter = new BufferedWriter(writer);
        Sample sample = new Sample();
        sample.range = new Range(new Year(1990), new Year(1999));
        sample.data.put(0, 1);
        sample.data.put(1, 2);
        sample.data.put(2, 3);
        
        tucsonSimple.save(sample, bufferedWriter);
        
        String expectedOutput = "      1      2      3\n";
        assertEquals(expectedOutput, writer.toString());
    }
    
}



