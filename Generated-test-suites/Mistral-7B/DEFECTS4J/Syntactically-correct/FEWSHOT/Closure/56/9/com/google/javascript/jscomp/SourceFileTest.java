package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.cli.SourceFile;
import org.apache.commons.cli.SourceFile.OnDisk;
import org.apache.commons.cli.SourceFile.Preloaded;
import org.apache.commons.cli.SourceFile.Generated;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.charset.Charset;

public class SourceFileTest {

    @Test
    public void testOnDisk() throws IOException {
        String fileName = "testOnDisk.js";
        File file = new File(fileName);
        Charset charset = Charsets.UTF_8;
        SourceFile sourceFile = SourceFile.fromFile(fileName, charset);
        String expectedCode = Files.toString(file, charset);
        String actualCode = sourceFile.getCode();
        assertEquals(expectedCode, actualCode);
    }

    @Test
    public void testPreloaded() {
        String fileName = "testPreloaded.js";
        String code = "function test() {\n" +
                "  print(\"Hello World\");\n" +
                "}";
        SourceFile sourceFile = SourceFile.fromCode(fileName, code);
        String actualCode = sourceFile.getCode();
        assertEquals(code, actualCode);
    }

    @Test
    public void testGenerated() {
        String fileName = "testGenerated.js";
        SourceFile.Generator generator = () -> "function test() {\n" +
                "  print(\"Hello World\");\n" +
                "}";
        SourceFile sourceFile = SourceFile.fromGenerator(fileName, generator);
        String actualCode = sourceFile.getCode();
        assertEquals(generator.getCode(), actualCode);
    }

    @Test
    public void testGetLine() throws IOException {
        String fileName = "testGetLine.js";
        File file = new File(fileName);
        Charset charset = Charsets.UTF_8;
        SourceFile sourceFile = SourceFile.fromFile(fileName, charset);
        String expectedLine1 = "function test() {\n";
        String expectedLine2 = "  print(\"Hello World\");\n";
        String expectedLine3 = "}";
        String actualLine1 = sourceFile.getLine(1);
        String actualLine2 = sourceFile.getLine(2);
        String actualLine3 = sourceFile.getLine(3);
        assertEquals(expectedLine1, actualLine1);
        assertEquals(expectedLine2, actualLine2);
        assertEquals(expectedLine3, actualLine3);
        assertNull(sourceFile.getLine(4));
    }

    @Test
    public void testGetRegion() throws IOException {
        String fileName = "testGetRegion.js";
        File file = new File(fileName);
        Charset charset = Charsets.UTF_8;
        SourceFile sourceFile = SourceFile.fromFile(fileName, charset);
        int lineNumber = 2;
        Region expectedRegion = new Region(1, 3, "  print(\"Hello World\");");
        Region actualRegion = sourceFile.getRegion(lineNumber);
        assertEquals(expectedRegion.getStartLine(), actualRegion.getStartLine());
        assertEquals(expectedRegion.getEndLine(), actualRegion.getEndLine());
        assertEquals(expectedRegion.getCode(), actualRegion.getCode());
    }
}

class Region {
    int startLine;
    int endLine;
    String code;

    Region(int startLine, int endLine, String code) {
        this.startLine = startLine;
        this.endLine = endLine;
        this.code = code;
    }

    public int getStartLine() {
        return startLine;
    }

    public int getEndLine() {
        return endLine;
    }

    public String getCode() {
        return code;
    }
}