import static org.junit.Assert.*;
import static com.google.javascript.jscomp.SourceMap.DetailLevel.*;
import static com.google.javascript.jscomp.SourceMap.Format.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.debugging.sourcemap.SourceMapFormat;
import com.google.debugging.sourcemap.SourceMapGenerator;
import com.google.debugging.sourcemap.SourceMapGeneratorV1;
import com.google.debugging.sourcemap.SourceMapGeneratorV2;
import com.google.javascript.jscomp.SourceMap.LocationMapping;
import com.google.javascript.jscomp.SourceMap;
import com.google.javascript.rhino.Node;

public class SourceMapTest {

  @Test
  public void testAddMapping() throws IOException {
    SourceMap sourceMap = V1.getInstance().newInstance();

    Node tree = Node.objectLiteral();

    FilePosition outputStartPosition = new FilePosition(1, 2);
    FilePosition outputEndPosition = new FilePosition(1, 5);

    sourceMap.addMapping(tree, outputStartPosition, outputEndPosition);

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    sourceMap.appendTo(outputStream, "sourcemap.js");

    String actualSourceMap = outputStream.toString();

    String expectedSourceMap = "{\"version\":1,\"file\":\"sourcemap.js\",\"sources\":[],\"mappings\":[[]]}";

    assertEquals(expectedSourceMap, actualSourceMap);
  }

  @Test
  public void testFixupSourceLocation() {
    SourceMap sourceMap = V1.getInstance().newInstance();

    String sourceFile = "test/SourceMapTest.java";

    String fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);

    assertEquals(sourceFile, fixedSourceFile);
  }

  @Test
  public void testFixupSourceLocationWithPrefixMapping() {
    SourceMap sourceMap = V1.getInstance().newInstance();

    List<LocationMapping> prefixMappings = ImmutableList.of(
        new LocationMapping("test/", "test-output/")
    );

    sourceMap.setPrefixMappings(prefixMappings);

    String sourceFile = "test/SourceMapTest.java";

    String fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);

    assertEquals("test-output/SourceMapTest.java", fixedSourceFile);
  }

  @Test(expected = IOException.class)
  public void testAddMappingWithNullNode() {
    SourceMap sourceMap = V1.getInstance().newInstance();

    Node nullNode = null;

    FilePosition outputStartPosition = new FilePosition(1, 2);
    FilePosition outputEndPosition = new FilePosition(1, 5);

    sourceMap.addMapping(nullNode, outputStartPosition, outputEndPosition);
  }

  @Test(expected = NullPointerException.class)
  public void testFixupSourceLocationWithNullSourceFile() {
    SourceMap sourceMap = V1.getInstance().newInstance();

    String nullSourceFile = null;

    String fixedSourceFile = sourceMap.fixupSourceLocation(nullSourceFile);
  }

  @Test
  public void testValidate() throws IOException {
    SourceMap sourceMap = V1.getInstance().newInstance();

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    sourceMap.appendTo(outputStream, "sourcemap.js");

    sourceMap.validate(true);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValidateWithInvalidFormat() throws IOException {
    SourceMap sourceMap = new SourceMap(null);

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    sourceMap.appendTo(outputStream, "sourcemap.js");

    sourceMap.validate(true);
  }

  @Test
  public void testSetStartingPosition() {
    SourceMap sourceMap = V1.getInstance().newInstance();

    sourceMap.setStartingPosition(1, 1);
  }

  @Test
  public void testSetWrapperPrefix() {
    SourceMap sourceMap = V1.getInstance().newInstance();

    sourceMap.setWrapperPrefix("test/");
  }

  @Test
  public void testReset() {
    SourceMap sourceMap = V1.getInstance().newInstance();

    sourceMap.reset();
  }

  @Test
  public void testInstanceOfV1() {
    assertTrue(V1.getInstance() instanceof SourceMap);
  }

  @Test
  public void testInstanceOfDefault() {
    assertTrue(DEFAULT.getInstance() instanceof SourceMap);
  }

  @Test
  public void testInstanceOfV2() {
    assertTrue(V2.getInstance() instanceof SourceMap);
  }

  @Test
  public void testInstanceOfV3() {
    assertTrue(V3.getInstance() instanceof SourceMap);
  }

  @Test
  public void testApplyDetailLevelAll() {
    Node node = Node.literal(1);

    boolean actual = ALL.apply(node);

    assertTrue(actual);
  }

  @Test
  public void testApplyDetailLevelSymbols() {
    Node node = Node.literal(1);

    boolean actual = SYMBOLS.apply(node);

    assertFalse(actual);
  }

  @Test
  public void testAppendTo() throws IOException {
    SourceMap sourceMap = V1.getInstance().newInstance();

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    sourceMap.appendTo(outputStream, "sourcemap.js");

    String actualSourceMap = outputStream.toString();

    String expectedSourceMap = "{\"version\":1,\"file\":\"sourcemap.js\",\"sources\":[],\"mappings\":[[]]}";

    assertEquals(expectedSourceMap, actualSourceMap);
  }

  @Test
  public void testGetInstanceWithInvalidFormat() {
    assertNull(SourceMapGeneratorFactory.getInstance(null));
  }

  @Test
  public void testGetInstanceWithUnknownFormat() {
    assertNull(SourceMapGeneratorFactory.getInstance("UNKNOWN"));
  }

  @Test
  public void testGetInstanceWithV1() {
    SourceMapGenerator generator = SourceMapGeneratorV1.newInstance();

    SourceMap sourceMap = new SourceMap(generator);

    assertEquals(V1.class, sourceMap.getClass());
  }

  @Test
  public void testGetInstanceWithDefault() {
    SourceMapGenerator generator = SourceMapGeneratorFactory.getDefaultInstance();

    SourceMap sourceMap = new SourceMap(generator);

    assertEquals(DEFAULT.class, sourceMap.getClass());
  }

  @Test
  public void testGetInstanceWithV2() {
    SourceMapGenerator generator = SourceMapGeneratorV2.newInstance();

    SourceMap sourceMap = new SourceMap(generator);

    assertEquals(V2.class, sourceMap.getClass());
  }

  @Test
  public void testGetInstanceWithV3() {
    SourceMapGenerator generator = SourceMapGeneratorFactory.getInstance(SourceMapFormat.V3);

    SourceMap sourceMap = new SourceMap(generator);

    assertEquals(V3.class, sourceMap.getClass());
  }
}