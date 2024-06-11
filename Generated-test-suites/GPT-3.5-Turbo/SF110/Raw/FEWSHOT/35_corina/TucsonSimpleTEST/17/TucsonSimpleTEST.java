package corina.formats;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import corina.Sample;
import corina.WrongFiletypeException;
import corina.Year;
import corina.Range;
import corina.util.StringUtils;

public class TucsonSimpleTest {
    
    private TucsonSimple tucsonSimple;
    
    @Mock
    private BufferedReader mockBufferedReader;
    
    @Mock
    private BufferedWriter mockBufferedWriter;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
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
        tucsonSimple.load(mockBufferedReader);
    }
    
    @Test
    public void testMake6digitCode_whenMetaContainsId() {
        Sample sampleWithId = new Sample();
        sampleWithId.getMeta().put("id", "1234567");
        
        String code = tucsonSimple.make6digitCode(sampleWithId);
        
        assertEquals("123456", code);
    }
    
    @Test
    public void testMake6digitCode_whenMetaDoesNotContainId() {
        Sample sampleWithoutId = new Sample();
        
        String code = tucsonSimple.make6digitCode(sampleWithoutId);
        
        assertEquals("000000", code);
    }
    
    @Test
    public void testMake6digitCode_whenCodeLengthIsGreaterThan6() {
        Sample sampleWithLongCode = new Sample();
        sampleWithLongCode.getMeta().put("id", "123456789");
        
        String code = tucsonSimple.make6digitCode(sampleWithLongCode);
        
        assertEquals("123456", code);
    }
    
    @Test
    public void testSaveRowHeader_whenYIsLessThanSampleRangeStart() throws IOException {
        Sample sample = new Sample();
        sample.getRange().setStart(new Year(2000));
        
        tucsonSimple.saveRowHeader(mockBufferedWriter, sample, "123456", 6, new Year(1999));
        
        // assert code + year
    }
    
    @Test
    public void testSaveRowHeader_whenYIsEqualToSampleRangeStart() throws IOException {
        Sample sample = new Sample();
        sample.getRange().setStart(new Year(2000));
        
        tucsonSimple.saveRowHeader(mockBufferedWriter, sample, "123456", 6, new Year(2000));
        
        // assert code + year
    }
    
    @Test
    public void testSaveData() throws IOException {
        // Test implementation of saveData method
    }
    
    @Test
    public void testComputeRange_whenStartIsGreaterThanOrEqualToYear1() {
        Sample sample = new Sample();
        sample.getRange().setStart(new Year(2000));
        
        Range range = tucsonSimple.computeRange(sample);
        
        assertNotNull(range);
        assertEquals(sample.getRange(), range);
    }
    
    @Test
    public void testComputeRange_whenStartPlus8000IsGreaterThanOrEqualToYear1() {
        Sample sample = new Sample();
        sample.getRange().setStart(new Year(-7999));
        
        Range range = tucsonSimple.computeRange(sample);
        
        assertNotNull(range);
        assertEquals(sample.getRange().redateBy(8000), range);
    }
    
    @Test
    public void testComputeRange_whenStartIsLessThanYear1() {
        Sample sample = new Sample();
        sample.getRange().setStart(new Year(-999));
        
        Range range = tucsonSimple.computeRange(sample);
        
        assertNotNull(range);
        assertEquals(sample.getRange().redateStartTo(new Year(1001)), range);
    }
    
    @Test
    public void testSave() throws IOException {
        // Mock the method and test it
    }
}