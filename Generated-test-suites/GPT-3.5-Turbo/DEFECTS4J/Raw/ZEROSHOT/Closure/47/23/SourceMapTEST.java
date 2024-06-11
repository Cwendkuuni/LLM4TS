import org.junit.Assert;
import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.debugging.sourcemap.FilePosition;
import com.google.debugging.sourcemap.SourceMapGenerator;
import com.google.debugging.sourcemap.SourceMapGeneratorFactory;
import com.google.debugging.sourcemap.SourceMapFormat;

import java.io.IOException;
import java.util.Arrays;

public class SourceMapTest {

  @Test
  public void testAddMapping() {
    SourceMapGenerator mockGenerator = SourceMapGeneratorFactory.getInstance(SourceMapFormat.V1);
    SourceMap sourceMap = new SourceMap(mockGenerator);
    
    Node node = new Node();
    node.setSourceFileName("test.js");
    node.setLineno(1);
    node.setCharno(0);
    
    sourceMap.addMapping(node, new FilePosition(1, 0), new FilePosition(1, 5));

    // Verify that the mapping was added correctly
    // TODO: implement verification of mapping
    
    // Reset the source map
    sourceMap.reset();
  }

  @Test
  public void testFixupSourceLocation() {
    SourceMapGenerator mockGenerator = SourceMapGeneratorFactory.getInstance(SourceMapFormat.V1);
    SourceMap sourceMap = new SourceMap(mockGenerator);

    List<SourceMap.LocationMapping> prefixMappings = Arrays.asList(
      new SourceMap.LocationMapping("prefix1/", "replacement1/"),
      new SourceMap.LocationMapping("prefix2/", "replacement2/")
    );
    sourceMap.setPrefixMappings(prefixMappings);

    String sourceFile1 = "prefix1/test.js";
    String fixedSourceFile1 = sourceMap.fixupSourceLocation(sourceFile1);
    Assert.assertEquals("replacement1/test.js", fixedSourceFile1);

    String sourceFile2 = "prefix2/subfolder/test.js";
    String fixedSourceFile2 = sourceMap.fixupSourceLocation(sourceFile2);
    Assert.assertEquals("replacement2/subfolder/test.js", fixedSourceFile2);

    String sourceFile3 = "prefix3/test.js";
    String fixedSourceFile3 = sourceMap.fixupSourceLocation(sourceFile3);
    Assert.assertEquals("prefix3/test.js", fixedSourceFile3);
  }

  // TODO: add tests for remaining methods in the SourceMap class

}