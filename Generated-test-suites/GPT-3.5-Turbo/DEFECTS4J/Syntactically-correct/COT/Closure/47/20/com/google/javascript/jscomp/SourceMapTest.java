import org.junit.Test;
import static org.junit.Assert.*;

public class SourceMapTest {

    @Test
    public void testAddMapping() {
        Node node = new Node();
        node.setSourceFileName("test.js");
        node.setLineno(1);
        node.setCharno(0);
        FilePosition outputStartPosition = new FilePosition(0, 0);
        FilePosition outputEndPosition = new FilePosition(1, 0);

        SourceMap sourceMap = new SourceMap(SourceMap.Format.V1.getInstance());
        sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

        // Verify that the mapping has been added correctly
        // ...

    }

    @Test
    public void testFixupSourceLocation() {
        SourceMap sourceMap = new SourceMap(SourceMap.Format.V1.getInstance());
        sourceMap.setPrefixMappings(Collections.singletonList(
                new LocationMapping("src/", "test/")));

        String fixedSourceFile = sourceMap.fixupSourceLocation("src/test.js");

        // Verify that the source file has been fixed up correctly
        // ...

    }

    // Add test cases for other methods in the SourceMap class
    // ...

}