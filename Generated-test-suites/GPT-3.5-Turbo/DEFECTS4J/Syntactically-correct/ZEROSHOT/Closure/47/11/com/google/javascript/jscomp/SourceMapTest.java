import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import com.google.debugging.sourcemap.FilePosition;
import com.google.debugging.sourcemap.SourceMapFormat;
import com.google.debugging.sourcemap.SourceMapGenerator;
import com.google.debugging.sourcemap.SourceMapGeneratorFactory;
import com.google.debugging.sourcemap.SourceMapGeneratorV1;
import com.google.debugging.sourcemap.SourceMapGeneratorV2;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SourceMapTest {

  private SourceMap sourceMap;
  
  @Before
  public void setUp() {
    sourceMap = mock(SourceMap.class);
  }

  @Test
  public void testAddMapping() {
    Node node = mock(Node.class);
    FilePosition outputStartPosition = mock(FilePosition.class);
    FilePosition outputEndPosition = mock(FilePosition.class);

    when(node.getSourceFileName()).thenReturn("filename.js");
    when(node.getLineno()).thenReturn(1);
    when(node.getCharno()).thenReturn(1);
    
    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    verify(sourceMap, times(1)).addMapping(node, outputStartPosition, outputEndPosition);
  }

  @Test
  public void testFixupSourceLocation() {
    String sourceFile = "//maps/file.js";

    List<LocationMapping> prefixMappings = Collections.singletonList(
        new SourceMap.LocationMapping("//maps/", "/source/")
    );

    sourceMap.setPrefixMappings(prefixMappings);

    String fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);

    assertEquals("/source/file.js", fixedSourceFile);
  }

  @Test
  public void testAppendTo() throws IOException {
    Appendable out = mock(Appendable.class);
    String name = "testSourceMap";

    sourceMap.appendTo(out, name);

    verify(sourceMap, times(1)).appendTo(out, name);
  }

  @Test
  public void testReset() {
    sourceMap.reset();
    
    verify(sourceMap, times(1)).reset();
  }

  @Test
  public void testSetStartingPosition() {
    int offsetLine = 0;
    int offsetIndex = 0;

    sourceMap.setStartingPosition(offsetLine, offsetIndex);
    
    verify(sourceMap, times(1)).setStartingPosition(offsetLine, offsetIndex);
  }

  @Test
  public void testSetWrapperPrefix() {
    String prefix = "wrapper_";

    sourceMap.setWrapperPrefix(prefix);

    verify(sourceMap, times(1)).setWrapperPrefix(prefix);
  }

  @Test
  public void testValidate() {
    boolean validate = true;

    sourceMap.validate(validate);

    verify(sourceMap, times(1)).validate(validate);
  }

  @Test
  public void testSetPrefixMappings() {
    List<LocationMapping> prefixMappings = Collections.singletonList(
        new SourceMap.LocationMapping("//maps/", "/source/")
    );

    sourceMap.setPrefixMappings(prefixMappings);

    verify(sourceMap, times(1)).setPrefixMappings(prefixMappings);
  }
}