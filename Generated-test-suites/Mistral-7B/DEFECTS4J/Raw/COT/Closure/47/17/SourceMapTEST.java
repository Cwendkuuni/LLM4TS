import static org.junit.Assert.*;
import static com.google.javascript.jscomp.SourceMap.DetailLevel.*;
import static com.google.javascript.jscomp.SourceMap.Format.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class SourceMapTest {

  private SourceMap sourceMap;
  private SourceMapGenerator generatorV1;
  private SourceMapGenerator generatorV2;
  private ByteArrayOutputStream outputStream;

  @Before
  public void setup() throws Exception {
    generatorV1 = new SourceMapGeneratorV1();
    generatorV2 = new SourceMapGeneratorV2();
    sourceMap = new SourceMap(V1.getInstance());
    outputStream = new ByteArrayOutputStream();
  }

  @Test
  public void testAddMapping() {
    String sourceFile = "TestSourceFile.js";
    Node node = Node.newJsonObject();
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 10);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    String fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);
    assertEquals(sourceFile, fixedSourceFile);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddMappingNullNode() {
    Node nullNode = null;
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 10);

    sourceMap.addMapping(nullNode, outputStartPosition, outputEndPosition);
  }

  @Test
  public void testAddMappingInvalidSourceFile() {
    String invalidSourceFile = null;
    Node node = Node.newJsonObject();
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 10);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    String fixedSourceFile = sourceMap.fixupSourceLocation(invalidSourceFile);
    assertEquals(invalidSourceFile, fixedSourceFile);
  }

  @Test
  public void testFixupSourceLocation() {
    String sourceFile = "TestPrefixSourceFile.js";
    String prefix = "TestPrefix";
    List<LocationMapping> prefixMappings = Lists.newArrayList(
        new SourceMap.LocationMapping(prefix, "TestFixedPrefix"));

    sourceMap.setPrefixMappings(prefixMappings);

    String fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);
    assertEquals("TestFixedPrefix" + sourceFile.substring(prefix.length()),
        fixedSourceFile);
  }

  @Test
  public void testAppendTo() throws IOException {
    String name = "TestOutputFile.js.map";

    sourceMap.appendTo(outputStream, name);

    String expected = generatorV1.generateSourceMap(null, generatorV1.getDefaultOptions());
    String actual = new String(outputStream.toByteArray());

    assertEquals(expected, actual);
  }

  @Test(expected = IOException.class)
  public void testAppendToNullOutput() throws IOException {
    String name = null;

    sourceMap.appendTo(null, name);
  }

  @Test
  public void testReset() {
    sourceMap.reset();

    assertEquals(Collections.emptyList(), sourceMap.prefixMappings);
    assertEquals(Maps.newHashMap(), sourceMap.sourceLocationFixupCache);
  }

  @Test
  public void testSetStartingPosition() {
    int offsetLine = 1;
    int offsetIndex = 1;

    sourceMap.setStartingPosition(offsetLine, offsetIndex);

    FilePosition expected = new FilePosition(offsetLine, offsetIndex);
    FilePosition actual = sourceMap.generator.getStartingPosition();

    assertEquals(expected, actual);
  }

  @Test
  public void testSetWrapperPrefix() {
    String prefix = "TestWrapperPrefix";

    sourceMap.setWrapperPrefix(prefix);

    String expected = prefix;
    String actual = sourceMap.generator.getWrapperPrefix();

    assertEquals(expected, actual);
  }

  @Test
  public void testValidate() {
    boolean validate = true;

    sourceMap.validate(validate);

    boolean expected = true;
    boolean actual = sourceMap.generator.isValidate();

    assertEquals(expected, actual);
  }
}