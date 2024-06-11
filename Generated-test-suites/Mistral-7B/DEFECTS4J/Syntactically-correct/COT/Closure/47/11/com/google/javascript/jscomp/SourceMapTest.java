import static org.junit.Assert.*;
import static com.google.javascript.jscomp.SourceMap.DetailLevel.*;
import static com.google.javascript.jscomp.SourceMap.Format.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class SourceMapTest {

  @Test
  public void testGetInstance() {
    for (Format format : Format.values()) {
      SourceMap sourceMap = format.getInstance();
      assertNotNull(sourceMap);
    }
  }

  @Test
  public void testAddMapping() throws IOException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    SourceMap sourceMap = V1.getInstance();

    List<LocationMapping> prefixMappings = Lists.newArrayList(
        new LocationMapping("oldPath/", "newPath/"));

    sourceMap.setPrefixMappings(prefixMappings);
    sourceMap.setStartingPosition(1, 1);

    Node node = Node.newText("test", "test.js", 1, 1);
    FilePosition outputStartPosition = new FilePosition(2, 2);
    FilePosition outputEndPosition = new FilePosition(2, 3);
    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    String sourceCode = "function test() {}\n";
    sourceMap.appendTo(outputStream, "test.js");
    sourceMap.validate(true);

    String expectedSourceMap = "{\"version\":1,\"file\":\"test.js\",\"sources\":[\"test.js\"],\"names\":[],\"mappings\":[";
    expectedSourceMap += "{\"original\":{\"source\":\"test.js\",\"line\":1,\"column\":1},";
    expectedSourceMap += "\"generated\":{\"source\":\"test.js\",\"line\":2,\"column\":2}}";
    expectedSourceMap += "]}\n";

    String actualSourceMap = new String(outputStream.toByteArray());
    assertEquals(expectedSourceMap, actualSourceMap);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddMappingNullNode() {
    SourceMap sourceMap = V1.getInstance();
    Node node = null;

    sourceMap.addMapping(node, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddMappingNullOutputPosition() {
    SourceMap sourceMap = V1.getInstance();
    Node node = new Node("test");
    FilePosition outputPosition = null;

    sourceMap.addMapping(node, null, outputPosition);
  }

  @Test
  public void testFixupSourceLocation() {
    String sourceFile = "oldPath/test.js";
    String expectedFixedFile = "newPath/test.js";

    SourceMap sourceMap = V1.getInstance();
    List<LocationMapping> prefixMappings = Lists.newArrayList(
        new LocationMapping(sourceFile.substring(0, 7), expectedFixedFile));
    sourceMap.setPrefixMappings(prefixMappings);

    String fixedFile = sourceMap.fixupSourceLocation(sourceFile);
    assertEquals(expectedFixedFile, fixedFile);
  }

  @Test
  public void testFixupSourceLocationEmptyPrefixMappings() {
    SourceMap sourceMap = V1.getInstance();

    String fixedFile = sourceMap.fixupSourceLocation("test.js");
    assertEquals("test.js", fixedFile);
  }

  @Test
  public void testValidate() throws IOException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    SourceMap sourceMap = V1.getInstance();

    sourceMap.appendTo(outputStream, "test.js");
    sourceMap.validate(true);
  }

  @Test(expected = IOException.class)
  public void testValidateNullOutputStream() throws IOException {
    SourceMap sourceMap = V1.getInstance();

    sourceMap.validate(null);
  }

  @Test
  public void testReset() throws IOException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    SourceMap sourceMap = V1.getInstance();

    sourceMap.appendTo(outputStream, "test.js");
    sourceMap.reset();

    String expectedSourceMap = "";
    String actualSourceMap = new String(outputStream.toByteArray());
    assertEquals(expectedSourceMap, actualSourceMap);
  }

  @Test
  public void testSetWrapperPrefix() throws IOException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    SourceMap sourceMap = V1.getInstance();

    String prefix = "wrapper_";
    sourceMap.setWrapperPrefix(prefix);
    sourceMap.appendTo(outputStream, "test.js");

    String expectedSourceMap = prefix + "test.js\n";
    String actualSourceMap = new String(outputStream.toByteArray());
    assertEquals(expectedSourceMap, actualSourceMap);
  }

  @Test
  public void testSetStartingPosition() throws IOException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    SourceMap sourceMap = V1.getInstance();

    sourceMap.setStartingPosition(1, 1);
    sourceMap.appendTo(outputStream, "test.js");

    String expectedSourceMap = "{\"version\":1,\"file\":\"test.js\",\"sources\":[\"test.js\"],\"names\":[],\"mappings\":[";
    expectedSourceMap += "{\"original\":{\"source\":\"test.js\",\"line\":1,\"column\":1},";
    expectedSourceMap += "\"generated\":{\"source\":\"test.js\",\"line\":1,\"column\":1}}";
    expectedSourceMap += "]}\n";

    String actualSourceMap = new String(outputStream.toByteArray());
    assertEquals(expectedSourceMap, actualSourceMap);
  }

  @Test
  public void testSetDetailLevel() throws IOException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    SourceMap sourceMap = V1.getInstance();

    sourceMap.setDetailLevel(ALL);
    sourceMap.appendTo(outputStream, "test.js");

    String expectedSourceMap = "{\"version\":1,\"file\":\"test.js\",\"sources\":[\"test.js\"],\"names\":[],\"mappings\":[";
    expectedSourceMap += "{\"original\":{\"source\":\"test.js\",\"line\":1,\"column\":1},";
    expectedSourceMap += "\"generated\":{\"source\":\"test.js\",\"line\":1,\"column\":1}}";
    expectedSourceMap += "]}\n";

    String actualSourceMap = new String(outputStream.toByteArray());
    assertEquals(expectedSourceMap, actualSourceMap);
  }

  @Test
  public void testSetDetailLevelSymbols() throws IOException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    SourceMap sourceMap = V1.getInstance();

    sourceMap.setDetailLevel(SYMBOLS);
    Node node = Node.newText("test", "test.js", 1, 1);
    FilePosition outputStartPosition = new FilePosition(2, 2);
    FilePosition outputEndPosition = new FilePosition(2, 3);
    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
    sourceMap.appendTo(outputStream, "test.js");

    String expectedSourceMap = "{\"version\":1,\"file\":\"test.js\",\"sources\":[\"test.js\"],\"names\":[],\"mappings\":[";
    expectedSourceMap += "{\"original\":{\"source\":\"test.js\",\"line\":1,\"column\":1},";
    expectedSourceMap += "\"generated\":{\"source\":\"test.js\",\"line\":2,\"column\":2}}";
    expectedSourceMap += "]}\n";

    String actualSourceMap = new String(outputStream.toByteArray());
    assertEquals(expectedSourceMap, actualSourceMap);
  }

  @Test
  public void testSetPrefixMappings() throws IOException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    SourceMap sourceMap = V1.getInstance();

    List<LocationMapping> prefixMappings = Lists.newArrayList(
        new LocationMapping("oldPath/", "newPath/"),
        new LocationMapping("oldPath2/", "newPath2/"));
    sourceMap.setPrefixMappings(prefixMappings);

    String expectedSourceMap = "{\"version\":1,\"file\":\"test.js\",\"sources\":[\"newPath/test.js\",\"newPath2/test2.js\"],\"names\":[],\"mappings\":[";

    sourceMap.appendTo(outputStream, "test.js");
    sourceMap.addMapping(Node.newText("test", "test.js", 1, 1), null, null);
    sourceMap.appendTo(outputStream, "test.js");

    expectedSourceMap += "{\"original\":{\"source\":\"test.js\",\"line\":1,\"column\":1},";
    expectedSourceMap += "\"generated\":{\"source\":\"newPath/test.js\",\"line\":1,\"column\":1}}";

    sourceMap.appendTo(outputStream, "test2.js");
    sourceMap.addMapping(Node.newText("test2", "test2.js", 1, 1), null, null);
    sourceMap.appendTo(outputStream, "test2.js");

    expectedSourceMap += ",\"original\":{\"source\":\"test2.js\",\"line\":1,\"column\":1},";
    expectedSourceMap += "\"generated\":{\"source\":\"newPath2/test2.js\",\"line\":1,\"column\":1}}";

    expectedSourceMap += "]}\n";

    String actualSourceMap = new String(outputStream.toByteArray());
    assertEquals(expectedSourceMap, actualSourceMap);
  }
}