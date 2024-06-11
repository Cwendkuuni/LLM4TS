import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.debugging.sourcemap.FilePosition;
import com.google.debugging.sourcemap.SourceMapFormat;
import com.google.debugging.sourcemap.SourceMapGenerator;
import com.google.debugging.sourcemap.SourceMapGeneratorFactory;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.NodeFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class SourceMapTest {

  private SourceMap sourceMap;
  private SourceMapGenerator generatorMock;
  private Node nodeMock;

  @org.junit.Before
  public void setUp() {
    generatorMock = mock(SourceMapGenerator.class);
    NodeFactory nodeFactory = mock(NodeFactory.class);
    nodeMock = mock(Node.class);

    when(generatorMock.getInstance()).thenReturn(generatorMock);
    when(generatorMock.appendTo(org.apache.commons.io.output.ByteArrayOutputStream.class, (String) anyObject())).thenReturn(null);
    when(generatorMock.reset()).thenReturn(null);
    when(generatorMock.setStartingPosition(anyInt(), anyInt())).thenReturn(null);
    when(generatorMock.setWrapperPrefix((String) anyObject())).thenReturn(null);
    when(generatorMock.validate(anyBoolean())).thenReturn(null);

    sourceMap = new SourceMap(generatorMock);
  }

  @org.junit.Test
  public void testInstance() {
    assertNotNull(sourceMap);
  }

  @org.junit.Test
  public void testAddMapping() {
    String sourceFile = "TestSourceFile.js";
    Node node = NodeUtil.newNode("testNode");
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 3);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    String fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);
    assert (fixedSourceFile.equals(sourceFile));
  }

  @org.junit.Test
  public void testFixupSourceLocation() {
    String sourceFile = "TestSourceFile/prefixTest.js";
    String expectedFixedSourceFile = "fixedTestSourceFile.js";
    List<LocationMapping> prefixMappings = Lists.newArrayList(
        new LocationMapping("TestSourceFile/", "fixedTestSourceFile.")
    );
    sourceMap.setPrefixMappings(prefixMappings);

    String fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);
    assert (fixedSourceFile.equals(expectedFixedSourceFile));
  }

  @org.junit.Test
  public void testAppendTo() throws IOException {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

    sourceMap.appendTo(byteArrayOutputStream, "TestName");

    String expectedOutput = "TestName";
    String actualOutput = new String(byteArrayOutputStream.toByteArray());

    assert (expectedOutput.equals(actualOutput));
  }

  @org.junit.Test
  public void testReset() {
    sourceMap.reset();
  }

  @org.junit.Test
  public void testSetStartingPosition() {
    sourceMap.setStartingPosition(1, 1);
  }

  @org.junit.Test
  public void testSetWrapperPrefix() {
    sourceMap.setWrapperPrefix("TestPrefix");
  }

  @org.junit.Test
  public void testValidate() {
    sourceMap.validate(true);
  }
}