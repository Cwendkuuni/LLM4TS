import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.SourceMap;
import com.google.javascript.jscomp.SourceMap.Format;
import com.google.javascript.jscomp.SourceMap.DetailLevel;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class SourceMapTest {

 private SourceMap sourceMap;
 private Node node;
 private FilePosition outputStartPosition;
 private FilePosition outputEndPosition;
 private String originalSourceFileName;
 private String fixedSourceFileName;

 @Before
 public void setUp() {
 sourceMap = new SourceMap(SourceMap.Format.V1.getInstance()).setDetailLevel(DetailLevel.ALL);

 node = new Node();
 node.setSourceFileName("testSourceFile.js");
 node.setLineno(1);
 node.setCharno(1);
 outputStartPosition = new FilePosition(1, 1);
 outputEndPosition = new FilePosition(1, 5);
 originalSourceFileName = "originalSourceFile.js";
 fixedSourceFileName = "fixedSourceFile.js";
 }

 @Test
 public void testAddMapping() {
 sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

 // Test that the source file is correctly fixed up
 String sourceFile = sourceMap.fixupSourceLocation(node.getSourceFileName());
 assertEquals(fixedSourceFileName, sourceFile);
 }

 @Test
 public void testValidateSourceMap() {
 sourceMap.appendTo(new StringBuffer(), "testSourceMap.js.map");
 sourceMap.validate(true);
 }

 @Test
 public void testInvalidMapping() {
 node.setSourceFileName(null);
 sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

 // Test that a null source file does not add a mapping
 assertFalse(sourceMap.getMappings().contains(mapping(node, outputStartPosition, outputEndPosition)));
 }

 @Test
 public void testSetPrefixMappings() {
 List<LocationMapping> prefixMappings = Lists.newArrayList(
 new LocationMapping("testPrefix", "replacementPrefix")
 );
 sourceMap.setPrefixMappings(prefixMappings);

 // Test that a source file with the prefix is correctly fixed up
 String sourceFileWithPrefix = "testPrefix/testSourceFileWithPrefix.js";
 assertEquals("replacementPrefix/testSourceFileWithPrefix.js", sourceMap.fixupSourceLocation(sourceFileWithPrefix));
 }
}