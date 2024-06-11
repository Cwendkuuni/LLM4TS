import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Predicate;
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
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SourceMapTest {

  private SourceMap sourceMap;
  private SourceMapGenerator generatorMock;

  @Before
  public void setUp() {
    SourceMapFormat formatV1 = mock(SourceMapFormat.class);
    SourceMapGenerator generatorV1 = new SourceMapGeneratorV1(formatV1);
    SourceMapGeneratorFactory factoryV1 = mock(SourceMapGeneratorFactory.class);
    when(factoryV1.getInstance(SourceMapFormat.V1)).thenReturn(generatorV1);

    SourceMapFormat formatV2 = mock(SourceMapFormat.class);
    SourceMapGenerator generatorV2 = new SourceMapGeneratorV2(formatV2);
    SourceMapGeneratorFactory factoryV2 = mock(SourceMapGeneratorFactory.class);
    when(factoryV2.getInstance(SourceMapFormat.DEFAULT)).thenReturn(generatorV2);

    generatorMock = mock(SourceMapGenerator.class);
    when(generatorMock.appendTo(Mockito.any(Appendable.class), Mockito.anyString())).thenReturn(true);
    when(generatorMock.validate(Mockito.anyBoolean())).thenReturn(true);
    when(generatorMock.reset()).thenReturn(true);
    when(generatorMock.addMapping(
        Mockito.anyString(),
        Mockito.anyString(),
        Mockito.any(FilePosition.class),
        Mockito.any(FilePosition.class))).thenReturn(true);
    when(generatorMock.setStartingPosition(Mockito.anyInt(), Mockito.anyInt()))
        .thenReturn(true);
    when(generatorMock.setWrapperPrefix(Mockito.anyString())).thenReturn(true);

    sourceMap = new SourceMap(factoryV1);
    sourceMap.setPrefixMappings(Collections.emptyList());
  }

  @Test
  public void testInstance_V1() {
    SourceMap sourceMapV1 = SourceMap.Format.V1.getInstance().getInstance();
    assertSame(SourceMap.class, sourceMapV1.getClass());
  }

  @Test
  public void testInstance_Default() {
    SourceMap sourceMapDefault = SourceMap.Format.DEFAULT.getInstance().getInstance();
    assertSame(SourceMap.class, sourceMapDefault.getClass());
  }

  @Test
  public void testInstance_V2() {
    SourceMap sourceMapV2 = SourceMap.Format.V2.getInstance().getInstance();
    assertSame(SourceMap.class, sourceMapV2.getClass());
  }

  @Test
  public void testInstance_V3() {
    SourceMap sourceMapV3 = SourceMap.Format.V3.getInstance().getInstance();
    assertSame(SourceMap.class, sourceMapV3.getClass());
  }

  @Test
  public void testAddMapping_NullNode() {
    sourceMap.addMapping(null, new FilePosition(1, 1), new FilePosition(2, 2));
    assertFalse(sourceMap.generator.hasMappings());
  }

  @Test
  public void testAddMapping_Valid() {
    Node node = mock(Node.class);
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(2, 2);
    String sourceFile = "test.js";

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
    when(node.getSourceFileName()).thenReturn(sourceFile);

    assertTrue(sourceMap.generator.hasMappings());
  }

  @Test
  public void testAddMapping_EmptySourceFile() {
    Node node = mock(Node.class);
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(2, 2);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    assertTrue(sourceMap.generator.hasMappings());
  }

  @Test
  public void testAddMapping_NegativeLineNumber() {
    Node node = mock(Node.class);
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(2, 2);
    node.setLineno(-1);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
    assertFalse(sourceMap.generator.hasMappings());
  }

  @Test(expected = NullPointerException.class)
  public void testAddMapping_NullSourceFile() {
    Node node = mock(Node.class);
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(2, 2);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
    when(node.getSourceFileName()).thenReturn(null);
  }

  @Test
  public void testFixupSourceLocation_NoPrefixMappings() {
    String sourceFile = "test.js";
    sourceMap.addMapping(mock(Node.class), new FilePosition(1, 1), new FilePosition(2, 2));

    String fixed = sourceMap.fixupSourceLocation(sourceFile);
    assertEquals(sourceFile, fixed);
  }

  @Test
  public void testFixupSourceLocation_PrefixMatchFound() {
    String sourceFile = "src/test.js";
    String prefix = "src/";
    LocationMapping locationMapping = new LocationMapping(prefix, "");

    when(sourceMap.prefixMappings.size()).thenReturn(1);
    when(sourceMap.prefixMappings.get(0)).thenReturn(locationMapping);

    String fixed = sourceMap.fixupSourceLocation(sourceFile);
    assertEquals("test.js", fixed);
  }

  @Test
  public void testAppendTo() {
    Appendable appendable = mock(Appendable.class);
    String name = "output.js.map";

    assertTrue(sourceMap.appendTo(appendable, name));
  }

  @Test
  public void testReset() {
    assertTrue(sourceMap.generator.hasMappings());
    sourceMap.reset();
    assertFalse(sourceMap.generator.hasMappings());
  }

  @Test
  public void testSetStartingPosition() {
    int offsetLine = 1;
    int offsetIndex = 2;

    assertTrue(sourceMap.generator.hasMappings());
    sourceMap.setStartingPosition(offsetLine, offsetIndex);
    assertFalse(sourceMap.generator.hasMappings());
  }

  @Test
  public void testSetWrapperPrefix() {
    String prefix = "test";

    sourceMap.setWrapperPrefix(prefix);
    assertEquals(prefix, sourceMap.generator.getWrapperPrefix());
  }

  @Test
  public void testValidate() {
    assertTrue(sourceMap.generator.validate(true));
  }

  @Test
  public void testAddMapping_WithPrefixMappings() {
    String sourceFile = "src/test.js";
    String prefix = "src/";
    LocationMapping locationMapping = new LocationMapping(prefix, "");

    when(sourceMap.prefixMappings.size()).thenReturn(1);
    when(sourceMap.prefixMappings.get(0)).thenReturn(locationMapping);

    Node node = mock(Node.class);
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(2, 2);

    sourceMap.setPrefixMappings(Lists.newArrayList(locationMapping));
    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    String fixed = sourceMap.fixupSourceLocation(sourceFile);
    assertEquals("test.js", fixed);
  }

  @Test
  public void testGetInstance_InvalidFormat() {
    assertNull(SourceMap.Format.INVALID.getInstance().getInstance());
  }
}