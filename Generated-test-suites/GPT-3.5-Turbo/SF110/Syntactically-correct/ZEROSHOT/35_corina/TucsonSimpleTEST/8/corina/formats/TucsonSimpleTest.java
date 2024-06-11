package corina.formats;

import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class TucsonSimpleTest {

    private TucsonSimple tucsonSimple;
    private Sample sample;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    @Before
    public void setUp() {
        tucsonSimple = new TucsonSimple();
        sample = mock(Sample.class);
        bufferedReader = mock(BufferedReader.class);
        bufferedWriter = mock(BufferedWriter.class);
    }

    @Test(expected = WrongFiletypeException.class)
    public void testLoad() throws IOException {
        tucsonSimple.load(bufferedReader);
    }

    @Test
    public void testToString() {
        assertEquals("format.tucsonsimple", tucsonSimple.toString());
    }

    @Test
    public void testGetDefaultExtension() {
        assertEquals(".RWL", tucsonSimple.getDefaultExtension());
    }

    @Test
    public void testMake6digitCode() {
        Map<String, Object> meta = new HashMap<>();
        meta.put("id", "123456789");
        when(sample.meta).thenReturn(meta);
        String code = tucsonSimple.make6digitCode(sample);
        assertEquals("123456", code);

        meta.clear();
        code = tucsonSimple.make6digitCode(sample);
        assertEquals("000000", code);
    }

    @Test
    public void testSaveRowHeaderWithComparison() throws IOException {
        Sample sample = mock(Sample.class);
        Year start = mock(Year.class);
        Year y = mock(Year.class);

        when(this.sample.range.getStart()).thenReturn(start);
        when(y.compareTo(this.sample.range.getStart())).thenReturn(-1);

        tucsonSimple.saveRowHeader(bufferedWriter, this.sample, "123456", 6, null);

        verify(bufferedWriter).write("123456 " + start.toString());
    }

    @Test
    public void testSaveRowHeaderWithoutComparison() throws IOException {
        Sample sample = mock(Sample.class);
        Year start = mock(Year.class);
        Year y = mock(Year.class);

        when(this.sample.range.getStart()).thenReturn(start);
        when(y.toString()).thenReturn("2020");

        tucsonSimple.saveRowHeader(bufferedWriter, this.sample, "123456", 6, y);

        verify(bufferedWriter).write("123456 2020");
    }

    @Test
    public void testSaveData() throws IOException {
        Sample sample = mock(Sample.class);
        Range range = mock(Range.class);
        Year start = mock(Year.class);
        Year end = mock(Year.class);
        Year y = mock(Year.class);

        when(this.sample.range).thenReturn(range);
        when(range.getStart()).thenReturn(start);
        when(range.getEnd()).thenReturn(end);

        when(start.column()).thenReturn(0);
        when(y.column()).thenReturn(0);
        when(y.compareTo(end)).thenReturn(1);

        tucsonSimple.saveData(this.sample, bufferedWriter);

        verify(bufferedWriter).write("123456 " + start.toString());
        verify(bufferedWriter).write("   999");
    }

    @Test
    public void testComputeRangeWithGreaterThanOneYearFromStart() {
        Sample sample = mock(Sample.class);
        Range range = mock(Range.class);
        Year start = mock(Year.class);
        Year year1 = mock(Year.class);
        Year year2 = mock(Year.class);
        Year year3 = mock(Year.class);
        Year year4 = mock(Year.class);
        Year year5 = mock(Year.class);

        when(this.sample.range).thenReturn(range);
        when(range.getStart()).thenReturn(start);
        when(start.compareTo(new Year(1))).thenReturn(1);
        when(start.add(8000)).thenReturn(year1);
        when(year1.compareTo(new Year(1))).thenReturn(1);
        when(year1.add(8000)).thenReturn(year2);
        when(year2.compareTo(new Year(1))).thenReturn(1);
        when(year2.add(8000)).thenReturn(year3);
        when(year3.compareTo(new Year(1))).thenReturn(1);
        when(year3.add(8000)).thenReturn(year4);
        when(year4.compareTo(new Year(1))).thenReturn(1);
        when(year4.add(8000)).thenReturn(year5);
        when(year5.compareTo(new Year(1))).thenReturn(-1);

        Range result = tucsonSimple.computeRange(sample);

        assertEquals(range, result);
    }

    @Test
    public void testComputeRangeWithLessThanOneYearFromStart() {
        Sample sample = mock(Sample.class);
        Range range = mock(Range.class);
        Year start = mock(Year.class);
        Year year1 = mock(Year.class);
        Year year2 = mock(Year.class);

        when(this.sample.range).thenReturn(range);
        when(range.getStart()).thenReturn(start);
        when(start.compareTo(new Year(1))).thenReturn(-1);
        when(start.add(8000)).thenReturn(year1);
        when(year1.compareTo(new Year(1))).thenReturn(1);
        when(year1.add(8000)).thenReturn(year2);
        when(year2.compareTo(new Year(1))).thenReturn(-1);

        Range result = tucsonSimple.computeRange(sample);

        assertEquals("1001-01-01", result.getStart().toString());
        assertEquals(range.getEnd(), result.getEnd());
    }

    @Test
    public void testSave() throws IOException {
        Sample sample = mock(Sample.class);

        tucsonSimple.save(sample, bufferedWriter);

        verify(tucsonSimple).saveData(sample, bufferedWriter);
    }
}