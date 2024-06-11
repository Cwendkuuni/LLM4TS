package corina.formats;

import org.junit.Test;
import org.junit.Assert;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import corina.ui.*;
import corina.util.*;
import corina.*;

public class TucsonSimpleTest {
    
    @Test
    public void testToString() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        String expected = "format.tucsonsimple";
        String result = tucsonSimple.toString();
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testGetDefaultExtension() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        String expected = ".RWL";
        String result = tucsonSimple.getDefaultExtension();
        Assert.assertEquals(expected, result);
    }
    
    @Test(expected = WrongFiletypeException.class)
    public void testLoad() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedReader r = null;
        tucsonSimple.load(r);
    }
    
    @Test
    public void testMake6digitCodeWithValidId() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        Map<String, Object> meta = new HashMap<>();
        meta.put("id", "123456789");
        sample.meta = meta;
        String expected = "123456";
        String result = tucsonSimple.make6digitCode(sample);
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testMake6digitCodeWithInvalidId() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        Map<String, Object> meta = new HashMap<>();
        sample.meta = meta;
        String expected = "000000";
        String result = tucsonSimple.make6digitCode(sample);
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testMake6digitCodeWithLongId() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        Map<String, Object> meta = new HashMap<>();
        meta.put("id", "1234567890");
        sample.meta = meta;
        String expected = "123456";
        String result = tucsonSimple.make6digitCode(sample);
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testSaveRowHeaderWithStartYearBeforeSampleRangeStart() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedWriter w = new BufferedWriter(new FileWriter("test.txt"));
        Sample sample = new Sample();
        Range range = new Range(new Year(2000), new Year(2020));
        sample.range = range;
        String code = "123456";
        int yearWidth = 6;
        Year y = new Year(1990);
        tucsonSimple.saveRowHeader(w, sample, code, yearWidth, y);
        w.close();
        BufferedReader reader = new BufferedReader(new FileReader("test.txt"));
        String expected = "123456 1990";
        String result = reader.readLine();
        reader.close();
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testSaveRowHeaderWithStartYearEqualToSampleRangeStart() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedWriter w = new BufferedWriter(new FileWriter("test.txt"));
        Sample sample = new Sample();
        Range range = new Range(new Year(2000), new Year(2020));
        sample.range = range;
        String code = "123456";
        int yearWidth = 6;
        Year y = new Year(2000);
        tucsonSimple.saveRowHeader(w, sample, code, yearWidth, y);
        w.close();
        BufferedReader reader = new BufferedReader(new FileReader("test.txt"));
        String expected = "123456 2000";
        String result = reader.readLine();
        reader.close();
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testSaveRowHeaderWithStartYearAfterSampleRangeStart() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedWriter w = new BufferedWriter(new FileWriter("test.txt"));
        Sample sample = new Sample();
        Range range = new Range(new Year(2000), new Year(2020));
        sample.range = range;
        String code = "123456";
        int yearWidth = 6;
        Year y = new Year(2005);
        tucsonSimple.saveRowHeader(w, sample, code, yearWidth, y);
        w.close();
        BufferedReader reader = new BufferedReader(new FileReader("test.txt"));
        String expected = "123456 2005";
        String result = reader.readLine();
        reader.close();
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testSaveData() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedWriter w = new BufferedWriter(new FileWriter("test.txt"));
        Sample sample = new Sample();
        Range range = new Range(new Year(2000), new Year(2005));
        sample.range = range;
        Map<Year, Object> data = new HashMap<>();
        data.put(new Year(2000), 10);
        data.put(new Year(2001), 20);
        data.put(new Year(2002), 30);
        data.put(new Year(2003), 40);
        data.put(new Year(2004), 50);
        data.put(new Year(2005), 60);
        sample.data = data;
        tucsonSimple.saveData(sample, w);
        w.close();
        BufferedReader reader = new BufferedReader(new FileReader("test.txt"));
        String expected = "      10     20     30     40     50     60";
        String result = reader.readLine();
        reader.close();
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testComputeRangeWithStartGreaterThanYearOne() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        Year start = new Year(2000);
        Year end = new Year(2020);
        Range range = new Range(start, end);
        sample.range = range;
        Range expected = range;
        Range result = tucsonSimple.computeRange(sample);
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testComputeRangeWithStartPlus8000GreaterThanYearOne() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        Year start = new Year(-7999);
        Year end = new Year(2020);
        Range range = new Range(start, end);
        sample.range = range;
        Range expected = range.redateBy(8000);
        Range result = tucsonSimple.computeRange(sample);
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testComputeRangeWithStartLessThanYearOne() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        Year start = new Year(-1000);
        Year end = new Year(2020);
        Range range = new Range(start, end);
        sample.range = range;
        Range expected = range.redateStartTo(new Year(1001));
        Range result = tucsonSimple.computeRange(sample);
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testSave() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedWriter w = new BufferedWriter(new FileWriter("test.txt"));
        Sample sample = new Sample();
        Range range = new Range(new Year(2000), new Year(2020));
        sample.range = range;
        Map<Year, Object> data = new HashMap<>();
        data.put(new Year(2000), 10);
        data.put(new Year(2001), 20);
        data.put(new Year(2002), 30);
        data.put(new Year(2003), 40);
        data.put(new Year(2004), 50);
        data.put(new Year(2005), 60);
        sample.data = data;
        tucsonSimple.save(sample, w);
        w.close();
        BufferedReader reader = new BufferedReader(new FileReader("test.txt"));
        String expected = "      10     20     30     40     50     60";
        String result = reader.readLine();
        reader.close();
        Assert.assertEquals(expected, result);
    }
}