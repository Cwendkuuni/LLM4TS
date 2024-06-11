import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class SourceMapTest {

  @Test
  public void testAddMapping() {
    SourceMap sourceMap = new SourceMap(SourceMap.Format.DEFAULT.getInstance());

    Node node = mock(Node.class);
    FilePosition outputStartPosition = mock(FilePosition.class);
    FilePosition outputEndPosition = mock(FilePosition.class);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
    // Additional test assertions for addMapping() method
  }

  @Test
  public void testFixupSourceLocation() {
    SourceMap sourceMap = new SourceMap(SourceMap.Format.DEFAULT.getInstance());

    String sourceFile = "testFile.js";
    String expected = "testFile.js";

    String result = sourceMap.fixupSourceLocation(sourceFile);

    assertEquals(expected, result);
    // Additional test assertions for fixupSourceLocation() method
  }

  @Test
  public void testAppendTo() throws IOException {
    SourceMap sourceMap = new SourceMap(SourceMap.Format.DEFAULT.getInstance());

    Appendable out = new StringBuilder("output");
    String name = "test";

    sourceMap.appendTo(out, name);
    // Additional test assertions for appendTo() method
  }

  @Test
  public void testReset() {
    SourceMap sourceMap = new SourceMap(SourceMap.Format.DEFAULT.getInstance());

    sourceMap.reset();
    // Additional test assertions for reset() method
  }

  @Test
  public void testSetStartingPosition() {
    SourceMap sourceMap = new SourceMap(SourceMap.Format.DEFAULT.getInstance());

    int offsetLine = 1;
    int offsetIndex = 0;

    sourceMap.setStartingPosition(offsetLine, offsetIndex);
    // Additional test assertions for setStartingPosition() method
  }

  @Test
  public void testSetWrapperPrefix() {
    SourceMap sourceMap = new SourceMap(SourceMap.Format.DEFAULT.getInstance());

    String prefix = "prefix";

    sourceMap.setWrapperPrefix(prefix);
    // Additional test assertions for setWrapperPrefix() method
  }

  @Test
  public void testValidate() {
    SourceMap sourceMap = new SourceMap(SourceMap.Format.DEFAULT.getInstance());

    boolean validate = true;

    sourceMap.validate(validate);
    // Additional test assertions for validate() method
  }

  @Test
  public void testSetPrefixMappings() {
    SourceMap sourceMap = new SourceMap(SourceMap.Format.DEFAULT.getInstance());

    List<SourceMap.LocationMapping> prefixMappings = new ArrayList<>();
    prefixMappings.add(new SourceMap.LocationMapping("prefix", "replacement"));

    sourceMap.setPrefixMappings(prefixMappings);
    // Additional test assertions for setPrefixMappings() method
  }

}