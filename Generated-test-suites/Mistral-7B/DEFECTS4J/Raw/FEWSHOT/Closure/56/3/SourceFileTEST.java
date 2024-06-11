package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.cli.SourceFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Test cases for the SourceFile class.
 */
public class SourceFileTest {

    @Test
    public void testPreloadedSourceFile() throws IOException {
        String fileName = "TestPreloadedSourceFile.java";
        String code = "public class TestPreloadedSourceFile {\n" +
                "}";

        SourceFile sourceFile = SourceFile.fromCode(fileName, code);
        assertEquals(fileName, sourceFile.getName());
        assertEquals(code, sourceFile.getCode());
    }

    @Test
    public void testOnDiskSourceFile() throws IOException {
        String fileName = "TestOnDiskSourceFile.java";
        File file = new File(fileName);
        Charset charset = StandardCharsets.UTF_8;

        String code = "public class TestOnDiskSourceFile {\n" +
                "}";
        Files.write(file.toPath(), code.getBytes(charset));

        SourceFile sourceFile = SourceFile.fromFile(fileName, charset);
        assertEquals(fileName, sourceFile.getName());
        assertEquals(code, sourceFile.getCode());

        Files.deleteIfExists(file.toPath());
    }

    @Test
    public void testGetLine() throws IOException {
        String fileName = "TestGetLineSourceFile.java";
        File file = new File(fileName);
        Charset charset = StandardCharsets.UTF_8;

        String code = "public class TestGetLineSourceFile {\n" +
                "    public static void main(String[] args) {\n" +
                "        System.out.println(\"Hello World\");\n" +
                "    }\n" +
                "}";
        Files.write(file.toPath(), code.getBytes(charset));

        SourceFile sourceFile = SourceFile.fromFile(fileName, charset);

        assertEquals("public class TestGetLineSourceFile {\n",
                sourceFile.getCode());
        assertEquals("public class TestGetLineSourceFile {\n    public static void main(String[] args) {\n",
                sourceFile.getLine(1));
        assertEquals("        System.out.println(\"Hello World\");\n",
                sourceFile.getLine(4));

        Files.deleteIfExists(file.toPath());
    }

    @Test
    public void testGetRegion() throws IOException {
        String fileName = "TestGetRegionSourceFile.java";
        File file = new File(fileName);
        Charset charset = StandardCharsets.UTF_8;

        String code = "public class TestGetRegionSourceFile {\n" +
                "    public static void main(String[] args) {\n" +
                "        System.out.println(\"Hello World\");\n" +
                "    }\n" +
                "}";
        Files.write(file.toPath(), code.getBytes(charset));

        SourceFile sourceFile = SourceFile.fromFile(fileName, charset);

        Region region = sourceFile.getRegion(2);
        assertEquals(2, region.getStartLineNumber());
        assertEquals(3, region.getEndLineNumber());
        assertEquals("        System.out.println(\"Hello World\");\n", region.getCode());

        Files.deleteIfExists(file.toPath());
    }
}