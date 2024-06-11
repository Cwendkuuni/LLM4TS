package corina.formats;

import org.junit.Test;
import static org.junit.Assert.*;
import corina.ui.*;
import java.io.*;
import corina.util.*;
import corina.*;

public class TucsonSimpleTest {

    @Test
    public void testToString() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        String result = tucsonSimple.toString();
        String expected = I18n.getText("format.tucsonsimple");
        assertEquals(expected, result);
    }

    @Test
    public void testGetDefaultExtension() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        String result = tucsonSimple.getDefaultExtension();
        String expected = ".RWL";
        assertEquals(expected, result);
    }

    @Test(expected = WrongFiletypeException.class)
    public void testLoad() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedReader r = null;
        tucsonSimple.load(r);
    }

    @Test
    public void testMake6digitCode() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample s = new Sample();
        s.meta.put("id", "123456789");
        String result = tucsonSimple.make6digitCode(s);
        String expected = "123456";
        assertEquals(expected, result);

        s.meta.clear();
        result = tucsonSimple.make6digitCode(s);
        expected = "000000";
        assertEquals(expected, result);
    }

    @Test
    public void testSaveRowHeader() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedWriter w = null;
        Sample s = new Sample();
        String code = "123456";
        int yearWidth = 6;
        Year y = new Year(2000);
        assertThrows(IOException.class, () -> {
            tucsonSimple.saveRowHeader(w, s, code, yearWidth, y);
        });
    }

    @Test
    public void testSaveData() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample s = new Sample();
        BufferedWriter w = null;
        assertThrows(IOException.class, () -> {
            tucsonSimple.saveData(s, w);
        });
    }

    @Test
    public void testComputeRange() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample s = new Sample();
        Range result = tucsonSimple.computeRange(s);
        Year start = s.range.getStart();
        Year expected;
        if (start.compareTo(new Year(1)) >= 0) {
            expected = s.range;
        } else if (start.add(8000).compareTo(new Year(1)) >= 0) {
            expected = s.range.redateBy(8000);
        } else {
            expected = s.range.redateStartTo(new Year(1001));
        }
        assertEquals(expected, result);
    }

    @Test
    public void testSave() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedWriter w = null;
        Sample s = new Sample();
        assertThrows(IOException.class, () -> {
            tucsonSimple.save(s, w);
        });
    }
}