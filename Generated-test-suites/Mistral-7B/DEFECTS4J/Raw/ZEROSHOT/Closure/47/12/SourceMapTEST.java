import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.debugging.sourcemap.FilePosition;
import com.google.debugging.sourcemap.SourceMapFormat;
import com.google.debugging.sourcemap.SourceMapGenerator;
import com.google.debugging.sourcemap.SourceMapGeneratorFactory;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.SourceMap;
import com.google.javascript.jscomp.SourceMap.Format;
import com.google.javascript.jscomp.SourceMap.DetailLevel;
import com.google.javascript.rhino.NodeFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;

/**
 * Tests for the SourceMap class.
 */
public class SourceMapTest {

  private NodeFactory nodeFactory;
  private SourceMapGeneratorFactory generatorFactory;
  private SourceMap sourceMap;
  private Format format;

  @Before
  public void setUp() {
    nodeFactory = NodeFactory.getPrototypeTable();
    generatorFactory = mock(SourceMapGeneratorFactory.class);
    format = Format.DEFAULT;
    sourceMap = format.getInstance().newInstance();
  }

  @Test
  public void testAddMapping() {
    String sourceFile = "test.js";
    Node node = nodeFactory.createCall(nodeFactory.createIdentifier("console"),
        nodeFactory.createIdentifier("log"), Lists.newArrayList("msg"));
    node.setSourceFileName(sourceFile);
    node.setLineno(1);
    node.setCharno(10);

    FilePosition outputStartPosition = new FilePosition(1, 15);
    FilePosition outputEndPosition = new FilePosition(1, 20);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    SourceMapGenerator generator = when(generatorFactory.getInstance(format))
        .thenReturn(mock(SourceMapGenerator.class));
    verify(generator).addMapping(sourceFile, null, new FilePosition(1, 1),
        outputStartPosition, outputEndPosition);
  }

  @Test
  public void testFixupSourceLocation() {
    String sourceFile = "test.js";
    String prefix = "test_prefix/";

    sourceMap.setPrefixMappings(Lists.newArrayList(new SourceMapTest.LocationMapping(
        prefix, "test.js")));

    String fixed = sourceMap.fixupSourceLocation(sourceFile);

    assertNotNull(fixed);
    assertEquals("test.js", fixed);

    String fixedWithPrefix = sourceMap.fixupSourceLocation(prefix + sourceFile);

    assertNotNull(fixedWithPrefix);
    assertEquals("test.js", fixedWithPrefix);
  }

  @Test
  public void testAppendTo() throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(out);

    sourceMap.appendTo(printStream, "test.js_map");

    String expectedSourceMap = "{\n" +
        "  \"sources\": [\n" +
        "    \"test.js\"\n" +
        "  ],\n" +
        "  \"file\": \"test.js_map\",\n" +
        "  \"version\": 3\n" +
        "}";

    assertEquals(expectedSourceMap, out.toString());
  }

  @Test
  public void testValidate() {
    sourceMap.validate(true);
  }

  @After
  public void tearDown() {
    sourceMap = null;
  }

  static class LocationMapping {
    final String prefix;
    LocationMapping(String prefix) {
      this.prefix = prefix;
    }
  }
}