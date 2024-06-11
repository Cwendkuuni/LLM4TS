import org.junit.Test;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class SourceMapTest {

    @Test
    public void testAddMapping_withValidNode_shouldAddMapping() {
        // Arrange
        SourceMap sourceMap = new SourceMap(SourceMap.Format.DEFAULT.getInstance());
        Node node = new Node();
        node.putProp(Node.ORIGINALNAME_PROP, "test");
        FilePosition outputStartPosition = new FilePosition(0, 0);
        FilePosition outputEndPosition = new FilePosition(1, 1);

        // Act
        sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

        // Assert
        // Assert that the mapping was successfully added to the source map
        assertEquals(1, sourceMap.getGenerator().getMappings().size());
    }

    @Test
    public void testAddMapping_withInvalidNode_shouldNotAddMapping() {
        // Arrange
        SourceMap sourceMap = new SourceMap(SourceMap.Format.DEFAULT.getInstance());
        Node node = new Node();
        node.setSourceFileName(null);
        node.setLineno(-1);
        FilePosition outputStartPosition = new FilePosition(0, 0);
        FilePosition outputEndPosition = new FilePosition(1, 1);

        // Act
        sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

        // Assert
        // Assert that no mapping was added to the source map
        assertEquals(0, sourceMap.getGenerator().getMappings().size());
    }

    @Test
    public void testFixupSourceLocation_withNoMappings_shouldReturnSameSourceFile() {
        // Arrange
        SourceMap sourceMap = new SourceMap(SourceMap.Format.DEFAULT.getInstance());
        String sourceFile = "test.js";

        // Act
        String fixed = sourceMap.fixupSourceLocation(sourceFile);

        // Assert
        // Assert that the fixed source file is the same as the input source file
        assertEquals(sourceFile, fixed);
    }

    @Test
    public void testFixupSourceLocation_withMatchingPrefixMapping_shouldReturnFixedSourceFile() {
        // Arrange
        SourceMap sourceMap = new SourceMap(SourceMap.Format.DEFAULT.getInstance());
        String prefix = "src/";
        String replacement = "fixed/src/";
        List<SourceMap.LocationMapping> mappings = new ArrayList<>();
        mappings.add(new SourceMap.LocationMapping(prefix, replacement));
        sourceMap.setPrefixMappings(mappings);
        String sourceFile = "src/test.js";

        // Act
        String fixed = sourceMap.fixupSourceLocation(sourceFile);

        // Assert
        // Assert that the fixed source file has the correct prefix mapping applied
        assertEquals(replacement + "test.js", fixed);
    }

    @Test
    public void testFixupSourceLocation_withNoMatchingPrefixMapping_shouldReturnSameSourceFile() {
        // Arrange
        SourceMap sourceMap = new SourceMap(SourceMap.Format.DEFAULT.getInstance());
        String prefix = "src/";
        String replacement = "fixed/src/";
        List<SourceMap.LocationMapping> mappings = new ArrayList<>();
        mappings.add(new SourceMap.LocationMapping(prefix, replacement));
        sourceMap.setPrefixMappings(mappings);
        String sourceFile = "test.js";

        // Act
        String fixed = sourceMap.fixupSourceLocation(sourceFile);

        // Assert
        // Assert that the fixed source file is the same as the input source file
        assertEquals(sourceFile, fixed);
    }

    @Test
    public void testSetStartingPosition_withValidPosition_shouldSetStartingPosition() {
        // Arrange
        SourceMap sourceMap = new SourceMap(SourceMap.Format.DEFAULT.getInstance());
        int offsetLine = 0;
        int offsetIndex = 0;

        // Act
        sourceMap.setStartingPosition(offsetLine, offsetIndex);

        // Assert
        // Assert that the starting position was successfully set
        assertEquals(offsetLine, sourceMap.getGenerator().getStartingLine());
        assertEquals(offsetIndex, sourceMap.getGenerator().getStartingIndex());
    }

    @Test
    public void testSetWrapperPrefix_withValidPrefix_shouldSetWrapperPrefix() {
        // Arrange
        SourceMap sourceMap = new SourceMap(SourceMap.Format.DEFAULT.getInstance());
        String prefix = "//# sourceMappingURL=";

        // Act
        sourceMap.setWrapperPrefix(prefix);

        // Assert
        // Assert that the wrapper prefix was successfully set
        assertEquals(prefix, sourceMap.getGenerator().getWrapperPrefix());
    }

    @Test
    public void testValidate_withTrue_shouldSetValidationToTrue() {
        // Arrange
        SourceMap sourceMap = new SourceMap(SourceMap.Format.DEFAULT.getInstance());
        boolean validate = true;

        // Act
        sourceMap.validate(validate);

        // Assert
        // Assert that validation was successfully set to true
        assertEquals(validate, sourceMap.getGenerator().shouldValidate());
    }

    @Test
    public void testValidate_withFalse_shouldSetValidationToFalse() {
        // Arrange
        SourceMap sourceMap = new SourceMap(SourceMap.Format.DEFAULT.getInstance());
        boolean validate = false;

        // Act
        sourceMap.validate(validate);

        // Assert
        // Assert that validation was successfully set to false
        assertEquals(validate, sourceMap.getGenerator().shouldValidate());
    }

    @Test
    public void testReset_shouldResetSourceMap() {
        // Arrange
        SourceMap sourceMap = new SourceMap(SourceMap.Format.DEFAULT.getInstance());
        Node node = new Node();
        node.putProp(Node.ORIGINALNAME_PROP, "test");
        FilePosition outputStartPosition = new FilePosition(0, 0);
        FilePosition outputEndPosition = new FilePosition(1, 1);
        sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

        // Act
        sourceMap.reset();

        // Assert
        // Assert that the source map has been reset and contains no mappings
        assertEquals(0, sourceMap.getGenerator().getMappings().size());
    }

    @Test
    public void testAppendTo_withValidAppendable_shouldAppendToAppendable() throws IOException {
        // Arrange
        SourceMap sourceMap = new SourceMap(SourceMap.Format.DEFAULT.getInstance());
        String name = "test";
        StringBuilder builder = new StringBuilder();

        // Act
        sourceMap.appendTo(builder, name);

        // Assert
        // Assert that the source map was successfully appended to the appendable
        assertEquals(1, builder.length());
        assertEquals('#', builder.charAt(0));
    }
}