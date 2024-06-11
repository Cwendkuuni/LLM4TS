import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.SourceMap;
import com.google.javascript.jscomp.SourceMap.DetailLevel;
import com.google.javascript.jscomp.SourceMap.Format;
import com.google.javascript.rhino.Node;

public class SourceMapTest {

  private SourceMap sourceMap;

  @Before
  public void setUp() {
    sourceMap = new SourceMap(Format.V1.getInstance());
  }

  @After
  public void tearDown() {
    sourceMap.reset();
  }

  @Test
  public void testAddMapping_typicalCase() {
    Node node = new Node(Node.CALL);
    node.setSourceFileName("test.js");
    node.setLineno(1);
    node.putProp(Node.ORIGINALNAME_PROP, "originalFunction");

    FilePosition outputStartPosition = new FilePosition(0, 0);
    FilePosition outputEndPosition = new FilePosition(0, 5);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    // Assert that a mapping has been added
    assertEquals(1, sourceMap.getMappingsCount());
    assertEquals("test.js", sourceMap.getSourceFile(0));
    assertEquals("originalFunction", sourceMap.getOriginalName(0));

    // Assert that the mapping positions are correct
    assertEquals(1, sourceMap.getOriginalLineNumber(0));
    assertEquals(0, sourceMap.getOriginalColumnNumber(0));
    assertEquals(0, sourceMap.getOutputStartPosition(0).getLineNumber());
    assertEquals(0, sourceMap.getOutputStartPosition(0).getColumnNumber());
    assertEquals(0, sourceMap.getOutputEndPosition(0).getLineNumber());
    assertEquals(5, sourceMap.getOutputEndPosition(0).getColumnNumber());
  }

  @Test
  public void testAddMapping_noSourceFile() {
    Node node = new Node(Node.CALL);
    node.setLineno(1);
    node.putProp(Node.ORIGINALNAME_PROP, "originalFunction");

    FilePosition outputStartPosition = new FilePosition(0, 0);
    FilePosition outputEndPosition = new FilePosition(0, 5);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    // Assert that no mapping has been added
    assertEquals(0, sourceMap.getMappingsCount());
  }

  @Test
  public void testAddMapping_negativeLineNumber() {
    Node node = new Node(Node.CALL);
    node.setSourceFileName("test.js");
    node.setLineno(-1);
    node.putProp(Node.ORIGINALNAME_PROP, "originalFunction");

    FilePosition outputStartPosition = new FilePosition(0, 0);
    FilePosition outputEndPosition = new FilePosition(0, 5);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    // Assert that no mapping has been added
    assertEquals(0, sourceMap.getMappingsCount());
  }

  @Test
  public void testAddMapping_noOriginalName() {
    Node node = new Node(Node.CALL);
    node.setSourceFileName("test.js");
    node.setLineno(1);

    FilePosition outputStartPosition = new FilePosition(0, 0);
    FilePosition outputEndPosition = new FilePosition(0, 5);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    // Assert that a mapping has been added with empty original name
    assertEquals(1, sourceMap.getMappingsCount());
    assertEquals("", sourceMap.getOriginalName(0));
  }

  @Test
  public void testAddMapping_emptySourceFile() {
    Node node = new Node(Node.CALL);
    node.setSourceFileName("");
    node.setLineno(1);
    node.putProp(Node.ORIGINALNAME_PROP, "originalFunction");

    FilePosition outputStartPosition = new FilePosition(0, 0);
    FilePosition outputEndPosition = new FilePosition(0, 5);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    // Assert that a mapping has been added with empty source file
    assertEquals(1, sourceMap.getMappingsCount());
    assertEquals("", sourceMap.getSourceFile(0));
  }

  @Test
  public void testFixupSourceLocation_typicalCase() {
    List<LocationMapping> prefixMappings = new ArrayList<>();
    prefixMappings.add(new LocationMapping("file:/src/", "/project/src/"));
    sourceMap.setPrefixMappings(prefixMappings);

    String sourceFile = "file:/src/test.js";
    String fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);

    // Assert that the source file has been fixed
    assertEquals("/project/src/test.js", fixedSourceFile);
  }

  @Test
  public void testFixupSourceLocation_noPrefixMatch() {
    List<LocationMapping> prefixMappings = new ArrayList<>();
    prefixMappings.add(new LocationMapping("file:/src/", "/project/src/"));
    sourceMap.setPrefixMappings(prefixMappings);

    String sourceFile = "file:/other/test.js";
    String fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);

    // Assert that the source file remains the same since no prefix-match is found
    assertEquals(sourceFile, fixedSourceFile);
  }

  @Test
  public void testAppendTo_typicalCase() throws IOException {
    Node node = new Node(Node.CALL);
    node.setSourceFileName("test.js");
    node.setLineno(1);
    node.putProp(Node.ORIGINALNAME_PROP, "originalFunction");

    FilePosition outputStartPosition = new FilePosition(0, 0);
    FilePosition outputEndPosition = new FilePosition(0, 5);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    StringBuilder stringBuilder = new StringBuilder();
    sourceMap.appendTo(stringBuilder, "sourcemap.js");

    String expected = "{\"version\":3,\"file\":\"sourcemap.js\",\"mappings\":\"AAAA\",\"sources\":[\"test.js\"],\"names\":[\"originalFunction\"]}";
    assertEquals(expected, stringBuilder.toString());
  }

  @Test
  public void testSetStartingPosition_typicalCase() {
    sourceMap.setStartingPosition(1, 2);

    // Assert that the starting position has been set
    assertEquals(1, sourceMap.getOutputStartPosition().getLineNumber());
    assertEquals(2, sourceMap.getOutputStartPosition().getColumnNumber());
  }

  @Test
  public void testSetWrapperPrefix_typicalCase() {
    sourceMap.setWrapperPrefix("/* ");

    // Assert that the wrapper prefix has been set
    assertEquals("/* ", sourceMap.getWrapperPrefix());
  }

  @Test
  public void testValidate_true() {
    sourceMap.validate(true);

    // Assert that validate flag is set to true
    assertTrue(sourceMap.getValidate());
  }

  @Test
  public void testValidate_false() {
    sourceMap.validate(false);

    // Assert that validate flag is set to false
    assertTrue(!sourceMap.getValidate());
  }
}