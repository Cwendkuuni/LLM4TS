import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Predicate;
import com.google.common.collect.Maps;
import com.google.debugging.sourcemap.FilePosition;
import com.google.debugging.sourcemap.SourceMapFormat;
import com.google.debugging.sourcemap.SourceMapGenerator;
import com.google.debugging.sourcemap.SourceMapGeneratorFactory;
import com.google.debugging.sourcemap.SourceMapGeneratorV1;
import com.google.debugging.sourcemap.SourceMapGeneratorV2;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;

public class SourceMapTest {

    SourceMap sourceMap;
    SourceMapGenerator generatorMock;

    @Before
    public void setUp() {
        generatorMock = mock(SourceMapGenerator.class);
        sourceMap = new SourceMap(generatorMock);
    }

    @Test
    public void testAddMapping() {
        Node nodeMock = mock(Node.class);
        FilePosition[] filePositions = {
            new FilePosition(0, 0),
            new FilePosition(1, 1),
            new FilePosition(2, 2)
        };

        when(nodeMock.getSourceFileName()).thenReturn("test.js");
        when(nodeMock.getLineno()).thenReturn(1, 2, 3);
        when(nodeMock.getCharno()).thenReturn(1, 2, 3);
        when(nodeMock.getProp(Node.ORIGINALNAME_PROP)).thenReturn("original");

        sourceMap.addMapping(nodeMock, filePositions[0], filePositions[1]);
        sourceMap.addMapping(nodeMock, filePositions[1], filePositions[2]);

        verify(generatorMock).addMapping("test.js", "original", filePositions[0], filePositions[1], filePositions[1]);
        verify(generatorMock).addMapping("test.js", "original", filePositions[1], filePositions[2], filePositions[2]);
    }

    @Test
    public void testAddMapping_invalidNode() {
        Node nodeMock = mock(Node.class);

        when(nodeMock.getSourceFileName()).thenReturn(null);
        when(nodeMock.getLineno()).thenReturn(-1);

        sourceMap.addMapping(nodeMock, any(FilePosition.class), any(FilePosition.class));

        verify(generatorMock, never()).addMapping(anyString(), anyString(), any(FilePosition.class), any(FilePosition.class), any(FilePosition.class));
    }

    @Test
    public void testAppendTo() throws IOException {
        Appendable appendableMock = mock(Appendable.class);

        sourceMap.appendTo(appendableMock, "name");

        verify(generatorMock).appendTo(appendableMock, "name");
    }

    @Test
    public void testReset() {
        sourceMap.reset();

        verify(generatorMock).reset();
    }

    @Test
    public void testSetStartingPosition() {
        sourceMap.setStartingPosition(0, 0);

        verify(generatorMock).setStartingPosition(0, 0);
    }

    @Test
    public void testSetWrapperPrefix() {
        sourceMap.setWrapperPrefix("prefix");

        verify(generatorMock).setWrapperPrefix("prefix");
    }

    @Test
    public void testValidate() {
        sourceMap.validate(true);

        verify(generatorMock).validate(true);
    }

    @Test
    public void testSetPrefixMappings() {
        sourceMap.setPrefixMappings(anyList());

        verify(generatorMock, never()).setPrefixMappings(anyList());
    }

}