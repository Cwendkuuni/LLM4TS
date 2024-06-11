package com.puppycrawl.tools.checkstyle.api;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

public class FileTextTest {
private FileText fileText;

@Before
public void setUp() throws IOException {
File file = new File("testFile.txt");
List<String> lines = Arrays.asList("Line 1", "Line 2", "Line 3");
fileText = FileText.fromLines(file, lines);
}

@Test
public void testGetFile() {
    assertEquals(new File("testFile.txt"), fileText.getFile());
}

@Test
public void testGetCharset() {
    assertNull(fileText.getCharset());
}

@Test
public void testGetFullText() {
    assertEquals("Line 1\nLine 2\nLine 3\n", fileText.getFullText());
}

@Test
public void testToLinesArray() {
    assertArrayEquals(new String[]{"Line 1", "Line 2", "Line 3"}, fileText.toLinesArray());
}

@Test
public void testLineColumn() {
    LineColumn lineColumn = fileText.lineColumn(5);
    assertEquals(1, lineColumn.getLine());
    assertEquals(4, lineColumn.getColumn());

    lineColumn = fileText.lineColumn(10);
    assertEquals(2, lineColumn.getLine());
    assertEquals(4, lineColumn.getColumn());

    lineColumn = fileText.lineColumn(15);
    assertEquals(3, lineColumn.getLine());
    assertEquals(4, lineColumn.getColumn());
}

@Test
public void testGet() {
    assertEquals("Line 1", fileText.get(0));
    assertEquals("Line 2", fileText.get(1));
    assertEquals("Line 3", fileText.get(2));
}

@Test
public void testSize() {
    assertEquals(3, fileText.size());
}
}