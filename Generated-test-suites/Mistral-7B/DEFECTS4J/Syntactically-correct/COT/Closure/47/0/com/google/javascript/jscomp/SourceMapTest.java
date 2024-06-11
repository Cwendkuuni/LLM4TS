import static org.junit.Assert.*;
import static com.google.javascript.jscomp.SourceMap.DetailLevel.*;
import static com.google.javascript.jscomp.SourceMap.Format.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.debugging.sourcemap.FilePosition;
import com.google.debugging.sourcemap.SourceMapGenerator;
import com.google.debugging.sourcemap.SourceMapGeneratorFactory;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Test class for SourceMap.java
 */
public class SourceMapTest {

  @org.junit.Test
  public void testConstructor() throws IOException {
    SourceMap sourceMap = V1.getInstance();
    assertNotNull(sourceMap);
  }

  @org.junit.Test
  public void testAddMapping() {
    SourceMap sourceMap = V1.getInstance();

    Node node = Node.newIntLiteral(1);
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 2);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
  }

  @org.junit.Test
  public void testFixupSourceLocation() {
    SourceMap sourceMap = V1.getInstance();

    String sourceFile = "test/SourceMapTest.java";
    String expected = "target/test-classes/SourceMapTest.java";

    String fixed = sourceMap.fixupSourceLocation(sourceFile);
    assertEquals(expected, fixed);
  }

  @org.junit.Test
  public void testAppendTo() throws IOException {
    SourceMap sourceMap = V1.getInstance();

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    sourceMap.appendTo(outputStream, "test.js");

    String expected = "sourceMappingURL=data:application/json;base64,...";
    String actual = outputStream.toString();
    assertTrue(actual.contains(expected));
  }

  @org.junit.Test
  public void testReset() {
    SourceMap sourceMap = V1.getInstance();
    sourceMap.reset();
  }

  @org.junit.Test
  public void testSetStartingPosition() {
    SourceMap sourceMap = V1.getInstance();
    sourceMap.setStartingPosition(1, 1);
  }

  @org.junit.Test
  public void testSetWrapperPrefix() {
    SourceMap sourceMap = V1.getInstance();
    sourceMap.setWrapperPrefix("testPrefix");
  }

  @org.junit.Test
  public void testValidate() {
    SourceMap sourceMap = V1.getInstance();
    sourceMap.validate(true);
  }

  @org.junit.Test
  public void testSetPrefixMappings() {
    SourceMap sourceMap = V1.getInstance();

    List<SourceMap.LocationMapping> prefixMappings = Lists.newArrayList(
        new SourceMap.LocationMapping("test", "target")
    );
    sourceMap.setPrefixMappings(prefixMappings);

    String sourceFile = "test/SourceMapTest.java";
    String expected = "target/test-classes/SourceMapTest.java";

    String fixed = sourceMap.fixupSourceLocation(sourceFile);
    assertEquals(expected, fixed);
  }

  @org.junit.Test
  public void testAddMappingWithInvalidNode() {
    SourceMap sourceMap = V1.getInstance();
    Node node = Node.newNull();

    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 2);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    // No exception should be thrown
  }

  @org.junit.Test
  public void testAddMappingWithNullOutputStartPosition() {
    SourceMap sourceMap = V1.getInstance();
    Node node = Node.newIntLiteral(1);

    FilePosition outputEndPosition = new FilePosition(1, 2);

    sourceMap.addMapping(node, null, outputEndPosition);

    // No exception should be thrown
  }

  @org.junit.Test
  public void testAddMappingWithNullOutputEndPosition() {
    SourceMap sourceMap = V1.getInstance();
    Node node = Node.newIntLiteral(1);

    FilePosition outputStartPosition = new FilePosition(1, 1);

    sourceMap.addMapping(node, outputStartPosition, null);

    // No exception should be thrown
  }

  @org.junit.Test
  public void testAddMappingWithEmptySourceFile() {
    SourceMap sourceMap = V1.getInstance();
    Node node = Node.newIntLiteral(1);

    String emptySourceFile = "";

    sourceMap.addMapping(node, new FilePosition(1, 1), new FilePosition(1, 2), emptySourceFile, emptySourceFile);

    // No exception should be thrown
  }

  @org.junit.Test
  public void testAddMappingWithNullSourceFile() {
    SourceMap sourceMap = V1.getInstance();
    Node node = Node.newIntLiteral(1);

    sourceMap.addMapping(node, new FilePosition(1, 1), new FilePosition(1, 2), null, null);

    // No exception should be thrown
  }

  /**
   * Test for the case when a node has a source file but its line number is -1.
   */
  @org.junit.Test
  public void testAddMappingWithInvalidLineNumber() {
    SourceMap sourceMap = V1.getInstance();
    Node node = Node.newIntLiteral(-1);

    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 2);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    // No exception should be thrown
  }
}