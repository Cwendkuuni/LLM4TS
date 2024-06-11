import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SourceMapTest {

    private SourceMap sourceMap;

    @Before
    public void setUp() {
        sourceMap = new SourceMap(SourceMap.Format.DEFAULT.getInstance());
    }

    @Test
    public void testAddMapping() {
        Node node = new Node();
        node.setSourceFileName("test.js");
        node.setLineno(1);
        node.setCharno(0);
        FilePosition startPosition = new FilePosition(1, 0);
        FilePosition endPosition = new FilePosition(1, 2);
        sourceMap.addMapping(node, startPosition, endPosition);
    }

    @Test
    public void testAppendTo() throws IOException {
        StringBuilder out = new StringBuilder();
        sourceMap.appendTo(out, "test");
        String expectedOutput = ""; // Add expected output here
        Assert.assertEquals(expectedOutput, out.toString());
    }

    @Test
    public void testReset() {
        sourceMap.reset();
    }

    @Test
    public void testSetStartingPosition() {
        sourceMap.setStartingPosition(0, 0);
    }

    @Test
    public void testSetWrapperPrefix() {
        sourceMap.setWrapperPrefix("prefix");
    }

    @Test
    public void testValidate() {
        sourceMap.validate(true);
    }

    @Test
    public void testSetPrefixMappings() {
        List<SourceMap.LocationMapping> prefixMappings = new ArrayList<>();
        prefixMappings.add(new SourceMap.LocationMapping("prefix", "replacement"));
        sourceMap.setPrefixMappings(prefixMappings);
    }
}