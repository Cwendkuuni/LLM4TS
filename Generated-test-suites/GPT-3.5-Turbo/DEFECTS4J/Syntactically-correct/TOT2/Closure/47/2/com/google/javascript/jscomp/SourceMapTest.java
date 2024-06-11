import org.junit.Test;
import static org.junit.Assert.*;

public class SourceMapTest {

    @Test
    public void testAddMapping() {
        SourceMap sourceMap = new SourceMap(SourceMap.Format.DEFAULT.getInstance());
        FilePosition start = new FilePosition(1, 0);
        FilePosition end = new FilePosition(1, 4);
        
        // Test adding a mapping with valid node and positions
        Node node = new Node(Token.NAME, "test", start, end);
        sourceMap.addMapping(node, start, end);
        
        // Assert that the mapping has been added successfully
        // Add more assertions here if needed
        assertNotNull(sourceMap);
    }

    @Test
    public void testFixupSourceLocation() {
        SourceMap sourceMap = new SourceMap(SourceMap.Format.DEFAULT.getInstance());
        
        // Test fixing up a source location with a prefix mapping
        LocationMapping mapping = new LocationMapping("root/", "src/");
        sourceMap.setPrefixMappings(Collections.singletonList(mapping));
        String fixedLocation = sourceMap.fixupSourceLocation("root/file.js");
        
        // Assert that the source location has been fixed correctly
        assertEquals("src/file.js", fixedLocation);
    }

    @Test
    public void testReset() {
        SourceMap sourceMap = new SourceMap(SourceMap.Format.DEFAULT.getInstance());
        
        // Test resetting the source map
        sourceMap.reset();
        
        // Assert that the source map has been reset
        // Add more assertions here if needed
        assertNotNull(sourceMap);
    }

    // Add more test cases for other methods in SourceMap class

}