package com.puppycrawl.tools.checkstyle.api;
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class FileTextTest {

    @Test
    public void testConstructorWithFileAndCharsetName() {
        File file = new File("testFile.txt");
        String charsetName = "UTF-8";
        try {
            FileText fileText = new FileText(file, charsetName);
            assertNotNull(fileText);
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testConstructorWithFileTextObject() {
        File file = new File("testFile.txt");
        String charsetName = "UTF-8";
        try {
            FileText originalFileText = new FileText(file, charsetName);
            FileText copyFileText = new FileText(originalFileText);
            assertNotNull(copyFileText);
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testGetFile() {
        File file = new File("testFile.txt");
        String charsetName = "UTF-8";
        try {
            FileText fileText = new FileText(file, charsetName);
            assertEquals(file, fileText.getFile());
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    // Additional test cases for other public methods and edge cases can be added here

}