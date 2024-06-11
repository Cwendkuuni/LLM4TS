import com.google.javascript.jscomp.SourceMap;
import com.google.javascript.jscomp.SourceMap.DetailLevel;
import com.google.javascript.jscomp.SourceMap.Format;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SourceMapTest {

  @Test
  public void testSourceMapGeneration() throws IOException {
    // Test generating a source map with default settings
    SourceMap sourceMap = SourceMap.newInstance(Format.DEFAULT, DetailLevel.ALL);
    Node node = Node.newIntLiteral(10);
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 5);
    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
    String sourceMapText = "";
    sourceMap.appendTo(new StringWriter(sourceMapText), "source-map.js");
    assertEquals(
        "{\"version\":3,\"file\":\"source-map.js\",\"sources\":[],\"sourceRoot\":\"\",\"mappings\":[]}",
        sourceMapText
    );

    // Test generating a source map with custom settings and prefix mappings
    List<SourceMap.LocationMapping> prefixMappings = Collections.singletonList(
        new SourceMap.LocationMapping("test/", "src/")
    );
    SourceMap sourceMapV1 = SourceMap.newInstance(Format.V1, DetailLevel.SYMBOLS, prefixMappings);
    Node nodeSymbol = Node.newSymbol("testSymbol");
    FilePosition outputStartPositionSymbol = new FilePosition(2, 2);
    FilePosition outputEndPositionSymbol = new FilePosition(2, 6);
    sourceMapV1.addMapping(nodeSymbol, outputStartPositionSymbol, outputEndPositionSymbol);
    String sourceMapTextV1 = "";
    sourceMapV1.appendTo(new StringWriter(sourceMapTextV1), "source-map.js");
    assertEquals(
        "{\"version\":1,\"file\":\"source-map.js\",\"sources\":[\"src/TestSourceMap.java\"]," +
        "\"sourceRoot\":\"test/\",\"mappings\":[\"test/TestSourceMap.java:2:2-2:6\",\"testSymbol\"],\"fileMappings\":[]}",
        sourceMapTextV1
    );
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddMappingWithNullNode() {
    SourceMap sourceMap = new SourceMap(new SourceMapGeneratorFactory.getInstance(SourceMapFormat.DEFAULT));
    Node nullNode = null;
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 5);
    sourceMap.addMapping(nullNode, outputStartPosition, outputEndPosition);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddMappingWithNullFilePosition() {
    SourceMap sourceMap = new SourceMap(new SourceMapGeneratorFactory.getInstance(SourceMapFormat.DEFAULT));
    Node node = Node.newIntLiteral(10);
    FilePosition nullOutputStartPosition = null;
    FilePosition outputEndPosition = new FilePosition(1, 5);
    sourceMap.addMapping(node, nullOutputStartPosition, outputEndPosition);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddMappingWithNullFilePositionEnd() {
    SourceMap sourceMap = new SourceMap(new SourceMapGeneratorFactory.getInstance(SourceMapFormat.DEFAULT));
    Node node = Node.newIntLiteral(10);
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition nullOutputEndPosition = null;
    sourceMap.addMapping(node, outputStartPosition, nullOutputEndPosition);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddMappingWithNullSourceFile() {
    SourceMap sourceMap = new SourceMap(new SourceMapGeneratorFactory.getInstance(SourceMapFormat.DEFAULT));
    Node node = Node.newIntLiteral(10);
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 5);
    sourceMap.addMapping(node, null, outputStartPosition, outputEndPosition);
  }

  @Test
  public void testValidate() {
    SourceMap sourceMap = new SourceMap(new SourceMapGeneratorFactory.getInstance(SourceMapFormat.DEFAULT));
    String sourceMapText = "{\"version\":3,\"file\":\"source-map.js\",\"sources\":[],\"sourceRoot\":\"\",\"mappings\":[]}";
    sourceMap.setSourceMapText(sourceMapText);
    sourceMap.validate(true);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValidateWithNullSourceMapText() {
    SourceMap sourceMap = new SourceMap(new SourceMapGeneratorFactory.getInstance(SourceMapFormat.DEFAULT));
    sourceMap.validate(true);
  }

  @Test
  public void testReset() {
    SourceMap sourceMap = new SourceMap(new SourceMapGeneratorFactory.getInstance(SourceMapFormat.DEFAULT));
    String sourceMapText = "{\"version\":3,\"file\":\"source-map.js\",\"sources\":[],\"sourceRoot\":\"\",\"mappings\":[]}";
    sourceMap.setSourceMapText(sourceMapText);
    sourceMap.reset();
    assertEquals("", sourceMap.getSourceMapText());
  }
}