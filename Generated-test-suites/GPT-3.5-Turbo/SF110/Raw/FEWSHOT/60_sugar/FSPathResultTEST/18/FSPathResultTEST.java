package net.sf.sugar.fspath;

import org.junit.Test;
import static org.junit.Assert.*;

public class FSPathResultTest {

    private String testString = "testString";
    private File testFile = new File("testFile");
    private Double testDouble = 1.0;
    private Date testDate = new Date();
    private URI testURI = new URI("testURI");
    private Boolean testBoolean = true;

    @Test
    public void testGetFile() {
        FSPathResult result = new FSPathResult(testFile);
        assertEquals(testFile, result.getFile());
    }

    @Test
    public void testGetString() {
        FSPathResult result = new FSPathResult(testString);
        assertEquals(testString, result.getString());
    }

    @Test
    public void testGetDouble() {
        FSPathResult result = new FSPathResult(testDouble);
        assertEquals(testDouble, result.getDouble());
    }

    @Test
    public void testGetDate() {
        FSPathResult result = new FSPathResult(testDate);
        assertEquals(testDate, result.getDate());
    }

    @Test
    public void testGetURI() {
        FSPathResult result = new FSPathResult(testURI);
        assertEquals(testURI, result.getURI());
    }

    @Test
    public void testGetBoolean() {
        FSPathResult result = new FSPathResult(testBoolean);
        assertEquals(testBoolean, result.getBoolean());
    }

    @Test
    public void testToString() {
        FSPathResult fileResult = new FSPathResult(testFile);
        assertEquals(testFile.getAbsolutePath(), fileResult.toString());

        FSPathResult stringResult = new FSPathResult(testString);
        assertEquals(testString, stringResult.toString());

        FSPathResult doubleResult = new FSPathResult(testDouble);
        assertEquals(testDouble.toString(), doubleResult.toString());

        FSPathResult booleanResult = new FSPathResult(testBoolean);
        assertEquals(testBoolean.toString(), booleanResult.toString());

        FSPathResult dateResult = new FSPathResult(testDate);
        assertEquals(testDate.toString(), dateResult.toString());

        FSPathResult uriResult = new FSPathResult(testURI);
        assertEquals(testURI.toString(), uriResult.toString());

        // Test empty result
        FSPathResult emptyResult = new FSPathResult(null);
        assertEquals("FSPathResult : empty", emptyResult.toString());
    }
}