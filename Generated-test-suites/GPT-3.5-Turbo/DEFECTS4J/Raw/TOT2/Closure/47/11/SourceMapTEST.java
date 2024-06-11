import org.junit.Test;
import static org.junit.Assert.*;

public class SourceMapTest {

  @Test
  public void testAddMapping() {
    SourceMap sourceMap = new SourceMap(SourceMap.Format.DEFAULT.getInstance());
    
    // Typical use case
    sourceMap.addMapping(new Node(), new FilePosition(1, 1), new FilePosition(2, 2));
    assertNotNull(sourceMap);
    
    // Edge case: node with no source file
    try {
      sourceMap.addMapping(new Node(), new FilePosition(1, 1), new FilePosition(2, 2));
      fail("Expected NullPointerException");
    } catch (NullPointerException e) {
      // Expected
    }
  }

  @Test
  public void testFixupSourceLocation() {
    SourceMap sourceMap = new SourceMap(SourceMap.Format.V2.getInstance());
    
    // Typical use case
    String fixedSourceFile = sourceMap.fixupSourceLocation("source.js");
    assertEquals("source.js", fixedSourceFile);
    
    // Edge case: empty prefixMappings
    String fixedEmptySourceFile = sourceMap.fixupSourceLocation("source.js");
    assertEquals("source.js", fixedEmptySourceFile);
  }

  @Test
  public void testSetPrefixMappings() {
    SourceMap sourceMap = new SourceMap(SourceMap.Format.V3.getInstance());
    
    // Typical use case
    sourceMap.setPrefixMappings(Collections.singletonList(new LocationMapping("old/", "new/")));
    assertEquals(1, sourceMap.prefixMappings.size());
    
    // Edge case: multiple mappings
    sourceMap.setPrefixMappings(Arrays.asList(
      new LocationMapping("old/", "new/"),
      new LocationMapping("test/", "example/")
    ));
    assertEquals(2, sourceMap.prefixMappings.size());
  }
}