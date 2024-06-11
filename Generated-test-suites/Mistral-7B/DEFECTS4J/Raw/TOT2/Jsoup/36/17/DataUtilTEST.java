package org.jsoup.helper; //import statements go here

import org.junit.Test;
import static org.junit.Assert.*;

public class DataUtilTest { // Test class name should be the same as the class being tested with "Test" suffix

    @Test
    public void testLoadFileDocument() throws IOException {
        File file = new File("test.html"); // replace with actual test file
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";
        Document document = DataUtil.load(file, charsetName, baseUri); // call method under test
        assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testLoadFileDocumentWithInvalidFile() throws IOException {
        File file = new File("non-existent-file.html"); // replace with actual non-existent file
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";
        DataUtil.load(file, charsetName, baseUri); // call method under test, which should throw an IOException
    }

    // Add test cases for other methods here

}