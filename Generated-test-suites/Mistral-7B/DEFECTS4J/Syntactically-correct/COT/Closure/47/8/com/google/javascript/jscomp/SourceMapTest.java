import static org.junit.Assert.*;
import com.google.javascript.jscomp.SourceMap.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SourceMapTest {

  @SuppressWarnings("unchecked")
  private static <T> List<T> listOf(T... elements) {
    return Lists.newArrayList(Arrays.asList(elements));
  }

  @SuppressWarnings("unchecked")
  private static <K, V> Map<K, V> mapOf(K key, V value) {
    return Maps.newHashMap();
  }

  @Test
  public void testGetInstance() {
    SourceMap sourceMapV1 = Format.V1.getInstance();
    SourceMap sourceMapDefault = Format.DEFAULT.getInstance();
    SourceMap sourceMapV2 = Format.V2.getInstance();
    SourceMap sourceMapV3 = Format.V3.getInstance();

    assertNotNull(sourceMapV1);
    assertNotNull(sourceMapDefault);
    assertNotNull(sourceMapV2);
    assertNotNull(sourceMapV3);
  }

  @Test
  public void testAddMapping() throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    SourceMap sourceMap = new SourceMap(SourceMapGeneratorFactory.getInstance(SourceMapFormat.V1));
    sourceMap.setStartingPosition(1, 0);

    Node node = Node.newNode("someNode");
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 2);
    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    sourceMap.appendTo(out, "test.js");

    assertEquals(
        "{\"version\":1,\"file\":\"test.js\",\"sources\":[],\"names\":[],\"mappings\":[["
            + "\"test.js\",\"someNode\",0,0,0,"
            + "0,1,1,1,0,"
            + "0,2,2,1,0]]}",
        out.toString());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddMappingWithNullNode() {
    SourceMap sourceMap = new SourceMap(SourceMapGeneratorFactory.getInstance(SourceMapFormat.V1));

    sourceMap.addMapping(null, new FilePosition(1, 1), new FilePosition(1, 2));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddMappingWithNullOutputStartPosition() {
    SourceMap sourceMap = new SourceMap(SourceMapGeneratorFactory.getInstance(SourceMapFormat.V1));

    Node node = Node.newNode("someNode");
    sourceMap.addMapping(node, null, new FilePosition(1, 2));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddMappingWithNullOutputEndPosition() {
    SourceMap sourceMap = new SourceMap(SourceMapGeneratorFactory.getInstance(SourceMapFormat.V1));

    Node node = Node.newNode("someNode");
    FilePosition outputStartPosition = new FilePosition(1, 1);
    sourceMap.addMapping(node, outputStartPosition, null);
  }

  @Test
  public void testFixupSourceLocation() {
    SourceMap sourceMap = new SourceMap(SourceMapGeneratorFactory.getInstance(SourceMapFormat.V1));
    sourceMap.setPrefixMappings(listOf(
        new SourceMap.LocationMapping("oldPrefix", "newPrefix"),
        new SourceMap.LocationMapping("anotherOldPrefix", "anotherNewPrefix")
    ));

    String sourceFile = "oldPrefix/test.js";
    String fixed = sourceMap.fixupSourceLocation(sourceFile);

    assertEquals("newPrefix/test.js", fixed);

    sourceFile = "anotherOldPrefix/test.js";
    fixed = sourceMap.fixupSourceLocation(sourceFile);

    assertEquals("anotherNewPrefix/test.js", fixed);

    sourceFile = "test.js";
    fixed = sourceMap.fixupSourceLocation(sourceFile);

    assertEquals("test.js", fixed);
  }

  @Test
  public void testAppendTo() throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    SourceMap sourceMap = new SourceMap(SourceMapGeneratorFactory.getInstance(SourceMapFormat.V1));
    sourceMap.setStartingPosition(1, 0);

    Node node1 = Node.newNode("node1");
    FilePosition outputStartPosition1 = new FilePosition(1, 1);
    FilePosition outputEndPosition1 = new FilePosition(1, 2);
    sourceMap.addMapping(node1, outputStartPosition1, outputEndPosition1);

    Node node2 = Node.newNode("node2");
    FilePosition outputStartPosition2 = new FilePosition(2, 1);
    FilePosition outputEndPosition2 = new FilePosition(2, 2);
    sourceMap.addMapping(node2, outputStartPosition2, outputEndPosition2);

    sourceMap.appendTo(out, "test.js");

    assertEquals(
        "{\"version\":1,\"file\":\"test.js\",\"sources\":[],\"names\":[],\"mappings\":[["
            + "\"test.js\",\"node1\",0,0,0,"
            + "0,1,1,1,0,"
            + "0,2,2,1,0,"
            + "1,1,1,2,0,"
            + "1,2,2,2,0]]}",
        out.toString());
  }

  @Test
  public void testValidate() {
    SourceMap sourceMap = new SourceMap(SourceMapGeneratorFactory.getInstance(SourceMapFormat.V1));
    sourceMap.validate(true);
    sourceMap.validate(false);
  }

  @Test
  public void testReset() {
    SourceMap sourceMap = new SourceMap(SourceMapGeneratorFactory.getInstance(SourceMapFormat.V1));

    Node node = Node.newNode("someNode");
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 2);
    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    sourceMap.reset();

    assertEquals(0, sourceMap.prefixMappings.size());
    assertFalse(sourceMap.sourceLocationFixupCache.containsKey("test.js"));
  }

  @Test
  public void testSetWrapperPrefix() throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    SourceMap sourceMap = new SourceMap(SourceMapGeneratorFactory.getInstance(SourceMapFormat.V1));
    sourceMap.setWrapperPrefix("wrapper");
    sourceMap.setStartingPosition(1, 0);

    Node node = Node.newNode("someNode");
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 2);
    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    sourceMap.appendTo(out, "test.js");

    assertEquals(
        "{\"version\":1,\"file\":\"test.js\",\"wrapper\":\"wrapper\",\"sources\":[],\"names\":[],\"mappings\":[["
            + "\"test.js\",\"someNode\",0,0,0,"
            + "0,1,1,1,0,"
            + "0,2,2,1,0]]}",
        out.toString());
  }
}