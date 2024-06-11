import static org.junit.Assert.*;
import static com.google.javascript.jscomp.SourceMap.DetailLevel.*;
import static com.google.javascript.jscomp.SourceMap.Format.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.debugging.sourcemap.SourceMapFormat;
import com.google.debugging.sourcemap.SourcePosition;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.SourceMap;
import com.google.javascript.jscomp.SourceMapGenerator;
import com.google.javascript.jscomp.SourceMapGeneratorFactory;
import com.google.javascript.rhino.NodeFactory;

public class SourceMapTest {

  private NodeFactory nodeFactory;
  private SourceMapGenerator generator;
  private SourceMap sourceMap;

  @Before
  public void setUp() {
    nodeFactory = new NodeFactory();
    generator = SourceMapGeneratorFactory.getInstance(SourceMapFormat.V1);
    sourceMap = new SourceMap(generator);
  }

  @Test
  public void testConstructor() {
    SourceMap sourceMap = new SourceMap(generator);
    assertNotNull(sourceMap);
  }

  @Test
  public void testAddMapping() {
    Node node = nodeFactory.createCall("myFunction", null);
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(2, 2);
    String sourceFile = "input.js";

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    String fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);
    assertEquals("input.js", fixedSourceFile);

    SourcePosition expectedPosition = new SourcePosition("input.js", 1, 1);
    SourcePosition actualPosition = sourceMap.generator.getMappingForPosition(
        expectedPosition);
    SourcePosition resultPosition = node.getSourcePosition();
    assertEquals(resultPosition, actualPosition);
  }

  @Test
  public void testFixupSourceLocationWithPrefixMappings() {
    List<LocationMapping> prefixMappings = ImmutableList.of(
        new LocationMapping("input/", "output/")
    );
    sourceMap.setPrefixMappings(prefixMappings);

    String sourceFile = "input/myFile.js";
    String fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);
    assertEquals("output/myFile.js", fixedSourceFile);
  }

  @Test
  public void testAppendTo() throws IOException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    sourceMap.appendTo(outputStream, "test.js");

    String expectedSourceMap = "sourceMappingURL=data:application/json;base64,...";
    String actualSourceMap = new String(outputStream.toByteArray());
    assertTrue(actualSourceMap.startsWith(expectedSourceMap));
  }

  @Test
  public void testSetStartingPosition() {
    sourceMap.setStartingPosition(1, 1);

    Node node = nodeFactory.createIntLiteral(10);
    FilePosition outputStartPosition = new FilePosition(1, 2);
    FilePosition outputEndPosition = new FilePosition(2, 3);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    SourcePosition expectedPosition = new SourcePosition("test.js", 1, 1);
    SourcePosition actualPosition = sourceMap.generator.getMappingForPosition(
        expectedPosition);
    SourcePosition resultPosition = node.getSourcePosition();
    assertEquals(resultPosition, actualPosition);
  }

  @Test
  public void testValidate() {
    sourceMap.validate(true);
  }

  @Test
  public void testReset() {
    sourceMap.addMapping(nodeFactory.createCall("myFunction", null), null, null);
    sourceMap.reset();
    assertTrue(sourceMap.generator.getMappings().isEmpty());
  }

  @Test
  public void testSetWrapperPrefix() {
    sourceMap.setWrapperPrefix("wrapper_");

    Node node = nodeFactory.createCall("myFunction", null);
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(2, 2);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    String fixedSourceFile = sourceMap.fixupSourceLocation("wrapper_input.js");
    assertEquals("input.js", fixedSourceFile);
  }

  @Test
  public void testSetDetailLevel() {
    Node node = nodeFactory.createCall("myFunction", null);
    sourceMap.setDetailLevel(ALL);
    sourceMap.addMapping(node, null, null);

    Node node2 = nodeFactory.createStringLiteral("str");
    sourceMap.setDetailLevel(SYMBOLS);
    sourceMap.addMapping(node2, null, null);

    Predicate<Node> detailLevel = sourceMap.getDetailLevel();
    assertTrue(detailLevel.apply(node));
    assertTrue(detailLevel.apply(node2));
    assertFalse(detailLevel.apply(nodeFactory.createIntLiteral(10)));
  }
}