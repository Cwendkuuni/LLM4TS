import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Predicate;
import com.google.common.collect.Maps;
import com.google.debugging.sourcemap.FilePosition;
import com.google.debugging.sourcemap.SourceMap;
import com.google.debugging.sourcemap.SourceMapFormat;
import com.google.debugging.sourcemap.SourceMapGenerator;
import com.google.debugging.sourcemap.SourceMapGeneratorFactory;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.NodeFactory;

public class SourceMapTest {

  private SourceMapGeneratorFactory generatorFactory;
  private SourceMapGenerator generatorV1;
  private SourceMapGenerator generatorV2;
  private SourceMap sourceMapV1;
  private SourceMap sourceMapV2;
  private NodeFactory nodeFactory;

  @Before
  public void setUp() {
    generatorFactory = mock(SourceMapGeneratorFactory.class);
    generatorV1 = mock(SourceMapGeneratorV1.class);
    generatorV2 = mock(SourceMapGeneratorV2.class);
    when(generatorFactory.getInstance(SourceMapFormat.V1)).thenReturn(generatorV1);
    when(generatorFactory.getInstance(SourceMapFormat.DEFAULT)).thenReturn(generatorV2);
    nodeFactory = NodeFactory.getPrototypeTable();
  }

  @Test
  public void testNewSourceMapInstance() {
    SourceMap sourceMapV1 = SourceMap.Format.V1.getInstance();
    SourceMap sourceMapV2 = SourceMap.Format.DEFAULT.getInstance();
    assertNotNull(sourceMapV1);
    assertNotNull(sourceMapV2);
  }

  @Test
  public void testAddMapping() throws IOException {
    SourceMap sourceMapV1 = new SourceMap(generatorV1);
    Node node = nodeFactory.createCall(null, "testFunction", null);
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 10);
    sourceMapV1.addMapping(node, outputStartPosition, outputEndPosition);

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    sourceMapV1.appendTo(out, "testFile.js");
    assertNotNull(out.toString());
  }

  @Test
  public void testFixupSourceLocation() {
    String sourceFile = "src/main/java/com/google/javascript/jscomp/SourceMapTest.java";
    String fixed = SourceMap.getInstance(generatorFactory).fixupSourceLocation(sourceFile);
    assertNotNull(fixed);
    assertEquals("com/google/javascript/jscomp/SourceMapTest.java", fixed);
  }

  @Test
  public void testSetPrefixMappings() {
    SourceMap sourceMap = new SourceMap(generatorV2);
    List<SourceMap.LocationMapping> sourceMapLocationMappings =
        Collections.singletonList(new SourceMap.LocationMapping("src/", "src_"));
    sourceMap.setPrefixMappings(sourceMapLocationMappings);
    String sourceFile = "src/main/java/com/google/javascript/jscomp/SourceMapTest.java";
    String fixed = sourceMap.fixupSourceLocation(sourceFile);
    assertEquals("src_main_java_com_google_javascript_jscomp_SourceMapTest.java", fixed);
  }

  @Test
  public void testValidate() {
    SourceMap sourceMap = new SourceMap(generatorV2);
    sourceMap.validate(true);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValidateWithFalse() {
    SourceMap sourceMap = new SourceMap(generatorV2);
    sourceMap.validate(false);
  }
}