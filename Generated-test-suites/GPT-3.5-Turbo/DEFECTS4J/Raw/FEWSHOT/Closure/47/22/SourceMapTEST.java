
package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.debugging.sourcemap.FilePosition;
import com.google.debugging.sourcemap.SourceMapFormat;
import com.google.debugging.sourcemap.SourceMapGenerator;
import com.google.debugging.sourcemap.SourceMapGeneratorFactory;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class SourceMapTest {

    private SourceMapGenerator generator;
    private SourceMap sourceMap;

    @Before
    public void setUp() {
        generator = mock(SourceMapGenerator.class);
        SourceMapGeneratorFactory factory = mock(SourceMapGeneratorFactory.class);
        Mockito.when(factory.getInstance(SourceMapFormat.DEFAULT)).thenReturn(generator);
        SourceMapGeneratorFactory.setInstance(factory);
        sourceMap = SourceMap.Format.DEFAULT.getInstance();
    }

    @Test
    public void testAddMapping() {
        Node node = new Node();
        node.putProp(Node.ORIGINALNAME_PROP, "originalName");
        node.setLineno(5);
        node.setCharno(10);
        FilePosition outputStartPosition = new FilePosition(1, 2);
        FilePosition outputEndPosition = new FilePosition(3, 4);

        sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

        verify(generator).addMapping(any(String.class), any(String.class), any(FilePosition.class),
                any(FilePosition.class), any(FilePosition.class));
    }

    @Test
    public void testAddMapping_noSourceFile() {
        Node node = new Node();
        node.putProp(Node.ORIGINALNAME_PROP, "originalName");
        node.setLineno(-1);
        node.setCharno(10);
        FilePosition outputStartPosition = new FilePosition(1, 2);
        FilePosition outputEndPosition = new FilePosition(3, 4);

        sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

        verify(generator, Mockito.never()).addMapping(any(String.class), any(String.class), any(FilePosition.class),
                any(FilePosition.class), any(FilePosition.class));
    }

    @Test
    public void testAddMapping_noOriginalName() {
        Node node = new Node();
        node.setSourceFileName("sourceFile");
        node.setLineno(5);
        node.setCharno(10);
        FilePosition outputStartPosition = new FilePosition(1, 2);
        FilePosition outputEndPosition = new FilePosition(3, 4);

        sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

        verify(generator).addMapping(any(String.class), any(String.class), any(FilePosition.class),
                any(FilePosition.class), any(FilePosition.class));
    }

    @Test
    public void testAppendTo() throws IOException {
        Appendable out = mock(Appendable.class);
        String name = "name";

        sourceMap.appendTo(out, name);

        verify(generator).appendTo(out, name);
    }

    @Test
    public void testReset() {
        sourceMap.reset();

        verify(generator).reset();
    }

    @Test
    public void testSetStartingPosition() {
        int offsetLine = 5;
        int offsetIndex = 10;

        sourceMap.setStartingPosition(offsetLine, offsetIndex);

        verify(generator).setStartingPosition(offsetLine, offsetIndex);
    }

    @Test
    public void testSetWrapperPrefix() {
        String prefix = "prefix";

        sourceMap.setWrapperPrefix(prefix);

        verify(generator).setWrapperPrefix(prefix);
    }

    @Test
    public void testValidate() {
        boolean validate = true;

        sourceMap.validate(validate);

        verify(generator).validate(validate);
    }

    @Test
    public void testSetPrefixMappings() {
        List<SourceMap.LocationMapping> sourceMapLocationMappings = Collections.emptyList();

        sourceMap.setPrefixMappings(sourceMapLocationMappings);

        verify(generator, Mockito.never()).setPrefixMappings(sourceMapLocationMappings);
    }

}
