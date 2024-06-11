import org.junit.Test;
import static org.junit.Assert.*;

public class SourceMapTest {

    @Test
    public void testGetInstance() {
        // Test if getInstance() returns a valid instance of SourceMap for each Format
        SourceMap sourceMapV1 = SourceMap.Format.V1.getInstance();
        assertNotNull(sourceMapV1);

        SourceMap sourceMapDefault = SourceMap.Format.DEFAULT.getInstance();
        assertNotNull(sourceMapDefault);

        SourceMap sourceMapV2 = SourceMap.Format.V2.getInstance();
        assertNotNull(sourceMapV2);

        SourceMap sourceMapV3 = SourceMap.Format.V3.getInstance();
        assertNotNull(sourceMapV3);
    }

    @Test
    public void testAddMapping() {
        // Test if addMapping() adds the mapping correctly
        SourceMap sourceMap = SourceMap.Format.DEFAULT.getInstance();
        Node node = new Node();
        node.setSourceFileName("file.js");
        node.setLineno(1);
        node.setCharno(0);
        FilePosition outputStartPosition = new FilePosition(0, 0);
        FilePosition outputEndPosition = new FilePosition(1, 1);
        sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

        // Assert if the mapping was added correctly
        // (assuming appendTo() and reset() methods are working correctly)
        StringBuilder sb = new StringBuilder();
        sourceMap.appendTo(sb, "file.map");
        assertTrue(sb.toString().contains("file.js"));
    }

    @Test
    public void testFixupSourceLocation() {
        // Test if fixupSourceLocation() remaps the source file correctly
        SourceMap sourceMap = SourceMap.Format.DEFAULT.getInstance();
        // Add a location mapping
        SourceMap.LocationMapping locationMapping = new SourceMap.LocationMapping("src/", "remapped/");
        sourceMap.setPrefixMappings(Collections.singletonList(locationMapping));

        // Test if a source file with a matching prefix is remapped correctly
        String sourceFile1 = "src/file.js";
        String remappedFile1 = sourceMap.fixupSourceLocation(sourceFile1);
        assertEquals("remapped/file.js", remappedFile1);

        // Test if a source file without a matching prefix remains unchanged
        String sourceFile2 = "other/file.js";
        String remappedFile2 = sourceMap.fixupSourceLocation(sourceFile2);
        assertEquals(sourceFile2, remappedFile2);
    }

    @Test
    public void testSetWrapperPrefix() {
        // Test if setWrapperPrefix() sets the wrapper prefix correctly
        SourceMap sourceMap = SourceMap.Format.DEFAULT.getInstance();
        String prefix = "Wrapper Start ";
        sourceMap.setWrapperPrefix(prefix);
        assertEquals(prefix, sourceMap.generator.getWrapperPrefix());
    }

    @Test
    public void testValidate() {
        // Test if validate() sets the validate flag correctly
        SourceMap sourceMap = SourceMap.Format.DEFAULT.getInstance();
        boolean validate = true;
        sourceMap.validate(validate);
        assertEquals(validate, sourceMap.generator.getValidate());
    }

    @Test
    public void testSetPrefixMappings() {
        // Test if setPrefixMappings() sets the prefix mappings correctly
        SourceMap sourceMap = SourceMap.Format.DEFAULT.getInstance();
        SourceMap.LocationMapping locationMapping = new SourceMap.LocationMapping("src/", "remapped/");
        sourceMap.setPrefixMappings(Collections.singletonList(locationMapping));

        assertEquals(1, sourceMap.prefixMappings.size());
        assertEquals(locationMapping, sourceMap.prefixMappings.get(0));
    }

    @Test
    public void testReset() {
        // Test if reset() resets the source map generator correctly
        SourceMap sourceMap = SourceMap.Format.DEFAULT.getInstance();
        sourceMap.reset();

        // Assert if the source map generator is reset
        assertNull(sourceMap.generator.getWrapperPrefix());
    }
}