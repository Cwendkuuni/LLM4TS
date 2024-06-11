import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.google.common.base.Predicate;
import com.google.common.collect.Maps;
import com.google.debugging.sourcemap.FilePosition;
import com.google.debugging.sourcemap.SourceMapFormat;
import com.google.debugging.sourcemap.SourceMapGenerator;
import com.google.debugging.sourcemap.SourceMapGeneratorFactory;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.NodeFactory;

public class SourceMapTest {

  @Test
  public void testGetInstance() {
    for (SourceMap.Format format : SourceMap.Format.values()) {
      SourceMap sourceMap = format.getInstance();
      assertNotNull(sourceMap);
    }
  }

  @Test
  public void testAddMapping() throws IOException {
    SourceMapGenerator mockGenerator = mock(SourceMapGenerator.class);
    SourceMap sourceMap = new SourceMap(mockGenerator);

    String sourceFile = "test.js";
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 2);
    Node node = NodeFactory.getRoot();

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    String expectedSourceFile = fixupSourceLocation(sourceFile);
    when(mockGenerator.addMapping(assertEquals(expectedSourceFile),
        eq("originalName"),
        eq(outputStartPosition),
        eq(outputEndPosition)))
        .thenReturn(null);

    sourceMap.appendTo(createByteArrayOutputStream(), "name");

    sourceMap.validate(true);
  }

  @Test(expected = IOException.class)
  public void testAddMappingWithNullSourceFile() {
    SourceMapGenerator mockGenerator = mock(SourceMapGenerator.class);
    SourceMap sourceMap = new SourceMap(mockGenerator);

    Node node = NodeFactory.getRoot();
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 2);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
  }

  @Test
  public void testFixupSourceLocation() {
    List<LocationMapping> prefixMappings = Collections.singletonList(
        new SourceMap.LocationMapping("test/", "src/"));
    SourceMap sourceMap = new SourceMap(mock(SourceMapGenerator.class)) {
      @Override public List<LocationMapping> prefixMappings() {
        return prefixMappings;
      }
    };

    String sourceFile = "test/test.js";
    String expectedSourceFile = "src/test.js";

    assertEquals(expectedSourceFile, sourceMap.fixupSourceLocation(sourceFile));
  }

  @Test
  public void testFixupSourceLocationWithNoPrefixMappings() {
    SourceMap sourceMap = new SourceMap(mock(SourceMapGenerator.class));

    String sourceFile = "test.js";

    assertEquals(sourceFile, sourceMap.fixupSourceLocation(sourceFile));
  }

  @Test
  public void testAppendTo() throws IOException {
    SourceMapGenerator mockGenerator = mock(SourceMapGenerator.class);
    SourceMap sourceMap = new SourceMap(mockGenerator);

    String name = "test.js.map";
    byte[] expectedBytes = new byte[] {
      0x1, 0x2, 0x3, // ...
    };

    when(mockGenerator.appendTo(anyString(), eq(name)))
        .thenReturn(createByteArrayOutputStream());

    sourceMap.appendTo(createByteArrayOutputStream(), name);

    sourceMap.validate(true);

    assertEquals(expectedBytes, createByteArrayOutputStream().toByteArray());
  }

  @Test
  public void testReset() {
    SourceMap generator = new SourceMap(mock(SourceMapGenerator.class));
    generator.reset();

    assertNotNull(generator.getGenerator());
    assertTrue(generator.getGenerator().isEmpty());
  }

  @Test
  public void testSetStartingPosition() {
    SourceMapGenerator mockGenerator = mock(SourceMapGenerator.class);
    SourceMap sourceMap = new SourceMap(mockGenerator);

    sourceMap.setStartingPosition(1, 1);

    when(mockGenerator.setStartingPosition(eq(1), eq(1)))
        .thenReturn(mockGenerator);

    sourceMap.validate(true);
  }

  @Test
  public void testSetWrapperPrefix() {
    SourceMapGenerator mockGenerator = mock(SourceMapGenerator.class);
    SourceMap sourceMap = new SourceMap(mockGenerator);

    sourceMap.setWrapperPrefix("test_");

    when(mockGenerator.setWrapperPrefix(eq("test_")))
        .thenReturn(mockGenerator);

    sourceMap.validate(true);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValidateWithNullGenerator() {
    SourceMap sourceMap = new SourceMap(null);
    sourceMap.validate(true);
  }

  @Test
  public void testValidateWithEmptyGenerator() {
    SourceMapGenerator mockGenerator = mock(SourceMapGenerator.class);
    when(mockGenerator.isEmpty()).thenReturn(true);

    SourceMap sourceMap = new SourceMap(mockGenerator);
    sourceMap.validate(true);
  }

  private byte[] toByteArray(List<LocationMapping> sourceMapLocationMappings) {
    SourceMap sourceMap = new SourceMap(mock(SourceMapGenerator.class)) {
      @Override public List<LocationMapping> prefixMappings() {
        return sourceMapLocationMappings;
      }
    };

    byte[] bytes = new byte[10];
    sourceMap.appendTo(createByteArrayOutputStream(), "name");

    return createByteArrayOutputStream().toByteArray();
  }

  private void assertEquals(String expectedSourceFile,
      SourceMapGenerator.AddMappingCallback callback) {
    when(callback.addMapping(eq(expectedSourceFile), anyString(),
        any(FilePosition.class), any(FilePosition.class)))
        .thenReturn(null);
  }

  private String fixupSourceLocation(String sourceFile) {
    // Implementation based on the code in the SourceMap class.
    if (prefixMappings.isEmpty()) {
      return sourceFile;
    }

    String fixed = sourceLocationFixupCache.get(sourceFile);
    if (fixed != null) {
      return fixed;
    }

    for (LocationMapping mapping : prefixMappings) {
      if (sourceFile.startsWith(mapping.prefix)) {
        fixed = mapping.replacement + sourceFile.substring(
            mapping.prefix.length());
        break;
      }
    }

    if (fixed == null) {
      fixed = sourceFile;
    }

    sourceLocationFixupCache.put(sourceFile, fixed);
    return fixed;
  }

  private Appendable createByteArrayOutputStream() {
    return new ByteArrayOutputStream();
  }
}