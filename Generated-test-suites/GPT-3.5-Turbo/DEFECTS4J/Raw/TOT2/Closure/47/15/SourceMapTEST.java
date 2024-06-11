import org.junit.Test;
import static org.junit.Assert.*;

public class SourceMapTest {

    @Test
    public void testAddMapping() {
        SourceMap sourceMap = new SourceMap(SourceMap.Format.V1.getInstance());

        // Test adding a mapping with valid input
        sourceMap.addMapping(new Node("testNode", "testFile.js", 1, 0), new FilePosition(0, 0), new FilePosition(1, 1));

        // Add assertions here to verify the functionality of the method
    }

    @Test
    public void testFixupSourceLocation() {
        SourceMap sourceMap = new SourceMap(SourceMap.Format.DEFAULT.getInstance());

        // Test fixupSourceLocation with different prefix mappings
        sourceMap.setPrefixMappings(Collections.singletonList(new SourceMap.LocationMapping("test", "fixed")));
        String fixedFile = sourceMap.fixupSourceLocation("testFile.js");

        assertEquals("fixedFile.js", fixedFile);

        // Add assertions here to verify the functionality of the method
    }

    // Add more test cases for other methods in the SourceMap class

}