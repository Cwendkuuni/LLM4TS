import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.google.common.collect.Lists;
import com.google.debugging.sourcemap.FilePosition;
import com.google.debugging.sourcemap.SourceMapGenerator;
import com.google.debugging.sourcemap.SourceMapGeneratorFactory;
import com.google.javascript.rhino.Node;

public class SourceMapTest {
  
  private SourceMap sourceMap;
  private SourceMapGenerator generator;
  
  @Before
  public void setUp() {
    generator = SourceMapGeneratorFactory.getInstance(SourceMapFormat.V1);
    sourceMap = new SourceMap(generator);
  }
  
  @Test
  public void testAddMapping() {
    Node node1 = new Node("Node1");
    node1.setSourceFileName("TestFile1.js");
    node1.setLineno(10);
    node1.setCharno(5);
    FilePosition outputStartPosition1 = new FilePosition(5, 2);
    FilePosition outputEndPosition1 = new FilePosition(6, 3);
    sourceMap.addMapping(node1, outputStartPosition1, outputEndPosition1);
    
    List<LocationMapping> prefixMappings = new ArrayList<>();
    prefixMappings.add(new LocationMapping("js/", ""));
    sourceMap.setPrefixMappings(prefixMappings);
    
    assertEquals("TestFile1.js", sourceMap.fixupSourceLocation("TestFile1.js"));
    
    Node node2 = new Node("Node2");
    node2.setSourceFileName("TestFile2.js");
    node2.setLineno(-1);
    node2.setCharno(3);
    FilePosition outputStartPosition2 = new FilePosition(8, 1);
    FilePosition outputEndPosition2 = new FilePosition(10, 4);
    sourceMap.addMapping(node2, outputStartPosition2, outputEndPosition2);
    
    assertEquals("TestFile2.js", sourceMap.fixupSourceLocation("TestFile2.js"));
    
    Node node3 = new Node("Node3");
    node3.setSourceFileName("js/TestFile3.js");
    node3.setLineno(7);
    node3.setCharno(0);
    FilePosition outputStartPosition3 = new FilePosition(2, 4);
    FilePosition outputEndPosition3 = new FilePosition(3, 1);
    sourceMap.addMapping(node3, outputStartPosition3, outputEndPosition3);
    
    assertEquals("TestFile3.js", sourceMap.fixupSourceLocation("js/TestFile3.js"));
    
    Node node4 = new Node("Node4");
    node4.setSourceFileName("lib/TestFile4.js");
    node4.setLineno(15);
    node4.setCharno(8);
    FilePosition outputStartPosition4 = new FilePosition(12, 5);
    FilePosition outputEndPosition4 = new FilePosition(15, 10);
    sourceMap.addMapping(node4, outputStartPosition4, outputEndPosition4);
    
    assertEquals("TestFile4.js", sourceMap.fixupSourceLocation("lib/TestFile4.js"));
  }
  
  @Test
  public void testSetStartingPosition() {
    sourceMap.setStartingPosition(5, 2);
    assertEquals(5, generator.getStartingLine());
    assertEquals(2, generator.getStartingIndex());
  }
  
  @Test
  public void testSetWrapperPrefix() {
    sourceMap.setWrapperPrefix("foo");
    assertEquals("foo", generator.getWrapperPrefix());
  }
  
  @Test
  public void testValidate() {
    sourceMap.validate(true);
    assertTrue(generator.isValidating());
    
    sourceMap.validate(false);
    assertFalse(generator.isValidating());
  }
  
  @Test
  public void testAppendTo() throws IOException {
    StringBuilder sb = new StringBuilder();
    sourceMap.appendTo(sb, "TestSourceMap");
    assertEquals("TestSourceMap", sb.toString());
  }
  
  @Test
  public void testReset() {
    sourceMap.reset();
    assertEquals(0, generator.getGeneratedPositions().size());
    assertEquals(0, generator.getSourceFileIndexes().size());
    assertEquals(0, generator.getSourceRootIndexes().size());
    assertEquals(0, generator.getNames().size());
    assertEquals(0, generator.getLineMappings().size());
    assertEquals(0, generator.getColumnMappings().size());
    assertEquals(0, generator.getNameMappings().size());
    assertEquals(0, sourceMap.sourceLocationFixupCache.size());
    assertTrue(sourceMap.prefixMappings.isEmpty());
  }
}