
package corina.formats;

import org.junit.Test;
import corina.ui.*;
import java.io.*;
import corina.util.*;
import corina.*;

public class TucsonSimpleTest {

    @Test
    public void testToString() {
        TucsonSimple tucson = new TucsonSimple();
        String expected = "Tucson Simple";
        String actual = tucson.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetDefaultExtension() {
        TucsonSimple tucson = new TucsonSimple();
        String expected = ".RWL";
        String actual = tucson.getDefaultExtension();
        assertEquals(expected, actual);
    }

    @Test(expected = WrongFiletypeException.class)
    public void testLoad() throws IOException {
        TucsonSimple tucson = new TucsonSimple();
        BufferedReader reader = new BufferedReader(new FileReader("test.txt"));
        tucson.load(reader);
    }

    @Test
    public void testMake6digitCodeWithId() {
        TucsonSimple tucson = new TucsonSimple();
        Sample sample = new Sample();
        sample.meta.put("id", "1234567");
        String expected = "123456";
        String actual = tucson.make6digitCode(sample);
        assertEquals(expected, actual);
    }

    @Test
    public void testMake6digitCodeWithoutId() {
        TucsonSimple tucson = new TucsonSimple();
        Sample sample = new Sample();
        String expected = "000000";
        String actual = tucson.make6digitCode(sample);
        assertEquals(expected, actual);
    }

    @Test
    public void testSaveRowHeader() throws IOException {
        TucsonSimple tucson = new TucsonSimple();
        BufferedWriter writer = new BufferedWriter(new FileWriter("test.txt"));
        Sample sample = new Sample();
        sample.range = new Range(new Year(2000), new Year(2010));
        Year year = new Year(2005);
        tucson.saveRowHeader(writer, sample, "123456", 6, year);
        writer.close();
        BufferedReader reader = new BufferedReader(new FileReader("test.txt"));
        String expected = "123456  2005";
        String actual = reader.readLine();
        assertEquals(expected, actual);
        reader.close();
        new File("test.txt").delete();
    }
    
    // Test case for saveData, computeRange and save methods is missing, as they are interdependent and require mocking data for Sample object.
}

