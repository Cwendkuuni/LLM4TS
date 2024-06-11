import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Predicate;
import com.google.common.collect.Maps;
import com.google.debugging.sourcemap.FilePosition;
import com.google.debugging.sourcemap.SourceMapFormat;
import com.google.debugging.sourcemap.SourceMapGenerator;
import com.google.debugging.sourcemap.SourceMapGeneratorFactory;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SourceMapTest {

  private SourceMap sourceMap;
  private SourceMapGenerator generatorMock;

  @Before
  public void setup() {
    SourceMapFormat formatV1 = mock(SourceMapFormat.class);
    SourceMapGenerator generatorV1 = new SourceMapGeneratorV1();
    SourceMapGeneratorFactory factoryV1 = mock(SourceMapGeneratorFactory.class);
    when(factoryV1.getInstance(SourceMapFormat.V1)).thenReturn(generatorV1);
    when(factoryV1.getInstance(SourceMapFormat.V1)).thenReturn(generatorV1); // mocking multiple return types

    SourceMapFormat formatDefault = mock(SourceMapFormat.class);
    SourceMapGenerator generatorDefault = new SourceMapGenerator();
    SourceMapGeneratorFactory factoryDefault = mock(SourceMapGeneratorFactory.class);
    when(factoryDefault.getInstance(SourceMapFormat.DEFAULT)).thenReturn(generatorDefault);

    SourceMapFormat formatV2 = mock(SourceMapFormat.class);
    SourceMapGenerator generatorV2 = new SourceMapGeneratorV2();
    SourceMapGeneratorFactory factoryV2 = mock(SourceMapGeneratorFactory.class);
    when(factoryV2.getInstance(SourceMapFormat.V2)).thenReturn(generatorV2);

    SourceMapFormat formatV3 = mock(SourceMapFormat.class);
    SourceMapGenerator generatorV3 = new SourceMapGeneratorV2(); // it seems there is a typo in the class name
    SourceMapGeneratorFactory factoryV3 = mock(SourceMapGeneratorFactory.class);
    when(factoryV3.getInstance(SourceMapFormat.V3)).thenReturn(generatorV3);

    generatorMock = mock(SourceMapGenerator.class);
    sourceMap = new SourceMap(generatorMock);
  }

  @Test
  public void testGetInstance_V1() {
    SourceMap sourceMapV1 = SourceMap.Format.V1.getInstance();
    assertNotNull(sourceMapV1);
  }

  @Test
  public void testGetInstance_Default() {
    SourceMap sourceMapDefault = SourceMap.Format.DEFAULT.getInstance();
    assertNotNull(sourceMapDefault);
  }

  @Test
  public void testGetInstance_V2() {
    SourceMap sourceMapV2 = SourceMap.Format.V2.getInstance();
    assertNotNull(sourceMapV2);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetInstance_InvalidFormat() {
    SourceMap.Format invalidFormat = new SourceMap.Format() {
      @Override
      SourceMap getInstance() {
        throw new UnsupportedOperationException();
      }
    };
    SourceMap sourceMapInvalid = invalidFormat.getInstance();
  }

  @Test
  public void testAddMapping() {
    // Add a mapping
    Node node = mock(Node.class);
    FilePosition outputStartPosition = mock(FilePosition.class);
    FilePosition outputEndPosition = mock(FilePosition.class);
    String sourceFile = "test.js";
    when(node.getSourceFileName()).thenReturn(sourceFile);
    when(node.getLineno()).thenReturn(1);
    when(node.getCharno()).thenReturn(1);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    // Verify that the generator received the mapping
    FilePosition expectedOutputStartPosition = new FilePosition(1, 1);
    FilePosition expectedOutputEndPosition = new FilePosition(1, 1);
    when(generatorMock.addMapping(eq(sourceFile), eq("test.js"), any(FilePosition.class), eq(outputStartPosition), eq(outputEndPosition)))
        .thenReturn(true);
    verify(generatorMock).addMapping(eq(sourceFile), eq("test.js"), any(FilePosition.class), eq(outputStartPosition), eq(outputEndPosition));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddMapping_NullNode() {
    Node node = null;
    FilePosition outputStartPosition = mock(FilePosition.class);
    FilePosition outputEndPosition = mock(FilePosition.class);
    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddMapping_NullOutputStartPosition() {
    Node node = mock(Node.class);
    FilePosition outputStartPosition = null;
    FilePosition outputEndPosition = mock(FilePosition.class);
    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddMapping_NullOutputEndPosition() {
    Node node = mock(Node.class);
    FilePosition outputStartPosition = mock(FilePosition.class);
    FilePosition outputEndPosition = null;
    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
  }

  @Test
  public void testAppendTo() throws IOException {
    // Given a string to write to
    String expectedOutput = "test";
    Appendable out = mock(Appendable.class);
    when(out.append(eq("test"))).thenReturn(out);

    // When appending to the source map
    sourceMap.appendTo(out, "test.js");

    // Then the generator should have appended the string
    verify(generatorMock).appendTo(eq("test.js"), eq("test"));
  }

  @Test
  public void testReset() {
    // When resetting the source map
    sourceMap.reset();

    // Then the generator should have been reset
    verify(generatorMock).reset();
  }

  @Test
  public void testSetStartingPosition() {
    // Given a starting position
    int offsetLine = 1;
    int offsetIndex = 1;

    // When setting the starting position
    sourceMap.setStartingPosition(offsetLine, offsetIndex);

    // Then the generator should have been set the starting position
    verify(generatorMock).setStartingPosition(offsetLine, offsetIndex);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetStartingPosition_NegativeOffsetLine() {
    int offsetLine = -1;
    int offsetIndex = 1;
    sourceMap.setStartingPosition(offsetLine, offsetIndex);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetStartingPosition_NegativeOffsetIndex() {
    int offsetLine = 1;
    int offsetIndex = -1;
    sourceMap.setStartingPosition(offsetLine, offsetIndex);
  }

  @Test
  public void testSetWrapperPrefix() {
    // Given a wrapper prefix
    String prefix = "test";

    // When setting the wrapper prefix
    sourceMap.setWrapperPrefix(prefix);

    // Then the generator should have been set the wrapper prefix
    verify(generatorMock).setWrapperPrefix(prefix);
  }

  @Test
  public void testValidate() {
    // When validating the source map
    sourceMap.validate(true);

    // Then the generator should have been validated
    verify(generatorMock).validate(true);
  }

  @Test
  public void testSetPrefixMappings() {
    // Given a list of prefix mappings
    List<LocationMapping> sourceMapLocationMappings = Collections.singletonList(new LocationMapping("oldPath", "newPath"));

    // When setting the prefix mappings
    sourceMap.setPrefixMappings(sourceMapLocationMappings);

    // Then the prefix mappings should have been set
    verify(generatorMock).setPrefixMappings(sourceMapLocationMappings);
  }

  @Test
  public void testFixupSourceLocation_NoPrefixMappings() {
    // Given a source file without any prefix mappings
    String sourceFile = "test.js";

    // When fixing up the source location
    String expected = sourceFile;
    String fixed = sourceMap.fixupSourceLocation(sourceFile);

    // Then the fixed source file should be the same as the original source file
    assertEquals(expected, fixed);
  }

  @Test
  public void testFixupSourceLocation_PrefixMappingsFound() {
    // Given a source file with a prefix mapping
    List<LocationMapping> sourceMapLocationMappings = Collections.singletonList(new LocationMapping("oldPath", "newPath"));
    sourceMap.setPrefixMappings(sourceMapLocationMappings);

    // Given a source file with the prefix
    String sourceFile = "oldPath/test.js";

    // When fixing up the source location
    String expected = "newPath/test.js";
    String fixed = sourceMap.fixupSourceLocation(sourceFile);

    // Then the fixed source file should be the expected one
    assertEquals(expected, fixed);
  }
}