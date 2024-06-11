package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.cli.SourceFile;
import org.apache.commons.cli.SourceFile.Generator;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class SourceFileTest {

    @Test
    public void testGetCode() throws IOException {
        String fileName = "test.js";
        String code = "function test() {\n" +
                "  print(\"Hello World\");\n" +
                "}";
        SourceFile sourceFile = new SourceFile(fileName) {
            @Override
            public String getCode() throws IOException {
                return code;
            }

            @Override
            public void clearCachedSource() {
                // Do nothing
            }
        };

        assertEquals(code, sourceFile.getCode());
    }

    @Test
    public void testGetCodeReader() throws IOException {
        String fileName = "test.js";
        String code = "function test() {\n" +
                "  print(\"Hello World\");\n" +
                "}";
        SourceFile sourceFile = new SourceFile(fileName) {
            @Override
            public Reader getCodeReader() throws IOException {
                return new StringReader(code);
            }

            @Override
            public void clearCachedSource() {
                // Do nothing
            }
        };

        String actualCode = CharStreams.toString(sourceFile.getCodeReader());
        assertEquals(code, actualCode);
    }

    @Test
    public void testGetLine() throws IOException {
        String fileName = "test.js";
        String code = "function test() {\n" +
                "  print(\"Hello World\");\n" +
                "}\n" +
                "print(\"Hello from getLine\");";
        SourceFile sourceFile = new SourceFile(fileName) {
            @Override
            public String getCode() throws IOException {
                return code;
            }
        };

        String expectedLine1 = "function test() {\n" +
                "  print(\"Hello World\");\n";
        String expectedLine2 = "print(\"Hello from getLine\");";

        assertEquals(expectedLine1, sourceFile.getLine(1));
        assertEquals(expectedLine2, sourceFile.getLine(3));
        assertNull(sourceFile.getLine(5));
    }

    @Test
    public void testGetRegion() throws IOException {
        String fileName = "test.js";
        String code = "function test() {\n" +
                "  print(\"Hello World\");\n" +
                "}\n" +
                "print(\"Hello from getLine\");";
        SourceFile sourceFile = new SourceFile(fileName) {
            @Override
            public String getCode() throws IOException {
                return code;
            }
        };

        String expectedRegion1 = "function test() {\n" +
                "  print(\"Hello World\");\n";
        String expectedRegion2 = "print(\"Hello from getLine\");";

        Region region1 = sourceFile.getRegion(1);
        Region region2 = sourceFile.getRegion(3);

        assertEquals(expectedRegion1, region1.getCode());
        assertEquals(expectedRegion2, region2.getCode());
    }

    @Test
    public void testFromFile() throws IOException {
        String fileName = "test.js";
        String code = "function test() {\n" +
                "  print(\"Hello World\");\n" +
                "}";
        Files.write(new File(fileName).toPath(), code.getBytes(StandardCharsets.UTF_8));

        SourceFile sourceFile = SourceFile.fromFile(fileName);

        String actualCode = sourceFile.getCode();
        assertEquals(code, actualCode);

        Files.delete(new File(fileName).toPath());
    }

    @Test
    public void testFromGenerator() {
        String fileName = "test.js";
        Generator generator = () -> "function test() {\n" +
                "  print(\"Hello World\");\n" +
                "}";
        SourceFile sourceFile = new SourceFile(fileName) {
            @Override
            public SourceFile.Generator getGenerator() {
                return generator;
            }
        };

        String actualCode = sourceFile.getCode();
        assertEquals("function test() {\n" +
                "  print(\"Hello World\");\n", actualCode);
    }
}

class Region {
    private int startLine;
    private int endLine;
    private String code;

    Region(int startLine, int endLine, String code) {
        this.startLine = startLine;
        this.endLine = endLine;
        this.code = code;
    }

    String getCode() {
        return code;
    }
}