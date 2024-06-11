import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SourceMapTest {

    private SourceMap sourceMap;

    @Before
    public void setUp() {
        sourceMap = new SourceMap(SourceMap.Format.DEFAULT.getInstance());
    }

    @Test
    public void testAddMapping_typical() {
        Node node = new Node.Builder()
                        .setSourceFileName("test.js")
                        .setLineno(5)
                        .setCharno(10)
                        .setProp(Node.ORIGINALNAME_PROP, "originalName")
                        .build();
        FilePosition outputStartPosition = new FilePosition(1, 2);
        FilePosition outputEndPosition = new FilePosition(3, 4);

        sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

        // Assert mappings are added correctly
        // ...

        // Assert sourceFile is fixed up correctly
        // ...
    }

    @Test
    public void testAddMapping_noSourceFile() {
        Node node = new Node.Builder()
                        .setLineno(5)
                        .setCharno(10)
                        .setProp(Node.ORIGINALNAME_PROP, "originalName")
                        .build();
        FilePosition outputStartPosition = new FilePosition(1, 2);
        FilePosition outputEndPosition = new FilePosition(3, 4);

        sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

        // Assert no mappings are added
        // ...
    }

    @Test
    public void testAddMapping_negativeLineNo() {
        Node node = new Node.Builder()
                        .setSourceFileName("test.js")
                        .setLineno(-1)
                        .setCharno(10)
                        .setProp(Node.ORIGINALNAME_PROP, "originalName")
                        .build();
        FilePosition outputStartPosition = new FilePosition(1, 2);
        FilePosition outputEndPosition = new FilePosition(3, 4);

        sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

        // Assert no mappings are added
        // ...
    }

    @Test
    public void testFixupSourceLocation_typical() {
        sourceMap.setPrefixMappings(Arrays.asList(
            new SourceMap.LocationMapping("prefix/", "replacement/"),
            new SourceMap.LocationMapping("prefix2/", "replacement2/")
        ));
        String sourceFile = "prefix/test.js";

        String fixed = sourceMap.fixupSourceLocation(sourceFile);

        // Assert sourceFile is correctly fixed up
        // ...
    }

    @Test
    public void testFixupSourceLocation_noPrefixMatch() {
        sourceMap.setPrefixMappings(Arrays.asList(
            new SourceMap.LocationMapping("prefix/", "replacement/")
        ));
        String sourceFile = "not_matching_prefix/test.js";

        String fixed = sourceMap.fixupSourceLocation(sourceFile);

        // Assert sourceFile remains unchanged
        // ...
    }

    @Test
    public void testFixupSourceLocation_emptyPrefixMappings() {
        sourceMap.setPrefixMappings(Collections.emptyList());
        String sourceFile = "test.js";

        String fixed = sourceMap.fixupSourceLocation(sourceFile);

        // Assert sourceFile remains unchanged
        // ...
    }

    @Test
    public void testAppendTo_typical() {
        StringBuilder out = new StringBuilder();
        String name = "sourceMap.js";

        sourceMap.appendTo(out, name);

        // Assert out contains the correct source map content
        // ...
    }

    // Additional tests for other methods in SourceMap class...

}