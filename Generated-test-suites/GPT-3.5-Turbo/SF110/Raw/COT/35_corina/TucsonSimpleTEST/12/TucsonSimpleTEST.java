import static org.junit.Assert.*;
import org.junit.Test;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TucsonSimpleTest {

    @Test
    public void testToString() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        String expected = "Tucson Simple";
        String actual = tucsonSimple.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetDefaultExtension() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        String expected = ".RWL";
        String actual = tucsonSimple.getDefaultExtension();
        assertEquals(expected, actual);
    }

    @Test(expected = WrongFiletypeException.class)
    public void testLoad() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedReader reader = null; // replace with actual BufferedReader
        tucsonSimple.load(reader);
    }

    @Test
    public void testMake6digitCodeWithExistingId() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.meta = new HashMap<>();
        sample.meta.put("id", "1234567890");
        String expected = "123456";
        String actual = tucsonSimple.make6digitCode(sample);
        assertEquals(expected, actual);
    }

    @Test
    public void testMake6digitCodeWithoutId() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        String expected = "000000";
        String actual = tucsonSimple.make6digitCode(sample);
        assertEquals(expected, actual);
    }

    @Test
    public void testMake6digitCodeWithLongId() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.meta = new HashMap<>();
        sample.meta.put("id", "1234567890");
        String expected = "123456";
        String actual = tucsonSimple.make6digitCode(sample);
        assertEquals(expected, actual);
    }

    //... continue with more test cases for the rest of the methods
    
}

class Sample {
    Map<String, Object> meta;
    //...other fields
}

class Year {
    //...implementation not provided
}

class Range {
    //...implementation not provided
}

class WrongFiletypeException extends IOException {
    //...implementation not provided
}

class StringUtils {
    //...implementation not provided
}