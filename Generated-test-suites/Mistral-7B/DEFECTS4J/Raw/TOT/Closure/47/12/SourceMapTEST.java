import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.google.javascript.jscomp.SourceMap;
import com.google.javascript.jscomp.SourceMap.Format;
import com.google.javascript.jscomp.SourceMap.DetailLevel;
import com.google.javascript.rhino.Node;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SourceMapTest {

  private SourceMap sourceMap;
  private Node testNode;

  @Before
  public void setUp() {
    Format format = SourceMap.Format.V1;
    DetailLevel detailLevel = SourceMap.DetailLevel.ALL;
    sourceMap = new SourceMap(format.getInstance()).setPrefixMappings(getPrefixMappings());
    testNode = Node.newIntLiteral(1);
  }

  @Test
  public void testAddMapping() throws IOException {
    String sourceFile = "TestSource.java";
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 5);

    sourceMap.addMapping(testNode, sourceFile, outputStartPosition, outputEndPosition);
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    sourceMap.appendTo(new OutputStreamWriter(outputStream), "TestSourceMap.js");

    String sourceMapString = outputStream.toString();
    int index = sourceMapString.indexOf("\"TestSource.java\",\"TestSource.java\",");
    String mapping = sourceMapString.substring(index).trim();

    assertTrue(mapping.contains("\"sourceFile\":\"TestSource.java\""));
    assertTrue(mapping.contains("\"originalName\":\"TestSource.java\""));
    assertTrue(mapping.contains("\"generatedPosition\":{"));
    assertTrue(mapping.contains("\"line\":1"));
    assertTrue(mapping.contains("\"column\":1"));
  }

  @Test
  public void testFixupSourceLocation() {
    String sourceFile = "TestPrefixSource.java";
    String fixedFile = sourceMap.fixupSourceLocation(sourceFile);

    assertEquals("TestSuffixSource.java", fixedFile);
  }

  @Test(expected = IllegalStateException.class)
  public void testAddMappingWithoutSourceFile() {
    Node nodeWithoutSourceFile = Node.newIntLiteral(1);

    String sourceFile = null;
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 5);

    sourceMap.addMapping(nodeWithoutSourceFile, sourceFile, outputStartPosition, outputEndPosition);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddMappingWithNegativeLineNumber() {
    Node nodeWithNegativeLineNumber = Node.newIntLiteral(-1);

    String sourceFile = "TestSource.java";
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 5);

    sourceMap.addMapping(nodeWithNegativeLineNumber, sourceFile, outputStartPosition, outputEndPosition);
  }

  @Test
  public void testReset() throws IOException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    sourceMap.appendTo(new OutputStreamWriter(outputStream), "TestSourceMap.js");

    String sourceMapString = outputStream.toString();

    sourceMap.reset();
    sourceMap.appendTo(new OutputStreamWriter(outputStream), "TestSourceMap.js");

    String newSourceMapString = outputStream.toString();

    assertFalse(sourceMapString.equals(newSourceMapString));
  }

  @Test
  public void testValidate() throws IOException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    sourceMap.appendTo(new OutputStreamWriter(outputStream), "TestSourceMap.js");

    sourceMap.validate(true);
  }

  private List<LocationMapping> getPrefixMappings() {
    return Collections.singletonList(new SourceMap.LocationMapping("TestPrefix", "TestSuffix"));
  }
}