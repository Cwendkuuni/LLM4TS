import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.Charset;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class SourceFileTest {

    @Test
    public void testGetLine() {
        SourceFile sourceFile = new SourceFile("test.js");

        String line = sourceFile.getLine(1);
        assertNotNull(line);
        assertEquals("/*", line);

        line = sourceFile.getLine(100);
        assertNull(line);
    }

    @Test
    public void testGetRegion() {
        SourceFile sourceFile = new SourceFile("test.js");

        SourceFile.Region region = sourceFile.getRegion(1);
        assertNotNull(region);
        assertEquals("/*\n * Copyright 2009 The Closure Compiler Authors.\n *\n * Licensed under the Apache License, Version 2.0 (the \"License\");\n * you may not use this file except in compliance with the License.\n * You may obtain a copy of the License at\n *\n *     http://www.apache.org/licenses/LICENSE-2.0\n *\n * Unless required by applicable law or agreed to in writing, software\n * distributed under the License is distributed on an \"AS IS\" BASIS,\n * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n * See the License for the specific language governing permissions and\n * limitations under the License.\n */", region.getText());

        region = sourceFile.getRegion(100);
        assertNull(region);
    }

    @Test
    public void testGetLineOffset() {
        SourceFile sourceFile = new SourceFile("test.js");

        int offset = sourceFile.getLineOffset(1);
        assertEquals(0, offset);

        offset = sourceFile.getLineOffset(100);
        try {
            sourceFile.getLineOffset(-1);
        } catch (IllegalArgumentException e) {
            assertEquals("Expected line number between 1 and 1\nActual: -1", e.getMessage());
        }

        try {
            sourceFile.getLineOffset(1000);
        } catch (IllegalArgumentException e) {
            assertEquals("Expected line number between 1 and 1\nActual: 1000", e.getMessage());
        }
    }

    @Test
    public void testGetNumLines() {
        SourceFile sourceFile = new SourceFile("test.js");

        int numLines = sourceFile.getNumLines();
        assertEquals(1, numLines);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullFileName() {
        new SourceFile(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithEmptyFileName() {
        new SourceFile("");
    }

    @Test
    public void testFromCode() throws IOException {
        SourceFile sourceFile = SourceFile.fromCode("test.js", "console.log('Hello, world!');");

        assertEquals("test.js", sourceFile.getName());
        assertEquals("console.log('Hello, world!');", sourceFile.getCode());
    }

    @Test
    public void testFromFile() throws IOException {
        SourceFile sourceFile = SourceFile.fromFile("test.js");

        assertEquals("test.js", sourceFile.getName());
        assertNotNull(sourceFile.getCode());
    }
}