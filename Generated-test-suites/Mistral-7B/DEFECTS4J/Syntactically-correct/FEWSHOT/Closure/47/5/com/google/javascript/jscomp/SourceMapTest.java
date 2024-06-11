package com.google.javascript.jscomp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.debugging.sourcemap.SourceMap;
import com.google.debugging.sourcemap.SourceMapFormat;
import com.google.debugging.sourcemap.SourceMapGenerator;
import com.google.debugging.sourcemap.SourceMapGeneratorFactory;
import com.google.javascript.rhino.Node;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Test class for SourceMap.
 */
public class SourceMapTest {

  @Test
  public void testAddMapping() throws IOException {
    String sourceFile = "test.js";
    String renamedFile = "renamedTest.js";
    SourceMap sourceMap = new SourceMap(SourceMap.Format.V1.getInstance()).setPrefixMappings(getPrefixMappings());

    Node node = new Node();
    node.setSourceFileName(sourceFile);
    node.setLineno(1);
    node.setProp(Node.ORIGINALNAME_PROP, "originalName");

    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 10);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    sourceMap.appendTo(out, "sourceMap.js.map");

    String mapContent = out.toString();
    int index = mapContent.indexOf(sourceFile);
    int renamedIndex = mapContent.indexOf(renamedFile);

    assertTrue("Source file not found in sourcemap", index != -1);
    assertTrue("Renamed source file not found in sourcemap", renamedIndex != -1);
  }

  @Test
  public void testFixupSourceLocation() {
    String sourceFile = "test.js";
    String prefix = "test/";
    SourceMap sourceMap = new SourceMap(SourceMap.Format.V1.getInstance()).setPrefixMappings(getPrefixMappings());

    String fixed = sourceMap.fixupSourceLocation(sourceFile);

    assertTrue("Prefix not added to source file", fixed.startsWith(prefix));
    assertEquals("test.js", fixed);
  }

  @Test
  public void testValidate() throws IOException {
    SourceMap sourceMap = new SourceMap(SourceMap.Format.V1.getInstance());

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    sourceMap.appendTo(out, "sourceMap.js.map");

    sourceMap.validate(true);
  }

  @Test(expected = AssertionError.class)
  public void testValidateFailure() throws IOException {
    SourceMap sourceMap = new SourceMap(SourceMap.Format.V1.getInstance());

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    sourceMap.appendTo(out, "sourceMap.js.map");

    sourceMap.validate(false);
  }

  @Test
  public void testReset() {
    SourceMap sourceMap = new SourceMap(SourceMap.Format.V1.getInstance());

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    sourceMap.appendTo(out, "sourceMap.js.map");

    sourceMap.reset();

    ByteArrayOutputStream out2 = new ByteArrayOutputStream();
    sourceMap.appendTo(out2, "sourceMap.js.map");

    assertNotNull("Sourcemap not reset", out2.toString().isEmpty());
  }

  private List<SourceMap.LocationMapping> getPrefixMappings() {
    return Lists.newArrayList(
        new SourceMap.LocationMapping("test/", "test-renamed/"),
        new SourceMap.LocationMapping("lib/", "lib-renamed/")
    );
  }
}