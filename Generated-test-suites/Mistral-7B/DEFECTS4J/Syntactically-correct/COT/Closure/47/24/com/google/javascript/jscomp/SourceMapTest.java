import static org.junit.Assert.*;
import static com.google.javascript.rhino.Node.*;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SourceMapTest {

  private SourceMap sourceMap;
  private ByteArrayOutputStream outputStream;

  @Before
  public void setup() {
    SourceMapGeneratorFactory factory = SourceMapGeneratorFactory.getInstance(SourceMapFormat.DEFAULT);
    SourceMapGenerator generator = factory.newInstance();
    sourceMap = new SourceMap(generator);
    outputStream = new ByteArrayOutputStream();
  }

  @Test
  public void testAddMapping() {
    String sourceFile = "TestSource.java";
    Node node = newObjectLiteral();
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 5);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
    String fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);
    assertEquals("TestSource.java", fixedSourceFile);
  }

  @Test
  public void testAddMappingWithPrefix() {
    String sourceFile = "/path/to/TestSource.java";
    Node node = newObjectLiteral();
    String prefix = "/path/to/";
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 5);

    List<LocationMapping> prefixMappings = Collections.singletonList(new LocationMapping(prefix, ""));
    sourceMap.setPrefixMappings(prefixMappings);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
    String fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);
    assertEquals("TestSource.java", fixedSourceFile);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddMappingNullNode() {
    Node node = null;
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 5);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddMappingNullOutputStartPosition() {
    Node node = newObjectLiteral();
    FilePosition outputStartPosition = null;
    FilePosition outputEndPosition = new FilePosition(1, 5);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddMappingNullOutputEndPosition() {
    Node node = newObjectLiteral();
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = null;

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
  }

  @Test
  public void testFixupSourceLocation() throws IOException {
    String sourceFile = "TestSource.java";
    String fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);
    assertEquals("TestSource.java", fixedSourceFile);
  }

  @Test
  public void testFixupSourceLocationWithPrefix() {
    String sourceFile = "/path/to/TestSource.java";
    List<LocationMapping> prefixMappings = Collections.singletonList(new LocationMapping("/path/to/", ""));
    sourceMap.setPrefixMappings(prefixMappings);

    String fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);
    assertEquals("TestSource.java", fixedSourceFile);
  }

  @Test
  public void testAppendTo() throws IOException {
    String name = "TestSourceMap.js.map";
    sourceMap.appendTo(outputStream, name);

    String expected = "sourceMappingURL=TestSourceMap.js.map\n";
    String actual = outputStream.toString();
    assertEquals(expected, actual);
  }

  @Test
  public void testReset() {
    sourceMap.reset();
    assertNull(sourceMap.generator);
    assertEquals(Collections.emptyList(), sourceMap.prefixMappings);
    assertEquals(Collections.emptyMap(), sourceMap.sourceLocationFixupCache);
  }

  @Test
  public void testSetStartingPosition() {
    int offsetLine = 1;
    int offsetIndex = 1;
    sourceMap.setStartingPosition(offsetLine, offsetIndex);
    FilePosition expected = new FilePosition(offsetLine, offsetIndex);
    FilePosition actual = sourceMap.generator.getStartingPosition();
    assertEquals(expected, actual);
  }

  @Test
  public void testSetWrapperPrefix() {
    String prefix = "wrapper.";
    sourceMap.setWrapperPrefix(prefix);
    String actual = sourceMap.generator.getWrapperPrefix();
    assertEquals(prefix, actual);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetWrapperPrefixNullPrefix() {
    String prefix = null;
    sourceMap.setWrapperPrefix(prefix);
  }

  @Test
  public void testValidate() {
    sourceMap.validate(true);
  }

  @Test
  public void testValidateFalse() {
    sourceMap.validate(false);
  }
}