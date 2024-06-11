import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.debugging.sourcemap.SourceMap;
import com.google.javascript.rhino.Node;
import com.google.debugging.sourcemap.SourceMapGenerator;
import com.google.debugging.sourcemap.FilePosition;

import java.util.Arrays;
import java.util.List;

public class SourceMapTest {

  private SourceMap sourceMap;
  private SourceMapGenerator generator;

  @Before
  public void setUp() {
    generator = mock(SourceMapGenerator.class);
    sourceMap = new SourceMap(generator);
  }

  @Test
  public void testAddMapping_withValidNode_shouldAddMapping() {
    Node node = mock(Node.class);
    FilePosition startPosition = mock(FilePosition.class);
    FilePosition endPosition = mock(FilePosition.class);

    when(node.getSourceFileName()).thenReturn("file.js");
    when(node.getLineno()).thenReturn(1);
    when(node.getCharno()).thenReturn(1);
    when(node.getProp(Node.ORIGINALNAME_PROP)).thenReturn("originalName");

    sourceMap.addMapping(node, startPosition, endPosition);

    verify(generator).addMapping("file.js", "originalName", startPosition, endPosition);
  }

  @Test
  public void testAddMapping_withNodeWithNoSourceFile_shouldNotAddMapping() {
    Node node = mock(Node.class);
    FilePosition startPosition = mock(FilePosition.class);
    FilePosition endPosition = mock(FilePosition.class);

    when(node.getSourceFileName()).thenReturn(null);

    sourceMap.addMapping(node, startPosition, endPosition);

    verify(generator, never()).addMapping(anyString(), anyString(), any(FilePosition.class), any(FilePosition.class));
  }

  @Test
  public void testAddMapping_withNodeWithNegativeLineNumber_shouldNotAddMapping() {
    Node node = mock(Node.class);
    FilePosition startPosition = mock(FilePosition.class);
    FilePosition endPosition = mock(FilePosition.class);

    when(node.getSourceFileName()).thenReturn("file.js");
    when(node.getLineno()).thenReturn(-1);

    sourceMap.addMapping(node, startPosition, endPosition);

    verify(generator, never()).addMapping(anyString(), anyString(), any(FilePosition.class), any(FilePosition.class));
  }

  @Test
  public void testFixupSourceLocation_withNoPrefixMappings_shouldReturnSameSourceFile() {
    String sourceFile = "file.js";

    String fixed = sourceMap.fixupSourceLocation(sourceFile);

    assertEquals(sourceFile, fixed);
  }

  @Test
  public void testFixupSourceLocation_withMatchingPrefixMapping_shouldReturnFixedSourceFile() {
    String sourceFile = "path/to/file.js";
    String prefix = "path/to/";
    String replacement = "new/path/";

    List<SourceMap.LocationMapping> prefixMappings = Arrays.asList(new SourceMap.LocationMapping(prefix, replacement));
    sourceMap.setPrefixMappings(prefixMappings);

    String fixed = sourceMap.fixupSourceLocation(sourceFile);

    assertEquals("new/path/file.js", fixed);
  }

  @Test
  public void testFixupSourceLocation_withNoMatchingPrefixMappings_shouldReturnSameSourceFile() {
    String sourceFile = "file.js";
    String prefix = "path/to/";
    String replacement = "new/path/";

    List<SourceMap.LocationMapping> prefixMappings = Arrays.asList(new SourceMap.LocationMapping(prefix, replacement));
    sourceMap.setPrefixMappings(prefixMappings);

    String fixed = sourceMap.fixupSourceLocation(sourceFile);

    assertEquals(sourceFile, fixed);
  }

  @Test
  public void testAppendTo_withValidAppendableAndName_shouldAppendToAppendable() {
    Appendable out = mock(Appendable.class);
    String name = "name";

    sourceMap.appendTo(out, name);

    verify(generator).appendTo(out, name);
  }

  @Test
  public void testReset_shouldResetGeneratorAndClearSourceLocationFixupCache() {
    sourceMap.reset();

    verify(generator).reset();
    assertTrue(sourceMap.sourceLocationFixupCache.isEmpty());
  }

  @Test
  public void testSetStartingPosition_shouldSetGeneratorStartingPosition() {
    int offsetLine = 1;
    int offsetIndex = 1;

    sourceMap.setStartingPosition(offsetLine, offsetIndex);

    verify(generator).setStartingPosition(offsetLine, offsetIndex);
  }

  @Test
  public void testSetWrapperPrefix_shouldSetGeneratorWrapperPrefix() {
    String prefix = "prefix";

    sourceMap.setWrapperPrefix(prefix);

    verify(generator).setWrapperPrefix(prefix);
  }

  @Test
  public void testValidate_shouldSetGeneratorValidate() {
    boolean validate = true;

    sourceMap.validate(validate);

    verify(generator).validate(validate);
  }
}