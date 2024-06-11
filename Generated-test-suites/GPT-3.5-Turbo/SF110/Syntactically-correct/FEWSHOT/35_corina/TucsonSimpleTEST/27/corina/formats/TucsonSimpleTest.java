package corina.formats;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import corina.Sample;
import corina.WrongFiletypeException;
import corina.ui.I18n;
import corina.util.Range;
import corina.util.StringUtils;
import corina.util.Year;

public class TucsonSimpleTest {
    private TucsonSimple tucsonSimple;
    
    @Before
    public void setUp() {
        tucsonSimple = new TucsonSimple();
    }
    
    @Test
    public void testToString() {
        assertEquals(I18n.getText("format.tucsonsimple"), tucsonSimple.toString());
    }
    
    @Test
    public void testGetDefaultExtension() {
        assertEquals(".RWL", tucsonSimple.getDefaultExtension());
    }
    
    @Test(expected = WrongFiletypeException.class)
    public void testLoad() throws IOException {
        BufferedReader r = mock(BufferedReader.class);
        tucsonSimple.load(r);
    }
    
    @Test
    public void testMake6digitCode() {
        Sample sample = new Sample();
        sample.meta.put("id", "12345678");
        assertEquals("123456", tucsonSimple.make6digitCode(sample));
        
        sample.meta.remove("id");
        assertEquals("000000", tucsonSimple.make6digitCode(sample));
    }
    
    @Test
    public void testSaveRowHeader() throws IOException {
        BufferedWriter w = mock(BufferedWriter.class);
        Sample sample = new Sample();
        sample.range = new Range(new Year(2000), new Year(2020));
        String code = "123456";
        int yearWidth = 6;
        Year y = new Year(2000);
        tucsonSimple.saveRowHeader(w, sample, code, yearWidth, y);
    }
    
    @Test
    public void testSaveData() throws IOException {
        BufferedWriter w = mock(BufferedWriter.class);
        Sample sample = new Sample();
        sample.data.put(0, 1);
        sample.count.put(0, 2);
        tucsonSimple.saveData(sample, w);
    }
    
    @Test
    public void testComputeRange() {
        Sample sample = new Sample();
        sample.range = new Range(new Year(1), new Year(10));
        assertEquals(sample.range, tucsonSimple.computeRange(sample));
        
        sample.range = new Range(new Year(8001), new Year(8010));
        assertEquals(sample.range.redateBy(8000), tucsonSimple.computeRange(sample));
        
        sample.range = new Range(new Year(1001), new Year(2000));
        assertEquals(sample.range.redateStartTo(new Year(1001)), tucsonSimple.computeRange(sample));
    }
    
    @Test
    public void testSave() throws IOException {
        BufferedWriter w = mock(BufferedWriter.class);
        Sample sample = new Sample();
        tucsonSimple.save(sample, w);
    }
}