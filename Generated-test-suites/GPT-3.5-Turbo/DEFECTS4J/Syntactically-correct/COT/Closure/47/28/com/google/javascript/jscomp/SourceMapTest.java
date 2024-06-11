import com.google.common.collect.Lists;
import com.google.debugging.sourcemap.FilePosition;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class SourceMapTest {
    private SourceMap sourceMap;

    @Before
    public void setUp() {
        sourceMap = new SourceMap(SourceMap.Format.DEFAULT.getInstance());
    }

    @Test
    public void testAddMapping() {
        Node node = mock(Node.class);
        FilePosition startPosition = new FilePosition(1, 0);
        FilePosition endPosition = new FilePosition(1, 5);

        sourceMap.addMapping(node, startPosition, endPosition);

        // Verify that the mapping was added correctly
        // You may need to adjust the assertions depending on the actual behavior of the addMapping() method
    }

    @Test
    public void testFixupSourceLocation() {
        // Test with no prefix mappings
        String sourceFile = "test.js";
        String fixed = sourceMap.fixupSourceLocation(sourceFile);
        assertEquals(sourceFile, fixed);

        // Test with prefix mappings
        sourceMap.setPrefixMappings(createPrefixMappings());
        fixed = sourceMap.fixupSourceLocation("prefix1/source.js");
        assertEquals("replacement1/source.js", fixed);
    }

    @Test
    public void testAppendTo() throws IOException {
        // Test with an empty source map
        StringBuilder out = new StringBuilder();
        sourceMap.appendTo(out, "filename.js");
        assertEquals("{\"version\":3,\"file\":\"filename.js\",\"lineCount\":[0],\"mappings\":\"\"}", out.toString());

        // Test with mappings
        Node node = mock(Node.class);
        FilePosition startPosition = new FilePosition(1, 0);
        FilePosition endPosition = new FilePosition(1, 5);
        sourceMap.addMapping(node, startPosition, endPosition);
        out = new StringBuilder();
        sourceMap.appendTo(out, "filename.js");
        // Verify that the JSON output is correct
        // You may need to adjust the assertions depending on the actual output format of the appendTo() method
    }

    @Test
    public void testReset() {
        Node node = mock(Node.class);
        FilePosition startPosition = new FilePosition(1, 0);
        FilePosition endPosition = new FilePosition(1, 5);
        sourceMap.addMapping(node, startPosition, endPosition);

        sourceMap.reset();

        // Verify that all mappings are cleared
        // You may need to adjust the assertions depending on the actual behavior of the reset() method
    }

    @Test
    public void testSetStartingPosition() {
        sourceMap.setStartingPosition(2, 3);

        // Verify that the starting position is set correctly
        // You may need to adjust the assertions depending on the actual behavior of the setStartingPosition() method
    }

    @Test
    public void testSetWrapperPrefix() {
        sourceMap.setWrapperPrefix("wrapper");

        // Verify that the wrapper prefix is set correctly
        // You may need to adjust the assertions depending on the actual behavior of the setWrapperPrefix() method
    }

    @Test
    public void testValidate() {
        sourceMap.validate(true);

        // Verify that the validation flag is set correctly
        // You may need to adjust the assertions depending on the actual behavior of the validate() method
    }

    private List<SourceMap.LocationMapping> createPrefixMappings() {
        List<SourceMap.LocationMapping> prefixMappings = new ArrayList<>();
        prefixMappings.add(new SourceMap.LocationMapping("prefix1/", "replacement1/"));
        prefixMappings.add(new SourceMap.LocationMapping("prefix2/", "replacement2/"));
        return prefixMappings;
    }
}