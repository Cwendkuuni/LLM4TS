import static org.junit.Assert.*;
import org.junit.Test;

public class SourceMapTest {

  @Test
  public void testGetInstance_V1() {
    SourceMap.Format format = SourceMap.Format.V1;
    SourceMap sourceMap = format.getInstance();
    assertEquals(SourceMapGeneratorV1.class, sourceMap.generator.getClass());
  }

  @Test
  public void testGetInstance_DEFAULT() {
    SourceMap.Format format = SourceMap.Format.DEFAULT;
    SourceMap sourceMap = format.getInstance();
    assertEquals(SourceMapGenerator.class, sourceMap.generator.getClass());
  }

  @Test
  public void testGetInstance_V2() {
    SourceMap.Format format = SourceMap.Format.V2;
    SourceMap sourceMap = format.getInstance();
    assertEquals(SourceMapGeneratorV2.class, sourceMap.generator.getClass());
  }

  @Test
  public void testGetInstance_V3() {
    SourceMap.Format format = SourceMap.Format.V3;
    SourceMap sourceMap = format.getInstance();
    // Add specific assertion for V3 source map generator
  }

  @Test
  public void testAddMapping() {
    // Test adding mapping with valid node and positions
  }

  @Test
  public void testAddMapping_NullSourceFile() {
    // Test adding mapping with null source file
  }

  @Test
  public void testAddMapping_NegativeLineNo() {
    // Test adding mapping with negative line number
  }

  @Test
  public void testFixupSourceLocation() {
    // Test fixup source location with empty prefix mappings
  }

  @Test
  public void testFixupSourceLocation_PrefixMatch() {
    // Test fixup source location with matching prefix mapping
  }

  @Test
  public void testFixupSourceLocation_NoPrefixMatch() {
    // Test fixup source location with no prefix match
  }

  // Add more test cases for other methods in SourceMap class

}