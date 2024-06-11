import static org.junit.Assert.*;
import static com.google.javascript.jscomp.SourceMap.DetailLevel.*;
import static com.google.javascript.jscomp.SourceMap.Format.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.debugging.sourcemap.FilePosition;
import com.google.debugging.sourcemap.SourceMapFormat;
import com.google.debugging.sourcemap.SourceMapGenerator;
import com.google.debugging.sourcemap.SourceMapGeneratorFactory;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class SourceMapTest {

  @org.junit.Test
  public void testAddMapping() throws IOException {
    SourceMap sourceMap = Format.V1.getInstance().newInstance();
    Node node = new Node("testNode");
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 2);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    sourceMap.appendTo(out, "test.js");
    String sourceMapText = out.toString();

    int indexOfSourceMapping = sourceMapText.indexOf("//# sourceMappingURL=data:application/json;base64,");
    String base64EncodedSourceMap = sourceMapText.substring(indexOfSourceMapping);
    String decodedSourceMap = new String(Base64.getDecoder().decode(base64EncodedSourceMap));

    Map<String, Object> parsedSourceMap = Parser.parseMap(decodedSourceMap);

    List<Map<String, Object>> sources = (List<Map<String, Object>>) parsedSourceMap.get("sources");
    assertEquals(1, sources.size());
    Map<String, Object> source = sources.get(0);
    String sourceFile = (String) source.get("file");
    List<Map<String, Object>> sourcesContent = (List<Map<String, Object>>) source.get("sourceContent");

    Map<String, Object> content = sourcesContent.get(0);
    List<Map<String, Object>> sourcesRoot = (List<Map<String, Object>>) content.get("sourcesRoot");
    String expectedSourceMapContent = "{\"mappings\":[{\"original\":{\"source\":\"test.js\"," +
        "\"line\":1,\"column\":1}," +
        "\"generated\":{\"line\":1,\"column\":1}}]}";
    assertEquals(expectedSourceMapContent, content.get("content").toString());
  }

  @org.junit.Test
  public void testAddMappingWithPrefixMapping() throws IOException {
    String prefix = "test/prefix_";

    SourceMap sourceMap = Format.V1.getInstance().newInstance();
    SourceMap.LocationMapping locationMapping = new SourceMap.LocationMapping(prefix, "prefix_");

    SourceMap.setPrefixMappings(Lists.newArrayList(locationMapping));

    Node node = new Node("testNode");
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 2);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    sourceMap.appendTo(out, "test.js");
    String sourceMapText = out.toString();

    int indexOfSourceMapping = sourceMapText.indexOf("//# sourceMappingURL=data:application/json;base64,");
    String base64EncodedSourceMap = sourceMapText.substring(indexOfSourceMapping);
    String decodedSourceMap = new String(Base64.getDecoder().decode(base64EncodedSourceMap));

    Map<String, Object> parsedSourceMap = Parser.parseMap(decodedSourceMap);

    List<Map<String, Object>> sources = (List<Map<String, Object>>) parsedSourceMap.get("sources");
    assertEquals(1, sources.size());
    Map<String, Object> source = sources.get(0);
    String sourceFile = (String) source.get("file");

    assertTrue(sourceFile.startsWith(prefix));
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void testAddMappingNullNode() throws IOException {
    SourceMap sourceMap = Format.V1.getInstance().newInstance();
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 2);

    sourceMap.addMapping(null, outputStartPosition, outputEndPosition);
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void testAddMappingNullOutputStartPosition() throws IOException {
    SourceMap sourceMap = Format.V1.getInstance().newInstance();
    Node node = new Node("testNode");
    FilePosition outputEndPosition = new FilePosition(1, 2);

    sourceMap.addMapping(node, null, outputEndPosition);
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void testAddMappingNullOutputEndPosition() throws IOException {
    SourceMap sourceMap = Format.V1.getInstance().newInstance();
    Node node = new Node("testNode");
    FilePosition outputStartPosition = new FilePosition(1, 1);

    sourceMap.addMapping(node, outputStartPosition, null);
  }

  @org.junit.Test
  public void testReset() throws IOException {
    SourceMap sourceMap = Format.V1.getInstance().newInstance();
    Node node = new Node("testNode");
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 2);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    sourceMap.appendTo(out, "test.js");

    sourceMap.reset();

    ByteArrayOutputStream out2 = new ByteArrayOutputStream();
    sourceMap.appendTo(out2, "test.js");

    assertEquals(0, out2.size());
  }

  @org.junit.Test
  public void testSetWrapperPrefix() throws IOException {
    String prefix = "test_prefix_";

    SourceMap sourceMap = Format.V1.getInstance().newInstance();

    sourceMap.setWrapperPrefix(prefix);

    Node node = new Node("testNode");
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 2);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    sourceMap.appendTo(out, "test.js");
    String sourceMapText = out.toString();

    int indexOfSourceMapping = sourceMapText.indexOf("//# sourceMappingURL");
    String base64EncodedSourceMap = sourceMapText.substring(indexOfSourceMapping);
    String decodedSourceMap = new String(Base64.getDecoder().decode(base64EncodedSourceMap));

    Map<String, Object> parsedSourceMap = Parser.parseMap(decodedSourceMap);

    List<Map<String, Object>> sources = (List<Map<String, Object>>) parsedSourceMap.get("sources");
    assertEquals(1, sources.size());
    Map<String, Object> source = sources.get(0);
    String sourceFile = (String) source.get("file");

    assertTrue(sourceFile.startsWith(prefix));
  }

  @org.junit.Test
  public void testAppendTo() throws IOException {
    SourceMap sourceMap = Format.V1.getInstance().newInstance();
    Node node = new Node("testNode");
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 2);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    sourceMap.appendTo(out, "test.js");

    String expectedOutput = "//# sourceMappingURL=data:application/json;base64,...";
    assertEquals(expectedOutput, out.toString().substring(0, expectedOutput.length()));
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void testAppendToNullOutput() throws IOException {
    SourceMap sourceMap = Format.V1.getInstance().newInstance();
    Node node = new Node("testNode");
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 2);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    Appendable out = null;
    sourceMap.appendTo(out, "test.js");
  }

  @org.junit.Test
  public void testValidateTrue() throws IOException {
    SourceMap sourceMap = Format.V1.getInstance().newInstance();
    Node node = new Node("testNode");
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 2);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    sourceMap.appendTo(out, "test.js");

    sourceMap.validate(true);
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void testValidateFalse() throws IOException {
    SourceMap sourceMap = Format.V1.getInstance().newInstance();
    Node node = new Node("testNode");
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 2);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    sourceMap.appendTo(out, "test.js");

    sourceMap.validate(false);
  }

  @org.junit.Test(expected = IOException.class)
  public void testAppendToWithInvalidDetailLevel() throws IOException {
    SourceMap sourceMap = Format.V1.getInstance().newInstance();
    Node node = new Node("testNode");
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 2);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    Appendable out = new StringWriter();
    sourceMap.setDetailLevel(new DetailLevel() {
      @Override public boolean apply(Node node) {
        throw new UnsupportedOperationException();
      }
    });

    sourceMap.appendTo(out, "test.js");
  }

  @org.junit.Test
  public void testSetDetailLevel() throws IOException {
    SourceMap sourceMap = Format.V1.getInstance().newInstance();

    sourceMap.setDetailLevel(ALL);

    Node node = new Node("testNode");
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 2);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    sourceMap.appendTo(out, "test.js");
  }

  @org.junit.Test
  public void testSetDetailLevelSymbols() throws IOException {
    SourceMap sourceMap = Format.V1.getInstance().newInstance();

    sourceMap.setDetailLevel(SYMBOLS);

    Node node = new Node("testNode");
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 2);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    sourceMap.appendTo(out, "test.js");
  }
}