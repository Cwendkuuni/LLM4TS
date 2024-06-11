import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class SourceMapTest {
    @Test
    public void testAddMapping() {
        SourceMap sourceMap = SourceMap.Format.V1.getInstance();
        FilePosition outputStartPosition = new FilePosition(0, 0);
        FilePosition outputEndPosition = new FilePosition(1, 1);

        Node node = new Node();
        node.setSourceFileName("input.js");
        node.setLineno(0);
        node.setCharno(0);

        sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
    }

    @Test
    public void testFixupSourceLocation_noPrefixMappings() {
        SourceMap sourceMap = SourceMap.Format.DEFAULT.getInstance();

        String sourceFile = "input.js";
        String fixed = sourceMap.fixupSourceLocation(sourceFile);

        assertEquals(sourceFile, fixed);
    }

    @Test
    public void testFixupSourceLocation_withMatchingPrefixMapping() {
        SourceMap sourceMap = SourceMap.Format.V3.getInstance();

        List<LocationMapping> prefixMappings = new ArrayList<>();
        LocationMapping mapping = new LocationMapping("src/main/java", "path/to/sources");
        prefixMappings.add(mapping);
        sourceMap.setPrefixMappings(prefixMappings);

        String sourceFile = "src/main/java/com/example/Test.java";
        String fixed = sourceMap.fixupSourceLocation(sourceFile);

        assertEquals("path/to/sources/com/example/Test.java", fixed);
    }

    @Test
    public void testFixupSourceLocation_withNoMatchingPrefixMapping() {
        SourceMap sourceMap = SourceMap.Format.V2.getInstance();

        List<LocationMapping> prefixMappings = new ArrayList<>();
        LocationMapping mapping = new LocationMapping("src/test/java", "path/to/tests");
        prefixMappings.add(mapping);
        sourceMap.setPrefixMappings(prefixMappings);

        String sourceFile = "src/main/java/com/example/Test.java";
        String fixed = sourceMap.fixupSourceLocation(sourceFile);

        assertEquals(sourceFile, fixed);
    }

    @Test
    public void testReset() {
        SourceMap sourceMap = SourceMap.Format.DEFAULT.getInstance();

        sourceMap.reset();
    }

    @Test
    public void testSetStartingPosition() {
        SourceMap sourceMap = SourceMap.Format.V3.getInstance();

        int offsetLine = 0;
        int offsetIndex = 0;

        sourceMap.setStartingPosition(offsetLine, offsetIndex);
    }

    @Test
    public void testSetWrapperPrefix() {
        SourceMap sourceMap = SourceMap.Format.V2.getInstance();

        String prefix = "/* Wrapper Start */";

        sourceMap.setWrapperPrefix(prefix);
    }

    @Test
    public void testValidate() {
        SourceMap sourceMap = SourceMap.Format.DEFAULT.getInstance();

        boolean validate = true;

        sourceMap.validate(validate);
    }
}