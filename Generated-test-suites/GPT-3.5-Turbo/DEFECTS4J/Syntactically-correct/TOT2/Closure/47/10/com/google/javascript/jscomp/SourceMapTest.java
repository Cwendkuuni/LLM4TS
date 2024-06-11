import static org.junit.Assert.*;
import org.junit.Test;

public class SourceMapTest {

    @Test
    public void testAddMapping() {
        // Test adding a mapping with valid parameters
        SourceMap sourceMap = new SourceMap(SourceMap.Format.DEFAULT.getInstance());
        FilePosition outputStartPosition = new FilePosition(1, 0);
        FilePosition outputEndPosition = new FilePosition(1, 5);
        Node node = new Node();
        
        sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
        // Assert that the mapping was added successfully
        // Additional assertions can be added to further validate the behavior
    }

    @Test
    public void testFixupSourceLocation() {
        // Test fixupSourceLocation method with prefix mappings
        SourceMap sourceMap = new SourceMap(SourceMap.Format.DEFAULT.getInstance());
        List<LocationMapping> prefixMappings = new ArrayList<>();
        prefixMappings.add(new LocationMapping("source/", "replaced/"));
        sourceMap.setPrefixMappings(prefixMappings);
        
        String fixedSourceFile = sourceMap.fixupSourceLocation("source/file.js");
        
        assertEquals("replaced/file.js", fixedSourceFile);
    }

    @Test
    public void testSetStartingPosition() {
        // Test setting starting position with valid parameters
        SourceMap sourceMap = new SourceMap(SourceMap.Format.DEFAULT.getInstance());
        
        sourceMap.setStartingPosition(1, 0);
        // Assert that starting position was set successfully
        // Additional assertions can be added to further validate the behavior
    }

    // Additional test cases for other public methods in SourceMap class can be added here

}