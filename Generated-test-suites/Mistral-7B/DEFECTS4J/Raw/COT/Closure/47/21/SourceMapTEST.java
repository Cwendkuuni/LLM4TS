import static org.junit.Assert.*;
import static com.google.javascript.jscomp.SourceMap.DetailLevel.*;
import static com.google.javascript.jscomp.SourceMap.Format.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.common.io.CharStreams;
import com.google.debugging.sourcemap.SourceMapFormat;
import com.google.debugging.sourcemap.SourceMapGenerator;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.SourceMap;
import com.google.javascript.rhino.NodeFactory;

public class SourceMapTest {

  private SourceMap sourceMap;
  private NodeFactory nodeFactory;

  @Before
  public void setUp() {
    nodeFactory = new NodeFactory();
    sourceMap = V1.getInstance();
  }

  @Test
  public void testAddMapping() {
    String sourceFile = "test.js";
    Node node = nodeFactory.newTextNode("test", null, null);
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 5);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
    String fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);
    assertEquals("test.js", fixedSourceFile);
  }

  @Test
  public void testFixupSourceLocationWithPrefixMapping() {
    String sourceFile = "/path/to/test.js";
    List<SourceMap.LocationMapping> prefixMappings =
        ImmutableList.of(new SourceMap.LocationMapping("/path", ""));
    sourceMap.setPrefixMappings(prefixMappings);

    String fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);
    assertEquals("", fixedSourceFile);
  }

  @Test
  public void testAppendTo() throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    String name = "test.js.map";
    sourceMap.appendTo(out, name);
    String expectedSourceMap = getExpectedSourceMapV1();
    assertEquals(expectedSourceMap, CharStreams.toString(out.toString()));
  }

  @Test
  public void testReset() throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    String name = "test.js.map";
    sourceMap.appendTo(out, name);

    sourceMap.reset();

    sourceMap.appendTo(out, name);
    String expectedSourceMap = getExpectedSourceMapV1();
    assertEquals(expectedSourceMap, CharStreams.toString(out.toString()));
  }

  @Test
  public void testValidate() {
    try {
      sourceMap.validate(true);
    } catch (IOException e) {
      fail("Validation should not throw an exception");
    }
  }

  @Test(expected = IOException.class)
  public void testInvalidValidate() throws IOException {
    sourceMap.validate(false);
  }

  @Test
  public void testSetStartingPosition() {
    sourceMap.setStartingPosition(1, 1);
    FilePosition expectedPosition = new FilePosition(1, 1);
    FilePosition actualPosition = sourceMap.getStartingPosition();
    assertEquals(expectedPosition, actualPosition);
  }

  @Test
  public void testSetWrapperPrefix() {
    sourceMap.setWrapperPrefix("wrapper_");
    String wrapperPrefix = sourceMap.getWrapperPrefix();
    assertEquals("wrapper_", wrapperPrefix);
  }

  private String getExpectedSourceMapV1() {
    return "{\n" +
        "  \"version\":1,\n" +
        "  \"file\":\"test.js\",\n" +
        "  \"sources\":[\"test.js\"],\n" +
        "  \"sourcesContent\":{}, \n" +
        "  \"mappings\":\"..." + // Ellipsis represents the rest of the mappings
        "}\n";
  }

  @Test
  public void testGetInstanceV1() {
    SourceMap sm = V1.getInstance();
    assertTrue(sm instanceof SourceMapV1);
  }

  @Test
  public void testGetInstanceDefault() {
    SourceMap sm = DEFAULT.getInstance();
    assertTrue(sm instanceof SourceMap);
  }

  @Test
  public void testGetInstanceV2() {
    SourceMap sm = V2.getInstance();
    assertTrue(sm instanceof SourceMapV2);
  }

  @Test
  public void testGetInstanceV3() {
    SourceMap sm = V3.getInstance();
    assertTrue(sm instanceof SourceMap);
  }

  @Test
  public void testApplyDetailLevelSymbols() {
    Node node = nodeFactory.newTextNode("test", null, null);
    DetailLevel detailLevel = SYMBOLS;
    boolean actual = detailLevel.apply(node);
    assertTrue(actual);
  }

  @Test
  public void testApplyDetailLevelAll() {
    Node node = nodeFactory.newTextNode("test", null, null);
    DetailLevel detailLevel = ALL;
    boolean actual = detailLevel.apply(node);
    assertTrue(actual);
  }

  @Test
  public void testAddMappingWithDetailLevelSymbols() throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    String name = "test.js.map";

    SourceMap sourceMap = new SourceMap(new SourceMapGeneratorFactory() {
      @Override
      public SourceMapGenerator getGenerator() {
        return new SourceMapGeneratorV2();
      }

      @Override
      public SourceMapGeneratorV1 getGeneratorV1() {
        return null;
      }

      @Override
      public SourceMapGeneratorV2 getGeneratorV2() {
        return new SourceMapGeneratorV2() {
          @Override
          public void addMapping(
              Function<String, String> sourceFileFunction,
              String originalName, FilePosition jsSourceMapPosition,
              FilePosition sourcePosition) {
            if (DetailLevel.SYMBOLS.apply(jsSourceMapPosition.getNode())) {
              // Add mapping only when the detail level is SYMBOLS
              super.addMapping(sourceFileFunction, originalName, jsSourceMapPosition, sourcePosition);
            }
          }
        };
      }
    });

    Node node = nodeFactory.newTextNode("test", null, null);
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 5);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
    String expectedSourceMap = getExpectedSourceMapV2();
    assertEquals(expectedSourceMap, CharStreams.toString(out.toString()));
  }

  private String getExpectedSourceMapV2() {
    return "{\n" +
        "  \"version\":2,\n" +
        "  \"file\":\"test.js\",\n" +
        "  \"sources\":[\"test.js\"],\n" +
        "  \"names\":[],\n" +
        "  \"mappings\":\"....\",\n" +
        "  \"sourceRoot\":\"\"\n" +
        "}\n";
  }

  @Test
  public void testAddMappingWithDetailLevelAll() throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    String name = "test.js.map";

    SourceMap sourceMap = new SourceMap(new SourceMapGeneratorFactory() {
      @Override
      public SourceMapGenerator getGenerator() {
        return new SourceMapGeneratorV2();
      }

      @Override
      public SourceMapGeneratorV1 getGeneratorV1() {
        return null;
      }

      @Override
      public SourceMapGeneratorV2 getGeneratorV2() {
        return new SourceMapGeneratorV2() {
          @Override
          public void addMapping(
              Function<String, String> sourceFileFunction,
              String originalName, FilePosition jsSourceMapPosition,
              FilePosition sourcePosition) {
            if (DetailLevel.ALL.apply(jsSourceMapPosition.getNode())) {
              super.addMapping(sourceFileFunction, originalName, jsSourceMapPosition, sourcePosition);
            }
          }
        };
      }
    });

    Node node = nodeFactory.newTextNode("test", null, null);
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 5);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
    String expectedSourceMap = getExpectedSourceMapV2WithAllMappings();
    assertEquals(expectedSourceMap, CharStreams.toString(out.toString()));
  }

  private String getExpectedSourceMapV2WithAllMappings() {
    return "{\n" +
        "  \"version\":2,\n" +
        "  \"file\":\"test.js\",\n" +
        "  \"sources\":[\"test.js\"],\n" +
        "  \"names\":[],\n" +
        "  \"mappings\":\"...\",\n" + // Ellipsis represents the rest of the mappings
        "  \"sourceRoot\":\"\"\n" +
        "}\n";
  }
}