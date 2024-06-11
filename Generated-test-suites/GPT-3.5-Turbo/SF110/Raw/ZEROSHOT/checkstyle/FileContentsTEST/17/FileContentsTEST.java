import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FileContentsTest {
    
    private FileContents fileContents;
    
    @Before
    public void setUp() {
        String[] lines = {
            "package com.puppycrawl.tools.checkstyle.api;",
            "",
            "import com.puppycrawl.tools.checkstyle.grammars.*;",
            "import java.util.regex.*;",
            "import java.io.*;",
            "import com.google.common.collect.*;",
            "import java.util.*;",
            "",
            "public final class FileContents implements CommentListener",
            "{",
            "    // class implementation",
            "}"
        };
        fileContents = new FileContents(new FileText(lines));
    }
    
    @Test
    public void testReportSingleLineComment() {
        fileContents.reportSingleLineComment("type", 1, 3);
        
        ImmutableMap<Integer, TextBlock> cppComments = fileContents.getCppComments();
        TextBlock expectedComment = new Comment(new String[] {"// class implementation"}, 3, 1, 18);
        TextBlock actualComment = cppComments.get(1);
        
        assertEquals(expectedComment, actualComment);
    }
    
    @Test
    public void testReportBlockComment() {
        fileContents.reportBlockComment("type", 1, 3, 6, 15);
        
        ImmutableMap<Integer, List<TextBlock>> cComments = fileContents.getCComments();
        List<TextBlock> expectedComments = new ArrayList<>();
        expectedComments.add(new Comment(new String[] {"// class implementation"}, 3, 6, 15));
        List<TextBlock> actualComments = cComments.get(1);
        
        assertEquals(expectedComments, actualComments);
    }
    
    @Test
    public void testGetJavadocBefore() {
        fileContents.reportBlockComment("type", 1, 3, 6, 15);
        
        TextBlock expectedJavadoc = new Comment(new String[] {"// class implementation"}, 3, 6, 15);
        TextBlock actualJavadoc = fileContents.getJavadocBefore(7);
        
        assertEquals(expectedJavadoc, actualJavadoc);
    }
    
    @Test
    public void testLineIsBlank() {
        assertTrue(fileContents.lineIsBlank(0));
        assertFalse(fileContents.lineIsBlank(1));
    }
    
    @Test
    public void testLineIsComment() {
        assertTrue(fileContents.lineIsComment(11));
        assertFalse(fileContents.lineIsComment(10));
    }
    
    @Test
    public void testHasIntersectionWithComment() {
        boolean result = fileContents.hasIntersectionWithComment(3, 2, 7, 15);
        
        assertFalse(result);
    }
    
    @Test
    public void testInPackageInfo() {
        boolean result = fileContents.inPackageInfo();
        
        assertFalse(result);
    }
    
    @Test
    public void testGetFileName() {
        String expectedFileName = "";
        String actualFileName = fileContents.getFileName();
        
        assertEquals(expectedFileName, actualFileName);
    }
    
    @Test
    public void testGetText() {
        FileText expectedText = new FileText(new String[] {
            "package com.puppycrawl.tools.checkstyle.api;",
            "",
            "import com.puppycrawl.tools.checkstyle.grammars.*;",
            "import java.util.regex.*;",
            "import java.io.*;",
            "import com.google.common.collect.*;",
            "import java.util.*;",
            "",
            "public final class FileContents implements CommentListener",
            "{",
            "    // class implementation",
            "}"
        });
        FileText actualText = fileContents.getText();
        
        assertEquals(expectedText, actualText);
    }
    
    @Test
    public void testGetLines() {
        String[] expectedLines = {
            "package com.puppycrawl.tools.checkstyle.api;",
            "",
            "import com.puppycrawl.tools.checkstyle.grammars.*;",
            "import java.util.regex.*;",
            "import java.io.*;",
            "import com.google.common.collect.*;",
            "import java.util.*;",
            "",
            "public final class FileContents implements CommentListener",
            "{",
            "    // class implementation",
            "}"
        };
        String[] actualLines = fileContents.getLines();
        
        assertArrayEquals(expectedLines, actualLines);
    }
    
    @Test
    public void testGetLine() {
        String expectedLine = "package com.puppycrawl.tools.checkstyle.api;";
        String actualLine = fileContents.getLine(0);
        
        assertEquals(expectedLine, actualLine);
    }
    
    @Deprecated
    @Test
    public void testGetFilename() {
        String expectedFilename = "";
        String actualFilename = fileContents.getFilename();
        
        assertEquals(expectedFilename, actualFilename);
    }
    
}