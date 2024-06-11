import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SourceMapTest {

    private SourceMap sourceMap;

    @Before
    public void setUp() {
        sourceMap = new SourceMap(SourceMap.Format.V1.getInstance());
    }

    @After
    public void tearDown() {
        sourceMap = null;
    }

    @Test
    public void testAddMapping_validNode_addsMapping() {
        // Arrange
        Node node = mock(Node.class);
        FilePosition outputStartPosition = mock(FilePosition.class);
        FilePosition outputEndPosition = mock(FilePosition.class);

        // Act
        sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

        // Assert
        // Verify that the mapping was added to the generator
        verify(generator).addMapping(anyString(), anyString(), any(FilePosition.class), any(FilePosition.class), any(FilePosition.class));
    }

    @Test
    public void testAddMapping_invalidNode_doesNotAddMapping() {
        // Arrange
        Node node = mock(Node.class);
        when(node.getSourceFileName()).thenReturn(null);
        when(node.getLineno()).thenReturn(-1);
        FilePosition outputStartPosition = mock(FilePosition.class);
        FilePosition outputEndPosition = mock(FilePosition.class);

        // Act
        sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

        // Assert
        // Verify that the mapping was not added to the generator
        verify(generator, times(0)).addMapping(anyString(), anyString(), any(FilePosition.class), any(FilePosition.class), any(FilePosition.class));
    }

    @Test
    public void testFixupSourceLocation_mappingExists_replacesPrefix() {
        // Arrange
        List<LocationMapping> prefixMappings = Arrays.asList(new LocationMapping("oldPrefix", "newPrefix"));
        sourceMap.setPrefixMappings(prefixMappings);
        String sourceFile = "oldPrefix/file.js";

        // Act
        String fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);

        // Assert
        assertEquals("newPrefix/file.js", fixedSourceFile);
    }

    @Test
    public void testFixupSourceLocation_mappingDoesNotExist_returnsOriginalSourceFile() {
        // Arrange
        List<LocationMapping> prefixMappings = Arrays.asList(new LocationMapping("oldPrefix", "newPrefix"));
        sourceMap.setPrefixMappings(prefixMappings);
        String sourceFile = "otherPrefix/file.js";

        // Act
        String fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);

        // Assert
        assertEquals(sourceFile, fixedSourceFile);
    }

    @Test
    public void testAppendTo_validArguments_appendsToOutput() throws IOException {
        // Arrange
        Appendable out = mock(Appendable.class);
        String name = "output";

        // Act
        sourceMap.appendTo(out, name);

        // Assert
        // Verify that the generator's appendTo method was called with the correct arguments
        verify(generator).appendTo(eq(out), eq(name));
    }

    @Test
    public void testReset_resetsGeneratorAndCache() {
        // Arrange
        // Add a mapping to the source map
        Node node = mock(Node.class);
        FilePosition outputStartPosition = mock(FilePosition.class);
        FilePosition outputEndPosition = mock(FilePosition.class);
        sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

        // Act
        sourceMap.reset();

        // Assert
        // Verify that the generator's reset method was called
        verify(generator).reset();
        // Verify that the sourceLocationFixupCache was cleared
        assertTrue(sourceMap.sourceLocationFixupCache.isEmpty());
    }

    @Test
    public void testSetStartingPosition_validArguments_setsPosition() {
        // Arrange
        int offsetLine = 10;
        int offsetIndex = 5;

        // Act
        sourceMap.setStartingPosition(offsetLine, offsetIndex);

        // Assert
        // Verify that the generator's setStartingPosition method was called with the correct arguments
        verify(generator).setStartingPosition(eq(offsetLine), eq(offsetIndex));
    }

    @Test
    public void testSetWrapperPrefix_validPrefix_setsPrefix() {
        // Arrange
        String prefix = "wrapper";

        // Act
        sourceMap.setWrapperPrefix(prefix);

        // Assert
        // Verify that the generator's setWrapperPrefix method was called with the correct prefix
        verify(generator).setWrapperPrefix(eq(prefix));
    }

    @Test
    public void testValidate_enableValidation_validatesGenerator() {
        // Arrange
        boolean validate = true;

        // Act
        sourceMap.validate(validate);

        // Assert
        // Verify that the generator's validate method was called with the correct validate flag
        verify(generator).validate(eq(validate));
    }
}