import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TucsonSimpleTest {
    
    @Test
    public void testToString() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        assertEquals("format.tucsonsimple", tucsonSimple.toString());
    }
    
    @Test
    public void testGetDefaultExtension() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        assertEquals(".RWL", tucsonSimple.getDefaultExtension());
    }
    
    @Test(expected = WrongFiletypeException.class)
    public void testLoad() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedReader reader = mock(BufferedReader.class);
        tucsonSimple.load(reader);
    }
    
    @Test
    public void testMake6digitCodeWithValidId() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        Map<String, Object> meta = new HashMap<>();
        meta.put("id", "123456");
        sample.setMeta(meta);
        
        String code = tucsonSimple.make6digitCode(sample);
        
        assertEquals("123456", code);
    }
    
    @Test
    public void testMake6digitCodeWithMissingId() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        
        String code = tucsonSimple.make6digitCode(sample);
        
        assertEquals("000000", code);
    }
    
    @Test
    public void testMake6digitCodeWithLongId() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        Map<String, Object> meta = new HashMap<>();
        meta.put("id", "123456789");
        sample.setMeta(meta);
        
        String code = tucsonSimple.make6digitCode(sample);
        
        assertEquals("123456", code);
    }
    
    @Test
    public void testSaveRowHeaderWithPrefixGreaterThanYearWidth() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        BufferedWriter writer = mock(BufferedWriter.class);
        String code = "123456";
        int yearWidth = 6;
        Year year = new Year(2000);
        
        tucsonSimple.saveRowHeader(writer, sample, code, yearWidth, year);
        
        verify(writer).write(code + "2000  ");
    }
    
    @Test
    public void testSaveRowHeaderWithPrefixLessThanYearWidth() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        BufferedWriter writer = mock(BufferedWriter.class);
        String code = "123456";
        int yearWidth = 6;
        Year year = new Year(99);
        
        tucsonSimple.saveRowHeader(writer, sample, code, yearWidth, year);
        
        verify(writer).write(code + "   99");
    }
    
    @Test
    public void testSaveData() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        BufferedWriter writer = mock(BufferedWriter.class);
        Range range = new Range(new Year(2000), new Year(2001));
        Map<Integer, Object> data = new HashMap<>();
        data.put(0, 1);
        data.put(1, 2);
        sample.setData(data);
        sample.setRange(range);
        
        tucsonSimple.saveData(sample, writer);
        
        verify(writer).write("000001 2000  ");
        verify(writer).write("       2     ");
        verify(writer).write("   999");
        verify(writer).newLine();
    }
    
    @Test
    public void testComputeRangeWithStartGreaterThanOne() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        Range range = new Range(new Year(2000), new Year(2001));
        sample.setRange(range);
        
        Range computedRange = tucsonSimple.computeRange(sample);
        
        assertEquals(range, computedRange);
    }
    
    @Test
    public void testComputeRangeWithStartPlus8000GreaterThanOne() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        Range range = new Range(new Year(1), new Year(2001));
        sample.setRange(range);
        
        Range computedRange = tucsonSimple.computeRange(sample);
        
        assertEquals(range.redateBy(8000), computedRange);
    }
    
    @Test
    public void testComputeRangeWithStartPlus8000LessThanOne() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        Range range = new Range(new Year(1000), new Year(2001));
        sample.setRange(range);
        
        Range computedRange = tucsonSimple.computeRange(sample);
        
        assertEquals(range.redateStartTo(new Year(1001)), computedRange);
    }
    
    @Test
    public void testSave() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        BufferedWriter writer = mock(BufferedWriter.class);
        
        tucsonSimple.save(sample, writer);
        
        verify(tucsonSimple).saveData(sample, writer);
    }
}