package corina.formats;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import corina.Sample;
import corina.WrongFiletypeException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class TucsonSimpleTest {

    private TucsonSimple tucsonSimple;
    private BufferedReader mockedReader;
    private BufferedWriter mockedWriter;
    private Sample mockedSample;

    @Before
    public void setUp() {
        tucsonSimple = new TucsonSimple();
        mockedReader = mock(BufferedReader.class);
        mockedWriter = mock(BufferedWriter.class);
        mockedSample = mock(Sample.class);
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
        tucsonSimple.load(mockedReader);
    }

    @Test
    public void testMake6digitCodeWithId() {
        when(mockedSample.getMeta("id")).thenReturn("123456");
        assertEquals("123456", tucsonSimple.make6digitCode(mockedSample));
    }

    @Test
    public void testMake6digitCodeWithoutId() {
        when(mockedSample.getMeta("id")).thenReturn(null);
        assertEquals("000000", tucsonSimple.make6digitCode(mockedSample));
    }

    @Test
    public void testMake6digitCodeLongId() {
        when(mockedSample.getMeta("id")).thenReturn("1234567890");
        assertEquals("123456", tucsonSimple.make6digitCode(mockedSample));
    }

    @Test
    public void testSaveRowHeaderWithStartBeforeYear() throws IOException {
        Year start = mock(Year.class);
        Year year = mock(Year.class);
        when(mockedSample.getRange()).thenReturn(importRange);
        when(start.toString()).thenReturn("1990");
        when(year.toString()).thenReturn("1991");
        when(year.compareTo(importRange.getStart())).thenReturn(-1);
        when(year.add(-start.column())).thenReturn(year);
        tucsonSimple.saveRowHeader(mockedWriter, mockedSample, "123456", 4, year);
        verify(mockedWriter).write("1234561990");
    }

    @Test
    public void testSaveRowHeaderWithStartEqualToYear() throws IOException {
        Year start = mock(Year.class);
        Year year = mock(Year.class);
        when(mockedSample.getRange()).thenReturn(importRange);
        when(start.toString()).thenReturn("1990");
        when(year.toString()).thenReturn("1990");
        when(year.compareTo(importRange.getStart())).thenReturn(0);
        when(year.add(-start.column())).thenReturn(year);
        tucsonSimple.saveRowHeader(mockedWriter, mockedSample, "123456", 6, year);
        verify(mockedWriter).write("1234561990");
    }

    @Test
    public void testSaveRowHeaderWithStartAfterYear() throws IOException {
        Year start = mock(Year.class);
        Year year = mock(Year.class);
        when(mockedSample.getRange()).thenReturn(importRange);
        when(start.toString()).thenReturn("1992");
        when(year.toString()).thenReturn("1991");
        when(year.compareTo(importRange.getStart())).thenReturn(1);
        when(year.add(-start.column())).thenReturn(year);
        tucsonSimple.saveRowHeader(mockedWriter, mockedSample, "123456", 6, year);
        verify(mockedWriter).write("1234561991");
    }

    @Test
    public void testSaveData() throws IOException {
        Year start = mock(Year.class);
        Year end = mock(Year.class);
        Sample.Data mockedData = mock(Sample.Data.class);

        when(mockedSample.getData()).thenReturn(mockedData);
        when(mockedSample.getRange()).thenReturn(importRange);
        when(importRange.getStart()).thenReturn(start);
        when(importRange.getEnd()).thenReturn(end);
        when(start.column()).thenReturn(0);
        when(end.column()).thenReturn(9);
        when(mockedData.get(year.diff(start))).thenReturn("10");
        when(year.diff(start)).thenReturn(0);

        tucsonSimple.saveData(mockedSample, mockedWriter);

        verify(mockedWriter).write("000010");
        verify(mockedWriter, times(8)).write(anyString());
        verify(mockedWriter).newLine();
    }

    @Test
    public void testComputeRangeWithStartGreaterThan1() {
        Range mockedRange = mock(Range.class);

        when(mockedSample.getRange()).thenReturn(mockedRange);
        when(mockedRange.getStart()).thenReturn(new Year(2));

        assertEquals(mockedRange, tucsonSimple.computeRange(mockedSample));
    }

    @Test
    public void testComputeRangeWithStartEqualTo1() {
        Range mockedRange = mock(Range.class);

        when(mockedSample.getRange()).thenReturn(mockedRange);
        when(mockedRange.getStart()).thenReturn(new Year(1));

        assertEquals(mockedRange, tucsonSimple.computeRange(mockedSample));
    }

    @Test
    public void testComputeRangeWithStartLessThan1() {
        Range mockedRange = mock(Range.class);
        Range mockedRedatedRange = mock(Range.class);

        when(mockedSample.getRange()).thenReturn(mockedRange);
        when(mockedRange.getStart()).thenReturn(new Year(0));
        when(mockedRange.redateBy(8000)).thenReturn(mockedRedatedRange);

        assertEquals(mockedRedatedRange, tucsonSimple.computeRange(mockedSample));
    }

    @Test
    public void testSave() throws IOException {
        tucsonSimple.save(mockedSample, mockedWriter);

        verify(mockedSample).getData();
        verify(mockedWriter).write("000000   999");
        verify(mockedWriter).newLine();
    }
}