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
import com.google.debugging.sourcemap.SourceMapGeneratorV1;
import com.google.debugging.sourcemap.SourceMapGeneratorV2;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.NodeFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Test class for SourceMap.java
 */
public class SourceMapTest {

  @org.junit.Test
  public void testConstructor() {
    SourceMap sourceMap = new SourceMap(
        SourceMapGeneratorFactory.getInstance(SourceMapFormat.DEFAULT));
    assertNotNull(sourceMap);
  }

  @org.junit.Test
  public void testAddMapping() {
    SourceMap sourceMap = new SourceMap(
        mockSourceMapGenerator(SourceMapFormat.DEFAULT));

    Node node = NodeFactory.createNodeFromString("test");
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 3);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    // Check if mapping is added
    // This check should be moved to a separate test method
    assertNotNull(sourceMap.generator);
  }

  @org.junit.Test(expected = NullPointerException.class)
  public void testAddMappingWithNullNode() {
    SourceMap sourceMap = new SourceMap(
        mockSourceMapGenerator(SourceMapFormat.DEFAULT));
    Node node = null;

    sourceMap.addMapping(node, new FilePosition(1, 1), new FilePosition(1, 3));
  }

  @org.junit.Test(expected = NullPointerException.class)
  public void testAddMappingWithNullOutputPosition() {
    SourceMap sourceMap = new SourceMap(
        mockSourceMapGenerator(SourceMapFormat.DEFAULT));
    Node node = NodeFactory.createNodeFromString("test");

    sourceMap.addMapping(node, null, new FilePosition(1, 3));
  }

  @org.junit.Test(expected = NullPointerException.class)
  public void testAddMappingWithNullOutputEndPosition() {
    SourceMap sourceMap = new SourceMap(
        mockSourceMapGenerator(SourceMapFormat.DEFAULT));
    Node node = NodeFactory.createNodeFromString("test");

    sourceMap.addMapping(node, new FilePosition(1, 1), null);
  }

  @org.junit.Test
  public void testFixupSourceLocation() {
    SourceMap sourceMap = new SourceMap(
        mockSourceMapGenerator(SourceMapFormat.DEFAULT));
    sourceMap.setPrefixMappings(getPrefixMappings());

    String sourceFile = "test/SourceMapTest.java";
    String fixed = sourceMap.fixupSourceLocation(sourceFile);

    // Check if source file is fixed up correctly
    assert (fixed.equals("test_fix/SourceMapTest.java"));
  }

  @org.junit.Test
  public void testAppendTo() throws IOException {
    SourceMap sourceMap = new SourceMap(
        mockSourceMapGenerator(SourceMapFormat.DEFAULT));

    ByteArrayOutputStream out = new ByteArrayOutputStream();

    sourceMap.appendTo(out, "test");

    // Check if source map is appended to the output stream
    String expectedOutput = "sourceMappingURL=data:application/json;base64,...";
    String actualOutput = new String(out.toByteArray());

    assert (actualOutput.contains(expectedOutput));
  }

  @org.junit.Test
  public void testReset() {
    SourceMap sourceMap = new SourceMap(
        mockSourceMapGenerator(SourceMapFormat.DEFAULT));

    sourceMap.reset();

    // Check if the generator is reset
    assertNotNull(sourceMap.generator);
    assert (sourceMap.generator.getMappings().isEmpty());
  }

  @org.junit.Test
  public void testSetStartingPosition() {
    SourceMap sourceMap = new SourceMap(
        mockSourceMapGenerator(SourceMapFormat.DEFAULT));

    sourceMap.setStartingPosition(1, 1);

    // Check if the starting position is set
    FilePosition expectedStartingPosition = new FilePosition(1, 1);
    FilePosition actualStartingPosition = sourceMap.generator.getStartingPosition();

    assert (expectedStartingPosition.equals(actualStartingPosition));
  }

  @org.junit.Test
  public void testSetWrapperPrefix() {
    SourceMap sourceMap = new SourceMap(
        mockSourceMapGenerator(SourceMapFormat.DEFAULT));

    sourceMap.setWrapperPrefix("test_fix/");

    // Check if the wrapper prefix is set
    String expectedWrapperPrefix = "test_fix/";
    String actualWrapperPrefix = sourceMap.generator.getWrapperPrefix();

    assert (expectedWrapperPrefix.equals(actualWrapperPrefix));
  }

  @org.junit.Test
  public void testValidate() {
    SourceMap sourceMap = new SourceMap(
        mockSourceMapGenerator(SourceMapFormat.DEFAULT));

    sourceMap.validate(true);

    // Check if the generator is validated
    boolean expectedValid = true;
    boolean actualValid = sourceMap.generator.isValid();

    assert (expectedValid == actualValid);
  }

  private SourceMapGenerator mockSourceMapGenerator(SourceMapFormat format) {
    SourceMapGenerator generator;

    if (format == SourceMapFormat.V1) {
      generator = mock(SourceMapGeneratorV1.class);
    } else if (format == SourceMapFormat.V2) {
      generator = mock(SourceMapGeneratorV2.class);
    } else {
      generator = mock(SourceMapGenerator.class);
    }

    when(generator.getInstance()).thenReturn(generator);

    return generator;
  }

  private List<LocationMapping> getPrefixMappings() {
    List<LocationMapping> sourceMapLocationMappings = Lists.newArrayList();

    sourceMapLocationMappings.add(new LocationMapping(
        "test_fix/", "test/"));

    return sourceMapLocationMappings;
  }
}