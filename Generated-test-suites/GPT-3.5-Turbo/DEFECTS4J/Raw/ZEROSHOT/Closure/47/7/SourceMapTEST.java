import org.junit.Test;

import static org.junit.Assert.*;

public class SourceMapTest {

    @Test
    public void testAddMapping_noSourceFile() {
        SourceMap sourceMap = SourceMap.Format.V1.getInstance();
        Node node = new Node();
        node.setSourceFileName(null);
        node.setLineno(0);

        sourceMap.addMapping(node, new FilePosition(), new FilePosition());

        // No mapping should be added
    }

    @Test
    public void testAddMapping_invalidLine() {
        SourceMap sourceMap = SourceMap.Format.V1.getInstance();
        Node node = new Node();
        node.setSourceFileName("test.js");
        node.setLineno(-1);

        sourceMap.addMapping(node, new FilePosition(), new FilePosition());

        // No mapping should be added
    }

    @Test
    public void testAddMapping_validMapping() {
        SourceMap sourceMap = SourceMap.Format.V1.getInstance();
        Node node = new Node();
        node.setSourceFileName("test.js");
        node.setLineno(10);
        node.setCharno(5);

        FilePosition outputStartPosition = new FilePosition(1, 1);
        FilePosition outputEndPosition = new FilePosition(2, 1);

        sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

        // Assert that the mapping is added correctly to the source map generator
    }

    @Test
    public void testFixupSourceLocation_noPrefixMappings() {
        SourceMap sourceMap = SourceMap.Format.V1.getInstance();
        String sourceFile = "test.js";

        String fixed = sourceMap.fixupSourceLocation(sourceFile);

        assertEquals(fixed, sourceFile);
    }

    @Test
    public void testFixupSourceLocation_matchingPrefixMapping() {
        SourceMap sourceMap = SourceMap.Format.V1.getInstance();
        String prefix = "src/";
        String replacement = "lib/";
        sourceMap.setPrefixMappings(Collections.singletonList(new SourceMap.LocationMapping(prefix, replacement)));

        String sourceFile = "src/test.js";

        String fixed = sourceMap.fixupSourceLocation(sourceFile);

        assertEquals(fixed, "lib/test.js");
    }

    @Test
    public void testFixupSourceLocation_noMatchingPrefixMapping() {
        SourceMap sourceMap = SourceMap.Format.V1.getInstance();
        String prefix = "src/";
        String replacement = "lib/";
        sourceMap.setPrefixMappings(Collections.singletonList(new SourceMap.LocationMapping(prefix, replacement)));

        String sourceFile = "test.js";

        String fixed = sourceMap.fixupSourceLocation(sourceFile);

        assertEquals(fixed, sourceFile);
    }

    @Test
    public void testReset() {
        SourceMap sourceMap = SourceMap.Format.V1.getInstance();
        // Add some mappings
        sourceMap.addMapping(new Node(), new FilePosition(), new FilePosition());
        sourceMap.addMapping(new Node(), new FilePosition(), new FilePosition());

        sourceMap.reset();

        // Assert that the source map generator is reset
    }

    @Test
    public void testSetWrapperPrefix() {
        SourceMap sourceMap = SourceMap.Format.V1.getInstance();
        String prefix = "//# sourceMappingURL=";
        sourceMap.setWrapperPrefix(prefix);

        // Assert that the wrapper prefix is set correctly in the source map generator
    }

    @Test
    public void testValidate_true() {
        SourceMap sourceMap = SourceMap.Format.V1.getInstance();
        sourceMap.validate(true);

        // Assert that the validate flag is set to true in the source map generator
    }

    @Test
    public void testValidate_false() {
        SourceMap sourceMap = SourceMap.Format.V1.getInstance();
        sourceMap.validate(false);

        // Assert that the validate flag is set to false in the source map generator
    }
}