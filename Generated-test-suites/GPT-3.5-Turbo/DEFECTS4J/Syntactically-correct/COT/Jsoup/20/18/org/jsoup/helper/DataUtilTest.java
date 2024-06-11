
import static org.junit.Assert.*;
import org.junit.Test;

import org.jsoup.helper.DataUtil;

public class DataUtilTest {

    //Test for load(File in, String charsetName, String baseUri)
    @Test
    public void testLoadFile() {
        //1. Test with a valid file, valid charsetName, and valid baseUri
        try {
            DataUtil.load(new File("validFile.html"), "UTF-8", "http://example.com");
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }

        //2. Test with a null file
        try {
            DataUtil.load(null, "UTF-8", "http://example.com");
            fail("No exception thrown");
        } catch (IOException e) {
            assertEquals("Unexpected exception message", "java.io.FileNotFoundException: null (No such file or directory)", e.getMessage());
        }
    }

    //Test for load(InputStream in, String charsetName, String baseUri)
    @Test
    public void testLoadInputStream() {
        //1. Test with a valid input stream, valid charsetName, and valid baseUri
        try {
            DataUtil.load(new ByteArrayInputStream("valid html".getBytes()), "UTF-8", "http://example.com");
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }

        //2. Test with a null input stream
        try {
            DataUtil.load(null, "UTF-8", "http://example.com");
            fail("No exception thrown");
        } catch (IOException e) {
            assertEquals("Unexpected exception message", "java.io.IOException: InputStream is null", e.getMessage());
        }
    }

    //Test for load(InputStream in, String charsetName, String baseUri, Parser parser)
    @Test
    public void testLoadInputStreamWithParser() {
        //1. Test with a valid input stream, valid charsetName, valid baseUri, and valid parser
        try {
            DataUtil.load(new ByteArrayInputStream("valid html".getBytes()), "UTF-8", "http://example.com", Parser.htmlParser());
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }

        //2. Test with a null input stream
        try {
            DataUtil.load(null, "UTF-8", "http://example.com", Parser.htmlParser());
            fail("No exception thrown");
        } catch (IOException e) {
            assertEquals("Unexpected exception message", "java.io.IOException: InputStream is null", e.getMessage());
        }
    }

    //Test for parseByteData(ByteBuffer byteData, String charsetName, String baseUri, Parser parser)
    // This method is a private method and cannot be directly tested.
    // It is tested indirectly by testing the public methods that use it.

    //Test for readToByteBuffer(InputStream inStream)
    // This method is a private method and cannot be directly tested.
    // It is tested indirectly by testing the public methods that use it.

    //Test for getCharsetFromContentType(String contentType)
    // This method is a private method and cannot be directly tested.
    // It is tested indirectly by testing the public methods that use it.

}
