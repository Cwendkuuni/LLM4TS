import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.SourceMap;

public class SourceMapTest {

 @Test
 public void testGetInstance() {
 SourceMap sourceMap = SourceMap.Format.V1.getInstance();
 assertNotNull(sourceMap);
 }

 @Test
 public void testAddMappingWithNullNodeAndNullOutputPositions() {
 SourceMap sourceMap = SourceMap.Format.DEFAULT.getInstance();
 Node node = null;
 FilePosition outputStartPosition = null;
 FilePosition outputEndPosition = null;
 sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
 assertTrue(sourceMap.getPrefixMappings().isEmpty());
 }

 // Add more tests for other methods with similar pattern

 @Test(expected = NullPointerException.class)
 public void testAddMappingWithNullNodeAndValidOutputPositions() {
 SourceMap sourceMap = SourceMap.Format.DEFAULT.getInstance();
 Node node = null;
 FilePosition outputStartPosition = new FilePosition(1, 1);
 FilePosition outputEndPosition = new FilePosition(1, 1);
 sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
 assertTrue(sourceMap.getPrefixMappings().isEmpty());
 }

 @Test(expected = NullPointerException.class)
 public void testAddMappingWithValidNodeAndNullOutputStartPosition() {
 SourceMap sourceMap = SourceMap.Format.DEFAULT.getInstance();
 Node node = new Node();
 FilePosition outputStartPosition = null;
 FilePosition outputEndPosition = new FilePosition(1, 1);
 sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
 assertTrue(sourceMap.getPrefixMappings().isEmpty());
 }

 @Test(expected = NullPointerException.class)
 public void testAddMappingWithValidNodeAndNullOutputEndPosition() {
 SourceMap sourceMap = SourceMap.Format.DEFAULT.getInstance();
 Node node = new Node();
 FilePosition outputStartPosition = new FilePosition(1, 1);
 FilePosition outputEndPosition = null;
 sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
 assertTrue(sourceMap.getPrefixMappings().isEmpty());
 }

 @Test
 public void testAddMappingWithValidNodeAndValidOutputPositions() {
 SourceMap sourceMap = SourceMap.Format.DEFAULT.getInstance();
 Node node = new Node();
 FilePosition outputStartPosition = new FilePosition(1, 1);
 FilePosition outputEndPosition = new FilePosition(1, 1);
 sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
 // Check the size of prefixMappings
 assertEquals(0, sourceMap.getPrefixMappings().size());
 }

 // Add more tests for other methods

}