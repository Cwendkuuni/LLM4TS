import static org.junit.Assert.*;
import com.google.javascript.jscomp.SourceMap.*;
import com.google.javascript.jscomp.Node;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class SourceMapTest {

  @Test
  public void testGetInstance() {
    for (Format format : Format.values()) {
      SourceMap sourceMap = format.getInstance();
      assertNotNull(sourceMap);
    }
  }

  @Test
  public void testAddMapping() throws IOException {
    SourceMap sourceMap = Format.DEFAULT.getInstance();
    ByteArrayOutputStream out = new ByteArrayOutputStream();

    Node node = new Node();
    node.setSourceFileName("TestSourceFile.js");
    node.setLineno(1);
    node.setCharno(10);

    sourceMap.addMapping(node, new FilePosition(1, 10), new FilePosition(2, 20));
    sourceMap.appendTo(out, "TestMapFileName.js.map");

    String expectedOutput = "{\n" +
        "  \"sources\": [\"TestSourceFile.js\"],\n" +
        "  \"names\": [],\n" +
        "  \"mappings\": [\n" +
        "    \"AAAA,TestSourceFile.js:1:10|2:20\",\n" +
        "  ]\n" +
        "}";

    String actualOutput = new String(out.toByteArray());
    assertEquals(expectedOutput, actualOutput);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddMappingNullNode() throws IOException {
    SourceMap sourceMap = Format.DEFAULT.getInstance();
    sourceMap.addMapping(null, new FilePosition(1, 10), new FilePosition(2, 20));
  }

  @Test(expected = IOException.class)
  public void testAppendToNullOutputStream() throws IOException {
    SourceMap sourceMap = Format.DEFAULT.getInstance();
    sourceMap.appendTo(null, "TestMapFileName.js.map");
  }

  @Test
  public void testFixupSourceLocation() {
    SourceMap sourceMap = Format.DEFAULT.getInstance();
    sourceMap.setPrefixMappings(getPrefixMappings());

    String sourceFile = "TestPrefixSourceFile.js";
    String fixed = sourceMap.fixupSourceLocation(sourceFile);

    assertEquals("TestFixedSourceFile.js", fixed);
  }

  @Test
  public void testReset() throws IOException {
    SourceMap sourceMap = Format.DEFAULT.getInstance();
    ByteArrayOutputStream out = new ByteArrayOutputStream();

    sourceMap.addMapping(new Node(), new FilePosition(1, 10), new FilePosition(2, 20));
    sourceMap.appendTo(out, "TestMapFileName.js.map");

    sourceMap.reset();

    String expectedOutput = "";

    String actualOutput = new String(out.toByteArray());
    assertEquals(expectedOutput, actualOutput);
  }

  private List<LocationMapping> getPrefixMappings() {
    return Lists.newArrayList(new LocationMapping("TestPrefix", "TestFixed"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetPrefixMappingsNull() {
    SourceMap sourceMap = Format.DEFAULT.getInstance();
    sourceMap.setPrefixMappings(null);
  }

  @Test
  public void testValidateTrue() throws IOException {
    SourceMap sourceMap = Format.DEFAULT.getInstance();

    sourceMap.validate(true);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValidateFalseNullGenerator() {
    SourceMap sourceMap = Format.DEFAULT.getInstance();
    sourceMap.generator = null;
    sourceMap.validate(false);
  }

  @Test(expected = IOException.class)
  public void testValidateFalseIOException() throws IOException {
    SourceMap sourceMap = Format.DEFAULT.getInstance();
    sourceMap.generator = new SourceMapGenerator() {
      @Override
      public void validate(boolean validate) throws IOException {
        throw new IOException();
      }
    };
    sourceMap.validate(false);
  }

  @Test
  public void testValidateFalseSuccess() throws IOException {
    SourceMap sourceMap = Format.DEFAULT.getInstance();
    sourceMap.generator = new SourceMapGenerator() {
      @Override
      public void validate(boolean validate) throws IOException {
        // empty
      }
    };
    sourceMap.validate(false);
  }
}