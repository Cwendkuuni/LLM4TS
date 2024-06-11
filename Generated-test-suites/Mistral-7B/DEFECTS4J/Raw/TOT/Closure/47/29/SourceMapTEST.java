import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.SourceMap;
import com.google.javascript.jscomp.SourceMapGeneratorFactory;
import com.google.javascript.jscomp.SourceMapGeneratorV1;
import com.google.javascript.jscomp.SourceMapGeneratorV2;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class SourceMapTest {

  private SourceMap sourceMap;
  private Node node;

  @Before
  public void setUp() {
    SourceMapGenerator generatorV1 = SourceMapGeneratorFactory.getInstance(SourceMapFormat.V1);
    SourceMapGenerator generatorV2 = SourceMapGeneratorFactory.getInstance(SourceMapFormat.V2);

    List<LocationMapping> prefixMappings = Collections.singletonList(
        new LocationMapping("test/", "src/"));

    sourceMap = new SourceMap(
        // For the purpose of testing, we use the V1 and V2 generators to verify that the test case works
        // with both formats.
        (Format.V1.getInstance().getInstance()) // V1
        // or
        // (Format.V2.getInstance().getInstance()) // V2
    );

    sourceMap.setPrefixMappings(prefixMappings);
  }

  @Test
  public void testBasicMapping() throws IOException {
    String originalSource = "function test() { return 42; }";
    String compiledSource = "function test() { return a; }";
    node = Node.jsonToNode(originalSource);

    sourceMap.addMapping(node, new FilePosition(1, 0), new FilePosition(1, 0));
    sourceMap.addMapping(Node.jsonToNode(compiledSource), new FilePosition(1, 0), new FilePosition(1, 0));

    String expectedSourceMapV1 = "{" +
        "\"version\":1,\"file\":\"test.js\",\"sources\":[\"test.js\"],\"mappings\":[";
    String expectedSourceMapV2 = "{\"version\":2,\"file\":\"test.js\",\"sources\":[\"test.js\"],\"names\":[],\"mappings\":[";

    String sourceMapV1 = sourceMap.toString();
    String sourceMapV2 = sourceMap.toSourceMapV2String();

    assertEquals(expectedSourceMapV1, sourceMapV1);
    assertEquals(expectedSourceMapV2, sourceMapV2);
  }

  @Test(expected = AssertionError.class)
  public void testMissingMapping() throws IOException {
    String originalSource = "function test() { return 42; }";
    String compiledSource = "function test() { return a; }";
    node = Node.jsonToNode(originalSource);

    // No mapping is added for the node

    sourceMap.appendTo(System.out, "test.js.map");
  }
}