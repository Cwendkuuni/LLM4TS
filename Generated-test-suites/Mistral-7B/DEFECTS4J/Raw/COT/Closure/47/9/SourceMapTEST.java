package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.debugging.sourcemap.FilePosition;
import com.google.debugging.sourcemap.SourceMapFormat;
import com.google.javascript.jscomp.SourceMap;
import com.google.javascript.jscomp.SourceMap.DetailLevel;
import com.google.javascript.jscomp.SourceMap.Format;
import com.google.javascript.jscomp.SourceMapGenerator;
import com.google.javascript.jscomp.SourceMapGeneratorFactory;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

/**
 * Test class for SourceMap.
 */
public class SourceMapTest {

  private SourceMapGenerator generator;
  private SourceMap sourceMap;
  private ByteArrayOutputStream out;

  @Before
  public void setup() {
    generator = mock(SourceMapGenerator.class);
    out = new ByteArrayOutputStream();
    sourceMap = new SourceMap(SourceMapGeneratorFactory.getInstance(SourceMapFormat.V1));
  }

  @Test
  public void testGetInstance_V1() {
    SourceMap actual = SourceMap.Format.V1.getInstance();
    SourceMap expected = new SourceMap(generator);
    assertNotNull(actual);
    assertNotNull(actual.generator);
    assertInstanceOf(SourceMap.class, actual);
    assertSame(expected.generator, actual.generator);
  }

  @Test
  public void testAddMapping() throws IOException {
    Node node = mock(Node.class);
    FilePosition outputStartPosition = mock(FilePosition.class);
    FilePosition outputEndPosition = mock(FilePosition.class);
    when(node.getSourceFileName()).thenReturn("test.js");

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    ArgumentCaptor<String> sourceFileCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> originalNameCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<FilePosition> inputStartPositionCaptor = ArgumentCaptor.forClass(FilePosition.class);
    ArgumentCaptor<FilePosition> inputEndPositionCaptor = ArgumentCaptor.forClass(FilePosition.class);

    sourceMap.generator.addMapping(sourceFileCaptor.capture(), originalNameCaptor.capture(),
        inputStartPositionCaptor.capture(), outputStartPosition, outputEndPosition);

    String sourceFile = sourceFileCaptor.getValue();
    String originalName = originalNameCaptor.getValue();
    FilePosition inputStartPosition = inputStartPositionCaptor.getValue();

    assertEquals("test.js", sourceFile);
    assertEquals(originalName, null);
    assertEquals(node.getLineno(), inputStartPosition.getLine());
    assertEquals(node.getCharno(), inputStartPosition.getCharacter());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddMapping_NullSourceFileName() {
    Node node = mock(Node.class);
    FilePosition outputStartPosition = mock(FilePosition.class);
    FilePosition outputEndPosition = mock(FilePosition.class);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    when(node.getSourceFileName()).thenReturn(null);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
  }

  @Test
  public void testAddMapping_NegativeLineNumber() {
    Node node = mock(Node.class);
    FilePosition outputStartPosition = mock(FilePosition.class);
    FilePosition outputEndPosition = mock(FilePosition.class);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    when(node.getSourceFileName()).thenReturn("test.js");
    when(node.getLineno()).thenReturn(-1);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
  }

  @Test
  public void testAppendTo() throws IOException {
    String name = "test.js.map";

    sourceMap.appendTo(out, name);

    assertNotNull(out.toString());
  }

  @Test
  public void testReset() {
    sourceMap.reset();

    assertNotNull(sourceMap.generator);
    assertEmpty(sourceMap.prefixMappings);
    assertEmpty(sourceMap.sourceLocationFixupCache);
  }

  @Test
  public void testSetStartingPosition() {
    int offsetLine = 1;
    int offsetIndex = 2;

    sourceMap.setStartingPosition(offsetLine, offsetIndex);

    assertNotNull(sourceMap.generator);
    assertEquals(offsetLine, sourceMap.generator.getStartingPosition().getLine());
    assertEquals(offsetIndex, sourceMap.generator.getStartingPosition().getCharacter());
  }

  @Test
  public void testSetWrapperPrefix() {
    String prefix = "prefix";

    sourceMap.setWrapperPrefix(prefix);

    assertEquals(prefix, sourceMap.generator.getWrapperPrefix());
  }

  @Test
  public void testValidate() {
    sourceMap.validate(true);

    assertNotNull(sourceMap.generator);
    assertTrue(sourceMap.generator.isValidate());
  }

  @Test
  public void testValidate_False() {
    sourceMap.validate(false);

    assertNotNull(sourceMap.generator);
    assertFalse(sourceMap.generator.isValidate());
  }

  @Test
  public void testSetPrefixMappings() {
    List<LocationMapping> sourceMapLocationMappings = Lists.newArrayList(
        new LocationMapping("old", "new"));

    sourceMap.setPrefixMappings(sourceMapLocationMappings);

    assertNotNull(sourceMap.generator);
    assertEquals(sourceMapLocationMappings, sourceMap.prefixMappings);
  }

  private void assertInstanceOf(Class<?> expectedType, Object obj) {
    assertTrue("Expected: " + expectedType + ", but got: " + obj.getClass(),
        expectedType.isInstance(obj));
  }

  private void assertEmpty(List<?> list) {
    assertTrue("Expected an empty list, but got: " + list, list.isEmpty());
  }

  private void assertSame(Object expected, Object actual) {
    assertEquals("Expected: " + expected + ", but got: " + actual, expected, actual);
  }

  private void assertEquals(Object expected, Object actual) {
    assertEquals("Expected: " + expected + ", but got: " + actual, expected, actual);
  }

  private void assertEquals(int expectedInt, int actualInt) {
    assertEquals("Expected: " + expectedInt + ", but got: " + actualInt, expectedInt, actualInt);
  }
}