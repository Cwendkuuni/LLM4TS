import org.junit.Assert;
import org.junit.Test;

public class FileContentsTest {

    // Test case for FileContents constructor with filename and lines
    @Test
    public void testConstructorWithFilenameAndLines() {
        String filename = "testfilename";
        String[] lines = {
            "// test line 1",
            "// test line 2",
            "Test line 3"
        };

        FileContents fileContents = new FileContents(filename, lines);

        Assert.assertEquals(filename, fileContents.getFileName());
        Assert.assertEquals(lines.length, fileContents.getLines().length);
    }

    // Test case for FileContents constructor with FileText
    @Test
    public void testConstructorWithFileText() {
        FileText fileText = new FileText(new File("testfile.txt"));
        FileContents fileContents = new FileContents(fileText);

        Assert.assertEquals(fileText.getFile().toString(), fileContents.getFileName());
        Assert.assertEquals(fileText.getNumberOfLines(), fileContents.getLines().length);
    }

    // Test case for reportSingleLineComment method
    @Test
    public void testReportSingleLineComment() {
        FileContents fileContents = new FileContents("testfilename");
        
        fileContents.reportSingleLineComment("test type", 4, 3);

        ImmutableMap<Integer, TextBlock> cppComments = fileContents.getCppComments();
        Assert.assertEquals(1, cppComments.size());
        Assert.assertNotNull(cppComments.get(4));
    }

    // Test case for reportBlockComment method
    @Test
    public void testReportBlockComment() {
        FileContents fileContents = new FileContents("testfilename");

        fileContents.reportBlockComment("test type", 2, 4, 3, 6);

        ImmutableMap<Integer, List<TextBlock>> cComments = fileContents.getCComments();
        Assert.assertEquals(1, cComments.size());
        Assert.assertNotNull(cComments.get(2));
        Assert.assertEquals(1, cComments.get(2).size());
    }

    // Test case for getJavadocBefore method
    @Test
    public void testGetJavadocBefore() {
        FileContents fileContents = new FileContents("testfilename");

        fileContents.reportSingleLineComment("type", 2, 3);
        fileContents.reportBlockComment("type", 4, 1, 5, 2);

        TextBlock javadoc = fileContents.getJavadocBefore(5);

        Assert.assertNotNull(javadoc);
    }

    // Test case for lineIsBlank method
    @Test
    public void testLineIsBlank() {
        FileContents fileContents = new FileContents("testfilename");

        Assert.assertTrue(fileContents.lineIsBlank(0));
        Assert.assertFalse(fileContents.lineIsBlank(1));
    }

    // Test case for lineIsComment method
    @Test
    public void testLineIsComment() {
        FileContents fileContents = new FileContents("testfilename");

        Assert.assertTrue(fileContents.lineIsComment(0));
        Assert.assertFalse(fileContents.lineIsComment(2));
    }

    // Test case for hasIntersectionWithComment method
    @Test
    public void testHasIntersectionWithComment() {
        FileContents fileContents = new FileContents("testfilename");

        Assert.assertFalse(fileContents.hasIntersectionWithComment(0, 0, 1, 1));
    }

    // Test case for inPackageInfo method
    @Test
    public void testInPackageInfo() {
        FileContents fileContents = new FileContents("package-info.java");

        Assert.assertTrue(fileContents.inPackageInfo());
    }
  
    // Test case for getFileName method
    @Test
    public void testGetFileName() {
        String filename = "testfilename";
        FileContents fileContents = new FileContents(filename, "test line");

        Assert.assertEquals(filename, fileContents.getFileName());
    }
  
    // Test case for getText method
    @Test
    public void testGetText() {
        FileText fileText = new FileText(new File("testfile.txt"));
        FileContents fileContents = new FileContents(fileText);

        Assert.assertEquals(fileText.getNumberOfLines(), fileContents.getText().getNumberOfLines());
    }
  
    // Test case for getLine method
    @Test
    public void testGetLine() {
        FileContents fileContents = new FileContents("testfilename", "line1");
        String line = fileContents.getLine(0);

        Assert.assertEquals("line1", line);
    }
  
    // Test case for lineIsComment method with invalid line number
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testLineIsCommentInvalidLineNumber() {
        FileContents fileContents = new FileContents("testfilename");
        fileContents.lineIsComment(2);
    }
  
    // Test case for hasIntersectionWithComment method with invalid line numbers
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testHasIntersectionWithCommentInvalidLineNumbers() {
        FileContents fileContents = new FileContents("testfilename");
        fileContents.hasIntersectionWithComment(0, 0, 3, 4);
    }
  
    // Test case for hasIntersectionWithCComment method with invalid line numbers
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testHasIntersectionWithCCommentInvalidLineNumbers() {
        FileContents fileContents = new FileContents("testfilename");
        fileContents.hasIntersectionWithCComment(0, 0, 3, 4);
    }

}