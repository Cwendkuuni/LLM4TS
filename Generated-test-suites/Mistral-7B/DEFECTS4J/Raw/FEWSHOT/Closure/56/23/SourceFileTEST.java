package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.cli.SourceFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class SourceFileTest {

    @Test
    public void testBasicFunctionality() throws IOException {
        String fileName = "testFile.js";
        SourceFile sourceFile = new SourceFile(fileName);
        assertEquals(fileName, sourceFile.getName());
        assertEquals(false, sourceFile.isExtern());
        assertNull(sourceFile.getOriginalPath());
        assertEquals(0, sourceFile.getLineOffset(1));
        assertEquals(1, sourceFile.getNumLines());

        String code = "function test() {\n" +
                "  var x = 5;\n" +
                "}\n";
        sourceFile.setCode(code);
        assertEquals(code, sourceFile.getCode());
        assertEquals(code, sourceFile.getCodeNoCache());
        assertEquals("function test() {\n" +
                "  var x = 5;\n" +
                "}", sourceFile.getLine(1));
        Region region = sourceFile.getRegion(1);
        assertEquals(1, region.getStartLineNumber());
        assertEquals(1, region.getEndLineNumber());
        assertEquals("function test() {\n" +
                "  var x = 5;\n" +
                "}", region.getSourceCode());
    }

    @Test
    public void testFromFile() throws IOException {
        String fileName = "testFile.js";
        SourceFile sourceFile = SourceFile.fromFile(fileName);
        assertEquals(fileName, sourceFile.getName());
        assertNotNull(sourceFile.getOriginalPath());
        assertEquals(fileName, sourceFile.getOriginalPath());
        String code = Files.toString(new File(fileName), StandardCharsets.UTF_8);
        assertEquals(code, sourceFile.getCode());
        assertEquals(code, sourceFile.getCodeNoCache());
        Region region = sourceFile.getRegion(1);
        assertEquals(1, region.getStartLineNumber());
        assertEquals(1, region.getEndLineNumber());
        assertEquals(code.substring(0, Math.min(code.length(), 100)),
                region.getSourceCode());
    }

    @Test
    public void testFromInputStream() throws IOException {
        String fileName = "testFile.js";
        File file = new File(fileName);
        InputStream inputStream = new FileInputStream(file);
        SourceFile sourceFile = SourceFile.fromInputStream(fileName, inputStream);
        assertEquals(fileName, sourceFile.getName());
        assertNotNull(sourceFile.getOriginalPath());
        assertEquals(fileName, sourceFile.getOriginalPath());
        String code = CharStreams.toString(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        assertEquals(code, sourceFile.getCode());
        assertEquals(code, sourceFile.getCodeNoCache());
        Region region = sourceFile.getRegion(1);
        assertEquals(1, region.getStartLineNumber());
        assertEquals(1, region.getEndLineNumber());
        assertEquals(code.substring(0, Math.min(code.length(), 100)),
                region.getSourceCode());
        inputStream.close();
    }

    @Test
    public void testFromReader() throws IOException {
        String fileName = "testFile.js";
        File file = new File(fileName);
        Reader reader = new FileReader(file);
        SourceFile sourceFile = SourceFile.fromReader(fileName, reader);
        assertEquals(fileName, sourceFile.getName());
        assertNotNull(sourceFile.getOriginalPath());
        assertEquals(fileName, sourceFile.getOriginalPath());
        String code = CharStreams.toString(reader);
        assertEquals(code, sourceFile.getCode());
        assertEquals(code, sourceFile.getCodeNoCache());
        Region region = sourceFile.getRegion(1);
        assertEquals(1, region.getStartLineNumber());
        assertEquals(1, region.getEndLineNumber());
        assertEquals(code.substring(0, Math.min(code.length(), 100)),
                region.getSourceCode());
        reader.close();
    }

    @Test
    public void testFromGenerator() throws IOException {
        String fileName = "testFile.js";
        String code = "function test() {\n" +
                "  var x = 5;\n" +
                "}\n";
        Generator generator = () -> code;
        SourceFile sourceFile = SourceFile.fromGenerator(fileName, generator);
        assertEquals(fileName, sourceFile.getName());
        assertNull(sourceFile.getOriginalPath());
        assertEquals(code, sourceFile.getCode());
        assertEquals(code, sourceFile.getCodeNoCache());
        Region region = sourceFile.getRegion(1);
        assertEquals(1, region.getStartLineNumber());
        assertEquals(1, region.getEndLineNumber());
        assertEquals(code.substring(0, Math.min(code.length(), 100)),
                region.getSourceCode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullFileName() {
        new SourceFile(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyFileName() {
        new SourceFile("");
    }
}