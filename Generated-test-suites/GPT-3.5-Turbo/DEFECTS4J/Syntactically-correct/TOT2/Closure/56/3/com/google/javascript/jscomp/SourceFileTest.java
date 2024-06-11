import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;

public class SourceFileTest {

    @Test
    public void testGetLineOffset() {
        SourceFile sourceFile = new SourceFile("test_file.java");
        assertEquals(4, sourceFile.getLineOffset(1));
    }

    @Test
    public void testGetNumLines() {
        SourceFile sourceFile = new SourceFile("test_file.java");
        assertEquals(8, sourceFile.getNumLines());
    }

    @Test
    public void testGetLine() {
        SourceFile sourceFile = new SourceFile("test_file.java");
        assertEquals(" * Licensed under the Apache License, Version 2.0 (the \"License\");", sourceFile.getLine(2));
    }

    @Test
    public void testGetRegion() {
        SourceFile sourceFile = new SourceFile("test_file.java");
        assertEquals(" * Licensed under the Apache License, Version 2.0 (the \"License\");", sourceFile.getRegion(2).getCodeSnippet());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLineOffsetInvalidInput() {
        SourceFile sourceFile = new SourceFile("test_file.java");
        sourceFile.getLineOffset(0);
    }

    @Test(expected = IOException.class)
    public void testGetCodeException() throws IOException {
        SourceFile sourceFile = new SourceFile("test_file.java");
        sourceFile.getCode();
    }
}