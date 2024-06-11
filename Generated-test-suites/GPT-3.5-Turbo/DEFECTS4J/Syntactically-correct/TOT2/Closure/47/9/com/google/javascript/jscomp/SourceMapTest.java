import org.junit.Test;
import static org.junit.Assert.*;

public class SourceMapTest {

    @Test
    public void testAddMapping_NodeOutputStartAndEndPositions_CorrectlyAdded() {
        // Initialize SourceMap instance
        SourceMap sourceMap = new SourceMap(SourceMap.Format.V1.getInstance());

        // Create a sample node and output positions
        Node node = new Node(Token.NAME, "sampleNode");
        FilePosition outputStart = new FilePosition(1, 0);
        FilePosition outputEnd = new FilePosition(1, 5);

        // Add mapping
        sourceMap.addMapping(node, outputStart, outputEnd);

        // Verify that mapping was added correctly
        // Add your assertions here
    }

    @Test
    public void testFixupSourceLocation_PrefixMapping_SourceFileFixed() {
        // Initialize SourceMap instance
        SourceMap sourceMap = new SourceMap(SourceMap.Format.DEFAULT.getInstance());

        // Set up prefix mappings
        List<LocationMapping> prefixMappings = new ArrayList<>();
        prefixMappings.add(new LocationMapping("prefix1/", "replacement1/"));
        prefixMappings.add(new LocationMapping("prefix2/", "replacement2/"));
        sourceMap.setPrefixMappings(prefixMappings);

        // Test fixupSourceLocation method with prefixed source file
        String fixedSourceFile = sourceMap.fixupSourceLocation("prefix1/sampleFile.js");

        // Verify that source file is fixed correctly
        assertEquals("replacement1/sampleFile.js", fixedSourceFile);
    }

    // Add more test cases for other methods in SourceMap class

}