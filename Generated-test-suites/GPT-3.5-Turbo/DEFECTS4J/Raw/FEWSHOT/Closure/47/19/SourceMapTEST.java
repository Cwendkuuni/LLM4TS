
package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.debugging.sourcemap.FilePosition;
import com.google.javascript.rhino.Node;

public class SourceMapTest {
    private SourceMap sourceMap;
    private SourceMapGeneratorMock generator;

    @Before
    public void setUp() {
        generator = new SourceMapGeneratorMock();
        sourceMap = new SourceMap(generator);
    }

    @Test
    public void testAddMapping_shouldAddMappingWithValidNode() {
        Node node = new Node(Token.ROOT);
        node.setInputId("inputId");
        node.setLineno(1);
        node.setCharno(3);

        FilePosition outputStartPosition = new FilePosition(0, 0);
        FilePosition outputEndPosition = new FilePosition(0, 3);

        sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

        Mapping mapping = generator.getMappings().get(0);
        assertEquals("inputId", mapping.getSourceFile());
        assertNull(mapping.getOriginalName());
        assertEquals(1, mapping.getOriginalFilePos().getLine());
        assertEquals(3, mapping.getOriginalFilePos().getColumn());
        assertEquals(0, mapping.getOutputStartPos().getLine());
        assertEquals(0, mapping.getOutputStartPos().getColumn());
        assertEquals(0, mapping.getOutputEndPos().getLine());
        assertEquals(3, mapping.getOutputEndPos().getColumn());
    }

    @Test
    public void testAddMapping_shouldNotAddMappingWithInvalidNode() {
        Node node = new Node(Token.ROOT);

        FilePosition outputStartPosition = new FilePosition(0, 0);
        FilePosition outputEndPosition = new FilePosition(0, 3);

        sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

        assertTrue(generator.getMappings().isEmpty());
    }

    @Test
    public void testAddMapping_shouldNotAddMappingWithInvalidSourceFile() {
        Node node = new Node(Token.ROOT);
        node.setInputId(null);

        FilePosition outputStartPosition = new FilePosition(0, 0);
        FilePosition outputEndPosition = new FilePosition(0, 3);

        sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

        assertTrue(generator.getMappings().isEmpty());
    }

    @Test
    public void testAddMapping_shouldNotAddMappingWithInvalidLineNo() {
        Node node = new Node(Token.ROOT);
        node.setInputId("inputId");
        node.setLineno(-1);

        FilePosition outputStartPosition = new FilePosition(0, 0);
        FilePosition outputEndPosition = new FilePosition(0, 3);

        sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

        assertTrue(generator.getMappings().isEmpty());
    }

    @Test
    public void testFixupSourceLocation_shouldReturnSourceFileWhenNoMappings() {
        List<SourceMap.LocationMapping> prefixMappings = Collections.emptyList();
        sourceMap.setPrefixMappings(prefixMappings);

        String sourceFile = "file.js";
        String fixed = sourceMap.fixupSourceLocation(sourceFile);

        assertEquals(sourceFile, fixed);
    }

    @Test
    public void testFixupSourceLocation_shouldReturnSourceFileWhenNoMatchingMappings() {
        SourceMap.LocationMapping mapping1 = new SourceMap.LocationMapping("prefix1", "replacement1");
        SourceMap.LocationMapping mapping2 = new SourceMap.LocationMapping("prefix2", "replacement2");
        List<SourceMap.LocationMapping> prefixMappings = Arrays.asList(mapping1, mapping2);
        sourceMap.setPrefixMappings(prefixMappings);

        String sourceFile = "file.js";
        String fixed = sourceMap.fixupSourceLocation(sourceFile);

        assertEquals(sourceFile, fixed);
    }

    @Test
    public void testFixupSourceLocation_shouldReturnRemappedSourceFileWithMatchingMappings() {
        SourceMap.LocationMapping mapping1 = new SourceMap.LocationMapping("prefix1", "replacement1");
        SourceMap.LocationMapping mapping2 = new SourceMap.LocationMapping("prefix2", "replacement2");
        List<SourceMap.LocationMapping> prefixMappings = Arrays.asList(mapping1, mapping2);
        sourceMap.setPrefixMappings(prefixMappings);

        String sourceFile = "prefix1/file.js";
        String fixed = sourceMap.fixupSourceLocation(sourceFile);

        assertEquals("replacement1/file.js", fixed);
    }

    @Test
    public void testFixupSourceLocation_shouldCacheFixedSourceFile() {
        SourceMap.LocationMapping mapping1 = new SourceMap.LocationMapping("prefix1", "replacement1");
        SourceMap.LocationMapping mapping2 = new SourceMap.LocationMapping("prefix2", "replacement2");
        List<SourceMap.LocationMapping> prefixMappings = Arrays.asList(mapping1, mapping2);
        sourceMap.setPrefixMappings(prefixMappings);

        String sourceFile = "prefix1/file.js";
        String fixed1 = sourceMap.fixupSourceLocation(sourceFile);
        String fixed2 = sourceMap.fixupSourceLocation(sourceFile);

        assertEquals(fixed1, fixed2);
    }

    private static class SourceMapGeneratorMock implements SourceMapGenerator {
        private List<Mapping> mappings;

        public SourceMapGeneratorMock() {
            mappings = Collections.emptyList();
        }

        public List<Mapping> getMappings() {
            return mappings;
        }

        @Override
        public void addMapping(String sourceFile, String originalName, FilePosition originalFilePos,
                FilePosition outputStartPos, FilePosition outputEndPos) {
            mappings.add(new Mapping(sourceFile, originalName, originalFilePos, outputStartPos, outputEndPos));
        }

        @Override
        public void reset() {
            mappings = Collections.emptyList();
        }

        @Override
        public void setStartingPosition(int offsetLine, int offsetIndex) {

        }

        @Override
        public void setWrapperPrefix(String prefix) {

        }

        @Override
        public void validate(boolean validate) {

        }

        @Override
        public void appendTo(Appendable out, String name) throws IOException {

        }

        private static class Mapping {
            private String sourceFile;
            private String originalName;
            private FilePosition originalFilePos;
            private FilePosition outputStartPos;
            private FilePosition outputEndPos;

            public Mapping(String sourceFile, String originalName, FilePosition originalFilePos,
                    FilePosition outputStartPos, FilePosition outputEndPos) {
                this.sourceFile = sourceFile;
                this.originalName = originalName;
                this.originalFilePos = originalFilePos;
                this.outputStartPos = outputStartPos;
                this.outputEndPos = outputEndPos;
            }

            public String getSourceFile() {
                return sourceFile;
            }

            public String getOriginalName() {
                return originalName;
            }

            public FilePosition getOriginalFilePos() {
                return originalFilePos;
            }

            public FilePosition getOutputStartPos() {
                return outputStartPos;
            }

            public FilePosition getOutputEndPos() {
                return outputEndPos;
            }
        }
    }
}

