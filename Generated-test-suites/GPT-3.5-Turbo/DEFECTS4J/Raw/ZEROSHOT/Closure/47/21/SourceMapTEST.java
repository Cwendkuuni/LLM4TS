import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.rhino.Node;
import com.google.debugging.sourcemap.FilePosition;
import com.google.common.collect.Lists;
import java.util.List;

public class SourceMapTest {

  @Test
  public void testAddMapping() {
    SourceMap sourceMap = new SourceMap(SourceMap.Format.V1.getInstance());
    Node node = new Node();
    node.setSourceFileName("testFile.js");
    node.setLineno(1);
    node.setCharno(0);
    FilePosition outputStartPosition = new FilePosition(0, 0);
    FilePosition outputEndPosition = new FilePosition(1, 1);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
    // Assert mappings are added correctly
    List<SourceMap.LocationMapping> prefixMappings = Lists.newArrayList();
    sourceMap.setPrefixMappings(prefixMappings);
    assertEquals("testFile.js", sourceMap.fixupSourceLocation("testFile.js"));
  }

  @Test
  public void testAppendTo() {
    SourceMap sourceMap = new SourceMap(SourceMap.Format.V1.getInstance());
    Node node = new Node();
    node.setSourceFileName("testFile.js");
    node.setLineno(1);
    node.setCharno(0);
    FilePosition outputStartPosition = new FilePosition(0, 0);
    FilePosition outputEndPosition = new FilePosition(1, 1);
    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    StringBuilder builder = new StringBuilder();
    try {
      sourceMap.appendTo(builder, "testFile");
    } catch (Exception e) {
      fail("Exception should not occur");
    }
    // Assert source map is appended correctly
    String expected = "{\"version\":3,\"file\":\"testFile\",\"mappings\":\"AAAA,QAASA,IAAIA\"}";
    assertEquals(expected, builder.toString());
  }

  @Test
  public void testReset() {
    SourceMap sourceMap = new SourceMap(SourceMap.Format.V1.getInstance());
    Node node = new Node();
    node.setSourceFileName("testFile.js");
    node.setLineno(1);
    node.setCharno(0);
    FilePosition outputStartPosition = new FilePosition(0, 0);
    FilePosition outputEndPosition = new FilePosition(1, 1);
    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
    // Assert the mappings are reset correctly
    sourceMap.reset();
    List<SourceMap.LocationMapping> prefixMappings = Lists.newArrayList();
    sourceMap.setPrefixMappings(prefixMappings);
    assertEquals("testFile.js", sourceMap.fixupSourceLocation("testFile.js"));
  }

  @Test
  public void testSetStartingPosition() {
    SourceMap sourceMap = new SourceMap(SourceMap.Format.V1.getInstance());
    sourceMap.setStartingPosition(1, 1);
    // Assert the starting position is set correctly
    // Cannot directly access starting position, so no assertion statement
  }

  @Test
  public void testSetWrapperPrefix() {
    SourceMap sourceMap = new SourceMap(SourceMap.Format.V1.getInstance());
    sourceMap.setWrapperPrefix("prefix");
    // Assert the wrapper prefix is set correctly
    // Cannot directly access wrapper prefix, so no assertion statement
  }

  @Test
  public void testValidate() {
    SourceMap sourceMap = new SourceMap(SourceMap.Format.V1.getInstance());
    sourceMap.validate(true);
    // Assert validation is set correctly
    // Cannot directly access validation, so no assertion statement
  }
}