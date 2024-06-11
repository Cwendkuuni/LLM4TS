import static org.junit.Assert.*;
import com.google.javascript.jscomp.SourceMap.DetailLevel;
import com.google.javascript.jscomp.SourceMap.Format;
import com.google.javascript.jscomp.SourceMap;
import com.google.javascript.rhino.Node;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SourceMapTest {

  @SuppressWarnings("unchecked")
  @Test
  public void testConstructor() {
    SourceMap sourceMap = SourceMap.Format.V1.getInstance();
    assertNotNull(sourceMap);
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testAddMapping() throws IOException {
    SourceMap sourceMap = SourceMap.Format.V1.getInstance();

    Node node = Node.newNull();
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(2, 2);
    String sourceFile = "test.js";

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
    String fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);
    assertEquals("test.js", fixedSourceFile);

    sourceMap.reset();
    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
    fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);
    assertNotEquals("test.js", fixedSourceFile);
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testFixupSourceLocationWithPrefixMappings() throws IOException {
    SourceMap sourceMap = SourceMap.Format.V1.getInstance();

    List<SourceMap.LocationMapping> prefixMappings = Collections.singletonList(
        new SourceMap.LocationMapping("test/", "src/"));

    sourceMap.setPrefixMappings(prefixMappings);

    Node node = Node.newNull();
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(2, 2);
    String sourceFile = "test/test.js";

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
    String fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);
    assertEquals("src/test.js", fixedSourceFile);
  }

  @SuppressWarnings("unchecked")
  @Test(expected = IllegalArgumentException.class)
  public void testAddMappingWithNullNode() throws IOException {
    SourceMap sourceMap = SourceMap.Format.V1.getInstance();

    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(2, 2);
    Node node = null;

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
  }

  @SuppressWarnings("unchecked")
  @Test(expected = IllegalArgumentException.class)
  public void testAddMappingWithNullOutputStartPosition() throws IOException {
    SourceMap sourceMap = SourceMap.Format.V1.getInstance();

    Node node = Node.newNull();
    FilePosition outputStartPosition = null;
    FilePosition outputEndPosition = new FilePosition(2, 2);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
  }

  @SuppressWarnings("unchecked")
  @Test(expected = IllegalArgumentException.class)
  public void testAddMappingWithNullOutputEndPosition() throws IOException {
    SourceMap sourceMap = SourceMap.Format.V1.getInstance();

    Node node = Node.newNull();
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = null;

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
  }

  @Test
  public void testSetStartingPosition() {
    SourceMap sourceMap = SourceMap.Format.V1.getInstance();

    sourceMap.setStartingPosition(1, 1);
  }

  @Test
  public void testSetWrapperPrefix() {
    SourceMap sourceMap = SourceMap.Format.V1.getInstance();

    sourceMap.setWrapperPrefix("wrapper_");
  }

  @Test
  public void testValidate() {
    SourceMap sourceMap = SourceMap.Format.V1.getInstance();

    sourceMap.validate(true);
  }

  @Test
  public void testReset() {
    SourceMap sourceMap = SourceMap.Format.V1.getInstance();

    sourceMap.reset();
  }

  @Test
  public void testAppendTo() throws IOException {
    SourceMap sourceMap = SourceMap.Format.V1.getInstance();

    Appendable output = new StringBuilder();
    String name = "output.js.map";

    sourceMap.appendTo(output, name);

    assertEquals("", output.toString());
  }

  @Test
  public void testGetInstanceWithInvalidFormat() {
    assertThrows(IllegalArgumentException.class, () -> {
      SourceMap sourceMap = SourceMap.Format.INVALID.getInstance();
    });
  }

  @Test
  public void testDetermineDetailLevel() {
    SourceMap sourceMap = SourceMap.Format.DEFAULT.getInstance();

    Node node = Node.newNull();

    DetailLevel detailLevel = sourceMap.DetailLevel.ALL;
    assertTrue(detailLevel.apply(node));

    detailLevel = sourceMap.DetailLevel.SYMBOLS;
    assertFalse(detailLevel.apply(NodeUtil.newIntLiteral(5)));
    assertFalse(detailLevel.apply(NodeUtil.newIntLiteral(-1)));
    assertTrue(detailLevel.apply(Node.newName("foo")));
  }
}