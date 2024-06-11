import static org.junit.Assert.*;
import static com.google.javascript.jscomp.SourceMap.DetailLevel.*;
import static com.google.javascript.jscomp.SourceMap.Format.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

import com.google.common.base.Predicate;
import com.google.common.collect.Maps;
import com.google.debugging.sourcemap.FilePosition;
import com.google.debugging.sourcemap.SourceMap;
import com.google.debugging.sourcemap.SourceMapFormat;
import com.google.debugging.sourcemap.SourceMapGenerator;
import com.google.debugging.sourcemap.SourceMapGeneratorFactory;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;

@RunWith(JUnit4.class)
public class SourceMapTest {

  private SourceMapGeneratorFactory generatorFactory;
  private SourceMapGenerator generator;
  private SourceMap sourceMap;
  private Appendable outputStream;

  @Before
  public void setUp() throws Exception {
    generatorFactory = Mockito.mock(SourceMapGeneratorFactory.class);
    generator = Mockito.mock(SourceMapGenerator.class);
    Mockito.when(generatorFactory.getInstance(any(SourceMapFormat.class)))
        .thenReturn(generator);

    outputStream = new ByteArrayOutputStream();
  }

  @Test
  public void testGetInstance() throws IOException {
    for (Format format : Format.values()) {
      SourceMap sourceMap = SourceMap.getInstance(format);
      assertEquals(format, sourceMap.getFormat());
    }
  }

  @Test
  public void testAddMapping() throws IOException {
    sourceMap = V1.getInstance();

    Node node = Mockito.mock(Node.class);
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 2);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    String expectedSourceMap =
        "1 1\n" +
        "SourceFile \"SourceFile.js\"\n" +
        "SourceFile \"SourceFile.js\"\n" +
        "SQ SourceFile \"SourceFile.js\" \"SourceFile.js\"\n" +
        "FQ Name \"SourceMapTest$SourceMapTest.java\" \"SourceMapTest$SourceMapTest.java\"\n" +
        "SQ Name \"SourceMapTest$SourceMapTest.java\" \"SourceMapTest$SourceMapTest.java\"\n" +
        "FQ Folder \"/\n" +
        "FQ Folder \"/\n" +
        "FQ Line 1\n" +
        "FQ Line 1\n" +
        "FQ Column 1\n" +
        "FQ Column 1\n";

    Mockito.verify(generator).addMapping(
        "SourceFile.js", "SourceMapTest$SourceMapTest.java",
        outputStartPosition, outputEndPosition);

    Mockito.verify(generator).appendTo(outputStream, expectedSourceMap);
  }

  @Test
  public void testFixupSourceLocation() throws IOException {
    sourceMap = V1.getInstance();

    String sourceFile = "test/SourceMapTest/SourceMapTest.java";
    String prefix = "test/";

    String fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);
    assertEquals("SourceMapTest.java", fixedSourceFile);

    prefixMappings = Collections.singletonList(new SourceMap.LocationMapping(prefix, ""));
    sourceMap.setPrefixMappings(prefixMappings);

    fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);
    assertEquals("", fixedSourceFile);

    fixedSourceFile = sourceMap.fixupSourceLocation(prefix + sourceFile);
    assertEquals("SourceMapTest.java", fixedSourceFile);
  }

  @Test
  public void testAppendTo() throws IOException {
    String name = "test.js";

    sourceMap = V1.getInstance();
    sourceMap.setPrefixMappings(Collections.emptyList());

    sourceMap.appendTo(outputStream, name);

    String expectedSourceMap =
        "1 1\n" +
        "SourceFile \"test.js\"\n" +
        "SourceFile \"test.js\"\n" +
        "SQ SourceFile \"test.js\" \"test.js\"\n" +
        "FQ Name \"\" \"\"\n" +
        "SQ Name \"\" \"\"\n" +
        "FQ Folder \"/\n" +
        "FQ Folder \"/\n" +
        "FQ Line 1\n" +
        "FQ Line 1\n" +
        "FQ Column 1\n" +
        "FQ Column 1\n";

    String actualSourceMap = outputStream.toString();
    assertEquals(expectedSourceMap, actualSourceMap);
  }

  @Test
  public void testReset() throws IOException {
    sourceMap = V1.getInstance();
    sourceMap.setPrefixMappings(prefixMappings);
    sourceMap.appendTo(outputStream, "test.js");

    sourceMap.reset();

    String expectedSourceMap = "";
    String actualSourceMap = outputStream.toString();
    assertEquals(expectedSourceMap, actualSourceMap);

    Mockito.verify(generator).reset();
  }

  @Test
  public void testSetStartingPosition() throws IOException {
    sourceMap = V1.getInstance();

    sourceMap.setStartingPosition(1, 1);

    String expectedSourceMap =
        "1 1\n" +
        "SourceFile \"SourceFile.js\"\n" +
        "SourceFile \"SourceFile.js\"\n" +
        "SQ SourceFile \"SourceFile.js\" \"SourceFile.js\"\n" +
        "FQ Name \"SourceMapTest$SourceMapTest.java\" \"SourceMapTest$SourceMapTest.java\"\n" +
        "SQ Name \"SourceMapTest$SourceMapTest.java\" \"SourceMapTest$SourceMapTest.java\"\n" +
        "FQ Folder \"/\n" +
        "FQ Folder \"/\n" +
        "FQ Line 1\n" +
        "FQ Line 1\n" +
        "FQ Column 1\n" +
        "FQ Column 1\n" +
        "FQ Line 0\n" +
        "FQ Line 0\n";

    Mockito.verify(generator).setStartingPosition(1, 1);
    Mockito.verify(generator).appendTo(outputStream, expectedSourceMap);
  }

  @Test
  public void testSetWrapperPrefix() throws IOException {
    sourceMap = V1.getInstance();

    sourceMap.setWrapperPrefix("wrap_");

    String expectedSourceMap =
        "1 1\n" +
        "SourceFile \"SourceFile.js\"\n" +
        "SourceFile \"SourceFile.js\"\n" +
        "SQ SourceFile \"SourceFile.js\" \"SourceFile.js\"\n" +
        "FQ Name \"SourceMapTest$SourceMapTest.java\" \"SourceMapTest$SourceMapTest.java\"\n" +
        "SQ Name \"SourceMapTest$SourceMapTest.java\" \"SourceMapTest$SourceMapTest.java\"\n" +
        "FQ Folder \"/\n" +
        "FQ Folder \"/\n" +
        "FQ Line 1\n" +
        "FQ Line 1\n" +
        "FQ Column 1\n" +
        "FQ Column 1\n" +
        "FQ Line 0\n" +
        "FQ Line 0\n" +
        "FQ Line 1\n" +
        "FQ Line 1\n" +
        "FQ Column 1\n" +
        "FQ Column 1\n" +
        "FQ Line 0\n" +
        "FQ Line 0\n" +
        "FQ Name \"wrap_SourceMapTest$SourceMapTest.java\" \"wrap_SourceMapTest$SourceMapTest.java\"\n" +
        "SQ Name \"wrap_SourceMapTest$SourceMapTest.java\" \"wrap_SourceMapTest$SourceMapTest.java\"\n" +
        "FQ Folder \"/\n" +
        "FQ Folder \"/\n";

    Mockito.verify(generator).setWrapperPrefix("wrap_");
    Mockito.verify(generator).appendTo(outputStream, expectedSourceMap);
  }

  @Test
  public void testValidate() throws IOException {
    sourceMap = V1.getInstance();

    sourceMap.validate(true);

    Mockito.verify(generator).validate(true);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddMappingWithNullNode() throws IOException {
    sourceMap = V1.getInstance();

    sourceMap.addMapping(null, Mockito.mock(FilePosition.class),
        Mockito.mock(FilePosition.class));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddMappingWithNullOutputStartPosition() throws IOException {
    sourceMap = V1.getInstance();

    Node node = Mockito.mock(Node.class);

    sourceMap.addMapping(node, null, Mockito.mock(FilePosition.class));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddMappingWithNullOutputEndPosition() throws IOException {
    sourceMap = V1.getInstance();

    Node node = Mockito.mock(Node.class);
    FilePosition outputStartPosition = Mockito.mock(FilePosition.class);

    sourceMap.addMapping(node, outputStartPosition, null);
  }

  @Test(expected = IOException.class)
  public void testAppendToWithNullOutput() throws IOException {
    sourceMap = V1.getInstance();

    sourceMap.appendTo(null, "test.js");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValidateWithNullGenerator() throws IOException {
    SourceMap sourceMap = new SourceMap(null);

    sourceMap.validate(true);
  }
}