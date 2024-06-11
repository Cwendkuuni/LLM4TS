import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class SourceMapTest {

  private SourceMap sourceMap;
  private SourceMapGenerator generatorMock;
  private Appendable outputStreamMock;

  @Before
  public void setUp() {
    generatorMock = mock(SourceMapGenerator.class);
    outputStreamMock = mock(Appendable.class);

    sourceMap = new SourceMap(generatorMock);
  }

  @Test
  public void testInstanceV1() {
    SourceMap sourceMapV1 = SourceMap.Format.V1.getInstance().getInstance();
    assertNotNull(sourceMapV1);
  }

  @Test
  public void testInstanceDefault() {
    SourceMap sourceMapDefault = SourceMap.Format.DEFAULT.getInstance().getInstance();
    assertNotNull(sourceMapDefault);
  }

  @Test
  public void testInstanceV2() {
    SourceMap sourceMapV2 = SourceMap.Format.V2.getInstance().getInstance();
    assertNotNull(sourceMapV2);
  }

  @Test
  public void testInstanceV3() {
    SourceMap sourceMapV3 = SourceMap.Format.V3.getInstance().getInstance();
    assertNotNull(sourceMapV3);
  }

  @Test
  public void testAddMapping() throws IOException {
    when(generatorMock.addMapping(
        anyString(), anyString(), any(FilePosition.class),
        any(FilePosition.class))).thenReturn(true);

    Node nodeMock = mock(Node.class);
    FilePosition outputStartPosition = mock(FilePosition.class);
    FilePosition outputEndPosition = mock(FilePosition.class);
    String sourceFile = "testSourceFile.js";

    sourceMap.addMapping(nodeMock, outputStartPosition, outputEndPosition);

    sourceMap.appendTo(outputStreamMock, "testOutputFile.js");
    sourceMap.validate(true);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddMappingWithNullSourceFile() {
    when(generatorMock.addMapping(
        anyString(), anyString(), any(FilePosition.class),
        any(FilePosition.class))).thenReturn(true);

    Node nodeMock = mock(Node.class);
    FilePosition outputStartPosition = mock(FilePosition.class);
    FilePosition outputEndPosition = mock(FilePosition.class);

    sourceMap.addMapping(nodeMock, outputStartPosition, outputEndPosition);

    sourceMap.appendTo(outputStreamMock, "testOutputFile.js");
    sourceMap.validate(true);

    String nullSourceFile = null;

    sourceMap.addMapping(nodeMock, outputStartPosition, outputEndPosition);
    sourceMap.addMapping(nodeMock, outputStartPosition, outputEndPosition);
    sourceMap.appendTo(outputStreamMock, "testOutputFile.js");
    sourceMap.validate(true);

    sourceMap.addMapping(nodeMock, outputStartPosition, outputEndPosition);
    sourceMap.addMapping(nodeMock, outputStartPosition, outputEndPosition);
    sourceMap.addMapping(nullSourceFile, outputStartPosition, outputEndPosition);
    sourceMap.appendTo(outputStreamMock, "testOutputFile.js");
    sourceMap.validate(true);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddMappingWithNegativeLineno() {
    when(generatorMock.addMapping(
        anyString(), anyString(), any(FilePosition.class),
        any(FilePosition.class))).thenReturn(true);

    Node nodeMock = mock(Node.class);
    FilePosition outputStartPosition = mock(FilePosition.class);
    FilePosition outputEndPosition = mock(FilePosition.class);

    sourceMap.addMapping(nodeMock, outputStartPosition, outputEndPosition);

    sourceMap.appendTo(outputStreamMock, "testOutputFile.js");
    sourceMap.validate(true);

    Node nodeMockWithNegativeLineno = mock(Node.class);
    nodeMockWithNegativeLineno.setLineno(-1);

    sourceMap.addMapping(nodeMockWithNegativeLineno, outputStartPosition, outputEndPosition);
  }

  @Test
  public void testFixupSourceLocation() throws IOException {
    String originalSourceFile = "originalSourceFile.js";
    String prefixMappingString = "prefixMapping:";
    String replacementMappingString = "replacementMapping:";
    List<LocationMapping> prefixMappings = Lists.newArrayList(
        new LocationMapping(prefixMappingString, replacementMappingString));

    sourceMap.setPrefixMappings(prefixMappings);
    when(generatorMock.addMapping(
        anyString(), anyString(), any(FilePosition.class),
        any(FilePosition.class))).thenReturn(true);

    String sourceFile = originalSourceFile;

    String fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);

    assertNotNull(fixedSourceFile);
    assertEquals(originalSourceFile, fixedSourceFile);

    sourceFile = prefixMappingString + originalSourceFile;

    fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);

    assertEquals(replacementMappingString + originalSourceFile, fixedSourceFile);
  }

  @Test
  public void testAppendTo() throws IOException {
    when(generatorMock.appendTo(outputStreamMock, anyString())).thenReturn(true);

    sourceMap.appendTo(outputStreamMock, "testOutputFile.js");

    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    Appendable appendable = byteArrayOutputStream;

    sourceMap.appendTo(appendable, "testOutputFile.js");

    String output = byteArrayOutputStream.toString();

    assertNotNull(output);
    assertTrue(output.length() > 0);
  }

  @Test
  public void testReset() {
    sourceMap.reset();

    assertNotNull(sourceMap.generator);
    assertFalse(sourceMap.prefixMappings.isEmpty());
    assertTrue(sourceMap.sourceLocationFixupCache.isEmpty());
  }

  @Test
  public void testSetStartingPosition() {
    sourceMap.setStartingPosition(1, 1);

    assertNotNull(sourceMap.generator);
    assertEquals(1, sourceMap.generator.getStartingLineNumber());
    assertEquals(1, sourceMap.generator.getStartingColumnNumber());
  }

  @Test
  public void testSetWrapperPrefix() {
    String wrapperPrefix = "wrapperPrefix";

    sourceMap.setWrapperPrefix(wrapperPrefix);

    assertNotNull(sourceMap.generator);
    assertEquals(wrapperPrefix, sourceMap.generator.getWrapperPrefix());
  }

  @Test
  public void testValidate() throws IOException {
    when(generatorMock.validate(true)).thenReturn(true);

    sourceMap.validate(true);

    assertNotNull(sourceMap.generator);
    assertTrue(sourceMap.generator.isValid());
  }
}