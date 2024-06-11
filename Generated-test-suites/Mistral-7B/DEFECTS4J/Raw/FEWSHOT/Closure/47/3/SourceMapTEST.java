package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
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
import com.google.javascript.jscomp.SourceMap.Format;
import com.google.javascript.jscomp.SourceMap.DetailLevel;
import com.google.javascript.jscomp.SourceMap.LocationMapping;
import com.google.javascript.jscomp.util.NodeUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class SourceMapTest {

  private SourceMapGenerator generatorV1;
  private SourceMapGenerator generatorV2;

  @Before
  public void setUp() {
    generatorV1 = mock(SourceMapGeneratorV1.class);
    generatorV2 = mock(SourceMapGeneratorV2.class);
  }

  @Test
  public void testFormatV1() {
    SourceMap sourceMap = new SourceMap(Format.V1.getInstance()).setPrefixMappings(Collections.emptyList());
    SourceMapGeneratorFactory factory = mock(SourceMapGeneratorFactory.class);
    when(factory.getInstance(SourceMapFormat.V1)).thenReturn(generatorV1);

    sourceMap.setPrefixMappings(Collections.emptyList());
    sourceMap.addMapping(createNode(), createOutputStartPosition(), createOutputEndPosition());

    ArgumentCaptor<String> sourceFileArgumentCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> originalNameArgumentCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<FilePosition> inputPositionArgumentCaptor = ArgumentCaptor.forClass(FilePosition.class);
    ArgumentCaptor<FilePosition> outputStartPositionArgumentCaptor = ArgumentCaptor.forClass(FilePosition.class);
    ArgumentCaptor<FilePosition> outputEndPositionArgumentCaptor = ArgumentCaptor.forClass(FilePosition.class);

    sourceMap.validate(true);
    sourceMap.generator.appendTo(ArgumentCaptor.capture(ByteArrayOutputStream.class), ArgumentCaptor.capture(String.class));

    sourceMap.generator.addMapping(sourceFileArgumentCaptor.capture(), originalNameArgumentCaptor.capture(),
        inputPositionArgumentCaptor.capture(), outputStartPositionArgumentCaptor.capture(),
        outputEndPositionArgumentCaptor.capture());

    assertThat(sourceFileArgumentCaptor.getValue().toString()).isNotNull();
    assertThat(originalNameArgumentCaptor.getValue()).isEqualTo("nodeName");
    assertThat(inputPositionArgumentCaptor.getValue().getLine()).isEqualTo(1);
    assertThat(inputPositionArgumentCaptor.getValue().getColumn()).isEqualTo(1);
    assertThat(outputStartPositionArgumentCaptor.getValue().getLine()).isEqualTo(1);
    assertThat(outputStartPositionArgumentCaptor.getValue().getColumn()).isEqualTo(1);
    assertThat(outputEndPositionArgumentCaptor.getValue().getLine()).isEqualTo(1);
    assertThat(outputEndPositionArgumentCaptor.getValue().getColumn()).isEqualTo(1);
  }

  @Test
  public void testFormatV2() {
    SourceMap sourceMap = new SourceMap(Format.V2.getInstance()).setPrefixMappings(Collections.emptyList());
    SourceMapGeneratorFactory factory = mock(SourceMapGeneratorFactory.class);
    when(factory.getInstance(SourceMapFormat.V2)).thenReturn(generatorV2);

    sourceMap.setPrefixMappings(Collections.emptyList());
    sourceMap.addMapping(createNode(), createOutputStartPosition(), createOutputEndPosition());

    ArgumentCaptor<String> sourceFileArgumentCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> originalNameArgumentCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<SourceMapGenerator.SourceMapEntry> inputPositionArgumentCaptor = ArgumentCaptor.forClass(SourceMapGenerator.SourceMapEntry.class);
    ArgumentCaptor<SourceMapGenerator.SourceMapEntry> outputStartPositionArgumentCaptor = ArgumentCaptor.forClass(SourceMapGenerator.SourceMapEntry.class);
    ArgumentCaptor<SourceMapGenerator.SourceMapEntry> outputEndPositionArgumentCaptor = ArgumentCaptor.forClass(SourceMapGenerator.SourceMapEntry.class);

    sourceMap.validate(true);
    sourceMap.generator.appendTo(ArgumentCaptor.capture(ByteArrayOutputStream.class), ArgumentCaptor.capture(String.class));

    sourceMap.generator.addMapping(sourceFileArgumentCaptor.capture(), originalNameArgumentCaptor.capture(),
        inputPositionArgumentCaptor.capture(), outputStartPositionArgumentCaptor.capture(),
        outputEndPositionArgumentCaptor.capture());

    assertThat(sourceFileArgumentCaptor.getValue().getSource()).isNotNull();
    assertThat(originalNameArgumentCaptor.getValue()).isEqualTo("nodeName");
    assertThat(inputPositionArgumentCaptor.getValue().getLine()).isEqualTo(1);
    assertThat(inputPositionArgumentCaptor.getValue().getColumn()).isEqualTo(1);
    assertThat(outputStartPositionArgumentCaptor.getValue().getLine()).isEqualTo(1);
    assertThat(outputStartPositionArgumentCaptor.getValue().getColumn()).isEqualTo(1);
    assertThat(outputEndPositionArgumentCaptor.getValue().getLine()).isEqualTo(1);
    assertThat(outputEndPositionArgumentCaptor.getValue().getColumn()).isEqualTo(1);
  }

  @Test
  public void testAddMappingNullGenerator() {
    SourceMap sourceMap = new SourceMap(Format.V1.getInstance()).setPrefixMappings(Collections.emptyList());
    sourceMap.generator = null;

    assertThrows(IllegalStateException.class, () -> sourceMap.addMapping(createNode(), createOutputStartPosition(), createOutputEndPosition()));
  }

  @Test
  public void testAddMappingNullNode() {
    SourceMap sourceMap = new SourceMap(Format.V1.getInstance()).setPrefixMappings(Collections.emptyList());

    assertThrows(NullPointerException.class, () -> sourceMap.addMapping(null, createOutputStartPosition(), createOutputEndPosition()));
  }

  @Test
  public void testAddMappingNullOutputStartPosition() {
    SourceMap sourceMap = new SourceMap(Format.V1.getInstance()).setPrefixMappings(Collections.emptyList());

    assertThrows(NullPointerException.class, () -> sourceMap.addMapping(createNode(), null, createOutputEndPosition()));
  }

  @Test
  public void testAddMappingNullOutputEndPosition() {
    SourceMap sourceMap = new SourceMap(Format.V1.getInstance()).setPrefixMappings(Collections.emptyList());

    assertThrows(NullPointerException.class, () -> sourceMap.addMapping(createNode(), createOutputStartPosition(), null));
  }

  @Test
  public void testSetPrefixMappingsNullList() {
    SourceMap sourceMap = new SourceMap(Format.V1.getInstance()).setPrefixMappings(null);

    assertThrows(NullPointerException.class, () -> sourceMap.setPrefixMappings(null));
  }

  @Test
  public void testSetPrefixMappingsEmptyList() {
    SourceMap sourceMap = new SourceMap(Format.V1.getInstance()).setPrefixMappings(Collections.emptyList());

    sourceMap.setPrefixMappings(Collections.emptyList());
  }

  @Test
  public void testFixupSourceLocationNullSourceFile() {
    SourceMap sourceMap = new SourceMap(Format.V1.getInstance()).setPrefixMappings(Collections.emptyList());

    assertNull(sourceMap.fixupSourceLocation(null));
  }

  @Test
  public void testFixupSourceLocationEmptyPrefixMappings() {
    SourceMap sourceMap = new SourceMap(Format.V1.getInstance()).setPrefixMappings(Collections.emptyList());

    assertNull(sourceMap.fixupSourceLocation("File1.js"));
  }

  @Test
  public void testFixupSourceLocationWithPrefixMapping() {
    SourceMap sourceMap = new SourceMap(Format.V1.getInstance())
        .setPrefixMappings(Lists.newArrayList(new LocationMapping("File1.js", "File2.js")));

    String fixed = sourceMap.fixupSourceLocation("File1.js");

    assertThat(fixed).isEqualTo("File2.js");
  }

  @Test
  public void testAddMappingWithPrefixMapping() {
    SourceMap sourceMap = new SourceMap(Format.V1.getInstance())
        .setPrefixMappings(Lists.newArrayList(new LocationMapping("File1.js", "File2.js")));

    Node node = createNode();
    FilePosition outputStartPosition = createOutputStartPosition();
    FilePosition outputEndPosition = createOutputEndPosition();

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    String sourceFile = "File1.js";
    String originalName = "nodeName";

    String fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);

    assertThat(fixedSourceFile).isEqualTo("File2.js");
    assertThat(sourceMap.generator.getSourceFile(fixedSourceFile)).isEqualTo(sourceFile);
  }

  @Test
  public void testAppendTo() throws IOException {
    SourceMap sourceMap = new SourceMap(Format.V1.getInstance()).setPrefixMappings(Collections.emptyList());
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    sourceMap.appendTo(outputStream, "Test.js");

    assertThat(outputStream.toString()).isEqualTo("{\"version\":1,\"file\":\"Test.js\",\"sources\":{},\"names\":{},\"mappings\":[]}");
  }

  @Test
  public void testReset() throws IOException {
    SourceMap sourceMap = new SourceMap(Format.V1.getInstance()).setPrefixMappings(Collections.emptyList());
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    sourceMap.appendTo(outputStream, "Test.js");
    sourceMap.addMapping(createNode(), createOutputStartPosition(), createOutputEndPosition());
    sourceMap.reset();

    assertThat(outputStream.size()).isZero();
  }

  @Test
  public void testSetWrapperPrefix() {
    SourceMap sourceMap = new SourceMap(Format.V1.getInstance()).setPrefixMappings(Collections.emptyList());

    sourceMap.setWrapperPrefix("wrapper");

    String wrapperPrefix = sourceMap.getWrapperPrefix();

    assertThat(wrapperPrefix).isEqualTo("wrapper");
  }

  @Test
  public void testValidate() {
    SourceMap sourceMap = new SourceMap(Format.V1.getInstance()).setPrefixMappings(Collections.emptyList());

    sourceMap.validate(true);
  }

  private Node createNode() {
    Node node = mock(Node.class);
    when(node.getSourceFileName()).thenReturn("File1.js");
    when(node.getLineno()).thenReturn(1);
    when(node.getCharno()).thenReturn(1);
    when(node.getProp(Node.ORIGINALNAME_PROP)).thenReturn("nodeName");
    return node;
  }

  private FilePosition createOutputStartPosition() {
    return new FilePosition(1, 1);
  }

  private FilePosition createOutputEndPosition() {
    return new FilePosition(1, 1);
  }
}