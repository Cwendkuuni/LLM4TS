import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import com.google.javascript.jscomp.SourceMap;
import com.google.javascript.jscomp.SourceMap.Format;
import com.google.javascript.jscomp.SourceMap.DetailLevel;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SourceMapTest {

  private SourceMap sourceMap;
  private ByteArrayOutputStream outContent;

  @Before
  public void setup() {
    String sourceFile = "TestSource.java";
    String prefix = "/test/";
    List<SourceMap.LocationMapping> locationMappings = Lists.newArrayList(
        new SourceMap.LocationMapping(prefix, "src/test/"));

    Format format = SourceMap.Format.V1;
    SourceMapGenerator generator = format.getInstance();
    sourceMap = new SourceMap(generator);
    sourceMap.setPrefixMappings(locationMappings);
    outContent = new ByteArrayOutputStream();
  }

  @Test
  public void testAddMapping() throws IOException {
    Node node = Node.newNode("foo");
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 5);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
    sourceMap.appendTo(outContent, "source-map.js");

    String sourceMapJson = outContent.toString();
    assertNotNull(sourceMapJson);

    // Check if the mapping is added correctly
    String expectedMapping = String.format(
        "{\"version\":1,\"file\":\"%s\",\"sources\":[\"%s\"]," +
        "\"sourcesContent\":{},\"names\":[],\"mappings\":[",
        sourceFile, "src/test/");

    int startIndex = sourceMapJson.indexOf(expectedMapping);
    int endIndex = sourceMapJson.indexOf("],\"file\":", startIndex);

    String mapping = sourceMapJson.substring(startIndex, endIndex + 12);

    // Check if the node name matches the original name
    String originalName = (String) node.getProp(Node.ORIGINALNAME_PROP);
    assertEquals(originalName, mapping.substring(13, mapping.length() - 1));
  }

  @Test
  public void testFixupSourceLocation() {
    String sourceFile = "TestSource.java";
    String fixedSourceFile = "fixed/TestSource.java";

    String fixedSourceFileExpected = sourceMap.fixupSourceLocation(sourceFile);
    assertEquals(fixedSourceFileExpected, fixedSourceFile);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddMappingWithNullSourceFile() {
    Node node = Node.newNode("foo");
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 5);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
    sourceMap.appendTo(outContent, "source-map.js");

    String sourceFile = null;

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddMappingWithNegativeLineno() {
    Node node = Node.newNode("foo");
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 5);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
    sourceMap.appendTo(outContent, "source-map.js");

    Node nodeWithNegativeLineno = Node.newNode("bar");
    nodeWithNegativeLineno.setLineno(-1);

    sourceMap.addMapping(nodeWithNegativeLineno, outputStartPosition, outputEndPosition);
  }

  @Test
  public void testSetDetailLevel() {
    DetailLevel detailLevel = SourceMap.DetailLevel.SYMBOLS;
    sourceMap.setDetailLevel(detailLevel);

    Node node = Node.newNode("foo");
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 5);

    // Check if the node is mapped when its detail level is set to SYMBOLS
    boolean isMapped = sourceMap.apply(node, outputStartPosition, outputEndPosition);
    assertTrue(isMapped);

    // Check if the node is not mapped when its detail level is not SYMBOLS
    sourceMap.setDetailLevel(SourceMap.DetailLevel.ALL);

    isMapped = sourceMap.apply(node, outputStartPosition, outputEndPosition);
    assertFalse(isMapped);
  }

  @Test
  public void testValidate() {
    sourceMap.validate(true);
  }

  @Test
  public void testReset() {
    sourceMap.reset();
    assertNull(sourceMap.generator);
    assertEquals(Collections.emptyList(), sourceMap.prefixMappings);
  }
}