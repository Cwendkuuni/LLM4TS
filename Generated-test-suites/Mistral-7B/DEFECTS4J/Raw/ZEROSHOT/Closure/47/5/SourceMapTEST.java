import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.debugging.sourcemap.FilePosition;
import com.google.debugging.sourcemap.SourceMap;
import com.google.debugging.sourcemap.SourceMapFormat;
import com.google.debugging.sourcemap.SourceMapGenerator;
import com.google.debugging.sourcemap.SourceMapGeneratorFactory;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class SourceMapTest {

  @Test
  public void testInstanceCreation() {
    SourceMap sourceMapV1 = SourceMap.Format.V1.getInstance();
    assertNotNull(sourceMapV1);

    SourceMap sourceMapDefault = SourceMap.Format.DEFAULT.getInstance();
    assertNotNull(sourceMapDefault);

    SourceMap sourceMapV2 = SourceMap.Format.V2.getInstance();
    assertNotNull(sourceMapV2);

    SourceMap sourceMapV3 = SourceMap.Format.V3.getInstance();
    assertNotNull(sourceMapV3);
  }

  @Test
  public void testAddMapping() throws IOException {
    SourceMap sourceMapV1 = SourceMap.Format.V1.getInstance();

    Node node = mock(Node.class);
    when(node.getSourceFileName()).thenReturn("test.js");
    when(node.getLineno()).thenReturn(1);
    when(node.getCharno()).thenReturn(10);

    FilePosition outputStartPosition = new FilePosition(2, 20);
    FilePosition outputEndPosition = new FilePosition(3, 30);

    sourceMapV1.addMapping(node, outputStartPosition, outputEndPosition);

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    sourceMapV1.appendTo(out, "test.js");

    String expectedSourceMap = "{\"version\":1,\"file\":\"test.js\",\"sources\":[],\"sourcesContent\":{},\"mappings\":[";
    expectedSourceMap += "{\"original\":{\"source\":\"test.js\",\"line\":1,\"column\":10},\"generated\":{\"line\":2,"
        + "\"column\":20}}";
    expectedSourceMap += "]}";

    assertNotNull(out.toString());
    assertNotNull(sourceMapV1.getGenerator());

    // Validate the generated sourcemap is correct
    assertNotNull(sourceMapV1.getGenerator().getSourceMap());
    assertNotNull(sourceMapV1.getGenerator().getSourceMap().getSourceFile("test.js"));
    assertNotNull(sourceMapV1.getGenerator().getSourceMap().getSourceLine(2));
    assertNotNull(sourceMapV1.getGenerator().getSourceMap().getSourceColumn(2, 20));
    assertNull(sourceMapV1.getGenerator().getSourceMap().getSourceLine(1));

    // Check that mappings are added correctly
    assertFalse(sourceMapV1.getGenerator().getSourceMap().getSourceLine(3) != null);
    assertFalse(sourceMapV1.getGenerator().getSourceMap().getSourceColumn(3, 30) != null);
  }

  @Test
  public void testSetPrefixMappings() throws IOException {
    SourceMap sourceMapV1 = SourceMap.Format.V1.getInstance();

    Node node = mock(Node.class);
    when(node.getSourceFileName()).thenReturn("test.js");
    when(node.getLineno()).thenReturn(1);
    when(node.getCharno()).thenReturn(10);

    List<LocationMapping> prefixMappings = Lists.newArrayList(
        new LocationMapping("/test/", "/src/test/"));

    sourceMapV1.setPrefixMappings(prefixMappings);

    sourceMapV1.addMapping(node, null, null);

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    sourceMapV1.appendTo(out, "test.js");

    String expectedSourceMap = "{\"version\":1,\"file\":\"src/test/test.js\",\"sources\":[],\"sourcesContent\":{},\"mappings\":[";
    expectedSourceMap += "{\"original\":{\"source\":\"test.js\",\"line\":1,\"column\":10},\"generated\":{\"line\":1,"
        + "\"column\":10}}";
    expectedSourceMap += "]}";

    assertNotNull(out.toString());
    assertNotNull(sourceMapV1.getGenerator().getSourceMap());

    // Validate the generated sourcemap is correct
    assertNotNull(sourceMapV1.getGenerator().getSourceMap().getSourceFile("src/test/test.js"));
    assertNotNull(sourceMapV1.getGenerator().getSourceMap().getSourceLine(1));
    assertNotNull(sourceMapV1.getGenerator().getSourceMap().getSourceColumn(1, 10));
  }

  @Test
  public void testFixupSourceLocation() {
    SourceMap sourceMap = SourceMap.Format.V1.getInstance();

    String sourceFile = "test.js";
    String fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);
    assertEquals(sourceFile, fixedSourceFile);

    sourceFile = "test/test.js";
    fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);
    assertEquals("/test/test.js", fixedSourceFile);

    sourceFile = "src/test/test.js";
    fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);
    assertEquals("src/test/test.js", fixedSourceFile);

    sourceMap.setPrefixMappings(Lists.newArrayList(new LocationMapping("/test/", "/src/test/")));

    sourceFile = "test/test.js";
    fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);
    assertEquals("src/test/test.js", fixedSourceFile);
  }

  @Test
  public void testAppendTo() throws IOException {
    SourceMap sourceMapV1 = SourceMap.Format.V1.getInstance();

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    sourceMapV1.appendTo(out, "test.js");

    String expectedSourceMap = "{\"version\":1,\"file\":\"test.js\",\"sources\":[],\"sourcesContent\":{},\"mappings\":[]}";

    assertNotNull(out.toString());
    assertEquals(expectedSourceMap, out.toString());

    sourceMapV1.addMapping(mock(Node.class), null, null);

    out = new ByteArrayOutputStream();
    sourceMapV1.appendTo(out, "test.js");

    expectedSourceMap = "{\"version\":1,\"file\":\"test.js\",\"sources\":[],\"sourcesContent\":{},\"mappings\":[{}]}";

    assertNotNull(out.toString());
    assertEquals(expectedSourceMap, out.toString());
  }

  @Test
  public void testReset() throws IOException {
    SourceMap sourceMapV1 = SourceMap.Format.V1.getInstance();

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    sourceMapV1.appendTo(out, "test.js");

    sourceMapV1.addMapping(mock(Node.class), null, null);

    sourceMapV1.reset();

    ByteArrayOutputStream out2 = new ByteArrayOutputStream();
    sourceMapV1.appendTo(out2, "test.js");

    assertNotNull(out2.toString());
    assertEquals("", out2.toString());
  }

  @Test
  public void testSetWrapperPrefix() throws IOException {
    SourceMap sourceMapV1 = SourceMap.Format.V1.getInstance();

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    sourceMapV1.appendTo(out, "test.js");

    sourceMapV1.setWrapperPrefix("wrapper_");

    sourceMapV1.addMapping(mock(Node.class), null, null);

    ByteArrayOutputStream out2 = new ByteArrayOutputStream();
    sourceMapV1.appendTo(out2, "test.js");

    String expectedSourceMap = "{\"version\":1,\"file\":\"test.js\",\"sources\":[],\"sourcesContent\":{},\"mappings\":[{}],\"fileContent\":\"wrapper_test.js\"}";

    assertNotNull(out2.toString());
    assertEquals(expectedSourceMap, out2.toString());
  }

  @Test
  public void testValidate() throws IOException {
    SourceMap sourceMapV1 = SourceMap.Format.V1.getInstance();

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    sourceMapV1.appendTo(out, "test.js");

    sourceMapV1.validate(true);

    ByteArrayOutputStream out2 = new ByteArrayOutputStream();
    sourceMapV1.appendTo(out2, "test.js");

    assertEquals(out.toString(), out2.toString());

    sourceMapV1.addMapping(mock(Node.class), null, null);

    ByteArrayOutputStream out3 = new ByteArrayOutputStream();
    sourceMapV1.appendTo(out3, "test.js");

    assertFalse(out.toString().equals(out3.toString()));

    sourceMapV1.validate(false);

    ByteArrayOutputStream out4 = new ByteArrayOutputStream();
    sourceMapV1.appendTo(out4, "test.js");

    assertEquals(out3.toString(), out4.toString());
  }

  @Test
  public void testDetermineSourceLocation() throws IOException {
    SourceMap sourceMapV1 = SourceMap.Format.V1.getInstance();

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    sourceMapV1.appendTo(out, "test.js");

    Node node = mock(Node.class);
    when(node.getSourceFileName()).thenReturn("test.js");
    when(node.getLineno()).thenReturn(1);
    when(node.getCharno()).thenReturn(10);

    sourceMapV1.addMapping(node, null, null);

    String sourceLocation = sourceMapV1.getGenerator().determineSourceLocation(new FilePosition(2, 20));

    assertEquals("test.js:1:10", sourceLocation);

    sourceMapV1.setPrefixMappings(Lists.newArrayList(new LocationMapping("/test/", "/src/test/")));

    sourceLocation = sourceMapV1.getGenerator().determineSourceLocation(new FilePosition(2, 20));

    assertEquals("src/test/test.js:1:10", sourceLocation);
  }

  @Test
  public void testGetSourceLine() throws IOException {
    SourceMap sourceMapV1 = SourceMap.Format.V1.getInstance();

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    sourceMapV1.appendTo(out, "test.js");

    Node node = mock(Node.class);
    when(node.getSourceFileName()).thenReturn("test.js");
    when(node.getLineno()).thenReturn(1);
    when(node.getCharno()).thenReturn(10);

    sourceMapV1.addMapping(node, null, null);

    assertNull(sourceMapV1.getGenerator().getSourceLine(2));
    assertNull(sourceMapV1.getGenerator().getSourceLine(3));
    assertNotNull(sourceMapV1.getGenerator().getSourceLine(1));
  }

  @Test
  public void testGetSourceColumn() throws IOException {
    SourceMap sourceMapV1 = SourceMap.Format.V1.getInstance();

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    sourceMapV1.appendTo(out, "test.js");

    Node node = mock(Node.class);
    when(node.getSourceFileName()).thenReturn("test.js");
    when(node.getLineno()).thenReturn(1);
    when(node.getCharno()).thenReturn(10);

    sourceMapV1.addMapping(node, null, null);

    assertNull(sourceMapV1.getGenerator().getSourceColumn(2, 20));
    assertNull(sourceMapV1.getGenerator().getSourceColumn(3, 30));
    assertNotNull(sourceMapV1.getGenerator().getSourceColumn(1, 10));
  }
}