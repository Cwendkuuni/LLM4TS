package net.sf.sugar.fspath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.URI;
import java.util.Date;

public class FSPathResultTest {

    private File file;
    private String string;
    private Double resultDouble;
    private Date date;
    private URI uri;
    private Boolean resultBoolean;
    private FSPathResult fsPathResult;

    @Before
    public void setUp() {
        file = new File("testFile");
        string = "testString";
        resultDouble = 1.0;
        date = new Date();
        uri = URI.create("testURI");
        resultBoolean = true;
        fsPathResult = new FSPathResult(file);
    }

    @Test
    public void testGetFile() {
        assertEquals(file, fsPathResult.getFile());
    }

    @Test
    public void testGetString() {
        fsPathResult = new FSPathResult(string);
        assertEquals(string, fsPathResult.getString());
    }

    @Test
    public void testGetDouble() {
        fsPathResult = new FSPathResult(resultDouble);
        assertEquals(resultDouble, fsPathResult.getDouble());
    }

    @Test
    public void testGetDate() {
        fsPathResult = new FSPathResult(date);
        assertEquals(date, fsPathResult.getDate());
    }

    @Test
    public void testGetURI() {
        fsPathResult = new FSPathResult(uri);
        assertEquals(uri, fsPathResult.getURI());
    }

    @Test
    public void testGetBoolean() {
        fsPathResult = new FSPathResult(resultBoolean);
        assertEquals(resultBoolean, fsPathResult.getBoolean());
    }

    @Test
    public void testToStringFile() {
        assertEquals(file.getAbsolutePath(), fsPathResult.toString());
    }

    @Test
    public void testToStringString() {
        fsPathResult = new FSPathResult(string);
        assertEquals(string, fsPathResult.toString());
    }

    @Test
    public void testToStringDouble() {
        fsPathResult = new FSPathResult(resultDouble);
        assertEquals(resultDouble.toString(), fsPathResult.toString());
    }

    @Test
    public void testToStringBoolean() {
        fsPathResult = new FSPathResult(resultBoolean);
        assertEquals(resultBoolean.toString(), fsPathResult.toString());
    }

    @Test
    public void testToStringDate() {
        fsPathResult = new FSPathResult(date);
        assertEquals(date.toString(), fsPathResult.toString());
    }

    @Test
    public void testToStringURI() {
        fsPathResult = new FSPathResult(uri);
        assertEquals(uri.toString(), fsPathResult.toString());
    }

    @Test
    public void testToStringEmpty() {
        assertNull(fsPathResult.toString());
    }
}