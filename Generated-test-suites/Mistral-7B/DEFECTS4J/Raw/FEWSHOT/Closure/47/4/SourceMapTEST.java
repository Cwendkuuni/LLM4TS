package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
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
import com.google.javascript.rhino.Node;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class SourceMapTest {

  private SourceMap generator;
  private ByteArrayOutputStream outputStream;

  @Before
  public void setUp() {
    SourceMapGeneratorFactory factory = mock(SourceMapGeneratorFactory.class);
    when(factory.getInstance(SourceMapFormat.V1)).thenReturn(new SourceMapGeneratorV1());
    when(factory.getInstance(SourceMapFormat.DEFAULT)).thenReturn(new SourceMapGeneratorV2());
    when(factory.getInstance(SourceMapFormat.V2)).thenReturn(new SourceMapGeneratorV2());

    this.generator = new SourceMap(factory);
    this.outputStream = new ByteArrayOutputStream();
  }

  @Test
  public void testAddMapping() {
    String sourceFile = "test.js";
    Node node = mock(Node.class);
    FilePosition outputStartPosition = new FilePosition(1, 2);
    FilePosition outputEndPosition = new FilePosition(1, 3);

    this.generator.addMapping(node, outputStartPosition, outputEndPosition);

    SourceMap actualSourceMap = this.generator.getInstance();
    List<com.google.debugging.sourcemap.Mapping> mappings = actualSourceMap.getSourceContent();

    assertEquals(1, mappings.size());
    com.google.debugging.sourcemap.Mapping mapping = mappings.get(0);
    assertEquals(sourceFile, mapping.getSource());
    assertEquals("", mapping.getName());
    assertEquals(outputStartPosition.getLine(), mapping.getLine());
    assertEquals(outputStartPosition.getColumn(), mapping.getColumn());
    assertEquals(outputEndPosition.getLine(), mapping.getEndLine());
    assertEquals(outputEndPosition.getColumn(), mapping.getEndColumn());
  }

  @Test
  public void testFixUpSourceLocation() {
    String sourceFile = "test.js";
    String fixedSourceFile = "test_fixed.js";

    List<LocationMapping> prefixMappings = Lists.newArrayList(
        new LocationMapping("test.js", "test_fixed.js")
    );

    this.generator.setPrefixMappings(prefixMappings);

    String result = this.generator.fixupSourceLocation(sourceFile);

    assertEquals(fixedSourceFile, result);

    result = this.generator.fixupSourceLocation("/test.js");

    assertEquals("/test_fixed.js", result);
  }

  @Test
  public void testFixUpSourceLocationWithNoPrefixMapping() {
    String sourceFile = "test.js";

    this.generator.setPrefixMappings(Collections.emptyList());

    String result = this.generator.fixupSourceLocation(sourceFile);

    assertEquals(sourceFile, result);
  }

  @Test
  public void testValidate() {
    this.generator.validate(true);
  }

  @Test(expected = IllegalStateException.class)
  public void testValidateWithInvalidFormat() {
    SourceMapGeneratorFactory factory = mock(SourceMapGeneratorFactory.class);
    when(factory.getInstance(SourceMapFormat.INVALID_FORMAT)).thenReturn(null);

    SourceMap generator = new SourceMap(factory);

    generator.validate(true);
  }

  @Test
  public void testSetStartingPosition() {
    int lineNumber = 1;
    int characterIndex = 2;

    this.generator.setStartingPosition(lineNumber, characterIndex);

    FilePosition expectedStartingPosition = new FilePosition(lineNumber, characterIndex);

    FilePosition actualStartingPosition = this.generator.getStartingPosition();

    assertEquals(expectedStartingPosition.getLine(), actualStartingPosition.getLine());
    assertEquals(expectedStartingPosition.getColumn(), actualStartingPosition.getColumn());
  }

  @Test
  public void testSetWrapperPrefix() {
    String wrapperPrefix = "foo";

    this.generator.setWrapperPrefix(wrapperPrefix);

    String actualWrapperPrefix = this.generator.getWrapperPrefix();

    assertEquals(wrapperPrefix, actualWrapperPrefix);
  }

  @Test
  public void testIsDetailLevel() {
    Node node = mock(Node.class);

    DetailLevel detailLevelAll = DetailLevel.ALL;
    DetailLevel detailLevelSymbols = DetailLevel.SYMBOLS;

    assertTrue(detailLevelAll.apply(node));
    assertTrue(detailLevelSymbols.apply(node));

    Node nodeWithFunction = mock(Node.class);
    when(nodeWithFunction.isFunction()).thenReturn(true);

    assertFalse(detailLevelSymbols.apply(nodeWithFunction));
    assertTrue(detailLevelAll.apply(nodeWithFunction));
  }

  @Test
  public void testAppendTo() throws IOException {
    String name = "test.js.map";

    this.generator.appendTo(this.outputStream, name);

    String expectedSourceMap = "sourceMappingURL=data:application/json;base64,eyJ2ZXJpZmllZCI6ICIkIiwic291cmNlcyI6W3sicGF0RmlldmVycyI6W3siaWF0IjoiIiwic291cmNlUmFtcGxlLWltYWdlLW1lc3NhZ2UiOiAiSFMyXSx7CmFkYXkpOwoiLCJuYW1lIjoiIiwic291cmNlcnNpb24iOiAiSFMyXSx7CkFkbWluZCwic291cmNlUmFtcGxlLXdpZHRoIjoiMywic291cmNlcnNpb24iOiAiSFMyXSx7CmFkYXkpOwoic291cmNlcnNpb24iOiAiSFMyXSx7CmFkYXkpOwoiLCJmaWxlIjoiIiwic291cmNlcnNpb24iOiAiSFMyXSx7CmFkYXkpOwoiLCJmaWxlIjpmYWxzZSwibmFtZSI6Ik1hcmtlb3MifQ==";

    String actualSourceMap = new String(this.outputStream.toByteArray());

    assertEquals(expectedSourceMap, actualSourceMap);
  }

  @Test
  public void testReset() {
    this.generator.validate(true);
    this.generator.reset();

    assertTrue(this.generator.isValid());
  }
}