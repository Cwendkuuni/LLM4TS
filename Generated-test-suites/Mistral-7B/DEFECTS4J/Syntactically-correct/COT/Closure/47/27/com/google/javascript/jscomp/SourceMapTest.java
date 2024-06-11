import static org.junit.Assert.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.SourceMap;
import com.google.javascript.jscomp.SourceMapFormat;
import com.google.javascript.jscomp.SourceMapGenerator;
import com.google.javascript.jscomp.SourceMapGeneratorFactory;
import com.google.javascript.rhino.NodeFactory;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SourceMapTest {

  @Test
  public void testFormatV1() throws IOException {
    SourceMap sourceMap = SourceMap.Format.V1.getInstance().getInstance();
    testSourceMap(sourceMap);
  }

  @Test
  public void testFormatDefault() throws IOException {
    SourceMap sourceMap = SourceMap.Format.DEFAULT.getInstance().getInstance();
    testSourceMap(sourceMap);
  }

  @Test
  public void testFormatV2() throws IOException {
    SourceMap sourceMap = SourceMap.Format.V2.getInstance().getInstance();
    testSourceMap(sourceMap);
  }

  @Test
  public void testFormatV3() throws IOException {
    SourceMap sourceMap = SourceMap.Format.V3.getInstance().getInstance();
    testSourceMap(sourceMap);
  }

  @Test
  public void testAddMapping() throws IOException {
    SourceMap sourceMap = new SourceMap(new SourceMapGeneratorFactory()
        .getInstance(SourceMapFormat.V1));

    Node node = NodeFactory.createText("test", null, null);
    node.setSourceFileName("TestFile.java");
    node.setLineno(1);

    FilePosition outputStartPosition = new FilePosition(1, 0);
    FilePosition outputEndPosition = new FilePosition(1, 5);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    sourceMap.appendTo(out, "SourceMapTest.java");

    String result = out.toString();
    String expected = "{\"version\":1,\"file\":\"TestFile.java\",\"sources\":[],\"mappings\":[";
    assertTrue(result.startsWith(expected));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddMappingNullNode() throws IOException {
    SourceMap sourceMap = new SourceMap(new SourceMapGeneratorFactory()
        .getInstance(SourceMapFormat.V1));

    sourceMap.addMapping(null, new FilePosition(1, 0), new FilePosition(1, 5));
  }

  @Test(expected = IOException.class)
  public void testAddMappingNullOutput() throws IOException {
    SourceMap sourceMap = new SourceMap(new SourceMapGeneratorFactory()
        .getInstance(SourceMapFormat.V1));

    Node node = NodeFactory.createText("test", null, null);
    node.setSourceFileName("TestFile.java");
    node.setLineno(1);

    sourceMap.addMapping(node, null, new FilePosition(1, 5));
  }

  @Test(expected = IOException.class)
  public void testAddMappingNullOutputStart() throws IOException {
    SourceMap sourceMap = new SourceMap(new SourceMapGeneratorFactory()
        .getInstance(SourceMapFormat.V1));

    Node node = NodeFactory.createText("test", null, null);
    node.setSourceFileName("TestFile.java");
    node.setLineno(1);

    sourceMap.addMapping(node, new FilePosition(1, 0), null);
  }

  @Test(expected = IOException.class)
  public void testAddMappingNullOutputEnd() throws IOException {
    SourceMap sourceMap = new SourceMap(new SourceMapGeneratorFactory()
        .getInstance(SourceMapFormat.V1));

    Node node = NodeFactory.createText("test", null, null);
    node.setSourceFileName("TestFile.java");
    node.setLineno(1);

    sourceMap.addMapping(node, new FilePosition(1, 0), new FilePosition(1, -1));
  }

  @Test
  public void testSetPrefixMappings() throws IOException {
    SourceMap sourceMap = new SourceMap(new SourceMapGeneratorFactory()
        .getInstance(SourceMapFormat.V1));

    List<LocationMapping> prefixMappings = Lists.newArrayList();
    prefixMappings.add(new LocationMapping("foo/", "bar/"));

    sourceMap.setPrefixMappings(prefixMappings);

    Node node = NodeFactory.createText("test", null, null);
    node.setSourceFileName("foo/TestFile.java");
    node.setLineno(1);

    FilePosition outputStartPosition = new FilePosition(1, 0);
    FilePosition outputEndPosition = new FilePosition(1, 5);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    sourceMap.appendTo(out, "SourceMapTest.java");

    String result = out.toString();
    String expected = "{\"version\":1,\"file\":\"bar/TestFile.java\",\"sources\":[],\"mappings\":[";
    assertTrue(result.startsWith(expected));
  }

  private void testSourceMap(SourceMap sourceMap) throws IOException {
    Node node = NodeFactory.createText("test", null, null);
    node.setSourceFileName("TestFile.java");
    node.setLineno(1);

    FilePosition outputStartPosition = new FilePosition(1, 0);
    FilePosition outputEndPosition = new FilePosition(1, 5);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    sourceMap.appendTo(out, "SourceMapTest.java");

    String result = out.toString();
    String expected = generateExpectedSourceMap("SourceMapTest.java");

    assertEquals(expected, result);

    sourceMap.reset();
  }

  private String generateExpectedSourceMap(String fileName) {
    String format = SourceMapFormat.V1.getName();
    String version = "1";
    String file = fileName;
    String sources = "[]";

    String mappings = "[]";

    if (format.equals(SourceMapFormat.V2.getName())) {
      version = "2";
      mappings = "[]";
    } else if (format.equals(SourceMapFormat.V3.getName())) {
      version = "3";
      mappings = "[]";
    }

    return String.format(
        "{%s:%s,%s:%s,%s:%s,%s:%s,%s:%s}",
        "version", version,
        "file", file,
        "sources", sources,
        "mappings", mappings,
        "names", "[]",
        "sourcesContent", "{}"
    );
  }
}