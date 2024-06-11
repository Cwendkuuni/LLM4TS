import org.junit.Test;
import static org.junit.Assert.*;

public class SourceMapTest {

    @Test
    public void testAddMapping_validNode_validOutputPosition() {
        SourceMap sourceMap = new SourceMap(SourceMap.Format.DEFAULT.getInstance());
        Node node = new Node(1, "test", null);
        FilePosition outputStart = new FilePosition(1, 0);
        FilePosition outputEnd = new FilePosition(1, 5);

        sourceMap.addMapping(node, outputStart, outputEnd);

        // Assert that the mapping was successfully added
        // You may add more specific assertions here based on the implementation
    }

    @Test
    public void testAddMapping_invalidNode_invalidOutputPosition() {
        SourceMap sourceMap = new SourceMap(SourceMap.Format.DEFAULT.getInstance());
        // Invalid node with no source file name
        Node node = new Node(1, null, null);
        FilePosition outputStart = new FilePosition(-1, -1);
        FilePosition outputEnd = new FilePosition(-1, -1);

        sourceMap.addMapping(node, outputStart, outputEnd);

        // Assert that the mapping was not added due to invalid input
    }

    @Test
    public void testSetPrefixMappings_validPrefixMappings() {
        SourceMap sourceMap = new SourceMap(SourceMap.Format.DEFAULT.getInstance());
        List<LocationMapping> mappings = Arrays.asList(
            new LocationMapping("path/to/source", "new/path/to/source")
        );

        sourceMap.setPrefixMappings(mappings);

        // Assert that the prefix mappings were set successfully
    }

    @Test
    public void testSetPrefixMappings_noPrefixMappings() {
        SourceMap sourceMap = new SourceMap(SourceMap.Format.DEFAULT.getInstance());

        sourceMap.setPrefixMappings(Collections.emptyList());

        // Assert that the prefix mappings were set successfully even when empty
    }

    // Add more test cases here for other methods in the SourceMap class

}