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
import com.google.javascript.rhino.NodeUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class SourceMapTest {

  @org.junit.Test
  public void testSourceMap() throws IOException {
    // Test SourceMap instance creation with different formats
    SourceMap sourceMapV1 = SourceMap.Format.V1.getInstance().getInstance();
    SourceMap sourceMapDefault = SourceMap.Format.DEFAULT.getInstance().getInstance();
    SourceMap sourceMapV2 = SourceMap.Format.V2.getInstance().getInstance();

    assertNotNull(sourceMapV1);
    assertNotNull(sourceMapDefault);
    assertNotNull(sourceMapV2);

    // Test addMapping method
    Node node = mock(Node.class);
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 2);
    String sourceFile = "test.js";

    sourceMapV1.addMapping(node, outputStartPosition, outputEndPosition, sourceFile, null);
    sourceMapV2.addMapping(node, outputStartPosition, outputEndPosition, sourceFile, null);

    // Test fixupSourceLocation method
    when(node.getSourceFileName()).thenReturn("test.js");
    when(node.getLineno()).thenReturn(1);
    when(node.getCharno()).thenReturn(1);

    String fixedSourceFileV1 = sourceMapV1.fixupSourceLocation(sourceFile);
    String fixedSourceFileV2 = sourceMapV2.fixupSourceLocation(sourceFile);

    assertNotNull(fixedSourceFileV1);
    assertNotNull(fixedSourceFileV2);
    assertEquals(sourceFile, fixedSourceFileV1);
    assertEquals(sourceFile, fixedSourceFileV2);

    // Test setPrefixMappings method
    List<SourceMapTest.LocationMapping> locationMappings = Lists.newArrayList(
        new SourceMapTest.LocationMapping("test", "test_fixed.js")
    );

    sourceMapV1.setPrefixMappings(locationMappings);
    sourceMapV2.setPrefixMappings(locationMappings);

    String fixedSourceFileV1WithPrefix = sourceMapV1.fixupSourceLocation("test.js");
    String fixedSourceFileV2WithPrefix = sourceMapV2.fixupSourceLocation("test.js");

    assertNotNull(fixedSourceFileV1WithPrefix);
    assertNotNull(fixedSourceFileV2WithPrefix);
    assertEquals("test_fixed.js", fixedSourceFileV1WithPrefix);
    assertEquals("test_fixed.js", fixedSourceFileV2WithPrefix);

    // Test appendTo method
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    sourceMapV1.appendTo(outputStream, "test.js.map");
    sourceMapV2.appendTo(outputStream, "test.js.map");

    String v1Output = outputStream.toString();
    String v2Output = outputStream.toString();

    assertNotNull(v1Output);
    assertNotNull(v2Output);

    // Test reset method
    sourceMapV1.reset();
    sourceMapV2.reset();

    // Test validate method
    sourceMapV1.validate(true);
    sourceMapV2.validate(true);

    // Test setStartingPosition method
    sourceMapV1.setStartingPosition(1, 1);
    sourceMapV2.setStartingPosition(1, 1);

    // Test setWrapperPrefix method
    String prefix = "wrapper";
    sourceMapV1.setWrapperPrefix(prefix);
    sourceMapV2.setWrapperPrefix(prefix);
  }

  static class LocationMapping {
    final String prefix;
    final String replacement;

    LocationMapping(String prefix, String replacement) {
      this.prefix = prefix;
      this.replacement = replacement;
    }
  }

  @org.junit.Test
  public void testDetectMappingFormat() {
    // Test detecting mapping format based on file extension
    SourceMapFormat format = SourceMap.detectMappingFormat("test.js.map");
    assertEquals(SourceMapFormat.V3, format);

    format = SourceMap.detectMappingFormat("test.js.map.v2");
    assertEquals(SourceMapFormat.V2, format);

    format = SourceMap.detectMappingFormat("test.js.map.v1.json");
    assertEquals(SourceMapFormat.V1, format);
  }

  public enum SourceMapFormat {
    V1, DEFAULT, V2, V3
  }
}