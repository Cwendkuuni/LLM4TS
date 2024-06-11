import static org.junit.Assert.*;

import com.google.javascript.jscomp.Node;

import com.google.debugging.sourcemap.FilePosition;

import org.junit.Before;
import org.junit.Test;

public class SourceMapTest {

  private SourceMap sourceMap;

  @Before
  public void setUp() {
    sourceMap = SourceMap.Format.V1.getInstance();
  }

  @Test
  public void testAddMapping() {
    Node node = new Node();
    FilePosition outputStartPosition = new FilePosition(1, 0);
    FilePosition outputEndPosition = new FilePosition(1, 5);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
  }

  @Test
  public void testFixupSourceLocation() {
    String sourceFile = "test.js";
    String fixed = sourceMap.fixupSourceLocation(sourceFile);
    assertEquals(sourceFile, fixed);
  }

  @Test
  public void testReset() {
    sourceMap.reset();
  }

  @Test
  public void testSetStartingPosition() {
    int offsetLine = 1;
    int offsetIndex = 0;

    sourceMap.setStartingPosition(offsetLine, offsetIndex);
  }

  @Test
  public void testSetWrapperPrefix() {
    String prefix = "wrapper";

    sourceMap.setWrapperPrefix(prefix);
  }

  @Test
  public void testValidate() {
    boolean validate = true;

    sourceMap.validate(validate);
  }

  @Test
  public void testSetPrefixMappings() {
    sourceMap.setPrefixMappings(null);
  }
}