import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.Charset;

import org.junit.Before;
import org.junit.Test;

public class SourceFileTest {

    private SourceFile sourceFile;

    @Before
    public void setUp() {
        sourceFile = new SourceFile("test");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_emptyFileName_throwsIllegalArgumentException() {
        SourceFile sourceFile = new SourceFile("");
    }

    @Test
    public void testConstructor_validFileName_setsFileName() {
        assertEquals("test", sourceFile.getName());
    }

    @Test
    public void testIsExtern_defaultValue_returnsFalse() {
        assertFalse(sourceFile.isExtern());
    }

    @Test
    public void testSetIsExtern_setsIsExternValue() {
        sourceFile.setIsExtern(true);
        assertTrue(sourceFile.isExtern());

        sourceFile.setIsExtern(false);
        assertFalse(sourceFile.isExtern());
    }

    @Test
    public void testToString_returnsFileName() {
        assertEquals("test", sourceFile.toString());
    }

    @Test
    public void testGetCodeReader_withPreloadedCode_returnsCorrectReader() throws IOException {
        String code = "var test = 'Hello World!';";
        SourceFile sourceFile = SourceFile.fromCode("test", code);

        Reader expectedReader = new StringReader(code);
        Reader actualReader = sourceFile.getCodeReader();

        char[] expectedChars = new char[100];
        int expectedLength = expectedReader.read(expectedChars);

        char[] actualChars = new char[100];
        int actualLength = actualReader.read(actualChars);

        assertArrayEquals(expectedChars, actualChars);
        assertEquals(expectedLength, actualLength);
    }

    @Test
    public void testGetCodeReader_fromOnDiskFile_returnsCorrectReader() throws IOException {
        File file = new File("test/test.js");
        String code = "var test = 'Hello World!';";
        Charset charset = Charset.forName("UTF-8");
        SourceFile sourceFile = SourceFile.fromFile(file, charset);

        Reader expectedReader = new StringReader(code);
        Reader actualReader = sourceFile.getCodeReader();

        char[] expectedChars = new char[100];
        int expectedLength = expectedReader.read(expectedChars);

        char[] actualChars = new char[100];
        int actualLength = actualReader.read(actualChars);

        assertArrayEquals(expectedChars, actualChars);
        assertEquals(expectedLength, actualLength);
    }

    // More test cases can be added here for other methods in the SourceFile class

}