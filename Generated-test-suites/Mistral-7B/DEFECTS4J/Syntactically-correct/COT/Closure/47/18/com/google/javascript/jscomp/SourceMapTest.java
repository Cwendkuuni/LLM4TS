import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.debugging.sourcemap.FilePosition;
import com.google.debugging.sourcemap.SourceMapFormat;
import com.google.debugging.sourcemap.SourceMapGenerator;
import com.google.debugging.sourcemap.SourceMapGeneratorFactory;
import com.google.debugging.sourcemap.SourceMapGeneratorV1;
import com.google.debugging.sourcemap.SourceMapGeneratorV2;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class SourceMapTest {

  private SourceMap sourceMap;
  private SourceMapGenerator generatorMock;

  @Before
  public void setup() {
    generatorMock = mock(SourceMapGenerator.class);
    SourceMapGeneratorFactory factoryMock = mock(SourceMapGeneratorFactory.class);
    when(factoryMock.getInstance(SourceMapFormat.V1)).thenReturn(generatorMock);

    sourceMap = new SourceMap(factoryMock);
  }

  @Test
  public void testSetPrefixMappings() {
    List<LocationMapping> mappings = Lists.newArrayList(
        new LocationMapping("testPrefix", "testReplacement"));
    sourceMap.setPrefixMappings(mappings);
    assertNotNull(sourceMap.prefixMappings);
  }

  @Test
  public void testAddMapping_NullSourceFile() {
    Node node = mock(Node.class);
    FilePosition outputStartPosition = mock(FilePosition.class);
    FilePosition outputEndPosition = mock(FilePosition.class);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    // No operation since sourceFile is null
    // No assertion needed as no exception is expected
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddMapping_InvalidNode() {
    Node node = null;
    FilePosition outputStartPosition = mock(FilePosition.class);
    FilePosition outputEndPosition = mock(FilePosition.class);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddMapping_NullSourceFileNode() {
    String sourceFile = null;
    Node node = mock(Node.class);
    FilePosition outputStartPosition = mock(FilePosition.class);
    FilePosition outputEndPosition = mock(FilePosition.class);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition, sourceFile);
  }

  @Test
  public void testAddMapping() throws IOException {
    Node node = mock(Node.class);
    FilePosition outputStartPosition = mock(FilePosition.class);
    FilePosition outputEndPosition = mock(FilePosition.class);
    String sourceFile = "testSourceFile.js";

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition, sourceFile);

    when(generatorMock.addMapping(
        eq("testSourceFile.js"), eq("originalName"),
        any(FilePosition.class), eq(outputStartPosition),
        eq(outputEndPosition)))
        .thenReturn(true);

    assertTrue(sourceMap.generator.isAddedMapping(sourceFile, "originalName"));
  }

  @Test
  public void testValidate() {
    sourceMap.validate(true);

    // No assertion needed as no exception is expected
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValidate_InvalidBoolean() {
    sourceMap.validate("invalidBoolean");
  }

  @Test
  public void testAppendTo() throws IOException {
    String name = "testFile.js";
    Appendable out = mock(Appendable.class);

    sourceMap.appendTo(out, name);

    when(generatorMock.appendTo(out, name)).thenReturn(true);

    assertTrue(sourceMap.generator.isAppendedTo(name));
  }

  @Test
  public void testReset() {
    sourceMap.reset();

    // No assertion needed as no exception is expected
  }

  @Test
  public void testSetStartingPosition() {
    sourceMap.setStartingPosition(1, 1);

    // No assertion needed as no exception is expected
  }

  @Test
  public void testSetWrapperPrefix() {
    sourceMap.setWrapperPrefix("testWrapperPrefix");

    // No assertion needed as no exception is expected
  }

  private boolean eq(Object expected) {
    return expected != null && expected.equals(expected);
  }

  private boolean isAddedMapping(SourceMap sourceMap, String sourceFile,
      String originalName) {
    if (sourceMap.generator.getMappings().isEmpty()) {
      return false;
    }

    for (Map.Entry<String, List<Map<String, Object>>> entry :
        sourceMap.generator.getMappings().entrySet()) {
      for (Map<String, Object> mapping : entry.getValue()) {
        if (mapping.get("source").equals(sourceFile)
            && mapping.get("name").equals(originalName)) {
          return true;
        }
      }
    }

    return false;
  }

  private void assertTrue(boolean condition) {
    if (!condition) {
      fail();
    }
  }
}