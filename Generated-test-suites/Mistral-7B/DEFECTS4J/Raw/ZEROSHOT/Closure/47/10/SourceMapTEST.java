import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.debugging.sourcemap.FilePosition;
import com.google.debugging.sourcemap.SourceMapFormat;
import com.google.debugging.sourcemap.SourceMapGenerator;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.SourceMap;
import com.google.javascript.jscomp.SourceMap.Format;
import com.google.javascript.jscomp.SourceMap.DetailLevel;
import com.google.javascript.jscomp.NodeUtil;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SourceMapTest {

  private SourceMap generatorV1;
  private SourceMap generatorV2;
  private SourceMap generatorV3;
  private Node node;
  private SourceMap sourceMap;

  @Before
  public void setup() {
    SourceMapGeneratorFactory factory = mock(SourceMapGeneratorFactory.class);
    when(factory.getInstance(SourceMapFormat.V1)).thenReturn(new SourceMapGeneratorV1());
    when(factory.getInstance(SourceMapFormat.V2)).thenReturn(new SourceMapGeneratorV2());

    generatorV1 = new SourceMap(factory.getInstance(SourceMapFormat.V1));
    generatorV2 = new SourceMap(factory.getInstance(SourceMapFormat.V2));
    generatorV3 = new SourceMap(factory.getInstance(SourceMapFormat.V3));

    node = mock(Node.class);
  }

  @Test
  public void testConstructor() {
    SourceMap sourceMapV1 = new SourceMap(generatorV1.getInstance());
    SourceMap sourceMapV2 = new SourceMap(generatorV2.getInstance());
    SourceMap sourceMapV3 = new SourceMap(generatorV3.getInstance());

    assertNotNull(sourceMapV1);
    assertNotNull(sourceMapV2);
    assertNotNull(sourceMapV3);
  }

  @Test
  public void testAddMapping() throws IOException {
    String sourceFile = "test.js";
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 10);

    sourceMap.addMapping(node, null, outputStartPosition, outputEndPosition);

    String fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);
    assertNotNull(fixedSourceFile);
    assertEquals(sourceFile, fixedSourceFile);

    sourceMap.addMapping(node, null, outputStartPosition, outputEndPosition);
    fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);
    assertNotNull(fixedSourceFile);
    assertEquals(sourceFile, fixedSourceFile);

    sourceFile = "test2.js";
    sourceMap.addMapping(node, null, outputStartPosition, outputEndPosition);
    fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);
    assertNotNull(fixedSourceFile);
    assertEquals("test2.js", fixedSourceFile);
  }

  @Test
  public void testFixupSourceLocation() throws IOException {
    List<LocationMapping> prefixMappings = Lists.newArrayList(
        new LocationMapping("test/", "test"),
        new LocationMapping("test2/", "test2"));

    sourceMap.setPrefixMappings(prefixMappings);

    String sourceFile = "test/test.js";
    String fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);
    assertEquals("test:test.js", fixedSourceFile);

    sourceFile = "test2/test.js";
    fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);
    assertEquals("test2:test.js", fixedSourceFile);

    sourceFile = "test/test2.js";
    fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);
    assertEquals("test:test2.js", fixedSourceFile);
  }

  @Test
  public void testAppendTo() throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();

    sourceMap.appendTo(out, "test.js");
    sourceMap.addMapping(node, null, null, null);
    sourceMap.appendTo(out, "test.js");

    String expected = "// SourceMap:\n";
    expected += "// version=0.1.0\n";
    expected += "// sourcefile=test.js\n";
    expected += "1 1 test.js test.js:1:1 test.js:1:1\n";

    assertEquals(expected, out.toString());
  }

  @Test
  public void testReset() throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();

    sourceMap.appendTo(out, "test.js");
    sourceMap.addMapping(node, null, null, null);
    sourceMap.appendTo(out, "test.js");
    sourceMap.reset();

    assertEquals("", out.toString());
  }

  @Test
  public void testSetStartingPosition() throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();

    sourceMap.appendTo(out, "test.js");
    sourceMap.addMapping(node, null, null, null);
    sourceMap.appendTo(out, "test.js");

    sourceMap.setStartingPosition(1, 1);
    sourceMap.appendTo(out, "test.js");

    String expected = "// SourceMap:\n";
    expected += "// version=0.1.0\n";
    expected += "// sourcefile=test.js\n";
    expected += "1 1 test.js test.js:1:1 test.js:1:1\n";
    expected += "1 1 test.js test.js:1:1 test.js:1:1\n";

    assertEquals(expected, out.toString());
  }

  @Test
  public void testValidate() throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();

    sourceMap.appendTo(out, "test.js");
    sourceMap.addMapping(node, null, null, null);
    sourceMap.appendTo(out, "test.js");

    sourceMap.validate(true);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetWrapperPrefixNull() throws IOException {
    sourceMap.setWrapperPrefix(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetWrapperPrefixEmptyString() throws IOException {
    sourceMap.setWrapperPrefix("");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetWrapperPrefixZeroLengthString() throws IOException {
    sourceMap.setWrapperPrefix("\u0000");
  }

  @Test
  public void testSetWrapperPrefix() throws IOException {
    String wrapperPrefix = "test_";

    sourceMap.setWrapperPrefix(wrapperPrefix);

    ByteArrayOutputStream out = new ByteArrayOutputStream();

    sourceMap.appendTo(out, "test.js");
    sourceMap.addMapping(node, null, null, null);
    sourceMap.appendTo(out, "test.js");

    String expected = "// SourceMap:\n";
    expected += "// version=0.1.0\n";
    expected += "// sourcefile=test.js\n";
    expected += "1 1 test_test.js test.js:1:1 test.js:1:1\n";

    assertEquals(expected, out.toString());
  }

  @Test
  public void testAddMappingDetailLevelAll() throws IOException {
    DetailLevel detailLevel = DetailLevel.ALL;
    sourceMap.setDetailLevel(detailLevel);

    String sourceFile = "test.js";
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 10);

    sourceMap.addMapping(node, null, outputStartPosition, outputEndPosition);

    String fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);
    assertNotNull(fixedSourceFile);
    assertEquals(sourceFile, fixedSourceFile);
  }

  @Test
  public void testAddMappingDetailLevelSymbols() throws IOException {
    DetailLevel detailLevel = DetailLevel.SYMBOLS;
    sourceMap.setDetailLevel(detailLevel);

    String sourceFile = "test.js";
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 10);

    sourceMap.addMapping(node, null, outputStartPosition, outputEndPosition);

    String fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);
    assertNotNull(fixedSourceFile);
    assertEquals("test.js", fixedSourceFile);
  }

  @Test
  public void testFixupSourceLocationDetailLevelAll() throws IOException {
    DetailLevel detailLevel = DetailLevel.ALL;
    sourceMap.setDetailLevel(detailLevel);

    List<LocationMapping> prefixMappings = Lists.newArrayList(
        new LocationMapping("test/", "test"),
        new LocationMapping("test2/", "test2"));

    sourceMap.setPrefixMappings(prefixMappings);

    String sourceFile = "test/test.js";
    String fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);
    assertEquals("test:test.js", fixedSourceFile);

    sourceFile = "test2/test.js";
    fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);
    assertEquals("test2:test.js", fixedSourceFile);

    sourceFile = "test/test2.js";
    fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);
    assertEquals("test:test2.js", fixedSourceFile);
  }

  @Test
  public void testFixupSourceLocationDetailLevelSymbols() throws IOException {
    DetailLevel detailLevel = DetailLevel.SYMBOLS;
    sourceMap.setDetailLevel(detailLevel);

    List<LocationMapping> prefixMappings = Lists.newArrayList(
        new LocationMapping("test/", "test"),
        new LocationMapping("test2/", "test2"));

    sourceMap.setPrefixMappings(prefixMappings);

    String sourceFile = "test/test.js";
    String fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);
    assertEquals("test.js", fixedSourceFile);

    sourceFile = "test2/test.js";
    fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);
    assertEquals("test2.js", fixedSourceFile);

    sourceFile = "test/test2.js";
    fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);
    assertEquals("test.js", fixedSourceFile);
  }
}