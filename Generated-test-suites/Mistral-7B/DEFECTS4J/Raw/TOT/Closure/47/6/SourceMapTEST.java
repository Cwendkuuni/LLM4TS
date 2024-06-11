import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.google.javascript.jscomp.SourceMap;
import com.google.javascript.jscomp.SourceMap.Format;
import com.google.javascript.jscomp.SourceMap.DetailLevel;
import com.google.javascript.rhino.Node;

public class SourceMapTest {

  private SourceMap sourceMap;
  private Node sampleNode;

  @Before
  public void setup() {
    // Initialize the sourceMap instance with desired format and detail level
    sourceMap = SourceMap.Format.V1.getInstance().newInstance();

    // Prepare a sample Node for testing
    sampleNode = new Node();
    sampleNode.setSourceFileName("TestSource.java");
    sampleNode.setLineno(1);
    sampleNode.setCharno(1);
  }

  @Test
  public void testAddMapping() {
    // Add mapping for the sample Node
    FilePosition outputStartPosition = new FilePosition(2, 3);
    FilePosition outputEndPosition = new FilePosition(3, 5);
    sourceMap.addMapping(sampleNode, outputStartPosition, outputEndPosition);

    // Verify the mapping has been added correctly
    assertNotNull(sourceMap.getInstance().getSourceMap());
    assertEquals("TestSource.java", sourceMap.getInstance().getSourceMap().getSourceFile(0).getName());
    assertEquals(2, sourceMap.getInstance().getSourceMap().getSourceFile(0).getSource().getLineNumber(1));
    assertEquals(3, sourceMap.getInstance().getSourceMap().getSourceFile(0).getSource().getLineNumber(1) + 1);
  }

  @Test
  public void testAddMappingWithInvalidNode() {
    // Add mapping for a null Node
    sourceMap.addMapping(null, new FilePosition(1, 1), new FilePosition(1, 1));

    // Verify no exception is thrown
    assertNotNull(sourceMap.getInstance().getSourceMap());
  }

  @Test
  public void testValidate() {
    // Validate the sourceMap instance
    sourceMap.validate(true);

    // No exception should be thrown
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValidateWithInvalidFormat() {
    // Create a SourceMap instance with an invalid format
    SourceMap invalidSourceMap = SourceMap.Format.INVALID.getInstance().newInstance();

    // Validate the instance and expect an IllegalArgumentException
    invalidSourceMap.validate(true);
  }

  @Test
  public void testReset() {
    // Add some mappings and then reset the sourceMap instance
    sourceMap.addMapping(sampleNode, new FilePosition(1, 1), new FilePosition(1, 1));
    sourceMap.addMapping(sampleNode, new FilePosition(2, 2), new FilePosition(2, 2));
    sourceMap.reset();

    // Verify the sourceMap has been reset
    assertNull(sourceMap.getInstance().getSourceMap());
  }
}