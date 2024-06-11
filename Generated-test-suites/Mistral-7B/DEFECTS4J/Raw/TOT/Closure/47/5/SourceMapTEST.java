import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import com.google.javascript.jscomp.SourceMap;
import com.google.javascript.jscomp.SourceMap.Format;
import com.google.javascript.jscomp.SourceMap.DetailLevel;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Test class for SourceMap class
 */
public class SourceMapTest {

  @Test
  public void testGenerateSourceMapV1() throws IOException {
    SourceMap sourceMap = new SourceMap.Format.V1().getInstance();

    // Test adding mappings
    addMappingsToSourceMap(sourceMap);

    // Test appending to output stream
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    sourceMap.appendTo(out, "test.js");

    String expectedSourceMap = "{\"version\":1,\"file\":\"test.js\",\"sources\":{},\"mappings\":[]}";
    assertEquals(expectedSourceMap, out.toString());
  }

  @Test
  public void testGenerateSourceMapDefault() throws IOException {
    SourceMap sourceMap = new SourceMap.Format.DEFAULT.getInstance();

    // Test adding mappings
    addMappingsToSourceMap(sourceMap);

    // Test appending to output stream
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    sourceMap.appendTo(out, "test.js");

    String expectedSourceMap = "{\"version\":3,\"file\":\"test.js\",\"sources\":{},\"mappings\":[]}";
    assertEquals(expectedSourceMap, out.toString());
  }

  @Test
  public void testGenerateSourceMapV2() throws IOException {
    SourceMap sourceMap = new SourceMap.Format.V2.getInstance();

    // Test adding mappings
    addMappingsToSourceMap(sourceMap);

    // Test appending to output stream
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    sourceMap.appendTo(out, "test.js");

    String expectedSourceMap = "{\"version\":2,\"file\":\"test.js\",\"sources\":{},\"names\":[],\"mappings\":[]}";
    assertEquals(expectedSourceMap, out.toString());
  }

  @Test
  public void testGenerateSourceMapV3() throws IOException {
    SourceMap sourceMap = new SourceMap.Format.V3.getInstance();

    // Test adding mappings
    addMappingsToSourceMap(sourceMap);

    // Test appending to output stream
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    sourceMap.appendTo(out, "test.js");

    String expectedSourceMap = "{\"version\":3,\"file\":\"test.js\",\"sources\":{},\"names\":[],\"mappings\":[],\"fileMappings\":[],\"originMappings\":[],\"sourceRoot\":\"\"}";
    assertEquals(expectedSourceMap, out.toString());
  }

  @Test
  public void testSetPrefixMappings() throws IOException {
    SourceMap sourceMap = new SourceMap.Format.V1().getInstance();

    List<SourceMap.LocationMapping> locationMappings = Lists.newArrayList(
        new SourceMap.LocationMapping("src", "dist")
    );

    sourceMap.setPrefixMappings(locationMappings);

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    sourceMap.appendTo(out, "test.js");

    String expectedSourceMap = "{\"version\":1,\"file\":\"test.js\",\"sources\":{},\"mappings\":[]}"; // No change in source map

    assertEquals(expectedSourceMap, out.toString());

    String fixedSourceFile = sourceMap.fixupSourceLocation("src/test.js");
    assertEquals("dist/test.js", fixedSourceFile);
  }

  private void addMappingsToSourceMap(SourceMap sourceMap) {
    Node node = Node.newCompilationUnit();
    node.setSourceFileName("test.js");
    node.setLineno(1);
    node.setCharno(1);

    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 2);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
  }
}